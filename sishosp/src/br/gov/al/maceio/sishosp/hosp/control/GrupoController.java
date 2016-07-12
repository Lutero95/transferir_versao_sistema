package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.GrupoDAO;
import br.gov.al.maceio.sishosp.hosp.model.EquipeBean;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProfissionalBean;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;

public class GrupoController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GrupoBean grupo;
	private List<GrupoBean> listaGrupos;
	private List<GrupoBean> listaGruposProgramas;
	private Integer tipoBuscar;
	private String descricaoBusca;
	private String tipo;
	private String cabecalho;
	private ProgramaBean programaSelecionado;
	private EquipeBean equipeSelecionado;
	private ProfissionalBean profissionalSelecionado;
	private Integer abaAtiva = 0;

	GrupoDAO gDao = new GrupoDAO();

	public GrupoController() {
		this.grupo = new GrupoBean();
		this.listaGrupos = null;
		this.listaGruposProgramas = new ArrayList<>();
		this.descricaoBusca = new String();
		this.tipo = new String();
		this.cabecalho = "";
		this.programaSelecionado = new ProgramaBean();
		this.equipeSelecionado = new EquipeBean();
		this.profissionalSelecionado = new ProfissionalBean();
	}

	public void limparDados() {
		this.grupo = new GrupoBean();
		this.listaGrupos = new ArrayList<>();
		this.listaGruposProgramas = new ArrayList<>();
		this.descricaoBusca = new String();
		listaGrupos = gDao.listarGrupos();
		this.programaSelecionado = new ProgramaBean();
	}

	public void gravarGrupo() throws ProjetoException, SQLException {

		boolean cadastrou = gDao.gravarGrupo(grupo);

		if (cadastrou == true) {
			limparDados();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Grupo cadastrado com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante o cadastro!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	public String alterarGrupo() throws ProjetoException {
		boolean alterou = gDao.alterarGrupo(grupo);
		if (alterou == true) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Grupo alterado com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			listaGrupos = gDao.listarGrupos();
			return "/pages/sishosp/gerenciarGrupo.faces?faces-redirect=true";
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante o cadastro!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return "";
		}

	}

	public void excluirGrupo() throws ProjetoException {
		boolean ok = gDao.excluirGrupo(grupo);
		if (ok == true) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Grupo excluido com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			RequestContext.getCurrentInstance().execute(
					"PF('dialogAtencao').hide();");
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante a exclusao!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);

			RequestContext.getCurrentInstance().execute(
					"PF('dialogAtencao').hide();");
		}
		listaGrupos = gDao.listarGrupos();
	}

	public GrupoBean getGrupo() {
		return grupo;
	}

	public void setGrupo(GrupoBean grupo) {
		this.grupo = grupo;
	}

	public List<GrupoBean> getListaGrupos() {
		if (listaGrupos == null) {
			listaGrupos = gDao.listarGrupos();
		}
		return listaGrupos;
	}

	public void setListaGrupos(List<GrupoBean> listaGrupos) {
		this.listaGrupos = listaGrupos;
	}

	public void atualizaLista(ProgramaBean p) {
		GrupoDAO gDao = new GrupoDAO();
		this.programaSelecionado = p;
		this.listaGruposProgramas = gDao.listarGruposPorPrograma(p.getIdPrograma());
	}

	public List<GrupoBean> getListaGruposProgramas() {
		return listaGruposProgramas;
	}

	public void setListaGruposProgramas(List<GrupoBean> listaGruposProgramas) {
		this.listaGruposProgramas = listaGruposProgramas;
	}

	public void buscarGrupo() {
		this.listaGrupos = gDao.listarGruposBusca(descricaoBusca, tipoBuscar);
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

	public String getCabecalho() {
		if (this.tipo.equals("I")) {
			cabecalho = "CADASTRO DE GRUPO";
		} else if (this.tipo.equals("A")) {
			cabecalho = "ALTERAR GRUPO";
		}
		return cabecalho;
	}

	public List<GrupoBean> listaGrupoAutoComplete(String query)
			throws ProjetoException {
		return gDao.listarGruposAutoComplete(query, this.programaSelecionado);
	}
	
	public List<GrupoBean> listaGrupoAutoComplete2(String query)
			throws ProjetoException {
		return gDao.listarGruposAutoComplete2(query);
	}

	public void selectPrograma(SelectEvent event) {
		this.programaSelecionado = (ProgramaBean) event.getObject();
		atualizaLista(programaSelecionado);
	}
	
	public void selectEqupProf(SelectEvent event) {
		//if(equipe ID == TRUE){}
		this.equipeSelecionado = (EquipeBean) event.getObject();
		//atualizaLista2(equipeSelecionado);

		//if(profissional ID == TRUE){}
		this.profissionalSelecionado = (ProfissionalBean) event.getObject();
		//atualizaLista3(profissionalSelecionado);
	}
}
