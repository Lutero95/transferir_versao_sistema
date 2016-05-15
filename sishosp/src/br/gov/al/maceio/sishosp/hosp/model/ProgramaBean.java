package br.gov.al.maceio.sishosp.hosp.model;

public class ProgramaBean {
	private Integer idPrograma;
	private String descPrograma;
	private Double codFederal;
	
	public ProgramaBean(){
		this.idPrograma = null;
		this.descPrograma = new String();
		this.codFederal = null;
	}
	
	public ProgramaBean(Integer idPrograma, String descPrograma, Double codFederal) {
		this.idPrograma = idPrograma;
		this.descPrograma = descPrograma;
		this.codFederal = codFederal;
	}

	public Integer getIdPrograma() {
		return idPrograma;
	}

	public void setIdPrograma(Integer idPrograma) {
		this.idPrograma = idPrograma;
	}

	public String getDescPrograma() {
		return descPrograma;
	}

	public void setDescPrograma(String descPrograma) {
		this.descPrograma = descPrograma;
	}

	public Double getCodFederal() {
		return codFederal;
	}

	public void setCodFederal(Double codFederal) {
		this.codFederal = codFederal;
	}
	
}
