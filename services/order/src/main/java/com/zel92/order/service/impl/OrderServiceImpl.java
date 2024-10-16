package com.zel92.order.service.impl;

import com.zel92.order.client.InventoryClient;
import com.zel92.order.client.PaymentClient;
import com.zel92.order.client.ProductClient;
import com.zel92.order.client.UserClient;
import com.zel92.order.dto.OrderDTO;
import com.zel92.order.dto.OrderRequest;
import com.zel92.order.dto.OrderedItemRequest;
import com.zel92.order.dto.PaymentDTO;
import com.zel92.order.entity.OrderEntity;
import com.zel92.order.exception.ProductNotAvailableException;
import com.zel92.order.repository.OrderRepository;
import com.zel92.order.repository.OrderedItemRepository;
import com.zel92.order.service.OrderService;
import com.zel92.order.utils.OrderUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.hc.core5.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;

import static com.zel92.order.utils.OrderUtils.*;
import static org.apache.hc.core5.http.HttpHeaders.*;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderedItemRepository itemRepository;
    private final InventoryClient inventoryClient;
    private final ProductClient productClient;
    private final UserClient userClient;
    private final PaymentClient paymentClient;

    @Override
    public void placeOrder(OrderRequest orderReq, HttpServletRequest request) {
        var jwt = request.getHeader(AUTHORIZATION).substring(7);
        var user = userClient.retrieveUser(jwt);

        List<OrderedItemRequest> sorted = orderReq.orderedItems().stream().sorted(Comparator.comparing(OrderedItemRequest::productId)).toList();
        List<String> productIds = sorted.stream().map(OrderedItemRequest::productId).toList();
        List<Integer> quantities = sorted.stream().map(OrderedItemRequest::quantity).toList();
        var orderDTO = OrderDTO.builder().productIds(productIds).quantities(quantities).build();
            var productCheck = inventoryClient.checkStock(orderDTO);
            if (!productCheck.getIsInStock()){
                throw new ProductNotAvailableException("Products with IDs: " + productCheck.getProductIds() + " are not available");
            }
        List<BigDecimal> prices = productClient.getPrice(orderDTO);
        var totalAmount = calculateTotalAmount.apply(prices, quantities);
        OrderEntity order = orderRepository.save(buildOrderEntity(user, totalAmount));

        persistOrderedItems(productIds, prices, quantities, order);

        paymentClient.persistTransaction(buildPaymentDTO(orderReq.paymentMethod(), totalAmount, user.fullName(), user.email()));
    }

    private PaymentDTO buildPaymentDTO(String method, BigDecimal totalAmount, String name, String email) {
        return new PaymentDTO(method, totalAmount, name, email);
    }

    private void persistOrderedItems(List<String> productIds, List<BigDecimal> prices, List<Integer> quantities, OrderEntity order){
        for (int i = 0; i < productIds.size(); i++){
            var productId = productIds.get(i);
            var price = prices.get(i);
            var quantity = quantities.get(i);
            itemRepository.save(buildOrderedItem(productId, order, price, quantity));
        }
    }

    private final BiFunction<List<BigDecimal>, List<Integer>, BigDecimal> calculateTotalAmount = (prices, quantities) -> {
        var totalAmount = BigDecimal.ZERO;
        for (int i = 0; i < prices.size(); i++){
            var quantity = quantities.get(i);
            var price = prices.get(i);
            totalAmount = totalAmount.add(price.multiply(BigDecimal.valueOf(quantity)));
        }
        return totalAmount;
    };
}
