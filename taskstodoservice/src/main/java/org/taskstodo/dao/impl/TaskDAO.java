package org.taskstodo.dao.impl;

import java.util.List;

import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.taskstodo.dao.ITaskDAO;
import org.taskstodo.entity.Task;
@Transactional
@Repository
public class TaskDAO extends GenericDAO<Task, Long>implements ITaskDAO{

    public TaskDAO() {
        super(Task.class);
    }

    public Task findById(Long taskId) {
        return (Task) sessionFactory.getCurrentSession().createCriteria(Task.class).add(Restrictions.idEq(taskId))
        .setFetchMode("fileUploads", FetchMode.JOIN).setFetchMode("notes", FetchMode.JOIN).setFetchMode("keywords", FetchMode.JOIN)
        .uniqueResult();
    }
    
    
    public Task findByIdForSearchUseCase(Long taskId) {
        Task currentTask = (Task) sessionFactory.getCurrentSession().createCriteria(Task.class).add(Restrictions.idEq(taskId))
        .setFetchMode("keywords", FetchMode.JOIN)		
        .setFetchMode("parentTask", FetchMode.JOIN).uniqueResult();
        return currentTask;
    }
    
    public Task findByIdWithParent(Long taskId) {
        Task currentTask = findByIdForSearchUseCase(taskId);
        
        // FIXME: WORKAROUND FOR EXPANDATION
        Task expandTask = currentTask;
        while(expandTask.getParentTask() != null){
        	expandTask.setParentTask(findByIdForSearchUseCase(expandTask.getParentTask().getTaskId()));
        	expandTask = expandTask.getParentTask();
        }
        return currentTask;
    }
	
	@SuppressWarnings("unchecked")
    public List<Task> findAllByOwner(String owner){
	    return (List<Task>) sessionFactory.getCurrentSession().
	            createCriteria(Task.class)
	                .add( Restrictions.isNull("parentTask"))
	                .createCriteria("owner")
	                    .add(Restrictions.like("username", owner))
	                .setFetchMode("fileUploads", FetchMode.SELECT)
	                .setFetchMode("childTasks", FetchMode.SELECT)
	                .setFetchMode("notes", FetchMode.SELECT)
	                .setFetchMode("keywords", FetchMode.SELECT)
	                .setFetchMode("owner", FetchMode.SELECT)
	                .setFetchMode("parentTask", FetchMode.SELECT)
	                .setFetchMode("searchItems", FetchMode.SELECT)
	                .list();
	}

	@Override
	public Task findRootTask(final Long taskId) {
		// TODO Auto-generated method stub
		List<Task> result = getSubtree(taskId);
		return null;
	}
	
	public List<Task> getSubtree(final Long taskId){
		return (List<Task>) sessionFactory.getCurrentSession().createCriteria(Task.class).list();
	}
}
