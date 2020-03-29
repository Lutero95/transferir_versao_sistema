package br.gov.al.maceio.sishosp.hosp.model.dto;

import java.util.List;

import br.gov.saude.servicos.schema.sigtap.procedimento.v1.procedimento.ProcedimentoType;

public class ProcedimentoMensalDTO {
	private Long idProcedimentoMensal;
	private Integer idProcedimento;
	private ProcedimentoType procedimentoMensal;
    private List<ModalidadeAtendimentoDTO> listaModalidadeAtendimento;
    private List<InstrumentoRegistroDTO> listaInstrumentosRegistro;
    private List<CboDTO> listaCBOs;
    private List<CidDTO> listaCids;
    private List<ServicoClassificacaoDTO> listaServicoClassificacao;
	
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
	public List<ModalidadeAtendimentoDTO> getListaModalidadeAtendimento() {
		return listaModalidadeAtendimento;
	}
	public void setListaModalidadeAtendimento(List<ModalidadeAtendimentoDTO> listaModalidadeAtendimento) {
		this.listaModalidadeAtendimento = listaModalidadeAtendimento;
	}
	public List<InstrumentoRegistroDTO> getListaInstrumentosRegistro() {
		return listaInstrumentosRegistro;
	}
	public void setListaInstrumentosRegistro(List<InstrumentoRegistroDTO> listaInstrumentosRegistro) {
		this.listaInstrumentosRegistro = listaInstrumentosRegistro;
	}
	public List<CboDTO> getListaCBOs() {
		return listaCBOs;
	}
	public void setListaCBOs(List<CboDTO> listaCBOs) {
		this.listaCBOs = listaCBOs;
	}
	public List<CidDTO> getListaCids() {
		return listaCids;
	}
	public void setListaCids(List<CidDTO> listaCids) {
		this.listaCids = listaCids;
	}
	public List<ServicoClassificacaoDTO> getListaServicoClassificacao() {
		return listaServicoClassificacao;
	}
	public void setListaServicoClassificacao(List<ServicoClassificacaoDTO> listaServicoClassificacao) {
		this.listaServicoClassificacao = listaServicoClassificacao;
	}
    
}
