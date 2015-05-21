package com.eisgroup.notification_manager.service;

import com.eisgroup.notification_manager.model.User;
import com.eisgroup.notification_manager.model.UserGroup;
import org.primefaces.model.TreeNode;

import java.util.List;

public interface UserService {
    void createUser(User user);
    void saveUser(User user);
    void deleteUser(User user);

    List<User> getAllActiveUsers();
    User findUserByEmail(String email);
    User findUserByMobile(String mobile);

    List<UserGroup> getAllActiveGroups();
    void createUserGroup(UserGroup userGroup);
    void saveUserGroup(UserGroup userGroup);
    void deleteUserGroup(UserGroup userGroup);
    UserGroup getUserGroupById(long id);
    TreeNode getAllActiveUsersNode();

}
