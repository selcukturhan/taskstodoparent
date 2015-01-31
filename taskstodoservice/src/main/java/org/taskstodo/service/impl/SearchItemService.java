package org.taskstodo.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.taskstodo.dao.ISearchItemDAO;
import org.taskstodo.dao.ITaskDAO;
import org.taskstodo.entity.SearchItem;
import org.taskstodo.entity.Task;
import org.taskstodo.service.ISearchItemService;
import org.taskstodo.to.SearchItemTO;

@Service(value = "SearchItemService")
public class SearchItemService extends AbstractService implements ISearchItemService {
    
    @Autowired
    private ISearchItemDAO searchItemDAO;
    @Autowired
    private ITaskDAO taskDAO;
    
    public static final Logger logger = LoggerFactory.getLogger(SearchItemService.class);
    
    public SearchItemService() {}

    @Override
    public SearchItemTO create(SearchItemTO searchItemTO, Long taskId) {
       SearchItem searchItem = mapTO2Entity(SearchItem.class, searchItemTO);
       Task task = taskDAO.findById(taskId);
       task.getSearchItems().add(searchItem);
       searchItem.setTask(task);
       searchItemDAO.create(searchItem);
       return mapEntity2TO(SearchItemTO.class,searchItem);
    }

    @Override
    public void update(final SearchItemTO searchItemTO) {
        SearchItem searchItem = searchItemDAO.findById(searchItemTO.getId());
        searchItem.setValue(searchItemTO.getValue());
        searchItem.setDate(new Date());
        searchItemDAO.update(searchItem);
    }
    
    @Override
    public List<SearchItemTO> findAllByTaskId(final Long taskId) {
        List<SearchItemTO> searchItemTOs = new ArrayList<SearchItemTO>();
        List<SearchItem> searchItems = searchItemDAO.findAllByTaskId(taskId);
        for(SearchItem currentSearchItem : searchItems){
            searchItemTOs.add(mapEntity2TO(SearchItemTO.class, currentSearchItem));
        }
        return searchItemTOs;
    }

    @Override
    public SearchItemTO findBySearchItemId(final Long searchItemId) {
        return mapEntity2TO(SearchItemTO.class, searchItemDAO.findById(searchItemId));
    }

    @Override
    public void delete(final Long searchItemId){
        SearchItem searchItemToRemove = searchItemDAO.findById(searchItemId);
        try {
            searchItemDAO.remove(searchItemToRemove);
        } catch (Exception e) {
           logger.error(e.getLocalizedMessage());
        }
    }
    
    
    public List<SearchItemTO> searchByKeywords(final Long taskId, final List<String> keywords){
    	List<SearchItemTO> searchItemTOs = new ArrayList<SearchItemTO>();
    	
    	
    	return searchItemTOs;
    }
}
