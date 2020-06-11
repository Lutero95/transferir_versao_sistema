package br.gov.al.maceio.sishosp.hosp.enums;

import java.math.BigDecimal;

import br.gov.al.maceio.sishosp.hosp.interfaces.IDocumentosTBSigtap;
import br.gov.al.maceio.sishosp.hosp.model.dto.DescricaoProcedimentoDTO;
import sigtap.br.gov.arquivo.enums.TipoComplexidade;
import sigtap.br.gov.saude.servicos.schema.cbo.v1.cbo.CBOType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.cid.v1.cid.CIDType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.financiamento.v1.tipofinanciamento.TipoFinanciamentoType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.nivelagregacao.v1.formaorganizacao.FormaOrganizacaoType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.nivelagregacao.v1.grupo.GrupoType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.nivelagregacao.v1.subgrupo.SubgrupoType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.renases.v1.renases.RENASESType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.servicoclassificacao.v1.servico.ServicoType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.servicoclassificacao.v1.servicoclassificacao.ServicoClassificacaoType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.v1.complexidade.ComplexidadeType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.v1.instrumentoregistro.InstrumentoRegistroType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.v1.modalidadeatendimento.ModalidadeAtendimentoType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.v1.procedimento.ProcedimentoType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.v1.procedimento.ProcedimentoType.CIDsVinculados.CIDVinculado;
import sigtap.br.gov.saude.servicos.schema.sigtap.v1.idadelimite.IdadeLimiteType;

public enum DocumentosTBImportacaoSigtap implements IDocumentosTBSigtap{
	

