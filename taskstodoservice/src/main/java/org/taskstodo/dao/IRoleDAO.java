package org.taskstodo.dao;

import java.util.List;

import org.taskstodo.entity.Role;

public interface IRoleDAO {
	public abstract Role create(Role role);

    public abstract Role update(Role role);

    public abstract Role findById(Long roleId);

    public abstract void remove(Role role);

    public abstract List<Role> findAllByTaskId(Long roleId);

}
