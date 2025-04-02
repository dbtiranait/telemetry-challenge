package com.denisballa.telemetrychallenge;

public interface LogDataAnomalyDetector {
    boolean isAnomaly(LogData logData, DeviceData lastRecordedData);
    String getAnomaly();
}
