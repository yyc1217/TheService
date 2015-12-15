package com.theservice.service.impl;

import java.io.IOException;

import org.apache.http.HttpVersion;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.theservice.domain.Issue;
import com.theservice.service.IHookService;

@Service
public class HookService implements IHookService {

    @Value("${target.url}")
    private String targetSystemUrl;
    
    private static final Logger logger = LoggerFactory.getLogger(HookService.class);
    
    @Override
    public void fireUpdatedIssueHook(Issue issue) {
        
        String body = issue.toString();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        fireHook(headers, body);
    }
    
    @Override
    public void fireHook(HttpHeaders headers, String body) {
        
        try {

            Request request = request(targetSystemUrl, headers, body);

            int statusCode = request.execute()
                                    .returnResponse()
                                    .getStatusLine()
                                    .getStatusCode();

            if (statusCode != 200) {
                logger.error("{} connection error with status code {}", targetSystemUrl, statusCode);
            }

        } catch (IOException e) {
            logger.error(targetSystemUrl + " connection error", e);
        }
    }

    private Request request(String url, HttpHeaders headers, String body) {

        Request request = Request.Post(url)
                                 .useExpectContinue()
                                 .version(HttpVersion.HTTP_1_1)
                                 .bodyString(body, contentType(headers));

        request = copyHeadersToRequest(headers, request);

        return request;
    }

    private Request copyHeadersToRequest(HttpHeaders headers, Request request) {
        
        headers.remove(HttpHeaders.CONTENT_LENGTH);

        headers.toSingleValueMap()
               .forEach((name, value) -> {
                   request.addHeader(name, value);
               });

        return request;
    }

    private ContentType contentType(HttpHeaders headers) {
        return ContentType.create(headers.getContentType().getType());
    }
}
