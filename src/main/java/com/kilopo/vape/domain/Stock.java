package com.kilopo.vape.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "stock",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"product_id", "warehouse_id"})})
public class Stock extends BaseDomain {
    private Product product;
    private Warehouse warehouse;
    private Long quantity;

    public Stock(Product product, Warehouse warehouse, Long quantity) {
        this.product = product;
        this.warehouse = warehouse;
        this.quantity = quantity;
    }

    public Stock() {
    }

    @ManyToOne
    @NotNull
    public Product getProduct() {
        return product;
    }


    @ManyToOne
    @NotNull
    public Warehouse getWarehouse() {
        return warehouse;
    }

    @Column
    @NotNull
    @Min(value = 0L, message = "The value must be positive")
    public Long getQuantity() {
        return quantity;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
