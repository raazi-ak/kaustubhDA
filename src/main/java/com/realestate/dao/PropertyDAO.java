package com.realestate.dao;

import com.realestate.model.Property;
import com.realestate.util.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class PropertyDAO {
    public void save(Property property) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(property);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public Property findById(Long id) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.find(Property.class, id);
        } finally {
            em.close();
        }
    }

    public List<Property> findAll() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Property> query = em.createQuery("SELECT p FROM Property p", Property.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Property> findByType(String propertyType) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Property> query = em.createQuery(
                "SELECT p FROM Property p WHERE p.propertyType = :type", Property.class);
            query.setParameter("type", propertyType);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Property> findByPriceRange(java.math.BigDecimal minPrice, java.math.BigDecimal maxPrice) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Property> query = em.createQuery(
                "SELECT p FROM Property p WHERE p.price BETWEEN :minPrice AND :maxPrice", Property.class);
            query.setParameter("minPrice", minPrice);
            query.setParameter("maxPrice", maxPrice);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Property> findByStatus(String status) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Property> query = em.createQuery(
                "SELECT p FROM Property p WHERE p.status = :status", Property.class);
            query.setParameter("status", status);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public void update(Property property) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(property);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public void delete(Property property) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Property managedProperty = em.find(Property.class, property.getId());
            if (managedProperty != null) {
                em.remove(managedProperty);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
}

