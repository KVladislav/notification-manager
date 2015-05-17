package com.eisgroup.notification_manager.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Vladislav Karpenko
 * Date: 13.05.2015
 * Time: 22:40
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "MESSAGE_TYPE")
public abstract class Message extends BaseObject{

    private String address;

    private String body;

    private String state;

    private Date dispatchDay;

    @Enumerated(EnumType.STRING)
    MessageStatus status;

    @ManyToOne
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getDispatchDay() {
        return dispatchDay;
    }

    public void setDispatchDay(Date dispatchDay) {
        this.dispatchDay = dispatchDay;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus messageStatus) {
        this.status = messageStatus;
    }
}
