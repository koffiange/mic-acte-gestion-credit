package ci.gouv.dgbf.agc.client;

import ci.gouv.dgbf.agc.dto.Demande;

import javax.ws.rs.*;
import java.util.List;

public interface DemandeClient {
    @GET
    List<Demande> listAll();

    @GET
    @Path("/demande/{uuid}")
    Demande findById(@PathParam("uuid") String uuid);

    @POST
    @Path("/demande")
    void persist(Demande demande);

    @PUT
    @Path("/demande")
    void update(Demande demande);

    @DELETE
    @Path("/demande/{uuid}")
    void delete(@PathParam("uuid") String uuid);
}
