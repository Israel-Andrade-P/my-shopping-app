package com.zel92.order.dto;

public record OrderedItemRequest(
         String productId,
         Integer quantity
) {}
