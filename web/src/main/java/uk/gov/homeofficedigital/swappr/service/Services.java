package uk.gov.homeofficedigital.swappr.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.homeofficedigital.swappr.daos.OfferDao;
import uk.gov.homeofficedigital.swappr.daos.RotaDao;
import uk.gov.homeofficedigital.swappr.daos.VolunteerDao;

@Configuration
public class Services {

    @Bean
    public RotaService rotaService(RotaDao rotaDao, OfferDao offerDao, VolunteerDao volunteerDao) {
        return new RotaService(rotaDao, offerDao, volunteerDao);
    }

}
