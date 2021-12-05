package org.todotask.service.auth;

public interface TokenService {

    String getToken(String username);

    String getTokenSubject(String token);
}
