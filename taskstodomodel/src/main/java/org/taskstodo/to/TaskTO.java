package org.taskstodo.to;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.taskstodo.common.Priority;
import org.taskstodo.common.TaskType;
import org.taskstodo.protocol.CustomDateDeserializer;
import org.taskstodo.protocol.CustomDateSerializer;
import org.taskstodo.protocol.CustomKeywordsDeserializer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class TaskTO implements Serializable {

	private Long id;
	
	private String title;
	private String description;
	private Priority priority;

//	private TaskType taskType;

	private Date startDate;

	private Date endDate;

	private Date creationDate;

	private Date lastModifiedDate;

	private Long parentId;
	
    private UserTO owner;
  
    private Boolean finished;
	
    private List<TaskTO> childTasks = new ArrayList<TaskTO>();

	private List<KeywordTO> keywords = new ArrayList<KeywordTO>();

    private List<FileLoadTO> fileUploads = new ArrayList<FileLoadTO>();

    private List<NoteTO> notes = new ArrayList<NoteTO>();
    
    private List<SearchItemTO> searchItems = new ArrayList<SearchItemTO>();
	
	public TaskTO() {}

	
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

	

	@JsonDeserialize(using = CustomDateDeserializer.class)
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}
	@JsonSerialize(using = CustomDateSerializer.class)
	public Date getStartDate() {
		return startDate;
	}

	@JsonSerialize(using = CustomDateSerializer.class)
	public Date getEndDate() {
		return endDate;
	}

	@JsonDeserialize(using = CustomDateDeserializer.class)
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public List<KeywordTO> getKeywords() {
		return keywords;
	}

	@JsonDeserialize(using = CustomKeywordsDeserializer.class)
	public void setKeywords(List<KeywordTO> keywords) {
		this.keywords = keywords;
	}

	public void addKeyword(KeywordTO keyword) {
		if (keywords == null) {
			this.keywords = new ArrayList<KeywordTO>();
		}

		keywords.add(keyword);
	}

	public Date getCreationDate() {
		return creationDate;
	}

	protected void setCreationDate() {
		if (this.creationDate == null) {
			creationDate = new Date();
		}
		lastModifiedDate = new Date();
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	protected void setLastModifiedDate() {
		lastModifiedDate = new Date();
	}

    

    public List<TaskTO> getChildTasks() {
        return childTasks;
    }

    public void setChildTasks(List<TaskTO> childTasks) {
        this.childTasks = childTasks;
    }

//    public TaskType getTaskType() {
//        return taskType;
//    }
//
//    public void setTaskType(TaskType taskType) {
//        this.taskType = taskType;
//    }


    public List<FileLoadTO> getFileUploads() {
        return fileUploads;
    }

    public void setFileUploads(List<FileLoadTO> fileUploads) {
        this.fileUploads = fileUploads;
    }

    public List<NoteTO> getNotes() {
        return notes;
    }

    public void setNotes(List<NoteTO> notes) {
        this.notes = notes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }


    public UserTO getOwner() {
        return owner;
    }


    public void setOwner(UserTO owner) {
        this.owner = owner;
    }


    public Boolean getFinished() {
        return finished;
    }


    public void setFinished(Boolean finished) {
        this.finished = finished;
    }


    public List<SearchItemTO> getSearchItems() {
        return searchItems;
    }


    public void setSearchItems(List<SearchItemTO> searchItems) {
        this.searchItems = searchItems;
    }


    public Long getParentId() {
        return parentId;
    }


    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

  
}
