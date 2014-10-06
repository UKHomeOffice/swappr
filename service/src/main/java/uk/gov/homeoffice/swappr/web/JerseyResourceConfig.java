package uk.gov.homeoffice.swappr.web;


import org.glassfish.jersey.server.ResourceConfig;

public class JerseyResourceConfig extends ResourceConfig {

    public JerseyResourceConfig() {
        super();
        packages("uk.gov.homeoffice.swappr.web");
    }



}
