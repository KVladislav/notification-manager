package com.eisgroup.notification_manager.ui.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@FacesValidator("emailValidator")
public class MobileValidator implements Validator {

    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {

        String inputString = (String) value;
        if (inputString == null)
            return;
        Pattern p = Pattern.compile("^[0-9]{12}$");
        Matcher m = p.matcher(inputString);
        if (!m.matches()) {
            ResourceBundle resourceBundle = PropertyResourceBundle.getBundle("messages/messages");

            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
                    resourceBundle.getString("Error.notValidMobile")));

        }
    }
}
