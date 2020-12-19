package com.dsinnovators.devprofilesbackend.github;

import com.dsinnovators.devprofilesbackend.configurations.AppProperties;
import com.dsinnovators.devprofilesbackend.github.entities.GithubResponseData;
import com.dsinnovators.devprofilesbackend.github.entities.GithubUser;
import com.dsinnovators.devprofilesbackend.github.querymodels.DateRange;
import com.dsinnovators.devprofilesbackend.github.querymodels.DateRangeWithUserName;
import com.dsinnovators.devprofilesbackend.github.querymodels.GraphqlQuery;
import com.dsinnovators.devprofilesbackend.utils.GithubUserNotFound;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
    private static final String tokenAccessUrl = "https://github.com/login/oauth/access_token";

    private final RestTemplate restTemplate;

    private final AppProperties appProperties;

    public GithubUser fetchUserProfileWithContributions(String token) throws GithubUserNotFound {
        return fetchUserProfile(token, null);
    }

    private GithubUser fetchUserProfile(String token, String userName) throws GithubUserNotFound {
        Map<String, String> params = clientParams();
        HttpEntity<String> request = buildRequestEntity(token);
        if (!StringUtils.isEmpty(userName)) {
            request = buildRequestEntityForPublicProfile(token, userName);
        }
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

    public GithubUser fetchUserPublicProfile(String token, String userName) throws GithubUserNotFound {
        token = "<insert application token here>";
        return fetchUserProfile(token, userName);
    }

    private HttpEntity<String> buildRequestEntity(String token) {
        try {
            GraphqlQuery query = GraphqlQuery.builder()
                                .query(appProperties.getGithub_gpl_profile_query())
                                .variables(new ObjectMapper().writeValueAsString(getDateRangeOfLastSevenDays()))
                                .build();
            return new HttpEntity<>(new ObjectMapper().writeValueAsString(query), attachHeaders(token));
        } catch (JsonProcessingException e) {
            log.error("Failed to build Github request entity: ", e);
        }
        return null;
    }

    private HttpEntity<String> buildRequestEntityForPublicProfile(String token, String userName) {
        try {
            GraphqlQuery query = GraphqlQuery.builder()
                                             .query(appProperties.getGithub_gpl_public_profile_query())
                                             .variables(new ObjectMapper().writeValueAsString(
                                                     new DateRangeWithUserName(userName, getDateRangeOfLastSevenDays())))
                                             .build();
            return new HttpEntity<>(new ObjectMapper().writeValueAsString(query), attachHeaders(token));
        } catch (JsonProcessingException e) {
            log.error("Failed to build Github request entity: ", e);
        }
        return null;
    }

    private DateRange getDateRangeOfLastSevenDays() {
        return DateRange.builder()
                .fromDate(LocalDateTime.now().minusDays(6).format(DateTimeFormatter.ISO_DATE_TIME))
                .toDate(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                .build();
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
        if (!StringUtils.isEmpty(token)) {
            headers.set("Authorization", "Bearer " + token);
        }
        return headers;
    }

    public String getAccessTokenFromGithub(String code) {
        HttpHeaders headers = attachHeaders(null);
        Map<String, String> params = clientParams();
        params.put("code", code);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(params, headers);
        String response = this.restTemplate.postForObject(tokenAccessUrl, request, String.class);
        try {
            return new ObjectMapper().readTree(response).get("access_token").asText();
        } catch (JsonProcessingException e) {
            // TODO: refactor while handling errors globally
            throw new RuntimeException("Error parsing response while requesting token from Github");
        }
    }
}
