package br.gov.al.maceio.sishosp.hosp.model;

import java.util.Date;

public class LaudoBean {
	private Integer id_apac;
	private Integer codpaciente;
	private String apac;
	private Integer codprograma;
	private Integer codmedico;
	private String unidade;
	private String cid10_1;
	private String cid10_2;
	private String cid10_3;
	private Integer mesvenc;
	private Integer anovenc;
	private Date dtasolicitacao;
	private String assinaturas;
	private String situacao;
	private String tipo;
	private String obs;
	private Integer codproc;
	private String modelo;
	private Integer codfornecedor;
	private Double valor;
	private Integer qtd;
	private String nota;
	private Date dtautorizacao;
	private String sta;
	private Integer codequipamento;
	private String categoria;
	private String recurso;
	private Integer codgrupo;
	private Integer codempresa;
	private Integer codLaudoApac;
	private String desceApac;
	private Integer codLaudoBpi;	
	private String descBpi;
	
	//CLASSES HERDADAS
	private PacienteBean paciente;
	
	public LaudoBean(){
		paciente = new PacienteBean();
	}

	public Integer getCodLaudoApac() {
		return codLaudoApac;
	}

	public void setCodLaudoApac(Integer codLaudoApac) {
		this.codLaudoApac = codLaudoApac;
	}

	public String getDesceApac() {
		return desceApac;
	}

	public void setDesceApac(String desceApac) {
		this.desceApac = desceApac;
	}

	public Integer getCodLaudoBpi() {
		return codLaudoBpi;
	}

	public void setCodLaudoBpi(Integer codLaudoBpi) {
		this.codLaudoBpi = codLaudoBpi;
	}

	public String getDescBpi() {
		return descBpi;
	}

	public void setDescBpi(String descBpi) {
		this.descBpi = descBpi;
	}

	public Integer getId_apac() {
		return id_apac;
	}

	public void setId_apac(Integer id_apac) {
		this.id_apac = id_apac;
	}

	public Integer getCodpaciente() {
		return codpaciente;
	}

	public void setCodpaciente(Integer codpaciente) {
		this.codpaciente = codpaciente;
	}

	public String getApac() {
		return apac;
	}

	public void setApac(String apac) {
		this.apac = apac;
	}

	public Integer getCodprograma() {
		return codprograma;
	}

	public void setCodprograma(Integer codprograma) {
		this.codprograma = codprograma;
	}

	public String getUnidade() {
		return unidade;
	}

	public void setUnidade(String unidade) {
		this.unidade = unidade;
	}

	public String getCid10_1() {
		return cid10_1;
	}

	public void setCid10_1(String cid10_1) {
		this.cid10_1 = cid10_1;
	}

	public String getCid10_2() {
		return cid10_2;
	}

	public void setCid10_2(String cid10_2) {
		this.cid10_2 = cid10_2;
	}

	public String getCid10_3() {
		return cid10_3;
	}

	public void setCid10_3(String cid10_3) {
		this.cid10_3 = cid10_3;
	}

	public Integer getMesvenc() {
		return mesvenc;
	}

	public void setMesvenc(Integer mesvenc) {
		this.mesvenc = mesvenc;
	}

	public Integer getAnovenc() {
		return anovenc;
	}

	public void setAnovenc(Integer anovenc) {
		this.anovenc = anovenc;
	}

	public String getAssinaturas() {
		return assinaturas;
	}

	public void setAssinaturas(String assinaturas) {
		this.assinaturas = assinaturas;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	public Integer getCodproc() {
		return codproc;
	}

	public void setCodproc(Integer codproc) {
		this.codproc = codproc;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public Integer getCodfornecedor() {
		return codfornecedor;
	}

	public void setCodfornecedor(Integer codfornecedor) {
		this.codfornecedor = codfornecedor;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public Integer getQtd() {
		return qtd;
	}

	public void setQtd(Integer qtd) {
		this.qtd = qtd;
	}

	public String getNota() {
		return nota;
	}

	public void setNota(String nota) {
		this.nota = nota;
	}

	public String getSta() {
		return sta;
	}

	public void setSta(String sta) {
		this.sta = sta;
	}

	public Integer getCodequipamento() {
		return codequipamento;
	}

	public void setCodequipamento(Integer codequipamento) {
		this.codequipamento = codequipamento;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getRecurso() {
		return recurso;
	}

	public void setRecurso(String recurso) {
		this.recurso = recurso;
	}

	public Integer getCodgrupo() {
		return codgrupo;
	}

	public void setCodgrupo(Integer codgrupo) {
		this.codgrupo = codgrupo;
	}

	public Integer getCodempresa() {
		return codempresa;
	}

	public void setCodempresa(Integer codempresa) {
		this.codempresa = codempresa;
	}

	public Integer getCodmedico() {
		return codmedico;
	}

	public void setCodmedico(Integer codmedico) {
		this.codmedico = codmedico;
	}

	public PacienteBean getPaciente() {
		return paciente;
	}

	public void setPaciente(PacienteBean paciente) {
		this.paciente = paciente;
	}

	public Date getDtasolicitacao() {
		return dtasolicitacao;
	}

	public void setDtasolicitacao(Date dtasolicitacao) {
		this.dtasolicitacao = dtasolicitacao;
	}

	public Date getDtautorizacao() {
		return dtautorizacao;
	}

	public void setDtautorizacao(Date dtautorizacao) {
		this.dtautorizacao = dtautorizacao;
	}
	
	
	
}
