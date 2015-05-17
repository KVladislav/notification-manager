package com.eisgroup.notification_manager.ui.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

@FacesValidator("emailValidator")
public class EmailValidator implements Validator {

    private Pattern pattern;

    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public EmailValidator() {
        pattern = Pattern.compile(EMAIL_PATTERN);
    }

    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {

        if (value == null) {
            return;
        }
        if (!pattern.matcher(value.toString().trim()).matches()) {
            ResourceBundle resourceBundle = PropertyResourceBundle.getBundle("messages/messages");

            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
                    resourceBundle.getString("Error.notValidEmail")));

        }
    }
}
