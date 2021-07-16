package br.gov.al.maceio.sishosp.acl.dao;

import br.gov.al.maceio.sishosp.acl.model.Funcao;
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

public class FuncaoDAO {

	private Connection conexao;

	public Boolean cadastrarFuncao(Funcao funcao) throws ProjetoException {

		boolean cadastrou = false;

		try {
			conexao = ConnectionFactory.getConnection();
			CallableStatement cs = conexao
					.prepareCall("{ ? = call acl.gravarfuncao(?, ?, ?, ?) }");
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setString(2, funcao.getDescricao());
			cs.setString(3, funcao.getCodigo());
			cs.setInt(4, funcao.getIdSistema());
			cs.setBoolean(5, funcao.isAtiva());
			cs.execute();

			Integer idRetornoFunc = cs.getInt(1);

			String codAux = "FC-" + idRetornoFunc;

			String sql = "update acl.funcao set codigo = ? where id = ?";

			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setString(1, codAux);
			stmt.setInt(2, idRetornoFunc);
			stmt.executeUpdate();

			cs = conexao.prepareCall("{ ? = call acl.gravarpermissao(?) }");
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setString(2, funcao.getDescricao());
			cs.execute();

			Integer idRetornoPerm = cs.getInt(1);

			sql = "insert into acl.perm_geral (id_funcao, id_permissao) values (?, ?)";

			stmt = conexao.prepareStatement(sql);
			stmt.setLong(1, idRetornoFunc);
			stmt.setLong(2, idRetornoPerm);
			stmt.execute();

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
				//comentado walter erro log ex.printStackTrace();
				System.exit(1);
			}
		}
		return cadastrou;
	}

	public Boolean alterarFuncao(Funcao funcao) throws ProjetoException {

		String sql = "update acl.funcao set descricao = ?,  "
				+ "id_sistema = ?, ativa = ? where id = ?";

		boolean alterou = false;

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setString(1, funcao.getDescricao());
			stmt.setInt(2, funcao.getIdSistema());
			stmt.setBoolean(3, funcao.isAtiva());
			stmt.setLong(4, funcao.getId());
			stmt.executeUpdate();

			conexao.commit();

			alterou = true;
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				conexao.close();
			} catch (Exception ex) {
				//comentado walter erro log ex.printStackTrace();
				System.exit(1);
			}
		}
		return alterou;
	}

	public boolean excluirFuncao(Funcao funcao) throws ProjetoException {

		String sql = "delete from acl.funcao where id = ?";

		boolean excluiu = false;

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setLong(1, funcao.getId());
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
				//comentado walter erro log ex.printStackTrace();
				System.exit(1);
			}
		}
		return excluiu;
	}

	public List<Funcao> listarFuncoes() throws ProjetoException {

		String sql = "select fun.id, fun.descricao, fun.codigo, "
				+ "fun.id_sistema, fun.ativa,  "
				+ "sis.descricao as sis_desc from acl.funcao fun "
				+ " join acl.sistema sis on sis.id = fun.id_sistema "
				+ "order by fun.ativa desc, fun.descricao, sis.descricao";

		List<Funcao> lista = new ArrayList<>();

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Funcao funcao = new Funcao();
				funcao.setId(rs.getLong("id"));
				funcao.setDescricao(rs.getString("descricao"));
				funcao.setCodigo(rs.getString("codigo"));

				funcao.setAtiva(rs.getBoolean("ativa"));

				funcao.setIdSistema(rs.getInt("id_sistema"));
				funcao.setDescSistema(rs.getString("sis_desc"));
				lista.add(funcao);
			}
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				conexao.close();
			} catch (Exception ex) {
				//comentado walter erro log ex.printStackTrace();
				System.exit(1);
			}
		}
		return lista;
	}

	public List<Funcao> listarFuncoesComSisRot() throws ProjetoException {

		String sql = "select fu.id, fu.descricao, fu.codigo,  fu.ativa, si.id as id_sis, "
				+ "si.descricao as desc_sis, si.sigla as sigla_sis  "
				+ "from acl.permissao pm "
				+ "join acl.perm_geral pg on pg.id_permissao = pm.id "
				+ "join acl.funcao fu on fu.id = pg.id_funcao "
				+ "join acl.sistema si on si.id = fu.id_sistema ";

		List<Funcao> lista = new ArrayList<>();

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Funcao funcao = new Funcao();
				funcao.setId(rs.getLong("id"));
				funcao.setDescricao(rs.getString("descricao"));
				funcao.setAtiva(rs.getBoolean("ativa"));

				funcao.setCodigo(rs.getString("codigo"));

				funcao.setIdSistema(rs.getInt("id_sis"));
				funcao.setDescSistema(rs.getString("desc_sis"));
				funcao.setSiglaSistema(rs.getString("sigla_sis").toUpperCase());
				lista.add(funcao);
			}
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				conexao.close();
			} catch (Exception ex) {
				//comentado walter erro log ex.printStackTrace();
				System.exit(1);
			}
		}
		return lista;
	}

	public List<Funcao> listarFuncoesSourceEdit(Long idPerfil)
			throws ProjetoException {

		String sql = "select fu.id, fu.descricao, fu.codigo,  fu.ativa, "
				+ "si.id as id_sis, si.descricao as desc_sis, si.sigla as sigla_sis "
				+ " from acl.funcao fu "
				+ "join acl.perm_geral pg on pg.id_funcao = fu.id "
				+ "join acl.sistema si on si.id = fu.id_sistema "

				+ "where fu.id not in (select fu.id from acl.permissao pm "
				+ "join acl.perm_geral pg on pg.id_permissao = pm.id "
				+ "join acl.perm_perfil pp on pp.id_permissao = pm.id "
				+ "join acl.perfil pf on pf.id = pp.id_perfil "
				+ "join acl.funcao fu on fu.id = pg.id_funcao "
				+ "where pp.id_perfil = ?)";

		List<Funcao> lista = new ArrayList<>();

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setLong(1, idPerfil);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Funcao funcao = new Funcao();
				funcao.setId(rs.getLong("id"));
				funcao.setDescricao(rs.getString("descricao"));
				funcao.setAtiva(rs.getBoolean("ativa"));

				funcao.setCodigo(rs.getString("codigo"));

				funcao.setIdSistema(rs.getInt("id_sis"));
				funcao.setDescSistema(rs.getString("desc_sis"));
				funcao.setSiglaSistema(rs.getString("sigla_sis").toUpperCase());
				lista.add(funcao);
			}
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				conexao.close();
			} catch (Exception ex) {
				//comentado walter erro log ex.printStackTrace();
				System.exit(1);
			}
		}
		return lista;
	}

	public List<Funcao> listarFuncoesTargetEdit(Long idPerfil)
			throws ProjetoException {

		String sql = "select fu.id, fu.descricao, fu.codigo,  fu.ativa, "
				+ "si.id as id_sis, si.descricao as desc_sis, si.sigla as sigla_sis "
				+ " from acl.funcao fu join acl.perm_geral pg "
				+ "on pg.id_funcao = fu.id join acl.permissao pm on pm.id = pg.id_permissao "
				+ "join acl.perm_perfil pp on pp.id_permissao = pm.id join acl.perfil pf on "
				+ "pf.id = pp.id_perfil join acl.sistema si on si.id = fu.id_sistema  "
				+ " where pf.id = ?";

		List<Funcao> lista = new ArrayList<>();

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setLong(1, idPerfil);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Funcao funcao = new Funcao();
				funcao.setId(rs.getLong("id"));
				funcao.setDescricao(rs.getString("descricao"));
				funcao.setAtiva(rs.getBoolean("ativa"));

				funcao.setCodigo(rs.getString("codigo"));

				funcao.setIdSistema(rs.getInt("id_sis"));
				funcao.setDescSistema(rs.getString("desc_sis"));
				funcao.setSiglaSistema(rs.getString("sigla_sis").toUpperCase());
				lista.add(funcao);
			}
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				conexao.close();
			} catch (Exception ex) {
				//comentado walter erro log ex.printStackTrace();
				System.exit(1);
			}
		}
		return lista;
	}

	public List<Funcao> listarFuncaoItemSourcerUser(Long idPerfil)
			throws ProjetoException {

		String sql = "select fu.id, fu.descricao, fu.codigo, fu.ativa, si.id as id_sis, si.descricao as desc_sis, si.sigla as sigla_sis from acl.permissao "
				+ "pm join acl.perm_geral pg on pg.id_permissao = pm.id "
				+ "join acl.funcao fu on fu.id = pg.id_funcao "
				+ "join acl.sistema si on si.id = fu.id_sistema "
				+ "where fu.id not in (select fu.id from acl.perm_perfil pp "
				+ "join acl.perfil pf on pf.id = pp.id_perfil "
				+ "join acl.permissao pm on pm.id = pp.id_permissao "
				+ "join acl.perm_geral pg on pg.id_permissao = pm.id "
				+ "join acl.funcao fu on fu.id = pg.id_funcao "
				+ "join acl.sistema si on si.id = fu.id_sistema where pf.id = ? ) order by fu.descricao";

		List<Funcao> lista = new ArrayList<>();

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setLong(1, idPerfil);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Funcao funcao = new Funcao();
				funcao.setId(rs.getLong("id"));
				funcao.setDescricao(rs.getString("descricao"));
				funcao.setAtiva(rs.getBoolean("ativa"));
				funcao.setCodigo(rs.getString("codigo"));
				funcao.setIdSistema(rs.getInt("id_sis"));
				funcao.setDescSistema(rs.getString("desc_sis"));
				funcao.setSiglaSistema(rs.getString("sigla_sis").toUpperCase());
				lista.add(funcao);
			}
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				conexao.close();
			} catch (Exception ex) {
				//comentado walter erro log ex.printStackTrace();
				System.exit(1);
			}
		}
		return lista;
	}

	public List<Funcao> listarFuncaosPerfil(Integer idPerfil)
			throws ProjetoException {

		String sql = "select fu.id, fu.descricao, fu.codigo, fu.ativa, si.id as id_sis, "
				+ "si.descricao as desc_sis, si.sigla as sigla_sis "
				+ "from acl.permissao pm "
				+ "join acl.perm_geral pg on pg.id_permissao = pm.id "
				+ "join acl.perm_perfil pp on pp.id_permissao = pg.id_permissao "
				+ "join acl.perfil pf on pf.id = pp.id_perfil "
				+ "join acl.funcao fu on fu.id = pg.id_funcao "
				+ "join acl.sistema si on si.id = fu.id_sistema "
				+ "  where pf.id = ? " + "order by fu.descricao;";

		List<Funcao> lista = new ArrayList<>();

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setInt(1, idPerfil);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Funcao funcao = new Funcao();
				funcao.setId(rs.getLong("id"));
				funcao.setDescricao(rs.getString("descricao"));
				funcao.setAtiva(rs.getBoolean("ativa"));

				funcao.setCodigo(rs.getString("codigo"));

				funcao.setIdSistema(rs.getInt("id_sis"));
				funcao.setDescSistema(rs.getString("desc_sis"));
				funcao.setSiglaSistema(rs.getString("sigla_sis").toUpperCase());
				lista.add(funcao);
			}
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				conexao.close();
			} catch (Exception ex) {
				//comentado walter erro log ex.printStackTrace();
				System.exit(1);
			}
		}
		return lista;
	}

	public List<Funcao> listarFuncaoItemSourcerUserEdit(Integer idPerfil,
			Integer idUsuario) throws ProjetoException {

		String sql = "select fu.id, fu.descricao, fu.codigo,  fu.ativa, "
				+ "si.id as id_sis, si.descricao as desc_sis, si.sigla as sigla_sis "
				+ " from acl.funcao fu "
				+ "join acl.perm_geral pg on pg.id_funcao = fu.id "
				+ "join acl.sistema si on si.id = fu.id_sistema "
				+ "where fu.id not in ( "
				+ "select fu.id from acl.permissao pm "
				+ "join acl.perm_geral pg on pg.id_permissao = pm.id "
				+ "join acl.perm_perfil pp on pp.id_permissao = pm.id "
				+ "join acl.perfil pf on pf.id = pp.id_perfil "
				+ "join acl.funcao fu on fu.id = pg.id_funcao "
				+ "where pp.id_perfil = ? " + "union "
				+ "select fu.id from acl.perm_usuario pu "
				+ "join acl.permissao pm on pm.id = pu.id_permissao "
				+ "join acl.perm_geral pg on pg.id_permissao = pm.id "
				+ "join acl.funcao fu on fu.id = pg.id_funcao "
				+ "join acl.sistema si on si.id = fu.id_sistema "
				+ "where pu.id_usuario = ? " + ") order by fu.descricao;";

		List<Funcao> lista = new ArrayList<>();

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setInt(1, idPerfil);
			stmt.setInt(2, idUsuario);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Funcao funcao = new Funcao();
				funcao.setId(rs.getLong("id"));
				funcao.setDescricao(rs.getString("descricao"));
				funcao.setAtiva(rs.getBoolean("ativa"));

				funcao.setCodigo(rs.getString("codigo"));

				funcao.setIdSistema(rs.getInt("id_sis"));
				funcao.setDescSistema(rs.getString("desc_sis"));
				funcao.setSiglaSistema(rs.getString("sigla_sis").toUpperCase());
				lista.add(funcao);
			}
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				conexao.close();
			} catch (Exception ex) {
				//comentado walter erro log ex.printStackTrace();
				System.exit(1);
			}
		}
		return lista;
	}

	public List<Funcao> listarFuncaoItemTargetUserEdit(Integer idUsuario)
			throws ProjetoException {

		String sql = "select fu.id, fu.descricao, fu.codigo,  fu.ativa, "
				+ "si.id as id_sis, si.descricao as desc_sis, si.sigla as sigla_sis "
				+ "from acl.perm_usuario pu "
				+ "join acl.permissao pm on pm.id = pu.id_permissao "
				+ "join acl.perm_geral pg on pg.id_permissao = pm.id "
				+ "join acl.funcao fu on fu.id = pg.id_funcao "
				+ "join acl.sistema si on si.id = fu.id_sistema "
				+ "where pu.id_usuario = ? order by fu.descricao;";

		List<Funcao> lista = new ArrayList<>();

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setInt(1, idUsuario);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Funcao funcao = new Funcao();
				funcao.setId(rs.getLong("id"));
				funcao.setDescricao(rs.getString("descricao"));
				funcao.setAtiva(rs.getBoolean("ativa"));
				funcao.setCodigo(rs.getString("codigo"));

				funcao.setIdSistema(rs.getInt("id_sis"));
				funcao.setDescSistema(rs.getString("desc_sis"));
				funcao.setSiglaSistema(rs.getString("sigla_sis").toUpperCase());
				lista.add(funcao);
			}
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				conexao.close();
			} catch (Exception ex) {
				//comentado walter erro log ex.printStackTrace();
				System.exit(1);
			}
		}
		return lista;
	}
}