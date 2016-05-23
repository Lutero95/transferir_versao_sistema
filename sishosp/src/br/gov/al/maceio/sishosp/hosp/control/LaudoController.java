package br.gov.al.maceio.sishosp.hosp.control;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.LaudoDAO;
import br.gov.al.maceio.sishosp.hosp.model.LaudoBean;


public class LaudoController {
	private Integer abaAtiva = 0;
	private LaudoBean laudo;
	
	
	
	//LISTAS
	private List<LaudoBean> listaLaudos;
	
	//BUSCAS
	private String tipo;
	private Integer tipoBuscaLaudo;
	private String campoBuscaLaudo;
	private String statusLaudo;
	
	public LaudoController(){
		
		laudo = new LaudoBean();
		
		 //BUSCA
		tipo ="";
		tipoBuscaLaudo = 1;
		campoBuscaLaudo = "";
		statusLaudo = "P";
		
		 listaLaudos = new ArrayList<>();
		 listaLaudos = null;

	}
	
	public void gravarLaudoApac() throws ProjetoException {
		LaudoDAO udao = new LaudoDAO();    
                boolean cadastrou = udao.cadastrarLaudoApac(laudo);
               
                if(cadastrou == true) {
                	limparDados();
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Laudo Apac cadastrado com sucesso!", "Sucesso");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    listaLaudos = null;
                    

                } else {
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Ocorreu um erro durante o cadastro!", "Erro");
                    FacesContext.getCurrentInstance().addMessage(null, msg);

 
                }
            
    }
	
	public String alterarLaudoApac() throws ProjetoException {

		LaudoDAO rdao = new LaudoDAO();
         boolean alterou = rdao.alterarLaudoApac(laudo);
         listaLaudos = null;
         if(alterou == true) {
             limparDados();
             FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                 "Laudo Apac alterado com sucesso!", "Sucesso");
             FacesContext.getCurrentInstance().addMessage(null, msg);
             return "/pages/sishosp/gerenciarEscola.faces?faces-redirect=true";
             //RequestContext.getCurrentInstance().execute("dlgAltMenu.hide();");
         } else {
             FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                 "Ocorreu um erro durante o cadastro!", "Erro");
             FacesContext.getCurrentInstance().addMessage(null, msg);
             return "";
             //RequestContext.getCurrentInstance().execute("dlgAltMenu.hide();");
         }
		
	}
	
	public void excluirLaudoApac() throws ProjetoException {
		LaudoDAO udao = new LaudoDAO();

        boolean excluio = udao.excluirLaudoApac(laudo);
        
        if(excluio == true) {
        	
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Laudo Apac excluido com sucesso!", "Sucesso");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            listaLaudos = null;
            RequestContext.getCurrentInstance().execute("PF('dialogAtencao').hide();");
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Ocorreu um erro durante a exclusao!", "Erro");
            FacesContext.getCurrentInstance().addMessage(null, msg);

            RequestContext.getCurrentInstance().execute("PF('dialogAtencao').hide();");
        }
	}
	
	public void gravarLaudoBpi() throws ProjetoException {
		LaudoDAO udao = new LaudoDAO();  
		
                boolean cadastrou = udao.cadastrarLaudoBpi(laudo);
                
                if(cadastrou == true) {
                	limparDados();
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Laudo Bpi cadastrado com sucesso!", "Sucesso");
                    FacesContext.getCurrentInstance().addMessage(null, msg);

                    

                } else {
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Ocorreu um erro durante o cadastro!", "Erro");
                    FacesContext.getCurrentInstance().addMessage(null, msg);

 
                }
            
    }
	
	public void alterarLaudoBpi() throws ProjetoException {

		LaudoDAO rdao = new LaudoDAO();
         boolean alterou = rdao.alterarLaudoBpi(laudo);

         if(alterou == true) {

             FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                 "Laudo Bpi alterado com sucesso!", "Sucesso");
             FacesContext.getCurrentInstance().addMessage(null, msg);

             //RequestContext.getCurrentInstance().execute("dlgAltMenu.hide();");
         } else {
             FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                 "Ocorreu um erro durante o cadastro!", "Erro");
             FacesContext.getCurrentInstance().addMessage(null, msg);

             //RequestContext.getCurrentInstance().execute("dlgAltMenu.hide();");
         }
		
	}
	
	public void excluirLaudoBpi() throws ProjetoException {
		LaudoDAO udao = new LaudoDAO();

        boolean excluio = udao.excluirLaudoBpi(laudo);

        if(excluio == true) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Laudo Bpi excluido com sucesso!", "Sucesso");
            FacesContext.getCurrentInstance().addMessage(null, msg);

            //RequestContext.getCurrentInstance().execute("PF('dialogAtencao').hide();");
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Ocorreu um erro durante a exclusao!", "Erro");
            FacesContext.getCurrentInstance().addMessage(null, msg);

            //RequestContext.getCurrentInstance().execute("PF('dialogAtencao').hide();");
        }
	}
	
	public void buscarEscolas() {

		List<LaudoBean> listaAux = null;
		listaLaudos = new ArrayList<>();

		LaudoDAO adao = new LaudoDAO();

		listaAux = adao.buscarTipoLaudo(campoBuscaLaudo,tipoBuscaLaudo);

		if (listaAux != null && listaAux.size() > 0) {
			// listaAss = null;
			listaLaudos = listaAux;
		} else {
			// listaAss = null;
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Nenhuma Escola encontrada.", "Aviso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

	}
	
	public void limparBuscaDados() {
		tipoBuscaLaudo = 1;
		campoBuscaLaudo = "";
		statusLaudo = "P";
		listaLaudos = null;
	}
	
	public void limparDados(){
		laudo = new LaudoBean();
	
	}
	



	public Integer getAbaAtiva() {
		return abaAtiva;
	}

	public void setAbaAtiva(Integer abaAtiva) {
		this.abaAtiva = abaAtiva;
	}

	public LaudoBean getLaudo() {
		return laudo;
	}

	public void setLaudo(LaudoBean laudo) {
		this.laudo = laudo;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Integer getTipoBuscaLaudo() {
		return tipoBuscaLaudo;
	}

	public void setTipoBuscaLaudo(Integer tipoBuscaLaudo) {
		this.tipoBuscaLaudo = tipoBuscaLaudo;
	}

	public String getCampoBuscaLaudo() {
		return campoBuscaLaudo;
	}

	public void setCampoBuscaLaudo(String campoBuscaLaudo) {
		this.campoBuscaLaudo = campoBuscaLaudo;
	}

	public String getStatusLaudo() {
		return statusLaudo;
	}

	public void setStatusLaudo(String statusLaudo) {
		this.statusLaudo = statusLaudo;
	}

	public List<LaudoBean> getListaLaudos() {
		if (listaLaudos == null) {

			LaudoDAO fdao = new LaudoDAO();
			listaLaudos = fdao.listaLaudos();

		}
		return listaLaudos;
	}

	public void setListaLaudos(List<LaudoBean> listaLaudos) {
		this.listaLaudos = listaLaudos;
	}

	    
	    

}
