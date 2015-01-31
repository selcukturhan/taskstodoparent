package org.taskstodo.entity;

import java.util.Date;

public class FileUpload{
    
    private Long fileUploadId;
    
    private String title;
    private String description;
    private String uri;
    private String contentType;
    private Date creationDate;

    private Task task;

    public FileUpload(){}
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Date getCreationDate() {
        return creationDate;
    }


    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Long getFileUploadId() {
        return fileUploadId;
    }

    public void setFileUploadId(Long fileUploadId) {
        this.fileUploadId = fileUploadId;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
