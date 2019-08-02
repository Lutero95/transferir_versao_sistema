package br.gov.al.maceio.sishosp.comum.exception;

import br.gov.al.maceio.sishosp.comum.util.JSFUtil;

public class ProjetoException extends Exception{
    /**
	 * 
	 */
	private static final long serialVersionUID = -8768268699640382537L;

	public ProjetoException() {
    }
     public ProjetoException(String arg) {
        JSFUtil.adicionarMensagemErro(arg,"ERRO");    
    }

    public ProjetoException(Throwable arg) {
        super(arg);    
    }

    public ProjetoException(String arg,Throwable arg1){
        super(arg,arg1);
}

}
