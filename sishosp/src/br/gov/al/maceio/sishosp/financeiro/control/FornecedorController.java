package br.gov.al.maceio.sishosp.financeiro.control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.financeiro.dao.BuscaDAO;
import br.gov.al.maceio.sishosp.financeiro.dao.FornecedorDAO;
import br.gov.al.maceio.sishosp.financeiro.model.BuscaBeanFornecedor;
import br.gov.al.maceio.sishosp.financeiro.model.FornecedorBean;


@ManagedBean (name = "fornecedorControllerFin")
@ViewScoped
public class FornecedorController implements Serializable {

	private FornecedorBean func;
	private FornecedorBean rowBean;
	private List<FornecedorBean> listafornecedores;
	private ArrayList<FornecedorBean> lstFornecedorALL;
	private BuscaBeanFornecedor busca;

	/* construtor */
	public FornecedorController() {

		func = new FornecedorBean();
		rowBean = null;
		func.setTipoPessoa("F");
		listafornecedores = new ArrayList<>();
		lstFornecedorALL = null;
		busca = new BuscaBeanFornecedor();
		busca.setOrdenacao("N");
		busca.setTipoBusca("N");

	}

	public void busca() {
		lstFornecedorALL = null;
	}

	public void limpabusca() {
		busca = new BuscaBeanFornecedor();
		busca.setOrdenacao("N");
		busca.setTipoBusca("N");
		lstFornecedorALL = null;
	}

	public void salvar() throws ProjetoException {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		FornecedorDAO dao = new FornecedorDAO();
		dao.salvarFornecedor(this.func);
		JSFUtil.fecharDialog("dlginc");
		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Salvo com Sucesso!", "Alerta"));

		this.func = new FornecedorBean();
		lstFornecedorALL = null;
	}

	public void editar() throws ProjetoException {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		FornecedorDAO dao = new FornecedorDAO();
		if (dao.editarFornecedor(this.rowBean)) {
			rowBean = null;
			JSFUtil.atualizarComponente("frm:outBotoes");
			JSFUtil.fecharDialog("dlgedit");
			facesContext.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Alterado com sucesso", "Alerta"));

		} else {
			facesContext.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falha ao Alterar!", ""));
		}
	}

	public void excluir() throws ProjetoException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		FornecedorDAO dao = new FornecedorDAO();
		boolean apagou = dao.deleteFornecedor(this.rowBean);

		if (apagou) {
			facesContext.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Excluido com sucesso", "Alerta"));
		} else {
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falha ao Excluir", ""));
		}
		rowBean = null;
		lstFornecedorALL = null;
		JSFUtil.atualizarComponente("frm:outBotoes");
		JSFUtil.fecharDialog("dialogAtencao");
	}

	public void acaoEditar() throws ProjetoException {

		FacesContext facesContext = FacesContext.getCurrentInstance();

		if (rowBean != null) {
			JSFUtil.abrirDialog("dlgedit");
		} else
			facesContext.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Selecione um Fornecedor antes", "Alerta"));
	}

	public void acaoExcluir() throws ProjetoException {
		FacesContext facesContext = FacesContext.getCurrentInstance();

		if (rowBean != null) {
			JSFUtil.abrirDialog("dialogAtencao");
		} else
			facesContext.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Selecione um Fornecedor antes", "Alerta"));
	}

	public ArrayList<FornecedorBean> listAll() throws ProjetoException {

		if (lstFornecedorALL == null) {
			FornecedorDAO dao = new FornecedorDAO();
			this.lstFornecedorALL = (ArrayList<FornecedorBean>) dao.lstFornecedor(busca);
		}
		return this.lstFornecedorALL;
	}

	public void onRowSelect(FornecedorBean event) {
		rowBean = event;
		JSFUtil.atualizarComponente("frm:outBotoes");
	}

	public void buscarFornecedores() throws ProjetoException {
		BuscaDAO funcDao = new BuscaDAO();
		listafornecedores = funcDao.buscaForn(func.getNome().toUpperCase(), null);

	}

	// Metodo que lista todas as empresas
	public List getListarForncedores() throws Exception {

		if (listafornecedores == null) {
			BuscaDAO icdao = new BuscaDAO();
			listafornecedores = icdao.buscaFornecedor();
		}
		return listafornecedores;
	}

	public void limpaListaFornecedores() {
		listafornecedores = new ArrayList<>();
	}

	public void limpaobj() {
		func = new FornecedorBean();
	}

	public FornecedorBean getFunc() {
		return func;
	}

	public List<FornecedorBean> getListafornecedores() {
		return listafornecedores;
	}

	public void setFunc(FornecedorBean func) {
		this.func = func;
	}

	public void setListafornecedores(List<FornecedorBean> listafornecedores) {
		this.listafornecedores = listafornecedores;
	}

	public FornecedorBean getRowBean() {
		return rowBean;
	}

	public void setRowBean(FornecedorBean rowBean) {
		this.rowBean = rowBean;
	}

	public ArrayList<FornecedorBean> getLstFornecedorALL() {
		return lstFornecedorALL;
	}

	public void setLstFornecedorALL(ArrayList<FornecedorBean> lstFornecedorALL) {
		this.lstFornecedorALL = lstFornecedorALL;
	}

	public BuscaBeanFornecedor getBusca() {
		return busca;
	}

	public void setBusca(BuscaBeanFornecedor busca) {
		this.busca = busca;
	}

}
