package br.gov.al.maceio.sishosp.hosp.control;

import br.gov.al.maceio.sishosp.comum.enums.TipoCabecalho;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.RedirecionarUtil;
import br.gov.al.maceio.sishosp.hosp.dao.TipoLogradouroDAO;
import br.gov.al.maceio.sishosp.hosp.model.RacaBean;
import br.gov.al.maceio.sishosp.hosp.model.TipoLogradouroBean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ManagedBean(name = "TipoLogradouroController")
@ViewScoped
public class TipoLogradouroController implements Serializable {

	private static final long serialVersionUID = 1L;
	private TipoLogradouroBean tipoLogradouro;
	private Integer tipo;
	private String cabecalho;
	private TipoLogradouroDAO cDao = new TipoLogradouroDAO();
	private List<TipoLogradouroBean> listaTipoLogradouro;
	private String tipoBusca;
	private String campoBusca;

	//CONSTANTES
	private static final String ENDERECO_CADASTRO = "cadastrotipologradouro?faces-redirect=true";
	private static final String ENDERECO_TIPO = "&amp;tipo=";
	private static final String ENDERECO_ID = "&amp;id=";
	private static final String CABECALHO_INCLUSAO = "Inclusão de Tipo de Logradouro";
    private static final String CABECALHO_ALTERACAO = "Alteração de Tipo de Logradouro";

	public TipoLogradouroController() {
		this.tipoLogradouro = new TipoLogradouroBean();
		this.cabecalho = "";
		listaTipoLogradouro = new ArrayList<>();

	}

	public void limparDados() {
		this.tipoLogradouro = new TipoLogradouroBean();
	}

	public String redirectEdit() {
		return RedirecionarUtil.redirectEdit(ENDERECO_CADASTRO, ENDERECO_ID, this.tipoLogradouro.getId(), ENDERECO_TIPO, tipo);
	}

	public String redirectInsert() {
	    return RedirecionarUtil.redirectInsert(ENDERECO_CADASTRO, ENDERECO_TIPO, tipo);
	}

	public void getEditTipoLogradouro() throws ProjetoException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Map<String, String> params = facesContext.getExternalContext()
				.getRequestParameterMap();
		if (params.get("id") != null) {
			Integer id = Integer.parseInt(params.get("id"));
			tipo = Integer.parseInt(params.get("tipo"));

			this.tipoLogradouro = cDao.buscaTipoLogradouroPorId(id);
		} else {

			tipo = Integer.parseInt(params.get("tipo"));

		}

	}

	public void gravarTipoLogradouro() throws Exception {

		boolean cadastrou = cDao.gravarTipoLogradouro(tipoLogradouro);

		if (cadastrou == true) {
			limparDados();
			JSFUtil.adicionarMensagemSucesso("Tipo de Logradouro cadastrado com sucesso!", "Sucesso");
		} 
	}



	public void limparCampoBusca(){
		campoBusca = "";
	}

	public void alterarTipoLogradouro() throws ProjetoException {
		boolean alterou = cDao.alterarTipoLogradouro(tipoLogradouro);

		if (alterou == true) {
            JSFUtil.adicionarMensagemSucesso("Tipo de Logradouro alterado com sucesso!", "Sucesso");
		} else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a alteração!", "Erro");
		}
	}

	public void excluirTipoLogradouro() throws ProjetoException {
		boolean ok = cDao.excluirTipoLogradouro(tipoLogradouro);

		if (ok == true) {
            JSFUtil.adicionarMensagemSucesso("Tipo de Logradouro excluído com sucesso!", "Sucesso");
			JSFUtil.fecharDialog("dialogExclusao");
			listarTipoLogradouro();
		} else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a exclusão!", "Erro");
            JSFUtil.fecharDialog("dialogExclusao");
		}
	}

	public void buscarTipoLogradouro() throws ProjetoException {
		listaTipoLogradouro = cDao.buscarTipoLogradouro(campoBusca, tipoBusca);
	}

	public void listarTipoLogradouro() throws ProjetoException {
		listaTipoLogradouro = cDao.listarTipoLogradouro();
    }

	public List<TipoLogradouroBean> listarTiposLogradouro() throws ProjetoException {
		return listaTipoLogradouro = cDao.listarTipoLogradouro();
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

	public String getCampoBusca() {
		return campoBusca;
	}

	public void setCampoBusca(String campoBusca) {
		this.campoBusca = campoBusca;
	}

	public String getTipoBusca() {
		return tipoBusca;
	}

	public void setTipoBusca(String tipoBusca) {
		this.tipoBusca = tipoBusca;
	}

	public TipoLogradouroBean getTipoLogradouro() {
		return tipoLogradouro;
	}

	public void setTipoLogradouro(TipoLogradouroBean tipoLogradouro) {
		this.tipoLogradouro = tipoLogradouro;
	}

	public List<TipoLogradouroBean> getListaTipoLogradouro() {
		return listaTipoLogradouro;
	}

	public void setListaTipoLogradouro(List<TipoLogradouroBean> listaTipoLogradouro) {
		this.listaTipoLogradouro = listaTipoLogradouro;
	}

	public Integer getTipo() {
		return tipo;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}
}
