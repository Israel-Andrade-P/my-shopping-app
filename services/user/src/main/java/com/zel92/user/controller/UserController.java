package com.zel92.user.controller;

import com.zel92.user.domain.Response;
import com.zel92.user.dto.response.UserResponse;
import com.zel92.user.service.UserService;
import com.zel92.user.utils.RequestUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.Collections.emptyMap;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/all")
    //@PreAuthorize()
    public ResponseEntity<List<UserResponse>> fetchAll(){
        return ResponseEntity.ok().body(userService.fetchAll());
    }

    @DeleteMapping("/delete/{user-id}")
    public ResponseEntity<Response> deleteUser(@PathVariable("user-id") String userId, HttpServletRequest request){
        userService.deleteUser(userId);
        return ResponseEntity.ok().body(RequestUtils.getResponse(request, emptyMap(), "User has been deleted successfully!", OK));
    }
}
