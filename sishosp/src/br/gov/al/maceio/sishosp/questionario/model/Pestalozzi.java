package br.gov.al.maceio.sishosp.questionario.model;

import java.io.Serializable;

public class Pestalozzi implements Serializable {
    private Integer id;
    //SAÚDE
    private String saudeRealizaAtendimentoNestaInstituicao;
    private String saudeUnidade;
    private String saudeServicoAtendimentoRedeReabilitacaoAuditiva;
    private String saudeServicoAtendimentoRedeReabilitacaoFisica;
    private String saudeServicoAtendimentoRedeReabilitacaoIntelectual;
    private String saudeServicoAtendimentoRedeReabilitacaoVisual;
    private String saudeServicoAmbulatorio;
    private String saudeOutrosServicosHomeCare;
    private String saudeOutrosServicosEquoterapia;
    private String saudeOutrosServicosGrupoConvivencia;
    private String saudeOutrosServicosGrupoTerapeuticoFamiliar;
    private String saudeOutrosServicosOrteseProtesesMeiosLocomocao;
    private String saudeOutrosServicosOficinaOrtopedica;
    private String saudeOutrosServicosEstimulacaoPrecoce;
    private String saudeOutrosServicosHidroTerapia;
    private String saudeUsoOrteseProtese;
    private String saudeUsoOrteseProteseQuantoTempo;
    private String saudeUsoOrteseProteseQual;
    private String saudeUsaEquipamentoOrtopedicoOrtese;
    private String saudeUsaEquipamentoOrtopedicoAASI;
    private String saudeUsaEquipamentoOrtopedicoAuxilioOptico;
    private String saudeUsaEquipamentoOrtopedicoMeiosAuxiliaresDeLocomocao;
    private String saudeUsaEquipamentoOrtopedicoProtese;
    private String saudeUsaEquipamentoOrtopedicoOculosAdaptado;
    private String saudeUsaEquipamentoOrtopedicoQuantoTempo;
    private String saudeUsaEquipamentoOrtopedicoEntidadeQueConcedeu;
    private String saudeUsaEquipamentoOrtopedicoRealizouReabilitacao;
    private String saudeUsaEquipamentoOrtopedicoRealizouReabilitacaoOnde;
    private String saudeUsaEquipamentoOrtopedicoAlguemNaFamiliaComDeficiencia;
    private String saudeUsaEquipamentoOrtopedicoAlguemNaFamiliaComDeficienciaParentesco;
    private String saudeUsaEquipamentoOrtopedicoMorbidadeCID;
    private String saudeUsaEquipamentoOrtopedicoMorbidadeCIDQuantoTempo;
    private String saudeUsaEquipamentoOrtopedicoCausaDoencaDeficiencia;
    private String saudeUsaEquipamentoOrtopedicoResidenciaCobertaPSF;
    private String saudeUsaEquipamentoOrtopedicoRecebeuVisitaAgenteSaude;
    private String saudeUsaEquipamentoOrtopedicoFazParaSerAtendidoDoente;
    private String saudeUsaEquipamentoOrtopedicoPostoSaudeRegiaoReside;
    private String saudeUsaEquipamentoOrtopedicoUsoMedicacao;
    private String saudeUsaEquipamentoOrtopedicoUsoMedicacaoQual;
    private String saudeUsaEquipamentoOrtopedicoUsoMedicacaoFrequencia;

    //EDUCAcÃO
    private String educacaoMatriculado;
    private String educacaoEscolaridade;
    private String educacaoTurno;
    private String educacaoEscolaEstaMatriculado;
    private String educacaoAcessoEscola;
    private String educacaoEscolaFicaNoBairro;
    private String educacaoEscolaQualBairro;
    private String educacaoEscolaTemSalaDeRecurso;
    private String educacaoRecebeApoioAuxiliarSala;
    private String educacaoResponsavelParticipaReuniaoEscolar;
    private String educacaoAtividadeInstituconalPossui;
    private String educacaoAtividadeInstituconalQualAEE;
    private String educacaoAtividadeInstituconalQualEJA;
    private String educacaoAtividadeInstituconalQualEsporte;
    private String educacaoAtividadeInstituconalQualOficinaProf;
    private String educacaoAtividadeInstituconalQualJovemAprendiz;
    private String educacaoAtividadeInstituconalQualArteCultura;
    private String educacaoAtividadeComplementaresPossui;
    private String educacaoAtividadeComplementaresDanca;
    private String educacaoAtividadeComplementaresCapoeira;
    private String educacaoAtividadeComplementaresTeatro;
    private String educacaoAtividadeComplementaresPercussao;
    private String educacaoAtividadeComplementaresMusica;
    private String educacaoAtividadeComplementaresCoral;

