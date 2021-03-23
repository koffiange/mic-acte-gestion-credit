package ci.gouv.dgbf.agc.backing;

import ci.gouv.dgbf.agc.dto.ActeDto;
import ci.gouv.dgbf.agc.dto.Composition;
import ci.gouv.dgbf.agc.dto.LigneOperation;
import ci.gouv.dgbf.agc.enumeration.TypeOperation;
import ci.gouv.dgbf.agc.service.ActeService;
import ci.gouv.dgbf.agc.service.CompositionService;
import ci.gouv.dgbf.agc.service.JasperReportService;
import ci.gouv.dgbf.appmodele.backing.BaseBacking;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Named(value = "cmcBacking")
@ViewScoped
public class ConsultationMouvementCreditBacking extends BaseBacking {
    private final Logger LOG = Logger.getLogger(this.getClass().getName());

    @Inject
    ActeService acteService;
    @Inject
    CompositionService compositionService;
    @Inject
    JasperReportService jasperReportService;

    @Getter @Setter
    private ActeDto acteDto;
    @Getter @Setter
    private List<Composition> compositionList;
    @Getter @Setter
    private BigDecimal cumulAE;
    @Getter @Setter
    private BigDecimal cumulCP;
    @Setter
    private StreamedContent ficheActeFile;
    @Setter
    private StreamedContent ficheListeActeFile;

    private Map<String, String> params;

    @PostConstruct
    public void init(){
        params = getRequestParameterMap();
        if (params.containsKey("uuid")){
            acteDto = acteService.findActeDtoById(params.get("uuid"));
            this.computeCumule();
        }
    }

    public List<LigneOperation> findByTypeOperation(String s){
        TypeOperation typeOperation = TypeOperation.valueOf(s);
        return acteDto.getLigneOperationList().stream()
                .filter(operation -> operation.getTypeOperation().equals(typeOperation))
                .collect(Collectors.toList());
    }

    private void computeCumule(){
        acteDto.getLigneOperationList().stream().filter(operation -> operation.getTypeOperation().equals(TypeOperation.ORIGINE))
                .map(LigneOperation::getMontantOperationAE).map(BigDecimal::negate).reduce(BigDecimal::add).ifPresent(this::setCumulAE);
        acteDto.getLigneOperationList().stream().filter(operation -> operation.getTypeOperation().equals(TypeOperation.ORIGINE))
                .map(LigneOperation::getMontantOperationCP).map(BigDecimal::negate).reduce(BigDecimal::add).ifPresent(this::setCumulCP);
    }

    public String goBack(){
        return "/protected/user/pages/acte/index.xhtml";
    }

    public StreamedContent getFicheActeFile(){
        ficheActeFile = DefaultStreamedContent.builder()
                .name("fiche_acte_"+acteDto.getActe().getReference()+".pdf")
                .contentType("application/pdf")
                .stream(() -> jasperReportService.downloadFicheActe(acteDto.getActe().getUuid()))
                .build();
        return ficheActeFile;
    }

    public StreamedContent getFicheListeActeFile(String statut){
        ficheListeActeFile = DefaultStreamedContent.builder()
                .name("fiche_liste_acte_en_attente_application.pdf")
                .contentType("application/pdf")
                .stream(() -> jasperReportService.downloadFicheListeActe(statut, String.valueOf(LocalDate.now().getYear())))
                .build();
        return ficheListeActeFile;
    }
}
