package br.gov.al.maceio.sishosp.hosp.model;

import java.util.Date;

public class ProgramaGrupoEvolucaoBean {
	public ProgramaGrupoEvolucaoBean() {
		programa = new ProgramaBean();
		grupo = new GrupoBean();
	}
	private ProgramaBean programa;
	private GrupoBean grupo;
	private Date dataInicioEvolucao;
	
	
	
	public ProgramaBean getPrograma() {
		return programa;
	}
	public GrupoBean getGrupo() {
		return grupo;
	}
	public Date getDataInicioEvolucao() {
		return dataInicioEvolucao;
	}
	public void setPrograma(ProgramaBean programa) {
		this.programa = programa;
	}
	public void setGrupo(GrupoBean grupo) {
		this.grupo = grupo;
	}
	public void setDataInicioEvolucao(Date dataInicioEvolucao) {
		this.dataInicioEvolucao = dataInicioEvolucao;
	}

}
