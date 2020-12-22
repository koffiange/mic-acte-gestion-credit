package ci.gouv.dgbf.agc.client;

import ci.gouv.dgbf.agc.dto.Visa;

import javax.ws.rs.*;
import java.util.List;

@Path("/v1/visas")
public interface VisaClient {
    @GET
    List<Visa> listAll();

    @GET
    @Path("/visa/{uuid}")
    Visa findById(@PathParam("uuid") String uuid);

    @POST
    @Path("/visa")
    void persist(Visa visa);

    @PUT
    @Path("/visa")
    void update(Visa visa);

    @DELETE
    @Path("/visa/{uuid}")
    void delete(@PathParam("uuid") String uuid);
}
