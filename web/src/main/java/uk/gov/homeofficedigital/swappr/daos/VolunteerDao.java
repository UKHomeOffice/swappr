package uk.gov.homeofficedigital.swappr.daos;

import uk.gov.homeofficedigital.swappr.model.Offer;
import uk.gov.homeofficedigital.swappr.model.Rota;
import uk.gov.homeofficedigital.swappr.model.Volunteer;
import uk.gov.homeofficedigital.swappr.model.VolunteerStatus;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class VolunteerDao {

    private Set<Volunteer> volunteers = new ConcurrentSkipListSet<>();
    private AtomicLong incrementingId = new AtomicLong(0);

    public Volunteer create(Rota swapFrom, Offer swapTo, VolunteerStatus status) {
        Volunteer volunteer = new Volunteer(incrementingId.incrementAndGet(), swapFrom, swapTo, status);
        volunteers.add(volunteer);
        return volunteer;
    }

    public Optional<Volunteer> findById(Long volunteerId) {
        return volunteers.stream().filter(v -> v.getId().equals(volunteerId)).findFirst();
    }

    public Set<Volunteer> findByOffer(Long offerId) {
        return volunteers
                .stream()
                .filter(v -> v.getSwapTo().getId().equals(offerId))
                .collect(Collectors.toSet());
    }

    public Set<Volunteer> findByRota(Long rotaId) {
        return volunteers.stream().filter(v -> v.getSwapFrom().getId().equals(rotaId)).collect(Collectors.toSet());
    }

    public Volunteer updateStatus(Volunteer volunteer, VolunteerStatus requested) {
        Volunteer updated = volunteer.withStatus(requested);
        volunteers.remove(volunteer);
        volunteers.add(updated);
        return updated;
    }

}
