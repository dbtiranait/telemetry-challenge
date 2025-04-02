package com.denisballa.telemetrychallenge;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Utility class for handling log data and JSON conversion.
 */
public class Utils {

    /**
     * DateTimeFormatter for parsing log timestamps.
     */
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Private constructor to prevent instantiation.
     */
    public static BufferedReader getBufferedReader(String filePath) throws FileNotFoundException {
        return new BufferedReader(new FileReader(filePath));
    }

    /**
     * Reads the next log data from the BufferedReader.
     *
     * @param bufferedReader the BufferedReader to read from
     * @return a LogData object containing the parsed log data
     * @throws IOException if an I/O error occurs
     */
    public static LogData getNextLogData(BufferedReader bufferedReader) throws IOException {
        String line = bufferedReader.readLine();
        if(line == null) {
            return null;
        }
        String[] parts = line.split(" ");
        LocalDateTime timestamp = LocalDateTime.parse(parts[0] + " " + parts[1], formatter);
        String deviceId = parts[3];
        String eventType = parts[5];
        String eventValue = parts[7];
        return new LogData(timestamp, deviceId, eventType, eventValue);
    }

    /**
     * Prints the JSON object to the console.
     *
     * @param jsonResult the JSON object to print
     */
    public static void printJsonObject(JsonNode jsonResult) {
        if (jsonResult == null) {
            System.out.println("No data available.");
            return;
        }
        System.out.println(jsonResult.toPrettyString());
    }

    /**
     * Converts a map of device data to a JSON node.
     *
     * @param deviceDataMap the map of device data
     * @return a JsonNode representing the device data
     */
    public static JsonNode getJsonNodeForDeviceData(Map<String, DeviceData> deviceDataMap) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode rootNode = objectMapper.createObjectNode();
        for (Map.Entry<String, DeviceData> entry : deviceDataMap.entrySet()) {
            String deviceId = entry.getKey();
            DeviceData deviceData = entry.getValue();
            ObjectNode deviceNode = objectMapper.createObjectNode();
            deviceNode.put("error_count", deviceData.getErrorCount());
            for (Map.Entry<String, Object> eventEntry : deviceData.getEvents().entrySet()) {
                if (eventEntry.getValue() instanceof Number) {
                    deviceNode.put(eventEntry.getKey(), ((Number) eventEntry.getValue()).doubleValue());
                } else {
                    deviceNode.put(eventEntry.getKey(), eventEntry.getValue().toString());
                }
            }
            rootNode.set(deviceId, deviceNode);
        }
        return rootNode;
    }

    /**
     * Converts a map of device anomalies to a JSON node.
     *
     * @param deviceAnomaliesMap the map of device anomalies
     * @return a JsonNode representing the device anomalies
     */
    public static JsonNode getJsonNodeForAnomalies(Map<String, List<String>> deviceAnomaliesMap) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode rootNode = objectMapper.createObjectNode();
        ObjectNode anomaliesNode = objectMapper.createObjectNode();

        for (Map.Entry<String, List<String>> entry : deviceAnomaliesMap.entrySet()) {
            String deviceId = entry.getKey();
            List<String> anomalies = entry.getValue();
            ArrayNode anomaliesArray = objectMapper.createArrayNode();
            for (String anomaly : anomalies) {
                anomaliesArray.add(anomaly);
            }
            anomaliesNode.set(deviceId, anomaliesArray);
        }

        rootNode.set("anomalies", anomaliesNode);
        return rootNode;
    }

    /**
     * Finds the anomaly detector that matches the log data and last recorded data.
     *
     * @param logData the log data to check
     * @param anomalyDetectors the list of anomaly detectors
     * @param lastRecordedData the last recorded data for the device
     * @return a list of matching anomaly detectors
     */
    public static List<LogDataAnomalyDetector> findAnomalyDetectors
            (LogData logData, List<LogDataAnomalyDetector> anomalyDetectors, DeviceData lastRecordedData) {
        List<LogDataAnomalyDetector> anomalyDetectorsResultList = new ArrayList<>();
        for (LogDataAnomalyDetector anomalyDetector : anomalyDetectors) {
            if (anomalyDetector.isAnomaly(logData, lastRecordedData)) {
                anomalyDetectorsResultList.add(anomalyDetector);
            }
        }
        return anomalyDetectorsResultList;
    }

}
