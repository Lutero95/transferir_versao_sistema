package br.gov.al.maceio.sishosp.hosp.model;

public class ParametroEmpresaBean {
	
	private Integer id;
	private Integer idEmpresa;
	private SituacaoAtendimentoBean situacaoPadraoFaltaProfissional; 
	private SituacaoAtendimentoBean situacaoPadraoLicencaMedica;
	private SituacaoAtendimentoBean situacaoPadraoFerias;
	private SituacaoAtendimentoBean situacaoPadraoDesligamento;
	
	public ParametroEmpresaBean() {
		situacaoPadraoFaltaProfissional = new SituacaoAtendimentoBean();
		situacaoPadraoLicencaMedica  = new SituacaoAtendimentoBean();
		situacaoPadraoFerias = new SituacaoAtendimentoBean();
		situacaoPadraoDesligamento = new SituacaoAtendimentoBean();
	}
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getIdEmpresa() {
		return idEmpresa;
	}
	public void setIdEmpresa(Integer idEmpresa) {
		this.idEmpresa = idEmpresa;
	}
	public SituacaoAtendimentoBean getSituacaoPadraoFaltaProfissional() {
		return situacaoPadraoFaltaProfissional;
	}
	public void setSituacaoPadraoFaltaProfissional(SituacaoAtendimentoBean situacaoPadraoFaltaProfissional) {
		this.situacaoPadraoFaltaProfissional = situacaoPadraoFaltaProfissional;
	}
	public SituacaoAtendimentoBean getSituacaoPadraoLicencaMedica() {
		return situacaoPadraoLicencaMedica;
	}
	public void setSituacaoPadraoLicencaMedica(SituacaoAtendimentoBean situacaoPadraoLicencaMedica) {
		this.situacaoPadraoLicencaMedica = situacaoPadraoLicencaMedica;
	}
	public SituacaoAtendimentoBean getSituacaoPadraoFerias() {
		return situacaoPadraoFerias;
	}
	public void setSituacaoPadraoFerias(SituacaoAtendimentoBean situacaoPadraoFerias) {
		this.situacaoPadraoFerias = situacaoPadraoFerias;
	}
	public SituacaoAtendimentoBean getSituacaoPadraoDesligamento() {
		return situacaoPadraoDesligamento;
	}
	public void setSituacaoPadraoDesligamento(SituacaoAtendimentoBean situacaoPadraoDesligamento) {
		this.situacaoPadraoDesligamento = situacaoPadraoDesligamento;
	}
}
