package br.gov.al.maceio.sishosp.comum.util;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

public final class RedirecionarUtil {

	public static String redirectEdit(String ENDERECO_CADASTRO, String ENDERECO_ID, Integer idObjeto, String ENDERECO_TIPO, Integer tipo) {
		return ENDERECO_CADASTRO+ENDERECO_ID+idObjeto+ENDERECO_TIPO+tipo;
	}

	public static String redirectInsert(String ENDERECO_CADASTRO, String ENDERECO_TIPO, Integer tipo) {
		return ENDERECO_CADASTRO+ENDERECO_TIPO+tipo;
	}

	public static String redirectEditSemTipo(String ENDERECO_CADASTRO, String ENDERECO_ID, Integer idObjeto) {
		return ENDERECO_CADASTRO+ENDERECO_ID+idObjeto;
	}
	
	public static String redirectEditSemTeipoComDoisParametros(String ENDERECO_CADASTRO, String ENDERECO_ID, Integer idObjeto,String ENDERECO_ID2, Integer idObjeto2) {
		return ENDERECO_CADASTRO+ENDERECO_ID+idObjeto+ENDERECO_ID2+idObjeto2;
	}

	public static String redirectInsertSemTipo(String ENDERECO_CADASTRO) {
		return ENDERECO_CADASTRO;
	}

	public static String redirectPagina(String PAGINA) {
		return PAGINA;
	}
	
	public static ServletContext getServleContext() {
		ServletContext scontext = (ServletContext) getFacesContext().getExternalContext().getContext();
		return scontext;
	}
	
	private static FacesContext getFacesContext() {
		FacesContext context = FacesContext.getCurrentInstance();
		return context;
	}
}
