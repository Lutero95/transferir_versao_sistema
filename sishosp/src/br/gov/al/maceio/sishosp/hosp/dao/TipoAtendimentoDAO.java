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
import br.gov.al.maceio.sishosp.hosp.model.TipoAtendimentoBean;

public class TipoAtendimentoDAO {

	Connection con = null;
	PreparedStatement ps = null;

	public boolean gravarTipoAt(TipoAtendimentoBean tipo) throws SQLException,
			ProjetoException {

		String sql = "insert into hosp.tipoatendimento (desctipoatendimento, "
				+ " primeiroatendimento, equipe_programa, codempresa, id) values (?, ?, ?, ?, DEFAULT) RETURNING id;";
		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, tipo.getDescTipoAt().toUpperCase());
			ps.setBoolean(2, tipo.isPrimeiroAt());
			ps.setBoolean(3, tipo.isEquipe());
			ps.setInt(4, 0);// cod empresa ?
			// ps.execute();

			ResultSet rs = ps.executeQuery();
			con.commit();
			int idTipo = 0;
			if (rs.next()) {
				idTipo = rs.getInt("id");
				insereTipoAtendimentoGrupo(idTipo, tipo.getGrupo());

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

	public void insereTipoAtendimentoGrupo(int idTipo,
			List<GrupoBean> listaGrupo) throws ProjetoException {
		String sql = "insert into hosp.tipoatendimento_grupo (codgrupo, codtipoatendimento) values(?,?);";
		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			for (GrupoBean grupoBean : listaGrupo) {
				ps.setInt(1, grupoBean.getIdGrupo());
				ps.setInt(2, idTipo);

				ps.execute();
				con.commit();
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

	public List<TipoAtendimentoBean> listarTipoAtPorGrupo(int codGrupo)
			throws ProjetoException {
		List<TipoAtendimentoBean> lista = new ArrayList<>();
		String sql = "select t.id, t.desctipoatendimento, t.primeiroatendimento, t.equipe_programa, t.codempresa"
				+ " from hosp.grupo g, hosp.tipoatendimento t, hosp.tipoatendimento_grupo tg"
				+ " where ? = tg.codgrupo and t.id = tg.codtipoatendimento"
				+ " group by t.id, t.desctipoatendimento, t.primeiroatendimento, t.equipe_programa, t.codempresa";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, codGrupo);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				TipoAtendimentoBean tipo = new TipoAtendimentoBean();
				tipo.setIdTipo(rs.getInt("id"));
				tipo.setDescTipoAt(rs.getString("desctipoatendimento"));
				tipo.setPrimeiroAt(rs.getBoolean("primeiroatendimento"));
				// tipo.setCodEmpresa(rs.getInt("codempresa"));//COD EMPRESA ?
				tipo.setEquipe(rs.getBoolean("equipe_programa"));

				lista.add(tipo);
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

	public List<TipoAtendimentoBean> listarTipoAt() throws ProjetoException {
		List<TipoAtendimentoBean> lista = new ArrayList<>();
		String sql = "select id, desctipoatendimento, primeiroatendimento, codempresa, equipe_programa"
				+ " from hosp.tipoatendimento order by desctipoatendimento";
		GrupoDAO gDao = new GrupoDAO();
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				TipoAtendimentoBean tipo = new TipoAtendimentoBean();
				tipo.setIdTipo(rs.getInt("id"));
				tipo.setDescTipoAt(rs.getString("desctipoatendimento"));
				tipo.setPrimeiroAt(rs.getBoolean("primeiroatendimento"));
				tipo.setCodEmpresa(rs.getInt("codempresa"));
				tipo.setEquipe(rs.getBoolean("equipe_programa"));
				tipo.setGrupo(gDao.listarGruposPorTipoAtend(rs.getInt("id")));

				lista.add(tipo);
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

	public GrupoBean buscarGrupo(int codGrupo) throws ProjetoException {
		String sql = "select descgrupo, qtdfrequencia, codempresa from hosp.grupo where id_grupo = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, codGrupo);
			ResultSet rs = stm.executeQuery();
			GrupoBean grupo = new GrupoBean();
			while (rs.next()) {
				grupo.setDescGrupo(rs.getString(1));
				grupo.setQtdFrequencia(rs.getInt(2));
			}
			return grupo;
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

	public List<TipoAtendimentoBean> listarTipoAtBusca(String descricao,
			Integer tipo) throws ProjetoException {
		List<TipoAtendimentoBean> lista = new ArrayList<>();
		String sql = "select id, desctipoatendimento, primeiroatendimento, codempresa, equipe_programa"
				+ " from hosp.tipoatendimento";
		if (tipo == 1) {
			sql += " where desctipoatendimento LIKE ?  order by desctipoatendimento";
		}
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setString(1, "%" + descricao.toUpperCase() + "%");
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				TipoAtendimentoBean tipo1 = new TipoAtendimentoBean();
				tipo1.setIdTipo(rs.getInt(1));
				tipo1.setDescTipoAt(rs.getString(2));
				tipo1.setPrimeiroAt(rs.getBoolean(3));
				tipo1.setCodEmpresa(rs.getInt(4));
				tipo1.setEquipe(rs.getBoolean(5));

				lista.add(tipo1);
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

	public List<TipoAtendimentoBean> listarTipoAtAutoComplete(String descricao,
			GrupoBean grupo) throws ProjetoException {
		List<TipoAtendimentoBean> lista = new ArrayList<>();
		String sql = "select t.id, t.id ||' - '|| t.desctipoatendimento as desctipoatendimento, t.primeiroatendimento, t.equipe_programa, t.codempresa "
				+ " from hosp.grupo g, hosp.tipoatendimento t, hosp.tipoatendimento_grupo tg "
				+ " where ? = tg.codgrupo and t.id = tg.codtipoatendimento "
				+ " and upper(t.id ||' - '|| t.desctipoatendimento) LIKE ? "
				+ " group by t.id, t.id ||' - '|| t.desctipoatendimento, t.primeiroatendimento, t.equipe_programa, t.codempresa "
				+ " order by t.desctipoatendimento ";

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, grupo.getIdGrupo());
			stm.setString(2, "%" + descricao.toUpperCase() + "%");
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				TipoAtendimentoBean tipo1 = new TipoAtendimentoBean();
				tipo1.setIdTipo(rs.getInt("id"));
				tipo1.setDescTipoAt(rs.getString("desctipoatendimento"));
				tipo1.setPrimeiroAt(rs.getBoolean("primeiroatendimento"));
				tipo1.setEquipe(rs.getBoolean("primeiroatendimento"));
				tipo1.setCodEmpresa(rs.getInt("codempresa"));

				lista.add(tipo1);
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

	public boolean alterarTipo(TipoAtendimentoBean tipo)
			throws ProjetoException {
		String sql = "update hosp.tipoatendimento set desctipoatendimento = ?, primeiroatendimento = ?, equipe_programa = ? where id = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, tipo.getDescTipoAt().toUpperCase());
			stmt.setBoolean(2, tipo.isPrimeiroAt());
			stmt.setBoolean(3, tipo.isEquipe());
			stmt.setInt(4, tipo.getIdTipo());
			stmt.executeUpdate();
			excluirTipoGrupo(tipo.getIdTipo());
			insereTipoAtendimentoGrupo(tipo.getIdTipo(), tipo.getGrupoNovo());
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

	public Boolean excluirTipo(TipoAtendimentoBean tipo)
			throws ProjetoException {
		String sql = "delete from hosp.tipoatendimento where id = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setLong(1, tipo.getIdTipo());
			stmt.execute();
			con.commit();
			excluirTipoGrupo(tipo.getIdTipo());
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

	public void excluirTipoGrupo(int idTipo) throws ProjetoException {
		String sql = "delete from hosp.tipoatendimento_grupo where codtipoatendimento = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setLong(1, idTipo);
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

	public TipoAtendimentoBean listarTipoPorId(int id) throws ProjetoException {
		String sql = "select id, desctipoatendimento, primeiroatendimento, codempresa, equipe_programa"
				+ " from hosp.tipoatendimento WHERE id = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, id);
			ResultSet rs = stm.executeQuery();
			GrupoDAO gDao = new GrupoDAO();
			TipoAtendimentoBean tipo = null;
			while (rs.next()) {
				tipo = new TipoAtendimentoBean();
				tipo.setIdTipo(rs.getInt("id"));
				tipo.setDescTipoAt(rs.getString("desctipoatendimento"));
				tipo.setPrimeiroAt(rs.getBoolean("primeiroatendimento"));
				tipo.setCodEmpresa(rs.getInt("codempresa"));
				tipo.setEquipe(rs.getBoolean("equipe_programa"));
				tipo.setGrupo(gDao.listarGruposPorTipoAtend(tipo.getIdTipo()));
			}
			return tipo;
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
