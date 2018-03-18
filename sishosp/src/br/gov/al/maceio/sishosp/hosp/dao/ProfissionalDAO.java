package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sun.xml.internal.ws.wsdl.writer.document.Types;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProfissionalBean;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;

public class ProfissionalDAO {

	private Connection con = null;
	private PreparedStatement ps = null;
	private EspecialidadeDAO espDao = new EspecialidadeDAO();
	private ProgramaDAO progDao = new ProgramaDAO();
	private CboDAO cDao = new CboDAO();
	private GrupoDAO gDao = new GrupoDAO();
	private ProcedimentoDAO procDao = new ProcedimentoDAO();

	public boolean gravarProfissional(ProfissionalBean prof)
			throws SQLException, ProjetoException {

		String sql = "insert into hosp.medicos (descmedico, codespecialidade, cns, ativo, codcbo,"
				+ " codprocedimentopadrao, codprocedimentopadrao2, codempresa) values (?, ?, ?, ?, ?, ?, ?, ?) returning id_medico;";
		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, prof.getDescricaoProf().toUpperCase());

			if (prof.getEspecialidade() != null) {
				ps.setInt(2, prof.getEspecialidade().getCodEspecialidade());
			} else {
				ps.setInt(2, 0);
			}

			ps.setString(3, prof.getCns().toUpperCase());
			ps.setBoolean(4, prof.isAtivo());

			if (prof.getCbo() != null) {
				ps.setInt(5, prof.getCbo().getCodCbo());
			} else {
				ps.setInt(5, 0);
			}

			if (prof.getProc1() != null) {
				ps.setInt(6, prof.getProc1().getCodProc());
			} else {
				ps.setInt(6, 0);
			}

			if (prof.getProc2() != null) {
				ps.setInt(7, prof.getProc2().getCodProc());
			} else {
				ps.setInt(7, 0);
			}

			ps.setInt(8, 0);// COD EMPRESA ??

			ResultSet rs = ps.executeQuery();
			con.commit();

