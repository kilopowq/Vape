package com.kilopo.vape.service.impl;

import com.google.common.collect.ImmutableList;
import com.kilopo.vape.domain.Stock;
import com.kilopo.vape.domain.Warehouse;
import com.kilopo.vape.repository.StockRepository;
import com.kilopo.vape.repository.WarehouseRepository;
import com.kilopo.vape.service.StockService;
import com.kilopo.vape.service.batch.SimpleBatchService;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StockServiceImpl extends SimpleBatchService<Stock, Long> implements StockService {
    private StockRepository stockRepository;
    private WarehouseRepository warehouseRepository;

    public StockServiceImpl(final StockRepository stockRepository,
                            final WarehouseRepository warehouseRepository) {
        this.stockRepository = stockRepository;
        this.warehouseRepository = warehouseRepository;
    }

    @Override
    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    @Override
    public Stock addStock(final Stock stock) {
        return stockRepository.save(stock);
    }

    @Override
    public void removeStockById(final Long id) {
        stockRepository.deleteById(id);
    }

    @Override
    public Stock editStock(final Stock stock) {
        return stockRepository.save(stock);
    }

    @Override
    public Stock getStockById(final Long id) {
        return stockRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public void moveProductsToAnotherStock(final Long warehouseIdFrom, final Long warehouseIdTo, final Long productId, final Long quantity) {
        Stock stockFrom = stockRepository.findByProductIdAndWarehouseId(productId, warehouseIdFrom);
        Stock stockTo = stockRepository.findByProductIdAndWarehouseId(productId, warehouseIdTo);

        if (stockFrom.getQuantity() >= quantity) {
            stockTo.setQuantity(stockTo.getQuantity() + quantity);
            stockFrom.setQuantity(stockFrom.getQuantity() - quantity);

            stockRepository.saveAll(ImmutableList.<Stock>builder().add(stockFrom).add(stockTo).build());
        }
    }

    @Override
    public void moveAllProductsToAnotherStock(final Long warehouseIdFrom, final Long warehouseIdTo, final Long productId) {
        Stock stockFrom = stockRepository.findByProductIdAndWarehouseId(productId, warehouseIdFrom);
        Stock stockTo = stockRepository.findByProductIdAndWarehouseId(productId, warehouseIdTo);

        stockTo.setQuantity(stockTo.getQuantity() + stockFrom.getQuantity());
        stockFrom.setQuantity(NumberUtils.LONG_ZERO);

        stockRepository.saveAll(ImmutableList.<Stock>builder().add(stockFrom).add(stockTo).build());
    }

    @Override
    public List<Stock> getAllStocksForProduct(final Long productId) {
        return stockRepository.findStocksByProductId(productId);
    }

    @Override
    public List<Stock> getAllStocksForWarehouse(final Long warehouseId) {
        return stockRepository.findStocksByWarehouseId(warehouseId);
    }

    @Override
    public void addStockQuantityForProductInWarehouse(final Long productId, final Long warehouseId, final Long quantity) {
        Stock stock = stockRepository.findByProductIdAndWarehouseId(productId, warehouseId);
        if (stock.getQuantity() >= quantity) {
            stock.setQuantity(stock.getQuantity() + quantity);
            stockRepository.save(stock);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void subtractStockQuantityForProductInWarehouse(final Long productId, final Long warehouseId, final Long quantity) {
        Stock stock = stockRepository.findByProductIdAndWarehouseId(productId, warehouseId);
        stock.setQuantity(stock.getQuantity() - quantity);
        stockRepository.save(stock);
    }

    @Override
    public void updateStockQuantityForProductInWarehouse(final Long productId, final Long warehouseId, final Long quantity) {
        if (quantity >= NumberUtils.LONG_ZERO) {
            Stock stock = stockRepository.findByProductIdAndWarehouseId(productId, warehouseId);
            stock.setQuantity(quantity);
            stockRepository.save(stock);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Long getSummaryQuantityOfProduct(Long productId) {
        return stockRepository.findStocksByProductId(productId).stream().map(Stock::getQuantity).count();
    }

    @Override
    public void moveAllProductsToAnotherWarehouse(Long warehouseFromId, Long warehouseToId) {
        List<Stock> stocksFrom =
                stockRepository.findStocksByWarehouseId(warehouseFromId)
                        .stream()
                        .filter(stock -> stock.getQuantity() > NumberUtils.LONG_ZERO)
                        .collect(Collectors.toList());
        List<Stock> stocksTo = stockRepository.findStocksByWarehouseId(warehouseToId);
        Optional<Warehouse> warehouseTo = warehouseRepository.findById(warehouseToId);

        warehouseTo.ifPresent(warehouse -> stocksFrom.forEach(stock -> {

            Optional<Stock> resultStockOptional = stocksTo.stream()
                    .filter(stockTo -> stockTo.getProduct().getId().equals(stock.getProduct().getId()))
                    .findFirst();

            if (resultStockOptional.isPresent()) {
                Stock resultStock = resultStockOptional.get();
                resultStock.setQuantity(resultStock.getQuantity() + stock.getQuantity());
                stock.setQuantity(NumberUtils.LONG_ZERO);
            } else {
                stock.setWarehouse(warehouse);
            }
        }));

        List<Stock> resultStocks = ImmutableList.<Stock>builder()
                .addAll(stocksFrom)
                .addAll(stocksTo)
                .build();

        stockRepository.saveAll(resultStocks);
    }
}
