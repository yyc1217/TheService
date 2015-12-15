package com.theservice.service.impl;

import static org.junit.Assert.assertEquals;

import java.time.Instant;

import org.junit.Before;
import org.junit.Test;

public class GithubIssueServiceTest {

    private GithubIssueService githubIssueService = new GithubIssueService();

    @Before
    public void setUp() {
        githubIssueService.setClientId("fakeId");
        githubIssueService.setClientSecret("fakeSecret");
        githubIssueService.setEndpoint("fake.api.github.com");
    }
    
    @Test
    public void testUrl() {
        String expected = "https://fake.api.github.com/repos/yyc1217/Merl/issues?since=2015-12-15T14:02:50.607Z&client_id=fakeId&client_secret=fakeSecret";
        String actual = githubIssueService.buildIssueUrl("yyc1217", "Merl", Instant.parse("2015-12-15T14:02:50.607Z"));
        assertEquals(expected, actual);
    }

}
