package org.taskstodo.service.impl;

import javax.persistence.Entity;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.taskstodo.dao.IUserDAO;

public class UserSecurityContraintAspect {
    
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
    private IUserDAO userDAO;

	public static final Logger logger = LoggerFactory.getLogger(UserSecurityContraintAspect.class);

	
	public boolean isUserAuthorized(Entity entity){
//		Long id = entity.
//		sessionFactory.getSession().
	    return true;
	}
	
	public IUserDAO getUserDAO() {
        return userDAO;
    }

    public void setUserDAO(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

}
