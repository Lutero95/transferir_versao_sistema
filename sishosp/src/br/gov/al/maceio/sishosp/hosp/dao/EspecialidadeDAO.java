package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.EspecialidadeBean;

public class EspecialidadeDAO {

    Connection con = null;
    PreparedStatement ps = null;

    public boolean gravarEspecialidade(EspecialidadeBean esp) {
        Boolean retorno = false;
        String sql = "insert into hosp.especialidade (descespecialidade) values (?);";
        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, esp.getDescEspecialidade().toUpperCase());
            ps.execute();
            con.commit();
            con.close();
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

    public boolean alterarEspecialidade(EspecialidadeBean espec) {
        Boolean retorno = false;
        String sql = "update hosp.especialidade set descespecialidade = ? where id_especialidade = ?";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, espec.getDescEspecialidade().toUpperCase());
            stmt.setInt(2, espec.getCodEspecialidade());
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

    public boolean excluirEspecialidade(EspecialidadeBean espec) {
        Boolean retorno = false;
        String sql = "delete from hosp.especialidade where id_especialidade = ?";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setLong(1, espec.getCodEspecialidade());
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

    public List<EspecialidadeBean> listarEspecialidades()
            throws ProjetoException {
        List<EspecialidadeBean> lista = new ArrayList<>();
        String sql = "select id_especialidade, descespecialidade, codempresa from hosp.especialidade order by descespecialidade";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                EspecialidadeBean esp = new EspecialidadeBean();
                esp.setCodEspecialidade(rs.getInt("id_especialidade"));
                esp.setDescEspecialidade(rs.getString("descespecialidade"));
                esp.setCodEmpresa(rs.getInt("codempresa"));

                lista.add(esp);
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

    public List<EspecialidadeBean> listarEspecialidadesBusca(String descricao,
                                                             Integer tipo) throws ProjetoException {
        List<EspecialidadeBean> lista = new ArrayList<>();
        String sql = "select id_especialidade, descespecialidade, codempresa from hosp.especialidade ";
        if (tipo == 1) {
            sql += " where descespecialidade LIKE ?";
        }
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setString(1, "%" + descricao.toUpperCase() + "%");
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                EspecialidadeBean esp = new EspecialidadeBean();
                esp.setCodEspecialidade(rs.getInt("id_especialidade"));
                esp.setDescEspecialidade(rs.getString("descespecialidade"));
                esp.setCodEmpresa(rs.getInt("codempresa"));

                lista.add(esp);
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

    public EspecialidadeBean listarEspecialidadePorId(int id)
            throws ProjetoException {

        EspecialidadeBean esp = new EspecialidadeBean();
        String sql = "select id_especialidade, descespecialidade, codempresa from hosp.especialidade where id_especialidade = ?";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                esp = new EspecialidadeBean();
                esp.setCodEspecialidade(rs.getInt("id_especialidade"));
                esp.setDescEspecialidade(rs.getString("descespecialidade"));
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
        return esp;
    }

}
