package com.supplyhouse.SuppliersProcessor.processor;

import com.supplyhouse.SuppliersProcessor.entity.StockItem;
import lombok.NoArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Processor class for Excel (XLSX) files to extract StockItem data.
 *
 * This class is responsible for processing Excel (XLSX) files,
 * extracting relevant information (product and quantity), and mapping them to StockItem objects.
 */
@NoArgsConstructor
public class XlsxProcessor {

    /**
     * Processes an Excel (XLSX) file and extracts StockItem data.
     *
     * @param file The Excel (XLSX) file to be processed.
     * @return A list of StockItem objects extracted from the file.
     */
    public List<StockItem> process(File file) {
        List<StockItem> stockItemList = new ArrayList<>();
        final var supplierId = FilenameUtils.removeExtension(file.getName());

        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.iterator();
            Row headerRow = rowIterator.next();

            int productIndex = -1;
            int quantityIndex = -1;

            Iterator<Cell> cellIterator = headerRow.iterator();
            int columnIndex = 0;
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                String header = cell.getStringCellValue();
                if (header.equalsIgnoreCase("product")) {
                    productIndex = columnIndex;
                } else if (header.equalsIgnoreCase("quantity") || header.equalsIgnoreCase("inventory")) {
                    quantityIndex = columnIndex;
                } else if (productIndex > -1 && quantityIndex > -1){
                    break;
                }
                columnIndex++;
            }

            if (productIndex == -1 || quantityIndex == -1) {
                throw new IllegalArgumentException("Required columns not found in the "+ file.getName() +" file.");
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Cell productCell = row.getCell(productIndex);
                Cell quantityOrInventoryCell = row.getCell(quantityIndex);

                String productName = productCell.getStringCellValue();
                int quantity = (int) quantityOrInventoryCell.getNumericCellValue();

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
        }

        return stockItemList;
    }
}
