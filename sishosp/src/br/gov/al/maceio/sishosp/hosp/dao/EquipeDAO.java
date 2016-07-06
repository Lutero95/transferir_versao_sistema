package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.EquipeBean;
import br.gov.al.maceio.sishosp.hosp.model.ProfissionalBean;

public class EquipeDAO {

	Connection con = null;
	PreparedStatement ps = null;
	ProfissionalDAO pDao = new ProfissionalDAO();
	public boolean gravarEquipe(EquipeBean equipe)
			throws SQLException {

		String sql = "insert into hosp.equipe (descequipe) values (?) RETURNING id_equipe;";
		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, equipe.getDescEquipe().toUpperCase());
			ResultSet rs = ps.executeQuery();
			con.commit();
			int idEquipe = 0;
			if (rs.next()) {
				idEquipe = rs.getInt("id_equipe");
				insereEquipeProfissional(idEquipe, equipe, 0);

			}
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
	
	public void insereEquipeProfissional(int idEquipe, EquipeBean equipe, int gamb) {
		String sql = "insert into hosp.equipe_medico (equipe, medico) values(?,?);";
		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			if(gamb == 0){
				for (ProfissionalBean prof : equipe.getProfissionais()) {
					ps.setInt(1, idEquipe);
					ps.setInt(2, prof.getIdProfissional());
					ps.execute();
					con.commit();
				}
			}else if(gamb == 1){
				for (ProfissionalBean prof : equipe.getProfissionaisNovo()) {
					ps.setInt(1, idEquipe);
					ps.setInt(2, prof.getIdProfissional());
					ps.execute();
					con.commit();
				}
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
	}

	public List<EquipeBean> listarEquipe() {
		List<EquipeBean> lista = new ArrayList<>();
		String sql = "select id_equipe, descequipe, codempresa from hosp.equipe order by id_equipe";
		
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				EquipeBean equipe = new EquipeBean();
				equipe.setCodEquipe(rs.getInt("id_equipe"));
				equipe.setDescEquipe(rs.getString("descequipe"));
				equipe.setCodEmpresa(rs.getInt("codempresa"));
				equipe.setProfissionais(pDao.listarProfissionaisPorEquipe(rs.getInt("id_equipe")));
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
		String sql = "select id_equipe,id_equipe ||'-'|| descequipe as descequipe, codempresa from hosp.equipe ";
		if (tipo == 1) {
			sql += " where upper(id_equipe ||'-'|| descequipe) LIKE ? order by id_equipe";
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
				equipe.setProfissionais(pDao.listarProfissionaisPorEquipe(rs.getInt("id_equipe")));
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
			excluirTabEquipeProf(equipe.getCodEquipe());
			insereEquipeProfissional(equipe.getCodEquipe(), equipe, 1);
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
			excluirTabEquipeProf(equipe.getCodEquipe());
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
	
	public void excluirTabEquipeProf(int id){
		String sql = "delete from hosp.equipe_medico where equipe = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setLong(1, id);
			stmt.execute();
			con.commit();
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				//con.close();
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
