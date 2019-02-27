package br.gov.al.maceio.sishosp.hosp.converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import br.gov.al.maceio.sishosp.hosp.dao.GrupoDAO;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;

@FacesConverter(value = "conGrupo")
public class GrupoConverter implements Converter {
	GrupoDAO g = new GrupoDAO();

	public Object getAsObject(FacesContext contet, UIComponent component,
			String value) {
		// if (value.equals("null") || value.equals("") || value == null)
		if (value == null)
			return null;
		try {
			int id = Integer.parseInt(value);
			return g.listarGrupoPorId(id);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ConverterException(new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Grupo não válido", ""));
		}
	}

	public String getAsString(FacesContext contet, UIComponent component,
			Object value) {
		if (value == null || value.equals(""))
			return null;
		return String.valueOf(((GrupoBean) value).getIdGrupo());
	}
}