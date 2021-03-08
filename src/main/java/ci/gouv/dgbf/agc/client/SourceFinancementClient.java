package ci.gouv.dgbf.agc.client;

import ci.gouv.dgbf.agc.dto.SourceFinancement;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.List;

@Path("/v1/sources-financement")
public interface SourceFinancementClient {
    @GET
    List<SourceFinancement> listAll();
}
