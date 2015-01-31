package org.taskstodo.dao;

import java.util.List;

import org.taskstodo.entity.Keyword;

public interface IKeywordDAO extends IGenericDAO<Keyword, Long>{
   
    public abstract Keyword create(Keyword keyword);

    public abstract Keyword update(Keyword keyword);

    public abstract Keyword findById(Long noteId);

    public abstract void remove(Keyword keyword);

    public abstract List<Keyword> findAllByTaskId(Long taskId);

	public abstract List<Keyword> getTypeahead();
}
