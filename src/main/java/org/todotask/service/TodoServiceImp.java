package org.todotask.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.todotask.dao.TodoDao;
import org.todotask.model.Todo;

import java.util.List;

@Service
public class TodoServiceImp implements TodoService {

    private TodoDao todoDao;

    @Autowired
    public TodoServiceImp(TodoDao todoDao) {
        this.todoDao = todoDao;
    }

    @Override
    public void createTodo(Long userId, String text) {
        todoDao.create(Todo.getInstance(userId, text));
    }

    @Override
    public List<Todo> getAllTodoCurrentUser(Long userId) {
        return todoDao.getAll(userId);
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
