package ci.gouv.dgbf.agc.enumeration;

public enum EffetOperation {
    RESERVATION("Réservation"),
    APPLICATION("Application");

    public String libelle;

    EffetOperation(String libelle) {
        this.libelle = libelle;
    }
}
