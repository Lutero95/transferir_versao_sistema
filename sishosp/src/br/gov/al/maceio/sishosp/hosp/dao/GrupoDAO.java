package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;

public class GrupoDAO {

	Connection con = null;
	PreparedStatement ps = null;

	public boolean gravarGrupo(GrupoBean grupo) throws SQLException {

		String sql = "insert into hosp.grupo (descgrupo, qtdfrequencia, auditivo) values (?, ?, ?);";
		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, grupo.getDescGrupo().toUpperCase());
			ps.setInt(2, grupo.getQtdFrequencia());
			ps.setBoolean(3, grupo.isAuditivo());
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

	public List<GrupoBean> listarGruposPorPrograma(int codPrograma) {
		List<GrupoBean> lista = new ArrayList<>();
		String sql = "select g.id_grupo, g.descgrupo, g.qtdfrequencia, g.auditivo, g.equipe from hosp.grupo g, hosp.grupo_programa gp, hosp.programa p"
				+ " where p.id_programa = ? and g.id_grupo = gp.codgrupo and p.id_programa = gp.codprograma order by g.id_grupo";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, codPrograma);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				GrupoBean grupo = new GrupoBean();
				grupo.setIdGrupo(rs.getInt("id_grupo"));
				grupo.setDescGrupo(rs.getString("descgrupo"));
				grupo.setQtdFrequencia(rs.getInt("qtdfrequencia"));
				grupo.setAuditivo(rs.getBoolean("auditivo"));
                grupo.setEquipeThulio(rs.getBoolean("equipe"));
				lista.add(grupo);
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
	
	public List<GrupoBean> listarGruposPorTipoAtend(int idTipo) {
		List<GrupoBean> lista = new ArrayList<>();
		String sql = "select g.id_grupo, g.descgrupo, g.qtdfrequencia, g.auditivo from hosp.grupo g, "
				+ " hosp.tipoatendimento_grupo tg, hosp.tipoatendimento t"
				+ " where t.id = ? and g.id_grupo = tg.codgrupo "
				+ " and t.id = tg.codtipoatendimento order by g.id_grupo";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, idTipo);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				GrupoBean grupo = new GrupoBean();
				grupo.setIdGrupo(rs.getInt("id_grupo"));
				grupo.setDescGrupo(rs.getString("descgrupo"));
				grupo.setQtdFrequencia(rs.getInt("qtdfrequencia"));
				grupo.setAuditivo(rs.getBoolean("auditivo"));

				lista.add(grupo);
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

