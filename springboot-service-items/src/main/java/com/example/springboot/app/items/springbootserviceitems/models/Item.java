package com.example.springboot.app.items.springbootserviceitems.models;

import com.example.springboot.app.commons.models.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    private Product product;

    private Integer quantity;

    public Double getTotal() {

        return product.getPrice() * quantity.doubleValue();
    }
}
