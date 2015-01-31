package org.taskstodo.service;

import java.util.List;

import org.taskstodo.to.RoleKeyValueTO;
import org.taskstodo.to.UserTO;

public interface IAdminService {

    List<RoleKeyValueTO> findAllRoles();

	List<UserTO> findAll();

	UserTO findByUsername(String username);

	UserTO create(UserTO user);
	
	UserTO update(UserTO user);
	
	void delete(Long userId);

	void addRoleToUser(Long userId, Long roleId);

	void deleteRoleForUser(Long userId, Long roleId);
}
