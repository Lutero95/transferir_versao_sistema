package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.EquipamentoBean;

public class EquipamentoDAO {
    Connection con = null;
    PreparedStatement ps = null;

    public boolean gravarEquipamento(EquipamentoBean equip) {
        Boolean retorno = false;
        String sql = "insert into hosp.tipoaparelho (desctipoaparelho) values (?);";

        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, equip.getDescEquipamento().toUpperCase());
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

    public ArrayList<EquipamentoBean> listarEquipamentos() throws ProjetoException {
        ArrayList<EquipamentoBean> lista = new ArrayList();
        String sql = "select id, desctipoaparelho from hosp.tipoaparelho order by desctipoaparelho";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                EquipamentoBean e = new EquipamentoBean();
                e.setId_equipamento(rs.getInt("id"));
                e.setDescEquipamento(rs.getString("desctipoaparelho"));

                lista.add(e);
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
            return lista;
        }
    }

    public Boolean alterarEquipamento(EquipamentoBean equip) {
        Boolean retorno = false;
        String sql = "update hosp.tipoaparelho set desctipoaparelho = ? where id = ?";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, equip.getDescEquipamento().toUpperCase());
            stmt.setInt(2, equip.getId_equipamento());
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

    public Boolean excluirEquipamento(EquipamentoBean equip) {
        Boolean retorno = false;
        String sql = "delete from hosp.tipoaparelho where id = ?";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setLong(1, equip.getId_equipamento());
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

    public EquipamentoBean buscaEquipamentoPorId(Integer i) {

        String sql = "select id, desctipoaparelho from hosp.tipoaparelho where id =? order by desctipoaparelho";
        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, i);
            ResultSet rs = ps.executeQuery();
            EquipamentoBean equip = new EquipamentoBean();
            while (rs.next()) {
                equip.setId_equipamento(rs.getInt("id"));
                equip.setDescEquipamento(rs.getString("desctipoaparelho"));

            }
            return equip;
        } catch (SQLException | ProjetoException ex) {
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

    public List<EquipamentoBean> listarEquipamentoAutoComplete(String descricao) {
        List<EquipamentoBean> lista = new ArrayList<>();
        String sql = "select id, desctipoaparelho from hosp.tipoaparelho where desctipoaparelho like ? order by desctipoaparelho";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setString(1, "%" + descricao.toUpperCase() + "%");
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                EquipamentoBean equipamento = new EquipamentoBean();
                equipamento.setId_equipamento(rs.getInt("id"));
                equipamento.setDescEquipamento(rs.getString("desctipoaparelho"));

                lista.add(equipamento);
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
            return lista;
        }
    }

}
