package br.gov.al.maceio.sishosp.hosp.model;

public class ConfigAgendaParte2Bean {

	private ProgramaBean programa;
	private GrupoBean grupo;
	private TipoAtendimentoBean tipoAt;
	private Integer qtd;

	public ConfigAgendaParte2Bean() {
		this.programa = new ProgramaBean();
		this.grupo = new GrupoBean();
		this.tipoAt = new TipoAtendimentoBean();
	}

	public ConfigAgendaParte2Bean(ProgramaBean programa, GrupoBean grupo,
			TipoAtendimentoBean tipoAt, Integer qtd) {
		super();
		this.programa = programa;
		this.grupo = grupo;
		this.tipoAt = tipoAt;
		this.qtd = qtd;
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
