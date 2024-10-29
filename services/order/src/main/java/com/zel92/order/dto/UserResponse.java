package com.zel92.order.dto;

public record UserResponse(String userId, String fullName, String email, String role, String country, String city, String street, String zipCode) {
}
