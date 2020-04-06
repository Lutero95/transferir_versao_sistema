package br.gov.al.maceio.sishosp.hosp.model.dto;

import java.util.ArrayList;
import java.util.List;

import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.v1.procedimento.ProcedimentoType;

public class GravarProcedimentoMensalDTO {
	private Long idProcedimentoMensal;
	private Integer idProcedimento;
	private ProcedimentoType procedimentoMensal;
	//DADOS EXISTENTES NO BANCO
    private List<Integer> listaIdModalidadeAtendimentoExistente;
    private List<Integer> listaIdInstrumentosRegistroExistente;
    private List<Integer> listaIdCBOsExistente;
    private List<Integer> listaIdCidsExistente;
    private List<Integer> listaIdRenasesExistente;
    private Integer idFormasDeOrganizacaoExistente;
    private Integer idTipoFinanciamentoExistente;
    
    public GravarProcedimentoMensalDTO() {
    	this.listaIdModalidadeAtendimentoExistente = new ArrayList();
    	this.listaIdInstrumentosRegistroExistente = new ArrayList();
    	this.listaIdCBOsExistente = new ArrayList();
    	this.listaIdCidsExistente = new ArrayList();
    	this.listaIdRenasesExistente = new ArrayList();
    }
	
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
	public List<Integer> getListaIdModalidadeAtendimentoExistente() {
		return listaIdModalidadeAtendimentoExistente;
	}
	public void setListaIdModalidadeAtendimentoExistente(List<Integer> listaIdModalidadeAtendimentoExistente) {
		this.listaIdModalidadeAtendimentoExistente = listaIdModalidadeAtendimentoExistente;
	}
	public List<Integer> getListaIdInstrumentosRegistroExistente() {
		return listaIdInstrumentosRegistroExistente;
	}
	public void setListaIdInstrumentosRegistroExistente(List<Integer> listaIdInstrumentosRegistroExistente) {
		this.listaIdInstrumentosRegistroExistente = listaIdInstrumentosRegistroExistente;
	}
	public List<Integer> getListaIdCBOsExistente() {
		return listaIdCBOsExistente;
	}
	public void setListaIdCBOsExistente(List<Integer> listaIdCBOsExistente) {
		this.listaIdCBOsExistente = listaIdCBOsExistente;
	}
	public List<Integer> getListaIdCidsExistente() {
		return listaIdCidsExistente;
	}
	public void setListaIdCidsExistente(List<Integer> listaIdCidsExistente) {
		this.listaIdCidsExistente = listaIdCidsExistente;
	}
	public List<Integer> getListaIdRenasesExistente() {
		return listaIdRenasesExistente;
	}
	public void setListaIdRenasesExistente(List<Integer> listaIdRenasesExistente) {
		this.listaIdRenasesExistente = listaIdRenasesExistente;
	}
	public Integer getIdFormasDeOrganizacaoExistente() {
		return idFormasDeOrganizacaoExistente;
	}
	public void setIdFormasDeOrganizacaoExistente(Integer idFormasDeOrganizacaoExistente) {
		this.idFormasDeOrganizacaoExistente = idFormasDeOrganizacaoExistente;
	}
	public Integer getIdTipoFinanciamentoExistente() {
		return idTipoFinanciamentoExistente;
	}
	public void setIdTipoFinanciamentoExistente(Integer idTipoFinanciamentoExistente) {
		this.idTipoFinanciamentoExistente = idTipoFinanciamentoExistente;
	}	    
}
