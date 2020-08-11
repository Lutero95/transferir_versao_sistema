package br.gov.al.maceio.sishosp.hosp.model.dto;

import java.util.Date;

public class BuscaLaudoDTO {

    private String situacao;
    private String tipoBusca;
    private String campoBusca;
    private Date dataInicial;
    private Date dataFinal;
    private String tipoPeriodoData; 

    public BuscaLaudoDTO() {
    }

	public String getSituacao() {
		return situacao;
	}

	public String getCampoBusca() {
		return campoBusca;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public void setCampoBusca(String campoBusca) {
		this.campoBusca = campoBusca;
	}

	public String getTipoBusca() {
		return tipoBusca;
	}

	public void setTipoBusca(String tipoBusca) {
		this.tipoBusca = tipoBusca;
	}

	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public String getTipoPeriodoData() {
		return tipoPeriodoData;
	}

	public void setTipoPeriodoData(String tipoPeriodoData) {
		this.tipoPeriodoData = tipoPeriodoData;
	}
 
}
