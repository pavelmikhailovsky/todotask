package org.todotask.service;

import org.springframework.web.multipart.MultipartFile;
import org.todotask.model.User;

import java.io.IOException;
import java.util.List;

public interface UserService {

    List<User> getAllUsers(String authorizationHeader);

    User getByIdUser(Long id);

    String createUser(User user);

    void uploadImage(MultipartFile file, String authorizationHeader);

    byte[] getImage(String authorizationHeader) throws IOException;

    String login(String username, String password) throws ValuesNotMatchException;
}
