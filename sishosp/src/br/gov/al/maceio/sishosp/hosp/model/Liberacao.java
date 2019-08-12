package br.gov.al.maceio.sishosp.hosp.model;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;

import java.io.Serializable;
import java.sql.Timestamp;

public class Liberacao implements Serializable {

	private Integer id;
	private String motivo;
	private Integer idUsuarioLiberacao;
	private Timestamp dataHoraLiberacao;
	private String rotina;

	public Liberacao(String motivo, Integer idUsuarioLiberacao, Timestamp dataHoraLiberacao, String rotina) {
		this.motivo = motivo;
		this.idUsuarioLiberacao = idUsuarioLiberacao;
		this.dataHoraLiberacao = dataHoraLiberacao;
		this.rotina = rotina;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public Integer getIdUsuarioLiberacao() {
		return idUsuarioLiberacao;
	}

	public void setIdUsuarioLiberacao(Integer idUsuarioLiberacao) {
		this.idUsuarioLiberacao = idUsuarioLiberacao;
	}

	public Timestamp getDataHoraLiberacao() {
		return dataHoraLiberacao;
	}

	public void setDataHoraLiberacao(Timestamp dataHoraLiberacao) {
		this.dataHoraLiberacao = dataHoraLiberacao;
	}

	public String getRotina() {
		return rotina;
	}

	public void setRotina(String rotina) {
		this.rotina = rotina;
	}
}
