package com.kilopo.vape.api.batch;

import com.kilopo.vape.api.util.ResourcesUtil;
import com.kilopo.vape.domain.Mutation;
import com.kilopo.vape.domain.Warehouse;
import com.kilopo.vape.service.impl.WarehouseServiceImpl;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RepositoryRestController
@ResponseBody
@RequestMapping(path = "/private/warehouses/batch")
public class WarehouseBatchController {
    private final WarehouseServiceImpl service;

    public WarehouseBatchController(WarehouseServiceImpl service) {
        this.service = service;
    }

    @PostMapping
    public Resources<Mutation<Warehouse>> batch(@RequestBody Resources<Mutation<Warehouse>> groupResources) {
        return ResourcesUtil.map(groupResources, service::validateAndPerformMutations);
    }
}
