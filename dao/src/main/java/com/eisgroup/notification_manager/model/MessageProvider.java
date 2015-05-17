package com.eisgroup.notification_manager.model;

/**
 * Created with IntelliJ IDEA.
 * User: Vladislav Karpenko
 * Date: 17.05.2015
 * Time: 10:59
 */
public class MessageProvider {
    private String smsProviderURL;
    private String smsProviderKey;
    private String smsProviderSender;
    private String emailProviderHost;
    private String emailProviderPort;
    private String emailProviderFrom;
    private String emailProviderLogin;
    private String emailProviderPassword;


    public String getEmailProviderHost() {
        return emailProviderHost;
    }

    public void setEmailProviderHost(String emailProviderHost) {
        this.emailProviderHost = emailProviderHost;
    }

    public String getEmailProviderPort() {
        return emailProviderPort;
    }

    public void setEmailProviderPort(String emailProviderPort) {
        this.emailProviderPort = emailProviderPort;
    }

    public String getEmailProviderLogin() {
        return emailProviderLogin;
    }

    public void setEmailProviderLogin(String emailProviderLogin) {
        this.emailProviderLogin = emailProviderLogin;
    }

    public String getEmailProviderPassword() {
        return emailProviderPassword;
    }

    public void setEmailProviderPassword(String emailProviderPassword) {
        this.emailProviderPassword = emailProviderPassword;
    }

    public String getSmsProviderSender() {
        return smsProviderSender;
    }

    public void setSmsProviderSender(String smsProviderSender) {
        this.smsProviderSender = smsProviderSender;
    }

    public String getSmsProviderURL() {
        return smsProviderURL;
    }

    public void setSmsProviderURL(String smsProviderURL) {
        this.smsProviderURL = smsProviderURL;
    }

    public String getSmsProviderKey() {
        return smsProviderKey;
    }

    public void setSmsProviderKey(String smsProviderKey) {
        this.smsProviderKey = smsProviderKey;
    }

    public String getEmailProviderFrom() {
        return emailProviderFrom;
    }

    public void setEmailProviderFrom(String emailProviderFrom) {
        this.emailProviderFrom = emailProviderFrom;
    }

}
