package br.gov.al.maceio.sishosp.hosp.converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import br.gov.al.maceio.sishosp.hosp.dao.EscolaDAO;
import br.gov.al.maceio.sishosp.hosp.dao.RecursoDAO;
import br.gov.al.maceio.sishosp.hosp.model.EscolaBean;
import br.gov.al.maceio.sishosp.hosp.model.RecursoBean;

@FacesConverter(value = "conRecurso")
public class RecursoConverter implements Converter {
	RecursoDAO rDao = new RecursoDAO();

	public Object getAsObject(FacesContext contet, UIComponent component,
			String value) {
		if (value == null || value.equals("null")) {
			value = "0";
		}
		try {
			int id = Integer.parseInt(value);
			return rDao.buscaRecursoCodigo(id);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ConverterException(new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Recurso não válido", ""));
		}
	}

	public String getAsString(FacesContext contet, UIComponent component,
			Object value) {
		if (value == null || value.equals(""))
			return null;
		return String.valueOf(((RecursoBean) value).getIdRecurso());
	}
}
