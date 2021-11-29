package org.todotask.service;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.todotask.dao.DataAccessObject;
import org.todotask.dao.UserDao;
import org.todotask.model.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

@Service
public class UserServiceImp implements UserService {

    private DataAccessObject<User> dataAccessObject;
    private PasswordEncoder passwordEncoder;
    private TokenService tokenService;

    @Autowired
    public UserServiceImp(UserDao userDao, PasswordEncoder passwordEncoder, TokenServiceImp tokenServiceImp) {
        this.dataAccessObject = userDao;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenServiceImp;
    }

    public List<User> getAllUsers(String authorizationHeader) {
        String subject = tokenService.getTokenSubject(authorizationHeader);
        return dataAccessObject.getAll();
    }

    public User getByIdUser(Long id) {
        return dataAccessObject.getById(id);
    }

    public String createUser(User user) {
        String hashPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPassword);
        dataAccessObject.create(user);
        return tokenService.getToken(user.getUsername());
    }

    @SneakyThrows
    public void uploadImage(MultipartFile file, String authorizationHeader) {
        String usernameFromToken = tokenService.getTokenSubject(authorizationHeader);
        User user = dataAccessObject.getInstanceByName(usernameFromToken);
        String pathToUserDirectory = "src/main/resources/static/images/" + user.getUsername() + "/";

        if(!Files.isDirectory(Path.of(pathToUserDirectory))) {
             Files.createDirectories(Path.of(pathToUserDirectory));
        }

        Path path = Path.of(pathToUserDirectory + file.getOriginalFilename());
        byte[] bytes = file.getBytes();
        Files.write(path, bytes, StandardOpenOption.CREATE, StandardOpenOption.APPEND);

        user.setImage(pathToUserDirectory + file.getOriginalFilename());
        dataAccessObject.update(user);
    }

    public String login(String username, String password) throws ValuesNotMatchException {
        User user = dataAccessObject.getInstanceByName(username);
        String s = user.getPassword();
        boolean match = passwordEncoder.matches(password, s);

        if(!match) {
            throw new ValuesNotMatchException("username or password not match");
        }

        return tokenService.getToken(user.getUsername());
    }

    public byte[] getImage(String authorizationHeader) throws IOException {
        String usernameFromToken = tokenService.getTokenSubject(authorizationHeader);
        User user = dataAccessObject.getInstanceByName(usernameFromToken);

        return Files.readAllBytes(Path.of(user.getImage()));
    }
}
