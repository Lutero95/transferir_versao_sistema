package sigtap.br.gov.al.maceio.sigtapclient.util;

import sigtap.br.gov.saude.servicos.sigtap.v1.procedimentoservice.ProcedimentoService;
import sigtap.br.gov.saude.servicos.sigtap.v1.procedimentoservice.ProcedimentoServicePortType;
import sigtap.br.gov.saude.servicos.sigtap.v1.procedimentoservice.RequestDetalharProcedimento;
import sigtap.br.gov.saude.servicos.sigtap.v1.procedimentoservice.RequestPesquisarProcedimentos;
import sigtap.br.gov.saude.servicos.sigtap.v1.procedimentoservice.ResponseDetalharProcedimento;
import sigtap.br.gov.saude.servicos.sigtap.v1.procedimentoservice.ResponsePesquisarProcedimentos;
import sigtap.br.gov.saude.servicos.sigtap.v1.procedimentoservice.SIGTAPFault;
import sigtap.br.gov.saude.servicos.wsdl.mensageria.v1r0.resultadosdetalhaprocedimentos.ResultadosDetalhaProcedimentosType;
import sigtap.br.gov.saude.servicos.wsdl.mensageria.v1r0.resultadospesquisaprocedimentos.ResultadosPesquisaProcedimentosType;

public class ProcedimentoUtil {
	
	public static ResultadosPesquisaProcedimentosType pesquisarProcedimentos
		(RequestPesquisarProcedimentos requestPesquisarProcedimentos) throws SIGTAPFault{
		
		ProcedimentoService procedimentoService = new ProcedimentoService();
		ProcedimentoServicePortType procedimentoServicePortType = procedimentoService.getProcedimentoServicePort();
		
		HeaderUtil.montaSecurityHeader(procedimentoServicePortType);
		
		ResponsePesquisarProcedimentos responsePesquisarProcedimentos = procedimentoServicePortType.pesquisarProcedimentos(requestPesquisarProcedimentos);
		ResultadosPesquisaProcedimentosType resultadosPesquisaProcedimentosType = responsePesquisarProcedimentos.getResultadosPesquisaProcedimentos();
		return resultadosPesquisaProcedimentosType;
	}
	
	public static ResultadosDetalhaProcedimentosType detalharProcedimentos
	(RequestDetalharProcedimento requestDetalharProcedimento) throws SIGTAPFault{
		ProcedimentoService procedimentoService = new ProcedimentoService();
		ProcedimentoServicePortType procedimentoServicePortType = procedimentoService.getProcedimentoServicePort();
		
		HeaderUtil.montaSecurityHeader(procedimentoServicePortType);
		
		ResponseDetalharProcedimento responseDetalharProcedimento = procedimentoServicePortType.detalharProcedimento(requestDetalharProcedimento);
		ResultadosDetalhaProcedimentosType resultadosDetalhaProcedimentosType = responseDetalharProcedimento.getResultadosDetalhaProcedimentos();
		return resultadosDetalhaProcedimentosType;
	}
}
