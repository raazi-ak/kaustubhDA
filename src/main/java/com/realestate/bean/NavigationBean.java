package com.realestate.bean;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import java.io.Serializable;

@Named("navigationBean")
@RequestScoped
public class NavigationBean implements Serializable {
    public String goToHome() {
        return "home?faces-redirect=true";
    }

    public String goToAddListing() {
        return "add-listing?faces-redirect=true";
    }

    public String goToViewListings() {
        return "view-listings?faces-redirect=true";
    }

    public String goToContactSeller() {
        return "contact-seller?faces-redirect=true";
    }

    public String goToAddAgent() {
        return "add-agent?faces-redirect=true";
    }
}

