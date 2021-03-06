package ci.gouv.dgbf.agc.dto;


import ci.gouv.dgbf.agc.enumeration.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.*;
import java.util.Date;
import java.util.Objects;

public class Acte extends BaseDto{
    @Getter @Setter
    private String libelle;
    @Getter @Setter
    private String corpus;
    @Getter @Setter
    private CategorieActe categorieActe;
    @Getter @Setter
    private NatureActe natureActe;
    @Getter @Setter
    private NatureTransaction natureTransaction;
    @Getter @Setter
    private StatutActe statutActe;
    @Getter @Setter
    private String reference;
    @Getter @Setter
    private LocalDate dateSignature;
    @Getter @Setter
    private Demande demande;
    @Getter @Setter
    private ModeleVisa modeleVisa;
    @Getter @Setter
    private LocalDateTime createdDate;
    @Getter @Setter
    private String exercice = String.valueOf(LocalDate.now().getYear());
    @Getter @Setter
    private BigDecimal cumulRetranchementAE;
    @Getter @Setter
    private BigDecimal cumulRetranchementCP;
    @Getter @Setter
    private BigDecimal cumulAjoutAE;
    @Getter @Setter
    private BigDecimal cumulAjoutCP;
    @Getter @Setter
    private ActeRole acteParDefaut;
    @Getter @Setter
    private String signataire;

    public Acte() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Acte acte = (Acte) o;
        return Objects.equals(super.getUuid(), acte.getUuid()) &&
                Objects.equals(libelle, acte.libelle) &&
                Objects.equals(corpus, acte.corpus) &&
                categorieActe == acte.categorieActe &&
                natureActe == acte.natureActe &&
                statutActe == acte.statutActe &&
                Objects.equals(reference, acte.reference) &&
                Objects.equals(dateSignature, acte.dateSignature) &&
                Objects.equals(demande, acte.demande);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.getUuid(), libelle, corpus, categorieActe, natureActe, statutActe, reference, dateSignature, demande);
    }

    @Override
    public String toString() {
        return "Acte{" +
                "uuid='" + super.getUuid() + '\'' +
                ", libelle='" + libelle + '\'' +
                ", corpus='" + corpus + '\'' +
                ", categorieActe=" + categorieActe +
                ", natureActe=" + natureActe +
                ", statutActe=" + statutActe +
                ", reference='" + reference + '\'' +
                ", dateSignature=" + dateSignature +
                ", demande=" + demande +
                '}';
    }
}
