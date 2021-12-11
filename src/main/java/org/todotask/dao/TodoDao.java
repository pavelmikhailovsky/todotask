package org.todotask.dao;

import org.todotask.model.Todo;

import java.util.List;

public interface TodoDao extends DataAccessObject<Todo>{

    void updateComplete(Todo todo);

    List<Todo> getAll(Long id);
}
