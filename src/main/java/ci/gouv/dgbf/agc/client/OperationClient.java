package ci.gouv.dgbf.agc.client;

import ci.gouv.dgbf.agc.dto.LigneOperation;
import ci.gouv.dgbf.agc.dto.Operation;
import ci.gouv.dgbf.agc.dto.OperationBag;

import javax.ws.rs.*;
import java.util.List;

@Path("/v1/operations")
public interface OperationClient {
    @GET
    List<OperationBag> listAll();

    @GET
    @Path("/operation/{uuid}")
    Operation findById(@PathParam("uuid") String uuid);

    @GET
    @Path("/operation/bag/{uuid}")
    OperationBag findBagById(@PathParam("uuid") String uuid);

    @POST
    @Path("/operation")
    OperationBag persist(OperationBag operationBag);

    @PUT
    @Path("/operation")
    OperationBag update(OperationBag operationBag);

    @PUT
    @Path("/operation/appliquer")
    void appliquer(OperationBag operationBag);

    @DELETE
    @Path("/operation/{uuid}")
    void delete(@PathParam("uuid") String uuid);


}
