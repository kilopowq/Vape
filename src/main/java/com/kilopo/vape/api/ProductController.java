package com.kilopo.vape.api;

import com.kilopo.vape.domain.Product;
import com.kilopo.vape.service.ProductService;
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
@RequestMapping(path = "/private/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return service.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable long id) {
        return service.getProductById(id);
    }

    @DeleteMapping("/{id}")
    public void removeProductById(@PathVariable long id){
        service.removeProductById(id);
    }

    @PostMapping("/add")
    public Product addProduct(@RequestBody Resource<Product> resource){
        Product product = resource.getContent();
        return service.addProduct(product);
    }

    @PutMapping("/edit")
    public Product editProduct(@RequestBody Resource<Product> resource){
        Product product = resource.getContent();
        return service.editProduct(product);
    }
}
