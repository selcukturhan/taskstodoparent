package org.taskstodo.to;

import java.io.Serializable;

public class KeywordTO implements Serializable {
    /* Generated serialVersionUID. */

    private Long id;
   
    private String value;

    public KeywordTO() {
    }

    public KeywordTO(String value) {
        this.value = value;
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
   
   
}
