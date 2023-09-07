package com.supplyhouse.SuppliersProcessor.processor;

import com.supplyhouse.SuppliersProcessor.entity.StockItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.supplyhouse.SuppliersProcessor.backup.BackupManager.moveFileToTempBackupDirectory;

/**
 * Component class for processing a directory and extracting stock items.
 *
 * Responsible for processing a specified directory,
 * identifying files, and using FileProcessor instances to extract
 * stock items from various file formats (CSV, TXT, XLSX).
 *
 * @see com.supplyhouse.SuppliersProcessor.processor.CsvProcessor#processFile(File)
 * @see com.supplyhouse.SuppliersProcessor.processor.TxtProcessor#processFile(File)
 * @see com.supplyhouse.SuppliersProcessor.processor.XlsxProcessor#process(File)
 */
@Component
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Log4j2
public class DirectoryProcessor {
    private String dirPath;

    /**
     * Processes the specified directory and extracts stock items from files.
     * Deletes the file after extracting the data.
     *
     * @return A list of StockItem objects extracted from the directory's files.
     */
    public List<StockItem> processDir() throws Exception {
        File directory = new File(dirPath);

        if (!directory.exists() || !directory.isDirectory()) {
            log.error("Invalid directory path.");
            return null;
        }
        List<StockItem> directoryItems = new ArrayList<>();
        final var files = directory.listFiles();
        if (files != null && files.length > 0) {

            for (File file : files) {
                if (file.isFile()) {
                    FileProcessor fileProcessor = new FileProcessor(new CsvProcessor(), new TxtProcessor(),
                            new XlsxProcessor(), file.getAbsolutePath());
                    List<StockItem> items = fileProcessor.processFile();
                    if (items != null && items.size() > 0) items.forEach(i -> directoryItems.add(i));
                    moveFileToTempBackupDirectory(file);
                }
            }
        } else {
            log.warn(dirPath + " is empty.");
        }
        return directoryItems;
    }


}
