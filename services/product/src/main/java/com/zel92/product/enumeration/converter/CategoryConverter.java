package com.zel92.product.enumeration.converter;

import com.zel92.product.enumeration.CategoryType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class CategoryConverter implements AttributeConverter<CategoryType, String> {
    @Override
    public String convertToDatabaseColumn(CategoryType category) {
        if (category == null){
            return null;
        }
        return category.getValue();
    }

    @Override
    public CategoryType convertToEntityAttribute(String value) {
        if (value == null){
            return null;
        }
       return Stream.of(CategoryType.values())
               .filter(category -> category.getValue().equals(value))
               .findFirst()
               .orElseThrow(IllegalArgumentException::new);
    }
}
