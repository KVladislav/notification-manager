package com.eisgroup.notification_manager.ui.converters;

import com.eisgroup.notification_manager.model.UserGroup;
import com.eisgroup.notification_manager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 * Created with IntelliJ IDEA.
 * User: Vladislav Karpenko
 * Date: 08.02.2015
 * Time: 16:39
 */
@Component
@FacesConverter("groupConverter")
public class GroupConverter implements Converter {

    @Autowired
    private UserService userService;

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
        if (value != null && value.trim().length() > 0) {
            try {

                return userService.getUserGroupById(Long.parseLong(value));
            } catch (NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid group."));
            }
        } else {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object object) {
        if (object != null) {
            return String.valueOf(((UserGroup) object).getId());
        } else {
            return null;
        }
    }
}
