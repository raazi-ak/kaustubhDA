package com.realestate.bean;

import com.realestate.dao.CustomerDAO;
import com.realestate.dao.InquiryDAO;
import com.realestate.dao.PropertyDAO;
import com.realestate.model.Customer;
import com.realestate.model.Inquiry;
import com.realestate.model.Property;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.time.LocalDate;

@Named("inquiryBean")
@SessionScoped
public class InquiryBean implements Serializable {
    private Inquiry inquiry = new Inquiry();
    private Customer customer = new Customer();
    private InquiryDAO inquiryDAO = new InquiryDAO();
    private CustomerDAO customerDAO = new CustomerDAO();
    private PropertyDAO propertyDAO = new PropertyDAO();
    private Long selectedPropertyId;

    public String submitInquiry() {
        try {
            if (selectedPropertyId == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                        "Error", "Please select a property"));
                return null;
            }

            Property property = propertyDAO.findById(selectedPropertyId);
            if (property == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                        "Error", "Property not found"));
                return null;
            }

            Customer existingCustomer = customerDAO.findByEmail(customer.getEmail());
            if (existingCustomer != null) {
                customer = existingCustomer;
            } else {
                customerDAO.save(customer);
            }

            inquiry.setProperty(property);
            inquiry.setCustomer(customer);
            inquiry.setAgent(property.getAgent());
            inquiry.setInquiryDate(LocalDate.now());
            inquiry.setStatus("New");

            inquiryDAO.save(inquiry);

            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, 
                    "Success", "Inquiry submitted successfully!"));
            
            inquiry = new Inquiry();
            customer = new Customer();
            selectedPropertyId = null;

            return "view-listings?faces-redirect=true";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Error", "Failed to submit inquiry: " + e.getMessage()));
            return null;
        }
    }

    public Inquiry getInquiry() {
        return inquiry;
    }

    public void setInquiry(Inquiry inquiry) {
        this.inquiry = inquiry;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Long getSelectedPropertyId() {
        return selectedPropertyId;
    }

    public void setSelectedPropertyId(Long selectedPropertyId) {
        this.selectedPropertyId = selectedPropertyId;
    }

    public java.util.List<Property> getAvailableProperties() {
        return propertyDAO.findByStatus("Available");
    }
}

