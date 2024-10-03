package com.zel92.order.dto;

import java.util.List;

public record OrderRequest(
        List<OrderedItemRequest> orderedItems
) {
}
