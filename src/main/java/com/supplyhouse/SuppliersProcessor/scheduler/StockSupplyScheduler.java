package com.supplyhouse.SuppliersProcessor.scheduler;


import com.supplyhouse.SuppliersProcessor.service.StockItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Schedules and manages stock supply operations.
 */
@Component
@EnableScheduling
public class StockSupplyScheduler {
    private StockItemService service;

    public StockSupplyScheduler(StockItemService service) {
        this.service = service;
    }

    /**
     * Executed once after the bean is constructed. It triggers the
     * processing of the stock items directory.
     */
    @PostConstruct
    public void onStartUp(){
        service.processDirectory();
    }
    /**
     * Configured to run at 10:00 AM daily. It triggers the processing of the stock items directory.
     */
    @Scheduled(cron = "0 0 10 * * ?")
    public void dailyPopulateStockItems() {
        service.processDirectory();
    }
}
