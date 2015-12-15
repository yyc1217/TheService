package com.theservice.service;

import org.springframework.http.HttpHeaders;

import com.theservice.domain.Issue;

public interface IHookService {

	void fireHook(HttpHeaders headers, String body);

    void fireUpdatedIssueHook(Issue issue);

}
