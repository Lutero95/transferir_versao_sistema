package br.gov.al.maceio.sishosp.hosp.model;

public class EquipamentoBean {
	
	private Integer idEquipamento;
	private String descEquipamento;
	
	
	public EquipamentoBean(){
		
	}
	
	public EquipamentoBean(Integer idEquipamento, String descEquipamento) {
		this.idEquipamento = idEquipamento;
		this.descEquipamento = descEquipamento;
		
	}

	public Integer getIdEquipamento() {
		return idEquipamento;
	}

	public void setIdEquipamento(Integer idEquipamento) {
		this.idEquipamento = idEquipamento;
	}

	public String getDescEquipamento() {
		return descEquipamento;
	}

	public void setDescEquipamento(String descEquipamento) {
		this.descEquipamento = descEquipamento;
	}

	
	
	

}
