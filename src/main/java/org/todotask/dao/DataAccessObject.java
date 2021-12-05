package org.todotask.dao;

import java.util.List;

public interface DataAccessObject<T> {

    T getById(Long id);

    void create(T t);

    void delete(Long id);

    void update(T t);

    default List<T> getAll() {return null;}

    default List<T> getAll(Long id) {return null;}

    default T getInstanceByName(String name) {return null;}
}
