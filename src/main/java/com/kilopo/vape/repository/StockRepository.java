package com.kilopo.vape.repository;

import com.kilopo.vape.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockRepository extends JpaRepository<Stock, Long> {
    Stock findByProductIdAndWarehouseId(Long productId, Long warehouseId);

    List<Stock> findStocksByProductId(Long productId);

    List<Stock> findStocksByWarehouseId(Long warehouseId);

}
