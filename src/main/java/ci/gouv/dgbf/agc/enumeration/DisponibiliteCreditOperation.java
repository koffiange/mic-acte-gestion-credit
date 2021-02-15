package ci.gouv.dgbf.agc.enumeration;

public enum DisponibiliteCreditOperation {
    CREDIT_DISPONIBLES("Crédits disponibles"),
    CREDIT_INSUFFISANTS("Crédits insuffisants");

    public String libelle;

    DisponibiliteCreditOperation(String libelle) {
        this.libelle = libelle;
    }
}
