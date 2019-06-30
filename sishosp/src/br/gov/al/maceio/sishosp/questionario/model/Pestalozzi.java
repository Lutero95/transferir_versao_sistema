package br.gov.al.maceio.sishosp.questionario.model;

import java.io.Serializable;
import java.util.ArrayList;

import br.gov.al.maceio.sishosp.hosp.model.PacienteBean;

public class Pestalozzi implements Serializable {
    private Integer id;
    private PacienteBean paciente;
    //SAUDE
    private Boolean saudeRealizaAtendimentoNestaInstituicao;
    private String saudeUnidade;
    private Boolean saudeServicoAtendimentoRedeReabilitacaoAuditiva;
    private Boolean saudeServicoAtendimentoRedeReabilitacaoFisica;
    private Boolean saudeServicoAtendimentoRedeReabilitacaoIntelectual;
    private Boolean saudeServicoAtendimentoRedeReabilitacaoVisual;
    private String saudeServicoAmbulatorio;
    private Boolean saudeOutrosServicosHomeCare;
    private Boolean saudeOutrosServicosEquoterapia;
    private Boolean saudeOutrosServicosGrupoConvivencia;
    private Boolean saudeOutrosServicosGrupoTerapeuticoFamiliar;
    private Boolean saudeOutrosServicosOrteseProtesesMeiosLocomocao;
    private Boolean saudeOutrosServicosOficinaOrtopedica;
    private Boolean saudeOutrosServicosEstimulacaoPrecoce;
    private Boolean saudeOutrosServicosHidroTerapia;
    private Boolean saudeUsoOrteseProtese;
    private String saudeUsoOrteseProteseQuantoTempo;
    private String saudeUsoOrteseProteseQual;
    private Boolean saudeUsaEquipamentoOrtopedicoOrtese;
    private Boolean saudeUsaEquipamentoOrtopedicoAASI;
    private Boolean saudeUsaEquipamentoOrtopedicoAuxilioOptico;
    private Boolean saudeUsaEquipamentoOrtopedicoMeiosAuxiliaresDeLocomocao;
    private Boolean saudeUsaEquipamentoOrtopedicoProtese;
    private Boolean saudeUsaEquipamentoOrtopedicoOculosAdaptado;
    private String saudeUsaEquipamentoOrtopedicoQuantoTempo;
    private String saudeUsaEquipamentoOrtopedicoEntidadeQueConcedeu;
    private Boolean saudeUsaEquipamentoOrtopedicoRealizouReabilitacao;
    private String saudeUsaEquipamentoOrtopedicoRealizouReabilitacaoOnde;
    private Boolean saudeUsaEquipamentoOrtopedicoAlguemNaFamiliaComDeficiencia;
    private Integer saudeUsaEquipamentoOrtopedicoAlguemNaFamiliaComDeficienciaParentesco;
    private String saudeUsaEquipamentoOrtopedicoMorbidadeCID;
    private String saudeUsaEquipamentoOrtopedicoMorbidadeCIDQuantoTempo;
    private String saudeUsaEquipamentoOrtopedicoCausaDoencaDeficiencia;
    private String saudeUsaEquipamentoOrtopedicoResidenciaCobertaPSF;
    private Boolean saudeUsaEquipamentoOrtopedicoRecebeuVisitaAgenteSaude;
    private String saudeUsaEquipamentoOrtopedicoFazParaSerAtendidoDoente;
    private String saudeUsaEquipamentoOrtopedicoPostoSaudeRegiaoReside;
    private Boolean saudeUsaEquipamentoOrtopedicoUsoMedicacao;
    private String saudeUsaEquipamentoOrtopedicoUsoMedicacaoQual;
    private String saudeUsaEquipamentoOrtopedicoUsoMedicacaoFrequencia;

    //EDUCACAO
    private Boolean educacaoMatriculado;
    private Integer educacaoEscolaridade;
    private String educacaoTurno;
    private String educacaoEscolaEstaMatriculado;
    private String educacaoAcessoEscola;
    private Boolean educacaoEscolaFicaNoBairro;
    private String educacaoEscolaQualBairro;
    private Boolean educacaoEscolaTemSalaDeRecurso;
    private Boolean educacaoRecebeApoioAuxiliarSala;
    private Boolean educacaoResponsavelParticipaReuniaoEscolar;
    private Boolean educacaoAtividadeInstituconalPossui;
    private Boolean educacaoAtividadeInstituconalQualAEE;
    private Boolean educacaoAtividadeInstituconalQualEJA;
    private Boolean educacaoAtividadeInstituconalQualEsporte;
    private Boolean educacaoAtividadeInstituconalQualOficinaProf;
    private Boolean educacaoAtividadeInstituconalQualJovemAprendiz;
    private Boolean educacaoAtividadeInstituconalQualArteCultura;
    private Boolean educacaoAtividadeComplementaresPossui;
    private Boolean educacaoAtividadeComplementaresDanca;
    private Boolean educacaoAtividadeComplementaresCapoeira;
    private Boolean educacaoAtividadeComplementaresTeatro;
    private Boolean educacaoAtividadeComplementaresPercussao;
    private Boolean educacaoAtividadeComplementaresMusica;
    private Boolean educacaoAtividadeComplementaresCoral;

    //BENEFICIOS SOCIAIS
    private Boolean beneficioSocialAposentadoriaPossui;
    private String beneficioSocialAposentadoriaTipo;
    private String beneficioSocialAposentadoriaQuantoTempo;
    private Double beneficioSocialAposentadoriaValor;
    private Boolean beneficioSocialBeneficioFamiliaPossui;
    private String beneficioSocialBeneficioFamiliaTipo;
    private String beneficioSocialBeneficioFamiliaQuantoTempo;
    private Double beneficioSocialBeneficioFamiliaValor;
    private Boolean beneficioSocialIncapacidadePossui;
    private String beneficioSocialIncapacidadeTipo;
    private String beneficioSocialIncapacidadeQuantoTempo;
    private Double beneficioSocialIncapacidadeValor;
    private Boolean beneficioSocialINSSPossui;
    private String beneficioSocialINSSTipo;
    private String beneficioSocialINSSQuantoTempo;
    private Double beneficioSocialINSSValor;
    private Boolean beneficioSocialProgramaSociaisPossui;
    private String beneficioSocialProgramaLeitePossui;
    private String beneficioSocialProgramaSopaPossui;
    private Boolean beneficioSocialBolsaFamiliaPossui;
    private String beneficioSocialBolsaFamiliaQuantoTempo;
    private Double beneficioSocialBolsaFamiliaValor;
    private Boolean beneficioSocialMinhaCasaMinhaVidaPossui;
    private String beneficioSocialCadastroUnico;
    private String beneficioSocialNumeroNisPossui;
    private String beneficioSocialNumeroNis;

    //TRANSPORTE
    private Boolean transporteCarteiraTransportePossui;
    private Boolean transporteCarteiraTransporteAcompanhate;
    private String transporteCarteiraTransporteTipo;
    private String transporteUtilizadoTratamento;
    private String transporteQuantidadeOnibusAteInstituicao;
    private Boolean transporteExisteAcessibilidadeAcessoPercusoInstituicao;

