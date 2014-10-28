package uk.gov.homeofficedigital.swappr.service;

import org.junit.Before;
import org.junit.Test;
import uk.gov.homeofficedigital.swappr.daos.OfferDao;
import uk.gov.homeofficedigital.swappr.daos.RotaDao;
import uk.gov.homeofficedigital.swappr.daos.VolunteerDao;
import uk.gov.homeofficedigital.swappr.model.*;

import java.time.LocalDate;

import static com.natpryce.makeiteasy.MakeItEasy.a;
import static com.natpryce.makeiteasy.MakeItEasy.make;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

public class RotaServiceTest {

    RotaDao rotaDao = mock(RotaDao.class);
    OfferDao offerDao = mock(OfferDao.class);
    VolunteerDao volunteerDao = mock(VolunteerDao.class);
    private RotaService service = new RotaService(rotaDao, offerDao, volunteerDao);

    @Before
    public void setUp() {
        reset(rotaDao, offerDao, volunteerDao);
    }

    @Test
    public void requestSwapShouldCreateOfferWithStatusOfCreated() {
        Rota rota = make(a(RotaMaker.Rota));
        Shift shift = new Shift(LocalDate.now(), ShiftType.B1H);
        service.requestSwap(rota, shift);

        verify(offerDao).create(eq(rota), eq(shift), eq(OfferStatus.CREATED));
    }

    @Test
    public void volunteerSwapShouldCreateVolunteerWithStatusOfCreated() {
        Rota rota = make(a(RotaMaker.Rota));
        Offer offer = make(a(OfferMaker.Offer));
        service.volunteerSwap(rota, offer);

        verify(volunteerDao).create(eq(rota), eq(offer), eq(VolunteerStatus.CREATED));
    }
}