package br.gov.al.maceio.sishosp.hosp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ConselhoBean implements Serializable{
	
	
	private static final long serialVersionUID = 5974309651809465220L;
	private Integer id;
	private String descricao;
	private List<CboBean> listaCbos;
	
	public ConselhoBean() {
		this.listaCbos = new ArrayList<>();
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
	public List<CboBean> getListaCbos() {
		return listaCbos;
	}
	public void setListaCbos(List<CboBean> listaCbos) {
		this.listaCbos = listaCbos;
	}
}
