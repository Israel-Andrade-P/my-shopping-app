package com.zel92.product.enumeration;

import lombok.Getter;
@Getter
public enum CategoryType {
    ELECTRONICS("Electronics"),
    CLOTHING("Clothing"),
    BOOKS("Books"),
    HOUSEHOLD_ESSENTIALS("Household Essentials"),
    BEAUTY_AND_PERSONAL_CARE("Beauty and Personal Care");

    private final String value;

    CategoryType(String value) {
        this.value = value;
    }
}
