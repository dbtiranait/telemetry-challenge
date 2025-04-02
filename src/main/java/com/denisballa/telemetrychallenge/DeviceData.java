package com.denisballa.telemetrychallenge;

import java.util.HashMap;
import java.util.Map;

public class DeviceData {

    /**
     * DeviceData class to hold the data for each device
     */
    private String deviceId;

    /**
     * Error count for the device
     */
    private int errorCount;

    /**
     * Map to hold the events for the device
     */
    private Map<String, Object> events;

    /**
     * Constructor for DeviceData
     * @param deviceId The ID of the device
     */
    public DeviceData(String deviceId){
        this.deviceId = deviceId;
        this.errorCount = 0;
        this.events = new HashMap<>();
    }

    /**
     * Method to add an event to the device data
     * @param logData The log data to be added
     */
    public void addEvent(LogData logData) {
        if (logData.getEventType().equals("error")) {
            errorCount++;
        }
        events.put(logData.getEventType(), logData.getEventValue());
    }

    /**
     * Method to get the error count for the device
     * @return The error count
     */
    public int getErrorCount() {
        return errorCount;
    }

    /**
     * Method to get the device ID
     * @return The device ID
     */
    public String getDeviceId() {
        return deviceId;
    }

    /**
     * Method to get the events for the device
     * @return The events map
     */
    public Map<String, Object> getEvents() {
        return events;
    }

}
