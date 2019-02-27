package br.gov.al.maceio.sishosp.hosp.model;

import java.io.Serializable;

public class MotivoDesligamentoBean implements Serializable {

	private Integer id_motivo;
	private String motivo_desligamento;

	public MotivoDesligamentoBean() {

	}

	public Integer getId_motivo() {
		return id_motivo;
	}

	public void setId_motivo(Integer id_motivo) {
		this.id_motivo = id_motivo;
	}

	public String getMotivo_desligamento() {
		return motivo_desligamento;
	}

	public void setMotivo_desligamento(String motivo_desligamento) {
		this.motivo_desligamento = motivo_desligamento;
	}

}
