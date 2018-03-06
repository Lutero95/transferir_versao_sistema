package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.acl.model.UsuarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;

public class ProgramaDAO {

	Connection con = null;

	public boolean gravarPrograma(ProgramaBean prog) throws SQLException,
			ProjetoException {

		PreparedStatement ps = null;

		String sql = "insert into hosp.programa (descprograma, codfederal) values (?, ?) RETURNING id_programa;";
		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, prog.getDescPrograma().toUpperCase());
			ps.setDouble(2, prog.getCodFederal());
			ResultSet rs = ps.executeQuery();

			int idProg = 0;
			if (rs.next()) {
				idProg = rs.getInt("id_programa");
			}

			String sql2 = "insert into hosp.grupo_programa (codprograma, codgrupo) values(?,?);";
			PreparedStatement ps2 = con.prepareStatement(sql2);

			if (prog.getGrupo().size() > 0) {
				for (int i = 0; i < prog.getGrupo().size(); i++) {
					ps2.setInt(1, idProg);
					ps2.setInt(2, prog.getGrupo().get(i).getIdGrupo());

					ps2.execute();
				}
			}
			con.commit();

			return true;
		} catch (SQLException ex) {
			ex.printStackTrace();
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

	// SEM USO, MATEI ESSE MÉTODO POIS O CÓDIGO ESTÁ ERRADO
	public void insereProgramaGrupo(int idProg, ProgramaBean programa, int gamb)
			throws ProjetoException {
		PreparedStatement ps = null;

		String sql = "insert into hosp.grupo_programa (codprograma, codgrupo) values(?,?);";
		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			if (gamb == 0) {
				for (GrupoBean grupoBean : programa.getGrupo()) {
					ps.setInt(1, idProg);
					ps.setInt(2, grupoBean.getIdGrupo());

					ps.execute();
					con.commit();
				}
			} else if (gamb == 1) {
				for (GrupoBean grupoBean : programa.getGrupoNovo()) {
					ps.setInt(1, idProg);
					ps.setInt(2, grupoBean.getIdGrupo());

					ps.execute();
					con.commit();
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
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

	public List<ProgramaBean> listarProgramas() throws ProjetoException {
		List<ProgramaBean> lista = new ArrayList<>();
		String sql = "select id_programa, descprograma, codfederal "
				+ "from hosp.programa left join hosp.usuario_programa_grupo on programa.id_programa = usuario_programa_grupo.codprograma "
				// + "where codusuario = ? "
				+ "order by descprograma";
		UsuarioBean user_session = (UsuarioBean) FacesContext
				.getCurrentInstance().getExternalContext().getSessionMap()
				.get("obj_usuario");

		GrupoDAO gDao = new GrupoDAO();
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			System.out.println("user: " + user_session.getCodigo());
			// stm.setInt(1, user_session.getCodigo());
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				ProgramaBean programa = new ProgramaBean();
				programa.setIdPrograma(rs.getInt("id_programa"));
				programa.setDescPrograma(rs.getString("descprograma"));
				programa.setCodFederal(rs.getDouble("codfederal"));
				programa.setGrupo(gDao.listarGruposPorPrograma(rs
						.getInt("id_programa")));
				lista.add(programa);
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

	public ArrayList<ProgramaBean> BuscalistarProgramas()
			throws ProjetoException {
		PreparedStatement ps = null;
		con = ConnectionFactory.getConnection();
		UsuarioBean user_session = (UsuarioBean) FacesContext
				.getCurrentInstance().getExternalContext().getSessionMap()
				.get("obj_usuario");

		String sql = "select id_programa, descprograma, codfederal from hosp.programa "
				+ "join hosp.usuario_programa_grupo on programa.id_programa = usuario_programa_grupo.codprograma "
				// + "where codusuario = ? "
				+ "order by descprograma";
		GrupoDAO gDao = new GrupoDAO();
		ArrayList<ProgramaBean> lista = new ArrayList();
		try {
			ps = con.prepareStatement(sql);
			// ps.setInt(1, user_session.getCodigo());
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				ProgramaBean programa = new ProgramaBean();
				programa.setIdPrograma(rs.getInt("id_programa"));
				programa.setDescPrograma(rs.getString("descprograma"));
				programa.setCodFederal(rs.getDouble("codfederal"));
				programa.setGrupo(gDao.listarGruposPorPrograma(rs
						.getInt("id_programa")));
				lista.add(programa);
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

	public ArrayList<ProgramaBean> BuscalistarProgramasDefaut()
			throws ProjetoException {
		PreparedStatement ps = null;
		con = ConnectionFactory.getConnection();
		UsuarioBean user_session = (UsuarioBean) FacesContext
				.getCurrentInstance().getExternalContext().getSessionMap()
				.get("obj_usuario");
		String sql = "select id_programa, descprograma, codfederal from hosp.programa "
				+ "join hosp.usuario_programa_grupo on programa.id_programa = usuario_programa_grupo.codprograma "
				// + "where codusuario = ? "
				+ "order by descprograma";
		GrupoDAO gDao = new GrupoDAO();
		ArrayList<ProgramaBean> lista = new ArrayList();
		try {
			ps = con.prepareStatement(sql);
			// ps.setInt(1, user_session.getCodigo());
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				ProgramaBean programa = new ProgramaBean();
				programa.setIdPrograma(rs.getInt("id_programa"));
				programa.setDescPrograma(rs.getString("descprograma"));
				programa.setCodFederal(rs.getDouble("codfederal"));
				programa.setGrupo(gDao.listarGruposPorPrograma(rs
						.getInt("id_programa")));
				lista.add(programa);
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

	public List<ProgramaBean> listarProgramasBusca(String descricao,
			Integer tipo) throws ProjetoException {
		List<ProgramaBean> lista = new ArrayList<>();
		String sql = "select id_programa,id_programa ||'-'|| descprograma as descprograma , codfederal from hosp.programa "
				+ "join hosp.usuario_programa_grupo on programa.id_programa = usuario_programa_grupo.codprograma "
				+ "where codusuario = ?";

		if (tipo == 1) {
			sql += " and upper(id_programa ||'-'|| descprograma) LIKE ? order by descprograma";
		}
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			UsuarioBean user_session = (UsuarioBean) FacesContext
					.getCurrentInstance().getExternalContext().getSessionMap()
					.get("obj_usuario");
			System.out.println("codigo e " + user_session.getCodigo());
			stm.setInt(1, user_session.getCodigo());
			stm.setString(2, "%" + descricao.toUpperCase() + "%");

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				ProgramaBean programa = new ProgramaBean();
				programa.setIdPrograma(rs.getInt("id_programa"));
				programa.setDescPrograma(rs.getString("descprograma"));
				programa.setCodFederal(rs.getDouble("codfederal"));

				lista.add(programa);
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

	public Boolean excluirPrograma(ProgramaBean prog) throws ProjetoException {
		String sql = "delete from hosp.programa where id_programa = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setLong(1, prog.getIdPrograma());
			stmt.execute();
			
			String sql2 = "delete from hosp.grupo_programa where codprograma = ?";
			PreparedStatement stmt2 = con.prepareStatement(sql2);
			stmt2.setLong(1, prog.getIdPrograma());
			stmt2.execute();
			
			con.commit();
			//excluirNaTabProgramaGrupo(prog.getIdPrograma());
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

	// SEM USO, MATEI ESSE MÉTODO POR FAZER 2 COMMITS
	public void excluirNaTabProgramaGrupo(int cod) throws ProjetoException {
		String sql = "delete from hosp.grupo_programa where codprograma = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setLong(1, cod);
			stmt.execute();
			con.commit();
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

	public Boolean alterarPrograma(ProgramaBean prog) throws ProjetoException {
		String sql = "update hosp.programa set descprograma = ?, codfederal = ? where id_programa = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, prog.getDescPrograma().toUpperCase());
			stmt.setDouble(2, prog.getCodFederal());
			stmt.setInt(3, prog.getIdPrograma());
			stmt.executeUpdate();

			String sql2 = "delete from hosp.grupo_programa where codprograma = ?";
			PreparedStatement stmt2 = con.prepareStatement(sql2);
			stmt2.setLong(1, prog.getIdPrograma());
			stmt2.execute();

			String sql3 = "insert into hosp.grupo_programa (codprograma, codgrupo) values(?,?);";
			PreparedStatement stmt3 = con.prepareStatement(sql3);

			if (prog.getGrupo().size() > 0) {
				for (int i = 0; i < prog.getGrupo().size(); i++) {
					stmt3.setInt(1, prog.getIdPrograma());
					stmt3.setInt(2, prog.getGrupo().get(i).getIdGrupo());

					stmt3.execute();
				}
			}

			con.commit();

			return true;

		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	public ProgramaBean listarProgramaPorId(int id) throws ProjetoException {

		ProgramaBean programa = new ProgramaBean();
		GrupoDAO gDao = new GrupoDAO();
		String sql = "select id_programa, descprograma, codfederal from hosp.programa where id_programa = ? order by descprograma";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, id);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				programa = new ProgramaBean();
				programa.setIdPrograma(rs.getInt("id_programa"));
				programa.setDescPrograma(rs.getString("descprograma"));
				programa.setGrupo(gDao.listarGruposPorPrograma(rs
						.getInt("id_programa")));
				if (rs.getString("codfederal") != null)
					programa.setCodFederal(rs.getDouble("codfederal"));
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
		return programa;
	}
}
