package com.theservice.service.impl;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.theservice.service.IGithubIssueService;

@Service
public class GithubIssueService implements IGithubIssueService {

    private static final String PATH = "/repos/{owner}/{repo}/issues";
    
    private String endpoint;

    private String client_id;

    private String client_secret;

    @Value("${github.endpoint}")
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    @Value("${github.client_id}")
    public void setClientId(String client_id) {
        this.client_id = client_id;
    }
    
    @Value("${github.client_secret}")
    public void setClientSecret(String client_secret) {
        this.client_secret = client_secret;
    }
    
    @Override
    public void pullUpdatedIssuesSince(Instant since) {
        String owner = "yyc1217";
        String repo = "Merl";
        pullUpdatedIssues(owner, repo, since);
    }
    
    @Override
    public void pullUpdatedIssues(String owner, String repo, Instant since) {
        String url = url(owner, repo, since);
    }
    
    protected String url(String owner, String repo, Instant since) {
        
        return UriComponentsBuilder
                .newInstance()
                .host(endpoint)
                .scheme("https")
                .path(PATH)
                .queryParam("since", since)
                .queryParam("client_id",  client_id)
                .queryParam("client_secret", client_secret)
                .buildAndExpand(owner, repo).toString();
    }

}
