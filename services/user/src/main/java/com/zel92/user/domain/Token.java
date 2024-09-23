package com.zel92.user.domain;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Token {
    private String access;
    private String refresh;
}
