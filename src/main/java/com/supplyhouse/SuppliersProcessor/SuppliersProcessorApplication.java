package com.supplyhouse.SuppliersProcessor;

import com.supplyhouse.SuppliersProcessor.processor.DirectoryProcessor;
import com.supplyhouse.SuppliersProcessor.processor.FileProcessor;
import com.supplyhouse.SuppliersProcessor.service.StockItemService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * SuppliersProcessorApplication.
 *
 * This Spring Boot application serves as the central hub for processing
 * supplier data. It utilizes various components including StockItemService,
 * DirectoryProcessor, and FileProcessor to manage and extract
 * stock information from different file formats (CSV, TXT, XLSX).
 *
 * The application is equipped with scheduled tasks to automatically process
 * directories at startup and daily at 10 AM. This application assumes that the files received
 * in the server directory include only the quantity of new product arrivals without considering
 * those currently in stock. Files data is only persisted in database once if and only if all files
 * data is processed and persisted. Otherwise, files stay the same inside the original directory.
 * All the files are deleted once they have been processed.
 *
 * @see StockItemService
 * @see DirectoryProcessor
 * @see FileProcessor
 *
 * @author Alex Lopez
 * <a href="https://www.linkedin.com/in/alexhumbertolopez/">LinkedIn</a>
 * <a href="https://github.com/lop19029">GitHub</a>
 */
@SpringBootApplication
public class SuppliersProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(SuppliersProcessorApplication.class, args);
	}

}
