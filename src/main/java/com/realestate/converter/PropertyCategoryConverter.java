package com.realestate.converter;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;

@FacesConverter("propertyCategoryConverter")
public class PropertyCategoryConverter implements Converter<String> {
    private static final String[] CATEGORIES = {
        "Apartment", "House", "Villa", "Condo", "Townhouse", "Studio", "Land"
    };

    @Override
    public String getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        for (String category : CATEGORIES) {
            if (category.equalsIgnoreCase(value)) {
                return category;
            }
        }
        return value;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, String value) {
        if (value == null) {
            return "";
        }
        return value;
    }

    public static String[] getCategories() {
        return CATEGORIES.clone();
    }
}

