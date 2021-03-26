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

    @GET
    @Path("/acte/dto/{uuid}")
    Acte findActeDtoById(@PathParam("uuid") String uuid);

    @POST
    @Path("/acte")
    Acte persist(Acte acteDto);

    @GET
    @Path("/operation/{uuid}")
    List<Acte> findActeByOperation(@PathParam("uuid") String uuid);

    @PUT
    @Path("/acte/dto/")
    Acte update(@QueryParam("appliquer") boolean appliquer, Acte acteDto);

    @PUT
    @Path("/acte/appliquer/{uuid}")
    void appliquer(@PathParam("uuid") String uuid);

    @PUT
    @Path("/acte/appliquer/plusieurs")
    void appliquerPlusieur(List<String> uuidList);

    @DELETE
    @Path("/acte/{uuid}")
    void delete(@PathParam("uuid") String uuid);

    @GET
    @Path("/acte/check-reference-already-exist")
    boolean checkReferenceAlreadyExist(@QueryParam("reference") String reference);
}
