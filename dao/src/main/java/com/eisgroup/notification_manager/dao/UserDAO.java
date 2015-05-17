package com.eisgroup.notification_manager.dao;

import com.eisgroup.notification_manager.model.User;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladislav Karpenko
 * Date: 13.05.2015
 * Time: 23:36
 */
public interface UserDAO extends BaseObjectDAO<User>{
    List<User> getAllActiveUsers();
    User findUserByEmail(String email);
    User findUserByMobile(String mobile);
}
