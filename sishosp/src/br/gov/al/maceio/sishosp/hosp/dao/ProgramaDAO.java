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

public class ProgramaDAO {

	Connection con = null;
	PreparedStatement ps = null;

	public boolean gravarPrograma(ProgramaBean prog) throws SQLException {

		String sql = "insert into hosp.programa (descprograma, codfederal) values (?, ?) RETURNING id_programa;";
		try {
			System.out.println("VAI CADASTRAR PROG");
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, prog.getDescPrograma().toUpperCase());
			ps.setDouble(2, prog.getCodFederal());
			ResultSet rs = ps.executeQuery();
			con.commit();
			int idProg = 0;
			if (rs.next()) {
				idProg = rs.getInt("id_programa");
				System.out.println("retorno " + idProg);
				insereProgramaGrupo(idProg, prog);

			}
			System.out.println("CADASTROU PROG");
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

	public void insereProgramaGrupo(int idProg, ProgramaBean programa) {
		String sql = "insert into hosp.grupo_programa (codprograma, codgrupo) values(?,?);";
		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			for (GrupoBean grupoBean : programa.getGrupo()) {
				ps.setInt(1, idProg);
				ps.setInt(2, grupoBean.getIdGrupo());

				ps.execute();
				con.commit();
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

	public List<ProgramaBean> listarProgramas() {
		List<ProgramaBean> lista = new ArrayList<>();
		String sql = "select id_programa, descprograma, codfederal from hosp.programa";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
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

	public List<ProgramaBean> listarProgramasBusca(String descricao,
			Integer tipo) {
		List<ProgramaBean> lista = new ArrayList<>();
		System.out.println("2");
		String sql = "select id_programa, descprograma, codfederal from hosp.programa ";
		if (tipo == 1) {
			sql += " where descprograma LIKE ?";
		}
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setString(1, "%" + descricao.toUpperCase() + "%");
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
		System.out.println("3");
		for (ProgramaBean programaBean : lista) {
			System.out.println(programaBean.getDescPrograma());
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

	public Boolean alterarPrograma(ProgramaBean prog) throws ProjetoException {
		String sql = "update hosp.programa set descprograma = ?, codfederal = ? where id_programa = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, prog.getDescPrograma().toUpperCase());
			stmt.setDouble(2, prog.getCodFederal());
			stmt.setInt(3, prog.getIdPrograma());
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

	public ProgramaBean listarProgramaPorId(int id) {

		ProgramaBean programa = new ProgramaBean();
		String sql = "select id_programa, descprograma, codfederal from hosp.programa where id_programa = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, id);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				programa = new ProgramaBean();
				programa.setIdPrograma(rs.getInt("id_programa"));
				programa.setDescPrograma(rs.getString("descprograma"));
				// programa.setCodFederal(rs.getDouble("codfederal"));
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
