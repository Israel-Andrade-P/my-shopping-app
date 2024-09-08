package com.zel92.user.controller;

import com.zel92.user.domain.Response;
import com.zel92.user.dto.request.UserRequest;
import com.zel92.user.service.UserService;
import com.zel92.user.utils.ResponseUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import static com.zel92.user.utils.ResponseUtils.*;
import static java.util.Collections.emptyMap;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Response> registerUser(@RequestBody UserRequest user, HttpServletRequest request){
        userService.createUser(user);
        return ResponseEntity.created(getURI())
                .body(getResponse(CREATED, request, "Your account has been created successfully. Please check your email to enable your account.", emptyMap()));
    }

    private URI getURI() {
        return URI.create("");
    }
}