	TB_CID("tb_cid.txt"){
		public Object retornarObjectoDaString(String linhaDocumento) {
			CIDVinculado cidVinculado = new CIDVinculado();
			CIDType cid = new CIDType();
			cid.setCodigo(retornaCampoFormatadoParaTabela(linhaDocumento, 0, 4));
			cid.setNome(retornaCampoFormatadoParaTabela(linhaDocumento, 4, 104).trim());
			cidVinculado.setCID(cid);
			return cidVinculado;
		}
	},
	TB_DESCRICAO("tb_descricao.txt"){
		public Object retornarObjectoDaString(String linhaDocumento) {
			DescricaoProcedimentoDTO descricaoProcedimento = new DescricaoProcedimentoDTO();
			descricaoProcedimento.setCodigoProcedimento(retornaCampoFormatadoParaTabela
					(linhaDocumento, INICIO_SUBSTRING_PROCEDIMENTO, FINAL_SUBSTRING_PROCEDIMENTO));
			descricaoProcedimento.setDescricao(retornaCampoFormatadoParaTabela(linhaDocumento, 4, 104).trim());
			return descricaoProcedimento;
		}
	},
	TB_FINANCIAMENTO("tb_financiamento.txt"){
		public Object retornarObjectoDaString(String linhaDocumento) {
			TipoFinanciamentoType tipoFinanciamento = new TipoFinanciamentoType();
			tipoFinanciamento.setCodigo(retornaCampoFormatadoParaTabela(linhaDocumento, 0, 2));
			tipoFinanciamento.setNome(retornaCampoFormatadoParaTabela(linhaDocumento, 2, 102).trim());
			return tipoFinanciamento;
		}
	},
	TB_FORMA_ORGANIZACAO("tb_forma_organizacao.txt"){
		public Object retornarObjectoDaString(String linhaDocumento) {
			
			GrupoType grupo = new GrupoType();
			grupo.setCodigo(retornaCampoFormatadoParaTabela(linhaDocumento, 0, 2));
			
			SubgrupoType subgrupo = new SubgrupoType();
			subgrupo.setGrupo(grupo);
			subgrupo.setCodigo(retornaCampoFormatadoParaTabela(linhaDocumento, 2, 4));
			
			FormaOrganizacaoType formaOrganizacao = new FormaOrganizacaoType();
			formaOrganizacao.setSubgrupo(subgrupo);
			formaOrganizacao.setCodigo(retornaCampoFormatadoParaTabela(linhaDocumento, 4, 6));
			formaOrganizacao.setNome(retornaCampoFormatadoParaTabela(linhaDocumento, 6, 106).trim());
			return formaOrganizacao;
		}
	},
	TB_GRUPO("tb_grupo.txt"){
		public Object retornarObjectoDaString(String linhaDocumento) {
			GrupoType grupo = new GrupoType();
			grupo.setCodigo(retornaCampoFormatadoParaTabela(linhaDocumento, 0, 2));
			grupo.setNome(retornaCampoFormatadoParaTabela(linhaDocumento, 2, 102).trim());
			return grupo;
		}
	},
	TB_MODALIDADE("tb_modalidade.txt"){
		public Object retornarObjectoDaString(String linhaDocumento) {
			ModalidadeAtendimentoType modalidadeAtendimento = new ModalidadeAtendimentoType();
			modalidadeAtendimento.setCodigo(retornaCampoFormatadoParaTabela(linhaDocumento, 0, 2));
			modalidadeAtendimento.setNome(retornaCampoFormatadoParaTabela(linhaDocumento, 2, 102).trim());
			return modalidadeAtendimento;
		}
	},
	TB_CBO("tb_ocupacao.txt"){
		public Object retornarObjectoDaString(String linhaDocumento) {
			CBOType cbo = new CBOType();
			cbo.setCodigo(retornaCampoFormatadoParaTabela(linhaDocumento, 0, 6));
			cbo.setNome(retornaCampoFormatadoParaTabela(linhaDocumento, 6, 156).trim());
			return cbo;
		}
	},
	TB_PROCEDIMENTO("tb_procedimento.txt"){
		public Object retornarObjectoDaString(String linhaDocumento) {
			ProcedimentoType procedimento = new ProcedimentoType();
			procedimento.setCodigo(retornaCampoFormatadoParaTabela(linhaDocumento, 0, 10));
			
			procedimento.setNome(retornaCampoFormatadoParaTabela(linhaDocumento, 10, 260).trim());
			String tipoComplexidade = retornaCampoFormatadoParaTabela(linhaDocumento, 260, 261);
			if (tipoComplexidade.equals(TipoComplexidade.NAO_SE_APLICA.getTipoComplexidade()))
			procedimento.setComplexidade(ComplexidadeType.NAO_SE_APLICA);
			else
			if (tipoComplexidade.equals(TipoComplexidade.ATENCAO_BASICA_COMPLEXIDADE.getTipoComplexidade()))
				procedimento.setComplexidade(ComplexidadeType.ATENCAO_BASICA);
			else
			if (tipoComplexidade.equals(TipoComplexidade.MEDIA_COMPLEXIDADE.getTipoComplexidade()))
				procedimento.setComplexidade(ComplexidadeType.MEDIA_COMPLEXIDADE);
			else
			if (tipoComplexidade.equals(TipoComplexidade.ALTA_COMPLEXIDADE.getTipoComplexidade()))
				procedimento.setComplexidade(ComplexidadeType.ALTA_COMPLEXIDADE_OU_CUSTO);

			procedimento.setSexoPermitido(retornaCampoFormatadoParaTabela(linhaDocumento, 261, 262)); //*
			procedimento.setQuantidadeMaxima(Integer.valueOf(retornaCampoFormatadoParaTabela(linhaDocumento, 262, 266)));
			
			IdadeLimiteType idadeMinima = new IdadeLimiteType();
			idadeMinima.setQuantidadeLimite(Integer.valueOf(retornaCampoFormatadoParaTabela(linhaDocumento, 274, 278)));
			IdadeLimiteType idadeMaxima = new IdadeLimiteType();
			idadeMaxima.setQuantidadeLimite(Integer.valueOf(retornaCampoFormatadoParaTabela(linhaDocumento, 278, 282)));
			procedimento.setIdadeMinimaPermitida(idadeMinima);
			procedimento.setIdadeMaximaPermitida(idadeMaxima);
			
			Long valorSA = Long.valueOf(retornaCampoFormatadoParaTabela(linhaDocumento, 292, 302));
			procedimento.setValorSA(BigDecimal.valueOf(valorSA));
			Long valorSH = Long.valueOf(retornaCampoFormatadoParaTabela(linhaDocumento, 282, 292));
			procedimento.setValorSH(BigDecimal.valueOf(valorSH));
			Long valorSP = Long.valueOf(retornaCampoFormatadoParaTabela(linhaDocumento, 302, 312));
			procedimento.setValorSP(BigDecimal.valueOf(valorSP));
			
			TipoFinanciamentoType tipoFinanciamento = new TipoFinanciamentoType();
			tipoFinanciamento.setCodigo(retornaCampoFormatadoParaTabela(linhaDocumento, 312, 314));
			procedimento.setTipoFinanciamento(tipoFinanciamento);
			procedimento.setCompetenciaValidade(retornaCampoFormatadoParaTabela(linhaDocumento, 324, 330));
			
			FormaOrganizacaoType formaOrganizacao = new FormaOrganizacaoType();
			formaOrganizacao.setCodigo(retornaCampoFormatadoParaTabela(linhaDocumento, 4, 6));
			procedimento.setFormaOrganizacao(formaOrganizacao);
			
			return procedimento;
		}
	},
	TB_INSTRUMENTO_REGISTRO("tb_registro.txt"){
		public Object retornarObjectoDaString(String linhaDocumento) {
			InstrumentoRegistroType instrumentoRegistro = new InstrumentoRegistroType();
			instrumentoRegistro.setCodigo(retornaCampoFormatadoParaTabela(linhaDocumento, 0, 2));
			instrumentoRegistro.setNome(retornaCampoFormatadoParaTabela(linhaDocumento, 2, 52).trim());
			return instrumentoRegistro;
		}
	},
	TB_RENASES("tb_renases.txt"){
		public Object retornarObjectoDaString(String linhaDocumento) {
			RENASESType renases = new RENASESType();
			renases.setCodigo(retornaCampoFormatadoParaTabela(linhaDocumento, 0, 10).trim());
			renases.setNome(retornaCampoFormatadoParaTabela(linhaDocumento, 10, 160).trim());
			return renases;
		}
	}, 
	TB_SERVICO("tb_servico.txt"){
		public Object retornarObjectoDaString(String linhaDocumento) {
			ServicoType servico = new ServicoType();
			servico.setCodigo(retornaCampoFormatadoParaTabela(linhaDocumento, 0, 3));
			servico.setNome(retornaCampoFormatadoParaTabela(linhaDocumento, 3, 123).trim());
			return servico;
		}
	},
	TB_SERVICO_CLASSIFICACAO("tb_servico_classificacao.txt"){
		public Object retornarObjectoDaString(String linhaDocumento) {
			ServicoClassificacaoType servicoClassificacao = new ServicoClassificacaoType();
			ServicoType servico = new ServicoType();
			servico.setCodigo(retornaCampoFormatadoParaTabela(linhaDocumento, 0, 3));
			servicoClassificacao.setServico(servico);
			servicoClassificacao.setCodigoClassificacao(retornaCampoFormatadoParaTabela(linhaDocumento, 3, 6));
			servicoClassificacao.setNomeClassificacao(retornaCampoFormatadoParaTabela(linhaDocumento, 6, 156).trim());
			return servicoClassificacao;
		}		
	},
	TB_SUBGRUPO("tb_sub_grupo.txt"){
		public Object retornarObjectoDaString(String linhaDocumento) {
			SubgrupoType subgrupo = new SubgrupoType();
			subgrupo.setCodigo(retornaCampoFormatadoParaTabela(linhaDocumento, 0, 2));
			GrupoType grupo = new GrupoType();
			grupo.setCodigo(retornaCampoFormatadoParaTabela(linhaDocumento, 2, 4));
			subgrupo.setNome(retornaCampoFormatadoParaTabela(linhaDocumento, 4, 104).trim());
			return subgrupo;
		}
	};
	

    private String sigla;
    private static final Integer INICIO_SUBSTRING_PROCEDIMENTO = 0;
	private static final Integer FINAL_SUBSTRING_PROCEDIMENTO = 10;
	
    DocumentosTBImportacaoSigtap(String sigla) {
        this.sigla = sigla;
    }
    
    protected String retornaCampoFormatadoParaTabela(String linhaDoDocumento, Integer caracterInicial, Integer caracterFinal) {
    	String codigo = linhaDoDocumento.substring(caracterInicial, caracterFinal); 
    	return codigo;
    }

    public String getSigla() {
        return sigla;
    }
}
