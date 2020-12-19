package com.dsinnovators.devprofilesbackend.configurations;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@RequiredArgsConstructor
public class AppProperties {
    @Value("${github_client_id}")
    private String clientId;

    @Value("${github_client_secret}")
    private String clientSecret;

    @Value("${dsi-client-id}")
    private String dsiClientId;

    @Value("${dsi-client-secret}")
    private String dsiClientSecret;

    @Value(
       "#{T(com.dsinnovators.devprofilesbackend.utils.ResourceReader).readAsString('classpath:github/profile.graphql')}"
    )
    private String github_gpl_profile_query;

    @Value(
            "#{T(com.dsinnovators.devprofilesbackend.utils.ResourceReader).readAsString('classpath:github/public_profile.graphql')}"
    )
    private String github_gpl_public_profile_query;
}
