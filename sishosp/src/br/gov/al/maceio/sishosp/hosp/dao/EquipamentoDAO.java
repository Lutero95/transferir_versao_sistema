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
import br.gov.al.maceio.sishosp.hosp.model.EquipeBean;

public class EquipamentoDAO {
	private Connection conexao = null;
	Connection con = null;
	PreparedStatement ps = null;

	public boolean gravarEquipamento(EquipamentoBean equip)
			throws SQLException, ProjetoException {

		String sql = "insert into hosp.tipoaparelho (desctipoaparelho) values (?);";
		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, equip.getDescEquipamento().toUpperCase());
			ps.execute();
			con.commit();
			return true;
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				System.exit(1);
			}
		}
	}

	public ArrayList<EquipamentoBean> listarEquipamentos() throws SQLException,
			ProjetoException {
		String sql = "select id, desctipoaparelho from hosp.tipoaparelho order by desctipoaparelho";

		ArrayList<EquipamentoBean> lista = new ArrayList();

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stm = conexao.prepareStatement(sql);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				EquipamentoBean e = new EquipamentoBean();
				e.setId_equipamento(rs.getInt("id"));
				e.setDescEquipamento(rs.getString("desctipoaparelho"));

				lista.add(e);
			}
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				conexao.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				System.exit(1);
			}
		}
		return lista;
	}

	public List<EquipamentoBean> listarEquipamentoBusca(String descricao,
			Integer tipo) throws ProjetoException {
		List<EquipamentoBean> lista = new ArrayList<>();
		String sql = "select id, desctipoaparelho from hosp.tipoaparelho ";
		if (tipo == 1) {
			sql += " where desctipoaparelho LIKE ?  order by desctipoaparelho";
		}
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setString(1, "%" + descricao.toUpperCase() + "%");
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				EquipamentoBean equip = new EquipamentoBean();
				equip.setId_equipamento(rs.getInt("id"));
				equip.setDescEquipamento(rs.getString("desctipoaparelho"));

				lista.add(equip);
			}
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				System.exit(1);
			}
		}

		return lista;
	}

	public Boolean alterarEquipamento(EquipamentoBean equip)
			throws ProjetoException {
		String sql = "update hosp.tipoaparelho set desctipoaparelho = ? where id = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, equip.getDescEquipamento().toUpperCase());
			stmt.setInt(2, equip.getId_equipamento());
			stmt.executeUpdate();
			con.commit();
			return true;
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	public Boolean excluirEquipamento(EquipamentoBean equip)
			throws ProjetoException {
		String sql = "delete from hosp.tipoaparelho where id = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setLong(1, equip.getId_equipamento());
			stmt.execute();
			con.commit();
			return true;
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	public EquipamentoBean buscaEquipamentoPorId(Integer i)
			throws ProjetoException {
		String sql = "select id, desctipoaparelho from hosp.tipoaparelho where id =? order by desctipoaparelho";
		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, i);
			ResultSet rs = ps.executeQuery();
			EquipamentoBean g = new EquipamentoBean();
			while (rs.next()) {
				g.setId_equipamento(rs.getInt("id"));
				g.setDescEquipamento(rs.getString("desctipoaparelho"));

			}

			return g;
		} catch (Exception sqle) {
			sqle.printStackTrace();
			throw new ProjetoException(sqle);

		} finally {
			try {
				con.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
				System.exit(1);
				// TODO: handle exception
			}

		}
	}
	
	public List<EquipamentoBean> listarEquipamentoAutoComplete(String descricao)
			throws ProjetoException {
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
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				System.exit(1);
			}
		}
		return lista;
	}

}
