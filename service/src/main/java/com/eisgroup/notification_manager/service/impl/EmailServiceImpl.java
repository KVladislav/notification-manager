package com.eisgroup.notification_manager.service.impl;

import com.eisgroup.notification_manager.dao.MessageDAO;
import com.eisgroup.notification_manager.model.EmailMessage;
import com.eisgroup.notification_manager.model.MessageProvider;
import com.eisgroup.notification_manager.model.MessageStatus;
import com.eisgroup.notification_manager.model.User;
import com.eisgroup.notification_manager.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("emailService")
public class EmailServiceImpl implements EmailService {

    @Autowired
    MessageDAO messageDAO;

    @Autowired
    private MailSender mailSender;

    @Autowired
    MessageProvider messageProvider;

    @Override
    public List<EmailMessage> getAllEmailsByUser(User user) {
        return messageDAO.getAllEmailsByUser(user);
    }


    @Override
    public void sendMultipleEmails(EmailMessage message, List<User> selectedUsers) {
        if (selectedUsers == null) {
            return;
        }
        for (User user : selectedUsers) {
            message.setId(null);
            message.setUser(user);
            message.setAddress(user.geteMail());
            sendEmail(message);
        }
    }

    @Override
    public void sendEmail(EmailMessage emailMessage) {
        try {
            emailMessage.setStatus(MessageStatus.NEW);
            emailMessage.setDispatchDay(new Date());
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(messageProvider.getEmailProviderFrom());
            message.setTo(emailMessage.getAddress());
            message.setSubject(emailMessage.getSubject());
            message.setText(emailMessage.getBody());
            mailSender.send(message);
            emailMessage.setStatus(MessageStatus.SENT);
        } catch (MailSendException e) {
            emailMessage.setStatus(MessageStatus.FAIL);
            emailMessage.setState(e.getMessage());
        }
        messageDAO.create(emailMessage);
    }

}
