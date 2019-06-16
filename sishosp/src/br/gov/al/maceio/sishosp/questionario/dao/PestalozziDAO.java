package br.gov.al.maceio.sishosp.questionario.dao;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.questionario.model.Pestalozzi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PestalozziDAO {
    //conexao = ConnectionFactory.getConnection();
    Connection conexao = null;

    public Boolean gravarQuestionario(Pestalozzi pestalozzi, Integer idPaciente) throws ProjetoException {
        boolean retorno = false;
        String sql = "INSERT INTO questionario_social.questionario_social_pestalozzimaceio(id_paciente) VALUES (?) ";
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, idPaciente);
            ResultSet rs = stmt.executeQuery();
            Integer idInsercao = rs.getInt("id");

            if(gravarQuestionarioSaude(pestalozzi,idInsercao,conexao)){

            }else{
            }
            //MÉTODOS PARA INSERIR EM CADA CATEGORIA ESPECIFICA

            conexao.commit();
            retorno = true;
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
        String sql = "INSERT INTO questionario_social.questionario_social_pestalozzimaceio( " +
                "saude_realiza_atend_instituicao, saude_unidade_atend, saude_serv_atend_rede_reab_aud,  " +
                "saude_serv_atend_rede_reab_fis, saude_serv_atend_rede_reab_int, saude_serv_atend_rede_reab_vis,  " +
                "saude_serv_ambulatorio, saude_outros_serv_homecare, saude_outros_serv_equoterapia,  " +
                "saude_outros_serv_grupo_convivencia, saude_outros_serv_grupo_terap_fam, saude_outros_serv_orteseprotese,  " +
                "saude_outros_serv_oficina, saude_outros_serv_estimul_precoce, saude_outros_serv_hidroterapia,  " +
                "saude_uso_opm, saude_uso_opm_tempo, saude_uso_opm_qual,  " +
                "saude_uso_equip_ortese, saude_uso_equip_aasi, saude_uso_equip_aux_optico,  " +
                "saude_uso_equip_meios_aux_loc, saude_uso_equip_protese, saude_uso_equip_oculos_adapt,  " +
                "saude_uso_equip_quanto_tempo, saude_uso_equip_entidade_concedeu, saude_uso_equip_realizou_reab,  " +
                "saude_uso_equip_realizou_reab_onde, saude_uso_equip_alguem_fam_deficiencia, saude_uso_equip_alguem_fam_deficiencia_parentesco, - " +
                "saude_uso_equip_morbidade_cid, saude_uso_equip_morbidade_cid_quanto_tempo, saude_uso_equip_causa_doenca,  " +
                "saude_uso_equip_residencia_coberta_psf, saude_uso_equip_visita_agenda_saude, saude_uso_equip_como_atendido_doente,  " +
                "saude_uso_equip_posto_saude_regiao, saude_uso_equip_faz_uso_medicacao, saude_uso_equip_faz_uso_qual_medicacao,  " +
                "saude_medicacao_frequencia)  " +
                "VALUES ( " +
                "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                ") WHERE id = ?";
        try {
            PreparedStatement stmt = conexaoAuxiliar.prepareStatement(sql);
            stmt.setString(1, p.getSaudeRealizaAtendimentoNestaInstituicao());
            stmt.setString(2, p.getSaudeUnidade());
            stmt.setString(3, p.getSaudeServicoAtendimentoRedeReabilitacaoAuditiva());
            stmt.setString(4, p.getSaudeServicoAtendimentoRedeReabilitacaoFisica());
            stmt.setString(5, p.getSaudeServicoAtendimentoRedeReabilitacaoIntelectual());
            stmt.setString(6, p.getSaudeServicoAtendimentoRedeReabilitacaoVisual());
            stmt.setString(7, p.getSaudeServicoAmbulatorio());
            stmt.setString(8, p.getSaudeOutrosServicosHomeCare());
            stmt.setString(9, p.getSaudeOutrosServicosEquoterapia());
            stmt.setString(10, p.getSaudeOutrosServicosGrupoConvivencia());
            stmt.setString(11, p.getSaudeOutrosServicosGrupoTerapeuticoFamiliar());
            stmt.setString(12, p.getSaudeOutrosServicosOrteseProtesesMeiosLocomocao());
            stmt.setString(13, p.getSaudeOutrosServicosOficinaOrtopedica());
            stmt.setString(14, p.getSaudeOutrosServicosEstimulacaoPrecoce());
            stmt.setString(15, p.getSaudeOutrosServicosHidroTerapia());
            stmt.setString(16, p.getSaudeUsoOrteseProtese());
            stmt.setString(17, p.getSaudeUsoOrteseProteseQual());
            stmt.setString(18, p.getSaudeUsoOrteseProteseQuantoTempo());
            stmt.setString(19, p.getSaudeUsaEquipamentoOrtopedicoOrtese());
            stmt.setString(20, p.getSaudeUsaEquipamentoOrtopedicoAASI());
            stmt.setString(21, p.getSaudeUsaEquipamentoOrtopedicoAuxilioOptico());
            stmt.setString(22, p.getSaudeUsaEquipamentoOrtopedicoMeiosAuxiliaresDeLocomocao());
            stmt.setString(23, p.getSaudeUsaEquipamentoOrtopedicoProtese());
            stmt.setString(24, p.getSaudeUsaEquipamentoOrtopedicoOculosAdaptado());
            stmt.setString(25, p.getSaudeUsaEquipamentoOrtopedicoQuantoTempo());
            stmt.setString(26, p.getSaudeUsaEquipamentoOrtopedicoEntidadeQueConcedeu());
            stmt.setString(27, p.getSaudeUsaEquipamentoOrtopedicoRealizouReabilitacao());
            stmt.setString(28, p.getSaudeUsaEquipamentoOrtopedicoRealizouReabilitacaoOnde());
            stmt.setString(29, p.getSaudeUsaEquipamentoOrtopedicoAlguemNaFamiliaComDeficiencia());
            stmt.setString(30, p.getSaudeUsaEquipamentoOrtopedicoAlguemNaFamiliaComDeficienciaParentesco());
            stmt.setString(31, p.getSaudeUsaEquipamentoOrtopedicoMorbidadeCID());
            stmt.setString(32, p.getSaudeUsaEquipamentoOrtopedicoMorbidadeCIDQuantoTempo());
            stmt.setString(33, p.getSaudeUsaEquipamentoOrtopedicoCausaDoencaDeficiencia());
            stmt.setString(34, p.getSaudeUsaEquipamentoOrtopedicoResidenciaCobertaPSF());
            stmt.setString(35, p.getSaudeUsaEquipamentoOrtopedicoRecebeuVisitaAgenteSaude());
            stmt.setString(36, p.getSaudeUsaEquipamentoOrtopedicoFazParaSerAtendidoDoente());
            stmt.setString(37, p.getSaudeUsaEquipamentoOrtopedicoPostoSaudeRegiaoReside());
            stmt.setString(38, p.getSaudeUsaEquipamentoOrtopedicoUsoMedicacao());
            stmt.setString(39, p.getSaudeUsaEquipamentoOrtopedicoUsoMedicacaoQual());
            stmt.setString(40, p.getSaudeUsaEquipamentoOrtopedicoUsoMedicacaoFrequencia());
            stmt.setInt(41, idInsercao);

            stmt.executeQuery();
            retorno = true;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return retorno;
    }

}
