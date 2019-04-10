package br.gov.al.maceio.sishosp.comum.util;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

public class SessionUtil {

    public static HttpSession getSession() {
        ExternalContext extCon = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) extCon.getSession(true);
        return session;
    }

    public static FuncionarioBean recuperarDadosSessao() {
        FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("obj_funcionario");

        return user_session;
    }
}