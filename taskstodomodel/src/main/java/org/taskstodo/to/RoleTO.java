package org.taskstodo.to;

import java.io.Serializable;

public class RoleTO implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 3393283857696558853L;
    
    private Long id;
    public RoleTO() {}
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }  
}
