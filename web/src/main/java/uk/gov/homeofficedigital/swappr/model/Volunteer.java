package uk.gov.homeofficedigital.swappr.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Volunteer {

    private final Long id;
    private final Rota swapFrom;
    private final Offer swapTo;
    private final VolunteerStatus status;

    public Volunteer(Long id, Rota swapFrom, Offer swapTo, VolunteerStatus status) {
        this.id = id;
        this.swapFrom = swapFrom;
        this.swapTo = swapTo;
        this.status = status;
    }

    public Volunteer withStatus(VolunteerStatus status) {
        return new Volunteer(id, swapFrom, swapTo, status);
    }

    public Long getId() {
        return id;
    }

    public Rota getSwapFrom() {
        return swapFrom;
    }

    public Offer getSwapTo() {
        return swapTo;
    }

    public VolunteerStatus getStatus() {
        return status;
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
