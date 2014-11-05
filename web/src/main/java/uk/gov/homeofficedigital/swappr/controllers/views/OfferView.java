package uk.gov.homeofficedigital.swappr.controllers.views;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import uk.gov.homeofficedigital.swappr.model.*;

import java.util.Comparator;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

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
        return volunteers.stream().sorted(Comparator.comparing(Volunteer::getStatus)).collect(toSet());
    }

    public String getAcceptedVolunteer() {
        return volunteers.stream().filter(v -> v.getStatus() == VolunteerStatus.ACCEPTED || v.getStatus() == VolunteerStatus.APPROVED).findFirst().map(v -> v.getSwapFrom().getWorker().getFullname()).orElse(null);
    }

    public String getDeniedVolunteer() {
        return volunteers.stream().filter(v -> v.getStatus().equals(VolunteerStatus.DENIED)).findFirst().map(v -> v.getSwapFrom().getWorker().getFullname()).orElse(null);
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
        return !isOfferForCurrentUser()
                && volunteers.stream()
                .filter(v -> v.getSwapFrom().getWorker().getUsername().equals(username))
                .findFirst().isPresent();
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
