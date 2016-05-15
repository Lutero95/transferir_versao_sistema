package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.TipoAtendimentoBean;

public class TipoAtendimentoDAO {

	Connection con = null;
	PreparedStatement ps = null;

	public boolean gravarTipoAt(TipoAtendimentoBean tipo) throws SQLException {

		String sql = "insert into hosp.tipoatendimento (codgrupo, desctipoatendimento, "
				+ "primeiroatendimento, equipe_programa) values (?, ?, ?, ?);";
		try {
			System.out.println("VAI CADASTRAR TIPO ATEN");
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, tipo.getGrupo().getIdGrupo());
			ps.setString(2, tipo.getDescTipoAt().toUpperCase());
			ps.setBoolean(3, tipo.isPrimeiroAt());
			ps.setBoolean(4, tipo.isEquipe());
			ps.execute();
			con.commit();
			con.close();
			System.out.println("CADASTROU TIPO ATEN");
			return true;
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}

	public List<TipoAtendimentoBean> listarTipoAtPorGrupo(int codGrupo) {
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
				tipo.setIdTipo(rs.getInt(1));
				tipo.setGrupo(buscarGrupo(rs.getInt(2)));
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
	}

	public List<TipoAtendimentoBean> listarTipoAt() {
		List<TipoAtendimentoBean> lista = new ArrayList<>();
		String sql = "select id, codgrupo, desctipoatendimento, primeiroatendimento, codempresa, equipe_programa"
				+ " from hosp.tipoatendimento order by id";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				TipoAtendimentoBean tipo = new TipoAtendimentoBean();
				tipo.setIdTipo(rs.getInt(1));
				tipo.setGrupo(buscarGrupo(rs.getInt(2)));
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

	public List<TipoAtendimentoBean> listarTipoAtBusca(String descricao, Integer tipo) {
		List<TipoAtendimentoBean> lista = new ArrayList<>();
		String sql = "select id, codgrupo, desctipoatendimento, primeiroatendimento, codempresa, equipe_programa"
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
				tipo1.setGrupo(buscarGrupo(rs.getInt(2)));
				tipo1.setDescTipoAt(rs.getString(3));
				tipo1.setPrimeiroAt(rs.getBoolean(4));
				tipo1.setCodEmpresa(rs.getInt(5));
				tipo1.setEquipe(rs.getBoolean(6));

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

}
