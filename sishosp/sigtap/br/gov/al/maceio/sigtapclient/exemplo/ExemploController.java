package br.gov.al.maceio.sigtapclient.exemplo;

import java.math.BigInteger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.xml.bind.JAXBException;

import br.gov.al.maceio.sigtapclient.util.ProcedimentoUtil;
import br.gov.saude.servicos.schema.cbo.v1.cbo.CBOType;
import br.gov.saude.servicos.schema.sigtap.procedimento.cid.v1.cid.CIDType;
import br.gov.saude.servicos.schema.sigtap.procedimento.servicoclassificacao.v1.servicoclassificacao.ServicoClassificacaoType;
import br.gov.saude.servicos.schema.sigtap.procedimento.v1.procedimento.BaseProcedimentoType;
import br.gov.saude.servicos.schema.sigtap.procedimento.v1.procedimento.ProcedimentoType.CIDsVinculados;
import br.gov.saude.servicos.schema.sigtap.procedimento.v1.procedimento.ProcedimentoType.CIDsVinculados.CIDVinculado;
import br.gov.saude.servicos.sigtap.v1.procedimentoservice.RequestDetalharProcedimento;
import br.gov.saude.servicos.sigtap.v1.procedimentoservice.RequestDetalharProcedimento.DetalhesAdicionais;
import br.gov.saude.servicos.sigtap.v1.procedimentoservice.RequestPesquisarProcedimentos;
import br.gov.saude.servicos.sigtap.v1.procedimentoservice.SIGTAPFault;
import br.gov.saude.servicos.wsdl.mensageria.sigtap.v1.detalheadicional.CategoriaDetalheAdicionalType;
import br.gov.saude.servicos.wsdl.mensageria.sigtap.v1.detalheadicional.DetalheAdicionalType;
import br.gov.saude.servicos.wsdl.mensageria.v1.paginacao.PaginacaoType;
import br.gov.saude.servicos.wsdl.mensageria.v1r0.resultadosdetalhaprocedimentos.ResultadosDetalhaProcedimentosType;
import br.gov.saude.servicos.wsdl.mensageria.v1r0.resultadospesquisaprocedimentos.ResultadosPesquisaProcedimentosType;

@ManagedBean
@ViewScoped
public class ExemploController {
	
	public static void main(String[] args) throws JAXBException {
		pesquisarProcedimentosExemplo();
		System.out.println("---------------------------------------------------------\n\n\n");
		detalharProcedimento();
	}
	
	public void testarRequestsProcedimentos() {
		pesquisarProcedimentosExemplo();
		//detalharProcedimento();
	}

	private static void pesquisarProcedimentosExemplo() {
		RequestPesquisarProcedimentos requestPesquisarProcedimentos = new RequestPesquisarProcedimentos();
		requestPesquisarProcedimentos.setCodigoGrupo("05");     
		requestPesquisarProcedimentos.setCodigoSubgrupo("04");  
		requestPesquisarProcedimentos.setCompetencia("201501"); 
		
		PaginacaoType paginacaoType = new PaginacaoType();
		paginacaoType.setRegistroInicial(new BigInteger("1")); 
		paginacaoType.setQuantidadeRegistros(20);
		paginacaoType.setTotalRegistros(new BigInteger("200"));
		requestPesquisarProcedimentos.setPaginacao(paginacaoType);
		
		try {
			ResultadosPesquisaProcedimentosType resultadosPesquisaProcedimentosType = 
					ProcedimentoUtil.pesquisarProcedimentos(requestPesquisarProcedimentos);
			for(BaseProcedimentoType baseProcedimento : resultadosPesquisaProcedimentosType.getBaseProcedimento()) {
				System.out.println("BASE DE PROCEDIMENTO: "+baseProcedimento.getNome());
			}
			System.out.println("QUANTIDADE DE REGISTROS: "+resultadosPesquisaProcedimentosType.getPaginacao().getQuantidadeRegistros());
		} catch (SIGTAPFault e) {
			e.printStackTrace();
		}
	}
	
	private static void detalharProcedimento() {
		RequestDetalharProcedimento requestDetalharProcedimento = new RequestDetalharProcedimento();
		requestDetalharProcedimento.setCodigoProcedimento("0301070075");
		
		DetalhesAdicionais detalhesAdicionais = new DetalhesAdicionais();
		
		DetalheAdicionalType detalheAdicionalType = new DetalheAdicionalType();
		detalheAdicionalType.setCategoriaDetalheAdicional(CategoriaDetalheAdicionalType.CIDS);
		
		PaginacaoType paginacaoType = new PaginacaoType();
		paginacaoType.setRegistroInicial(new BigInteger("1"));
		paginacaoType.setQuantidadeRegistros(20);
		paginacaoType.setTotalRegistros(new BigInteger("0")); //PARAMETRO NAO USADO
		
		detalheAdicionalType.setPaginacao(paginacaoType);
		
		detalhesAdicionais.getDetalheAdicional().add(detalheAdicionalType); 
		
		requestDetalharProcedimento.setDetalhesAdicionais(detalhesAdicionais);
		
		try {
			ResultadosDetalhaProcedimentosType resultadosDetalhaProcedimentosType = 
					ProcedimentoUtil.detalharProcedimentos(requestDetalharProcedimento);
			
			System.out.println("NOME: "+ resultadosDetalhaProcedimentosType.getProcedimento().getNome());
			//System.out.println("DESCRICAO: "+ resultadosDetalhaProcedimentosType.getProcedimento().getDescricao());
			System.out.println("CODIGO: "+ resultadosDetalhaProcedimentosType.getProcedimento().getCodigo());
			System.out.println("SEXO PERMITIDO: "+ resultadosDetalhaProcedimentosType.getProcedimento().getSexoPermitido());
			System.out.println("IDADE MINIMA: "+ resultadosDetalhaProcedimentosType.getProcedimento().getIdadeMinimaPermitida().getQuantidadeLimite());
			System.out.println("IDADE MAXIMA: "+ resultadosDetalhaProcedimentosType.getProcedimento().getIdadeMaximaPermitida().getQuantidadeLimite());
//			for(CBOType CBO : resultadosDetalhaProcedimentosType.getProcedimento().getCBOsVinculados().getCBO()) {
//				System.out.println("CBO: "+CBO.getNome());
//				System.out.println("CBO CODIGO: "+CBO.getCodigo());
//			}
//			for(CIDVinculado CID : resultadosDetalhaProcedimentosType.getProcedimento().getCIDsVinculados().getCIDVinculado()) {
//				System.out.println("CID: "+CID.getCID().getNome());
//				System.out.println("CID CODIGO: "+CID.getCID().getCodigo());
//			}
			for(ServicoClassificacaoType servico : resultadosDetalhaProcedimentosType.getProcedimento().getServicosClassificacoesVinculados().getServicoClassificacao()) {
				System.out.println("NOME DO SERVICO: " +servico.getServico().getNome());
				System.out.println("CODIGO: "+servico.getServico().getCodigo());
				System.out.println("NOME DA CLASSIFICACAO: "+servico.getNomeClassificacao());
				System.out.println("CODIGO DA CLASSIFICACAO: "+servico.getCodigoClassificacao());
			}
			System.out.println(resultadosDetalhaProcedimentosType.getDetalheAdicional().get(0).getPaginacao().getQuantidadeRegistros());
		} catch (SIGTAPFault e) {
			e.printStackTrace();
		}
	}

}
