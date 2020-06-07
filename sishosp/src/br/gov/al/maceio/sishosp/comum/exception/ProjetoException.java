package br.gov.al.maceio.sishosp.comum.exception;

import br.gov.al.maceio.sishosp.comum.dao.ExceptionDAO;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;

public class ProjetoException extends Exception{
    /**
	 * 
	 */
	private static final long serialVersionUID = -8768268699640382537L;
	private ExceptionDAO exceptionDAO = new ExceptionDAO();

	public ProjetoException() {

    }
     public ProjetoException(String arg) {
    	 exceptionDAO.gravaExcecao(arg);
         JSFUtil.adicionarMensagemErro(arg, "Erro");
         JSFUtil.atualizarComponente(":msgPagina");
    }

    public ProjetoException(Throwable arg) {
    	exceptionDAO.gravaExcecao(arg.getMessage());
        JSFUtil.adicionarMensagemErro(arg.getMessage(), "Erro");
        JSFUtil.atualizarComponente(":msgPagina");
    }

    public ProjetoException(String arg,Throwable arg1){
        super(arg,arg1);
    }

}
