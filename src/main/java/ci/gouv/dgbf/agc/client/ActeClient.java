package ci.gouv.dgbf.agc.client;

import ci.gouv.dgbf.agc.dto.Acte;
import ci.gouv.dgbf.agc.dto.ActeDto;

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
    ActeDto findActeDtoById(@PathParam("uuid") String uuid);

    @POST
    @Path("/acte")
    void persist(@QueryParam("appliquer") boolean appliquer, ActeDto acteDto);

    @PUT
    @Path("/acte/dto/")
    void update(@QueryParam("appliquer") boolean appliquer, ActeDto acteDto);

    @PUT
    @Path("/acte/appliquer")
    void appliquer(String uuid);

    @PUT
    @Path("/acte/appliquer/plusieurs")
    void appliquerPlusieur(List<String> uuidList);

    @DELETE
    @Path("/acte/{uuid}")
    void delete(@PathParam("uuid") String uuid);
}
