package org.taskstodo.dao;

import java.util.List;

import org.taskstodo.entity.SearchItem;

public interface ISearchItemDAO extends IGenericDAO<SearchItem, Long>{
   
    public abstract SearchItem create(SearchItem searchItem);

    public abstract SearchItem update(SearchItem searchItem);

    public abstract SearchItem findById(Long noteId);

    public abstract void remove(SearchItem searchItem);

    public abstract List<SearchItem> findAllByTaskId(Long taskId);
}
