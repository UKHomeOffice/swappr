package uk.gov.homeofficedigital.swappr.model;

import java.util.Set;

public class RotaView {

    private Rota rota;
    private Set<Offer> offers;
    private Set<Volunteer> volunteers;

    public RotaView(Rota rota, Set<Offer> offers, Set<Volunteer> volunteers) {
        this.rota = rota;
        this.offers = offers;
        this.volunteers = volunteers;
    }

    public Rota getRota() {
        return rota;
    }

    public Set<Offer> getOffers() {
        return offers;
    }

    public Set<Volunteer> getVolunteers() {
        return volunteers;
    }
}
