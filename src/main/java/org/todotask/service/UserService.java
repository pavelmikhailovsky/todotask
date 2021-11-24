package org.todotask.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.todotask.dao.UserDao;
import org.todotask.model.User;
import org.todotask.utils.HashingPasswordUtil;

import java.util.List;

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

}
