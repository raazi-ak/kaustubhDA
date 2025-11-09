package com.realestate.bean;

import com.realestate.dao.AgentDAO;
import com.realestate.dao.PropertyDAO;
import com.realestate.model.Agent;
import com.realestate.model.Property;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Named("propertyBean")
@SessionScoped
public class PropertyBean implements Serializable {
    private Property property = new Property();
    private PropertyDAO propertyDAO = new PropertyDAO();
    private AgentDAO agentDAO = new AgentDAO();
    private List<Property> properties;
    private Long selectedAgentId;

    public PropertyBean() {
        loadProperties();
    }

    public String addProperty() {
        try {
            if (selectedAgentId != null) {
                Agent agent = agentDAO.findById(selectedAgentId);
                if (agent != null) {
                    property.setAgent(agent);
                    property.setListingDate(LocalDate.now());
                    if (property.getStatus() == null || property.getStatus().isEmpty()) {
                        property.setStatus("Available");
                    }
                    propertyDAO.save(property);
                    property = new Property();
                    loadProperties();
                    FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, 
                            "Success", "Property listed successfully!"));
                    return "view-listings?faces-redirect=true";
                }
            }
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Error", "Please select an agent"));
            return null;
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Error", "Failed to add property: " + e.getMessage()));
            return null;
        }
    }

    public void loadProperties() {
        properties = propertyDAO.findAll();
    }

    public List<Property> getProperties() {
        if (properties == null) {
            loadProperties();
        }
        return properties;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public Long getSelectedAgentId() {
        return selectedAgentId;
    }

    public void setSelectedAgentId(Long selectedAgentId) {
        this.selectedAgentId = selectedAgentId;
    }

    public List<Agent> getAgents() {
        return agentDAO.findAll();
    }

    public boolean hasAgents() {
        List<Agent> agents = getAgents();
        return agents != null && !agents.isEmpty();
    }

    public String navigateToAddListing() {
        property = new Property();
        return "add-listing?faces-redirect=true";
    }
}

