package com.eisgroup.notification_manager.service;

import com.eisgroup.notification_manager.model.User;

import java.util.List;

public interface UserService {
    void createUser(User user);
    void saveUser(User user);
    void deleteUser(User user);

    List<User> getAllActiveUsers();
    User findUserByEmail(String email);
    User findUserByMobile(String mobile);

}
