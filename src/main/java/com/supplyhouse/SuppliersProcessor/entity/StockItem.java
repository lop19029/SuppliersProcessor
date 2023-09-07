package com.supplyhouse.SuppliersProcessor.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
/**
 * Entity class representing a StockItem in the system. It is mapped into a
 * database table "SUPPLIER_PRODUCT".
 * It uses the @IdClass annotation to indicate the composite primary key
 * composed of productId and supplierId.
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@IdClass(StockItemId.class)
@Table(name = "SUPPLIER_PRODUCT")
public class StockItem {
    @Id
    private String productId;
    @Id
    private String supplierId;
    @NotNull(message = "Quantity field is required.")
    private Integer quantity;
}
