package com.kilopo.vape.service;

import com.kilopo.vape.domain.Product;
import com.kilopo.vape.repository.ProductRepository;
import com.kilopo.vape.service.batch.SimpleBatchService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl extends SimpleBatchService<Product, Long> implements ProductService {
    private ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}
