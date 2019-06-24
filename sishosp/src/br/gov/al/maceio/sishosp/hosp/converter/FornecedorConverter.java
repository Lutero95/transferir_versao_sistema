package br.gov.al.maceio.sishosp.hosp.converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import br.gov.al.maceio.sishosp.hosp.dao.FornecedorDAO;
import br.gov.al.maceio.sishosp.hosp.model.FornecedorBean;



@FacesConverter(value = "conFornecedor")
public class FornecedorConverter implements Converter {
	FornecedorDAO f = new FornecedorDAO();

	public Object getAsObject(FacesContext contet, UIComponent component,
			String value) {
		if (value.equals("null") || value.equals("") || value == null)
			return null;
		try {
			int id = Integer.parseInt(value);
			return f.listarFornecedorPorId(id);
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
		return String.valueOf(((FornecedorBean) value).getId());
	}
}