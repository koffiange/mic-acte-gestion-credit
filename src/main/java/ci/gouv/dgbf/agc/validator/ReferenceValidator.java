package ci.gouv.dgbf.agc.validator;

import ci.gouv.dgbf.agc.service.ActeService;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import java.util.logging.Logger;

@FacesValidator("agc.validator.ReferenceValidator")
public class ReferenceValidator implements Validator {
    Logger LOG = Logger.getLogger(this.getClass().getName());
    @Inject
    ActeService acteService;

    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object o) throws ValidatorException {
        LOG.info("ActeService : "+acteService);
        String reference = (String) o;
        LOG.info("reference : "+reference);
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur de saisie", "La reférence "+reference+" est déjà associée à un acte existant");
        if (acteService.checkReferenceAlreadyExist(reference))
            throw new ValidatorException(message);
    }


}
