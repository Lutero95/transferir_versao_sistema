package br.gov.al.maceio.sishosp.acl.dao;

import br.gov.al.maceio.sishosp.acl.model.Sistema;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SistemaDAO {

	private Connection conexao = null;

	public boolean cadastrarSistema(Sistema sistema) throws ProjetoException {

		String sql = "insert into acl.sistema (descricao, sigla, url, imagem, "
				+ "versao, ativo) values (?, ?, ?, ?, ?, ?)";

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setString(1, sistema.getDescricao());
			stmt.setString(2, sistema.getSigla());
			stmt.setString(3, sistema.getUrl());
			stmt.setString(4, sistema.getImagem());
			stmt.setString(5, sistema.getVersao());
			stmt.setBoolean(6, sistema.getAtivo());
			stmt.execute();

			conexao.commit();

			return true;
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
	}

	public boolean alterarSistema(Sistema sistema) throws ProjetoException {
		String sql = "update acl.sistema set descricao = ?, imagem = ?, versao = ?, "
				+ "ativo = ? where id = ?";

		boolean alterou = false;
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setString(1, sistema.getDescricao());
			stmt.setString(2, sistema.getImagem());
			stmt.setString(3, sistema.getVersao());
			stmt.setBoolean(4, sistema.getAtivo());
			stmt.setLong(5, sistema.getId());
			stmt.execute();

			conexao.commit();

			alterou = true;
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
		return alterou;
	}

	public boolean excluirSistema(Sistema sistema) throws ProjetoException {

		String sql = "delete from acl.sistema where id = ?";

		boolean excluiu = false;
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setLong(1, sistema.getId());
			stmt.execute();

			conexao.commit();

			excluiu = true;
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
		return excluiu;
	}

	public ArrayList<Sistema> buscarSistemaDesc(String valor)
			throws ProjetoException {

		String sql = "select * from acl.sistema where upper(descricao) like ? "
				+ "order by ativo desc, descricao";

		ArrayList<Sistema> lista = new ArrayList();

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setString(1, "%" + valor.toUpperCase() + "%");
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Sistema s = new Sistema();
				s.setId(rs.getInt("id"));
				s.setDescricao(rs.getString("descricao"));
				s.setSigla(rs.getString("sigla"));
				s.setUrl(rs.getString("url"));
				s.setImagem(rs.getString("imagem"));

				s.setImagem(s.getImagem().replace("../../imgs/", ""));
				s.setImagem(s.getImagem().replace(".png", ""));
				s.setImagem(s.getImagem().replace(".jpg", ""));
				s.setImagem(s.getImagem().replace(".gif", ""));
				s.setVersao(rs.getString("versao"));
				s.setAtivo(rs.getBoolean("ativo"));
				lista.add(s);
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

	public ArrayList<Sistema> listarSistemas() throws ProjetoException {

		String sql = "select * from acl.sistema order by ativo desc, descricao";

		ArrayList<Sistema> lista = new ArrayList();

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stm = conexao.prepareStatement(sql);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				Sistema s = new Sistema();
				s.setId(rs.getInt("id"));
				s.setDescricao(rs.getString("descricao"));
				s.setSigla(rs.getString("sigla"));
				s.setUrl(rs.getString("url"));
				s.setImagem(rs.getString("imagem"));

				s.setImagem(s.getImagem().replace("../../imgs/", ""));
				s.setImagem(s.getImagem().replace(".png", ""));
				s.setImagem(s.getImagem().replace(".jpg", ""));
				s.setImagem(s.getImagem().replace(".gif", ""));
				s.setVersao(rs.getString("versao"));
				s.setAtivo(rs.getBoolean("ativo"));
				lista.add(s);
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

	public ArrayList<Sistema> listarSiglas() throws ProjetoException {

		String sql = "select id, sigla from acl.sistema order by descricao";

		ArrayList<Sistema> lista = new ArrayList();

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stm = conexao.prepareStatement(sql);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				Sistema s = new Sistema();
				s.setId(rs.getInt("id"));
				s.setSigla(rs.getString("sigla").toLowerCase());
				lista.add(s);
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

	public List<Sistema> listarSistemasNaoAss(Integer id)
			throws ProjetoException {

		String sql = "select id,descricao from acl.sistema where id not in "
				+ "(select si.id from acl.sistema si "
				+ "join acl.perm_perfil pp on si.id = pp.id_sistema "
				+ "join acl.perfil pf on pf.id = pp.id_perfil where pf.id = ?)";

		ArrayList<Sistema> lista = new ArrayList();

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Sistema s = new Sistema();
				s.setId(rs.getInt("id"));
				s.setDescricao(rs.getString("descricao"));
				lista.add(s);
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

	public ArrayList<Sistema> listarSistemasAss(Integer id)
			throws ProjetoException {

		String sql = "select si.id, si.descricao from acl.sistema si "
				+ "join acl.perm_perfil pp on si.id = pp.id_sistema "
				+ "join acl.perfil pf on pf.id = pp.id_perfil "
				+ "where pf.id = ? group by si.id";

		ArrayList<Sistema> lista = new ArrayList();

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Sistema s = new Sistema();
				s.setId(rs.getInt("id"));
				s.setDescricao(rs.getString("descricao"));
				lista.add(s);
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

	public Sistema buscarSisMenuPreview(Integer idSistema)
			throws ProjetoException {

		String sql = "select * from acl.sistema where id = ?";

		Sistema sis = null;

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setInt(1, idSistema);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				sis = new Sistema();
				sis.setId(rs.getInt("id"));
				sis.setDescricao(rs.getString("descricao"));
				sis.setSigla(rs.getString("sigla"));
				sis.setUrl(rs.getString("url"));
				sis.setImagem(rs.getString("imagem"));

				sis.setImagem(sis.getImagem().replace("../../imgs/", ""));
				sis.setImagem(sis.getImagem().replace(".png", ""));
				sis.setImagem(sis.getImagem().replace(".jpg", ""));
				sis.setImagem(sis.getImagem().replace(".gif", ""));
				sis.setVersao(rs.getString("versao"));
				sis.setAtivo(rs.getBoolean("ativo"));
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
		return sis;
	}

	public ArrayList<Sistema> listarSistemasSource() throws ProjetoException {

		String sql = "select id, descricao from acl.sistema order by descricao";

		ArrayList<Sistema> lista = new ArrayList();

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stm = conexao.prepareStatement(sql);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				Sistema s = new Sistema();
				s.setId(rs.getInt("id"));
				s.setDescricao(rs.getString("descricao"));
				lista.add(s);
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
}