package br.gov.al.maceio.sishosp.hosp.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConfiguracaoProducaoBpaBean {
	
	private Integer id;
	private String descricao;
	private Integer operadorGravacao;
	private Date dataHoraGravacao;
	private String status;
	private Integer OperadorExclusao;
	private Date dataHoraExclusao;
	private List<UnidadeBean> listaUnidades;
	
	public ConfiguracaoProducaoBpaBean() {
		listaUnidades = new ArrayList<>();
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public Integer getOperadorGravacao() {
		return operadorGravacao;
	}
	public void setOperadorGravacao(Integer operadorGravacao) {
		this.operadorGravacao = operadorGravacao;
	}
	public Date getDataHoraGravacao() {
		return dataHoraGravacao;
	}
	public void setDataHoraGravacao(Date dataHoraGravacao) {
		this.dataHoraGravacao = dataHoraGravacao;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getOperadorExclusao() {
		return OperadorExclusao;
	}
	public void setOperadorExclusao(Integer operadorExclusao) {
		OperadorExclusao = operadorExclusao;
	}
	public Date getDataHoraExclusao() {
		return dataHoraExclusao;
	}
	public void setDataHoraExclusao(Date dataHoraExclusao) {
		this.dataHoraExclusao = dataHoraExclusao;
	}
	public List<UnidadeBean> getListaUnidades() {
		return listaUnidades;
	}
	public void setListaUnidades(List<UnidadeBean> listaUnidades) {
		this.listaUnidades = listaUnidades;
	}
	
}
