package com.theservice.service;

import java.time.Instant;
import java.util.List;

import com.theservice.domain.Issue;

public interface IGithubIssueService {
    List<Issue> pullUpdatedIssuesSince(Instant instant);

    List<Issue> pullUpdatedIssues(String owner, String repo, Instant since);
}
