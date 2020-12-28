package ci.gouv.dgbf.agc.enumeration;

public enum DisponibiliteCreditOperation {
    CREDIT_DISPONIBLES("Crédits disponibles"),
    CREDIT_INSUFFICANTS("Crédits insuffisants");

    public String libelle;

    DisponibiliteCreditOperation(String libelle) {
        this.libelle = libelle;
    }
}
