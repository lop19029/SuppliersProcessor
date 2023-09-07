# Suppliers Processor Application

**Suppliers Processor Application** is a Spring Boot application designed to serve as the central hub for processing supplier data. Suppliers upload a file containing their deliveries into a directory in the server. Those files contain different tables and different file formats (CSV, TXT, XLSX).
This application processes each of those file and persists their data into a single H2 database.

## Features

- Automatic processing of directories at startup and daily at 10 AM.
- Assumes that the files received in the server directory include only the quantity of new product arrivals without considering those currently in stock.
- Files data is only persisted in the database once if and only if all files data is processed and persisted. Otherwise, files remain in the original directory.
- All processed files are deleted to maintain system cleanliness.

## Usage

To run the application, use the following command:

```bash
mvn spring-boot:run
```
## Accesing the Dabase
- Open the H2 console in any browser: http://localhost:8080/h2-console 
- Enter the "bloom" username

### Author
- Alex López
 https://www.linkedin.com/in/alexhumbertolopez/
