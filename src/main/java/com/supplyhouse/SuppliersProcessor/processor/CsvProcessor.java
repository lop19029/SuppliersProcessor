package com.supplyhouse.SuppliersProcessor.processor;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.supplyhouse.SuppliersProcessor.entity.StockItem;
import lombok.NoArgsConstructor;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Processor class for CSV files to extract StockItem data.
 *
 * This class is responsible for processing CSV files, extracting relevant
 * information (product and quantity), and mapping them to StockItem objects.
 */
@NoArgsConstructor
public class CsvProcessor {

    /**
     * Processes a CSV file and extracts StockItem data.
     *
     * @param file The CSV file to be processed.
     * @return A list of StockItem objects extracted from the file.
     */
    public List<StockItem> processFile(File file) {
        List<StockItem> stockItemList = new ArrayList<>();
        final var supplierId = FilenameUtils.removeExtension(file.getName());

        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            final var csvData = reader.readAll();

            final var header = csvData.get(0);
            int productIndex = -1;
            int quantityIndex = -1;

            for (int i = 0; i < header.length; i++) {
                if (header[i].equalsIgnoreCase("product")) {
                    productIndex = i;
                } else if (header[i].equalsIgnoreCase("quantity") || header[i].equalsIgnoreCase("inventory")) {
                    quantityIndex = i;
                } else if (productIndex > -1 && quantityIndex > -1){
                    break;
                }
            }

            if (productIndex == -1 || quantityIndex == -1) {
                throw new IllegalArgumentException("Required columns not found in the "+ file.getName() +" file.");
            }

            for (int i = 1; i < csvData.size(); i++) {
                String[] rowData = csvData.get(i);
                String productName = rowData[productIndex];
                int quantity = Integer.parseInt(rowData[quantityIndex]);

                StockItem item = new StockItem();
                item.setSupplierId(supplierId);
                item.setProductId(productName);
                item.setQuantity(quantity);

                stockItemList.add(item);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }

        return stockItemList;
    }

}
