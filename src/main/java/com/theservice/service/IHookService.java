package com.theservice.service;

import org.springframework.http.HttpHeaders;

import com.theservice.domain.Issue;

public interface IHookService {

    /**
     * Fire hook with headers and body.
     * @param headers
     * @param body
     */
	void fireHook(HttpHeaders headers, String body);

	/**
	 * Fire updated issue hook to target system.
	 * @param updatedIssue Updated issue from github.
	 */
    void fireUpdatedIssueHook(Issue updatedIssue);

}
