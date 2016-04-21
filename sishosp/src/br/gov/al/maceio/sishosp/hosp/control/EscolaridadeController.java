package br.gov.al.maceio.sishosp.hosp.control;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.EscolaDAO;
import br.gov.al.maceio.sishosp.hosp.dao.EscolaridadeDAO;
import br.gov.al.maceio.sishosp.hosp.model.EscolaridadeBean;



public class EscolaridadeController {
	
	
	private EscolaridadeBean escolaridade;
	
	public EscolaridadeController(){
		
		escolaridade = new EscolaridadeBean();
	}
	
	public void gravarEscolaridade() throws ProjetoException {
		EscolaridadeDAO udao = new EscolaridadeDAO();    
                boolean cadastrou = udao.cadastrar(escolaridade);

                if(cadastrou == true) {
                	limparDados();
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Escolaridade cadastrado com sucesso!", "Sucesso");
                    FacesContext.getCurrentInstance().addMessage(null, msg);

                    

                } else {
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Ocorreu um erro durante o cadastro!", "Erro");
                    FacesContext.getCurrentInstance().addMessage(null, msg);

 
                }
            
    }
	
	public void alterarEscolaridade() throws ProjetoException {

		EscolaridadeDAO rdao = new EscolaridadeDAO();
         boolean alterou = rdao.alterar(escolaridade);

         if(alterou == true) {

             FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                 "Escolaridade alterado com sucesso!", "Sucesso");
             FacesContext.getCurrentInstance().addMessage(null, msg);

             //RequestContext.getCurrentInstance().execute("dlgAltMenu.hide();");
         } else {
             FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                 "Ocorreu um erro durante o cadastro!", "Erro");
             FacesContext.getCurrentInstance().addMessage(null, msg);

             //RequestContext.getCurrentInstance().execute("dlgAltMenu.hide();");
         }
		
	}
	
	public void excluirEscolaridade() throws ProjetoException {
		EscolaridadeDAO udao = new EscolaridadeDAO();

        boolean excluio = udao.excluir(escolaridade);

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
		escolaridade = new EscolaridadeBean();
		
	}
	public EscolaridadeBean getEscolaridade() {
		return escolaridade;
	}

	public void setEscolaridade(EscolaridadeBean escolaridade) {
		this.escolaridade = escolaridade;
	}

	    
	    
}
