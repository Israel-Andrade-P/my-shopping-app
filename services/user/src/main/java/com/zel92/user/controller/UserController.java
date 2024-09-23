package com.zel92.user.controller;

import com.zel92.user.domain.Response;
import com.zel92.user.dto.request.UserRequest;
import com.zel92.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static com.zel92.user.utils.RequestUtils.getResponse;
import static java.util.Collections.emptyMap;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Response> registerUser(@RequestBody @Valid UserRequest user, HttpServletRequest request){
        userService.createUser(user);
        return ResponseEntity.created(getURI())
                .body(getResponse(request, emptyMap(),"Your account has been created successfully. Please check your email to enable your account.", CREATED));
    }

    @GetMapping("/verify")
    public ResponseEntity<Response> verifyAccount(@RequestParam(name = "key") String key, HttpServletRequest request){
        userService.verifyAccount(key);
        return ResponseEntity.ok().body(getResponse(request,emptyMap(),"Your account has been verified", OK));
    }

    private URI getURI() {
        return URI.create("");
    }
}
