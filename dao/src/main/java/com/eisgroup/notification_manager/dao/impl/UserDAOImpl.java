package com.eisgroup.notification_manager.dao.impl;

import com.eisgroup.notification_manager.dao.UserDAO;
import com.eisgroup.notification_manager.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladislav Karpenko
 * Date: 13.05.2015
 * Time: 23:45
 */

@Repository
public class UserDAOImpl extends BaseObjectDAOImpl<User> implements UserDAO {
    public UserDAOImpl(){super(User.class);}

    @Override
    public List<User> getAllActiveUsers() {
        Query query = entityManager.createQuery("select p from USERS p where p.isDeleted=false ORDER BY p.userGroup.groupName, p.surName, p.id");
        return query.getResultList();
    }

    @Override
    public User findUserByEmail(String email) {
        Query query = entityManager.createQuery("select user from USERS user where (LOWER(user.eMail) = :email and user.isDeleted=false)");
        query.setParameter("email", email.toLowerCase());
        try {
            return (User) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public User findUserByMobile(String mobile) {
        Query query = entityManager.createQuery("select user from USERS user where (LOWER(user.mobileNumber) = :mobileNumber and user.isDeleted=false)");
        query.setParameter("mobileNumber", mobile.toLowerCase());
        try {
            return (User) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
