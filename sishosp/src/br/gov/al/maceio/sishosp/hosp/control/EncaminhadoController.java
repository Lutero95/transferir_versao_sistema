package br.gov.al.maceio.sishosp.hosp.control;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.EncaminhadoDAO;
import br.gov.al.maceio.sishosp.hosp.model.EncaminhadoBean;



public class EncaminhadoController {
	
	private EncaminhadoBean encaminhado;

	
	public EncaminhadoController(){
    encaminhado = new EncaminhadoBean();
		
	}
	
	
	public void gravarEncaminhado() throws ProjetoException {
        EncaminhadoDAO udao = new EncaminhadoDAO();    
                boolean cadastrou = udao.cadastrar(encaminhado);

                if(cadastrou == true) {
                	limparDados();
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Encaminhado cadastrado com sucesso!", "Sucesso");
                    FacesContext.getCurrentInstance().addMessage(null, msg);

                    

                } else {
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Ocorreu um erro durante o cadastro!", "Erro");
                    FacesContext.getCurrentInstance().addMessage(null, msg);

 
                }
            
    }
	
	public void alterarEncaminhado() throws ProjetoException {

		EncaminhadoDAO rdao = new EncaminhadoDAO();
         boolean alterou = rdao.alterar(encaminhado);

         if(alterou == true) {

             FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                 "Encaminhado alterado com sucesso!", "Sucesso");
             FacesContext.getCurrentInstance().addMessage(null, msg);

             //RequestContext.getCurrentInstance().execute("dlgAltMenu.hide();");
         } else {
             FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                 "Ocorreu um erro durante o cadastro!", "Erro");
             FacesContext.getCurrentInstance().addMessage(null, msg);

             //RequestContext.getCurrentInstance().execute("dlgAltMenu.hide();");
         }
		
	}
	
	public void excluirEncaminhado() throws ProjetoException {
		EncaminhadoDAO udao = new EncaminhadoDAO();

        boolean excluio = udao.excluir(encaminhado);

        if(excluio == true) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Encaminhado excluido com sucesso!", "Sucesso");
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
		
		 encaminhado = new EncaminhadoBean();
	}
	
	public EncaminhadoBean getEncaminhado() {
		return encaminhado;
	}


	public void setEncaminhado(EncaminhadoBean encaminhado) {
		this.encaminhado = encaminhado;
	}

}


