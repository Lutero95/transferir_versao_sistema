package br.gov.al.maceio.sishosp.administrativo.dao;

import br.gov.al.maceio.sishosp.administrativo.model.RemocaoProfissionalEquipe;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.DataUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.enums.Turno;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RemocaoProfissionalEquipeDAO {

    private Connection con = null;
    private PreparedStatement ps = null;

    public List<RemocaoProfissionalEquipe> listarSeremRemovidos(RemocaoProfissionalEquipe RemocaoProfissionalEquipe) {

        List<RemocaoProfissionalEquipe> lista = new ArrayList<>();
        int i = 4;

        String sql = "SELECT a.id_atendimento, a1.id_atendimentos1, a.dtaatende, a.codprograma, p.descprograma, " +
                "a1.codprofissionalatendimento, f.descfuncionario, a.codgrupo, g.descgrupo, a.codequipe, e.descequipe, " +
                "CASE WHEN a.turno = 'A' THEN 'AMBOS' WHEN a.turno = 'T' THEN 'TARDE' " +
                "WHEN a.turno = 'M' THEN 'MANHÃ' END AS turno " +
                "FROM hosp.atendimentos a " +
                "JOIN hosp.atendimentos1 a1 ON (a.id_atendimento = a1.id_atendimento) " +
                "JOIN acl.funcionarios f ON (a1.codprofissionalatendimento = f.id_funcionario) " +
                "JOIN hosp.programa p ON (a.codprograma = p.id_programa) " +
                "JOIN hosp.grupo g ON (a.codgrupo = g.id_grupo) " +
                "JOIN hosp.equipe e ON (a.codequipe = e.id_equipe) " +
                "WHERE a.codprograma = ? AND a1.codprofissionalatendimento = ? AND a.dtaatende >= ? AND a.dtaatende <= ? " +
                "AND a.situacao = 'A' AND coalesce(a1.excluido,'N')='N' ";

        if(!RemocaoProfissionalEquipe.getTurno().equals(Turno.AMBOS.getSigla())){
            sql = sql + "AND a.turno = ? ";
        }
        if (!VerificadorUtil.verificarSeObjetoNuloOuZero(RemocaoProfissionalEquipe.getGrupo().getIdGrupo())) {
            sql = sql + "AND a.codgrupo = ? ";
        }
        if (!VerificadorUtil.verificarSeObjetoNuloOuZero(RemocaoProfissionalEquipe.getEquipe().getCodEquipe())) {
            sql = sql + "AND a.codequipe = ? ";
        }


        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);

            stm.setInt(1, RemocaoProfissionalEquipe.getPrograma().getIdPrograma());
            stm.setLong(2, RemocaoProfissionalEquipe.getFuncionario().getId());
            stm.setDate(3, DataUtil.converterDateUtilParaDateSql(RemocaoProfissionalEquipe.getPeriodoInicio()));
            stm.setDate(4, DataUtil.converterDateUtilParaDateSql(RemocaoProfissionalEquipe.getPeriodoFinal()));

            if(!RemocaoProfissionalEquipe.getTurno().equals(Turno.AMBOS.getSigla())) {
                i++;
                stm.setString(i, RemocaoProfissionalEquipe.getTurno());
            }

            if (!VerificadorUtil.verificarSeObjetoNuloOuZero(RemocaoProfissionalEquipe.getGrupo().getIdGrupo())) {
                i++;
                stm.setInt(i, RemocaoProfissionalEquipe.getGrupo().getIdGrupo());
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuZero(RemocaoProfissionalEquipe.getGrupo().getIdGrupo())) {
                i++;
                stm.setInt(i, RemocaoProfissionalEquipe.getEquipe().getCodEquipe());
            }

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                RemocaoProfissionalEquipe remocaoProfissionalEquipe = new RemocaoProfissionalEquipe();
                remocaoProfissionalEquipe.getAtendimentoBean().setId(rs.getInt("id_atendimento"));
                remocaoProfissionalEquipe.getAtendimentoBean().setId1(rs.getInt("id_atendimentos1"));
                remocaoProfissionalEquipe.getAtendimentoBean().setDataAtendimentoInicio(rs.getDate("dtaatende"));
                remocaoProfissionalEquipe.getAtendimentoBean().getPrograma().setIdPrograma(rs.getInt("codprograma"));
                remocaoProfissionalEquipe.getAtendimentoBean().getPrograma().setDescPrograma(rs.getString("descprograma"));
                remocaoProfissionalEquipe.getAtendimentoBean().getFuncionario().setId(rs.getLong("codprofissionalatendimento"));
                remocaoProfissionalEquipe.getAtendimentoBean().getFuncionario().setNome(rs.getString("descfuncionario"));
                remocaoProfissionalEquipe.getAtendimentoBean().setTurno(rs.getString("turno"));
                remocaoProfissionalEquipe.getAtendimentoBean().getGrupo().setIdGrupo(rs.getInt("codgrupo"));
                remocaoProfissionalEquipe.getAtendimentoBean().getGrupo().setDescGrupo(rs.getString("descgrupo"));
                remocaoProfissionalEquipe.getAtendimentoBean().getEquipe().setCodEquipe(rs.getInt("codequipe"));
                remocaoProfissionalEquipe.getAtendimentoBean().getEquipe().setDescEquipe(rs.getString("descequipe"));

                lista.add(remocaoProfissionalEquipe);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return lista;
    }

    public Boolean gravarRemocao(RemocaoProfissionalEquipe RemocaoProfissionalEquipe, List<RemocaoProfissionalEquipe> listaRemocao) {

       return true;

    }


    public List<RemocaoProfissionalEquipe> listarRemovidos() {

        List<RemocaoProfissionalEquipe> lista = new ArrayList<>();

        String sql = "SELECT f.descfuncionario, a.data_inicio, a.data_final, p.descprograma, g.descgrupo, e.descequipe, " +
                "CASE WHEN a.turno = 'A' THEN 'AMBOS' WHEN a.turno = 'T' THEN 'TARDE' " +
                "WHEN a.turno = 'M' THEN 'MANHÃ' END AS turno " +
                "FROM adm.insercao_profissional_equipe_atendimento a " +
                "JOIN adm.insercao_profissional_equipe_atendimento_1 a1 ON (a.id = a1.id_insercao_profissional_equipe_atendimento) " +
                "JOIN acl.funcionarios f ON (a1.id_profissional = f.id_funcionario) " +
                "LEFT JOIN hosp.programa p ON (a.cod_programa = p.id_programa) " +
                "LEFT JOIN hosp.grupo g ON (a.cod_grupo = g.id_grupo) " +
                "LEFT JOIN hosp.equipe e ON (a.cod_equipe = e.id_equipe);";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                RemocaoProfissionalEquipe RemocaoProfissionalEquipe = new RemocaoProfissionalEquipe();
                RemocaoProfissionalEquipe.getFuncionario().setNome(rs.getString("descfuncionario"));
                RemocaoProfissionalEquipe.setPeriodoInicio(rs.getDate("data_inicio"));
                RemocaoProfissionalEquipe.setPeriodoFinal(rs.getDate("data_final"));
                RemocaoProfissionalEquipe.getPrograma().setDescPrograma(rs.getString("descprograma"));
                RemocaoProfissionalEquipe.getGrupo().setDescGrupo(rs.getString("descgrupo"));
                RemocaoProfissionalEquipe.getEquipe().setDescEquipe(rs.getString("descequipe"));
                RemocaoProfissionalEquipe.setTurno(rs.getString("turno"));

                lista.add(RemocaoProfissionalEquipe);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return lista;
    }

}
