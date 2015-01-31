package org.taskstodo.service;


import java.util.List;

import org.taskstodo.common.NodeCombination;
import org.taskstodo.to.TaskTO;

public interface ITaskService {

	/**
	 * Returns all user tasks.
	 * 
	 * @param owner - the owner.
	 * @return the users' tasks.
	 * @throws Exception 
	 * 
	 * @throws Exception - error loading the tasks.
	 */
    public abstract TaskTO findByTaskId(Long taskId);

    public abstract org.taskstodo.entity.Task inmprofindByTaskId(Long taskId);
    
    public abstract List<TaskTO> findAll();

	public abstract TaskTO create(TaskTO task);
	public abstract void update(TaskTO task);
	public abstract void delete(Long userId);

	public abstract void reassignNode(NodeCombination nodeCombination);
}