    //RENDA FAMILIAR
    private Boolean rendaFamiliarInseridoMercadoTrabalho;
    private String rendaFamiliarProfissaoFuncao;
    private String rendaFamiliarAtividadeAntesAgravo;
    private String rendaFamiliarTempoAntesAgravo;
    private Double rendaFamiliarValor;
    private Boolean rendaFamiliarMantemUsuario;
    private Boolean rendaFamiliarMantemPai;
    private Boolean rendaFamiliarMantemMae;
    private Boolean rendaFamiliarMantemIrmao;
    private Boolean rendaFamiliarMantemAvo;
    private Boolean rendaFamiliarMantemCuidador;
    private Boolean rendaFamiliarMantemTio;
    private Boolean rendaFamiliarMantemEsposo;
    private Boolean rendaFamiliarMantemVizinho;
    private Boolean rendaFamiliarMantemFilho;
    private Boolean rendaFamiliarMantemPadastroMadastra;
    private Boolean rendaFamiliarMantemGenroNora;
    private Boolean rendaFamiliarMantemSobrinho;
    private Boolean rendaFamiliarMantemEnteado;

    //HABITACAO
    private String habitacaoSitucaoMoradia;
    private String habitacaoTipoConstrucao;
    private String habitacaoTipoResidencia;
    private Integer habitacaoNumeroComodo;
    private String habitacaoAbastecimentoDeAgua;
    private String habitacaoTipoIluminacao;
    private String habitacaoEscoamentoSanitario;
    private String habitacaoDestinoLixo;
    private String habitacaoResidenciaAdaptada;

    //COMPOSICAO FAMILIAR
    private ArrayList<ComposicaoFamiliar> listaComposicaoFamiliar;

    //RELATO DE VIDA
    private String relatoVida;

    //PARECER SOCIAL ENCAMINHAMENTOS
    private String parecerSocialEncaminhamento;