	public List<GrupoBean> listarGrupos() {
		List<GrupoBean> lista = new ArrayList<>();
		String sql = "select id_grupo, descgrupo, qtdfrequencia, auditivo from hosp.grupo order by id_grupo";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				GrupoBean grupo = new GrupoBean();
				grupo.setIdGrupo(rs.getInt("id_grupo"));
				grupo.setDescGrupo(rs.getString("descgrupo"));
				grupo.setQtdFrequencia(rs.getInt("qtdfrequencia"));
				grupo.setAuditivo(rs.getBoolean("auditivo"));

				lista.add(grupo);
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

	public List<GrupoBean> listarGruposBusca(String descricao, Integer tipo) {
		List<GrupoBean> lista = new ArrayList<>();
		String sql = "select id_grupo, descgrupo, qtdfrequencia, auditivo from hosp.grupo ";
		if (tipo == 1) {
			sql += " where descgrupo LIKE ?  order by id_grupo";
		}
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setString(1, "%" + descricao.toUpperCase() + "%");
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				GrupoBean grupo = new GrupoBean();
				grupo.setIdGrupo(rs.getInt("id_grupo"));
				grupo.setDescGrupo(rs.getString("descgrupo"));
				grupo.setQtdFrequencia(rs.getInt("qtdfrequencia"));
				grupo.setAuditivo(rs.getBoolean("auditivo"));

				lista.add(grupo);
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

	public List<GrupoBean> listarGruposAutoComplete(String descricao,
			ProgramaBean prog) {
		List<GrupoBean> lista = new ArrayList<>();
<<<<<<< HEAD
		String sql = "select g.id_grupo, g.id_grupo ||'-'|| g.descgrupo as descgrupo , g.qtdfrequencia, g.auditivo from hosp.grupo g, hosp.grupo_programa gp, hosp.programa p"
=======
		System.out.println("De " + descricao);
		System.out.println("Pr " + prog.getDescPrograma());
		String sql = "select g.id_grupo, g.id_grupo ||'-'|| g.descgrupo as descgrupo , g.qtdfrequencia, g.auditivo, g.equipe from hosp.grupo g, hosp.grupo_programa gp, hosp.programa p"
>>>>>>> 5fba252056e73857aa5481379f2f22b5dfad1f85
				+ " where p.id_programa = ? and g.id_grupo = gp.codgrupo and p.id_programa = gp.codprograma"
				+ " and upper(g.id_grupo ||'-'|| g.descgrupo) LIKE ?  order by id_grupo";

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, prog.getIdPrograma());
			stm.setString(2, "%" + descricao.toUpperCase() + "%");
			ResultSet rs = stm.executeQuery();
		

			while (rs.next()) {
				GrupoBean grupo = new GrupoBean();
				grupo.setIdGrupo(rs.getInt("id_grupo"));
				grupo.setDescGrupo(rs.getString("descgrupo"));
				grupo.setQtdFrequencia(rs.getInt("qtdfrequencia"));
				grupo.setAuditivo(rs.getBoolean("auditivo"));
				grupo.setEquipeThulio(rs.getBoolean("equipe"));
				System.out.println("THULIO:"+grupo.isEquipeThulio() + " - " + grupo.getDescGrupo());
				lista.add(grupo);
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

	public Boolean alterarGrupo(GrupoBean grupo) throws ProjetoException {
		String sql = "update hosp.grupo set descgrupo = ?, qtdfrequencia = ?, auditivo = ? where id_grupo = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, grupo.getDescGrupo().toUpperCase());
			stmt.setDouble(2, grupo.getQtdFrequencia());
			stmt.setBoolean(3, grupo.isAuditivo());
			stmt.setInt(4, grupo.getIdGrupo());
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

	public Boolean excluirGrupo(GrupoBean grupo) throws ProjetoException {
		String sql = "delete from hosp.grupo where id_grupo = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setLong(1, grupo.getIdGrupo());
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

	

	public GrupoBean listarGrupoPorId(int id) {

		GrupoBean grupo = new GrupoBean();
		String sql = "select id_grupo, descgrupo, qtdfrequencia, auditivo from hosp.grupo where id_grupo = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, id);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				grupo.setIdGrupo(rs.getInt("id_grupo"));
				grupo.setDescGrupo(rs.getString("descgrupo"));
				grupo.setQtdFrequencia(rs.getInt("qtdfrequencia"));
				grupo.setAuditivo(rs.getBoolean("auditivo"));

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
		return grupo;
	}

	public List<GrupoBean> listarGruposAutoComplete2(String descricao) {
		List<GrupoBean> lista = new ArrayList<>();

		String sql = "select id_grupo, descgrupo, qtdfrequencia, auditivo from hosp.grupo  "
				+ "where descgrupo LIKE ?  order by id_grupo";

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setString(1, "%" + descricao.toUpperCase() + "%");
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				GrupoBean grupo = new GrupoBean();
				grupo.setIdGrupo(rs.getInt("id_grupo"));
				grupo.setDescGrupo(rs.getString("descgrupo"));
				grupo.setQtdFrequencia(rs.getInt("qtdfrequencia"));
				grupo.setAuditivo(rs.getBoolean("auditivo"));

				lista.add(grupo);
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
