package org.todotask.service;

import org.todotask.model.Todo;

import java.util.List;

public interface TodoService {

    void createTodo(String authorizationHeader, String text);

    List<Todo> getAllTodoCurrentUser(String authorizationHeader);

    void updateTodo(Long todoId, String text);

    void deleteTodo(Long todoId);

    void updateCompleteStatusTodo(Long todoId);
}
