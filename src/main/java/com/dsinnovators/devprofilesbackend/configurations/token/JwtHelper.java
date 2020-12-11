package com.dsinnovators.devprofilesbackend.configurations.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtHelper {
    private final RSAPrivateKey privateKey;
    private final RSAPublicKey publicKey;

    @Value("${app.security.jwt.token-lifetime-sec}")
    private Integer tokenLifetime;

    public TokenResponse createJwtForClaims(String subject, Map<String, String> claims) {
        Instant now = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        Instant expireAt = now.plus(tokenLifetime, ChronoUnit.SECONDS);
        JWTCreator.Builder jwtBuilder = JWT.create()
                .withSubject(subject)
                .withIssuedAt(Date.from(now))
                .withNotBefore(Date.from(now))
                .withExpiresAt(Date.from(expireAt));
        claims.forEach(jwtBuilder::withClaim);
        return new TokenResponse(jwtBuilder.sign(Algorithm.RSA256(publicKey, privateKey)), Date.from(expireAt));
    }

    @Data
    @AllArgsConstructor
    public static class TokenResponse {
        @NonNull
        private String token;

        private Date expiresAt;
    }
}
