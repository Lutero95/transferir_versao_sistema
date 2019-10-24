package br.gov.al.maceio.sishosp.acl.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.acl.model.Menu;
import br.gov.al.maceio.sishosp.acl.model.Permissao;
import br.gov.al.maceio.sishosp.acl.model.Permissoes;
import br.gov.al.maceio.sishosp.acl.model.Sistema;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactoryPublico;
import br.gov.al.maceio.sishosp.comum.util.SessionUtil;
import br.gov.al.maceio.sishosp.hosp.dao.CboDAO;
import br.gov.al.maceio.sishosp.hosp.dao.EspecialidadeDAO;
import br.gov.al.maceio.sishosp.hosp.dao.GrupoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.ProcedimentoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.ProgramaDAO;
import br.gov.al.maceio.sishosp.hosp.enums.ValidacaoSenha;
import br.gov.al.maceio.sishosp.hosp.model.CboBean;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;
import br.gov.al.maceio.sishosp.hosp.model.UnidadeBean;

public class FuncionarioDAO {

	private Connection con = null;
	private PreparedStatement ps = null;
	private EspecialidadeDAO espDao = new EspecialidadeDAO();
	private ProgramaDAO progDao = new ProgramaDAO();
	private CboDAO cDao = new CboDAO();
	private GrupoDAO gDao = new GrupoDAO();
	private ProcedimentoDAO procDao = new ProcedimentoDAO();
	FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
			.getSessionMap().get("obj_usuario");

