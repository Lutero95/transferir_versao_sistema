package br.gov.al.maceio.sishosp.hosp.converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import br.gov.al.maceio.sishosp.hosp.dao.EscolaDAO;

import br.gov.al.maceio.sishosp.hosp.model.EscolaBean;



@FacesConverter(value = "conEscola")
public class EscolaConverter implements Converter {
	   EscolaDAO escolaDAO = new EscolaDAO();
	    public Object getAsObject(FacesContext contet, UIComponent component, String value) {
	        if(value==null || value.equals(""))
	            return null;
	        try{
	            int id = Integer.parseInt(value);
	            return escolaDAO.buscaescolacodigo(id);
	        }catch (Exception e) {
	            e.printStackTrace();
	            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Escola não válido", ""));
	        }
	    }

	    public String getAsString(FacesContext contet, UIComponent component, Object value) {
	        if(value==null || value.equals(""))
	            return null;
	        return String.valueOf(((EscolaBean)value).getCodEscola());
	    }
	}
