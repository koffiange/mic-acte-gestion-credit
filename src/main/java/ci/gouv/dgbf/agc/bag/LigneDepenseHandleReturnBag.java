package ci.gouv.dgbf.agc.bag;

import ci.gouv.dgbf.agc.dto.LigneOperation;
import ci.gouv.dgbf.agc.enumeration.TypeOperation;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LigneDepenseHandleReturnBag {
    @Getter @Setter
    private TypeOperation typeOperation;

    @Getter @Setter
    private List<LigneOperation> ligneOperationList = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LigneDepenseHandleReturnBag that = (LigneDepenseHandleReturnBag) o;
        return Objects.equals(ligneOperationList, that.ligneOperationList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ligneOperationList);
    }

    @Override
    public String toString() {
        return "LigneDepenseHandleReturnBag{" +
                "ligneOperationList=" + ligneOperationList +
                '}';
    }
}
