package ci.gouv.dgbf.agc.client;

import ci.gouv.dgbf.agc.dto.ActiviteDeService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;

@Path("/v1/activites-services")
public interface ActiviteDeServiceClient {
    @GET
    List<ActiviteDeService> findAll();

    @GET
    @Path("/code/{code}")
    ActiviteDeService findByCode(@PathParam("code") String code);

    @GET
    @Path("/section/{code}")
    List<ActiviteDeService> findBySectionCode(@PathParam("code") String sectionCode);
}
