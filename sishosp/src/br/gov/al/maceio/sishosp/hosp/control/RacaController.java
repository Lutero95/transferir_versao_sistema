package br.gov.al.maceio.sishosp.hosp.control;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.RedirecionarUtil;
import br.gov.al.maceio.sishosp.hosp.dao.*;
import br.gov.al.maceio.sishosp.hosp.model.*;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ManagedBean(name = "RacaController")
@ViewScoped
public class RacaController implements Serializable {

	private static final long serialVersionUID = 1L;
	private String cabecalho;
	private RacaBean raca;
	private int tipo;
	private List<RacaBean> listaRaca;
	private RacaDAO rDao = new RacaDAO();

	//CONSTANTES
	private static final String ENDERECO_CADASTRO = "cadastroRaca?faces-redirect=true";
	private static final String ENDERECO_TIPO = "&amp;tipo=";
	private static final String ENDERECO_ID = "&amp;id=";
	private static final String CABECALHO_INCLUSAO = "Inclusão de Raça";
	private static final String CABECALHO_ALTERACAO = "Alteração de Raça";

	public RacaController() {
		raca = new RacaBean();
		listaRaca = new ArrayList<>();
		listaRaca = null;
		this.cabecalho = "";
	}

	public void limparDados() {
		this.raca = new RacaBean();
		this.listaRaca = new ArrayList<>();
	}

	public String redirectEdit() {
		return RedirecionarUtil.redirectEdit(ENDERECO_CADASTRO, ENDERECO_ID, this.raca.getCodRaca(), ENDERECO_TIPO, tipo);
	}

	public String redirectInsert() {
		return RedirecionarUtil.redirectInsert(ENDERECO_CADASTRO, ENDERECO_TIPO, tipo);
	}


	public void getEditRaca() throws ProjetoException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Map<String, String> params = facesContext.getExternalContext()
				.getRequestParameterMap();
		if (params.get("id") != null) {
			Integer id = Integer.parseInt(params.get("id"));
			tipo = Integer.parseInt(params.get("tipo"));
			this.raca = rDao.listarRacaPorID(id);
		} else {
			tipo = Integer.parseInt(params.get("tipo"));

		}

	}

	public void gravarRaca() throws ProjetoException {
		boolean cadastrou = rDao.cadastrar(raca);

		if (cadastrou == true) {
			limparDados();
			JSFUtil.adicionarMensagemSucesso("Raça cadastrada com sucesso!", "Sucesso");

		} else {
			JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cadastro!", "Erro");
		}

	}

	public void alterarRaca() throws ProjetoException {

		boolean alterou = rDao.alterar(raca);

		if (alterou == true) {
			JSFUtil.adicionarMensagemSucesso("Raça alterada com sucesso!", "Sucesso");
		} else {
			JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a alteração!", "Erro");
		}

	}

	public void excluirRaca() throws ProjetoException {

		boolean excluiu = rDao.excluir(raca);

		if (excluiu == true) {
			JSFUtil.adicionarMensagemSucesso("Raça excluído com sucesso!", "Sucesso");
			JSFUtil.fecharDialog("dialogExclusao");
		} else {
			JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a exclusão!", "Erro");
			JSFUtil.fecharDialog("dialogExclusao");
		}

	}

	public void listarRaca() throws ProjetoException {
		listaRaca = rDao.listaCor();
	}

	public List<RacaBean> listaRacas() throws ProjetoException {
			listaRaca = rDao.listaCor();

		return listaRaca;
	}

	public RacaBean getRaca() {
		return raca;
	}

	public void setRaca(RacaBean raca) {
		this.raca = raca;
	}


	public String getCabecalho() {
		if (this.tipo == 1) {
			cabecalho = CABECALHO_INCLUSAO;
		} else if (this.tipo == 2) {
			cabecalho = CABECALHO_ALTERACAO;
		}
		return cabecalho;
	}

	public void setCabecalho(String cabecalho) {
		this.cabecalho = cabecalho;
	}


	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

}
