package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.EnderecoDAO;
import br.gov.al.maceio.sishosp.hosp.model.EnderecoBean;

@ManagedBean(name = "EnderecoController")
@ViewScoped
public class EnderecoController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer abaAtiva = 0;
	// CLASSES HERDADAS
	private EnderecoBean endereco;

	// LISTAS
	private List<EnderecoBean> listaMunicipios;
	private List<EnderecoBean> listaBairros;

	// BUSCAS
	private int tipo;
	private Integer tipoBuscaMunicipio;
	private String campoBuscaMunicipio;
	private String statusMunicipio;
	private String cabecalho;
	private Integer tipoBuscaBairro;
	private String campoBuscaBairro;
	private String statusBairro;
	private String cabecalhoBairro;
	private String bairro;
	private Integer codbairro;

	public EnderecoController() {
		endereco = new EnderecoBean();

		// BUSCA
		tipoBuscaMunicipio = 1;
		campoBuscaMunicipio = "";
		statusMunicipio = "P";
		tipoBuscaBairro = 1;
		campoBuscaBairro = "";
		statusBairro = "P";

		// LISTAS
		listaMunicipios = new ArrayList<>();
		listaMunicipios = null;
		listaBairros = new ArrayList<>();
		listaBairros = null;
	}

	public String redirectEdit() {
		return "cadastroMunicipios?faces-redirect=true&amp;id="
				+ this.endereco.getCodmunicipio() + "&amp;tipo=" + tipo;
	}

	public String redirectEditBairro() {
		return "cadastroBairros?faces-redirect=true&amp;id="
				+ this.endereco.getCodbairro() + "&amp;tipo=" + tipo;
	}

	public String redirectInsert() {
		return "cadastroMunicipios?faces-redirect=true&amp;tipo=" + tipo;
	}

	public String redirectInsertBairro() {
		return "cadastroBairros?faces-redirect=true&amp;tipo=" + tipo;
	}

	public void getEditMunicipio() throws ProjetoException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Map<String, String> params = facesContext.getExternalContext()
				.getRequestParameterMap();
		if (params.get("id") != null) {
			Integer id = Integer.parseInt(params.get("id"));
			tipo = Integer.parseInt(params.get("tipo"));
			EnderecoDAO cDao = new EnderecoDAO();
			this.endereco = cDao.listarMunicipioPorId(id);
		} else {
			tipo = Integer.parseInt(params.get("tipo"));

		}

	}

	public void getEditBairro() throws ProjetoException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Map<String, String> params = facesContext.getExternalContext()
				.getRequestParameterMap();
		if (params.get("id") != null) {
			Integer id = Integer.parseInt(params.get("id"));
			tipo = Integer.parseInt(params.get("tipo"));
			EnderecoDAO cDao = new EnderecoDAO();
			this.endereco = cDao.listarBairroPorId(id);
			// foi criado bairro e codbairro porque não estava indo pelos atributos
			// do objeto endereco
			bairro = endereco.getBairro();
			codbairro = endereco.getCodbairro();
		} else {
			tipo = Integer.parseInt(params.get("tipo"));

		}

	}

	public void gravarLogradouro() throws ProjetoException {
		EnderecoDAO udao = new EnderecoDAO();

		boolean cadastrou = udao.cadastrarLogradouro(endereco);

		if (cadastrou == true) {
			limparDados();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Logradouro cadastrado com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);

		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante o cadastro!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);

		}

	}

	public void gravarMunicipios() throws ProjetoException {
		EnderecoDAO udao = new EnderecoDAO();

		boolean cadastrou = udao.cadastrarMunicipio(endereco);

		if (cadastrou == true) {
			limparDados();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Município cadastrado com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);

			listaMunicipios = null;

		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante o cadastro!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);

		}

	}

	public void gravarBairros() throws ProjetoException {
		EnderecoDAO udao = new EnderecoDAO();
		// foi criado bairro e codbairro porque não estava indo pelos atributos
		// do objeto endereco
		endereco.setBairro(bairro);
		boolean cadastrou = udao.cadastrarBairros(endereco);

		if (cadastrou == true) {
			limparDados();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Bairro cadastrado com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);

			listaMunicipios = null;

		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante o cadastro!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);

		}

	}

	public void alterarMunicipios() throws ProjetoException {

		EnderecoDAO udao = new EnderecoDAO();
		boolean alterou = udao.alterarMunicipio(endereco);
		listaMunicipios = null;
		if (alterou == true) {
			// limparDados();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Município alterado com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			// return
			// "/pages/sishosp/gerenciarMunicipio.faces?faces-redirect=true";
			// RequestContext.getCurrentInstance().execute("dlgAltMenu.hide();");
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante o cadastro!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			// return "";
			// RequestContext.getCurrentInstance().execute("dlgAltMenu.hide();");
		}

	}

	public void alterarBairros() throws ProjetoException {
		EnderecoDAO udao = new EnderecoDAO();
		// foi criado bairro e codbairro porque não estava indo pelos atributos
		// do objeto endereco
		endereco.setBairro(bairro);
		endereco.setCodbairro(codbairro);
		boolean alterou = udao.alterarBairros(endereco);
		listaBairros = null;
		if (alterou == true) {
			// limparDados();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Bairro alterado com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			// return
			// "/pages/sishosp/gerenciarMunicipio.faces?faces-redirect=true";
			// RequestContext.getCurrentInstance().execute("dlgAltMenu.hide();");
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante o cadastro!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			// return "";
			// RequestContext.getCurrentInstance().execute("dlgAltMenu.hide();");
		}

	}

	public void excluirLogradourou() throws ProjetoException {
		EnderecoDAO udao = new EnderecoDAO();
		boolean excluio = udao.excluirLogradouro(endereco);

		if (excluio == true) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Logradouro excluído com sucesso!", "Sucesso");
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

	}

	public void excluirMunicipios() throws ProjetoException {
		EnderecoDAO udao = new EnderecoDAO();

		boolean excluio = udao.excluirMunicipio(endereco);

		if (excluio == true) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Município excluído com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			listarMunicipios();
			RequestContext.getCurrentInstance().execute(
					"PF('dialogAtencao').hide();");
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante a exclusao!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);

			RequestContext.getCurrentInstance().execute(
					"PF('dialogAtencao').hide();");
		}

	}

	public void excluirBairros() throws ProjetoException {
		EnderecoDAO udao = new EnderecoDAO();

		boolean excluiu = udao.excluirMunicipio(endereco);

		if (excluiu == true) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Bairro excluído com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			listarMunicipios();
			RequestContext.getCurrentInstance().execute(
					"PF('dialogAtencao').hide();");
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante a exclusao!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);

			RequestContext.getCurrentInstance().execute(
					"PF('dialogAtencao').hide();");
		}

	}

	public void buscarMunicipios() throws ProjetoException {

		List<EnderecoBean> listaAux = null;
		listaMunicipios = new ArrayList<>();

		EnderecoDAO adao = new EnderecoDAO();

		listaAux = adao.buscarTipoMunicipio(campoBuscaMunicipio,
				tipoBuscaMunicipio);

		if (listaAux != null && listaAux.size() > 0) {
			// listaAss = null;
			listaMunicipios = listaAux;
		} else {
			// listaAss = null;
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Nenhum Municipio encontrado.", "Aviso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

	}

	public void limparBuscaDados() {
		tipoBuscaMunicipio = 1;
		campoBuscaMunicipio = "";
		statusMunicipio = "P";
		listaMunicipios = null;
	}

	public void limparBuscaDadosBairro() {
		tipoBuscaBairro = 1;
		campoBuscaBairro = "";
		statusBairro = "P";
		listaBairros = null;
	}

	public void limparDados() {
		endereco = new EnderecoBean();

	}

	public List<EnderecoBean> listaMunicipioAutoComplete(String query)
			throws ProjetoException {
		EnderecoDAO eDao = new EnderecoDAO();
		List<EnderecoBean> result = eDao.buscaMunicipioAutoComplete(query);
		return result;
	}
	

	public EnderecoBean getEndereco() {
		return endereco;
	}

	public void setEndereco(EnderecoBean endereco) {
		this.endereco = endereco;
	}

	public void listarMunicipios() throws ProjetoException {
		EnderecoDAO fdao = new EnderecoDAO();
		listaMunicipios = fdao.listaMunicipios();

	}

	public List<EnderecoBean> listarMunicipiosCadastro()
			throws ProjetoException {
		EnderecoDAO fdao = new EnderecoDAO();
		listaMunicipios = fdao.listaMunicipios();
		return listaMunicipios;

	}

	public void listarBairros() throws ProjetoException {
		EnderecoDAO fdao = new EnderecoDAO();
		listaBairros = fdao.listaBairros();

	}

	public void setListaMunicipios(List<EnderecoBean> listaMunicipios) {
		this.listaMunicipios = listaMunicipios;
	}

	public List<EnderecoBean> getListaBairros() throws ProjetoException {
		if (listaBairros == null) {

			EnderecoDAO fdao = new EnderecoDAO();
			listaBairros = fdao.listaBairros();

		}
		return listaBairros;
	}

	public void setListaBairros(List<EnderecoBean> listaBairros) {
		this.listaBairros = listaBairros;
	}

	public Integer getTipoBuscaMunicipio() {
		return tipoBuscaMunicipio;
	}

	public void setTipoBuscaMunicipio(Integer tipoBuscaMunicipio) {
		this.tipoBuscaMunicipio = tipoBuscaMunicipio;
	}

	public String getCampoBuscaMunicipio() {
		return campoBuscaMunicipio;
	}

	public void setCampoBuscaMunicipio(String campoBuscaMunicipio) {
		this.campoBuscaMunicipio = campoBuscaMunicipio;
	}

	public String getStatusMunicipio() {
		return statusMunicipio;
	}

	public void setStatusMunicipio(String statusMunicipio) {
		this.statusMunicipio = statusMunicipio;
	}

	public Integer getAbaAtiva() {
		return abaAtiva;
	}

	public void setAbaAtiva(Integer abaAtiva) {
		this.abaAtiva = abaAtiva;
	}

	public String getCabecalho() {
		if (this.tipo == 1) {
			cabecalho = "Inclusão de Município";
		} else if (this.tipo == 2) {
			cabecalho = "Alteração de Município";
		}
		return cabecalho;
	}

	public void setCabecalho(String cabecalho) {
		this.cabecalho = cabecalho;
	}

	public List<EnderecoBean> getListaMunicipios() {
		return listaMunicipios;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public Integer getTipoBuscaBairro() {
		return tipoBuscaBairro;
	}

	public void setTipoBuscaBairro(Integer tipoBuscaBairro) {
		this.tipoBuscaBairro = tipoBuscaBairro;
	}

	public String getCampoBuscaBairro() {
		return campoBuscaBairro;
	}

	public void setCampoBuscaBairro(String campoBuscaBairro) {
		this.campoBuscaBairro = campoBuscaBairro;
	}

	public String getStatusBairro() {
		return statusBairro;
	}

	public void setStatusBairro(String statusBairro) {
		this.statusBairro = statusBairro;
	}

	public String getCabecalhoBairro() {
		if (this.tipo == 1) {
			cabecalhoBairro = "Inclusão de Bairros";
		} else if (this.tipo == 2) {
			cabecalhoBairro = "Alteração de Bairros";
		}
		return cabecalhoBairro;
	}

	public void setCabecalhoBairro(String cabecalhoBairro) {
		this.cabecalhoBairro = cabecalhoBairro;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

}
