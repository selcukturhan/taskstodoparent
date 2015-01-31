package org.taskstodo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.taskstodo.dao.IAdminDAO;
import org.taskstodo.dao.IRoleDAO;
import org.taskstodo.entity.Role;
import org.taskstodo.entity.User;
import org.taskstodo.service.IAdminService;
import org.taskstodo.to.RoleKeyValueTO;
import org.taskstodo.to.UserTO;



@Service(value = "AdminService")
public class AdminService extends AbstractService implements IAdminService {

	@Autowired
	IAdminDAO adminDAO;
	
	@Autowired
	IRoleDAO roleDAO;
	
	public static final Logger logger = LoggerFactory.getLogger(AdminService.class);

	
	@Override
	public List<RoleKeyValueTO> findAllRoles() {
	    List<RoleKeyValueTO> roles = new ArrayList<RoleKeyValueTO>();
        for (Role role : adminDAO.findAllRoles()) {
            roles.add(mapEntity2TO(RoleKeyValueTO.class, role));
        }
      return roles;
	}

	@Override
	public List<UserTO> findAll() {
		List<UserTO> users = new ArrayList<UserTO>();
		for (User user : adminDAO.findAll()) {
			users.add(mapEntity2TO(UserTO.class, user));
		}
		return users;
	}

	@Override
	public UserTO findByUsername(String username) {
		return mapEntity2TO(UserTO.class,adminDAO.findByUsername(username));
	}

	@Override
	public UserTO create(UserTO user) {
		return mapEntity2TO(UserTO.class, adminDAO.create(mapTO2Entity(User.class, user)));
	}

	@Override
	public UserTO update(UserTO user) {
		return mapEntity2TO(UserTO.class, adminDAO.update(mapTO2Entity(User.class, user)));
	}

	@Override
	public void delete(Long userId) {   
		User userToRemove = adminDAO.findById(userId);
		if(userToRemove != null){
		   adminDAO.remove(userToRemove);
		}
	}

	@Override
	public void addRoleToUser(Long userId, Long roleId) {
		User user = adminDAO.findById(userId);
		if(user != null){
			user.getRoles().add(roleDAO.findById(roleId));
		}
	}

	@Override
	public void deleteRoleForUser(Long userId, Long roleId) {
		User user = adminDAO.findById(userId);
		if(user != null){
		    Role roleToDetach = roleDAO.findById(roleId);
		    user.getRoles().remove(roleToDetach);
		    adminDAO.update(user);
		}
	}
}