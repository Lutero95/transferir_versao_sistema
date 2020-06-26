package br.gov.al.maceio.sishosp.comum.util;

import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.model.ErroSistema;

public class ErrosUtil {
	
	
    public static ErroSistema retornaErroSistema(StackTraceElement[] classesErro, String nomeClasse, String exception ) {
    	FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
    			.getSessionMap().get("obj_usuario");
    	
    	String nomeBancoAcesso = (String) SessionUtil.resgatarDaSessao("nomeBancoAcesso");
    	
    	ErroSistema erroSistema = new ErroSistema();
    	
    	if(VerificadorUtil.verificarSeObjetoNulo(user_session)) {
    		erroSistema.setIdUsuarioLogado(null);
    		erroSistema.setBanco(null);
    	}
    	else {
    		erroSistema.setIdUsuarioLogado(user_session.getId());
    		erroSistema.setBanco(nomeBancoAcesso);
		}
    	
		erroSistema.setDescricao(exception);
		
    	for (StackTraceElement classe : classesErro) {
			if(classe.getClassName().equals(nomeClasse)) {
				erroSistema.setNomeClasse(classe.getClassName());
				erroSistema.setNomeMetodo(classe.getMethodName());
				erroSistema.setLinhaErro(classe.getLineNumber());
			}
		}
		return erroSistema;
	}
    
}
