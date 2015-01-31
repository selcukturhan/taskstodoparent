package org.taskstodo.dao.impl;

import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.taskstodo.dao.IUserDAO;
import org.taskstodo.entity.User;

@Repository
@Transactional
public class UserDAO extends GenericDAO<User, Long>implements IUserDAO{

    public UserDAO(){
        super(User.class);
    }
    
    @Override
    public User findByName(String userName) {
        return (User) sessionFactory.getCurrentSession().createCriteria(User.class).
        		setFetchMode("roles", FetchMode.JOIN).add( Restrictions.eq("username", userName)).uniqueResult();
    }
 }
