package br.gov.al.maceio.sishosp.acl.dao;

import br.gov.al.maceio.sishosp.acl.model.Perfil;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.TratamentoErrosUtil;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class PerfilDAO {

	private Connection conexao = null;

	public Boolean cadastrar(Perfil perfil) throws ProjetoException {

		boolean cadastrou = false;

		List<Long> listaIdPerm = perfil.getListaPermissoes();
		try {
			conexao = ConnectionFactory.getConnection();
			CallableStatement cs = conexao
					.prepareCall("{ ? = call acl.gravarperfil(?) }");
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setString(2, perfil.getDescricao());
			cs.execute();

			String idRetorno = null;
			idRetorno = String.valueOf(cs.getInt(1));

			String sql = "insert into acl.perm_perfil (id_perfil, id_permissao) values (?, ?)";

			PreparedStatement stmt = conexao.prepareStatement(sql);
			for (long idPermissao : listaIdPerm) {
				stmt.setLong(1, Long.parseLong(idRetorno));
				stmt.setLong(2, idPermissao);
				stmt.execute();
			}

			conexao.commit();

			cadastrou = true;
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				conexao.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				System.exit(1);
			}
		}
		return cadastrou;
	}

	public Boolean alterar(Perfil perfil) throws ProjetoException {

		String sql = "update acl.perfil set descricao = ? where id = ?";

		boolean alterou = false;

		List<Long> listaIdPerm = perfil.getListaPermissoes();
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setString(1, perfil.getDescricao());
			stmt.setLong(2, perfil.getId());
			stmt.executeUpdate();

			if (!perfil.getListaPermissoes().isEmpty()) {
				sql = "delete from acl.perm_perfil where id_perfil = ?";

				stmt = conexao.prepareStatement(sql);
				stmt.setLong(1, perfil.getId());
				stmt.execute();

				sql = "insert into acl.perm_perfil (id_perfil, id_permissao) values (?, ?)";

				stmt = conexao.prepareStatement(sql);
				for (long idPermissao : listaIdPerm) {
					stmt.setLong(1, perfil.getId());
					stmt.setLong(2, idPermissao);
					stmt.execute();
				}

				conexao.commit();

				alterou = true;
			}
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
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

	public boolean excluirPerfil(Perfil perfil) throws ProjetoException {

		String sql = "delete from acl.perm_perfil where id_perfil = ?";

		boolean excluiu = false;
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setLong(1, perfil.getId());
			stmt.execute();

			sql = "delete from acl.perfil where id = ?";

			stmt = conexao.prepareStatement(sql);
			stmt.setLong(1, perfil.getId());
			stmt.execute();

			conexao.commit();

			excluiu = true;
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
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

	public ArrayList<Perfil> listarPerfil() throws ProjetoException {

		String sql = "select * from acl.perfil order by descricao";

		ArrayList<Perfil> lista = new ArrayList<>();
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stm = conexao.prepareStatement(sql);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				Perfil p = new Perfil();
				p.setId(rs.getLong("id"));
				p.setDescricao(rs.getString("descricao"));
				lista.add(p);
			}
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
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

	public ArrayList<Perfil> buscarPerfisDesc(String valor)
			throws ProjetoException {

		String sql = "select id, descricao from acl.perfil "
				+ "where upper(descricao) like ? order by descricao";

		ArrayList<Perfil> lista = new ArrayList<>();
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setString(1, "%" + valor.toUpperCase() + "%");
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Perfil p = new Perfil();
				p.setId(rs.getLong("id"));
				p.setDescricao(rs.getString("descricao"));
				lista.add(p);
			}
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
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

	public boolean associarPermAoPerf(Perfil perfil) throws ProjetoException {

		String sql = "delete from acl.perm_perfil where id_permissao = ?";

		String sql2 = "insert into acl.perm_perfil (id_perfil, id_permissao) values (?, ?)";

		boolean associou = false;

		List<Long> listaId = perfil.getListaPermissoes();

		PreparedStatement stmt;
		try {
			conexao = ConnectionFactory.getConnection();
			stmt = conexao.prepareStatement(sql);
			stmt.setLong(1, perfil.getId());
			stmt.execute();
			stmt.close();

			if (!perfil.getListaPermissoes().isEmpty()) {
				stmt = conexao.prepareStatement(sql2);
				for (long idPermissao : listaId) {
					stmt.setLong(1, perfil.getId());
					stmt.setLong(2, idPermissao);
					stmt.execute();
				}
			}
			associou = true;

			conexao.commit();
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				conexao.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				System.exit(1);
			}
		}
		return associou;
	}

	public boolean editarPermissaoAssAoPerf(Perfil perfil)
			throws ProjetoException {

		String sql = "delete from acl.perm_perfil where id_permissao = ?";

		String sql2 = "insert into acl.perm_perfil (id_perfil, id_permissao) values (?, ?)";

		boolean associou = false;
		List<Long> listaId = perfil.getListaPermissoes();

		PreparedStatement stmt;
		try {
			conexao = ConnectionFactory.getConnection();
			stmt = conexao.prepareStatement(sql);
			stmt.setLong(1, perfil.getId());
			stmt.execute();

			if (!perfil.getListaPermissoes().isEmpty()) {
				stmt = conexao.prepareStatement(sql2);
				for (long idPermissao : listaId) {
					stmt.setLong(1, perfil.getId());
					stmt.setLong(2, idPermissao);
					stmt.execute();
				}
			}
			associou = true;
			conexao.commit();
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				conexao.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				System.exit(1);
			}
		}
		return associou;
	}

	public boolean associarPerfilAoSisAlt(Perfil perfil)
			throws ProjetoException {

		String sql = "update acl.perm_perfil set id_sistema = ? where id_perfil = ? and id_permissao = ?";

		boolean associou = false;

		List<Long> listaPerm = perfil.getListaPermissoes();
		List<Integer> listaSis = perfil.getListaSistemas();

		if (!listaPerm.isEmpty() && !listaSis.isEmpty()) {
			try {
				conexao = ConnectionFactory.getConnection();
				PreparedStatement stmt = conexao.prepareStatement(sql);
				for (long idSis : listaSis) {
					for (long idPerm : listaPerm) {
						stmt.setLong(1, idSis);
						stmt.setLong(2, perfil.getId());
						stmt.setLong(3, idPerm);
						stmt.execute();
					}
				}
				associou = true;

				conexao.commit();
			} catch (SQLException sqle) {
				throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
			} catch (Exception ex) {
				throw new ProjetoException(ex, this.getClass().getName());
			} finally {
				try {
					conexao.close();
				} catch (Exception ex) {
					ex.printStackTrace();
					System.exit(1);
				}
			}
		}
		return associou;
	}

	public boolean associarPerfilAoSisCad(Perfil perfil)
			throws ProjetoException {

		String sql = "delete from acl.perm_perfil where id_perfil = ?";

		String sql2 = "insert into acl.perm_perfil (id_perfil, id_permissao, id_sistema) values (?, ?, ?)";

		boolean associou = false;

		List<Long> listaPerm = perfil.getListaPermissoes();
		List<Integer> listaSis = perfil.getListaSistemas();

		PreparedStatement stmt;
		if (!listaSis.isEmpty() && !listaSis.isEmpty()) {
			try {
				conexao = ConnectionFactory.getConnection();
				stmt = conexao.prepareStatement(sql);
				stmt.setLong(1, perfil.getId());
				stmt.execute();

				stmt = conexao.prepareStatement(sql2);
				for (long idSis : listaSis) {
					for (long idPerm : listaPerm) {
						stmt.setLong(1, perfil.getId());
						stmt.setLong(2, idPerm);
						stmt.setLong(3, idSis);
						stmt.execute();
					}
				}
				associou = true;

				conexao.commit();
			} catch (SQLException sqle) {
				throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
			} catch (Exception ex) {
				throw new ProjetoException(ex, this.getClass().getName());
			} finally {
				try {
					conexao.close();
				} catch (Exception ex) {
					ex.printStackTrace();
					System.exit(1);
				}
			}
		}
		return associou;
	}
}