package com.theservice.task;

import static org.springframework.util.CollectionUtils.isEmpty;

import java.time.Instant;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.theservice.domain.Issue;
import com.theservice.service.IGithubIssueService;
import com.theservice.service.IHookService;

@Component
public class GithubIssueUpdateCheckTask {
    
    private static final Logger logger = LoggerFactory.getLogger(GithubIssueUpdateCheckTask.class);
    
    @Autowired
    private IGithubIssueService githubIssueService;
    
    @Autowired
    private IHookService hookService;

    @Scheduled(fixedRate = 20 * 1000)
    public void checkUpdateEvent() {
        
        logger.info(this.getClass().getSimpleName() + " is fired");
        
        List<Issue> issues = githubIssueService.pullUpdatedIssuesSince(Instant.parse("2015-12-12T03:40:34Z"));

        issues.forEach(issue -> {
            try {
                this.hookService.fireUpdatedIssueHook(issue);
            } catch (Exception e) {
                logger.error("Error while fire updated issue hook of {}", issue);
            }
        });
    }

}
