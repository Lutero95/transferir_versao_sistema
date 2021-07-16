package br.gov.al.maceio.sishosp.hosp.converter;


import br.gov.al.maceio.sishosp.hosp.dao.OperadoraDAO;
import br.gov.al.maceio.sishosp.hosp.model.Operadora;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "conOperadora")
public class OperadoraConverter implements Converter {
    OperadoraDAO o = new OperadoraDAO();

    public Object getAsObject(FacesContext contet, UIComponent component,
                              String value) {
        //if (value.equals("null") || value.equals("") || value == null)
        if (value == null || value.equals("null")) {
            value = "0";
        }
        try {
            int id = Integer.parseInt(value);
            return o.buscarOperadoraPorId(id);
        } catch (Exception e) {
            //comentado walter erro log ex.printStackTrace();
            throw new ConverterException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "Operadora não válida", ""));
        }
    }

    public String getAsString(FacesContext contet, UIComponent component,
                              Object value) {
        if (value == null || value.equals(""))
            return null;
        return String.valueOf(((Operadora) value).getId());
    }
}