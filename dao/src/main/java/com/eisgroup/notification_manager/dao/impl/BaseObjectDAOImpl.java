package com.eisgroup.notification_manager.dao.impl;


import com.eisgroup.notification_manager.dao.BaseObjectDAO;
import com.eisgroup.notification_manager.model.BaseObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladislav Karpenko
 * Date: 20.01.2015
 * Time: 12:22
 */
@Transactional
public abstract class BaseObjectDAOImpl<T extends BaseObject> implements BaseObjectDAO<T> {

    private static final Logger LOG = LoggerFactory.getLogger(BaseObjectDAOImpl.class);
    private Class<T> type;
    @PersistenceContext
    protected EntityManager entityManager;

    public BaseObjectDAOImpl(Class<T> type) {
        this.type = type;
    }

    @Override
    public void create(T object) {
        LOG.info("create started");
        long tStart = System.nanoTime();
        entityManager.persist(object);
        long tEnd = System.nanoTime();
        long tRes = tEnd - tStart;
        LOG.info("create finished, time: " + tRes / 1000000 + " ms");
    }

    @Override
    public void update(T object) {
        LOG.info("update started");
        long tStart = System.nanoTime();
        entityManager.merge(object);
        long tEnd = System.nanoTime();
        long tRes = tEnd - tStart;
        LOG.info("update finished, time: " + tRes / 1000000 + " ms");

    }

    @Override
    public List<T> getAll() {
        LOG.info("getAll started");
        long tStart = System.nanoTime();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteria = criteriaBuilder.createQuery(type);
        Root<T> root = criteria.from(type);
        criteria.orderBy(criteriaBuilder.asc(root.get("id")));
        List<T> list = entityManager.createQuery(criteria).getResultList();
        long tEnd = System.nanoTime();
        long tRes = tEnd - tStart;
        LOG.info("getAll finished, time: " + tRes / 1000000 + " ms");
        return list;

    }


    @Override
    public T find(long id) {
        LOG.info("find started, param: " + id);
        long tStart = System.nanoTime();
        T result = entityManager.find(type, id);
        long tEnd = System.nanoTime();
        long tRes = tEnd - tStart;
        LOG.info("find finished, time: " + tRes / 1000000 + " ms");

        return result;

    }

    @Override
    public void delete(T object) {
        LOG.info("delete started");
        long tStart = System.nanoTime();
        entityManager.remove(object);
        long tEnd = System.nanoTime();
        long tRes = tEnd - tStart;
        LOG.info("delete finished, time: " + tRes / 1000000 + " ms");
    }
}
