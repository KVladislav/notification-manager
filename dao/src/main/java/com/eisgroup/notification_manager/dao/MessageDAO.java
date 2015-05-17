package com.eisgroup.notification_manager.dao;

import com.eisgroup.notification_manager.model.EmailMessage;
import com.eisgroup.notification_manager.model.Message;
import com.eisgroup.notification_manager.model.SMSMessage;
import com.eisgroup.notification_manager.model.User;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladislav Karpenko
 * Date: 13.05.2015
 * Time: 23:36
 */
public interface MessageDAO extends BaseObjectDAO<Message>{
    List<SMSMessage> getAllSmsByUser(User user);
    List<EmailMessage> getAllEmailsByUser(User user);
}
