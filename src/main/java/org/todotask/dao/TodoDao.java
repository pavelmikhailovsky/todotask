package org.todotask.dao;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.todotask.model.Todo;

import java.sql.SQLDataException;
import java.util.List;

@Repository
public class TodoDao implements DataAccessObject<Todo>{

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public TodoDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Todo> getAll(Long id) {
        return jdbcTemplate.query(
                "SELECT * FROM todo WHERE user_id = ?", new BeanPropertyRowMapper<>(Todo.class), id
        );
    }

    @Override
    @SneakyThrows
    public Todo getById(Long id) {
        return jdbcTemplate.query(
                "SELECT * FROM todo",
                new BeanPropertyRowMapper<>(Todo.class)
        ).stream().findAny().orElseThrow(() -> new SQLDataException("Todo is not find"));
    }

    @Override
    public void create(Todo todo) {
        jdbcTemplate.update(
                "INSERT INTO todo(user_id, text) VALUES (?, ?)",
                todo.getUserId(), todo.getText()
        );
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM todo WHERE todo_id = ?", id);
    }

    @Override
    public void update(Todo todo) {
        jdbcTemplate.update(
                "UPDATE todo SET text = ? WHERE todo_id = ?",
                todo.getText(), todo.getTodoId()
        );
    }

    public void updateComplete(Todo todo) {
        jdbcTemplate.update(
                "UPDATE todo SET is_completed = ? WHERE todo_id = ?",
                todo.getIsCompleted(), todo.getTodoId()
        );
    }
}
