package org.todotask.dao;

import java.util.List;

public interface DataAccessObject<T> {

    List<T> getAll();

    T getById(Long id);

    void create(T t);

    void delete(Long id);

    void update(T t);

    default T getInstanceByName(String name) {return null;}
}
