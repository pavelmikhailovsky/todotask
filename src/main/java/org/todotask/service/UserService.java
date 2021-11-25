package org.todotask.service;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.todotask.dao.UserDao;
import org.todotask.model.User;
import org.todotask.utils.HashingPasswordUtil;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Objects;

@Service
public class UserService {

    private UserDao userDao;
    private HashingPasswordUtil hashingPasswordUtil;

    @Autowired
    public UserService(UserDao userDao, HashingPasswordUtil hashingPasswordService) {
        this.userDao = userDao;
        this.hashingPasswordUtil = hashingPasswordService;
    }

    public List<User> getAllUsers() {
        return userDao.getAll();
    }

    public User getByIdUser(Long id) {
        return userDao.getById(id);
    }

    public void createUser(User user) {
        String hashPassword = hashingPasswordUtil.getHashPassword(user.getPassword());
        user.setPassword(hashPassword);
        userDao.create(user);
    }

    @SneakyThrows
    public void uploadFile(MultipartFile file) {
        String path = "src/main/resources/static/images/" + file.getOriginalFilename();
        Path p = Path.of(path);
        byte[] bytes = file.getBytes();
        Files.write(p, bytes, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }
}
