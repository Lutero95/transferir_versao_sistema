package br.gov.al.maceio.sishosp.hosp.enums;

import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.interfaces.IBpaIndividualizado;

public enum CamposBpaIndividualizados implements IBpaIndividualizado  {

	
    PRD_CNES { 
    	public String preencheCaracteresRestantes(String campo) {
    		Integer tamanho = 7;
    		campo = retornaStringVaziaQuandoValorEhNulo(campo);
    		if(campo.isEmpty())
    			return retornaCampoComEspacosAhDireita(campo, tamanho);
    		return retornaCampoNumericoComZeroAhEsquerda(campo, tamanho);
    	}

    },
    
    PRD_CMP {
    	public String preencheCaracteresRestantes(String campo) {
    		Integer tamanho = 6;
    		campo = retornaStringVaziaQuandoValorEhNulo(campo);
    		if(campo.isEmpty())
    			return retornaCampoComEspacosAhDireita(campo, tamanho);
    		return campo;
    	}
    },
    
    PRD_CNSMED {
    	public String preencheCaracteresRestantes(String campo) {
    		Integer tamanho = 15;
    		campo = retornaStringVaziaQuandoValorEhNulo(campo);
    		if(campo.isEmpty())
    			return retornaCampoComEspacosAhDireita(campo, tamanho);
    		return campo;
    	}    	
    },
    
    PRD_CBO {
    	public String preencheCaracteresRestantes(String campo) {
    		Integer tamanho = 6;
    		campo = retornaStringVaziaQuandoValorEhNulo(campo);
    		if(campo.isEmpty())
    			return retornaCampoComEspacosAhDireita(campo, tamanho);
    		return campo;
    	}    	
    },
    
    PRD_DTATEN {
    	public String preencheCaracteresRestantes(String campo) {
    		Integer tamanho = 8;
    		campo = retornaStringVaziaQuandoValorEhNulo(campo);
    		if(campo.isEmpty())
    			return retornaCampoComEspacosAhDireita(campo, tamanho);
    		return campo;
    	}    	
    },
    
    PRD_FLH { 
    	public String preencheCaracteresRestantes(String campo) {
			Integer tamanho = 3;
			campo = retornaStringVaziaQuandoValorEhNulo(campo);
			return retornaCampoNumericoComZeroAhEsquerda(campo, tamanho);
    	}
    },
    
    PRD_SEQ {
    	public String preencheCaracteresRestantes(String campo) {
			Integer tamanho = 2;
			campo = retornaStringVaziaQuandoValorEhNulo(campo);
			return retornaCampoNumericoComZeroAhEsquerda(campo, tamanho);
    	}
    },
    
    PRD_PA {
    	public String preencheCaracteresRestantes(String campo) {
			Integer tamanho = 10;
			campo = retornaStringVaziaQuandoValorEhNulo(campo);
			return retornaCampoNumericoComZeroAhEsquerda(campo, tamanho);
    	}
    },
    
    PRD_CNSPAC {
    	public String preencheCaracteresRestantes(String campo) {
			Integer tamanho = 15;
			campo = retornaStringVaziaQuandoValorEhNulo(campo);
    		if(campo.isEmpty())
    			return retornaCampoComEspacosAhDireita(campo, tamanho);
    		
			return retornaCampoNumericoComZeroAhEsquerda(campo, tamanho);
    	}    	
    },
    
    PRD_SEXO {//**
    	public String preencheCaracteresRestantes(String campo) {
    		campo = retornaStringVaziaQuandoValorEhNulo(campo);
    		return campo;
    	}
    },
    
    PRD_IBGE {
    	public String preencheCaracteresRestantes(String campo) {
			Integer tamanho = 6;
			campo = retornaStringVaziaQuandoValorEhNulo(campo);
    		if(campo.isEmpty())
    			return retornaCampoComEspacosAhDireita(campo, tamanho);
			return retornaCampoNumericoComZeroAhEsquerda(campo, tamanho);
    	}    	
    },
    
    PRD_CID {
    	public String preencheCaracteresRestantes(String campo) {
			Integer tamanho = 4;
			campo = retornaStringVaziaQuandoValorEhNulo(campo);
    		if(campo.isEmpty())
    			return retornaCampoComEspacosAhDireita(campo, tamanho);
    		return campo;
    	}
    },
    
