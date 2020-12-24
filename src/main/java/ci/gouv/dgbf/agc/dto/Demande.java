package ci.gouv.dgbf.agc.dto;

import ci.gouv.dgbf.agc.enumeration.StatutDemande;
import ci.gouv.dgbf.agc.enumeration.TypeDemande;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;

public class Demande {
    @Getter @Setter
    @Enumerated(EnumType.STRING)
    private TypeDemande typeDemande;

    @Getter @Setter
    @Lob
    private String contenu;

    @Getter @Setter
    @Enumerated(EnumType.STRING)
    private StatutDemande statutDemande;

    @Getter @Setter
    private String demandeurSectionCode;

    @Getter @Setter
    private String demandeurSectionLibelle;

    @Getter @Setter
    private String demandeurUaCode;

    @Getter @Setter
    private String demandeurUaLibelle;
}
