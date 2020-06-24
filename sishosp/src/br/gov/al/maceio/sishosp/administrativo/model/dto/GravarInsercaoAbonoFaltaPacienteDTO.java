package br.gov.al.maceio.sishosp.administrativo.model.dto;

import java.util.List;

import br.gov.al.maceio.sishosp.hosp.model.AtendimentoBean;

public class GravarInsercaoAbonoFaltaPacienteDTO {
	
	private List<AtendimentoBean> atendimentosParaAbono;
	private Long idUsuarioOperacao;
	private String justificativa;
	
	
	public GravarInsercaoAbonoFaltaPacienteDTO
		(List<AtendimentoBean> atendimentosParaAbono, Long idUsuarioOperacao, String justificativa) {
		
		this.atendimentosParaAbono = atendimentosParaAbono;
		this.idUsuarioOperacao = idUsuarioOperacao;
		this.justificativa = justificativa;
	}
	
	public List<AtendimentoBean> getAtendimentosParaAbono() {
		return atendimentosParaAbono;
	}
	public void setAtendimentosParaAbono(List<AtendimentoBean> atendimentosParaAbono) {
		this.atendimentosParaAbono = atendimentosParaAbono;
	}
	public Long getIdUsuarioOperacao() {
		return idUsuarioOperacao;
	}
	public void setIdUsuarioOperacao(Long idUsuarioOperacao) {
		this.idUsuarioOperacao = idUsuarioOperacao;
	}
	public String getJustificativa() {
		return justificativa;
	}
	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((atendimentosParaAbono == null) ? 0 : atendimentosParaAbono.hashCode());
		result = prime * result + ((idUsuarioOperacao == null) ? 0 : idUsuarioOperacao.hashCode());
		result = prime * result + ((justificativa == null) ? 0 : justificativa.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GravarInsercaoAbonoFaltaPacienteDTO other = (GravarInsercaoAbonoFaltaPacienteDTO) obj;
		if (atendimentosParaAbono == null) {
			if (other.atendimentosParaAbono != null)
				return false;
		} else if (!atendimentosParaAbono.equals(other.atendimentosParaAbono))
			return false;
		if (idUsuarioOperacao == null) {
			if (other.idUsuarioOperacao != null)
				return false;
		} else if (!idUsuarioOperacao.equals(other.idUsuarioOperacao))
			return false;
		if (justificativa == null) {
			if (other.justificativa != null)
				return false;
		} else if (!justificativa.equals(other.justificativa))
			return false;
		return true;
	}
	
}
