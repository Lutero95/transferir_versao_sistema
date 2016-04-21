package br.gov.al.maceio.sishosp.hosp.control;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.EnderecoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.EscolaDAO;
import br.gov.al.maceio.sishosp.hosp.model.EscolaBean;

public class EscolaController {
	
	private EscolaBean escola;
	
	//LISTAS
	private List<EscolaBean> listaTipoEscola;
	
	public EscolaController(){
		
		escola = new EscolaBean();
		
		 listaTipoEscola = new ArrayList<>();
	     listaTipoEscola = null;
	}
	
	public void gravarEscola() throws ProjetoException {
		EscolaDAO udao = new EscolaDAO();    
                boolean cadastrou = udao.cadastrar(escola);

                if(cadastrou == true) {
                	limparDados();
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Escola cadastrado com sucesso!", "Sucesso");
                    FacesContext.getCurrentInstance().addMessage(null, msg);

                    

                } else {
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Ocorreu um erro durante o cadastro!", "Erro");
                    FacesContext.getCurrentInstance().addMessage(null, msg);

 
                }
            
    }
	
	public void alterarEscola() throws ProjetoException {

		EscolaDAO rdao = new EscolaDAO();
         boolean alterou = rdao.alterar(escola);

         if(alterou == true) {

             FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                 "Escola alterado com sucesso!", "Sucesso");
             FacesContext.getCurrentInstance().addMessage(null, msg);

             //RequestContext.getCurrentInstance().execute("dlgAltMenu.hide();");
         } else {
             FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                 "Ocorreu um erro durante o cadastro!", "Erro");
             FacesContext.getCurrentInstance().addMessage(null, msg);

             //RequestContext.getCurrentInstance().execute("dlgAltMenu.hide();");
         }
		
	}
	
	public void excluirEscola() throws ProjetoException {
		EscolaDAO udao = new EscolaDAO();

        boolean excluio = udao.excluir(escola);

        if(excluio == true) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Escola excluido com sucesso!", "Sucesso");
            FacesContext.getCurrentInstance().addMessage(null, msg);

            //RequestContext.getCurrentInstance().execute("PF('dialogAtencao').hide();");
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Ocorreu um erro durante a exclusao!", "Erro");
            FacesContext.getCurrentInstance().addMessage(null, msg);

            //RequestContext.getCurrentInstance().execute("PF('dialogAtencao').hide();");
        }
	}
	
	public void gravarTipoEscola() throws ProjetoException {
		EscolaDAO udao = new EscolaDAO();  
		
                boolean cadastrou = udao.cadastrarTipoEscola(escola);
                
                if(cadastrou == true) {
                	limparDados();
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Tipo Escola cadastrado com sucesso!", "Sucesso");
                    FacesContext.getCurrentInstance().addMessage(null, msg);

                    

                } else {
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Ocorreu um erro durante o cadastro!", "Erro");
                    FacesContext.getCurrentInstance().addMessage(null, msg);

 
                }
            
    }
	
	public void alterarTipoEscola() throws ProjetoException {

		EscolaDAO rdao = new EscolaDAO();
         boolean alterou = rdao.alterarTipoEscola(escola);

         if(alterou == true) {

             FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                 "Tipo Escola alterado com sucesso!", "Sucesso");
             FacesContext.getCurrentInstance().addMessage(null, msg);

             //RequestContext.getCurrentInstance().execute("dlgAltMenu.hide();");
         } else {
             FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                 "Ocorreu um erro durante o cadastro!", "Erro");
             FacesContext.getCurrentInstance().addMessage(null, msg);

             //RequestContext.getCurrentInstance().execute("dlgAltMenu.hide();");
         }
		
	}
	
	public void excluirTipoEscola() throws ProjetoException {
		EscolaDAO udao = new EscolaDAO();

        boolean excluio = udao.excluirTipoEscola(escola);

        if(excluio == true) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Tipo Escola excluido com sucesso!", "Sucesso");
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
		escola = new EscolaBean();
	
	}
	

	public EscolaBean getEscola() {
		return escola;
	}

	public void setEscola(EscolaBean escola) {
		this.escola = escola;
	}

	public List<EscolaBean> getListaTipoEscola() {
		if(listaTipoEscola == null) {
			
            EscolaDAO fdao = new EscolaDAO();
            listaTipoEscola  = fdao.listaTipoEscola(); 
        }
		return listaTipoEscola;
	}

	public void setListaTipoEscola(List<EscolaBean> listaTipoEscola) {
		this.listaTipoEscola = listaTipoEscola;
	}
	
	
	    
	    

}
