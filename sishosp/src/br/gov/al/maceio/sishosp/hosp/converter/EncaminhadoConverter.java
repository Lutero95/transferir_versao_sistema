package br.gov.al.maceio.sishosp.hosp.converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import br.gov.al.maceio.sishosp.hosp.dao.EncaminhadoDAO;

import br.gov.al.maceio.sishosp.hosp.model.EncaminhadoBean;

@FacesConverter(value = "conEncaminhado")
public class EncaminhadoConverter implements Converter {
	EncaminhadoDAO encaminhadoDAO = new EncaminhadoDAO();

	public Object getAsObject(FacesContext contet, UIComponent component,
			String value) {
		if (value.equals("null") || value.equals("") || value == null)
			return null;
		try {
			int id = Integer.parseInt(value);
			return encaminhadoDAO.buscaencaminhadocodigo(id);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ConverterException(new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Encaminhado não válido", ""));
		}
	}

	public String getAsString(FacesContext contet, UIComponent component,
			Object value) {
		if (value == null || value.equals(""))
			return null;
		return String.valueOf(((EncaminhadoBean) value).getCodencaminhado());
	}
}
