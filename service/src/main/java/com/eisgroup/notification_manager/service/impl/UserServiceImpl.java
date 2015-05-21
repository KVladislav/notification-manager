package com.eisgroup.notification_manager.service.impl;

import com.eisgroup.notification_manager.dao.UserDAO;
import com.eisgroup.notification_manager.dao.UserGroupDAO;
import com.eisgroup.notification_manager.model.User;
import com.eisgroup.notification_manager.model.UserGroup;
import com.eisgroup.notification_manager.service.UserService;
import org.primefaces.model.CheckboxTreeNode;
import org.primefaces.model.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        UserGroup existedUserGroup = userGroupDAO.getGroupByName(userGroup.getGroupName());
        if (existedUserGroup != null && existedUserGroup.isDeleted()) {
            existedUserGroup.setGroupName(userGroup.getGroupName());
            existedUserGroup.setIsDeleted(false);
            userGroupDAO.update(existedUserGroup);
        } else if (existedUserGroup == null) {
            userGroupDAO.create(userGroup);
        }
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

    @Override
    public TreeNode getAllActiveUsersNode() {
        TreeNode rootNode = new CheckboxTreeNode(new User(), null);
        Map<UserGroup, List<User>> userMap = new HashMap<>();
        List<User> userList = userDAO.getAllActiveUsers();
        if (userList == null || userList.isEmpty()) {
            return rootNode;
        }

        for (User user : userList) {

            List<User> groupedUsers = userMap.get(user.getUserGroup());
            if (groupedUsers == null) {
                groupedUsers = new ArrayList<>();
            }
            groupedUsers.add(user);
            userMap.put(user.getUserGroup(), groupedUsers);
        }

        for (UserGroup userGroup : userMap.keySet()) {
            User userGroupTitle = new User();
            userGroupTitle.setSurName(userGroup.getGroupName());

            User userGroupInactiveTitle = new User();
            userGroupInactiveTitle.setSurName(userGroup.getGroupName());
            userGroupInactiveTitle.setName("inactive");

            TreeNode groupNode = new CheckboxTreeNode(userGroupTitle, rootNode);
            TreeNode groupInactiveNode = new CheckboxTreeNode(userGroupInactiveTitle, rootNode);
            groupInactiveNode.setSelectable(false);
            List<User> groupedUsers = userMap.get(userGroup);
            for (User user : groupedUsers) {
                TreeNode userNode;
                if (user.isActive()) {
                    userNode = new CheckboxTreeNode(user, groupNode);

                } else {
                    userNode = new CheckboxTreeNode(user, groupInactiveNode);
                }
                userNode.setSelectable(user.isActive());
            }
        }
        return rootNode;
    }
}


