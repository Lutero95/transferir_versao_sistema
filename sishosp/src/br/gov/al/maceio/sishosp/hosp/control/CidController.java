package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.comum.enums.TipoCabecalho;
import br.gov.al.maceio.sishosp.comum.util.RedirecionarUtil;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.CidDAO;
import br.gov.al.maceio.sishosp.hosp.model.CidBean;

@ManagedBean(name = "CidController")
@ViewScoped
public class CidController implements Serializable {

	private static final long serialVersionUID = 1L;
	private CidBean cid;
	private Integer tipo;
	private String cabecalho;
	private CidDAO cDao = new CidDAO();
	private List<CidBean> listaCid;

	//CONSTANTES
	private static final String ENDERECO_CADASTRO = "cadastroCid?faces-redirect=true";
	private static final String ENDERECO_TIPO = "&amp;tipo=";
	private static final String ENDERECO_ID = "&amp;id=";
	private static final String CABECALHO_INCLUSAO = "Inclusão de CID";
    private static final String CABECALHO_ALTERACAO = "Alteração de CID";

	public CidController() {
		this.cid = new CidBean();
		this.cabecalho = "";
		listaCid = new ArrayList<>();

	}

	public void limparDados() {
		this.cid = new CidBean();
	}

	public String redirectEdit() {
		return RedirecionarUtil.redirectEdit(ENDERECO_CADASTRO, ENDERECO_ID, this.cid.getIdCid(), ENDERECO_TIPO, tipo);
	}

	public String redirectInsert() {
	    return RedirecionarUtil.redirectInsert(ENDERECO_CADASTRO, ENDERECO_TIPO, tipo);
	}

	public void getEditCid() throws ProjetoException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Map<String, String> params = facesContext.getExternalContext()
				.getRequestParameterMap();
		if (params.get("id") != null) {
			Integer id = Integer.parseInt(params.get("id"));
			tipo = Integer.parseInt(params.get("tipo"));

			this.cid = cDao.buscaCidPorId(id);
		} else {

			tipo = Integer.parseInt(params.get("tipo"));

		}

	}

	public void gravarCid() throws Exception {

		boolean cadastrou = cDao.gravarCid(cid);

		if (cadastrou == true) {
			limparDados();
			JSFUtil.adicionarMensagemSucesso("CID cadastrado com sucesso!", "Sucesso");
		} else {
			JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cadastro!", "Erro");
		}
	}

	public void alterarCid() {
		boolean alterou = cDao.alterarCid(cid);

		if (alterou == true) {
            JSFUtil.adicionarMensagemSucesso("CID alterado com sucesso!", "Sucesso");
		} else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a alteração!", "Erro");
		}
	}

	public void excluirCid() throws ProjetoException {
		boolean ok = cDao.excluirCid(cid);

		if (ok == true) {
            JSFUtil.adicionarMensagemSucesso("CID excluído com sucesso!", "Sucesso");
			JSFUtil.fecharDialog("dialogExclusao");
			listarCids();
		} else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a exclusão!", "Erro");
            JSFUtil.fecharDialog("dialogExclusao");
		}
	}

	public List<CidBean> listaCidAutoComplete(String query)
			throws ProjetoException {
		List<CidBean> result = cDao.listarCidsBusca(query);
		return result;
	}

	public void listarCids() throws ProjetoException {
		listaCid = cDao.listarCid();
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

	public CidBean getCid() {
		return cid;
	}

	public void setCid(CidBean cid) {
		this.cid = cid;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public List<CidBean> getListaCid() {
		return listaCid;
	}

	public void setListaCid(List<CidBean> listaCid) {
		this.listaCid = listaCid;
	}
}
