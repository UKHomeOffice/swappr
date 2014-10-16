package uk.gov.homeofficedigital.swappr.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.homeofficedigital.swappr.daos.SwapDao;

@Configuration
public class Services {

    @Bean
    public SwapService swapService(SwapDao swapDAO) {
        return new SwapService(swapDAO);
    }
}
