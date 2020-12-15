package com.dsinnovators.devprofilesbackend.github;

import com.dsinnovators.devprofilesbackend.configurations.AppProperties;
import com.dsinnovators.devprofilesbackend.github.entities.GithubUser;
import com.dsinnovators.devprofilesbackend.utils.GithubUserNotFound;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static com.dsinnovators.devprofilesbackend.utils.ResourceReader.readAsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class GithubGraphqlClientTest {
    @Mock
    private RestTemplate restTemplate;

    @Mock
    private AppProperties appProperties;

    @InjectMocks
    private GithubGraphqlClient githubGraphqlClient;

    private static String githubSampleQuery;

    private static String githubSampleResponse;

    @BeforeAll
    static void setUp() {
        try {
            githubSampleQuery = readAsString("classpath:github/github_profile_test_query.graphql");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            githubSampleResponse = readAsString("classpath:github/qithub_profile_gpl_response.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void fetchUserProfileWithContributions() throws GithubUserNotFound {
        ResponseEntity<String> apiResponse = (ResponseEntity<String>) mock(ResponseEntity.class);
        when(apiResponse.getBody()).thenReturn(githubSampleResponse);
        when(appProperties.getGithub_gpl_profile_query()).thenReturn(githubSampleQuery);
        when(this.restTemplate.exchange(anyString(), any(HttpMethod.class), ArgumentMatchers.<HttpEntity<String>>any(),
                    eq(String.class), ArgumentMatchers.<String, String>anyMap()))
                .thenReturn(apiResponse);

        GithubUser responseData = githubGraphqlClient.fetchUserProfileWithContributions("mockToken");

        assertThat(responseData, is(notNullValue()));
        assertThat(responseData.getLogin(), is("gaearon"));
        assertThat(responseData.getOrganizationConnections().getOrganizations().size(), is(7));
    }
}