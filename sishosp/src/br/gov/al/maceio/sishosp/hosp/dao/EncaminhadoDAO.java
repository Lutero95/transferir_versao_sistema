package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.EncaminhadoBean;

public class EncaminhadoDAO {
    private Connection conexao = null;

    public Boolean cadastrar(EncaminhadoBean encaminhado) {
        boolean retorno = false;

        String sql = "insert into hosp.encaminhado (descencaminhado) values (?)";

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, encaminhado.getDescencaminhado().toUpperCase().trim());

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

    public Boolean alterar(EncaminhadoBean encaminhado) {

        boolean retorno = false;
        String sql = "update hosp.encaminhado set descencaminhado = ? where id_encaminhado = ?";
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, encaminhado.getDescencaminhado().toUpperCase());
            stmt.setInt(2, encaminhado.getCodencaminhado());
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

    public Boolean excluir(EncaminhadoBean encaminhado) {

        boolean retorno = false;
        String sql = "delete from hosp.encaminhado where id_encaminhado = ?";
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, encaminhado.getCodencaminhado());
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

    public ArrayList<EncaminhadoBean> listaEncaminhados()
            throws ProjetoException {

        String sql = "select * from hosp.encaminhado order by descencaminhado";

        ArrayList<EncaminhadoBean> lista = new ArrayList();

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stm = conexao.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                EncaminhadoBean p = new EncaminhadoBean();

                p.setCodencaminhado(rs.getInt("id_encaminhado"));
                p.setDescencaminhado(rs.getString("descencaminhado")
                        .toUpperCase());

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

    public EncaminhadoBean buscaencaminhadocodigo(Integer i)
            throws ProjetoException {
        PreparedStatement ps = null;
        conexao = ConnectionFactory.getConnection();

        try {

            String sql = "select id_encaminhado, descencaminhado from hosp.encaminhado where id_encaminhado = ? order by descencaminhado";

            ps = conexao.prepareStatement(sql);
            ps.setInt(1, i);
            ResultSet rs = ps.executeQuery();

            EncaminhadoBean encaminhado = new EncaminhadoBean();
            while (rs.next()) {

                encaminhado.setCodencaminhado(rs.getInt(1));
                encaminhado.setDescencaminhado(rs.getString(2));

            }
            return encaminhado;
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

    public List<EncaminhadoBean> buscaencaminhado(String s)
            throws ProjetoException {
        PreparedStatement ps = null;
        conexao = ConnectionFactory.getConnection();

        try {
            List<EncaminhadoBean> listaencaminhados = new ArrayList<EncaminhadoBean>();
            String sql = "select id_encaminhado,id_encaminhado ||'-'|| descencaminhado descencaminhado from hosp.encaminhado "
                    + " where upper(id_encaminhado ||'-'|| descencaminhado) like ? order by descencaminhado";

            ps = conexao.prepareStatement(sql);
            ps.setString(1, "%" + s.toUpperCase() + "%");
            ResultSet rs = ps.executeQuery();

            List<EncaminhadoBean> colecao = new ArrayList<EncaminhadoBean>();

            while (rs.next()) {

                EncaminhadoBean encaminhado = new EncaminhadoBean();
                encaminhado.setCodencaminhado(rs.getInt("id_encaminhado"));
                encaminhado.setDescencaminhado(rs.getString("descencaminhado"));
                colecao.add(encaminhado);

            }
            return colecao;
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
