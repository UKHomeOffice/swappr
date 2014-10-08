package uk.gov.homeoffice.swappr.web.handlers;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.io.File;
import java.net.URISyntaxException;

@Path("/css/{filename}")
@Produces("text/css")
public class CssResource {

    @GET
    public File get(@PathParam("filename") String filename) throws URISyntaxException {
        return new File(this.getClass().getResource("/css/" + filename).toURI());
    }

}
