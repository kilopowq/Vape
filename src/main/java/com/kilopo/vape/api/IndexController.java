package com.kilopo.vape.api;

import com.kilopo.vape.service.ProductService;
import com.kilopo.vape.service.StockService;
import com.kilopo.vape.service.WarehouseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class IndexController {

    private StockService stockService;
    private WarehouseService warehouseService;
    private ProductService productService;

    public IndexController(StockService stockService, WarehouseService warehouseService, ProductService productService) {
        this.stockService = stockService;
        this.warehouseService = warehouseService;
        this.productService = productService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/private")
    public String privateOne(Model model) {
        return "private";
    }
}