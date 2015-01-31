package org.taskstodo.dao;

import java.util.List;

import org.taskstodo.entity.Note;

public interface INoteDAO extends IGenericDAO<Note, Long>{

   
    public abstract Note create(Note note);

    public abstract Note update(Note note);

    public abstract Note findById(Long noteId);

    public abstract void remove(Note note);

    public abstract List<Note> findAllByTaskId(Long taskId);
}
