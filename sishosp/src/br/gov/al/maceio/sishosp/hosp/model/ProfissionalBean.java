package br.gov.al.maceio.sishosp.hosp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProfissionalBean implements Serializable{
	private Integer idProfissional;
	private List<ProgramaBean> programa;
	private List<ProgramaBean> programaNovo;
	private List<GrupoBean> grupo;
	private List<GrupoBean> grupoNovo;
	private EspecialidadeBean especialidade;
	private CboBean cbo;
	private ProcedimentoBean proc1;
	private ProcedimentoBean proc2;
	private String descricaoProf;
	private String cns;
	private boolean ativo;
	private Integer codEmpresa;
	private ProgramaBean progAdd;
	private GrupoBean grupoAdd;
	private ProgramaBean progRmv;
	private GrupoBean grupoRmv;

	public ProfissionalBean() {
		this.programa = new ArrayList<ProgramaBean>();
		this.programaNovo = new ArrayList<ProgramaBean>();
		this.grupo = new ArrayList<GrupoBean>();
		this.grupoNovo = new ArrayList<GrupoBean>();
		this.especialidade = new EspecialidadeBean();
		this.cbo = new CboBean();
		this.proc1 = new ProcedimentoBean();
		this.proc2 = new ProcedimentoBean();
		this.descricaoProf = "";
		this.cns = "";
		this.ativo = true;
		this.progAdd = new ProgramaBean();
		this.grupoAdd = new GrupoBean();
		this.progRmv = new ProgramaBean();
		this.grupoRmv = new GrupoBean();
	}

	public List<ProgramaBean> getProgramaNovo() {
		this.programaNovo = this.programa;
		return programaNovo;
	}

	public void setProgramaNovo(List<ProgramaBean> programaNovo) {
		this.programaNovo = programaNovo;
	}

	public List<GrupoBean> getGrupoNovo() {
		this.grupoNovo = this.grupo;
		return grupoNovo;
	}

	public void setGrupoNovo(List<GrupoBean> grupoNovo) {
		this.grupoNovo = grupoNovo;
	}

	public List<GrupoBean> getGrupo() {
		return grupo;
	}

	public void setGrupo(List<GrupoBean> grupo) {
		this.grupo = grupo;
	}

	public List<ProgramaBean> getPrograma() {
		return programa;
	}

	public void setPrograma(List<ProgramaBean> programa) {
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

	public Integer getCodEmpresa() {
		return codEmpresa;
	}

	public void setCodEmpresa(Integer codEmpresa) {
		this.codEmpresa = codEmpresa;
	}

	public Integer getIdProfissional() {
		return idProfissional;
	}

	public void setIdProfissional(Integer idProfissional) {
		this.idProfissional = idProfissional;
	}

	public ProgramaBean getProgAdd() {
		return progAdd;
	}

	public void setProgAdd(ProgramaBean progAdd) {
		this.progAdd = progAdd;
	}

	public GrupoBean getGrupoAdd() {
		return grupoAdd;
	}

	public void setGrupoAdd(GrupoBean grupoAdd) {
		this.grupoAdd = grupoAdd;
	}

	public ProgramaBean getProgRmv() {
		return progRmv;
	}

	public void setProgRmv(ProgramaBean progRmv) {
		this.progRmv = progRmv;
	}

	public GrupoBean getGrupoRmv() {
		return grupoRmv;
	}

	public void setGrupoRmv(GrupoBean grupoRmv) {
		this.grupoRmv = grupoRmv;
	}

	public void addGrupoLista() {
		this.grupo.add(grupoAdd);
	}

	public void addProgLista() {
		this.programa.add(progAdd);
	}
	
	public void removeProgLista(){
		this.programa.remove(this.progRmv);
	}
	
	public void removeGrupoLista(){
		this.grupo.remove(this.grupoRmv);
	}
}
