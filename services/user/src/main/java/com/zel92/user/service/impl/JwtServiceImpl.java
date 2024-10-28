package com.zel92.user.service.impl;

import com.zel92.user.domain.Token;
import com.zel92.user.domain.TokenData;
import com.zel92.user.dto.response.UserResponse;
import com.zel92.user.enumeration.TokenType;
import com.zel92.user.exception.InvalidJwtException;
import com.zel92.user.model.User;
import com.zel92.user.security.JwtConfig;
import com.zel92.user.service.AuthService;
import com.zel92.user.service.JwtService;
import com.zel92.user.utils.UserUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.zel92.user.constants.Constants.*;
import static com.zel92.user.enumeration.TokenType.ACCESS;
import static com.zel92.user.enumeration.TokenType.REFRESH;
import static com.zel92.user.utils.UserUtils.*;
import static io.jsonwebtoken.Header.JWT_TYPE;
import static io.jsonwebtoken.Header.TYPE;
import static java.time.Instant.now;
import static java.util.Date.from;
import static org.springframework.security.core.authority.AuthorityUtils.commaSeparatedStringToAuthorityList;
@Service
@RequiredArgsConstructor
public class JwtServiceImpl extends JwtConfig implements JwtService {
    private final AuthService userService;

    private final Supplier<SecretKey> key = () -> Keys.hmacShaKeyFor(Decoders.BASE64.decode(getSecret()));

    private final Supplier<JwtBuilder> builder = () ->
            Jwts.builder()
                    .header().add(Map.of(TYPE, JWT_TYPE))
                    .and()
                    .audience().add(ZEL_CODING_INC)
                    .and()
                    .id(UUID.randomUUID().toString())
                    .issuedAt(from(now()))
                    .notBefore(new Date())
                    .signWith(key.get(), Jwts.SIG.HS512);

    private final BiFunction<User, TokenType, String> buildToken = (user, type) ->
            Objects.equals(type, ACCESS) ? builder.get()
                    .subject(user.getEmail())
                    .claim(AUTHORITIES, user.getAuthorities())
                    .claim(ROLE, user.getRole())
                    .expiration(from(now().plusSeconds(getExpiration())))
                    .compact() : builder.get()
                    .subject(user.getEmail())
                    .expiration(from(now().plusSeconds(getExpiration())))
                    .compact();

    private final Function<String, Claims> getClaims = token ->
            Jwts.parser()
                    .verifyWith(key.get())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

    private final Function<String, String> subject = token -> getClaimsValue(token, Claims::getSubject);

    private <T> T getClaimsValue(String token, Function<Claims, T> claims){
        return getClaims.andThen(claims).apply(token);
    }

    public Function<String, List<GrantedAuthority>> authorities = token ->
            commaSeparatedStringToAuthorityList(new StringJoiner(AUTHORITY_DELIMITER)
                    .add(getClaims.apply(token).get(AUTHORITIES, String.class))
                    .add(ROLE_PREFIX + getClaims.apply(token).get(ROLE, String.class)).toString());

    @Override
    public String createToken(User user, Function<Token, String> tokenFunction) {
        var token = Token.builder().access(buildToken.apply(user, ACCESS)).refresh(buildToken.apply(user, REFRESH)).build();
        return tokenFunction.apply(token);
    }

    @Override
    public <T> T getTokenData(String token, Function<TokenData, T> tokenDataFunction) {
        return tokenDataFunction.apply(
                TokenData.builder()
                        .valid(Objects.equals(userService.getUserByEmail(subject.apply(token)).getEmail(), getClaims.apply(token).getSubject()) &&
                                getClaimsValue(token, Claims::getExpiration).after(new Date()))
                        .authorities(authorities.apply(token))
                        .claims(getClaims.apply(token))
                        .user(userService.getUserByEmail(subject.apply(token)))
                        .build()
        );
    }
    @Override
    public Boolean validateToken(String jwt)
    {
        return getTokenData(jwt, TokenData::getValid);
    }

    @Override
    public UserResponse retrieveUser(String token) {
        var user = userService.getUserByEmail(subject.apply(token));
        var location = userService.getLocationByUserId(user.getId());
        return toUserResponse(user, location);
    }


}
