package br.gov.al.maceio.sishosp.hosp.interfaces;

import br.gov.al.maceio.sishosp.hosp.model.dto.RelacaoObjetoComProcedimentoDTO;

public interface IDocumentosRLSigtap {

	public RelacaoObjetoComProcedimentoDTO retornarCodigoObjeto(String linhaDocumento);
}
