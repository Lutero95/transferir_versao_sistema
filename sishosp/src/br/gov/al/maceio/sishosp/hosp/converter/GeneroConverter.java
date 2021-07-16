package br.gov.al.maceio.sishosp.hosp.converter;

import br.gov.al.maceio.sishosp.hosp.dao.GeneroDAO;
import br.gov.al.maceio.sishosp.hosp.model.Genero;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "conGenero")
public class GeneroConverter implements Converter {
    GeneroDAO s = new GeneroDAO();

    public Object getAsObject(FacesContext contet, UIComponent component,
                              String value) {
        //if (value.equals("null") || value.equals("") || value == null)
        if (value == null || value.equals("null")) {
            value = "0";
        }
        try {
            int id = Integer.parseInt(value);
            return s.buscaGeneroPorId(id);
        } catch (Exception e) {
            //comentado walter erro log ex.printStackTrace();
            throw new ConverterException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "Gênero não válido", ""));
        }
    }

    public String getAsString(FacesContext contet, UIComponent component,
                              Object value) {
        if (value == null || value.equals(""))
            return null;
        return String.valueOf(((Genero) value).getId());
    }
}