package org.taskstodo.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.taskstodo.common.Priority;
import org.taskstodo.common.TaskType;


public class Task implements Serializable {

   
	private Long taskId;
	
	private String title;
	private String description;
	private Priority priority;

//	private TaskType taskType = TaskType.TASKGROUP;

	private Date startDate;

	private Date endDate;

	private Date creationDate;

	private Date lastModifiedDate;

	
    private User owner;
	
	private Task parentTask;
	
    private Set<Task> childTasks = new HashSet<Task>();

	private Set<Keyword> keywords = new HashSet<Keyword>();

    private Set<FileUpload> fileUploads = new HashSet<FileUpload>();

    private Set<Note> notes = new HashSet<Note>();
    
    private Set<SearchItem> searchItems = new HashSet<SearchItem>();
    
    private Boolean finished;
	
	public Task() {}

	public Task(User owner) {
		this.owner = owner;
	}


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

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	
	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Set<Keyword> getKeywords() {
		return keywords;
	}

	public void setKeywords(Set<Keyword> keywords) {
		this.keywords = keywords;
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

    

    public Set<Task> getChildTasks() {
        return childTasks;
    }

    public void setChildTasks(Set<Task> childTasks) {
        this.childTasks = childTasks;
    }

    public Task getParentTask() {
        return parentTask;
    }

    public void setParentTask(Task parentTask) {
        this.parentTask = parentTask;
    }

    public Set<FileUpload> getFileUploads() {
        return fileUploads;
    }

    public void setFileUploads(Set<FileUpload> fileUploads) {
        this.fileUploads = fileUploads;
    }

    public Set<Note> getNotes() {
        return notes;
    }

    public void setNotes(Set<Note> notes) {
        this.notes = notes;
    }
    
    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long id) {
        this.taskId = id;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }
//
//    public TaskType getTaskType() {
//        return taskType;
//    }
//
//    public void setTaskType(TaskType taskType) {
//        this.taskType = taskType;
//    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    public Set<SearchItem> getSearchItems() {
        return searchItems;
    }

    public void setSearchItems(Set<SearchItem> searchItems) {
        this.searchItems = searchItems;
    }
}
