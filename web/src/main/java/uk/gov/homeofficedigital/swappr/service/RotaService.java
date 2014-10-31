package uk.gov.homeofficedigital.swappr.service;

import uk.gov.homeofficedigital.swappr.controllers.views.OfferView;
import uk.gov.homeofficedigital.swappr.controllers.views.RotaView;
import uk.gov.homeofficedigital.swappr.daos.OfferDao;
import uk.gov.homeofficedigital.swappr.daos.RotaDao;
import uk.gov.homeofficedigital.swappr.daos.VolunteerDao;
import uk.gov.homeofficedigital.swappr.model.*;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RotaService {


    private final RotaDao rotaDao;
    private final OfferDao offerDao;
    private final VolunteerDao volunteerDao;

    public RotaService(RotaDao rotaDao, OfferDao offerDao, VolunteerDao volunteerDao) {
        this.rotaDao = rotaDao;
        this.offerDao = offerDao;
        this.volunteerDao = volunteerDao;
    }

    public Offer requestSwap(Rota swapFrom, Shift swapTo) {
        return offerDao.create(swapFrom, swapTo, OfferStatus.CREATED);
    }

    public Volunteer volunteerSwap(Rota swapFrom, Offer swapTo) {
        return volunteerDao.create(swapFrom, swapTo, VolunteerStatus.CREATED);
    }

    public void acceptVolunteer(Volunteer volunteer) {
        volunteerDao.updateStatus(volunteer, VolunteerStatus.ACCEPTED);
        offerDao.updateStatus(volunteer.getSwapTo(), OfferStatus.ACCEPTED);
        volunteerDao.findActiveByOffer(volunteer.getSwapTo()).stream().filter(v -> !v.getId().equals(volunteer.getId())).forEach((r) -> volunteerDao.updateStatus(r, VolunteerStatus.REJECTED));
    }

    public void rejectVolunteer(Volunteer volunteer) {
        volunteerDao.updateStatus(volunteer, VolunteerStatus.REJECTED);
    }

    public void approveSwap(Volunteer volunteer) {
        volunteerDao.updateStatus(volunteer, VolunteerStatus.APPROVED);
        offerDao.updateStatus(volunteer.getSwapTo(), OfferStatus.APPROVED);
    }

    public void denySwap(Volunteer volunteer) {
        volunteerDao.updateStatus(volunteer, VolunteerStatus.DENIED);
        offerDao.updateStatus(volunteer.getSwapTo(), OfferStatus.DENIED);
    }

    public Set<RotaView> findMyRotas(SwapprUser worker) {
        return mapToRotaView(rotaDao.findByWorker(worker));
    }

    public Set<RotaView> findAllRotas() {
        return mapToRotaView(rotaDao.findAll());
    }

    public Set<OfferView> findAllOffers() {
        Stream<OfferView> offerViews = offerDao.findOffersBetween(LocalDate.now(), LocalDate.now().plusMonths(2)).stream().map(o -> new OfferView(o, volunteerDao.findActiveByOffer(o)));
        return offerViews.collect(Collectors.toSet());
    }

    private Set<RotaView> mapToRotaView(Set<Rota> rotas) {
        return rotas.stream()
                .map(r -> new RotaView(r,
                        offerDao.findByRota(r).stream().map(o -> new OfferView(o, volunteerDao.findActiveByOffer(o))).collect(Collectors.toSet()),
                        volunteerDao.findByRota(r)))
                .collect(Collectors.toSet());
    }

    public Optional<OfferView> getOffer(Long offerId) {
        return offerDao.findById(offerId).map(o -> new OfferView(o, volunteerDao.findByOffer(o)));
    }
}