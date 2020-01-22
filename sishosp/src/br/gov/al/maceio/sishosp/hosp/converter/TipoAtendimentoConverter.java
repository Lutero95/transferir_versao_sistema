package br.gov.al.maceio.sishosp.hosp.converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import br.gov.al.maceio.sishosp.hosp.dao.TipoAtendimentoDAO;
import br.gov.al.maceio.sishosp.hosp.model.TipoAtendimentoBean;

@FacesConverter(value = "conTipoAtendimento")
public class TipoAtendimentoConverter implements Converter {
	TipoAtendimentoDAO tDAO = new TipoAtendimentoDAO();

	public Object getAsObject(FacesContext contet, UIComponent component,
			String value) {
		 if (value.equals("null") || value.equals("") || value == null)
			return null;
		try {
			int id = Integer.parseInt(value);
			return tDAO.listarTipoPorIdParaConverter(id);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ConverterException(new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Tipo de atendimento inválido!", ""));
		}
	}

	public String getAsString(FacesContext contet, UIComponent component,
			Object value) {
		if (value == null || value.equals(""))
			return null;
		return String.valueOf(((TipoAtendimentoBean) value).getIdTipo());
	}
}
