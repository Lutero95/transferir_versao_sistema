package br.gov.al.maceio.sishosp.financeiro.control;

import java.io.Serializable;
import java.util.ArrayList;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.financeiro.dao.TipoDocumentoDao;
import br.gov.al.maceio.sishosp.financeiro.model.TipoDocumentoBean;


@ManagedBean
@ViewScoped
public class TipoDocumentoController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private TipoDocumentoBean tipoDoc;
	private TipoDocumentoBean doc;
	private TipoDocumentoBean rowBean;

	private ArrayList<TipoDocumentoBean> lstTipo;

	public TipoDocumentoController() {

		tipoDoc = new TipoDocumentoBean();
		doc = new TipoDocumentoBean();
		rowBean = null;
	//	lstTipo = new ArrayList<>();
	}
	
	public void limpaobj() {
		tipoDoc = new TipoDocumentoBean();
	}

	public void salvar() throws ProjetoException {
		TipoDocumentoDao dao = new TipoDocumentoDao();
		if (dao.salvarDocumento(tipoDoc)) {
			JSFUtil.fecharDialog("dlginc");
		lstTipo = null;
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Salvo com sucesso!", "teste"));
		}
	}

	public void editar() throws ProjetoException {

		TipoDocumentoDao dao = new TipoDocumentoDao();
		if (dao.editarTipoDocumento(rowBean)) {
		rowBean = null;
		lstTipo = null;
			JSFUtil.fecharDialog("dlginc");
			JSFUtil.atualizarComponente("frm:outBotoes");

		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Editado com sucesso!", "teste"));
		}

	}
	
	public void acaoExcluir() {
		if (rowBean != null) {
			JSFUtil.abrirDialog("dialogAtencao");

		} else {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							"SELECIONE UM TIPO DE DOCUMENTO ANTES!", "teste"));
		}

	}

	public void excluir() throws ProjetoException {

		TipoDocumentoDao dao = new TipoDocumentoDao();
		dao.excluirTipoDocumento(rowBean);
		rowBean = null;
		lstTipo = null;
		JSFUtil.abrirDialog("dialogAtencao");
		JSFUtil.atualizarComponente("frm:outBotoes");

		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Excluído com sucesso!", "teste"));

	}

	public ArrayList<TipoDocumentoBean> lstTipoDocs() throws ProjetoException {
		if (lstTipo==null) {
		TipoDocumentoDao dao = new TipoDocumentoDao();
		lstTipo=  (ArrayList<TipoDocumentoBean>) dao.lstTipoDocumento();
		}
		return lstTipo;

	}
	
	public void acaoEditar() throws ProjetoException {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (rowBean != null) {
			JSFUtil.abrirDialog("dlgedit");
		} else
			facesContext.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_INFO, "Selecione um Tipo de Documento antes.",
					"Alerta"));
	}
	
	public void onRowSelect(TipoDocumentoBean tipoDoc) {
		if(tipoDoc != null) {
			rowBean = tipoDoc;
		} else {
			rowBean = null;
		}
		JSFUtil.atualizarComponente("frm:outBotoes");
	}

	public TipoDocumentoBean getTipoDoc() {
		return tipoDoc;
	}

	public void setTipoDoc(TipoDocumentoBean tipoDoc) {
		this.tipoDoc = tipoDoc;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public TipoDocumentoBean getRowBean() {
		return rowBean;
	}

	public void setRowBean(TipoDocumentoBean rowBean) {
		this.rowBean = rowBean;
	}

	public ArrayList<TipoDocumentoBean> getLstTipo() {
		return lstTipo;
	}

	public void setLstTipo(ArrayList<TipoDocumentoBean> lstTipo) {
		this.lstTipo = lstTipo;
	}

	public TipoDocumentoBean getDoc() {
		return doc;
	}

	public void setDoc(TipoDocumentoBean doc) {
		this.doc = doc;
	}

}

