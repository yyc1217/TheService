package com.theservice.service;

import java.time.Instant;

public interface IGithubIssueService {
    void pullUpdatedIssuesSince(Instant instant);

    void pullUpdatedIssues(String owner, String repo, Instant since);
}
