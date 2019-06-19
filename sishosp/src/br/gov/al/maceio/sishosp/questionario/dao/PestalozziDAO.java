package br.gov.al.maceio.sishosp.questionario.dao;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.questionario.model.ComposicaoFamiliar;
import br.gov.al.maceio.sishosp.questionario.model.Pestalozzi;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;

import javax.faces.context.FacesContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PestalozziDAO {
    //conexao = ConnectionFactory.getConnection();
    Connection conexao = null;
    FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
            .getSessionMap().get("obj_funcionario");

    public Boolean gravarQuestionario(Pestalozzi pestalozzi) throws ProjetoException {
        boolean retorno = false;
        Integer idInsercao = null;
        String sql = "INSERT INTO questionario_social.questionario_social_pestalozzimaceio( " +
                "id_paciente, relato_vida, parecer_social_encaminhamentos, usuario_cadastro_entrevista, datahora_cadastro_entrevista) " +
                "VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP) RETURNING id ";
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, pestalozzi.getPaciente().getId_paciente());
            stmt.setString(2, pestalozzi.getRelatoVida());
            stmt.setString(3, pestalozzi.getParecerSocialEncaminhamento());
            stmt.setLong(4, user_session.getId());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                idInsercao = rs.getInt("id");
            }
            if (pestalozzi.getListaComposicaoFamiliar().size() > 0) {
                if (gravarComposicaoFamiliar(pestalozzi.getListaComposicaoFamiliar(), idInsercao, conexao) == false) {
                    throw new SQLException();
                }
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
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getSaudeUsoOrteseProtese())) {
                stmt.setBoolean(16, p.getSaudeUsoOrteseProtese());
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
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getSaudeUsaEquipamentoOrtopedicoRealizouReabilitacao())) {
                stmt.setBoolean(27, p.getSaudeUsaEquipamentoOrtopedicoRealizouReabilitacao());
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
            if (!VerificadorUtil.verificarSeObjetoNuloOuZero(p.getSaudeUsaEquipamentoOrtopedicoAlguemNaFamiliaComDeficienciaParentesco())) {
                stmt.setInt(30, p.getSaudeUsaEquipamentoOrtopedicoAlguemNaFamiliaComDeficienciaParentesco());
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
            if (!VerificadorUtil.verificarSeObjetoNuloOuZero(p.getEducacaoEscolaridade())) {
                stmt.setInt(2, p.getEducacaoEscolaridade());
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
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getEducacaoEscolaFicaNoBairro())) {
                stmt.setBoolean(6, p.getEducacaoEscolaFicaNoBairro());
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
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getEducacaoRecebeApoioAuxiliarSala())) {
                stmt.setBoolean(9, p.getEducacaoRecebeApoioAuxiliarSala());
            } else {
                stmt.setNull(9, java.sql.Types.NULL);
            }
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getEducacaoResponsavelParticipaReuniaoEscolar())) {
                stmt.setBoolean(10, p.getEducacaoResponsavelParticipaReuniaoEscolar());
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
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getTransporteCarteiraTransporteAcompanhate())) {
                stmt.setBoolean(2, p.getTransporteCarteiraTransporteAcompanhate());
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
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getTransporteExisteAcessibilidadeAcessoPercusoInstituicao())) {
                stmt.setBoolean(6, p.getTransporteExisteAcessibilidadeAcessoPercusoInstituicao());
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


            if (!VerificadorUtil.verificarSeObjetoNulo(p.getRendaFamiliarInseridoMercadoTrabalho())) {
                stmt.setBoolean(1, p.getRendaFamiliarInseridoMercadoTrabalho());
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
            if (!VerificadorUtil.verificarSeObjetoNulo(p.getRendaFamiliarMantemEnteado())) {
                stmt.setBoolean(19, p.getRendaFamiliarMantemEnteado());
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
            if (!VerificadorUtil.verificarSeObjetoNuloOuZero(p.getHabitacaoNumeroComodo())) {
                stmt.setInt(4, p.getHabitacaoNumeroComodo());
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

    public Boolean gravarComposicaoFamiliar(ArrayList<ComposicaoFamiliar> listaComposicaoFamiliar, Integer idInsercao, Connection conexaoAuxiliar) throws ProjetoException {
        boolean retorno = false;
        String sql = "INSERT INTO questionario_social.composicao_familiar_pestalozzimaceio( " +
                "id_questionario_social, nome, sexo, idade, codparentesco, trabalha, valor_salario, pcd, benef) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
        try {
            PreparedStatement stmt = conexaoAuxiliar.prepareStatement(sql);
            for (ComposicaoFamiliar composicaoFamiliar : listaComposicaoFamiliar) {
                stmt.setInt(1, idInsercao);
                if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(composicaoFamiliar.getNome())) {
                    stmt.setString(2, composicaoFamiliar.getNome());
                } else {
                    stmt.setNull(2, java.sql.Types.NULL);
                }
                if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(composicaoFamiliar.getSexo())) {
                    stmt.setString(3, composicaoFamiliar.getSexo());
                } else {
                    stmt.setNull(3, java.sql.Types.NULL);
                }
                if (!VerificadorUtil.verificarSeObjetoNuloOuZero(composicaoFamiliar.getIdade())) {
                    stmt.setInt(4, composicaoFamiliar.getIdade());
                } else {
                    stmt.setNull(4, java.sql.Types.NULL);
                }
                if (!VerificadorUtil.verificarSeObjetoNuloOuZero(composicaoFamiliar.getParentesco())) {
                    stmt.setInt(5, composicaoFamiliar.getParentesco());
                } else {
                    stmt.setNull(5, java.sql.Types.NULL);
                }
                if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(composicaoFamiliar.getTrabalha())) {
                    stmt.setString(6, composicaoFamiliar.getTrabalha());
                } else {
                    stmt.setNull(6, java.sql.Types.NULL);
                }
                if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(composicaoFamiliar.getValor())) {
                    stmt.setDouble(7, composicaoFamiliar.getValor());
                } else {
                    stmt.setNull(7, java.sql.Types.NULL);
                }
                if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(composicaoFamiliar.getPcd())) {
                    stmt.setString(8, composicaoFamiliar.getPcd());
                } else {
                    stmt.setNull(8, java.sql.Types.NULL);
                }
                if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(composicaoFamiliar.getBenef())) {
                    stmt.setString(9, composicaoFamiliar.getBenef());
                } else {
                    stmt.setNull(9, java.sql.Types.NULL);
                }
                stmt.executeUpdate();
            }
            retorno = true;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return retorno;
    }

    public ArrayList<ComposicaoFamiliar> retornarComposicaoFamiliar(Integer idQuestionarioSocial, Connection conexaoAuxiliar) throws ProjetoException {
        ArrayList<ComposicaoFamiliar> listaComposicaoFamiliar = new ArrayList<>();
        String sql = "SELECT nome, sexo, idade, codparentesco, trabalha, valor_salario, pcd, benef " +
                "FROM questionario_social.composicao_familiar_pestalozzimaceio where id_questionario_social = ?;";
        try {
            PreparedStatement stmt = conexaoAuxiliar.prepareStatement(sql);
            stmt.setInt(1, idQuestionarioSocial);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ComposicaoFamiliar composicaoFamiliar = new ComposicaoFamiliar();
                composicaoFamiliar.setNome(rs.getString("nome"));
                composicaoFamiliar.setSexo(rs.getString("sexo"));
                composicaoFamiliar.setIdade(rs.getInt("idade"));
                composicaoFamiliar.setParentesco(rs.getInt("codparentesco"));
                composicaoFamiliar.setTrabalha(rs.getString("trabalha"));
                composicaoFamiliar.setValor(rs.getDouble("valor_salario"));
                composicaoFamiliar.setPcd(rs.getString("pcd"));
                composicaoFamiliar.setBenef(rs.getString("benef"));
                listaComposicaoFamiliar.add(composicaoFamiliar);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return listaComposicaoFamiliar;
    }

    public Pestalozzi retornarQuestionario(Integer idPaciente) throws ProjetoException {
        String sql = "SELECT id, id_paciente, saude_realiza_atend_instituicao, saude_unidade_atend, saude_serv_atend_rede_reab_aud, saude_serv_atend_rede_reab_fis, saude_serv_atend_rede_reab_int, saude_serv_atend_rede_reab_vis, saude_serv_ambulatorio, saude_outros_serv_homecare, saude_outros_serv_equoterapia, saude_outros_serv_grupo_convivencia, saude_outros_serv_grupo_terap_fam, saude_outros_serv_orteseprotese, saude_outros_serv_oficina, saude_outros_serv_estimul_precoce, saude_outros_serv_hidroterapia, saude_uso_opm, saude_uso_opm_tempo, saude_uso_opm_qual, saude_uso_equip_ortese, saude_uso_equip_aasi, saude_uso_equip_aux_optico, saude_uso_equip_meios_aux_loc, saude_uso_equip_protese, saude_uso_equip_oculos_adapt, saude_uso_equip_quanto_tempo, saude_uso_equip_entidade_concedeu, saude_uso_equip_realizou_reab, saude_uso_equip_realizou_reab_onde, saude_uso_equip_alguem_fam_deficiencia, saude_uso_equip_alguem_fam_deficiencia_parentesco, saude_uso_equip_morbidade_cid, saude_uso_equip_morbidade_cid_quanto_tempo, saude_uso_equip_causa_doenca, saude_uso_equip_residencia_coberta_psf, saude_uso_equip_visita_agenda_saude, saude_uso_equip_como_atendido_doente, saude_uso_equip_posto_saude_regiao, saude_uso_equip_faz_uso_medicacao, saude_uso_equip_faz_uso_qual_medicacao, saude_medicacao_frequencia, educacao_matriculado, educacao_escolaridade, educacao_turno, educacao_escola_matriculado, educacao_acesso_escola, educacao_escola_fica_no_bairro, educacao_escola_qual_bairro, educacao_escola_tem_sala_recurso, educacao_recebe_apoio_aux_sala, educacao_resp_part_reuniao_escola, educacao_ativ_institucional_possui, educacao_qual_ativ_instit_aee, educacao_qual_ativ_instit_eja, educacao_qual_ativ_instit_esporte, educacao_qual_ativ_instit_oficina_prof, educacao_qual_ativ_instit_jovem_aprendiz, educacao_qual_ativ_instit_arte, educacao_ativ_complem_possui, educacao_ativ_complem_danca, educacao_ativ_complem_capoeira, educacao_ativ_complem_teatro, educacao_ativ_complem_percussao, educacao_ativ_complem_musica, educacao_ativ_complem_coral, benefsociais_possui_apos, benefsociais_tipo_apos, benefsociais_tempo_apos, benefsociais_valor_apos, benefsociais_benef_familia, benefsociais_benef_familia_tipobenef, benefsociais_tempo_benef_familia, benefsociais_valor_benef_familia, benefsociais_possui_benef_inss, benefsociais_tempo_benef_inss, benefsociais_tipo_benef_inss, benefsociais_valor_benef_inss, benefsociais_possui_prog_sociais, benefsociais_possui_prog_leite, benefsociais_possui_prog_sopa, benefsociais_possui_bolsa_fam, benefsociais_tempo_bolsa_fam, benefsociais_valor_bolsa_fam, benefsociais_possui_minhacasa_minhavida, benefsociais_possui_cad_unico, benefsociais_possui_numero_nis, benefsociais_numeronis, transporte_possui_cart_transp, transporte_cart_transp_acompanhante, transporte_tipo_carteira, transporte_utilizado_tratamento, transporte_qtd_onibus_ate_instituicao, transporte_acessibilidade_acesso_percurso_instit, rendafam_inserida_merc_trab, rendafam_profissao_funcao, rendafam_atividade_antes_agravo, rendafam_tempo_atividade_antes_agravo, rendafam_valor, rendafam_mantem_fam_usuario, rendafam_mantem_fam_pai, rendafam_mantem_fam_mae, rendafam_mantem_fam_irmao, rendafam_mantem_fam_avo, rendafam_mantem_fam_cuidador, rendafam_mantem_fam_tio, rendafam_mantem_fam_esposo, rendafam_mantem_fam_vizinho, rendafam_mantem_fam_filho, rendafam_mantem_fam_padastro_madastra, rendafam_mantem_fam_genro_nora, rendafam_mantem_fam_sobrinho, rendafam_mantem_fam_enteado, habitacao_situacao_moradia, habitacao_tipo_construcao, habitacao_tipo_residencia, habitacao_num_comodos, habitacao_abastecimento_agua, habitacao_tipo_iluminacao, habitacao_escoamento_sanitario, habitacao_destino_lixo, habitacao_residencia_adaptada, relato_vida, parecer_social_encaminhamentos, usuario_cadastro_entrevista, datahora_cadastro_entrevista, benefsociais_incapacidade, benefsociais_tipo_incapacidade, benefsociais_tempo_incapacidade, benefsociais_valor_incapacidade " +
                "FROM questionario_social.questionario_social_pestalozzimaceio WHERE id_paciente = ? ORDER BY id DESC LIMIT 1";

        Pestalozzi p = new Pestalozzi();
        Integer idQuestionario = null;
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stm = conexao.prepareStatement(sql);
            stm.setLong(1, idPaciente);
            ResultSet rs = stm.executeQuery();
            p.getPaciente().setId_paciente(idPaciente);
            while (rs.next()) {
                //SAUDE
                idQuestionario = rs.getInt("id");
                if (!VerificadorUtil.verificarSeObjetoNulo(rs.getBoolean("saude_realiza_atend_instituicao"))) {
                    p.setSaudeRealizaAtendimentoNestaInstituicao(rs.getBoolean("saude_realiza_atend_instituicao"));
                }
                p.setSaudeUnidade(rs.getString("saude_unidade_atend"));
                p.setSaudeServicoAtendimentoRedeReabilitacaoAuditiva(rs.getBoolean("saude_serv_atend_rede_reab_aud"));
                p.setSaudeServicoAtendimentoRedeReabilitacaoFisica(rs.getBoolean("saude_serv_atend_rede_reab_fis"));
                p.setSaudeServicoAtendimentoRedeReabilitacaoIntelectual(rs.getBoolean("saude_serv_atend_rede_reab_int"));
                p.setSaudeServicoAtendimentoRedeReabilitacaoVisual(rs.getBoolean("saude_serv_atend_rede_reab_vis"));
                p.setSaudeServicoAmbulatorio(rs.getString("saude_serv_ambulatorio"));
                p.setSaudeOutrosServicosHomeCare(rs.getBoolean("saude_outros_serv_homecare"));
                p.setSaudeOutrosServicosEquoterapia(rs.getBoolean("saude_outros_serv_equoterapia"));
                p.setSaudeOutrosServicosGrupoConvivencia(rs.getBoolean("saude_outros_serv_grupo_convivencia"));
                p.setSaudeOutrosServicosGrupoTerapeuticoFamiliar(rs.getBoolean("saude_outros_serv_grupo_terap_fam"));
                p.setSaudeOutrosServicosOrteseProtesesMeiosLocomocao(rs.getBoolean("saude_outros_serv_orteseprotese"));
                p.setSaudeOutrosServicosOficinaOrtopedica(rs.getBoolean("saude_outros_serv_oficina"));
                p.setSaudeOutrosServicosEstimulacaoPrecoce(rs.getBoolean("saude_outros_serv_estimul_precoce"));
                p.setSaudeOutrosServicosHidroTerapia(rs.getBoolean("saude_outros_serv_hidroterapia"));
                if (!VerificadorUtil.verificarSeObjetoNulo(rs.getBoolean("saude_uso_opm"))) {
                    p.setSaudeUsoOrteseProtese(rs.getBoolean("saude_uso_opm"));
                }
                p.setSaudeUsoOrteseProteseQuantoTempo(rs.getString("saude_uso_opm_tempo"));
                p.setSaudeUsoOrteseProteseQual(rs.getString("saude_uso_opm_qual"));
                p.setSaudeUsaEquipamentoOrtopedicoOrtese(rs.getBoolean("saude_uso_equip_ortese"));
                p.setSaudeUsaEquipamentoOrtopedicoAASI(rs.getBoolean("saude_uso_equip_aasi"));
                p.setSaudeUsaEquipamentoOrtopedicoAuxilioOptico(rs.getBoolean("saude_uso_equip_aux_optico"));
                p.setSaudeUsaEquipamentoOrtopedicoMeiosAuxiliaresDeLocomocao(rs.getBoolean("saude_uso_equip_meios_aux_loc"));
                p.setSaudeUsaEquipamentoOrtopedicoProtese(rs.getBoolean("saude_uso_equip_protese"));
                p.setSaudeUsaEquipamentoOrtopedicoOculosAdaptado(rs.getBoolean("saude_uso_equip_oculos_adapt"));
                p.setSaudeUsaEquipamentoOrtopedicoQuantoTempo(rs.getString("saude_uso_equip_quanto_tempo"));
                p.setSaudeUsaEquipamentoOrtopedicoEntidadeQueConcedeu(rs.getString("saude_uso_equip_entidade_concedeu"));
                p.setSaudeUsaEquipamentoOrtopedicoRealizouReabilitacao(rs.getBoolean("saude_uso_equip_realizou_reab"));
                p.setSaudeUsaEquipamentoOrtopedicoRealizouReabilitacaoOnde(rs.getString("saude_uso_equip_realizou_reab_onde"));
                if (!VerificadorUtil.verificarSeObjetoNulo(rs.getBoolean("saude_uso_equip_alguem_fam_deficiencia"))) {
                    p.setSaudeUsaEquipamentoOrtopedicoAlguemNaFamiliaComDeficiencia(rs.getBoolean("saude_uso_equip_alguem_fam_deficiencia"));
                }
                p.setSaudeUsaEquipamentoOrtopedicoAlguemNaFamiliaComDeficienciaParentesco(rs.getInt("saude_uso_equip_alguem_fam_deficiencia_parentesco"));
                p.setSaudeUsaEquipamentoOrtopedicoMorbidadeCID(rs.getString("saude_uso_equip_morbidade_cid"));
                p.setSaudeUsaEquipamentoOrtopedicoMorbidadeCIDQuantoTempo(rs.getString("saude_uso_equip_morbidade_cid_quanto_tempo"));
                p.setSaudeUsaEquipamentoOrtopedicoCausaDoencaDeficiencia(rs.getString("saude_uso_equip_causa_doenca"));
                p.setSaudeUsaEquipamentoOrtopedicoResidenciaCobertaPSF(rs.getString("saude_uso_equip_residencia_coberta_psf"));
                if (!VerificadorUtil.verificarSeObjetoNulo(rs.getBoolean("saude_uso_equip_visita_agenda_saude"))) {
                    p.setSaudeUsaEquipamentoOrtopedicoRecebeuVisitaAgenteSaude(rs.getBoolean("saude_uso_equip_visita_agenda_saude"));
                }
                p.setSaudeUsaEquipamentoOrtopedicoFazParaSerAtendidoDoente(rs.getString("saude_uso_equip_como_atendido_doente"));
                p.setSaudeUsaEquipamentoOrtopedicoPostoSaudeRegiaoReside(rs.getString("saude_uso_equip_posto_saude_regiao"));
                if (!VerificadorUtil.verificarSeObjetoNulo(rs.getBoolean("saude_uso_equip_faz_uso_medicacao"))) {
                    p.setSaudeUsaEquipamentoOrtopedicoUsoMedicacao(rs.getBoolean("saude_uso_equip_faz_uso_medicacao"));
                }
                p.setSaudeUsaEquipamentoOrtopedicoUsoMedicacaoQual(rs.getString("saude_uso_equip_faz_uso_qual_medicacao"));
                p.setSaudeUsaEquipamentoOrtopedicoUsoMedicacaoFrequencia(rs.getString("saude_medicacao_frequencia"));

                //EDUCACAO
                p.setEducacaoMatriculado(rs.getBoolean("educacao_matriculado"));
                p.setEducacaoEscolaridade(rs.getInt("educacao_escolaridade"));
                p.setEducacaoTurno(rs.getString("educacao_turno"));
                p.setEducacaoEscolaEstaMatriculado(rs.getString("educacao_escola_matriculado"));
                p.setEducacaoAcessoEscola(rs.getString("educacao_acesso_escola"));
                p.setEducacaoEscolaFicaNoBairro(rs.getBoolean("educacao_escola_fica_no_bairro"));
                p.setEducacaoEscolaQualBairro(rs.getString("educacao_escola_qual_bairro"));
                p.setEducacaoEscolaTemSalaDeRecurso(rs.getBoolean("educacao_escola_tem_sala_recurso"));
                p.setEducacaoRecebeApoioAuxiliarSala(rs.getBoolean("educacao_recebe_apoio_aux_sala"));
                p.setEducacaoResponsavelParticipaReuniaoEscolar(rs.getBoolean("educacao_resp_part_reuniao_escola"));
                p.setEducacaoAtividadeInstituconalPossui(rs.getBoolean("educacao_ativ_institucional_possui"));
                p.setEducacaoAtividadeInstituconalQualAEE(rs.getBoolean("educacao_qual_ativ_instit_aee"));
                p.setEducacaoAtividadeInstituconalQualEJA(rs.getBoolean("educacao_qual_ativ_instit_eja"));
                p.setEducacaoAtividadeInstituconalQualEsporte(rs.getBoolean("educacao_qual_ativ_instit_esporte"));
                p.setEducacaoAtividadeInstituconalQualJovemAprendiz(rs.getBoolean("educacao_qual_ativ_instit_jovem_aprendiz"));
                p.setEducacaoAtividadeInstituconalQualArteCultura(rs.getBoolean("educacao_qual_ativ_instit_arte"));
                p.setEducacaoAtividadeComplementaresPossui(rs.getBoolean("educacao_ativ_complem_possui"));
                p.setEducacaoAtividadeComplementaresDanca(rs.getBoolean("educacao_ativ_complem_danca"));
                p.setEducacaoAtividadeComplementaresCapoeira(rs.getBoolean("educacao_ativ_complem_capoeira"));
                p.setEducacaoAtividadeComplementaresTeatro(rs.getBoolean("educacao_ativ_complem_teatro"));
                p.setEducacaoAtividadeComplementaresPercussao(rs.getBoolean("educacao_ativ_complem_percussao"));
                p.setEducacaoAtividadeComplementaresMusica(rs.getBoolean("educacao_ativ_complem_musica"));
                p.setEducacaoAtividadeComplementaresCoral(rs.getBoolean("educacao_ativ_complem_coral"));

                //BENEF SOCIAL
                p.setBeneficioSocialAposentadoriaPossui(rs.getBoolean("benefsociais_possui_apos"));
                p.setBeneficioSocialAposentadoriaTipo(rs.getString("benefsociais_tipo_apos"));
                p.setBeneficioSocialAposentadoriaQuantoTempo(rs.getString("benefsociais_tempo_apos"));
                if (!VerificadorUtil.verificarSeObjetoNuloOuZero(rs.getDouble("benefsociais_valor_apos"))) {
                    p.setBeneficioSocialAposentadoriaValor(rs.getDouble("benefsociais_valor_apos"));
                }
                p.setBeneficioSocialBeneficioFamiliaPossui(rs.getBoolean("benefsociais_benef_familia"));
                p.setBeneficioSocialBeneficioFamiliaTipo(rs.getString("benefsociais_benef_familia_tipobenef"));
                p.setBeneficioSocialBeneficioFamiliaQuantoTempo(rs.getString("benefsociais_tempo_benef_familia"));
                if (!VerificadorUtil.verificarSeObjetoNuloOuZero(rs.getDouble("benefsociais_valor_benef_familia"))) {
                    p.setBeneficioSocialBeneficioFamiliaValor(rs.getDouble("benefsociais_valor_benef_familia"));
                }
                p.setBeneficioSocialINSSPossui(rs.getBoolean("benefsociais_possui_benef_inss"));
                p.setBeneficioSocialINSSTipo(rs.getString("benefsociais_tempo_benef_inss"));
                p.setBeneficioSocialINSSQuantoTempo(rs.getString("benefsociais_tipo_benef_inss"));
                if (!VerificadorUtil.verificarSeObjetoNuloOuZero(rs.getDouble("benefsociais_valor_benef_inss"))) {
                    p.setBeneficioSocialINSSValor(rs.getDouble("benefsociais_valor_benef_inss"));
                }
                p.setBeneficioSocialProgramaLeitePossui(rs.getString("benefsociais_possui_prog_leite"));
                p.setBeneficioSocialProgramaSopaPossui(rs.getString("benefsociais_possui_prog_sopa"));
                p.setBeneficioSocialBolsaFamiliaPossui(rs.getBoolean("benefsociais_possui_bolsa_fam"));
                p.setBeneficioSocialBolsaFamiliaQuantoTempo(rs.getString("benefsociais_tempo_bolsa_fam"));
                if (!VerificadorUtil.verificarSeObjetoNuloOuZero(rs.getDouble("benefsociais_valor_bolsa_fam"))) {
                    p.setBeneficioSocialBolsaFamiliaValor(rs.getDouble("benefsociais_valor_bolsa_fam"));
                }
                p.setBeneficioSocialMinhaCasaMinhaVidaPossui(rs.getBoolean("benefsociais_possui_minhacasa_minhavida"));
                p.setBeneficioSocialCadastroUnico(rs.getString("benefsociais_possui_cad_unico"));
                p.setBeneficioSocialNumeroNisPossui(rs.getString("benefsociais_possui_numero_nis"));
                p.setBeneficioSocialNumeroNis(rs.getString("benefsociais_numeronis"));
                p.setBeneficioSocialIncapacidadePossui(rs.getBoolean("benefsociais_incapacidade"));
                p.setBeneficioSocialIncapacidadeTipo(rs.getString("benefsociais_tipo_incapacidade"));
                p.setBeneficioSocialIncapacidadeQuantoTempo(rs.getString("benefsociais_tempo_incapacidade"));
                if (!VerificadorUtil.verificarSeObjetoNuloOuZero(rs.getDouble("benefsociais_valor_incapacidade"))) {
                    p.setBeneficioSocialIncapacidadeValor(rs.getDouble("benefsociais_valor_incapacidade"));
                }


                //TRANSPORTE
                p.setTransporteCarteiraTransportePossui(rs.getBoolean("transporte_possui_cart_transp"));
                p.setTransporteCarteiraTransporteAcompanhate(rs.getBoolean("transporte_cart_transp_acompanhante"));
                p.setTransporteCarteiraTransporteTipo(rs.getString("transporte_tipo_carteira"));
                p.setTransporteUtilizadoTratamento(rs.getString("transporte_utilizado_tratamento"));
                p.setTransporteQuantidadeOnibusAteInstituicao(rs.getString("transporte_qtd_onibus_ate_instituicao"));
                p.setTransporteExisteAcessibilidadeAcessoPercusoInstituicao(rs.getBoolean("transporte_acessibilidade_acesso_percurso_instit"));

                //RENDA FAMILIAR
                p.setRendaFamiliarInseridoMercadoTrabalho(rs.getBoolean("rendafam_inserida_merc_trab"));
                p.setRendaFamiliarProfissaoFuncao(rs.getString("rendafam_profissao_funcao"));
                p.setRendaFamiliarAtividadeAntesAgravo(rs.getString("rendafam_atividade_antes_agravo"));
                p.setRendaFamiliarTempoAntesAgravo(rs.getString("rendafam_tempo_atividade_antes_agravo"));
                if (!VerificadorUtil.verificarSeObjetoNuloOuZero(rs.getDouble("rendafam_valor"))) {
                    p.setRendaFamiliarValor(rs.getDouble("rendafam_valor"));
                }
                p.setRendaFamiliarMantemUsuario(rs.getBoolean("rendafam_mantem_fam_usuario"));
                p.setRendaFamiliarMantemPai(rs.getBoolean("rendafam_mantem_fam_pai"));
                p.setRendaFamiliarMantemMae(rs.getBoolean("rendafam_mantem_fam_mae"));
                p.setRendaFamiliarMantemIrmao(rs.getBoolean("rendafam_mantem_fam_irmao"));
                p.setRendaFamiliarMantemAvo(rs.getBoolean("rendafam_mantem_fam_avo"));
                p.setRendaFamiliarMantemCuidador(rs.getBoolean("rendafam_mantem_fam_cuidador"));
                p.setRendaFamiliarMantemTio(rs.getBoolean("rendafam_mantem_fam_tio"));
                p.setRendaFamiliarMantemEsposo(rs.getBoolean("rendafam_mantem_fam_esposo"));
                p.setRendaFamiliarMantemVizinho(rs.getBoolean("rendafam_mantem_fam_vizinho"));
                p.setRendaFamiliarMantemFilho(rs.getBoolean("rendafam_mantem_fam_filho"));
                p.setRendaFamiliarMantemPadastroMadastra(rs.getBoolean("rendafam_mantem_fam_padastro_madastra"));
                p.setRendaFamiliarMantemGenroNora(rs.getBoolean("rendafam_mantem_fam_genro_nora"));
                p.setRendaFamiliarMantemSobrinho(rs.getBoolean("rendafam_mantem_fam_sobrinho"));
                p.setRendaFamiliarMantemEnteado(rs.getBoolean("rendafam_mantem_fam_enteado"));

                //HABITACAO
                p.setHabitacaoSitucaoMoradia(rs.getString("habitacao_situacao_moradia"));
                p.setHabitacaoTipoConstrucao(rs.getString("habitacao_tipo_construcao"));
                p.setHabitacaoTipoResidencia(rs.getString("habitacao_tipo_residencia"));
                p.setHabitacaoNumeroComodo(rs.getInt("habitacao_num_comodos"));
                p.setHabitacaoAbastecimentoDeAgua(rs.getString("habitacao_abastecimento_agua"));
                p.setHabitacaoTipoIluminacao(rs.getString("habitacao_tipo_iluminacao"));
                p.setHabitacaoEscoamentoSanitario(rs.getString("habitacao_escoamento_sanitario"));
                p.setHabitacaoDestinoLixo(rs.getString("habitacao_destino_lixo"));
                p.setHabitacaoResidenciaAdaptada(rs.getString("habitacao_residencia_adaptada"));

                //RELATO DE VIDA
                p.setRelatoVida(rs.getString("relato_vida"));

                //PARECER SOCIAL ENCAMINHAMENTOS
                p.setParecerSocialEncaminhamento(rs.getString("parecer_social_encaminhamentos"));
                p.setListaComposicaoFamiliar(this.retornarComposicaoFamiliar(idQuestionario, conexao));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return p;
    }

}
