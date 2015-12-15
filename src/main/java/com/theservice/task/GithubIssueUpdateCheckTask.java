package com.theservice.task;

import java.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.theservice.service.IGithubIssueService;

@Component
public class GithubIssueUpdateCheckTask {
    
    @Autowired
    private IGithubIssueService githubIssueService;

    @Scheduled(fixedRate = 10 * 1000)
    public void checkUpdateEvent() {
        githubIssueService.pullUpdatedIssuesSince(Instant.now());
//
    }
}
