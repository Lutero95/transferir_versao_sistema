package br.gov.al.maceio.sishosp.hosp.dao;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.Parentesco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ParentescoDAO {
    private Connection conexao = null;

    public Boolean cadastrar(Parentesco parentesco) {
        boolean retorno = false;

        String sql = "insert into hosp.parentesco (descparentesco) values (?)";

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, parentesco.getDescParentesco().toUpperCase().trim());

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

    public Boolean alterar(Parentesco parentesco) {
        boolean retorno = false;
        String sql = "update hosp.parentesco set descparentesco = ? where id_parentesco = ?";
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, parentesco.getDescParentesco().toUpperCase());
            stmt.setInt(2, parentesco.getCodParentesco());
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

    public Boolean excluir(Parentesco parentesco) {
        boolean retorno = false;
        String sql = "delete from hosp.parentesco where id_parentesco = ?";
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, parentesco.getCodParentesco());
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

    public ArrayList<Parentesco> listaParentescos() throws ProjetoException {

        String sql = "select id_parentesco, descparentesco from hosp.parentesco order by descparentesco";

        ArrayList<Parentesco> lista = new ArrayList();

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stm = conexao.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                Parentesco p = new Parentesco();

                p.setCodParentesco(rs.getInt("id_parentesco"));
                p.setDescParentesco(rs.getString("descparentesco"));

                lista.add(p);
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

    public Parentesco buscaParentesCocodigo(Integer cod) throws ProjetoException {
        PreparedStatement ps = null;
        conexao = ConnectionFactory.getConnection();

        try {

            String sql = "select id_parentesco, descparentesco, coalesce(tipo,'O') tipoparentesco from hosp.parentesco where id_parentesco=? order by descparentesco";

            ps = conexao.prepareStatement(sql);
            ps.setInt(1, cod);
            ResultSet rs = ps.executeQuery();

            Parentesco parentesco = new Parentesco();
            while (rs.next()) {

                parentesco.setCodParentesco(rs.getInt("id_parentesco"));
                parentesco.setDescParentesco(rs.getString("descparentesco"));
                parentesco.setTipoParentesco(rs.getString("tipoparentesco"));

            }
            return parentesco;
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

    public List<Parentesco> buscaParentesco(String s) throws ProjetoException {
        PreparedStatement ps = null;
        conexao = ConnectionFactory.getConnection();

        try {
            List<Parentesco> listaParentescos = new ArrayList<Parentesco>();

            String sql = "select id_parentesco, descparentesco from hosp.parentesco "
                    + " where upper(descparentesco) like ? order by descparentesco";

            ps = conexao.prepareStatement(sql);
            ps.setString(1, "%" + s.toUpperCase() + "%");
            ResultSet rs = ps.executeQuery();

            List<Parentesco> colecao = new ArrayList<Parentesco>();

            while (rs.next()) {

                Parentesco parentesco = new Parentesco();
                parentesco.setCodParentesco(rs.getInt("id_parentesco"));
                parentesco.setDescParentesco(rs.getString("descparentesco"));
                colecao.add(parentesco);

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
