package com.eisgroup.notification_manager.ui.beans;

import com.eisgroup.notification_manager.model.EmailMessage;
import com.eisgroup.notification_manager.model.SMSMessage;
import com.eisgroup.notification_manager.model.User;
import com.eisgroup.notification_manager.service.EmailService;
import com.eisgroup.notification_manager.service.SmsService;
import com.eisgroup.notification_manager.service.UserService;
import com.eisgroup.notification_manager.ui.validators.EmailValidator;
import com.eisgroup.notification_manager.ui.validators.MobileValidator;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

@ManagedBean
@ViewScoped
public class UsersBean implements Serializable {

    @ManagedProperty(value = "#{userService}")
    UserService userService;

    @ManagedProperty(value = "#{smsService}")
    SmsService smsService;

    @ManagedProperty(value = "#{emailService}")
    EmailService emailService;

    private boolean iSSmsMessageType;
    private User user;
    private SMSMessage newSMS;
    private EmailMessage newEmail;
    private List<User> users;
    private List<SMSMessage> smsMessageList;
    private List<EmailMessage> EmailList;
    private boolean isMultipleDelivery;
    private List<User> selectedUsers;

    @PostConstruct
    public void init() {
        users = userService.getAllActiveUsers();
        setiSSmsMessageType(true);
        setIsMultipleDelivery(false);
    }


    public void sendMessage() {

        if (iSSmsMessageType()) {
            getNewSMS().setBody(getNewSMS().getBody().trim());
            if (getSelectedUsers() != null && getSelectedUsers().size() > 0 && isMultipleDelivery()) {
                smsService.sendMultipleSMSMessage(getNewSMS(), getSelectedUsers());
            } else if (getNewSMS().getUser() != null) {
                smsService.sendSMSMessage(getNewSMS());
            }
        } else {
            getNewEmail().setBody(getNewEmail().getBody().trim());
            getNewEmail().setSubject(getNewEmail().getSubject().trim());
            if (getSelectedUsers() != null && getSelectedUsers().size() > 0 && isMultipleDelivery()) {
                emailService.sendMultipleEmails(getNewEmail(), getSelectedUsers());
            } else if (getNewEmail().getUser() != null) {
                emailService.sendEmail(getNewEmail());
            }
        }
        setNewSMS(null);
        setNewEmail(null);
        setSelectedUsers(null);
        setIsMultipleDelivery(false);
    }

    public void prepareNewMessage(User user) {
        if (user != null && !isMultipleDelivery) {
            setNewEmail(new EmailMessage());
            setNewSMS(new SMSMessage());
            getNewSMS().setUser(user);
            getNewEmail().setUser(user);
            getNewSMS().setAddress(user.getMobileNumber());
            getNewEmail().setAddress(user.geteMail());
        } else if (isMultipleDelivery) {
            setNewSMS(new SMSMessage());
            setNewEmail(new EmailMessage());
        }


    }

    public void prepareMessageLog(User user) {
        setUser(user);
        setSmsMessageList(smsService.getAllSmsByUser(user));
        setEmailList(emailService.getAllEmailsByUser(user));
    }

    public void startCreateEdit(User user) {
        if (user == null) {
            user = new User();
        }
        setUser(user);
    }

    public void deleteUser() {
        userService.deleteUser(getUser());
        init();
    }

    public void emailValidator(FacesContext context, UIComponent component, Object value) {
        String email = ((String) value).trim();
        new EmailValidator().validate(context, component, value);
        if (!isUniqueEmail(email)) {
            ResourceBundle resourceBundle = PropertyResourceBundle.getBundle("messages/messages");
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "", resourceBundle.getString("Error.notUniqueEmail")));
        }
    }

    public void refreshSMSState(SMSMessage smsMessage) {
        smsService.checkSMSState(smsMessage);
        prepareMessageLog(getUser());
    }

    private boolean isUniqueEmail(String email) {
        User dbUser = userService.findUserByEmail(email);
        return !(dbUser != null && (getUser().getId() == null || !getUser().getId().equals(dbUser.getId())));
    }

    public void mobileValidator(FacesContext context, UIComponent component, Object value) {
        String mobile = ((String) value).trim();
        new MobileValidator().validate(context, component, value);
        if (!isUniqueMobile(mobile)) {
            ResourceBundle resourceBundle = PropertyResourceBundle.getBundle("messages/messages");
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "", resourceBundle.getString("Error.notUniqueMobile")));
        }
    }

    private boolean isUniqueMobile(String mobile) {
        User dbUser = userService.findUserByMobile(mobile);
        return !(dbUser != null && (getUser().getId() == null || !getUser().getId().equals(dbUser.getId())));
    }

    public void create() {
        getUser().setCreationDate(new Date());
        getUser().seteMail(getUser().geteMail().trim());
        getUser().setName(getUser().getName().trim());
        getUser().setSurName(getUser().getSurName().trim());
        getUser().setMobileNumber(getUser().getMobileNumber().trim());
        userService.createUser(getUser());
        init();
    }

    public void save() {
        getUser().setCreationDate(new Date());
        getUser().seteMail(getUser().geteMail().trim());
        getUser().setName(getUser().getName().trim());
        getUser().setSurName(getUser().getSurName().trim());
        getUser().setMobileNumber(getUser().getMobileNumber().trim());
        userService.saveUser(getUser());
        init();
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setSmsService(SmsService smsService) {
        this.smsService = smsService;
    }

    public List<User> getUsers() {
        return users;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public SMSMessage getNewSMS() {
        return newSMS;
    }

    public void setNewSMS(SMSMessage newSMS) {
        this.newSMS = newSMS;
    }

    public List<SMSMessage> getSmsMessageList() {
        return smsMessageList;
    }

    public void setSmsMessageList(List<SMSMessage> smsMessageList) {
        this.smsMessageList = smsMessageList;
    }

    public boolean isMultipleDelivery() {
        return isMultipleDelivery;
    }

    public void setIsMultipleDelivery(boolean isMultipleDelivery) {
        setUser(null);
        this.isMultipleDelivery = isMultipleDelivery;
    }

    public List<User> getSelectedUsers() {
        return selectedUsers;
    }

    public void setSelectedUsers(List<User> selectedUsers) {
        this.selectedUsers = selectedUsers;
    }

    public EmailMessage getNewEmail() {
        return newEmail;
    }

    public void setNewEmail(EmailMessage newEmail) {
        this.newEmail = newEmail;
    }

    public boolean iSSmsMessageType() {
        return iSSmsMessageType;
    }

    public boolean getiSSmsMessageType() {
        return iSSmsMessageType;
    }

    public void setiSSmsMessageType(boolean iSSmsMessageType) {
        this.iSSmsMessageType = iSSmsMessageType;
    }

    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }

    public List<EmailMessage> getEmailList() {
        return EmailList;
    }

    public void setEmailList(List<EmailMessage> emailList) {
        EmailList = emailList;
    }
}
