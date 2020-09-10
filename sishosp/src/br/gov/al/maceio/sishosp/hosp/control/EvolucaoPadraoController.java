package br.gov.al.maceio.sishosp.hosp.control;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.enums.TipoCabecalho;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.RedirecionarUtil;
import br.gov.al.maceio.sishosp.hosp.dao.EvolucaoPadraoDAO;
import br.gov.al.maceio.sishosp.hosp.model.EvolucaoPadraoBean;

@ManagedBean
@ViewScoped
public class EvolucaoPadraoController {

	private Integer tipo;
	private List<EvolucaoPadraoBean> listaEvolucoesPadrao;
	private List<EvolucaoPadraoBean> listaEvolucoesPadraoFiltrada;
	private EvolucaoPadraoBean evolucaoPadrao;
	private EvolucaoPadraoDAO evolucaoPadraoDAO;
	private FuncionarioBean user_session;
	private String cabecalho;
	
	private static final String ENDERECO_CADASTRO = "cadastroevolucaopadrao?faces-redirect=true";
	private static final String ENDERECO_TIPO = "&amp;tipo=";
	private static final String ENDERECO_ID = "&amp;id=";
	private static final String CABECALHO_INCLUSAO = "Inclusão de Evolução Padrão";
    private static final String CABECALHO_ALTERACAO = "Alteração de Evolução Padrão";
	
	public EvolucaoPadraoController() {
		this.listaEvolucoesPadrao = new ArrayList<>();
		this.listaEvolucoesPadraoFiltrada = new ArrayList<>();
		this.evolucaoPadrao = new EvolucaoPadraoBean();
		this.evolucaoPadraoDAO = new EvolucaoPadraoDAO();
		this.user_session =  (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().get("obj_usuario");
	}
	
	public void getEditEvolucaoPadrao() throws ProjetoException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Map<String, String> params = facesContext.getExternalContext()
				.getRequestParameterMap();
		if (params.get("id") != null) {
			Integer id = Integer.parseInt(params.get("id"));
			tipo = Integer.parseInt(params.get("tipo"));

			this.evolucaoPadrao = evolucaoPadraoDAO.buscarEvolucaoPadrao(id);
		} else {
			tipo = Integer.parseInt(params.get("tipo"));
		}
	}
	
	public String redirecionaEditar() {
		return RedirecionarUtil.redirectEdit(ENDERECO_CADASTRO, ENDERECO_ID, this.evolucaoPadrao.getId(), ENDERECO_TIPO, tipo);
	}

	public String redirecionaInserir() {
	    return RedirecionarUtil.redirectInsert(ENDERECO_CADASTRO, ENDERECO_TIPO, tipo);
	}
	
	public void listarEvolucoesPadrao() throws ProjetoException {
		this.listaEvolucoesPadraoFiltrada.clear();
		this.listaEvolucoesPadrao = evolucaoPadraoDAO.listarEvolucoesPadrao(this.user_session.getId());
		this.listaEvolucoesPadraoFiltrada.addAll(this.listaEvolucoesPadrao); 
	}
	
	public void cadastrarEvolucaoPadrao() throws ProjetoException {
		this.evolucaoPadrao.setIdFuncionario(user_session.getId());
		if(evolucaoPadraoDAO.gravarEvolucaoPadrao(this.evolucaoPadrao)) {
			JSFUtil.adicionarMensagemSucesso("Evolução Padrão Cadastrada com Sucesso!", "");
			this.evolucaoPadrao = new EvolucaoPadraoBean();
		}
	}
	
	public void alterarEvolucaoPadrao() throws ProjetoException {
		if(evolucaoPadraoDAO.alterarEvolucaoPadrao(this.evolucaoPadrao)) {
			JSFUtil.adicionarMensagemSucesso("Evolução Padrão Altrerada com Sucesso!", "");
		}
	}
	
	public void abrirDialogExcluir() {
		JSFUtil.abrirDialog("dialogExclusao");
	}
	
	public void excluirEvolucaoPadrao() throws ProjetoException {
		if(evolucaoPadraoDAO.excluirEvolucaoPadrao(this.evolucaoPadrao.getId())) {
			JSFUtil.adicionarMensagemSucesso("Evolução Padrão Excluída com Sucesso!", "");
			this.evolucaoPadrao = new EvolucaoPadraoBean();			
			listarEvolucoesPadrao();
			JSFUtil.fecharDialog("dialogExclusao");
		}
	}

	public List<EvolucaoPadraoBean> getListaEvolucoesPadrao() {
		return listaEvolucoesPadrao;
	}

	public void setListaEvolucoesPadrao(List<EvolucaoPadraoBean> listaEvolucoesPadrao) {
		this.listaEvolucoesPadrao = listaEvolucoesPadrao;
	}

	public EvolucaoPadraoBean getEvolucaoPadrao() {
		return evolucaoPadrao;
	}

	public void setEvolucaoPadrao(EvolucaoPadraoBean evolucaoPadrao) {
		this.evolucaoPadrao = evolucaoPadrao;
	}

	public EvolucaoPadraoDAO getEvolucaoPadraoDAO() {
		return evolucaoPadraoDAO;
	}

	public void setEvolucaoPadraoDAO(EvolucaoPadraoDAO evolucaoPadraoDAO) {
		this.evolucaoPadraoDAO = evolucaoPadraoDAO;
	}

	public Integer getTipo() {
		return tipo;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

	public String getCabecalho() {
		if (this.tipo.equals(TipoCabecalho.INCLUSAO.getSigla())) {
			cabecalho = CABECALHO_INCLUSAO;
		} else if (this.tipo.equals(TipoCabecalho.ALTERACAO.getSigla())) {
			cabecalho = CABECALHO_ALTERACAO;
		}
		return cabecalho;
	}

	public void setCabecalho(String cabecalho) {
		this.cabecalho = cabecalho;
	}

	public List<EvolucaoPadraoBean> getListaEvolucoesPadraoFiltrada() {
		return listaEvolucoesPadraoFiltrada;
	}

	public void setListaEvolucoesPadraoFiltrada(List<EvolucaoPadraoBean> listaEvolucoesPadraoFiltrada) {
		this.listaEvolucoesPadraoFiltrada = listaEvolucoesPadraoFiltrada;
	}
	
}
