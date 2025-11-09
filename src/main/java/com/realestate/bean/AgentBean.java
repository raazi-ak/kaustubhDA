package com.realestate.bean;

import com.realestate.dao.AgentDAO;
import com.realestate.model.Agent;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named("agentBean")
@SessionScoped
public class AgentBean implements Serializable {
    private Agent agent = new Agent();
    private AgentDAO agentDAO = new AgentDAO();
    private List<Agent> agents;

    public String addAgent() {
        try {
            Agent existingAgent = agentDAO.findByEmail(agent.getEmail());
            if (existingAgent != null) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                        "Error", "An agent with this email already exists"));
                return null;
            }
            
            agentDAO.save(agent);
            agent = new Agent();
            loadAgents();
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, 
                    "Success", "Agent added successfully!"));
            return "add-listing?faces-redirect=true";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Error", "Failed to add agent: " + e.getMessage()));
            return null;
        }
    }

    public void loadAgents() {
        agents = agentDAO.findAll();
    }

    public List<Agent> getAgents() {
        if (agents == null) {
            loadAgents();
        }
        return agents;
    }

    public boolean hasAgents() {
        List<Agent> agentList = getAgents();
        return agentList != null && !agentList.isEmpty();
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public String navigateToAddAgent() {
        agent = new Agent();
        return "add-agent?faces-redirect=true";
    }
}

