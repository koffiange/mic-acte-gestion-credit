package ci.gouv.dgbf.agc.client;

import ci.gouv.dgbf.agc.dto.Bailleur;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.List;

@Path("/v1/bailleurs")
public interface BailleurClient {
    @GET
    List<Bailleur> listAll();
}
