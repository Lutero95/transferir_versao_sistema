package br.gov.al.maceio.sishosp.acl.dao;

import br.gov.al.maceio.sishosp.acl.model.Menu;
import br.gov.al.maceio.sishosp.acl.model.Sistema;
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

public class MenuDAO {

	private Connection conexao;

	public Boolean cadastrar(Menu menu) throws ProjetoException {

		boolean cadastrou = false;

		List<Integer> listaId = menu.getListaSistemas();

		try {
			conexao = ConnectionFactory.getConnection();
			CallableStatement cs = conexao
					.prepareCall("{ ? = call acl.gravarmenu2(?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }");
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setString(2, menu.getDescricao());
			cs.setString(3, menu.getCodigo());
			cs.setString(4, menu.getIndice());
			cs.setString(5, menu.getTipo());
			cs.setBoolean(6, menu.isAtivo());
			cs.setString(7, menu.getAction());
			cs.setString(8, menu.getOnclick());
			cs.setString(9, menu.getDiretorio());
			cs.setString(10, menu.getDescPagina());
			cs.setString(11, menu.getExtensao());
			cs.execute();

			Integer idRetornoMenu = cs.getInt(1);

			String codAux = "MN-" + idRetornoMenu;

			String sql = "update acl.menu set codigo = ? where id = ?";

			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setString(1, codAux);
			stmt.setInt(2, idRetornoMenu);
			stmt.executeUpdate();

			cs = conexao.prepareCall("{ ? = call acl.gravarpermissao(?) }");
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setString(2, menu.getDescricao());
			cs.execute();

			Integer idRetornoPerm = cs.getInt(1);

			sql = "insert into acl.menu_sistema (id_menu, id_sistema) values (?, ?)";

			stmt = conexao.prepareStatement(sql);
			for (Integer idSis : listaId) {
				stmt.setLong(1, idRetornoMenu);
				stmt.setLong(2, idSis);
				stmt.execute();
			}

			sql = "insert into acl.perm_geral (id_menu, id_permissao) values (?, ?)";

			stmt = conexao.prepareStatement(sql);
			stmt.setLong(1, idRetornoMenu);
			stmt.setInt(2, idRetornoPerm);
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

	public Boolean alterar(Menu menu) throws ProjetoException {

		String sql = "update acl.menu set descricao = ?, desc_pagina = ?, diretorio = ?, "
				+ "extensao = ?, codigo = ?, indice = ?, tipo = ?,  ativo = ?, "
				+ "action_rel = ?, onclick_rel = ? where id = ?";

		boolean alterou = false;

		List<Integer> listaId = menu.getListaSistemas();

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setString(1, menu.getDescricao());
			stmt.setString(2, menu.getDescPagina());
			stmt.setString(3, menu.getDiretorio());
			stmt.setString(4, menu.getExtensao());
			stmt.setString(5, menu.getCodigo());
			stmt.setString(6, menu.getIndice());
			stmt.setString(7, menu.getTipo());
			stmt.setBoolean(8, menu.isAtivo());
			stmt.setString(9, menu.getAction());
			stmt.setString(10, menu.getOnclick());
			stmt.setLong(11, menu.getId());
			stmt.executeUpdate();

			if (!menu.getListaSistemas().isEmpty()) {
				sql = "delete from acl.menu_sistema where id_menu = ?";

				stmt = conexao.prepareStatement(sql);
				stmt.setLong(1, menu.getId());
				stmt.execute();

				sql = "insert into acl.menu_sistema (id_menu, id_sistema) values (?, ?)";

				stmt = conexao.prepareStatement(sql);
				for (Integer idSis : listaId) {
					stmt.setLong(1, menu.getId());
					stmt.setInt(2, idSis);
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
				//comentado walter erro log ex.printStackTrace();
				System.exit(1);
			}
		}
		return alterou;
	}

	public boolean excluirMenu(Menu menu) throws ProjetoException {

		String sql = "delete from acl.menu where id = ?";

		boolean excluiu = false;
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setLong(1, menu.getId());
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

	public List<Menu> buscarMenuDesc(String valor) throws ProjetoException {

		String sql = "select me.id, me.descricao, me.codigo, me.indice, me.tipo, "
				+ "me.ativo, me.diretorio, me.desc_pagina, me.extensao,  "
				+ "me.action_rel, me.onclick_rel, diretorio, desc_pagina, extensao from "
				+ "acl.menu me  where "
				+ "upper(me.descricao) like ? order by me.ativo desc, me.descricao, me.tipo";

		List<Menu> lista = new ArrayList<>();

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setString(1, "%" + valor.toUpperCase() + "%");
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Menu menu = new Menu();
				menu.setId(rs.getLong("id"));
				menu.setDescricao(rs.getString("descricao"));
				menu.setCodigo(rs.getString("codigo"));
				menu.setIndice(rs.getString("indice"));
				menu.setTipo(rs.getString("tipo"));
				menu.setAtivo(rs.getBoolean("ativo"));

				menu.setDiretorio(rs.getString("diretorio"));
				menu.setDescPagina(rs.getString("desc_pagina"));
				menu.setExtensao(rs.getString("extensao"));

				if (rs.getString("tipo").equals("menuItem")) {
					menu.setUrl("/pages/" + menu.getDiretorio() + "/"
							+ menu.getDescPagina() + menu.getExtensao());

				}
				menu.setIndiceAux(rs.getString("descricao"));

				if (rs.getString("action_rel") != null) {
					menu.setAction(rs.getString("action_rel"));
				} else {
					menu.setAction(null);
				}

				if (rs.getString("onclick_rel") != null) {
					menu.setOnclick(rs.getString("onclick_rel"));
				} else {
					menu.setOnclick(null);
				}

				lista.add(menu);
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

	public List<Menu> listarMenuGeral() throws ProjetoException {

		String sql = "select me.id, me.descricao, me.codigo, me.indice, me.tipo, "
				+ "me.ativo, diretorio, desc_pagina, extensao,  "
				+ "me.action_rel, me.onclick_rel from "
				+ "acl.menu me   order by "
				+ "me.ativo desc, me.descricao, me.tipo";

		List<Menu> lista = new ArrayList<>();

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Menu menu = new Menu();
				menu.setId(rs.getLong("id"));
				menu.setDescricao(rs.getString("descricao"));
				menu.setCodigo(rs.getString("codigo"));
				menu.setIndice(rs.getString("indice"));
				menu.setTipo(rs.getString("tipo"));
				menu.setAtivo(rs.getBoolean("ativo"));

				menu.setDiretorio(rs.getString("diretorio"));
				menu.setDescPagina(rs.getString("desc_pagina"));
				menu.setExtensao(rs.getString("extensao"));

				if (rs.getString("tipo").equals("menuItem")) {
					menu.setUrl("/pages/" + menu.getDiretorio() + "/"
							+ menu.getDescPagina() + menu.getExtensao());
				}

				menu.setIndiceAux(rs.getString("descricao"));

				if (rs.getString("action_rel") != null) {
					menu.setAction(rs.getString("action_rel"));
				} else {
					menu.setAction(null);
				}

				if (rs.getString("onclick_rel") != null) {
					menu.setOnclick(rs.getString("onclick_rel"));
				} else {
					menu.setOnclick(null);
				}

				lista.add(menu);
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

	public ArrayList<Menu> listarMenus() throws ProjetoException {

		String sql = "select * from acl.menu order by descricao";

		ArrayList<Menu> lista = new ArrayList<>();
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Menu menu = new Menu();
				menu.setId(rs.getLong("id"));
				menu.setDescricao(rs.getString("descricao"));
				menu.setUrl(rs.getString("url"));
				menu.setCodigo(rs.getString("codigo"));
				menu.setIndice(rs.getString("indice"));
				menu.setTipo(rs.getString("tipo"));
				menu.setAtivo(rs.getBoolean("ativo"));
				lista.add(menu);
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

	public List<Menu> listarMenusPaiSubmenus() throws ProjetoException {

		String sql = "select * from acl.menu where tipo = 'menuPai' or tipo = 'submenu' "
				+ "and ativo = true order by descricao, tipo";

		List<Menu> lista = new ArrayList<>();

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Menu menu = new Menu();
				menu.setId(rs.getLong("id"));
				menu.setDescricao(rs.getString("descricao"));
				menu.setCodigo(rs.getString("codigo"));
				menu.setIndice(rs.getString("indice"));
				menu.setTipo(rs.getString("tipo"));
				menu.setAtivo(rs.getBoolean("ativo"));

				menu.setDiretorio(rs.getString("diretorio"));
				menu.setDescPagina(rs.getString("desc_pagina"));
				menu.setExtensao(rs.getString("extensao"));

				if (rs.getString("tipo").equals("menuItem")) {
					menu.setUrl("/pages/" + menu.getDiretorio() + "/"
							+ menu.getDescPagina() + menu.getExtensao());
				}
				menu.setIndiceAux(rs.getString("descricao"));
				lista.add(menu);
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

	public List<Menu> listarMenuItem() throws ProjetoException {

		String sql = "select * from acl.menu where tipo = 'menuItem' and ativo = true "
				+ "order by descricao";

		List<Menu> lista = new ArrayList<>();

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Menu menu = new Menu();
				menu.setId(rs.getLong("id"));
				menu.setDescricao(rs.getString("descricao"));
				menu.setUrl(rs.getString("url"));
				menu.setCodigo(rs.getString("codigo"));
				menu.setIndice(rs.getString("indice"));
				menu.setTipo(rs.getString("tipo"));
				menu.setAtivo(rs.getBoolean("ativo"));
				lista.add(menu);
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

	public ArrayList<Menu> listarMenuItemComSis() throws ProjetoException {

		String sql = "select me.id, me.descricao, me.codigo, me.indice, me.tipo, "
				+ "me.ativo, diretorio, desc_pagina, extensao, si.id as id_sis, "
				+ "si.descricao as desc_sis, si.sigla as sigla_sis from acl.permissao pm "
				+ "join acl.perm_geral pg on pg.id_permissao = pm.id "
				+ "join acl.menu me on me.id = pg.id_menu "
				+ "join acl.menu_sistema ms on ms.id_menu = me.id "
				+ "join acl.sistema si on si.id = ms.id_sistema "

				+ "where me.tipo = 'menuItem' or me.tipo = 'menuItemRel'";

		ArrayList<Menu> lista = new ArrayList<>();
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Menu menu = new Menu();
				menu.setId(rs.getLong("id"));
				menu.setDescricao(rs.getString("descricao"));
				menu.setCodigo(rs.getString("codigo"));
				menu.setIndice(rs.getString("indice"));
				menu.setTipo(rs.getString("tipo"));
				menu.setAtivo(rs.getBoolean("ativo"));

				menu.setDiretorio(rs.getString("diretorio"));
				menu.setDescPagina(rs.getString("desc_pagina"));
				menu.setExtensao(rs.getString("extensao"));

				if (rs.getString("tipo").equals("menuItem")) {
					menu.setUrl("/pages/" + menu.getDiretorio() + "/"
							+ menu.getDescPagina() + menu.getExtensao());

				}
				menu.setIndiceAux(rs.getString("codigo"));

				menu.setIdSistema(rs.getInt("id_sis"));
				menu.setDescSistema(rs.getString("desc_sis"));
				menu.setSiglaSistema(rs.getString("sigla_sis").toUpperCase());
				lista.add(menu);
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

	public ArrayList<Menu> listarMenuItemSourcerEdit(Long idPerfil, Long idUsuario)
			throws ProjetoException {

		String sql = "select me.id, me.descricao, me.codigo, me.indice, me.tipo, "
				+ "me.ativo, diretorio, desc_pagina, extensao, si.id as id_sis, "
				+ "si.descricao as desc_sis, si.sigla as sigla_sis from acl.menu me "
				+ "join acl.perm_geral pg on pg.id_menu = me.id "
				+ "join acl.permissao pm on pm.id = pg.id_permissao "
				+ "join acl.menu_sistema ms on ms.id_menu = me.id "
				+ "join acl.sistema si on si.id = ms.id_sistema where me.id not in("
				+ "select me.id from acl.perm_perfil pp "
				+ "join acl.perfil pf on pf.id = pp.id_perfil "
				+ "join acl.permissao pm on pm.id = pp.id_permissao "
				+ "join acl.perm_geral pg on pg.id_permissao = pm.id "
				+ "join acl.menu me on me.id = pg.id_menu "
				+ "join acl.menu_sistema ms on ms.id_menu = me.id "
				+ "join acl.sistema si on si.id = ms.id_sistema "
				+ "where (me.tipo = 'menuItem' or me.tipo = 'menuItemRel' or me.tipo = 'rotinaInterna') and pf.id = ?) " +
				" AND pm.id NOT IN (SELECT pu.id_permissao FROM acl.perm_usuario pu WHERE pu.id_usuario = ?)"
				+ "and (me.tipo = 'menuItem' or me.tipo = 'menuItemRel' or me.tipo = 'rotinaInterna') order by me.descricao";

		ArrayList<Menu> lista = new ArrayList<>();
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setLong(1, idPerfil);
			stmt.setLong(2, idUsuario);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Menu menu = new Menu();
				menu.setId(rs.getLong("id"));
				menu.setDescricao(rs.getString("descricao"));
				menu.setCodigo(rs.getString("codigo"));
				menu.setIndice(rs.getString("indice"));
				menu.setTipo(rs.getString("tipo"));
				menu.setAtivo(rs.getBoolean("ativo"));

				menu.setDiretorio(rs.getString("diretorio"));
				menu.setDescPagina(rs.getString("desc_pagina"));
				menu.setExtensao(rs.getString("extensao"));

				if (rs.getString("tipo").equals("menuItem")) {
					menu.setUrl("/pages/" + menu.getDiretorio() + "/"
							+ menu.getDescPagina() + menu.getExtensao());
				}
				menu.setIndiceAux(rs.getString("codigo"));

				menu.setIdSistema(rs.getInt("id_sis"));
				menu.setDescSistema(rs.getString("desc_sis"));
				menu.setSiglaSistema(rs.getString("sigla_sis").toUpperCase());
				lista.add(menu);
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

	// LISTA OK
	public ArrayList<Menu> listarMenuItemTargetEdit(Integer idPerfil)
			throws ProjetoException {

		String sql = " select distinct mex.id , me.descricao, me.codigo, me.indice, me.tipo, \n" + 
				"me.ativo, me.diretorio, me.desc_pagina, me.extensao, si.id as id_sis, \n" + 
				"si.descricao as desc_sis, si.sigla as sigla_sis, pm.id as id_permissao \n" + 
				"from acl.menu me \n" + 
				"join acl.perm_geral pg on pg.id_menu = me.id \n" + 
				"join acl.menu mex on pg.id_menu = mex.id \n" + 
				"join acl.permissao pm on pm.id = pg.id_permissao \n" + 
				"join acl.menu_sistema ms on ms.id_menu = me.id \n" + 
				"join acl.sistema si on si.id = ms.id_sistema \n" + 
				"join acl.perm_perfil pp on pp.id_permissao = pg.id_permissao \n" + 
				"join acl.perfil pf on pf.id = pp.id_perfil \n" + 
				"where me.ativo = true and (me.tipo = 'menuItem' or me.tipo = 'menuItemRel' OR me.tipo = 'rotinaInterna') \n" + 
				"and pf.id = ? order by me.descricao;";

		ArrayList<Menu> lista = new ArrayList<>();
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setLong(1, idPerfil);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Menu menu = new Menu();
				menu.setId(rs.getLong("id"));
				menu.setDescricao(rs.getString("descricao"));
				menu.setCodigo(rs.getString("codigo"));
				menu.setIndice(rs.getString("indice"));
				menu.setTipo(rs.getString("tipo"));
				menu.setAtivo(rs.getBoolean("ativo"));

				menu.setDiretorio(rs.getString("diretorio"));
				menu.setDescPagina(rs.getString("desc_pagina"));
				menu.setExtensao(rs.getString("extensao"));

				if (rs.getString("tipo").equals("menuItem")) {
					menu.setUrl("/pages/" + menu.getDiretorio() + "/"
							+ menu.getDescPagina() + menu.getExtensao());
				}
				menu.setIndiceAux(rs.getString("codigo"));

				menu.setIdSistema(rs.getInt("id_sis"));
				menu.setDescSistema(rs.getString("desc_sis"));
				menu.setSiglaSistema(rs.getString("sigla_sis").toUpperCase());
				lista.add(menu);
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

	public ArrayList<Menu> listarMenuPaiSubmenuComSis() throws ProjetoException {

		String sql = "select me.id, me.descricao, me.codigo, me.indice, me.tipo, me.ativo, "
				+ "diretorio, desc_pagina, extensao, si.id as id_sis, "
				+ "si.descricao as desc_sis, si.sigla as sigla_sis from acl.permissao pm "
				+ "join acl.perm_geral pg on pg.id_permissao = pm.id "
				+ "join acl.menu me on me.id = pg.id_menu "
				+ "join acl.menu_sistema ms on ms.id_menu = me.id "
				+ "join acl.sistema si on si.id = ms.id_sistema "
				+ "where me.tipo = 'menuPai' or me.tipo = 'submenu'";

		ArrayList<Menu> lista = new ArrayList<>();
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Menu menu = new Menu();
				menu.setId(rs.getLong("id"));
				menu.setDescricao(rs.getString("descricao"));
				menu.setCodigo(rs.getString("codigo"));
				menu.setIndice(rs.getString("indice"));
				menu.setTipo(rs.getString("tipo"));
				menu.setAtivo(rs.getBoolean("ativo"));

				menu.setDiretorio(rs.getString("diretorio"));
				menu.setDescPagina(rs.getString("desc_pagina"));
				menu.setExtensao(rs.getString("extensao"));

				if (rs.getString("tipo").equals("menuItem")) {
					menu.setUrl("/pages/" + menu.getDiretorio() + "/"
							+ menu.getDescPagina() + menu.getExtensao());
				}
				menu.setIndiceAux(rs.getString("codigo"));

				menu.setIdSistema(rs.getInt("id_sis"));
				menu.setDescSistema(rs.getString("desc_sis"));
				menu.setSiglaSistema(rs.getString("sigla_sis").toUpperCase());
				lista.add(menu);
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

	public ArrayList<Menu> listarMenusPorSistema(Integer id)
			throws ProjetoException {

		String sql = "select ms.id, " + "ms.id_menu, " + "ms.id_sistema, "
				+ "m.descricao " + "from acl.menu_sistema ms join acl.menu m "
				+ "on m.id = ms.id_menu "
				+ "join acl.sistema s on s.id = ms.id_sistema "
				+ "where s.id = ? order by m.descricao";

		ArrayList<Menu> lista = new ArrayList<>();

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Menu menu = new Menu();
				menu.setIdRecSistema(rs.getInt("id"));
				menu.setDescricao(rs.getString("descricao"));
				menu.setIdSistema(rs.getInt("id_sistema"));
				menu.setId(rs.getLong("id_menu"));
				lista.add(menu);
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

	public ArrayList<Sistema> listarSisAssNaoMenuSource(Long idMenu)
			throws ProjetoException {

		String sql = "select id, descricao from acl.sistema where id not in "
				+ "(select si.id from acl.sistema si "
				+ "join acl.menu_sistema ms on ms.id_sistema = si.id "
				+ "join acl.menu me on me.id = ms.id_menu "
				+ "where me.id = ?)";

		ArrayList<Sistema> lista = new ArrayList<>();

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setLong(1, idMenu);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Sistema sistema = new Sistema();
				sistema.setId(rs.getInt("id"));
				sistema.setDescricao(rs.getString("descricao"));
				lista.add(sistema);
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

	public ArrayList<Sistema> listarSisAssMenuTarget(Long idMenu)
			throws ProjetoException {

		String sql = "select si.id, si.descricao from acl.sistema si "
				+ "join acl.menu_sistema ms on ms.id_sistema = si.id "
				+ "join acl.menu me on me.id = ms.id_menu " + "where me.id = ?";

		ArrayList<Sistema> lista = new ArrayList<>();

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setLong(1, idMenu);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Sistema sistema = new Sistema();
				sistema.setId(rs.getInt("id"));
				sistema.setDescricao(rs.getString("descricao"));
				lista.add(sistema);
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

	public ArrayList<Menu> listarMenuItemSourcerUser(Long idPerfil)
			throws ProjetoException {

		String sql = "select me.id, me.descricao, me.codigo, me.indice, me.tipo, "
				+ "me.ativo, diretorio, desc_pagina, extensao, si.id as id_sis, "
				+ "si.descricao as desc_sis, si.sigla as sigla_sis from acl.menu me "
				+ "join acl.perm_geral pg on pg.id_menu = me.id "
				+ "join acl.permissao pm on pm.id = pg.id_permissao "
				+ "join acl.menu_sistema ms on ms.id_menu = me.id "
				+ "join acl.sistema si on si.id = ms.id_sistema "
				+ "where me.id not in("
				+ "	select me.id from acl.perm_perfil pp "
				+ "	join acl.perfil pf on pf.id = pp.id_perfil "
				+ "	join acl.permissao pm on pm.id = pp.id_permissao "
				+ "	join acl.perm_geral pg on pg.id_permissao = pm.id "
				+ "	join acl.menu me on me.id = pg.id_menu "
				+ "	join acl.menu_sistema ms on ms.id_menu = me.id "
				+ "	join acl.sistema si on si.id = ms.id_sistema "
				+ "	where (me.tipo = 'menuItem' or me.tipo = 'menuItemRel') and pf.id = ?"
				+ ") order by me.descricao";

		ArrayList<Menu> lista = new ArrayList<>();
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setLong(1, idPerfil);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Menu menu = new Menu();
				menu.setId(rs.getLong("id"));
				menu.setDescricao(rs.getString("descricao"));
				menu.setCodigo(rs.getString("codigo"));
				menu.setIndice(rs.getString("indice"));

				menu.setTipo(rs.getString("tipo"));
				menu.setAtivo(rs.getBoolean("ativo"));

				menu.setDiretorio(rs.getString("diretorio"));
				menu.setDescPagina(rs.getString("desc_pagina"));
				menu.setExtensao(rs.getString("extensao"));

				if (rs.getString("tipo").equals("menuItem")) {
					menu.setUrl("/pages/" + menu.getDiretorio() + "/"
							+ menu.getDescPagina() + menu.getExtensao());
				}
				menu.setIndiceAux(rs.getString("codigo"));

				menu.setIdSistema(rs.getInt("id_sis"));
				menu.setDescSistema(rs.getString("desc_sis"));
				menu.setSiglaSistema(rs.getString("sigla_sis").toUpperCase());
				lista.add(menu);
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

	public ArrayList<Menu> listarMenuItemSourcerEditUser(Integer idPerfil,
			Integer idUsuario) throws ProjetoException {

		String sql = "select me.id, me.descricao, me.codigo, me.indice, me.tipo, "
				+ "me.ativo, diretorio, desc_pagina, extensao, si.id as id_sis, "
				+ "si.descricao as desc_sis, si.sigla as sigla_sis from acl.menu me "
				+ "join acl.perm_geral pg on pg.id_menu = me.id "
				+ "join acl.permissao pm on pm.id = pg.id_permissao "
				+ "join acl.menu_sistema ms on ms.id_menu = me.id "
				+ "join acl.sistema si on si.id = ms.id_sistema "
				+ "where me.id not in("
				+ "	select me.id from acl.perm_perfil pp "
				+ "	join acl.perfil pf on pf.id = pp.id_perfil "
				+ "	join acl.permissao pm on pm.id = pp.id_permissao "
				+ "	join acl.perm_geral pg on pg.id_permissao = pm.id "
				+ "	join acl.menu me on me.id = pg.id_menu "
				+ "	join acl.menu_sistema ms on ms.id_menu = me.id "
				+ "	join acl.sistema si on si.id = ms.id_sistema "
				+ "	where (me.tipo = 'menuItem') and pf.id = ?"
				+ "	union"
				+ "	select me.id from acl.perm_usuario pu "
				+ "	join acl.permissao pm on pm.id = pu.id_permissao "
				+ "	join acl.perm_geral pg on pg.id_permissao = pm.id "
				+ "	join acl.menu me on me.id = pg.id_menu "
				+ "	join acl.menu_sistema ms on ms.id_menu = me.id "
				+ "	join acl.sistema si on si.id = ms.id_sistema "
				+ "	where (me.tipo = 'menuItem') and pu.id_usuario = ?"
				+ ") order by me.descricao;";

		ArrayList<Menu> lista = new ArrayList<>();
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setInt(1, idPerfil);
			stmt.setInt(2, idUsuario);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Menu menu = new Menu();
				menu.setId(rs.getLong("id"));
				menu.setDescricao(rs.getString("descricao"));
				menu.setCodigo(rs.getString("codigo"));
				menu.setIndice(rs.getString("indice"));

				menu.setTipo(rs.getString("tipo"));
				menu.setAtivo(rs.getBoolean("ativo"));

				menu.setDiretorio(rs.getString("diretorio"));
				menu.setDescPagina(rs.getString("desc_pagina"));
				menu.setExtensao(rs.getString("extensao"));

				if (rs.getString("tipo").equals("menuItem")) {
					menu.setUrl("/pages/" + menu.getDiretorio() + "/"
							+ menu.getDescPagina() + menu.getExtensao());
				}
				menu.setIndiceAux(rs.getString("codigo"));

				menu.setIdSistema(rs.getInt("id_sis"));
				menu.setDescSistema(rs.getString("desc_sis"));
				menu.setSiglaSistema(rs.getString("sigla_sis").toUpperCase());
				lista.add(menu);
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

	public ArrayList<Menu> listarMenuItemTargetEditUser(Long idUsuario)
			throws ProjetoException {

		String sql = "select me.id, me.descricao, me.codigo, me.indice, me.tipo, "
				+ "me.ativo, diretorio, desc_pagina, extensao, si.id as id_sis, "
				+ "si.descricao as desc_sis, si.sigla as sigla_sis from acl.perm_usuario pu "
				+ "join acl.permissao pm on pm.id = pu.id_permissao "
				+ "join acl.perm_geral pg on pg.id_permissao = pm.id "
				+ "join acl.menu me on me.id = pg.id_menu "
				+ "join acl.menu_sistema ms on ms.id_menu = me.id "
				+ "join acl.sistema si on si.id = ms.id_sistema "
				+ "where (me.tipo = 'menuItem') and pu.id_usuario = ?";

		ArrayList<Menu> lista = new ArrayList<>();
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setLong(1, idUsuario);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Menu menu = new Menu();
				menu.setId(rs.getLong("id"));
				menu.setDescricao(rs.getString("descricao"));
				menu.setCodigo(rs.getString("codigo"));
				menu.setIndice(rs.getString("indice"));
				menu.setTipo(rs.getString("tipo"));
				menu.setAtivo(rs.getBoolean("ativo"));

				menu.setDiretorio(rs.getString("diretorio"));
				menu.setDescPagina(rs.getString("desc_pagina"));
				menu.setExtensao(rs.getString("extensao"));

				if (rs.getString("tipo").equals("menuItem")) {
					menu.setUrl("/pages/" + menu.getDiretorio() + "/"
							+ menu.getDescPagina() + menu.getExtensao());
				}
				menu.setIndiceAux(rs.getString("codigo"));

				menu.setIdSistema(rs.getInt("id_sis"));
				menu.setDescSistema(rs.getString("desc_sis"));
				menu.setSiglaSistema(rs.getString("sigla_sis").toUpperCase());
				lista.add(menu);
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

	public ArrayList<Menu> listarMenusPerfil(Long idPerfil)
			throws ProjetoException {

		String sql = "select distinct me.id, me.descricao, me.codigo, me.indice, me.tipo, "
				+ "me.ativo, diretorio, desc_pagina, extensao, si.id as id_sis, "
				+ "si.descricao as desc_sis, si.sigla as sigla_sis from acl.perm_perfil pp "
				+ "join acl.permissao pm on pm.id = pp.id_permissao "
				+ "join acl.perm_geral pg on pg.id_permissao = pm.id "
				+ "join acl.menu me on me.id = pg.id_menu "
				+ "join acl.menu_sistema ms on ms.id_menu = me.id "
				+ "join acl.sistema si on si.id = ms.id_sistema "
				+ "where pp.id_perfil = ? order by me.descricao";

		ArrayList<Menu> lista = new ArrayList<>();
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setLong(1, idPerfil);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Menu menu = new Menu();
				menu.setId(rs.getLong("id"));
				menu.setDescricao(rs.getString("descricao"));
				menu.setCodigo(rs.getString("codigo"));
				menu.setIndice(rs.getString("indice"));
				menu.setTipo(rs.getString("tipo"));
				menu.setAtivo(rs.getBoolean("ativo"));

				menu.setDiretorio(rs.getString("diretorio"));
				menu.setDescPagina(rs.getString("desc_pagina"));
				menu.setExtensao(rs.getString("extensao"));

				if (rs.getString("tipo").equals("menuItem")) {
					menu.setUrl("/pages/" + menu.getDiretorio() + "/"
							+ menu.getDescPagina() + menu.getExtensao());

				}
				menu.setIndiceAux(rs.getString("codigo"));

				menu.setIdSistema(rs.getInt("id_sis"));
				menu.setDescSistema(rs.getString("desc_sis"));
				menu.setSiglaSistema(rs.getString("sigla_sis").toUpperCase());
				lista.add(menu);
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