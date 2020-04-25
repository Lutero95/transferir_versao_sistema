package br.gov.al.maceio.sishosp.hosp.enums;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.interfaces.IBpaIndividualizado;
import br.gov.al.maceio.sishosp.questionario.enums.ModeloSexo;

public enum CamposBpaIndividualizados implements IBpaIndividualizado  {

	
    PRD_CNES { 
    	public String preencheCaracteresRestantes(String campo) throws ProjetoException {
    		Integer tamanho = 7;
    		if(VerificadorUtil.verificarSeObjetoNuloOuVazio(campo))
    			throw new ProjetoException("O campo CNES não poder ser nulo no BPA Individualizado");
    		campo = excluiCaracteresExcedentes(campo, tamanho);
    		return retornaCampoNumericoComZeroAhEsquerda(campo, tamanho);
    	}

    },
    
    PRD_CMP {
    	public String preencheCaracteresRestantes(String campo) throws ProjetoException {
    		Integer tamanho = 6;
    		if(VerificadorUtil.verificarSeObjetoNuloOuVazio(campo) || campo.length() < tamanho)
    			throw new ProjetoException("O campo COMPETENCIA está nulo ou inválido no BPA Individualizado");
    		campo = excluiCaracteresExcedentes(campo, tamanho);
    		return campo;
    	}
    },
    
    PRD_CNSMED {
    	public String preencheCaracteresRestantes(String campo) throws ProjetoException {
    		Integer tamanho = 15;
    		if(VerificadorUtil.verificarSeObjetoNuloOuVazio(campo) || campo.length() < tamanho)
    			throw new ProjetoException("O campo CNS DO MÉDICO está nulo ou inválido no BPA Individualizado");
    		campo = excluiCaracteresExcedentes(campo, tamanho);
    		return campo;
    	}    	
    },
    
    PRD_CBO {
    	public String preencheCaracteresRestantes(String campo) throws ProjetoException {
    		Integer tamanho = 6;
    		campo = retornaStringVaziaQuandoValorEhNulo(campo);
    		if(VerificadorUtil.verificarSeObjetoNuloOuVazio(campo) || campo.length() < tamanho)
    			throw new ProjetoException("O campo CBO está nulo ou inválido no BPA Individualizado");
    		campo = excluiCaracteresExcedentes(campo, tamanho);
    		return campo;
    	}    	
    },
    
    PRD_DTATEN {
    	public String preencheCaracteresRestantes(String campo) throws ProjetoException {
    		Integer tamanho = 8;
    		campo = retornaStringVaziaQuandoValorEhNulo(campo);
    		if(campo.isEmpty())
    			return retornaCampoComEspacosAhDireita(campo, tamanho);
    		else if (campo.length() < tamanho)
    			throw new ProjetoException("O campo DATA DE ATENDIMENTO é inválido no BPA Individualizado");
    		campo = excluiCaracteresExcedentes(campo, tamanho);
    		return campo;
    	}    	
    },
    
    PRD_FLH { 
    	public String preencheCaracteresRestantes(String campo) {
			Integer tamanho = 3;
			campo = retornaStringVaziaQuandoValorEhNulo(campo);
			campo = excluiCaracteresExcedentes(campo, tamanho);
			return retornaCampoNumericoComZeroAhEsquerda(campo, tamanho);
    	}
    },
    
