package org.taskstodo.to;

import java.io.Serializable;
import java.sql.Date;

public class SearchItemTO implements Serializable{
    
    /**
     * 
     */
    private static final long serialVersionUID = -2006465859415818894L;
    private Long id;
    private String value;
    
    private Date date;

   
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
