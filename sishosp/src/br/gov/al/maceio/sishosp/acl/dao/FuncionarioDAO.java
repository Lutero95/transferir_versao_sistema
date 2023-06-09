package br.gov.al.maceio.sishosp.acl.dao;

import java.sql.*;
import java.time.ZoneId;
import java.util.*;
import java.util.Date;

import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.acl.model.*;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.BancoUtil;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactoryPublico;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.SessionUtil;
import br.gov.al.maceio.sishosp.comum.util.TratamentoErrosUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.dao.CboDAO;
import br.gov.al.maceio.sishosp.hosp.dao.ConselhoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.EspecialidadeDAO;
import br.gov.al.maceio.sishosp.hosp.dao.GrupoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.ProcedimentoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.ProgramaDAO;
import br.gov.al.maceio.sishosp.hosp.enums.ValidacaoSenha;
import br.gov.al.maceio.sishosp.hosp.model.*;

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
			pstmt.setString(2, BancoUtil.obterSenhaCriptografada(usuario.getSenha()));

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

	public FuncionarioBean verificaExisteCnsCadastrado(String cnsProfissional) throws ProjetoException {
		FuncionarioBean retorno = null;

		String sql = "select descfuncionario from acl.funcionarios where cns=?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);

			stmt.setString(1, cnsProfissional);

			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				retorno = new FuncionarioBean();
				retorno.setNome(rs.getString(1));
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
	
	public Boolean verificaSeRealizaEvolucaoFalta(Long id_funcionario) throws ProjetoException {

		String sql = "SELECT count(id_funcionario) as amount FROM acl.funcionarios WHERE id_funcionario = ? AND coalesce(realiza_evolucao_faltosos, FALSE) IS TRUE;";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);

			stmt.setLong(1, id_funcionario);

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				if(rs.getLong("amount") > 0) {
					return true;
				}
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
		return false;
	}

	public Boolean verificaSeRealizaAlteracaoLaudo(Long id_funcionario) throws ProjetoException {
		String sql = "SELECT count(id_funcionario) as amount FROM acl.funcionarios WHERE id_funcionario = ? AND coalesce(realiza_alteracao_laudo, FALSE) IS TRUE;";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);

			stmt.setLong(1, id_funcionario);

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				if(rs.getLong("amount") > 0) {
					return true;
				}
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
		return false;
	}

	public FuncionarioBean autenticarUsuario(FuncionarioBean usuario) throws ProjetoException {

		String sql = "select us.id_funcionario, us.descfuncionario, us.senha, us.email, permite_liberacao, permite_encaixe, us.realiza_atendimento, us.permite_autorizacao_laudo,"
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
				+ " coalesce(p.permite_agendamento_duplicidade, false) permite_agendamento_duplicidade, p.agenda_avulsa_valida_paciente_ativo , e.restringir_laudo_unidade, p.cpf_paciente_obrigatorio,  "
				+ " p.verifica_periodo_inicial_evolucao_programa, p.inicio_evolucao_unidade, us.realiza_auditoria, p.busca_automatica_cep_paciente, p.valida_dados_laudo_sigtap, "
				+ " p.capacidades_funcionais_pts_obrigatorio, p.objetivos_gerais_pts_obrigatorio, p.cid_agenda_obrigatorio, p.cid_paciente_terapia_obrigatorio, p.bpa_com_laudo_autorizado, "
				+ " p.bloquear_edicao_evolucao, p.bloquear_edicao_pts, us.excecao_evolucao_com_pendencia, p.bloqueia_por_pendencia_evolucao_anterior from acl.funcionarios us "
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
			
			String cpfFormatado = usuario.getCpf().replaceAll("[^0-9]", "");
			bloqueiaAcessoSeProfissionalEstaAfastadoOuDesligado(cpfFormatado, con);
			
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setString(1, cpfFormatado);
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
				funcionario.setPermiteAutorizacaoLaudo(rs.getBoolean("permite_autorizacao_laudo"));
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
				funcionario.getUnidade().getParametro().setCpfPacienteObrigatorio(rs.getBoolean("cpf_paciente_obrigatorio"));
				funcionario.getUnidade().getParametro().setVerificaPeriodoInicialEvolucaoPrograma(rs.getBoolean("verifica_periodo_inicial_evolucao_programa"));
				funcionario.getUnidade().getParametro().setInicioEvolucaoUnidade(rs.getDate("inicio_evolucao_unidade"));
				funcionario.getUnidade().getParametro().setBuscaAutomaticaCepPaciente(rs.getBoolean("busca_automatica_cep_paciente"));
				funcionario.getUnidade().getParametro().setCapacidadesFuncionaisPTSObrigatorio(rs.getBoolean("capacidades_funcionais_pts_obrigatorio"));
				funcionario.getUnidade().getParametro().setObjetivosGeraisPTSObrigatorio(rs.getBoolean("objetivos_gerais_pts_obrigatorio"));
				funcionario.getUnidade().getParametro().setValidaDadosLaudoSigtap(rs.getBoolean("valida_dados_laudo_sigtap"));
				funcionario.getUnidade().getParametro().setCidAgendaObrigatorio(rs.getBoolean("cid_agenda_obrigatorio"));
				funcionario.getUnidade().getParametro().setCidPacienteTerapiaObrigatorio(rs.getBoolean("cid_paciente_terapia_obrigatorio"));
				funcionario.getUnidade().getParametro().setBpaComLaudoAutorizado(rs.getBoolean("bpa_com_laudo_autorizado"));
				funcionario.getUnidade().getParametro().setBloquearEdicaoEvolucao(rs.getBoolean("bloquear_edicao_evolucao"));
				funcionario.getUnidade().getParametro().setBloquearEdicaoPTS(rs.getBoolean("bloquear_edicao_pts"));
				funcionario.getUnidade().setRestringirLaudoPorUnidade(rs.getBoolean("restringir_laudo_unidade"));
				funcionario.setRealizaAuditoria(rs.getBoolean("realiza_auditoria"));
				funcionario.setExcecaoEvolucaoComPendencia(rs.getBoolean("excecao_evolucao_com_pendencia"));
				funcionario.getUnidade().getParametro().setBloqueiaPorPendenciaEvolucaoAnterior(rs.getBoolean("bloqueia_por_pendencia_evolucao_anterior"));
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
				sistema.setUrl(rs.getString("url"));
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

		String sql = "\n" +
				"select\n" +
				"\tdistinct si.id as sid,\n" +
				"\tsi.descricao as sdesc,\n" +
				"\tpf.descricao pfdesc,\n" +
				"\tpm.id as pmid,\n" +
				"\tpm.descricao as pmdesc,\n" +
				"\t0 as funid,\n" +
				"\t'' as fundesc,\n" +
				"\t'' as funcodigo,\n" +
				"\tfalse as funativa,\n" +
				"\t0 as funidsis,\n" +
				"\tme.id as meid,\n" +
				"\tme.descricao as medesc,\n" +
				"\tme.desc_pagina,\n" +
				"\tme.diretorio,\n" +
				"\tme.extensao,\n" +
				"\tme.codigo as cod_menu,\n" +
				"\tme.indice,\n" +
				"\tme.tipo,\n" +
				"\tme.ativo as meativo,\n" +
				"\tme.action_rel,\n" +
				"\tme.onclick_rel\n" +
				"from\n" +
				"\tacl.perm_perfil pp\n" +
				"join acl.funcionarios us on\n" +
				"\tus.id_perfil = pp.id_perfil\n" +
				"join acl.perfil pf on\n" +
				"\tpf.id = pp.id_perfil\n" +
				"join acl.permissao pm on\n" +
				"\tpm.id = pp.id_permissao\n" +
				"join acl.perm_geral pg on\n" +
				"\tpg.id_permissao = pm.id\n" +
				"join acl.menu me on\n" +
				"\tme.id = pg.id_menu\n" +
				"join acl.menu_sistema ms on\n" +
				"\tms.id_menu = me.id\n" +
				"join acl.sistema si on\n" +
				"\tsi.id = ms.id_sistema\n" +
				"where\n" +
				"\tus.id_funcionario = ?\n" +
				"\tand me.ativo = true\n" +
				"union\n" +
				"select distinct \n" +
				"\tsi.id as sid,\n" +
				"\tsi.descricao as sdesc,\n" +
				"\tpf.descricao pfdesc,\n" +
				"\tpm.id pmid,\n" +
				"\tpm.descricao as pmdesc,\n" +
				"\tfun.id as funid,\n" +
				"\tfun.descricao as fundesc,\n" +
				"\tfun.codigo as funcodigo,\n" +
				"\tfun.ativa as funativa,\n" +
				"\tfun.id_sistema as fusidsis,\n" +
				"\t0 as meid,\n" +
				"\t'' as medesc,\n" +
				"\t'',\n" +
				"\t'',\n" +
				"\t'',\n" +
				"\t'' as cod_menu,\n" +
				"\t'',\n" +
				"\tcase\n" +
				"\t\twhen '' = '' then 'funcao'\n" +
				"\tend,\n" +
				"\tfalse as meativo,\n" +
				"\t'',\n" +
				"\t''\n" +
				"from\n" +
				"\tacl.perm_perfil pp\n" +
				"join acl.funcionarios us on\n" +
				"\tus.id_perfil = pp.id_perfil\n" +
				"join acl.perfil pf on\n" +
				"\tpf.id = pp.id_perfil\n" +
				"join acl.permissao pm on\n" +
				"\tpm.id = pp.id_permissao\n" +
				"join acl.perm_geral pg on\n" +
				"\tpg.id_permissao = pm.id\n" +
				"join acl.funcao fun on\n" +
				"\tfun.id = pg.id_funcao\n" +
				"join acl.sistema si on\n" +
				"\tsi.id = fun.id_sistema\n" +
				"where\n" +
				"\tus.id_funcionario = ?\n" +
				"\tand fun.ativa = true\n" +
				"union\n" +
				"select distinct \n" +
				"\tsi.id as sid,\n" +
				"\tsi.descricao as sdesc,\n" +
				"\tpf.descricao as pfdesc,\n" +
				"\tpm.id as pmid,\n" +
				"\tpm.descricao as pmdesc,\n" +
				"\t0 as funid,\n" +
				"\t'' as fundesc,\n" +
				"\t'' as funcodigo,\n" +
				"\tfalse as funativa,\n" +
				"\t0 as funidsis,\n" +
				"\tme.id as meid,\n" +
				"\tme.descricao as medesc,\n" +
				"\tme.desc_pagina,\n" +
				"\tme.diretorio,\n" +
				"\tme.extensao,\n" +
				"\tme.codigo as cod_menu,\n" +
				"\tme.indice,\n" +
				"\tme.tipo,\n" +
				"\tme.ativo as meativo,\n" +
				"\tme.action_rel,\n" +
				"\tme.onclick_rel\n" +
				"from\n" +
				"\tacl.perm_usuario pu\n" +
				"join acl.funcionarios us on\n" +
				"\tus.id_funcionario = pu.id_usuario\n" +
				"join acl.permissao pm on\n" +
				"\tpm.id = pu.id_permissao\n" +
				"join acl.perm_geral pg on\n" +
				"\tpg.id_permissao = pm.id\n" +
				"join acl.menu me on\n" +
				"\tme.id = pg.id_menu\n" +
				"join acl.menu_sistema ms on\n" +
				"\tms.id_menu = me.id\n" +
				"join acl.sistema si on\n" +
				"\tsi.id = ms.id_sistema\n" +
				"\tjoin acl.perfil pf on\n" +
				"\tpf.id = us.id_perfil \n" +
				"where\n" +
				"\tus.id_funcionario = ?\n" +
				"\tand me.ativo = true\n" +
				"union\n" +
				"select distinct \n" +
				"\tsi.id as sid,\n" +
				"\tsi.descricao as sdesc,\n" +
				"\tpf.descricao as pfdesc,\n" +
				"\tpm.id pmid,\n" +
				"\tpm.descricao as pmdesc,\n" +
				"\tfun.id as funid,\n" +
				"\tfun.descricao as fundesc,\n" +
				"\tfun.codigo as funcodigo,\n" +
				"\tfun.ativa as funativa,\n" +
				"\tfun.id_sistema as funidsis,\n" +
				"\t0 as meid,\n" +
				"\t'' as medesc,\n" +
				"\t'',\n" +
				"\t'',\n" +
				"\t'',\n" +
				"\t'' as cod_menu,\n" +
				"\t'',\n" +
				"\tcase\n" +
				"\t\twhen '' = '' then 'funcao'\n" +
				"\tend,\n" +
				"\tfalse as meativo,\n" +
				"\t'',\n" +
				"\t''\n" +
				"from\n" +
				"\tacl.perm_usuario pu\n" +
				"join acl.funcionarios us on\n" +
				"\tus.id_funcionario = pu.id_usuario\n" +
				"join acl.permissao pm on\n" +
				"\tpm.id = pu.id_permissao\n" +
				"join acl.perm_geral pg on\n" +
				"\tpg.id_permissao = pm.id\n" +
				"join acl.funcao fun on\n" +
				"\tfun.id = pg.id_funcao\n" +
				"join acl.sistema si on\n" +
				"\tsi.id = fun.id_sistema\n" +
				"\tjoin acl.perfil pf on\n" +
				"\tpf.id = us.id_perfil \t\n" +
				"where\n" +
				"\tus.id_funcionario = ?\n" +
				"\tand fun.ativa = true\n" +
				"order by\n" +
				"\tmedesc,\n" +
				"\tsid;";

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
		String sql = "update acl.funcionarios set descfuncionario = ?, cpf = ?, email = ?, "
				+ "senha = ?, id_perfil = ?, ativo = ?, codunidade = ? where id_funcionario = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, usuario.getNome());
			stmt.setString(2, usuario.getCpf().replaceAll("[^0-9]", ""));
			stmt.setString(3, usuario.getEmail());
			stmt.setString(4, BancoUtil.obterSenhaCriptografada(usuario.getSenha()));
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
			stmt.setString(4, BancoUtil.obterSenhaCriptografada(usuario.getSenha()));
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
				//comentado walter erro log ex.printStackTrace();
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
				//comentado walter erro log ex.printStackTrace();
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
			stmt.setString(1, BancoUtil.obterSenhaCriptografada(usuario.getNovaSenha()));
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
			stmt.setString(1, BancoUtil.obterSenhaCriptografada(usuario.getNovaSenha()));
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

	public boolean gravarProfissional(FuncionarioBean profissional, ArrayList<ProgramaBean> lista)
			throws ProjetoException {

		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().get("obj_funcionario");

		Boolean retorno = false;

		String sql = "INSERT INTO acl.funcionarios(descfuncionario, cpf, senha, log_user, codespecialidade, cns, "
				+ " codprocedimentopadrao, ativo, realiza_atendimento, datacriacao, primeiroacesso, id_perfil, codunidade, "
				+ "permite_liberacao, permite_encaixe, excecao_bloqueio_horario, permite_autorizacao_laudo, realiza_auditoria, id_conselho, "
				+ "numero_conselho, excecao_evolucao_com_pendencia, realiza_evolucao_faltosos, realiza_alteracao_laudo) "
				+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, current_timestamp, false, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning id_funcionario;";
		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);

			ps.setString(1, profissional.getNome().toUpperCase());
			ps.setString(2, profissional.getCpf().replaceAll("[^0-9]", ""));
			ps.setString(3, BancoUtil.obterSenhaCriptografada(profissional.getSenha()));
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

			if (VerificadorUtil.verificarSeObjetoNuloOuVazio(profissional.getCns())) {
				ps.setNull(6, Types.NULL);
			} else {
				ps.setString(6, profissional.getCns().toUpperCase());
			}


			if (profissional.getProc1() != null) {
				if (profissional.getProc1().getIdProc() == null) {
					ps.setNull(7, Types.NULL);
				} else {
					ps.setInt(7, profissional.getProc1().getIdProc());
				}
			} else {
				ps.setNull(7, Types.NULL);
			}

			ps.setString(8, profissional.getAtivo());
			ps.setBoolean(9, profissional.getRealizaAtendimento());
			ps.setLong(10, profissional.getPerfil().getId());
			ps.setInt(11, profissional.getUnidade().getId());
			ps.setBoolean(12, profissional.getRealizaLiberacoes());
			ps.setBoolean(13, profissional.getRealizaEncaixes());
			ps.setBoolean(14, profissional.getExcecaoBloqueioHorario());
			ps.setBoolean(15, profissional.getPermiteAutorizacaoLaudo());
			ps.setBoolean(16, profissional.getRealizaAuditoria());
			
			if(VerificadorUtil.verificarSeObjetoNulo(profissional.getConselho()) 
					|| VerificadorUtil.verificarSeObjetoNuloOuZero(profissional.getConselho().getId())) {
				ps.setNull(17, Types.NULL);
			} else {
				ps.setInt(17, profissional.getConselho().getId());
			}
			
			ps.setString(18, profissional.getNumeroConselho());
			ps.setBoolean(19, profissional.isExcecaoEvolucaoComPendencia());
			ps.setBoolean(20, profissional.getRealizaEvolucaoFaltosos());
			ps.setBoolean(21, profissional.getRealizaAlteracaoLaudo());
			
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				profissional.setId(rs.getLong("id_funcionario"));
			}

			for (int i = 0; i < profissional.getListaUnidades().size(); i++) {
				String sql2 = "INSERT INTO hosp.funcionario_unidades (cod_unidade, cod_funcionario) VALUES (?, ?);";
				ps = con.prepareStatement(sql2);

				ps.setInt(1, profissional.getListaUnidades().get(i).getId());
				ps.setLong(2, profissional.getId());

				ps.executeUpdate();
			}

			for (int i = 0; i < lista.size(); i++) {
				String sql3 = "INSERT INTO hosp.profissional_programa_grupo(codprofissional, codprograma, codgrupo) VALUES (?, ?, ?);";
				ps = con.prepareStatement(sql3);

				ps.setLong(1, profissional.getId());
				ps.setInt(2, lista.get(i).getIdPrograma());
				ps.setInt(3, lista.get(i).getGrupoBean().getIdGrupo());

				ps.executeUpdate();
			}

			String sql4 = "insert into acl.perm_usuario (id_usuario, id_permissao) values (?, ?)";
			List<Long> listaPerm = profissional.getListaIdPermissoes();
			ps = con.prepareStatement(sql4);
			for (Long idPerm : listaPerm) {
				ps.setLong(1, profissional.getId());
				ps.setLong(2, idPerm);
				ps.execute();
			}

			String sql5 = "insert into acl.perm_sistema (id_usuario, id_sistema) values (?, ?)";
			List<Integer> listaIdSistemas = profissional.getListaIdSistemas();
			ps = con.prepareStatement(sql5);
			for (Integer idSistema : listaIdSistemas) {
				ps.setLong(1, profissional.getId());
				ps.setLong(2, idSistema);
				ps.execute();
			}

			retorno = gravarProfissionalBancoPublico(profissional, profissional.getId().intValue());
			if(profissional.getRealizaAtendimento()) {
				gravarCbosProfissional(profissional, con);
			}

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
			ps.setString(3, BancoUtil.obterSenhaCriptografada(profissional.getSenha()));
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

	public void gravarCbosProfissional (FuncionarioBean funcionario, Connection conexaoAuxiliar)
			throws ProjetoException, SQLException {

		String sql = "INSERT INTO hosp.cbo_funcionario (id_cbo, id_profissional) VALUES(?, ?);";
		try {

			PreparedStatement ps = conexaoAuxiliar.prepareStatement(sql);
			for (CboBean cbo : funcionario.getListaCbos()) {
				ps.setInt(1, cbo.getCodCbo());
				ps.setLong(2, funcionario.getId());
				ps.executeUpdate();
			}
		} catch (SQLException sqle) {
			conexaoAuxiliar.rollback();
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			conexaoAuxiliar.rollback();
			throw new ProjetoException(ex, this.getClass().getName());
		}
	}

	public void removerCbosProfissional(Long idProfissional, Connection conexaoAuxiliar)
			throws ProjetoException, SQLException {

		String sql = "DELETE FROM hosp.cbo_funcionario WHERE id_profissional = ?;";
		try {
			PreparedStatement ps = conexaoAuxiliar.prepareStatement(sql);
			ps.setLong(1, idProfissional);
			ps.executeUpdate();
		} catch (SQLException sqle) {
			conexaoAuxiliar.rollback();
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(),
					sqle);
		} catch (Exception ex) {
			conexaoAuxiliar.rollback();
			throw new ProjetoException(ex, this.getClass().getName());
		}
	}

	private void removerProfissionalPermissaoSistema(Long idProfissional, Connection conexaoAuxiliar)
			throws ProjetoException, SQLException {

		String sql = "DELETE FROM acl.perm_sistema WHERE id_usuario = ?;";
		try {
			PreparedStatement ps = conexaoAuxiliar.prepareStatement(sql);
			ps.setLong(1, idProfissional);
			ps.executeUpdate();
		} catch (SQLException sqle) {
			conexaoAuxiliar.rollback();
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(),
					sqle);
		} catch (Exception ex) {
			conexaoAuxiliar.rollback();
			throw new ProjetoException(ex, this.getClass().getName());
		}
	}

	public List<FuncionarioBean> listarProfissionalBusca(String descricaoBusca, Integer tipoBuscar)
			throws ProjetoException {

		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().get("obj_funcionario");

		List<FuncionarioBean> lista = new ArrayList<>();
		String sql = "SELECT f.id_funcionario, f.id_funcionario ||'-'|| f.descfuncionario AS descfuncionario, f.codespecialidade, e.descespecialidade, "
				+ "f.cns, f.ativo, f.codprocedimentopadrao, p.nome, f.permite_liberacao, permite_encaixe "
				+ " FROM acl.funcionarios f "
				+ "LEFT JOIN hosp.especialidade e ON (f.codespecialidade = e.id_especialidade) "
				+ "LEFT JOIN hosp.proc p ON (f.codprocedimentopadrao = p.id) ";

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
				funcionario.getProc1().setNomeProc(rs.getString("nome"));
				funcionario.getProc1().setIdProc(rs.getInt("codprocedimentopadrao"));
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
	
	public List<FuncionarioBean> listarProfissionalAtivoBusca(String descricaoBusca, Integer tipoBuscar)
			throws ProjetoException {

		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().get("obj_funcionario");

		List<FuncionarioBean> lista = new ArrayList<>();
		String sql = "SELECT f.id_funcionario, f.id_funcionario ||'-'|| f.descfuncionario AS descfuncionario, "
				+ "f.cns, f.ativo, f.codprocedimentopadrao, f.permite_liberacao, permite_encaixe "
				+ " FROM acl.funcionarios f where upper(f.id_funcionario ||' - '|| f.descfuncionario) LIKE ? and ativo = 'S' ";

		if (tipoBuscar == 1) {
			sql += " and f.realiza_atendimento is true and f.codunidade = ? order by f.descfuncionario";
		}


		if (tipoBuscar == 2) {
			sql += " and f.codunidade = ? order by f.descfuncionario";
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
				funcionario.setCns(rs.getString("cns"));
				funcionario.setAtivo(rs.getString("ativo"));
				funcionario.getProc1().setIdProc(rs.getInt("codprocedimentopadrao"));
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

		sql.append("SELECT DISTINCT h.id_funcionario,  h.descfuncionario AS descfuncionario, h.codespecialidade, h.permite_liberacao, ");
		sql.append("h.permite_encaixe, h.cns, h.ativo, h.codprocedimentopadrao, e.descespecialidade, p.nome AS descproc ");
		sql.append("FROM acl.funcionarios h ");
		sql.append("JOIN hosp.profissional_programa_grupo ppg ON (h.id_funcionario = ppg.codprofissional) ");
		sql.append("LEFT JOIN hosp.especialidade e ON (e.id_especialidade = h.codespecialidade) ");
		sql.append("LEFT JOIN hosp.proc p ON (p.id = h.codprocedimentopadrao) ");
		sql.append("WHERE UPPER(id_funcionario ||' - '|| descfuncionario) LIKE ? AND ppg.codgrupo = ? AND h.ativo = 'S' ");
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

	public HashMap<Long, String> mapFuncionariosAfastados(Date dataAtendimento) throws ProjetoException {

		HashMap<Long, String> ids = new HashMap<>();

		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().get("obj_funcionario");

		String sql = "select af.id_funcionario_afastado, af.motivo_afastamento \n" +
				"from adm.afastamento_funcionario af\n" +
				"where ? between af.inicio_afastamento and af.fim_afastamento ";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setTimestamp(1, new Timestamp(dataAtendimento.getTime()));
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				String motivo = "";
				switch (rs.getString("motivo_afastamento")){
					case "FA":
						motivo = "Falta";
						break;
					case "LM":
						motivo = "Licença Médica";
						break;
					case "FE":
						motivo = "Férias";
						break;
					case "DE":
						motivo = "Desligamento";
						break;
				}

				ids.put(rs.getLong("id_funcionario_afastado"), motivo);
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
		return ids;
	}

	public List<FuncionarioBean> listarProfissionalAtendimento() throws ProjetoException {

		List<FuncionarioBean> listaProf = new ArrayList<FuncionarioBean>();

		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().get("obj_funcionario");

		String sql = " select distinct a.* from ( select distinct id_funcionario, descfuncionario, codespecialidade,e.descespecialidade, cns, funcionarios.ativo, \n" +
				" codprocedimentopadrao, p.nome descprocpadrao, cpf, senha, realiza_atendimento, id_perfil, permite_liberacao, permite_encaixe \n" +
				" from acl.funcionarios left join hosp.especialidade e on e.id_especialidade = funcionarios.codespecialidade \n" +
				"left join hosp.proc p on p.id = funcionarios.codprocedimentopadrao where funcionarios.ativo='S' and funcionarios.codunidade =? AND realiza_atendimento IS TRUE " +
				"union all\n" +
				" select distinct id_funcionario, descfuncionario, codespecialidade,e.descespecialidade, cns, funcionarios.ativo, \n" +
				" codprocedimentopadrao, p.nome descprocpadrao, cpf, senha, realiza_atendimento, id_perfil, permite_liberacao, permite_encaixe \n" +
				" from acl.funcionarios left join hosp.especialidade e on e.id_especialidade = funcionarios.codespecialidade \n" +
				"left join hosp.proc p on p.id = funcionarios.codprocedimentopadrao \n" +
				"join hosp.funcionario_unidades fu on fu.cod_funcionario = funcionarios.id_funcionario\n" +
				"where  funcionarios.ativo='S' and fu.cod_unidade=?  AND realiza_atendimento IS TRUE " +
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
				"select distinct id_funcionario, descfuncionario, codespecialidade, e.descespecialidade,  cns, funcionarios.ativo, \n" +
				" codprocedimentopadrao, p.nome descprocedimentopadrao, cpf, senha, realiza_atendimento, id_perfil, permite_liberacao, permite_encaixe, unidade.nome nomeunidade \n" +
				" from acl.funcionarios join hosp.unidade on unidade.id = funcionarios.codunidade \n" +
				" left join hosp.especialidade e on e.id_especialidade = funcionarios.codespecialidade \n" +
				" left join hosp.proc p on p.id = funcionarios.codprocedimentopadrao\n" +
				" where coalesce(admin,false) is false \n" +
				" union\n" +
				" select distinct id_funcionario, descfuncionario, codespecialidade, e.descespecialidade,  cns, funcionarios.ativo,  \n" +
				" codprocedimentopadrao, p.nome descprocedimentopadrao, cpf, senha, realiza_atendimento, id_perfil, permite_liberacao, permite_encaixe, unidade.nome nomeunidade \n" +
				" from acl.funcionarios join hosp.unidade on unidade.id = funcionarios.codunidade \n" +
				" left join hosp.especialidade e on e.id_especialidade = funcionarios.codespecialidade \n" +
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
	
	public List<FuncionarioBean> listarTodosOsProfissionaisAtivos() throws ProjetoException {

		List<FuncionarioBean> listaProfissional = new ArrayList<FuncionarioBean>();

		String sql = "select distinct id_funcionario, descfuncionario, codespecialidade,  cns, funcionarios.ativo, \n" +
				" cpf, senha, realiza_atendimento, id_perfil, permite_liberacao, permite_encaixe \n" +
				" from acl.funcionarios join hosp.unidade on unidade.id = funcionarios.codunidade \n" +
				" where funcionarios.ativo = 'S' order by descfuncionario";
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
				profissioanl.setCns(rs.getString("cns"));
				profissioanl.setAtivo(rs.getString("ativo"));
				profissioanl.getPerfil().setId(rs.getLong("id_perfil"));
				profissioanl.setRealizaLiberacoes(rs.getBoolean("permite_liberacao"));
				profissioanl.setRealizaEncaixes(rs.getBoolean("permite_encaixe"));

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
		String sql = "select distinct m.id_funcionario, m.descfuncionario, m.codespecialidade, e.descespecialidade, m.cns\n"
				+ "	from acl.funcionarios m \n"
				+ "                join hosp.profissional_programa_grupo ppg on (m.id_funcionario = ppg.codprofissional)\n"
				+ "                 left join hosp.especialidade e on (e.id_especialidade = m.codespecialidade)\n"
				+ "                 where m.ativo = 'S' and realiza_atendimento is true and ppg.codgrupo = ? order by descfuncionario";
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

	public boolean desativarProfissional(Long idProfissional) throws ProjetoException {

		Boolean retorno = false;

		String sql1 = "update acl.funcionarios set ativo = 'N' where id_funcionario = ?";

		try {

			con = ConnectionFactory.getConnection();

			ps = con.prepareStatement(sql1);
			ps.setLong(1, idProfissional);
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

	public boolean alterarProfissional
			(FuncionarioBean profissional, ArrayList<ProgramaBean> lista) throws ProjetoException {

		user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().get("obj_usuario");
		
		Boolean retorno = false;
		String sql = "update acl.funcionarios set descfuncionario = ?, codespecialidade = ?, cns = ?, ativo = ?,"
				+ " codprocedimentopadrao = ?, id_perfil = ?, permite_liberacao = ?, realiza_atendimento = ?, permite_encaixe = ?, cpf=?, "
				+ " codunidade=?, excecao_bloqueio_horario=?, permite_autorizacao_laudo=?, realiza_auditoria=?, usuario_alteracao = ?, "
				+ " datahora_alteracao = CURRENT_TIMESTAMP , id_conselho = ?, numero_conselho = ?, excecao_evolucao_com_pendencia = ?, "
		        + " realiza_evolucao_faltosos = ?, realiza_alteracao_laudo = ? where id_funcionario = ?";

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

			if (profissional.getProc1() != null) {
				if (profissional.getProc1().getCodProc() != null) {
					stmt.setInt(5, profissional.getProc1().getIdProc());
				} else {
					stmt.setNull(5, Types.NULL);
				}
			} else {
				stmt.setNull(5, Types.NULL);
			}

			stmt.setLong(6, profissional.getPerfil().getId());
			stmt.setBoolean(7, profissional.getRealizaLiberacoes());
			stmt.setBoolean(8, profissional.getRealizaAtendimento());
			stmt.setBoolean(9, profissional.getRealizaEncaixes());
			stmt.setString(10, profissional.getCpf().replaceAll("[^0-9]", ""));
			stmt.setLong(11, profissional.getUnidade().getId());
			stmt.setBoolean(12, profissional.getExcecaoBloqueioHorario());
			stmt.setBoolean(13, profissional.getPermiteAutorizacaoLaudo());
			stmt.setBoolean(14, profissional.getRealizaAuditoria());
			stmt.setLong(15, user_session.getId());
			
			if(VerificadorUtil.verificarSeObjetoNulo(profissional.getConselho()) 
					|| VerificadorUtil.verificarSeObjetoNuloOuZero(profissional.getConselho().getId())) {
				stmt.setNull(16, Types.NULL);
			} else {
				stmt.setInt(16, profissional.getConselho().getId());
			}
			stmt.setString(17, profissional.getNumeroConselho());
			stmt.setBoolean(18, profissional.isExcecaoEvolucaoComPendencia());
			stmt.setBoolean(19, profissional.getRealizaEvolucaoFaltosos());
			stmt.setBoolean(20, profissional.getRealizaAlteracaoLaudo());
			stmt.setLong(21, profissional.getId());

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

			removerCbosProfissional(profissional.getId(), con);
			if(profissional.getRealizaAtendimento()) {
				gravarCbosProfissional(profissional, con);
			}

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
		String sql = "UPDATE acl.funcionarios SET ativo = ? , descfuncionario=? , cpf=? WHERE id_funcionario = ? and banco_acesso=?";

		try {
			conexaoPublica = ConnectionFactoryPublico.getConnection();
			PreparedStatement stmt = conexaoPublica.prepareStatement(sql);

			//stmt.setString(1, BancoUtil.obterSenhaCriptografada(profissional.getSenha()));
			stmt.setString(1, profissional.getAtivo().toUpperCase());
			stmt.setString(2, profissional.getNome());
			stmt.setString(3, profissional.getCpf().replaceAll("[^0-9]", ""));
			stmt.setLong(4, profissional.getId());
			stmt.setString(5, (String) SessionUtil.resgatarDaSessao("nomeBancoAcesso"));

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

		String sql = "select id_funcionario, descfuncionario, codespecialidade, cns, ativo, codprocedimentopadrao, permite_autorizacao_laudo, id_conselho, excecao_evolucao_com_pendencia, "
				+ " cpf, senha, realiza_atendimento, id_perfil, codunidade, permite_liberacao, permite_encaixe, excecao_bloqueio_horario, realiza_auditoria, numero_conselho, realiza_evolucao_faltosos, "
				+ " realiza_alteracao_laudo from acl.funcionarios where id_funcionario = ?  order by descfuncionario";

		ConselhoDAO conselhoDAO = new ConselhoDAO();
		
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
				profissional.setProc1(procDao.listarProcedimentoPorId(rs.getInt("codprocedimentopadrao")));
				profissional.getPerfil().setId(rs.getLong("id_perfil"));
				profissional.getUnidade().setId(rs.getInt("codunidade"));
				profissional.setRealizaLiberacoes(rs.getBoolean("permite_liberacao"));
				profissional.setRealizaEncaixes(rs.getBoolean("permite_encaixe"));
				profissional.setExcecaoBloqueioHorario(rs.getBoolean("excecao_bloqueio_horario"));
				profissional.setPermiteAutorizacaoLaudo(rs.getBoolean("permite_autorizacao_laudo"));
				profissional.setRealizaAuditoria(rs.getBoolean("realiza_auditoria"));
				profissional.setConselho(conselhoDAO.buscaConselhoPorId(rs.getInt("id_conselho")));
				profissional.setNumeroConselho(rs.getString("numero_conselho"));
				profissional.setExcecaoEvolucaoComPendencia(rs.getBoolean("excecao_evolucao_com_pendencia"));
				profissional.setRealizaEvolucaoFaltosos(rs.getBoolean("realiza_evolucao_faltosos"));
				profissional.setRealizaAlteracaoLaudo(rs.getBoolean("realiza_alteracao_laudo"));
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

	public List<CboBean> buscarCbosFuncionario(Integer idFuncionario) throws ProjetoException {
		List<CboBean> listaCbos = new ArrayList<>();

		String sql = "select c.id, c.descricao, c.codigo " +
				"from hosp.cbo_funcionario cf join hosp.cbo c on cf.id_cbo = c.id " +
				"where cf.id_profissional = ?;";

		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, idFuncionario);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				CboBean cbo = new CboBean();
				mapearResultSetCbo(cbo, rs);
				listaCbos.add(cbo);
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
		return listaCbos;
	}

	public FuncionarioBean buscarProfissionalPorIdParaConverter(Integer id) throws ProjetoException {
		FuncionarioBean profissional = null;

		String sql = "select id_funcionario, descfuncionario, codespecialidade, e.descespecialidade , cns, ativo, codprocedimentopadrao, "
				+ " cpf, senha, realiza_atendimento, id_perfil, codunidade, permite_liberacao, permite_encaixe, realiza_evolucao_faltosos "
				+ " from acl.funcionarios left join hosp.especialidade e on e.id_especialidade = funcionarios.codespecialidade "
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
				profissional.setListaCbos(listarCbosProfissional(profissional.getId(), con));
				profissional.setExcecaoEvolucaoComPendencia(rs.getBoolean("realiza_evolucao_faltosos"));
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
		if (tipoValidacao.equals(ValidacaoSenha.EVOLUCAO_FALTA.getSigla())) {
			sql = sql + " AND coalesce(realiza_evolucao_faltosos, FALSE) IS TRUE;";
		}
		if (tipoValidacao.equals(ValidacaoSenha.ALTERACAO_LAUDO.getSigla())) {
			sql = sql + " AND coalesce(realiza_alteracao_laudo, FALSE) IS TRUE;";
		}

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);

			stmt.setString(1, cpf.replaceAll("[^0-9]", ""));
			stmt.setString(2, BancoUtil.obterSenhaCriptografada(senha));

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
			stmt.setString(2, BancoUtil.obterSenhaCriptografada(senha));

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

	public List<CboBean> listarCbosProfissional(Long idFuncionario, Connection conexaoAuxiliar)
			throws ProjetoException, SQLException {

		String sql = "select c.id, c.descricao, c.codigo from hosp.cbo_funcionario cf " +
				" join hosp.cbo c on cf.id_cbo = c.id " +
				" where cf.id_profissional = ?;";

		List<CboBean> listaCbos = new ArrayList<>();

		try {
			PreparedStatement ps = conexaoAuxiliar.prepareStatement(sql);
			ps.setLong(1, idFuncionario);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				CboBean cbo = new CboBean();
				mapearResultSetCbo(cbo, rs);
				listaCbos.add(cbo);
			}

		} catch (SQLException sqle) {
			conexaoAuxiliar.rollback();
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			conexaoAuxiliar.rollback();
			throw new ProjetoException(ex, this.getClass().getName());
		}
		return listaCbos;
	}

	public CboBean buscaCboCompativelComProcedimento
			(Date data, Integer idProcedimento, Long idFuncionario, Connection conexaoAuxiliar)
			throws ProjetoException, SQLException {

		String sql = "select cbo.id, cbo.descricao, cbo.codigo from hosp.cbo_funcionario cf " +
				"	join hosp.cbo cbo on cf.id_cbo = cbo.id " +
				"	join sigtap.cbo_procedimento_mensal cpm on cf.id_cbo = cpm.id_cbo " +
				"	join sigtap.procedimento_mensal pm on cpm.id_procedimento_mensal = pm.id " +
				"	join hosp.proc p on pm.id_procedimento = p.id " +
				"	join sigtap.historico_consumo_sigtap hcs on pm.id_historico_consumo_sigtap = hcs.id " +
				"	join acl.funcionarios f on cf.id_profissional = f.id_funcionario " +
				"	where p.ativo = 'S' and hcs.status = 'A' and hcs.mes = ? and hcs.ano = ? and p.id = ? and f.id_funcionario = ? "+
				"   order by id limit 1;";

		CboBean cbo = new CboBean();
		try {
			PreparedStatement ps = null;
			ps = conexaoAuxiliar.prepareStatement(sql);

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(data);
			int mes = calendar.get(Calendar.MONDAY) + 1;
			ps.setInt(1, mes);
			ps.setInt(2, calendar.get(Calendar.YEAR));
			ps.setInt(3, idProcedimento);
			ps.setLong(4, idFuncionario);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				mapearResultSetCbo(cbo, rs);
			}

		} catch (SQLException sqle) {
			conexaoAuxiliar.rollback();
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			conexaoAuxiliar.rollback();
			throw new ProjetoException(ex, this.getClass().getName());
		}
		return cbo;
	}

	public boolean funcionarioPossuiCboCompativelComProcedimento(Date data, Integer idProcedimento, Long idFuncionario)
			throws ProjetoException {

		String sql = "select exists (select cbo.id, cbo.descricao, cbo.codigo from hosp.cbo_funcionario cf "
				+ "	join hosp.cbo cbo on cf.id_cbo = cbo.id "
				+ "	join sigtap.cbo_procedimento_mensal cpm on cf.id_cbo = cpm.id_cbo "
				+ "	join sigtap.procedimento_mensal pm on cpm.id_procedimento_mensal = pm.id "
				+ "	join hosp.proc p on pm.id_procedimento = p.id "
				+ "	join sigtap.historico_consumo_sigtap hcs on pm.id_historico_consumo_sigtap = hcs.id "
				+ "	join acl.funcionarios f on cf.id_profissional = f.id_funcionario "
				+ "	where p.ativo = 'S' and hcs.status = 'A' and hcs.mes = ? and hcs.ano = ? and p.id = ? and f.id_funcionario = ? "
				+ "   order by id limit 1) existe;";

		boolean existe = false;
		try {
			PreparedStatement ps = null;
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(data);
			int mes = calendar.get(Calendar.MONDAY) + 1;
			ps.setInt(1, mes);
			ps.setInt(2, calendar.get(Calendar.YEAR));
			ps.setInt(3, idProcedimento);
			ps.setLong(4, idFuncionario);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				existe = rs.getBoolean("existe");
			}

		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(),
					sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception e) {
				//comentado walter erro log ex.printStackTrace();
			}
		}
		return existe;
	}


	public CboBean retornaPrimeiroCboProfissional(Long idFuncionario) throws ProjetoException {

		String sql = "select c.id, c.descricao, c.codigo from hosp.cbo_funcionario cf " +
				" join hosp.cbo c on cf.id_cbo = c.id " +
				" where cf.id_profissional = ? order by c.id limit 1;";

		CboBean cbo = new CboBean();

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setLong(1, idFuncionario);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				mapearResultSetCbo(cbo, rs);
			}
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception e) {
				//comentado walter erro log ex.printStackTrace();
			}
		}
		return cbo;
	}

	public List<CboBean> listaCbosProfissional(Long idFuncionario) throws ProjetoException {

		String sql = "select c.id, c.descricao, c.codigo from hosp.cbo_funcionario cf " +
				" join hosp.cbo c on cf.id_cbo = c.id " +
				" where cf.id_profissional = ? order by c.descricao;";
		List<CboBean> listaCbos = new ArrayList<>();

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setLong(1, idFuncionario);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				CboBean cbo = new CboBean();
				mapearResultSetCbo(cbo, rs);
				listaCbos.add(cbo);
			}
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception e) {
				//comentado walter erro log ex.printStackTrace();
			}
		}
		return listaCbos;
	}

	public List<CboBean> listaCbosProfissionalComMesmaConexao(Long idFuncionario, Connection conexaoAuxiliar) throws ProjetoException, SQLException {

		String sql = "select c.id, c.descricao, c.codigo from hosp.cbo_funcionario cf " +
				" join hosp.cbo c on cf.id_cbo = c.id " +
				" where cf.id_profissional = ? order by c.descricao;";
		List<CboBean> listaCbos = new ArrayList<>();

		try {
			PreparedStatement ps = conexaoAuxiliar.prepareStatement(sql);
			ps.setLong(1, idFuncionario);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				CboBean cbo = new CboBean();
				mapearResultSetCbo(cbo, rs);
				listaCbos.add(cbo);
			}
		} catch (SQLException sqle) {
			conexaoAuxiliar.rollback();
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			conexaoAuxiliar.rollback();
			throw new ProjetoException(ex, this.getClass().getName());
		}
		return listaCbos;
	}

	private void mapearResultSetCbo(CboBean cbo, ResultSet rs) throws SQLException {
		cbo.setCodCbo(rs.getInt("id"));
		cbo.setDescCbo(rs.getString("descricao"));
		cbo.setCodigo(rs.getString("codigo"));
	}

	public boolean cboCompativelComProfissional (Integer idCbo, Long idFuncionario) throws ProjetoException {

		String sql = "select exists (select cf.id from hosp.cbo_funcionario cf " +
				"	where cf.id_cbo = ? and cf.id_profissional = ?) cbo_compativel_profissional ";

		boolean cboCompativel = false;

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, idCbo);
			ps.setLong(2, idFuncionario);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				cboCompativel = rs.getBoolean("cbo_compativel_profissional");
			}
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception e) {
				//comentado walter erro log ex.printStackTrace();
			}
		}
		return cboCompativel;
	}

	public FuncionarioBean buscarProfissionalPorCns(String cns) throws ProjetoException {
		FuncionarioBean profissional = null;

		String sql = "select id_funcionario, descfuncionario, codespecialidade, cns "
				+ " from acl.funcionarios where cns = ?  order by descfuncionario";

		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, cns);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				profissional = new FuncionarioBean();
				profissional.setId(rs.getLong("id_funcionario"));
				profissional.setNome(rs.getString("descfuncionario"));
				profissional.setEspecialidade(espDao.listarEspecialidadePorId(rs.getInt("codespecialidade")));
				profissional.setCns(rs.getString("cns"));
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

	public boolean gravarNovaSenhaProfissional(Long idProfissional, String novaSenha)
			throws ProjetoException {

		Boolean retorno = false;

		String sql = "UPDATE acl.funcionarios SET senha= ? WHERE id_funcionario = ?;";
		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);

			ps.setString(1, BancoUtil.obterSenhaCriptografada(novaSenha));
			ps.setLong(2, idProfissional);
			ps.executeUpdate();

			retorno = gravarNovaSenhaProfissionalBancoPublico(idProfissional, novaSenha);

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

	private boolean gravarNovaSenhaProfissionalBancoPublico(Long idProfissional, String novaSenha) throws ProjetoException {

		Boolean retorno = false;
		Connection conexaoPublica = null;

		String sql = "UPDATE acl.funcionarios SET senha= ? WHERE id_funcionario = ? and banco_acesso = ?;";

		try {
			conexaoPublica = ConnectionFactoryPublico.getConnection();
			PreparedStatement ps = conexaoPublica.prepareStatement(sql);

			ps.setString(1, BancoUtil.obterSenhaCriptografada(novaSenha));
			ps.setLong(2, idProfissional);
			ps.setString(3, (String) SessionUtil.resgatarDaSessao("nomeBancoAcesso"));
			ps.executeUpdate();

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
	
	public void bloqueiaAcessoSeProfissionalEstaAfastadoOuDesligado(String cpf, Connection conexaoAuxiliar)
			throws ProjetoException, SQLException {

		String sql = "select f.ativo,\r\n" +
				"	case when current_date between af.inicio_afastamento and af.fim_afastamento\r\n" +
				"		then 'S'\r\n" +
				"		else 'N'\r\n" +
				"		end as afastado\r\n" +
				"	from acl.funcionarios f \r\n" +
				"	left join adm.afastamento_funcionario af on af.id_funcionario_afastado = f.id_funcionario \r\n" +
				"	left join hosp.unidade u on f.codunidade = u.id \r\n" +
				"	left join hosp.parametro p on p.codunidade = u.id \r\n" +
				"	where af.motivo_afastamento != 'FA' and f.cpf = ? and \r\n" +
				"		( (current_date between af.inicio_afastamento and af.fim_afastamento)\r\n" +
				"		or f.ativo = 'N') and p.bloquear_acesso_em_afastamento = true \r\n" +
				"		order by af.fim_afastamento desc	limit 1;";

		try {
			PreparedStatement ps = conexaoAuxiliar.prepareStatement(sql);
			ps.setString(1, cpf);
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				if (rs.getString("ativo").equals("N")) {
					JSFUtil.adicionarMensagemAdvertencia
						("Não é possível entrar com este usuário pois ele está desligado", "");
					conexaoAuxiliar.close();
				}
				else if (rs.getString("afastado").equals("S")) {
					JSFUtil.adicionarMensagemAdvertencia
						("Não é possível entrar com este usuário pois ele está afastado", "");
					conexaoAuxiliar.close();
				}
			}

		} catch (SQLException sqle) {
			conexaoAuxiliar.rollback();
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			conexaoAuxiliar.rollback();
			throw new ProjetoException(ex, this.getClass().getName());
		}
	}

}