    //BENEFICIOS SOCIAIS
    private String beneficioSocialAposentadoriaPossui;
    private String beneficioSocialAposentadoriaTipo;
    private String beneficioSocialAposentadoriaQuantoTempo;
    private String beneficioSocialAposentadoriaValor;
    private String beneficioSocialBeneficioFamilia;
    private String beneficioSocialBeneficioFamiliaTipo;
    private String beneficioSocialBeneficioFamiliaQuantoTempo;
    private String beneficioSocialBeneficioFamiliaValor;
    private String beneficioSocialIncapacidadePossui;
    private String beneficioSocialIncapacidadeTipo;
    private String beneficioSocialIncapacidadeQuantoTempo;
    private String beneficioSocialIncapacidadeValor;
    private String beneficioSocialINSSPossui;
    private String beneficioSocialINSSTipo;
    private String beneficioSocialINSSQuantoTempo;
    private String beneficioSocialINSSValor;
    private String beneficioSocialProgramaSociaisPossui;
    private String beneficioSocialProgramaLeitePossui;
    private String beneficioSocialProgramaSopaPossui;
    private String beneficioSocialBolsaFamiliaPossui;
    private String beneficioSocialBolsaFamiliaQuantoTempo;
    private String beneficioSocialBolsaFamiliaValor;
    private String beneficioSocialMinhaCasaMinhaVidaPossui;
    private String beneficioSocialCadastroUnico;
    private String beneficioSocialNumeroNisPossui;
    private String beneficioSocialNumeroNis;

    //TRANSPORTE
    private String transporteCarteiraTransportePossui;
    private String transporteCarteiraTransporteAcompanhate;
    private String transporteCarteiraTransporteTipo;
    private String transporteUtilizadoTratamento;
    private String transporteQuantidadeOnibusAteInstituicao;
    private String transporteExisteAcessibilidadeAcessoPercusoInstituicao;

    //RENDA FAMILIAR
    private String rendaFamiliarInseridoMercadoTrabalho;
    private String rendaFamiliarProfissaoFuncao;
    private String rendaFamiliarAtividadeAntesAgravo;
    private String rendaFamiliarTempoAntesAgravo;
    private String rendaFamiliarValor;
    private String rendaFamiliarMantem;
    private String rendaFamiliarUsuario;
    private String rendaFamiliarPai;
    private String rendaFamiliarMae;
    private String rendaFamiliarIrmao;
    private String rendaFamiliarAvo;
    private String rendaFamiliarCuidador;
    private String rendaFamiliarTio;
    private String rendaFamiliarEsposo;
    private String rendaFamiliarVizinho;
    private String rendaFamiliarFilho;
    private String rendaFamiliarPadastroMadastra;
    private String rendaFamiliarGenroNora;
    private String rendaFamiliarSobrinho;
    private String rendaFamiliarEntenado;

    //HABITACAO
    private String habitacaoSitucaoMoradia;
    private String habitacaoTipoConstrucao;
    private String habitacaoTipoResidencia;
    private String habitacaoNumeroComodo;
    private String habitacaoAbastecimentoDeAgua;
    private String habitacaoTipoIluminacao;
    private String habitacaoEscoamentoSanitario;
    private String habitacaoDestinoLixo;
    private String habitacaoResidenciaAdaptada;

    //COMPOSIcÃO FAMILIAR

    //RELATO DE VIDA
    private String relatoVida;

    //PARECER SOCIAL ENCAMINHAMENTOS
    private String parecerSocialEncaminhamento;

    public Pestalozzi() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSaudeRealizaAtendimentoNestaInstituicao() {
        return saudeRealizaAtendimentoNestaInstituicao;
    }

    public void setSaudeRealizaAtendimentoNestaInstituicao(String saudeRealizaAtendimentoNestaInstituicao) {
        this.saudeRealizaAtendimentoNestaInstituicao = saudeRealizaAtendimentoNestaInstituicao;
    }

    public String getSaudeUnidade() {
        return saudeUnidade;
    }

    public void setSaudeUnidade(String saudeUnidade) {
        this.saudeUnidade = saudeUnidade;
    }

    public String getSaudeServicoAtendimentoRedeReabilitacaoAuditiva() {
        return saudeServicoAtendimentoRedeReabilitacaoAuditiva;
    }

    public void setSaudeServicoAtendimentoRedeReabilitacaoAuditiva(String saudeServicoAtendimentoRedeReabilitacaoAuditiva) {
        this.saudeServicoAtendimentoRedeReabilitacaoAuditiva = saudeServicoAtendimentoRedeReabilitacaoAuditiva;
    }

