package com.goit.sarvlet;

import com.goit.dao.WarehouseDAO;
import com.goit.dao.impl.WarehouseDAOImpl;
import com.goit.model.Warehouse;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/warehouses")
public class WarehouseServlet2 extends HttpServlet {
    private WarehouseDAO warehouseDAO = new WarehouseDAOImpl();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("search".equals(action)) {
            handleSearch(request, response);
        } else if ("view".equals(action)) {
            handleView(request, response);
        } else if ("add".equals(action)) {
            handleAdd(request, response);
        } else if ("update".equals(action)) {
            handleUpdate(request, response);
        } else if ("delete".equals(action)) {
            handleDelete(request, response);
        } else {
            // Показати загальну сторінку складів
            showWarehouseList(request, response);
        }
    }

    private void handleSearch(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Отримання параметрів з запиту
            String name = request.getParameter("name");
            String address = request.getParameter("address");
            String city = request.getParameter("city");
            String state = request.getParameter("state");
            String country = request.getParameter("country");
            String inventoryQuantityParam = request.getParameter("inventoryQuantity");
            String limitParam = request.getParameter("limit");
            String offsetParam = request.getParameter("offset");

            // Перевірка і конвертація параметрів в числа
            int inventoryQuantity = (inventoryQuantityParam != null) ? Integer.parseInt(inventoryQuantityParam) : 0;
            int limit = (limitParam != null) ? Integer.parseInt(limitParam) : 10; // Призначте значення за замовчуванням
            int offset = (offsetParam != null) ? Integer.parseInt(offsetParam) : 0;

            // Логіка пошуку в сервісі
            List<Warehouse> warehouses = warehouseDAO.searchWarehouses(name, address, city, state, country, inventoryQuantity, limit, offset);
            int totalWarehouses = warehouseDAO.getTotalWarehouses(name, address, city, state, country, inventoryQuantity);

            // Передача результатів на сторінку
            request.setAttribute("warehouses", warehouses);
            request.setAttribute("totalWarehouses", totalWarehouses);
            request.setAttribute("limit", limit);
            request.setAttribute("offset", offset);

            // Показати сторінку результатів пошуку
            RequestDispatcher dispatcher = request.getRequestDispatcher("/search-results.html");
            dispatcher.forward(request, response);
        } catch (NumberFormatException e) {
            // Обробка помилки, коли не вдалося конвертувати рядок в число
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid numeric parameter");
        }
    }


    private void handleView(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int warehouseId = Integer.parseInt(request.getParameter("id"));
        Warehouse warehouse = warehouseDAO.getWarehouseById(warehouseId);

        // Передача даних на сторінку
        request.setAttribute("warehouse", warehouse);

        // Показати сторінку перегляду складу
        RequestDispatcher dispatcher = request.getRequestDispatcher("/view-warehouse.html");
        dispatcher.forward(request, response);
    }

    private void handleAdd(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Отримання даних для нового складу з параметрів запиту
        String name = request.getParameter("name");
        String addressLine1 = request.getParameter("addressLine1");
        String addressLine2 = request.getParameter("addressLine2");
        String city = request.getParameter("city");
        String state = request.getParameter("state");
        String country = request.getParameter("country");
        int inventoryQuantity = Integer.parseInt(request.getParameter("inventoryQuantity"));

        // Створення нового складу
        Warehouse newWarehouse = new Warehouse(name, addressLine1, addressLine2, city, state, country, inventoryQuantity);

        // Додавання нового складу в базу даних
        warehouseDAO.addWarehouse(newWarehouse);

        // Перенаправлення на головну сторінку складів
        response.sendRedirect(request.getContextPath() + "/warehouses");
    }

    private void handleUpdate(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Отримання даних для оновлення складу з параметрів запиту
        int warehouseId = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String addressLine1 = request.getParameter("addressLine1");
        String addressLine2 = request.getParameter("addressLine2");
        String city = request.getParameter("city");
        String state = request.getParameter("state");
        String country = request.getParameter("country");
        int inventoryQuantity = Integer.parseInt(request.getParameter("inventoryQuantity"));

        // Отримання існуючого складу з бази даних
        Warehouse existingWarehouse = warehouseDAO.getWarehouseById(warehouseId);

        // Перевірка, чи склад існує
        if (existingWarehouse != null) {
            // Оновлення даних складу
            existingWarehouse.setName(name);
            existingWarehouse.setAddressLine1(addressLine1);
            existingWarehouse.setAddressLine2(addressLine2);
            existingWarehouse.setCity(city);
            existingWarehouse.setState(state);
            existingWarehouse.setCountry(country);
            existingWarehouse.setInventoryQuantity(inventoryQuantity);

            // Збереження оновленого складу в базі даних
            warehouseDAO.updateWarehouse(existingWarehouse);

            // Перенаправлення на сторінку перегляду оновленого складу
            response.sendRedirect(request.getContextPath() + "/warehouses?action=view&id=" + warehouseId);
        } else {
            // Склад не знайдено
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Warehouse not found");
        }
    }


    private void handleDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Отримання ID складу для видалення
        int warehouseId = Integer.parseInt(request.getParameter("id"));

        // Видалення складу з бази даних
        warehouseDAO.deleteWarehouse(warehouseId);

        // Перенаправлення на головну сторінку складів
        response.sendRedirect(request.getContextPath() + "/warehouses");
    }

    private void showWarehouseList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Отримання всіх складів
        List<Warehouse> warehouses = warehouseDAO.getAllWarehouses();

        // Передача даних на сторінку
        request.setAttribute("warehouses", warehouses);

        // Показати головну сторінку складів
        RequestDispatcher dispatcher = request.getRequestDispatcher("/warehouse-list.jsp");
        dispatcher.forward(request, response);
    }
}

