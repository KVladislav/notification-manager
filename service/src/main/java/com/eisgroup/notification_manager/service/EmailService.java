package com.eisgroup.notification_manager.service;

import com.eisgroup.notification_manager.model.EmailMessage;
import com.eisgroup.notification_manager.model.User;

import java.util.List;

public interface EmailService {
    void sendEmail(EmailMessage message);
    List<EmailMessage> getAllEmailsByUser(User user);
    void sendMultipleEmails(EmailMessage message, List<User> selectedUsers);
}
