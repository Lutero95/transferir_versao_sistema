package br.gov.al.maceio.sishosp.comum.util;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.acl.model.Permissoes;
import br.gov.al.maceio.sishosp.acl.model.Sistema;

import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpSession;



public class AuthorizationListener implements PhaseListener {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	@Override
	public void afterPhase(PhaseEvent event) {

		// Adiquirindo o FacesContext.
		FacesContext facesContext = event.getFacesContext();

		// Página Atual.
		String currentPage = facesContext.getViewRoot().getViewId();

		// Sessão do Usuário
		HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);

		String pageLogin = "/pages/comum/login.xhtml";
		String pagePrincipal = "/pages/comum/selecaoSistema2.xhtml";

		// Verifica se está na página de login.
		boolean isLoginPage = currentPage.endsWith(pageLogin);
		boolean isPrincipal = currentPage.endsWith(pagePrincipal);

		FuncionarioBean usuarioBean = null;
		Sistema sistemaLogado = null;
		List<Permissoes> permsUsuarioLogado = null;

		// Obtendo o Usuário, Sistema Logado e Permissões
		usuarioBean = (FuncionarioBean) session.getAttribute("obj_usuario");
		sistemaLogado = (Sistema) session.getAttribute("sistema_logado");
		permsUsuarioLogado = (List<Permissoes>) FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().get("perms_usuario");
		
		Integer count = 0;
		if (!isLoginPage && !isPrincipal && usuarioBean != null) {
			for (Permissoes p : permsUsuarioLogado) {
				String aux = p.getMenu().getUrl();
				if (p.getMenu().getUrl() != null) {
					if (currentPage.replace(".xhtml", ".faces").replace("?faces-redirect=true", "")
							.equals(aux.replace("?faces-redirect=true", ""))) {
						if (currentPage.contains(sistemaLogado.getSigla())) {
							count++;
						}
					}
				}
			}
		}
	}

	@Override
	public void beforePhase(PhaseEvent event) {
	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.RESTORE_VIEW;
	}
}