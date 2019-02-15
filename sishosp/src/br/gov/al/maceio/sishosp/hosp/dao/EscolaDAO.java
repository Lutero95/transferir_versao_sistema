package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.EscolaBean;

public class EscolaDAO {
    private Connection conexao = null;

    public Boolean cadastrar(EscolaBean escola) {
        boolean retorno = false;

        String sql = "insert into hosp.escola (descescola) values (?)";

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, escola.getDescescola().toUpperCase().trim());

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

    public Boolean alterar(EscolaBean escola) {
        boolean retorno = false;
        String sql = "update hosp.escola set descescola = ? where id_escola = ?";
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, escola.getDescescola().toUpperCase());
            stmt.setInt(2, escola.getCodEscola());
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

    public Boolean excluir(EscolaBean escola) {
        boolean retorno = false;
        String sql = "delete from hosp.escola where id_escola = ?";
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, escola.getCodEscola());
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

    public ArrayList<EscolaBean> listaEscolas() throws ProjetoException {

        String sql = "select id_escola, descescola, codtipoescola from hosp.escola order by descescola";

        ArrayList<EscolaBean> lista = new ArrayList();

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stm = conexao.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                EscolaBean p = new EscolaBean();

                p.setCodEscola(rs.getInt("id_escola"));
                p.setDescescola(rs.getString("descescola"));
                p.setCodtipoescola(rs.getInt("codtipoescola"));

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

    public EscolaBean buscaescolacodigo(Integer i) throws ProjetoException {
        PreparedStatement ps = null;
        conexao = ConnectionFactory.getConnection();

        try {

            String sql = "select id_escola, descescola from hosp.escola where id_escola=? order by descescola";

            ps = conexao.prepareStatement(sql);
            ps.setInt(1, i);
            ResultSet rs = ps.executeQuery();

            EscolaBean escola = new EscolaBean();
            while (rs.next()) {

                escola.setCodEscola(rs.getInt("id_escola"));
                escola.setDescescola(rs.getString("descescola"));

            }
            return escola;
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

    public List<EscolaBean> buscaescola(String s) throws ProjetoException {
        PreparedStatement ps = null;
        conexao = ConnectionFactory.getConnection();

        try {
            List<EscolaBean> listaescolas = new ArrayList<EscolaBean>();
            String sql = "select id_escola,id_escola ||'-'|| descescola descescola from hosp.escola "
                    + " where upper(id_escola ||'-'|| descescola) like ? order by descescola";

            ps = conexao.prepareStatement(sql);
            ps.setString(1, "%" + s.toUpperCase() + "%");
            ResultSet rs = ps.executeQuery();

            List<EscolaBean> colecao = new ArrayList<EscolaBean>();

            while (rs.next()) {

                EscolaBean escola = new EscolaBean();
                escola.setCodEscola(rs.getInt("id_escola"));
                escola.setDescescola(rs.getString("descescola"));
                colecao.add(escola);

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

    public ArrayList<EscolaBean> listaTipoEscola() throws ProjetoException {

        String sql = "select * from hosp.tipoescola order by desctipoescola";

        ArrayList<EscolaBean> lista = new ArrayList();

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stm = conexao.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                EscolaBean p = new EscolaBean();

                p.setCodtipoescola(rs.getInt("codtipoescola"));
                p.setDesctipoescola(rs.getString("desctipoescola")
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
}
