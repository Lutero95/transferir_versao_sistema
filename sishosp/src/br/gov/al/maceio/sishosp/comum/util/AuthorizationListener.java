package br.gov.al.maceio.sishosp.comum.util;



import java.io.IOException;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpSession;

import br.gov.al.maceio.sishosp.acl.control.FuncionarioController;
import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.acl.model.Permissoes;
import br.gov.al.maceio.sishosp.acl.model.Sistema;

public class AuthorizationListener implements PhaseListener {

    private List<Permissoes> permsUsuarioLogado;
    private static String CONTEXTO_APLICACAO = FacesContext.getCurrentInstance().getExternalContext().getApplicationContextPath();

    @Override
    public void afterPhase(PhaseEvent event) {
        // Adiquirindo o FacesContext.
        FacesContext facesContext = event.getFacesContext();

        // Página Atual.
        String currentPage = facesContext.getViewRoot().getViewId();

        // Verifica se está na página de login.
        boolean isLoginPage = (currentPage.lastIndexOf("login") > -1);

        // Verifica se está na página principal.
        boolean selecaoSistemas = (currentPage.replace(".xhtml", ".faces")
                .equals("/pages/comum/selecaoSistema.faces"));
        

        
        boolean contato = (currentPage.replace(".xhtml", ".faces")
                .equals("/pages/comum/contato.faces"));
        
       Integer count = 0;

        // Verifica se se tem permissão para a página.
        boolean naoPermitido = false;

        // Recuperando objetos da sessão.
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);

        FuncionarioBean usuario = (FuncionarioBean) session.getAttribute("obj_usuario");

        Sistema sistemaLogado = (Sistema) session.getAttribute("sistema_logado");

        permsUsuarioLogado = (List<Permissoes>) FacesContext.getCurrentInstance()
                .getExternalContext().getSessionMap().get("perms_usuario");

        // Redireciona para a página de login quando a sessão expira.
        if (!isLoginPage && usuario == null) {
            try {
                FuncionarioController.timeOut();
                if (SessionUtil.getSession() != null) {
                    SessionUtil.getSession().invalidate();

                    FacesContext.getCurrentInstance().getExternalContext()
                            .getSessionMap().put("expired", "S");
                }

                String url = CONTEXTO_APLICACAO + "/pages/comum/login.faces";

                FacesContext.getCurrentInstance().getExternalContext()
                        .redirect(url);

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } else {
            // Lógica do controle de acesso as páginas.
        }

        if (!isLoginPage && !selecaoSistemas && !contato && usuario != null) {
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

            if (count < 1) {
                naoPermitido = true;
            }

            if (permsUsuarioLogado.isEmpty() && !selecaoSistemas) {
                naoPermitido = true;
            }
        }
        String url = "";
        if (naoPermitido == true) {
            try {

                url = CONTEXTO_APLICACAO + "/pages/comum/selecaoSistema.faces";

                FacesContext.getCurrentInstance().getExternalContext()
                        .redirect(url);
                FacesContext.getCurrentInstance().getELContext();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
           }
        }
       
        	
    }

    @Override
    public void beforePhase(PhaseEvent event) {
        // Nada...
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }
}