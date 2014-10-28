package uk.gov.homeofficedigital.swappr.daos;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import uk.gov.homeofficedigital.swappr.SpringIntegrationTest;
import uk.gov.homeofficedigital.swappr.model.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;

public class VolunteerDaoTest  extends SpringIntegrationTest {

    @Autowired
    private VolunteerDao volunteerDao;
    @Autowired
    private OfferDao offerDao;
    @Autowired
    private RotaDao rotaDao;

    @Test
    public void create_shouldCreateAndPersistAVolunteer() throws Exception {
        User bill = UserMaker.bill();
        User ben = UserMaker.ben();
        LocalDate now = LocalDate.now();
        Rota from = rotaDao.create(bill, new Shift(now, ShiftType.BFH));
        Rota offerRota = rotaDao.create(ben, new Shift(now, ShiftType.S1H));
        Offer offer = offerDao.create(offerRota, new Shift(now, ShiftType.BFH), OfferStatus.CREATED);

        Volunteer expected = volunteerDao.create(from, offer, VolunteerStatus.CREATED);

        Optional<Volunteer> actual = volunteerDao.findById(expected.getId());

        assertTrue(actual.isPresent());
        assertEquals(from, actual.get().getSwapFrom());
        assertEquals(offer, actual.get().getSwapTo());
        assertEquals(VolunteerStatus.CREATED, actual.get().getStatus());
    }

    @Test
    public void findByRota_shouldReturnVolunteersForTheGivenRota() throws Exception {
        User bill = UserMaker.bill();
        User ben = UserMaker.ben();
        LocalDate now = LocalDate.now();
        Rota from = rotaDao.create(bill, new Shift(now, ShiftType.BFH));
        Rota offerRota = rotaDao.create(ben, new Shift(now, ShiftType.S1H));
        Offer offer = offerDao.create(offerRota, new Shift(now, ShiftType.BFH), OfferStatus.CREATED);
        Volunteer expected = volunteerDao.create(from, offer, VolunteerStatus.CREATED);

        Set<Volunteer> actual = volunteerDao.findByRota(expected.getSwapFrom());

        assertEquals(1, actual.size());
        assertEquals(expected, actual.iterator().next());

        actual = volunteerDao.findByRota(offerRota);

        assertEquals(0, actual.size());
    }

    @Test
    public void findByOffer_shouldReturnVolunteersForTheGivenOffer() throws Exception {
        User bill = UserMaker.bill();
        User ben = UserMaker.ben();
        LocalDate now = LocalDate.now();
        Rota from = rotaDao.create(bill, new Shift(now, ShiftType.BFH));
        Rota offerRota = rotaDao.create(ben, new Shift(now, ShiftType.S1H));
        Offer offer = offerDao.create(offerRota, new Shift(now, ShiftType.BFH), OfferStatus.CREATED);
        Offer otherOffer = offerDao.create(offerRota, new Shift(now.plusDays(2), ShiftType.BFH), OfferStatus.CREATED);
        Volunteer expected = volunteerDao.create(from, offer, VolunteerStatus.CREATED);
        Volunteer otherVolunteer = volunteerDao.create(from, otherOffer, VolunteerStatus.CREATED);

        Set<Volunteer> actual = volunteerDao.findByOffer(offer);

        assertEquals(1, actual.size());
        assertEquals(expected, actual.iterator().next());

        actual = volunteerDao.findByOffer(otherOffer);

        assertEquals(otherVolunteer, actual.iterator().next());
    }

    @Test
    public void updateStatus_shouldChangeTheStatusOfTheVolunteer() throws Exception {
        User bill = UserMaker.bill();
        User ben = UserMaker.ben();
        LocalDate now = LocalDate.now();
        Rota from = rotaDao.create(bill, new Shift(now, ShiftType.BFH));
        Rota offerRota = rotaDao.create(ben, new Shift(now, ShiftType.S1H));
        Offer offer = offerDao.create(offerRota, new Shift(now, ShiftType.BFH), OfferStatus.CREATED);
        Volunteer expected = volunteerDao.create(from, offer, VolunteerStatus.CREATED);
        volunteerDao.updateStatus(expected, VolunteerStatus.ACCEPTED);

        Optional<Volunteer> actual = volunteerDao.findById(expected.getId());

        assertEquals(VolunteerStatus.ACCEPTED, actual.get().getStatus());
    }
}