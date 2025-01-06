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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

import static com.zel92.user.utils.RequestUtils.*;
import static java.util.Collections.*;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('user:delete')")
@SecurityRequirement(name = Constants.SECURITY)
public class AdminController {
    private final UserService service;

    @GetMapping("/all")
    public ResponseEntity<List<UserInfoResp>> fetchAll(){
        return ResponseEntity.ok().body(service.fetchAll());
    }

    @GetMapping("/user/{user-id}")
    public ResponseEntity<UserInfoResp> fetchById(@PathVariable("user-id") String userId){
        return ResponseEntity.ok().body(service.fetchById(userId));
    }

    @GetMapping("/update/role")
    public ResponseEntity<Response> updateRole(@RequestParam(name = "user-id") String userId,
                                               @RequestParam(name = "role") String role,
                                               HttpServletRequest request){
        service.updateRole(userId, role);
        return ResponseEntity.ok().body(getResponse(request, emptyMap(), "Role has been updated", OK));
    }

    @PutMapping("/update/user/{user-id}")
    public ResponseEntity<Response> updateUser(@PathVariable("user-id") String userId,
                                               @RequestBody UserRequest user,
                                               HttpServletRequest request){
        service.updateUser(userId, user);
        return ResponseEntity.ok().body(RequestUtils.getResponse(request, emptyMap(), "User has been updated successfully!", OK));
    }

    @DeleteMapping("/delete/{user-id}")
    public ResponseEntity<Response> deleteUser(@PathVariable("user-id") String userId, HttpServletRequest request){
        service.deleteUser(userId);
        return ResponseEntity.ok().body(RequestUtils.getResponse(request, emptyMap(), "User has been deleted successfully!", OK));
    }
}
