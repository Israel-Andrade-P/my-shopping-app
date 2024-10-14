package com.zel92.user.controller;

import com.zel92.user.domain.Response;
import com.zel92.user.dto.request.UserRequest;
import com.zel92.user.dto.response.UserResponse;
import com.zel92.user.service.AuthService;
import com.zel92.user.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static com.zel92.user.constants.Constants.ACCOUNT_CREATED_MESSAGE;
import static com.zel92.user.constants.Constants.ACCOUNT_VERIFIED_MESSAGE;
import static com.zel92.user.utils.RequestUtils.getResponse;
import static java.util.Collections.emptyMap;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<Response> registerUser(@RequestBody @Valid UserRequest user, HttpServletRequest request){
        authService.createUser(user);
        return ResponseEntity.created(getURI())
                .body(getResponse(request, emptyMap(), ACCOUNT_CREATED_MESSAGE, CREATED));
    }

    @GetMapping("/verify")
    public ResponseEntity<Response> verifyAccount(@RequestParam(name = "key") String key, HttpServletRequest request){
        authService.verifyAccount(key);
        return ResponseEntity.ok().body(getResponse(request,emptyMap(),ACCOUNT_VERIFIED_MESSAGE, OK));
    }

    @GetMapping("/validate")
    public Boolean validateJwt(@RequestParam(name = "token") String jwt){
       return jwtService.validateToken(jwt);
    }

    @GetMapping("/retrieve")
    public UserResponse retrieveUser(@RequestParam(name = "token") String token){
        return jwtService.retrieveUser(token);
    }

    private URI getURI() {
        return URI.create("");
    }
}
