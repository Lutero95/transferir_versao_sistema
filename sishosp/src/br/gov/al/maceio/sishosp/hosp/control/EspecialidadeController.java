package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.EspecialidadeDAO;
import br.gov.al.maceio.sishosp.hosp.model.EspecialidadeBean;

public class EspecialidadeController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EspecialidadeBean espec;
	private List<EspecialidadeBean> listaEspecialidade;
	private Integer tipoBuscar;
	private String descricaoBusca;
	private String tipo;
	private Integer abaAtiva = 0;
	private String cabecalho;

	EspecialidadeDAO eDao = new EspecialidadeDAO();

	public EspecialidadeController() {
		this.espec = new EspecialidadeBean();
		this.listaEspecialidade = null;
		this.descricaoBusca = new String();
		this.tipo = new String();
		this.descricaoBusca = new String();
	}

	public void limparDados() {
		espec = new EspecialidadeBean();
		this.descricaoBusca = new String();
		this.tipo = new String();
		this.tipoBuscar = 1;
		this.listaEspecialidade = eDao.listarEspecialidades();
	}

	public EspecialidadeBean getEspec() {
		return espec;
	}

	public void setEspec(EspecialidadeBean espec) {
		this.espec = espec;
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

	public List<EspecialidadeBean> getListaEspecialidade() {
		if (listaEspecialidade == null) {
			this.listaEspecialidade = eDao.listarEspecialidades();
		}
		return listaEspecialidade;
	}

	public void gravarEspecialidade() throws ProjetoException, SQLException {

		boolean cadastrou = eDao.gravarEspecialidade(espec);

		if (cadastrou == true) {
			limparDados();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Especialidade cadastrada com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante o cadastro!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	public void buscarEspecialidades() {
		this.listaEspecialidade = eDao.listarEspecialidadesBusca(
				descricaoBusca, tipoBuscar);
	}

	public String alterarEspecialidade() throws ProjetoException {
		boolean alterou = eDao.alterarEspecialidade(espec);
		if (alterou == true) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Especialidade alterada com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			this.listaEspecialidade = eDao.listarEspecialidades();
			return "/pages/sishosp/gerenciarEspecialidade.faces?faces-redirect=true";
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante o cadastro!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			this.listaEspecialidade = eDao.listarEspecialidades();
			return "";
		}
	}
	
	public void excluirEspecialidade() throws ProjetoException {
        boolean ok = eDao.excluirEspecialidade(espec);
        if(ok == true) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Especialidade excluida com sucesso!", "Sucesso");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().execute("PF('dialogAtencao').hide();");
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Ocorreu um erro durante a exclusao!", "Erro");
            FacesContext.getCurrentInstance().addMessage(null, msg);

            RequestContext.getCurrentInstance().execute("PF('dialogAtencao').hide();");
        }
		this.listaEspecialidade = eDao.listarEspecialidades();
	}
	
	public String getCabecalho() {
		if(this.tipo.equals("I")){
			cabecalho = "CADASTRO DE ESPECIALIDADE";
		}else if(this.tipo.equals("A")){
			cabecalho = "ALTERAR ESPECIALIDADE";
		}
		return cabecalho;
	}

	public void setCabecalho(String cabecalho) {
		this.cabecalho = cabecalho;
	}
}
