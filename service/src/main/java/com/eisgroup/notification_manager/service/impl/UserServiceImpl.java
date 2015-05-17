package com.eisgroup.notification_manager.service.impl;

import com.eisgroup.notification_manager.dao.UserDAO;
import com.eisgroup.notification_manager.model.User;
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
