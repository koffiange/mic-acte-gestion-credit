package ci.gouv.dgbf.appmodele.dto;

public class User {
    private String firstName;
    private String lastNames;
    private String fonction;
    private String electronicMailAddress;
    private String sectionAsString;
    private String administrativeUnitAsString;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastNames() {
        return lastNames;
    }

    public void setLastNames(String lastNames) {
        this.lastNames = lastNames;
    }

    public String getFonction() {
        return fonction;
    }

    public void setFonction(String fonction) {
        this.fonction = fonction;
    }

    public String getElectronicMailAddress() {
        return electronicMailAddress;
    }

    public void setElectronicMailAddress(String electronicMailAddress) {
        this.electronicMailAddress = electronicMailAddress;
    }

    public String getSectionAsString() {
        return sectionAsString;
    }

    public void setSectionAsString(String sectionAsString) {
        this.sectionAsString = sectionAsString;
    }

    public String getAdministrativeUnitAsString() {
        return administrativeUnitAsString;
    }

    public void setAdministrativeUnitAsString(String administrativeUnitAsString) {
        this.administrativeUnitAsString = administrativeUnitAsString;
    }
}
