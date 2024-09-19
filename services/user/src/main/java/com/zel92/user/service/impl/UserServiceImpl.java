package com.zel92.user.service.impl;

import com.zel92.user.dto.request.UserRequest;
import com.zel92.user.entity.ConfirmationEntity;
import com.zel92.user.entity.CredentialEntity;
import com.zel92.user.entity.RoleEntity;
import com.zel92.user.entity.UserEntity;
import com.zel92.user.exception.ConfirmationKeyExpiredException;
import com.zel92.user.exception.CustomInvalidKeyException;
import com.zel92.user.exception.RoleDoesntExistException;
import com.zel92.user.exception.UserNotFoundException;
import com.zel92.user.kafka.UserProducer;
import com.zel92.user.repository.*;
import com.zel92.user.service.UserService;
import com.zel92.user.utils.LocationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.function.Supplier;

import static com.zel92.user.constants.Constants.EXPIRATION;
import static com.zel92.user.utils.UserUtils.buildAccVerificationEvent;
import static com.zel92.user.utils.UserUtils.buildUserEntity;
import static java.time.LocalDateTime.now;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CredentialRepository credentialRepository;
    private final ConfirmationRepository confirmationRepository;
    private final RoleRepository roleRepository;
    private final LocationRepository locationRepository;
    private final UserProducer producer;

    @Override
    public void createUser(UserRequest user) {
        UserEntity userEntity = userRepository.save(createNewUser(user));
        credentialRepository.save(new CredentialEntity(userEntity, user.password()));
        ConfirmationEntity confirmation = confirmationRepository.save(new ConfirmationEntity(userEntity, suppliesKey.get()));
        locationRepository.save(LocationUtils.buildLocation(user.location(), userEntity));

        sendMessageToBroker(userEntity.fullName(), user.email(), confirmation.getKey());
    }
    @Override
    public void verifyAccount(String key) {
        var confirmationEntity = getConfirmationByKey(key);
        isKeyValid(confirmationEntity);
        var user = getUserEntityByEmail(confirmationEntity.getUser().getEmail());
        user.setEnabled(true);
        userRepository.save(user);
        confirmationRepository.delete(confirmationEntity);
    }
    private void isKeyValid(ConfirmationEntity confirmationEntity) {
        if (confirmationEntity.getCreatedAt().plusMinutes(EXPIRATION).isBefore(now())){
            confirmationRepository.delete(confirmationEntity);
            throw new ConfirmationKeyExpiredException("The confirmation key is expired. Please request a new key");
        }
    }
    private UserEntity getUserEntityByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }
    private ConfirmationEntity getConfirmationByKey(String key) {
       return confirmationRepository.findByKey(key)
                .orElseThrow(() -> new CustomInvalidKeyException("The key is invalid"));
    }
    private UserEntity createNewUser(UserRequest user) {
        var role = getRole();
        return buildUserEntity(user, role);
    }
    private RoleEntity getRole() {
        return roleRepository.findByRoleName("USER")
                .orElseThrow(() -> new RoleDoesntExistException("This role doesn't exist"));
    }

    private void sendMessageToBroker(String userFullName, String email, String key) {
        var accountVerificationDto = buildAccVerificationEvent(userFullName, email, key);
        producer.sendAccountVerificationMessage(accountVerificationDto);
    }

    private final Supplier<String> suppliesKey = () -> {
        String pool = "0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 6; i++){
            int randomIndex = random.nextInt(pool.length());
            builder.append(pool.charAt(randomIndex));
        }
        return builder.toString();
    };
}
