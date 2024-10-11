package com.zel92.product.controller;

import com.zel92.product.domain.Response;
import com.zel92.product.dto.OrderDTO;
import com.zel92.product.dto.request.ProductRequest;
import com.zel92.product.dto.response.ProductResponse;
import com.zel92.product.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

import static com.zel92.product.constants.Constants.PRODUCT_ADDED;
import static com.zel92.product.constants.Constants.PRODUCT_DELETED;
import static com.zel92.product.utils.RequestUtils.getResponse;
import static java.util.Collections.emptyMap;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService service;

    @PostMapping("/add")
    public ResponseEntity<Response> addProduct(@RequestBody @Valid ProductRequest product, HttpServletRequest request){
        service.createProduct(product);
        return ResponseEntity.created(getUri()).body(getResponse(request, emptyMap(), PRODUCT_ADDED, CREATED));
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable("id") String productId){
        return ResponseEntity.ok().body(service.findByProductId(productId));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> deleteProductById(@PathVariable("id") String productId, HttpServletRequest request){
        service.deleteProduct(productId);
        return ResponseEntity.ok().body(getResponse(request, emptyMap(), PRODUCT_DELETED, OK));
    }

    @PostMapping("/price")
    public List<BigDecimal> getPrice(@RequestBody OrderDTO orderDTO){
        return service.getPrice(orderDTO);
    }

    private URI getUri(){
        return URI.create("");
    }
}
