package com.denisballa.telemetrychallenge;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {

        // Check if the log file path is provided as an argument
        if (args.length != 1) {
            System.out.println("Usage: java -jar telemetry-challenge.jar <log_file_path>");
            return;
        }

        // Create an instance of TelemetryLogParser and parse the log file
        String logFilePath = args[0];
        TelemetryLogParser telemetryLogParser = new TelemetryLogParser(logFilePath);

        // The parse method will read the log file and process the data
        telemetryLogParser.parseWithAnomalyDetecting(List.of(
                new LogDataAnomalyDetector[]{
                        new LogDataAnomalyDetector() {
                            @Override
                            public boolean isAnomaly(LogData logData, DeviceData lastRecordedData) {
                                if(!logData.getEventType().equals("temperature")){
                                    return false;
                                }
                                Object lastTemperature = lastRecordedData.getEvents().get("temperature");
                                if (lastTemperature != null) {
                                    double lastTemperatureValue = Double.parseDouble(lastTemperature.toString());
                                    double currentTemperatureValue = Double.parseDouble(logData.getEventValue());
                                    return Math.abs(currentTemperatureValue - lastTemperatureValue) > 5;
                                }
                                return false;
                            }
                            @Override
                            public String getAnomaly() {
                                return "Temperature fluctuation over 5 degrees";
                            }
                        },
                        new LogDataAnomalyDetector() {
                            @Override
                            public boolean isAnomaly(LogData logData, DeviceData lastRecordedData) {
                                if(!logData.getEventType().equals("battery")){
                                    return false;
                                }
                                Object lastBatteryLevel = lastRecordedData.getEvents().get("battery");
                                if (lastBatteryLevel != null) {
                                    double lastBatteryLevelValue = Double.parseDouble(lastBatteryLevel.toString());
                                    double currentBatteryLevelValue = Double.parseDouble(logData.getEventValue());
                                    return Math.abs(currentBatteryLevelValue - lastBatteryLevelValue) > 20;
                                }
                                return false;
                            }
                            @Override
                            public String getAnomaly() {
                                return "Battery level drop over 20%";
                            }
                        },
                        new LogDataAnomalyDetector() {
                            @Override
                            public boolean isAnomaly(LogData logData, DeviceData lastRecordedData) {
                                if(!logData.getEventType().equals("error")){
                                    return false;
                                }
                                return lastRecordedData.getErrorCount() > 3;
                            }
                            @Override
                            public String getAnomaly() {
                                return "More than 3 errors reported";
                            }
                        }
                }
        ));

        // Print the results in JSON format
        JsonNode deviceDataResults = telemetryLogParser.getDeviceDataResults();
        Utils.printJsonObject(deviceDataResults);

        // Print the anomalies detected in the telemetry data
        JsonNode anomaliesResults = telemetryLogParser.getDeviceAnomaliesResults();
        Utils.printJsonObject(anomaliesResults);

    }

}