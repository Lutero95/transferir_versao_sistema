package br.gov.al.maceio.sishosp.comum.util;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;


public class Utilites {
	
	   public String getDateTime() {
	        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	        Date date = new Date();
	        return dateFormat.format(date);
	    }
	   
	   
	   public String getDate() {
	        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	        Date date = new Date();
	        return dateFormat.format(date);
	    }
	   
	    public String irParaURL(){  
	    	  
	        String retorno = null;  
	  
	  
	  
	        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();  
	        try {  
	           response.sendRedirect("pages/novoprocesso");  
	           FacesContext.getCurrentInstance().responseComplete();  
	        }  
	  
	        catch (Exception ex) {  
	            //comentado walter erro log ex.printStackTrace();  
	        }  
	          
	  
	        return "novoprocesso";  
	  
	  
	    }  	   

}
