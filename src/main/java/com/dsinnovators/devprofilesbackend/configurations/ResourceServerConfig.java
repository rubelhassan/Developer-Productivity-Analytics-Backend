package com.dsinnovators.devprofilesbackend.configurations;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class ResourceServerConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // TODO: remove this line to work with AS
        http.authorizeRequests().mvcMatchers("/**").permitAll();

        http
            .mvcMatcher("/**")
                .authorizeRequests()
                .mvcMatchers("/profiles/**").access("hasAuthority('SCOPE_api.read')")
                .mvcMatchers("/**").access("hasAuthority('SCOPE_api.write')")
            .and()
            .oauth2ResourceServer()
                .jwt();
    }
}
