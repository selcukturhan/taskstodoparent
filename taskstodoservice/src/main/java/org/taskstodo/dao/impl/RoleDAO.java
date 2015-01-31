package org.taskstodo.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.taskstodo.dao.IRoleDAO;
import org.taskstodo.entity.Role;

@Repository
@Transactional
public class RoleDAO extends GenericDAO<Role, Long>implements IRoleDAO{
    public RoleDAO() {
        super(Role.class);
    }
 }
