package br.gov.al.maceio.sishosp.questionario.dao;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.questionario.model.Pestalozzi;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PestalozziDAO {
    //conexao = ConnectionFactory.getConnection();
    Connection conexao = null;

    public Boolean gravarQuestionario(Pestalozzi pestalozzi, Integer idPaciente, Integer idUsuarioCadastroEntrevista) throws ProjetoException {
        boolean retorno = false;
        Integer idInsercao = null;
        String sql = "INSERT INTO questionario_social.questionario_social_pestalozzimaceio( " +
                "id_paciente, relato_vida, parecer_social_encaminhamentos, usuario_cadastro_entrevista, datahora_cadastro_entrevista) " +
                "VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP) RETURNING id ";
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, idPaciente);
            stmt.setString(2, pestalozzi.getRelatoVida());
            stmt.setString(3, pestalozzi.getParecerSocialEncaminhamento());
            stmt.setInt(4, idUsuarioCadastroEntrevista);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                idInsercao = rs.getInt("id");
            }

            if (gravarQuestionarioSaude(pestalozzi, idInsercao, conexao)) {
                if (gravarQuestionarioEducacao(pestalozzi, idInsercao, conexao)) {
                    if (gravarQuestionarioBeneficioSocial(pestalozzi, idInsercao, conexao)) {
                        if (gravarQuestionarioTransporte(pestalozzi, idInsercao, conexao)) {
                            if (gravarQuestionarioRendaFamilar(pestalozzi, idInsercao, conexao)) {
                                if (gravarQuestionarioHabitacao(pestalozzi, idInsercao, conexao)) {
                                    conexao.commit();
                                    retorno = true;
                                } else {
                                    throw new SQLException();
                                }
                            } else {
                                throw new SQLException();
                            }
                        } else {
                            throw new SQLException();
                        }
                    } else {
                        throw new SQLException();
                    }
                } else {
                    throw new SQLException();
                }
            } else {
                throw new SQLException();
            }

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.exit(1);
            }
        }
        return retorno;
    }

    public Boolean gravarQuestionarioSaude(Pestalozzi p, Integer idInsercao, Connection conexaoAuxiliar) throws ProjetoException {
        boolean retorno = false;
        String sql = "UPDATE questionario_social.questionario_social_pestalozzimaceio " +
                "SET saude_realiza_atend_instituicao=?, saude_unidade_atend=?, saude_serv_atend_rede_reab_aud=?, saude_serv_atend_rede_reab_fis=?, saude_serv_atend_rede_reab_int=?, saude_serv_atend_rede_reab_vis=?, saude_serv_ambulatorio=?, saude_outros_serv_homecare=?, saude_outros_serv_equoterapia=?, saude_outros_serv_grupo_convivencia=?, saude_outros_serv_grupo_terap_fam=?, saude_outros_serv_orteseprotese=?, saude_outros_serv_oficina=?, saude_outros_serv_estimul_precoce=?, saude_outros_serv_hidroterapia=?, saude_uso_opm=?, saude_uso_opm_tempo=?, saude_uso_opm_qual=?, saude_uso_equip_ortese=?, saude_uso_equip_aasi=?, saude_uso_equip_aux_optico=?, saude_uso_equip_meios_aux_loc=?, saude_uso_equip_protese=?, saude_uso_equip_oculos_adapt=?, saude_uso_equip_quanto_tempo=?, saude_uso_equip_entidade_concedeu=?, saude_uso_equip_realizou_reab=?, saude_uso_equip_realizou_reab_onde=?, saude_uso_equip_alguem_fam_deficiencia=?, saude_uso_equip_alguem_fam_deficiencia_parentesco=?, saude_uso_equip_morbidade_cid=?, saude_uso_equip_morbidade_cid_quanto_tempo=?, saude_uso_equip_causa_doenca=?, saude_uso_equip_residencia_coberta_psf=?, saude_uso_equip_visita_agenda_saude=?, saude_uso_equip_como_atendido_doente=?, saude_uso_equip_posto_saude_regiao=?, saude_uso_equip_faz_uso_medicacao=?, saude_uso_equip_faz_uso_qual_medicacao=?, saude_medicacao_frequencia=? " +
                "WHERE id = ?;";
        try {
            PreparedStatement stmt = conexaoAuxiliar.prepareStatement(sql);
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getSaudeRealizaAtendimentoNestaInstituicao())) {
                stmt.setBoolean(1, p.getSaudeRealizaAtendimentoNestaInstituicao());
            } else {
                stmt.setNull(1, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getSaudeUnidade())) {
                stmt.setString(2, p.getSaudeUnidade());
            } else {
                stmt.setNull(2, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getSaudeServicoAtendimentoRedeReabilitacaoAuditiva())) {
                stmt.setBoolean(3, p.getSaudeServicoAtendimentoRedeReabilitacaoAuditiva());
            } else {
                stmt.setNull(3, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getSaudeServicoAtendimentoRedeReabilitacaoFisica())) {
                stmt.setBoolean(4, p.getSaudeServicoAtendimentoRedeReabilitacaoFisica());
            } else {
                stmt.setNull(4, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getSaudeServicoAtendimentoRedeReabilitacaoIntelectual())) {
                stmt.setBoolean(5, p.getSaudeServicoAtendimentoRedeReabilitacaoIntelectual());
            } else {
                stmt.setNull(5, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getSaudeServicoAtendimentoRedeReabilitacaoVisual())) {
                stmt.setBoolean(6, p.getSaudeServicoAtendimentoRedeReabilitacaoVisual());
            } else {
                stmt.setNull(6, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getSaudeServicoAmbulatorio())) {
                stmt.setString(7, p.getSaudeServicoAmbulatorio());
            } else {
                stmt.setNull(7, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getSaudeOutrosServicosHomeCare())) {
                stmt.setBoolean(8, p.getSaudeOutrosServicosHomeCare());
            } else {
                stmt.setNull(8, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getSaudeOutrosServicosEquoterapia())) {
                stmt.setBoolean(9, p.getSaudeOutrosServicosEquoterapia());
            } else {
                stmt.setNull(9, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getSaudeOutrosServicosGrupoConvivencia())) {
                stmt.setBoolean(10, p.getSaudeOutrosServicosGrupoConvivencia());
            } else {
                stmt.setNull(10, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getSaudeOutrosServicosGrupoTerapeuticoFamiliar())) {
                stmt.setBoolean(11, p.getSaudeOutrosServicosGrupoTerapeuticoFamiliar());
            } else {
                stmt.setNull(11, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getSaudeOutrosServicosOrteseProtesesMeiosLocomocao())) {
                stmt.setBoolean(12, p.getSaudeOutrosServicosOrteseProtesesMeiosLocomocao());
            } else {
                stmt.setNull(12, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getSaudeOutrosServicosOficinaOrtopedica())) {
                stmt.setBoolean(13, p.getSaudeOutrosServicosOficinaOrtopedica());
            } else {
                stmt.setNull(13, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getSaudeOutrosServicosEstimulacaoPrecoce())) {
                stmt.setBoolean(14, p.getSaudeOutrosServicosEstimulacaoPrecoce());
            } else {
                stmt.setNull(14, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getSaudeOutrosServicosHidroTerapia())) {
                stmt.setBoolean(15, p.getSaudeOutrosServicosHidroTerapia());
            } else {
                stmt.setNull(15, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getSaudeUsoOrteseProtese())) {
                stmt.setString(16, p.getSaudeUsoOrteseProtese());
            } else {
                stmt.setNull(16, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getSaudeUsoOrteseProteseQual())) {
                stmt.setString(17, p.getSaudeUsoOrteseProteseQual());
            } else {
                stmt.setNull(17, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getSaudeUsoOrteseProteseQuantoTempo())) {
                stmt.setString(18, p.getSaudeUsoOrteseProteseQuantoTempo());
            } else {
                stmt.setNull(18, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getSaudeUsaEquipamentoOrtopedicoOrtese())) {
                stmt.setBoolean(19, p.getSaudeUsaEquipamentoOrtopedicoOrtese());
            } else {
                stmt.setNull(19, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getSaudeUsaEquipamentoOrtopedicoAASI())) {
                stmt.setBoolean(20, p.getSaudeUsaEquipamentoOrtopedicoAASI());
            } else {
                stmt.setNull(20, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getSaudeUsaEquipamentoOrtopedicoAuxilioOptico())) {
                stmt.setBoolean(21, p.getSaudeUsaEquipamentoOrtopedicoAuxilioOptico());
            } else {
                stmt.setNull(21, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getSaudeUsaEquipamentoOrtopedicoMeiosAuxiliaresDeLocomocao())) {
                stmt.setBoolean(22, p.getSaudeUsaEquipamentoOrtopedicoMeiosAuxiliaresDeLocomocao());
            } else {
                stmt.setNull(22, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getSaudeUsaEquipamentoOrtopedicoProtese())) {
                stmt.setBoolean(23, p.getSaudeUsaEquipamentoOrtopedicoProtese());
            } else {
                stmt.setNull(23, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getSaudeUsaEquipamentoOrtopedicoOculosAdaptado())) {
                stmt.setBoolean(24, p.getSaudeUsaEquipamentoOrtopedicoOculosAdaptado());
            } else {
                stmt.setNull(24, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getSaudeUsaEquipamentoOrtopedicoQuantoTempo())) {
                stmt.setString(25, p.getSaudeUsaEquipamentoOrtopedicoQuantoTempo());
            } else {
                stmt.setNull(25, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getSaudeUsaEquipamentoOrtopedicoEntidadeQueConcedeu())) {
                stmt.setString(26, p.getSaudeUsaEquipamentoOrtopedicoEntidadeQueConcedeu());
            } else {
                stmt.setNull(26, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getSaudeUsaEquipamentoOrtopedicoRealizouReabilitacao())) {
                stmt.setString(27, p.getSaudeUsaEquipamentoOrtopedicoRealizouReabilitacao());
            } else {
                stmt.setNull(27, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getSaudeUsaEquipamentoOrtopedicoRealizouReabilitacaoOnde())) {
                stmt.setString(28, p.getSaudeUsaEquipamentoOrtopedicoRealizouReabilitacaoOnde());
            } else {
                stmt.setNull(28, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getSaudeUsaEquipamentoOrtopedicoAlguemNaFamiliaComDeficiencia())) {
                stmt.setBoolean(29, p.getSaudeUsaEquipamentoOrtopedicoAlguemNaFamiliaComDeficiencia());
            } else {
                stmt.setNull(29, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getSaudeUsaEquipamentoOrtopedicoAlguemNaFamiliaComDeficienciaParentesco())) {
                stmt.setString(30, p.getSaudeUsaEquipamentoOrtopedicoAlguemNaFamiliaComDeficienciaParentesco());
            } else {
                stmt.setNull(30, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getSaudeUsaEquipamentoOrtopedicoMorbidadeCID())) {
                stmt.setString(31, p.getSaudeUsaEquipamentoOrtopedicoMorbidadeCID());
            } else {
                stmt.setNull(31, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getSaudeUsaEquipamentoOrtopedicoMorbidadeCIDQuantoTempo())) {
                stmt.setString(32, p.getSaudeUsaEquipamentoOrtopedicoMorbidadeCIDQuantoTempo());
            } else {
                stmt.setNull(32, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getSaudeUsaEquipamentoOrtopedicoCausaDoencaDeficiencia())) {
                stmt.setString(33, p.getSaudeUsaEquipamentoOrtopedicoCausaDoencaDeficiencia());
            } else {
                stmt.setNull(33, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getSaudeUsaEquipamentoOrtopedicoResidenciaCobertaPSF())) {
                stmt.setString(34, p.getSaudeUsaEquipamentoOrtopedicoResidenciaCobertaPSF());
            } else {
                stmt.setNull(34, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getSaudeUsaEquipamentoOrtopedicoRecebeuVisitaAgenteSaude())) {
                stmt.setBoolean(35, p.getSaudeUsaEquipamentoOrtopedicoRecebeuVisitaAgenteSaude());
            } else {
                stmt.setNull(35, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getSaudeUsaEquipamentoOrtopedicoFazParaSerAtendidoDoente())) {
                stmt.setString(36, p.getSaudeUsaEquipamentoOrtopedicoFazParaSerAtendidoDoente());
            } else {
                stmt.setNull(36, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getSaudeUsaEquipamentoOrtopedicoPostoSaudeRegiaoReside())) {
                stmt.setString(37, p.getSaudeUsaEquipamentoOrtopedicoPostoSaudeRegiaoReside());
            } else {
                stmt.setNull(37, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getSaudeUsaEquipamentoOrtopedicoUsoMedicacao())) {
                stmt.setBoolean(38, p.getSaudeUsaEquipamentoOrtopedicoUsoMedicacao());
            } else {
                stmt.setNull(38, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getSaudeUsaEquipamentoOrtopedicoUsoMedicacaoQual())) {
                stmt.setString(39, p.getSaudeUsaEquipamentoOrtopedicoUsoMedicacaoQual());
            } else {
                stmt.setNull(39, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getSaudeUsaEquipamentoOrtopedicoUsoMedicacaoFrequencia())) {
                stmt.setString(40, p.getSaudeUsaEquipamentoOrtopedicoUsoMedicacaoFrequencia());
            } else {
                stmt.setNull(40, java.sql.Types.NULL);
            }
            stmt.setInt(41, idInsercao);

            stmt.executeUpdate();
            retorno = true;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return retorno;
    }


    public Boolean gravarQuestionarioEducacao(Pestalozzi p, Integer idInsercao, Connection conexaoAuxiliar) throws ProjetoException {
        boolean retorno = false;
        String sql = "UPDATE questionario_social.questionario_social_pestalozzimaceio " +
                "SET educacao_matriculado=?, educacao_escolaridade=?, educacao_turno=?, educacao_escola_matriculado=?, educacao_acesso_escola=?, educacao_escola_fica_no_bairro=?, educacao_escola_qual_bairro=?, educacao_escola_tem_sala_recurso=?, educacao_recebe_apoio_aux_sala=?, educacao_resp_part_reuniao_escola=?, educacao_ativ_institucional_possui=?, educacao_qual_ativ_instit_aee=?, educacao_qual_ativ_instit_eja=?, educacao_qual_ativ_instit_esporte=?, educacao_qual_ativ_instit_oficina_prof=?, educacao_qual_ativ_instit_jovem_aprendiz=?, educacao_qual_ativ_instit_arte=?, educacao_ativ_complem_possui=?, educacao_ativ_complem_danca=?, educacao_ativ_complem_capoeira=?, educacao_ativ_complem_teatro=?, educacao_ativ_complem_percussao=?, educacao_ativ_complem_musica=?, educacao_ativ_complem_coral=? " +
                "WHERE id = ?;";
        try {
            PreparedStatement stmt = conexaoAuxiliar.prepareStatement(sql);
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getEducacaoMatriculado())) {
                stmt.setBoolean(1, p.getEducacaoMatriculado());
            } else {
                stmt.setNull(1, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getEducacaoEscolaridade())) {
                stmt.setString(2, p.getEducacaoEscolaridade());
            } else {
                stmt.setNull(2, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getEducacaoTurno())) {
                stmt.setString(3, p.getEducacaoTurno());
            } else {
                stmt.setNull(3, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getEducacaoEscolaEstaMatriculado())) {
                stmt.setString(4, p.getEducacaoEscolaEstaMatriculado());
            } else {
                stmt.setNull(4, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getEducacaoAcessoEscola())) {
                stmt.setString(5, p.getEducacaoAcessoEscola());
            } else {
                stmt.setNull(5, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getEducacaoEscolaFicaNoBairro())) {
                stmt.setString(6, p.getEducacaoEscolaFicaNoBairro());
            } else {
                stmt.setNull(6, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getEducacaoEscolaQualBairro())) {
                stmt.setString(7, p.getEducacaoEscolaQualBairro());
            } else {
                stmt.setNull(7, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getEducacaoEscolaTemSalaDeRecurso())) {
                stmt.setBoolean(8, p.getEducacaoEscolaTemSalaDeRecurso());
            } else {
                stmt.setNull(8, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getEducacaoRecebeApoioAuxiliarSala())) {
                stmt.setString(9, p.getEducacaoRecebeApoioAuxiliarSala());
            } else {
                stmt.setNull(9, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getEducacaoResponsavelParticipaReuniaoEscolar())) {
                stmt.setString(10, p.getEducacaoResponsavelParticipaReuniaoEscolar());
            } else {
                stmt.setNull(10, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getEducacaoAtividadeInstituconalPossui())) {
                stmt.setBoolean(11, p.getEducacaoAtividadeInstituconalPossui());
            } else {
                stmt.setNull(11, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getEducacaoAtividadeInstituconalQualAEE())) {
                stmt.setBoolean(12, p.getEducacaoAtividadeInstituconalQualAEE());
            } else {
                stmt.setNull(12, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getEducacaoAtividadeInstituconalQualEJA())) {
                stmt.setBoolean(13, p.getEducacaoAtividadeInstituconalQualEJA());
            } else {
                stmt.setNull(13, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getEducacaoAtividadeInstituconalQualEsporte())) {
                stmt.setBoolean(14, p.getEducacaoAtividadeInstituconalQualEsporte());
            } else {
                stmt.setNull(14, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getEducacaoAtividadeInstituconalQualOficinaProf())) {
                stmt.setBoolean(15, p.getEducacaoAtividadeInstituconalQualOficinaProf());
            } else {
                stmt.setNull(15, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getEducacaoAtividadeInstituconalQualJovemAprendiz())) {
                stmt.setBoolean(16, p.getEducacaoAtividadeInstituconalQualJovemAprendiz());
            } else {
                stmt.setNull(16, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getEducacaoAtividadeInstituconalQualArteCultura())) {
                stmt.setBoolean(17, p.getEducacaoAtividadeInstituconalQualArteCultura());
            } else {
                stmt.setNull(17, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getEducacaoAtividadeComplementaresPossui())) {
                stmt.setBoolean(18, p.getEducacaoAtividadeComplementaresPossui());
            } else {
                stmt.setNull(18, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getEducacaoAtividadeComplementaresDanca())) {
                stmt.setBoolean(19, p.getEducacaoAtividadeComplementaresDanca());
            } else {
                stmt.setNull(19, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getEducacaoAtividadeComplementaresCapoeira())) {
                stmt.setBoolean(20, p.getEducacaoAtividadeComplementaresCapoeira());
            } else {
                stmt.setNull(20, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getEducacaoAtividadeComplementaresTeatro())) {
                stmt.setBoolean(21, p.getEducacaoAtividadeComplementaresTeatro());
            } else {
                stmt.setNull(21, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getEducacaoAtividadeComplementaresPercussao())) {
                stmt.setBoolean(22, p.getEducacaoAtividadeComplementaresPercussao());
            } else {
                stmt.setNull(22, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getEducacaoAtividadeComplementaresMusica())) {
                stmt.setBoolean(23, p.getEducacaoAtividadeComplementaresMusica());
            } else {
                stmt.setNull(23, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getEducacaoAtividadeComplementaresCoral())) {
                stmt.setBoolean(24, p.getEducacaoAtividadeComplementaresCoral());
            } else {
                stmt.setNull(24, java.sql.Types.NULL);
            }
            stmt.setInt(25, idInsercao);

            stmt.executeUpdate();
            retorno = true;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return retorno;
    }

    public Boolean gravarQuestionarioBeneficioSocial(Pestalozzi p, Integer idInsercao, Connection conexaoAuxiliar) throws ProjetoException {
        boolean retorno = false;
        String sql = "UPDATE questionario_social.questionario_social_pestalozzimaceio " +
                "SET benefsociais_possui_apos=?, benefsociais_tipo_apos=?, benefsociais_tempo_apos=?, benefsociais_valor_apos=?, benefsociais_benef_familia=?, benefsociais_benef_familia_tipobenef=?, benefsociais_tempo_benef_familia=?, benefsociais_valor_benef_familia=?, benefsociais_possui_benef_inss=?, benefsociais_tempo_benef_inss=?, benefsociais_tipo_benef_inss=?, benefsociais_valor_benef_inss=?, benefsociais_possui_prog_sociais=?, benefsociais_possui_prog_leite=?, benefsociais_possui_prog_sopa=?, benefsociais_possui_bolsa_fam=?, benefsociais_tempo_bolsa_fam=?, benefsociais_valor_bolsa_fam=?, benefsociais_possui_minhacasa_minhavida=?, benefsociais_possui_cad_unico=?, benefsociais_possui_numero_nis=?, benefsociais_numeronis=?, benefsociais_incapacidade=?, benefsociais_tipo_incapacidade=?, benefsociais_tempo_incapacidade=?, benefsociais_valor_incapacidade=? " +
                "WHERE id = ?;";
        try {
            PreparedStatement stmt = conexaoAuxiliar.prepareStatement(sql);
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getBeneficioSocialAposentadoriaPossui())) {
                stmt.setBoolean(1, p.getBeneficioSocialAposentadoriaPossui());
            } else {
                stmt.setNull(1, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getBeneficioSocialAposentadoriaTipo())) {
                stmt.setString(2, p.getBeneficioSocialAposentadoriaTipo());
            } else {
                stmt.setNull(2, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getBeneficioSocialAposentadoriaQuantoTempo())) {
                stmt.setString(3, p.getBeneficioSocialAposentadoriaQuantoTempo());
            } else {
                stmt.setNull(3, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuZero(p.getBeneficioSocialAposentadoriaValor())) {
                stmt.setDouble(4, p.getBeneficioSocialAposentadoriaValor());
            } else {
                stmt.setNull(4, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getBeneficioSocialBeneficioFamiliaPossui())) {
                stmt.setBoolean(5, p.getBeneficioSocialBeneficioFamiliaPossui());
            } else {
                stmt.setNull(5, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getBeneficioSocialBeneficioFamiliaTipo())) {
                stmt.setString(6, p.getBeneficioSocialBeneficioFamiliaTipo());
            } else {
                stmt.setNull(6, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getBeneficioSocialBeneficioFamiliaQuantoTempo())) {
                stmt.setString(7, p.getBeneficioSocialBeneficioFamiliaQuantoTempo());
            } else {
                stmt.setNull(7, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuZero(p.getBeneficioSocialBeneficioFamiliaValor())) {
                stmt.setDouble(8, p.getBeneficioSocialBeneficioFamiliaValor());
            } else {
                stmt.setNull(8, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getBeneficioSocialINSSPossui())) {
                stmt.setBoolean(9, p.getBeneficioSocialINSSPossui());
            } else {
                stmt.setNull(9, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getBeneficioSocialINSSQuantoTempo())) {
                stmt.setString(10, p.getBeneficioSocialINSSQuantoTempo());
            } else {
                stmt.setNull(10, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getBeneficioSocialINSSTipo())) {
                stmt.setString(11, p.getBeneficioSocialINSSTipo());
            } else {
                stmt.setNull(11, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuZero(p.getBeneficioSocialINSSValor())) {
                stmt.setDouble(12, p.getBeneficioSocialINSSValor());
            } else {
                stmt.setNull(12, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getBeneficioSocialProgramaSociaisPossui())) {
                stmt.setBoolean(13, p.getBeneficioSocialProgramaSociaisPossui());
            } else {
                stmt.setNull(13, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getBeneficioSocialProgramaLeitePossui())) {
                stmt.setString(14, p.getBeneficioSocialProgramaLeitePossui());
            } else {
                stmt.setNull(14, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getBeneficioSocialProgramaSopaPossui())) {
                stmt.setString(15, p.getBeneficioSocialProgramaSopaPossui());
            } else {
                stmt.setNull(15, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getBeneficioSocialBolsaFamiliaPossui())) {
                stmt.setBoolean(16, p.getBeneficioSocialBolsaFamiliaPossui());
            } else {
                stmt.setNull(16, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getBeneficioSocialBolsaFamiliaQuantoTempo())) {
                stmt.setString(17, p.getBeneficioSocialBolsaFamiliaQuantoTempo());
            } else {
                stmt.setNull(17, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuZero(p.getBeneficioSocialBolsaFamiliaValor())) {
                stmt.setDouble(18, p.getBeneficioSocialBolsaFamiliaValor());
            } else {
                stmt.setNull(18, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getBeneficioSocialMinhaCasaMinhaVidaPossui())) {
                stmt.setBoolean(19, p.getBeneficioSocialMinhaCasaMinhaVidaPossui());
            } else {
                stmt.setNull(19, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getBeneficioSocialCadastroUnico())) {
                stmt.setString(20, p.getBeneficioSocialCadastroUnico());
            } else {
                stmt.setNull(20, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getBeneficioSocialNumeroNisPossui())) {
                stmt.setString(21, p.getBeneficioSocialNumeroNisPossui());
            } else {
                stmt.setNull(21, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getBeneficioSocialNumeroNis())) {
                stmt.setString(22, p.getBeneficioSocialNumeroNis());
            } else {
                stmt.setNull(22, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getBeneficioSocialIncapacidadePossui())) {
                stmt.setBoolean(23, p.getBeneficioSocialIncapacidadePossui());
            } else {
                stmt.setNull(23, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getBeneficioSocialIncapacidadeTipo())) {
                stmt.setString(24, p.getBeneficioSocialIncapacidadeTipo());
            } else {
                stmt.setNull(24, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getBeneficioSocialIncapacidadeQuantoTempo())) {
                stmt.setString(25, p.getBeneficioSocialIncapacidadeQuantoTempo());
            } else {
                stmt.setNull(25, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuZero(p.getBeneficioSocialIncapacidadeValor())) {
                stmt.setDouble(26, p.getBeneficioSocialIncapacidadeValor());
            } else {
                stmt.setNull(26, java.sql.Types.NULL);
            }
            stmt.setInt(27, idInsercao);
            stmt.executeUpdate();
            retorno = true;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return retorno;
    }

    public Boolean gravarQuestionarioTransporte(Pestalozzi p, Integer idInsercao, Connection conexaoAuxiliar) throws ProjetoException {
        boolean retorno = false;
        String sql = "UPDATE questionario_social.questionario_social_pestalozzimaceio " +
                "SET transporte_possui_cart_transp=?, transporte_cart_transp_acompanhante=?, transporte_tipo_carteira=?, transporte_utilizado_tratamento=?, transporte_qtd_onibus_ate_instituicao=?, transporte_acessibilidade_acesso_percurso_instit=? " +
                "WHERE id = ?;";
        try {
            PreparedStatement stmt = conexaoAuxiliar.prepareStatement(sql);
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getTransporteCarteiraTransportePossui())) {
                stmt.setBoolean(1, p.getTransporteCarteiraTransportePossui());
            } else {
                stmt.setNull(1, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getTransporteCarteiraTransporteAcompanhate())) {
                stmt.setString(2, p.getTransporteCarteiraTransporteAcompanhate());
            } else {
                stmt.setNull(2, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getTransporteCarteiraTransporteTipo())) {
                stmt.setString(3, p.getTransporteCarteiraTransporteTipo());
            } else {
                stmt.setNull(3, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getTransporteUtilizadoTratamento())) {
                stmt.setString(4, p.getTransporteUtilizadoTratamento());
            } else {
                stmt.setNull(4, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getTransporteQuantidadeOnibusAteInstituicao())) {
                stmt.setString(5, p.getTransporteQuantidadeOnibusAteInstituicao());
            } else {
                stmt.setNull(5, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getTransporteExisteAcessibilidadeAcessoPercusoInstituicao())) {
                stmt.setString(6, p.getTransporteExisteAcessibilidadeAcessoPercusoInstituicao());
            } else {
                stmt.setNull(6, java.sql.Types.NULL);
            }
            stmt.setInt(7, idInsercao);
            stmt.executeUpdate();
            retorno = true;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return retorno;
    }


    public Boolean gravarQuestionarioRendaFamilar(Pestalozzi p, Integer idInsercao, Connection conexaoAuxiliar) throws ProjetoException {
        boolean retorno = false;
        String sql = "UPDATE questionario_social.questionario_social_pestalozzimaceio " +
                "SET rendafam_inserida_merc_trab=?, rendafam_profissao_funcao=?, rendafam_atividade_antes_agravo=?, rendafam_tempo_atividade_antes_agravo=?, rendafam_valor=?, rendafam_mantem_fam_usuario=?, rendafam_mantem_fam_pai=?, rendafam_mantem_fam_mae=?, rendafam_mantem_fam_irmao=?, rendafam_mantem_fam_avo=?, rendafam_mantem_fam_cuidador=?, rendafam_mantem_fam_tio=?, rendafam_mantem_fam_esposo=?, rendafam_mantem_fam_vizinho=?, rendafam_mantem_fam_filho=?, rendafam_mantem_fam_padastro_madastra=?, rendafam_mantem_fam_genro_nora=?, rendafam_mantem_fam_sobrinho=?, rendafam_mantem_fam_enteado=? " +
                "WHERE id = ?;";
        try {
            PreparedStatement stmt = conexaoAuxiliar.prepareStatement(sql);


            if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getRendaFamiliarInseridoMercadoTrabalho())) {
                stmt.setString(1, p.getRendaFamiliarInseridoMercadoTrabalho());
            } else {
                stmt.setNull(1, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getRendaFamiliarProfissaoFuncao())) {
                stmt.setString(2, p.getRendaFamiliarProfissaoFuncao());
            } else {
                stmt.setNull(2, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getRendaFamiliarAtividadeAntesAgravo())) {
                stmt.setString(3, p.getRendaFamiliarAtividadeAntesAgravo());
            } else {
                stmt.setNull(3, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getRendaFamiliarTempoAntesAgravo())) {
                stmt.setString(4, p.getRendaFamiliarTempoAntesAgravo());
            } else {
                stmt.setNull(4, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuZero(p.getRendaFamiliarValor())) {
                stmt.setDouble(5, p.getRendaFamiliarValor());
            } else {
                stmt.setNull(5, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getRendaFamiliarMantemUsuario())) {
                stmt.setBoolean(6, p.getRendaFamiliarMantemUsuario());
            } else {
                stmt.setNull(6, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getRendaFamiliarMantemPai())) {
                stmt.setBoolean(7, p.getRendaFamiliarMantemPai());
            } else {
                stmt.setNull(7, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getRendaFamiliarMantemMae())) {
                stmt.setBoolean(8, p.getRendaFamiliarMantemMae());
            } else {
                stmt.setNull(8, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getRendaFamiliarMantemIrmao())) {
                stmt.setBoolean(9, p.getRendaFamiliarMantemIrmao());
            } else {
                stmt.setNull(9, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getRendaFamiliarMantemAvo())) {
                stmt.setBoolean(10, p.getRendaFamiliarMantemAvo());
            } else {
                stmt.setNull(10, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getRendaFamiliarMantemCuidador())) {
                stmt.setBoolean(11, p.getRendaFamiliarMantemCuidador());
            } else {
                stmt.setNull(11, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getRendaFamiliarMantemTio())) {
                stmt.setBoolean(12, p.getRendaFamiliarMantemTio());
            } else {
                stmt.setNull(12, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getRendaFamiliarMantemEsposo())) {
                stmt.setBoolean(13, p.getRendaFamiliarMantemEsposo());
            } else {
                stmt.setNull(13, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getRendaFamiliarMantemVizinho())) {
                stmt.setBoolean(14, p.getRendaFamiliarMantemVizinho());
            } else {
                stmt.setNull(14, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getRendaFamiliarMantemFilho())) {
                stmt.setBoolean(15, p.getRendaFamiliarMantemFilho());
            } else {
                stmt.setNull(15, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getRendaFamiliarMantemPadastroMadastra())) {
                stmt.setBoolean(16, p.getRendaFamiliarMantemPadastroMadastra());
            } else {
                stmt.setNull(16, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getRendaFamiliarMantemGenroNora())) {
                stmt.setBoolean(17, p.getRendaFamiliarMantemGenroNora());
            } else {
                stmt.setNull(17, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getRendaFamiliarMantemSobrinho())) {
                stmt.setBoolean(18, p.getRendaFamiliarMantemSobrinho());
            } else {
                stmt.setNull(18, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getRendaFamiliarMantemEntenado())) {
                stmt.setBoolean(19, p.getRendaFamiliarMantemEntenado());
            } else {
                stmt.setNull(19, java.sql.Types.NULL);
            }
            stmt.setInt(20, idInsercao);
            stmt.executeUpdate();
            retorno = true;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return retorno;
    }

    public Boolean gravarQuestionarioHabitacao(Pestalozzi p, Integer idInsercao, Connection conexaoAuxiliar) throws ProjetoException {
        boolean retorno = false;
        String sql = "UPDATE questionario_social.questionario_social_pestalozzimaceio " +
                "SET habitacao_situacao_moradia=?, habitacao_tipo_construcao=?, habitacao_tipo_residencia=?, habitacao_num_comodos=?, habitacao_abastecimento_agua=?, habitacao_tipo_iluminacao=?, habitacao_escoamento_sanitario=?, habitacao_destino_lixo=?, habitacao_residencia_adaptada=? " +
                "WHERE id = ?;";
        try {
            PreparedStatement stmt = conexaoAuxiliar.prepareStatement(sql);

            if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getHabitacaoSitucaoMoradia())) {
                stmt.setString(1, p.getHabitacaoSitucaoMoradia());
            } else {
                stmt.setNull(1, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getHabitacaoTipoConstrucao())) {
                stmt.setString(2, p.getHabitacaoTipoConstrucao());
            } else {
                stmt.setNull(2, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getHabitacaoTipoResidencia())) {
                stmt.setString(3, p.getHabitacaoTipoResidencia());
            } else {
                stmt.setNull(3, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getHabitacaoNumeroComodo())) {
                stmt.setString(4, p.getHabitacaoNumeroComodo());
            } else {
                stmt.setNull(4, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getHabitacaoAbastecimentoDeAgua())) {
                stmt.setString(5, p.getHabitacaoAbastecimentoDeAgua());
            } else {
                stmt.setNull(5, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getHabitacaoTipoIluminacao())) {
                stmt.setString(6, p.getHabitacaoTipoIluminacao());
            } else {
                stmt.setNull(6, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getHabitacaoEscoamentoSanitario())) {
                stmt.setString(7, p.getHabitacaoEscoamentoSanitario());
            } else {
                stmt.setNull(7, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getHabitacaoDestinoLixo())) {
                stmt.setString(8, p.getHabitacaoDestinoLixo());
            } else {
                stmt.setNull(8, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getHabitacaoResidenciaAdaptada())) {
                stmt.setString(9, p.getHabitacaoResidenciaAdaptada());
            } else {
                stmt.setNull(9, java.sql.Types.NULL);
            }
            stmt.setInt(10, idInsercao);
            stmt.executeUpdate();
            retorno = true;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return retorno;
    }
}
