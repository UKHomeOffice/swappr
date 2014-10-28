package uk.gov.homeofficedigital.swappr.controllers.views;

import uk.gov.homeofficedigital.swappr.model.Offer;
import uk.gov.homeofficedigital.swappr.model.Volunteer;
import uk.gov.homeofficedigital.swappr.model.VolunteerStatus;

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

    public Long getId() {
        return offer.getId();
    }

    public Set<Volunteer> getVolunteers() {
        return volunteers;
    }

    public String getAcceptedVolunteer() {
        return volunteers.stream().filter(v -> v.getStatus() == VolunteerStatus.ACCEPTED).findFirst().map(v -> v.getSwapFrom().getWorker().getUsername()).orElse(null);
    }
}
