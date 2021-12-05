package org.todotask.service;

import org.todotask.model.Todo;

import java.util.List;

public interface TodoService {

    void createTodo(Long userId, String text);

    List<Todo> getAllTodoCurrentUser(Long userId);

    void updateTodo(Long todoId, String text);

    void deleteTodo(Long todoId);

    void updateCompleteStatusTodo(Long todoId);
}
