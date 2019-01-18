package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.TipoAtendimentoBean;

public class TipoAtendimentoDAO {

    Connection con = null;
    PreparedStatement ps = null;

    public boolean gravarTipoAt(TipoAtendimentoBean tipo) {

        Boolean retorno = false;
        String sql = "insert into hosp.tipoatendimento (desctipoatendimento, primeiroatendimento, equipe_programa, id, intervalo_minimo) "
                + " values (?, ?, ?, DEFAULT, ?) RETURNING id;";
        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, tipo.getDescTipoAt().toUpperCase());
            ps.setBoolean(2, tipo.isPrimeiroAt());
            ps.setBoolean(3, tipo.isEquipe());
            ps.setInt(4, tipo.getIntervaloMinimo());
            ResultSet rs = ps.executeQuery();
            int idTipo = 0;
            if (rs.next()) {
                idTipo = rs.getInt("id");
                retorno = insereTipoAtendimentoGrupo(idTipo, tipo.getGrupo(), con);
            }

            if (retorno) {
                con.commit();
                retorno = true;
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
            return retorno;
        }
    }

    public boolean alterarTipo(TipoAtendimentoBean tipo) {

        Boolean retorno = false;
        String sql = "update hosp.tipoatendimento set desctipoatendimento = ?, primeiroatendimento = ?, equipe_programa = ?, intervalo_minimo = ? where id = ?";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, tipo.getDescTipoAt().toUpperCase());
            stmt.setBoolean(2, tipo.isPrimeiroAt());
            stmt.setBoolean(3, tipo.isEquipe());
            stmt.setInt(4, tipo.getIntervaloMinimo());
            stmt.setInt(5, tipo.getIdTipo());
            stmt.executeUpdate();

            retorno = excluirTipoGrupo(tipo.getIdTipo(), con);

            if (retorno) {
                retorno = insereTipoAtendimentoGrupo(tipo.getIdTipo(), tipo.getGrupoNovo(), con);
            }

            if (retorno) {
                con.commit();
                retorno = true;
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
            return retorno;
        }
    }

    public Boolean excluirTipo(TipoAtendimentoBean tipo) {

        Boolean retorno = false;

        String sql = "delete from hosp.tipoatendimento where id = ?";

        try {
            con = ConnectionFactory.getConnection();

            retorno = excluirTipoGrupo(tipo.getIdTipo(), con);

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setLong(1, tipo.getIdTipo());
            stmt.execute();

            if (retorno) {
                con.commit();
                retorno = true;
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
            return retorno;
        }
    }

    public Boolean excluirTipoGrupo(int idTipo, Connection conn) {

        Boolean retorno = false;

        String sql = "delete from hosp.tipoatendimento_grupo where codtipoatendimento = ?";
        retorno = true;

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setLong(1, idTipo);
            stmt.execute();
            retorno = true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return retorno;
        }
    }

    public Boolean insereTipoAtendimentoGrupo(int idTipo,
                                              List<GrupoBean> listaGrupo, Connection conn) {

        Boolean retorno = false;
        String sql = "insert into hosp.tipoatendimento_grupo (codgrupo, codtipoatendimento) values(?,?);";
        try {
            ps = conn.prepareStatement(sql);
            for (GrupoBean grupoBean : listaGrupo) {
                ps.setInt(1, grupoBean.getIdGrupo());
                ps.setInt(2, idTipo);

                ps.execute();
            }

            retorno = true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return retorno;
        }
    }

    public List<TipoAtendimentoBean> listarTipoAtPorGrupo(int codGrupo)
            throws ProjetoException {
        List<TipoAtendimentoBean> lista = new ArrayList<>();
        String sql = "select t.id, t.desctipoatendimento, t.primeiroatendimento, t.equipe_programa, t.intervalo_minimo "
                + " from hosp.tipoatendimento t left join hosp.tipoatendimento_grupo tg on (t.id = tg.codtipoatendimento) "
                + " where tg.codgrupo = ? order by t.desctipoatendimento";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, codGrupo);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                TipoAtendimentoBean tipo = new TipoAtendimentoBean();
                tipo.setIdTipo(rs.getInt("id"));
                tipo.setDescTipoAt(rs.getString("desctipoatendimento"));
                tipo.setPrimeiroAt(rs.getBoolean("primeiroatendimento"));
                tipo.setEquipe(rs.getBoolean("equipe_programa"));
                tipo.setIntervaloMinimo(rs.getInt("intervalo_minimo"));

                lista.add(tipo);
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

    public List<TipoAtendimentoBean> listarTipoAt() throws ProjetoException {
        List<TipoAtendimentoBean> lista = new ArrayList<>();
        String sql = "select id, desctipoatendimento, primeiroatendimento, equipe_programa, intervalo_minimo from hosp.tipoatendimento order by desctipoatendimento";
        GrupoDAO gDao = new GrupoDAO();
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                TipoAtendimentoBean tipo = new TipoAtendimentoBean();
                tipo.setIdTipo(rs.getInt("id"));
                tipo.setDescTipoAt(rs.getString("desctipoatendimento"));
                tipo.setPrimeiroAt(rs.getBoolean("primeiroatendimento"));
                tipo.setEquipe(rs.getBoolean("equipe_programa"));
                tipo.setGrupo(gDao.listarGruposPorTipoAtend(rs.getInt("id")));
                tipo.setIntervaloMinimo(rs.getInt("intervalo_minimo"));

                lista.add(tipo);
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

    public List<TipoAtendimentoBean> listarTipoAtBusca(String descricao,
                                                       Integer tipo) throws ProjetoException {
        List<TipoAtendimentoBean> lista = new ArrayList<>();
        String sql = "select id, desctipoatendimento, primeiroatendimento, equipe_programa, intervalo_minimo "
                + " from hosp.tipoatendimento";
        if (tipo == 1) {
            sql += " where desctipoatendimento LIKE ?  order by desctipoatendimento";
        }
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setString(1, "%" + descricao.toUpperCase() + "%");
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                TipoAtendimentoBean tipo1 = new TipoAtendimentoBean();
                tipo1.setIdTipo(rs.getInt("id"));
                tipo1.setDescTipoAt(rs.getString("desctipoatendimento"));
                tipo1.setPrimeiroAt(rs.getBoolean("primeiroatendimento"));
                tipo1.setEquipe(rs.getBoolean("equipe_programa"));
                tipo1.setIntervaloMinimo(rs.getInt("intervalo_minimo"));

                lista.add(tipo1);
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

    public List<TipoAtendimentoBean> listarTipoAtAutoComplete(String descricao,
                                                              GrupoBean grupo) throws ProjetoException {

        PreparedStatement stm = null;
        con = ConnectionFactory.getConnection();
        List<TipoAtendimentoBean> lista = new ArrayList<>();

        try {
            String sql = "select t.id, t.desctipoatendimento, t.primeiroatendimento, t.equipe_programa, t.intervalo_minimo "
                    + " from hosp.tipoatendimento t left join hosp.tipoatendimento_grupo tg on (t.id = tg.codtipoatendimento) "
                    + " where tg.codgrupo = ? and upper(t.id ||' - '|| t.desctipoatendimento) LIKE ? order by t.desctipoatendimento";

            stm = con.prepareStatement(sql);
            stm.setInt(1, grupo.getIdGrupo());
            stm.setString(2, "%" + descricao.toUpperCase() + "%");
            ResultSet rs = stm.executeQuery();


            while (rs.next()) {
                TipoAtendimentoBean tipo1 = new TipoAtendimentoBean();
                tipo1.setIdTipo(rs.getInt("id"));
                tipo1.setDescTipoAt(rs.getString("desctipoatendimento"));
                tipo1.setPrimeiroAt(rs.getBoolean("primeiroatendimento"));
                tipo1.setEquipe(rs.getBoolean("primeiroatendimento"));
                tipo1.setIntervaloMinimo(rs.getInt("intervalo_minimo"));

                lista.add(tipo1);
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

    public TipoAtendimentoBean listarTipoPorId(int id) throws ProjetoException {
        String sql = "select id, desctipoatendimento, primeiroatendimento, equipe_programa, intervalo_minimo "
                + " from hosp.tipoatendimento WHERE id = ?";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            GrupoDAO gDao = new GrupoDAO();
            TipoAtendimentoBean tipo = null;
            while (rs.next()) {
                tipo = new TipoAtendimentoBean();
                tipo.setIdTipo(rs.getInt("id"));
                tipo.setDescTipoAt(rs.getString("desctipoatendimento"));
                tipo.setPrimeiroAt(rs.getBoolean("primeiroatendimento"));
                tipo.setEquipe(rs.getBoolean("equipe_programa"));
                tipo.setGrupo(gDao.listarGruposPorTipoAtend(tipo.getIdTipo()));
                tipo.setIntervaloMinimo(rs.getInt("intervalo_minimo"));
            }
            return tipo;
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
    }

}
