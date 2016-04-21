package br.gov.al.maceio.sishosp.hosp.control;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.FormaTransporteDAO;
import br.gov.al.maceio.sishosp.hosp.model.FormaTransporteBean;




public class FormaTransporteController {
	
	
	private FormaTransporteBean transporte;
	
	public FormaTransporteController(){
		transporte = new FormaTransporteBean();
		
		
	}
	
	public void gravarTransporte() throws ProjetoException {
		FormaTransporteDAO udao = new FormaTransporteDAO();    
                boolean cadastrou = udao.cadastrar(transporte);

                if(cadastrou == true) {
                	limparDados();
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Transporte cadastrado com sucesso!", "Sucesso");
                    FacesContext.getCurrentInstance().addMessage(null, msg);

                    

                } else {
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Ocorreu um erro durante o cadastro!", "Erro");
                    FacesContext.getCurrentInstance().addMessage(null, msg);

 
                }
            
    }
	
	public void alterarTransporte() throws ProjetoException {

		FormaTransporteDAO rdao = new FormaTransporteDAO();
         boolean alterou = rdao.alterar(transporte);

         if(alterou == true) {

             FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                 "Transporte alterado com sucesso!", "Sucesso");
             FacesContext.getCurrentInstance().addMessage(null, msg);

             //RequestContext.getCurrentInstance().execute("dlgAltMenu.hide();");
         } else {
             FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                 "Ocorreu um erro durante o cadastro!", "Erro");
             FacesContext.getCurrentInstance().addMessage(null, msg);

             //RequestContext.getCurrentInstance().execute("dlgAltMenu.hide();");
         }
		
	}
	
	public void excluirTransporte() throws ProjetoException {
		FormaTransporteDAO udao = new FormaTransporteDAO();

        boolean excluio = udao.excluir(transporte);

        if(excluio == true) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Transporte excluido com sucesso!", "Sucesso");
            FacesContext.getCurrentInstance().addMessage(null, msg);

            //RequestContext.getCurrentInstance().execute("PF('dialogAtencao').hide();");
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Ocorreu um erro durante a exclusao!", "Erro");
            FacesContext.getCurrentInstance().addMessage(null, msg);

            //RequestContext.getCurrentInstance().execute("PF('dialogAtencao').hide();");
        }
	}
	
	public void limparDados(){
		transporte = new FormaTransporteBean();
		
	}

	public FormaTransporteBean getTransporte() {
		return transporte;
	}

	public void setTransporte(FormaTransporteBean transporte) {
		this.transporte = transporte;
	}

    
	    
}
