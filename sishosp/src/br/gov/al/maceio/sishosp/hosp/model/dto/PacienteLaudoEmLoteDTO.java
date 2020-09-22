package br.gov.al.maceio.sishosp.hosp.model.dto;

import br.gov.al.maceio.sishosp.hosp.model.CidBean;
import br.gov.al.maceio.sishosp.hosp.model.PacienteBean;

public class PacienteLaudoEmLoteDTO {
	
	private PacienteBean paciente;
	private CidBean cid1;
	private CidBean cid2;
	private CidBean cid3;
	
	
	public PacienteLaudoEmLoteDTO() {
		this.paciente = new PacienteBean();
		this.cid1 = new CidBean();
		this.cid2 = new CidBean();
		this.cid3 = new CidBean();
	}
	
	public PacienteLaudoEmLoteDTO(PacienteBean paciente) {
		this.paciente = paciente;
		this.cid1 = new CidBean();
		this.cid2 = new CidBean();
		this.cid3 = new CidBean();
	}
	
	public PacienteBean getPaciente() {
		return paciente;
	}
	public void setPaciente(PacienteBean paciente) {
		this.paciente = paciente;
	}

	public CidBean getCid1() {
		return cid1;
	}

	public void setCid1(CidBean cid1) {
		this.cid1 = cid1;
	}

	public CidBean getCid2() {
		return cid2;
	}

	public void setCid2(CidBean cid2) {
		this.cid2 = cid2;
	}

	public CidBean getCid3() {
		return cid3;
	}

	public void setCid3(CidBean cid3) {
		this.cid3 = cid3;
	}
}
