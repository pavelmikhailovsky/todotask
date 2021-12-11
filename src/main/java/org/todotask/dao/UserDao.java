package org.todotask.dao;

import org.todotask.model.User;

import java.util.List;

public interface UserDao extends DataAccessObject<User>{

    List<User> getAll();

    User getInstanceByName(String name);

}
