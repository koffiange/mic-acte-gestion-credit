package ci.gouv.dgbf.agc.client;

import ci.gouv.dgbf.agc.dto.Composition;
import ci.gouv.dgbf.agc.dto.ModeleVisa;
import ci.gouv.dgbf.agc.dto.VisaDto;

import javax.ws.rs.*;
import java.util.List;

@Path("/v1/compositions")
public interface CompositionClient {
    @GET
    @Path("/modele-visa/{uuid}")
    List<Composition> findByModele(@PathParam("uuid") String uuid);

    @POST
    @Path("/modele-visa/")
    void persist(ModeleVisa modeleVisa, List<VisaDto> visaList);

    @DELETE
    @Path("/{uuid}")
    void delete(@PathParam("uuid") String uuid);
}
