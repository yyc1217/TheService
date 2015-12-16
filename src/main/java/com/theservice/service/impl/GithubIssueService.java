package com.theservice.service.impl;

import static java.util.stream.Collectors.toList;
import static org.springframework.util.CollectionUtils.isEmpty;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.theservice.domain.Issue;
import com.theservice.service.IGithubIssueService;

@Service
public class GithubIssueService implements IGithubIssueService {
    
    private static final Logger logger = LoggerFactory.getLogger(GithubIssueService.class);
    
    private static final String ISSUES_PATH = "/repos/{owner}/{repo}/issues";
    
    private RestTemplate restTemplate = new RestTemplate();
    
    @Autowired
    private ResourceLoader resourceLoader;
    
    @Value("${repos.filename}")
    private String repoListFilename;
    
    
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
    
    
    private List<RepoInfo> repoList;
    
    /**
     * Read repos that we want to monitor from file.
     * @throws IOException
     */
    @PostConstruct
    public void setRepoList() throws IOException {
        
        URI uri = resourceLoader.getResource("classpath:" + repoListFilename).getURI();
        
        Path path = Paths.get(uri);

        repoList = Files.lines(path)
                        .map(StringUtils::commaDelimitedListToStringArray)
                        .map(RepoInfo::new)
                        .collect(toList());
    }
    
    /**
     * A simple data holder class.
     * @author yyc1217
     *
     */
    static class RepoInfo {
        String owner;
        String repo;
        
        RepoInfo(String[] infos) {
            this.owner = infos[0];
            this.repo = infos[1];
        }
    }
    
    /**
     * Pull updated issues from github after since.
     * @param since All updated issues after this checkpoint will be pulled.
     * @return
     */
    @Override
    public List<Issue> pullUpdatedIssuesSince(Instant since) {
        
        List<Issue> returnList = new ArrayList<Issue>();
        
        for (RepoInfo info : repoList) {
            
            List<Issue> issueList = pullUpdatedIssues(info.owner, info.repo, since);
            returnList.addAll(issueList);
            
            log(info.owner, info.repo, issueList);
        }
        
        return returnList;
    }
    
    /**
     * Pull updated issues from github after since which is the repo belongs to owner.
     * @param owner
     * @param repo
     * @param since All updated issues after this checkpoint will be pulled.
     * @return
     */
    @Override
    public List<Issue> pullUpdatedIssues(String owner, String repo, Instant since) {
        
        String url = buildIssueUrl(owner, repo, since);
        
        ResponseEntity<Issue[]> responseEntity = restTemplate.getForEntity(url, Issue[].class);
        Issue[] issues = responseEntity.getBody();
        
        List<Issue> issueList = grepUpdatedIssues(issues);
        issueList =  fillRepositoryInfo(issueList, owner, repo);
        
        return issueList;
    }

    /**
     * Using {@link UriComponentsBuilder} to build issues api url.
     * To get higher rate limit of github, we use client_id and client_secret.
     * @param owner
     * @param repo
     * @param since
     * @return url
     */
    protected String buildIssueUrl(String owner, String repo, Instant since) {
        
        return UriComponentsBuilder.newInstance()
                                   .host(endpoint)
                                   .scheme("https")
                                   .path(ISSUES_PATH)
                                   .queryParam("since", since)
                                   .queryParam("client_id",  client_id)
                                   .queryParam("client_secret", client_secret)
                                   .buildAndExpand(owner, repo)
                                   .toString();
    }
    
    /**
     * Grep updated issues.
     * @param issues
     * @return
     */
    private List<Issue> grepUpdatedIssues(Issue[] issues) {
        
        return Arrays.asList(issues)
                     .stream()
                     .filter(Issue::isUpdated)
                     .filter(Issue::isIssue)
                     .collect(toList());
    }

    /**
     * Fill information of repository which issue issued.
     * @param issues
     * @param repo 
     * @param owner 
     * @return
     */
    private List<Issue> fillRepositoryInfo(List<Issue> issues, String owner, String repo) {
        
        issues.forEach(issue -> {
            issue.setRepoOwnerUsername(owner);
            issue.setRepoName(repo);
        });
        
        return issues;
    }
    
    /**
     * Logging updated issues of repo which owned by owner.
     * @param owner
     * @param repo
     * @param updatedIssues
     */
    private void log(String owner, String repo, List<Issue> updatedIssues) {
        
        if (isEmpty(updatedIssues)) {
            logger.info("No updated issue(s): [owner={}, repo={}]", owner, repo);
            return;
        }
        
        logger.info("Found {} updated issue(s): [owner={}, repo={}]", updatedIssues.size(), owner, repo);
        
        if (logger.isDebugEnabled()) {
            updatedIssues.forEach(issue -> logger.debug("Updated issue : {}", issue));
        }
    }
    
}
