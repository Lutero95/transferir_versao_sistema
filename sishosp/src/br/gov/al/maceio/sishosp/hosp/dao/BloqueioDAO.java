package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.BloqueioBean;

public class BloqueioDAO {
    Connection con = null;
    PreparedStatement ps = null;

    public boolean gravarBloqueio(BloqueioBean bloqueio) {

        Calendar calendarData = Calendar.getInstance();
        boolean condicao = true;
        String sql = "insert into hosp.bloqueio_agenda (codmedico, dataagenda, turno, descricao, codempresa) values (?, ?, ?, ?, ?);";

        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            Date dataInicio = bloqueio.getDataInicio();
            Date dataFim = bloqueio.getDataFim();
            do {
                if (dataInicio.after(dataFim) == false) {
                    ps.setLong(1, bloqueio.getProf().getId());
                    ps.setDate(2, new java.sql.Date(dataInicio.getTime()));
                    ps.setString(3, bloqueio.getTurno().toUpperCase());
                    ps.setString(4, bloqueio.getDescBloqueio().toUpperCase());
                    ps.setInt(5, 0); // COD EMPRESA ?
                    ps.execute();
                    con.commit();
                    condicao = true;
                    calendarData.setTime(dataInicio);
                    calendarData.add(Calendar.DATE, 1);
                    dataInicio = calendarData.getTime();
                } else {
                    condicao = false;
                }
            } while (condicao);
            con.commit();
            condicao = true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return condicao;
        }
    }

    public Boolean alterarBloqueio(BloqueioBean bloqueio) {

        Boolean retorno = false;
        String sql = "update hosp.bloqueio_agenda set codmedico = ?, dataagenda = ?,turno = ?, descricao = ?"
                + " where id_bloqueioagenda = ?";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setLong(1, bloqueio.getProf().getId());
            stmt.setDate(2, new java.sql.Date(bloqueio.getDataInicio()
                    .getTime()));
            stmt.setString(3, bloqueio.getTurno().toUpperCase());
            stmt.setString(4, bloqueio.getDescBloqueio().toUpperCase());
            stmt.setInt(5, bloqueio.getIdBloqueio());
            stmt.executeUpdate();
            con.commit();
            retorno = true;
        } catch (SQLException ex) {
            ex.printStackTrace();
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

    public Boolean excluirBloqueio(BloqueioBean bloqueio) {

        Boolean retorno = false;
        String sql = "delete from hosp.bloqueio_agenda where id_bloqueioagenda = ?";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setLong(1, bloqueio.getIdBloqueio());
            stmt.execute();
            con.commit();
            retorno = true;
        } catch (SQLException ex) {
            ex.printStackTrace();
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

    public List<BloqueioBean> listarBloqueio() throws ProjetoException {
        List<BloqueioBean> lista = new ArrayList<>();
        String sql = "select b.id_bloqueioagenda, b.codmedico, m.descfuncionario, b.dataagenda, b.turno, b.descricao, b.codempresa "
                + " from hosp.bloqueio_agenda b left join acl.funcionarios m on (b.codmedico = m.id_funcionario) order by b.id_bloqueioagenda";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                BloqueioBean bloqueio = new BloqueioBean();
                bloqueio.setIdBloqueio(rs.getInt("id_bloqueioagenda"));
                bloqueio.getProf().setId(rs.getLong("codmedico"));
                bloqueio.getProf().setNome(rs.getString("descfuncionario"));
                bloqueio.setDataInicio(rs.getDate("dataagenda"));
                bloqueio.setTurno(rs.getString("turno"));
                bloqueio.setDescBloqueio(rs.getString("descricao"));
                bloqueio.setCodEmpresa(rs.getInt("codempresa"));
                lista.add(bloqueio);
            }
        } catch (SQLException ex) {
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

    public BloqueioBean listarBloqueioPorId(int id) throws ProjetoException {

        BloqueioBean bloqueio = new BloqueioBean();
        String sql = "select b.id_bloqueioagenda, b.descricao, b.dataagenda, b.codmedico, m.descfuncionario "
                + " from hosp.bloqueio_agenda b left join acl.funcionarios m on (b.codmedico = m.id_funcionario) where b.id_bloqueioagenda = ?";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                bloqueio = new BloqueioBean();
                bloqueio.setIdBloqueio(rs.getInt("id_bloqueioagenda"));
                bloqueio.setDescBloqueio(rs.getString("descricao"));
                bloqueio.setDataInicio(rs.getDate("dataagenda"));
                bloqueio.getProf().setId(rs.getLong("codmedico"));
                bloqueio.getProf().setNome(rs.getString("descfuncionario"));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return bloqueio;
    }

}
