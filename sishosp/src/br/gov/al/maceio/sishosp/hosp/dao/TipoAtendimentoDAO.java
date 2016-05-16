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

	public boolean gravarTipoAt(TipoAtendimentoBean tipo) throws SQLException {

		String sql = "insert into hosp.tipoatendimento (desctipoatendimento, "
				+ " primeiroatendimento, equipe_programa, codempresa, id) values (?, ?, ?, ?, DEFAULT) RETURNING id;";
		try {
			System.out.println("VAI CADASTRAR TIPO ATEN");
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, tipo.getDescTipoAt().toUpperCase());
			ps.setBoolean(2, tipo.isPrimeiroAt());
			ps.setBoolean(3, tipo.isEquipe());
			ps.setInt(4, 0);//cod empresa ?
			ps.execute();
			con.commit();
			
			ResultSet rs = ps.executeQuery();
			int idTipo = 0;
			if(rs.next()) {
				idTipo = rs.getInt("id");
				System.out.println("retorno "+ idTipo);
				insereTipoAtendimentoGrupo(idTipo, tipo.getGrupo().getIdGrupo());

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
	
	public void insereTipoAtendimentoGrupo (int idTipo, int idGrupo){
		String sql = "insert into hosp.tipoatendimento_grupo (codgrupo, codtipoatendimento) values(?,?);";
		try {
			System.out.println("VAI CADASTRAR TIPO ATEN");
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, idGrupo);
			ps.setInt(2, idTipo);
			
			ps.execute();
			con.commit();
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

	/*public List<TipoAtendimentoBean> listarTipoAtPorGrupo(int codGrupo) {
		List<TipoAtendimentoBean> lista = new ArrayList<>();
		String sql = "select t.id, t.codgrupo, t.desctipoatendimento, t.primeiroatendimento, t.codempresa, t.equipe_programa"
				+ " from hosp.grupo g, hosp.tipoatendimento t"
				+ " where g.id_grupo = ? and g.id_grupo = t.codgrupo";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, codGrupo);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				TipoAtendimentoBean tipo = new TipoAtendimentoBean();

				tipo.setDescTipoAt(rs.getString(3));
				tipo.setPrimeiroAt(rs.getBoolean(4));
				tipo.setCodEmpresa(rs.getInt(5));
				tipo.setEquipe(rs.getBoolean(6));

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
	}*/

	public List<TipoAtendimentoBean> listarTipoAt() {
		List<TipoAtendimentoBean> lista = new ArrayList<>();
		String sql = "select id, desctipoatendimento, primeiroatendimento, codempresa, equipe_programa"
				+ " from hosp.tipoatendimento order by id";
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

	public GrupoBean buscarGrupo(int codGrupo) {
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
			System.out.println("AQUI " + grupo.getDescGrupo());
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
			Integer tipo) {
		List<TipoAtendimentoBean> lista = new ArrayList<>();
		String sql = "select id, desctipoatendimento, primeiroatendimento, codempresa, equipe_programa"
				+ " from hosp.tipoatendimento";
		if (tipo == 1) {
			sql += " where desctipoatendimento LIKE ?  order by id";
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

	public boolean alterarTipo(TipoAtendimentoBean tipo) {
		String sql = "update hosp.tipoatendimento set desctipoatendimento = ?, primeiroatendimento = ?, equipe_programa = ? where id = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			ps.setString(1, tipo.getDescTipoAt().toUpperCase());
			ps.setBoolean(2, tipo.isPrimeiroAt());
			ps.setBoolean(3, tipo.isEquipe());
			ps.setInt(4, tipo.getIdTipo());
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
	
	public boolean alterarTipoGrupo(TipoAtendimentoBean tipo) {
		String sql = "update hosp.tipoatendimento set codgrupo = ? where codtipoatendimento = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			ps.setInt(1, tipo.getGrupo().getIdGrupo());
			ps.setInt(2, tipo.getIdTipo());
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
	
	public Boolean excluirTipo(TipoAtendimentoBean tipo) throws ProjetoException {
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
				con.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
}
