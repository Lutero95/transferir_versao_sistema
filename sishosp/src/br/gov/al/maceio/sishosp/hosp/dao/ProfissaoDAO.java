package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.ProfissaoBean;

public class ProfissaoDAO {
    private Connection conexao = null;

    public Boolean cadastrar(ProfissaoBean profissao) {
        boolean retorno = false;

        String sql = "insert into hosp.profissao (descprofissao)"
                + " values (?)";

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, profissao.getDescprofissao().toUpperCase().trim());

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

    public Boolean alterar(ProfissaoBean profissao) {
        boolean retorno = false;
        String sql = "update hosp.profissao set descprofissao = ? where id_profissao = ?";
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, profissao.getDescprofissao());
            stmt.setInt(2, profissao.getCodprofissao());
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

    public Boolean excluir(ProfissaoBean profissao) {
        boolean retorno = false;
        String sql = "delete from hosp.profissao where id_profissao = ?";
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, profissao.getCodprofissao());
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

    public ArrayList<ProfissaoBean> listaProfissoes() throws ProjetoException {

        String sql = "select * from hosp.profissao order by descprofissao";

        ArrayList<ProfissaoBean> lista = new ArrayList();

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stm = conexao.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                ProfissaoBean p = new ProfissaoBean();

                p.setCodprofissao(rs.getInt("id_profissao"));
                p.setDescprofissao(rs.getString("descprofissao").toUpperCase());

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

    public ProfissaoBean buscaprofissaocodigo(Integer i)
            throws ProjetoException {
        PreparedStatement ps = null;
        conexao = ConnectionFactory.getConnection();

        try {

            String sql = "select id_profissao, descprofissao from hosp.profissao where id_profissao=? order by descprofissao";

            ps = conexao.prepareStatement(sql);
            ps.setInt(1, i);
            ResultSet rs = ps.executeQuery();

            ProfissaoBean profissao = new ProfissaoBean();
            while (rs.next()) {

                profissao.setCodprofissao(rs.getInt(1));
                profissao.setDescprofissao(rs.getString(2));

            }
            return profissao;
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

    public List<ProfissaoBean> buscaprofissao(String s) throws ProjetoException {
        PreparedStatement ps = null;
        conexao = ConnectionFactory.getConnection();

        try {
            List<ProfissaoBean> listaprofissoes = new ArrayList<ProfissaoBean>();
            String sql = "select id_profissao,id_profissao ||'-'|| descprofissao descprofissao from hosp.profissao "
                    + " where upper(id_profissao ||'-'|| descprofissao) like ? order by descprofissao";

            ps = conexao.prepareStatement(sql);
            ps.setString(1, "%" + s.toUpperCase() + "%");
            ResultSet rs = ps.executeQuery();

            List<ProfissaoBean> colecao = new ArrayList<ProfissaoBean>();

            while (rs.next()) {

                ProfissaoBean profissao = new ProfissaoBean();
                profissao.setCodprofissao(rs.getInt("id_profissao"));
                profissao.setDescprofissao(rs.getString("descprofissao"));
                colecao.add(profissao);

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
