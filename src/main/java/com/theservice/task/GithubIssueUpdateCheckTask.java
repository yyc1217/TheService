package com.theservice.task;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.theservice.domain.Issue;
import com.theservice.service.IGithubIssueService;

@Component
public class GithubIssueUpdateCheckTask {
    
    @Autowired
    private IGithubIssueService githubIssueService;

    @Scheduled(fixedRate = 15 * 1000)
    public void checkUpdateEvent() {
        List<Issue> issues = githubIssueService.pullUpdatedIssuesSince(Instant.parse("2015-12-01T03:40:34Z"));
        System.out.println(issues);
    }
}
