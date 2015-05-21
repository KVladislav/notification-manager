package com.eisgroup.notification_manager.dao.impl;

import com.eisgroup.notification_manager.dao.UserGroupDAO;
import com.eisgroup.notification_manager.model.UserGroup;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladislav Karpenko
 * Date: 20.05.2015
 * Time: 15:21
 */
@Repository
public class UserGroupDAOImpl extends BaseObjectDAOImpl<UserGroup> implements UserGroupDAO{

    public UserGroupDAOImpl() {
        super(UserGroup.class);
    }

    @Override
    public List<UserGroup> getAllActiveGroups() {
        Query query = entityManager.createQuery("select p from UserGroup p where p.isDeleted=false ORDER BY p.groupName");
        return query.getResultList();
    }

    @Override
    public UserGroup getGroupByName(String name) {
        Query query = entityManager.createQuery("select p from UserGroup p where (LOWER(p.groupName) = :name)");
        query.setParameter("name", name.toLowerCase());
        try {
            return (UserGroup) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
