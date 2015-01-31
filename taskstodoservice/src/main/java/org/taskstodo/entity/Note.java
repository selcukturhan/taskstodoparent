package org.taskstodo.entity;

import java.util.Date;


public class Note {

    private String title;
    private String content;

    private Long noteId;
    
    private Date creationDate;

    private Date lastModifiedDate;

    private Task task;
    
    public Note() {}
    
    public Note(String title, String content) {
        super();
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

  

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Long getNoteId() {
        return noteId;
    }

    public void setNoteId(Long nodeId) {
        this.noteId = nodeId;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }  
}
