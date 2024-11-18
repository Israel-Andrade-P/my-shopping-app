package com.zel92.order.service.impl;

import com.zel92.order.client.InventoryClient;
import com.zel92.order.client.PaymentClient;
import com.zel92.order.client.ProductClient;
import com.zel92.order.client.UserClient;
import com.zel92.order.dto.*;
import com.zel92.order.entity.OrderEntity;
import com.zel92.order.exception.AuthorizationFailedException;
import com.zel92.order.exception.OrderNotFoundException;
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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
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
        var userFuture = getUserFuture.apply(userClient, request);

        List<OrderedItemRequest> sorted = orderReq.orderedItems().stream().sorted(Comparator.comparing(OrderedItemRequest::productId)).toList();
        List<String> productIds = sorted.stream().map(OrderedItemRequest::productId).toList();
        List<Integer> quantities = sorted.stream().map(OrderedItemRequest::quantity).toList();
        var orderDTO = OrderDTO.builder().productIds(productIds).quantities(quantities).build();
            var productCheckFuture = callInvService(orderDTO);
            var productCheck = (ProductCheck) CompletableFuture.anyOf(productCheckFuture).join();
            if (!productCheck.getIsInStock()){
                throw new ProductNotAvailableException("Products with IDs: " + productCheck.getProductIds() + " are not available");
            }
           CompletableFuture<List<BigDecimal>> pricesFuture = callProdService(orderDTO);
           var prices = (List<BigDecimal>) CompletableFuture.anyOf(pricesFuture).join();
        var totalAmount = calculateTotalAmount.apply(prices, quantities);
        var user = (UserResponse) CompletableFuture.anyOf(userFuture).join();
        OrderEntity order = orderRepository.save(buildOrderEntity(user, totalAmount));

        persistOrderedItems(productIds, prices, quantities, order);

        paymentClient.persistTransaction(buildPaymentDTO(orderReq.paymentMethod(), totalAmount, user.fullName(), user.email()));
    }

    @Override
    public List<OrderResponse> findByUserId(String userId, HttpServletRequest request) {
        var user = getUser.apply(userClient, request);

        if (Objects.equals(user.userId(), userId)){
            var orders = orderRepository.findByUserId(userId);
            List<OrderResponse> orderResponses = new ArrayList<>();

            orders.forEach(order -> {
                List<OrderedItemResponse> items = itemRepository.findByOrder(order).stream().map(OrderUtils::toOrderedItemResponse).toList();
                var orderResponse = new OrderResponse(order.getOrderId(), order.getClientName(), order.getTotalAmount(), items);
                orderResponses.add(orderResponse);
            });

            return orderResponses;
        }
        throw new AuthorizationFailedException("You don't have enough permission for that");
    }

    @Override
    public OrderResponse findByOrderId(String orderId, HttpServletRequest request) {
        var user = getUser.apply(userClient, request);
        var order = orderRepository.findByOrderId(orderId).orElseThrow(() -> new OrderNotFoundException("Order not found"));


        if (Objects.equals(user.userId(), order.getUserId())){
            var items = itemRepository.findByOrder(order).stream().map(OrderUtils::toOrderedItemResponse).toList();
            return new OrderResponse(order.getOrderId(), order.getClientName(), order.getTotalAmount(), items);
        }
        throw new AuthorizationFailedException("You don't have enough permission");
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

    private final BiFunction<UserClient, HttpServletRequest, UserResponse> getUser = (client, request) -> {
        var jwt = request.getHeader(AUTHORIZATION).substring(7);
        return client.retrieveUser(jwt);
    };

    private final BiFunction<UserClient, HttpServletRequest, CompletableFuture<UserResponse>> getUserFuture = (client, request) ->
            CompletableFuture.supplyAsync(() -> {
            var jwt = request.getHeader(AUTHORIZATION).substring(7);
            return client.retrieveUser(jwt);
        });

    private CompletableFuture<ProductCheck> callInvService(OrderDTO orderDTO){
        return CompletableFuture.supplyAsync(() -> inventoryClient.checkStock(orderDTO));
    }

    private CompletableFuture<List<BigDecimal>> callProdService(OrderDTO orderDTO){
        return CompletableFuture.supplyAsync(() -> productClient.getPrice(orderDTO));
    }

}
