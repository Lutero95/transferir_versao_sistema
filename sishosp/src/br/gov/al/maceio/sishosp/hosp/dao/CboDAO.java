package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.CboBean;

public class CboDAO {

    Connection con = null;
    PreparedStatement ps = null;

    public boolean gravarCBO(CboBean cbo) {
        Boolean retorno = false;
        String sql = "insert into hosp.cbo (descricao, codigo) values (?,?);";
        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, cbo.getDescCbo().toUpperCase());
            ps.setString(2, cbo.getCodigo().toUpperCase());
            ps.execute();
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

    public List<CboBean> listarCbo() throws ProjetoException {
        List<CboBean> lista = new ArrayList<>();
        String sql = "select id , descricao, codigo from hosp.cbo order by descricao ";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                CboBean cbo = new CboBean();
                cbo.setCodCbo(rs.getInt("id"));
                cbo.setDescCbo(rs.getString("descricao"));
                cbo.setCodigo(rs.getString("codigo"));
                lista.add(cbo);
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

    public CboBean listarCboPorId(int id) throws ProjetoException {

        CboBean cbo = new CboBean();
        String sql = "select id , descricao, codigo from hosp.cbo where id = ?";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                cbo = new CboBean();
                cbo.setCodCbo(rs.getInt("id"));
                cbo.setDescCbo(rs.getString("descricao"));
                cbo.setCodigo(rs.getString("codigo"));
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
        return cbo;
    }

    public List<CboBean> listarCboBusca(String descricao)
            throws ProjetoException {
        List<CboBean> lista = new ArrayList<>();
        String sql = "select id , descricao, codigo from hosp.cbo where descricao LIKE ? order by descricao";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setString(1, "%" + descricao.toUpperCase() + "%");
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                CboBean cbo = new CboBean();
                cbo.setCodCbo(rs.getInt("id"));
                cbo.setDescCbo(rs.getString("descricao"));
                cbo.setCodigo(rs.getString("codigo"));

                lista.add(cbo);
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

    public Boolean alterarCbo(CboBean cbo) {
        Boolean retorno = false;
        String sql = "update hosp.cbo set descricao = ?, codigo=? where id = ?";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, cbo.getDescCbo().toUpperCase());
            stmt.setString(2, cbo.getCodigo().toUpperCase());
            stmt.setInt(3, cbo.getCodCbo());
            stmt.executeUpdate();
            con.commit();
            retorno = true;
        } catch (SQLException ex) {
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

    public Boolean excluirCbo(CboBean cbo) {
        Boolean retorno = false;
        String sql = "delete from hosp.cbo where id = ?";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setLong(1, cbo.getCodCbo());
            stmt.execute();
            con.commit();
            retorno = true;
        } catch (SQLException ex) {
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
}
