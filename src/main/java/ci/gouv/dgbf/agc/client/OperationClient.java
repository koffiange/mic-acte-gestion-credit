package ci.gouv.dgbf.agc.client;

import ci.gouv.dgbf.agc.dto.Operation;

import javax.ws.rs.*;
import java.util.List;

@Path("/v1/operations")
public interface OperationClient {
    @GET
    List<Operation> listAll();

    @GET
    @Path("/operation/{uuid}")
    Operation findById(@PathParam("uuid") String uuid);

    @POST
    @Path("/operation")
    void persist(Operation operation);

    @PUT
    @Path("/operation")
    void update(Operation operation);

    @DELETE
    @Path("/operation/{uuid}")
    void delete(@PathParam("uuid") String uuid);
}
