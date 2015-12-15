package com.theservice.service.impl;

import java.io.IOException;

import org.apache.http.HttpVersion;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.theservice.service.IClientHookService;

@Service
public class ClientHookService implements IClientHookService {

    private static final Logger logger = LoggerFactory.getLogger(ClientHookService.class);

    @Override
    public void transferHook(HttpHeaders headers, String body) {

        String notifyUrl = "http://httpbin.org/post";

        try {

            Request request = request(notifyUrl, headers, body);

            int statusCode = request.execute()
                                    .returnResponse()
                                    .getStatusLine()
                                    .getStatusCode();

            if (statusCode != 200) {
                logger.error("{} connection error with status code {}", notifyUrl, statusCode);
            }

        } catch (IOException e) {
            logger.error(notifyUrl + " connection error", e);
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
