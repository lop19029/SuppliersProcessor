package com.supplyhouse.SuppliersProcessor.service;

import com.supplyhouse.SuppliersProcessor.backup.BackupManager;
import com.supplyhouse.SuppliersProcessor.entity.StockItem;
import com.supplyhouse.SuppliersProcessor.processor.DirectoryProcessor;
import com.supplyhouse.SuppliersProcessor.repository.StockItemRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Service class for managing stock items.
 *
 * Handles the processing and persistence of stock items.
 * It utilizes a StockItemRepository for database operations and a
 * DirectoryProcessor to process a specific directory. It also employs a
 * BackupManager to handle backup and restoration of files during processing.
 *
 * @see DirectoryProcessor#processDir()
 * @see com.supplyhouse.SuppliersProcessor.backup.BackupManager
 */

@Service
@Log4j2
public class StockItemService {
    @Autowired
    private StockItemRepository stockItemRepository;
    @Autowired
    private DirectoryProcessor directoryProcessor;
    @Autowired
    private BackupManager backupManager;
    @Value("${dir-feed-db.folderName}")
    private String dirFeedDbFolderName;

    /**
     * Processes the specified directory, extracts stock items, and saves them to the database.
     */
    @Transactional
    public void processDirectory(){
        final var dirPath = System.getProperty("user.dir")  + dirFeedDbFolderName;
        try {
            directoryProcessor.setDirPath(dirPath);
            List<StockItem> stockItemList = directoryProcessor.processDir();
            stockItemList.forEach(i -> saveOrUpdateIfExists(i));
            backupManager.deleteTempDataDirectory();
        } catch (Exception e) {
            log.error("A problem occurred while processing the files. No data was persisted into the database.");
            backupManager.restoreFromTempData(dirPath);
            log.info("All files have been restored to "+ dirPath);
            throw new RuntimeException(e);
        }
    }

    /**
     * Saves a stock item to the database.
     *
     * @param stockItem The stock item to be saved.
     */
    public void saveStockItem(StockItem stockItem){
        stockItemRepository.save(stockItem);
    }

    public void saveOrUpdateIfExists(StockItem stockItem) {
        final var stockItemOriginal = stockItemRepository.findByProductIdAndSupplierId(stockItem.getProductId(),
                stockItem.getSupplierId());
        if(stockItemOriginal == null){
            saveStockItem(stockItem);
        } else {
            stockItem.setQuantity(stockItem.getQuantity() + stockItemOriginal.getQuantity());
            saveStockItem(stockItem);
        }
    }
}
