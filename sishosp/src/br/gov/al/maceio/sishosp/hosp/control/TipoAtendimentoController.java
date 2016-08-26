package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.TipoAtendimentoDAO;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.TipoAtendimentoBean;

@ManagedBean(name="TipoAtendimentoController")
@ViewScoped
public class TipoAtendimentoController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TipoAtendimentoBean tipo;
	private List<TipoAtendimentoBean> listaTipos;
	private Integer tipoBuscar;
	private String descricaoBusca;
	private int tipoS;
	private GrupoBean grupoSelecionado;
	private String cabecalho;

	private Integer abaAtiva = 0;

	TipoAtendimentoDAO tDao = new TipoAtendimentoDAO();

	public TipoAtendimentoController() {
		this.tipo = new TipoAtendimentoBean();
		this.listaTipos = null;
		this.descricaoBusca = new String();
		this.grupoSelecionado = new GrupoBean();
	}

	public void limparDados() throws ProjetoException {
		this.tipo = new TipoAtendimentoBean();
		this.listaTipos = null;
		this.descricaoBusca = new String();
		this.listaTipos = tDao.listarTipoAt();
		this.grupoSelecionado = new GrupoBean();
	}

	public TipoAtendimentoBean getTipo() {
		return tipo;
	}

	public void setTipo(TipoAtendimentoBean tipo) {
		this.tipo = tipo;
	}

	public void listarTipos() throws ProjetoException {
			listaTipos = tDao.listarTipoAt();


	}

	public void atualizaListaTipos(GrupoBean g) throws ProjetoException {
		this.grupoSelecionado = g;
		this.listaTipos = tDao.listarTipoAtPorGrupo(g.getIdGrupo());
	}

	public void buscarTipoAt() throws ProjetoException {
		this.listaTipos = tDao.listarTipoAtBusca(descricaoBusca, tipoBuscar);
	}

	public void listarTodosTipos() throws ProjetoException {
		this.listaTipos = tDao.listarTipoAt();
	}

	public void setListaTipos(List<TipoAtendimentoBean> listaTipos) {
		this.listaTipos = listaTipos;
	}

	public void gravarTipo() throws ProjetoException, SQLException {
		if (this.tipo.getGrupo().isEmpty()) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"É necessário no minimo informar um grupo!", "Campos obrigatórios!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		boolean cadastrou = tDao.gravarTipoAt(tipo);

		if (cadastrou == true) {
			limparDados();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Tipo de Atendimento cadastrado com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante o cadastro!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		listaTipos = tDao.listarTipoAt();
	}

	public String redirectEdit() {
		return "cadastroTipoAtendimento?faces-redirect=true&amp;id=" + this.tipo.getIdTipo()+"&amp;tipo="+tipoS;
	}	
	
	
	public String redirectInsert() {
		return "cadastroTipoAtendimento?faces-redirect=true&amp;tipo="+tipoS;
	}		
	
	public void getEditTipoAtend() throws ProjetoException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Map<String,String> params = facesContext.getExternalContext().getRequestParameterMap();
		System.out.println("vai ve se entrar no editar");
		if(params.get("id") != null) {
			System.out.println("entrou no editar");
			Integer id = Integer.parseInt(params.get("id"));
			TipoAtendimentoDAO cDao = new TipoAtendimentoDAO();
			tipoS =Integer.parseInt(params.get("tipo"));			
			this.tipo = cDao.listarTipoPorId(id);
		}
		else{
			System.out.println("tipo sera"+tipo);
			tipoS =Integer.parseInt(params.get("tipo"));
			
		}
		
	}
	
	
	public String alterarTipo() throws ProjetoException {
		if (this.tipo.getGrupo().isEmpty()) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"É necessário no mínimo informar um grupo!", "Campos obrigat�rios!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return "";
		}
		boolean alterou = tDao.alterarTipo(this.tipo);
		if (alterou == true) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Tipo de atendimento alterado com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			listaTipos = tDao.listarTipoAt();
			return "/pages/sishosp/gerenciarTipoAtendimento.faces?faces-redirect=true";
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante o cadastro!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return "";
		}

	}

	public void excluirTipo() throws ProjetoException {
		boolean ok = tDao.excluirTipo(this.tipo);
		if (ok == true) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Tipo de atendimento excluido com sucesso!", "Sucesso");
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
		listaTipos = tDao.listarTipoAt();
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
		if (this.tipoS==1) {
			cabecalho = "Inclusão de tipo de Atendimento";
		} else if (this.tipoS==2) {
			cabecalho = "Alteração de tipo de Atendimento";
		}
		return cabecalho;
	}

	public void setCabecalho(String cabecalho) {
		this.cabecalho = cabecalho;
	}

	public List<TipoAtendimentoBean> listaTipoAtAutoComplete(String query)
			throws ProjetoException {
		return tDao.listarTipoAtAutoComplete(query, this.grupoSelecionado);
	}

	public void selectGrupo(SelectEvent event) throws ProjetoException {
		this.grupoSelecionado = (GrupoBean) event.getObject();
		atualizaListaTipos(grupoSelecionado);
	}

	public List<TipoAtendimentoBean> listaTipoAtAutoCompleteTodos(String query)
			throws ProjetoException {
		return tDao.listarTipoAtBusca(query, 1);
	}

	public List<TipoAtendimentoBean> getListaTipos() {
		return listaTipos;
	}

	public int getTipoS() {
		return tipoS;
	}

	public void setTipoS(int tipoS) {
		this.tipoS = tipoS;
	}
};
