package com.eisgroup.notification_manager.dao;


import com.eisgroup.notification_manager.model.BaseObject;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladislav Karpenko
 * Date: 20.01.2015
 * Time: 12:19
 */
public interface BaseObjectDAO<T extends BaseObject> extends Serializable {
    void create(T object);

    void update(T object);

    T find(long id);

    List<T> getAll();


    void delete(T object);

}
