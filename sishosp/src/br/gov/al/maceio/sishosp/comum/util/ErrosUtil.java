package br.gov.al.maceio.sishosp.comum.util;

public class ErrosUtil {
	
	
    public static String retornaLinhaIhClasseDoErro(StackTraceElement[] classesErro, String nomeClasse ) {
    	String classeMetodoIhLinhaErro =  null;
    	for (StackTraceElement classe : classesErro) {
			if(classe.getClassName().equals(nomeClasse)) {
				classeMetodoIhLinhaErro = 
						" Classe: "+classe.getClassName()+" Método: "+classe.getMethodName()+" Linha do erro: "+classe.getLineNumber();
			}
		}
		return classeMetodoIhLinhaErro;
	}
    
    public static Throwable retornaThrowableComALinhaEspecificaDoErro(Throwable throwableParametro, String nomeClasse) {
    	String mensagemErroCompleta = throwableParametro.getLocalizedMessage() + retornaLinhaIhClasseDoErro(throwableParametro.getStackTrace(), nomeClasse);
    	Throwable throwable = new Throwable(mensagemErroCompleta);
    	throwable.setStackTrace(throwableParametro.getStackTrace());
        return throwable;    	
    }
}
