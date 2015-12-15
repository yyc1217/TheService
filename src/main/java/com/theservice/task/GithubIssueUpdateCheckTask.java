package com.theservice.task;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class GithubIssueUpdateCheckTask {

    @Value("${github.client_id}")
    private String client_id;

    @Value("${github.client_secret}")
    private String client_secret;

    @Scheduled(fixedRate = 10 * 1000)
    public void checkUpdateEvent() {

        System.out.println("client_id " + client_id);
        System.out.println("client_secret " + client_secret);

    }
}
