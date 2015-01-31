package org.taskstodo.dao;

import java.io.Serializable;
import java.util.List;

public interface IGenericDAO<T, ID extends Serializable> {

    public abstract T create(T entity);

    public abstract T update(T entity);

    public abstract void remove(T entity);

    public abstract T findById(ID id);

    public abstract List<T> findAllByTaskId(ID id);

}