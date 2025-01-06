package com.zel92.user.config.appinfo;

import com.zel92.user.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Endpoint(id = "app-roles")
@RequiredArgsConstructor
public class RoleEndpoint {
    private final RoleRepository roleRepository;

    @ReadOperation
    Map<String, Long> count(){
        return Map.of("count", roleRepository.count());
    }
}
