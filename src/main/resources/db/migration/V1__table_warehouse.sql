CREATE TABLE warehouse (
                            id SERIAL PRIMARY KEY,
                            name VARCHAR(50) NOT NULL,
                            address_line_1 VARCHAR(100) NOT NULL,
                            address_line_2 VARCHAR(100),
                            city VARCHAR(50) NOT NULL,
                            state VARCHAR(50) NOT NULL,
                            country VARCHAR(50) NOT NULL,
                            inventory_quantity INT NOT NULL DEFAULT 0
);
INSERT INTO warehouse (name, address_line_1, address_line_2, city, state, country, inventory_quantity)
VALUES
    ('Warehouse1', 'Address1', 'Address2', 'City1', 'State1', 'Country1', 100),
    ('Warehouse2', 'Address3', 'Address4', 'City2', 'State2', 'Country2', 150);
