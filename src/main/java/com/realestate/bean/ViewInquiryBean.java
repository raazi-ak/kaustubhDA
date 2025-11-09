package com.realestate.bean;

import com.realestate.dao.InquiryDAO;
import com.realestate.dao.PropertyDAO;
import com.realestate.model.Inquiry;
import com.realestate.model.Property;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named("viewInquiryBean")
@SessionScoped
public class ViewInquiryBean implements Serializable {
    private InquiryDAO inquiryDAO = new InquiryDAO();
    private PropertyDAO propertyDAO = new PropertyDAO();
    private Long selectedPropertyId;
    private Property selectedProperty;
    private List<Inquiry> inquiries;

    public void loadInquiries() {
        if (selectedPropertyId != null) {
            selectedProperty = propertyDAO.findById(selectedPropertyId);
            if (selectedProperty != null) {
                inquiries = inquiryDAO.findByPropertyId(selectedPropertyId);
            }
        }
    }

    public String viewPropertyInquiries() {
        loadInquiries();
        return "view-inquiries?faces-redirect=true";
    }
    
    public void setPropertyId(Long propertyId) {
        selectedPropertyId = propertyId;
    }

    public List<Inquiry> getInquiries() {
        if (inquiries == null && selectedPropertyId != null) {
            loadInquiries();
        }
        return inquiries;
    }

    public Property getSelectedProperty() {
        if (selectedProperty == null && selectedPropertyId != null) {
            selectedProperty = propertyDAO.findById(selectedPropertyId);
        }
        return selectedProperty;
    }

    public Long getSelectedPropertyId() {
        return selectedPropertyId;
    }

    public void setSelectedPropertyId(Long selectedPropertyId) {
        this.selectedPropertyId = selectedPropertyId;
    }

    public int getInquiryCount() {
        return inquiries != null ? inquiries.size() : 0;
    }
}

