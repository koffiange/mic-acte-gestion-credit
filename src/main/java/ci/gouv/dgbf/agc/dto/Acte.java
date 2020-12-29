package ci.gouv.dgbf.agc.dto;


import ci.gouv.dgbf.agc.enumeration.CategorieActe;
import ci.gouv.dgbf.agc.enumeration.NatureActe;
import ci.gouv.dgbf.agc.enumeration.NatureTransaction;
import ci.gouv.dgbf.agc.enumeration.StatutActe;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Objects;

public class Acte{
    @Getter @Setter
    private String uuid;
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

    public Acte() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Acte acte = (Acte) o;
        return Objects.equals(uuid, acte.uuid) &&
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
        return Objects.hash(uuid, libelle, corpus, categorieActe, natureActe, statutActe, reference, dateSignature, demande);
    }

    @Override
    public String toString() {
        return "Acte{" +
                "uuid='" + uuid + '\'' +
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
