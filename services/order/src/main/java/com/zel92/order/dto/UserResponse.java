package com.zel92.order.dto;

public record UserResponse(String fullName, String email, String country, String city, String street, String zipCode) {
}
