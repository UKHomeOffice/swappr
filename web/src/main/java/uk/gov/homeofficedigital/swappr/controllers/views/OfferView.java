package uk.gov.homeofficedigital.swappr.controllers.views;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import uk.gov.homeofficedigital.swappr.model.Offer;
import uk.gov.homeofficedigital.swappr.model.Shift;
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
        return volunteers.stream().filter(v -> v.getStatus() == VolunteerStatus.ACCEPTED).findFirst().map(v -> v.getSwapFrom().getWorker().getFullname()).orElse(null);
    }

    public Shift fromShift() {
        return offer.getSwapFrom().getShift();
    }

    public boolean isSameDaySwap() {
        return offer.getSwapTo().getDate().equals(offer.getSwapFrom().getShift().getDate());
    }

    public boolean isOfferForCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return username.equals(offer.getSwapFrom().getWorker().getUsername());
    }

    public boolean isCurrentUserVolunteered() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return !isOfferForCurrentUser() && volunteers.stream().filter(v -> v.getSwapFrom().getWorker().getUsername().equals(username)).findFirst().isPresent();
    }
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
