package com.supplyhouse.SuppliersProcessor.processor;

import com.supplyhouse.SuppliersProcessor.entity.StockItem;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.util.List;

/**
 * Class for processing multiple file formats (CSV, TXT, XLSX) to extract StockItem data.
 *
 * This class takes in processors for specific file formats and a file path. It determines the
 * file extension and delegates the processing to the appropriate processor. The result is a list
 * of StockItem objects extracted from the file.
 */
@Log4j2
public class FileProcessor {
    private CsvProcessor csvProcessor;
    private TxtProcessor txtProcessor;
    private XlsxProcessor xlsxProcessor;
    private String filePath;

    public FileProcessor(CsvProcessor csvProcessor, TxtProcessor txtProcessor, XlsxProcessor xlsxProcessor, String filePath) {
        this.csvProcessor = csvProcessor;
        this.txtProcessor = txtProcessor;
        this.filePath = filePath;
        this.xlsxProcessor = xlsxProcessor;
    }

    /**
     * Processes the file and extracts StockItem data.
     *
     * @return A list of StockItem objects extracted from the file.
     * @throws Exception if file extension is other than CSV, TXT, or XLSX
     */
    public List<StockItem> processFile() throws Exception {
        File file = new File(filePath);

        if (!file.exists()) {
            log.error(filePath + "doesn't exists.");
            return null;
        }

        String fileName = file.getName();
        String fileExtension = getFileExtension(fileName);

        if (fileExtension.equals("txt")) {
            return processTxtFile(file);
        } else if (fileExtension.equals("csv")) {
            return processCsvFile(file);
        } else if (fileExtension.equals("xlsx")) {
            return processXlsxFile(file);
        } else {
            throw new Exception("Couldn't process file in path "+ filePath + " due to unsupported extension: "+ fileExtension);
        }
    }

    /**
     * Gets the file extension from the file name.
     *
     * @param fileName The name of the file.
     * @return The file extension.
     */
    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex != -1 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1);
        }
        return "";
    }

    /**
     * Processes a TXT file using the TxtProcessor.
     *
     * @param file The TXT file to be processed.
     * @return A list of StockItem objects extracted from the file.
     */
    private List<StockItem> processTxtFile(File file) {
        return txtProcessor.processFile(file);
    }

    /**
     * Processes a CSV file using the CsvProcessor.
     *
     * @param file The CSV file to be processed.
     * @return A list of StockItem objects extracted from the file.
     */
    private List<StockItem> processCsvFile(File file) {
        return csvProcessor.processFile(file);
    }

    /**
     * Processes an XLSX file using the XlsxProcessor.
     *
     * @param file The XLSX file to be processed.
     * @return A list of StockItem objects extracted from the file.
     */
    private List<StockItem> processXlsxFile(File file) {
        return xlsxProcessor.process(file);
    }

}

