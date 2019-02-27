package br.gov.al.maceio.sishosp.hosp.model;

import java.io.Serializable;

public class EquipamentoBean implements Serializable {

	private Integer id_equipamento;
	private String descEquipamento;

	public EquipamentoBean() {
	}

	public Integer getId_equipamento() {
		return id_equipamento;
	}

	public void setId_equipamento(Integer id_equipamento) {
		this.id_equipamento = id_equipamento;
	}

	public String getDescEquipamento() {
		return descEquipamento;
	}

	public void setDescEquipamento(String descEquipamento) {
		this.descEquipamento = descEquipamento;
	}

}
