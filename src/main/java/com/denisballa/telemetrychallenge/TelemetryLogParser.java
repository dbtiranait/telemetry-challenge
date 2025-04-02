package com.denisballa.telemetrychallenge;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
        deviceDataMap = new HashMap<>();
        try (BufferedReader inputStream = Utils.getBufferedReader(logFilePath)) {
            LogData logData = null;
            while ((logData = Utils.getNextLogData(inputStream)) != null) {
                DeviceData currentDeviceData = deviceDataMap.get(logData.getDeviceId());
                if (currentDeviceData == null) {
                    currentDeviceData = new DeviceData(logData.getDeviceId());
                }
                currentDeviceData.addEvent(logData);
                deviceDataMap.put(logData.getDeviceId(), currentDeviceData);
            }
        }
    }

    /**
     * Returns the parsed results as a JsonNode.
     * @return JsonNode containing the parsed telemetry data.
     */
    public JsonNode getResults() {
        if (deviceDataMap != null) {
            return Utils.getJsonNode(deviceDataMap);
        }
        return null;
    }

}
