package com.goit.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class Warehouse {
    private int id;
    private String name;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String country;
    private int inventoryQuantity;

    public Warehouse(String name, String addressLine1, String addressLine2, String city, String state, String country, int inventoryQuantity) {
        this.name = name;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.city = city;
        this.state = state;
        this.country = country;
        this.inventoryQuantity = inventoryQuantity;
    }
}
