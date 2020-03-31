package br.gov.al.maceio.sishosp.hosp.model.dto;

import java.util.List;

import br.gov.saude.servicos.schema.cbo.v1.cbo.CBOType;
import br.gov.saude.servicos.schema.sigtap.procedimento.cid.v1.cid.CIDType;
import br.gov.saude.servicos.schema.sigtap.procedimento.servicoclassificacao.v1.servicoclassificacao.ServicoClassificacaoType;
import br.gov.saude.servicos.schema.sigtap.procedimento.v1.instrumentoregistro.InstrumentoRegistroType;
import br.gov.saude.servicos.schema.sigtap.procedimento.v1.modalidadeatendimento.ModalidadeAtendimentoType;
import br.gov.saude.servicos.schema.sigtap.procedimento.v1.procedimento.ProcedimentoType;

public class ProcedimentoMensalDTO {
	private Long idProcedimentoMensal;
	private Integer idProcedimento;
	private ProcedimentoType procedimentoMensal;
    private List<ModalidadeAtendimentoType> listaModalidadeAtendimento;
    private List<InstrumentoRegistroType> listaInstrumentosRegistro;
    private List<CBOType> listaCBOs;
    private List<CIDType> listaCids;
    private List<ServicoClassificacaoType> listaServicoClassificacao;
	
	public Long getIdProcedimentoMensal() {
		return idProcedimentoMensal;
	}
	public void setIdProcedimentoMensal(Long idProcedimentoMensal) {
		this.idProcedimentoMensal = idProcedimentoMensal;
	}
	public Integer getIdProcedimento() {
		return idProcedimento;
	}
	public void setIdProcedimento(Integer idProcedimento) {
		this.idProcedimento = idProcedimento;
	}
	public ProcedimentoType getProcedimentoMensal() {
		return procedimentoMensal;
	}
	public void setProcedimentoMensal(ProcedimentoType procedimentoMensal) {
		this.procedimentoMensal = procedimentoMensal;
	}
	public List<ModalidadeAtendimentoType> getListaModalidadeAtendimento() {
		return listaModalidadeAtendimento;
	}
	public void setListaModalidadeAtendimento(List<ModalidadeAtendimentoType> listaModalidadeAtendimento) {
		this.listaModalidadeAtendimento = listaModalidadeAtendimento;
	}
	public List<InstrumentoRegistroType> getListaInstrumentosRegistro() {
		return listaInstrumentosRegistro;
	}
	public void setListaInstrumentosRegistro(List<InstrumentoRegistroType> listaInstrumentosRegistro) {
		this.listaInstrumentosRegistro = listaInstrumentosRegistro;
	}
	public List<CBOType> getListaCBOs() {
		return listaCBOs;
	}
	public void setListaCBOs(List<CBOType> listaCBOs) {
		this.listaCBOs = listaCBOs;
	}
	public List<CIDType> getListaCids() {
		return listaCids;
	}
	public void setListaCids(List<CIDType> listaCids) {
		this.listaCids = listaCids;
	}
	public List<ServicoClassificacaoType> getListaServicoClassificacao() {
		return listaServicoClassificacao;
	}
	public void setListaServicoClassificacao(List<ServicoClassificacaoType> listaServicoClassificacao) {
		this.listaServicoClassificacao = listaServicoClassificacao;
	}
	    
}
