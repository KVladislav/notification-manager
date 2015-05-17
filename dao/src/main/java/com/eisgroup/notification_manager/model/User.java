package com.eisgroup.notification_manager.model;

import javax.persistence.Entity;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Vladislav Karpenko
 * Date: 15.05.2015
 * Time: 18:16
 */
@Entity(name = "USERS")
public class User extends BaseObject{
    private String name;
    private String surName;
    private String eMail;
    private String mobileNumber;
    private boolean isDeleted;
    private Date creationDate;

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String sureName) {
        this.surName = sureName;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getFullName(){
        return getName() + " " + getSurName();
    }
}
