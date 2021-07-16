package br.gov.al.maceio.sishosp.hosp.converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import br.gov.al.maceio.sishosp.hosp.dao.CboDAO;
import br.gov.al.maceio.sishosp.hosp.dao.ProcedimentoDAO;
import br.gov.al.maceio.sishosp.hosp.model.CboBean;
import br.gov.al.maceio.sishosp.hosp.model.ProcedimentoBean;

@FacesConverter(value = "conCbo")
public class CBOConverter implements Converter {
    CboDAO c = new CboDAO();

    public Object getAsObject(FacesContext contet, UIComponent component,
                              String value) {
        //if (value.equals("null") || value.equals("") || value == null)
        if (value == null || value.equals("null")) {
            value = "0";
        }
        try {
            int id = Integer.parseInt(value);
            return c.listarCboPorId(id);
        } catch (Exception e) {
            //comentado walter erro log ex.printStackTrace();
            throw new ConverterException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "CBO não válido", ""));
        }
    }

    public String getAsString(FacesContext contet, UIComponent component,
                              Object value) {
        if (value == null || value.equals(""))
            return null;
        return String.valueOf(((CboBean) value).getCodCbo());
    }
}