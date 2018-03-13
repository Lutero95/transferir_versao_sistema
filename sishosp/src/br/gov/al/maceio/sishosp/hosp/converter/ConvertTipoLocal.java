package br.gov.al.maceio.sishosp.hosp.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "conTpLocal")
public class ConvertTipoLocal implements Converter {

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String valor) {
		// TODO Auto-generated method stub

		return valor;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object objeto) {
		// TODO Auto-generated method stub

		String descTipo = objeto.toString();

		if (descTipo == null || descTipo.trim().length() == 0 || descTipo == "") {

			return null;

		} else {

			return descTipo;
		}

	}

}
