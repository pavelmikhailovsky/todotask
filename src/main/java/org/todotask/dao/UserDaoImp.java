package org.todotask.dao;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.todotask.model.User;

import java.sql.SQLDataException;
import java.util.List;

@Repository
public class UserDaoImp implements UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDaoImp(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query(
                "SELECT * FROM users ORDER BY create_at DESC", new BeanPropertyRowMapper<>(User.class)
        );
    }

    @Override
    @SneakyThrows
    public User getById(Long id) {
        return jdbcTemplate.query("SELECT * FROM users WHERE user_id = ?",
                new BeanPropertyRowMapper<>(User.class),
                id
        ).stream().findAny().orElseThrow(() -> new SQLDataException("User not find"));
    }

    @Override
    public void create(User user) {
        jdbcTemplate.update(
                "INSERT INTO users(username, password) VALUES(?, ?)",
                user.getUsername(), user.getPassword()
        );
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public void update(User user) {
        jdbcTemplate.update(
                "UPDATE users SET image = ? WHERE user_id = ?",
                user.getImage(), user.getUserId()
        );
    }

    @Override
    @SneakyThrows
    public User getInstanceByName(String name) {
        return jdbcTemplate.query("SELECT * FROM users WHERE username = ?",
                new BeanPropertyRowMapper<>(User.class),
                name
        ).stream().findAny().orElseThrow(() -> new SQLDataException("username or password not match"));
    }
}
