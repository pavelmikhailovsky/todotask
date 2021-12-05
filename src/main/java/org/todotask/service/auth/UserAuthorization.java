package org.todotask.service.auth;

import org.todotask.model.User;
import org.todotask.service.ValuesNotMatchException;

public interface UserAuthorization {

    String login(String username, String password) throws ValuesNotMatchException;

    User getUserByAuthorizationHeader(String authorizationHeader);
}
