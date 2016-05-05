package br.gov.al.maceio.sishosp.hosp.control;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.FormaTransporteDAO;
import br.gov.al.maceio.sishosp.hosp.dao.PacienteDAO;
import br.gov.al.maceio.sishosp.hosp.dao.ProfissaoDAO;
import br.gov.al.maceio.sishosp.hosp.model.PacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.ProfissaoBean;



public class ProfissaoController {
	
	private ProfissaoBean profissao;
	private Integer abaAtiva = 0;
	//BUSCAS
		private String tipo;
		private Integer tipoBuscaProfissao;
		private String campoBuscaProfissao;
		private String statusProfissao;
	
	private List<ProfissaoBean> listaProfissoes;
	
	public ProfissaoController(){
		
		profissao = new ProfissaoBean();
		
		listaProfissoes = new ArrayList<>();
		listaProfissoes = null;
		
		 //BUSCA
		tipo ="";
		tipoBuscaProfissao = 1;
		campoBuscaProfissao = "";
		statusProfissao = "P";
	}
	    
	public void gravarProfissao() throws ProjetoException {
		ProfissaoDAO udao = new ProfissaoDAO();    
                boolean cadastrou = udao.cadastrar(profissao);
                listaProfissoes = null;
                if(cadastrou == true) {
                	limparDados();
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Profissao cadastrado com sucesso!", "Sucesso");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                 listaProfissoes = null;
                    

                } else {
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Ocorreu um erro durante o cadastro!", "Erro");
                    FacesContext.getCurrentInstance().addMessage(null, msg);

 
                }
            
    }
	
	public String alterarProfissao() throws ProjetoException {

		ProfissaoDAO rdao = new ProfissaoDAO();
         boolean alterou = rdao.alterar(profissao);
         listaProfissoes = null;
         if(alterou == true) {
        	 limparDados();
             FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                 "Profissao alterado com sucesso!", "Sucesso");
             FacesContext.getCurrentInstance().addMessage(null, msg);
             return "/pages/sishosp/gerenciarProfissoes.faces?faces-redirect=true";

             //RequestContext.getCurrentInstance().execute("dlgAltMenu.hide();");
         } else {
             FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                 "Ocorreu um erro durante o cadastro!", "Erro");
             FacesContext.getCurrentInstance().addMessage(null, msg);
             return "";
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
            listaProfissoes = null;
            RequestContext.getCurrentInstance().execute("PF('dialogAtencao').hide();");
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Ocorreu um erro durante a exclusao!", "Erro");
            FacesContext.getCurrentInstance().addMessage(null, msg);

            RequestContext.getCurrentInstance().execute("PF('dialogAtencao').hide();");
        }
	}
	
	public void buscarProfissoes() {

		List<ProfissaoBean> listaAux = null;
		listaProfissoes = new ArrayList<>();

		ProfissaoDAO adao = new ProfissaoDAO();

		listaAux = adao.buscarTipoProfissao(campoBuscaProfissao,tipoBuscaProfissao);

		if (listaAux != null && listaAux.size() > 0) {
			// listaAss = null;
			listaProfissoes = listaAux;
		} else {
			// listaAss = null;
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Nenhuma Profissao encontrada.", "Aviso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

	}
	
	
	public void limparBuscaDados() {
		tipoBuscaProfissao = 1;
		campoBuscaProfissao = "";
		statusProfissao = "P";
		listaProfissoes = null;
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

	public List<ProfissaoBean> getListaProfissoes() {
		if (listaProfissoes == null) {

			ProfissaoDAO fdao = new ProfissaoDAO();
			listaProfissoes = fdao.listaProfissoes();

		}
		return listaProfissoes;
	}

	public void setListaProfissoes(List<ProfissaoBean> listaProfissoes) {
		this.listaProfissoes = listaProfissoes;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Integer getTipoBuscaProfissao() {
		return tipoBuscaProfissao;
	}

	public void setTipoBuscaProfissao(Integer tipoBuscaProfissao) {
		this.tipoBuscaProfissao = tipoBuscaProfissao;
	}

	public String getCampoBuscaProfissao() {
		return campoBuscaProfissao;
	}

	public void setCampoBuscaProfissao(String campoBuscaProfissao) {
		this.campoBuscaProfissao = campoBuscaProfissao;
	}

	public String getStatusProfissao() {
		return statusProfissao;
	}

	public void setStatusProfissao(String statusProfissao) {
		this.statusProfissao = statusProfissao;
	}

	public Integer getAbaAtiva() {
		return abaAtiva;
	}

	public void setAbaAtiva(Integer abaAtiva) {
		this.abaAtiva = abaAtiva;
	}
	
	
	
	    
}
