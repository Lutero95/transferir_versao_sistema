package br.gov.al.maceio.sishosp.comum.util;

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

	public static String redirectInsertSemTipo(String ENDERECO_CADASTRO) {
		return ENDERECO_CADASTRO;
	}

	public static String redirectPagina(String PAGINA) {
		return PAGINA;
	}

}
