package com.realestate.converter;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

@FacesConverter("currencyConverter")
public class CurrencyConverter implements Converter<BigDecimal> {
    private static final NumberFormat currencyFormat = 
        NumberFormat.getCurrencyInstance(Locale.US);

    @Override
    public BigDecimal getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            String cleanValue = value.replaceAll("[^0-9.]", "");
            if (cleanValue.isEmpty()) {
                throw new jakarta.faces.convert.ConverterException(
                    "Invalid currency format: " + value);
            }
            BigDecimal result = new BigDecimal(cleanValue);
            if (result.compareTo(BigDecimal.ZERO) < 0) {
                throw new jakarta.faces.convert.ConverterException(
                    "Price cannot be negative");
            }
            return result;
        } catch (NumberFormatException e) {
            throw new jakarta.faces.convert.ConverterException(
                "Invalid currency format: " + value, e);
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, BigDecimal value) {
        if (value == null) {
            return "";
        }
        return currencyFormat.format(value);
    }
}

