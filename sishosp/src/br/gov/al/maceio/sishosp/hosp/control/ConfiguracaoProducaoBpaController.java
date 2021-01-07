package br.gov.al.maceio.sishosp.hosp.control;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.comum.enums.TipoCabecalho;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.RedirecionarUtil;
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
	private List<UnidadeBean> listaUnidadesSelecionadas;
	
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
		this.listaUnidadesSelecionadas = new ArrayList<>();
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
			listaUnidadesSelecionadas.add(unidadeSelecionada);
			JSFUtil.fecharDialog("dlgConsulUni");
		}
	}

	private boolean unidadeJaFoiAdicionada(UnidadeBean unidadeSelecionada) {
		if(listaUnidadesSelecionadas.contains(unidadeSelecionada)) {
			JSFUtil.adicionarMensagemErro("Está Unidade Já foi Adicionada", "");
			return true;
		}
		return false;
	}

	public void adicionarTodasUnidadesSelecionadas() {
		listaUnidadesSelecionadas.clear();
		listaUnidadesSelecionadas.addAll(listaUnidades);
		JSFUtil.fecharDialog("dlgConsulUni");
	}
	
	public void removerUnidadeAdicionada(UnidadeBean unidadeSelecionada) {
			listaUnidadesSelecionadas.remove(unidadeSelecionada);
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

	public List<UnidadeBean> getListaUnidadesSelecionadas() {
		return listaUnidadesSelecionadas;
	}

	public void setListaUnidadesSelecionadas(List<UnidadeBean> listaUnidadesSelecionadas) {
		this.listaUnidadesSelecionadas = listaUnidadesSelecionadas;
	}
}
