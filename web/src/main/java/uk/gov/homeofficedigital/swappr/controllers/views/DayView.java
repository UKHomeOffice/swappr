package uk.gov.homeofficedigital.swappr.controllers.views;

import uk.gov.homeofficedigital.swappr.model.OfferStatus;
import uk.gov.homeofficedigital.swappr.model.SwapStatus;

import java.time.LocalDate;
import java.util.Optional;

public class DayView {

    private final LocalDate day;
    private final Optional<RotaView> rotaView;

    public DayView(LocalDate day, Optional<RotaView> rotaView) {
        this.day = day;
        this.rotaView = rotaView;
    }

    public LocalDate getDay() {
        return day;
    }

    public Optional<RotaView> getRotaView() {
        return rotaView;
    }

    public RotaView getRota() {
        return rotaView.orElse(null);
    }

    public SwapStatus getStatus() {
        if (!rotaView.isPresent())
            return SwapStatus.NotWorking;
        if (rotaView.get().getOffers().isEmpty() && rotaView.get().getVolunteers().isEmpty())
            return SwapStatus.WorkingOnly;
        if (rotaView.get().getOffers().stream().findFirst().map(o -> o.getVolunteers().isEmpty()).orElse(false))
            return SwapStatus.OfferAwaitingVolunteers;
        if (rotaView.get().getOffers().stream().findFirst().map(o -> (o.getOffer().getStatus() == OfferStatus.CREATED && o.getVolunteers().size() > 0)).orElse(false))
            return SwapStatus.OfferWithVolunteers;
        if (rotaView.get().getOffers().stream().findFirst().map(o -> (o.getOffer().getStatus() == OfferStatus.ACCEPTED)).orElse(false))
            return SwapStatus.OfferAwaitingApproval;
        if (rotaView.get().getOffers().stream().findFirst().map(o -> (o.getOffer().getStatus() == OfferStatus.APPROVED)).orElse(false))
            return SwapStatus.OfferApproved;
        if (rotaView.get().getOffers().stream().findFirst().map(o -> (o.getOffer().getStatus() == OfferStatus.DENIED)).orElse(false))
            return SwapStatus.OfferDenied;
        if (rotaView.get().getVolunteers().size() > 0)
            return SwapStatus.Volunteered;

        throw new RuntimeException("undetermined status");
    }
}
