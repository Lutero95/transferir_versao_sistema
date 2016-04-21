package br.gov.al.maceio.sishosp.hosp.control;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.FormaTransporteDAO;
import br.gov.al.maceio.sishosp.hosp.dao.ProfissaoDAO;
import br.gov.al.maceio.sishosp.hosp.model.ProfissaoBean;



public class ProfissaoController {
	
	private ProfissaoBean profissao;
	
	public ProfissaoController(){
		
		profissao = new ProfissaoBean();
	}
	    
	public void gravarProfissao() throws ProjetoException {
		ProfissaoDAO udao = new ProfissaoDAO();    
                boolean cadastrou = udao.cadastrar(profissao);

                if(cadastrou == true) {
                	limparDados();
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Profissao cadastrado com sucesso!", "Sucesso");
                    FacesContext.getCurrentInstance().addMessage(null, msg);

                    

                } else {
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Ocorreu um erro durante o cadastro!", "Erro");
                    FacesContext.getCurrentInstance().addMessage(null, msg);

 
                }
            
    }
	
	public void alterarProfissao() throws ProjetoException {

		ProfissaoDAO rdao = new ProfissaoDAO();
         boolean alterou = rdao.alterar(profissao);

         if(alterou == true) {

             FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                 "Profissao alterado com sucesso!", "Sucesso");
             FacesContext.getCurrentInstance().addMessage(null, msg);

             //RequestContext.getCurrentInstance().execute("dlgAltMenu.hide();");
         } else {
             FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                 "Ocorreu um erro durante o cadastro!", "Erro");
             FacesContext.getCurrentInstance().addMessage(null, msg);

             //RequestContext.getCurrentInstance().execute("dlgAltMenu.hide();");
         }
		
	}
	
	public void excluirProfissao() throws ProjetoException {
		ProfissaoDAO udao = new ProfissaoDAO();

        boolean excluio = udao.excluir(profissao);

        if(excluio == true) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Profissao excluido com sucesso!", "Sucesso");
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
		profissao = new ProfissaoBean();
		
	}

	public ProfissaoBean getProfissao() {
		return profissao;
	}

	public void setProfissao(ProfissaoBean profissao) {
		this.profissao = profissao;
	}
	
	
	    
}
