package com.realestate.bean;

import com.realestate.dao.PropertyDAO;
import com.realestate.model.Property;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Named("filterBean")
@ViewScoped
public class FilterBean implements Serializable {
    private PropertyDAO propertyDAO = new PropertyDAO();
    private List<Property> filteredProperties;
    private String selectedType;
    private String selectedStatus;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private String sortBy = "listingDate";
    private String sortOrder = "DESC";
    private String searchTerm;

    public void filterProperties() {
        List<Property> allProperties = propertyDAO.findAll();
        
        filteredProperties = allProperties.stream()
            .filter(p -> selectedType == null || selectedType.isEmpty() || 
                        p.getPropertyType().equals(selectedType))
            .filter(p -> selectedStatus == null || selectedStatus.isEmpty() || 
                        p.getStatus().equals(selectedStatus))
            .filter(p -> minPrice == null || p.getPrice().compareTo(minPrice) >= 0)
            .filter(p -> maxPrice == null || p.getPrice().compareTo(maxPrice) <= 0)
            .filter(p -> searchTerm == null || searchTerm.isEmpty() || 
                        p.getTitle().toLowerCase().contains(searchTerm.toLowerCase()) ||
                        p.getAddress().toLowerCase().contains(searchTerm.toLowerCase()))
            .sorted((p1, p2) -> {
                int result = 0;
                switch (sortBy) {
                    case "price":
                        result = p1.getPrice().compareTo(p2.getPrice());
                        break;
                    case "listingDate":
                        result = p1.getListingDate().compareTo(p2.getListingDate());
                        break;
                    case "propertyType":
                        result = p1.getPropertyType().compareTo(p2.getPropertyType());
                        break;
                    default:
                        result = p1.getListingDate().compareTo(p2.getListingDate());
                }
                return "ASC".equals(sortOrder) ? result : -result;
            })
            .collect(Collectors.toList());
    }

    public void onFilterChange(AjaxBehaviorEvent event) {
        filterProperties();
    }

    public void onSortChange(AjaxBehaviorEvent event) {
        filterProperties();
    }

    public List<Property> getFilteredProperties() {
        if (filteredProperties == null) {
            filterProperties();
        }
        return filteredProperties;
    }

    public String getSelectedType() {
        return selectedType;
    }

    public void setSelectedType(String selectedType) {
        this.selectedType = selectedType;
    }

    public String getSelectedStatus() {
        return selectedStatus;
    }

    public void setSelectedStatus(String selectedStatus) {
        this.selectedStatus = selectedStatus;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public String[] getPropertyTypes() {
        return new String[]{"Apartment", "House", "Villa", "Condo", "Townhouse", "Studio", "Land"};
    }

    public String[] getStatuses() {
        return new String[]{"Available", "Sold", "Pending", "Rented"};
    }

    public String[] getSortOptions() {
        return new String[]{"listingDate", "price", "propertyType"};
    }
}

