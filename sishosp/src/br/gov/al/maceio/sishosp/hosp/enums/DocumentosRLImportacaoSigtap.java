package br.gov.al.maceio.sishosp.hosp.enums;

import br.gov.al.maceio.sishosp.hosp.interfaces.IDocumentosRLSigtap;
import br.gov.al.maceio.sishosp.hosp.model.dto.RelacaoObjetoComProcedimentoDTO;

public enum DocumentosRLImportacaoSigtap implements IDocumentosRLSigtap{
	
	RL_PROCEDIMENTO_CID("rl_procedimento_cid.txt"){
		public RelacaoObjetoComProcedimentoDTO retornarCodigoObjeto(String linhaDocumento){
			Integer fimDaSubstring = 14;
			String codigoProcedimento = retornarCodigoProcedimento(linhaDocumento);
			String codigoCid = retornaCodigoFormatadoParaTabela(linhaDocumento, FINAL_SUBSTRING_PROCEDIMENTO, fimDaSubstring);
			return new RelacaoObjetoComProcedimentoDTO(codigoProcedimento, codigoCid);
		}
	},
	RL_PROCEDIMENTO_MODALIDADE("rl_procedimento_modalidade.txt"){
		public RelacaoObjetoComProcedimentoDTO retornarCodigoObjeto(String linhaDocumento){
			Integer fimDaSubstring = 12;
			String codigoProcedimento = retornarCodigoProcedimento(linhaDocumento);
			String codigoModalidade = retornaCodigoFormatadoParaTabela(linhaDocumento, FINAL_SUBSTRING_PROCEDIMENTO, fimDaSubstring);
			return new RelacaoObjetoComProcedimentoDTO(codigoProcedimento, codigoModalidade);
		}
	},
	RL_PROCEDIMENTO_CBO("rl_procedimento_ocupacao.txt"){
		public RelacaoObjetoComProcedimentoDTO retornarCodigoObjeto(String linhaDocumento){
			Integer fimDaSubstring = 16;
			String codigoProcedimento = retornarCodigoProcedimento(linhaDocumento);
			String codigoOcupacao = retornaCodigoFormatadoParaTabela(linhaDocumento, FINAL_SUBSTRING_PROCEDIMENTO, fimDaSubstring);
			return new RelacaoObjetoComProcedimentoDTO(codigoProcedimento, codigoOcupacao);
		} 	
	},
	RL_PROCEDIMENTO_INSTRUMENTO_REGISTRO("rl_procedimento_registro.txt"){
		public RelacaoObjetoComProcedimentoDTO retornarCodigoObjeto(String linhaDocumento){
			Integer fimDaSubstring = 12;
			String codigoProcedimento = retornarCodigoProcedimento(linhaDocumento);
			String codigoRegistro = retornaCodigoFormatadoParaTabela(linhaDocumento, FINAL_SUBSTRING_PROCEDIMENTO, fimDaSubstring);
			return new RelacaoObjetoComProcedimentoDTO(codigoProcedimento, codigoRegistro);			
		}
	},
	RL_PROCEDIMENTO_RENASES("rl_procedimento_renases.txt"){
		public RelacaoObjetoComProcedimentoDTO retornarCodigoObjeto(String linhaDocumento){
			Integer fimDaSubstring = 20;
			String codigoProcedimento = retornarCodigoProcedimento(linhaDocumento);
			String codigoRenases = retornaCodigoFormatadoParaTabela(linhaDocumento, FINAL_SUBSTRING_PROCEDIMENTO, fimDaSubstring).trim();
			return new RelacaoObjetoComProcedimentoDTO(codigoProcedimento, codigoRenases);
		}
	},	
	RL_PROCEDIMENTO_SERVICO("rl_procedimento_servico.txt"){
		public RelacaoObjetoComProcedimentoDTO retornarCodigoObjeto(String linhaDocumento){
			Integer fimDaSubstring = 13;
			String codigoProcedimento = retornarCodigoProcedimento(linhaDocumento);
			String codigoServico = retornaCodigoFormatadoParaTabela(linhaDocumento, FINAL_SUBSTRING_PROCEDIMENTO, fimDaSubstring);
			String codigoClassificacao = retornarCodigoClassificaca(linhaDocumento);
			return new RelacaoObjetoComProcedimentoDTO(codigoProcedimento, codigoServico, codigoClassificacao);
		}
		
		private String retornarCodigoClassificaca(String linhaDocumento){
			Integer inicioDaSubstring = 13;
			Integer fimDaSubstring = 16;
			return retornaCodigoFormatadoParaTabela(linhaDocumento, inicioDaSubstring, fimDaSubstring);
		}
	};

    private String sigla;
    private static final Integer INICIO_SUBSTRING_PROCEDIMENTO = 0;
	private static final Integer FINAL_SUBSTRING_PROCEDIMENTO = 10;
	
    DocumentosRLImportacaoSigtap(String sigla) {
        this.sigla = sigla;
    }
    
	protected String retornarCodigoProcedimento(String linhaDocumento){
		return retornaCodigoFormatadoParaTabela(linhaDocumento, INICIO_SUBSTRING_PROCEDIMENTO, FINAL_SUBSTRING_PROCEDIMENTO);
	}
    
    protected String retornaCodigoFormatadoParaTabela(String linhaDoDocumento, Integer caracterInicial, Integer caracterFinal) {
    	return linhaDoDocumento.substring(caracterInicial, caracterFinal); 
    }

    public String getSigla() {
        return sigla;
    }
}
