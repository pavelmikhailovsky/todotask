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

    @Autowired
    public UserService(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
        return userDao.getAll();
    }

    public User getByIdUser(Long id) {
        return userDao.getById(id);
    }

    public void createUser(User user) {
        String hashPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPassword);
        userDao.create(user);
    }

    @SneakyThrows
    public void uploadImage(MultipartFile file) {
        String path = "src/main/resources/static/images/" + file.getOriginalFilename();
        Path p = Path.of(path);
        byte[] bytes = file.getBytes();
        Files.write(p, bytes, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }
}
