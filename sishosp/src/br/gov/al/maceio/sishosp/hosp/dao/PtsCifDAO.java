package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.enums.TipoCabecalho;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.TratamentoErrosUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.model.CompetenciaPtsCifBean;
import br.gov.al.maceio.sishosp.hosp.model.ObjetivoPtsCifBean;
import br.gov.al.maceio.sishosp.hosp.model.PtsCifBean;
import br.gov.al.maceio.sishosp.hosp.model.AvaliadorPtsCifBean;


public class PtsCifDAO {

	private final Integer SEIS_MESES_VENCIMENTO = 180;

	public ArrayList<CompetenciaPtsCifBean> listarCompetencias() throws ProjetoException{
		String sql = "select c.id, c.descricao, c.data_inicial, c.data_final, c.ativa from hosp.competencia_ptscif c";

		ArrayList<CompetenciaPtsCifBean> lista = new ArrayList<>();

		Connection conexao = null;
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement ps = conexao.prepareStatement(sql);


			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				CompetenciaPtsCifBean competencia = new CompetenciaPtsCifBean();
				competencia.setId(rs.getInt("id"));
				competencia.setDescricao(rs.getString("descricao"));
				competencia.setDataInicial(rs.getTimestamp("data_inicial"));
				competencia.setDataFinal(rs.getTimestamp("data_final"));
				competencia.setAtiva(rs.getBoolean("ativa"));
				lista.add(competencia);
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
			}
		}
		return lista;
	}

	private void preencherStatementPtsCompleto(PtsCifBean pts, Integer idUsuario, PreparedStatement ps) throws SQLException {
		ps.setInt(1, pts.getGerenciarPaciente().getId());
		ps.setInt(2, pts.getPaciente().getId_paciente());
		ps.setString(3, pts.getQueixaPrincipal());
		ps.setString(4, pts.getCondicoesSaude());
		ps.setString(5, pts.getFuncaoEstruturaCorpo());
		ps.setString(6, pts.getAtividadeParticipacao());
		ps.setString(7, pts.getFatoresContextuais());
		ps.setString(8, pts.getFatoresPessoais());
		ps.setString(9, pts.getFacilitador());
		ps.setString(10, pts.getBarreira());
		ps.setInt(11, pts.getCompetencia().getId());
		ps.setInt(12, idUsuario);
	}

	public Boolean cadastrarPtsCif(PtsCifBean pts, FuncionarioBean usuario) throws ProjetoException {

		Boolean retorno = false;

		String sql = "INSERT INTO hosp.ptscif "
				+ " (id_paciente_instituicao, codpaciente, queixa_principal, condicoes_saude, "
				+ " funcao_estrutura_corpo, atividade_participacao, fatores_contextuais, "
				+ " fatores_pessoais, facilitador, barreira, id_competencia, ultima_modificacao, usuario_modificacao)"
				+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,current_timestamp,?) returning id; ";

		Connection conexao = null;
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement ps = conexao.prepareStatement(sql);

			preencherStatementPtsCompleto(pts, usuario.getId().intValue(), ps);

			ResultSet rs = ps.executeQuery();

			if (rs.next()){
				pts.setId(rs.getInt("id"));
			}

			retorno = true;
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
			}
		}
		return retorno;
	}

	public Integer cadastrarObjetivoPtsCif(ObjetivoPtsCifBean objetivo, PtsCifBean pts) throws ProjetoException {
		Integer id = null;
		Connection conexao = null;
		try {
			conexao = ConnectionFactory.getConnection();

			String sql = "insert into hosp.objetivos_ptscif (id_ptscif, forma_avaliacao, intervencao, objetivo) values (?,?,?,?) returning id; ";
			PreparedStatement ps = conexao.prepareStatement(sql);

			ps.setInt(1, pts.getId());
			ps.setString(2, objetivo.getFormaAvaliacao());
			ps.setString(3, objetivo.getIntervencao());
			ps.setString(4, objetivo.getObjetivo());

			ResultSet rs = ps.executeQuery();

			if (rs.next()){
				id = rs.getInt("id");
			}

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
			}
		}
		return id;
	}

	public void cadastrarAvaliadorPtsCif(AvaliadorPtsCifBean avaliador, PtsCifBean pts) throws ProjetoException{
		Connection conexao = null;
		try {
			conexao = ConnectionFactory.getConnection();

			String sql = "insert into hosp.avaliador_ptscif (id_avaliador, id_ptscif) values (?,?)";
			PreparedStatement ps = conexao.prepareStatement(sql);

			ps.setInt(1, avaliador.getAvaliador().getId().intValue());
			ps.setInt(2, pts.getId());

			ps.executeUpdate();
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
			}
		}
	}

	public Boolean alterarInformacoesPtsCif(PtsCifBean pts) throws ProjetoException {

		Boolean retorno = false;

		String sql = "UPDATE hosp.ptscif SET queixa_principal = ?, condicoes_saude = ?, funcao_estrutura_corpo = ?, "
				+" atividade_participacao = ?, fatores_pessoais = ?, fatores_contextuais = ?, id_competencia=? WHERE id = ?";

		Connection conexao = null;
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement ps = conexao.prepareStatement(sql);


			ps.setString(1, pts.getQueixaPrincipal());
			ps.setString(2, pts.getCondicoesSaude());
			ps.setString(3, pts.getFuncaoEstruturaCorpo());
			ps.setString(4, pts.getAtividadeParticipacao());
			ps.setString(5, pts.getFatoresPessoais());
			ps.setString(6, pts.getFatoresContextuais());
			ps.setInt(7, pts.getCompetencia().getId());
			ps.setInt(8, pts.getId());
			ps.executeUpdate();

			conexao.commit();
			retorno = true;
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				conexao.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return retorno;
	}

	public Boolean alterarFatoresAmbientaisPtsCif(PtsCifBean pts) throws ProjetoException {

		Boolean retorno = false;

		String sql = "UPDATE hosp.ptscif SET facilitador = ?, barreira = ? WHERE id = ?";

		Connection conexao = null;
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement ps = conexao.prepareStatement(sql);

			ps.setString(1, pts.getFacilitador());
			ps.setString(2, pts.getBarreira());
			ps.setInt(3, pts.getId());
			ps.executeUpdate();

			conexao.commit();
			retorno = true;
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				conexao.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return retorno;
	}

	public Boolean alterarObjetivoPtsCif(ObjetivoPtsCifBean objetivo) throws ProjetoException {

		Boolean retorno = false;

		String sql;// = "UPDATE hosp.objetivos_ptscif  SET forma_avaliacao = ?, intervencao = ?, objetivo = ? where id = ?";

		Connection conexao = null;
		try {
			conexao = ConnectionFactory.getConnection();

			sql = "update hosp.objetivos_ptscif set forma_avaliacao = ?, intervencao = ?, objetivo = ? where id = ?";
			PreparedStatement ps = conexao.prepareStatement(sql);

			ps.setString(1, objetivo.getFormaAvaliacao());
			ps.setString(2, objetivo.getIntervencao());
			ps.setString(3, objetivo.getObjetivo());
			ps.setInt(4, objetivo.getId());

			ps.executeUpdate();

			conexao.commit();
			retorno = true;
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				conexao.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return retorno;
	}

	public Boolean excluirObjetivoPtsCif(ObjetivoPtsCifBean objetivo, Integer id_usuario) throws ProjetoException {

		Boolean retorno = false;

		String sql;// = "UPDATE hosp.objetivos_ptscif  SET forma_avaliacao = ?, intervencao = ?, objetivo = ? where id = ?";

		Connection conexao = null;
		try {
			conexao = ConnectionFactory.getConnection();

			sql = "update hosp.objetivos_ptscif set excluido = true, usuario_exclusao = ?, datahora_exclusao = current_timestamp where id = ?";
			PreparedStatement ps = conexao.prepareStatement(sql);

			ps.setInt(1, id_usuario);
			ps.setInt(2, objetivo.getId());

			ps.executeUpdate();

			conexao.commit();
			retorno = true;
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				conexao.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return retorno;
	}

	public Boolean adicionarAvaliadorPtsCif(Integer idPtsCif, AvaliadorPtsCifBean avaliador) throws ProjetoException {
		Boolean retorno = false;
		String sql;
		Connection conexao = null;

		try {
			conexao = ConnectionFactory.getConnection();

			sql = "select id_ptscif from hosp.avaliador_ptscif where id_avaliador = ? and id_ptscif = ? and excluido = true";
			PreparedStatement ps = conexao.prepareStatement(sql);
			ps.setInt(1, avaliador.getAvaliador().getId().intValue());
			ps.setInt(2, idPtsCif);
			ResultSet rs = ps.executeQuery();

			if(rs.next()){
				sql = "update hosp.avaliador_ptscif set excluido = false, usuario_exclusao = null, datahora_exclusao = null " +
						"where id_avaliador = ? and id_ptscif = ?";
			} else {
				sql = "insert into hosp.avaliador_ptscif (id_avaliador, id_ptscif, data_validacao, usuario_exclusao, datahora_exclusao) " +
						"values (?, ?, null, null, null)";
			}

			PreparedStatement ps2 = conexao.prepareStatement(sql);
			ps.setInt(1, avaliador.getAvaliador().getId().intValue());
			ps.setInt(2, idPtsCif);
			retorno = ps2.executeUpdate() != 0;

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
			}
		}
		return retorno;
	}

	public Boolean removerAvaliadorPtsCif(Integer idPtsCif, AvaliadorPtsCifBean avaliador, FuncionarioBean usuario) throws ProjetoException {
		Boolean retorno;

		String sql = "update hosp.avaliador_ptscif set excluido = true, usuario_exclusao = ?, datahora_exclusao = current_timestamp " +
				"where id_avaliador = ? and id_ptscif = ?";
		Connection conexao = null;
		try {
			conexao = ConnectionFactory.getConnection();

			PreparedStatement ps = conexao.prepareStatement(sql);
			ps.setInt(1, usuario.getId().intValue());
			ps.setInt(2, avaliador.getAvaliador().getId().intValue());
			ps.setInt(3, idPtsCif);
			retorno = ps.executeUpdate() != 0;

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
			}
		}
		return retorno;
	}

	public Boolean alterarPtsCif(PtsCifBean pts, FuncionarioBean usuario) throws ProjetoException {

		Boolean retorno = false;

		String sql = "UPDATE hosp.ptscif SET id_paciente_instituicao = ?, codpaciente = ?, queixa_principal = ?, "
				+ " condicoes_saude = ?, funcao_estrutura_corpo = ?, atividade_participacao = ?, fatores_contextuais = ?, "
				+ " fatores_pessoais = ?, facilitador = ?, barreira = ?, id_competencia = ?, "
				+ " ultima_modificacao = current_timestamp, usuario_modificacao = ?"
				+ "WHERE id = ?";

		Connection conexao = null;
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement ps = conexao.prepareStatement(sql);

			preencherStatementPtsCompleto(pts, usuario.getId().intValue(), ps);
			ps.setInt(13, pts.getId());

			ps.executeUpdate();
			retorno = true;

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
			}
		}
		return retorno;
	}

	public PtsCifBean buscarPtsCifPorId(Integer idPts) throws ProjetoException {

		String sql = "select cif.id, pi2.id as id_paciente_instituicao, cif.*, c.descricao as desc_competencia, p.nome, p.cpf, p.cns, p.matricula, " +
				"\textract (year from age(p.dtanascimento)) as idade, p.id_paciente codpaciente, " +
				"\tpr.id_programa, pr.descprograma, g.id_grupo, g.descgrupo " +
				"from hosp.ptscif cif " +
				"left join hosp.competencia_ptscif c on (cif.id_competencia = c.id) " +
				"left join hosp.paciente_instituicao pi2 on (pi2.id = cif.id_paciente_instituicao) " +
				"left join hosp.laudo l on (pi2.codlaudo = l.id_laudo) " +
				"left join hosp.programa pr on (pi2.codprograma = pr.id_programa) "+
				"left join hosp.grupo g on (pi2.codgrupo = g.id_grupo) " +
				"left join hosp.pacientes p on (p.id_paciente = cif.codpaciente) " +
				"where cif.id = ?";

		Connection conexao = null;
		PtsCifBean pts = new PtsCifBean();
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement ps = conexao.prepareStatement(sql);
			ps.setInt(1, idPts);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				pts.setId(rs.getInt("id"));
				mapearResultSetPtsCif(rs, pts);
				pts.setQueixaPrincipal(rs.getString("queixa_principal"));
				pts.setCondicoesSaude(rs.getString("condicoes_saude"));
				pts.setFuncaoEstruturaCorpo(rs.getString("funcao_estrutura_corpo"));
				pts.setAtividadeParticipacao(rs.getString("atividade_participacao"));
				pts.setFatoresPessoais(rs.getString("fatores_pessoais"));
				pts.setFatoresContextuais(rs.getString("fatores_contextuais"));
				pts.setFacilitador(rs.getString("facilitador"));
				pts.setBarreira(rs.getString("barreira"));
				pts.setBloqueado(rs.getBoolean("bloqueado"));
				pts.setListaAvaliadores(buscaAvaliadoresPtsCif(pts, conexao));
				pts.setListaObjetivos(buscaObjetivosPtsCif(pts, conexao));
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
			}
		}
		return pts;
	}

	private List<AvaliadorPtsCifBean> buscaAvaliadoresPtsCif(PtsCifBean pts, Connection conexaoAux)
			throws ProjetoException, SQLException {

		String sql = "select f.id_funcionario, f.descfuncionario, f.cns, apc.data_validacao, apc.validado "+
				"	from acl.funcionarios f \r\n" +
				"	join hosp.avaliador_ptscif apc on f.id_funcionario = apc.id_avaliador \r\n" +
				"	join hosp.ptscif pc on apc.id_ptscif = pc.id \r\n" +
				"	where pc.id = ? and apc.excluido is false";

		List<AvaliadorPtsCifBean> listaAvaliadores = new ArrayList<>();
		try {
			PreparedStatement ps = conexaoAux.prepareStatement(sql);
			ps.setInt(1, pts.getId());
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				AvaliadorPtsCifBean avaliador = new AvaliadorPtsCifBean();
				avaliador.getAvaliador().setId(rs.getLong("id_funcionario"));
				avaliador.getAvaliador().setNome(rs.getString("descfuncionario"));
				avaliador.getAvaliador().setCns(rs.getString("cns"));
				avaliador.setData(rs.getDate("data_validacao"));
				avaliador.setValidado(rs.getBoolean("validado"));
				listaAvaliadores.add(avaliador);
			}
		} catch (SQLException sqle) {
			conexaoAux.rollback();
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			conexaoAux.rollback();
			throw new ProjetoException(ex, this.getClass().getName());
		}
		return listaAvaliadores;
	}

	private List<ObjetivoPtsCifBean> buscaObjetivosPtsCif(PtsCifBean pts, Connection conexaoAux)
			throws ProjetoException, SQLException {

		String sql = "select opc.id, opc.objetivo, opc.forma_avaliacao, opc.intervencao from hosp.objetivos_ptscif opc \r\n" +
				"	join hosp.ptscif pc on opc.id_ptscif = pc.id \r\n" +
				"	where pc.id = ?	";

		List<ObjetivoPtsCifBean> listaObjetivos = new ArrayList<>();
		try {
			PreparedStatement ps = conexaoAux.prepareStatement(sql);
			ps.setInt(1, pts.getId());
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				ObjetivoPtsCifBean objetivo = new ObjetivoPtsCifBean();
				objetivo.setId(rs.getInt("id"));
				objetivo.setObjetivo(rs.getString("objetivo"));
				objetivo.setFormaAvaliacao(rs.getString("forma_avaliacao"));
				objetivo.setIntervencao(rs.getString("intervencao"));
				listaObjetivos.add(objetivo);
			}
		} catch (SQLException sqle) {
			conexaoAux.rollback();
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			conexaoAux.rollback();
			throw new ProjetoException(ex, this.getClass().getName());
		}
		return listaObjetivos;
	}

	public ArrayList<PtsCifBean> buscarPtsPorCompetencia(String buscaPaciente, String tipoBuscaPaciente, Integer competencia) throws ProjetoException {
		String sql = "select cif.id, pi2.id as id_paciente_instituicao, cif.id_competencia, c.descricao as desc_competencia, p.nome, p.cpf, p.cns, p.matricula, " +
				"\textract (year from age(p.dtanascimento)) as idade, p.id_paciente codpaciente,  cif.ultima_modificacao, " +
				"\tpr.id_programa, pr.descprograma, g.id_grupo, g.descgrupo " +
				"from hosp.ptscif cif " +
				"left join hosp.competencia_ptscif c on (cif.id_competencia = c.id) " +
				"left join hosp.paciente_instituicao pi2 on (pi2.id = cif.id_paciente_instituicao) " +
				"left join hosp.laudo l on (pi2.codlaudo = l.id_laudo) " +
				"left join hosp.programa pr on (pi2.codprograma = pr.id_programa) "+
				"left join hosp.grupo g on (pi2.codgrupo = g.id_grupo) " +
				"left join hosp.pacientes p on (p.id_paciente = cif.codpaciente) " +
				"where pi2.status = 'A' and cif.id_competencia = ?";

		if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(tipoBuscaPaciente) && !VerificadorUtil.verificarSeObjetoNuloOuVazio(buscaPaciente)){
			if(tipoBuscaPaciente.equals("paciente")){
				sql = sql + " and p.nome ilike ?";
			} else if (tipoBuscaPaciente.equals("matpaciente")){
				sql = sql + " and p.matricula = ?";
			} else if (tipoBuscaPaciente.equals("prontpaciente")){
				sql = sql + " and p.id_paciente = ?";
			}
		}

		sql += " ORDER BY p.nome ";

		ArrayList<PtsCifBean> lista = new ArrayList<>();

		Connection conexao = null;
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement ps = conexao.prepareStatement(sql);

			ps.setInt(1, competencia);

			int i = 2;
			if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(tipoBuscaPaciente) && !VerificadorUtil.verificarSeObjetoNuloOuVazio(buscaPaciente)){
				if(tipoBuscaPaciente.equals("paciente")){
					ps.setString(i, "%" + buscaPaciente.toUpperCase() + "%");
				} else if (tipoBuscaPaciente.equals("matpaciente")){
					ps.setString(i, buscaPaciente.toUpperCase());
				} else if (tipoBuscaPaciente.equals("prontpaciente")){
					ps.setInt(i, Integer.parseInt(buscaPaciente));
				} else {
					i--; //tipoBuscaPaciente desconhecido, retornar i para o valor inicial
				}
				i++;
			}

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				PtsCifBean pts = new PtsCifBean();
				mapearResultSetPtsCif(rs, pts);
				lista.add(pts);
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
			}
		}
		return lista;
	}

	public ArrayList<PtsCifBean> buscarTodosOsPts(String buscaPaciente, String tipoBuscaPaciente, String formatoBusca) throws ProjetoException {
		String sql = (!(!VerificadorUtil.verificarSeObjetoNuloOuVazio(formatoBusca) && formatoBusca.equals("RP")) ? "select " : "select max");
		sql = sql+"(cif.id), pi2.id as id_paciente_instituicao, cif.id_competencia, c.descricao as desc_competencia, p.nome, p.cpf, p.cns, p.matricula, " +
				"extract (year from age(p.dtanascimento)) as idade, p.id_paciente codpaciente, cif.ultima_modificacao, " +
				"pr.id_programa, pr.descprograma, g.id_grupo, g.descgrupo " +
				"from hosp.ptscif cif " +
				"left join hosp.competencia_ptscif c on (cif.id_competencia = c.id) " +
				"left join hosp.paciente_instituicao pi2 on (pi2.id = cif.id_paciente_instituicao) " +
				"left join hosp.laudo l on (pi2.codlaudo = l.id_laudo) " +
				"left join hosp.programa pr on (pi2.codprograma = pr.id_programa) "+
				"left join hosp.grupo g on (pi2.codgrupo = g.id_grupo) " +
				"left join hosp.pacientes p on (p.id_paciente = cif.codpaciente) " +
				"where 1=1 ";

		if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(tipoBuscaPaciente) && !VerificadorUtil.verificarSeObjetoNuloOuVazio(buscaPaciente)){
			if(tipoBuscaPaciente.equals("paciente")){
				sql = sql + " and p.nome ilike ?";
			} else if (tipoBuscaPaciente.equals("matpaciente")){
				sql = sql + " and p.matricula = ?";
			} else if (tipoBuscaPaciente.equals("prontpaciente")){
				sql = sql + " and p.id_paciente = ?";
			}
		}

		if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(formatoBusca)){
			if(formatoBusca.equals("SA")){
				sql = sql + " and c.ativa = True";
			} else if(formatoBusca.equals("A")){
				sql = sql + " and c.ativa = False";
			} else if (formatoBusca.equals("RP")){
				sql = sql + " and pi2.status = 'A' and c.ativa = False group by 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14 ";
			}
		}

		sql += " ORDER BY p.nome ";

		ArrayList<PtsCifBean> lista = new ArrayList<>();

		Connection conexao = null;
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement ps = conexao.prepareStatement(sql);

			int i = 1;
			if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(tipoBuscaPaciente) && !VerificadorUtil.verificarSeObjetoNuloOuVazio(buscaPaciente)){
				if(tipoBuscaPaciente.equals("paciente")){
					ps.setString(i, "%" + buscaPaciente.toUpperCase() + "%");
				} else if (tipoBuscaPaciente.equals("matpaciente")){
					ps.setString(i, buscaPaciente.toUpperCase());
				} else if (tipoBuscaPaciente.equals("prontpaciente")){
					ps.setInt(i, Integer.parseInt(buscaPaciente));
				} else {
					i--; //tipoBuscaPaciente desconhecido, retornar i para o valor inicial
				}
				i++;
			}

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				PtsCifBean pts = new PtsCifBean();
				mapearResultSetPtsCif(rs, pts);
				lista.add(pts);
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
			}
		}
		return lista;
	}

	public ArrayList<PtsCifBean> buscarPacientesSemPts(String buscaPaciente, String tipoBuscaPaciente) throws ProjetoException {
		String sqlPaciente = "";
		if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(tipoBuscaPaciente) && !VerificadorUtil.verificarSeObjetoNuloOuVazio(buscaPaciente)){
			if(tipoBuscaPaciente.equals("paciente")){
				sqlPaciente = " and p.nome ilike ?";
			} else if (tipoBuscaPaciente.equals("matpaciente")){
				sqlPaciente = " and p.matricula = ?";
			} else if (tipoBuscaPaciente.equals("prontpaciente")){
				sqlPaciente = " and p.id_paciente = ?";
			}
		}

		String sql = "select distinct(p.id_paciente) as codpaciente, p.nome, p.cpf, p.cns, p.matricula, extract (year from age(p.dtanascimento)) as idade, max(cif.id)" +
				"from hosp.paciente_instituicao pi2 " +
				"left join hosp.laudo l on (pi2.codlaudo = l.id_laudo) "+
				"left join hosp.pacientes p on (p.id_paciente = coalesce(l.codpaciente, pi2.id_paciente)) "+
				"left join hosp.ptscif cif on (pi2.id = cif.id_paciente_instituicao) " +
				"where pi2.status='A' and cif.id is null "+sqlPaciente+"group by 1,2,3,4,5,6 ORDER BY 2";

		ArrayList<PtsCifBean> lista = new ArrayList<>();

		Connection conexao = null;
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement ps = conexao.prepareStatement(sql);

			int i = 1;
			if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(tipoBuscaPaciente) && !VerificadorUtil.verificarSeObjetoNuloOuVazio(buscaPaciente)){
				if(tipoBuscaPaciente.equals("paciente")){
					ps.setString(i, "%" + buscaPaciente.toUpperCase() + "%");
				} else if (tipoBuscaPaciente.equals("matpaciente")){
					ps.setString(i, buscaPaciente.toUpperCase());
				} else if (tipoBuscaPaciente.equals("prontpaciente")){
					ps.setInt(i, Integer.parseInt(buscaPaciente));
				} else {
					i--; //tipoBuscaPaciente desconhecido, retornar i para o valor inicial
				}
				i++;
			}

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				PtsCifBean pts = new PtsCifBean();
				pts.getPaciente().setId_paciente(rs.getInt("codpaciente"));
				pts.getPaciente().setNome(rs.getString("nome"));
				pts.getPaciente().setCns(rs.getString("cns"));
				pts.getPaciente().setCpf(rs.getString("cpf"));
				pts.getPaciente().setMatricula(rs.getString("matricula"));
				lista.add(pts);
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
			}
		}
		return lista;
	}


    
    private void cadastrarAvaliadoresPts(PtsCifBean pts, Integer tipo, Connection conexaoAux) 
    		throws ProjetoException, SQLException {

        String sql = "INSERT INTO hosp.avaliador_ptscif " +
        		" (id_avaliador, id_ptscif) VALUES (?, ?); ";
        
        try {
            PreparedStatement ps = conexaoAux.prepareStatement(sql);
            
            for (AvaliadorPtsCifBean avaliadorDTO : pts.getListaAvaliadores()) {
            	
            	if(tipo.equals(TipoCabecalho.INCLUSAO.getSigla()) || !avaliadorJaExisteNoPts(avaliadorDTO, pts.getId(), conexaoAux)) {
            		ps.setLong(1, avaliadorDTO.getAvaliador().getId());
            		ps.setInt(2, pts.getId()); 
            		ps.executeUpdate();
            	} else {
            		atualizaPossivelAvaliadorExcluido(avaliadorDTO, pts.getId(), conexaoAux);
            	}
			}
        } catch (SQLException sqle) {
        	conexaoAux.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
        	conexaoAux.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
    }
    
    private boolean avaliadorJaExisteNoPts(AvaliadorPtsCifBean avaliadorDTO, Integer idPts, Connection conexaoAux)
    		throws SQLException, ProjetoException {
    	
    	String sql = "SELECT EXISTS (SELECT id_avaliador FROM hosp.avaliador_ptscif " +
        		"WHERE id_avaliador = ? AND id_ptscif = ?) existe_cadastrado ;";
        
    	boolean existe = false;
    	
        try {
            PreparedStatement ps = conexaoAux.prepareStatement(sql);
            
            ps.setLong(1, avaliadorDTO.getAvaliador().getId());
            ps.setInt(2, idPts); 
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
            	existe = rs.getBoolean("existe_cadastrado");
            }

        } catch (SQLException sqle) {
        	conexaoAux.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
        	conexaoAux.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
        return existe;
    }
    
    private void atualizaPossivelAvaliadorExcluido(AvaliadorPtsCifBean avaliadorDTO, Integer idPts, Connection conexaoAux)
    		throws SQLException, ProjetoException {
    	
    	String sql = "UPDATE hosp.avaliador_ptscif SET excluido = false, data_validacao = null "
    			+ " WHERE id_avaliador = ? AND id_ptscif = ? and excluido is true";
        
        try {
            PreparedStatement ps = conexaoAux.prepareStatement(sql);
            
            ps.setLong(1, avaliadorDTO.getAvaliador().getId());
            ps.setInt(2, idPts); 
            ps.executeUpdate();

        } catch (SQLException sqle) {
        	conexaoAux.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
        	conexaoAux.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
    }
    
    private void excluirAvaliadoresPts
    	(List<AvaliadorPtsCifBean> listaAvaliadoresExcluidos, Integer idPts, Long idUsuario, Connection conexaoAux)
    		throws ProjetoException, SQLException {

        String sql = "UPDATE hosp.avaliador_ptscif SET excluido = true, usuario_exclusao = ?, datahora_exclusao = CURRENT_TIMESTAMP "+
        		" WHERE id_ptscif = ? and id_avaliador = ?";
        
        try {
            PreparedStatement ps = conexaoAux.prepareStatement(sql);
            for (AvaliadorPtsCifBean avaliador : listaAvaliadoresExcluidos) {
            	ps.setLong(1, idUsuario);
            	ps.setInt(2, idPts);
            	ps.setLong(3, avaliador.getAvaliador().getId());
            	ps.executeUpdate();				
			}
        } catch (SQLException sqle) {
        	conexaoAux.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
        	conexaoAux.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
    }
    
	public List<PtsCifBean> buscarPtsAvaliador(Long idAvaliador) throws ProjetoException {

		String sql = "select cif.*, pi2.id as id_paciente_instituicao, c.descricao as desc_competencia, p.nome, p.cpf, p.cns, p.matricula, " +
				"extract (year from age(p.dtanascimento)) as idade, p.id_paciente codpaciente, pr.id_programa, pr.descprograma, g.id_grupo, g.descgrupo " +
				"from hosp.ptscif cif " +
				"left join hosp.competencia_ptscif c on (cif.id_competencia = c.id) " +
				"left join hosp.paciente_instituicao pi2 on (pi2.id = cif.id_paciente_instituicao) " +
				"left join hosp.laudo l on (pi2.codlaudo = l.id_laudo) " +
				"left join hosp.programa pr on (pi2.codprograma = pr.id_programa) " +
				"left join hosp.grupo g on (pi2.codgrupo = g.id_grupo) " +
				"left join hosp.pacientes p on (cif.codpaciente = p.id_paciente)"+
				"left join hosp.avaliador_ptscif ap on (cif.id = ap.id_ptscif)" +
				"where ap.id_avaliador = ? and ap.validado is false";


		List<PtsCifBean> lista = new ArrayList<>();

		Connection conexao = null;
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement ps = conexao.prepareStatement(sql);
			ps.setLong(1, idAvaliador);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				PtsCifBean pts = new PtsCifBean();
				mapearResultSetPtsCif(rs, pts);
				lista.add(pts);
			}

		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(),
					sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				conexao.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return lista;
	}

	private void mapearResultSetPtsCif(ResultSet rs, PtsCifBean pts) throws SQLException {
		pts.setId(rs.getInt("id"));
		pts.setUltimaModificacao(rs.getTimestamp("ultima_modificacao"));
		pts.getGerenciarPaciente().setId(rs.getInt("id_paciente_instituicao"));
		pts.getGerenciarPaciente().getPrograma().setIdPrograma(rs.getInt("id_programa"));
		pts.getGerenciarPaciente().getPrograma().setDescPrograma(rs.getString("descprograma"));
		pts.getGerenciarPaciente().getGrupo().setIdGrupo(rs.getInt("id_grupo"));
		pts.getGerenciarPaciente().getGrupo().setDescGrupo(rs.getString("descgrupo"));
		pts.getCompetencia().setId(rs.getInt("id_competencia"));
		pts.getCompetencia().setDescricao(rs.getString("desc_competencia"));
		pts.getPaciente().setId_paciente(rs.getInt("codpaciente"));
		pts.getPaciente().setNome(rs.getString("nome"));
		pts.getPaciente().setCns(rs.getString("cns"));
		pts.getPaciente().setCpf(rs.getString("cpf"));
		pts.getPaciente().setMatricula(rs.getString("matricula"));
	}
	
    public Boolean gravarValidacaoAvaliador(Long idAvaliador, Integer idPts) throws ProjetoException {

        Boolean retorno = false;

        String sql = "UPDATE hosp.avaliador_ptscif " +
        		"SET data_validacao = CURRENT_TIMESTAMP, validado = TRUE " + 
        		"WHERE id_avaliador = ? AND id_ptscif = ?; ";
        
        Connection conexao = null;
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement ps = conexao.prepareStatement(sql);
            
            ps.setLong(1, idAvaliador);
            ps.setInt(2, idPts);
            ps.executeUpdate();

            retorno = true;
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
            }
        }
        return retorno;
    }
}
