package br.gov.al.maceio.sishosp.comum.util;

public final class TratamentoErrosUtil {
	
	//MENSAGENS DE RETORNO
	private static final String MENSAGEM_UNIQUE = "Já existe Valor que não pode ser repetido está sendo utilizado, por favor mude o valor";
	private static final String MENSAGEM_CHECK = "Valor não permitido para o campo!";
	private static final String MENSAGEM_NOT_NULL = "Existe um ou mais campos em branco que tem preenchimento obrigatório!";
	private static final String MENSAGEM_FK_INSERCAO = "Campo com valor não permitido sendo passado!";
	private static final String MENSAGEM_FK_EXCLUSAO = "Exclusão não permitida pois está sendo utilizado em alguma rotina!";
	
	//CÓDIGOS DE ERRO
	private static final String CODIGO_UNIQUE = "23505";
	private static final String CODIGO_CHECK = "23514";
	private static final String CODIGO_NOT_NULL = "23502";
	private static final String CODIGO_FK_INSERCAO = "23503";
	private static final String CODIGO_FK_EXCLUSAO = "23P01";
	

    public static String retornarMensagemDeErro(String codigoErro) {
        return tratarMensagemDeErro(codigoErro);    	
    }
    
    private static String tratarMensagemDeErro(String codigoErro) {
    	String retorno = "";
    	
    	if(codigoErro.equals(CODIGO_UNIQUE)) {
        	retorno = MENSAGEM_UNIQUE;
        }
    	
    	else if(codigoErro.equals(CODIGO_CHECK)) {
        	retorno = MENSAGEM_CHECK;
        }
    	
    	else if(codigoErro.equals(CODIGO_NOT_NULL)) {
        	retorno = MENSAGEM_NOT_NULL;
        }
    	
    	else if(codigoErro.equals(CODIGO_FK_INSERCAO)) {
        	retorno = MENSAGEM_FK_INSERCAO;
        }
    	
    	else if(codigoErro.equals(CODIGO_FK_EXCLUSAO)) {
        	retorno = MENSAGEM_FK_EXCLUSAO;
        }
    	
    	return retorno;
    }
    
}
