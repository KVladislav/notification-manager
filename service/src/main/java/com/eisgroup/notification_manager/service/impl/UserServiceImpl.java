package com.eisgroup.notification_manager.service.impl;

import com.eisgroup.notification_manager.dao.UserDAO;
import com.eisgroup.notification_manager.dao.UserGroupDAO;
import com.eisgroup.notification_manager.model.User;
import com.eisgroup.notification_manager.model.UserGroup;
import com.eisgroup.notification_manager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladislav Karpenko
 * Date: 15.05.2015
 * Time: 19:34
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    UserDAO userDAO;

    @Autowired
    UserGroupDAO userGroupDAO;

    @Override
    public User findUserByEmail(String email) {
        return userDAO.findUserByEmail(email);
    }

    @Override
    public User findUserByMobile(String mobile) {
        return userDAO.findUserByMobile(mobile);
    }

    @Override
    public void createUser(User user) {
        userDAO.create(user);
    }

    @Override
    public List<UserGroup> getAllActiveGroups() {
        return userGroupDAO.getAllActiveGroups();
    }

    @Override
    public void createUserGroup(UserGroup userGroup) {
        userGroupDAO.create(userGroup);
    }

    @Override
    public void saveUserGroup(UserGroup userGroup) {
          userGroupDAO.update(userGroup);
    }

    @Override
    public void deleteUserGroup(UserGroup userGroup) {
        userGroup.setIsDeleted(true);
        saveUserGroup(userGroup);

    }

    @Override
    public UserGroup getUserGroupById(long id) {
        return userGroupDAO.find(id);
    }

    @Override
    public void saveUser(User user) {
        userDAO.update(user);
    }

    @Override
    public void deleteUser(User user) {
        user.setIsDeleted(true);
        saveUser(user);
    }

    @Override
    public List<User> getAllActiveUsers() {
        return userDAO.getAllActiveUsers();
    }
}
