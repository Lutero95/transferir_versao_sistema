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
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.dao.ConfiguracaoProducaoBpaDAO;
import br.gov.al.maceio.sishosp.hosp.dao.UnidadeDAO;
import br.gov.al.maceio.sishosp.hosp.model.ConfiguracaoProducaoBpaBean;
import br.gov.al.maceio.sishosp.hosp.model.UnidadeBean;

@ManagedBean
@ViewScoped
public class ConfiguracaoProducaoBpaController {

	private ConfiguracaoProducaoBpaBean configuracaoProducaoBpa;
	private List<ConfiguracaoProducaoBpaBean> listaConfiguracaoProducaoBpa;
	private ConfiguracaoProducaoBpaDAO configuracaoProducaoBpaDAO;
	private Integer tipo;
	private String cabecalho;
	private List<UnidadeBean> listaUnidades;
    FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
            .getSessionMap().get("obj_funcionario");
	
	private static final String ENDERECO_CADASTRO = "cadastroconfiguracaoproducaobpa?faces-redirect=true";
	private static final String ENDERECO_TIPO = "&amp;tipo=";
	private static final String ENDERECO_ID = "&amp;id=";
	private static final String CABECALHO_INCLUSAO = "Inclusão de Configuração BPA";
    private static final String CABECALHO_ALTERACAO = "Alteração de Configuração BPA";
	
	public ConfiguracaoProducaoBpaController() {
		this.configuracaoProducaoBpa = new ConfiguracaoProducaoBpaBean();
		this.listaConfiguracaoProducaoBpa = new ArrayList<>();
		this.configuracaoProducaoBpaDAO = new ConfiguracaoProducaoBpaDAO();
		this.listaUnidades = new ArrayList<>();
	}
	
	public String redirecionaEdicao() {
		return RedirecionarUtil.redirectEdit(ENDERECO_CADASTRO, ENDERECO_ID, this.configuracaoProducaoBpa.getId(), ENDERECO_TIPO, tipo);
	}

	public String redirecionaInsercao() {
	    return RedirecionarUtil.redirectInsert(ENDERECO_CADASTRO, ENDERECO_TIPO, tipo);
	}

