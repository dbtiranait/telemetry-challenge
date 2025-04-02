package com.denisballa.telemetrychallenge;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

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
        telemetryLogParser.parse();

        // Print the results in JSON format
        JsonNode jsonResult = telemetryLogParser.getResults();
        Utils.printJsonObject(jsonResult);

    }

}