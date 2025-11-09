package com.realestate.dao;

import com.realestate.model.Agent;
import com.realestate.util.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class AgentDAO {
    public void save(Agent agent) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(agent);
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

    public Agent findById(Long id) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.find(Agent.class, id);
        } finally {
            em.close();
        }
    }

    public List<Agent> findAll() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Agent> query = em.createQuery("SELECT a FROM Agent a", Agent.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public Agent findByEmail(String email) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Agent> query = em.createQuery(
                "SELECT a FROM Agent a WHERE a.email = :email", Agent.class);
            query.setParameter("email", email);
            List<Agent> results = query.getResultList();
            return results.isEmpty() ? null : results.get(0);
        } finally {
            em.close();
        }
    }

    public void update(Agent agent) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(agent);
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

    public void delete(Agent agent) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Agent managedAgent = em.find(Agent.class, agent.getId());
            if (managedAgent != null) {
                em.remove(managedAgent);
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

