package ci.gouv.dgbf.agc.client;

import ci.gouv.dgbf.agc.dto.NatureEconomique;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;

@Path("/v1/natures-economiques")
public interface NatureEconomiqueClient {
    @GET
    List<NatureEconomique> findAll();

    @GET
    @Path("/codes/{code}")
    NatureEconomique findByCode(@PathParam("code") String code);

    @GET
    @Path("/{uuid}")
    List<NatureEconomique> findByUuid(@PathParam("uuid") String uuid);
}
