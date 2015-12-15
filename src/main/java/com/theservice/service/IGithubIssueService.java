package com.theservice.service;

import java.time.Instant;
import java.util.List;

import com.theservice.domain.Issue;

public interface IGithubIssueService {
    
    /**
     * Pull updated issues from github after since.
     * @param since All updated issues after this checkpoint will be pulled.
     * @return
     */
    List<Issue> pullUpdatedIssuesSince(Instant since);

    /**
     * Pull updated issues from github after since which is the repo belongs to owner.
     * @param owner
     * @param repo
     * @param since All updated issues after this checkpoint will be pulled.
     * @return
     */
    List<Issue> pullUpdatedIssues(String owner, String repo, Instant since);
}
