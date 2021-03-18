package ci.gouv.dgbf.agc.client;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.io.InputStream;

@Path("/rest_v2/reports")
public interface JasperReportClient {
    @GET
    @Path("/reports/sigobe/Actes_de_gestion/FicheActeV2.pdf")
    // @Path("/reports/sigobe/Documents_budgetaires/Elaboration_DPPD/Livre/dppd_1.1.0.pdf")
    InputStream downloadFicheActe(@QueryParam("ACTE_ID") String acteId,
                                  @QueryParam("j_username") String j_username,
                                  @QueryParam("j_password") String j_password);

    @GET
    @Path("/sigobe/Actes_de_gestion/FicheListeActe.pdf")
        // @Path("/reports/sigobe/Documents_budgetaires/Elaboration_DPPD/Livre/dppd_1.1.0.pdf")
    InputStream downloadFicheListeActe(@QueryParam("STATUT_ACTE") String statut,
                                       String exercice,
                                       @QueryParam("j_username") String j_username,
                                       @QueryParam("j_password") String j_password);

    /*
    @GET
    @Path("/sigobe/Actes_de_gestion/FicheActeV2.pdf")
    // @Path("/reports/sigobe/Documents_budgetaires/Elaboration_DPPD/Livre/sous_rapports/sr_pape01_objectif_par_programme.pdf")
    InputStream downloadSubReportPAPE01(@QueryParam("SECTION_CODE") String sectionCode,
                                        @QueryParam("j_username") String j_username,
                                        @QueryParam("j_password") String j_password);

    @GET
    @Path("/Reports/dppd/sous_rapport/sr_pape02_action_par_objectif_par_programme.pdf")
    // @Path("/reports/sigobe/Documents_budgetaires/Elaboration_DPPD/Livre/sous_rapports/sr_pape02_action_par_objectif_par_programme.pdf")
    InputStream downloadSubReportPAPE02(@QueryParam("SECTION_CODE") String sectionCode,
                                        @QueryParam("j_username") String j_username,
                                        @QueryParam("j_password") String j_password);
    */
}
