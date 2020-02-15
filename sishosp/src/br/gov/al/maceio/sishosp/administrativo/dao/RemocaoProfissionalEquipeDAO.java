package br.gov.al.maceio.sishosp.administrativo.dao;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.administrativo.model.RemocaoProfissionalEquipe;
import br.gov.al.maceio.sishosp.administrativo.model.dto.GravarRemocaoAtendimentoDTO;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.DataUtil;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.enums.Turno;

import javax.faces.context.FacesContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
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
                "CASE WHEN a.turno = 'T' THEN 'TARDE' " +
                "WHEN a.turno = 'M' THEN 'MANHÃ' END AS turno, pa.nome nomepaciente " +
                "FROM hosp.atendimentos a " +
                " join hosp.pacientes pa on pa.id_paciente = a.codpaciente " +
                "JOIN hosp.atendimentos1 a1 ON (a.id_atendimento = a1.id_atendimento) " +
                "JOIN acl.funcionarios f ON (a1.codprofissionalatendimento = f.id_funcionario) " +
                "JOIN hosp.programa p ON (a.codprograma = p.id_programa) " +
                "JOIN hosp.grupo g ON (a.codgrupo = g.id_grupo) " +
                "JOIN hosp.equipe e ON (a.codequipe = e.id_equipe) " +
                "WHERE a.codprograma = ? AND a1.codprofissionalatendimento = ? AND a.dtaatende >= ? AND a.dtaatende <= ? " +
                "AND a.situacao = 'A' AND coalesce(a1.excluido,'N')='N' and a1.evolucao is null";

        if (!RemocaoProfissionalEquipe.getTurno().equals(Turno.AMBOS.getSigla())) {
            sql = sql + "AND a.turno = ? ";
        }
        if ((!VerificadorUtil.verificarSeObjetoNuloOuZero(RemocaoProfissionalEquipe.getGrupo())) && (!VerificadorUtil.verificarSeObjetoNuloOuZero(RemocaoProfissionalEquipe.getGrupo().getIdGrupo()))) {
            sql = sql + "AND a.codgrupo = ? ";
        }
        if ((!VerificadorUtil.verificarSeObjetoNuloOuZero(RemocaoProfissionalEquipe)) && (!VerificadorUtil.verificarSeObjetoNuloOuZero(RemocaoProfissionalEquipe.getEquipe().getCodEquipe()))) {
            sql = sql + "AND a.codequipe = ? ";
        }

        if(RemocaoProfissionalEquipe.getDiasSemana().size() > 0){
            sql = sql + " AND ( ";
            for(int j=0; j<RemocaoProfissionalEquipe.getDiasSemana().size(); j++) {
                if(j == 0){
                    sql = sql + "EXTRACT(DOW FROM a.dtaatende) = ? ";
                }
                else{
                    sql = sql + "OR EXTRACT(DOW FROM a.dtaatende) = ? ";
                }
            }
            sql = sql + " ) ";
        }


        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);

            stm.setInt(1, RemocaoProfissionalEquipe.getPrograma().getIdPrograma());
            stm.setLong(2, RemocaoProfissionalEquipe.getFuncionario().getId());
            stm.setDate(3, DataUtil.converterDateUtilParaDateSql(RemocaoProfissionalEquipe.getPeriodoInicio()));
            stm.setDate(4, DataUtil.converterDateUtilParaDateSql(RemocaoProfissionalEquipe.getPeriodoFinal()));

            if (!RemocaoProfissionalEquipe.getTurno().equals(Turno.AMBOS.getSigla())) {
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
            if(RemocaoProfissionalEquipe.getDiasSemana().size() > 0){
                for(int j=0; j<RemocaoProfissionalEquipe.getDiasSemana().size(); j++) {
                    i++;
                    stm.setInt(i, Integer.parseInt(RemocaoProfissionalEquipe.getDiasSemana().get(j)));
                }
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
                remocaoProfissionalEquipe.getAtendimentoBean().getPaciente().setNome(rs.getString("nomepaciente"));

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

    public Boolean gravarRemocao(GravarRemocaoAtendimentoDTO gravarRemocaoAtendimentoDTO) {
        return gravarRemocaoProfissionalEquipeAtendimento(gravarRemocaoAtendimentoDTO);
    }

    private boolean gravarRemocaoProfissionalEquipeAtendimento(GravarRemocaoAtendimentoDTO gravarRemocaoAtendimentoDTO) {

        Boolean retorno = false;

        String sql = "INSERT INTO adm.remocao_profissional_equipe_atendimento " +
                "(data_hora_operacao, codusuario_operacao, cod_programa, cod_grupo, cod_equipe, data_inicio, data_final, turno) " +
                "VALUES (current_timestamp, ?, ?, ?, ?, ?, ?, ?) RETURNING id";

        try {

            FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
                    .getSessionMap().get("obj_usuario");

            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            ps.setLong(1, user_session.getId());
            ps.setInt(2, gravarRemocaoAtendimentoDTO.getRemocaoProfissionalEquipe().getPrograma().getIdPrograma());

            if(!VerificadorUtil.verificarSeObjetoNuloOuZero(gravarRemocaoAtendimentoDTO.getRemocaoProfissionalEquipe().getGrupo().getIdGrupo())){
                ps.setInt(3, gravarRemocaoAtendimentoDTO.getRemocaoProfissionalEquipe().getGrupo().getIdGrupo());
            }
            else {
                ps.setNull(3, Types.NULL);
            }

            if(!VerificadorUtil.verificarSeObjetoNuloOuZero(gravarRemocaoAtendimentoDTO.getRemocaoProfissionalEquipe().getEquipe().getCodEquipe())){
                ps.setInt(4, gravarRemocaoAtendimentoDTO.getRemocaoProfissionalEquipe().getEquipe().getCodEquipe());
            }
            else {
                ps.setNull(4, Types.NULL);
            }

            ps.setDate(5, DataUtil.converterDateUtilParaDateSql(gravarRemocaoAtendimentoDTO.getRemocaoProfissionalEquipe().getPeriodoInicio()));
            ps.setDate(6, DataUtil.converterDateUtilParaDateSql(gravarRemocaoAtendimentoDTO.getRemocaoProfissionalEquipe().getPeriodoFinal()));
            ps.setString(7, gravarRemocaoAtendimentoDTO.getRemocaoProfissionalEquipe().getTurno());

            ResultSet rs = ps.executeQuery();

            int id = 0;

            if (rs.next()) {
                id = rs.getInt("id");
            }

            GravarRemocaoAtendimentoDTO gravarAtendimentoDTO = new GravarRemocaoAtendimentoDTO(
                    gravarRemocaoAtendimentoDTO.getRemocaoProfissionalEquipe(), gravarRemocaoAtendimentoDTO.getListaRemocao(), con, id);

            retorno = gravarRemocaoProfissionalEquipeAtendimento1(gravarAtendimentoDTO);


        } catch (Exception ex) {
            ex.printStackTrace();
            JSFUtil.adicionarMensagemErro(ex.getMessage(), "Atenção");
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return retorno;
        }
    }

    private boolean gravarRemocaoProfissionalEquipeAtendimento1(GravarRemocaoAtendimentoDTO gravarRemocao) {

        Boolean retorno = false;

        String sql = "INSERT INTO adm.remocao_profissional_equipe_atendimento_1 " +
                "(id_atendimentos1, id_remocao_profissional_equipe_atendimento, id_profissional) " +
                "VALUES(?, ?, ?);";

        try {

            ps = gravarRemocao.getConexaoAuxiliar().prepareStatement(sql);

            for (int i = 0; i < gravarRemocao.getListaRemocao().size(); i++) {
                ps.setInt(1, gravarRemocao.getListaRemocao().get(i).getAtendimentoBean().getId1());
                ps.setInt(2, gravarRemocao.getCodRemocaoProfissional());
                ps.setLong(3, gravarRemocao.getRemocaoProfissionalEquipe().getFuncionario().getId());
                ps.execute();
            }


            retorno = gravarRemocaoAtendimentos1(gravarRemocao);

        } catch (Exception ex) {
            ex.printStackTrace();
            JSFUtil.adicionarMensagemErro(ex.getMessage(), "Atenção");
            throw new RuntimeException(ex);
        } finally {
            try {
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return retorno;
        }
    }

    private boolean gravarRemocaoAtendimentos1(GravarRemocaoAtendimentoDTO gravarRemocao) {

        Boolean retorno = false;

        String sql = "UPDATE hosp.atendimentos1 SET excluido = 'S', data_hora_exclusao = current_timestamp, usuario_exclusao = ? " +
                "WHERE id_atendimentos1 = ?";

        try {

            FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
                    .getSessionMap().get("obj_usuario");

            ps = gravarRemocao.getConexaoAuxiliar().prepareStatement(sql);

            for (int i = 0; i < gravarRemocao.getListaRemocao().size(); i++) {
                ps.setLong(1, user_session.getId());
                ps.setInt(2, gravarRemocao.getListaRemocao().get(i).getAtendimentoBean().getId1());
                ps.execute();
            }

            retorno = true;

            if (retorno) {
                con.commit();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JSFUtil.adicionarMensagemErro(ex.getMessage(), "Atenção");
            throw new RuntimeException(ex);
        } finally {
            try {
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return retorno;
        }
    }


    public List<RemocaoProfissionalEquipe> listarRemovidos() {

        List<RemocaoProfissionalEquipe> lista = new ArrayList<>();

        String sql = "SELECT DISTINCT  f.descfuncionario, a.data_inicio, a.data_final, " +
                "p.descprograma, g.descgrupo, e.descequipe, " +
                "CASE WHEN a.turno = 'A' THEN 'AMBOS' WHEN a.turno = 'T' THEN 'TARDE' " +
                "WHEN a.turno = 'M' THEN 'MANHÃ' END AS turno " +
                "FROM adm.remocao_profissional_equipe_atendimento a " +
                "JOIN adm.remocao_profissional_equipe_atendimento_1 a1 ON (a.id = a1.id_remocao_profissional_equipe_atendimento) " +
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
