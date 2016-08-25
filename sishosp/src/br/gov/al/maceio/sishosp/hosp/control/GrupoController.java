package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.EspecialidadeDAO;
import br.gov.al.maceio.sishosp.hosp.dao.GrupoDAO;
import br.gov.al.maceio.sishosp.hosp.model.EquipeBean;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProfissionalBean;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;


@ManagedBean(name = "GrupoController")
@ViewScoped
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
	private int tipo;
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
		this.cabecalho = "";
		this.programaSelecionado = new ProgramaBean();
		this.equipeSelecionado = new EquipeBean();
		this.profissionalSelecionado = new ProfissionalBean();
	}

	public void limparDados() throws ProjetoException {
		this.grupo = new GrupoBean();
		this.listaGrupos = new ArrayList<>();
		this.listaGruposProgramas = new ArrayList<>();
		this.descricaoBusca = new String();
		listaGrupos = gDao.listarGrupos();
		this.programaSelecionado = new ProgramaBean();
	}
	
	public String redirectEdit() {
		return "cadastroGrupo?faces-redirect=true&amp;id=" + this.grupo.getIdGrupo()+"&amp;tipo="+tipo;
	}	
	
	
	public String redirectInsert() {
		System.out.println("vai redir grupo");
		return "cadastroGrupo?faces-redirect=true&amp;tipo="+tipo;
	}	
	
	
	public void getEditGrupo() throws ProjetoException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Map<String,String> params = facesContext.getExternalContext().getRequestParameterMap();
		System.out.println("vai ve se entrar no editar");
		if(params.get("id") != null) {
			System.out.println("entrou no editar");
			Integer id = Integer.parseInt(params.get("id"));
			tipo =Integer.parseInt(params.get("tipo"));			
			
			GrupoDAO udao = new GrupoDAO();
			this.grupo = udao.listarGrupoPorId(id);
		}
		else{
			
			tipo =Integer.parseInt(params.get("tipo"));
			
		}
		
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

	public void getListaTodosGrupos() throws ProjetoException {
			listaGrupos = gDao.listarGrupos();
			System.out.println("listaGrupos size "+listaGrupos.size());

	}

	public void setListaGrupos(List<GrupoBean> listaGrupos) {
		this.listaGrupos = listaGrupos;
	}

	public void atualizaLista(ProgramaBean p) throws ProjetoException {
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

	public void buscarGrupo() throws ProjetoException {
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

	
	public Integer getAbaAtiva() {
		return abaAtiva;
	}

	public void setAbaAtiva(Integer abaAtiva) {
		this.abaAtiva = abaAtiva;
	}

	public String getCabecalho() {
		if (this.tipo==1) {
			cabecalho = "Inclusão de Grupo";
		} else if (this.tipo==2) {
			cabecalho = "Alteração de Grupo";
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

	public void selectPrograma(SelectEvent event) throws ProjetoException {
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

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public List<GrupoBean> getListaGrupos() {
		return listaGrupos;
	}
}