    public Pestalozzi() {
        listaComposicaoFamiliar = new ArrayList<>();
        paciente = new PacienteBean();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSaudeUnidade() {
        return saudeUnidade;
    }

    public void setSaudeUnidade(String saudeUnidade) {
        this.saudeUnidade = saudeUnidade;
    }

    public Boolean getSaudeServicoAtendimentoRedeReabilitacaoAuditiva() {
        return saudeServicoAtendimentoRedeReabilitacaoAuditiva;
    }

    public void setSaudeServicoAtendimentoRedeReabilitacaoAuditiva(Boolean saudeServicoAtendimentoRedeReabilitacaoAuditiva) {
        this.saudeServicoAtendimentoRedeReabilitacaoAuditiva = saudeServicoAtendimentoRedeReabilitacaoAuditiva;
    }

    public Boolean getSaudeServicoAtendimentoRedeReabilitacaoFisica() {
        return saudeServicoAtendimentoRedeReabilitacaoFisica;
    }

    public void setSaudeServicoAtendimentoRedeReabilitacaoFisica(Boolean saudeServicoAtendimentoRedeReabilitacaoFisica) {
        this.saudeServicoAtendimentoRedeReabilitacaoFisica = saudeServicoAtendimentoRedeReabilitacaoFisica;
    }

    public Boolean getSaudeServicoAtendimentoRedeReabilitacaoIntelectual() {
        return saudeServicoAtendimentoRedeReabilitacaoIntelectual;
    }

    public void setSaudeServicoAtendimentoRedeReabilitacaoIntelectual(Boolean saudeServicoAtendimentoRedeReabilitacaoIntelectual) {
        this.saudeServicoAtendimentoRedeReabilitacaoIntelectual = saudeServicoAtendimentoRedeReabilitacaoIntelectual;
    }

    public Boolean getSaudeServicoAtendimentoRedeReabilitacaoVisual() {
        return saudeServicoAtendimentoRedeReabilitacaoVisual;
    }

    public void setSaudeServicoAtendimentoRedeReabilitacaoVisual(Boolean saudeServicoAtendimentoRedeReabilitacaoVisual) {
        this.saudeServicoAtendimentoRedeReabilitacaoVisual = saudeServicoAtendimentoRedeReabilitacaoVisual;
    }

    public String getSaudeServicoAmbulatorio() {
        return saudeServicoAmbulatorio;
    }

    public void setSaudeServicoAmbulatorio(String saudeServicoAmbulatorio) {
        this.saudeServicoAmbulatorio = saudeServicoAmbulatorio;
    }

    public Boolean getSaudeOutrosServicosHomeCare() {
        return saudeOutrosServicosHomeCare;
    }

    public void setSaudeOutrosServicosHomeCare(Boolean saudeOutrosServicosHomeCare) {
        this.saudeOutrosServicosHomeCare = saudeOutrosServicosHomeCare;
    }

    public Boolean getSaudeOutrosServicosEquoterapia() {
        return saudeOutrosServicosEquoterapia;
    }

    public void setSaudeOutrosServicosEquoterapia(Boolean saudeOutrosServicosEquoterapia) {
        this.saudeOutrosServicosEquoterapia = saudeOutrosServicosEquoterapia;
    }

    public Boolean getSaudeOutrosServicosGrupoConvivencia() {
        return saudeOutrosServicosGrupoConvivencia;
    }

    public void setSaudeOutrosServicosGrupoConvivencia(Boolean saudeOutrosServicosGrupoConvivencia) {
        this.saudeOutrosServicosGrupoConvivencia = saudeOutrosServicosGrupoConvivencia;
    }

    public Boolean getSaudeOutrosServicosGrupoTerapeuticoFamiliar() {
        return saudeOutrosServicosGrupoTerapeuticoFamiliar;
    }

    public void setSaudeOutrosServicosGrupoTerapeuticoFamiliar(Boolean saudeOutrosServicosGrupoTerapeuticoFamiliar) {
        this.saudeOutrosServicosGrupoTerapeuticoFamiliar = saudeOutrosServicosGrupoTerapeuticoFamiliar;
    }

    public Boolean getSaudeOutrosServicosOrteseProtesesMeiosLocomocao() {
        return saudeOutrosServicosOrteseProtesesMeiosLocomocao;
    }

    public void setSaudeOutrosServicosOrteseProtesesMeiosLocomocao(Boolean saudeOutrosServicosOrteseProtesesMeiosLocomocao) {
        this.saudeOutrosServicosOrteseProtesesMeiosLocomocao = saudeOutrosServicosOrteseProtesesMeiosLocomocao;
    }

    public Boolean getSaudeOutrosServicosOficinaOrtopedica() {
        return saudeOutrosServicosOficinaOrtopedica;
    }

    public void setSaudeOutrosServicosOficinaOrtopedica(Boolean saudeOutrosServicosOficinaOrtopedica) {
        this.saudeOutrosServicosOficinaOrtopedica = saudeOutrosServicosOficinaOrtopedica;
    }

    public Boolean getSaudeOutrosServicosEstimulacaoPrecoce() {
        return saudeOutrosServicosEstimulacaoPrecoce;
    }

    public void setSaudeOutrosServicosEstimulacaoPrecoce(Boolean saudeOutrosServicosEstimulacaoPrecoce) {
        this.saudeOutrosServicosEstimulacaoPrecoce = saudeOutrosServicosEstimulacaoPrecoce;
    }

    public Boolean getSaudeOutrosServicosHidroTerapia() {
        return saudeOutrosServicosHidroTerapia;
    }

    public void setSaudeOutrosServicosHidroTerapia(Boolean saudeOutrosServicosHidroTerapia) {
        this.saudeOutrosServicosHidroTerapia = saudeOutrosServicosHidroTerapia;
    }

    public Boolean getSaudeUsoOrteseProtese() {
        return saudeUsoOrteseProtese;
    }

    public void setSaudeUsoOrteseProtese(Boolean saudeUsoOrteseProtese) {
        this.saudeUsoOrteseProtese = saudeUsoOrteseProtese;
    }

    public String getSaudeUsoOrteseProteseQuantoTempo() {
        return saudeUsoOrteseProteseQuantoTempo;
    }

    public void setSaudeUsoOrteseProteseQuantoTempo(String saudeUsoOrteseProteseQuantoTempo) {
        this.saudeUsoOrteseProteseQuantoTempo = saudeUsoOrteseProteseQuantoTempo;
    }

    public String getSaudeUsoOrteseProteseQual() {
        return saudeUsoOrteseProteseQual;
    }

    public void setSaudeUsoOrteseProteseQual(String saudeUsoOrteseProteseQual) {
        this.saudeUsoOrteseProteseQual = saudeUsoOrteseProteseQual;
    }

    public Boolean getSaudeUsaEquipamentoOrtopedicoOrtese() {
        return saudeUsaEquipamentoOrtopedicoOrtese;
    }

    public void setSaudeUsaEquipamentoOrtopedicoOrtese(Boolean saudeUsaEquipamentoOrtopedicoOrtese) {
        this.saudeUsaEquipamentoOrtopedicoOrtese = saudeUsaEquipamentoOrtopedicoOrtese;
    }

    public Boolean getSaudeUsaEquipamentoOrtopedicoAASI() {
        return saudeUsaEquipamentoOrtopedicoAASI;
    }

    public void setSaudeUsaEquipamentoOrtopedicoAASI(Boolean saudeUsaEquipamentoOrtopedicoAASI) {
        this.saudeUsaEquipamentoOrtopedicoAASI = saudeUsaEquipamentoOrtopedicoAASI;
    }

    public Boolean getSaudeUsaEquipamentoOrtopedicoAuxilioOptico() {
        return saudeUsaEquipamentoOrtopedicoAuxilioOptico;
    }

    public void setSaudeUsaEquipamentoOrtopedicoAuxilioOptico(Boolean saudeUsaEquipamentoOrtopedicoAuxilioOptico) {
        this.saudeUsaEquipamentoOrtopedicoAuxilioOptico = saudeUsaEquipamentoOrtopedicoAuxilioOptico;
    }

    public Boolean getSaudeUsaEquipamentoOrtopedicoMeiosAuxiliaresDeLocomocao() {
        return saudeUsaEquipamentoOrtopedicoMeiosAuxiliaresDeLocomocao;
    }

    public void setSaudeUsaEquipamentoOrtopedicoMeiosAuxiliaresDeLocomocao(Boolean saudeUsaEquipamentoOrtopedicoMeiosAuxiliaresDeLocomocao) {
        this.saudeUsaEquipamentoOrtopedicoMeiosAuxiliaresDeLocomocao = saudeUsaEquipamentoOrtopedicoMeiosAuxiliaresDeLocomocao;
    }

    public Boolean getSaudeUsaEquipamentoOrtopedicoProtese() {
        return saudeUsaEquipamentoOrtopedicoProtese;
    }

    public void setSaudeUsaEquipamentoOrtopedicoProtese(Boolean saudeUsaEquipamentoOrtopedicoProtese) {
        this.saudeUsaEquipamentoOrtopedicoProtese = saudeUsaEquipamentoOrtopedicoProtese;
    }

    public Boolean getSaudeUsaEquipamentoOrtopedicoOculosAdaptado() {
        return saudeUsaEquipamentoOrtopedicoOculosAdaptado;
    }

    public void setSaudeUsaEquipamentoOrtopedicoOculosAdaptado(Boolean saudeUsaEquipamentoOrtopedicoOculosAdaptado) {
        this.saudeUsaEquipamentoOrtopedicoOculosAdaptado = saudeUsaEquipamentoOrtopedicoOculosAdaptado;
    }

    public String getSaudeUsaEquipamentoOrtopedicoQuantoTempo() {
        return saudeUsaEquipamentoOrtopedicoQuantoTempo;
    }

    public void setSaudeUsaEquipamentoOrtopedicoQuantoTempo(String saudeUsaEquipamentoOrtopedicoQuantoTempo) {
        this.saudeUsaEquipamentoOrtopedicoQuantoTempo = saudeUsaEquipamentoOrtopedicoQuantoTempo;
    }

    public String getSaudeUsaEquipamentoOrtopedicoEntidadeQueConcedeu() {
        return saudeUsaEquipamentoOrtopedicoEntidadeQueConcedeu;
    }

    public void setSaudeUsaEquipamentoOrtopedicoEntidadeQueConcedeu(String saudeUsaEquipamentoOrtopedicoEntidadeQueConcedeu) {
        this.saudeUsaEquipamentoOrtopedicoEntidadeQueConcedeu = saudeUsaEquipamentoOrtopedicoEntidadeQueConcedeu;
    }

    public Boolean getSaudeUsaEquipamentoOrtopedicoRealizouReabilitacao() {
        return saudeUsaEquipamentoOrtopedicoRealizouReabilitacao;
    }

    public void setSaudeUsaEquipamentoOrtopedicoRealizouReabilitacao(Boolean saudeUsaEquipamentoOrtopedicoRealizouReabilitacao) {
        this.saudeUsaEquipamentoOrtopedicoRealizouReabilitacao = saudeUsaEquipamentoOrtopedicoRealizouReabilitacao;
    }

    public String getSaudeUsaEquipamentoOrtopedicoRealizouReabilitacaoOnde() {
        return saudeUsaEquipamentoOrtopedicoRealizouReabilitacaoOnde;
    }

    public void setSaudeUsaEquipamentoOrtopedicoRealizouReabilitacaoOnde(String saudeUsaEquipamentoOrtopedicoRealizouReabilitacaoOnde) {
        this.saudeUsaEquipamentoOrtopedicoRealizouReabilitacaoOnde = saudeUsaEquipamentoOrtopedicoRealizouReabilitacaoOnde;
    }

    public Boolean getSaudeUsaEquipamentoOrtopedicoAlguemNaFamiliaComDeficiencia() {
        return saudeUsaEquipamentoOrtopedicoAlguemNaFamiliaComDeficiencia;
    }

    public void setSaudeUsaEquipamentoOrtopedicoAlguemNaFamiliaComDeficiencia(Boolean saudeUsaEquipamentoOrtopedicoAlguemNaFamiliaComDeficiencia) {
        this.saudeUsaEquipamentoOrtopedicoAlguemNaFamiliaComDeficiencia = saudeUsaEquipamentoOrtopedicoAlguemNaFamiliaComDeficiencia;
    }

    public Integer getSaudeUsaEquipamentoOrtopedicoAlguemNaFamiliaComDeficienciaParentesco() {
        return saudeUsaEquipamentoOrtopedicoAlguemNaFamiliaComDeficienciaParentesco;
    }

    public void setSaudeUsaEquipamentoOrtopedicoAlguemNaFamiliaComDeficienciaParentesco(Integer saudeUsaEquipamentoOrtopedicoAlguemNaFamiliaComDeficienciaParentesco) {
        this.saudeUsaEquipamentoOrtopedicoAlguemNaFamiliaComDeficienciaParentesco = saudeUsaEquipamentoOrtopedicoAlguemNaFamiliaComDeficienciaParentesco;
    }

    public String getSaudeUsaEquipamentoOrtopedicoMorbidadeCID() {
        return saudeUsaEquipamentoOrtopedicoMorbidadeCID;
    }

    public void setSaudeUsaEquipamentoOrtopedicoMorbidadeCID(String saudeUsaEquipamentoOrtopedicoMorbidadeCID) {
        this.saudeUsaEquipamentoOrtopedicoMorbidadeCID = saudeUsaEquipamentoOrtopedicoMorbidadeCID;
    }

    public String getSaudeUsaEquipamentoOrtopedicoMorbidadeCIDQuantoTempo() {
        return saudeUsaEquipamentoOrtopedicoMorbidadeCIDQuantoTempo;
    }

    public void setSaudeUsaEquipamentoOrtopedicoMorbidadeCIDQuantoTempo(String saudeUsaEquipamentoOrtopedicoMorbidadeCIDQuantoTempo) {
        this.saudeUsaEquipamentoOrtopedicoMorbidadeCIDQuantoTempo = saudeUsaEquipamentoOrtopedicoMorbidadeCIDQuantoTempo;
    }

    public String getSaudeUsaEquipamentoOrtopedicoCausaDoencaDeficiencia() {
        return saudeUsaEquipamentoOrtopedicoCausaDoencaDeficiencia;
    }

    public void setSaudeUsaEquipamentoOrtopedicoCausaDoencaDeficiencia(String saudeUsaEquipamentoOrtopedicoCausaDoencaDeficiencia) {
        this.saudeUsaEquipamentoOrtopedicoCausaDoencaDeficiencia = saudeUsaEquipamentoOrtopedicoCausaDoencaDeficiencia;
    }

    public String getSaudeUsaEquipamentoOrtopedicoResidenciaCobertaPSF() {
        return saudeUsaEquipamentoOrtopedicoResidenciaCobertaPSF;
    }

    public void setSaudeUsaEquipamentoOrtopedicoResidenciaCobertaPSF(String saudeUsaEquipamentoOrtopedicoResidenciaCobertaPSF) {
        this.saudeUsaEquipamentoOrtopedicoResidenciaCobertaPSF = saudeUsaEquipamentoOrtopedicoResidenciaCobertaPSF;
    }

    public Boolean getSaudeUsaEquipamentoOrtopedicoRecebeuVisitaAgenteSaude() {
        return saudeUsaEquipamentoOrtopedicoRecebeuVisitaAgenteSaude;
    }

    public void setSaudeUsaEquipamentoOrtopedicoRecebeuVisitaAgenteSaude(Boolean saudeUsaEquipamentoOrtopedicoRecebeuVisitaAgenteSaude) {
        this.saudeUsaEquipamentoOrtopedicoRecebeuVisitaAgenteSaude = saudeUsaEquipamentoOrtopedicoRecebeuVisitaAgenteSaude;
    }

    public String getSaudeUsaEquipamentoOrtopedicoFazParaSerAtendidoDoente() {
        return saudeUsaEquipamentoOrtopedicoFazParaSerAtendidoDoente;
    }

    public void setSaudeUsaEquipamentoOrtopedicoFazParaSerAtendidoDoente(String saudeUsaEquipamentoOrtopedicoFazParaSerAtendidoDoente) {
        this.saudeUsaEquipamentoOrtopedicoFazParaSerAtendidoDoente = saudeUsaEquipamentoOrtopedicoFazParaSerAtendidoDoente;
    }

    public String getSaudeUsaEquipamentoOrtopedicoPostoSaudeRegiaoReside() {
        return saudeUsaEquipamentoOrtopedicoPostoSaudeRegiaoReside;
    }

    public void setSaudeUsaEquipamentoOrtopedicoPostoSaudeRegiaoReside(String saudeUsaEquipamentoOrtopedicoPostoSaudeRegiaoReside) {
        this.saudeUsaEquipamentoOrtopedicoPostoSaudeRegiaoReside = saudeUsaEquipamentoOrtopedicoPostoSaudeRegiaoReside;
    }

    public Boolean getSaudeUsaEquipamentoOrtopedicoUsoMedicacao() {
        return saudeUsaEquipamentoOrtopedicoUsoMedicacao;
    }

    public void setSaudeUsaEquipamentoOrtopedicoUsoMedicacao(Boolean saudeUsaEquipamentoOrtopedicoUsoMedicacao) {
        this.saudeUsaEquipamentoOrtopedicoUsoMedicacao = saudeUsaEquipamentoOrtopedicoUsoMedicacao;
    }

    public String getSaudeUsaEquipamentoOrtopedicoUsoMedicacaoQual() {
        return saudeUsaEquipamentoOrtopedicoUsoMedicacaoQual;
    }

    public void setSaudeUsaEquipamentoOrtopedicoUsoMedicacaoQual(String saudeUsaEquipamentoOrtopedicoUsoMedicacaoQual) {
        this.saudeUsaEquipamentoOrtopedicoUsoMedicacaoQual = saudeUsaEquipamentoOrtopedicoUsoMedicacaoQual;
    }

    public String getSaudeUsaEquipamentoOrtopedicoUsoMedicacaoFrequencia() {
        return saudeUsaEquipamentoOrtopedicoUsoMedicacaoFrequencia;
    }

    public void setSaudeUsaEquipamentoOrtopedicoUsoMedicacaoFrequencia(String saudeUsaEquipamentoOrtopedicoUsoMedicacaoFrequencia) {
        this.saudeUsaEquipamentoOrtopedicoUsoMedicacaoFrequencia = saudeUsaEquipamentoOrtopedicoUsoMedicacaoFrequencia;
    }

    public Boolean getEducacaoMatriculado() {
        return educacaoMatriculado;
    }

    public void setEducacaoMatriculado(Boolean educacaoMatriculado) {
        this.educacaoMatriculado = educacaoMatriculado;
    }

    public Integer getEducacaoEscolaridade() {
        return educacaoEscolaridade;
    }

    public void setEducacaoEscolaridade(Integer educacaoEscolaridade) {
        this.educacaoEscolaridade = educacaoEscolaridade;
    }

    public String getEducacaoTurno() {
        return educacaoTurno;
    }

    public void setEducacaoTurno(String educacaoTurno) {
        this.educacaoTurno = educacaoTurno;
    }

    public String getEducacaoEscolaEstaMatriculado() {
        return educacaoEscolaEstaMatriculado;
    }

    public void setEducacaoEscolaEstaMatriculado(String educacaoEscolaEstaMatriculado) {
        this.educacaoEscolaEstaMatriculado = educacaoEscolaEstaMatriculado;
    }

    public String getEducacaoAcessoEscola() {
        return educacaoAcessoEscola;
    }

    public void setEducacaoAcessoEscola(String educacaoAcessoEscola) {
        this.educacaoAcessoEscola = educacaoAcessoEscola;
    }

    public Boolean getEducacaoEscolaFicaNoBairro() {
        return educacaoEscolaFicaNoBairro;
    }

    public void setEducacaoEscolaFicaNoBairro(Boolean educacaoEscolaFicaNoBairro) {
        this.educacaoEscolaFicaNoBairro = educacaoEscolaFicaNoBairro;
    }

    public String getEducacaoEscolaQualBairro() {
        return educacaoEscolaQualBairro;
    }

    public void setEducacaoEscolaQualBairro(String educacaoEscolaQualBairro) {
        this.educacaoEscolaQualBairro = educacaoEscolaQualBairro;
    }

    public Boolean getEducacaoEscolaTemSalaDeRecurso() {
        return educacaoEscolaTemSalaDeRecurso;
    }

    public void setEducacaoEscolaTemSalaDeRecurso(Boolean educacaoEscolaTemSalaDeRecurso) {
        this.educacaoEscolaTemSalaDeRecurso = educacaoEscolaTemSalaDeRecurso;
    }

    public Boolean getEducacaoRecebeApoioAuxiliarSala() {
        return educacaoRecebeApoioAuxiliarSala;
    }

    public void setEducacaoRecebeApoioAuxiliarSala(Boolean educacaoRecebeApoioAuxiliarSala) {
        this.educacaoRecebeApoioAuxiliarSala = educacaoRecebeApoioAuxiliarSala;
    }

    public Boolean getEducacaoResponsavelParticipaReuniaoEscolar() {
        return educacaoResponsavelParticipaReuniaoEscolar;
    }

    public void setEducacaoResponsavelParticipaReuniaoEscolar(Boolean educacaoResponsavelParticipaReuniaoEscolar) {
        this.educacaoResponsavelParticipaReuniaoEscolar = educacaoResponsavelParticipaReuniaoEscolar;
    }

    public Boolean getEducacaoAtividadeInstituconalPossui() {
        return educacaoAtividadeInstituconalPossui;
    }

    public void setEducacaoAtividadeInstituconalPossui(Boolean educacaoAtividadeInstituconalPossui) {
        this.educacaoAtividadeInstituconalPossui = educacaoAtividadeInstituconalPossui;
    }

    public Boolean getEducacaoAtividadeInstituconalQualAEE() {
        return educacaoAtividadeInstituconalQualAEE;
    }

    public void setEducacaoAtividadeInstituconalQualAEE(Boolean educacaoAtividadeInstituconalQualAEE) {
        this.educacaoAtividadeInstituconalQualAEE = educacaoAtividadeInstituconalQualAEE;
    }

    public Boolean getEducacaoAtividadeInstituconalQualEJA() {
        return educacaoAtividadeInstituconalQualEJA;
    }

    public void setEducacaoAtividadeInstituconalQualEJA(Boolean educacaoAtividadeInstituconalQualEJA) {
        this.educacaoAtividadeInstituconalQualEJA = educacaoAtividadeInstituconalQualEJA;
    }

    public Boolean getEducacaoAtividadeInstituconalQualEsporte() {
        return educacaoAtividadeInstituconalQualEsporte;
    }

    public void setEducacaoAtividadeInstituconalQualEsporte(Boolean educacaoAtividadeInstituconalQualEsporte) {
        this.educacaoAtividadeInstituconalQualEsporte = educacaoAtividadeInstituconalQualEsporte;
    }

    public Boolean getEducacaoAtividadeInstituconalQualOficinaProf() {
        return educacaoAtividadeInstituconalQualOficinaProf;
    }

    public void setEducacaoAtividadeInstituconalQualOficinaProf(Boolean educacaoAtividadeInstituconalQualOficinaProf) {
        this.educacaoAtividadeInstituconalQualOficinaProf = educacaoAtividadeInstituconalQualOficinaProf;
    }

    public Boolean getEducacaoAtividadeInstituconalQualJovemAprendiz() {
        return educacaoAtividadeInstituconalQualJovemAprendiz;
    }

    public void setEducacaoAtividadeInstituconalQualJovemAprendiz(Boolean educacaoAtividadeInstituconalQualJovemAprendiz) {
        this.educacaoAtividadeInstituconalQualJovemAprendiz = educacaoAtividadeInstituconalQualJovemAprendiz;
    }

    public Boolean getEducacaoAtividadeInstituconalQualArteCultura() {
        return educacaoAtividadeInstituconalQualArteCultura;
    }

    public void setEducacaoAtividadeInstituconalQualArteCultura(Boolean educacaoAtividadeInstituconalQualArteCultura) {
        this.educacaoAtividadeInstituconalQualArteCultura = educacaoAtividadeInstituconalQualArteCultura;
    }

    public Boolean getEducacaoAtividadeComplementaresPossui() {
        return educacaoAtividadeComplementaresPossui;
    }

    public void setEducacaoAtividadeComplementaresPossui(Boolean educacaoAtividadeComplementaresPossui) {
        this.educacaoAtividadeComplementaresPossui = educacaoAtividadeComplementaresPossui;
    }

    public Boolean getEducacaoAtividadeComplementaresDanca() {
        return educacaoAtividadeComplementaresDanca;
    }

    public void setEducacaoAtividadeComplementaresDanca(Boolean educacaoAtividadeComplementaresDanca) {
        this.educacaoAtividadeComplementaresDanca = educacaoAtividadeComplementaresDanca;
    }

    public Boolean getEducacaoAtividadeComplementaresCapoeira() {
        return educacaoAtividadeComplementaresCapoeira;
    }

    public void setEducacaoAtividadeComplementaresCapoeira(Boolean educacaoAtividadeComplementaresCapoeira) {
        this.educacaoAtividadeComplementaresCapoeira = educacaoAtividadeComplementaresCapoeira;
    }

    public Boolean getEducacaoAtividadeComplementaresTeatro() {
        return educacaoAtividadeComplementaresTeatro;
    }

    public void setEducacaoAtividadeComplementaresTeatro(Boolean educacaoAtividadeComplementaresTeatro) {
        this.educacaoAtividadeComplementaresTeatro = educacaoAtividadeComplementaresTeatro;
    }

    public Boolean getEducacaoAtividadeComplementaresPercussao() {
        return educacaoAtividadeComplementaresPercussao;
    }

    public void setEducacaoAtividadeComplementaresPercussao(Boolean educacaoAtividadeComplementaresPercussao) {
        this.educacaoAtividadeComplementaresPercussao = educacaoAtividadeComplementaresPercussao;
    }

    public Boolean getEducacaoAtividadeComplementaresMusica() {
        return educacaoAtividadeComplementaresMusica;
    }

    public void setEducacaoAtividadeComplementaresMusica(Boolean educacaoAtividadeComplementaresMusica) {
        this.educacaoAtividadeComplementaresMusica = educacaoAtividadeComplementaresMusica;
    }

    public Boolean getEducacaoAtividadeComplementaresCoral() {
        return educacaoAtividadeComplementaresCoral;
    }

    public void setEducacaoAtividadeComplementaresCoral(Boolean educacaoAtividadeComplementaresCoral) {
        this.educacaoAtividadeComplementaresCoral = educacaoAtividadeComplementaresCoral;
    }

    public Boolean getBeneficioSocialAposentadoriaPossui() {
        return beneficioSocialAposentadoriaPossui;
    }

    public void setBeneficioSocialAposentadoriaPossui(Boolean beneficioSocialAposentadoriaPossui) {
        this.beneficioSocialAposentadoriaPossui = beneficioSocialAposentadoriaPossui;
    }

    public String getBeneficioSocialAposentadoriaTipo() {
        return beneficioSocialAposentadoriaTipo;
    }

    public void setBeneficioSocialAposentadoriaTipo(String beneficioSocialAposentadoriaTipo) {
        this.beneficioSocialAposentadoriaTipo = beneficioSocialAposentadoriaTipo;
    }

    public String getBeneficioSocialAposentadoriaQuantoTempo() {
        return beneficioSocialAposentadoriaQuantoTempo;
    }

    public void setBeneficioSocialAposentadoriaQuantoTempo(String beneficioSocialAposentadoriaQuantoTempo) {
        this.beneficioSocialAposentadoriaQuantoTempo = beneficioSocialAposentadoriaQuantoTempo;
    }

    public Double getBeneficioSocialAposentadoriaValor() {
        return beneficioSocialAposentadoriaValor;
    }

    public void setBeneficioSocialAposentadoriaValor(Double beneficioSocialAposentadoriaValor) {
        this.beneficioSocialAposentadoriaValor = beneficioSocialAposentadoriaValor;
    }

    public Boolean getBeneficioSocialBeneficioFamiliaPossui() {
        return beneficioSocialBeneficioFamiliaPossui;
    }

    public void setBeneficioSocialBeneficioFamiliaPossui(Boolean beneficioSocialBeneficioFamiliaPossui) {
        this.beneficioSocialBeneficioFamiliaPossui = beneficioSocialBeneficioFamiliaPossui;
    }

    public String getBeneficioSocialBeneficioFamiliaTipo() {
        return beneficioSocialBeneficioFamiliaTipo;
    }

    public void setBeneficioSocialBeneficioFamiliaTipo(String beneficioSocialBeneficioFamiliaTipo) {
        this.beneficioSocialBeneficioFamiliaTipo = beneficioSocialBeneficioFamiliaTipo;
    }

    public String getBeneficioSocialBeneficioFamiliaQuantoTempo() {
        return beneficioSocialBeneficioFamiliaQuantoTempo;
    }

    public void setBeneficioSocialBeneficioFamiliaQuantoTempo(String beneficioSocialBeneficioFamiliaQuantoTempo) {
        this.beneficioSocialBeneficioFamiliaQuantoTempo = beneficioSocialBeneficioFamiliaQuantoTempo;
    }

    public Double getBeneficioSocialBeneficioFamiliaValor() {
        return beneficioSocialBeneficioFamiliaValor;
    }

    public void setBeneficioSocialBeneficioFamiliaValor(Double beneficioSocialBeneficioFamiliaValor) {
        this.beneficioSocialBeneficioFamiliaValor = beneficioSocialBeneficioFamiliaValor;
    }

    public Boolean getBeneficioSocialIncapacidadePossui() {
        return beneficioSocialIncapacidadePossui;
    }

    public void setBeneficioSocialIncapacidadePossui(Boolean beneficioSocialIncapacidadePossui) {
        this.beneficioSocialIncapacidadePossui = beneficioSocialIncapacidadePossui;
    }

    public String getBeneficioSocialIncapacidadeTipo() {
        return beneficioSocialIncapacidadeTipo;
    }

    public void setBeneficioSocialIncapacidadeTipo(String beneficioSocialIncapacidadeTipo) {
        this.beneficioSocialIncapacidadeTipo = beneficioSocialIncapacidadeTipo;
    }

    public String getBeneficioSocialIncapacidadeQuantoTempo() {
        return beneficioSocialIncapacidadeQuantoTempo;
    }

    public void setBeneficioSocialIncapacidadeQuantoTempo(String beneficioSocialIncapacidadeQuantoTempo) {
        this.beneficioSocialIncapacidadeQuantoTempo = beneficioSocialIncapacidadeQuantoTempo;
    }

    public Double getBeneficioSocialIncapacidadeValor() {
        return beneficioSocialIncapacidadeValor;
    }

    public void setBeneficioSocialIncapacidadeValor(Double beneficioSocialIncapacidadeValor) {
        this.beneficioSocialIncapacidadeValor = beneficioSocialIncapacidadeValor;
    }

    public Boolean getBeneficioSocialINSSPossui() {
        return beneficioSocialINSSPossui;
    }

    public void setBeneficioSocialINSSPossui(Boolean beneficioSocialINSSPossui) {
        this.beneficioSocialINSSPossui = beneficioSocialINSSPossui;
    }

    public String getBeneficioSocialINSSTipo() {
        return beneficioSocialINSSTipo;
    }

    public void setBeneficioSocialINSSTipo(String beneficioSocialINSSTipo) {
        this.beneficioSocialINSSTipo = beneficioSocialINSSTipo;
    }

    public String getBeneficioSocialINSSQuantoTempo() {
        return beneficioSocialINSSQuantoTempo;
    }

    public void setBeneficioSocialINSSQuantoTempo(String beneficioSocialINSSQuantoTempo) {
        this.beneficioSocialINSSQuantoTempo = beneficioSocialINSSQuantoTempo;
    }

    public Double getBeneficioSocialINSSValor() {
        return beneficioSocialINSSValor;
    }

    public void setBeneficioSocialINSSValor(Double beneficioSocialINSSValor) {
        this.beneficioSocialINSSValor = beneficioSocialINSSValor;
    }

    public Boolean getBeneficioSocialProgramaSociaisPossui() {
        return beneficioSocialProgramaSociaisPossui;
    }

    public void setBeneficioSocialProgramaSociaisPossui(Boolean beneficioSocialProgramaSociaisPossui) {
        this.beneficioSocialProgramaSociaisPossui = beneficioSocialProgramaSociaisPossui;
    }

    public String getBeneficioSocialProgramaLeitePossui() {
        return beneficioSocialProgramaLeitePossui;
    }

    public void setBeneficioSocialProgramaLeitePossui(String beneficioSocialProgramaLeitePossui) {
        this.beneficioSocialProgramaLeitePossui = beneficioSocialProgramaLeitePossui;
    }

    public String getBeneficioSocialProgramaSopaPossui() {
        return beneficioSocialProgramaSopaPossui;
    }

    public void setBeneficioSocialProgramaSopaPossui(String beneficioSocialProgramaSopaPossui) {
        this.beneficioSocialProgramaSopaPossui = beneficioSocialProgramaSopaPossui;
    }

    public Boolean getBeneficioSocialBolsaFamiliaPossui() {
        return beneficioSocialBolsaFamiliaPossui;
    }

    public void setBeneficioSocialBolsaFamiliaPossui(Boolean beneficioSocialBolsaFamiliaPossui) {
        this.beneficioSocialBolsaFamiliaPossui = beneficioSocialBolsaFamiliaPossui;
    }

    public String getBeneficioSocialBolsaFamiliaQuantoTempo() {
        return beneficioSocialBolsaFamiliaQuantoTempo;
    }

    public void setBeneficioSocialBolsaFamiliaQuantoTempo(String beneficioSocialBolsaFamiliaQuantoTempo) {
        this.beneficioSocialBolsaFamiliaQuantoTempo = beneficioSocialBolsaFamiliaQuantoTempo;
    }

    public Double getBeneficioSocialBolsaFamiliaValor() {
        return beneficioSocialBolsaFamiliaValor;
    }

    public void setBeneficioSocialBolsaFamiliaValor(Double beneficioSocialBolsaFamiliaValor) {
        this.beneficioSocialBolsaFamiliaValor = beneficioSocialBolsaFamiliaValor;
    }

    public Boolean getBeneficioSocialMinhaCasaMinhaVidaPossui() {
        return beneficioSocialMinhaCasaMinhaVidaPossui;
    }

    public void setBeneficioSocialMinhaCasaMinhaVidaPossui(Boolean beneficioSocialMinhaCasaMinhaVidaPossui) {
        this.beneficioSocialMinhaCasaMinhaVidaPossui = beneficioSocialMinhaCasaMinhaVidaPossui;
    }

    public String getBeneficioSocialCadastroUnico() {
        return beneficioSocialCadastroUnico;
    }

    public void setBeneficioSocialCadastroUnico(String beneficioSocialCadastroUnico) {
        this.beneficioSocialCadastroUnico = beneficioSocialCadastroUnico;
    }

    public String getBeneficioSocialNumeroNisPossui() {
        return beneficioSocialNumeroNisPossui;
    }

    public void setBeneficioSocialNumeroNisPossui(String beneficioSocialNumeroNisPossui) {
        this.beneficioSocialNumeroNisPossui = beneficioSocialNumeroNisPossui;
    }

    public String getBeneficioSocialNumeroNis() {
        return beneficioSocialNumeroNis;
    }

    public void setBeneficioSocialNumeroNis(String beneficioSocialNumeroNis) {
        this.beneficioSocialNumeroNis = beneficioSocialNumeroNis;
    }

    public Boolean getTransporteCarteiraTransportePossui() {
        return transporteCarteiraTransportePossui;
    }

    public void setTransporteCarteiraTransportePossui(Boolean transporteCarteiraTransportePossui) {
        this.transporteCarteiraTransportePossui = transporteCarteiraTransportePossui;
    }

    public Boolean getTransporteCarteiraTransporteAcompanhate() {
        return transporteCarteiraTransporteAcompanhate;
    }

    public void setTransporteCarteiraTransporteAcompanhate(Boolean transporteCarteiraTransporteAcompanhate) {
        this.transporteCarteiraTransporteAcompanhate = transporteCarteiraTransporteAcompanhate;
    }

    public void setTransporteExisteAcessibilidadeAcessoPercusoInstituicao(Boolean transporteExisteAcessibilidadeAcessoPercusoInstituicao) {
        this.transporteExisteAcessibilidadeAcessoPercusoInstituicao = transporteExisteAcessibilidadeAcessoPercusoInstituicao;
    }

    public Boolean getTransporteExisteAcessibilidadeAcessoPercusoInstituicao() {
        return transporteExisteAcessibilidadeAcessoPercusoInstituicao;
    }

    public Boolean getRendaFamiliarInseridoMercadoTrabalho() {
        return rendaFamiliarInseridoMercadoTrabalho;
    }

    public void setRendaFamiliarInseridoMercadoTrabalho(Boolean rendaFamiliarInseridoMercadoTrabalho) {
        this.rendaFamiliarInseridoMercadoTrabalho = rendaFamiliarInseridoMercadoTrabalho;
    }

    public String getTransporteCarteiraTransporteTipo() {
        return transporteCarteiraTransporteTipo;
    }

    public void setTransporteCarteiraTransporteTipo(String transporteCarteiraTransporteTipo) {
        this.transporteCarteiraTransporteTipo = transporteCarteiraTransporteTipo;
    }

    public String getTransporteUtilizadoTratamento() {
        return transporteUtilizadoTratamento;
    }

    public void setTransporteUtilizadoTratamento(String transporteUtilizadoTratamento) {
        this.transporteUtilizadoTratamento = transporteUtilizadoTratamento;
    }

    public String getTransporteQuantidadeOnibusAteInstituicao() {
        return transporteQuantidadeOnibusAteInstituicao;
    }

    public void setTransporteQuantidadeOnibusAteInstituicao(String transporteQuantidadeOnibusAteInstituicao) {
        this.transporteQuantidadeOnibusAteInstituicao = transporteQuantidadeOnibusAteInstituicao;
    }

    public String getRendaFamiliarProfissaoFuncao() {
        return rendaFamiliarProfissaoFuncao;
    }

    public void setRendaFamiliarProfissaoFuncao(String rendaFamiliarProfissaoFuncao) {
        this.rendaFamiliarProfissaoFuncao = rendaFamiliarProfissaoFuncao;
    }

    public String getRendaFamiliarAtividadeAntesAgravo() {
        return rendaFamiliarAtividadeAntesAgravo;
    }

    public void setRendaFamiliarAtividadeAntesAgravo(String rendaFamiliarAtividadeAntesAgravo) {
        this.rendaFamiliarAtividadeAntesAgravo = rendaFamiliarAtividadeAntesAgravo;
    }

    public String getRendaFamiliarTempoAntesAgravo() {
        return rendaFamiliarTempoAntesAgravo;
    }

    public void setRendaFamiliarTempoAntesAgravo(String rendaFamiliarTempoAntesAgravo) {
        this.rendaFamiliarTempoAntesAgravo = rendaFamiliarTempoAntesAgravo;
    }

    public Double getRendaFamiliarValor() {
        return rendaFamiliarValor;
    }

    public void setRendaFamiliarValor(Double rendaFamiliarValor) {
        this.rendaFamiliarValor = rendaFamiliarValor;
    }

    public Boolean getRendaFamiliarMantemUsuario() {
        return rendaFamiliarMantemUsuario;
    }

    public void setRendaFamiliarMantemUsuario(Boolean rendaFamiliarMantemUsuario) {
        this.rendaFamiliarMantemUsuario = rendaFamiliarMantemUsuario;
    }

    public Boolean getRendaFamiliarMantemPai() {
        return rendaFamiliarMantemPai;
    }

    public void setRendaFamiliarMantemPai(Boolean rendaFamiliarMantemPai) {
        this.rendaFamiliarMantemPai = rendaFamiliarMantemPai;
    }

    public Boolean getRendaFamiliarMantemMae() {
        return rendaFamiliarMantemMae;
    }

    public void setRendaFamiliarMantemMae(Boolean rendaFamiliarMantemMae) {
        this.rendaFamiliarMantemMae = rendaFamiliarMantemMae;
    }

    public Boolean getRendaFamiliarMantemIrmao() {
        return rendaFamiliarMantemIrmao;
    }

    public void setRendaFamiliarMantemIrmao(Boolean rendaFamiliarMantemIrmao) {
        this.rendaFamiliarMantemIrmao = rendaFamiliarMantemIrmao;
    }

    public Boolean getRendaFamiliarMantemAvo() {
        return rendaFamiliarMantemAvo;
    }

    public void setRendaFamiliarMantemAvo(Boolean rendaFamiliarMantemAvo) {
        this.rendaFamiliarMantemAvo = rendaFamiliarMantemAvo;
    }

    public Boolean getRendaFamiliarMantemCuidador() {
        return rendaFamiliarMantemCuidador;
    }

    public void setRendaFamiliarMantemCuidador(Boolean rendaFamiliarMantemCuidador) {
        this.rendaFamiliarMantemCuidador = rendaFamiliarMantemCuidador;
    }

    public Boolean getRendaFamiliarMantemTio() {
        return rendaFamiliarMantemTio;
    }

    public void setRendaFamiliarMantemTio(Boolean rendaFamiliarMantemTio) {
        this.rendaFamiliarMantemTio = rendaFamiliarMantemTio;
    }

    public Boolean getRendaFamiliarMantemEsposo() {
        return rendaFamiliarMantemEsposo;
    }

    public void setRendaFamiliarMantemEsposo(Boolean rendaFamiliarMantemEsposo) {
        this.rendaFamiliarMantemEsposo = rendaFamiliarMantemEsposo;
    }

    public Boolean getRendaFamiliarMantemVizinho() {
        return rendaFamiliarMantemVizinho;
    }

    public void setRendaFamiliarMantemVizinho(Boolean rendaFamiliarMantemVizinho) {
        this.rendaFamiliarMantemVizinho = rendaFamiliarMantemVizinho;
    }

    public Boolean getRendaFamiliarMantemFilho() {
        return rendaFamiliarMantemFilho;
    }

    public void setRendaFamiliarMantemFilho(Boolean rendaFamiliarMantemFilho) {
        this.rendaFamiliarMantemFilho = rendaFamiliarMantemFilho;
    }

    public Boolean getRendaFamiliarMantemPadastroMadastra() {
        return rendaFamiliarMantemPadastroMadastra;
    }

    public void setRendaFamiliarMantemPadastroMadastra(Boolean rendaFamiliarMantemPadastroMadastra) {
        this.rendaFamiliarMantemPadastroMadastra = rendaFamiliarMantemPadastroMadastra;
    }

    public Boolean getRendaFamiliarMantemGenroNora() {
        return rendaFamiliarMantemGenroNora;
    }

    public void setRendaFamiliarMantemGenroNora(Boolean rendaFamiliarMantemGenroNora) {
        this.rendaFamiliarMantemGenroNora = rendaFamiliarMantemGenroNora;
    }

    public Boolean getRendaFamiliarMantemSobrinho() {
        return rendaFamiliarMantemSobrinho;
    }

    public void setRendaFamiliarMantemSobrinho(Boolean rendaFamiliarMantemSobrinho) {
        this.rendaFamiliarMantemSobrinho = rendaFamiliarMantemSobrinho;
    }

    public Boolean getRendaFamiliarMantemEnteado() {
        return rendaFamiliarMantemEnteado;
    }

    public void setRendaFamiliarMantemEnteado(Boolean rendaFamiliarMantemEnteado) {
        this.rendaFamiliarMantemEnteado = rendaFamiliarMantemEnteado;
    }

    public String getHabitacaoSitucaoMoradia() {
        return habitacaoSitucaoMoradia;
    }

    public void setHabitacaoSitucaoMoradia(String habitacaoSitucaoMoradia) {
        this.habitacaoSitucaoMoradia = habitacaoSitucaoMoradia;
    }

    public String getHabitacaoTipoConstrucao() {
        return habitacaoTipoConstrucao;
    }

    public void setHabitacaoTipoConstrucao(String habitacaoTipoConstrucao) {
        this.habitacaoTipoConstrucao = habitacaoTipoConstrucao;
    }

    public String getHabitacaoTipoResidencia() {
        return habitacaoTipoResidencia;
    }

    public void setHabitacaoTipoResidencia(String habitacaoTipoResidencia) {
        this.habitacaoTipoResidencia = habitacaoTipoResidencia;
    }

    public Integer getHabitacaoNumeroComodo() {
        return habitacaoNumeroComodo;
    }

    public void setHabitacaoNumeroComodo(Integer habitacaoNumeroComodo) {
        this.habitacaoNumeroComodo = habitacaoNumeroComodo;
    }

    public String getHabitacaoAbastecimentoDeAgua() {
        return habitacaoAbastecimentoDeAgua;
    }

    public void setHabitacaoAbastecimentoDeAgua(String habitacaoAbastecimentoDeAgua) {
        this.habitacaoAbastecimentoDeAgua = habitacaoAbastecimentoDeAgua;
    }

    public String getHabitacaoTipoIluminacao() {
        return habitacaoTipoIluminacao;
    }

    public void setHabitacaoTipoIluminacao(String habitacaoTipoIluminacao) {
        this.habitacaoTipoIluminacao = habitacaoTipoIluminacao;
    }

    public String getHabitacaoEscoamentoSanitario() {
        return habitacaoEscoamentoSanitario;
    }

    public void setHabitacaoEscoamentoSanitario(String habitacaoEscoamentoSanitario) {
        this.habitacaoEscoamentoSanitario = habitacaoEscoamentoSanitario;
    }

    public String getHabitacaoDestinoLixo() {
        return habitacaoDestinoLixo;
    }

    public void setHabitacaoDestinoLixo(String habitacaoDestinoLixo) {
        this.habitacaoDestinoLixo = habitacaoDestinoLixo;
    }

    public String getHabitacaoResidenciaAdaptada() {
        return habitacaoResidenciaAdaptada;
    }

    public void setHabitacaoResidenciaAdaptada(String habitacaoResidenciaAdaptada) {
        this.habitacaoResidenciaAdaptada = habitacaoResidenciaAdaptada;
    }

    public ArrayList<ComposicaoFamiliar> getListaComposicaoFamiliar() {
        return listaComposicaoFamiliar;
    }

    public void setListaComposicaoFamiliar(ArrayList<ComposicaoFamiliar> listaComposicaoFamiliar) {
        this.listaComposicaoFamiliar = listaComposicaoFamiliar;
    }

    public String getRelatoVida() {
        return relatoVida;
    }

    public void setRelatoVida(String relatoVida) {
        this.relatoVida = relatoVida;
    }

    public String getParecerSocialEncaminhamento() {
        return parecerSocialEncaminhamento;
    }

    public void setParecerSocialEncaminhamento(String parecerSocialEncaminhamento) {
        this.parecerSocialEncaminhamento = parecerSocialEncaminhamento;
    }

    public Boolean getSaudeRealizaAtendimentoNestaInstituicao() {
        return saudeRealizaAtendimentoNestaInstituicao;
    }

    public void setSaudeRealizaAtendimentoNestaInstituicao(Boolean saudeRealizaAtendimentoNestaInstituicao) {
        this.saudeRealizaAtendimentoNestaInstituicao = saudeRealizaAtendimentoNestaInstituicao;
    }

    public PacienteBean getPaciente() {
		return paciente;
	}

	public void setPaciente(PacienteBean paciente) {
		this.paciente = paciente;
	}
}
