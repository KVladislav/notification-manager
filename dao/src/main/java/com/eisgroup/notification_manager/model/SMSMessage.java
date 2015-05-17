package com.eisgroup.notification_manager.model;

import javax.persistence.Entity;

/**
 * Created with IntelliJ IDEA.
 * User: Vladislav Karpenko
 * Date: 13.05.2015
 * Time: 23:43
 */
@Entity
public class SMSMessage extends Message {

    private String smsId;

    public String getSmsId() {
        return smsId;
    }

    public void setSmsId(String smsId) {
        this.smsId = smsId;
    }
}
