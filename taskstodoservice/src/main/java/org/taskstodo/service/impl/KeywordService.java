package org.taskstodo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.taskstodo.dao.IKeywordDAO;
import org.taskstodo.dao.ITaskDAO;
import org.taskstodo.entity.Keyword;
import org.taskstodo.entity.Task;
import org.taskstodo.service.IKeywordService;
import org.taskstodo.to.KeywordTO;

@Service(value = "KeywordService")
public class KeywordService extends AbstractService implements IKeywordService {
    
    @Autowired
    private IKeywordDAO keywordDAO;
    
    @Autowired
    private ITaskDAO taskDAO;
    
    public static final Logger logger = LoggerFactory.getLogger(KeywordService.class);

    
    public KeywordService() {}

    
    @Override
    public KeywordTO create(KeywordTO keywordTO, Long taskId) {
       Keyword keyword = mapTO2Entity(Keyword.class, keywordTO);
       Task task = taskDAO.findById(taskId);
       task.getKeywords().add(keyword);
       keyword.setTask(task);
       keywordDAO.create(keyword);
       return mapEntity2TO(KeywordTO.class, keyword);
    }

    @Override
    public void update(KeywordTO keywordTO) {
        Keyword keyword = keywordDAO.findById(keywordTO.getId());
        keywordDAO.update(keyword);
    }
    
    @Override
    public List<KeywordTO> findAllByTaskId(Long taskId) {
        List<KeywordTO> keywordTOs = new ArrayList<KeywordTO>();
        List<Keyword> keywords = keywordDAO.findAllByTaskId(taskId);
        for(Keyword currentKeyword : keywords){
            keywordTOs.add(mapEntity2TO(KeywordTO.class, currentKeyword));
        }
        return keywordTOs;
    }

    @Override
    public KeywordTO findByKeywordId(Long keywordId) {
        return mapEntity2TO(KeywordTO.class, keywordDAO.findById(keywordId));
    }

    @Override
    public void delete(Long keywordId){
        Keyword keywordToRemove = keywordDAO.findById(keywordId);
        try {
            keywordDAO.remove(keywordToRemove);
        } catch (Exception e) {
        	logger.error(e.getLocalizedMessage());
        }
    }


	@Override
	public List<String> getTypeahead(String domain) {
		List<Keyword> keywords = keywordDAO.getTypeahead();
		List<String> keywordsStrings = new ArrayList<String>();
		for (Keyword keyword : keywords) {
			keywordsStrings.add(keyword.getValue());
		}
		return keywordsStrings;
	}
}
