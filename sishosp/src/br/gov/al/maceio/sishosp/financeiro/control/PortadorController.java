package br.gov.al.maceio.sishosp.financeiro.control;

import java.io.Serializable;
import java.util.ArrayList;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.financeiro.model.PortadorBean;


import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.financeiro.dao.PortadorDAO;

@ManagedBean
@ViewScoped
public class PortadorController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private PortadorBean port;
	private PortadorBean rowBean;

	
	private ArrayList<PortadorBean> lstportador;
	private String operacao;

	/* construtor */
	public PortadorController() throws ProjetoException {

		operacao = "I";
		
		port = new PortadorBean();
		lstportador = new ArrayList<PortadorBean>();
		lstportador = null;

		lstportador();

	}

	public void limparObjs() {

		lstportador = null;

	}

	public void acaoEditar() {
		if (port != null) {
			JSFUtil.abrirDialog("dlgedit");


			this.operacao = "E";
		} else {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							"SELECIONE UM PORTADOR ANTES!", "teste"));
		}

	}

	public void acaoExcluir() {
		if (port != null) {
			JSFUtil.abrirDialog("dialogAtencao");

		} else {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							"SELECIONE UM PORTADOR ANTES!", "teste"));
		}

	}

	public void valorInsert() {

		this.port = new PortadorBean();
		this.operacao = "I";
	}

	public void gravaInclusao() throws ProjetoException {

		

			PortadorDAO dao = new PortadorDAO();

			String retorno = "";
			retorno = dao.inserePort(this.port);
			
			if (retorno != null) {
				if (retorno.equals("OK")) {
					port = new PortadorBean();
					FacesContext.getCurrentInstance().addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_INFO,
									"CADASTRADO COM SUCESSO !", "teste"));

					JSFUtil.fecharDialog("dlginc");

					lstportador = null;

				}

				else
					FacesContext.getCurrentInstance().addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_INFO,
									"Falha na tentativa de Gravação: "
											+ retorno, "teste"));
			} else {

				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_INFO,
								"Falha na tentativa de Gravação: "
										+ retorno, "teste"));
			}
	}
	
	public void gravaAlteracao() throws ProjetoException {


			PortadorDAO dao = new PortadorDAO();


				String retorno = "";
				retorno = dao.atualizaPort(this.rowBean);
				
				if (retorno != null) {
					if (retorno.equals("OK")) {
						port = new PortadorBean();
						rowBean = null;
						FacesContext.getCurrentInstance().addMessage(
								null,
								new FacesMessage(FacesMessage.SEVERITY_INFO,
										"ALTERADO COM SUCESSO !", "teste"));
						JSFUtil.fecharDialog("dlgedit");

						lstportador = null;

					}

					else
						FacesContext.getCurrentInstance().addMessage(
								null,
								new FacesMessage(FacesMessage.SEVERITY_INFO,
										"Falha na tentativa de Gravação: "
												+ retorno, "teste"));
				} else {

					FacesContext.getCurrentInstance().addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_INFO,
									"Falha na tentativa de Gravação: "
											+ retorno, "teste"));
				}
			
		
	

	}


	// metodo para excluir PORTADOR.
	public void excluirPortador() throws ProjetoException {

		PortadorDAO dao = new PortadorDAO();
		String retorno = dao.excluir(this.rowBean);
		if ((retorno.equals("OK"))) {
			JSFUtil.fecharDialog("dialogAtencao");

			rowBean = null;
			lstportador = null;
			lstportador();
			JSFUtil.atualizarComponente("formUsuarios:outBotoes");

		} else {

			// controlMsg.erro();

		}

	}

	/* metodo de listar usuarios de tal empresa */
	public ArrayList<PortadorBean> lstportador() throws ProjetoException {
		PortadorDAO dao = new PortadorDAO();

		if (lstportador == null) {
			lstportador = dao.buscaTodosPort();

		}

		return lstportador;
	}
	
	public void onRowSelect(PortadorBean event) {
		rowBean = event;

		JSFUtil.atualizarComponente("frm:outBotoes");
	}

	/* Gets e Sets */

	public PortadorBean getPort() {
		return port;
	}

	public ArrayList<PortadorBean> getLstportador() {
		return lstportador;
	}

	public String getOperacao() {
		return operacao;
	}

	public void setPort(PortadorBean port) {
		this.port = port;
	}

	public void setLstportador(ArrayList<PortadorBean> lstportador) {
		this.lstportador = lstportador;
	}

	public void setOperacao(String operacao) {
		this.operacao = operacao;
	}
	
	public PortadorBean getRowBean() {
		return rowBean;
	}

	public void setRowBean(PortadorBean rowBean) {
		this.rowBean = rowBean;
	}


}
