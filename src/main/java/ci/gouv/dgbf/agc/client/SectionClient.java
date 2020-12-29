package ci.gouv.dgbf.agc.client;

import ci.gouv.dgbf.agc.dto.Section;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;

@Path("/v1/sections")
public interface SectionClient {

    @GET
    List<Section> list();

    @GET
    @Path("/code/{code}")
    Section findByCode(@PathParam("code") String code);

    @GET
    @Path("/filtre/type/{type}")
    List<Section> filteredByType(@PathParam("type") String type);
}
