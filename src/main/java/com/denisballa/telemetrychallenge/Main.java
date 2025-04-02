package com.denisballa.telemetrychallenge;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {


    /**
     * A timestamp (YYYY-MM-DD HH:MM:SS)
     * A device ID
     * An event type (temperature, battery, motion, error)
     * A value associated with the event
     *
     *  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
     *         LocalDateTime dateTime = LocalDateTime.parse(timestamp, formatter);
     *         System.out.println(dateTime);
     */

    static class Data {
        LocalDateTime timestamp;
        String deviceId;
        String eventType;
        String eventValue;
        public Data(LocalDateTime timestamp, String deviceId, String eventType, String eventValue) {
            this.timestamp = timestamp;
            this.deviceId = deviceId;
            this.eventType = eventType;
            this.eventValue = eventValue;
        }
    }

    static class Result {
        String deviceId;
        Map<String, String> eventsData;
        int errorCount;
    }

    public static List<Result> getData(String pathToFile){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Map<String, Result> dataMap = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(pathToFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                LocalDateTime timestamp = LocalDateTime.parse(parts[0] + " " + parts[1], formatter);
                String deviceId = parts[3];
                String eventType = parts[5];
                String value = parts[7];
                addNewRecord(new Data(timestamp, deviceId, eventType, value), dataMap);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataMap.values().stream().toList();
    }

    private static void addNewRecord(Data data, Map<String, Result> dataMap) {
        String key = data.deviceId;
        if (dataMap.containsKey(key)) {
            Result existingResult = dataMap.get(key);
            if (existingResult.eventsData.containsKey(data.eventType)) {
                if(data.eventType.equals("error")) {
                    existingResult.errorCount++;
                }
                else {
                    existingResult.eventsData.put(data.eventType, data.eventValue);
                }
            } else {
                existingResult.eventsData.put(data.eventType, data.eventValue);
            }
        } else {
            Result newResult = new Result();
            newResult.deviceId = key;
            newResult.eventsData = new HashMap<>();
            if(data.eventType.equals("error")) {
                newResult.errorCount = 1;
            }
            else {
                newResult.errorCount = 0;
                newResult.eventsData.put(data.eventType, data.eventValue);
            }
            dataMap.put(key, newResult);
        }
    }

    public static void main(String[] args) {

    }

}