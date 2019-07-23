package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.EscolaridadeBean;

public class EscolaridadeDAO {
    private Connection conexao = null;

    public Boolean cadastrar(EscolaridadeBean escolaridade) {
        boolean retorno = false;

        String sql = "insert into hosp.escolaridade (descescolaridade)"
                + " values (?)";

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, escolaridade.getDescescolaridade().toUpperCase()
                    .trim());

            stmt.execute();
            conexao.commit();
            retorno = true;

        } catch (Exception ex) {
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

    public Boolean alterar(EscolaridadeBean escolaridade) {
        boolean retorno = false;
        String sql = "update hosp.escolaridade set descescolaridade = ? where id_escolaridade = ?";
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, escolaridade.getDescescolaridade().toUpperCase());
            stmt.setInt(2, escolaridade.getCodescolaridade());
            stmt.executeUpdate();

            conexao.commit();

            retorno = true;

        } catch (Exception ex) {
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

    public Boolean excluir(EscolaridadeBean escolaridade) {
        boolean retorno = false;
        String sql = "delete from hosp.escolaridade where id_escolaridade = ?";
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, escolaridade.getCodescolaridade());
            stmt.executeUpdate();

            conexao.commit();

            retorno = true;

        } catch (Exception ex) {
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

    public ArrayList<EscolaridadeBean> listaEscolaridade()
            throws ProjetoException {

        String sql = "select * from hosp.escolaridade order by descescolaridade";

        ArrayList<EscolaridadeBean> lista = new ArrayList();

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stm = conexao.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                EscolaridadeBean e = new EscolaridadeBean();

                e.setCodescolaridade(rs.getInt("id_escolaridade"));
                e.setDescescolaridade(rs.getString("descescolaridade")
                        .toUpperCase());

                lista.add(e);
            }
        } catch (Exception ex) {
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

    public EscolaridadeBean buscaescolaridadecodigo(Integer i)
            throws ProjetoException {
        PreparedStatement ps = null;
        conexao = ConnectionFactory.getConnection();

        try {

            String sql = "select id_escolaridade, descescolaridade from hosp.escolaridade where id_escolaridade=? order by descescolaridade";

            ps = conexao.prepareStatement(sql);
            ps.setInt(1, i);
            ResultSet rs = ps.executeQuery();

            EscolaridadeBean escolaridade = new EscolaridadeBean();
            while (rs.next()) {

                escolaridade.setCodescolaridade(rs.getInt("id_escolaridade"));
                escolaridade.setDescescolaridade(rs.getString("descescolaridade"));

            }
            return escolaridade;
        } catch (Exception ex) {
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

    public List<EscolaridadeBean> buscaescolaridade(String s)
            throws ProjetoException {
        PreparedStatement ps = null;
        conexao = ConnectionFactory.getConnection();

        try {
            List<EscolaridadeBean> listaescolaridades = new ArrayList<EscolaridadeBean>();
            String sql = "select id_escolaridade,id_escolaridade ||'-'|| descescolaridade descescolaridade from hosp.escolaridade "
                    + " where upper(id_escolaridade ||'-'|| descescolaridade) like ? order by descescolaridade";

            ps = conexao.prepareStatement(sql);
            ps.setString(1, "%" + s.toUpperCase() + "%");
            ResultSet rs = ps.executeQuery();

            List<EscolaridadeBean> colecao = new ArrayList<EscolaridadeBean>();

            while (rs.next()) {

                EscolaridadeBean escolaridade = new EscolaridadeBean();
                escolaridade.setCodescolaridade(rs.getInt("id_escolaridade"));
                escolaridade.setDescescolaridade(rs
                        .getString("descescolaridade"));
                colecao.add(escolaridade);

            }
            return colecao;
        } catch (Exception ex) {
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
