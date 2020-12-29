package ci.gouv.dgbf.agc.client;

import ci.gouv.dgbf.agc.dto.Exercice;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

@Path("/v1/exercices")
public interface ExerciceClient {
    @GET
    @Path("/exercice/courant")
    Exercice findCurrent();

    @POST
    @Path("/exercice")
    void persist(Exercice exercice);

    @PUT
    @Path("/exercice")
    void update(Exercice exercice);
}
