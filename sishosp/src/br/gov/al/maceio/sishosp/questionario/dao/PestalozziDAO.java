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
            if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getSaudeRealizaAtendimentoNestaInstituicao())){
                stmt.setString(1, p.getSaudeRealizaAtendimentoNestaInstituicao());
            }else{
                stmt.setNull(1,  java.sql.Types.NULL);
            }
            if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getSaudeUnidade())){
                stmt.setString(2, p.getSaudeUnidade());
            }else{
                stmt.setNull(2,  java.sql.Types.NULL);
            }
            if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getSaudeServicoAtendimentoRedeReabilitacaoAuditiva())){
                stmt.setString(3, p.getSaudeServicoAtendimentoRedeReabilitacaoAuditiva());
            }else{
                stmt.setNull(3,  java.sql.Types.NULL);
            }
            if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getSaudeServicoAtendimentoRedeReabilitacaoFisica())){
                stmt.setString(4, p.getSaudeServicoAtendimentoRedeReabilitacaoFisica());
            }else{
                stmt.setNull(4,  java.sql.Types.NULL);
            }
            if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getSaudeServicoAtendimentoRedeReabilitacaoIntelectual())){
                stmt.setString(5, p.getSaudeServicoAtendimentoRedeReabilitacaoIntelectual());
            }else{
                stmt.setNull(5,  java.sql.Types.NULL);
            }
            if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getSaudeServicoAtendimentoRedeReabilitacaoVisual())){
                stmt.setString(6, p.getSaudeServicoAtendimentoRedeReabilitacaoVisual());
            }else{
                stmt.setNull(6,  java.sql.Types.NULL);
            }
            if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getSaudeServicoAmbulatorio())){
                stmt.setString(7, p.getSaudeServicoAmbulatorio());
            }else{
                stmt.setNull(7,  java.sql.Types.NULL);
            }
            if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getSaudeOutrosServicosHomeCare())){
                stmt.setString(8, p.getSaudeOutrosServicosHomeCare());
            }else{
                stmt.setNull(8,  java.sql.Types.NULL);
            }
            if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getSaudeOutrosServicosEquoterapia())){
                stmt.setString(9, p.getSaudeOutrosServicosEquoterapia());
            }else{
                stmt.setNull(9,  java.sql.Types.NULL);
            }
            if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getSaudeOutrosServicosGrupoConvivencia())){
                stmt.setString(10, p.getSaudeOutrosServicosGrupoConvivencia());
            }else{
                stmt.setNull(10,  java.sql.Types.NULL);
            }
            if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getSaudeOutrosServicosGrupoTerapeuticoFamiliar())){
                stmt.setString(11, p.getSaudeOutrosServicosGrupoTerapeuticoFamiliar());
            }else{
                stmt.setNull(11,  java.sql.Types.NULL);
            }
            if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getSaudeOutrosServicosOrteseProtesesMeiosLocomocao())){
                stmt.setString(12, p.getSaudeOutrosServicosOrteseProtesesMeiosLocomocao());
            }else{
                stmt.setNull(12,  java.sql.Types.NULL);
            }
            if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getSaudeOutrosServicosOficinaOrtopedica())){
                stmt.setString(13, p.getSaudeOutrosServicosOficinaOrtopedica());
            }else{
                stmt.setNull(13,  java.sql.Types.NULL);
            }
            if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getSaudeOutrosServicosEstimulacaoPrecoce())){
                stmt.setString(14, p.getSaudeOutrosServicosEstimulacaoPrecoce());
            }else{
                stmt.setNull(14,  java.sql.Types.NULL);
            }
            if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getSaudeOutrosServicosHidroTerapia())){
                stmt.setString(15, p.getSaudeOutrosServicosHidroTerapia());
            }else{
                stmt.setNull(15,  java.sql.Types.NULL);
            }
            if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getSaudeUsoOrteseProtese())){
                stmt.setString(16, p.getSaudeUsoOrteseProtese());
            }else{
                stmt.setNull(16,  java.sql.Types.NULL);
            }
            if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getSaudeUsoOrteseProteseQual())){
                stmt.setString(17, p.getSaudeUsoOrteseProteseQual());
            }else{
                stmt.setNull(17,  java.sql.Types.NULL);
            }
            if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getSaudeUsoOrteseProteseQuantoTempo())){
                stmt.setString(18, p.getSaudeUsoOrteseProteseQuantoTempo());
            }else{
                stmt.setNull(18,  java.sql.Types.NULL);
            }
            if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getSaudeUsaEquipamentoOrtopedicoOrtese())){
                stmt.setString(19, p.getSaudeUsaEquipamentoOrtopedicoOrtese());
            }else{
                stmt.setNull(19,  java.sql.Types.NULL);
            }
            if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getSaudeUsaEquipamentoOrtopedicoAASI())){
                stmt.setString(20, p.getSaudeUsaEquipamentoOrtopedicoAASI());
            }else{
                stmt.setNull(20,  java.sql.Types.NULL);
            }
            if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getSaudeUsaEquipamentoOrtopedicoAuxilioOptico())){
                stmt.setString(21, p.getSaudeUsaEquipamentoOrtopedicoAuxilioOptico());
            }else{
                stmt.setNull(21,  java.sql.Types.NULL);
            }
            if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getSaudeUsaEquipamentoOrtopedicoMeiosAuxiliaresDeLocomocao())){
                stmt.setString(22, p.getSaudeUsaEquipamentoOrtopedicoMeiosAuxiliaresDeLocomocao());
            }else{
                stmt.setNull(22,  java.sql.Types.NULL);
            }
            if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getSaudeUsaEquipamentoOrtopedicoProtese())){
                stmt.setString(23, p.getSaudeUsaEquipamentoOrtopedicoProtese());
            }else{
                stmt.setNull(23,  java.sql.Types.NULL);
            }
            if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getSaudeUsaEquipamentoOrtopedicoOculosAdaptado())){
                stmt.setString(24, p.getSaudeUsaEquipamentoOrtopedicoOculosAdaptado());
            }else{
                stmt.setNull(24,  java.sql.Types.NULL);
            }
            if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getSaudeUsaEquipamentoOrtopedicoQuantoTempo())){
                stmt.setString(25, p.getSaudeUsaEquipamentoOrtopedicoQuantoTempo());
            }else{
                stmt.setNull(25,  java.sql.Types.NULL);
            }
            if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getSaudeUsaEquipamentoOrtopedicoEntidadeQueConcedeu())){
                stmt.setString(26, p.getSaudeUsaEquipamentoOrtopedicoEntidadeQueConcedeu());
            }else{
                stmt.setNull(26,  java.sql.Types.NULL);
            }
            if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getSaudeUsaEquipamentoOrtopedicoRealizouReabilitacao())){
                stmt.setString(27, p.getSaudeUsaEquipamentoOrtopedicoRealizouReabilitacao());
            }else{
                stmt.setNull(27,  java.sql.Types.NULL);
            }
            if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getSaudeUsaEquipamentoOrtopedicoRealizouReabilitacaoOnde())){
                stmt.setString(28, p.getSaudeUsaEquipamentoOrtopedicoRealizouReabilitacaoOnde());
            }else{
                stmt.setNull(28,  java.sql.Types.NULL);
            }
            if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getSaudeUsaEquipamentoOrtopedicoAlguemNaFamiliaComDeficiencia())){
                stmt.setString(29, p.getSaudeUsaEquipamentoOrtopedicoAlguemNaFamiliaComDeficiencia());
            }else{
                stmt.setNull(29,  java.sql.Types.NULL);
            }
            if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getSaudeUsaEquipamentoOrtopedicoAlguemNaFamiliaComDeficienciaParentesco())){
                stmt.setString(30, p.getSaudeUsaEquipamentoOrtopedicoAlguemNaFamiliaComDeficienciaParentesco());
            }else{
                stmt.setNull(30,  java.sql.Types.NULL);
            }
            if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getSaudeUsaEquipamentoOrtopedicoMorbidadeCID())){
                stmt.setString(31, p.getSaudeUsaEquipamentoOrtopedicoMorbidadeCID());
            }else{
                stmt.setNull(31,  java.sql.Types.NULL);
            }
            if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getSaudeUsaEquipamentoOrtopedicoMorbidadeCIDQuantoTempo())){
                stmt.setString(32, p.getSaudeUsaEquipamentoOrtopedicoMorbidadeCIDQuantoTempo());
            }else{
                stmt.setNull(32,  java.sql.Types.NULL);
            }
            if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getSaudeUsaEquipamentoOrtopedicoCausaDoencaDeficiencia())){
                stmt.setString(33, p.getSaudeUsaEquipamentoOrtopedicoCausaDoencaDeficiencia());
            }else{
                stmt.setNull(33,  java.sql.Types.NULL);
            }
            if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getSaudeUsaEquipamentoOrtopedicoResidenciaCobertaPSF())){
                stmt.setString(34, p.getSaudeUsaEquipamentoOrtopedicoResidenciaCobertaPSF());
            }else{
                stmt.setNull(34,  java.sql.Types.NULL);
            }
            if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getSaudeUsaEquipamentoOrtopedicoRecebeuVisitaAgenteSaude())){
                stmt.setString(35, p.getSaudeUsaEquipamentoOrtopedicoRecebeuVisitaAgenteSaude());
            }else{
                stmt.setNull(35,  java.sql.Types.NULL);
            }
            if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getSaudeUsaEquipamentoOrtopedicoFazParaSerAtendidoDoente())){
                stmt.setString(36, p.getSaudeUsaEquipamentoOrtopedicoFazParaSerAtendidoDoente());
            }else{
                stmt.setNull(36,  java.sql.Types.NULL);
            }
            if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getSaudeUsaEquipamentoOrtopedicoPostoSaudeRegiaoReside())){
                stmt.setString(37, p.getSaudeUsaEquipamentoOrtopedicoPostoSaudeRegiaoReside());
            }else{
                stmt.setNull(37,  java.sql.Types.NULL);
            }
            if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getSaudeUsaEquipamentoOrtopedicoUsoMedicacao())){
                stmt.setString(38, p.getSaudeUsaEquipamentoOrtopedicoUsoMedicacao());
            }else{
                stmt.setNull(38,  java.sql.Types.NULL);
            }
            if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getSaudeUsaEquipamentoOrtopedicoUsoMedicacaoQual())){
                stmt.setString(39, p.getSaudeUsaEquipamentoOrtopedicoUsoMedicacaoQual());
            }else{
                stmt.setNull(39,  java.sql.Types.NULL);
            }
            if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getSaudeUsaEquipamentoOrtopedicoUsoMedicacaoFrequencia())){
                stmt.setString(40, p.getSaudeUsaEquipamentoOrtopedicoUsoMedicacaoFrequencia());
            }else{
                stmt.setNull(40,  java.sql.Types.NULL);
            }
            stmt.setInt(41, idInsercao);

            stmt.executeQuery();
            retorno = true;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return retorno;
    }


    public Boolean gravarQuestionarioEducacao(Pestalozzi p, Integer idInsercao, Connection conexaoAuxiliar) throws ProjetoException {
        boolean retorno = false;
        String sql = "INSERT INTO questionario_social.questionario_social_pestalozzimaceio(" +
                "educacao_matriculado, educacao_escolaridade, educacao_turno, " +
                "educacao_escola_matriculado, educacao_acesso_escola, educacao_escola_fica_no_bairro, " +
                "educacao_escola_qual_bairro, educacao_escola_tem_sala_recurso, educacao_recebe_apoio_aux_sala, " +
                "educacao_resp_part_reuniao_escola, educacao_ativ_institucional_possui, educacao_qual_ativ_instit_aee, " +
                "educacao_qual_ativ_instit_eja, educacao_qual_ativ_instit_esporte, educacao_qual_ativ_instit_oficina_prof, " +
                "educacao_qual_ativ_instit_jovem_aprendiz, educacao_qual_ativ_instit_arte, educacao_ativ_complem_possui, " +
                "educacao_ativ_complem_danca, educacao_ativ_complem_capoeira, educacao_ativ_complem_teatro, " +
                "educacao_ativ_complem_percussao, educacao_ativ_complem_musica, educacao_ativ_complem_coral)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, " +
                "?, ?, ?, ?, ?, ?, ?, ?, " +
                "?, ?, ?, ?, ?, ?, ?, ?) WHERE id = ?;";
        try {
            PreparedStatement stmt = conexaoAuxiliar.prepareStatement(sql);
            stmt.setString(1, p.getEducacaoMatriculado());

            if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getEducacaoMatriculado())){
                stmt.setString(1, p.getEducacaoMatriculado());
            }else{
                stmt.setNull(1,  java.sql.Types.NULL);
            }
            if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getEducacaoEscolaridade())){
                stmt.setString(2, p.getEducacaoEscolaridade());
            }else{
                stmt.setNull(2,  java.sql.Types.NULL);
            }
            if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getEducacaoTurno())){
                stmt.setString(3, p.getEducacaoTurno());
            }else{
                stmt.setNull(3,  java.sql.Types.NULL);
            }
            if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getEducacaoEscolaEstaMatriculado())){
                stmt.setString(4, p.getEducacaoEscolaEstaMatriculado());
            }else{
                stmt.setNull(4,  java.sql.Types.NULL);
            }
            if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getEducacaoAcessoEscola())){
                stmt.setString(5, p.getEducacaoAcessoEscola());
            }else{
                stmt.setNull(5,  java.sql.Types.NULL);
            }
            if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getEducacaoEscolaFicaNoBairro())){
                stmt.setString(6, p.getEducacaoEscolaFicaNoBairro());
            }else{
                stmt.setNull(6,  java.sql.Types.NULL);
            }
            if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getEducacaoEscolaQualBairro())){
                stmt.setString(7, p.getEducacaoEscolaQualBairro());
            }else{
                stmt.setNull(7,  java.sql.Types.NULL);
            }
            if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getEducacaoEscolaTemSalaDeRecurso())){
                stmt.setString(8, p.getEducacaoEscolaTemSalaDeRecurso());
            }else{
                stmt.setNull(8,  java.sql.Types.NULL);
            }
            if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getEducacaoRecebeApoioAuxiliarSala())){
                stmt.setString(9, p.getEducacaoRecebeApoioAuxiliarSala());
            }else{
                stmt.setNull(9,  java.sql.Types.NULL);
            }
            if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getEducacaoResponsavelParticipaReuniaoEscolar())){
                stmt.setString(10, p.getEducacaoResponsavelParticipaReuniaoEscolar());
            }else{
                stmt.setNull(10,  java.sql.Types.NULL);
            }
            if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getEducacaoAtividadeInstituconalPossui())){
                stmt.setString(11, p.getEducacaoAtividadeInstituconalPossui());
            }else{
                stmt.setNull(11,  java.sql.Types.NULL);
            }
            if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getEducacaoAtividadeInstituconalQualAEE())){
                stmt.setString(12, p.getEducacaoAtividadeInstituconalQualAEE());
            }else{
                stmt.setNull(12,  java.sql.Types.NULL);
            }
            if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getEducacaoAtividadeInstituconalQualEJA())){
                stmt.setString(13, p.getEducacaoAtividadeInstituconalQualEJA());
            }else{
                stmt.setNull(13,  java.sql.Types.NULL);
            }
            if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getEducacaoAtividadeInstituconalQualEsporte())){
                stmt.setString(14, p.getEducacaoAtividadeInstituconalQualEsporte());
            }else{
                stmt.setNull(14,  java.sql.Types.NULL);
            }
            if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getEducacaoAtividadeInstituconalQualOficinaProf())){
                stmt.setString(15, p.getEducacaoAtividadeInstituconalQualOficinaProf());
            }else{
                stmt.setNull(15,  java.sql.Types.NULL);
            }
            if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getEducacaoAtividadeInstituconalQualJovemAprendiz())){
                stmt.setString(16, p.getEducacaoAtividadeInstituconalQualJovemAprendiz());
            }else{
                stmt.setNull(16,  java.sql.Types.NULL);
            }
            if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getEducacaoAtividadeInstituconalQualArteCultura())){
                stmt.setString(17, p.getEducacaoAtividadeInstituconalQualArteCultura());
            }else{
                stmt.setNull(17,  java.sql.Types.NULL);
            }
            if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getEducacaoAtividadeComplementaresPossui())){
                stmt.setString(18, p.getEducacaoAtividadeComplementaresPossui());
            }else{
                stmt.setNull(18,  java.sql.Types.NULL);
            }
            if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getEducacaoAtividadeComplementaresDanca())){
                stmt.setString(19, p.getEducacaoAtividadeComplementaresDanca());
            }else{
                stmt.setNull(19,  java.sql.Types.NULL);
            }
            if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getEducacaoAtividadeComplementaresCapoeira())){
                stmt.setString(20, p.getEducacaoAtividadeComplementaresCapoeira());
            }else{
                stmt.setNull(20,  java.sql.Types.NULL);
            }
            if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getEducacaoAtividadeComplementaresTeatro())){
                stmt.setString(21, p.getEducacaoAtividadeComplementaresTeatro());
            }else{
                stmt.setNull(21,  java.sql.Types.NULL);
            }
            if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getEducacaoAtividadeComplementaresPercussao())){
                stmt.setString(22, p.getEducacaoAtividadeComplementaresPercussao());
            }else{
                stmt.setNull(22,  java.sql.Types.NULL);
            }
            if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getEducacaoAtividadeComplementaresMusica())){
                stmt.setString(23, p.getEducacaoAtividadeComplementaresMusica());
            }else{
                stmt.setNull(23,  java.sql.Types.NULL);
            }
            if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(p.getEducacaoAtividadeComplementaresCoral())){
                stmt.setString(24, p.getEducacaoAtividadeComplementaresCoral());
            }else{
                stmt.setNull(24,  java.sql.Types.NULL);
            }

            stmt.setInt(25, idInsercao);

            stmt.executeQuery();
            retorno = true;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return retorno;
    }
}
