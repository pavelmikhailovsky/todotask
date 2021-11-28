package org.todotask.service;

import org.springframework.web.multipart.MultipartFile;
import org.todotask.model.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers(String authorizationHeader);

    User getByIdUser(Long id);

    String createUser(User user);

    void uploadImage(MultipartFile file);

    String login(String username, String password) throws ValuesNotMatchException;
}
