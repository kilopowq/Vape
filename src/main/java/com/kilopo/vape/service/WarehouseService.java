package com.kilopo.vape.service;

import com.kilopo.vape.domain.Warehouse;

import java.util.List;

public interface WarehouseService {
    List<Warehouse> getAllWarehouses();

    Warehouse addWarehouse(final Warehouse warehouse);

    void removeWarehouseById(final Long id);

    Warehouse editWarehouse(final Warehouse warehouse);

    Warehouse getWarehouseById(final Long id);
}
