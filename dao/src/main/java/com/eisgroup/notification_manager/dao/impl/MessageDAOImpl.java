package com.eisgroup.notification_manager.dao.impl;

import com.eisgroup.notification_manager.dao.MessageDAO;
import com.eisgroup.notification_manager.model.EmailMessage;
import com.eisgroup.notification_manager.model.Message;
import com.eisgroup.notification_manager.model.SMSMessage;
import com.eisgroup.notification_manager.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladislav Karpenko
 * Date: 13.05.2015
 * Time: 23:45
 */

@Repository
public class MessageDAOImpl extends BaseObjectDAOImpl<Message> implements MessageDAO {
    public MessageDAOImpl(){super(Message.class);}

    @Override
    public List<SMSMessage> getAllSmsByUser(User user) {
        Query query = entityManager.createQuery("select p from Message p where (p.user = :user and MESSAGE_TYPE='SMSMessage') ORDER BY p.id");
        query.setParameter("user", user);
        return query.getResultList();
    }

    @Override
    public List<EmailMessage> getAllEmailsByUser(User user) {
        Query query = entityManager.createQuery("select p from Message p where (p.user = :user and MESSAGE_TYPE='EmailMessage') ORDER BY p.id");
        query.setParameter("user", user);
        return query.getResultList();
    }
}
