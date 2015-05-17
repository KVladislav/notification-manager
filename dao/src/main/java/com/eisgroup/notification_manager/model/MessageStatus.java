package com.eisgroup.notification_manager.model;

/**
 * Created with IntelliJ IDEA.
 * User: Vladislav Karpenko
 * Date: 13.05.2015
 * Time: 22:49
 */
public enum MessageStatus {
    NEW("New"),
    SENDING("Sending"),
    RETRY("Retry"),
    FAIL("Fail"),
    SENT("Sent"),
    DELIVERED("Delivered");

    private final String name;

    MessageStatus(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
