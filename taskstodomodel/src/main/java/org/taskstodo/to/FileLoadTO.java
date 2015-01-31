package org.taskstodo.to;

import java.io.Serializable;

public class FileLoadTO implements Serializable{
    
    /**
     * 
     */
    private static final long serialVersionUID = 6225275411396426309L;
    private Long id;
    private String title;
    private String filename;
    private String description;
    
    private ImageTO image;
    
    public String getTitle() {
        return title;
    }

    public FileLoadTO(){}
    
    public FileLoadTO(Long id, String title, String filename, String description) {
        super();
        this.id = id;
        this.title = title;
        this.filename = filename;
        this.description = description;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ImageTO getImage() {
        return image;
    }

    public void setImage(ImageTO image) {
        this.image = image;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}