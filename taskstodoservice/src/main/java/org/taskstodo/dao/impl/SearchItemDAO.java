package org.taskstodo.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.taskstodo.dao.ISearchItemDAO;
import org.taskstodo.entity.SearchItem;

@Repository
@Transactional
public class SearchItemDAO extends GenericDAO<SearchItem, Long>implements ISearchItemDAO {
    public SearchItemDAO() {
        super(SearchItem.class);
    }
}
