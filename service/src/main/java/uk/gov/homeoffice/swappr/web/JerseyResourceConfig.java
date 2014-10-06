package uk.gov.homeoffice.swappr.web;


import org.glassfish.jersey.server.ResourceConfig;
import uk.gov.homeoffice.swappr.web.resources.HelloWorldResource;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class JerseyResourceConfig extends ResourceConfig {

    public JerseyResourceConfig() {
        super();
        packages("uk.gov.homeoffice.swappr.web");
    }



}
