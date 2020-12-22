package ci.gouv.dgbf.appmodele.client;

import ci.gouv.dgbf.appmodele.dto.User;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/actor")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface UserClient {
    @GET
    @Path("/informations-profile-par-acteur")
    User getUserInfo(@QueryParam("nom_utilisateur") String username);
}
