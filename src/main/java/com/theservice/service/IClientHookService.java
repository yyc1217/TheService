package com.theservice.service;

import org.springframework.http.HttpHeaders;

public interface IClientHookService {

	void transferHook(HttpHeaders headers, String body);

}
