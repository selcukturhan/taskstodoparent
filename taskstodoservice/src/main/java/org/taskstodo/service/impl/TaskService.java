package org.taskstodo.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.taskstodo.common.NodeCombination;
import org.taskstodo.dao.IKeywordDAO;
import org.taskstodo.dao.ITaskDAO;
import org.taskstodo.dao.IUserDAO;
import org.taskstodo.entity.Keyword;
import org.taskstodo.entity.Task;
import org.taskstodo.entity.User;
import org.taskstodo.service.ITaskService;
import org.taskstodo.to.KeywordTO;
import org.taskstodo.to.TaskTO;

@Service(value = "TaskService")
@Transactional
public class TaskService extends AbstractService implements ITaskService {
    @Autowired
    private ITaskDAO taskDAO;
    @Autowired
    private IKeywordDAO keywordDAO;
    @Autowired
    private IUserDAO userDAO;
    
    public static final Logger logger = LoggerFactory.getLogger(TaskService.class);
    
    
    public TaskService() {}

    public TaskService(final ITaskDAO taskDAO) {
        this.taskDAO = taskDAO;
    }



    public List<TaskTO> findAll(){
        List<TaskTO> taskTOs = new ArrayList<TaskTO>();
        List<Task> tasks;
        try {
            tasks = taskDAO.findAllByOwner(getUserName());
            for(Task currentTask : tasks){
                taskTOs.add(mapEntity2TO(TaskTO.class,currentTask));
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return taskTOs;
    }
        
  
    public TaskTO create(TaskTO taskTO) {
        Task task = mapTO2Entity(Task.class, taskTO);
        User owner = userDAO.findByName(getUserName());
        task.setOwner(owner);
        
        List<KeywordTO> keywords = taskTO.getKeywords();
        
        processKeywords(task.getTaskId(), keywords);
        if(taskTO.getParentId() != null){
            Task parentTask = taskDAO.findById(taskTO.getParentId());
            parentTask.getChildTasks().add(task);
            task.setParentTask(parentTask);
        }
        task = taskDAO.create(task);
        TaskTO newTaskTO = mapEntity2TO(TaskTO.class,task);
        //TODO: Workaround find right dozer mapping
        newTaskTO.setParentId(taskTO.getParentId());
        return newTaskTO;
    }

    private void processKeywords(Long taskId, List<KeywordTO> keywords) {
        if(keywords != null){
            for (KeywordTO keywordTO : keywords) {
                keywordDAO.create(mapTO2Entity(Keyword.class, keywordTO));
            }
        }
    }

    public void update(TaskTO taskTO) {
        //Just update the information subset 
        Task persistentTask = taskDAO.findById(taskTO.getId());
        persistentTask.setDescription(taskTO.getDescription());
        persistentTask.setTitle(taskTO.getTitle());
        persistentTask.setPriority(taskTO.getPriority());
//        persistentTask.setTaskType(taskTO.getTaskType());
        persistentTask.setStartDate(taskTO.getStartDate());
        persistentTask.setEndDate(taskTO.getEndDate());
        persistentTask.setLastModifiedDate(new Date());
        persistentTask.setFinished(taskTO.getFinished());
        taskDAO.update(persistentTask);
    }

    public TaskTO findByTaskId(Long taskId) {
        return mapEntity2TO(TaskTO.class, taskDAO.findById(taskId));
    }

    public void delete(Long taskId){
        Task taskToRemove = taskDAO.findById(taskId);
        if(taskToRemove != null)
            taskDAO.remove(taskToRemove);
    }
     
    public void reassignNode(NodeCombination nodeCombination){
        //Load all involved nodes
        Task movedNode = taskDAO.findById(nodeCombination.getMovedNodeId());
        
        Task sourceNode = null;
        if (nodeCombination.getSourceNodeId() != null) {
            sourceNode = taskDAO.findById(nodeCombination.getSourceNodeId());
            sourceNode.getChildTasks().remove(movedNode);
        }
        
        Task targetNode = null;
        if(nodeCombination.getTargetNodeId() != null){
            targetNode = taskDAO.findById(nodeCombination.getTargetNodeId());
            targetNode.getChildTasks().add(movedNode);
        }
        
        movedNode.setParentTask(targetNode);
        taskDAO.update(movedNode);
    }


	@Override
	public org.taskstodo.entity.Task inmprofindByTaskId(Long taskId) {
		// TODO Auto-generated method stub
		return taskDAO.findByIdWithParent(taskId);
	}
}
