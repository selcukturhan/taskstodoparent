package org.taskstodo.dao;

import java.util.List;

import org.taskstodo.entity.Task;

public interface ITaskDAO extends IGenericDAO<Task, Long>{

    public List<Task> findAllByOwner(String owner);

	public Task create(Task task);

	public Task update(Task task);

	public Task findById(Long taskId);
	
	public Task findByIdWithParent(Long taskId);
	
	public void remove(Task task);
	
	public Task findRootTask(final Long taskId);

}
