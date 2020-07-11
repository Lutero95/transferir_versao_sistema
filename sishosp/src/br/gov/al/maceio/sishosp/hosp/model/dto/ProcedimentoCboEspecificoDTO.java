package br.gov.al.maceio.sishosp.hosp.model.dto;

import br.gov.al.maceio.sishosp.hosp.model.CboBean;
import br.gov.al.maceio.sishosp.hosp.model.ProcedimentoBean;

public class ProcedimentoCboEspecificoDTO {
	private ProcedimentoBean procedimento;
	private CboBean cbo;
	
	public ProcedimentoCboEspecificoDTO() {
		this.procedimento = new ProcedimentoBean();
		this.cbo = new CboBean();
	}
	
	
	public ProcedimentoBean getProcedimento() {
		return procedimento;
	}
	public void setProcedimento(ProcedimentoBean procedimento) {
		this.procedimento = procedimento;
	}
	public CboBean getCbo() {
		return cbo;
	}
	public void setCbo(CboBean cbo) {
		this.cbo = cbo;
	}
}
