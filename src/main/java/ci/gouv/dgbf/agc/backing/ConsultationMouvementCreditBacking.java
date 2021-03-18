package ci.gouv.dgbf.agc.backing;

import ci.gouv.dgbf.agc.dto.Acte;
import ci.gouv.dgbf.agc.dto.ActeDto;
import ci.gouv.dgbf.agc.dto.Composition;
import ci.gouv.dgbf.agc.dto.Operation;
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
import javax.swing.plaf.ButtonUI;
import java.math.BigDecimal;
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

    private Map<String, String> params;

    @PostConstruct
    public void init(){
        params = getRequestParameterMap();
        if (params.containsKey("uuid")){
            acteDto = acteService.findActeDtoById(params.get("uuid"));
            this.computeCumule();
        }
    }

    public List<Operation> findByTypeOperation(String s){
        TypeOperation typeOperation = TypeOperation.valueOf(s);
        return acteDto.getOperationList().stream()
                .filter(operation -> operation.getTypeOperation().equals(typeOperation))
                .collect(Collectors.toList());
    }

    private void computeCumule(){
        acteDto.getOperationList().stream().filter(operation -> operation.getTypeOperation().equals(TypeOperation.ORIGINE))
                .map(Operation::getMontantOperationAE).map(BigDecimal::negate).reduce(BigDecimal::add).ifPresent(this::setCumulAE);
        acteDto.getOperationList().stream().filter(operation -> operation.getTypeOperation().equals(TypeOperation.ORIGINE))
                .map(Operation::getMontantOperationCP).map(BigDecimal::negate).reduce(BigDecimal::add).ifPresent(this::setCumulCP);
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
}
