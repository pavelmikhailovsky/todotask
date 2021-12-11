package org.todotask.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.todotask.dao.TodoDao;
import org.todotask.model.Todo;
import org.todotask.model.User;
import org.todotask.service.auth.UserAuthorization;

import java.util.List;

@Service
public class TodoServiceImp implements TodoService {

    private TodoDao todoDao;
    private UserAuthorization userAuthorization;

    @Autowired
    public TodoServiceImp(TodoDao todoDao, UserAuthorization userAuthorization) {
        this.todoDao = todoDao;
        this.userAuthorization = userAuthorization;
    }

    @Override
    public void createTodo(String authorizationHeader, String text) {
        User user = userAuthorization.getUserByAuthorizationHeader(authorizationHeader);
        todoDao.create(Todo.getInstance(user.getUserId(), text));
    }

    @Override
    public List<Todo> getAllTodoCurrentUser(String authorizationHeader) {
        User user = userAuthorization.getUserByAuthorizationHeader(authorizationHeader);
        return todoDao.getAll(user.getUserId());
    }

    @Override
    public void updateTodo(Long todoId, String text) {
        Todo todo = todoDao.getById(todoId);
        todo.setText(text);
        todoDao.update(todo);
    }

    @Override
    public void deleteTodo(Long todoId) {
        todoDao.delete(todoId);
    }

    @Override
    public void updateCompleteStatusTodo(Long todoId) {
        Todo todo = todoDao.getById(todoId);
        todo.setIsCompleted(!todo.getIsCompleted());
        todoDao.updateComplete(todo);
    }
}
