package com.theservice.service.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.theservice.domain.User;
import com.theservice.service.IGithubUserService;

@Service
public class GithubUserService implements IGithubUserService {

    private static final Logger logger = LoggerFactory.getLogger(GithubUserService.class);
    
    private static final String REPO_PATH = "/repos/{owner}/{repo}";
    
    private RestTemplate restTemplate = new RestTemplate();
    
    private String endpoint;
    
    @Value("${github.endpoint}")
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }
    
    private String client_id;
    
    @Value("${github.client_id}")
    public void setClientId(String client_id) {
        this.client_id = client_id;
    }
    
    
    private String client_secret;
    
    @Value("${github.client_secret}")
    public void setClientSecret(String client_secret) {
        this.client_secret = client_secret;
    }
    
    
    @Override
    public Optional<User> findByOwnerAndRepo(String repoOwnerUsername, String repoName) {
        
        String url = buildUserUrl(repoOwnerUsername, repoName);
        ResponseEntity<User> responseEntity = restTemplate.getForEntity(url, User.class);

        if (HttpStatus.NOT_FOUND.equals(responseEntity.getStatusCode())) {
            logger.error("User {} and Repository {} not found!", repoOwnerUsername, repoName);
            return Optional.empty();
        }
        return Optional.of(responseEntity.getBody());
    }

    protected String buildUserUrl(String owner, String repo) {
        
        return UriComponentsBuilder.newInstance()
                                   .host(endpoint)
                                   .scheme("https")
                                   .path(REPO_PATH)
                                   .queryParam("client_id",  client_id)
                                   .queryParam("client_secret", client_secret)
                                   .buildAndExpand(owner, repo)
                                   .toString();
    }
}
