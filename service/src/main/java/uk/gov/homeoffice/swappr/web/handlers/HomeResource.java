package uk.gov.homeoffice.swappr.web.handlers;

import uk.gov.homeoffice.swappr.web.resources.Viewable;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;

@Path("/")
@Produces(MediaType.TEXT_HTML)
public class HomeResource {

    @GET
    public Viewable get() {
        return new Viewable(new HashMap<String, String>(), "home");
    }
}

