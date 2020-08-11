package br.gov.al.maceio.sishosp.hosp.model.dto;

public class BuscaLaudoDTO {

    private String situacao;
    private String tipoBusca;
    private String campoBusca;

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
 
  
}
