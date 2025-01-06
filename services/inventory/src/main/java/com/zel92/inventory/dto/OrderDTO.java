package com.zel92.inventory.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record OrderDTO(List<String> productIds, List<Integer> quantities) {
}
