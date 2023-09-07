package com.supplyhouse.SuppliersProcessor.backup;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

/**
 * Manages backup operations for files, including moving files to a temporary directory,
 * restoring files from the temporary directory, and deleting the temporary directory.
 */
@Component
@Log4j2
public class BackupManager {

    /**
     * Moves a file to the temp_data directory for backup while keeping a copy.
     *
     * @param file The file to be backed up.
     */
    public static void moveFileToTempBackupDirectory(File file) {
        File tempDataDir = new File("temp_data");
        if (!tempDataDir.exists() && !tempDataDir.isDirectory()) {
            tempDataDir.mkdir();
        }

        try {
            Path sourcePath = file.toPath();
            Path destinationPath = Paths.get("temp_data", file.getName());
            Files.move(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            log.info(file.getName() + " moved to temp_data successfully.");
        } catch (IOException e) {
            log.error("Failed to move " + file.getName() + " to temp_data.");
        }
    }

    /**
     * Deletes the temp_data directory and its contents.
     */
    public void deleteTempDataDirectory() {
        File tempDataDir = new File("temp_data");

        if (tempDataDir.exists() && tempDataDir.isDirectory()) {
            try {
                FileUtils.deleteDirectory(tempDataDir);
                log.info("temp_data directory deleted successfully.");
            } catch (IOException e) {
                log.error("Failed to delete temp_data directory.");
            }
        } else {
            log.warn("temp_data directory does not exist.");
        }
    }

    /**
     * Restores all files from the temp_data directory to the original directory
     * and deletes the temp_data directory.
     *
     * @param destinationDirPath The path of the original directory that needs to be specified.
     */
    public void restoreFromTempData(String destinationDirPath) {
        File tempDataDir = new File("temp_data");

        if (tempDataDir.exists() && tempDataDir.isDirectory()) {
            File destinationDir = new File(destinationDirPath);

            if (!destinationDir.exists() || !destinationDir.isDirectory()) {
                log.error("Invalid destination directory path.");
                return;
            }

            try {
                FileUtils.copyDirectory(tempDataDir, destinationDir);
                log.info("Files restored successfully.");
                FileUtils.deleteDirectory(tempDataDir);
            } catch (IOException e) {
                log.error("Failed to move files from temp_data directory.");
            }
        } else {
            log.warn("temp_data directory does not exist.");
        }
    }
}
