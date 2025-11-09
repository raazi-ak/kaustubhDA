package com.realestate.dao;

import com.realestate.model.Inquiry;
import com.realestate.util.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class InquiryDAO {
    public void save(Inquiry inquiry) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(inquiry);
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

    public Inquiry findById(Long id) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.find(Inquiry.class, id);
        } finally {
            em.close();
        }
    }

    public List<Inquiry> findAll() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Inquiry> query = em.createQuery("SELECT i FROM Inquiry i", Inquiry.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Inquiry> findByPropertyId(Long propertyId) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Inquiry> query = em.createQuery(
                "SELECT i FROM Inquiry i WHERE i.property.id = :propertyId", Inquiry.class);
            query.setParameter("propertyId", propertyId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Inquiry> findByCustomerId(Long customerId) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Inquiry> query = em.createQuery(
                "SELECT i FROM Inquiry i WHERE i.customer.id = :customerId", Inquiry.class);
            query.setParameter("customerId", customerId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Inquiry> findByAgentId(Long agentId) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Inquiry> query = em.createQuery(
                "SELECT i FROM Inquiry i WHERE i.agent.id = :agentId", Inquiry.class);
            query.setParameter("agentId", agentId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public void update(Inquiry inquiry) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(inquiry);
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

    public void delete(Inquiry inquiry) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Inquiry managedInquiry = em.find(Inquiry.class, inquiry.getId());
            if (managedInquiry != null) {
                em.remove(managedInquiry);
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

