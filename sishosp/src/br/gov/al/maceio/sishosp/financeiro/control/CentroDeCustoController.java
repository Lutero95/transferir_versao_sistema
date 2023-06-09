package br.gov.al.maceio.sishosp.financeiro.control;

import java.io.Serializable;
import java.util.ArrayList;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;


import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.financeiro.dao.CentroCustoDAO;
import br.gov.al.maceio.sishosp.financeiro.model.CentroCustoBean;


@ManagedBean
@ViewScoped
public class CentroDeCustoController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private CentroCustoBean bean;
	private CentroCustoBean rowBean;
	private ArrayList<CentroCustoBean> lstCustos;

	public CentroDeCustoController() {

		bean = new CentroCustoBean();
	}

	public void salvar() throws ProjetoException {

		CentroCustoDAO dao = new CentroCustoDAO();
		if (dao.salvarCentro(this.bean)) {
			bean = new CentroCustoBean();
			lstCustos = null;
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Centro de Custo Gravado com Sucesso",
					"Centro de Custo Gravado com Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			JSFUtil.fecharDialog("dlginc");

		}

	}

	public void editar() throws ProjetoException {

		CentroCustoDAO dao = new CentroCustoDAO();
		if (dao.editarCusto(this.rowBean)) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Centro de Custo Alterado com Sucesso",
					"Centro de Custo Alterado com Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			JSFUtil.fecharDialog("dlgedit");

		}

	}

	public void excluir() throws ProjetoException {

		CentroCustoDAO dao = new CentroCustoDAO();
		if (dao.deleteCusto(this.rowBean)) {
			lstCustos = null;
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Centro de Custo Excluído com Sucesso",
					"Centro de Custo Excluído com Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			JSFUtil.fecharDialog(
					"dialogAtencao");

		}

	}

	public ArrayList<CentroCustoBean> listaCustos() throws ProjetoException {
		if (lstCustos==null){
		CentroCustoDAO dao = new CentroCustoDAO();
		this.lstCustos = (ArrayList<CentroCustoBean>) dao.lstCustos();
		}
		return this.lstCustos;

	}

	public void acaoEditar() throws ProjetoException {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (rowBean != null) {
			JSFUtil.abrirDialog("dlgedit");
		} else
			facesContext.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_INFO, "Selecione um Estado antes.",
					"Alerta"));
	}

	public void acaoExcluir() throws ProjetoException {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (rowBean != null) {
			JSFUtil.abrirDialog(
					"dialogAtencao");
		} else
			facesContext.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_INFO, "Selecione um Estado antes.",
					"Alerta"));
	}
	
	public void onRowSelect(CentroCustoBean cust) {
		if(cust != null) {
			rowBean = cust;
		} else {
			rowBean = null;
		}
		JSFUtil.atualizarComponente("frm:outBotoes");
	}

	public CentroCustoBean getBean() {
		return bean;
	}

	public void setBean(CentroCustoBean bean) {
		this.bean = bean;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public CentroCustoBean getRowBean() {
		return rowBean;
	}

	public void setRowBean(CentroCustoBean rowBean) {
		this.rowBean = rowBean;
	}

	public ArrayList<CentroCustoBean> getLstCustos() {
		return lstCustos;
	}

	public void setLstCustos(ArrayList<CentroCustoBean> lstCustos) {
		this.lstCustos = lstCustos;
	}

}
