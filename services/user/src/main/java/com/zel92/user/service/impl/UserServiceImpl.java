package com.zel92.user.service.impl;

import com.zel92.user.domain.CustomAuthentication;
import com.zel92.user.dto.request.UserRequest;
import com.zel92.user.dto.response.UserInfoResp;
import com.zel92.user.dto.response.UserResponse;
import com.zel92.user.entity.LocationEntity;
import com.zel92.user.entity.RoleEntity;
import com.zel92.user.entity.UserEntity;
import com.zel92.user.exception.LocationNotFoundException;
import com.zel92.user.exception.RoleDoesntExistException;
import com.zel92.user.exception.UserNotFoundException;
import com.zel92.user.model.User;
import com.zel92.user.repository.LocationRepository;
import com.zel92.user.repository.RoleRepository;
import com.zel92.user.repository.UserRepository;
import com.zel92.user.service.UserService;
import com.zel92.user.utils.UserUtils;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static com.zel92.user.utils.UserUtils.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final LocationRepository locationRepository;
    @Override
    public void deleteUser(String userId) {
        UserEntity userEntity = getUserEntityByUserId(userId);
        if (checkAuthStatus(userId)){
            userRepository.delete(userEntity);
        }
        throw new AccessDeniedException("You don't have enough permission for that");
    }

    @Override
    public List<UserInfoResp> fetchAll() {
        return userRepository.findAll().stream().map(UserUtils::toUserInfoResp).toList();
    }

    @Override
    public UserInfoResp fetchById(String userId) {
        var user = getUserEntityByUserId(userId);
        if (checkAuthStatus(userId)){
            return UserUtils.toUserInfoResp(user);
        }

        throw new AccessDeniedException("You don't have enough permission for that");
    }



    @Override
    public void updateRole(String userId, String role) {
        var user = getUserEntityByUserId(userId);
        var roleEntity = getRole(role);
        user.setRole(roleEntity);
        user.getRole().setAuthority(roleEntity.getAuthority());
        userRepository.save(user);
    }

    @Override
    public void updateUser(String userId, UserRequest user) {
        var userDB = getUserEntityByUserId(userId);

        if (checkAuthStatus(userId)){
            if (StringUtils.isNotBlank(user.firstName())){
                userDB.setFirstName(user.firstName());
            }

            if (StringUtils.isNotBlank(user.lastName())){
                userDB.setLastName(user.lastName());
            }

            if (StringUtils.isNotBlank(user.email())){
                userDB.setEmail(user.email());
            }

            if (StringUtils.isNotBlank(user.telephone())){
                userDB.setTelephone(user.telephone());
            }

//        if (StringUtils.isNotBlank(user.dob().toString())){
//            userDB.setDob(user.dob());
//        }
            userRepository.save(userDB);
        } else throw new AccessDeniedException("You don't have enough permission for that");
    }

    private Boolean checkAuthStatus(String userId) {
        var user = getUserEntityByUserId(userId);
        var auth = (CustomAuthentication) SecurityContextHolder.getContext().getAuthentication();
        List<String> authorities = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        if (authorities.contains("user:delete")){
            return true;
        }else {
            if (Objects.equals(user.getEmail(), auth.getEmail())){
                return true;
            }
        }
        return false;
    }

    private UserEntity getUserEntityByUserId(String userId) {
        return userRepository.findByUserId(userId).orElseThrow(() -> new UserNotFoundException("Invalid user with id: " + userId));
    }

    private RoleEntity getRole(String role) {
        return roleRepository.findByRoleName(role)
                .orElseThrow(() -> new RoleDoesntExistException("This role doesn't exist"));
    }

    private LocationEntity getLocationByUserId(Long userId) {
        return locationRepository.findByUserId(userId).orElseThrow(() -> new LocationNotFoundException("Location not found"));
    }

}
