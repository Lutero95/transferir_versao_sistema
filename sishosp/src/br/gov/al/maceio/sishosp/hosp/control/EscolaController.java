package br.gov.al.maceio.sishosp.hosp.control;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.EscolaDAO;
import br.gov.al.maceio.sishosp.hosp.model.EscolaBean;

public class EscolaController {
	private Integer abaAtiva = 0;
	private EscolaBean escola;
	
	
	
	//LISTAS
	private List<EscolaBean> listaTipoEscola;
	private List<EscolaBean> listaEscolas;
	
	//BUSCAS
	private String tipo;
	private Integer tipoBuscaEscola;
	private String campoBuscaEscola;
	private String statusEscola;
	
	public EscolaController(){
		
		escola = new EscolaBean();
		
		 //BUSCA
		tipo ="";
		tipoBuscaEscola = 1;
		campoBuscaEscola = "";
		statusEscola = "P";
		
		 listaEscolas = new ArrayList<>();
		 listaEscolas = null;
		 listaTipoEscola = new ArrayList<>();
	     listaTipoEscola = null;
	}
	
	public void gravarEscola() throws ProjetoException {
		EscolaDAO udao = new EscolaDAO();    
                boolean cadastrou = udao.cadastrar(escola);
                listaEscolas = null;
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
         listaEscolas = null;
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
        listaEscolas = null;
        if(excluio == true) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Escola excluido com sucesso!", "Sucesso");
            FacesContext.getCurrentInstance().addMessage(null, msg);

            RequestContext.getCurrentInstance().execute("PF('dialogAtencao').hide();");
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Ocorreu um erro durante a exclusao!", "Erro");
            FacesContext.getCurrentInstance().addMessage(null, msg);

            RequestContext.getCurrentInstance().execute("PF('dialogAtencao').hide();");
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
	
	public void buscarEscolas() {

		List<EscolaBean> listaAux = null;
		listaEscolas = new ArrayList<>();

		EscolaDAO adao = new EscolaDAO();

		listaAux = adao.buscarTipoEscola(campoBuscaEscola,tipoBuscaEscola);

		if (listaAux != null && listaAux.size() > 0) {
			// listaAss = null;
			listaEscolas = listaAux;
		} else {
			// listaAss = null;
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Nenhuma Escola encontrada.", "Aviso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

	}
	
	public void limparBuscaDados() {
		tipoBuscaEscola = 1;
		campoBuscaEscola = "";
		statusEscola = "P";
		listaEscolas = null;
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

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Integer getTipoBuscaEscola() {
		return tipoBuscaEscola;
	}

	public void setTipoBuscaEscola(Integer tipoBuscaEscola) {
		this.tipoBuscaEscola = tipoBuscaEscola;
	}

	public String getCampoBuscaEscola() {
		return campoBuscaEscola;
	}

	public void setCampoBuscaEscola(String campoBuscaEscola) {
		this.campoBuscaEscola = campoBuscaEscola;
	}

	public String getStatusEscola() {
		return statusEscola;
	}

	public void setStatusEscola(String statusEscola) {
		this.statusEscola = statusEscola;
	}

	public List<EscolaBean> getListaEscolas() {
		if (listaEscolas == null) {

			EscolaDAO fdao = new EscolaDAO();
			listaEscolas = fdao.listaEscolas();

		}
		return listaEscolas;
	}

	public void setListaEscolas(List<EscolaBean> listaEscolas) {
		this.listaEscolas = listaEscolas;
	}

	public Integer getAbaAtiva() {
		return abaAtiva;
	}

	public void setAbaAtiva(Integer abaAtiva) {
		this.abaAtiva = abaAtiva;
	}
	
	
	    
	    

}
