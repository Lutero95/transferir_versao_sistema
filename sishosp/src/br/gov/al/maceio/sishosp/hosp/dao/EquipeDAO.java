package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.EquipeBean;

public class EquipeDAO {

	Connection con = null;
	PreparedStatement ps = null;

	public boolean gravarEquipe(EquipeBean equipe)
			throws SQLException {

		String sql = "insert into hosp.equipe (descequipe) values (?);";
		try {
			System.out.println("VAI CADASTRAR EQUPE");
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, equipe.getDescEquipe().toUpperCase());
			ps.execute();
			con.commit();
			System.out.println("CADASTROU EQUIPE");
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

	public List<EquipeBean> listarEquipe() {
		List<EquipeBean> lista = new ArrayList<>();
		String sql = "select id_equipe, descequipe, codempresa from hosp.equipe";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				EquipeBean equipe = new EquipeBean();
				equipe.setCodEquipe(rs.getInt("id_equipe"));
				equipe.setDescEquipe(rs.getString("descequipe"));
				equipe.setCodEmpresa(rs.getInt("codempresa"));

				lista.add(equipe);
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

	public List<EquipeBean> listarEquipeBusca(String descricao,
			Integer tipo) {
		List<EquipeBean> lista = new ArrayList<>();
		System.out.println("2");
		String sql = "select id_equipe,id_equipe ||'-'|| descequipe as descequipe, codempresa from hosp.equipe ";
		if (tipo == 1) {
			sql += " where upper(id_equipe ||'-'|| descequipe) LIKE ?";
		}
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setString(1, "%" + descricao.toUpperCase() + "%");
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				EquipeBean equipe = new EquipeBean();
				equipe.setCodEquipe(rs.getInt("id_equipe"));
				equipe.setDescEquipe(rs.getString("descequipe"));
				equipe.setCodEmpresa(rs.getInt("codempresa"));

				lista.add(equipe);
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

	public boolean alterarEquipe(EquipeBean equipe) {
		String sql = "update hosp.equipe set descequipe = ? where id_equipe = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, equipe.getDescEquipe().toUpperCase());
			stmt.setInt(2, equipe.getCodEquipe());
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

	public boolean excluirEquipe(EquipeBean equipe) {
		String sql = "delete from hosp.equipe where id_equipe = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setLong(1, equipe.getCodEquipe());
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
	
	public EquipeBean buscarEquipePorID(Integer id) throws SQLException {
		EquipeBean equipe = null;
		
		String sql = "select id_equipe, descequipe, codempresa"
				+ " from hosp.equipe where id_equipe = ?";
		
		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				equipe = new EquipeBean();
				equipe.setCodEquipe(rs.getInt("id_equipe"));
				equipe.setDescEquipe(rs.getString("descequipe"));
				equipe.setCodEmpresa(0);//COD EMPRESA ?
			}
			
			return equipe;
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
}
