package br.gov.al.maceio.sishosp.hosp.model;

public class ProfissionalBean {

	private ProgramaBean programa;
	private EspecialidadeBean especialidade;
	private CboBean cbo;
	private ProcedimentoBean proc1;
	private ProcedimentoBean proc2;
	private String descricaoProf;
	private String cns;
	private boolean ativo;

	public ProfissionalBean() {
		this.programa = null;//new ProgramaBean();
		this.especialidade = new EspecialidadeBean();
		this.cbo = new CboBean();
		this.proc1 = new ProcedimentoBean();
		this.proc2 = new ProcedimentoBean();
		this.descricaoProf = new String();
		this.cns = new String();
		this.ativo = true;
	
	}

	public ProgramaBean getPrograma() {
		return programa;
	}

	public void setPrograma(ProgramaBean programa) {
		this.programa = programa;
	}

	public EspecialidadeBean getEspecialidade() {
		return especialidade;
	}

	public void setEspecialidade(EspecialidadeBean especialidade) {
		this.especialidade = especialidade;
	}

	public CboBean getCbo() {
		return cbo;
	}

	public void setCbo(CboBean cbo) {
		this.cbo = cbo;
	}

	public ProcedimentoBean getProc1() {
		return proc1;
	}

	public void setProc1(ProcedimentoBean proc1) {
		this.proc1 = proc1;
	}

	public ProcedimentoBean getProc2() {
		return proc2;
	}

	public void setProc2(ProcedimentoBean proc2) {
		this.proc2 = proc2;
	}

	public String getDescricaoProf() {
		return descricaoProf;
	}

	public void setDescricaoProf(String descricaoProf) {
		this.descricaoProf = descricaoProf;
	}

	public String getCns() {
		return cns;
	}

	public void setCns(String cns) {
		this.cns = cns;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

}
