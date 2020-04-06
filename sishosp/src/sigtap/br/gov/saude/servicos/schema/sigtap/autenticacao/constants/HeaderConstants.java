package br.gov.saude.servicos.schema.sigtap.autenticacao.constants;

public class HeaderConstants {
	//ENDPOINTS
	public static final String ENDPOINT_PROCEDIMENTO = "https://servicos.saude.gov.br/sigtap/ProcedimentoService/v1";
	
	//CHAVES DO HASHMAP QUE MONTA O HEADER
	public static final String ACTION = "action";
	public static final String USER = "user";
	public static final String PASSWORD_TYPE = "passwordType";
	public static final String PASSWORD_CALLBACK_CLASS = "passwordCallbackClass";
	
	//VALORES DO HASHMAP QUE MONTA O HEADER
	public static final String USERNAME_TOKEN = "UsernameToken";
	public static final String USERNAME = "SIGTAP.PUBLICO";
	public static final String PASSWORD_TEXT = "PasswordText";
	public static final String PASSWORD_HANDLER_CLASS = "br.gov.saude.servicos.schema.sigtap.autenticacao.PasswordHandler";
	
	//SENHA DE AUTENTICACAO
	public static final String PASSWORD = "sigtap#2015public";
}
