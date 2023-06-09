package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.comum.enums.TipoCabecalho;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.RedirecionarUtil;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.EnderecoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.FornecedorDAO;
import br.gov.al.maceio.sishosp.hosp.model.EnderecoBean;
import br.gov.al.maceio.sishosp.hosp.model.FornecedorBean;

@ManagedBean(name = "FornecedorController")
@ViewScoped
public class FornecedorController implements Serializable {

	private static final long serialVersionUID = 1L;
	private FornecedorBean fornecedor;
	private Integer tipo;
	private String cabecalho;
	private List<FornecedorBean> listaFornecedores;
	private FornecedorDAO fDao = new FornecedorDAO();
	private List<EnderecoBean> listaMunicipios;

	//CONSTANTES
	private static final String ENDERECO_CADASTRO = "cadastroFornecedor?faces-redirect=true";
	private static final String ENDERECO_TIPO = "&amp;tipo=";
	private static final String ENDERECO_ID = "&amp;id=";
	private static final String CABECALHO_INCLUSAO = "Inclusão de Fornecedor";
	private static final String CABECALHO_ALTERACAO = "Alteração de Fornecedor";

	public FornecedorController() {
		fornecedor = new FornecedorBean();
		cabecalho = "";
		listaFornecedores = new ArrayList<>();
		listaMunicipios = new ArrayList<>();
	}

	public void limparDados() {
		this.fornecedor = new FornecedorBean();
	}

	public String redirectEdit() {
		return RedirecionarUtil.redirectEdit(ENDERECO_CADASTRO, ENDERECO_ID, this.fornecedor.getId(), ENDERECO_TIPO, tipo);
	}

	public String redirectInsert() {
		return RedirecionarUtil.redirectInsert(ENDERECO_CADASTRO, ENDERECO_TIPO, tipo);
	}

	public void getEditFornecedor() throws ProjetoException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Map<String, String> params = facesContext.getExternalContext()
				.getRequestParameterMap();
		if (params.get("id") != null) {
			Integer id = Integer.parseInt(params.get("id"));
			tipo = Integer.parseInt(params.get("tipo"));
			this.fornecedor = fDao.listarFornecedorPorId(id);
			EnderecoController enderecoController = new EnderecoController();
			listaMunicipios = enderecoController.listarMunicipiosPorEstadoGenerico(fornecedor.getEndereco().getUf());
		} else {

			tipo = Integer.parseInt(params.get("tipo"));
		}

	}

	public List<EnderecoBean> listarMunicipiosPorEstadoGenerico(String estado)
			throws ProjetoException {
		EnderecoDAO eDao = new EnderecoDAO();
		listaMunicipios = eDao.listaMunicipiosPorEstado(estado);
		return listaMunicipios;
	}

	public void gravarFornecedor() throws ProjetoException {

		boolean cadastrou = fDao.gravarFornecedor(fornecedor);

		if (cadastrou == true) {
			limparDados();
			JSFUtil.adicionarMensagemSucesso("Fornecedor cadastrado com sucesso!", "Sucesso");
		} else {
			JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cadastro!", "Erro");
		}
	}

	public void alterarFornecedor() throws ProjetoException {
		boolean alterou = fDao.alterarFornecedor(fornecedor);

		if (alterou == true) {
			JSFUtil.adicionarMensagemSucesso("Fornecedor alterado com sucesso!", "Sucesso");
		} else {
			JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a alteração!", "Erro");
		}
	}

	public void excluirFornecedor() throws ProjetoException {
		boolean excluiu = fDao.excluirFornecedor(fornecedor.getId());

		if (excluiu == true) {
			JSFUtil.adicionarMensagemSucesso("Fornecedor excluído com sucesso!", "Sucesso");
			JSFUtil.fecharDialog("dialogExclusao");
			listarFornecedores();
		} else {
			JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a exclusão!", "Erro");
			JSFUtil.fecharDialog("dialogExclusao");
		}
	}

	public List<FornecedorBean> listaFornecedorAutoComplete(String query)
			throws ProjetoException {
		List<FornecedorBean> result = fDao.listarFornecedorBusca(query);
		return result;
	}

	public void listarFornecedores() throws ProjetoException {
		listaFornecedores = fDao.listarFornecedores();
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

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public FornecedorBean getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(FornecedorBean fornecedor) {
		this.fornecedor = fornecedor;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public List<FornecedorBean> getListaFornecedores() {
		return listaFornecedores;
	}

	public void setListaFornecedores(List<FornecedorBean> listaFornecedores) {
		this.listaFornecedores = listaFornecedores;
	}

	public List<EnderecoBean> getListaMunicipios() {
		return listaMunicipios;
	}

	public void setListaMunicipios(List<EnderecoBean> listaMunicipios) {
		this.listaMunicipios = listaMunicipios;
	}
}
