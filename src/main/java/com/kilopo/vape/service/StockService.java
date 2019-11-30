package com.kilopo.vape.service;

import com.kilopo.vape.domain.Stock;

import java.util.List;

public interface StockService {
    List<Stock> getAllStocks();

    Stock addStock(final Stock stock);

    void removeStockById(final Long id);

    Stock editStock(final Stock stock);

    Stock getStockById(final Long id);

    void moveProductsToAnotherStock(final Long warehouseIdFrom, final Long warehouseIdTo, final Long productId, final Long quantity);

    void moveAllProductsToAnotherStock(final Long warehouseIdFrom, final Long warehouseIdTo, final Long productId);

    List<Stock> getAllStocksForProduct(final Long productId);

    List<Stock> getAllStocksForWarehouse(final Long warehouseId);

    void addStockQuantityForProductInWarehouse(final Long productId, final Long warehouseId, final Long quantity);

    void subtractStockQuantityForProductInWarehouse(final Long productId, final Long warehouseId, final Long quantity);

    void updateStockQuantityForProductInWarehouse(final Long productId, final Long warehouseId, final Long quantity);

    Long getSummaryQuantityOfProduct(final Long productId);

    void moveAllProductsToAnotherWarehouse(final Long warehouseFromId, final Long warehouseToId);
}
