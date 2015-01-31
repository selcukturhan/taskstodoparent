package org.taskstodo.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.taskstodo.dao.IGenericDAO;

abstract class GenericDAO<T, ID extends Serializable> implements IGenericDAO<T, ID> {
    
    protected Class<T> type;
    
    public GenericDAO(Class<T> type) {
        this.type = type;
    }

    @Autowired
    protected SessionFactory sessionFactory;
    
    /* (non-Javadoc)
     * @see org.taskstodo.dao.impl.IGenericDAO#create(T)
     */
    @Override
    public T create(T entity){ 
        sessionFactory.getCurrentSession().save(entity);
        return entity;
    }
    
    /* (non-Javadoc)
     * @see org.taskstodo.dao.impl.IGenericDAO#update(T)
     */
    @Override
    public T update(T entity){
        sessionFactory.getCurrentSession().update(entity);
        return entity;
    }

    /* (non-Javadoc)
     * @see org.taskstodo.dao.impl.IGenericDAO#remove(T)
     */
    @Override
    public void remove(T entity){
        sessionFactory.getCurrentSession().delete(entity);
    }

    /* (non-Javadoc)
     * @see org.taskstodo.dao.impl.IGenericDAO#findById(ID)
     */
    @Override
    @SuppressWarnings("unchecked")
    public T findById(ID id){ 
        return (T) sessionFactory.getCurrentSession().load(type, id);
    }
    
    
    /* (non-Javadoc)
     * @see org.taskstodo.dao.impl.IGenericDAO#findAllByTaskId(ID)
     */
    @Override
    public List<T> findAllByTaskId(ID id) {
        return (List<T>) sessionFactory.getCurrentSession().createCriteria(type).add( Restrictions.eq("task.id", id)).list();
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    
    
}
