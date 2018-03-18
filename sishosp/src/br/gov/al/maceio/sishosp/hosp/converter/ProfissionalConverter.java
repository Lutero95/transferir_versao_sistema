package br.gov.al.maceio.sishosp.hosp.converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import br.gov.al.maceio.sishosp.hosp.dao.ProfissionalDAO;
import br.gov.al.maceio.sishosp.hosp.model.ProfissionalBean;

@FacesConverter(value = "conProfissional")
public class ProfissionalConverter implements Converter {
	ProfissionalDAO pDAO = new ProfissionalDAO();

	public Object getAsObject(FacesContext contet, UIComponent component,
			String value) {
		System.out.println("Sim");
		if (value.equals("null") || value.equals("") || value == null)
			return null;
		try {
			int id = Integer.parseInt(value);
			return pDAO.buscarProfissionalPorId(id);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ConverterException(new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Profissional não válido", ""));
		}
	}

	public String getAsString(FacesContext contet, UIComponent component,
			Object value) {
		if (value == null || value.equals(""))
			return null;
		return String.valueOf(((ProfissionalBean) value).getIdProfissional());
	}
}
