package com.theservice.service;

import java.util.Optional;

import com.theservice.domain.User;

public interface IGithubUserService {

    Optional<User> findByOwnerAndRepo(String repoOwnerUsername, String string);

}
