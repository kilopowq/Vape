package com.kilopo.vape.api;

import com.kilopo.vape.domain.Stock;
import com.kilopo.vape.service.impl.StockServiceImpl;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RepositoryRestController
@ResponseBody
@RequestMapping(path = "/private/stocks")
public class StockController {
    private StockServiceImpl service;

    public StockController(StockServiceImpl service) {
        this.service = service;
    }

    @GetMapping
    public List<Stock> getAllStocks() {
        return service.getAllStocks();
    }

    @GetMapping("/{id}")
    public Stock getStockById(@PathVariable long id) {
        return service.getStockById(id);
    }

    @GetMapping("/product/{productId}")
    public List<Stock> getAllStockForProduct(@PathVariable long productId){
        return service.getAllStocksForProduct(productId);
    }

    @GetMapping("/warehouse/{warehouseId}")
    public List<Stock> getAllStockForWarehouse(@PathVariable long warehouseId){
        return service.getAllStocksForWarehouse(warehouseId);
    }

    @GetMapping("/product/{productId}/summary")
    public Long getSummaryStockForProduct(@PathVariable long productId){
        return service.getSummaryQuantityOfProduct(productId);
    }

    @DeleteMapping("/{id}")
    public void removeStockById(@PathVariable long id){
        service.removeStockById(id);
    }

    @PostMapping("/add")
    public Stock addStock(@RequestBody Resource<Stock> resource){
        Stock Stock = resource.getContent();
        return service.addStock(Stock);
    }

    @PutMapping("/edit")
    public Stock editStock(@RequestBody Resource<Stock> resource){
        Stock Stock = resource.getContent();
        return service.editStock(Stock);
    }

    @PutMapping("/{warehouseFromId}/move/{warehouseToId}/product/{productId}/quantity/{quantity}")
    public void MoveProductToAnotherStock(@PathVariable long warehouseFromId, @PathVariable long warehouseToId,
                                          @PathVariable long productId, @PathVariable long quantity){
        service.moveProductsToAnotherStock(warehouseFromId, warehouseToId, productId, quantity);
    }

    @PutMapping("/{warehouseFromId}/move/{warehouseToId}/product/{productId}")
    public void MoveProductToAnotherStock(@PathVariable long warehouseFromId, @PathVariable long warehouseToId,
                                          @PathVariable long productId){
        service.moveAllProductsToAnotherStock(warehouseFromId, warehouseToId, productId);
    }

    @PutMapping("/add/product/{productId}/warehouse/{warehouseId}/quantity/{quantity}")
    public void addStockQuantity(@PathVariable long productId, @PathVariable long warehouseId,
                                 @PathVariable long quantity){
        service.addStockQuantityForProductInWarehouse(productId, warehouseId, quantity);
    }

    @PutMapping("/subtract/product/{productId}/warehouse/{warehouseId}/quantity/{quantity}")
    public void subtractStockQuantity(@PathVariable long productId, @PathVariable long warehouseId,
                                       @PathVariable long quantity){
        service.subtractStockQuantityForProductInWarehouse(productId, warehouseId, quantity);
    }

    @PutMapping("/update/product/{productId}/warehouse/{warehouseId}/quantity/{quantity}")
    public void updateStockQuantity(@PathVariable long productId, @PathVariable long warehouseId,
                                    @PathVariable long quantity){
        service.updateStockQuantityForProductInWarehouse(productId, warehouseId, quantity);
    }

    @PutMapping("/{warehouseFromId}/move/{warehouseToId}")
    public void updateStockQuantity(@PathVariable long warehouseFromId, @PathVariable long warehouseToId){
        service.moveAllProductsToAnotherWarehouse(warehouseFromId, warehouseToId);
    }
}
