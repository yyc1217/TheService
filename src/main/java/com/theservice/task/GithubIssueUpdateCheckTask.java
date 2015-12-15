package com.theservice.task;

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

/**
 * A schedule task which check whether updated issues exist or not.
 * @author yyc1217
 *
 */
@Component
public class GithubIssueUpdateCheckTask {
    
    private static final Logger logger = LoggerFactory.getLogger(GithubIssueUpdateCheckTask.class);
    
    @Autowired
    private IGithubIssueService githubIssueService;
    
    @Autowired
    private IHookService hookService;

    /**
     * Get first checkpoint when system startup.
     */
    private Instant checkpoint = refreshCheckpoint();

    /**
     * Check updated issue(s) are exist or not by fixed rate.
     */
    @Scheduled(fixedDelay = 20 * 1000)
    public void checkUpdatedIssues() {
        
        logger.info(this.getClass().getSimpleName() + " is fired");
        
        List<Issue> issues = githubIssueService.pullUpdatedIssuesSince(checkpoint);

        for (Issue issue : issues) {
            try {
                this.hookService.fireUpdatedIssueHook(issue);
            } catch (Exception e) {
                logger.error("Hook error {}", e);
                logger.error("Error while fire updated issue hook of {}", issue);
            }
        }
        
        checkpoint = refreshCheckpoint(); //  Get next checkpoint when previous tasks is done.        
        logger.info("Next checkpoint {}", checkpoint);
    }

    private Instant refreshCheckpoint() {
        return Instant.now();
    }
}
