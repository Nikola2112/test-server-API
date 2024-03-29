package com.goit.dao;
import com.goit.model.Warehouse;

import java.util.List;

public interface WarehouseDAO {
    List<Warehouse> searchWarehouses(String name, String address, String city, String state, String country, int inventoryQuantity, int limit, int offset, String sortBy, String sortOrder);
    int getTotalWarehouses(String name, String address, String city, String state, String country, int inventoryQuantity, String sortBy, String sortOrder);
    Warehouse getWarehouseById(int id);
    void addWarehouse(Warehouse warehouse);
    void updateWarehouse(Warehouse warehouse);
    void deleteWarehouse(int id);
    List<Warehouse> getAllWarehouses();

}
