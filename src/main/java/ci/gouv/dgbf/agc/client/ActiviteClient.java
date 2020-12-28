package ci.gouv.dgbf.agc.client;

import ci.gouv.dgbf.agc.dto.Activite;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;

@Path("/v1/activites-services")
public interface ActiviteClient {
    @GET
    List<Activite> findAll();

    @GET
    @Path("/code/{code}")
    Activite findByCode(@PathParam("code") String code);
}
