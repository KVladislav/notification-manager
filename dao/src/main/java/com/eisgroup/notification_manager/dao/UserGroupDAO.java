package com.eisgroup.notification_manager.dao;

import com.eisgroup.notification_manager.model.UserGroup;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladislav Karpenko
 * Date: 13.05.2015
 * Time: 23:36
 */
public interface UserGroupDAO extends BaseObjectDAO<UserGroup>{
    List<UserGroup> getAllActiveGroups();
    UserGroup getGroupByName(String name);
}
