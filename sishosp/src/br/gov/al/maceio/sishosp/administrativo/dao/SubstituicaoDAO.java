package br.gov.al.maceio.sishosp.administrativo.dao;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.administrativo.model.SubstituicaoFuncionario;
import br.gov.al.maceio.sishosp.administrativo.model.dto.BuscaAgendamentosParaFuncionarioAfastadoDTO;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.DataUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.model.AtendimentoBean;

import javax.faces.context.FacesContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SubstituicaoDAO {

    Connection con = null;
    PreparedStatement ps = null;


    public Boolean substituirFuncionario(List<AtendimentoBean> listaSelecionados, SubstituicaoFuncionario substituicaoFuncionario) {

        Boolean retorno = false;

        String sql = "UPDATE hosp.atendimentos1 SET codprofissionalatendimento = ? WHERE id_atendimentos1 = ?";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);

            for(int i=0; i<listaSelecionados.size(); i++) {
                stmt.setLong(1, substituicaoFuncionario.getFuncionario().getId());
                stmt.setInt(2, listaSelecionados.get(i).getId1());

                stmt.executeUpdate();

                gravarSubstituicao(listaSelecionados.get(i), substituicaoFuncionario, con);

            }

            con.commit();

            retorno = true;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            return retorno;
        }
    }

    public boolean gravarSubstituicao(AtendimentoBean atendimentoBean, SubstituicaoFuncionario substituicaoFuncionario, Connection conAuxiliar) {

        Boolean retorno = false;

        String sql = "INSERT INTO adm.substituicao_funcionario " +
                "(id_afastamento_funcionario, id_atendimentos1, id_funcionario_substituido, id_funcionario_substituto, usuario_acao, data_hora_acao) " +
                "VALUES(?, ?, ?, ?, ?, CURRENT_TIMESTAMP );";
        try {

            FuncionarioBean user_session = (FuncionarioBean) FacesContext
                    .getCurrentInstance().getExternalContext().getSessionMap()
                    .get("obj_usuario");

            ps = conAuxiliar.prepareStatement(sql);
            ps.setInt(1, substituicaoFuncionario.getAfastamentoTemporario().getId());
            ps.setInt(2, atendimentoBean.getId1());
            ps.setLong(3, substituicaoFuncionario.getAfastamentoTemporario().getFuncionario().getId());
            ps.setLong(4, substituicaoFuncionario.getFuncionario().getId());
            ps.setLong(5, user_session.getId());

            ps.execute();
            retorno = true;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ProjetoException(ex);
        } finally {
            try {
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return retorno;
        }
    }

    public List<AtendimentoBean> listarHorariosParaSeremSubstituidos(BuscaAgendamentosParaFuncionarioAfastadoDTO buscaAgendamentosParaFuncionarioAfastadoDTO) {

        List<AtendimentoBean> lista = new ArrayList<>();

        String sql = "SELECT a1.id_atendimentos1, a.codgrupo, g.descgrupo, a.codprograma, p.descprograma, a.dtaatende, " +
                "CASE WHEN a.turno = 'M' THEN 'Manhã' WHEN a.turno = 'T' THEN 'Tarde' END AS turno " +
                "FROM hosp.atendimentos1 a1 " +
                "JOIN hosp.atendimentos a ON (a1.id_atendimento = a.id_atendimento) " +
                "JOIN hosp.grupo g ON (a.codgrupo = g.id_grupo) " +
                "JOIN hosp.programa p ON (a.codprograma = p.id_programa) " +
                "WHERE a1.codprofissionalatendimento = ? AND a.dtaatende >= ? AND a.dtaatende <= ? ";

        if (!VerificadorUtil.verificarSeObjetoNulo(buscaAgendamentosParaFuncionarioAfastadoDTO.getGrupo().getIdGrupo())) {
            sql = sql + "AND a.codprograma = ?";
        }

        if (!VerificadorUtil.verificarSeObjetoNulo(buscaAgendamentosParaFuncionarioAfastadoDTO.getPrograma().getIdPrograma())) {
            sql = sql + "AND a.codgrupo = ? ";
        }

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setLong(1, buscaAgendamentosParaFuncionarioAfastadoDTO.getFuncionario().getId());
            stm.setDate(2, DataUtil.converterDateUtilParaDateSql(buscaAgendamentosParaFuncionarioAfastadoDTO.getPeriodoInicio()));
            stm.setDate(3, DataUtil.converterDateUtilParaDateSql(buscaAgendamentosParaFuncionarioAfastadoDTO.getPeriodoFinal()));
            int i = 3;

            if (!VerificadorUtil.verificarSeObjetoNulo(buscaAgendamentosParaFuncionarioAfastadoDTO.getPrograma().getIdPrograma())) {
                i = i + 1;
                stm.setInt(i, buscaAgendamentosParaFuncionarioAfastadoDTO.getPrograma().getIdPrograma());
            }

            if (!VerificadorUtil.verificarSeObjetoNulo(buscaAgendamentosParaFuncionarioAfastadoDTO.getGrupo().getIdGrupo())) {
                i = i + 1;
                stm.setInt(i, buscaAgendamentosParaFuncionarioAfastadoDTO.getGrupo().getIdGrupo());
            }

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                AtendimentoBean atendimentoBean = new AtendimentoBean();
                atendimentoBean.setId1(rs.getInt("id_atendimentos1"));
                atendimentoBean.getGrupo().setIdGrupo(rs.getInt("codgrupo"));
                atendimentoBean.getGrupo().setDescGrupo(rs.getString("descgrupo"));
                atendimentoBean.getPrograma().setIdPrograma(rs.getInt("codprograma"));
                atendimentoBean.getPrograma().setDescPrograma(rs.getString("descprograma"));
                atendimentoBean.setDataAtendimentoInicio(rs.getDate("dtaatende"));
                atendimentoBean.setTurno(rs.getString("turno"));

                lista.add(atendimentoBean);
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
