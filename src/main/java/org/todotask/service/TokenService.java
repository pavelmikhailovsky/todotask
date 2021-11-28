package org.todotask.service;

public interface TokenService {

    String getToken(String username);

    String getTokenSubject(String token);
}
