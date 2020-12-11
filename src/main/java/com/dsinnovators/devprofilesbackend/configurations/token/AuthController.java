package com.dsinnovators.devprofilesbackend.configurations.token;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class AuthController {
    private final JwtHelper jwtHelper;
    private final UserDetailsService userDetailsService;

    @PostMapping(path = "/token")
    public JwtHelper.TokenResponse generateToken(Principal principal) {
        UserDetails userDetails;
        try {
            userDetails = userDetailsService.loadUserByUsername(principal.getName());
        } catch (UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
        }

        Map<String, String> claims = new HashMap<>();
        claims.put("username", principal.getName());
        String authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        claims.put("authorities", authorities);
        claims.put("userId", String.valueOf(1));
        return jwtHelper.createJwtForClaims(principal.getName(), claims);
    }
}
