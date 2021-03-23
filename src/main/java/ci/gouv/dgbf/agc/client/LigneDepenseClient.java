package ci.gouv.dgbf.agc.client;

import ci.gouv.dgbf.agc.dto.LigneDepense;
import ci.gouv.dgbf.agc.dto.LigneOperation;

import javax.ws.rs.*;
import java.util.List;

@Path("/v1/lignes-depenses")
public interface LigneDepenseClient {
    @GET
    List<LigneDepense> findAll();

    @GET
    @Path("/nature-economique/{nat_code}")
    List<LigneDepense> findByCritere(@PathParam("nat_code") String nat_code);

    @GET
    @Path("/activite/{ads_code}")
    List<LigneDepense> findByActivite(@PathParam("ads_code") String ads_code);

    @GET
    @Path("/section/{secb_code}")
    List<LigneDepense> findBySection(@PathParam("secb_code") String secb_code);

    @GET
    @Path("/criteres")
    List<LigneDepense> findByCritere(@QueryParam("exercice") String exercice,
                                     @QueryParam("sourceFinancement") String sourceFinancement,
                                     @QueryParam("natureEconomiqueCode") String natureEconomiqueCode,
                                     @QueryParam("activiteCode") String activiteCode,
                                     @QueryParam("bailleur") String bailleur,
                                     @QueryParam("sectionCode") String sectionCode,
                                     @QueryParam("natureDepense") String natureDepense,
                                     @QueryParam("programme") String programme,
                                     @QueryParam("action") String action);

    @POST
    @Path("/criteres/operations")
    List<LigneDepense> findByOperation(List<LigneOperation> ligneOperationList);
}
