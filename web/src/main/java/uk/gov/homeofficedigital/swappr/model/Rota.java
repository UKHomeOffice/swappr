package uk.gov.homeofficedigital.swappr.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.security.core.userdetails.User;

public class Rota {

    private final Long id;
    private final SwapprUser worker;
    private final Shift shift;

    public Rota(Long id, SwapprUser worker, Shift shift) {
        this.id = id;
        this.worker = worker;
        this.shift = shift;
    }

    public Long getId() {
        return id;
    }

    public SwapprUser getWorker() {
        return worker;
    }

    public Shift getShift() {
        return shift;
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
