package br.gov.al.maceio.sishosp.acl.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.acl.model.*;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactoryPublico;
import br.gov.al.maceio.sishosp.comum.util.SessionUtil;
import br.gov.al.maceio.sishosp.comum.util.TratamentoErrosUtil;
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
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return nomeBancoAcesso;
	}

	public Integer verificarSeTrabalhaEmMaisDeUmaEmpresa(String cpf) throws ProjetoException {

		String sql = "select count(*) as quantidade from acl.funcionarios where cpf = ? and ativo = 'S';";

		Integer quantidade = 0;

		try {
			con = ConnectionFactoryPublico.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setString(1, cpf.replaceAll("[^0-9]", ""));

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				quantidade = rs.getInt("quantidade");
			}
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return quantidade;
	}

	public List<Empresa> carregarEmpresasDoFuncionario(String cpf) throws ProjetoException {

		String sql = "select e.nome_empresa, e.nome_banco from acl.funcionarios f " +
				"join acl.empresas e on (f.banco_acesso = e.nome_banco) " +
				"where f.ativo = 'S' and f.cpf = ?;";

		List<Empresa> lista = new ArrayList<>();

		try {
			con = ConnectionFactoryPublico.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setString(1, cpf.replaceAll("[^0-9]", ""));

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Empresa empresa = new Empresa();
				empresa.setEmpresa(rs.getString("nome_empresa"));
				empresa.setBancoAcesso(rs.getString("nome_banco"));
				lista.add(empresa);
			}
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return lista;
	}

	public FuncionarioBean autenticarUsuario(FuncionarioBean usuario) throws ProjetoException {

			String sql = "select us.id_funcionario, us.descfuncionario, us.senha, us.email, permite_liberacao, permite_encaixe, us.realiza_atendimento, "
				+ "pf.descricao as descperfil, us.codunidade, p.tipo_atendimento_terapia,  case when us.ativo = 'S' "
				+ "then true else false end as usuarioativo, p.opcao_atendimento, atribuir_cor_tabela_tela_evolucao_profissional, "
				+ "pf.id as idperfil, u.id codunidade,u.nome nomeunidade, e.nome_principal, e.nome_fantasia, e.cod_empresa, "
				+ " coalesce(necessita_presenca_para_evolucao,'N') necessita_presenca_para_evolucao, "
				+ " coalesce(pts_mostra_obs_gerais_curto, false) pts_mostra_obs_gerais_curto, "
				+ " coalesce(pts_mostra_obs_gerais_medio,false) pts_mostra_obs_gerais_medio, coalesce(pts_mostra_obs_gerais_longo,false) pts_mostra_obs_gerais_longo, "
				+ " us.codprocedimentopadrao, proc.nome descprocedimentopadrao, proc.validade_laudo,  p.programa_ortese_protese,progortese.descprograma descprogramaortese, "
				+ " p.grupo_ortese_protese, grupoortese.descgrupo descgrupoortese, p.orgao_origem_responsavel_pela_informacao, p.sigla_orgao_origem_responsavel_pela_digitacao, "
				+ " p.cgcCpf_prestador_ou_orgao_publico, p.orgao_destino_informacao, p.indicador_orgao_destino_informacao, "
				+ " p.versao_sistema ,coalesce(us.excecao_bloqueio_horario, false) excecao_bloqueio_horario, coalesce(horario_limite_acesso, false) horario_limite_acesso, "
				+ " coalesce(p.permite_agendamento_duplicidade, false) permite_agendamento_duplicidade, p.agenda_avulsa_valida_paciente_ativo , e.restringir_laudo_unidade "
				+ " from acl.funcionarios us "
				+ " join acl.perfil pf on (pf.id = us.id_perfil) "
				+ " left join hosp.parametro p ON (p.codunidade = us.codunidade) "
				+ " join hosp.unidade u on u.id = us.codunidade "
				+ " join hosp.empresa e on e.cod_empresa = u.cod_empresa "
				+ " left join hosp.proc on proc.id = us.codprocedimentopadrao"
				+ " left join hosp.programa progortese on progortese.id_programa  = p.programa_ortese_protese"
				+ " left join hosp.grupo grupoortese on grupoortese.id_grupo  = p.grupo_ortese_protese "
				+ " where (us.cpf = ?) and us.ativo = 'S' ";

		FuncionarioBean funcionario = null;
		String setoresUsuario = "";

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setString(1, usuario.getCpf().replaceAll("[^0-9]", ""));
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				funcionario = new FuncionarioBean();
				funcionario.setId(rs.getLong("id_funcionario"));
				funcionario.setNome(rs.getString("descfuncionario"));
				funcionario.setSenha(rs.getString("senha"));
				funcionario.setEmail(rs.getString("email"));
				funcionario.setRealizaAtendimento(rs.getBoolean("realiza_atendimento"));
				funcionario.setExcecaoBloqueioHorario(rs.getBoolean("excecao_bloqueio_horario"));
				funcionario.getUnidade().setId(rs.getInt("codunidade"));
				funcionario.getUnidade().setCodEmpresa(rs.getInt("cod_empresa"));
				funcionario.getUnidade().setNomeUnidade(rs.getString("nomeunidade"));
				funcionario.getUnidade().setNomeEmpresa(rs.getString("nome_principal"));
				funcionario.getUnidade().setNomeFantasia(rs.getString("nome_fantasia"));
				funcionario.setRealizaLiberacoes(rs.getBoolean("permite_liberacao"));
				funcionario.setRealizaEncaixes(rs.getBoolean("permite_encaixe"));
				funcionario.getUnidade().getParametro().getTipoAtendimento().setIdTipo(rs.getInt("tipo_atendimento_terapia"));
				funcionario.getUnidade().getParametro().setOpcaoAtendimento(rs.getString("opcao_atendimento"));
				funcionario.getUnidade().getParametro().setNecessitaPresencaParaEvolucao(rs.getString("necessita_presenca_para_evolucao"));
				funcionario.getUnidade().getParametro().setPtsMostrarObjGeraisCurtoPrazo(rs.getBoolean("pts_mostra_obs_gerais_curto"));
				funcionario.getUnidade().getParametro().setPtsMostrarObjGeraisMedioPrazo(rs.getBoolean("pts_mostra_obs_gerais_medio"));
				funcionario.getUnidade().getParametro().setPtsMostrarObjGeraisLongoPrazo(rs.getBoolean("pts_mostra_obs_gerais_longo"));
				funcionario.getUnidade().getParametro().getOrteseProtese().getPrograma().setIdPrograma(rs.getInt("programa_ortese_protese"));
				funcionario.getUnidade().getParametro().getOrteseProtese().getPrograma().setDescPrograma(rs.getString("descprogramaortese"));
				funcionario.getUnidade().getParametro().getOrteseProtese().getGrupo().setIdGrupo(rs.getInt("grupo_ortese_protese"));
				funcionario.getUnidade().getParametro().getOrteseProtese().getGrupo().setDescGrupo(rs.getString("descgrupoortese"));
				funcionario.getUnidade().getParametro().setOrgaoOrigemResponsavelPelaInformacao(rs.getString("orgao_origem_responsavel_pela_informacao"));
				funcionario.getUnidade().getParametro().setSiglaOrgaoOrigemResponsavelPelaDigitacao(rs.getString("sigla_orgao_origem_responsavel_pela_digitacao"));
				funcionario.getUnidade().getParametro().setCgcCpfPrestadorOuOrgaoPublico(rs.getString("cgcCpf_prestador_ou_orgao_publico"));
				funcionario.getUnidade().getParametro().setOrgaoDestinoInformacao(rs.getString("orgao_destino_informacao"));
				funcionario.getUnidade().getParametro().setIndicadorOrgaoDestinoInformacao(rs.getString("indicador_orgao_destino_informacao"));
				funcionario.getUnidade().getParametro().setVersaoSistema(rs.getString("versao_sistema"));
				funcionario.getUnidade().getParametro().setUsaHorarioLimiteParaAcesso(rs.getBoolean("horario_limite_acesso"));
				funcionario.getUnidade().getParametro().setPermiteAgendamentoDuplicidade(rs.getBoolean("permite_agendamento_duplicidade"));
				funcionario.getUnidade().getParametro().setAgendaAvulsaValidaPacienteAtivo(rs.getBoolean("agenda_avulsa_valida_paciente_ativo"));
				funcionario.getUnidade().getParametro().setAtribuirCorTabelaTelaEvolucaoProfissional(rs.getBoolean("atribuir_cor_tabela_tela_evolucao_profissional"));
				funcionario.getUnidade().setRestringirLaudoPorUnidade(rs.getBoolean("restringir_laudo_unidade"));

				// ACL
				funcionario.setId(rs.getLong("id_funcionario"));
				funcionario.setUsuarioAtivo(rs.getBoolean("usuarioativo"));
				funcionario.getPerfil().setId(rs.getLong("idperfil"));
				funcionario.getPerfil().setDescricao(rs.getString("descperfil"));
				if (rs.getString("codprocedimentopadrao") != null){
					funcionario.getProc1().setIdProc(rs.getInt("codprocedimentopadrao"));
					funcionario.getProc1().setNomeProc(rs.getString("descprocedimentopadrao"));
					funcionario.getProc1().setValidade_laudo(rs.getInt("validade_laudo"));
				}
			}

			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("obj_funcionario", funcionario);
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("obj_usuario", funcionario);

			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("setores_usuario",
					setoresUsuario);

		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return funcionario;
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
				Sistema sistema = new Sistema();
				sistema.setId(rs.getInt("id"));
				sistema.setDescricao(rs.getString("descricao"));
				sistema.setSigla(rs.getString("sigla"));
				sistema.setUrl(rs.getString("url") + "?faces-redirect=true");
				sistema.setImagem(rs.getString("imagem"));
				sistema.setVersao(rs.getString("versao"));
				sistema.setAtivo(rs.getBoolean("ativo"));
				listaSistemas.add(sistema);
			}
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
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

		String sql = "select  distinct si.id as sid, si.descricao as sdesc, pf.descricao pfdesc, "
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
				Menu menu = new Menu();
				menu.setId(rs.getLong("meid"));
				menu.setDescricao(rs.getString("medesc"));
				menu.setCodigo(rs.getString("cod_menu"));
				menu.setIndice(rs.getString("indice"));
				menu.setTipo(rs.getString("tipo"));
				menu.setAtivo(rs.getBoolean("meativo"));
				menu.setDiretorio(rs.getString("diretorio"));
				menu.setDescPagina(rs.getString("desc_pagina"));
				menu.setExtensao(rs.getString("extensao"));
				menu.setAction(rs.getString("action_rel"));
				menu.setOnclick(rs.getString("onclick_rel"));

				if ((rs.getString("tipo").equals("menuItem")) || (rs.getString("tipo").equals("rotinaInterna"))) {
					// contextPath
					menu.setUrl("/pages/" + menu.getDiretorio() + "/" + menu.getDescPagina() + menu.getExtensao());
				}

				if (rs.getString("funativa").equals("t")) {
					menu.setAtivo(true);
				}

				Permissoes permissoes = new Permissoes();
				permissoes.setIdSistema(rs.getInt("sid"));
				permissoes.setDescSistema(rs.getString("sdesc"));
				permissoes.setDescPerfil(rs.getString("pfdesc"));
				permissoes.setIdPermissao(rs.getInt("pmid"));
				permissoes.setDescPermissao(rs.getString("pmdesc"));
				permissoes.setMenu(menu);

				permissoes.setCodigoFuncao(rs.getString("funcodigo"));
				permissoes.setFuncaoAtiva(rs.getBoolean("funativa"));
				permissoes.setIdSistemaFunc(rs.getInt("funidsis"));

				lista.add(permissoes);
			}
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return lista;
	}

	public Boolean alterar(FuncionarioBean usuario) throws ProjetoException {
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

		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return retorno;
	}

	public Boolean alterarSemPerm(FuncionarioBean usuario) throws ProjetoException {
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
			FuncionarioBean funcionario = usuario;

			boolean associouSis = false;

			if (funcionario.getId() != null) {
				sql = "delete from acl.perm_sistema where id_usuario = ?";
				String sql2 = "insert into acl.perm_sistema (id_usuario, id_sistema) values (?, ?)";
				List<Integer> listaId = usuario.getListaIdSistemas();
				PreparedStatement stmt2;
				try {
					stmt2 = con.prepareStatement(sql);
					stmt2.setLong(1, usuario.getId());
					stmt2.execute();
				} catch (SQLException sqle) {
					throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
				} catch (Exception ex) {
					throw new ProjetoException(ex, this.getClass().getName());
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
					} catch (SQLException sqle) {
						throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
					} catch (Exception ex) {
						throw new ProjetoException(ex, this.getClass().getName());
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
				} catch (SQLException sqle) {
					throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
				} catch (Exception ex) {
					throw new ProjetoException(ex, this.getClass().getName());
				}
			}

			con.commit();
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return retorno;
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

				if ((rs.getString("tipo").equals("menuItem")) || (rs.getString("tipo").equals("rotinaInterna"))) {
					menu.setUrl("/pages/" + menu.getDiretorio() + "/" + menu.getDescPagina() + menu.getExtensao());
				}
				menu.setIndiceAux(rs.getString("codigo"));

				menu.setIdSistema(rs.getInt("id_sis"));
				menu.setDescSistema(rs.getString("desc_sis"));
				menu.setSiglaSistema(rs.getString("sigla_sis").toUpperCase());
				lista.add(menu);
			}

			rs.close();
			stmt.close();
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
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

				if ((rs.getString("tipo").equals("menuItem")) || (rs.getString("tipo").equals("rotinaInterna"))) {
					menu.setUrl("/pages/" + menu.getDiretorio() + "/" + menu.getDescPagina() + menu.getExtensao());
				}

				menu.setIndiceAux(rs.getString("codigo"));
				menu.setIdSistema(rs.getInt("id_sis"));
				menu.setDescSistema(rs.getString("desc_sis"));
				menu.setSiglaSistema(rs.getString("sigla_sis").toUpperCase());
				lista.add(menu);
			}

			rs.close();
			stmt.close();
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return lista;
	}

	public ArrayList<FuncionarioBean> buscaUsuarios() throws ProjetoException {

		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().get("obj_funcionario");

		String sql = "select * from acl.funcionarios u where codunidade = ? order by ativo,descfuncionario";

		ArrayList<FuncionarioBean> lista = new ArrayList<>();

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, user_session.getUnidade().getId());
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				FuncionarioBean funcionario = new FuncionarioBean();
				funcionario = new FuncionarioBean();
				funcionario.setId(rs.getLong("id_funcionario"));
				funcionario.setNome(rs.getString("descfuncionario"));
				funcionario.setSenha(rs.getString("senha"));
				funcionario.setAtivo(rs.getString("ativo"));
				funcionario.setEmail(rs.getString("email"));
				funcionario.setCpf(rs.getString("cpf"));
				funcionario.getPerfil().setId(rs.getLong("id_perfil"));

				lista.add(funcionario);
			}
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
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
				Permissao permissao = new Permissao();
				permissao.setId(rs.getLong("id"));
				permissao.setDescricao(rs.getString("descricao"));
				lista.add(permissao);
			}
			rs.close();
			stmt.close();
			con.close();
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
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
				Permissao permissao = new Permissao();
				permissao.setId(rs.getLong("id"));
				permissao.setDescricao(rs.getString("descricao"));
				lista.add(permissao);
			}
			rs.close();
			stmt.close();
			con.close();
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
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
				Permissao permissao = new Permissao();
				permissao.setId(rs.getLong("id"));
				permissao.setDescricao(rs.getString("descricao"));
				lista.add(permissao);
			}
			rs.close();
			stmt.close();
			con.close();
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return lista;
	}

	public Boolean alterarSenha(FuncionarioBean usuario) throws ProjetoException {

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

		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return retorno;
	}

	public Boolean alterarSenhaBancoPublico(FuncionarioBean usuario) throws ProjetoException {

		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().get("obj_funcionario");

		Boolean retorno = false;

		String sql = "update acl.funcionarios set senha = ? where id_funcionario = ? and banco_acesso=?";

		try {
			con = ConnectionFactoryPublico.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, usuario.getNovaSenha());
			stmt.setLong(2, user_session.getId());
			stmt.setString(3, (String) SessionUtil.resgatarDaSessao("nomeBancoAcesso"));
			stmt.executeUpdate();
			con.commit();

			retorno = true;

		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return retorno;
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

		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return isExist;
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

		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return isExist;
	}

	// INICIO PROFISSIONALDAO

	public boolean gravarProfissional(FuncionarioBean profissional, ArrayList<ProgramaBean> lista) throws ProjetoException {

		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().get("obj_funcionario");

		Boolean retorno = false;

		String sql = "INSERT INTO acl.funcionarios(descfuncionario, cpf, senha, log_user, codespecialidade, cns, codcbo, "
				+ " codprocedimentopadrao, ativo, realiza_atendimento, datacriacao, primeiroacesso, id_perfil, codunidade, "
				+ "permite_liberacao, permite_encaixe, excecao_bloqueio_horario) "
				+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, current_timestamp, false, ?, ?, ?, ?, ?) returning id_funcionario;";
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
			ps.setBoolean(15, profissional.getExcecaoBloqueioHorario());

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

		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return retorno;
	}

	public boolean gravarProfissionalBancoPublico(FuncionarioBean profissional, Integer idProfissional) throws ProjetoException {

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
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				conexaoPublica.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return retorno;
	}

	public List<FuncionarioBean> listarProfissionalBusca(String descricaoBusca, Integer tipoBuscar)
			throws ProjetoException {

		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().get("obj_funcionario");

		List<FuncionarioBean> lista = new ArrayList<>();
		String sql = "SELECT f.id_funcionario, f.id_funcionario ||'-'|| f.descfuncionario AS descfuncionario, f.codespecialidade, e.descespecialidade, "
				+ "f.cns, f.ativo, f.codcbo, c.descricao, c.codigo, f.codprocedimentopadrao, p.nome, f.permite_liberacao, permite_encaixe "
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
				FuncionarioBean funcionario = new FuncionarioBean();
				funcionario.setId(rs.getLong("id_funcionario"));
				funcionario.setNome(rs.getString("descfuncionario"));
				funcionario.getEspecialidade().setDescEspecialidade(rs.getString("descespecialidade"));
				funcionario.getEspecialidade().setCodEspecialidade(rs.getInt("codespecialidade"));
				funcionario.setCns(rs.getString("cns"));
				funcionario.setAtivo(rs.getString("ativo"));
				funcionario.getCbo().setDescCbo(rs.getString("descricao"));
				funcionario.getCbo().setCodCbo(rs.getInt("codcbo"));
				funcionario.getCbo().setCodigo(rs.getString("codigo"));
				funcionario.getProc1().setNomeProc(rs.getString("nome"));
				funcionario.getProc1().setIdProc(rs.getInt("codprocedimentopadrao"));
				//prof.setPrograma(listarProgProf(rs.getInt("id_funcionario")));
				//prof.setGrupo(listarProgGrupo(rs.getInt("id_funcionario")));
				funcionario.setRealizaLiberacoes(rs.getBoolean("permite_liberacao"));
				funcionario.setRealizaEncaixes(rs.getBoolean("permite_encaixe"));

				lista.add(funcionario);
			}
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
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
				"SELECT DISTINCT h.id_funcionario,  h.descfuncionario AS descfuncionario, h.codespecialidade, h.permite_liberacao, ");
		sql.append(
				"h.permite_encaixe, h.cns, h.ativo, h.codcbo, h.codprocedimentopadrao, e.descespecialidade,c.codigo codigocbo, c.descricao AS desccbo, p.nome AS descproc ");
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
				FuncionarioBean funcionario = new FuncionarioBean();
				funcionario.setId(rs.getLong("id_funcionario"));
				funcionario.setNome(rs.getString("descfuncionario"));
				funcionario.getEspecialidade().setCodEspecialidade(rs.getInt("codespecialidade"));
				funcionario.getEspecialidade().setDescEspecialidade(rs.getString("descespecialidade"));
				funcionario.setCns(rs.getString("cns"));
				funcionario.setAtivo(rs.getString("ativo"));
				funcionario.getCbo().setCodCbo(rs.getInt("codcbo"));
				funcionario.getCbo().setCodigo(rs.getString("codigocbo"));
				funcionario.getCbo().setDescCbo(rs.getString("desccbo"));
				funcionario.getProc1().setIdProc(rs.getInt("codprocedimentopadrao"));
				funcionario.getProc1().setNomeProc(rs.getString("descproc"));
				funcionario.setRealizaLiberacoes(rs.getBoolean("permite_liberacao"));
				funcionario.setRealizaEncaixes(rs.getBoolean("permite_encaixe"));

				lista.add(funcionario);
			}
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
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

		String sql = " select distinct a.* from ( select distinct id_funcionario, descfuncionario, codespecialidade,e.descespecialidade, cns, funcionarios.ativo, codcbo,cbo.codigo codigocbo, cbo.descricao desccbo, \n" +
				" codprocedimentopadrao, p.nome descprocpadrao, cpf, senha, realiza_atendimento, id_perfil, permite_liberacao, permite_encaixe \n" +
				" from acl.funcionarios left join hosp.especialidade e on e.id_especialidade = funcionarios.codespecialidade left join hosp.cbo on cbo.id = funcionarios.codcbo \n" +
				"left join hosp.proc p on p.id = funcionarios.codprocedimentopadrao where funcionarios.codunidade =? AND realiza_atendimento IS TRUE " +
				"union all\n" +
				" select distinct id_funcionario, descfuncionario, codespecialidade,e.descespecialidade, cns, funcionarios.ativo, codcbo,cbo.codigo codigocbo, cbo.descricao desccbo, \n" +
				" codprocedimentopadrao, p.nome descprocpadrao, cpf, senha, realiza_atendimento, id_perfil, permite_liberacao, permite_encaixe \n" +
				" from acl.funcionarios left join hosp.especialidade e on e.id_especialidade = funcionarios.codespecialidade left join hosp.cbo on cbo.id = funcionarios.codcbo\n" +
				"left join hosp.proc p on p.id = funcionarios.codprocedimentopadrao \n" +
				"join hosp.funcionario_unidades fu on fu.cod_funcionario = funcionarios.id_funcionario\n" +
				"where  fu.cod_unidade=?  AND realiza_atendimento IS TRUE " +
				" ) a order by descfuncionario";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, user_session.getUnidade().getId());
			stm.setInt(2, user_session.getUnidade().getId());
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				FuncionarioBean funcionario = new FuncionarioBean();
				funcionario.setId(rs.getLong("id_funcionario"));
				funcionario.setCpf(rs.getString("cpf"));
				funcionario.setSenha(rs.getString("senha"));
				funcionario.setRealizaAtendimento(rs.getBoolean("realiza_atendimento"));
				funcionario.setNome(rs.getString("descfuncionario"));
				funcionario.getEspecialidade().setCodEspecialidade(rs.getInt("codespecialidade"));
				funcionario.getEspecialidade().setDescEspecialidade(rs.getString("descespecialidade"));
				funcionario.setCns(rs.getString("cns"));
				funcionario.setAtivo(rs.getString("ativo"));
				funcionario.getCbo().setCodCbo(rs.getInt("codcbo"));
				funcionario.getCbo().setCodigo(rs.getString("codigocbo"));
				funcionario.getCbo().setDescCbo(rs.getString("desccbo"));
				funcionario.getProc1().setIdProc(rs.getInt("codprocedimentopadrao"));
				funcionario.getProc1().setNomeProc(rs.getString("descprocpadrao"));
				funcionario.getPerfil().setId(rs.getLong("id_perfil"));
				funcionario.setRealizaLiberacoes(rs.getBoolean("permite_liberacao"));
				funcionario.setRealizaEncaixes(rs.getBoolean("permite_encaixe"));

				listaProf.add(funcionario);
			}
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
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

		List<FuncionarioBean> listaProfissional = new ArrayList<FuncionarioBean>();

		String sql = "select * from (\n" +
				"select distinct id_funcionario, descfuncionario, codespecialidade, e.descespecialidade,  cns, funcionarios.ativo, codcbo, c.descricao desccbo, c.codigo, \n" +
				" codprocedimentopadrao, p.nome descprocedimentopadrao, cpf, senha, realiza_atendimento, id_perfil, permite_liberacao, permite_encaixe, unidade.nome nomeunidade \n" +
				" from acl.funcionarios join hosp.unidade on unidade.id = funcionarios.codunidade \n" +
				" left join hosp.especialidade e on e.id_especialidade = funcionarios.codespecialidade \n" +
				" left join hosp.cbo c on c.id = funcionarios.codcbo \n" +
				" left join hosp.proc p on p.id = funcionarios.codprocedimentopadrao\n" +
				" where coalesce(admin,false) is false \n" +
				" union\n" +
				" select distinct id_funcionario, descfuncionario, codespecialidade, e.descespecialidade,  cns, funcionarios.ativo, codcbo, c.descricao desccbo, c.codigo, \n" +
				" codprocedimentopadrao, p.nome descprocedimentopadrao, cpf, senha, realiza_atendimento, id_perfil, permite_liberacao, permite_encaixe, unidade.nome nomeunidade \n" +
				" from acl.funcionarios join hosp.unidade on unidade.id = funcionarios.codunidade \n" +
				" left join hosp.especialidade e on e.id_especialidade = funcionarios.codespecialidade \n" +
				" left join hosp.cbo c on c.id = funcionarios.codcbo \n" +
				" left join hosp.proc p on p.id = funcionarios.codprocedimentopadrao\n" +
				" join hosp.funcionario_unidades fu on fu.cod_funcionario = funcionarios.id_funcionario\n" +
				" where coalesce(admin,false) is false \n" +
				" ) a order by descfuncionario";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				FuncionarioBean profissioanl = new FuncionarioBean();
				profissioanl.setId(rs.getLong("id_funcionario"));
				profissioanl.setCpf(rs.getString("cpf"));
				profissioanl.setSenha(rs.getString("senha"));
				profissioanl.setRealizaAtendimento(rs.getBoolean("realiza_atendimento"));
				profissioanl.setNome(rs.getString("descfuncionario"));
				profissioanl.getEspecialidade().setCodEspecialidade(rs.getInt("codespecialidade"));
				profissioanl.getEspecialidade().setDescEspecialidade(rs.getString("descespecialidade"));
				profissioanl.setCns(rs.getString("cns"));
				profissioanl.setAtivo(rs.getString("ativo"));
				profissioanl.getCbo().setCodCbo(rs.getInt("codcbo"));
				profissioanl.getCbo().setDescCbo(rs.getString("desccbo"));
				profissioanl.getCbo().setCodigo(rs.getString("codigo"));
				profissioanl.getProc1().setIdProc(rs.getInt("codprocedimentopadrao"));
				profissioanl.getProc1().setNomeProc(rs.getString("descprocedimentopadrao"));
				profissioanl.getPerfil().setId(rs.getLong("id_perfil"));
				profissioanl.setRealizaLiberacoes(rs.getBoolean("permite_liberacao"));
				profissioanl.setRealizaEncaixes(rs.getBoolean("permite_encaixe"));
				profissioanl.getUnidade().setNomeUnidade(rs.getString("nomeunidade"));

				listaProfissional.add(profissioanl);
			}
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return listaProfissional;
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
				FuncionarioBean profissional = new FuncionarioBean();
				profissional.setId(rs.getLong("id_funcionario"));
				profissional.setNome(rs.getString("descfuncionario"));
				profissional.getEspecialidade().setCodEspecialidade(rs.getInt("codespecialidade"));
				profissional.getEspecialidade().setDescEspecialidade(rs.getString("descespecialidade"));
				profissional.setCns(rs.getString("cns"));
				CboBean cbo = new CboBean();
				cbo.setCodCbo(rs.getInt("idcbo"));
				cbo.setCodigo(rs.getString("codcbo"));
				cbo.setDescCbo(rs.getString("desccbo"));
				profissional.setCbo(cbo);
				listaProf.add(profissional);
			}
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return listaProf;
	}

	public boolean excluirProfissional(FuncionarioBean profissional) throws ProjetoException {

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
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return retorno;
	}

	public boolean alterarProfissional(FuncionarioBean profissional, ArrayList<ProgramaBean> lista) throws ProjetoException {

		Boolean retorno = false;
		String sql = "update acl.funcionarios set descfuncionario = ?, codespecialidade = ?, cns = ?, ativo = ?,"
				+ " codcbo = ?, codprocedimentopadrao = ?, id_perfil = ?, permite_liberacao = ?, realiza_atendimento = ?, permite_encaixe = ?, senha = ?, cpf=?, "
				+ " codunidade=?, excecao_bloqueio_horario=? "
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
			stmt.setBoolean(14, profissional.getExcecaoBloqueioHorario());
			stmt.setLong(15, profissional.getId());

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

		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return retorno;
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
				Sistema sistema = new Sistema();
				sistema.setId(rs.getInt("id"));
				sistema.setDescricao(rs.getString("descricao"));
				listaSistemas.add(sistema);
			}
			rs.close();
			stmt.close();
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
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
				Sistema sistema = new Sistema();
				sistema.setId(rs.getInt("id"));
				sistema.setDescricao(rs.getString("descricao"));
				listaSistemas.add(sistema);
			}
			rs.close();
			stmt.close();
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
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
				Sistema sistema = new Sistema();
				sistema.setId(rs.getInt("id"));
				sistema.setDescricao(rs.getString("descricao"));
				listaSistemas.add(sistema);
			}
			rs.close();
			stmt.close();
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}

		}
		return listaSistemas;
	}

	public boolean alterarProfissionalBancoPublico(FuncionarioBean profissional) throws ProjetoException {

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

		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				conexaoPublica.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return retorno;
	}

	public FuncionarioBean buscarProfissionalPorId(Integer id) throws ProjetoException {
		FuncionarioBean profissional = null;

		String sql = "select id_funcionario, descfuncionario, codespecialidade, cns, ativo, codcbo, codprocedimentopadrao,"
				+ " cpf, senha, realiza_atendimento, id_perfil, codunidade, permite_liberacao, permite_encaixe, excecao_bloqueio_horario "
				+ " from acl.funcionarios where id_funcionario = ?  order by descfuncionario";

		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				profissional = new FuncionarioBean();
				profissional.setId(rs.getLong("id_funcionario"));
				profissional.setNome(rs.getString("descfuncionario"));
				profissional.setCpf(rs.getString("cpf"));
				profissional.setSenha(rs.getString("senha"));
				profissional.setRealizaAtendimento(rs.getBoolean("realiza_atendimento"));
				profissional.setPrograma(listarProgProf(rs.getInt("id_funcionario")));
				profissional.setGrupo(listarProgGrupo(rs.getInt("id_funcionario")));
				profissional.setEspecialidade(espDao.listarEspecialidadePorId(rs.getInt("codespecialidade")));
				profissional.setCns(rs.getString("cns"));
				profissional.setAtivo(rs.getString("ativo"));
				profissional.setCbo(cDao.listarCboPorId(rs.getInt("codcbo")));
				profissional.setProc1(procDao.listarProcedimentoPorId(rs.getInt("codprocedimentopadrao")));
				profissional.getPerfil().setId(rs.getLong("id_perfil"));
				profissional.getUnidade().setId(rs.getInt("codunidade"));
				profissional.setRealizaLiberacoes(rs.getBoolean("permite_liberacao"));
				profissional.setRealizaEncaixes(rs.getBoolean("permite_encaixe"));
				profissional.setExcecaoBloqueioHorario(rs.getBoolean("excecao_bloqueio_horario"));
			}

		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return profissional;
	}

	public FuncionarioBean buscarProfissionalPorIdParaConverter(Integer id) throws ProjetoException {
		FuncionarioBean profissional = null;

		String sql = "select id_funcionario, descfuncionario, codespecialidade, e.descespecialidade , cns, ativo, funcionarios.codcbo,c.codigo codigocbo, c.descricao desccbo, codprocedimentopadrao,"
				+ " cpf, senha, realiza_atendimento, id_perfil, codunidade, permite_liberacao, permite_encaixe "
				+ " from acl.funcionarios LEFT JOIN hosp.cbo c ON (funcionarios.codcbo = c.id) left join hosp.especialidade e on e.id_especialidade = funcionarios.codespecialidade "
				+	" where id_funcionario = ? and ativo = 'S' order by descfuncionario";

		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				profissional = new FuncionarioBean();
				profissional.setId(rs.getLong("id_funcionario"));
				profissional.setNome(rs.getString("descfuncionario"));
				profissional.setCpf(rs.getString("cpf"));
				profissional.setSenha(rs.getString("senha"));
				profissional.setRealizaAtendimento(rs.getBoolean("realiza_atendimento"));
				profissional.setCns(rs.getString("cns"));
				profissional.setAtivo(rs.getString("ativo"));
				profissional.getPerfil().setId(rs.getLong("id_perfil"));
				profissional.getUnidade().setId(rs.getInt("codunidade"));
				profissional.setRealizaLiberacoes(rs.getBoolean("permite_liberacao"));
				profissional.setRealizaEncaixes(rs.getBoolean("permite_encaixe"));
				profissional.getEspecialidade().setCodEspecialidade(rs.getInt("codespecialidade"));
				profissional.getEspecialidade().setDescEspecialidade(rs.getString("descespecialidade"));
				profissional.getCbo().setCodCbo(rs.getInt("codcbo"));
				profissional.getCbo().setCodigo(rs.getString("codigocbo"));
				profissional.getCbo().setDescCbo(rs.getString("desccbo"));

			}
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return profissional;
	}

	public FuncionarioBean buscarProfissionalConverterPorId(Integer id) throws ProjetoException {
		FuncionarioBean profissional = null;

		String sql = "select id_funcionario, descfuncionario, funcionarios.codespecialidade, e.descespecialidade, \n" +
				"funcionarios.codcbo,c.codigo codigocbo,  c.descricao desccbo from acl.funcionarios \n" +
				"join hosp.especialidade e on e.id_especialidade = funcionarios.codespecialidade\n" +
				"left join hosp.cbo c on c.id = funcionarios.codcbo"
				+ " where funcionarios.id_funcionario = ? and funcionarios.ativo = 'S' order by funcionarios.descfuncionario";

		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				profissional = new FuncionarioBean();
				profissional.setId(rs.getLong("id_funcionario"));
				profissional.setNome(rs.getString("descfuncionario"));
				profissional.getEspecialidade().setCodEspecialidade(rs.getInt("codespecialidade"));
				profissional.getEspecialidade().setDescEspecialidade(rs.getString("descespecialidade"));
				profissional.getCbo().setCodCbo(rs.getInt("codcbo"));
				profissional.getCbo().setCodigo (rs.getString("codigocbo"));
				profissional.getCbo().setDescCbo(rs.getString("desccbo"));
			}

		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return profissional;
	}

	public FuncionarioBean buscarProfissionalRealizaAtendimentoPorId(Integer id) throws ProjetoException {
		FuncionarioBean profissional = null;

		String sql = "select id_funcionario, descfuncionario, codespecialidade, cns, ativo, codcbo, codprocedimentopadrao,"
				+ " cpf, senha, realiza_atendimento, permite_liberacao, permite_encaixe "
				+ " from acl.funcionarios where id_funcionario = ? and ativo = 'S' and realiza_atendimento is true order by descfuncionario";

		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				profissional = new FuncionarioBean();
				profissional.setId(rs.getLong("id_funcionario"));
				profissional.setNome(rs.getString("descfuncionario"));
				profissional.setCpf(rs.getString("cpf"));
				profissional.setSenha(rs.getString("senha"));
				profissional.setRealizaAtendimento(rs.getBoolean("realiza_atendimento"));
				profissional.setPrograma(listarProgProf(rs.getInt("id_funcionario")));
				profissional.setGrupo(listarProgGrupo(rs.getInt("id_funcionario")));
				profissional.setEspecialidade(espDao.listarEspecialidadePorId(rs.getInt("codespecialidade")));
				profissional.setCns(rs.getString("cns"));
				profissional.setAtivo(rs.getString("ativo"));
				profissional.setCbo(cDao.listarCboPorId(rs.getInt("codcbo")));
				profissional.setProc1(procDao.listarProcedimentoPorId(rs.getInt("codprocedimentopadrao")));
				profissional.setRealizaLiberacoes(rs.getBoolean("permite_liberacao"));
				profissional.setRealizaEncaixes(rs.getBoolean("permite_encaixe"));
			}

		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return profissional;
	}

	public List<FuncionarioBean> listarProfissionaisPorEquipe(int id, Connection conAuxiliar) throws ProjetoException, SQLException {

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
		} catch (SQLException sqle) {
			conAuxiliar.rollback();
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			conAuxiliar.rollback();
			throw new ProjetoException(ex, this.getClass().getName());
		}
		return lista;
	}

	public List<ProgramaBean> listarProgProf(int idProf) throws ProjetoException {
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
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return lista;
	}

	public List<GrupoBean> listarProgGrupo(int idProf) throws ProjetoException {
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
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return lista;
	}

	public ArrayList<ProgramaBean> carregaProfissionalProgramaEGrupos(int idProf) throws ProjetoException {
		ArrayList<ProgramaBean> lista = new ArrayList<ProgramaBean>();

		String sql = "select ppg.codprograma, p.descprograma, ppg.codgrupo, g.descgrupo, u.nome as unidade, u.id " +
				"from hosp.profissional_programa_grupo ppg " +
				"left join hosp.programa p on (p.id_programa = ppg.codprograma) " +
				"left join hosp.grupo g on (g.id_grupo = ppg.codgrupo) " +
				"join hosp.unidade u on u.id = p.cod_unidade " +
				"where codprofissional = ?;";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, idProf);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				ProgramaBean programa = new ProgramaBean();

				programa.setIdPrograma(rs.getInt("codprograma"));
				programa.setDescPrograma(rs.getString("descprograma"));
				programa.getGrupoBean().setIdGrupo(rs.getInt("codgrupo"));
				programa.getGrupoBean().setDescGrupo(rs.getString("descgrupo"));
				programa.setDescricaoUnidade(rs.getString("unidade"));
				programa.setCodUnidade(rs.getInt("id"));
				lista.add(programa);
			}
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return lista;
	}

	public List<UnidadeBean> listarUnidadesUsuarioVisualiza(int idFuncionario) throws ProjetoException {

		List<UnidadeBean> lista = new ArrayList<UnidadeBean>();
		PreparedStatement stm = null;

		StringBuilder sql = new StringBuilder();

		sql.append("SELECT f.cod_unidade, e.nome nomeunidade ");
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
				UnidadeBean.setId(rs.getInt("cod_unidade"));
				UnidadeBean.setNomeUnidade(rs.getString("nomeunidade"));
				lista.add(UnidadeBean);

			}
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return lista;
	}

	public List<UnidadeBean> listarUnidadesDoFuncionario() throws ProjetoException {

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
				UnidadeBean.setId(rs.getInt("cod_unidade"));
				UnidadeBean.setNomeUnidade(rs.getString("nome"));
				lista.add(UnidadeBean);
			}
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return lista;
	}

	public FuncionarioBean validarCpfIhSenha(String cpf, String senha, String tipoValidacao) throws ProjetoException {

		FuncionarioBean funcionario = null;

		String sql = "SELECT id_funcionario, descfuncionario, codunidade FROM acl.funcionarios WHERE cpf = ? AND senha = ? ";

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
				funcionario = new FuncionarioBean();
				funcionario.setId(rs.getLong("id_funcionario"));
				funcionario.setNome(rs.getString("descfuncionario"));
				funcionario.getUnidade().setId(rs.getInt("codunidade"));
			}

		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return funcionario;
	}

	public Integer validarIdIhSenha(String senha) throws ProjetoException {

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

		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return idFuncionario;
	}

	public List<UnidadeBean> listarTodasAsUnidadesDoUsuario(Long idUsuario) throws ProjetoException{

		List<UnidadeBean> lista = new ArrayList<UnidadeBean>();
		PreparedStatement stm = null;

		StringBuilder sql = new StringBuilder();
		sql.append("select u.id, u.nome from acl.funcionarios f ");
		sql.append("join hosp.unidade u on (f.codunidade = u.id) ");
		sql.append("where f.id_funcionario = ? ");
		sql.append("union ");
		sql.append("select u.id, u.nome from acl.funcionarios f ");
		sql.append("join hosp.funcionario_unidades fu on (f.id_funcionario = fu.cod_funcionario) ");
		sql.append("join hosp.unidade u on (u.id = fu.cod_unidade) ");
		sql.append("where f.id_funcionario = ?");

		try {

			con = ConnectionFactory.getConnection();
			stm = con.prepareStatement(sql.toString());

			stm.setLong(1, idUsuario);
			stm.setLong(2, idUsuario);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				UnidadeBean UnidadeBean = new UnidadeBean();
				UnidadeBean.setId(rs.getInt("id"));
				UnidadeBean.setNomeUnidade(rs.getString("nome"));
				lista.add(UnidadeBean);
			}
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return lista;
	}

	public String retornaNomeDaUnidadeAtual(Integer codigoDaUnidadeSelecionada) throws ProjetoException{
		String textoUnidade = null;
		PreparedStatement stm = null;

		StringBuilder sql = new StringBuilder();
		sql.append("select u.nome from acl.funcionarios f ");
		sql.append("join hosp.unidade u on (f.codunidade = u.id) ");
		sql.append("where u.id = ? ");
		sql.append("union ");
		sql.append("select u.nome from acl.funcionarios f ");
		sql.append("join hosp.funcionario_unidades fu on (f.id_funcionario = fu.cod_funcionario) ");
		sql.append("join hosp.unidade u on (u.id = fu.cod_unidade) ");
		sql.append("where u.id = ?");

		try {

			con = ConnectionFactory.getConnection();
			stm = con.prepareStatement(sql.toString());

			stm.setLong(1, codigoDaUnidadeSelecionada);
			stm.setLong(2, codigoDaUnidadeSelecionada);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				textoUnidade = rs.getString("nome");
			}
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return textoUnidade.toUpperCase();
	}

	public Boolean verificarSeTemHorarioLimieIhSeHorarioEhPermitidoPorUsuario(String cpf) throws ProjetoException {

		String sql = "SELECT " +
				"CASE WHEN coalesce(p.horario_limite_acesso,false) = FALSE THEN TRUE " +
				"WHEN ((coalesce(p.horario_limite_acesso,false) is true) AND  (current_time between\n" +
				"p.horario_inicio_funcionamento  AND p.horario_final_funcionamento ))  THEN TRUE  " +
				"ELSE FALSE " +
				"END AS acesso_permitido " +
				"FROM hosp.parametro p " +
				"WHERE p.codunidade = " +
				"(SELECT f.codunidade FROM acl.funcionarios f WHERE f.cpf = ?)";

		Boolean acessoPermitido = false;

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setString(1, cpf.replaceAll("[^0-9]", ""));

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				acessoPermitido = rs.getBoolean("acesso_permitido");
			}

		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return acessoPermitido;
	}

	public Boolean verificarSeTemHorarioLimiteIhSeHorarioEhPermitidoPorUnidade(Integer codigoUnidade) throws ProjetoException {

		String sql = "SELECT " +
				"CASE WHEN coalesce(p.horario_limite_acesso,false) = FALSE THEN TRUE " +
				"WHEN coalesce(p.horario_limite_acesso,false) = TRUE AND p.horario_inicio_funcionamento <= current_time " +
				"AND p.horario_final_funcionamento >= current_time THEN TRUE " +
				"ELSE FALSE " +
				"END AS acesso_permitido " +
				"FROM hosp.parametro p " +
				"WHERE p.codunidade = ?";

		Boolean acessoPermitido = false;

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, codigoUnidade);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				acessoPermitido = rs.getBoolean("acesso_permitido");
			}

		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return acessoPermitido;
	}

	public boolean verificarSeTemPermissaoAcessoHoje(FuncionarioBean usuarioLogado, String diaDeHoje) throws ProjetoException {


		String sql = "select "+ diaDeHoje +" as acesso_permitido from hosp.parametro p " +
				"	join hosp.unidade u on p.codunidade = u.id " +
				"	join acl.funcionarios f on u.id = f.codunidade " +
				"	where f.id_funcionario = ? and f.codunidade = ?";

		Boolean acessoPermitido = false;

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, usuarioLogado.getId());
			pstmt.setInt(2, usuarioLogado.getUnidade().getId());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				acessoPermitido = rs.getBoolean("acesso_permitido");
			}

		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return acessoPermitido;
	}

}
