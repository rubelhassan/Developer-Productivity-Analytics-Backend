package com.dsinnovators.devprofilesbackend.github;

import com.dsinnovators.devprofilesbackend.configurations.AppProperties;
import com.dsinnovators.devprofilesbackend.github.entities.GithubResponseData;
import com.dsinnovators.devprofilesbackend.github.querymodels.DateRange;
import com.dsinnovators.devprofilesbackend.github.querymodels.GraphqlQuery;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
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

    private final RestTemplate restTemplate;

    private final AppProperties appProperties;

    public GithubResponseData fetchUserProfileWithContributions(String token) throws JsonProcessingException {
        Map<String, String> params = clientParams();
        ObjectMapper mapper = new ObjectMapper();
        GraphqlQuery query = GraphqlQuery.builder()
                .query(appProperties.getGithub_gpl_profile_query())
                .variables(dateRangeOfThisWeekToJsonString())
                .build();
        HttpEntity<String> request = new HttpEntity<>(mapper.writeValueAsString(query), attachHeaders(token));
        return new ObjectMapper().readValue(
                this.restTemplate
                    .exchange(graphApiUrl, HttpMethod.POST, request, String.class, params)
                    .getBody(),
                GithubResponseData.class);
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
        params.put("client_id", appProperties.getClientId());
        params.put("client_secret", appProperties.getClientSecret());
        return params;
    }

    private HttpHeaders attachHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        headers.set("Authorization", "Bearer " + token);
        return headers;
    }
}
