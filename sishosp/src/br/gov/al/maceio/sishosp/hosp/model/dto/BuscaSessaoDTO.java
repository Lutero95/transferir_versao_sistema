package br.gov.al.maceio.sishosp.hosp.model.dto;

import br.gov.al.maceio.sishosp.hosp.model.EquipeBean;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class BuscaSessaoDTO implements Serializable {

    private ProgramaBean programaBean;
    private GrupoBean grupoBean;
    private EquipeBean equipeBean;
    private Date periodoInicial;
    private Date periodoFinal;
    private String tela;
    private String campoBusca;
    private String tipoBusca;
    private String buscaEvolucao;
    private boolean listarEvolucoesPendentes;

    public BuscaSessaoDTO() {
        programaBean = new ProgramaBean();
        grupoBean = new GrupoBean();
    }

    public BuscaSessaoDTO(ProgramaBean programaBean, GrupoBean grupoBean, Date periodoInicial, Date periodoFinal, String tela) {
        this.programaBean = programaBean;
        this.grupoBean = grupoBean;
        this.periodoInicial = periodoInicial;
        this.periodoFinal = periodoFinal;
        this.tela = tela;
    }
    
    public BuscaSessaoDTO(ProgramaBean programaBean, GrupoBean grupoBean, EquipeBean equipe, Date periodoInicial, Date periodoFinal, String tela) {
        this.programaBean = programaBean;
        this.grupoBean = grupoBean;
        this.equipeBean = equipe;
        this.periodoInicial = periodoInicial;
        this.periodoFinal = periodoFinal;
        this.tela = tela;
    }
    
    public BuscaSessaoDTO(ProgramaBean programaBean, GrupoBean grupoBean, EquipeBean equipeBean, String tipoBusca, String campoBusca, String tela) {
        this.programaBean = programaBean;
        this.grupoBean = grupoBean;
        this.equipeBean = equipeBean;
        this.tipoBusca = tipoBusca;
        this.campoBusca = campoBusca;
        this.tela = tela;
    }
    
    public BuscaSessaoDTO(ProgramaBean programaBean, GrupoBean grupoBean, EquipeBean equipeBean, Date periodoInicial, Date periodoFinal, String tela,
    		String campoBusca, String tipoBusca, String buscaEvolucao, boolean listarEvolucoesPendentes) {
        this.programaBean = programaBean;
        this.grupoBean = grupoBean;
        this.equipeBean = equipeBean;
        this.periodoInicial = periodoInicial;
        this.periodoFinal = periodoFinal;
        this.tela = tela;
        this.campoBusca = campoBusca;
        this.tipoBusca = tipoBusca;
        this.buscaEvolucao = buscaEvolucao;
        this.listarEvolucoesPendentes = listarEvolucoesPendentes;
    }

    public ProgramaBean getProgramaBean() {
        return programaBean;
    }

    public void setProgramaBean(ProgramaBean programaBean) {
        this.programaBean = programaBean;
    }

    public GrupoBean getGrupoBean() {
        return grupoBean;
    }

    public void setGrupoBean(GrupoBean grupoBean) {
        this.grupoBean = grupoBean;
    }
    
    public EquipeBean getEquipeBean() {
		return equipeBean;
	}

	public void setEquipeBean(EquipeBean equipeBean) {
		this.equipeBean = equipeBean;
	}

	public Date getPeriodoInicial() {
        return periodoInicial;
    }

    public void setPeriodoInicial(Date periodoInicial) {
        this.periodoInicial = periodoInicial;
    }

    public Date getPeriodoFinal() {
        return periodoFinal;
    }

    public void setPeriodoFinal(Date periodoFinal) {
        this.periodoFinal = periodoFinal;
    }

    public String getTela() {
        return tela;
    }

    public void setTela(String tela) {
        this.tela = tela;
    }
    
    public String getCampoBusca() {
		return campoBusca;
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

	public String getBuscaEvolucao() {
		return buscaEvolucao;
	}

	public void setBuscaEvolucao(String buscaEvolucao) {
		this.buscaEvolucao = buscaEvolucao;
	}
	
	public boolean isListarEvolucoesPendentes() {
		return listarEvolucoesPendentes;
	}

	public void setListarEvolucoesPendentes(boolean listarEvolucoesPendentes) {
		this.listarEvolucoesPendentes = listarEvolucoesPendentes;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BuscaSessaoDTO that = (BuscaSessaoDTO) o;
        return Objects.equals(programaBean, that.programaBean) &&
                Objects.equals(grupoBean, that.grupoBean) &&
                Objects.equals(periodoInicial, that.periodoInicial) &&
                Objects.equals(periodoFinal, that.periodoFinal) &&
                Objects.equals(tela, that.tela) &&
                Objects.equals(campoBusca, that.campoBusca) &&
                Objects.equals(tipoBusca, that.tipoBusca) &&
                Objects.equals(buscaEvolucao, that.buscaEvolucao) &&
                Objects.equals(listarEvolucoesPendentes, that.listarEvolucoesPendentes) &&
                Objects.equals(equipeBean, that.equipeBean);
    }

    @Override
    public int hashCode() {
        return Objects.hash(programaBean, grupoBean, periodoInicial, periodoFinal, tela, campoBusca, tipoBusca, buscaEvolucao, listarEvolucoesPendentes, equipeBean);
    }

    @Override
    public String toString() {
        return "BuscaSessaoDTO{" +
                "programaBean=" + programaBean +
                ", grupoBean=" + grupoBean +
                ", periodoInicial=" + periodoInicial +
                ", periodoFinal=" + periodoFinal +
                ", tela='" + tela + '\'' +
                ", campoBusca=" +campoBusca +
                ", tipoBusca=" +tipoBusca +
                ", buscaEvolucao=" +buscaEvolucao +
                ", listarEvolucoesPendentes=" +listarEvolucoesPendentes +
                ", equipeBean=" +equipeBean +
                '}';
    }
}
