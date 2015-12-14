package com.theservice.service.impl;

import java.io.IOException;

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
		
		String notifyUrl = "http://test";
		
		try {
			
			ContentType contentType = ContentType.create(headers.getContentType().getType());
			Request request = Request.Post(notifyUrl).bodyString(body, contentType);
			
			headers.toSingleValueMap()
			.forEach((name, value) -> {
				request.addHeader(name, value);
			});
			
			int statusCode = request.execute().returnResponse().getStatusLine().getStatusCode();
			
			if (statusCode != 200) {
				logger.error("{} connection error with status code {}", notifyUrl, statusCode);
			}
			
		} catch (IOException e) {
			logger.error(notifyUrl + " connection error", e);
		}
	}

}