	public void getEditaConfiguracaoBpa() throws ProjetoException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Map<String, String> params = facesContext.getExternalContext()
				.getRequestParameterMap();
		if (params.get("id") != null) {
			Integer id = Integer.parseInt(params.get("id"));
			tipo = Integer.parseInt(params.get("tipo"));

			this.configuracaoProducaoBpa = configuracaoProducaoBpaDAO.buscaConfiguracoesBpa(id);
		} else {
			tipo = Integer.parseInt(params.get("tipo"));
		}
	}
	
	public void listarConfiguracoesBpa() throws ProjetoException {
		this.listaConfiguracaoProducaoBpa = this.configuracaoProducaoBpaDAO.listarConfiguracoesBpa(); 
	}
	
	public void listarUnidadesUsuario() throws ProjetoException {
		listaUnidades = new UnidadeDAO().carregarUnidadesDoFuncionario();
		JSFUtil.abrirDialog("dlgConsulUni");
	}
	
	public void adicionarUnidadeSelecionada(UnidadeBean unidadeSelecionada) {
		if(!unidadeJaFoiAdicionada(unidadeSelecionada)) {
			configuracaoProducaoBpa.getListaUnidades().add(unidadeSelecionada);
			JSFUtil.fecharDialog("dlgConsulUni");
		}
	}

	private boolean unidadeJaFoiAdicionada(UnidadeBean unidadeSelecionada) {
		for (UnidadeBean unidade : configuracaoProducaoBpa.getListaUnidades()) {
			if(unidade.getId().equals(unidadeSelecionada.getId())) {
				JSFUtil.adicionarMensagemErro("Está Unidade Já foi Adicionada", "");
				return true;				
			}
		}
		return false;
	}

	public void adicionarTodasUnidadesSelecionadas() {
		configuracaoProducaoBpa.getListaUnidades().clear();
		configuracaoProducaoBpa.getListaUnidades().addAll(listaUnidades);
		JSFUtil.fecharDialog("dlgConsulUni");
	}
	
	public void removerUnidadeAdicionada(UnidadeBean unidadeSelecionada) {
		configuracaoProducaoBpa.getListaUnidades().remove(unidadeSelecionada);
	}
	
	public void gravarConfiguracaoBpa() throws ProjetoException {
		if(!listaUnidadesEstaVazia() && !existeUnidadeComConfiguracaoBpa()) {
			configuracaoProducaoBpa.setOperadorGravacao(user_session.getId().intValue());
			boolean cadastrou = configuracaoProducaoBpaDAO.gravarConfiguracoesBpa(configuracaoProducaoBpa);

			if (cadastrou) {
				JSFUtil.adicionarMensagemSucesso("Configuração cadastrada com sucesso!", "");
				limparDados();
			}
		}
	}
	
	public void alterarConfiguracaoBpa() throws ProjetoException {
		if(!listaUnidadesEstaVazia() && !existeUnidadeComConfiguracaoBpa()) {
			configuracaoProducaoBpa.setOperadorGravacao(user_session.getId().intValue());
			boolean alterou = configuracaoProducaoBpaDAO.alterarConfiguracoesBpa(configuracaoProducaoBpa);

			if (alterou) {
				JSFUtil.adicionarMensagemSucesso("Configuração alterada com sucesso!", "");
			}
		}
	}
	
	public void excluirConfiguracaoBpa() throws ProjetoException {
		configuracaoProducaoBpa.setOperadorExclusao(user_session.getId().intValue());
		boolean excluiu = configuracaoProducaoBpaDAO.excluirConfiguracoesBpa(configuracaoProducaoBpa);

		if (excluiu) {
			JSFUtil.adicionarMensagemSucesso("Configuração excluída com sucesso!", "");
			JSFUtil.fecharDialog("dialogExclusao");
			listarConfiguracoesBpa();
		}
	}
	
	private boolean listaUnidadesEstaVazia() {
		if(configuracaoProducaoBpa.getListaUnidades().isEmpty()) {
			JSFUtil.adicionarMensagemErro("Adicione pelo menos uma unidade", "");
			return true;
		}
		return false;
	}
	
	private boolean existeUnidadeComConfiguracaoBpa() throws ProjetoException {
		String nomeUnidades = configuracaoProducaoBpaDAO.retornaUnidadesQueJaPossuemConfiguracao(configuracaoProducaoBpa);
		if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(nomeUnidades)) {
			JSFUtil.adicionarMensagemErro("A(s) unidade(s) "+nomeUnidades+" já possuem configuração", "");
			return true;
		}
		return false;
	}
	
	private void limparDados() {
		this.configuracaoProducaoBpa = new ConfiguracaoProducaoBpaBean();
	}

	public ConfiguracaoProducaoBpaBean getConfiguracaoProducaoBpa() {
		return configuracaoProducaoBpa;
	}

	public void setConfiguracaoProducaoBpa(ConfiguracaoProducaoBpaBean configuracaoProducaoBpa) {
		this.configuracaoProducaoBpa = configuracaoProducaoBpa;
	}

	public List<ConfiguracaoProducaoBpaBean> getListaConfiguracaoProducaoBpa() {
		return listaConfiguracaoProducaoBpa;
	}

	public void setListaConfiguracaoProducaoBpa(List<ConfiguracaoProducaoBpaBean> listaConfiguracaoProducaoBpa) {
		this.listaConfiguracaoProducaoBpa = listaConfiguracaoProducaoBpa;
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

	public List<UnidadeBean> getListaUnidades() {
		return listaUnidades;
	}

	public void setListaUnidades(List<UnidadeBean> listaUnidades) {
		this.listaUnidades = listaUnidades;
	}
}
