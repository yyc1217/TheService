package com.theservice.service.impl;

import java.io.IOException;
import java.util.Optional;

import org.apache.http.HttpVersion;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.theservice.domain.Issue;
import com.theservice.domain.Playload;
import com.theservice.domain.Repository;
import com.theservice.domain.User;
import com.theservice.service.IGithubUserService;
import com.theservice.service.IHookService;

@Service
public class HookService implements IHookService {
    
    private static final Logger logger = LoggerFactory.getLogger(HookService.class);

    @Autowired
    private IGithubUserService githubUserService;
    
    @Value("${target.url}")
    private String targetSystemUrl;    

    
    /**
     * Fire hook with headers and body.
     * @param headers
     * @param body
     */
    @Override
    public void fireUpdatedIssueHook(Issue updatedIssue) {
        
        String body = buildMockWebhookBody(updatedIssue);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        fireHook(headers, body);
    }
    
    /**
     * Build body of simulated webhook
     * @param issue
     * @return
     */
    private String buildMockWebhookBody(Issue issue) {
        
        User owner = this.githubUserService.findByOwnerAndRepo(issue.getRepoOwnerUsername(), issue.getRepoName()).get();
        Repository repository = new Repository(owner);
        Playload playload = new Playload("updated", issue, repository, owner);
                
        return playload.toString();
    }

    /**
     * Fire updated issue hook to target system.
     * @param updatedIssue Updated issue from github.
     */
    @Override
    public void fireHook(HttpHeaders headers, String body) {
        
        try {

            Request request = buildRequest(targetSystemUrl, headers, body);

            int statusCode = request.execute()
                                    .returnResponse()
                                    .getStatusLine()
                                    .getStatusCode();

            if (statusCode != HttpStatus.OK.value()) {
                logger.error("{} connection error with status code {}", targetSystemUrl, statusCode);
            }

        } catch (IOException e) {
            logger.error(targetSystemUrl + " connection error", e);
        }
    }

    /**
     * Build request from url, with headers and body;
     * @param url
     * @param headers
     * @param body
     * @return
     */
    private Request buildRequest(String url, HttpHeaders headers, String body) {

        Request request = Request.Post(url)
                                 .useExpectContinue()
                                 .version(HttpVersion.HTTP_1_1)
                                 .bodyString(body, contentType(headers));

        request = copyHeadersToRequest(headers, request);

        return request;
    }

    /**
     * Copy headers to request.
     * <br>
     * Because content_length will be calculated again so remove content_length header.
     * @param headers
     * @param request
     * @return
     */
    private Request copyHeadersToRequest(HttpHeaders headers, Request request) {
        
        headers.remove(HttpHeaders.CONTENT_LENGTH);

        headers.toSingleValueMap()
               .forEach((name, value) -> {
                   request.addHeader(name, value);
               });

        return request;
    }

    /**
     * Create ContentType from content_type from headers.
     * @param headers
     * @return
     */
    private ContentType contentType(HttpHeaders headers) {
        return ContentType.create(headers.getContentType().getType());
    }
}
