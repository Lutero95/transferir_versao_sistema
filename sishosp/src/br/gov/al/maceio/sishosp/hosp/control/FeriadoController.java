package br.gov.al.maceio.sishosp.hosp.control;

import java.util.ArrayList;
import java.util.List;

import br.gov.al.maceio.sishosp.hosp.model.FeriadoBean;

public class FeriadoController {
	
	private FeriadoBean feriado;
	private List<FeriadoBean> listaFeriados;
	private String tipo;
	private String descricaoBusca;
	private Integer tipoBuscar;
	
	public FeriadoController(){
		this.feriado = new FeriadoBean();
		this.listaFeriados = new ArrayList<FeriadoBean>();
		this.tipo = new String();
		this.descricaoBusca = new String();
	}
	
	public void limparDados(){
		this.feriado = new FeriadoBean();
		this.listaFeriados = new ArrayList<FeriadoBean>();
		this.tipo = new String();
		this.descricaoBusca = new String();
	}

	public FeriadoBean getFeriado() {
		return feriado;
	}

	public void setFeriado(FeriadoBean feriado) {
		this.feriado = feriado;
	}

	public List<FeriadoBean> getListaFeriados() {
		return listaFeriados;
	}

	public void setListaFeriados(List<FeriadoBean> listaFeriados) {
		this.listaFeriados = listaFeriados;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public String getDescricaoBusca() {
		return descricaoBusca;
	}

	public void setDescricaoBusca(String descricaoBusca) {
		this.descricaoBusca = descricaoBusca;
	}
	
	public Integer getTipoBuscar() {
		return tipoBuscar;
	}

	public void setTipoBuscar(Integer tipoBuscar) {
		this.tipoBuscar = tipoBuscar;
	}

	public void excluirFeriado(){
		
	}
	
	public void buscarFeriado(){
		
	}
}
