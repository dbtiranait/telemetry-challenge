package com.denisballa.telemetrychallenge;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * TelemetryLogParser is responsible for parsing a telemetry log file and extracting device data.
 * It reads the log file, processes the data, and stores it in a map where the key is the device ID.
 * The parsed results can be retrieved as a JsonNode.
 */
public class TelemetryLogParser {

    /**
     * The path to the telemetry log file.
     */
    private String logFilePath;

    /**
     * A map that stores device data, where the key is the device ID and the value is a DeviceData object.
     */
    private Map<String, DeviceData> deviceDataMap;

    /**
     * A map that stores anomalies detected in the telemetry data, where the key is the device ID and the value is a list of anomalies.
     */
    private Map<String, List<String>> deviceAnomaliesMap;

    /**
     * Constructor for TelemetryLogParser.
     * @param logFilePath the path to the telemetry log file.
     */
    public TelemetryLogParser(String logFilePath) {
        this.logFilePath = logFilePath;
    }

    /**
     * Returns the path to the telemetry log file.
     * @return the path to the log file.
     */
    public String getLogFilePath() {
        return logFilePath;
    }

    /**
     * Sets the path to the telemetry log file.
     * @param logFilePath the path to the log file.
     */
    public void setLogFilePath(String logFilePath) {
        this.logFilePath = logFilePath;
    }

    /**
     * Parses the telemetry log file and populates the deviceDataMap with DeviceData objects.
     * Each DeviceData object contains a list of events for a specific device.
     * @throws IOException if an error occurs while reading the log file.
     */
    public void parse() throws IOException {
        parseWithAnomalyDetecting(List.of());
    }

    /**
     * Parses the telemetry log file and populates the deviceDataMap with DeviceData objects.
     * Each DeviceData object contains a list of events for a specific device.
     * Additionally, it detects anomalies in the telemetry data using the provided anomaly detectors.
     * @param anomalyDetectors a list of anomaly detectors to be used for detecting anomalies in the telemetry data.
     * @throws IOException if an error occurs while reading the log file.
     */
    public void parseWithAnomalyDetecting(List<LogDataAnomalyDetector> anomalyDetectors) throws IOException {
        deviceDataMap = new HashMap<>();
        deviceAnomaliesMap = new HashMap<>();
        try (BufferedReader inputStream = Utils.getBufferedReader(logFilePath)) {
            LogData logData = null;
            while ((logData = Utils.getNextLogData(inputStream)) != null) {
                DeviceData currentDeviceData = deviceDataMap.get(logData.getDeviceId());
                if (currentDeviceData == null) {
                    currentDeviceData = new DeviceData(logData.getDeviceId());
                }
                currentDeviceData.addEvent(logData);
                deviceDataMap.put(logData.getDeviceId(), currentDeviceData);
                if(!anomalyDetectors.isEmpty()){
                    List<LogDataAnomalyDetector> anomalyDetectorsOfDevice = Utils.findAnomalyDetectors(logData, anomalyDetectors, currentDeviceData);
                    if(!anomalyDetectorsOfDevice.isEmpty()){
                        List<String> deviceAnomalies = deviceAnomaliesMap.get(logData.getDeviceId());
                        if (deviceAnomalies == null) {
                            deviceAnomalies = new ArrayList<>();
                        }
                        for(LogDataAnomalyDetector detector : anomalyDetectorsOfDevice){
                            if(!deviceAnomalies.contains(detector.getAnomaly())){
                                deviceAnomalies.add(detector.getAnomaly());
                            }
                        }
                        deviceAnomaliesMap.put(logData.getDeviceId(), deviceAnomalies);
                    }
                }
            }
        }
    }

    /**
     * Returns the parsed results as a JsonNode.
     * @return JsonNode containing the parsed telemetry data.
     */
    public JsonNode getDeviceDataResults() {
        if (deviceDataMap != null) {
            return Utils.getJsonNodeForDeviceData(deviceDataMap);
        }
        return null;
    }

    /**
     * Returns the detected anomalies as a JsonNode.
     * @return JsonNode containing the detected anomalies.
     */
    public JsonNode getDeviceAnomaliesResults() {
        if (deviceAnomaliesMap != null) {
            return Utils.getJsonNodeForAnomalies(deviceAnomaliesMap);
        }
        return null;
    }

    /**
     * Returns a list of DeviceData objects ordered by the number of errors.
     * @return a list of DeviceData objects ordered by error count.
     */
    public List<DeviceData> getDevicesOrderedByErrors() {
        if (deviceDataMap == null) {
            return new ArrayList<>();
        }
        return deviceDataMap.values().stream()
                .sorted(Comparator.comparingInt(DeviceData::getErrorCount))
                .collect(Collectors.toList());
    }

}
