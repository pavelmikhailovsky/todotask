package org.todotask.service.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Objects;

@Service
@PropertySource("classpath:application.properties")
public class TokenServiceImp implements TokenService {

    private Environment env;
    private final Key KEY;

    @Autowired
    public TokenServiceImp(Environment env) {
        this.env = env;
        this.KEY = Keys.hmacShaKeyFor(
                Objects.requireNonNull(this.env.getProperty("secret-key")).getBytes()
        );
    }

    public String getToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .signWith(KEY)
                .compact();
    }

    public String getTokenSubject(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
