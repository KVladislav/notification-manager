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

/**
 * Created with IntelliJ IDEA.
 * User: Vladislav Karpenko
 * Date: 09.02.2015
 * Time: 20:22
 */
@FacesValidator("smsValidator")
public class SMSValidator implements Validator {
    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object object) throws ValidatorException {
        ResourceBundle resourceBundle = PropertyResourceBundle.getBundle("messages/messages");

        String sms = (String) object;
        if (sms == null)
            return;
        if (sms.trim().isEmpty()) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "", resourceBundle.getString("Error.MandatoryFields")));
        }
        if (sms.length() > 255) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "", resourceBundle.getString("Error.sms255")));
        }
        Pattern p = Pattern.compile("^[\\x20-\\x7E]*$");
        Matcher m = p.matcher(sms);
        if (!m.matches()) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "", resourceBundle.getString("Error.nonLatinLetter")));
        }
    }
}
