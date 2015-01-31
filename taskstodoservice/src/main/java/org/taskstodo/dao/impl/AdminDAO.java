package org.taskstodo.dao.impl;

import java.util.List;

import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.taskstodo.dao.IAdminDAO;
import org.taskstodo.entity.Role;
import org.taskstodo.entity.Task;
import org.taskstodo.entity.User;
@Repository
@Transactional
public class AdminDAO extends GenericDAO<User, Long>implements IAdminDAO {

    
    
    public AdminDAO() {
       super(User.class);
    }
    
    
	@Override
	public List<Role> findAllRoles() {
		return (List<Role>) sessionFactory.getCurrentSession().createCriteria(Role.class).list();
	}

	@Override
	public List<User> findAll() {
	    return (List<User>) sessionFactory.getCurrentSession().createCriteria(User.class).list();
	}

	
	@Override
	public User findById(Long id) {
	    return (User) sessionFactory.getCurrentSession().createCriteria(User.class).add(Restrictions.idEq(id))
	            .setFetchMode("roles", FetchMode.JOIN).uniqueResult();
	}
	
	@Override
	public User findByUsername(String username) {
		// TODO Auto-generated method stub
		return null;
	}

}
