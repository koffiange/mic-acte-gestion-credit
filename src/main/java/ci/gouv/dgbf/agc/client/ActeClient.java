package ci.gouv.dgbf.agc.client;

import ci.gouv.dgbf.agc.dto.Acte;
import javax.ws.rs.*;
import java.util.List;


@Path("/v1/actes")
public interface ActeClient {
    @GET
    List<Acte> listAll();

    @GET
    @Path("/acte/{uuid}")
    Acte findById(@PathParam("uuid") String uuid);

    @POST
    @Path("/acte")
    void persist(Acte acte);

    @PUT
    @Path("/acte")
    void update(Acte acte);

    @DELETE
    @Path("/acte/{uuid}")
    void delete(@PathParam("uuid") String uuid);
}
