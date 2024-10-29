package com.zel92.order.controller;

import com.zel92.order.domain.Response;
import com.zel92.order.dto.OrderRequest;
import com.zel92.order.dto.OrderResponse;
import com.zel92.order.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.zel92.order.constants.Constants.ORDER_PLACED;
import static com.zel92.order.utils.RequestUtils.getResponse;
import static java.util.Collections.emptyMap;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService service;

    @PostMapping("/order")
    public ResponseEntity<Response> placeOrder(@RequestBody @Valid OrderRequest order, HttpServletRequest request){
        service.placeOrder(order, request);
        return ResponseEntity.ok().body(getResponse(request, emptyMap(), ORDER_PLACED, OK));
    }

    @GetMapping("/search")
    public ResponseEntity<OrderResponse> findByOrderId(@RequestParam(name = "order") String orderId, HttpServletRequest request){
        return ResponseEntity.ok().body(service.findByOrderId(orderId, request));
    }

    @GetMapping("/search/all/{user-id}")
    public ResponseEntity<List<OrderResponse>> findByUserId(@PathVariable("user-id") String userId,
                                                                HttpServletRequest request){
        return ResponseEntity.ok().body(service.findByUserId(userId, request));
    }
}
