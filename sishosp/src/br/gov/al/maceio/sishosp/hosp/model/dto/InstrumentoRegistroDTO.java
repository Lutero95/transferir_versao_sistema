package br.gov.al.maceio.sishosp.hosp.model.dto;

import br.gov.saude.servicos.schema.sigtap.procedimento.v1.instrumentoregistro.InstrumentoRegistroType;

public class InstrumentoRegistroDTO {

	private Long id;
	private InstrumentoRegistroType instrumentoRegistro;
	
	public InstrumentoRegistroDTO() {
		this.instrumentoRegistro = new InstrumentoRegistroType();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public InstrumentoRegistroType getInstrumentoRegistro() {
		return instrumentoRegistro;
	}

	public void setInstrumentoRegistro(InstrumentoRegistroType instrumentoRegistro) {
		this.instrumentoRegistro = instrumentoRegistro;
	}
}
