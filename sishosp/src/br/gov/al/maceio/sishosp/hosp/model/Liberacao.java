package br.gov.al.maceio.sishosp.hosp.model;


import java.io.Serializable;
import java.sql.Timestamp;

public class Liberacao implements Serializable {

	private Integer id;
	private String motivo;
	private Integer idUsuarioLiberacao;
	private Timestamp dataHoraLiberacao;

	public Liberacao(String motivo, Integer idUsuarioLiberacao, Timestamp dataHoraLiberacao) {
		this.motivo = motivo;
		this.idUsuarioLiberacao = idUsuarioLiberacao;
		this.dataHoraLiberacao = dataHoraLiberacao;
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
}
