package org.taskstodo.entity;

import java.io.Serializable;

public class Keyword implements Serializable {
    /* Generated serialVersionUID. */

    private Long keywordId;

    private Task task;

    private String value;

    public Keyword() {
    }

    public Keyword(String value) {
        this.value = value;
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Long getKeywordId() {
        return keywordId;
    }

    public void setKeywordId(Long keywordId) {
        this.keywordId = keywordId;
    }
}
