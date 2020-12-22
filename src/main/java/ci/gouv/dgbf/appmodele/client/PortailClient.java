/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ci.gouv.dgbf.appmodele.client;

import ci.gouv.dgbf.appmodele.dto.Portail;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 *
 * @author koffi
 */
@Path("/address")
public interface PortailClient {
    @GET
    public Portail portail();
}
