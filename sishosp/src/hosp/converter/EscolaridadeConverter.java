package br.gov.al.maceio.sishosp.hosp.converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import br.gov.al.maceio.sishosp.hosp.dao.EscolaridadeDAO;
import br.gov.al.maceio.sishosp.hosp.model.EscolaridadeBean;




@FacesConverter(value = "conEscolaridade")
public class EscolaridadeConverter implements Converter {
	   EscolaridadeDAO escolaridadeDAO = new EscolaridadeDAO();
	    public Object getAsObject(FacesContext contet, UIComponent component, String value) {
	        if(value==null || value.equals(""))
	            return null;
	        try{
	            int id = Integer.parseInt(value);
	            return escolaridadeDAO.buscaescolaridadecodigo(id);
	        }catch (Exception e) {
	            e.printStackTrace();
	            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Escola não válido", ""));
	        }
	    }

	    public String getAsString(FacesContext contet, UIComponent component, Object value) {
	        if(value==null || value.equals(""))
	            return null;
	        return String.valueOf(((EscolaridadeBean)value).getCodescolaridade());
	    }
	}
