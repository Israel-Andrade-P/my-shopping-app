package com.zel92.inventory.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class ProductCheck {
    private final List<String> productIds;
    @Getter
    @Setter
    private Boolean isInStock;

    public ProductCheck(){
        productIds = new ArrayList<>();
    }

    public List<String> getProductIds(){
        return productIds;
    }
}
