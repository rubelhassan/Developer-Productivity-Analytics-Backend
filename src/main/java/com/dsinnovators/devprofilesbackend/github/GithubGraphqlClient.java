package com.dsinnovators.devprofilesbackend.github;

import com.dsinnovators.devprofilesbackend.configurations.AppProperties;
import com.dsinnovators.devprofilesbackend.github.entities.GithubResponseData;
import com.dsinnovators.devprofilesbackend.github.entities.GithubUser;
import com.dsinnovators.devprofilesbackend.github.querymodels.DateRange;
import com.dsinnovators.devprofilesbackend.github.querymodels.GraphqlQuery;
import com.dsinnovators.devprofilesbackend.utils.GithubUserNotFound;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class GithubGraphqlClient {
    private static final String graphApiUrl = "https://api.github.com/graphql";

    private final RestTemplate restTemplate;

    private final AppProperties appProperties;

    public GithubUser fetchUserProfileWithContributions(String token) throws GithubUserNotFound {
        Map<String, String> params = clientParams();
        HttpEntity<String> request = buildRequestEntity(token);
        String result = this.restTemplate
                .exchange(graphApiUrl, HttpMethod.POST, request, String.class, params)
                .getBody();
        GithubResponseData responseData = null;
        try {
            responseData = new ObjectMapper().readValue(result, GithubResponseData.class);
        } catch (JsonProcessingException e) {
            log.error("Github response parsing error: ", e);
            throw new GithubUserNotFound("Github user could not be parsed");
        }
        // TODO: fetch if user has more repositories
        return responseData.getData().getUser();
    }

    private HttpEntity<String> buildRequestEntity(String token) {
        try {
            GraphqlQuery query = GraphqlQuery.builder()
                                .query(appProperties.getGithub_gpl_profile_query())
                                .variables(dateRangeOfThisWeekToJsonString())
                                .build();
            return new HttpEntity<>(new ObjectMapper().writeValueAsString(query), attachHeaders(token));
        } catch (JsonProcessingException e) {
            log.error("Failed to build Github request entity: ", e);
        }
        return null;
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
