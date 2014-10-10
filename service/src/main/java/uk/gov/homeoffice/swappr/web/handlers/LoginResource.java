package uk.gov.homeoffice.swappr.web.handlers;

import uk.gov.homeoffice.swappr.model.User;
import uk.gov.homeoffice.swappr.repository.UserRepository;
import uk.gov.homeoffice.swappr.web.resources.Viewable;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.Collections;

@Path("/")
@Produces(MediaType.TEXT_HTML)
public class LoginResource {

    private final UserRepository repo;

    public LoginResource(UserRepository repo) {
        this.repo = repo;
    }

    @Path("/login")
    @GET
    public Viewable get() {
        return new Viewable(Collections.<String, Object>emptyMap(), "login");
    }

    public Response login(String name, String password) {
        User user = repo.findUser(name, password);
        if (user == null) {
            return Response.temporaryRedirect(UriBuilder.fromPath("/login").build()).build();
        }
        return Response.seeOther(UriBuilder.fromPath("/").build()).cookie(new NewCookie("user", String.valueOf(user.getId()))).build();
    }
}
