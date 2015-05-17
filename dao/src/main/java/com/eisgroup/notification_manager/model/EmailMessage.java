package com.eisgroup.notification_manager.model;

import javax.persistence.Entity;

/**
 * Created with IntelliJ IDEA.
 * User: Vladislav Karpenko
 * Date: 13.05.2015
 * Time: 23:43
 */

@Entity
public class EmailMessage extends Message {
    private String subject;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
