package com.dsinnovators.devprofilesbackend.github;

import com.dsinnovators.devprofilesbackend.configurations.AppProperties;
import com.dsinnovators.devprofilesbackend.github.entities.GithubResponseData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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
            githubSampleQuery = loadFileAsString("github/github_profile_test_query.graphql");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            githubSampleResponse = loadFileAsString("github/qithub_profile_gpl_response.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void fetchUserProfileWithContributions() throws IOException {
        ResponseEntity<String> apiResponse = (ResponseEntity<String>) mock(ResponseEntity.class);
        when(apiResponse.getBody()).thenReturn(githubSampleResponse);
        when(appProperties.getGithub_gpl_profile_query()).thenReturn(githubSampleQuery);
        when(this.restTemplate.exchange(anyString(), any(HttpMethod.class), ArgumentMatchers.<HttpEntity<String>>any(),
                    eq(String.class), ArgumentMatchers.<String, String>anyMap()))
                .thenReturn(apiResponse);

        GithubResponseData responseData = githubGraphqlClient.fetchUserProfileWithContributions("mockToken");

        assertThat(responseData.getData(), is(notNullValue()));
        assertThat(responseData.getData().getUser(), is(notNullValue()));
        assertThat(responseData.getData().getRateLimit(), is(notNullValue()));
        assertThat(responseData.getData().getUser().getLogin(), is("gaearon"));
        assertThat(responseData.getData().getUser().getOrganizationConnections().getOrganizations().size(), is(7));
    }

    public static String loadFileAsString(String filePath)throws IOException {
        File resource = new ClassPathResource(filePath).getFile();
        return new String(Files.readAllBytes(resource.toPath()));

    }
}