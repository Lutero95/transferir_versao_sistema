package br.gov.al.maceio.sishosp.hosp.model.dto;

import br.gov.saude.servicos.schema.sigtap.procedimento.servicoclassificacao.v1.servicoclassificacao.ServicoClassificacaoType;

public class ServicoClassificacaoDTO {
	private Long idServicoClassificacao;
	private Long idServico;
	private Long idClassificacao;
	private ServicoClassificacaoType servicoClassificacao;
	
	public Long getIdServicoClassificacao() {
		return idServicoClassificacao;
	}

	public void setIdServicoClassificacao(Long idServicoClassificacao) {
		this.idServicoClassificacao = idServicoClassificacao;
	}

	public ServicoClassificacaoDTO() {
		this.servicoClassificacao = new ServicoClassificacaoType();
	}

	public Long getIdServico() {
		return idServico;
	}

	public void setIdServico(Long idServico) {
		this.idServico = idServico;
	}

	public Long getIdClassificacao() {
		return idClassificacao;
	}

	public void setIdClassificacao(Long idClassificacao) {
		this.idClassificacao = idClassificacao;
	}

	public ServicoClassificacaoType getServicoClassificacao() {
		return servicoClassificacao;
	}

	public void setServicoClassificacao(ServicoClassificacaoType servicoClassificacao) {
		this.servicoClassificacao = servicoClassificacao;
	}
}
