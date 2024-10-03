package com.zel92.user.service.impl;

import com.zel92.user.exception.UserNotFoundException;
import com.zel92.user.repository.UserRepository;
import com.zel92.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Override
    public void deleteUser(String userId) {
        userRepository.findByUserId(userId).orElseThrow(() -> new UserNotFoundException("Invalid user with id: " + userId ));
    }
}
