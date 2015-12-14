package com.theservice.service.impl;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.theservice.service.IClientHookService;

@Service
public class ClientHookService implements IClientHookService {

	@Override
	public void fireHook(HttpHeaders headers, String body) {
		// TODO Auto-generated method stub
		
	}

}
