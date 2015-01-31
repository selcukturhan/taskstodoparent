package org.taskstodo.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.taskstodo.dao.INoteDAO;
import org.taskstodo.entity.Note;

@Repository
@Transactional
public class NoteDAO extends GenericDAO<Note, Long>implements INoteDAO{
    public NoteDAO() {
        super(Note.class);
    }
}