			int idProf = 0;
			if (rs.next()) {
				idProf = rs.getInt("id_medico");
				insereTabProfProg(prof, idProf, 0);
				insereTabProfGrupo(prof, idProf, 0);
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

	public void insereTabProfProg(ProfissionalBean prof, int idProf, int gamb)
			throws SQLException, ProjetoException {
		String sql = "insert into hosp.profissional_programa (codprograma, codprofissional)"
				+ " values (?, ?);";
		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);

			if (gamb == 0) {
				for (ProgramaBean p : prof.getPrograma()) {
					ps.setInt(1, p.getIdPrograma());
					ps.setInt(2, idProf);

					ps.execute();
					con.commit();
				}
			} else if (gamb == 1) {
				for (ProgramaBean p : prof.getProgramaNovo()) {
					ps.setInt(1, p.getIdPrograma());
					ps.setInt(2, idProf);

					ps.execute();
					con.commit();
				}
			}
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				// con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				System.exit(1);
			}
		}
	}

	public void insereTabProfGrupo(ProfissionalBean prof, int idProf, int gamb)
			throws SQLException, ProjetoException {
		String sql = "insert into hosp.profissional_grupo (codgrupo, codprofissional)"
				+ " values (?, ?);";
		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);

			if (gamb == 0) {
				for (GrupoBean g : prof.getGrupo()) {
					ps.setInt(1, g.getIdGrupo());
					ps.setInt(2, idProf);

					ps.execute();
					con.commit();
				}
			} else if (gamb == 1) {
				for (GrupoBean g : prof.getGrupoNovo()) {
					ps.setInt(1, g.getIdGrupo());
					ps.setInt(2, idProf);

					ps.execute();
					con.commit();
				}
			}
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				// con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				System.exit(1);
			}
		}
	}

	public List<ProfissionalBean> listarProfissionalBusca(
			String descricaoBusca, Integer tipoBuscar) throws ProjetoException {
		System.out.println("OK2");
		List<ProfissionalBean> lista = new ArrayList<>();
		String sql = "select id_medico,id_medico ||'-'|| descmedico as descmedico, codespecialidade, cns, ativo, codcbo, codprocedimentopadrao, codprocedimentopadrao2, codempresa"
				+ " from hosp.medicos ";
		if (tipoBuscar == 1) {
			sql += " where upper(id_medico ||' - '|| descmedico) LIKE ?  order by descmedico";
		}
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setString(1, "%" + descricaoBusca.toUpperCase() + "%");
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				ProfissionalBean prof = new ProfissionalBean();
				prof.setIdProfissional(rs.getInt("id_medico"));
				prof.setDescricaoProf(rs.getString("descmedico"));
				prof.setEspecialidade(espDao.listarEspecialidadePorId(rs
						.getInt("codespecialidade")));
				prof.setCns(rs.getString("cns"));
				prof.setAtivo(rs.getBoolean("ativo"));
				prof.setCbo(cDao.listarCboPorId(rs.getInt("codcbo")));
				prof.setProc1(procDao.listarProcedimentoPorId(rs
						.getInt("codprocedimentopadrao")));
				prof.setProc2(procDao.listarProcedimentoPorId(rs
						.getInt("codprocedimentopadrao2")));
				prof.setPrograma(listarProgProf(rs.getInt("id_medico")));
				prof.setGrupo(listarProgGrupo(rs.getInt("id_medico")));

				lista.add(prof);
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

	public List<ProfissionalBean> listarProfissional() throws ProjetoException {
		List<ProfissionalBean> listaProf = new ArrayList<ProfissionalBean>();
		String sql = "select distinct id_medico, descmedico, codespecialidade, cns, ativo, codcbo, codprocedimentopadrao, codprocedimentopadrao2, codempresa"
				+ " from hosp.medicos order by descmedico";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				ProfissionalBean prof = new ProfissionalBean();
				prof.setIdProfissional(rs.getInt("id_medico"));
				prof.setDescricaoProf(rs.getString("descmedico"));
				prof.setEspecialidade(espDao.listarEspecialidadePorId(rs
						.getInt("codespecialidade")));
				prof.setCns(rs.getString("cns"));
				prof.setAtivo(rs.getBoolean("ativo"));
				prof.setCbo(cDao.listarCboPorId(rs.getInt("codcbo")));
				prof.setProc1(procDao.listarProcedimentoPorId(rs
						.getInt("codprocedimentopadrao")));
				prof.setProc2(procDao.listarProcedimentoPorId(rs
						.getInt("codprocedimentopadrao2")));

				listaProf.add(prof);
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

		return listaProf;
	}

	public boolean excluirProfissional(ProfissionalBean profissional)
			throws ProjetoException {
		String sql = "delete from hosp.medicos where id_medico = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setLong(1, profissional.getIdProfissional());
			stmt.execute();
			con.commit();
			excluirTabProfProg(profissional);
			excluirTabProfGrupo(profissional);
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

	public void excluirTabProfProg(ProfissionalBean profissional)
			throws ProjetoException {
		String sql = "delete from hosp.profissional_programa where codprofissional = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setLong(1, profissional.getIdProfissional());
			stmt.execute();
			con.commit();
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				// con.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	public void excluirTabProfGrupo(ProfissionalBean profissional)
			throws ProjetoException {
		String sql = "delete from hosp.profissional_grupo where codprofissional = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setLong(1, profissional.getIdProfissional());
			stmt.execute();
			con.commit();
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				// con.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	public boolean alterarProfissional(ProfissionalBean profissional)
			throws ProjetoException {

		String sql = "update hosp.medicos set descmedico = ?, codespecialidade = ?, cns = ?, ativo = ?,"
				+ " codcbo = ?, codprocedimentopadrao = ?, codprocedimentopadrao2 = ?, codempresa = ? "
				+ " where id_medico = ?";

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, profissional.getDescricaoProf().toUpperCase());
			stmt.setInt(2, profissional.getEspecialidade()
					.getCodEspecialidade());
			stmt.setString(3, profissional.getCns().toUpperCase());
			stmt.setBoolean(4, profissional.isAtivo());
			if (profissional.getCbo() != null) {
				stmt.setInt(5, profissional.getCbo().getCodCbo());
			} else {
				stmt.setInt(5, 0);
			}
			if (profissional.getProc1() != null) {
				stmt.setInt(6, profissional.getProc1().getCodProc());
			} else {
				stmt.setInt(6, 0);
			}
			if (profissional.getProc2() != null) {
				stmt.setInt(7, profissional.getProc2().getCodProc());
			} else {
				stmt.setInt(7, 0);
			}
			stmt.setInt(8, 0);// COD EMPRESA ? */
			stmt.setInt(9, profissional.getIdProfissional());
			stmt.executeUpdate();
			con.commit();
			excluirTabProfProg(profissional);
			insereTabProfProg(profissional, profissional.getIdProfissional(), 1);
			excluirTabProfGrupo(profissional);
			insereTabProfGrupo(profissional, profissional.getIdProfissional(),
					1);
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

	public ProfissionalBean buscarProfissionalPorId(Integer id)
			throws SQLException, ProjetoException {
		ProfissionalBean prof = null;

		String sql = "select id_medico, descmedico, codespecialidade, "
				+ " cns, ativo, codcbo, codprocedimentopadrao, codprocedimentopadrao2, codempresa"
				+ " from hosp.medicos where id_medico = ? order by descmedico";

		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				prof = new ProfissionalBean();
				prof.setIdProfissional(rs.getInt("id_medico"));
				prof.setDescricaoProf(rs.getString("descmedico"));
				prof.setPrograma(listarProgProf(rs.getInt("id_medico")));
				prof.setGrupo(listarProgGrupo(rs.getInt("id_medico")));
				prof.setEspecialidade(espDao.listarEspecialidadePorId(rs
						.getInt("codespecialidade")));
				prof.setCns(rs.getString("cns"));
				prof.setAtivo(rs.getBoolean("ativo"));
				prof.setCbo(cDao.listarCboPorId(rs.getInt("codcbo")));
				prof.setProc1(procDao.listarProcedimentoPorId(rs
						.getInt("codprocedimentopadrao")));
				prof.setProc2(procDao.listarProcedimentoPorId(rs
						.getInt("codprocedimentopadrao2")));
				// prof.setIdProfissional(rs.getInt("codempresa"));// COD
				// EMPRESA ??
			}

			return prof;
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

	public List<ProfissionalBean> listarProfissionaisPorEquipe(int id)
			throws ProjetoException {

		List<ProfissionalBean> lista = new ArrayList<ProfissionalBean>();
		String sql = "select medico from hosp.equipe_medico where equipe = ? order by medico";
		ProfissionalDAO pDao = new ProfissionalDAO();
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, id);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				lista.add(pDao.buscarProfissionalPorId(rs.getInt("medico")));
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

	public List<ProgramaBean> listarProgProf(int idProf)
			throws ProjetoException {
		List<ProgramaBean> lista = new ArrayList<ProgramaBean>();
		String sql = "select codprograma from hosp.profissional_programa where codprofissional = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, idProf);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				lista.add(progDao.listarProgramaPorId(rs.getInt("codprograma")));
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

	public List<GrupoBean> listarProgGrupo(int idProf) throws ProjetoException {
		List<GrupoBean> lista = new ArrayList<GrupoBean>();
		String sql = "select codgrupo from hosp.profissional_grupo where codprofissional = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, idProf);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				lista.add(gDao.listarGrupoPorId(rs.getInt("codgrupo")));
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

	public List<ProfissionalBean> listarProfissionalPorGrupo(int idGrupo)
			throws ProjetoException {
		List<ProfissionalBean> lista = new ArrayList<ProfissionalBean>();
		String sql = "select codprofissional from hosp.profissional_grupo where codgrupo = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, idGrupo);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				lista.add(buscarProfissionalPorId(rs.getInt("codprofissional")));
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
