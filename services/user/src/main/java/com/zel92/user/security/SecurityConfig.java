package com.zel92.user.security;

import com.zel92.user.constants.Constants;
import com.zel92.user.filters.AuthFilter;
import com.zel92.user.filters.JwtCheckFilter;
import com.zel92.user.service.JwtService;
import com.zel92.user.service.AuthService;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static com.zel92.user.constants.Constants.WHITE_LIST;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@SecurityScheme(name = Constants.SECURITY, type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "bearer")
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthService authService, JwtService jwtService, CustomAuthenticationManager manager) throws Exception {
        http.authorizeHttpRequests(request ->
                request.requestMatchers(WHITE_LIST).permitAll()
                        .anyRequest().authenticated());
        http.csrf(AbstractHttpConfigurer::disable);
//        http.formLogin(form -> form.defaultSuccessUrl("/api/v1/auth/home", true));
//        http.cors(c -> c.configurationSource(corsConfigurationSource()));
//        http.oauth2Login(oauth -> oauth.defaultSuccessUrl("http://localhost:5173/dashboard", true));
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(new AuthFilter(manager, authService, jwtService), AuthorizationFilter.class);
        http.addFilterBefore(new JwtCheckFilter(authService, jwtService), AuthorizationFilter.class);
        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}

//@Bean
//public CorsConfigurationSource corsConfigurationSource(){
//    CorsConfiguration corsConfiguration = new CorsConfiguration();
//    corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:9000/**"));
//    corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
//    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//    source.registerCorsConfiguration("/**", corsConfiguration);
//    return source;
//}


