package br.gov.al.maceio.sishosp.comum.util;

import org.primefaces.context.RequestContext;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public final class JSFUtil {
	
	public static void adicionarMensagemSucesso(String mensagem, String informativo){
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, mensagem, informativo);
		FacesContext contexto = FacesContext.getCurrentInstance();
		contexto.addMessage(null, msg);
	}
	
	public static void adicionarMensagemErro(String mensagem, String informativo){
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, mensagem, informativo);
		FacesContext contexto = FacesContext.getCurrentInstance();
		contexto.addMessage(null, msg);
	}
	
	public static void adicionarMensagemAdvertencia(String mensagem, String informativo){
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, mensagem, informativo);
		FacesContext contexto = FacesContext.getCurrentInstance();
		contexto.addMessage(null, msg);
	}

	public static RequestContext getRequestContext() {
		return RequestContext.getCurrentInstance();
	}

	public static void abrirDialog(String dialog) {
		getRequestContext().execute("PF('" + dialog + "').show();");
	}

	public static void fecharDialog(String dialog) {
		getRequestContext().execute("PF('" + dialog + "').hide();");
	}

	public static void atualizarComponente(String componente){
		RequestContext.getCurrentInstance().update(componente);
	}

	public static void selecionarTabEspecifica(String tab, String numero){
		getRequestContext().execute("PF('" + tab + "').select(" + numero + ");");
	}

}
