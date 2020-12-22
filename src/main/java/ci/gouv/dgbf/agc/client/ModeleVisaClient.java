package ci.gouv.dgbf.agc.client;

import ci.gouv.dgbf.agc.dto.ModeleVisa;
import ci.gouv.dgbf.agc.dto.ModeleVisaDto;
import ci.gouv.dgbf.agc.dto.VisaDto;

import javax.ws.rs.*;
import java.util.List;

@Path("/v1/modeles-visas")
public interface ModeleVisaClient {
    @GET
    List<ModeleVisa> listAll();

    @GET
    @Path("/modele/{uuid}")
    ModeleVisa findById(@PathParam("uuid") String uuid);

    @POST
    @Path("/modele")
    void persist(ModeleVisa modeleVisa);

    @POST
    @Path("/modele/compose")
    void persist(ModeleVisaDto modeleVisaDto);

    @PUT
    @Path("/modele")
    void update(ModeleVisaDto modeleVisaDto);

    @DELETE
    @Path("/modele/{uuid}")
    void delete(@PathParam("uuid") String uuid);
}
