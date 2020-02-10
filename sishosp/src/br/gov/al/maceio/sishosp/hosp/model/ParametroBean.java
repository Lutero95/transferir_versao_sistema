package br.gov.al.maceio.sishosp.hosp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class ParametroBean implements Serializable {

    private Integer motivoDesligamento;
    private String opcaoAtendimento;
    private Integer quantidadeSimultaneaProfissional;
    private Integer quantidadeSimultaneaEquipe;
    private Date horarioInicial;
    private Date horarioFinal;
    private Date almocoInicio;
    private Date almocoFinal;
    private Integer intervalo;
    private TipoAtendimentoBean tipoAtendimento;
    private OrteseProtese orteseProtese;
    private String necessitaPresencaParaEvolucao;
    private boolean ptsMostrarObjGeraisCurtoPrazo, ptsMostrarObjGeraisMedioPrazo, ptsMostrarObjGeraisLongoPrazo;
    private ArrayList<ProgramaGrupoEvolucaoBean> listaProgramasGruposComEvolucao;

    public ParametroBean() {
        tipoAtendimento = new TipoAtendimentoBean();
        orteseProtese = new OrteseProtese();
        listaProgramasGruposComEvolucao = new ArrayList<>();
    }

    public Integer getMotivoDesligamento() {
        return motivoDesligamento;
    }

    public void setMotivoDesligamento(Integer motivoDesligamento) {
        this.motivoDesligamento = motivoDesligamento;
    }

    public String getOpcaoAtendimento() {
        return opcaoAtendimento;
    }

    public void setOpcaoAtendimento(String opcaoAtendimento) {
        this.opcaoAtendimento = opcaoAtendimento;
    }

    public Integer getQuantidadeSimultaneaProfissional() {
        return quantidadeSimultaneaProfissional;
    }

    public void setQuantidadeSimultaneaProfissional(Integer quantidadeSimultaneaProfissional) {
        this.quantidadeSimultaneaProfissional = quantidadeSimultaneaProfissional;
    }

    public Integer getQuantidadeSimultaneaEquipe() {
        return quantidadeSimultaneaEquipe;
    }

    public void setQuantidadeSimultaneaEquipe(Integer quantidadeSimultaneaEquipe) {
        this.quantidadeSimultaneaEquipe = quantidadeSimultaneaEquipe;
    }

    public Date getHorarioInicial() {
        return horarioInicial;
    }

    public void setHorarioInicial(Date horarioInicial) {
        this.horarioInicial = horarioInicial;
    }

    public Date getHorarioFinal() {
        return horarioFinal;
    }

    public void setHorarioFinal(Date horarioFinal) {
        this.horarioFinal = horarioFinal;
    }

    public Integer getIntervalo() {
        return intervalo;
    }

    public void setIntervalo(Integer intervalo) {
        this.intervalo = intervalo;
    }

    public TipoAtendimentoBean getTipoAtendimento() {
        return tipoAtendimento;
    }

    public void setTipoAtendimento(TipoAtendimentoBean tipoAtendimento) {
        this.tipoAtendimento = tipoAtendimento;
    }

    public OrteseProtese getOrteseProtese() {
        return orteseProtese;
    }

    public void setOrteseProtese(OrteseProtese orteseProtese) {
        this.orteseProtese = orteseProtese;
    }

    public Date getAlmocoInicio() {
        return almocoInicio;
    }

    public void setAlmocoInicio(Date almocoInicio) {
        this.almocoInicio = almocoInicio;
    }

    public Date getAlmocoFinal() {
        return almocoFinal;
    }

    public void setAlmocoFinal(Date almocoFinal) {
        this.almocoFinal = almocoFinal;
    }

	public String getNecessitaPresencaParaEvolucao() {
		return necessitaPresencaParaEvolucao;
	}

	public void setNecessitaPresencaParaEvolucao(String necessitaPresencaParaEvolucao) {
		this.necessitaPresencaParaEvolucao = necessitaPresencaParaEvolucao;
	}

	public boolean isPtsMostrarObjGeraisCurtoPrazo() {
		return ptsMostrarObjGeraisCurtoPrazo;
	}

	public boolean isPtsMostrarObjGeraisMedioPrazo() {
		return ptsMostrarObjGeraisMedioPrazo;
	}

	public boolean isPtsMostrarObjGeraisLongoPrazo() {
		return ptsMostrarObjGeraisLongoPrazo;
	}

	public void setPtsMostrarObjGeraisCurtoPrazo(boolean ptsMostrarObjGeraisCurtoPrazo) {
		this.ptsMostrarObjGeraisCurtoPrazo = ptsMostrarObjGeraisCurtoPrazo;
	}

	public void setPtsMostrarObjGeraisMedioPrazo(boolean ptsMostrarObjGeraisMedioPrazo) {
		this.ptsMostrarObjGeraisMedioPrazo = ptsMostrarObjGeraisMedioPrazo;
	}

	public void setPtsMostrarObjGeraisLongoPrazo(boolean ptsMostrarObjGeraisLongoPrazo) {
		this.ptsMostrarObjGeraisLongoPrazo = ptsMostrarObjGeraisLongoPrazo;
	}

	public ArrayList<ProgramaGrupoEvolucaoBean> getListaProgramasGruposComEvolucao() {
		return listaProgramasGruposComEvolucao;
	}

	public void setListaProgramasGruposComEvolucao(ArrayList<ProgramaGrupoEvolucaoBean> listaProgramasGruposComEvolucao) {
		this.listaProgramasGruposComEvolucao = listaProgramasGruposComEvolucao;
	}

	
}