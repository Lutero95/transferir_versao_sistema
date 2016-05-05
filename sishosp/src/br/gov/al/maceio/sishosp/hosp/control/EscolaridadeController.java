package br.gov.al.maceio.sishosp.hosp.control;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.EscolaridadeDAO;
import br.gov.al.maceio.sishosp.hosp.model.EscolaridadeBean;



public class EscolaridadeController {
	
	private Integer abaAtiva = 0;
	private EscolaridadeBean escolaridade;
	
	
	
	//BUSCAS
	private String tipo;
	private Integer tipoBuscaEscolaridade;
	private String campoBuscaEscolaridade;
	private String statusEscolaridade;
	private List<EscolaridadeBean> listaEscolaridade;
	public EscolaridadeController(){
		
		escolaridade = new EscolaridadeBean();
		
		 //BUSCA
		tipo ="";
		tipoBuscaEscolaridade = 1;
		campoBuscaEscolaridade = "";
		statusEscolaridade = "P";
		
		 listaEscolaridade = new ArrayList<>();
		 listaEscolaridade = null;
	}
	
	public void gravarEscolaridade() throws ProjetoException {
		EscolaridadeDAO udao = new EscolaridadeDAO();    
                boolean cadastrou = udao.cadastrar(escolaridade);

                if(cadastrou == true) {
                	limparDados();
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Escolaridade cadastrado com sucesso!", "Sucesso");
                    FacesContext.getCurrentInstance().addMessage(null, msg);

                    listaEscolaridade = null;

                } else {
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Ocorreu um erro durante o cadastro!", "Erro");
                    FacesContext.getCurrentInstance().addMessage(null, msg);

 
                }
            
    }
	
	public String alterarEscolaridade() throws ProjetoException {

		EscolaridadeDAO rdao = new EscolaridadeDAO();
         boolean alterou = rdao.alterar(escolaridade);

         if(alterou == true) {
             limparDados();
             FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                 "Escolaridade alterado com sucesso!", "Sucesso");
             FacesContext.getCurrentInstance().addMessage(null, msg);
             return "/pages/sishosp/gerenciarEscolaridade.faces?faces-redirect=true";
             //RequestContext.getCurrentInstance().execute("dlgAltMenu.hide();");
         } else {
             FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                 "Ocorreu um erro durante o cadastro!", "Erro");
             FacesContext.getCurrentInstance().addMessage(null, msg);
             return "";
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
            listaEscolaridade = null;
            RequestContext.getCurrentInstance().execute("PF('dialogAtencao').hide();");
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Ocorreu um erro durante a exclusao!", "Erro");
            FacesContext.getCurrentInstance().addMessage(null, msg);

            RequestContext.getCurrentInstance().execute("PF('dialogAtencao').hide();");
        }
	}
	
	public void buscarEscolaridades() {

		List<EscolaridadeBean> listaAux = null;
		listaEscolaridade = new ArrayList<>();

		EscolaridadeDAO adao = new EscolaridadeDAO();

		listaAux = adao.buscarTipoEscolaridade(campoBuscaEscolaridade,tipoBuscaEscolaridade);

		if (listaAux != null && listaAux.size() > 0) {
			// listaAss = null;
			listaEscolaridade = listaAux;
		} else {
			// listaAss = null;
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Nenhuma Escolaridade encontrada.", "Aviso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

	}
	
	public void limparBuscaDados() {
		tipoBuscaEscolaridade = 1;
		campoBuscaEscolaridade = "";
		statusEscolaridade = "P";
		listaEscolaridade = null;
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

	public Integer getAbaAtiva() {
		return abaAtiva;
	}

	public void setAbaAtiva(Integer abaAtiva) {
		this.abaAtiva = abaAtiva;
	}



	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Integer getTipoBuscaEscolaridade() {
		return tipoBuscaEscolaridade;
	}

	public void setTipoBuscaEscolaridade(Integer tipoBuscaEscolaridade) {
		this.tipoBuscaEscolaridade = tipoBuscaEscolaridade;
	}

	public String getCampoBuscaEscolaridade() {
		return campoBuscaEscolaridade;
	}

	public void setCampoBuscaEscolaridade(String campoBuscaEscolaridade) {
		this.campoBuscaEscolaridade = campoBuscaEscolaridade;
	}

	public String getStatusEscolaridade() {
		return statusEscolaridade;
	}

	public void setStatusEscolaridade(String statusEscolaridade) {
		this.statusEscolaridade = statusEscolaridade;
	}

	public List<EscolaridadeBean> getListaEscolaridade() {
		if (listaEscolaridade == null) {

			EscolaridadeDAO fdao = new EscolaridadeDAO();
			listaEscolaridade = fdao.listaEscolaridade();

		}
		return listaEscolaridade;
	}

	public void setListaEscolaridade(List<EscolaridadeBean> listaEscolaridade) {
		this.listaEscolaridade = listaEscolaridade;
	}

	    
	    
}
