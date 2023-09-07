package com.supplyhouse.SuppliersProcessor.processor;

import com.supplyhouse.SuppliersProcessor.entity.StockItem;
import lombok.NoArgsConstructor;
import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Processor class for tab-delimited text files to extract StockItem data.
 *
 * This class is responsible for processing tab-delimited text files,
 * extracting relevant information (product and quantity), and mapping them to StockItem objects.
 */
@NoArgsConstructor
public class TxtProcessor {

    /**
     * Processes a tab-delimited text file and extracts StockItem data.
     *
     * @param file The tab-delimited text file to be processed.
     * @return A list of StockItem objects extracted from the file.
     */
    public List<StockItem> processFile(File file) {
        List<StockItem> stockItemList = new ArrayList<>();
        final var supplierId = FilenameUtils.removeExtension(file.getName());

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String headerLine = reader.readLine(); // Read the header line

            if (headerLine == null) {
                throw new IllegalArgumentException("Empty file.");
            }

            final var headers = headerLine.split("\t");

            int productIndex = -1;
            int quantityIndex = -1;

            for (int i = 0; i < headers.length; i++) {
                if (headers[i].equalsIgnoreCase("product")) {
                    productIndex = i;
                } else if (headers[i].equalsIgnoreCase("quantity") || headers[i].equalsIgnoreCase("inventory")) {
                    quantityIndex = i;
                } else if (productIndex > -1 && quantityIndex > -1){
                    break;
                }
            }

            if (productIndex == -1 || quantityIndex == -1) {
                throw new IllegalArgumentException("Required columns not found in the "+ file.getName() +" file.");
            }

            String line;
            while ((line = reader.readLine()) != null) {
                final var data = line.split("\t");

                if (data.length >= 2) {
                    String productName = data[productIndex];
                    int quantity = Integer.parseInt(data[quantityIndex]);

                    StockItem item = new StockItem();
                    item.setSupplierId(supplierId);
                    item.setProductId(productName);
                    item.setQuantity(quantity);

                    stockItemList.add(item);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return stockItemList;
    }
}
