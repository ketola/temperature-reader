# temperature-reader

temperature-reader
- library for reading temperature data sent to the arduino rf-receiver

temperature-rest
- a Spring Boot application that serves the temperature data + angular front for displaying the data

Start the application with:  java -jar temperature-rest-0.0.1-SNAPSHOT.jar --server.port=3000 --serial.port=/dev/ttyUSB0
