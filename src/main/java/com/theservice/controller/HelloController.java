package com.theservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.theservice.service.IClientHookService;

@RestController
public class HelloController {

	@Autowired
	private IClientHookService clientHookService;

	@RequestMapping(value = "/hook", method = RequestMethod.POST)
	public @ResponseBody void hook(@RequestHeader HttpHeaders headers, @RequestBody String body) {
		
		this.clientHookService.fireHook(headers, body);
		
		return;
	}
}
