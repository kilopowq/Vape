package com.kilopo.vape.service.impl;

import com.kilopo.vape.domain.Product;
import com.kilopo.vape.repository.ProductRepository;
import com.kilopo.vape.service.ProductService;
import com.kilopo.vape.service.batch.SimpleBatchService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl extends SimpleBatchService<Product, Long> implements ProductService {
    private ProductRepository productRepository;

    public ProductServiceImpl(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product addProduct(final Product product) {
        return productRepository.save(product);
    }

    @Override
    public void removeProductById(final Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public Product editProduct(final Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product getProductById(final Long id) {
        return productRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    }
}
