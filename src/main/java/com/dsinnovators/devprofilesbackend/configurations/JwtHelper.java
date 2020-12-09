package com.dsinnovators.devprofilesbackend.configurations;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtHelper {
    private final RSAPrivateKey privateKey;
    private final RSAPublicKey publicKey;

    public TokenResponse createJwtForClaims(String subject, Map<String, String> claims) {
        // TODO: fix time zone and token expiration time (5 minutes for now)
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Instant.now().toEpochMilli());
        calendar.add(Calendar.SECOND, 300);
        JWTCreator.Builder jwtBuilder = JWT.create().withSubject(subject);
        claims.forEach(jwtBuilder::withClaim);
        return
            new TokenResponse(
                jwtBuilder
                    .withNotBefore(new Date())
                    .withExpiresAt(calendar.getTime())
                    .sign(Algorithm.RSA256(publicKey, privateKey)),
                calendar.getTime()
        );
    }

    @Data
    @AllArgsConstructor
    public static class TokenResponse {
        @NonNull
        private String token;

        private Date expiresAt;
    }
}
