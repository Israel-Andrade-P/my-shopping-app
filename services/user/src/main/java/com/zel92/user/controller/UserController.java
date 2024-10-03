package com.zel92.user.controller;

import com.zel92.user.domain.Response;
import com.zel92.user.service.UserService;
import com.zel92.user.utils.RequestUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static java.util.Collections.emptyMap;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @DeleteMapping("/delete/{user-id}")
    public ResponseEntity<Response> deleteUser(@PathVariable("user-id") String userId, HttpServletRequest request){
        userService.deleteUser(userId);
        return ResponseEntity.ok().body(RequestUtils.getResponse(request, emptyMap(), "User has been deleted successfully!", OK));
    }
}
