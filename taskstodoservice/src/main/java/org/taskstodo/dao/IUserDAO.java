package org.taskstodo.dao;

import org.taskstodo.entity.User;

public interface IUserDAO extends IGenericDAO<User, Long>{
    public User findById(Long userId);
    public User findByName(String userName);
    public User create(User user);
    public User update(User user);
    public void remove(User user);
}