    public String getSaudeServicoAtendimentoRedeReabilitacaoFisica() {
        return saudeServicoAtendimentoRedeReabilitacaoFisica;
    }

    public void setSaudeServicoAtendimentoRedeReabilitacaoFisica(String saudeServicoAtendimentoRedeReabilitacaoFisica) {
        this.saudeServicoAtendimentoRedeReabilitacaoFisica = saudeServicoAtendimentoRedeReabilitacaoFisica;
    }

    public String getSaudeServicoAtendimentoRedeReabilitacaoIntelectual() {
        return saudeServicoAtendimentoRedeReabilitacaoIntelectual;
    }

    public void setSaudeServicoAtendimentoRedeReabilitacaoIntelectual(String saudeServicoAtendimentoRedeReabilitacaoIntelectual) {
        this.saudeServicoAtendimentoRedeReabilitacaoIntelectual = saudeServicoAtendimentoRedeReabilitacaoIntelectual;
    }

    public String getSaudeServicoAtendimentoRedeReabilitacaoVisual() {
        return saudeServicoAtendimentoRedeReabilitacaoVisual;
    }

    public void setSaudeServicoAtendimentoRedeReabilitacaoVisual(String saudeServicoAtendimentoRedeReabilitacaoVisual) {
        this.saudeServicoAtendimentoRedeReabilitacaoVisual = saudeServicoAtendimentoRedeReabilitacaoVisual;
    }

    public String getSaudeServicoAmbulatorio() {
        return saudeServicoAmbulatorio;
    }

    public void setSaudeServicoAmbulatorio(String saudeServicoAmbulatorio) {
        this.saudeServicoAmbulatorio = saudeServicoAmbulatorio;
    }

    public String getSaudeOutrosServicosHomeCare() {
        return saudeOutrosServicosHomeCare;
    }

    public void setSaudeOutrosServicosHomeCare(String saudeOutrosServicosHomeCare) {
        this.saudeOutrosServicosHomeCare = saudeOutrosServicosHomeCare;
    }

    public String getSaudeOutrosServicosEquoterapia() {
        return saudeOutrosServicosEquoterapia;
    }

    public void setSaudeOutrosServicosEquoterapia(String saudeOutrosServicosEquoterapia) {
        this.saudeOutrosServicosEquoterapia = saudeOutrosServicosEquoterapia;
    }

    public String getSaudeOutrosServicosGrupoConvivencia() {
        return saudeOutrosServicosGrupoConvivencia;
    }

    public void setSaudeOutrosServicosGrupoConvivencia(String saudeOutrosServicosGrupoConvivencia) {
        this.saudeOutrosServicosGrupoConvivencia = saudeOutrosServicosGrupoConvivencia;
    }

    public String getSaudeOutrosServicosGrupoTerapeuticoFamiliar() {
        return saudeOutrosServicosGrupoTerapeuticoFamiliar;
    }

    public void setSaudeOutrosServicosGrupoTerapeuticoFamiliar(String saudeOutrosServicosGrupoTerapeuticoFamiliar) {
        this.saudeOutrosServicosGrupoTerapeuticoFamiliar = saudeOutrosServicosGrupoTerapeuticoFamiliar;
    }

    public String getSaudeOutrosServicosOrteseProtesesMeiosLocomocao() {
        return saudeOutrosServicosOrteseProtesesMeiosLocomocao;
    }

    public void setSaudeOutrosServicosOrteseProtesesMeiosLocomocao(String saudeOutrosServicosOrteseProtesesMeiosLocomocao) {
        this.saudeOutrosServicosOrteseProtesesMeiosLocomocao = saudeOutrosServicosOrteseProtesesMeiosLocomocao;
    }

    public String getSaudeOutrosServicosOficinaOrtopedica() {
        return saudeOutrosServicosOficinaOrtopedica;
    }

    public void setSaudeOutrosServicosOficinaOrtopedica(String saudeOutrosServicosOficinaOrtopedica) {
        this.saudeOutrosServicosOficinaOrtopedica = saudeOutrosServicosOficinaOrtopedica;
    }

    public String getSaudeOutrosServicosEstimulacaoPrecoce() {
        return saudeOutrosServicosEstimulacaoPrecoce;
    }

    public void setSaudeOutrosServicosEstimulacaoPrecoce(String saudeOutrosServicosEstimulacaoPrecoce) {
        this.saudeOutrosServicosEstimulacaoPrecoce = saudeOutrosServicosEstimulacaoPrecoce;
    }

    public String getSaudeOutrosServicosHidroTerapia() {
        return saudeOutrosServicosHidroTerapia;
    }