	public String autenticarUsuarioInicialNomeBancoAcesso(FuncionarioBean usuario) throws ProjetoException {

		String sql = "SELECT banco_acesso FROM acl.funcionarios WHERE (cpf = ?) AND ((senha) = ?) and ativo = 'S' ";

		String nomeBancoAcesso = null;

		try {
			con = ConnectionFactoryPublico.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setString(1, usuario.getCpf().replaceAll("[^0-9]", ""));
			pstmt.setString(2, usuario.getSenha());

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				nomeBancoAcesso = rs.getString("banco_acesso");
			}

			SessionUtil.adicionarNaSessao(nomeBancoAcesso, "nomeBancoAcesso");

			return nomeBancoAcesso;

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public FuncionarioBean autenticarUsuario(FuncionarioBean usuario) throws ProjetoException {

		String sql = "select us.id_funcionario, us.descfuncionario, us.senha, us.email, permite_liberacao, permite_encaixe, "
				+ "pf.descricao as descperfil, us.codunidade, p.tipo_atendimento_terapia,  case when us.ativo = 'S' "
				+ "then true else false end as usuarioativo, p.opcao_atendimento, "
				+ "pf.id as idperfil, u.id codunidade,u.nome nomeunidade, e.nome_principal, e.nome_fantasia, e.cod_empresa  from acl.funcionarios us "
				+ "join acl.perfil pf on (pf.id = us.id_perfil) "
				+ " left join hosp.parametro p ON (p.codunidade = us.codunidade) "
				+ " join hosp.unidade u on u.id = us.codunidade "
				+ " join hosp.empresa e on e.cod_empresa = u.cod_empresa "
				+ "where (us.cpf = ?) and ((us.senha) = ?) and us.ativo = 'S'";

		FuncionarioBean ub = null;
		int count = 1;
		String setoresUsuario = "";

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setString(1, usuario.getCpf().replaceAll("[^0-9]", ""));
			pstmt.setString(2, usuario.getSenha());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				ub = new FuncionarioBean();
				ub.setId(rs.getLong("id_funcionario"));
				ub.setNome(rs.getString("descfuncionario"));
				ub.setSenha(rs.getString("senha"));
				ub.setEmail(rs.getString("email"));
				ub.getUnidade().setId(rs.getInt("codunidade"));
				ub.getUnidade().setCodEmpresa(rs.getInt("cod_empresa"));
				ub.getUnidade().setNomeUnidade(rs.getString("nomeunidade"));
				ub.getUnidade().setNomeEmpresa(rs.getString("nome_principal"));
				ub.getUnidade().setNomeFantasia(rs.getString("nome_fantasia"));
				ub.setRealizaLiberacoes(rs.getBoolean("permite_liberacao"));
				ub.setRealizaEncaixes(rs.getBoolean("permite_encaixe"));
				ub.getUnidade().getParametro().getTipoAtendimento().setIdTipo(rs.getInt("tipo_atendimento_terapia"));
				ub.getUnidade().getParametro().setOpcaoAtendimento(rs.getString("opcao_atendimento"));

				// ACL
				ub.setId(rs.getLong("id_funcionario"));
				ub.setUsuarioAtivo(rs.getBoolean("usuarioativo"));
				ub.getPerfil().setId(rs.getLong("idperfil"));
				ub.getPerfil().setDescricao(rs.getString("descperfil"));

				count++;
			}

			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("obj_funcionario", ub);

			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("setores_usuario",
					setoresUsuario);
			return ub;

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public List<Sistema> carregarSistemasUsuario(FuncionarioBean usuario) throws ProjetoException {

		String sql = "select si.id, si.descricao, si.sigla, si.url, si.imagem, "
				+ "si.versao, si.ativo from acl.perm_sistema ps " + "join acl.sistema si on si.id = ps.id_sistema "
				+ "join acl.funcionarios fuc on fuc.id_funcionario = ps.id_usuario "
				+ "where fuc.id_funcionario = ? and si.ativo = true order by si.descricao";

		List<Sistema> listaSistemas = new ArrayList<>();

		Connection con = null;
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setLong(1, usuario.getId());
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Sistema s = new Sistema();
				s.setId(rs.getInt("id"));
				s.setDescricao(rs.getString("descricao"));
				s.setSigla(rs.getString("sigla"));
				s.setUrl(rs.getString("url") + "?faces-redirect=true");
				s.setImagem(rs.getString("imagem"));
				s.setVersao(rs.getString("versao"));
				s.setAtivo(rs.getBoolean("ativo"));
				listaSistemas.add(s);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return listaSistemas;
	}

	public List<Permissoes> carregarPermissoes(FuncionarioBean u) throws ProjetoException {

		String sql = "select  si.id as sid, si.descricao as sdesc, pf.descricao pfdesc, "
				+ "pm.id as pmid, pm.descricao as pmdesc, 0 as funid, '' as fundesc, '' as funcodigo, "
				+ " false as funativa, 0 as funidsis, me.id as meid, me.descricao as medesc, me.desc_pagina, "
				+ "me.diretorio, me.extensao, me.codigo as cod_menu, me.indice, me.tipo, me.ativo as meativo, "
				+ "me.action_rel, me.onclick_rel from acl.perm_perfil pp "
				+ "join acl.funcionarios us on us.id_perfil = pp.id_perfil "
				+ "join acl.perfil pf on pf.id = pp.id_perfil " + "join acl.permissao pm on pm.id = pp.id_permissao "
				+ "join acl.perm_geral pg on pg.id_permissao = pm.id " + "join acl.menu me on me.id = pg.id_menu "
				+ "join acl.menu_sistema ms on ms.id_menu = me.id "
				+ "join acl.sistema si on si.id = ms.id_sistema where us.id_funcionario = ? and me.ativo = true "
				+ "union select si.id as sid, si.descricao as sdesc, pf.descricao pfdesc, "
				+ "pm.id pmid, pm.descricao as pmdesc, fun.id as funid, fun.descricao as fundesc, "
				+ "fun.codigo as funcodigo, fun.ativa as funativa, fun.id_sistema as fusidsis, 0 as meid, "
				+ "'' as medesc, '', '', '', '' as cod_menu, '', case when '' = '' then 'funcao' end, "
				+ "false as meativo, '', '' from acl.perm_perfil pp "
				+ "join acl.funcionarios us on us.id_perfil = pp.id_perfil "
				+ "join acl.perfil pf on pf.id = pp.id_perfil " + "join acl.permissao pm on pm.id = pp.id_permissao "
				+ "join acl.perm_geral pg on pg.id_permissao = pm.id " + "join acl.funcao fun on fun.id = pg.id_funcao "
				+ "join acl.sistema si on si.id = fun.id_sistema where us.id_funcionario = ? and fun.ativa = true "
				+ "union select si.id as sid, si.descricao as sdesc, '' as pfdesc, "
				+ "pm.id as pmid, pm.descricao as pmdesc, 0 as funid, '' as fundesc, '' as funcodigo, "
				+ "false as funativa, 0 as funidsis, me.id as meid, me.descricao as medesc, me.desc_pagina, "
				+ "me.diretorio, me.extensao, me.codigo as cod_menu, me.indice, me.tipo, me.ativo as meativo, "
				+ "me.action_rel, me.onclick_rel from acl.perm_usuario pu "
				+ "join acl.funcionarios us on us.id_funcionario = pu.id_usuario "
				+ "join acl.permissao pm on pm.id = pu.id_permissao "
				+ "join acl.perm_geral pg on pg.id_permissao = pm.id " + "join acl.menu me on me.id = pg.id_menu "
				+ "join acl.menu_sistema ms on ms.id_menu = me.id "
				+ "join acl.sistema si on si.id = ms.id_sistema where us.id_funcionario = ? and me.ativo = true "
				+ "union select   si.id as sid, si.descricao as sdesc, '', "
				+ "pm.id pmid, pm.descricao as pmdesc, fun.id as funid, fun.descricao as fundesc, "
				+ "fun.codigo as funcodigo, fun.ativa as funativa, fun.id_sistema as funidsis, 0 as meid, "
				+ "'' as medesc, '', '', '', '' as cod_menu, '', case when '' = '' then 'funcao' end, "
				+ "false as meativo, '', ''  from acl.perm_usuario pu "
				+ "join acl.funcionarios us on us.id_funcionario = pu.id_usuario "
				+ "join acl.permissao pm on pm.id = pu.id_permissao "
				+ "join acl.perm_geral pg on pg.id_permissao = pm.id " + "join acl.funcao fun on fun.id = pg.id_funcao "
				+ "join acl.sistema si on si.id = fun.id_sistema where us.id_funcionario = ? and fun.ativa = true "
				+ "order by medesc, sid";

		List<Permissoes> lista = new ArrayList<>();
		// FacesContext fc = FacesContext.getCurrentInstance();
		// HttpServletRequest request = (HttpServletRequest)
		// fc.getExternalContext().getRequest();
		// String contextPath = request.getServletContext().getContextPath();
		Connection con = null;
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setLong(1, u.getId());
			stmt.setLong(2, u.getId());
			stmt.setLong(3, u.getId());
			stmt.setLong(4, u.getId());
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Menu m = new Menu();
				m.setId(rs.getLong("meid"));
				m.setDescricao(rs.getString("medesc"));
				m.setCodigo(rs.getString("cod_menu"));
				m.setIndice(rs.getString("indice"));
				m.setTipo(rs.getString("tipo"));
				m.setAtivo(rs.getBoolean("meativo"));
				m.setDiretorio(rs.getString("diretorio"));
				m.setDescPagina(rs.getString("desc_pagina"));
				m.setExtensao(rs.getString("extensao"));
				m.setAction(rs.getString("action_rel"));
				m.setOnclick(rs.getString("onclick_rel"));

				if ((rs.getString("tipo").equals("menuItem")) || (rs.getString("tipo").equals("rotinaInterna"))) {
					// contextPath
					m.setUrl("/pages/" + m.getDiretorio() + "/" + m.getDescPagina() + m.getExtensao());
				}

				if (rs.getString("funativa").equals("t")) {
					m.setAtivo(true);
				}

				Permissoes p = new Permissoes();
				p.setIdSistema(rs.getInt("sid"));
				p.setDescSistema(rs.getString("sdesc"));
				p.setDescPerfil(rs.getString("pfdesc"));
				p.setIdPermissao(rs.getInt("pmid"));
				p.setDescPermissao(rs.getString("pmdesc"));
				p.setMenu(m);

				p.setCodigoFuncao(rs.getString("funcodigo"));
				p.setFuncaoAtiva(rs.getBoolean("funativa"));
				p.setIdSistemaFunc(rs.getInt("funidsis"));

				lista.add(p);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return lista;
	}

	public Boolean alterar(FuncionarioBean usuario) {
		boolean retorno = false;
		String sql = "update acl.funcionario set descfuncionario = ?, cpf = ?, email = ?, "
				+ "senha = ?, id_perfil = ?, ativo = ?, codunidade = ? where id_funcionario = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, usuario.getNome());
			stmt.setString(2, usuario.getCpf().replaceAll("[^0-9]", ""));
			stmt.setString(3, usuario.getEmail());
			stmt.setString(4, usuario.getSenha());
			stmt.setLong(5, usuario.getPerfil().getId());
			stmt.setString(6, usuario.getAtivo());
			stmt.setInt(7, usuario.getUnidade().getId());
			stmt.setLong(8, usuario.getId());
			stmt.executeUpdate();

			FuncionarioBean u = usuario;

			sql = "delete from acl.perm_sistema where id_usuario = ?";
			stmt = con.prepareStatement(sql);
			stmt.setLong(1, usuario.getId());
			stmt.execute();

			sql = "insert into acl.perm_sistema (id_usuario, id_sistema) values (?, ?)";
			List<Integer> listaId = usuario.getListaIdSistemas();
			stmt = con.prepareStatement(sql);
			for (Integer idSistema : listaId) {
				stmt.setLong(1, usuario.getId());
				stmt.setInt(2, idSistema);
				stmt.execute();
			}

			sql = "delete from acl.perm_usuario where id_usuario = ?";
			stmt = con.prepareStatement(sql);
			stmt.setLong(1, usuario.getId());
			stmt.execute();

			sql = "insert into acl.perm_usuario (id_usuario, id_permissao) values (?, ?)";
			List<Long> listaPerm = usuario.getListaIdPermissoes();
			stmt = con.prepareStatement(sql);
			for (Long idPerm : listaPerm) {
				stmt.setLong(1, usuario.getId());
				stmt.setLong(2, idPerm);
				stmt.execute();
			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return retorno;
		}
	}

	public Boolean alterarSemPerm(FuncionarioBean usuario) {
		boolean retorno = false;
		String sql = " update acl.funcionarios set descfuncionario = ?, cpf = ?, email = ?, "
				+ "senha = ?, id_perfil = ?, ativo = ? where id_funcionario = ? ";

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, usuario.getNome());
			stmt.setString(2, usuario.getCpf().replaceAll("[^0-9]", ""));
			stmt.setString(3, usuario.getEmail());
			stmt.setString(4, usuario.getSenha());
			stmt.setLong(5, usuario.getPerfil().getId());
			stmt.setString(6, usuario.getAtivo());
			stmt.setLong(7, usuario.getId());

			stmt.executeUpdate();
			FuncionarioBean u = usuario;

			boolean associouSis = false;

			if (u.getId() != null) {
				sql = "delete from acl.perm_sistema where id_usuario = ?";
				String sql2 = "insert into acl.perm_sistema (id_usuario, id_sistema) values (?, ?)";
				List<Integer> listaId = usuario.getListaIdSistemas();
				PreparedStatement stmt2;
				try {
					stmt2 = con.prepareStatement(sql);
					stmt2.setLong(1, usuario.getId());
					stmt2.execute();
				} catch (Exception ex) {
					ex.getMessage();
					throw new RuntimeException(ex);
				}
				if (!usuario.getListaIdSistemas().isEmpty()) {
					try {
						stmt2 = con.prepareStatement(sql2);
						for (Integer idSistema : listaId) {
							stmt2.setLong(1, usuario.getId());
							stmt2.setInt(2, idSistema);
							stmt2.execute();
						}
						associouSis = true;
					} catch (Exception ex) {
						throw new RuntimeException(ex);
					}
				}
			}

			boolean associouPerm = false;
			if (associouSis == true) {
				sql = "delete from acl.perm_usuario where id_usuario = ?";
				try {
					PreparedStatement stmt3 = con.prepareStatement(sql);
					stmt3.setLong(1, usuario.getId());
					stmt3.execute();
					associouPerm = true;
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				}
			}

			con.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return retorno;
		}
	}

	public ArrayList<Menu> listarMenuItemSourcerEditUser(Long idPerfil, Integer idUsuario)
			throws ProjetoException, SQLException {

		String sql = "select me.id, me.descricao, me.codigo, me.indice, me.tipo, "
				+ "me.ativo, diretorio, desc_pagina, extensao, si.id as id_sis, "
				+ "si.descricao as desc_sis, si.sigla as sigla_sis from acl.menu me "
				+ "join acl.perm_geral pg on pg.id_menu = me.id " + "join acl.permissao pm on pm.id = pg.id_permissao "
				+ "join acl.menu_sistema ms on ms.id_menu = me.id " + "join acl.sistema si on si.id = ms.id_sistema "
				+ "where me.id not in(" + "	select me.id from acl.perm_perfil pp "
				+ "	join acl.perfil pf on pf.id = pp.id_perfil "
				+ "	join acl.permissao pm on pm.id = pp.id_permissao "
				+ "	join acl.perm_geral pg on pg.id_permissao = pm.id " + "	join acl.menu me on me.id = pg.id_menu "
				+ "	join acl.menu_sistema ms on ms.id_menu = me.id "
				+ "	join acl.sistema si on si.id = ms.id_sistema "
				+ "	where (me.tipo = 'menuItem' or me.tipo = 'menuItemRel') and pf.id = ? ) " + " AND me.id NOT IN ( "
				+ " select me.id " + " from acl.perm_usuario pu "
				+ " LEFT join acl.permissao pm on pm.id = pu.id_permissao "
				+ " LEFT join acl.perm_geral pg on pg.id_permissao = pm.id "
				+ " LEFT join acl.menu me on me.id = pg.id_menu "
				+ " where (me.tipo = 'menuItem' or me.tipo = 'menuItemRel') and pu.id_usuario = ? )"
				+ " order by me.descricao";

		ArrayList<Menu> lista = new ArrayList<Menu>();
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setLong(1, idPerfil);
			stmt.setInt(2, idUsuario);

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Menu m = new Menu();
				m.setId(rs.getLong("id"));
				m.setDescricao(rs.getString("descricao"));
				m.setCodigo(rs.getString("codigo"));
				m.setIndice(rs.getString("indice"));

				m.setTipo(rs.getString("tipo"));
				m.setAtivo(rs.getBoolean("ativo"));

				m.setDiretorio(rs.getString("diretorio"));
				m.setDescPagina(rs.getString("desc_pagina"));
				m.setExtensao(rs.getString("extensao"));

				if ((rs.getString("tipo").equals("menuItem")) || (rs.getString("tipo").equals("rotinaInterna"))) {
					m.setUrl("/pages/" + m.getDiretorio() + "/" + m.getDescPagina() + m.getExtensao());
				}
				m.setIndiceAux(rs.getString("codigo"));

				m.setIdSistema(rs.getInt("id_sis"));
				m.setDescSistema(rs.getString("desc_sis"));
				m.setSiglaSistema(rs.getString("sigla_sis").toUpperCase());
				lista.add(m);
			}

			rs.close();
			stmt.close();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			con.close();
		}
		return lista;
	}

	public ArrayList<Menu> listarMenuItemTargetEditUser(Integer idUsuario) throws ProjetoException, SQLException {

		String sql = "select me.id, me.descricao, me.codigo, me.indice, me.tipo, "
				+ "me.ativo, me.diretorio, me.desc_pagina, me.extensao, si.id as id_sis, "
				+ "si.descricao as desc_sis, si.sigla as sigla_sis from acl.perm_usuario pu "
				+ "join acl.permissao pm on pm.id = pu.id_permissao "
				+ "join acl.perm_geral pg on pg.id_permissao = pm.id " + "join acl.menu me on me.id = pg.id_menu "
				+ "join acl.menu_sistema ms on ms.id_menu = me.id " + "join acl.sistema si on si.id = ms.id_sistema "
				+ "where pu.id_usuario = ? ";

		/* + " and (me.tipo = 'menuItem' or me.tipo = 'menuItemRel') " */

		ArrayList<Menu> lista = new ArrayList<Menu>();
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, idUsuario);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Menu m = new Menu();
				m.setId(rs.getLong("id"));
				m.setDescricao(rs.getString("descricao"));
				m.setCodigo(rs.getString("codigo"));
				m.setIndice(rs.getString("indice"));
				m.setTipo(rs.getString("tipo"));
				m.setAtivo(rs.getBoolean("ativo"));

				m.setDiretorio(rs.getString("diretorio"));
				m.setDescPagina(rs.getString("desc_pagina"));
				m.setExtensao(rs.getString("extensao"));

				if ((rs.getString("tipo").equals("menuItem")) || (rs.getString("tipo").equals("rotinaInterna"))) {
					m.setUrl("/pages/" + m.getDiretorio() + "/" + m.getDescPagina() + m.getExtensao());
				}
				m.setIndiceAux(rs.getString("codigo"));

				m.setIdSistema(rs.getInt("id_sis"));
				m.setDescSistema(rs.getString("desc_sis"));
				m.setSiglaSistema(rs.getString("sigla_sis").toUpperCase());
				lista.add(m);
			}

			rs.close();
			stmt.close();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			con.close();
		}
		return lista;
	}

	public ArrayList<FuncionarioBean> buscaUsuarios() throws ProjetoException {

		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().get("obj_funcionario");

		String sql = "select * from acl.funcionarios u where codunidade = ? order by ativo,descfuncionario";

		ArrayList<FuncionarioBean> lista = new ArrayList();

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, user_session.getUnidade().getId());
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				FuncionarioBean n = new FuncionarioBean();
				n = new FuncionarioBean();
				n.setId(rs.getLong("id_funcionario"));
				n.setNome(rs.getString("descfuncionario"));
				n.setSenha(rs.getString("senha"));
				n.setAtivo(rs.getString("ativo"));
				n.setEmail(rs.getString("email"));
				n.setCpf(rs.getString("cpf"));
				n.getPerfil().setId(rs.getLong("id_perfil"));

				lista.add(n);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return lista;
	}

	public ArrayList<Permissao> listarPermSemPerfSource(Integer id) throws ProjetoException {

		String sql = "select p.id, p.descricao from acl.permissao p "
				+ "where p.id not in (select pm.id from acl.perfil pf "
				+ "join acl.perm_perfil pp on pf.id = pp.id_perfil "
				+ "join acl.permissao pm on pm.id = pp.id_permissao " + "where pf.id = ?) order by 2";

		ArrayList<Permissao> lista = new ArrayList<>();

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Permissao p = new Permissao();
				p.setId(rs.getLong("id"));
				p.setDescricao(rs.getString("descricao"));
				lista.add(p);
			}
			rs.close();
			stmt.close();
			con.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return lista;
	}

	public ArrayList<Permissao> listarPermSemPerfAssSource(Integer idPefil, Integer idUsuario) throws ProjetoException {

		String sql = "select id, descricao from acl.permissao where id not in (" + "select pm.id from acl.perfil pf "
				+ "join acl.perm_perfil pp on pf.id = pp.id_perfil "
				+ "join acl.permissao pm on pm.id = pp.id_permissao " + "where pf.id = ? "
				+ "union select p.id from acl.funcionarios u "
				+ "join acl.perm_usuario pu on u.id_funcionario = pu.id_usuario "
				+ "join acl.permissao p on p.id = pu.id_permissao "
				+ "where u.id_funcionario = ? order by 1) order by 2";

		ArrayList<Permissao> lista = new ArrayList<>();

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, idPefil);
			stmt.setInt(2, idUsuario);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Permissao p = new Permissao();
				p.setId(rs.getLong("id"));
				p.setDescricao(rs.getString("descricao"));
				lista.add(p);
			}
			rs.close();
			stmt.close();
			con.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return lista;
	}

	public ArrayList<Permissao> listarPermSemPerfAssTarget(Integer id) throws ProjetoException {

		String sql = "select p.id, p.descricao from acl.funcionarios u "
				+ "join acl.perm_usuario pu on u.id_funcionario = pu.id_usuario "
				+ "join acl.permissao p on p.id = pu.id_permissao " + "where u.id_funcionario = ? order by 2";

		ArrayList<Permissao> lista = new ArrayList<>();

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Permissao p = new Permissao();
				p.setId(rs.getLong("id"));
				p.setDescricao(rs.getString("descricao"));
				lista.add(p);
			}
			rs.close();
			stmt.close();
			con.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return lista;
	}

	public Boolean alterarSenha(FuncionarioBean usuario) {

		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().get("obj_funcionario");

		Boolean retorno = false;

		String sql = "update acl.funcionarios set senha = ? where id_funcionario = ? ";

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, usuario.getNovaSenha());
			stmt.setLong(2, user_session.getId());
			stmt.executeUpdate();
			con.commit();

			retorno = true;

			if (retorno) {
				retorno = alterarSenhaBancoPublico(usuario);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return retorno;
		}
	}

	public Boolean alterarSenhaBancoPublico(FuncionarioBean usuario) {

		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().get("obj_funcionario");

		Boolean retorno = false;

		String sql = "update acl.funcionarios set senha = ?, cpf=? where id_funcionario = ? and banco_acesso=?";

		try {
			con = ConnectionFactoryPublico.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, usuario.getNovaSenha());
			stmt.setString(2, usuario.getCpf());
			stmt.setLong(3, user_session.getId());
			stmt.setString(4, (String) SessionUtil.resgatarDaSessao("nomeBancoAcesso"));
			stmt.executeUpdate();
			con.commit();

			retorno = true;

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return retorno;
		}
	}

	public String verificaLoginCadastrado(String login) throws ProjetoException {
		PreparedStatement ps = null;
		Connection con = ConnectionFactory.getConnection();
		String isExist = "N";

		try {
			String sql = "select * from acl.funcionarios where login=?";
			ps = con.prepareStatement(sql);
			ps.setString(1, login.toUpperCase());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				isExist = "S";
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return isExist;
		}
	}

	public String verificaUserCadastrado(String cpf) throws ProjetoException {
		PreparedStatement ps = null;
		Connection con = ConnectionFactory.getConnection();
		String isExist = "N";

		try {
			String sql = "select * from acl.funcionarios where cpf=? and ativo=?";

			ps = con.prepareStatement(sql);
			ps.setString(1, cpf);
			ps.setString(2, "S");

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				isExist = "S";
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return isExist;
		}
	}

	// INICIO PROFISSIONALDAO

	public boolean gravarProfissional(FuncionarioBean profissional, ArrayList<ProgramaBean> lista) {

		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().get("obj_funcionario");

		Boolean retorno = false;

		String sql = "INSERT INTO acl.funcionarios(descfuncionario, cpf, senha, log_user, codespecialidade, cns, codcbo, "
				+ " codprocedimentopadrao, ativo, realiza_atendimento, datacriacao, primeiroacesso, id_perfil, codunidade, permite_liberacao, permite_encaixe) "
				+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, current_timestamp, false, ?, ?, ?, ?) returning id_funcionario;";
		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);

			ps.setString(1, profissional.getNome().toUpperCase());

			ps.setString(2, profissional.getCpf().replaceAll("[^0-9]", ""));

			ps.setString(3, profissional.getSenha());

			ps.setLong(4, user_session.getId());

			if (profissional.getEspecialidade() != null) {
				if (profissional.getEspecialidade().getCodEspecialidade() != null) {
					ps.setInt(5, profissional.getEspecialidade().getCodEspecialidade());
				} else {
					ps.setNull(5, Types.INTEGER);
				}
			} else {
				ps.setNull(5, Types.INTEGER);
			}

			if (profissional.getCns() == null) {
				ps.setNull(6, Types.NULL);
			} else {
				ps.setString(6, profissional.getCns().toUpperCase());
			}

			if (profissional.getCbo() != null) {
				if (profissional.getCbo().getCodCbo() == null) {
					ps.setNull(7, Types.NULL);
				} else {
					ps.setInt(7, profissional.getCbo().getCodCbo());
				}
			} else {
				ps.setNull(7, Types.NULL);
			}

			if (profissional.getProc1() != null) {
				if (profissional.getProc1().getIdProc() == null) {
					ps.setNull(8, Types.NULL);
				} else {
					ps.setInt(8, profissional.getProc1().getIdProc());
				}
			} else {
				ps.setNull(8, Types.NULL);
			}

			ps.setString(9, profissional.getAtivo());

			ps.setBoolean(10, profissional.getRealizaAtendimento());

			ps.setLong(11, profissional.getPerfil().getId());

			ps.setInt(12, profissional.getUnidade().getId());

			ps.setBoolean(13, profissional.getRealizaLiberacoes());

			ps.setBoolean(14, profissional.getRealizaEncaixes());

			ResultSet rs = ps.executeQuery();

			int idProf = 0;
			if (rs.next()) {
				idProf = rs.getInt("id_funcionario");
			}

			for (int i = 0; i < profissional.getListaUnidades().size(); i++) {
				String sql2 = "INSERT INTO hosp.funcionario_unidades (cod_unidade, cod_funcionario) VALUES (?, ?);";
				ps = con.prepareStatement(sql2);

				ps.setInt(1, profissional.getListaUnidades().get(i).getId());
				ps.setInt(2, idProf);

				ps.executeUpdate();
			}

			for (int i = 0; i < lista.size(); i++) {
				String sql3 = "INSERT INTO hosp.profissional_programa_grupo(codprofissional, codprograma, codgrupo) VALUES (?, ?, ?);";
				ps = con.prepareStatement(sql3);

				ps.setInt(1, idProf);
				ps.setInt(2, lista.get(i).getIdPrograma());
				ps.setInt(3, lista.get(i).getGrupoBean().getIdGrupo());

				ps.executeUpdate();
			}

			String sql4 = "insert into acl.perm_usuario (id_usuario, id_permissao) values (?, ?)";
			List<Long> listaPerm = profissional.getListaIdPermissoes();
			ps = con.prepareStatement(sql4);
			for (Long idPerm : listaPerm) {
				ps.setLong(1, idProf);
				ps.setLong(2, idPerm);
				ps.execute();
			}

			String sql5 = "insert into acl.perm_sistema (id_usuario, id_sistema) values (?, ?)";
			List<Integer> listaIdSistemas = profissional.getListaIdSistemas();
			ps = con.prepareStatement(sql5);
			for (Integer idSistema : listaIdSistemas) {
				ps.setLong(1, idProf);
				ps.setLong(2, idSistema);
				ps.execute();
			}

			retorno = gravarProfissionalBancoPublico(profissional, idProf);

			if (retorno) {
				con.commit();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return retorno;
		}
	}

	public boolean gravarProfissionalBancoPublico(FuncionarioBean profissional, Integer idProfissional) {

		Boolean retorno = false;
		Connection conexaoPublica = null;

		String sql = "INSERT INTO acl.funcionarios(id_funcionario, cpf, senha, ativo, banco_acesso, admin, datacriacao, descfuncionario) "
				+ " VALUES (?, ?, ?, ?, ?, false, current_timestamp,?);";
		try {
			conexaoPublica = ConnectionFactoryPublico.getConnection();
			ps = conexaoPublica.prepareStatement(sql);

			ps.setInt(1, idProfissional);

			ps.setString(2, profissional.getCpf().replaceAll("[^0-9]", ""));

			ps.setString(3, profissional.getSenha());

			ps.setString(4, "S");

			ps.setString(5, (String) SessionUtil.resgatarDaSessao("nomeBancoAcesso"));

			ps.setString(6, profissional.getNome());

			ps.execute();

			conexaoPublica.commit();
			retorno = true;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				conexaoPublica.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return retorno;
		}
	}

	public List<FuncionarioBean> listarProfissionalBusca(String descricaoBusca, Integer tipoBuscar)
			throws ProjetoException {

		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().get("obj_funcionario");

		List<FuncionarioBean> lista = new ArrayList<>();
		String sql = "SELECT f.id_funcionario, f.id_funcionario ||'-'|| f.descfuncionario AS descfuncionario, f.codespecialidade, e.descespecialidade, "
				+ "f.cns, f.ativo, f.codcbo, c.descricao, f.codprocedimentopadrao, p.nome, f.permite_liberacao, permite_encaixe "
				+ " FROM acl.funcionarios f "
				+ "LEFT JOIN hosp.especialidade e ON (f.codespecialidade = e.id_especialidade) "
				+ "LEFT JOIN hosp.proc p ON (f.codprocedimentopadrao = p.id) "
				+ "LEFT JOIN hosp.cbo c ON (f.codcbo = c.id) ";

		if (tipoBuscar == 1) {
			sql += " where upper(f.id_funcionario ||' - '|| f.descfuncionario) LIKE ? and f.realiza_atendimento is true and f.codunidade = ? "
					+ "order by f.descfuncionario";
		}
		

		if (tipoBuscar == 2) {
			sql += " where upper(f.id_funcionario ||' - '|| f.descfuncionario) LIKE ? and f.codunidade = ? "
					+ "order by f.descfuncionario";
		}

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setString(1, "%" + descricaoBusca.toUpperCase() + "%");
			stm.setInt(2, user_session.getUnidade().getId());
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				FuncionarioBean prof = new FuncionarioBean();
				prof.setId(rs.getLong("id_funcionario"));
				prof.setNome(rs.getString("descfuncionario"));
				prof.getEspecialidade().setDescEspecialidade(rs.getString("descespecialidade"));
				prof.getEspecialidade().setCodEspecialidade(rs.getInt("codespecialidade"));
				prof.setCns(rs.getString("cns"));
				prof.setAtivo(rs.getString("ativo"));
				prof.getCbo().setDescCbo(rs.getString("descricao"));
				prof.getCbo().setCodCbo(rs.getInt("codcbo"));
				prof.getProc1().setNomeProc(rs.getString("nome"));
				prof.getProc1().setIdProc(rs.getInt("codprocedimentopadrao"));
				prof.setPrograma(listarProgProf(rs.getInt("id_funcionario")));
				prof.setGrupo(listarProgGrupo(rs.getInt("id_funcionario")));
				prof.setRealizaLiberacoes(rs.getBoolean("permite_liberacao"));
				prof.setRealizaEncaixes(rs.getBoolean("permite_encaixe"));

				lista.add(prof);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return lista;
	}

	public List<FuncionarioBean> listarProfissionalBuscaPorGrupo(String descricaoBusca, Integer codgrupo)
			throws ProjetoException {
		List<FuncionarioBean> lista = new ArrayList<>();

		StringBuilder sql = new StringBuilder();

		sql.append(
				"SELECT DISTINCT h.id_funcionario, h.id_funcionario ||'-'|| h.descfuncionario AS descfuncionario, h.codespecialidade, h.permite_liberacao, ");
		sql.append(
				"h.permite_encaixe, h.cns, h.ativo, h.codcbo, h.codprocedimentopadrao, e.descespecialidade, c.descricao AS desccbo, p.nome AS descproc ");
		sql.append("FROM acl.funcionarios h ");
		sql.append("JOIN hosp.profissional_programa_grupo ppg ON (h.id_funcionario = ppg.codprofissional)");
		sql.append("LEFT JOIN hosp.especialidade e ON (e.id_especialidade = h.codespecialidade) ");
		sql.append("LEFT JOIN hosp.cbo c ON (c.id = h.codcbo) ");
		sql.append("LEFT JOIN hosp.proc p ON (p.id = h.codprocedimentopadrao) ");
		sql.append(
				"WHERE UPPER(id_funcionario ||' - '|| descfuncionario) LIKE ? AND ppg.codgrupo = ? AND h.ativo = 'S' ");
		sql.append("AND realiza_atendimento IS TRUE ORDER BY descfuncionario ");

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql.toString());

			stm.setString(1, "%" + descricaoBusca.toUpperCase() + "%");
			stm.setInt(2, codgrupo);

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				FuncionarioBean prof = new FuncionarioBean();
				prof.setId(rs.getLong("id_funcionario"));
				prof.setNome(rs.getString("descfuncionario"));
				prof.getEspecialidade().setCodEspecialidade(rs.getInt("codespecialidade"));
				prof.getEspecialidade().setDescEspecialidade(rs.getString("descespecialidade"));
				prof.setCns(rs.getString("cns"));
				prof.setAtivo(rs.getString("ativo"));
				prof.getCbo().setCodCbo(rs.getInt("codcbo"));
				prof.getCbo().setDescCbo(rs.getString("desccbo"));
				prof.getProc1().setIdProc(rs.getInt("codprocedimentopadrao"));
				prof.getProc1().setNomeProc(rs.getString("descproc"));
				prof.setPrograma(listarProgProf(rs.getInt("id_funcionario")));
				prof.setGrupo(listarProgGrupo(rs.getInt("id_funcionario")));
				prof.setRealizaLiberacoes(rs.getBoolean("permite_liberacao"));
				prof.setRealizaEncaixes(rs.getBoolean("permite_encaixe"));

				lista.add(prof);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return lista;
	}

	public List<FuncionarioBean> listarProfissionalAtendimento() throws ProjetoException {

		List<FuncionarioBean> listaProf = new ArrayList<FuncionarioBean>();

		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().get("obj_funcionario");

		String sql = "select distinct id_funcionario, descfuncionario, codespecialidade,e.descespecialidade, cns, ativo, codcbo, "
				+ " codprocedimentopadrao, p.nome descprocpadrao, cpf, senha, realiza_atendimento, id_perfil, permite_liberacao, permite_encaixe "
				+ " from acl.funcionarios join hosp.especialidade e on e.id_especialidade = funcionarios.codespecialidade "
				+ "left join hosp.proc p on p.id = funcionarios.codprocedimentopadrao where funcionarios.codunidade = ? AND realiza_atendimento IS TRUE order by descfuncionario";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, user_session.getUnidade().getId());
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				FuncionarioBean prof = new FuncionarioBean();
				prof.setId(rs.getLong("id_funcionario"));
				prof.setCpf(rs.getString("cpf"));
				prof.setSenha(rs.getString("senha"));
				prof.setRealizaAtendimento(rs.getBoolean("realiza_atendimento"));
				prof.setNome(rs.getString("descfuncionario"));
				prof.getEspecialidade().setCodEspecialidade(rs.getInt("codespecialidade"));
				prof.getEspecialidade().setDescEspecialidade(rs.getString("descespecialidade"));
				prof.setCns(rs.getString("cns"));
				prof.setAtivo(rs.getString("ativo"));
				prof.setCbo(cDao.listarCboPorId(rs.getInt("codcbo")));
				prof.getProc1().setIdProc(rs.getInt("codprocedimentopadrao"));
				prof.getProc1().setNomeProc(rs.getString("descprocpadrao"));
				prof.getPerfil().setId(rs.getLong("id_perfil"));
				prof.setRealizaLiberacoes(rs.getBoolean("permite_liberacao"));
				prof.setRealizaEncaixes(rs.getBoolean("permite_encaixe"));

				listaProf.add(prof);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return listaProf;
	}

	public List<FuncionarioBean> listarTodosOsProfissional() throws ProjetoException {

		List<FuncionarioBean> listaProf = new ArrayList<FuncionarioBean>();
		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().get("obj_usuario");

		String sql = "select distinct id_funcionario, descfuncionario, codespecialidade, e.descespecialidade,  cns, funcionarios.ativo, codcbo, c.descricao desccbo, "
				+ " codprocedimentopadrao, p.nome descprocedimentopadrao, cpf, senha, realiza_atendimento, id_perfil, permite_liberacao, permite_encaixe, unidade.nome nomeunidade "
				+ " from acl.funcionarios join hosp.unidade on unidade.id = funcionarios.codunidade "
				+ " left join hosp.especialidade e on e.id_especialidade = funcionarios.codespecialidade "
				+ " left join hosp.cbo c on c.id = funcionarios.codcbo "
				+ " left join hosp.proc p on p.id = funcionarios.codprocedimentopadrao"
				+" where coalesce(admin,false) is false and unidade.id=? order by descfuncionario";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setLong(1, user_session.getUnidade().getId());
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				FuncionarioBean prof = new FuncionarioBean();
				prof.setId(rs.getLong("id_funcionario"));
				prof.setCpf(rs.getString("cpf"));
				prof.setSenha(rs.getString("senha"));
				prof.setRealizaAtendimento(rs.getBoolean("realiza_atendimento"));
				prof.setNome(rs.getString("descfuncionario"));
				prof.getEspecialidade().setCodEspecialidade(rs.getInt("codespecialidade"));
				prof.getEspecialidade().setDescEspecialidade(rs.getString("descespecialidade"));
				prof.setCns(rs.getString("cns"));
				prof.setAtivo(rs.getString("ativo"));
				prof.getCbo().setCodCbo(rs.getInt("codcbo"));
				prof.getCbo().setDescCbo(rs.getString("desccbo"));
				prof.getProc1().setIdProc(rs.getInt("codprocedimentopadrao"));
				prof.getProc1().setNomeProc(rs.getString("descprocedimentopadrao"));
				prof.getPerfil().setId(rs.getLong("id_perfil"));
				prof.setRealizaLiberacoes(rs.getBoolean("permite_liberacao"));
				prof.setRealizaEncaixes(rs.getBoolean("permite_encaixe"));
				prof.getUnidade().setNomeUnidade(rs.getString("nomeunidade"));

				listaProf.add(prof);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return listaProf;
	}

	public List<FuncionarioBean> listarProfissionalPorGrupo(Integer codgrupo) throws ProjetoException {
		List<FuncionarioBean> listaProf = new ArrayList<FuncionarioBean>();
		String sql = "select distinct m.id_funcionario, m.descfuncionario, m.codespecialidade, e.descespecialidade, m.cns,\n"
				+ "m.codcbo, c.id idcbo,c.descricao desccbo\n" + "from acl.funcionarios m\n"
				+ "                join hosp.profissional_programa_grupo ppg on (m.id_funcionario = ppg.codprofissional)\n"
				+ "                 left join hosp.especialidade e on (e.id_especialidade = m.codespecialidade)\n"
				+ "                 left join hosp.cbo c on (c.id = m.codcbo)\n"
				+ "                 where m.ativo = 'S' and realiza_atendimento is true and ppg.codgrupo = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, codgrupo);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				FuncionarioBean prof = new FuncionarioBean();
				prof.setId(rs.getLong("id_funcionario"));
				prof.setNome(rs.getString("descfuncionario"));
				prof.getEspecialidade().setCodEspecialidade(rs.getInt("codespecialidade"));
				prof.getEspecialidade().setDescEspecialidade(rs.getString("descespecialidade"));
				prof.setCns(rs.getString("cns"));
				CboBean cbo = new CboBean();
				cbo.setCodCbo(rs.getInt("idcbo"));
				cbo.setCodigo(rs.getString("codcbo"));
				cbo.setDescCbo(rs.getString("desccbo"));
				prof.setCbo(cbo);
				listaProf.add(prof);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return listaProf;
	}

	public boolean excluirProfissional(FuncionarioBean profissional) {

		Boolean retorno = false;

		String sql1 = "delete from hosp.profissional_programa_grupo where codprofissional = ?";
		String sql2 = "delete from acl.perm_usuario where id_usuario = ?";
		String sql3 = "delete from hosp.funcionario_unidades where cod_funcionario = ?";
		String sql4 = "delete from acl.funcionarios where id_funcionario = ?";

		try {

			con = ConnectionFactory.getConnection();

			ps = con.prepareStatement(sql1);
			ps.setLong(1, profissional.getId());
			ps.execute();

			ps = con.prepareStatement(sql2);
			ps.setLong(1, profissional.getId());
			ps.execute();

			ps = con.prepareStatement(sql3);
			ps.setLong(1, profissional.getId());
			ps.execute();

			ps = con.prepareStatement(sql4);
			ps.setLong(1, profissional.getId());
			ps.execute();

			con.commit();

			retorno = true;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return retorno;
		}
	}

	public boolean alterarProfissional(FuncionarioBean profissional, ArrayList<ProgramaBean> lista) {

		Boolean retorno = false;
		String sql = "update acl.funcionarios set descfuncionario = ?, codespecialidade = ?, cns = ?, ativo = ?,"
				+ " codcbo = ?, codprocedimentopadrao = ?, id_perfil = ?, permite_liberacao = ?, realiza_atendimento = ?, permite_encaixe = ?, senha = ?, cpf=?, "
				+ " codunidade=?"
				+ " where id_funcionario = ?";

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);

			stmt.setString(1, profissional.getNome().toUpperCase());

			if (profissional.getEspecialidade() != null) {
				if (profissional.getEspecialidade().getCodEspecialidade() != null) {
					stmt.setInt(2, profissional.getEspecialidade().getCodEspecialidade());
				} else {
					stmt.setNull(2, Types.NULL);
				}
			} else {
				stmt.setNull(2, Types.NULL);
			}

			if (profissional.getCns() != null) {
				stmt.setString(3, profissional.getCns().toUpperCase());
			} else {
				stmt.setString(3, "");
			}

			stmt.setString(4, profissional.getAtivo());

			if (profissional.getCbo() != null) {
				if (profissional.getCbo().getCodCbo() != null) {
					stmt.setInt(5, profissional.getCbo().getCodCbo());
				} else {
					stmt.setNull(5, Types.NULL);
				}
			} else {
				stmt.setNull(5, Types.NULL);
			}

			if (profissional.getProc1() != null) {
				if (profissional.getProc1().getCodProc() != null) {
					stmt.setInt(6, profissional.getProc1().getIdProc());
				} else {
					stmt.setNull(6, Types.NULL);
				}
			} else {
				stmt.setNull(6, Types.NULL);
			}

			stmt.setLong(7, profissional.getPerfil().getId());

			stmt.setBoolean(8, profissional.getRealizaLiberacoes());

			stmt.setBoolean(9, profissional.getRealizaAtendimento());

			stmt.setBoolean(10, profissional.getRealizaEncaixes());

			stmt.setString(11, profissional.getSenha());

			stmt.setString(12, profissional.getCpf().replaceAll("[^0-9]", ""));

			stmt.setLong(13, profissional.getUnidade().getId());
			
			stmt.setLong(14, profissional.getId());

			stmt.executeUpdate();

			String sql2 = "delete from hosp.funcionario_unidades where cod_funcionario = ?";
			stmt = con.prepareStatement(sql2);
			stmt.setLong(1, profissional.getId());
			stmt.executeUpdate();

			for (int i = 0; i < profissional.getListaUnidades().size(); i++) {
				String sql3 = "INSERT INTO hosp.funcionario_unidades (cod_unidade, cod_funcionario) VALUES (?, ?);";
				stmt = con.prepareStatement(sql3);

				stmt.setInt(1, profissional.getListaUnidades().get(i).getId());
				stmt.setLong(2, profissional.getId());

				stmt.executeUpdate();
			}

			String sql4 = "delete from hosp.profissional_programa_grupo where codprofissional = ?";
			stmt = con.prepareStatement(sql4);
			stmt.setLong(1, profissional.getId());
			stmt.executeUpdate();

			for (int i = 0; i < lista.size(); i++) {
				String sql5 = "INSERT INTO hosp.profissional_programa_grupo(codprofissional, codprograma, codgrupo) VALUES (?, ?, ?);";
				stmt = con.prepareStatement(sql5);

				stmt.setLong(1, profissional.getId());
				stmt.setInt(2, lista.get(i).getIdPrograma());
				stmt.setInt(3, lista.get(i).getGrupoBean().getIdGrupo());

				stmt.executeUpdate();
			}

			String sql6 = "delete from acl.perm_usuario where id_usuario = ?";
			ps = con.prepareStatement(sql6);
			ps.setLong(1, profissional.getId());
			ps.execute();

			String sql7 = "insert into acl.perm_usuario (id_usuario, id_permissao) values (?, ?)";
			List<Long> listaPerm = profissional.getListaIdPermissoes();
			ps = con.prepareStatement(sql7);
			for (Long idPerm : listaPerm) {
				ps.setLong(1, profissional.getId());
				ps.setLong(2, idPerm);
				ps.execute();
			}

			String sql8 = "delete from acl.perm_sistema where id_usuario = ?";
			stmt = con.prepareStatement(sql8);
			stmt.setLong(1, profissional.getId());
			stmt.executeUpdate();

			String sql9 = "insert into acl.perm_sistema (id_usuario, id_sistema) values (?, ?)";
			List<Integer> listaIdSistemas = profissional.getListaIdSistemas();
			ps = con.prepareStatement(sql9);
			for (Integer idSistema : listaIdSistemas) {
				ps.setLong(1, profissional.getId());
				ps.setLong(2, idSistema);
				ps.execute();
			}

			retorno = alterarProfissionalBancoPublico(profissional);

			if (retorno) {
				con.commit();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return retorno;
		}
	}

	public List<Sistema> carregaSistemasListaSourceAlteracao(Long id) throws ProjetoException {

		String sql = " select id, descricao from acl.sistema where sistema.id not in ("
				+ " select si.id from acl.perm_sistema ps" + " join acl.sistema si on si.id = ps.id_sistema"
				+ " join acl.funcionarios us on us.id_funcionario = ps.id_usuario"
				+ " where us.id_funcionario = ? and us.ativo = 'S') and coalesce(restrito,false) is false order by descricao";

		List<Sistema> listaSistemas = new ArrayList<>();

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setLong(1, id);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Sistema s = new Sistema();
				s.setId(rs.getInt("id"));
				s.setDescricao(rs.getString("descricao"));
				listaSistemas.add(s);
			}
			rs.close();
			stmt.close();
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}

		}
		return listaSistemas;
	}

	public List<Sistema> carregaListaSistemasSoucerInclusao() throws ProjetoException {

		String sql = "select id, descricao from acl.sistema "
				+ "where ativo = true and coalesce(restrito,false) is false order by descricao";

		List<Sistema> listaSistemas = new ArrayList<>();

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Sistema s = new Sistema();
				s.setId(rs.getInt("id"));
				s.setDescricao(rs.getString("descricao"));
				listaSistemas.add(s);
			}
			rs.close();
			stmt.close();
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}

		}
		return listaSistemas;
	}

	public List<Sistema> carregaListaSistemasTargetAlteracao(Long idFuncionario) throws ProjetoException {

		String sql = "select si.id, si.descricao from acl.perm_sistema ps "
				+ "join acl.sistema si on si.id = ps.id_sistema "
				+ "join acl.funcionarios us on us.id_funcionario = ps.id_usuario "
				+ "where us.id_funcionario= ? and us.ativo = 'S' and coalesce(restrito,false) is false order by si.descricao";

		List<Sistema> listaSistemas = new ArrayList<>();

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setLong(1, idFuncionario);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Sistema s = new Sistema();
				s.setId(rs.getInt("id"));
				s.setDescricao(rs.getString("descricao"));
				listaSistemas.add(s);
			}
			rs.close();
			stmt.close();
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}

		}
		return listaSistemas;
	}

	public boolean alterarProfissionalBancoPublico(FuncionarioBean profissional) {

		Connection conexaoPublica = null;

		Boolean retorno = false;
		String sql = "UPDATE acl.funcionarios SET senha = ?, ativo = ? , descfuncionario=? , cpf=? WHERE id_funcionario = ? and banco_acesso=?";

		try {
			conexaoPublica = ConnectionFactoryPublico.getConnection();
			PreparedStatement stmt = conexaoPublica.prepareStatement(sql);

			stmt.setString(1, profissional.getSenha());
			stmt.setString(2, profissional.getAtivo().toUpperCase());
			stmt.setString(3, profissional.getNome());
			stmt.setString(4, profissional.getCpf().replaceAll("[^0-9]", ""));
			stmt.setLong(5, profissional.getId());
			stmt.setString(6, (String) SessionUtil.resgatarDaSessao("nomeBancoAcesso"));

			stmt.executeUpdate();

			conexaoPublica.commit();
			retorno = true;

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				conexaoPublica.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return retorno;
		}
	}

	public FuncionarioBean buscarProfissionalPorId(Integer id) throws ProjetoException {
		FuncionarioBean prof = null;

		String sql = "select id_funcionario, descfuncionario, codespecialidade, cns, ativo, codcbo, codprocedimentopadrao,"
				+ " cpf, senha, realiza_atendimento, id_perfil, codunidade, permite_liberacao, permite_encaixe "
				+ " from acl.funcionarios where id_funcionario = ? and ativo = 'S' order by descfuncionario";

		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				prof = new FuncionarioBean();
				prof.setId(rs.getLong("id_funcionario"));
				prof.setNome(rs.getString("descfuncionario"));
				prof.setCpf(rs.getString("cpf"));
				prof.setSenha(rs.getString("senha"));
				prof.setRealizaAtendimento(rs.getBoolean("realiza_atendimento"));
				prof.setPrograma(listarProgProf(rs.getInt("id_funcionario")));
				prof.setGrupo(listarProgGrupo(rs.getInt("id_funcionario")));
				prof.setEspecialidade(espDao.listarEspecialidadePorId(rs.getInt("codespecialidade")));
				prof.setCns(rs.getString("cns"));
				prof.setAtivo(rs.getString("ativo"));
				prof.setCbo(cDao.listarCboPorId(rs.getInt("codcbo")));
				prof.setProc1(procDao.listarProcedimentoPorId(rs.getInt("codprocedimentopadrao")));
				prof.getPerfil().setId(rs.getLong("id_perfil"));
				prof.getUnidade().setId(rs.getInt("codunidade"));
				prof.setRealizaLiberacoes(rs.getBoolean("permite_liberacao"));
				prof.setRealizaEncaixes(rs.getBoolean("permite_encaixe"));

			}
			return prof;

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public FuncionarioBean buscarProfissionalRealizaAtendimentoPorId(Integer id) throws ProjetoException {
		FuncionarioBean prof = null;

		String sql = "select id_funcionario, descfuncionario, codespecialidade, cns, ativo, codcbo, codprocedimentopadrao,"
				+ " cpf, senha, realiza_atendimento, permite_liberacao, permite_encaixe "
				+ " from acl.funcionarios where id_funcionario = ? and ativo = 'S' and realiza_atendimento is true order by descfuncionario";

		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				prof = new FuncionarioBean();
				prof.setId(rs.getLong("id_funcionario"));
				prof.setNome(rs.getString("descfuncionario"));
				prof.setCpf(rs.getString("cpf"));
				prof.setSenha(rs.getString("senha"));
				prof.setRealizaAtendimento(rs.getBoolean("realiza_atendimento"));
				prof.setPrograma(listarProgProf(rs.getInt("id_funcionario")));
				prof.setGrupo(listarProgGrupo(rs.getInt("id_funcionario")));
				prof.setEspecialidade(espDao.listarEspecialidadePorId(rs.getInt("codespecialidade")));
				prof.setCns(rs.getString("cns"));
				prof.setAtivo(rs.getString("ativo"));
				prof.setCbo(cDao.listarCboPorId(rs.getInt("codcbo")));
				prof.setProc1(procDao.listarProcedimentoPorId(rs.getInt("codprocedimentopadrao")));
				prof.setRealizaLiberacoes(rs.getBoolean("permite_liberacao"));
				prof.setRealizaEncaixes(rs.getBoolean("permite_encaixe"));

			}

			return prof;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public List<FuncionarioBean> listarProfissionaisPorEquipe(int id, Connection conAuxiliar) {

		List<FuncionarioBean> lista = new ArrayList<FuncionarioBean>();
		String sql = "select medico from hosp.equipe_medico where equipe = ? order by medico";
		FuncionarioDAO pDao = new FuncionarioDAO();
		try {
			PreparedStatement stm = conAuxiliar.prepareStatement(sql);
			stm.setInt(1, id);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				lista.add(pDao.buscarProfissionalPorId(rs.getInt("medico")));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return lista;
		}
	}

	public List<ProgramaBean> listarProgProf(int idProf) {
		List<ProgramaBean> lista = new ArrayList<ProgramaBean>();
		String sql = "select codprograma from hosp.profissional_programa_grupo where codprofissional = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, idProf);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				lista.add(progDao.listarProgramaPorId(rs.getInt("codprograma")));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return lista;
		}
	}

	public List<GrupoBean> listarProgGrupo(int idProf) {
		List<GrupoBean> lista = new ArrayList<GrupoBean>();
		String sql = "select codgrupo from hosp.profissional_programa_grupo where codprofissional = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, idProf);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				lista.add(gDao.listarGrupoPorId(rs.getInt("codgrupo")));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return lista;
		}
	}

	public ArrayList<ProgramaBean> carregaProfissionalProgramaEGrupos(int idProf) {
		ArrayList<ProgramaBean> lista = new ArrayList<ProgramaBean>();

		String sql = "select ppg.codprograma, p.descprograma, ppg.codgrupo, g.descgrupo "
				+ "from hosp.profissional_programa_grupo ppg "
				+ "left join hosp.programa p on (p.id_programa = ppg.codprograma) "
				+ "left join hosp.grupo g on (g.id_grupo = ppg.codgrupo)" + "where codprofissional = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, idProf);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				ProgramaBean p = new ProgramaBean();

				p.setIdPrograma(rs.getInt("codprograma"));
				p.setDescPrograma(rs.getString("descprograma"));
				p.getGrupoBean().setIdGrupo(rs.getInt("codgrupo"));
				p.getGrupoBean().setDescGrupo(rs.getString("descgrupo"));

				lista.add(p);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return lista;
		}
	}

	public List<UnidadeBean> listarUnidadesUsuarioVisualiza(int idFuncionario) {

		List<UnidadeBean> lista = new ArrayList<UnidadeBean>();
		PreparedStatement stm = null;

		StringBuilder sql = new StringBuilder();

		sql.append("SELECT fun.codunidade, e.nome nomeunidade ");
		sql.append(" FROM hosp.funcionario_unidades f");
		sql.append(" join acl.funcionarios fun on fun.id_funcionario = f.cod_funcionario");
		sql.append(" join hosp.unidade e  ON (e.id = f.cod_unidade) ");
		sql.append(" WHERE f.cod_funcionario = ?");

		try {

			con = ConnectionFactory.getConnection();
			stm = con.prepareStatement(sql.toString());

			stm.setInt(1, idFuncionario);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				UnidadeBean UnidadeBean = new UnidadeBean();
				UnidadeBean.setId(rs.getInt("codunidade"));
				UnidadeBean.setNomeUnidade(rs.getString("nomeunidade"));
				lista.add(UnidadeBean);

			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return lista;
		}
	}

	public List<UnidadeBean> listarUnidadesDoFuncionario() {

        FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("obj_funcionario");

        List<UnidadeBean> lista = new ArrayList<UnidadeBean>();
        PreparedStatement stm = null;

        StringBuilder sql = new StringBuilder();

        sql.append("SELECT f.cod_unidade, e.nome, FALSE AS padrao \n"); 
        sql.append(		"FROM hosp.funcionario_unidades f\n") ; 
        sql.append(		"join hosp.unidade e on (e.id = f.cod_unidade) \n") ; 
        sql.append(		"WHERE f.cod_funcionario = ? \n") ;
        sql.append( 		"ORDER BY e.nome"); 

        try {

            con = ConnectionFactory.getConnection();
            stm = con.prepareStatement(sql.toString());

            stm.setLong(1, user_session.getId());
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                UnidadeBean UnidadeBean = new UnidadeBean();
                UnidadeBean.setId(rs.getInt("codunidade"));
                UnidadeBean.setNomeUnidade(rs.getString("nome"));                
                lista.add(UnidadeBean);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return lista;
        }
    }

	public FuncionarioBean validarCpfIhSenha(String cpf, String senha, String tipoValidacao) throws ProjetoException {

		FuncionarioBean func = null;

		String sql = "SELECT id_funcionario, descfuncionario FROM acl.funcionarios WHERE cpf = ? AND senha = ? ";

		if (tipoValidacao.equals(ValidacaoSenha.LIBERACAO.getSigla())) {
			sql = sql + " AND permite_liberacao IS TRUE;";
		}
		if (tipoValidacao.equals(ValidacaoSenha.ENCAIXE.getSigla())) {
			sql = sql + " AND permite_encaixe IS TRUE;";
		}

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);

			stmt.setString(1, cpf.replaceAll("[^0-9]", ""));
			stmt.setString(2, senha);

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				func = new FuncionarioBean();
				func.setId(rs.getLong("id_funcionario"));
				func.setNome(rs.getString("descfuncionario"));
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return func;
	}

	public Integer validarIdIhSenha(String senha) {

		Integer idFuncionario = 0;

		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().get("obj_funcionario");

		String sql = "SELECT id_funcionario FROM acl.funcionarios WHERE id_funcionario = ? AND senha = ? ";

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);

			stmt.setLong(1, user_session.getId());
			stmt.setString(2, senha);

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				idFuncionario = rs.getInt("id_funcionario");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return idFuncionario;
	}

}
