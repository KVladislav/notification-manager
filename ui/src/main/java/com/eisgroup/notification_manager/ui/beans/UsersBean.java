package com.eisgroup.notification_manager.ui.beans;

import com.eisgroup.notification_manager.model.EmailMessage;
import com.eisgroup.notification_manager.model.SMSMessage;
import com.eisgroup.notification_manager.model.User;
import com.eisgroup.notification_manager.model.UserGroup;
import com.eisgroup.notification_manager.service.EmailService;
import com.eisgroup.notification_manager.service.SmsService;
import com.eisgroup.notification_manager.service.UserService;
import com.eisgroup.notification_manager.ui.validators.EmailValidator;
import com.eisgroup.notification_manager.ui.validators.MobileValidator;
import org.primefaces.model.TreeNode;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import java.io.Serializable;
import java.util.*;

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
    private List<UserGroup> groupList;
    private UserGroup newGroup;
    private boolean isGroupEdited;
    private TreeNode userListNode;
    private TreeNode[] selectedNodes;
    private TreeNode userNode;

    @PostConstruct
    public void init() {
        users = userService.getAllActiveUsers();
        userListNode = userService.getAllActiveUsersNode();
        setiSSmsMessageType(true);
        setIsMultipleDelivery(false);
        groupList = userService.getAllActiveGroups();
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
        if (user != null) {
            setIsMultipleDelivery(false);
            setNewEmail(new EmailMessage());
            setNewSMS(new SMSMessage());
            getNewSMS().setUser(user);
            getNewEmail().setUser(user);
            getNewSMS().setAddress(user.getMobileNumber());
            getNewEmail().setAddress(user.geteMail());
        } else {
            setNewSMS(new SMSMessage());
            setIsMultipleDelivery(true);
            setNewEmail(new EmailMessage());
            selectedUsers = new ArrayList<>();
            for (TreeNode selectedUserNode : selectedNodes) {
                User selectedUser = (User) selectedUserNode.getData();
                if (selectedUser.getId() == null) {
                    continue;
                }
                selectedUsers.add(selectedUser);
            }
        }
    }

    public void startCreateEditGroup(UserGroup group) {
        if (group != null) {
            setNewGroup(group);
        } else {
            setNewGroup(new UserGroup());
        }
        setIsGroupEdited(true);
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

    public void deleteGroup() {
        userService.deleteUserGroup(getNewGroup());
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

    public void createUser() {
        getUser().setCreationDate(new Date());
        getUser().seteMail(getUser().geteMail().trim());
        getUser().setName(getUser().getName().trim());
        getUser().setSurName(getUser().getSurName().trim());
        getUser().setMobileNumber(getUser().getMobileNumber().trim());
        userService.createUser(getUser());
        init();
    }

    public void createGroup() {
        getNewGroup().setGroupName(getNewGroup().getGroupName().trim());
        getNewGroup().setDescription(getNewGroup().getDescription().trim());
        userService.createUserGroup(getNewGroup());
        setIsGroupEdited(false);
        init();
    }

    public void updateUser() {
        getUser().setCreationDate(new Date());
        getUser().seteMail(getUser().geteMail().trim());
        getUser().setName(getUser().getName().trim());
        getUser().setSurName(getUser().getSurName().trim());
        getUser().setMobileNumber(getUser().getMobileNumber().trim());
        userService.saveUser(getUser());
        init();
    }

    public void updateGroup() {
        getNewGroup().setGroupName(getNewGroup().getGroupName().trim());
        getNewGroup().setDescription(getNewGroup().getDescription().trim());
        userService.saveUserGroup(getNewGroup());
        setIsGroupEdited(false);
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

    public List<UserGroup> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<UserGroup> groupList) {
        this.groupList = groupList;
    }

    public UserGroup getNewGroup() {
        return newGroup;
    }

    public void setNewGroup(UserGroup newGroup) {
        this.newGroup = newGroup;
    }

    public boolean isGroupEdited() {
        return isGroupEdited;
    }

    public void setIsGroupEdited(boolean isGroupEdited) {
        this.isGroupEdited = isGroupEdited;
    }

    public TreeNode getUserListNode() {
        return userListNode;
    }

    public void setUserListNode(TreeNode userListNode) {
        this.userListNode = userListNode;
    }


    public TreeNode getUserNode() {
        return userNode;
    }

    public void setUserNode(TreeNode userNode) {
        this.userNode = userNode;
    }

    public void setSelectedNodes(TreeNode[] selectedNodes) {
        if (selectedNodes == null) {
            return;
        }
        List<TreeNode> selectedNodesList = new ArrayList<>();
        for (TreeNode node : selectedNodes) {
            if (node.isSelectable()) {
                selectedNodesList.add(node);
            } else {
                selectedNodesList.remove(node.getParent());
                node.setSelected(false);
            }
        }
        this.selectedNodes = selectedNodesList.toArray(new TreeNode[selectedNodesList.size()]);

        System.out.println(this.selectedNodes.length);
    }

    public TreeNode[] getSelectedNodes() {
        return selectedNodes;
    }
}
