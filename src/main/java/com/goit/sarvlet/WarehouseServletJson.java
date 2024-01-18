package com.goit.sarvlet;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goit.dao.WarehouseDAO;
import com.goit.dao.impl.WarehouseDAOImpl;
import com.goit.model.Warehouse;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/warehouses")
public class WarehouseServletJson extends HttpServlet {
    private WarehouseDAO warehouseDAO = new WarehouseDAOImpl();
    private ObjectMapper objectMapper;

    @Override
    public void init(ServletConfig config) throws ServletException {
        objectMapper = new ObjectMapper();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action != null) {
            switch (action) {
                case "search":
                    handleSearch(request, response);
                    break;
                case "view":
                    handleView(request, response);
                    break;
                case "add":
                    handleAdd(request, response);
                    break;
                case "update":
                    handleUpdate(request, response);
                    break;
                case "delete":
                    handleDelete(request, response);
                    break;
                default:
                    showWarehouseList(request, response);
                    break;
            }
        } else {
            showWarehouseList(request, response);
            response.setContentType("application/json; charset=utf-8");
            response.getWriter().write(objectMapper.writeValueAsString(Collections.emptyMap()));
        }
    }

    private void handleSearch(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    try {
        // Отримання параметрів пошуку
        String name = request.getParameter("name");
        String address = request.getParameter("address");
        String city = request.getParameter("city");
        String state = request.getParameter("state");
        String country = request.getParameter("country");
        String inventoryQuantityParam = request.getParameter("inventoryQuantity");
        String limitParam = request.getParameter("limit");
        String offsetParam = request.getParameter("offset");
        String sortBy = request.getParameter("sortBy"); // Новий параметр для сортування
        String sortOrder = request.getParameter("sortOrder"); // Новий параметр для напрямку сортування

        // Перевірка і конвертація параметрів в числа
        int inventoryQuantity = (inventoryQuantityParam != null) ? Integer.parseInt(inventoryQuantityParam) : 0;
        int limit = (limitParam != null) ? Integer.parseInt(limitParam) : 10;
        int offset = (offsetParam != null) ? Integer.parseInt(offsetParam) : 0;

        // Логіка пошуку в сервісі з додатковими параметрами сортування
        List<Warehouse> warehouses = warehouseDAO.searchWarehouses(name, address, city, state, country, inventoryQuantity, limit, offset, sortBy, sortOrder);
        int totalWarehouses = warehouseDAO.getTotalWarehouses(name, address, city, state, country, inventoryQuantity,sortBy, sortOrder);

        // Передача результатів на сторінку
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("warehouses", warehouses);
        resultMap.put("totalWarehouses", totalWarehouses);
        resultMap.put("limit", limit);
        resultMap.put("offset", offset);

        response.setContentType("application/json; charset=utf-8");
        response.getWriter().write(objectMapper.writeValueAsString(resultMap));
    } catch (NumberFormatException e) {
        e.printStackTrace();
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid numeric parameter");
    }
}

    private void handleView(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int warehouseId = Integer.parseInt(request.getParameter("id"));
        Warehouse warehouse = warehouseDAO.getWarehouseById(warehouseId);

        response.setContentType("application/json; charset=utf-8");
        response.getWriter().write(objectMapper.writeValueAsString(warehouse));
    }

    private void handleAdd(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String addressLine1 = request.getParameter("addressLine1");
        String addressLine2 = request.getParameter("addressLine2");
        String city = request.getParameter("city");
        String state = request.getParameter("state");
        String country = request.getParameter("country");
        int inventoryQuantity = Integer.parseInt(request.getParameter("inventoryQuantity"));

        Warehouse newWarehouse = new Warehouse(name, addressLine1, addressLine2, city, state, country, inventoryQuantity);

        response.setContentType("application/json; charset=utf-8");
        response.getWriter().write(objectMapper.writeValueAsString(Collections.singletonMap("message", "Warehouse added successfully")));
    }

    private void handleUpdate(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int warehouseId = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String addressLine1 = request.getParameter("addressLine1");
        String addressLine2 = request.getParameter("addressLine2");
        String city = request.getParameter("city");
        String state = request.getParameter("state");
        String country = request.getParameter("country");
        int inventoryQuantity = Integer.parseInt(request.getParameter("inventoryQuantity"));

        Warehouse existingWarehouse = warehouseDAO.getWarehouseById(warehouseId);

        if (existingWarehouse != null) {
            existingWarehouse.setName(name);
            existingWarehouse.setAddressLine1(addressLine1);
            existingWarehouse.setAddressLine2(addressLine2);
            existingWarehouse.setCity(city);
            existingWarehouse.setState(state);
            existingWarehouse.setCountry(country);
            existingWarehouse.setInventoryQuantity(inventoryQuantity);

            warehouseDAO.updateWarehouse(existingWarehouse);

            response.sendRedirect(request.getContextPath() + "/warehouses?action=view&id=" + warehouseId);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Warehouse not found");
        }
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int warehouseId = Integer.parseInt(request.getParameter("id"));
        warehouseDAO.deleteWarehouse(warehouseId);

        response.sendRedirect(request.getContextPath() + "/warehouses");
    }

    private void showWarehouseList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Warehouse> warehouses = warehouseDAO.getAllWarehouses();

        response.setContentType("application/json; charset=utf-8");
        response.getWriter().write(objectMapper.writeValueAsString(warehouses));
    }
}
