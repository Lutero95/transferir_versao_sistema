package br.gov.al.maceio.sishosp.administrativo.dao;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.administrativo.model.AfastamentoTemporario;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.DataUtil;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;

import javax.faces.context.FacesContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class AfastamentoTemporarioDAO {

    Connection con = null;
    PreparedStatement ps = null;

    public boolean gravarAfastamentoTemporario(AfastamentoTemporario afastamentoTemporario) {

        Boolean retorno = false;

        String sql = "insert into adm.afastamento_funcionario (tipo_afastamento, id_funcionario_afastado, inicio_afastamento, fim_afastamento, motivo_afastamento, " +
                "operador_cadastro, data_hora_cadastro, turno) " +
                "values (?,?,?,?,?,?,CURRENT_TIMESTAMP, ? );";
        try {

            FuncionarioBean user_session = (FuncionarioBean) FacesContext
                    .getCurrentInstance().getExternalContext().getSessionMap()
                    .get("obj_usuario");

            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, afastamentoTemporario.getTipoAfastamento());
            ps.setLong(2, afastamentoTemporario.getFuncionario().getId());
            ps.setDate(3, DataUtil.converterDateUtilParaDateSql(afastamentoTemporario.getPeriodoInicio()));
            ps.setDate(4, DataUtil.converterDateUtilParaDateSql(afastamentoTemporario.getPeriodoFinal()));
            ps.setString(5, afastamentoTemporario.getMotivoAfastamento());
            ps.setLong(6, user_session.getId());
			if (afastamentoTemporario.getTurno() != null) {
				ps.setString(7, afastamentoTemporario.getTurno());
			} else {
				ps.setNull(7, Types.NULL);
			}

            ps.execute();
            con.commit();
            retorno = true;
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

    public List<AfastamentoTemporario> listarAfastamentoTemporario() {

        List<AfastamentoTemporario> lista = new ArrayList<>();

        String sql = "SELECT a.id, a.tipo_afastamento, a.id_funcionario_afastado, f.descfuncionario, " +
                "a.inicio_afastamento, a.fim_afastamento, a.motivo_afastamento," +
                "CASE WHEN a.motivo_afastamento = 'FE' THEN 'Férias' " +
                "WHEN a.motivo_afastamento = 'LM' THEN 'Licença Médica' WHEN a.motivo_afastamento = 'FA' THEN 'Falta' END AS motivo_afastamento_extenso " +
                "FROM adm.afastamento_funcionario a " +
                "JOIN acl.funcionarios f ON (a.id_funcionario_afastado = f.id_funcionario) " +
                "ORDER BY a.inicio_afastamento ";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                AfastamentoTemporario afastamentoTemporario = new AfastamentoTemporario();
                afastamentoTemporario.setId(rs.getInt("id"));
                afastamentoTemporario.setTipoAfastamento(rs.getString("tipo_afastamento"));
                afastamentoTemporario.getFuncionario().setId(rs.getLong("id_funcionario_afastado"));
                afastamentoTemporario.getFuncionario().setNome(rs.getString("descfuncionario"));
                afastamentoTemporario.setPeriodoInicio(rs.getDate("inicio_afastamento"));
                afastamentoTemporario.setPeriodoFinal(rs.getDate("fim_afastamento"));
                afastamentoTemporario.setMotivoAfastamento(rs.getString("motivo_afastamento"));
                afastamentoTemporario.setMotivoAfastamentoPorExtenso(rs.getString("motivo_afastamento_extenso"));
                lista.add(afastamentoTemporario);
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

    public AfastamentoTemporario listarAfastamentoTemporarioPorId(int id) {

        AfastamentoTemporario afastamentoTemporario = new AfastamentoTemporario();

        String sql = "select id, tipo_afastamento, id_funcionario_afastado, inicio_afastamento, fim_afastamento, motivo_afastamento " +
                "from adm.afastamento_funcionario where id = ? ";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                afastamentoTemporario = new AfastamentoTemporario();
                afastamentoTemporario.setId(rs.getInt("id"));
                afastamentoTemporario.setTipoAfastamento(rs.getString("tipo_afastamento"));
                afastamentoTemporario.getFuncionario().setId(rs.getLong("id_funcionario_afastado"));
                afastamentoTemporario.setPeriodoInicio(rs.getDate("inicio_afastamento"));
                afastamentoTemporario.setPeriodoFinal(rs.getDate("fim_afastamento"));
                afastamentoTemporario.setMotivoAfastamento(rs.getString("motivo_afastamento"));
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
        return afastamentoTemporario;
    }

    public Boolean excluirAfastamentoTemporario(int id) {

        Boolean retorno = false;

        String sql = "delete from adm.afastamento_funcionario where id = ?";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setLong(1, id);
            stmt.execute();
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

    public FuncionarioBean carregarFuncionarioAfastado(int idAfastamento) {

        FuncionarioBean funcionarioBean = new FuncionarioBean();

        String sql = "SELECT a.id_funcionario_afastado, f.descfuncionario " +
                "FROM adm.afastamento_funcionario a " +
                "JOIN acl.funcionarios f ON (a.id_funcionario_afastado = f.id_funcionario) " +
                "WHERE id = ?;";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, idAfastamento);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                funcionarioBean.setId(rs.getLong("id_funcionario_afastado"));
                funcionarioBean.setNome(rs.getString("descfuncionario"));
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
        return funcionarioBean;
    }
}
