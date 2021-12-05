package org.todotask.service.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.todotask.dao.DataAccessObject;
import org.todotask.dao.UserDao;
import org.todotask.model.User;
import org.todotask.service.ValuesNotMatchException;

@Service
public class UserAuthorizationImp implements UserAuthorization {

    private TokenService tokenService;
    private DataAccessObject<User> dataAccessObject;
    private PasswordEncoder passwordEncoder;

    public UserAuthorizationImp(
            TokenServiceImp tokenService, UserDao dataAccessObject, PasswordEncoder passwordEncoder
    ) {
        this.tokenService = tokenService;
        this.dataAccessObject = dataAccessObject;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String login(String username, String password) throws ValuesNotMatchException {
        User user = dataAccessObject.getInstanceByName(username);
        String s = user.getPassword();
        boolean match = passwordEncoder.matches(password, s);

        if (!match) {
            throw new ValuesNotMatchException("username or password not match");
        }

        return tokenService.getToken(user.getUsername());
    }

    @Override
    public User getUserByAuthorizationHeader(String authorizationHeader) {
        String usernameFromToken = tokenService.getTokenSubject(authorizationHeader);
        return dataAccessObject.getInstanceByName(usernameFromToken);
    }
}
