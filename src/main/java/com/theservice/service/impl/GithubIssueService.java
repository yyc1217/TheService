package com.theservice.service.impl;

import static java.util.stream.Collectors.toList;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.theservice.domain.Issue;
import com.theservice.service.IGithubIssueService;

@Service
public class GithubIssueService implements IGithubIssueService {

    private RestTemplate restTemplate = new RestTemplate();
    
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
    public List<Issue> pullUpdatedIssuesSince(Instant since) {
        String owner = "yyc1217";
        String repo = "Merl";
        return pullUpdatedIssues(owner, repo, since);
    }
    
    @Override
    public List<Issue> pullUpdatedIssues(String owner, String repo, Instant since) {
        String url = url(owner, repo, since);
        ResponseEntity<Issue[]> responseEntity = restTemplate.getForEntity(url, Issue[].class);
        Issue[] issues = responseEntity.getBody();
        
        List<Issue> issueList = Arrays.asList(issues);
        issueList = grepUpdatedIssues(issueList);
        
        return issueList;
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
    
    private List<Issue> grepUpdatedIssues(List<Issue> issues) {
        return issues
                .stream()
                .filter(Issue::isUpdated)
                .filter(Issue::isIssue)
                .collect(toList());
    }

}
