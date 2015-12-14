package com.theservice.service;

import org.springframework.http.HttpHeaders;

public interface IClientHookService {

	void fireHook(HttpHeaders headers, String body);

}
