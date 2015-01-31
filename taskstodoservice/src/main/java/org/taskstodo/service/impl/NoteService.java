package org.taskstodo.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.taskstodo.dao.INoteDAO;
import org.taskstodo.dao.ITaskDAO;
import org.taskstodo.entity.Note;
import org.taskstodo.entity.Task;
import org.taskstodo.service.INoteService;
import org.taskstodo.to.NoteTO;

@Service(value = "NoteService")
public class NoteService extends AbstractService implements INoteService {
    
    @Autowired
    private INoteDAO noteDAO;
    @Autowired
    private ITaskDAO taskDAO;
    
    public static final Logger logger = LoggerFactory.getLogger(NoteService.class);
    
    public NoteService() {}

    
    @Override
    public NoteTO create(NoteTO noteTO, Long taskId) {
       Note note = mapTO2Entity(Note.class, noteTO);
       Task task = taskDAO.findById(taskId);
       task.getNotes().add(note);
       note.setTask(task);
       note.setCreationDate(new Date());
       noteDAO.create(note);
       return mapEntity2TO(NoteTO.class,note);
    }

    @Override
    public void update(NoteTO noteTO) {
        Note note = noteDAO.findById(noteTO.getId());
        note.setContent(noteTO.getContent());
        note.setTitle(noteTO.getTitle());
        note.setLastModifiedDate(new Date());
        noteDAO.update(note);
    }
    
    @Override
    public List<NoteTO> findAllByTaskId(Long taskId) {
        
        List<NoteTO> noteTOs = new ArrayList<NoteTO>();
        List<Note> notes = noteDAO.findAllByTaskId(taskId);
        for(Note currentNote : notes){
            noteTOs.add(mapEntity2TO(NoteTO.class,currentNote));
        }
        return noteTOs;
    }

    @Override
    public NoteTO findByNoteId(Long noteId) {
        return mapEntity2TO(NoteTO.class, noteDAO.findById(noteId));
    }

    @Override
    public void delete(Long noteId){
        Note noteToRemove = noteDAO.findById(noteId);
        try {
            noteDAO.remove(noteToRemove);
        } catch (Exception e) {
           logger.error(e.getLocalizedMessage());
        }
    }
}
