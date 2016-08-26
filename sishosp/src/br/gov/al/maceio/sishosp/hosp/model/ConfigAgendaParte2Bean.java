package br.gov.al.maceio.sishosp.hosp.model;

import java.io.Serializable;

public class ConfigAgendaParte2Bean implements Serializable {

	private ProgramaBean programa;
	private GrupoBean grupo;
	private TipoAtendimentoBean tipoAt;
	private Integer qtd;

	public ConfigAgendaParte2Bean() {
		this.programa = null;
		this.grupo = null;
		this.tipoAt = null;
	}

	public ProgramaBean getPrograma() {
		return programa;
	}

	public void setPrograma(ProgramaBean programa) {
		this.programa = programa;
	}

	public GrupoBean getGrupo() {
		return grupo;
	}

	public void setGrupo(GrupoBean grupo) {
		this.grupo = grupo;
	}

	public TipoAtendimentoBean getTipoAt() {
		return tipoAt;
	}

	public void setTipoAt(TipoAtendimentoBean tipoAt) {
		this.tipoAt = tipoAt;
	}

	public Integer getQtd() {
		return qtd;
	}

	public void setQtd(Integer qtd) {
		this.qtd = qtd;
	}

}
