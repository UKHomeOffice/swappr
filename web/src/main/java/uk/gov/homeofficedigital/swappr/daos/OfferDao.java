package uk.gov.homeofficedigital.swappr.daos;

import uk.gov.homeofficedigital.swappr.model.Offer;
import uk.gov.homeofficedigital.swappr.model.OfferStatus;
import uk.gov.homeofficedigital.swappr.model.Rota;
import uk.gov.homeofficedigital.swappr.model.Shift;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class OfferDao {

    private Set<Offer> offers = new ConcurrentSkipListSet<>();
    private AtomicLong incrementingId = new AtomicLong(0);

    public Offer create(Rota swapFrom, Shift swapTo, OfferStatus status) {
        Offer offer = new Offer(incrementingId.incrementAndGet(), swapFrom, swapTo, status);
        offers.add(offer);
        return offer;
    }

    public Optional<Offer> findById(Long offerId) {
        return offers.stream().filter(o -> o.getId().equals(offerId)).findFirst();
    }

    public Set<Offer> findByRota(Long rotaId) {
        return offers.stream().filter(o -> o.getSwapFrom().getId().equals(rotaId)).collect(Collectors.toSet());
    }

    public Offer updateStatus(Offer offer, OfferStatus status) {
        Offer updated = offer.withStatus(status);
        offers.remove(offer);
        offers.add(updated);
        return updated;
    }
}
