package org.taskstodo.dao;

import java.util.List;

import org.taskstodo.entity.Role;
import org.taskstodo.entity.User;

public interface IAdminDAO extends IGenericDAO<User, Long>{

	List<Role> findAllRoles();

	List<User> findAll();

	User findByUsername(String username);

	User create(User user);
	
	User update(User user);
	
	void remove(User user);
}
