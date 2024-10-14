package com.zel92.user.model;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    private Long id;
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate dob;
    private Integer loginAttempts;
    private LocalDateTime lastLogin;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean enabled;
    private String role;
    private String authorities;

    public String fullName(){
        return firstName + " " + lastName;
    }
}
