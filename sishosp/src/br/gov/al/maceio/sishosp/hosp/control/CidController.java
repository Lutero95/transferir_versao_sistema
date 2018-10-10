package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.CidDAO;
import br.gov.al.maceio.sishosp.hosp.model.CidBean;

@ManagedBean(name = "CidController")
@ViewScoped
public class CidController implements Serializable {

	private static final long serialVersionUID = 1L;
	private CidBean cid;
	private List<CidBean> listaCids;
	private Integer tipoBuscar;
	private String descricaoBusca;
	private int tipo;
	private String cabecalho;
	private Integer abaAtiva = 0;
	private CidDAO gDao = new CidDAO();

	//CONSTANTES
	private static final String ENDERECO_CADASTRO = "cadastroCid?faces-redirect=true";
	private static final String ENDERECO_TIPO = "&amp;tipo=";
	private static final String ENDERECO_ID = "&amp;id=";
	private static final String CABECALHO_INCLUSAO = "Inclusão de CID";
    private static final String CABECALHO_ALTERACAO = "Alteração de CID";

	public CidController() {
		this.cid = new CidBean();
		this.listaCids = new ArrayList<>();
		this.listaCids = null;
		this.descricaoBusca = new String();
		this.cabecalho = "";
	}

	public void limparDados() throws ProjetoException {
		this.cid = new CidBean();
		this.listaCids = new ArrayList<>();
		this.descricaoBusca = new String();
		listaCids = gDao.listarCid();
	}

	public String redirectEdit() {
		return ENDERECO_CADASTRO+ENDERECO_ID+this.cid.getIdCid()+ENDERECO_TIPO+tipo;
	}

	public String redirectInsert() {
	    return ENDERECO_CADASTRO+ENDERECO_TIPO+tipo;
	}

	public void getEditCid() throws ProjetoException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Map<String, String> params = facesContext.getExternalContext()
				.getRequestParameterMap();
		if (params.get("id") != null) {
			Integer id = Integer.parseInt(params.get("id"));
			tipo = Integer.parseInt(params.get("tipo"));

			this.cid = gDao.buscaCidPorId(id);
		} else {

			tipo = Integer.parseInt(params.get("tipo"));

		}

	}

	public void gravarCid() throws ProjetoException, SQLException {

		boolean cadastrou = gDao.gravarCid(cid);

		if (cadastrou == true) {
			limparDados();
			JSFUtil.adicionarMensagemSucesso("CID cadastrado com sucesso!", "Sucesso");
		} else {
			JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cadastro!", "Erro");
		}
	}

	public void alterarCid() throws ProjetoException {
		boolean alterou = gDao.alterarCid(cid);
		if (alterou == true) {
            JSFUtil.adicionarMensagemSucesso("CID alterado com sucesso!", "Sucesso");
		} else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a alteração!", "Erro");
		}
		listaCids = gDao.listarCid();

	}

	public void excluirCid() throws ProjetoException {
		boolean ok = gDao.excluirCid(cid);
		if (ok == true) {
            JSFUtil.adicionarMensagemSucesso("CID excluído com sucesso!", "Sucesso");
			JSFUtil.fecharDialog("dialogExclusao");
		} else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a exclusão!", "Erro");
            JSFUtil.fecharDialog("dialogExclusao");
		}
		listaCids = gDao.listarCid();
	}

	public List<CidBean> listaCidAutoComplete(String query)
			throws ProjetoException {
		List<CidBean> result = gDao.listarCidsBusca(query);
		return result;
	}

	public void buscarCid() throws ProjetoException {
		listaCids = gDao.listarCid();
	}

	public List<CidBean> listarCids() throws ProjetoException {
        if (listaCids == null) {
            listaCids = gDao.listarCid();
        }
        return listaCids;
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

	public CidBean getCid() {
		return cid;
	}

	public void setCid(CidBean cid) {
		this.cid = cid;
	}

    public List<CidBean> getListaCids() {
        return listaCids;
    }

    public void setListaCids(List<CidBean> listaCids) {
        this.listaCids = listaCids;
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

	public CidDAO getgDao() {
		return gDao;
	}

	public void setgDao(CidDAO gDao) {
		this.gDao = gDao;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

}