    PRD_SEQ {
    	public String preencheCaracteresRestantes(String campo) {
			Integer tamanho = 2;
			campo = retornaStringVaziaQuandoValorEhNulo(campo);
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
    
    PRD_CNSPAC {
    	public String preencheCaracteresRestantes(String campo) {
			Integer tamanho = 15;
			campo = retornaStringVaziaQuandoValorEhNulo(campo);
    		if(campo.isEmpty())
    			return retornaCampoComEspacosAhDireita(campo, tamanho);
    		campo = excluiCaracteresExcedentes(campo, tamanho);
			return retornaCampoNumericoComZeroAhEsquerda(campo, tamanho);
    	}    	
    },
    
    PRD_SEXO {
    	public String preencheCaracteresRestantes(String campo) throws ProjetoException {
    		campo = retornaStringVaziaQuandoValorEhNulo(campo);
    		if (!campo.equals(ModeloSexo.MASCULINO.getSigla()) && !campo.equals(ModeloSexo.FEMININO.getSigla()))
    			throw new ProjetoException("O campo SEXO é inválido no BPA Individualizado");
    		return campo;
    	}
    },
    
    PRD_IBGE {
    	public String preencheCaracteresRestantes(String campo) {
			Integer tamanho = 6;
			campo = retornaStringVaziaQuandoValorEhNulo(campo);
    		if(campo.isEmpty())
    			return retornaCampoComEspacosAhDireita(campo, tamanho);
    		campo = excluiCaracteresExcedentes(campo, tamanho);
			return retornaCampoNumericoComZeroAhEsquerda(campo, tamanho);
    	}    	
    },
    
    PRD_CID {
    	public String preencheCaracteresRestantes(String campo) throws ProjetoException {
			Integer tamanho = 4;
			campo = retornaStringVaziaQuandoValorEhNulo(campo);
    		if(campo.isEmpty())
    			return retornaCampoComEspacosAhDireita(campo, tamanho);
    		else if (campo.length() < tamanho)
    			throw new ProjetoException("O campo CID é inválido no BPA Individualizado");
    		campo = excluiCaracteresExcedentes(campo, tamanho);
    		return campo;
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
    },
    
    PRD_CATEN {
    	public String preencheCaracteresRestantes(String campo) {
			Integer tamanho = 2;
			campo = retornaStringVaziaQuandoValorEhNulo(campo);
    		if(campo.isEmpty())
    			return retornaCampoComEspacosAhDireita(campo, tamanho);
    		campo = excluiCaracteresExcedentes(campo, tamanho);
			return retornaCampoNumericoComZeroAhEsquerda(campo, tamanho);
    	}    	
    },
    
    PRD_NAUT {
    	public String preencheCaracteresRestantes(String campo) {
			Integer tamanho = 13;
			campo = retornaStringVaziaQuandoValorEhNulo(campo);
    		if(campo.isEmpty())
    			return retornaCampoComEspacosAhDireita(campo, tamanho);
    		campo = excluiCaracteresExcedentes(campo, tamanho);
			return retornaCampoNumericoComZeroAhEsquerda(campo, tamanho);
    	}    	
    },
    
    PRD_NMPAC {
    	public String preencheCaracteresRestantes(String campo) {
			Integer tamanho = 30;
			campo = retornaStringVaziaQuandoValorEhNulo(campo);
			campo = excluiCaracteresExcedentes(campo, tamanho);
			return retornaCampoComEspacosAhDireita(campo, tamanho);
    	}    	
    },
    
    PRD_DTNASC {
    	public String preencheCaracteresRestantes(String campo) throws ProjetoException {
    		Integer tamanho = 8;
    		campo = retornaStringVaziaQuandoValorEhNulo(campo);
    		if(campo.isEmpty())
    			return retornaCampoComEspacosAhDireita(campo, tamanho);
    		else if (campo.length() < tamanho)
    			throw new ProjetoException("O campo DATA DE NASCIMENTO é inválido no BPA Individualizado");
    		campo = excluiCaracteresExcedentes(campo, tamanho);
    		return campo;
    	}
    },
    
    PRD_RACA {
    	public String preencheCaracteresRestantes(String campo) {
			Integer tamanho = 2;
			campo = retornaStringVaziaQuandoValorEhNulo(campo);
    		if(campo.isEmpty())
    			return retornaCampoComEspacosAhDireita(campo, tamanho);
    		campo = excluiCaracteresExcedentes(campo, tamanho);
			return retornaCampoNumericoComZeroAhEsquerda(campo, tamanho);
    	}    	
    },
    
    PRD_ETNIA {
    	public String preencheCaracteresRestantes(String campo) {
    		Integer tamanho = 4;
    		campo = retornaStringVaziaQuandoValorEhNulo(campo);
    		campo = excluiCaracteresExcedentes(campo, tamanho);
    		return retornaCampoComEspacosAhDireita(campo, tamanho);
    	}
    },
    
    PRD_NAC {
    	public String preencheCaracteresRestantes(String campo) {
			Integer tamanho = 3;
			campo = retornaStringVaziaQuandoValorEhNulo(campo);
    		if(campo.isEmpty())
    			return retornaCampoComEspacosAhDireita(campo, tamanho);
    		campo = excluiCaracteresExcedentes(campo, tamanho);
			return retornaCampoNumericoComZeroAhEsquerda(campo, tamanho);
    	}    	
    },
    
    PRD_SRV {
    	public String preencheCaracteresRestantes(String campo) {
			Integer tamanho = 3;
			campo = retornaStringVaziaQuandoValorEhNulo(campo);
    		if(campo.isEmpty())
    			return retornaCampoComEspacosAhDireita(campo, tamanho);
    		campo = excluiCaracteresExcedentes(campo, tamanho);
			return retornaCampoNumericoComZeroAhEsquerda(campo, tamanho);
    	}    	
    },
    
    PRD_CLF {
    	public String preencheCaracteresRestantes(String campo) {
			Integer tamanho = 3;
			campo = retornaStringVaziaQuandoValorEhNulo(campo);
    		if(campo.isEmpty())
    			return retornaCampoComEspacosAhDireita(campo, tamanho);
    		campo = excluiCaracteresExcedentes(campo, tamanho);
    		return retornaCampoNumericoComZeroAhEsquerda(campo, tamanho);
    	}
    },
    
    PRD_EQUIPE_SEQ {
    	public String preencheCaracteresRestantes(String campo) {
			Integer tamanho = 8;
			campo = retornaStringVaziaQuandoValorEhNulo(campo);
    		if(campo.isEmpty())
    			return retornaCampoComEspacosAhDireita(campo, tamanho);
    		campo = excluiCaracteresExcedentes(campo, tamanho);
    		return retornaCampoNumericoComZeroAhEsquerda(campo, tamanho);
    	}    	
    },
    
    PRD_EQUIPE_AREA {
    	public String preencheCaracteresRestantes(String campo) {
			Integer tamanho = 4;
			campo = retornaStringVaziaQuandoValorEhNulo(campo);
    		if(campo.isEmpty())
    			return retornaCampoComEspacosAhDireita(campo, tamanho);
    		campo = excluiCaracteresExcedentes(campo, tamanho);
    		return retornaCampoNumericoComZeroAhEsquerda(campo, tamanho);
    	}    	
    },
    
    PRD_CNPJ {
    	public String preencheCaracteresRestantes(String campo) {
    		Integer tamanho = 14;
    		campo = retornaStringVaziaQuandoValorEhNulo(campo);
    		if(campo.isEmpty())
    			return retornaCampoComEspacosAhDireita(campo, tamanho);
    		campo = excluiCaracteresExcedentes(campo, tamanho);
    		return retornaCampoNumericoComZeroAhEsquerda(campo, tamanho);
    	}
    },
    
    PRD_CEP_PCNTE {
    	public String preencheCaracteresRestantes(String campo) {
    		Integer tamanho = 8;
    		campo = retornaStringVaziaQuandoValorEhNulo(campo);
    		if(campo.isEmpty())
    			return retornaCampoComEspacosAhDireita(campo, tamanho);
    		campo = excluiCaracteresExcedentes(campo, tamanho);
    		return retornaCampoNumericoComZeroAhEsquerda(campo, tamanho);
    	}    	
    },
    
    PRD_LOGRAD_PCNTE {
    	public String preencheCaracteresRestantes(String campo) {
    		Integer tamanho = 3;
    		campo = retornaStringVaziaQuandoValorEhNulo(campo);
    		if(campo.isEmpty())
    			return retornaCampoComEspacosAhDireita(campo, tamanho);
    		campo = excluiCaracteresExcedentes(campo, tamanho);
    		return retornaCampoNumericoComZeroAhEsquerda(campo, tamanho);
    	}    	
    },
    
    PRD_END_PCNTE {
    	public String preencheCaracteresRestantes(String campo) {
    		Integer tamanho = 30;
    		campo = retornaStringVaziaQuandoValorEhNulo(campo);
    		campo = excluiCaracteresExcedentes(campo, tamanho);
    		return retornaCampoComEspacosAhDireita(campo, tamanho);
    	}    	
    },
    
    PRD_COMPL_PCNTE {
    	public String preencheCaracteresRestantes(String campo) {
    		Integer tamanho = 10;
    		campo = retornaStringVaziaQuandoValorEhNulo(campo);
    		campo = excluiCaracteresExcedentes(campo, tamanho);
    		return retornaCampoComEspacosAhDireita(campo, tamanho);
    	}    	
    },
    
    PRD_NUM_PCNTE {
    	public String preencheCaracteresRestantes(String campo) {
    		Integer tamanho = 5;
    		campo = retornaStringVaziaQuandoValorEhNulo(campo);
    		if(campo.isEmpty())
    			return SN;
    		campo = excluiCaracteresExcedentes(campo, tamanho);
    		return retornaCampoComEspacosAhDireita(campo, tamanho);
    	}    	
    },
    
    PRD_BAIRRO_PCNTE {
    	public String preencheCaracteresRestantes(String campo) {
    		Integer tamanho = 30;
    		campo = retornaStringVaziaQuandoValorEhNulo(campo);
    		campo = excluiCaracteresExcedentes(campo, tamanho);
    		return retornaCampoComEspacosAhDireita(campo, tamanho);
    	}
    },
    
    PRD_DDTEL_PCNTE {
    	public String preencheCaracteresRestantes(String campo) {
    		Integer tamanho = 11;
    		campo = retornaStringVaziaQuandoValorEhNulo(campo);
    		if(campo.isEmpty())
    			return retornaCampoComEspacosAhDireita(campo, tamanho);
    		campo = excluiCaracteresExcedentes(campo, tamanho);
    		return retornaCampoNumericoComZeroAhEsquerda(campo, tamanho);
    	}    	
    },
    
    PRD_EMAIL_PCNTE {
    	public String preencheCaracteresRestantes(String campo) {
    		Integer tamanho = 40;
    		campo = retornaStringVaziaQuandoValorEhNulo(campo);
    		campo = excluiCaracteresExcedentes(campo, tamanho);
    		return retornaCampoComEspacosAhDireita(campo, tamanho);
    	}    	
    },
    
 	PRD_INE {    	
    	public String preencheCaracteresRestantes(String campo) {
    		Integer tamanho = 10;
    		campo = retornaStringVaziaQuandoValorEhNulo(campo);
   			if(campo.isEmpty())
   				return retornaCampoComEspacosAhDireita(campo, tamanho);
   			campo = excluiCaracteresExcedentes(campo, tamanho);
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
	
	protected String excluiCaracteresExcedentes(String campo, Integer tamanho) {
		if(campo.length() > tamanho) {
			campo = campo.substring(0, tamanho);
			return campo;
		}
		return campo;
	}
}
