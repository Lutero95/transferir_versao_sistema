package br.gov.al.maceio.sishosp.hosp.model.dto;

import br.gov.saude.servicos.schema.sigtap.procedimento.v1.modalidadeatendimento.ModalidadeAtendimentoType;

public class ModalidadeAtendimentoDTO {
	private Long id;
	private ModalidadeAtendimentoType modalidadeAtendimento;
	
	public ModalidadeAtendimentoDTO() {
		this.modalidadeAtendimento = new ModalidadeAtendimentoType();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ModalidadeAtendimentoType getModalidadeAtendimento() {
		return modalidadeAtendimento;
	}

	public void setModalidadeAtendimento(ModalidadeAtendimentoType modalidadeAtendimento) {
		this.modalidadeAtendimento = modalidadeAtendimento;
	}
}
