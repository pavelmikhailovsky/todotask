package org.todotask.service;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.todotask.dao.UserDao;
import org.todotask.model.User;
import org.todotask.service.auth.TokenService;
import org.todotask.service.auth.UserAuthorization;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

@Service
public class UserServiceImp implements UserService {

    private UserDao userDao;
    private PasswordEncoder passwordEncoder;
    private TokenService tokenService;
    private UserAuthorization userAuthorization;

    @Autowired
    public UserServiceImp(UserDao userDao, PasswordEncoder passwordEncoder,
                          TokenService tokenService, UserAuthorization userAuthorization) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.userAuthorization = userAuthorization;
    }

    /**
     *
     * Deprecated
     */
    @Deprecated
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
    public void uploadImage(MultipartFile file, String authorizationHeader) {
        User user = userAuthorization.getUserByAuthorizationHeader(authorizationHeader);
        String pathToUserDirectory = "src/main/resources/static/images/" + user.getUsername() + "/";

        if (!Files.isDirectory(Path.of(pathToUserDirectory))) {
            Files.createDirectories(Path.of(pathToUserDirectory));
        }

        Path path = Path.of(pathToUserDirectory + file.getOriginalFilename());
        byte[] bytes = file.getBytes();
        Files.write(path, bytes, StandardOpenOption.CREATE, StandardOpenOption.APPEND);

        user.setImage(pathToUserDirectory + file.getOriginalFilename());
        userDao.update(user);
    }

    public byte[] getImage(String authorizationHeader) throws IOException {
        User user = userAuthorization.getUserByAuthorizationHeader(authorizationHeader);
        return Files.readAllBytes(Path.of(user.getImage()));
    }
}
