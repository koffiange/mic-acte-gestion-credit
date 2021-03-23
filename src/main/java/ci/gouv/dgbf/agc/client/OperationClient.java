package ci.gouv.dgbf.agc.client;

import ci.gouv.dgbf.agc.dto.LigneOperation;
import ci.gouv.dgbf.agc.dto.OperationBag;

import javax.ws.rs.*;
import java.util.List;

@Path("/v1/operations")
public interface OperationClient {
    @GET
    List<OperationBag> listAll();

    @GET
    @Path("/operation/{uuid}")
    OperationBag findById(@PathParam("uuid") String uuid);

    @POST
    @Path("/operation")
    OperationBag persist(OperationBag operationBag);

    @PUT
    @Path("/operation")
    void update(OperationBag operationBag);

    @PUT
    @Path("/operation/appliquer")
    void appliquer(OperationBag operationBag);

    @DELETE
    @Path("/operation/{uuid}")
    void delete(@PathParam("uuid") String uuid);


}
