package com.zel92.user.controller;

import com.zel92.user.constants.Constants;
import com.zel92.user.domain.Response;
import com.zel92.user.dto.request.UserRequest;
import com.zel92.user.dto.response.UserInfoResp;
import com.zel92.user.service.UserService;
import com.zel92.user.utils.RequestUtils;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static java.util.Collections.emptyMap;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@SecurityRequirement(name = Constants.SECURITY)
public class UserController {
    private final UserService userService;

    @GetMapping("/user/{user-id}")
//    @PostAuthorize("returnObject.email == authentication.principal.email")
    public UserInfoResp fetchById(@PathVariable("user-id") String userId){
        return userService.fetchById(userId);
    }

    @PutMapping("/update/{user-id}")
    public ResponseEntity<Response> updateUser(@PathVariable("user-id") String userId,
                                               @RequestBody UserRequest user,
                                               HttpServletRequest request){
        userService.updateUser(userId, user);
        return ResponseEntity.ok().body(RequestUtils.getResponse(request, emptyMap(), "User has been updated successfully!", OK));
    }

    @DeleteMapping("/delete/{user-id}")
    public ResponseEntity<Response> deleteUser(@PathVariable("user-id") String userId, HttpServletRequest request){
        userService.deleteUser(userId);
        return ResponseEntity.ok().body(RequestUtils.getResponse(request, emptyMap(), "User has been deleted successfully!", OK));
    }
}
