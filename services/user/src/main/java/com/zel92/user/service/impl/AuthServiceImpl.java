package com.zel92.user.service.impl;

import com.zel92.user.cache.CacheStore;
import com.zel92.user.dto.request.UserRequest;
import com.zel92.user.entity.*;
import com.zel92.user.enumeration.LoginType;
import com.zel92.user.exception.*;
import com.zel92.user.kafka.UserProducer;
import com.zel92.user.model.User;
import com.zel92.user.repository.*;
import com.zel92.user.service.AuthService;
import com.zel92.user.utils.LocationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.function.Supplier;

import static com.zel92.user.constants.Constants.EXPIRATION;
import static com.zel92.user.utils.LocationUtils.*;
import static com.zel92.user.utils.UserUtils.*;
import static java.time.LocalDateTime.now;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
    private final UserRepository userRepository;
    private final CredentialRepository credentialRepository;
    private final ConfirmationRepository confirmationRepository;
    private final RoleRepository roleRepository;
    private final LocationRepository locationRepository;
    private final UserProducer producer;
    private final PasswordEncoder encoder;
    private final CacheStore<String, Integer> userCache;
    @Override
    public void createUser(UserRequest user) {
        UserEntity userEntity = userRepository.save(createNewUser(user));
        credentialRepository.save(new CredentialEntity(userEntity, encoder.encode(user.password())));
        ConfirmationEntity confirmation = confirmationRepository.save(new ConfirmationEntity(userEntity, suppliesKey.get()));
        locationRepository.save(buildLocation(user.location(), userEntity));

        sendMessageToBroker(userEntity.fullName(), user.email(), confirmation.getKeyCode());
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

    @Override
    public User getUserByEmail(String email) {
        return fromUserEntity(getUserEntityByEmail(email));
    }

    @Override
    public void updateLoginAttempt(String email, LoginType loginType) {
        var user = getUserEntityByEmail(email);
        switch (loginType){
            case LOGIN_ATTEMPT -> {
                if (userCache.get(user.getEmail()) == null){
                    user.setLoginAttempts(0);
                    user.setAccountNonLocked(true);
                }
                user.setLoginAttempts(user.getLoginAttempts() + 1);
                userCache.put(user.getEmail(), user.getLoginAttempts());
                if (userCache.get(user.getEmail()) > 5){
                    user.setAccountNonLocked(false);
                }
            }
            case LOGIN_SUCCESS -> {
                user.setLoginAttempts(0);
                user.setAccountNonLocked(true);
                user.setLastLogin(now());
                userCache.evict(user.getEmail());
            }
        }
        userRepository.save(user);
    }

    @Override
    public CredentialEntity getCredentialByUserId(Long userId) {
        return credentialRepository.findByUserId(userId).orElseThrow(() -> new UserNotFoundException("User with ID: " + userId + " not found"));
    }

    @Override
    public LocationEntity getLocationByUserId(Long userId) {
        return locationRepository.findByUserId(userId).orElseThrow(() -> new LocationNotFoundException("Location not found"));
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
