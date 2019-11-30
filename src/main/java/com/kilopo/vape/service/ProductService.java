package com.kilopo.vape.service;

import com.kilopo.vape.domain.Product;

import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();

    Product addProduct(final Product product);

    void removeProductById(final Long id);

    Product editProduct(final Product product);

    Product getProductById(final Long id);
}
