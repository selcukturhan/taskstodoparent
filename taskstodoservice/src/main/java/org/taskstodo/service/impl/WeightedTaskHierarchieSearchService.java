package org.taskstodo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.taskstodo.dao.ITaskDAO;
import org.taskstodo.entity.Task;
import org.taskstodo.to.SearchItemTO;

@Service(value = "WeightedTaskHierarchieSearchService")
public class WeightedTaskHierarchieSearchService extends AbstractService{
	private ITaskDAO taskDAO;
	public List<SearchItemTO> searchByKeywords(final Long taskId, final List<String> keywords){
    	List<SearchItemTO> searchItemTOs = new ArrayList<SearchItemTO>();
    	
    	//load taskkontext (Taskhierachie)
    	Task currentTask = taskDAO.findById(taskId);
    	
    	
    	//do searchcall against api with weighting-config or if not applicalable first obtain
    	//serach result & after that do ranking based on taskhierarchie
    	
    	return searchItemTOs;
    }
	
	
}
