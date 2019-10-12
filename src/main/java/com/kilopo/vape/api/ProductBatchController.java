package com.kilopo.vape.api;

import com.kilopo.vape.api.util.ResourcesUtil;
import com.kilopo.vape.domain.Mutation;
import com.kilopo.vape.domain.Product;
import com.kilopo.vape.service.ProductServiceImpl;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RepositoryRestController
@ResponseBody
@RequestMapping(path = "/private/products/batch")
public class ProductBatchController {
    private final ProductServiceImpl service;

    public ProductBatchController(ProductServiceImpl service) {
        this.service = service;
    }

    @PostMapping
    public Resources<Mutation<Product>> batch(@RequestBody Resources<Mutation<Product>> groupResources) {
        return ResourcesUtil.map(groupResources, service::validateAndPerformMutations);
    }
}