    PRD_IDADE {
    	public String preencheCaracteresRestantes(String campo) {
			Integer tamanho = 3;
			campo = retornaStringVaziaQuandoValorEhNulo(campo);
			return retornaCampoNumericoComZeroAhEsquerda(campo, tamanho);
    	}    	
    },
    
    PRD_QT {
    	public String preencheCaracteresRestantes(String campo) {
			Integer tamanho = 6;
			campo = retornaStringVaziaQuandoValorEhNulo(campo);
			return retornaCampoNumericoComZeroAhEsquerda(campo, tamanho);
    	}    	
    },
    
    PRD_CATEN {
    	public String preencheCaracteresRestantes(String campo) {
			Integer tamanho = 2;
			campo = retornaStringVaziaQuandoValorEhNulo(campo);
    		if(campo.isEmpty())
    			return retornaCampoComEspacosAhDireita(campo, tamanho);
			return retornaCampoNumericoComZeroAhEsquerda(campo, tamanho);
    	}    	
    },
    
    PRD_NAUT {
    	public String preencheCaracteresRestantes(String campo) {
			Integer tamanho = 13;
			campo = retornaStringVaziaQuandoValorEhNulo(campo);
    		if(campo.isEmpty())
    			return retornaCampoComEspacosAhDireita(campo, tamanho);
			return retornaCampoNumericoComZeroAhEsquerda(campo, tamanho);
    	}    	
    },
    
    PRD_ORG {//**
    	public String preencheCaracteresRestantes(String campo) {
    		campo = retornaStringVaziaQuandoValorEhNulo(campo);
    		return campo;
    	}
    },
    
    PRD_NMPAC {
    	public String preencheCaracteresRestantes(String campo) {
			Integer tamanho = 30;
			campo = retornaStringVaziaQuandoValorEhNulo(campo);
			return retornaCampoComEspacosAhDireita(campo, tamanho);
    	}    	
    },
    
    PRD_DTNASC {
    	public String preencheCaracteresRestantes(String campo) {
    		Integer tamanho = 8;
    		campo = retornaStringVaziaQuandoValorEhNulo(campo);
    		if(campo.isEmpty())
    			return retornaCampoComEspacosAhDireita(campo, tamanho);
    		return campo;
    	}
    },
    
    PRD_RACA {
    	public String preencheCaracteresRestantes(String campo) {
			Integer tamanho = 2;
			campo = retornaStringVaziaQuandoValorEhNulo(campo);
    		if(campo.isEmpty())
    			return retornaCampoComEspacosAhDireita(campo, tamanho);
			return retornaCampoNumericoComZeroAhEsquerda(campo, tamanho);
    	}    	
    },
    
    PRD_ETNIA {//**
    	public String preencheCaracteresRestantes(String campo) {
    		Integer tamanho = 4;
    		campo = retornaStringVaziaQuandoValorEhNulo(campo);
    		if(campo.isEmpty())
    			return retornaCampoComEspacosAhDireita(campo, tamanho);
    		return retornaCampoNumericoComZeroAhEsquerda(campo, tamanho);
    	}
    },
    
    PRD_NAC {
    	public String preencheCaracteresRestantes(String campo) {
			Integer tamanho = 3;
			campo = retornaStringVaziaQuandoValorEhNulo(campo);
    		if(campo.isEmpty())
    			return retornaCampoComEspacosAhDireita(campo, tamanho);
			return campo;
    	}    	
    },
    
    PRD_SRV {
    	public String preencheCaracteresRestantes(String campo) {
			Integer tamanho = 3;
			campo = retornaStringVaziaQuandoValorEhNulo(campo);
    		if(campo.isEmpty())
    			return retornaCampoComEspacosAhDireita(campo, tamanho);
			return retornaCampoNumericoComZeroAhEsquerda(campo, tamanho);
    	}    	
    },
    
    PRD_CLF {
    	public String preencheCaracteresRestantes(String campo) {
			Integer tamanho = 3;
			campo = retornaStringVaziaQuandoValorEhNulo(campo);
    		if(campo.isEmpty())
    			return retornaCampoComEspacosAhDireita(campo, tamanho);
    		return retornaCampoNumericoComZeroAhEsquerda(campo, tamanho);
    	}
    },
    
    PRD_EQUIPE_SEQ {
    	public String preencheCaracteresRestantes(String campo) {
			Integer tamanho = 8;
			campo = retornaStringVaziaQuandoValorEhNulo(campo);
    		if(campo.isEmpty())
    			return retornaCampoComEspacosAhDireita(campo, tamanho);
    		return retornaCampoNumericoComZeroAhEsquerda(campo, tamanho);
    	}    	
    },
    
