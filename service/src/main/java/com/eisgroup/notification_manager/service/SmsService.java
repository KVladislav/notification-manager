package com.eisgroup.notification_manager.service;

import com.eisgroup.notification_manager.model.SMSMessage;
import com.eisgroup.notification_manager.model.User;

import java.util.List;

public interface SmsService {
    void sendSMSMessage(SMSMessage message);
    List<SMSMessage> getAllSmsByUser(User user);
    void checkSMSState(SMSMessage message);
    void sendMultipleSMSMessage(SMSMessage newSMS, List<User> selectedUsers);
}
