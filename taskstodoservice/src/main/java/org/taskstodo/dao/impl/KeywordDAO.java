package org.taskstodo.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.taskstodo.dao.IKeywordDAO;
import org.taskstodo.entity.Keyword;
import org.taskstodo.entity.Task;

@Repository
@Transactional
public class KeywordDAO extends GenericDAO<Keyword, Long>implements IKeywordDAO {
    public KeywordDAO() {
        super(Keyword.class);
    }

	@Override
	public List<Keyword> getTypeahead() {
		//todo: distinct serach
		return sessionFactory.getCurrentSession().createCriteria(Keyword.class).list();
	}
}
