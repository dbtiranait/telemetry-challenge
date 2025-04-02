package com.denisballa.telemetrychallenge;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LogData {

    /**
     * LogData class to hold the data for each log entry
     */
    private LocalDateTime timestamp;

    /**
     * Device ID of the device that generated the log entry
     */
    private String deviceId;

    /**
     * Type of event
     */
    private String eventType;

    /**
     * Data associated with the event
     */
    private String eventValue;

    /**
     * Constructor for LogData
     * @param timestamp The timestamp of the log entry
     * @param deviceId The ID of the device that generated the log entry
     * @param eventType The type of event
     * @param eventValue The data associated with the event
     */
    public LogData(LocalDateTime timestamp, String deviceId, String eventType, String eventValue) {
        this.timestamp = timestamp;
        this.deviceId = deviceId;
        this.eventType = eventType;
        this.eventValue = eventValue;
    }

}
