package com.dsinnovators.devprofilesbackend.github;

import com.dsinnovators.devprofilesbackend.github.querymodels.DateRange;
import com.dsinnovators.devprofilesbackend.github.querymodels.GraphqlQuery;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GithubGraphqlClient {
    private static final String graphApiUrl = "https://api.github.com/graphql";

    @Value("${github_client_id}")
    private String clientId;

    @Value("${github_client_secret}")
    private String clientSecret;

    @Value(
        "#{T(com.dsinnovators.devprofilesbackend.util.ResourceReader).readAsString('classpath:github/profile.graphql')}"
    )
    private String profile;

    private final RestTemplate restTemplate;

    public String fetchUserProfileWithContributions(String token) throws JsonProcessingException {
        Map<String, String> params = clientParams();
        ObjectMapper mapper = new ObjectMapper();
        GraphqlQuery query = GraphqlQuery.builder()
                .query(profile)
                .variables(dateRangeOfThisWeekToJsonString())
                .build();
        HttpEntity<String> request = new HttpEntity<>(mapper.writeValueAsString(query), attachHeaders(token));
        return this.restTemplate.exchange(graphApiUrl, HttpMethod.POST, request, String.class, params).getBody();
    }

    private String dateRangeOfThisWeekToJsonString() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        DateRange range = DateRange.builder()
                .fromDate(LocalDateTime.now().minusDays(6).format(DateTimeFormatter.ISO_DATE_TIME))
                .toDate(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                .build();
        return mapper.writeValueAsString(range);
    }

    private Map<String, String> clientParams() {
        Map<String, String> params = new HashMap<>();
        params.put("client_id", clientId);
        params.put("client_secret", clientSecret);
        return params;
    }

    private HttpHeaders attachHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        headers.set("Authorization", "Bearer " + token);
        return headers;
    }
}
