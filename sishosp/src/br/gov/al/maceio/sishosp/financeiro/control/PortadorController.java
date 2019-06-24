package br.gov.al.maceio.sishosp.financeiro.control;

import java.io.Serializable;
import java.util.ArrayList;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.financeiro.model.PortadorBean;

import org.primefaces.context.RequestContext;

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

	private ArrayList<PortadorBean> lstportador;
	private String operacao;

	/* construtor */
	public PortadorController() throws ProjetoException {

		operacao = "I";
		
		port = null;
		lstportador = new ArrayList<PortadorBean>();
		lstportador = null;

		lstportador();

	}

	public void limparObjs() {

		lstportador = null;

	}

	public void acaoEditar() {
		if (port != null) {
			RequestContext.getCurrentInstance().execute(
					"PF('novaProfissao').show();");
			
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
			RequestContext.getCurrentInstance().execute(
					"PF('dialogAtencao').show();");

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

	public void gravar() throws ProjetoException {

		{

			PortadorDAO dao = new PortadorDAO();

			if (this.operacao.equals("E")) {

				String retorno = "";
				retorno = dao.atualizaPort(this.port);
				
				if (retorno != null) {
					if (retorno.equals("OK")) {
						port = null;
						RequestContext.getCurrentInstance().update("formUsuarios:outBotoes");
						FacesContext.getCurrentInstance().addMessage(
								null,
								new FacesMessage(FacesMessage.SEVERITY_INFO,
										"ALTERADO COM SUCESSO !", "teste"));

						RequestContext.getCurrentInstance().execute(
								"PF('novaProfissao').hide();");
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
			} else {

				String retorno = "";
				retorno = dao.inserePort(this.port);
				
				if (retorno != null) {
					if (retorno.equals("OK")) {
						port = new PortadorBean();
						FacesContext.getCurrentInstance().addMessage(
								null,
								new FacesMessage(FacesMessage.SEVERITY_INFO,
										"CADASTRADO COM SUCESSO !", "teste"));

						RequestContext.getCurrentInstance().execute(
								"PF('novaProfissao').hide();");
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
		}
	}

	// metodo para excluir PORTADOR.
	public void excluirPortador() throws ProjetoException {

		PortadorDAO dao = new PortadorDAO();
		String retorno = dao.excluir(this.port);
		if ((retorno.equals("OK"))) {

			RequestContext.getCurrentInstance().execute(
					"PF('dialogAtencao').hide();");
			port = null;
			lstportador = null;
			lstportador();
			RequestContext.getCurrentInstance().update("formUsuarios:outBotoes");

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
		port = event;
		RequestContext.getCurrentInstance().update("formUsuarios:outBotoes");
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

}
