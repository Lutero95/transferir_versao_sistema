package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.RecursoBean;

public class RecursoDAO {
    private Connection conexao = null;

    public Boolean cadastrar(RecursoBean recurso) {
        boolean retorno = false;

        String sql = "insert into hosp.recurso (descrecurso) values (?)";

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, recurso.getDescRecurso().toUpperCase().trim());

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

    public Boolean alterar(RecursoBean recurso) {

        boolean retorno = false;

        String sql = "update hosp.recurso set descrecurso = ? where id = ?";

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);

            stmt.setString(1, recurso.getDescRecurso().toUpperCase());
            stmt.setInt(2, recurso.getIdRecurso());
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

    public Boolean excluir(RecursoBean recurso) {

        boolean retorno = false;

        String sql = "delete from hosp.recurso where id = ?";

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, recurso.getIdRecurso());
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

    public ArrayList<RecursoBean> listaRecursos() throws ProjetoException {

        String sql = "select id, descrecurso from hosp.recurso order by descrecurso";

        ArrayList<RecursoBean> lista = new ArrayList();

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stm = conexao.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                RecursoBean r = new RecursoBean();

                r.setIdRecurso(rs.getInt("id"));
                r.setDescRecurso(rs.getString("descrecurso"));

                lista.add(r);
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

    public ArrayList<RecursoBean> listaRecursosPorProcedimento(Integer id_proc) throws ProjetoException {

        String sql = "select r.id, r.descrecurso from hosp.recurso r left join hosp.proc_recurso p on (r.id = p.id_recurso) " +
                "where p.id_proc = ? order by descrecurso";

        ArrayList<RecursoBean> lista = new ArrayList();

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stm = conexao.prepareStatement(sql);
            stm.setInt(1, id_proc);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                RecursoBean r = new RecursoBean();

                r.setIdRecurso(rs.getInt("id"));
                r.setDescRecurso(rs.getString("descrecurso"));

                lista.add(r);
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

    public RecursoBean buscaRecursoCodigo(Integer id) throws ProjetoException {
        PreparedStatement ps = null;
        conexao = ConnectionFactory.getConnection();

        try {

            String sql = "select id, descrecurso from hosp.recurso where id = ? order by descrecurso";

            ps = conexao.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            RecursoBean recurso = new RecursoBean();
            while (rs.next()) {

                recurso.setIdRecurso(rs.getInt("id"));
                recurso.setDescRecurso(rs.getString("descrecurso"));

            }
            return recurso;
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

    public List<RecursoBean> buscaRecursoAutoComplete(String s)
            throws ProjetoException {
        PreparedStatement ps = null;
        conexao = ConnectionFactory.getConnection();

        try {
            List<RecursoBean> listaRecursos = new ArrayList<RecursoBean>();
            String sql = "select id , descrecurso from hosp.recurso "
                    + " where upper(descrecurso) like ? order by descrecurso";

            ps = conexao.prepareStatement(sql);
            ps.setString(1, "%" + s.toUpperCase() + "%");
            ResultSet rs = ps.executeQuery();

            List<RecursoBean> colecao = new ArrayList<RecursoBean>();

            while (rs.next()) {

                RecursoBean recurso = new RecursoBean();
                recurso.setIdRecurso(rs.getInt("id"));
                recurso.setDescRecurso(rs.getString("descrecurso"));

                colecao.add(recurso);

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
