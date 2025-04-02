# Telemetry Challenge

## Description

This project parses a telemetry log file and extracts device data, including error counts and events. The results are printed in JSON format.

## Prerequisites

- Java 21 or higher
- Maven 3.6.0 or higher

## Building the Project

1. **Clone the repository**:
    ```sh
    git clone git@github.com:dbtiranait/telemetry-challenge.git
    cd telemetry-challenge
    ```

2. **Build the project using Maven**:
    ```sh
    mvn clean package
    ```

   This command will compile the project and package it into an executable JAR file. The JAR file will be located in the `target` directory.

## Running the Project

1. **Navigate to the project directory**:
    ```sh
    cd /path/to/telemetry-challenge
    ```

2. **Run the JAR file**:
    ```sh
    java -jar target/telemetry-challenge-1.0.0-shaded.jar src/main/resources/logsample.txt
    ```

   Replace `src/main/resources/logsample.txt` with the path to your log file.

## Usage

The program expects a single argument: the path to the telemetry log file. It will parse the log file and print the results in JSON format.

```sh
java -jar target/telemetry-challenge-1.0.0-shaded.jar <log_file_path>