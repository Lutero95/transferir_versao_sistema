package br.gov.al.maceio.sishosp.hosp.control;

import java.sql.SQLException;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.EquipeDAO;
import br.gov.al.maceio.sishosp.hosp.model.EquipeBean;

public class EquipeController {

	private EquipeBean equipe;
	private List<EquipeBean> listaEquipe;
	private Integer tipoBuscar;
	private String descricaoBusca;
	private String tipo;
	private Integer abaAtiva = 0;
	private String cabecalho;

	EquipeDAO eDao = new EquipeDAO();

	public EquipeController() {
		this.equipe = new EquipeBean();
		this.listaEquipe = null;
		this.descricaoBusca = new String();
		this.tipo = new String();
		this.descricaoBusca = new String();
	}

	public void limparDados() {
		equipe = new EquipeBean();
		this.descricaoBusca = new String();
		this.tipoBuscar = 1;
		this.listaEquipe = eDao.listarEquipe();
	}


	public EquipeBean getEquipe() {
		return equipe;
	}

	public void setEquipe(EquipeBean equipe) {
		this.equipe = equipe;
	}
	public Integer getTipoBuscar() {
		return tipoBuscar;
	}

	public void setTipoBuscar(Integer tipoBuscar) {
		this.tipoBuscar = tipoBuscar;
	}

	public String getDescricaoBusca() {
		return descricaoBusca;
	}

	public void setDescricaoBusca(String descricaoBusca) {
		this.descricaoBusca = descricaoBusca;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Integer getAbaAtiva() {
		return abaAtiva;
	}

	public void setAbaAtiva(Integer abaAtiva) {
		this.abaAtiva = abaAtiva;
	}

	public List<EquipeBean> getListaEquipe() {
		if (listaEquipe == null) {
			this.listaEquipe = eDao.listarEquipe();
		}
		return listaEquipe;
	}

	public void gravarEquipe() throws ProjetoException, SQLException {

		boolean cadastrou = eDao.gravarEquipe(equipe);

		if (cadastrou == true) {
			limparDados();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Equipe cadastrada com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante o cadastro!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	public void buscarEquipes() {
		this.listaEquipe = eDao.listarEquipeBusca(
				descricaoBusca, tipoBuscar);
	}

	public void alterarEquipe() throws ProjetoException {
		boolean alterou = eDao.alterarEquipe(equipe);
		if (alterou == true) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Equipe alterada com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante o cadastro!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		this.listaEquipe = eDao.listarEquipe();
	}
	
	public void excluirEquipe() throws ProjetoException {
        boolean ok = eDao.excluirEquipe(equipe);
        if(ok == true) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Equipe excluida com sucesso!", "Sucesso");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().execute("PF('dialogAtencao').hide();");
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Ocorreu um erro durante a exclusao!", "Erro");
            FacesContext.getCurrentInstance().addMessage(null, msg);

            RequestContext.getCurrentInstance().execute("PF('dialogAtencao').hide();");
        }
		this.listaEquipe = eDao.listarEquipe();
	}
	
	public String getCabecalho() {
		if(this.tipo.equals("I")){
			cabecalho = "CADASTRO DE EQUIPE";
		}else if(this.tipo.equals("A")){
			cabecalho = "ALTERAR EQUIPE";
		}
		return cabecalho;
	}

	public void setCabecalho(String cabecalho) {
		this.cabecalho = cabecalho;
	}
	
	public List<EquipeBean> listaEquipeAutoComplete(String query)
			throws ProjetoException {
		List<EquipeBean> result = eDao.listarEquipeBusca(query, 1);
		return result;
	}
}
