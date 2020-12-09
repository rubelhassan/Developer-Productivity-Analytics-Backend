package com.dsinnovators.devprofilesbackend.configurations;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;

@EnableWebSecurity
public class ResourceServerConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // TODO: remove this line to work with AS
        http.authorizeRequests().mvcMatchers("/**").permitAll();

        http
            .authorizeRequests(configurer -> configurer
                    .mvcMatchers("/profiles/**").access("hasAuthority('SCOPE_api.read')")
                    .mvcMatchers("/**").access("hasAuthority('SCOPE_api.write')"))
            .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
    }
}
