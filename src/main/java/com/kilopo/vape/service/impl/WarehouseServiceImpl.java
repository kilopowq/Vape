package com.kilopo.vape.service.impl;

import com.kilopo.vape.domain.Warehouse;
import com.kilopo.vape.repository.WarehouseRepository;
import com.kilopo.vape.service.WarehouseService;
import com.kilopo.vape.service.batch.SimpleBatchService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarehouseServiceImpl extends SimpleBatchService<Warehouse, Long> implements WarehouseService {
    private WarehouseRepository warehouseRepository;

    public WarehouseServiceImpl(final WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    public List<Warehouse> getAllWarehouses() {
        return warehouseRepository.findAll();
    }

    @Override
    public Warehouse addWarehouse(final Warehouse warehouse) {
        return warehouseRepository.save(warehouse);
    }

    @Override
    public void removeWarehouseById(final Long id) {
        warehouseRepository.deleteById(id);
    }

    @Override
    public Warehouse editWarehouse(final Warehouse warehouse) {
        return warehouseRepository.save(warehouse);
    }

    @Override
    public Warehouse getWarehouseById(final Long id) {
        return warehouseRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    }
}