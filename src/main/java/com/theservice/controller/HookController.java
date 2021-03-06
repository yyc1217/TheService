package com.theservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.theservice.service.IHookService;

@RestController
public class HookController {

    private static final Logger logger = LoggerFactory.getLogger(HookController.class);
    
	@Autowired
	private IHookService hookService;

	/**
	 * Used to receive webhook from github.
	 * @param headers headers of request.
	 * @param body body of request.
	 * @return status code 200 if ok
	 */
	@RequestMapping(value = "/github-webhook", method = RequestMethod.POST)
	public ResponseEntity<String> hook(@RequestHeader HttpHeaders headers, @RequestBody String body) {
			    
	    logger.info("Hook fired from github with headers {} \n body {}", headers, body);
	    	    
		this.hookService.fireHook(headers, body);
		
		return new ResponseEntity<String>(HttpStatus.OK);
	}
}
