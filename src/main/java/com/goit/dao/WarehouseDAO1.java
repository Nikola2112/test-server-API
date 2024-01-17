package com.goit.dao;
/*
import com.goit.model.Warehouse;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WarehouseDAO1 {
    private static final String JDBC_URL = "jdbc:postgresql://localhost:5433/postgresDB";
    private static final String JDBC_USER = "postgres";
    private static final String JDBC_PASSWORD = "flash8898";

    public int countWarehouses(String name, String address, String city, String state,
                               String country, int minInventory, int maxInventory,
                               int limit, int offset) {
        int totalCount = 0;

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            StringBuilder queryBuilder = new StringBuilder("SELECT COUNT(*) FROM warehouse WHERE 1=1");

            if (name != null && !name.isEmpty()) {
                queryBuilder.append(" AND name LIKE ? ");
            }

            if (address != null && !address.isEmpty()) {
                queryBuilder.append(" AND (address_line_1 LIKE ? OR address_line_2 LIKE ?) ");
            }

            if (minInventory >= 0) {
                queryBuilder.append(" AND inventory_quantity >= ? ");
            }

            if (maxInventory > 0) {
                queryBuilder.append(" AND inventory_quantity <= ? ");
            }

            // Adjust the query to include LIMIT and OFFSET
            if (limit > 0) {
                queryBuilder.append(" LIMIT ? ");
            }

            if (offset > 0) {
                queryBuilder.append(" OFFSET ? ");
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(queryBuilder.toString())) {
                int parameterIndex = 1;

                if (name != null && !name.isEmpty()) {
                    preparedStatement.setString(parameterIndex++, "%" + name + "%");
                }

                if (address != null && !address.isEmpty()) {
                    preparedStatement.setString(parameterIndex++, "%" + address + "%");
                    preparedStatement.setString(parameterIndex++, "%" + address + "%");
                }

                if (minInventory >= 0) {
                    preparedStatement.setInt(parameterIndex++, minInventory);
                }

                if (maxInventory > 0) {
                    preparedStatement.setInt(parameterIndex++, maxInventory);
                }

                // Set parameters for LIMIT and OFFSET
                if (limit > 0) {
                    preparedStatement.setInt(parameterIndex++, limit);
                }

                if (offset > 0) {
                    preparedStatement.setInt(parameterIndex++, offset);
                }

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    totalCount = resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalCount;
    }
    public List<Warehouse> searchWarehouses(String name, String address, String city, String state,
                                            String country, int minInventory, int maxInventory,
                                            String sortBy, int limit, int offset) {
        List<Warehouse> warehouses = new ArrayList<>();
        int totalCount = 0;

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            StringBuilder queryBuilder = new StringBuilder("SELECT * FROM warehouse WHERE 1=1");

            if (name != null && !name.isEmpty()) {
                queryBuilder.append(" AND name LIKE ? ");
            }

            if (address != null && !address.isEmpty()) {
                queryBuilder.append(" AND (address_line_1 LIKE ? OR address_line_2 LIKE ?) ");
            }

            // Додайте умови для інших параметрів фільтрації

            try (PreparedStatement preparedStatement = connection.prepareStatement(queryBuilder.toString())) {
                int parameterIndex = 1;

                if (name != null && !name.isEmpty()) {
                    preparedStatement.setString(parameterIndex++, "%" + name + "%");
                }

                if (address != null && !address.isEmpty()) {
                    preparedStatement.setString(parameterIndex++, "%" + address + "%");
                    preparedStatement.setString(parameterIndex++, "%" + address + "%");
                }

                // Додайте параметри для інших полів фільтрації

                // Використовуйте LIMIT і OFFSET
                if (limit > 0) {
                    preparedStatement.setInt(parameterIndex++, limit);
                }

                if (offset > 0) {
                    preparedStatement.setInt(parameterIndex++, offset);
                }

                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    Warehouse warehouse = mapResultSetToWarehouse(resultSet);
                    warehouses.add(warehouse);
                }
            }

            // Отримайте загальну кількість рядків для даного фільтру (без LIMIT і OFFSET)
            String countQuery = "SELECT COUNT(*) FROM warehouse WHERE 1=1";

            try (PreparedStatement countStatement = connection.prepareStatement(countQuery)) {
                ResultSet countResult = countStatement.executeQuery();

                if (countResult.next()) {
                    totalCount = countResult.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return warehouses;
    }

    public Warehouse getWarehouseById(int id) {
        Warehouse warehouse = null;

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String query = "SELECT * FROM warehouse WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, id);

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    warehouse = mapResultSetToWarehouse(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return warehouse;
    }
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
}
*/