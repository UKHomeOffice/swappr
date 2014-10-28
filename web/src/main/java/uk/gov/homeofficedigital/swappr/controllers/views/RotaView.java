package uk.gov.homeofficedigital.swappr.controllers.views;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import uk.gov.homeofficedigital.swappr.model.Rota;
import uk.gov.homeofficedigital.swappr.model.Volunteer;
import uk.gov.homeofficedigital.swappr.model.VolunteerStatus;

import java.util.Optional;
import java.util.Set;

public class RotaView {

    private Rota rota;
    private Set<OfferView> offers;
    private Set<Volunteer> volunteers;

    public RotaView(Rota rota, Set<OfferView> offers, Set<Volunteer> volunteers) {
        this.rota = rota;
        this.offers = offers;
        this.volunteers = volunteers;
    }

    public Rota getRota() {
        return rota;
    }

    public Set<OfferView> getOffers() {
        return offers;
    }

    public Optional<Volunteer> getAcceptedVolunteer() {
        return volunteers.stream().filter(v -> v.getStatus().equals(VolunteerStatus.ACCEPTED)).findFirst();
    }

    public Set<Volunteer> getVolunteers() {
        return volunteers;
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

    public String getShiftType() {
        return rota.getShift().getType().getLabel() + " " + rota.getShift().getType().toString();
    }
}
