package org.taskstodo.service;

import java.util.List;

import org.taskstodo.to.NoteTO;

public interface INoteService {

    public abstract NoteTO findByNoteId(Long noteId);
    public abstract List<NoteTO> findAllByTaskId(Long taskId);
   
    public abstract NoteTO create(NoteTO note, Long taskId);
    public abstract void update(NoteTO note);
    public abstract void delete(Long noteId);


}