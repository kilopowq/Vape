package com.kilopo.vape.api;

import com.kilopo.vape.domain.Warehouse;
import com.kilopo.vape.service.impl.WarehouseServiceImpl;
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
@RequestMapping(path = "/private/warehouses")
public class WarehouseController {
    private WarehouseServiceImpl service;

    @GetMapping
    public List<Warehouse> getAllWarehouses() {
        return service.getAllWarehouses();
    }

    @GetMapping("/{id}")
    public Warehouse getWarehouseById(@PathVariable long id) {
        return service.getWarehouseById(id);
    }

    @DeleteMapping("/{id}")
    public void removeWarehouseById(@PathVariable long id){
        service.removeWarehouseById(id);
    }

    @PostMapping("/add")
    public Warehouse addWarehouse(@RequestBody Resource<Warehouse> resource){
        Warehouse Warehouse = resource.getContent();
        return service.addWarehouse(Warehouse);
    }

    @PutMapping("/edit")
    public Warehouse editWarehouse(@RequestBody Resource<Warehouse> resource){
        Warehouse Warehouse = resource.getContent();
        return service.editWarehouse(Warehouse);
    }
}
