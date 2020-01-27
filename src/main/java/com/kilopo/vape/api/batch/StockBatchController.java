package com.kilopo.vape.api.batch;

import com.kilopo.vape.api.util.ResourcesUtil;
import com.kilopo.vape.domain.Mutation;
import com.kilopo.vape.domain.Stock;
import com.kilopo.vape.service.impl.StockServiceImpl;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RepositoryRestController
@ResponseBody
@RequestMapping(path = "/private/stocks/batch")
public class StockBatchController {
    private final StockServiceImpl service;

    public StockBatchController(StockServiceImpl service) {
        this.service = service;
    }

    @PostMapping
    public Resources<Mutation<Stock>> batch(@RequestBody Resources<Mutation<Stock>> groupResources) {
        return ResourcesUtil.map(groupResources, service::validateAndPerformMutations);
    }
}
