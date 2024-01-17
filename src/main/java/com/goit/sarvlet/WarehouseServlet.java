package com.goit.sarvlet;
/*
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goit.dao.WarehouseDAO1;
import com.goit.search.*;
import com.goit.model.Warehouse;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/warehouses")
public class WarehouseServlet extends HttpServlet {
    private WarehouseDAO1 warehouseDAO = new WarehouseDAO1();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("view".equals(action)) {
            viewWarehouse(request, response);
        } else if ("search".equals(action)) {
            searchWarehouses(request, response);
        } else if ("add".equals(action)) {
            showAddForm(request, response);
        } else if ("info".equals(action)) {
            showInfoPage(request, response);
        } else {
            // Виклик методу пошуку з параметрами за замовчуванням
            searchWarehouses(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("add".equals(action)) {
            addWarehouse(request, response);
        } else if ("update".equals(action)) {
            updateWarehouse(request, response);
        } else if ("delete".equals(action)) {
            deleteWarehouse(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void searchWarehouses(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Отримання параметрів пошуку
        String name = request.getParameter("name");
        String address = request.getParameter("address");
        String city = request.getParameter("city");
        String state = request.getParameter("state");
        String country = request.getParameter("country");
        int minInventory = parseIntParameter(request.getParameter("minInventory"), 0);
        int maxInventory = parseIntParameter(request.getParameter("maxInventory"), Integer.MAX_VALUE);
        String sortBy = request.getParameter("sortBy");
        int limit = parseIntParameter(request.getParameter("limit"), 0);
        int offset = parseIntParameter(request.getParameter("offset"), 0);

        // Виклик методу пошуку у DAO
        List<Warehouse> warehouses = warehouseDAO.searchWarehouses(name, address, city, state, country,
                minInventory, maxInventory, sortBy, limit, offset);

        SearchResult result = new SearchResult();
        result.setWarehouses(warehouses);
        result.setTotalCount(warehouses.size());

        // Генерація HTML-форми для пошуку
        String searchForm = "<h2>Search Warehouses</h2>\n" +
                "<form action=\"/warehouses\" method=\"GET\">\n" +
                "    <label for=\"name\">Name:</label>\n" +
                "    <input type=\"text\" id=\"name\" name=\"name\" value=\"" + name + "\">\n" +
                "    <label for=\"address\">Address:</label>\n" +
                "    <input type=\"text\" id=\"address\" name=\"address\" value=\"" + address + "\">\n" +
                "    <label for=\"city\">City:</label>\n" +
                "    <input type=\"text\" id=\"city\" name=\"city\" value=\"" + city + "\">\n" +
                "    <label for=\"state\">State:</label>\n" +
                "    <input type=\"text\" id=\"state\" name=\"state\" value=\"" + state + "\">\n" +
                "    <label for=\"country\">Country:</label>\n" +
                "    <input type=\"text\" id=\"country\" name=\"country\" value=\"" + country + "\">\n" +
                "    <label for=\"minInventory\">Min Inventory:</label>\n" +
                "    <input type=\"number\" id=\"minInventory\" name=\"minInventory\" value=\"" + minInventory + "\">\n" +
                "    <label for=\"maxInventory\">Max Inventory:</label>\n" +
                "    <input type=\"number\" id=\"maxInventory\" name=\"maxInventory\" value=\"" + maxInventory + "\">\n" +
                "    <label for=\"sortBy\">Sort By:</label>\n" +
                "    <select id=\"sortBy\" name=\"sortBy\">\n" +
                "        <option value=\"name\">Name</option>\n" +
                "        <option value=\"address\">Address</option>\n" +
                "        <!-- Додайте інші поля для сортування -->\n" +
                "    </select>\n" +
                "    <label for=\"limit\">Limit:</label>\n" +
                "    <input type=\"number\" id=\"limit\" name=\"limit\" value=\"" + limit + "\">\n" +
                "    <label for=\"offset\">Offset:</label>\n" +
                "    <input type=\"number\" id=\"offset\" name=\"offset\" value=\"" + offset + "\">\n" +
                "    <input type=\"hidden\" name=\"action\" value=\"search\">\n" +
                "    <input type=\"submit\" value=\"Search\">\n" +
                "</form>";

        // Вивід результатів пошуку
        response.setContentType("text/html");
        response.getWriter().write(searchForm);
        response.getWriter().write("<hr>");
        response.getWriter().write("<h2>Warehouses List</h2>");
        response.getWriter().write(new ObjectMapper().writeValueAsString(result.getWarehouses()));
        response.getWriter().write("<p>Total Warehouses: " + result.getTotalCount() + "</p>");
    }

    private void addWarehouse(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Отримання всіх параметрів для додавання складу
        String name = request.getParameter("name");
        String address = request.getParameter("address");
        String city = request.getParameter("city");
        String state = request.getParameter("state");
        String country = request.getParameter("country");
        int inventoryQuantity = parseIntParameter(request.getParameter("inventoryQuantity"), 0);

        // Створення нового об'єкту складу і додавання його у DAO
        Warehouse newWarehouse = new Warehouse(0, name, address, "", city, state, country, inventoryQuantity);
        warehouseDAO.addWarehouse(newWarehouse);

        // Редирект на сторінку додавання
        response.sendRedirect("/warehouses?action=add");
    }
    private void showAddForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Генерація HTML-форми для додавання
        String addForm = "<h2>Add Warehouse</h2>\n" +
                "<form action=\"/warehouses\" method=\"POST\">\n" +
                "    <label for=\"name\">Name:</label>\n" +
                "    <input type=\"text\" id=\"name\" name=\"name\" required>\n" +
                "    <br>\n" +
                "    <label for=\"addressLine1\">Address Line 1:</label>\n" +
                "    <input type=\"text\" id=\"addressLine1\" name=\"addressLine1\" required>\n" +
                "    <br>\n" +
                "    <!-- Додайте інші поля для додавання -->\n" +
                "    <br>\n" +
                "    <input type=\"submit\" name=\"action\" value=\"add\">\n" +
                "</form>";

        // Вивід форми додавання
        response.setContentType("text/html");
        response.getWriter().write(addForm);
    }
    private void viewWarehouse(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int warehouseId = parseIntParameter(request.getParameter("id"), -1);

        if (warehouseId != -1) {
            Warehouse warehouse = warehouseDAO.getWarehouseById(warehouseId);

            if (warehouse != null) {
                response.setContentType("text/html");
                response.getWriter().write("<h2>View Warehouse</h2>");
                response.getWriter().write(new ObjectMapper().writeValueAsString(warehouse));
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void showInfoPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Логіка для інформаційної сторінки
        String infoPage = "<h2>Info Page</h2>\n" +
                "<p>This is the information page.</p>";

        // Вивід сторінки
        response.setContentType("text/html");
        response.getWriter().write(infoPage);
    }
    private void deleteWarehouse(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int warehouseId = parseIntParameter(request.getParameter("id"), -1);

        if (warehouseId != -1) {
            Warehouse warehouseToDelete = warehouseDAO.getWarehouseById(warehouseId);

            if (warehouseToDelete != null) {
                warehouseDAO.deleteWarehouse(warehouseId);

                // HTML-форма для видалення
                String deleteForm = "<h2>Delete Warehouse</h2>\n" +
                        "<form action=\"/warehouses\" method=\"POST\">\n" +
                        "    <!-- Додайте приховані поля для видалення -->\n" +
                        "    <input type=\"hidden\" name=\"action\" value=\"delete\">\n" +
                        "    <input type=\"submit\" value=\"Delete\">\n" +
                        "</form>";

                // Вивід результатів видалення
                response.setContentType("text/html");
                response.getWriter().write(deleteForm);
                response.getWriter().write("<hr>");
                response.getWriter().write("<h2>Warehouse deleted successfully!</h2>");
                response.getWriter().write(new ObjectMapper().writeValueAsString(warehouseToDelete));
                return;
            }
        }

        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
    private void updateWarehouse(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int warehouseId = parseIntParameter(request.getParameter("id"), -1);

        if (warehouseId != -1) {
            Warehouse existingWarehouse = warehouseDAO.getWarehouseById(warehouseId);

            if (existingWarehouse != null) {
                String name = request.getParameter("name");
                String addressLine1 = request.getParameter("addressLine1");
                String addressLine2 = request.getParameter("addressLine2");
                String city = request.getParameter("city");
                String state = request.getParameter("state");
                String country = request.getParameter("country");
                int inventoryQuantity = parseIntParameter(request.getParameter("inventoryQuantity"), 0);

                existingWarehouse.setName(name);
                existingWarehouse.setAddressLine1(addressLine1);
                existingWarehouse.setAddressLine2(addressLine2);
                existingWarehouse.setCity(city);
                existingWarehouse.setState(state);
                existingWarehouse.setCountry(country);
                existingWarehouse.setInventoryQuantity(inventoryQuantity);

                warehouseDAO.updateWarehouse(existingWarehouse);

                // HTML-форма для оновлення
                String updateForm = "<h2>Update Warehouse</h2>\n" +
                        "<form action=\"/warehouses\" method=\"POST\">\n" +
                        "    <!-- Додайте приховані поля для оновлення -->\n" +
                        "    <input type=\"hidden\" name=\"action\" value=\"update\">\n" +
                        "    <input type=\"submit\" value=\"Update\">\n" +
                        "</form>";

                // Вивід результатів оновлення
                response.setContentType("text/html");
                response.getWriter().write(updateForm);
                response.getWriter().write("<hr>");
                response.getWriter().write("<h2>Warehouse updated successfully!</h2>");
                response.getWriter().write(new ObjectMapper().writeValueAsString(existingWarehouse));
                return;
            }
        }

        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    private int parseIntParameter(String param, int defaultValue) {
        try {
            return Integer.parseInt(param);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
*/