package br.gov.al.maceio.sishosp.hosp.enums;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.interfaces.IBpa;
import br.gov.al.maceio.sishosp.questionario.enums.ModeloSexo;

public enum CamposBpaConsolidado implements IBpa  {

	
    PRD_CNES { 
    	public String preencheCaracteresRestantes(String campo) throws ProjetoException {
    		Integer tamanho = 7;
    		if(VerificadorUtil.verificarSeObjetoNuloOuVazio(campo))
    			throw new ProjetoException("O campo CNES não poder ser nulo no BPA Consolidado");
    		campo = excluiCaracteresExcedentes(campo, tamanho);
    		return retornaCampoNumericoComZeroAhEsquerda(campo, tamanho);
    	}

    },
    
    PRD_CMP {
    	public String preencheCaracteresRestantes(String campo) throws ProjetoException {
    		Integer tamanho = 6;
    		if(VerificadorUtil.verificarSeObjetoNuloOuVazio(campo) || campo.length() < tamanho)
    			throw new ProjetoException("O campo COMPETENCIA está nulo ou inválido no BPA Consolidado");
    		campo = excluiCaracteresExcedentes(campo, tamanho);
    		return campo;
    	}
    },
    
    
    PRD_CBO {
    	public String preencheCaracteresRestantes(String campo) throws ProjetoException {
    		Integer tamanho = 6;
    		campo = retornaStringVaziaQuandoValorEhNulo(campo);
    		if(VerificadorUtil.verificarSeObjetoNuloOuVazio(campo) || campo.length() < tamanho)
    			throw new ProjetoException("O campo CBO está nulo ou inválido no BPA Consolidado");
    		campo = excluiCaracteresExcedentes(campo, tamanho);
    		return campo;
    	}    	
    },
    
    
    PRD_FLH { 
    	public String preencheCaracteresRestantes(String campo) throws ProjetoException {
			Integer tamanho = 3;
			campo = retornaStringVaziaQuandoValorEhNulo(campo);
    		if(VerificadorUtil.verificarSeObjetoNuloOuVazio(campo))
    			throw new ProjetoException("O campo FOLHA não poder ser nulo no BPA Consolidado");
			campo = excluiCaracteresExcedentes(campo, tamanho);
			return retornaCampoNumericoComZeroAhEsquerda(campo, tamanho);
    	}
    },
    
    PRD_SEQ {
    	public String preencheCaracteresRestantes(String campo) throws ProjetoException {
			Integer tamanho = 2;
			campo = retornaStringVaziaQuandoValorEhNulo(campo);
			if(VerificadorUtil.verificarSeObjetoNuloOuVazio(campo))
    			throw new ProjetoException("O campo LINHA DA FOLHA não poder ser nulo no BPA Consolidado");
			campo = excluiCaracteresExcedentes(campo, tamanho);
			return retornaCampoNumericoComZeroAhEsquerda(campo, tamanho);
    	}
    },
    
    PRD_PA {
    	public String preencheCaracteresRestantes(String campo) {
			Integer tamanho = 10;
			campo = retornaStringVaziaQuandoValorEhNulo(campo);
			campo = excluiCaracteresExcedentes(campo, tamanho);
			return retornaCampoNumericoComZeroAhEsquerda(campo, tamanho);
    	}
    },
    
    PRD_IDADE {
    	public String preencheCaracteresRestantes(String campo) {
			Integer tamanho = 3;
			campo = retornaStringVaziaQuandoValorEhNulo(campo);
			campo = excluiCaracteresExcedentes(campo, tamanho);
			return retornaCampoNumericoComZeroAhEsquerda(campo, tamanho);
    	}    	
    },
    
    PRD_QT {
    	public String preencheCaracteresRestantes(String campo) {
			Integer tamanho = 6;
			campo = retornaStringVaziaQuandoValorEhNulo(campo);
			campo = excluiCaracteresExcedentes(campo, tamanho);
			return retornaCampoNumericoComZeroAhEsquerda(campo, tamanho);
    	}    	
    };
	
	protected String retornaCampoNumericoComZeroAhEsquerda(String campo, Integer tamanho) {
		StringBuilder stringBuilder = new StringBuilder(campo);

		while (stringBuilder.length() < tamanho) {
			stringBuilder.insert(0, "0");
		}

		return stringBuilder.toString();
	}
	
	protected String retornaCampoComEspacosAhDireita(String campo, Integer tamanho) {
		while (campo.length() < tamanho) {
			campo += " ";
		}
		return campo;
	}
	
	protected String retornaStringVaziaQuandoValorEhNulo(String string) {
		if(VerificadorUtil.verificarSeObjetoNuloOuVazio(string))
			return new String();
		return string;
	}
	
	protected String excluiCaracteresExcedentes(String campo, Integer tamanho) {
		if(campo.length() > tamanho) {
			campo = campo.substring(0, tamanho);
			return campo;
		}
		return campo;
	}
}