    public void setSaudeOutrosServicosHidroTerapia(String saudeOutrosServicosHidroTerapia) {
        this.saudeOutrosServicosHidroTerapia = saudeOutrosServicosHidroTerapia;
    }

    public String getSaudeUsoOrteseProtese() {
        return saudeUsoOrteseProtese;
    }

    public void setSaudeUsoOrteseProtese(String saudeUsoOrteseProtese) {
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

    public String getSaudeUsaEquipamentoOrtopedicoOrtese() {
        return saudeUsaEquipamentoOrtopedicoOrtese;
    }

    public void setSaudeUsaEquipamentoOrtopedicoOrtese(String saudeUsaEquipamentoOrtopedicoOrtese) {
        this.saudeUsaEquipamentoOrtopedicoOrtese = saudeUsaEquipamentoOrtopedicoOrtese;
    }

    public String getSaudeUsaEquipamentoOrtopedicoAASI() {
        return saudeUsaEquipamentoOrtopedicoAASI;
    }

    public void setSaudeUsaEquipamentoOrtopedicoAASI(String saudeUsaEquipamentoOrtopedicoAASI) {
        this.saudeUsaEquipamentoOrtopedicoAASI = saudeUsaEquipamentoOrtopedicoAASI;
    }

    public String getSaudeUsaEquipamentoOrtopedicoAuxilioOptico() {
        return saudeUsaEquipamentoOrtopedicoAuxilioOptico;
    }

    public void setSaudeUsaEquipamentoOrtopedicoAuxilioOptico(String saudeUsaEquipamentoOrtopedicoAuxilioOptico) {
        this.saudeUsaEquipamentoOrtopedicoAuxilioOptico = saudeUsaEquipamentoOrtopedicoAuxilioOptico;
    }

    public String getSaudeUsaEquipamentoOrtopedicoMeiosAuxiliaresDeLocomocao() {
        return saudeUsaEquipamentoOrtopedicoMeiosAuxiliaresDeLocomocao;
    }

    public void setSaudeUsaEquipamentoOrtopedicoMeiosAuxiliaresDeLocomocao(String saudeUsaEquipamentoOrtopedicoMeiosAuxiliaresDeLocomocao) {
        this.saudeUsaEquipamentoOrtopedicoMeiosAuxiliaresDeLocomocao = saudeUsaEquipamentoOrtopedicoMeiosAuxiliaresDeLocomocao;
    }

    public String getSaudeUsaEquipamentoOrtopedicoProtese() {
        return saudeUsaEquipamentoOrtopedicoProtese;
    }

    public void setSaudeUsaEquipamentoOrtopedicoProtese(String saudeUsaEquipamentoOrtopedicoProtese) {
        this.saudeUsaEquipamentoOrtopedicoProtese = saudeUsaEquipamentoOrtopedicoProtese;
    }

    public String getSaudeUsaEquipamentoOrtopedicoOculosAdaptado() {
        return saudeUsaEquipamentoOrtopedicoOculosAdaptado;
    }

    public void setSaudeUsaEquipamentoOrtopedicoOculosAdaptado(String saudeUsaEquipamentoOrtopedicoOculosAdaptado) {
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

    public String getSaudeUsaEquipamentoOrtopedicoRealizouReabilitacao() {
        return saudeUsaEquipamentoOrtopedicoRealizouReabilitacao;
    }

    public void setSaudeUsaEquipamentoOrtopedicoRealizouReabilitacao(String saudeUsaEquipamentoOrtopedicoRealizouReabilitacao) {
        this.saudeUsaEquipamentoOrtopedicoRealizouReabilitacao = saudeUsaEquipamentoOrtopedicoRealizouReabilitacao;
    }

    public String getSaudeUsaEquipamentoOrtopedicoRealizouReabilitacaoOnde() {
        return saudeUsaEquipamentoOrtopedicoRealizouReabilitacaoOnde;
    }

    public void setSaudeUsaEquipamentoOrtopedicoRealizouReabilitacaoOnde(String saudeUsaEquipamentoOrtopedicoRealizouReabilitacaoOnde) {
        this.saudeUsaEquipamentoOrtopedicoRealizouReabilitacaoOnde = saudeUsaEquipamentoOrtopedicoRealizouReabilitacaoOnde;
    }

    public String getSaudeUsaEquipamentoOrtopedicoAlguemNaFamiliaComDeficiencia() {
        return saudeUsaEquipamentoOrtopedicoAlguemNaFamiliaComDeficiencia;
    }

    public void setSaudeUsaEquipamentoOrtopedicoAlguemNaFamiliaComDeficiencia(String saudeUsaEquipamentoOrtopedicoAlguemNaFamiliaComDeficiencia) {
        this.saudeUsaEquipamentoOrtopedicoAlguemNaFamiliaComDeficiencia = saudeUsaEquipamentoOrtopedicoAlguemNaFamiliaComDeficiencia;
    }

    public String getSaudeUsaEquipamentoOrtopedicoAlguemNaFamiliaComDeficienciaParentesco() {
        return saudeUsaEquipamentoOrtopedicoAlguemNaFamiliaComDeficienciaParentesco;
    }

    public void setSaudeUsaEquipamentoOrtopedicoAlguemNaFamiliaComDeficienciaParentesco(String saudeUsaEquipamentoOrtopedicoAlguemNaFamiliaComDeficienciaParentesco) {
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

    public String getSaudeUsaEquipamentoOrtopedicoRecebeuVisitaAgenteSaude() {
        return saudeUsaEquipamentoOrtopedicoRecebeuVisitaAgenteSaude;
    }

    public void setSaudeUsaEquipamentoOrtopedicoRecebeuVisitaAgenteSaude(String saudeUsaEquipamentoOrtopedicoRecebeuVisitaAgenteSaude) {
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

    public String getSaudeUsaEquipamentoOrtopedicoUsoMedicacao() {
        return saudeUsaEquipamentoOrtopedicoUsoMedicacao;
    }

    public void setSaudeUsaEquipamentoOrtopedicoUsoMedicacao(String saudeUsaEquipamentoOrtopedicoUsoMedicacao) {
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

    public String getEducacaoMatriculado() {
        return educacaoMatriculado;
    }

    public void setEducacaoMatriculado(String educacaoMatriculado) {
        this.educacaoMatriculado = educacaoMatriculado;
    }

    public String getEducacaoEscolaridade() {
        return educacaoEscolaridade;
    }

    public void setEducacaoEscolaridade(String educacaoEscolaridade) {
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

    public String getEducacaoEscolaFicaNoBairro() {
        return educacaoEscolaFicaNoBairro;
    }

    public void setEducacaoEscolaFicaNoBairro(String educacaoEscolaFicaNoBairro) {
        this.educacaoEscolaFicaNoBairro = educacaoEscolaFicaNoBairro;
    }

    public String getEducacaoEscolaQualBairro() {
        return educacaoEscolaQualBairro;
    }

    public void setEducacaoEscolaQualBairro(String educacaoEscolaQualBairro) {
        this.educacaoEscolaQualBairro = educacaoEscolaQualBairro;
    }

    public String getEducacaoEscolaTemSalaDeRecurso() {
        return educacaoEscolaTemSalaDeRecurso;
    }

    public void setEducacaoEscolaTemSalaDeRecurso(String educacaoEscolaTemSalaDeRecurso) {
        this.educacaoEscolaTemSalaDeRecurso = educacaoEscolaTemSalaDeRecurso;
    }

    public String getEducacaoRecebeApoioAuxiliarSala() {
        return educacaoRecebeApoioAuxiliarSala;
    }

    public void setEducacaoRecebeApoioAuxiliarSala(String educacaoRecebeApoioAuxiliarSala) {
        this.educacaoRecebeApoioAuxiliarSala = educacaoRecebeApoioAuxiliarSala;
    }

    public String getEducacaoResponsavelParticipaReuniaoEscolar() {
        return educacaoResponsavelParticipaReuniaoEscolar;
    }

    public void setEducacaoResponsavelParticipaReuniaoEscolar(String educacaoResponsavelParticipaReuniaoEscolar) {
        this.educacaoResponsavelParticipaReuniaoEscolar = educacaoResponsavelParticipaReuniaoEscolar;
    }

    public String getEducacaoAtividadeInstituconalPossui() {
        return educacaoAtividadeInstituconalPossui;
    }

    public void setEducacaoAtividadeInstituconalPossui(String educacaoAtividadeInstituconalPossui) {
        this.educacaoAtividadeInstituconalPossui = educacaoAtividadeInstituconalPossui;
    }

    public String getEducacaoAtividadeInstituconalQualAEE() {
        return educacaoAtividadeInstituconalQualAEE;
    }

    public void setEducacaoAtividadeInstituconalQualAEE(String educacaoAtividadeInstituconalQualAEE) {
        this.educacaoAtividadeInstituconalQualAEE = educacaoAtividadeInstituconalQualAEE;
    }

    public String getEducacaoAtividadeInstituconalQualEJA() {
        return educacaoAtividadeInstituconalQualEJA;
    }

    public void setEducacaoAtividadeInstituconalQualEJA(String educacaoAtividadeInstituconalQualEJA) {
        this.educacaoAtividadeInstituconalQualEJA = educacaoAtividadeInstituconalQualEJA;
    }

    public String getEducacaoAtividadeInstituconalQualEsporte() {
        return educacaoAtividadeInstituconalQualEsporte;
    }

    public void setEducacaoAtividadeInstituconalQualEsporte(String educacaoAtividadeInstituconalQualEsporte) {
        this.educacaoAtividadeInstituconalQualEsporte = educacaoAtividadeInstituconalQualEsporte;
    }

    public String getEducacaoAtividadeInstituconalQualOficinaProf() {
        return educacaoAtividadeInstituconalQualOficinaProf;
    }

    public void setEducacaoAtividadeInstituconalQualOficinaProf(String educacaoAtividadeInstituconalQualOficinaProf) {
        this.educacaoAtividadeInstituconalQualOficinaProf = educacaoAtividadeInstituconalQualOficinaProf;
    }

    public String getEducacaoAtividadeInstituconalQualJovemAprendiz() {
        return educacaoAtividadeInstituconalQualJovemAprendiz;
    }

    public void setEducacaoAtividadeInstituconalQualJovemAprendiz(String educacaoAtividadeInstituconalQualJovemAprendiz) {
        this.educacaoAtividadeInstituconalQualJovemAprendiz = educacaoAtividadeInstituconalQualJovemAprendiz;
    }

    public String getEducacaoAtividadeInstituconalQualArteCultura() {
        return educacaoAtividadeInstituconalQualArteCultura;
    }

    public void setEducacaoAtividadeInstituconalQualArteCultura(String educacaoAtividadeInstituconalQualArteCultura) {
        this.educacaoAtividadeInstituconalQualArteCultura = educacaoAtividadeInstituconalQualArteCultura;
    }

    public String getEducacaoAtividadeComplementaresPossui() {
        return educacaoAtividadeComplementaresPossui;
    }

    public void setEducacaoAtividadeComplementaresPossui(String educacaoAtividadeComplementaresPossui) {
        this.educacaoAtividadeComplementaresPossui = educacaoAtividadeComplementaresPossui;
    }

    public String getEducacaoAtividadeComplementaresDanca() {
        return educacaoAtividadeComplementaresDanca;
    }

    public void setEducacaoAtividadeComplementaresDanca(String educacaoAtividadeComplementaresDanca) {
        this.educacaoAtividadeComplementaresDanca = educacaoAtividadeComplementaresDanca;
    }

    public String getEducacaoAtividadeComplementaresCapoeira() {
        return educacaoAtividadeComplementaresCapoeira;
    }

    public void setEducacaoAtividadeComplementaresCapoeira(String educacaoAtividadeComplementaresCapoeira) {
        this.educacaoAtividadeComplementaresCapoeira = educacaoAtividadeComplementaresCapoeira;
    }

    public String getEducacaoAtividadeComplementaresTeatro() {
        return educacaoAtividadeComplementaresTeatro;
    }

    public void setEducacaoAtividadeComplementaresTeatro(String educacaoAtividadeComplementaresTeatro) {
        this.educacaoAtividadeComplementaresTeatro = educacaoAtividadeComplementaresTeatro;
    }

    public String getEducacaoAtividadeComplementaresPercussao() {
        return educacaoAtividadeComplementaresPercussao;
    }

    public void setEducacaoAtividadeComplementaresPercussao(String educacaoAtividadeComplementaresPercussao) {
        this.educacaoAtividadeComplementaresPercussao = educacaoAtividadeComplementaresPercussao;
    }

    public String getEducacaoAtividadeComplementaresMusica() {
        return educacaoAtividadeComplementaresMusica;
    }

    public void setEducacaoAtividadeComplementaresMusica(String educacaoAtividadeComplementaresMusica) {
        this.educacaoAtividadeComplementaresMusica = educacaoAtividadeComplementaresMusica;
    }

    public String getEducacaoAtividadeComplementaresCoral() {
        return educacaoAtividadeComplementaresCoral;
    }

    public void setEducacaoAtividadeComplementaresCoral(String educacaoAtividadeComplementaresCoral) {
        this.educacaoAtividadeComplementaresCoral = educacaoAtividadeComplementaresCoral;
    }

    public String getBeneficioSocialAposentadoriaPossui() {
        return beneficioSocialAposentadoriaPossui;
    }

    public void setBeneficioSocialAposentadoriaPossui(String beneficioSocialAposentadoriaPossui) {
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

    public String getBeneficioSocialAposentadoriaValor() {
        return beneficioSocialAposentadoriaValor;
    }

    public void setBeneficioSocialAposentadoriaValor(String beneficioSocialAposentadoriaValor) {
        this.beneficioSocialAposentadoriaValor = beneficioSocialAposentadoriaValor;
    }

    public String getBeneficioSocialBeneficioFamilia() {
        return beneficioSocialBeneficioFamilia;
    }

    public void setBeneficioSocialBeneficioFamilia(String beneficioSocialBeneficioFamilia) {
        this.beneficioSocialBeneficioFamilia = beneficioSocialBeneficioFamilia;
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

    public String getBeneficioSocialBeneficioFamiliaValor() {
        return beneficioSocialBeneficioFamiliaValor;
    }

    public void setBeneficioSocialBeneficioFamiliaValor(String beneficioSocialBeneficioFamiliaValor) {
        this.beneficioSocialBeneficioFamiliaValor = beneficioSocialBeneficioFamiliaValor;
    }

    public String getBeneficioSocialIncapacidadePossui() {
        return beneficioSocialIncapacidadePossui;
    }

    public void setBeneficioSocialIncapacidadePossui(String beneficioSocialIncapacidadePossui) {
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

    public String getBeneficioSocialIncapacidadeValor() {
        return beneficioSocialIncapacidadeValor;
    }

    public void setBeneficioSocialIncapacidadeValor(String beneficioSocialIncapacidadeValor) {
        this.beneficioSocialIncapacidadeValor = beneficioSocialIncapacidadeValor;
    }

    public String getBeneficioSocialINSSPossui() {
        return beneficioSocialINSSPossui;
    }

    public void setBeneficioSocialINSSPossui(String beneficioSocialINSSPossui) {
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

    public String getBeneficioSocialINSSValor() {
        return beneficioSocialINSSValor;
    }

    public void setBeneficioSocialINSSValor(String beneficioSocialINSSValor) {
        this.beneficioSocialINSSValor = beneficioSocialINSSValor;
    }

    public String getBeneficioSocialProgramaSociaisPossui() {
        return beneficioSocialProgramaSociaisPossui;
    }

    public void setBeneficioSocialProgramaSociaisPossui(String beneficioSocialProgramaSociaisPossui) {
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

    public String getBeneficioSocialBolsaFamiliaPossui() {
        return beneficioSocialBolsaFamiliaPossui;
    }

    public void setBeneficioSocialBolsaFamiliaPossui(String beneficioSocialBolsaFamiliaPossui) {
        this.beneficioSocialBolsaFamiliaPossui = beneficioSocialBolsaFamiliaPossui;
    }

    public String getBeneficioSocialBolsaFamiliaQuantoTempo() {
        return beneficioSocialBolsaFamiliaQuantoTempo;
    }

    public void setBeneficioSocialBolsaFamiliaQuantoTempo(String beneficioSocialBolsaFamiliaQuantoTempo) {
        this.beneficioSocialBolsaFamiliaQuantoTempo = beneficioSocialBolsaFamiliaQuantoTempo;
    }

    public String getBeneficioSocialBolsaFamiliaValor() {
        return beneficioSocialBolsaFamiliaValor;
    }

    public void setBeneficioSocialBolsaFamiliaValor(String beneficioSocialBolsaFamiliaValor) {
        this.beneficioSocialBolsaFamiliaValor = beneficioSocialBolsaFamiliaValor;
    }

    public String getBeneficioSocialMinhaCasaMinhaVidaPossui() {
        return beneficioSocialMinhaCasaMinhaVidaPossui;
    }

    public void setBeneficioSocialMinhaCasaMinhaVidaPossui(String beneficioSocialMinhaCasaMinhaVidaPossui) {
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

    public String getTransporteCarteiraTransportePossui() {
        return transporteCarteiraTransportePossui;
    }

    public void setTransporteCarteiraTransportePossui(String transporteCarteiraTransportePossui) {
        this.transporteCarteiraTransportePossui = transporteCarteiraTransportePossui;
    }

    public String getTransporteCarteiraTransporteAcompanhate() {
        return transporteCarteiraTransporteAcompanhate;
    }

    public void setTransporteCarteiraTransporteAcompanhate(String transporteCarteiraTransporteAcompanhate) {
        this.transporteCarteiraTransporteAcompanhate = transporteCarteiraTransporteAcompanhate;
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

    public String getTransporteExisteAcessibilidadeAcessoPercusoInstituicao() {
        return transporteExisteAcessibilidadeAcessoPercusoInstituicao;
    }

    public void setTransporteExisteAcessibilidadeAcessoPercusoInstituicao(String transporteExisteAcessibilidadeAcessoPercusoInstituicao) {
        this.transporteExisteAcessibilidadeAcessoPercusoInstituicao = transporteExisteAcessibilidadeAcessoPercusoInstituicao;
    }

    public String getRendaFamiliarInseridoMercadoTrabalho() {
        return rendaFamiliarInseridoMercadoTrabalho;
    }

    public void setRendaFamiliarInseridoMercadoTrabalho(String rendaFamiliarInseridoMercadoTrabalho) {
        this.rendaFamiliarInseridoMercadoTrabalho = rendaFamiliarInseridoMercadoTrabalho;
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

    public String getRendaFamiliarValor() {
        return rendaFamiliarValor;
    }

    public void setRendaFamiliarValor(String rendaFamiliarValor) {
        this.rendaFamiliarValor = rendaFamiliarValor;
    }

    public String getRendaFamiliarMantem() {
        return rendaFamiliarMantem;
    }

    public void setRendaFamiliarMantem(String rendaFamiliarMantem) {
        this.rendaFamiliarMantem = rendaFamiliarMantem;
    }

    public String getRendaFamiliarUsuario() {
        return rendaFamiliarUsuario;
    }

    public void setRendaFamiliarUsuario(String rendaFamiliarUsuario) {
        this.rendaFamiliarUsuario = rendaFamiliarUsuario;
    }

    public String getRendaFamiliarPai() {
        return rendaFamiliarPai;
    }

    public void setRendaFamiliarPai(String rendaFamiliarPai) {
        this.rendaFamiliarPai = rendaFamiliarPai;
    }

    public String getRendaFamiliarMae() {
        return rendaFamiliarMae;
    }

    public void setRendaFamiliarMae(String rendaFamiliarMae) {
        this.rendaFamiliarMae = rendaFamiliarMae;
    }

    public String getRendaFamiliarIrmao() {
        return rendaFamiliarIrmao;
    }

    public void setRendaFamiliarIrmao(String rendaFamiliarIrmao) {
        this.rendaFamiliarIrmao = rendaFamiliarIrmao;
    }

    public String getRendaFamiliarAvo() {
        return rendaFamiliarAvo;
    }

    public void setRendaFamiliarAvo(String rendaFamiliarAvo) {
        this.rendaFamiliarAvo = rendaFamiliarAvo;
    }

    public String getRendaFamiliarCuidador() {
        return rendaFamiliarCuidador;
    }

    public void setRendaFamiliarCuidador(String rendaFamiliarCuidador) {
        this.rendaFamiliarCuidador = rendaFamiliarCuidador;
    }

    public String getRendaFamiliarTio() {
        return rendaFamiliarTio;
    }

    public void setRendaFamiliarTio(String rendaFamiliarTio) {
        this.rendaFamiliarTio = rendaFamiliarTio;
    }

    public String getRendaFamiliarEsposo() {
        return rendaFamiliarEsposo;
    }

    public void setRendaFamiliarEsposo(String rendaFamiliarEsposo) {
        this.rendaFamiliarEsposo = rendaFamiliarEsposo;
    }

    public String getRendaFamiliarVizinho() {
        return rendaFamiliarVizinho;
    }

    public void setRendaFamiliarVizinho(String rendaFamiliarVizinho) {
        this.rendaFamiliarVizinho = rendaFamiliarVizinho;
    }

    public String getRendaFamiliarFilho() {
        return rendaFamiliarFilho;
    }

    public void setRendaFamiliarFilho(String rendaFamiliarFilho) {
        this.rendaFamiliarFilho = rendaFamiliarFilho;
    }

    public String getRendaFamiliarPadastroMadastra() {
        return rendaFamiliarPadastroMadastra;
    }

    public void setRendaFamiliarPadastroMadastra(String rendaFamiliarPadastroMadastra) {
        this.rendaFamiliarPadastroMadastra = rendaFamiliarPadastroMadastra;
    }

    public String getRendaFamiliarGenroNora() {
        return rendaFamiliarGenroNora;
    }

    public void setRendaFamiliarGenroNora(String rendaFamiliarGenroNora) {
        this.rendaFamiliarGenroNora = rendaFamiliarGenroNora;
    }

    public String getRendaFamiliarSobrinho() {
        return rendaFamiliarSobrinho;
    }

    public void setRendaFamiliarSobrinho(String rendaFamiliarSobrinho) {
        this.rendaFamiliarSobrinho = rendaFamiliarSobrinho;
    }

    public String getRendaFamiliarEntenado() {
        return rendaFamiliarEntenado;
    }

    public void setRendaFamiliarEntenado(String rendaFamiliarEntenado) {
        this.rendaFamiliarEntenado = rendaFamiliarEntenado;
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

    public String getHabitacaoNumeroComodo() {
        return habitacaoNumeroComodo;
    }

    public void setHabitacaoNumeroComodo(String habitacaoNumeroComodo) {
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
}
