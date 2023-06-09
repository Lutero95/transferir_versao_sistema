package br.gov.al.maceio.sishosp.comum.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.PrimeFaces;


public final class JSFUtil {

	public static void adicionarMensagemSucesso(String mensagem, String informativo) {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, mensagem, informativo);
		FacesContext contexto = FacesContext.getCurrentInstance();
		contexto.addMessage(null, msg);
	}

	public static void adicionarMensagemErro(String mensagem, String informativo) {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, mensagem, informativo);
		FacesContext contexto = FacesContext.getCurrentInstance();
		contexto.addMessage(null, msg);
	}

	public static void adicionarMensagemAdvertencia(String mensagem, String informativo) {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, mensagem, informativo);
		FacesContext contexto = FacesContext.getCurrentInstance();
		contexto.addMessage(null, msg);
	}

	public static Object resgatarObjetoDaSessao(String nomeDoObjeto) {
		return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(nomeDoObjeto);
	}

	public static void abrirDialog(String dialog) {
		//RequestContext.getCurrentInstance().execute("PF('" + dialog + "').show();");
		PrimeFaces.current().executeScript("PF('" + dialog + "').show();");
	}

	public static void fecharDialog(String dialog) {
		//RequestContext.getCurrentInstance().execute("PF('" + dialog + "').hide();");
		PrimeFaces.current().executeScript("PF('" + dialog + "').hide();");
	}

	public static void atualizarComponente(String componente) {
		PrimeFaces.current().ajax().update(componente);
	}

	public static void selecionarTabEspecifica(String tab, String numero) {
		//RequestContext.getCurrentInstance().execute("PF('" + tab + "').select(" + numero + ");");
		PrimeFaces.current().executeScript("PF('" + tab + "').select(" + numero + ");");
	}

	public static int geraNumeroRandomico() throws NoSuchAlgorithmException {
		 byte[] randomBytes = new byte[128];
		 SecureRandom secureRandomGenerator = SecureRandom.getInstance("SHA1PRNG");
		 secureRandomGenerator.nextBytes(randomBytes);
		 return secureRandomGenerator.nextInt();
	}

}
