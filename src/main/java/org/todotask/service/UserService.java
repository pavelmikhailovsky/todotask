package org.todotask.service;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.todotask.dao.UserDao;
import org.todotask.model.User;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

@Service
public class UserService {

    private UserDao userDao;
    private PasswordEncoder passwordEncoder;
    private TokenService tokenService;

    @Autowired
    public UserService(UserDao userDao, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    public List<User> getAllUsers(String authorizationHeader) {
        String subject = tokenService.getTokenSubject(authorizationHeader);
        return userDao.getAll();
    }

    public User getByIdUser(Long id) {
        return userDao.getById(id);
    }

    public String createUser(User user) {
        String hashPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPassword);
        userDao.create(user);
        return tokenService.getToken(user.getUsername());
    }

    @SneakyThrows
    public void uploadImage(MultipartFile file) {
        String path = "src/main/resources/static/images/" + file.getOriginalFilename();
        Path p = Path.of(path);
        byte[] bytes = file.getBytes();
        Files.write(p, bytes, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    public String login(String username, String password) throws ValuesNotMatchException {
        User user = userDao.getInstanceByName(username);
        String s = user.getPassword();
        boolean match = passwordEncoder.matches(password, s);

        if(!match) {
            throw new ValuesNotMatchException("username or password not match");
        }

        return tokenService.getToken(user.getUsername());
    }
}
