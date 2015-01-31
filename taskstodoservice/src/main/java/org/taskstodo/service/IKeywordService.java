package org.taskstodo.service;

import java.util.List;

import org.taskstodo.to.KeywordTO;

public interface IKeywordService {

    public abstract KeywordTO findByKeywordId(Long keywordId);
    public abstract List<KeywordTO> findAllByTaskId(Long taskId);

    public abstract KeywordTO create(KeywordTO keywordTO, Long taskId);
    public abstract void update(KeywordTO keywordTO);
    public abstract void delete(Long keywordId);
	public abstract List<String> getTypeahead(String domain);
}