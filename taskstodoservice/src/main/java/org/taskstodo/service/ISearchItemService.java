package org.taskstodo.service;

import java.util.List;

import org.taskstodo.to.SearchItemTO;

public interface ISearchItemService {

    public abstract SearchItemTO findBySearchItemId(Long searchItemId);
    public abstract List<SearchItemTO> findAllByTaskId(Long taskId);
   
    public abstract SearchItemTO create(SearchItemTO note, Long taskId);
    public abstract void update(SearchItemTO searchItem);
    public abstract void delete(Long searchItemId);


}