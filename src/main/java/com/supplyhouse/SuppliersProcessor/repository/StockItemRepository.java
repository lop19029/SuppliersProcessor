package com.supplyhouse.SuppliersProcessor.repository;

import com.supplyhouse.SuppliersProcessor.entity.StockItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing StockItem entities.
 *
 * This interface extends JpaRepository to inherit CRUD operations
 * for StockItem entities.
 *
 * @see org.springframework.data.jpa.repository.JpaRepository
 */
@Repository
public interface StockItemRepository extends JpaRepository<StockItem, Long> {
    public StockItem findByProductIdAndSupplierId(String productId, String supplierId);
}