    PRD_EQUIPE_AREA {
    	public String preencheCaracteresRestantes(String campo) {
			Integer tamanho = 4;
			campo = retornaStringVaziaQuandoValorEhNulo(campo);
    		if(campo.isEmpty())
    			return retornaCampoComEspacosAhDireita(campo, tamanho);
    		return retornaCampoNumericoComZeroAhEsquerda(campo, tamanho);
    	}    	
    },
    
    PRD_CNPJ {
    	public String preencheCaracteresRestantes(String campo) {
    		Integer tamanho = 14;
    		campo = retornaStringVaziaQuandoValorEhNulo(campo);
    		if(campo.isEmpty())
    			return retornaCampoComEspacosAhDireita(campo, tamanho);
    		return retornaCampoNumericoComZeroAhEsquerda(campo, tamanho);
    	}
    },
    
    PRD_CEP_PCNTE {
    	public String preencheCaracteresRestantes(String campo) {
    		Integer tamanho = 8;
    		campo = retornaStringVaziaQuandoValorEhNulo(campo);
    		if(campo.isEmpty())
    			return retornaCampoComEspacosAhDireita(campo, tamanho);
    		return retornaCampoNumericoComZeroAhEsquerda(campo, tamanho);
    	}    	
    },
    
    PRD_LOGRAD_PCNTE {
    	public String preencheCaracteresRestantes(String campo) {
    		Integer tamanho = 3;
    		campo = retornaStringVaziaQuandoValorEhNulo(campo);
    		if(campo.isEmpty())
    			return retornaCampoComEspacosAhDireita(campo, tamanho);
    		return retornaCampoNumericoComZeroAhEsquerda(campo, tamanho);
    	}    	
    },
    
    PRD_END_PCNTE {
    	public String preencheCaracteresRestantes(String campo) {
    		Integer tamanho = 30;
    		campo = retornaStringVaziaQuandoValorEhNulo(campo);
    		return retornaCampoComEspacosAhDireita(campo, tamanho);
    	}    	
    },
    
    PRD_COMPL_PCNTE {
    	public String preencheCaracteresRestantes(String campo) {
    		Integer tamanho = 10;
    		campo = retornaStringVaziaQuandoValorEhNulo(campo);
    		return retornaCampoComEspacosAhDireita(campo, tamanho);
    	}    	
    },
    
    PRD_NUM_PCNTE {//**
    	public String preencheCaracteresRestantes(String campo) {
    		Integer tamanho = 5;
    		campo = retornaStringVaziaQuandoValorEhNulo(campo);
    		if(campo.isEmpty())
    			return SN;
    		return retornaCampoComEspacosAhDireita(campo, tamanho);
    	}    	
    },
    
    PRD_BAIRRO_PCNTE {
    	public String preencheCaracteresRestantes(String campo) {
    		Integer tamanho = 30;
    		campo = retornaStringVaziaQuandoValorEhNulo(campo);
    		return retornaCampoComEspacosAhDireita(campo, tamanho);
    	}
    },
    
    PRD_DDTEL_PCNTE {
    	public String preencheCaracteresRestantes(String campo) {
    		Integer tamanho = 11;
    		campo = retornaStringVaziaQuandoValorEhNulo(campo);
    		if(campo.isEmpty())
    			return retornaCampoComEspacosAhDireita(campo, tamanho);
    		return retornaCampoNumericoComZeroAhEsquerda(campo, tamanho);
    	}    	
    },
    
    PRD_EMAIL_PCNTE {
    	public String preencheCaracteresRestantes(String campo) {
    		Integer tamanho = 40;
    		campo = retornaStringVaziaQuandoValorEhNulo(campo);
    		return retornaCampoComEspacosAhDireita(campo, tamanho);
    	}    	
    },
    
    PRD_INE {
    	public String preencheCaracteresRestantes(String campo) {
    		Integer tamanho = 10;
    		campo = retornaStringVaziaQuandoValorEhNulo(campo);
    		if(campo.isEmpty())
    			return retornaCampoComEspacosAhDireita(campo, tamanho);
    		return retornaCampoNumericoComZeroAhEsquerda(campo, tamanho);
    	}    	
    };
	
	private static final String SN = "SN   ";
	
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
}
