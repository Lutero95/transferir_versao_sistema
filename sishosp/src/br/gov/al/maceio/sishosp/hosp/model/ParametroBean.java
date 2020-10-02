package br.gov.al.maceio.sishosp.hosp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class ParametroBean implements Serializable {


    private static final long serialVersionUID = 1L;
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
    private Boolean usaHorarioLimiteParaAcesso;
    private Date horarioInicioFuncionamento;
    private Date horarioFinalFuncionamento;
    private boolean bloqueiaPorPendenciaEvolucaoAnterior;
    private String orgaoOrigemResponsavelPelaInformacao;
    private String siglaOrgaoOrigemResponsavelPelaDigitacao;
    private String cgcCpfPrestadorOuOrgaoPublico;
    private String orgaoDestinoInformacao;
    private String indicadorOrgaoDestinoInformacao;
    private String versaoSistema;
    private Integer validadePadraoLaudo;
    private boolean validaDadosLaudoSigtap;
    private Integer minutosTolerancia;
    private boolean acessoPermitidoDomingo;
    private boolean acessoPermitidoSegunda;
    private boolean acessoPermitidoTerca;
    private boolean acessoPermitidoQuarta;
    private boolean acessoPermitidoQuinta;
    private boolean acessoPermitidoSexta;
    private boolean acessoPermitidoSabado;
    private boolean permiteAgendamentoDuplicidade;
    private boolean agendaAvulsaValidaPacienteAtivo;
    private boolean atribuirCorTabelaTelaEvolucaoProfissional;
    private boolean cpfPacienteObrigatorio;
    private Integer diasPacienteAtivoSemEvolucao;

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

    public Boolean getUsaHorarioLimiteParaAcesso() {
        return usaHorarioLimiteParaAcesso;
    }

    public void setUsaHorarioLimiteParaAcesso(Boolean usaHorarioLimiteParaAcesso) {
        this.usaHorarioLimiteParaAcesso = usaHorarioLimiteParaAcesso;
    }

    public Date getHorarioInicioFuncionamento() {
        return horarioInicioFuncionamento;
    }

    public void setHorarioInicioFuncionamento(Date horarioInicioFuncionamento) {
        this.horarioInicioFuncionamento = horarioInicioFuncionamento;
    }

    public Date getHorarioFinalFuncionamento() {
        return horarioFinalFuncionamento;
    }

    public void setHorarioFinalFuncionamento(Date horarioFinalFuncionamento) {
        this.horarioFinalFuncionamento = horarioFinalFuncionamento;
    }

    public boolean isBloqueiaPorPendenciaEvolucaoAnterior() {
        return bloqueiaPorPendenciaEvolucaoAnterior;
    }

    public void setBloqueiaPorPendenciaEvolucaoAnterior(boolean bloqueiaPorPendenciaEvolucaoAnterior) {
        this.bloqueiaPorPendenciaEvolucaoAnterior = bloqueiaPorPendenciaEvolucaoAnterior;
    }

    public String getOrgaoOrigemResponsavelPelaInformacao() {
        return orgaoOrigemResponsavelPelaInformacao;
    }

    public void setOrgaoOrigemResponsavelPelaInformacao(String orgaoOrigemResponsavelPelaInformacao) {
        this.orgaoOrigemResponsavelPelaInformacao = orgaoOrigemResponsavelPelaInformacao;
    }

    public String getSiglaOrgaoOrigemResponsavelPelaDigitacao() {
        return siglaOrgaoOrigemResponsavelPelaDigitacao;
    }

    public void setSiglaOrgaoOrigemResponsavelPelaDigitacao(String siglaOrgaoOrigemResponsavelPelaDigitacao) {
        this.siglaOrgaoOrigemResponsavelPelaDigitacao = siglaOrgaoOrigemResponsavelPelaDigitacao;
    }

    public String getCgcCpfPrestadorOuOrgaoPublico() {
        return cgcCpfPrestadorOuOrgaoPublico;
    }

    public void setCgcCpfPrestadorOuOrgaoPublico(String cgcCpfPrestadorOuOrgaoPublico) {
        this.cgcCpfPrestadorOuOrgaoPublico = cgcCpfPrestadorOuOrgaoPublico;
    }

    public String getOrgaoDestinoInformacao() {
        return orgaoDestinoInformacao;
    }

    public void setOrgaoDestinoInformacao(String orgaoDestinoInformacao) {
        this.orgaoDestinoInformacao = orgaoDestinoInformacao;
    }

    public String getIndicadorOrgaoDestinoInformacao() {
        return indicadorOrgaoDestinoInformacao;
    }

    public void setIndicadorOrgaoDestinoInformacao(String indicadorOrgaoDestinoInformacao) {
        this.indicadorOrgaoDestinoInformacao = indicadorOrgaoDestinoInformacao;
    }

    public String getVersaoSistema() {
        return versaoSistema;
    }

    public void setVersaoSistema(String versaoSistema) {
        this.versaoSistema = versaoSistema;
    }
    public Integer getValidadePadraoLaudo() {
        return validadePadraoLaudo;
    }

    public void setValidadePadraoLaudo(Integer validadePadraoLaudo) {
        this.validadePadraoLaudo = validadePadraoLaudo;
    }

    public boolean isValidaDadosLaudoSigtap() {
        return validaDadosLaudoSigtap;
    }

    public void setValidaDadosLaudoSigtap(boolean validaDadosLaudoSigtap) {
        this.validaDadosLaudoSigtap = validaDadosLaudoSigtap;
    }

    public Integer getMinutosTolerancia() {
        return minutosTolerancia;
    }

    public void setMinutosTolerancia(Integer minutosTolerancia) {
        this.minutosTolerancia = minutosTolerancia;
    }

    public boolean isAcessoPermitidoDomingo() {
        return acessoPermitidoDomingo;
    }

    public void setAcessoPermitidoDomingo(boolean acessoPermitidoDomingo) {
        this.acessoPermitidoDomingo = acessoPermitidoDomingo;
    }

    public boolean isAcessoPermitidoSegunda() {
        return acessoPermitidoSegunda;
    }

    public void setAcessoPermitidoSegunda(boolean acessoPermitidoSegunda) {
        this.acessoPermitidoSegunda = acessoPermitidoSegunda;
    }

    public boolean isAcessoPermitidoTerca() {
        return acessoPermitidoTerca;
    }

    public void setAcessoPermitidoTerca(boolean acessoPermitidoTerca) {
        this.acessoPermitidoTerca = acessoPermitidoTerca;
    }

    public boolean isAcessoPermitidoQuarta() {
        return acessoPermitidoQuarta;
    }

    public void setAcessoPermitidoQuarta(boolean acessoPermitidoQuarta) {
        this.acessoPermitidoQuarta = acessoPermitidoQuarta;
    }

    public boolean isAcessoPermitidoQuinta() {
        return acessoPermitidoQuinta;
    }

    public void setAcessoPermitidoQuinta(boolean acessoPermitidoQuinta) {
        this.acessoPermitidoQuinta = acessoPermitidoQuinta;
    }

    public boolean isAcessoPermitidoSexta() {
        return acessoPermitidoSexta;
    }

    public void setAcessoPermitidoSexta(boolean acessoPermitidoSexta) {
        this.acessoPermitidoSexta = acessoPermitidoSexta;
    }

    public boolean isAcessoPermitidoSabado() {
        return acessoPermitidoSabado;
    }

    public void setAcessoPermitidoSabado(boolean acessoPermitidoSabado) {
        this.acessoPermitidoSabado = acessoPermitidoSabado;
    }

    public boolean isPermiteAgendamentoDuplicidade() {
        return permiteAgendamentoDuplicidade;
    }

    public void setPermiteAgendamentoDuplicidade(boolean permiteAgendamentoDuplicidade) {
        this.permiteAgendamentoDuplicidade = permiteAgendamentoDuplicidade;
    }

    public boolean isAgendaAvulsaValidaPacienteAtivo() {
        return agendaAvulsaValidaPacienteAtivo;
    }

    public void setAgendaAvulsaValidaPacienteAtivo(boolean agendaAvulsaValidaPacienteAtivo) {
        this.agendaAvulsaValidaPacienteAtivo = agendaAvulsaValidaPacienteAtivo;
    }
    public boolean isAtribuirCorTabelaTelaEvolucaoProfissional() {
        return atribuirCorTabelaTelaEvolucaoProfissional;
    }

    public void setAtribuirCorTabelaTelaEvolucaoProfissional(boolean atribuirCorTabelaTelaEvolucaoProfissional) {
        this.atribuirCorTabelaTelaEvolucaoProfissional = atribuirCorTabelaTelaEvolucaoProfissional;
    }

	public boolean isCpfPacienteObrigatorio() {
		return cpfPacienteObrigatorio;
	}

	public void setCpfPacienteObrigatorio(boolean cpfPacienteObrigatorio) {
		this.cpfPacienteObrigatorio = cpfPacienteObrigatorio;
	}

	public Integer getDiasPacienteAtivoSemEvolucao() {
		return diasPacienteAtivoSemEvolucao;
	}

	public void setDiasPacienteAtivoSemEvolucao(Integer diasPacienteAtivoSemEvolucao) {
		this.diasPacienteAtivoSemEvolucao = diasPacienteAtivoSemEvolucao;
	}
}