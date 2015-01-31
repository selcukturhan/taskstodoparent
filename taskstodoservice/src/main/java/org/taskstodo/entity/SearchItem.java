package org.taskstodo.entity;

import java.io.Serializable;
import java.util.Date;

public class SearchItem implements Serializable{
    
    
    
    /**
     * 
     */
    private static final long serialVersionUID = 4892793394107823866L;

    private Long searchItemId;

    private Task task;

    private String value;
    
    Date date;

    public Long getSearchItemId() {
        return searchItemId;
    }

    public void setSearchItemId(Long searchItemId) {
        this.searchItemId = searchItemId;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
