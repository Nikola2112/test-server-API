package com.goit.dao.impl;

import com.goit.dao.WarehouseDAO;
import com.goit.model.Warehouse;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WarehouseDAOImpl implements WarehouseDAO {

    private static final String JDBC_URL = "jdbc:postgresql://localhost:5433/postgresDB";
    private static final String JDBC_USER = "postgres";
    private static final String JDBC_PASSWORD = "flash8898";



    @Override
    public List<Warehouse> searchWarehouses(String name, String address, String city, String state, String country, int inventoryQuantity, int limit, int offset) {
        List<Warehouse> resultWarehouses = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String query = "SELECT * FROM warehouse WHERE name ILIKE ? AND address_line_1 ILIKE ? AND city ILIKE ? AND state ILIKE ? AND country ILIKE ? AND inventory_quantity >= ? LIMIT ? OFFSET ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, "%" + name + "%");
                preparedStatement.setString(2, "%" + address + "%");
                preparedStatement.setString(3, "%" + city + "%");
                preparedStatement.setString(4, "%" + state + "%");
                preparedStatement.setString(5, "%" + country + "%");
                preparedStatement.setInt(6, inventoryQuantity);
                preparedStatement.setInt(7, limit);
                preparedStatement.setInt(8, offset);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Warehouse warehouse = mapResultSetToWarehouse(resultSet);
                        resultWarehouses.add(warehouse);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultWarehouses;
    }

    @Override
    public Warehouse getWarehouseById(int id) {
        Warehouse warehouse = null;

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String query = "SELECT * FROM warehouse WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, id);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        warehouse = mapResultSetToWarehouse(resultSet);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return warehouse;
    }

    @Override
    public void addWarehouse(Warehouse warehouse) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String query = "INSERT INTO warehouse (name, address_line_1, address_line_2, city, state, country, inventory_quantity) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, warehouse.getName());
                preparedStatement.setString(2, warehouse.getAddressLine1());
                preparedStatement.setString(3, warehouse.getAddressLine2());
                preparedStatement.setString(4, warehouse.getCity());
                preparedStatement.setString(5, warehouse.getState());
                preparedStatement.setString(6, warehouse.getCountry());
                preparedStatement.setInt(7, warehouse.getInventoryQuantity());

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateWarehouse(Warehouse warehouse) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String query = "UPDATE warehouse SET name=?, address_line_1=?, address_line_2=?, city=?, state=?, country=?, inventory_quantity=? WHERE id=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, warehouse.getName());
                preparedStatement.setString(2, warehouse.getAddressLine1());
                preparedStatement.setString(3, warehouse.getAddressLine2());
                preparedStatement.setString(4, warehouse.getCity());
                preparedStatement.setString(5, warehouse.getState());
                preparedStatement.setString(6, warehouse.getCountry());
                preparedStatement.setInt(7, warehouse.getInventoryQuantity());
                preparedStatement.setInt(8, warehouse.getId());

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteWarehouse(int id) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String query = "DELETE FROM warehouse WHERE id=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, id);

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Warehouse mapResultSetToWarehouse(ResultSet resultSet) throws SQLException {
        Warehouse warehouse = new Warehouse();
        warehouse.setId(resultSet.getInt("id"));
        warehouse.setName(resultSet.getString("name"));
        warehouse.setAddressLine1(resultSet.getString("address_line_1"));
        warehouse.setAddressLine2(resultSet.getString("address_line_2"));
        warehouse.setCity(resultSet.getString("city"));
        warehouse.setState(resultSet.getString("state"));
        warehouse.setCountry(resultSet.getString("country"));
        warehouse.setInventoryQuantity(resultSet.getInt("inventory_quantity"));
        return warehouse;
    }
    @Override
    public int getTotalWarehouses(String name, String address, String city, String state, String country, int inventoryQuantity) {
        int totalWarehouses = 0;

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String query = "SELECT COUNT(*) FROM warehouse WHERE name ILIKE ? AND address_line_1 ILIKE ? AND city ILIKE ? AND state ILIKE ? AND country ILIKE ? AND inventory_quantity >= ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, "%" + name + "%");
                preparedStatement.setString(2, "%" + address + "%");
                preparedStatement.setString(3, "%" + city + "%");
                preparedStatement.setString(4, "%" + state + "%");
                preparedStatement.setString(5, "%" + country + "%");
                preparedStatement.setInt(6, inventoryQuantity);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        totalWarehouses = resultSet.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalWarehouses;
    }
    @Override
    public List<Warehouse> getAllWarehouses() {
        List<Warehouse> warehouses = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String query = "SELECT * FROM warehouse";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {

                while (resultSet.next()) {
                    Warehouse warehouse = mapResultSetToWarehouse(resultSet);
                    warehouses.add(warehouse);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return warehouses;
    }
}

