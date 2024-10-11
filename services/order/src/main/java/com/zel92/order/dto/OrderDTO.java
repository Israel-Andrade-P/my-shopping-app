package com.zel92.order.dto;

import lombok.Builder;

import java.util.List;
@Builder
public record OrderDTO(List<String> productIds, List<Integer> quantities) {
}