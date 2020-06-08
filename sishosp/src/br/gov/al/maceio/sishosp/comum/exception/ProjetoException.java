package br.gov.al.maceio.sishosp.comum.exception;

import java.sql.SQLException;

import br.gov.al.maceio.sishosp.comum.dao.ExceptionDAO;
import br.gov.al.maceio.sishosp.comum.model.ErroSistema;
import br.gov.al.maceio.sishosp.comum.util.ErrosUtil;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;

public class ProjetoException extends Exception{
    /**
	 * 
	 */
	private static final long serialVersionUID = -8768268699640382537L;
	private static final String NULLPOINTEREXCEPTION = "NullPointerException";
	
	private ExceptionDAO exceptionDAO = new ExceptionDAO();

	public ProjetoException() {

    }
     public ProjetoException(String mensagemUsuario, String nomeDaClasse, Throwable arg) {
    	 arg.printStackTrace();
    	 ErroSistema erroSistema = ErrosUtil.retornaErroSistema(arg.getStackTrace(), nomeDaClasse, arg.getMessage());
    	 exceptionDAO.gravaExcecao(erroSistema);
         JSFUtil.adicionarMensagemErro(mensagemUsuario, "Erro");
         JSFUtil.atualizarComponente(":msgPagina");
    }

    public ProjetoException(Throwable arg, String nomeDaClasse) {
    	arg.printStackTrace();
    	
    	String exception = arg.getMessage();
    	
    	if(arg.getClass().getSimpleName().equalsIgnoreCase(NULLPOINTEREXCEPTION))
    		exception = NULLPOINTEREXCEPTION;

    	ErroSistema erroSistema = ErrosUtil.retornaErroSistema(arg.getStackTrace(), nomeDaClasse, exception);
   	 	exceptionDAO.gravaExcecao(erroSistema);
        JSFUtil.adicionarMensagemErro(arg.getMessage(), "Erro");
        JSFUtil.atualizarComponente(":msgPagina");
    }

    public ProjetoException(String arg,Throwable arg1){
        super(arg,arg1);
    }
}
