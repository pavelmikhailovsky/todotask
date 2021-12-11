package org.todotask.dao;

public interface DataAccessObject<T> {

    T getById(Long id);

    void create(T t);

    void delete(Long id);

    void update(T t);

}
