package br.gov.al.maceio.sishosp.financeiro.converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import br.gov.al.maceio.sishosp.financeiro.dao.BuscaDAO;
import br.gov.al.maceio.sishosp.financeiro.model.FornecedorBean;
import br.gov.al.maceio.sishosp.financeiro.model.PortadorBean;

@FacesConverter(value = "conPortador")
public class ConverterPortador implements Converter{
	BuscaDAO bdao = new BuscaDAO();

	public Object getAsObject(FacesContext contet, UIComponent component, String value) {
	       if(value==null || value.equals("") || value.equals("null")){
	       
	           return null;
	       }
	       try{
	           int id = Integer.parseInt(value);
	           return bdao.buscarPortadorCod(id);
	       }catch (Exception e) {
	           e.printStackTrace();
	           throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "ErroC", ""));
	       }
	   }
	
	public String getAsString(FacesContext contet, UIComponent component, Object value) {
	       if(value==null || value.equals(""))
	           return null;
	       return String.valueOf(((PortadorBean)value).getCodportador());
	   }

	
}



