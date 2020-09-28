package br.gov.al.maceio.sishosp.gestao.model;

import java.util.List;

import br.gov.al.maceio.sishosp.acl.model.Perfil;
import br.gov.al.maceio.sishosp.gestao.enums.TipoInconsistencia;

public class InconsistenciaBean {
	
	private Integer id;
	private String titulo;
	private String descricao;
	private String sql;
	private TipoInconsistencia tipoInconsistencia;
	private List<Perfil> listaPerfis;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public TipoInconsistencia getTipoInconsistencia() {
		return tipoInconsistencia;
	}
	public void setTipoInconsistencia(TipoInconsistencia tipoInconsistencia) {
		this.tipoInconsistencia = tipoInconsistencia;
	}
	public List<Perfil> getListaPerfis() {
		return listaPerfis;
	}
	public void setListaPerfis(List<Perfil> listaPerfis) {
		this.listaPerfis = listaPerfis;
	}
}
