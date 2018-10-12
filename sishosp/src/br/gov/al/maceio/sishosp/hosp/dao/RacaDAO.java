package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.RacaBean;

public class RacaDAO {
    private Connection conexao = null;

    public Boolean cadastrar(RacaBean raca) {
        Boolean retorno = false;
        String sql = "insert into hosp.raca (descraca) values (?)";

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, raca.getDescRaca().toUpperCase().trim());
            stmt.execute();
            conexao.commit();
            retorno = true;

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return retorno;
        }
    }

    public Boolean alterar(RacaBean raca) {
        Boolean retorno = false;
        String sql = "update hosp.raca set descraca = ? where id_raca = ?";

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, raca.getDescRaca().toUpperCase());
            stmt.setInt(2, raca.getCodRaca());
            stmt.executeUpdate();

            conexao.commit();

            retorno = true;

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return retorno;
        }
    }

    public Boolean excluir(RacaBean raca) {
        Boolean retorno = false;
        String sql = "delete from hosp.raca where id_raca = ?";

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, raca.getCodRaca());
            stmt.executeUpdate();

            conexao.commit();

            retorno = true;

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return retorno;
        }
    }

    public ArrayList<RacaBean> listaCor() throws ProjetoException {

        String sql = "select  id_raca, descraca from hosp.raca order by descraca";

        ArrayList<RacaBean> lista = new ArrayList();

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stm = conexao.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                RacaBean p = new RacaBean();
                p.setCodRaca(rs.getInt("id_raca"));
                p.setDescRaca(rs.getString("descraca").toUpperCase());

                lista.add(p);
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
        return lista;
    }


    public RacaBean listarRacaPorID(int id) throws ProjetoException {
        String sql = "select  id_raca, descraca from hosp.raca where id_raca = ? order by descraca";

        ArrayList<RacaBean> lista = new ArrayList<RacaBean>();

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stm = conexao.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            RacaBean raca = new RacaBean();
            while (rs.next()) {
                raca.setCodRaca(rs.getInt("id_raca"));
                raca.setDescRaca(rs.getString("descraca").toUpperCase());
            }
            return raca;
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

    }
}
