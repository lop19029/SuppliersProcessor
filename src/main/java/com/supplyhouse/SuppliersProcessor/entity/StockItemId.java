package com.supplyhouse.SuppliersProcessor.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class StockItemId implements Serializable {
    private String productId;
    private String supplierId;
}
