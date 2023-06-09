package br.gov.al.maceio.sishosp.hosp.model.dto;

import br.gov.al.maceio.sishosp.hosp.model.CidBean;
import br.gov.al.maceio.sishosp.hosp.model.ProcedimentoBean;

public class ProcedimentoCidDTO {
	
	private ProcedimentoBean procedimento;
	private CidBean cid;
	private Integer id;
	
	public ProcedimentoCidDTO() {
		procedimento = new ProcedimentoBean();
		cid = new CidBean();
	}
	
	public ProcedimentoBean getProcedimento() {
		return procedimento;
	}
	public void setProcedimento(ProcedimentoBean procedimento) {
		this.procedimento = procedimento;
	}
	public CidBean getCid() {
		return cid;
	}
	public void setCid(CidBean cid) {
		this.cid = cid;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
