package uk.gov.homeofficedigital.swappr.model;

import java.util.Set;

public class OfferView {

    private final Offer offer;
    private final Set<Volunteer> volunteers;

    public OfferView(Offer offer, Set<Volunteer> volunteers) {
        this.offer = offer;
        this.volunteers = volunteers;
    }

    public Offer getOffer() {
        return offer;
    }

    public Set<Volunteer> getVolunteers() {
        return volunteers;
    }
}
