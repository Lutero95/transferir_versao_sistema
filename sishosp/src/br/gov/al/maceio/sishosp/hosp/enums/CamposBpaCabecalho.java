package br.gov.al.maceio.sishosp.hosp.enums;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.interfaces.IBpa;
import br.gov.al.maceio.sishosp.questionario.enums.ModeloSexo;

public enum CamposBpaCabecalho implements IBpa  {

	
    CBC_MVM { 
    	public String preencheCaracteresRestantes(String campo) throws ProjetoException {
    		Integer tamanho = 6;
    		if(VerificadorUtil.verificarSeObjetoNuloOuVazio(campo) || campo.length() < tamanho)
    			throw new ProjetoException("O campo ANO E MÊS DE PROCESSAMENTO é inválido no BPA Cabeçalho");
    		campo = excluiCaracteresExcedentes(campo, tamanho);
    		return campo;
    	}
    },
    
    CBC_LIN {
    	public String preencheCaracteresRestantes(String campo) throws ProjetoException {
    		Integer tamanho = 6;
    		campo = excluiCaracteresExcedentes(campo, tamanho);
    		return retornaCampoNumericoComZeroAhEsquerda(campo, tamanho);
    	}
    },
    
    CBC_FLH {
    	public String preencheCaracteresRestantes(String campo) throws ProjetoException {
    		Integer tamanho = 6;
    		campo = retornaStringVaziaQuandoValorEhNulo(campo);
    		campo = excluiCaracteresExcedentes(campo, tamanho);
    		return retornaCampoNumericoComZeroAhEsquerda(campo, tamanho);
    	}    	
    },
    
    
    CBC_SMT_VRF { 
    	public String preencheCaracteresRestantes(String campo) throws ProjetoException {//*
			Integer tamanho = 4;
			campo = retornaStringVaziaQuandoValorEhNulo(campo);
			campo = excluiCaracteresExcedentes(campo, tamanho);
			return retornaCampoNumericoComZeroAhEsquerda(campo, tamanho);
    	}
    },
    
    CBC_RSP {
    	public String preencheCaracteresRestantes(String campo) throws ProjetoException {
			Integer tamanho = 30;
			campo = retornaStringVaziaQuandoValorEhNulo(campo);
			campo = excluiCaracteresExcedentes(campo, tamanho);
			return retornaCampoComEspacosAhDireita(campo, tamanho);
    	}
    },
    
    CBC_SGL {
    	public String preencheCaracteresRestantes(String campo) {
			Integer tamanho = 6;
			campo = retornaStringVaziaQuandoValorEhNulo(campo);
			campo = excluiCaracteresExcedentes(campo, tamanho);
			return retornaCampoComEspacosAhDireita(campo, tamanho);
    	}
    },
    
    CBC_CGCCPF {
    	public String preencheCaracteresRestantes(String campo) {
			Integer tamanho = 14;
			campo = retornaStringVaziaQuandoValorEhNulo(campo);
			campo = excluiCaracteresExcedentes(campo, tamanho);
			return retornaCampoNumericoComZeroAhEsquerda(campo, tamanho);
    	}    	
    },
    
    CBC_DST {
    	public String preencheCaracteresRestantes(String campo) {
			Integer tamanho = 40;
			campo = retornaStringVaziaQuandoValorEhNulo(campo);
			campo = excluiCaracteresExcedentes(campo, tamanho);
			return retornaCampoComEspacosAhDireita(campo, tamanho);
    	}    	
    },

    CBC_DST_IN {
    	public String preencheCaracteresRestantes(String campo) throws ProjetoException {
			if(!campo.equals("E") && !campo.equals("M"))
				throw new ProjetoException("INDICADOR DE ORGÃO DE DESTINO inválido no cabeçalho");
			return campo;
    	}    	
    },
    
    /* CASO NÃO SEJA VÁLIDO O CABEÇALHO DEVIDO AO TAMANHO 6, ALTERAR PARA 10 
     * POIS O TAMANHO 6 ESTÁ SEGUINDO O PADRÃO DE UM DOCUMENTO RECEBIDO PELO WALTER
     * E O 10 A DOCUMENTAÇÃO OFICIAL */
    CBC_VERSAO {
    	public String preencheCaracteresRestantes(String campo) throws ProjetoException {
    		Integer tamanho = 6;
			campo = retornaStringVaziaQuandoValorEhNulo(campo);
			if(VerificadorUtil.verificarSeObjetoNuloOuVazio(campo))
				throw new ProjetoException("VERSÃO DO SISTEMA informada é nula");
			else if(campo.length() < tamanho)
				return retornaCampoComEspacosAhDireita(campo, tamanho);
			return campo;
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
