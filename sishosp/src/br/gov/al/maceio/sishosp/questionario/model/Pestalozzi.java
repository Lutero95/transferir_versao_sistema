package br.gov.al.maceio.sishosp.questionario.model;

import java.io.Serializable;
import java.util.ArrayList;

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

    //EDUCACAO
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
    private Double beneficioSocialAposentadoriaValor;
    private String beneficioSocialBeneficioFamilia;
    private String beneficioSocialBeneficioFamiliaTipo;
    private String beneficioSocialBeneficioFamiliaQuantoTempo;
    private Double beneficioSocialBeneficioFamiliaValor;
    private String beneficioSocialIncapacidadePossui;
    private String beneficioSocialIncapacidadeTipo;
    private String beneficioSocialIncapacidadeQuantoTempo;
    private Double beneficioSocialIncapacidadeValor;
    private String beneficioSocialINSSPossui;
    private String beneficioSocialINSSTipo;
    private String beneficioSocialINSSQuantoTempo;
    private Double beneficioSocialINSSValor;
    private String beneficioSocialProgramaSociaisPossui;
    private String beneficioSocialProgramaLeitePossui;
    private String beneficioSocialProgramaSopaPossui;
    private String beneficioSocialBolsaFamiliaPossui;
    private String beneficioSocialBolsaFamiliaQuantoTempo;
    private Double beneficioSocialBolsaFamiliaValor;
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
    private Double rendaFamiliarValor;
    private String rendaFamiliarMantemUsuario;
    private String rendaFamiliarMantemPai;
    private String rendaFamiliarMantemMae;
    private String rendaFamiliarMantemIrmao;
    private String rendaFamiliarMantemAvo;
    private String rendaFamiliarMantemCuidador;
    private String rendaFamiliarMantemTio;
    private String rendaFamiliarMantemEsposo;
    private String rendaFamiliarMantemVizinho;
    private String rendaFamiliarMantemFilho;
    private String rendaFamiliarMantemPadastroMadastra;
    private String rendaFamiliarMantemGenroNora;
    private String rendaFamiliarMantemSobrinho;
    private String rendaFamiliarMantemEntenado;

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
    private ArrayList<ComposicaoFamiliar> listaComposicaoFamiliar;

    //RELATO DE VIDA
    private String relatoVida;

    //PARECER SOCIAL ENCAMINHAMENTOS
    private String parecerSocialEncaminhamento;

    public Pestalozzi() {
        listaComposicaoFamiliar = new ArrayList<>();
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

    public String getRendaFamiliarMantemUsuario() {
        return rendaFamiliarMantemUsuario;
    }

    public void setRendaFamiliarMantemUsuario(String rendaFamiliarMantemUsuario) {
        this.rendaFamiliarMantemUsuario = rendaFamiliarMantemUsuario;
    }

    public String getRendaFamiliarMantemPai() {
        return rendaFamiliarMantemPai;
    }

    public void setRendaFamiliarMantemPai(String rendaFamiliarMantemPai) {
        this.rendaFamiliarMantemPai = rendaFamiliarMantemPai;
    }

    public String getRendaFamiliarMantemMae() {
        return rendaFamiliarMantemMae;
    }

    public void setRendaFamiliarMantemMae(String rendaFamiliarMantemMae) {
        this.rendaFamiliarMantemMae = rendaFamiliarMantemMae;
    }

    public String getRendaFamiliarMantemIrmao() {
        return rendaFamiliarMantemIrmao;
    }

    public void setRendaFamiliarMantemIrmao(String rendaFamiliarMantemIrmao) {
        this.rendaFamiliarMantemIrmao = rendaFamiliarMantemIrmao;
    }

    public String getRendaFamiliarMantemAvo() {
        return rendaFamiliarMantemAvo;
    }

    public void setRendaFamiliarMantemAvo(String rendaFamiliarMantemAvo) {
        this.rendaFamiliarMantemAvo = rendaFamiliarMantemAvo;
    }

    public String getRendaFamiliarMantemCuidador() {
        return rendaFamiliarMantemCuidador;
    }

    public void setRendaFamiliarMantemCuidador(String rendaFamiliarMantemCuidador) {
        this.rendaFamiliarMantemCuidador = rendaFamiliarMantemCuidador;
    }

    public String getRendaFamiliarMantemTio() {
        return rendaFamiliarMantemTio;
    }

    public void setRendaFamiliarMantemTio(String rendaFamiliarMantemTio) {
        this.rendaFamiliarMantemTio = rendaFamiliarMantemTio;
    }

    public String getRendaFamiliarMantemEsposo() {
        return rendaFamiliarMantemEsposo;
    }

    public void setRendaFamiliarMantemEsposo(String rendaFamiliarMantemEsposo) {
        this.rendaFamiliarMantemEsposo = rendaFamiliarMantemEsposo;
    }

    public String getRendaFamiliarMantemVizinho() {
        return rendaFamiliarMantemVizinho;
    }

    public void setRendaFamiliarMantemVizinho(String rendaFamiliarMantemVizinho) {
        this.rendaFamiliarMantemVizinho = rendaFamiliarMantemVizinho;
    }

    public String getRendaFamiliarMantemFilho() {
        return rendaFamiliarMantemFilho;
    }

    public void setRendaFamiliarMantemFilho(String rendaFamiliarMantemFilho) {
        this.rendaFamiliarMantemFilho = rendaFamiliarMantemFilho;
    }

    public String getRendaFamiliarMantemPadastroMadastra() {
        return rendaFamiliarMantemPadastroMadastra;
    }

    public void setRendaFamiliarMantemPadastroMadastra(String rendaFamiliarMantemPadastroMadastra) {
        this.rendaFamiliarMantemPadastroMadastra = rendaFamiliarMantemPadastroMadastra;
    }

    public String getRendaFamiliarMantemGenroNora() {
        return rendaFamiliarMantemGenroNora;
    }

    public void setRendaFamiliarMantemGenroNora(String rendaFamiliarMantemGenroNora) {
        this.rendaFamiliarMantemGenroNora = rendaFamiliarMantemGenroNora;
    }

    public String getRendaFamiliarMantemSobrinho() {
        return rendaFamiliarMantemSobrinho;
    }

    public void setRendaFamiliarMantemSobrinho(String rendaFamiliarMantemSobrinho) {
        this.rendaFamiliarMantemSobrinho = rendaFamiliarMantemSobrinho;
    }

    public String getRendaFamiliarMantemEntenado() {
        return rendaFamiliarMantemEntenado;
    }

    public void setRendaFamiliarMantemEntenado(String rendaFamiliarMantemEntenado) {
        this.rendaFamiliarMantemEntenado = rendaFamiliarMantemEntenado;
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

    public Double getBeneficioSocialAposentadoriaValor() {
        return beneficioSocialAposentadoriaValor;
    }

    public void setBeneficioSocialAposentadoriaValor(Double beneficioSocialAposentadoriaValor) {
        this.beneficioSocialAposentadoriaValor = beneficioSocialAposentadoriaValor;
    }

    public Double getBeneficioSocialBeneficioFamiliaValor() {
        return beneficioSocialBeneficioFamiliaValor;
    }

    public void setBeneficioSocialBeneficioFamiliaValor(Double beneficioSocialBeneficioFamiliaValor) {
        this.beneficioSocialBeneficioFamiliaValor = beneficioSocialBeneficioFamiliaValor;
    }

    public Double getBeneficioSocialIncapacidadeValor() {
        return beneficioSocialIncapacidadeValor;
    }

    public void setBeneficioSocialIncapacidadeValor(Double beneficioSocialIncapacidadeValor) {
        this.beneficioSocialIncapacidadeValor = beneficioSocialIncapacidadeValor;
    }

    public Double getBeneficioSocialINSSValor() {
        return beneficioSocialINSSValor;
    }

    public void setBeneficioSocialINSSValor(Double beneficioSocialINSSValor) {
        this.beneficioSocialINSSValor = beneficioSocialINSSValor;
    }

    public Double getBeneficioSocialBolsaFamiliaValor() {
        return beneficioSocialBolsaFamiliaValor;
    }

    public void setBeneficioSocialBolsaFamiliaValor(Double beneficioSocialBolsaFamiliaValor) {
        this.beneficioSocialBolsaFamiliaValor = beneficioSocialBolsaFamiliaValor;
    }

    public Double getRendaFamiliarValor() {
        return rendaFamiliarValor;
    }

    public void setRendaFamiliarValor(Double rendaFamiliarValor) {
        this.rendaFamiliarValor = rendaFamiliarValor;
    }

    public ArrayList<ComposicaoFamiliar> getListaComposicaoFamiliar() {
        return listaComposicaoFamiliar;
    }

    public void setListaComposicaoFamiliar(ArrayList<ComposicaoFamiliar> listaComposicaoFamiliar) {
        this.listaComposicaoFamiliar = listaComposicaoFamiliar;
    }
}
