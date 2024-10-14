package com.zel92.order.controller;

import com.zel92.order.domain.Response;
import com.zel92.order.dto.OrderRequest;
import com.zel92.order.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
