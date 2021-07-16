package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.administrativo.model.InsercaoProfissionalEquipe;
import br.gov.al.maceio.sishosp.administrativo.model.RemocaoProfissionalEquipe;
import br.gov.al.maceio.sishosp.administrativo.model.SubstituicaoProfissional;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.DataUtil;
import br.gov.al.maceio.sishosp.comum.util.TratamentoErrosUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.enums.BuscaEvolucao;
import br.gov.al.maceio.sishosp.hosp.enums.MotivoLiberacao;
import br.gov.al.maceio.sishosp.hosp.enums.TipoInconsistencia;
import br.gov.al.maceio.sishosp.hosp.log.dao.LogDAO;
import br.gov.al.maceio.sishosp.hosp.log.enums.Rotina;
import br.gov.al.maceio.sishosp.hosp.log.model.LogBean;
import br.gov.al.maceio.sishosp.hosp.model.AtendimentoBean;
import br.gov.al.maceio.sishosp.hosp.model.CboBean;
import br.gov.al.maceio.sishosp.hosp.model.CidBean;
import br.gov.al.maceio.sishosp.hosp.model.EspecialidadeBean;
import br.gov.al.maceio.sishosp.hosp.model.PacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.ProcedimentoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaGrupoEvolucaoBean;
import br.gov.al.maceio.sishosp.hosp.model.dto.PendenciaEvolucaoProgramaGrupoDTO;
import br.gov.al.maceio.sishosp.hosp.model.dto.ProcedimentoCidDTO;

import javax.faces.context.FacesContext;

public class AtendimentoDAO {
	Connection con = null;
	PreparedStatement ps = null;

	FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
			.getSessionMap().get("obj_usuario");

	public Boolean realizaAtendimentoProfissional
			(FuncionarioBean funcionario, AtendimentoBean atendimento, String palavrasChaveIncompativeis) throws ProjetoException {
		boolean alterou = false;
		con = ConnectionFactory.getConnection();
		try {

			String sql = "update hosp.atendimentos1 set codprocedimento = ?, "
					+ "dtaatendido = current_timestamp, id_situacao_atendimento = ?, evolucao = ?, id_cidprimario = ? "
					+ " where id_atendimento = ? and codprofissionalatendimento = ?";

			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, atendimento.getProcedimento().getIdProc());
			stmt.setInt(2, atendimento.getSituacaoAtendimento().getId());
			stmt.setString(3, atendimento.getEvolucao());
			if (VerificadorUtil.verificarSeObjetoNuloOuZero(atendimento.getCidPrimario().getIdCid())) {
				stmt.setNull(4, Types.NULL);
			} else {
				stmt.setInt(4, atendimento.getCidPrimario().getIdCid());
			}
			stmt.setInt(5, atendimento.getId());
			stmt.setLong(6, funcionario.getId());

			stmt.executeUpdate();

			String sql2 = "update hosp.atendimentos set situacao = 'F' " + " where id_atendimento = ?";

			PreparedStatement stmt2 = con.prepareStatement(sql2);
			stmt2.setInt(1, atendimento.getId());

			stmt2.executeUpdate();

			String sql3 = "update hosp.atendimentos1_procedimento_secundario set excluido = 'S', data_hora_exclusao=current_timestamp, usuario_exclusao=? " + " where id_atendimentos1 = ? and coalesce(excluido,'N')='N'";

			PreparedStatement stmt3 = con.prepareStatement(sql3);
			stmt3.setLong(1, user_session.getId());
			stmt3.setInt(2, atendimento.getId1());
			stmt3.executeUpdate();


			gravarProcedimentosSecundariosEvolucao(con, atendimento);
			gravarValidacaoSigtapAnterior(con, atendimento.getId(), atendimento.isValidadoPeloSigtapAnterior());
			if(user_session.getUnidade().getParametro().isVerificaPeriodoInicialEvolucaoPrograma()) {
				verificarInconsistenciaEvolucaoProgramaGrupo(atendimento, con);
			}

			if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(palavrasChaveIncompativeis)) {
				Integer idSituacao = atendimento.getSituacaoAtendimento().getId();
				String descricao = montarDescricaoLogPalavraChave(atendimento, palavrasChaveIncompativeis, idSituacao);

				LogBean log = new LogBean(user_session.getId(), descricao, Rotina.EVOLUCAO_PALAVRAS_CHAVE_DIVERGENTE.getSigla());
				new LogDAO().gravarLog(log, con);
			}
			con.commit();

			alterou = true;

		} catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				//comentado walter erro log ex.printStackTrace();
			}
		}
		return alterou;
	}

	private String montarDescricaoLogPalavraChave(AtendimentoBean atendimento, String palavrasChaveIncompativeis,
												  Integer idSituacao) throws ProjetoException, SQLException {
		String descricao = "Id_atendimento_1: "+ atendimento.getId1();
		descricao += " Id_situacao_atendimento: "+ idSituacao+" Situação: "+
				new SituacaoAtendimentoDAO().buscaDescricaoSituacaoAtendimento(idSituacao, con)+"\n";
		descricao += "Palavras-chave: "+palavrasChaveIncompativeis;
		return descricao;
	}

	private void gravarProcedimentosSecundariosEvolucao(Connection conexao, AtendimentoBean atendimento)
			throws SQLException, ProjetoException {

		String sql = "INSERT INTO hosp.atendimentos1_procedimento_secundario " +
				"(id_atendimentos1, id_procedimento, id_cid) " +
				"VALUES(?, ?, ?);";
		try {
			for (ProcedimentoCidDTO procedimentoCid : atendimento.getListaProcedimentoCid()) {
				PreparedStatement stm = conexao.prepareStatement(sql);
				stm.setInt(1, atendimento.getId1());
				stm.setInt(2, procedimentoCid.getProcedimento().getIdProc());
				stm.setInt(3, procedimentoCid.getCid().getIdCid());
				stm.executeUpdate();
			}
		} catch (SQLException ex2) {
			conexao.rollback();
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			conexao.rollback();
			throw new ProjetoException(ex, this.getClass().getName());
		}
	}

	public Boolean realizaAtendimentoEquipe(List<AtendimentoBean> lista, Integer idLaudo, Integer grupoAvaliacao,  List<AtendimentoBean> listaExcluir, Integer idAtendimento, boolean validaSigtapAnterior)
			throws ProjetoException {
		boolean alterou = false;
		con = ConnectionFactory.getConnection();
		try {

			GerenciarPacienteDAO gerenciarPacienteDAO = new GerenciarPacienteDAO();

			List<Integer> listaIdAtendimento01QueNaoPodemTerRegistroExcluidos = gerenciarPacienteDAO.retornaListaAtendimento01QueNaoPodemTerRegistroExcluidos(idAtendimento, con);

			ArrayList<SubstituicaoProfissional> listaSubstituicao =  gerenciarPacienteDAO.listaAtendimentosQueTiveramSubstituicaoProfissionalEmUmAtendimento(idAtendimento, con) ;

			ArrayList<InsercaoProfissionalEquipe> listaProfissionaisInseridosAtendimentoEquipe = new ArrayList<>();

			ArrayList<RemocaoProfissionalEquipe> listaProfissionaisRemovidosAtendimentoEquipe = new ArrayList<>();

			ArrayList<Integer> listaIdAtendimentosComIncosistenciaLog = new ArrayList<>();

			for (int i = 0; i < lista.size(); i++) {
				ArrayList<InsercaoProfissionalEquipe> listaProfissionaisInseridosAtendimentoEquipeAux = gerenciarPacienteDAO.listaAtendimentosQueTiveramInsercaoProfissionalAtendimentoEquipePeloIdAtendimentoCodProfissionalAtendimento(idAtendimento, lista.get(i).getFuncionario().getId(), con) ;
				listaProfissionaisInseridosAtendimentoEquipe.addAll(listaProfissionaisInseridosAtendimentoEquipeAux);

				ArrayList<RemocaoProfissionalEquipe> listaProfissionaisRemovidosAtendimentoEquipeAux = gerenciarPacienteDAO.listaAtendimentosQueTiveramRemocaoProfissionalAtendimentoEquipePeloIdAtendimentoCodProfissionalAtendimento(idAtendimento, lista.get(i).getFuncionario().getId(), con) ;
				listaProfissionaisRemovidosAtendimentoEquipe.addAll(listaProfissionaisRemovidosAtendimentoEquipeAux);

				if(!VerificadorUtil.verificarSeObjetoNuloOuZero(lista.get(i).getId1())) {
					alteraAtendimentoComInconsistenciaLog(lista.get(i), con);
				}

			}

			listaIdAtendimentosComIncosistenciaLog.addAll(retornaAtendimentosComInconsistenciaLog(lista.get(0).getId(), con));

			if (!gerenciarPacienteDAO.apagarAtendimentosDeUmAtendimento (idAtendimento, con,  listaSubstituicao, listaExcluir, listaProfissionaisInseridosAtendimentoEquipe, listaProfissionaisRemovidosAtendimentoEquipe)) {
				con.close();
				return alterou;
			}


			List<AtendimentoBean> listaAtendimentosParaUpdate = removeAtendimentosNaoExcluidos(lista,
					listaIdAtendimento01QueNaoPodemTerRegistroExcluidos);

			for (AtendimentoBean atendimento  : lista) {
				String sql2 = "INSERT INTO hosp.atendimentos1(codprofissionalatendimento, id_atendimento, "
						+ " cbo, codprocedimento, id_situacao_atendimento, evolucao, perfil_avaliacao, horario_atendimento, id_cidprimario) "
						+ " VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?) returning id_atendimentos1;";

				if (VerificadorUtil.verificarSeObjetoNuloOuZero(atendimento.getSituacaoAtendimentoAnterior().getId())
						&& !listaIdAtendimentosComIncosistenciaLog.contains(atendimento.getId1())) {
					
					if(gerenciarPacienteDAO.funcionarioEstaAfastadoDurantePeriodo(atendimento.getFuncionario(), atendimento.getDataAtendimento(), con))
	            		return false;
					
					PreparedStatement stmt2 = con.prepareStatement(sql2);
					stmt2.setLong(1, atendimento.getFuncionario().getId());
					stmt2.setInt(2, idAtendimento);
					if (VerificadorUtil.verificarSeObjetoNuloOuZero(atendimento.getCbo().getCodCbo())) {
						stmt2.setNull(3, Types.NULL);
					} else {
						stmt2.setInt(3, atendimento.getCbo().getCodCbo());
					}

					if (VerificadorUtil.verificarSeObjetoNuloOuZero(atendimento.getProcedimento().getIdProc())) {
						stmt2.setNull(4, Types.NULL);
					} else {
						stmt2.setInt(4, atendimento.getProcedimento().getIdProc());
					}

					if (!VerificadorUtil.verificarSeObjetoNuloOuZero(atendimento.getSituacaoAtendimento().getId()))
						stmt2.setInt(5, atendimento.getSituacaoAtendimento().getId());
					else
						stmt2.setNull(5, Types.NULL);
					stmt2.setString(6, atendimento.getEvolucao());
					stmt2.setString(7, atendimento.getPerfil());
					if (atendimento.getHorarioAtendimento() != null)
						stmt2.setTime(8, DataUtil.retornarHorarioEmTime(atendimento.getHorarioAtendimento()));
					else
						stmt2.setNull(8, Types.NULL);

					if(VerificadorUtil.verificarSeObjetoNuloOuZero(atendimento.getCidPrimario().getIdCid()))
						stmt2.setNull(9, Types.NULL);
					else
						stmt2.setInt(9, atendimento.getCidPrimario().getIdCid());

					ResultSet rs = stmt2.executeQuery();
					if(rs.next()) {
						atendimento.setId1(rs.getInt("id_atendimentos1"));
					}
				}
				else if (!VerificadorUtil.verificarSeObjetoNuloOuZero(atendimento.getSituacaoAtendimentoAnterior().getId())
						&& !atendimento.getSituacaoAtendimentoAnterior().getAtendimentoRealizado()) {
					listaAtendimentosParaUpdate.add(atendimento);
				}
			}

			alterarSituacaoAtendimentosNaoRegravados(listaAtendimentosParaUpdate, con);

			if (!VerificadorUtil.verificarSeObjetoNuloOuZero(grupoAvaliacao)) {
				String sql3 = "update hosp.atendimentos set cod_laudo = ?, grupo_avaliacao = ? where id_atendimento = ?";

				PreparedStatement stmt3 = con.prepareStatement(sql3);
				stmt3.setInt(1, idLaudo);
				stmt3.setInt(2, grupoAvaliacao);
				stmt3.setInt(3, lista.get(0).getId());
				stmt3.executeUpdate();
			}

			AtendimentoDAO aDao = new AtendimentoDAO();
			if (listaSubstituicao.size() > 0) {
				String sql6 = "insert into adm.substituicao_funcionario (id_atendimentos1,id_afastamento_funcionario,\n"
						+ "id_funcionario_substituido, id_funcionario_substituto, usuario_acao, data_hora_acao)	\n"
						+ "values ((select id_atendimentos1 from hosp.atendimentos1\n"
						+ "a11 join hosp.atendimentos aa on aa.id_atendimento = a11.id_atendimento\n"
						+ "where aa.dtaatende=? and a11.codprofissionalatendimento=? and coalesce(aa.situacao, 'A')<> 'C'	and coalesce(a11.excluido, 'N' )= 'N' limit 1),?,?,?,?, current_timestamp)";
				PreparedStatement ps6 = null;
				ps6 = con.prepareStatement(sql6);
				for (int i = 0; i < listaSubstituicao.size(); i++) {
					String sql8 = "update hosp.atendimentos1 set codprofissionalatendimento=?, cbo=? where atendimentos1.id_atendimentos1 = (\n"
							+ "select distinct a1.id_atendimentos1 from hosp.paciente_instituicao pi\n"
							+ "join hosp.atendimentos a on a.id_paciente_instituicao = pi.id\n"
							+ "join hosp.atendimentos1 a1 on a1.id_atendimento = a.id_atendimento\n"
							+ "where a1.id_atendimento=? and a.dtaatende=? and a1.codprofissionalatendimento = ? and coalesce(a.situacao, 'A')<> 'C'	and coalesce(a1.excluido, 'N' )= 'N' limit 1)";
					PreparedStatement ps8 = null;
					ps8 = con.prepareStatement(sql8);
					ps8.setLong(1, listaSubstituicao.get(i).getFuncionario().getId());
					ps8.setLong(2, listaSubstituicao.get(i).getAtendimento().getCbo().getCodCbo());
					ps8.setLong(3, idAtendimento);
					ps8.setDate(4, new java.sql.Date(listaSubstituicao.get(i).getDataAtendimento().getTime()));
					ps8.setLong(5, listaSubstituicao.get(i).getAfastamentoProfissional().getFuncionario().getId());
					ps8.execute();

					ps6 = null;
					ps6 = con.prepareStatement(sql6);

					ps6.setDate(1, new java.sql.Date(listaSubstituicao.get(i).getDataAtendimento().getTime()));
					ps6.setLong(2, listaSubstituicao.get(i).getAfastamentoProfissional().getFuncionario().getId());
					ps6.setLong(3, listaSubstituicao.get(i).getAfastamentoProfissional().getId());
					ps6.setLong(4, listaSubstituicao.get(i).getAfastamentoProfissional().getFuncionario().getId());
					ps6.setLong(5, listaSubstituicao.get(i).getFuncionario().getId());
					ps6.setLong(6, listaSubstituicao.get(i).getUsuarioAcao().getId());
					ps6.execute();
				}
			}

			if (listaProfissionaisInseridosAtendimentoEquipe.size()>0) {
				String sql6 = "insert into adm.insercao_profissional_equipe_atendimento_1 (id_atendimentos1,id_insercao_profissional_equipe_atendimento, id_profissional) "+
						"values ((select id_atendimentos1 from hosp.atendimentos1\n" +
						"a11 join hosp.atendimentos aa on aa.id_atendimento = a11.id_atendimento\n" +
						"where aa.dtaatende=? and  aa.codprograma=? and aa.codgrupo=?  and a11.codprofissionalatendimento =? and coalesce(aa.situacao, 'A')<> 'C'	and coalesce(a11.excluido, 'N' )= 'N'  limit 1),?,?)";
				PreparedStatement ps6 = null;
				ps6 = null;
				ps6 = con.prepareStatement(sql6);
				for (int i = 0; i < listaProfissionaisInseridosAtendimentoEquipe.size(); i++) {
				/*
				String sql8 = "INSERT INTO hosp.atendimentos1 " +
		                "(codprofissionalatendimento, id_atendimento, cbo, codprocedimento) " +
		                "VALUES (?, (select id_atendimento from hosp.atendimentos aa " +
		                " where aa.dtaatende=? and  aa.codprograma=? and aa.codgrupo=? limit 1), ?, (select cod_procedimento from hosp.programa " +
		                "where programa.id_programa = ? )) RETURNING id_atendimentos1";

				PreparedStatement ps8 = null;
				ps8 = con.prepareStatement(sql8);
				ps8.setLong(1, listaProfissionaisInseridosAtendimentoEquipe.get(i).getFuncionario().getId());
				ps8.setDate(2,new java.sql.Date( listaProfissionaisInseridosAtendimentoEquipe.get(i).getDataAtendimento().getTime()));
				ps8.setLong(3, listaProfissionaisInseridosAtendimentoEquipe.get(i).getPrograma().getIdPrograma());
				ps8.setLong(4, listaProfissionaisInseridosAtendimentoEquipe.get(i).getGrupo().getIdGrupo());
				ps8.setLong(5, listaProfissionaisInseridosAtendimentoEquipe.get(i).getFuncionario().getCbo().getCodCbo());
				ps8.setLong(6, listaProfissionaisInseridosAtendimentoEquipe.get(i).getPrograma().getIdPrograma());
				ps8.execute();
				*/
					ps6 = null;
					ps6 = con.prepareStatement(sql6);

					ps6.setDate(1,new java.sql.Date( listaProfissionaisInseridosAtendimentoEquipe.get(i).getDataAtendimento().getTime()));
					ps6.setLong(2, listaProfissionaisInseridosAtendimentoEquipe.get(i).getPrograma().getIdPrograma());
					ps6.setLong(3, listaProfissionaisInseridosAtendimentoEquipe.get(i).getGrupo().getIdGrupo());
					ps6.setLong(4, listaProfissionaisInseridosAtendimentoEquipe.get(i).getFuncionario().getId());
					ps6.setLong(5, listaProfissionaisInseridosAtendimentoEquipe.get(i).getId());
					ps6.setLong(6, listaProfissionaisInseridosAtendimentoEquipe.get(i).getFuncionario().getId());
					ps6.execute();
				}
			}


			if (listaProfissionaisRemovidosAtendimentoEquipe.size()>0) {
				String sql6 = "insert into adm.remocao_profissional_equipe_atendimento_1 (id_atendimentos1,id_remocao_profissional_equipe_atendimento, id_profissional) "+
						"values ((select id_atendimentos1 from hosp.atendimentos1\n" +
						"a11 join hosp.atendimentos aa on aa.id_atendimento = a11.id_atendimento\n" +
						"where aa.dtaatende=? and  aa.codprograma=? and aa.codgrupo=?  and a11.codprofissionalatendimento =? and coalesce(aa.situacao, 'A')<> 'C'	and coalesce(a11.excluido, 'N' )= 'N' limit 1),?,?)";
				PreparedStatement ps6 = null;
				ps6 = null;
				ps6 = con.prepareStatement(sql6);
				for (int i = 0; i < listaProfissionaisRemovidosAtendimentoEquipe.size(); i++) {
					AtendimentoBean atendimento = new AtendimentoBean();
					atendimento.setDataAtendimentoInicio( listaProfissionaisRemovidosAtendimentoEquipe.get(i).getDataAtendimento());
					atendimento.getPrograma().setIdPrograma(listaProfissionaisRemovidosAtendimentoEquipe.get(i).getPrograma().getIdPrograma());
					atendimento.getGrupo().setIdGrupo(listaProfissionaisRemovidosAtendimentoEquipe.get(i).getGrupo().getIdGrupo());
					atendimento.getEquipe().setCodEquipe(listaProfissionaisRemovidosAtendimentoEquipe.get(i).getEquipe().getCodEquipe());
					atendimento.getFuncionario().setId(listaProfissionaisRemovidosAtendimentoEquipe.get(i).getFuncionario().getId());
					if (aDao.verificaSeExisteAtendimentoparaProfissionalNaDataNaEquipe(atendimento) ) {
						String sql8 = "update hosp.atendimentos1 set excluido='S', usuario_exclusao=?, data_hora_exclusao=current_timestamp where id_atendimentos1=(select id_atendimentos1 from hosp.atendimentos1 " +
								"	a11 join hosp.atendimentos aa on aa.id_atendimento = a11.id_atendimento " +
								"	 where aa.dtaatende=? and  aa.codprograma=? and aa.codgrupo=?  and a11.codprofissionalatendimento =? and coalesce(aa.situacao, 'A')<> 'C'	and coalesce(a11.excluido, 'N' )= 'N'  limit 1)" ;


						PreparedStatement ps8 = null;
						ps8 = con.prepareStatement(sql8);
						ps8.setLong(1, user_session.getId());
						ps8.setDate(2,new java.sql.Date( listaProfissionaisRemovidosAtendimentoEquipe.get(i).getDataAtendimento().getTime()));
						ps8.setLong(3, listaProfissionaisRemovidosAtendimentoEquipe.get(i).getPrograma().getIdPrograma());
						ps8.setLong(4, listaProfissionaisRemovidosAtendimentoEquipe.get(i).getGrupo().getIdGrupo());
						ps8.setLong(5, listaProfissionaisRemovidosAtendimentoEquipe.get(i).getFuncionario().getId());
						ps8.execute();
						ps6 = con.prepareStatement(sql6);

						ps6.setDate(1,new java.sql.Date( listaProfissionaisRemovidosAtendimentoEquipe.get(i).getDataAtendimento().getTime()));
						ps6.setLong(2, listaProfissionaisRemovidosAtendimentoEquipe.get(i).getPrograma().getIdPrograma());
						ps6.setLong(3, listaProfissionaisRemovidosAtendimentoEquipe.get(i).getGrupo().getIdGrupo());
						ps6.setLong(4, listaProfissionaisRemovidosAtendimentoEquipe.get(i).getFuncionario().getId());
						ps6.setLong(5, listaProfissionaisRemovidosAtendimentoEquipe.get(i).getId());
						ps6.setLong(6, listaProfissionaisRemovidosAtendimentoEquipe.get(i).getFuncionario().getId());
						ps6.execute();
					}
				}
			}

			gravarValidacaoSigtapAnterior(con, idAtendimento, validaSigtapAnterior);
			if(user_session.getUnidade().getParametro().isVerificaPeriodoInicialEvolucaoPrograma()) {
				for (AtendimentoBean atendimento : lista) {
					verificarInconsistenciaEvolucaoProgramaGrupo(atendimento, con);
				}
			}

			con.commit();
			alterou = true;
		} catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				//comentado walter erro log ex.printStackTrace();
			}
		}
		return alterou;
	}

	private void alteraAtendimentoComInconsistenciaLog(AtendimentoBean atendimento, Connection conAuxiliar)
			throws SQLException {

		try {
			PreparedStatement ps2;
			String sql = " UPDATE hosp.atendimentos1 SET id_situacao_atendimento = ? " + " where id_atendimentos1 = "
					+ "	( select distinct a3.id_atendimentos1 from hosp.atendimentos1 a3 "
					+ "                    	join hosp.atendimentos ate on ate.id_atendimento = a3.id_atendimento "
					+ "                    	join hosp.inconsistencias_log il on a3.id_atendimentos1 = il.id_atendimento1 "
					+ "                    	where il.id_atendimento1 = ? );";

			ps2 = conAuxiliar.prepareStatement(sql);

			if(VerificadorUtil.verificarSeObjetoNuloOuZero(atendimento.getSituacaoAtendimento().getId()))
				ps2.setNull(1,Types.NULL);
			else
				ps2.setInt(1, atendimento.getSituacaoAtendimento().getId());
			ps2.setLong(2, atendimento.getId1());
			ps2.executeUpdate();
		} catch (Exception e) {
			conAuxiliar.rollback();
		}
	}

	private List<Integer> retornaAtendimentosComInconsistenciaLog(Integer idAtendimento, Connection conAuxiliar)
			throws SQLException {

		List<Integer> listaIdAtendimento1 = new ArrayList<>();
		try {
			PreparedStatement ps2;
			String sql = "	select distinct a3.id_atendimentos1 from hosp.atendimentos1 a3 "
					+ "                    	join hosp.atendimentos ate on ate.id_atendimento = a3.id_atendimento "
					+ "                    	join hosp.inconsistencias_log il on a3.id_atendimentos1 = il.id_atendimento1 "
					+ "						where ate.id_atendimento = ?;";

			ps2 = conAuxiliar.prepareStatement(sql);
			ps2.setLong(1, idAtendimento);
			ResultSet rs = ps2.executeQuery();
			while(rs.next())
				listaIdAtendimento1.add(rs.getInt("id_atendimentos1"));
		} catch (Exception e) {
			conAuxiliar.rollback();
		}
		return listaIdAtendimento1;
	}

	private void verificarInconsistenciaEvolucaoProgramaGrupo
			(AtendimentoBean atendimento, Connection conexaoAuxiliar) throws ProjetoException, SQLException {

		List<ProgramaGrupoEvolucaoBean> listaProgramaGrupoEvolucao =
				new UnidadeDAO().carregarProgramasEGruposEmEvolucao(user_session.getUnidade().getId(), conexaoAuxiliar);

		boolean existe = false;
		for (ProgramaGrupoEvolucaoBean programaGrupoEvolucao : listaProgramaGrupoEvolucao) {
			if(programaGrupoEvolucao.getPrograma().getIdPrograma() == atendimento.getPrograma().getIdPrograma()
					&& programaGrupoEvolucao.getGrupo().getIdGrupo() == atendimento.getGrupo().getIdGrupo()) {
				existe = true;
			}
		}
		if(!existe && !VerificadorUtil.verificarSeObjetoNuloOuZero(atendimento.getSituacaoAtendimento().getId())) {
			gravarTabelaInconsistenciasAtendimento
					(atendimento.getId1(), TipoInconsistencia.EVOLUCAO_PROGRAMA_GRUPO.getSigla(), conexaoAuxiliar);
		}
	}


	private List<AtendimentoBean> removeAtendimentosNaoExcluidos(List<AtendimentoBean> lista,
																 List<Integer> listaIdAtendimento01QueNaoPodemTerRegistroExcluidos) {
		List<AtendimentoBean> listaAtendimentosParaUpdate = new ArrayList<AtendimentoBean>();

		List<AtendimentoBean> listaAtendimentoAuxiliar = new ArrayList<AtendimentoBean>();
		listaAtendimentoAuxiliar.addAll(lista);
		for (AtendimentoBean atendimento : lista) {
			if (listaIdAtendimento01QueNaoPodemTerRegistroExcluidos.contains(atendimento.getId1())
					&& !atendimento.getSituacaoAtendimentoAnterior().getAtendimentoRealizado()) {
				listaAtendimentoAuxiliar.remove(atendimento);
				listaAtendimentosParaUpdate.add(atendimento);
			}
		}

		lista.clear();
		lista.addAll(listaAtendimentoAuxiliar);
		return listaAtendimentosParaUpdate;
	}

	public void alterarSituacaoAtendimentosNaoRegravados
			(List<AtendimentoBean> listaAtendimentosComSituacaoAlterada, Connection conexao) throws SQLException, ProjetoException {

		String sql = "UPDATE hosp.atendimentos1 SET id_situacao_atendimento = ? WHERE id_atendimentos1 = ?" ;

		try {
			PreparedStatement stmt = conexao.prepareStatement(sql);

			for (AtendimentoBean atendimento : listaAtendimentosComSituacaoAlterada) {
				if(VerificadorUtil.verificarSeObjetoNuloOuZero(atendimento.getSituacaoAtendimento().getId()))
					stmt.setNull(1, Types.NULL);
				else
					stmt.setInt(1, atendimento.getSituacaoAtendimento().getId());
				stmt.setInt(2, atendimento.getId1());
				stmt.execute();
			}
		} catch (SQLException ex2) {
			conexao.rollback();
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			conexao.rollback();
			throw new ProjetoException(ex, this.getClass().getName());
		}
	}

	public Boolean insereProfissionalParaRealizarAtendimentoNaEquipe(AtendimentoBean atendimento, FuncionarioBean novoProfissional)
			throws ProjetoException {
		boolean alterou = false;
		con = ConnectionFactory.getConnection();
		try {

			String sql2 = "INSERT INTO hosp.atendimentos1( id_atendimento, codprofissionalatendimento, "
					+ " cbo, codprocedimento) VALUES ( ?, ?, ?,(select coalesce(cod_procedimento, null) from hosp.programa where programa.id_programa=?));";

			PreparedStatement stmt2 = con.prepareStatement(sql2);
			stmt2.setLong(1, atendimento.getId());
			stmt2.setLong(2, novoProfissional.getId());
			if (VerificadorUtil.verificarSeObjetoNuloOuZero(atendimento.getCbo().getCodCbo())) {
				stmt2.setNull(3, Types.NULL);
			} else {
				stmt2.setInt(3, atendimento.getCbo().getCodCbo());
			}

			stmt2.setInt(4, atendimento.getPrograma().getIdPrograma());

			stmt2.executeUpdate();
			con.commit();
			alterou = true;

		} catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				//comentado walter erro log ex.printStackTrace();
			}
		}
		return alterou;
	}

	public Boolean cancelarAtendimentoProfissional(AtendimentoBean atendimento) throws ProjetoException {
		boolean alterou = false;
		con = ConnectionFactory.getConnection();
		try {

			String sql = "update hosp.atendimentos1 set dtaatendido = null, situacao = '' "
					+ " where id_atendimentos1 = ?";

			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, atendimento.getId1());

			stmt.executeUpdate();
			con.commit();
			alterou = true;
		} catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				//comentado walter erro log ex.printStackTrace();
			}
		}
		return alterou;
	}

	public List<AtendimentoBean> carregaAtendimentos(AtendimentoBean atendimento, String campoBusca, String tipo)
			throws ProjetoException {

		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().get("obj_funcionario");

		String sql = "select distinct a.id_atendimento, a.dtaatende, a.codpaciente, p.nome, p.cns, a.turno, a.codmedico, f.descfuncionario, " +
				" a.codprograma, pr.descprograma, a.codtipoatendimento, t.desctipoatendimento, p.matricula,  " +
				" a.codequipe, e.descequipe, a.avaliacao,   " +
				" case when t.equipe_programa is true then 'Sim' else 'Não' end as ehEquipe, coalesce(a.presenca, 'N') presenca, a.avulso,  " +
				" case when  " +
				" (select count(*) from hosp.atendimentos1 a1 where a1.id_atendimento = a.id_atendimento and a1.id_situacao_atendimento is null and coalesce(a1.excluido,'N')='N') =   " +
				" (select count(*) from hosp.atendimentos1 a1 where a1.id_atendimento = a.id_atendimento and coalesce(a1.excluido,'N')='N')  " +
				" then 'Atendimento Não Informado'  when  " +
				" (select count(*) from hosp.atendimentos1 a1 where a1.id_atendimento = a.id_atendimento and a1.id_situacao_atendimento is not null and coalesce(a1.excluido,'N')='N') =  " +
				" (select count(*) from hosp.atendimentos1 a1 where a1.id_atendimento = a.id_atendimento and coalesce(a1.excluido,'N')='N')  " +
				" then 'Atendimento Informado'  else 'Atendimento Informado Parcialmente'  end as situacao  " +
				" from hosp.atendimentos a left join hosp.pacientes p on (p.id_paciente = a.codpaciente) " +
				" join hosp.atendimentos1 a1 on a1.id_atendimento = a.id_atendimento " +
				" left join acl.funcionarios f on (f.id_funcionario = a.codmedico) " +
				" left join hosp.programa pr on (pr.id_programa = a.codprograma) " +
				" left join hosp.tipoatendimento t on (t.id = a.codtipoatendimento) " +
				" left join hosp.equipe e on (e.id_equipe = a.codequipe) " +
				" where a.dtaatende >= ? and a.dtaatende <= ? and a.cod_unidade = ? and coalesce(a.situacao, '')<> 'C' and coalesce(a1.excluido,'N' )='N'";

		if (!VerificadorUtil.verificarSeObjetoNulo(atendimento.getPrograma())
				&& !VerificadorUtil.verificarSeObjetoNuloOuZero(atendimento.getPrograma().getIdPrograma())) {
			sql = sql + " and  a.codprograma = ?";
		}
		if (!VerificadorUtil.verificarSeObjetoNulo(atendimento.getGrupo())
				&& !VerificadorUtil.verificarSeObjetoNuloOuZero(atendimento.getGrupo().getIdGrupo())) {
			sql = sql + " and  a.codgrupo = ?";
		}
		if (!VerificadorUtil.verificarSeObjetoNulo(atendimento.getEquipe())
				&& !VerificadorUtil.verificarSeObjetoNuloOuZero(atendimento.getEquipe().getCodEquipe())) {
			sql = sql + " and  a.codequipe = ?";
		}

		if (tipo.equals("nome")) {
			sql = sql + " and p.nome like ?";
		} else if (tipo.equals("cpf")) {
			sql = sql + " and p.cpf like ?";
		} else if (tipo.equals("cns")) {
			sql = sql + " and p.cns like ?";
		} else if (tipo.equals("prontuario")) {
			sql = sql + " and p.id_paciente = ?";
		} else if (tipo.equals("matricula")) {
			sql = sql + " and p.matricula like ?";
		}

		sql = sql + " order by a.dtaatende";

		ArrayList<AtendimentoBean> lista = new ArrayList<AtendimentoBean>();

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			int i = 4;
			stm.setDate(1, new java.sql.Date(atendimento.getDataAtendimentoInicio().getTime()));
			stm.setDate(2, new java.sql.Date(atendimento.getDataAtendimentoFinal().getTime()));
			stm.setInt(3, user_session.getUnidade().getId());

			if (!VerificadorUtil.verificarSeObjetoNulo(atendimento.getPrograma())
					&& !VerificadorUtil.verificarSeObjetoNuloOuZero(atendimento.getPrograma().getIdPrograma())) {
				stm.setInt(i, atendimento.getPrograma().getIdPrograma());
				i = i + 1;
			}
			if (!VerificadorUtil.verificarSeObjetoNulo(atendimento.getGrupo())
					&& !VerificadorUtil.verificarSeObjetoNuloOuZero(atendimento.getGrupo().getIdGrupo())) {
				stm.setInt(i, atendimento.getGrupo().getIdGrupo());
				i = i + 1;
			}

			if (!VerificadorUtil.verificarSeObjetoNulo(atendimento.getEquipe())
					&& !VerificadorUtil.verificarSeObjetoNuloOuZero(atendimento.getEquipe().getCodEquipe())) {
				stm.setInt(i, atendimento.getEquipe().getCodEquipe());
				i = i + 1;
			}

			if (!campoBusca.equals(null)) {
				if ((tipo.equals("nome")) || (tipo.equals("cpf")) || (tipo.equals("cns")) || (tipo.equals("matricula")))
					stm.setString(i, "%" + campoBusca.toUpperCase() + "%");
				else
					stm.setInt(i, Integer.valueOf(campoBusca));
				i = i + 1;
			}

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				AtendimentoBean at = new AtendimentoBean();
				at.setId(rs.getInt("id_atendimento"));
				at.setDataAtendimentoInicio(rs.getDate("dtaatende"));
				at.getPaciente().setId_paciente(rs.getInt("codpaciente"));
				at.getPaciente().setNome(rs.getString("nome"));
				at.getPaciente().setCns(rs.getString("cns"));
				at.getPaciente().setMatricula(rs.getString("matricula"));
				at.setTurno(rs.getString("turno"));
				at.getFuncionario().setId(rs.getLong("codmedico"));
				at.getFuncionario().setNome(rs.getString("descfuncionario"));
				at.getPrograma().setIdPrograma(rs.getInt("codprograma"));
				at.getPrograma().setDescPrograma(rs.getString("descprograma"));
				at.setSituacao(rs.getString("situacao"));
				at.getTipoAt().setIdTipo(rs.getInt("codtipoatendimento"));
				at.getTipoAt().setDescTipoAt(rs.getString("desctipoatendimento"));
				at.getEquipe().setCodEquipe(rs.getInt("codequipe"));
				at.getEquipe().setDescEquipe(rs.getString("descequipe"));
				at.setPresenca(rs.getString("presenca"));
				at.setAvulso(rs.getBoolean("avulso"));
				at.setEhEquipe(rs.getString("ehEquipe"));
				at.setAvaliacao(rs.getBoolean("avaliacao"));

				if(at.getAvulso() || at.getEhEquipe().equalsIgnoreCase("Sim"))
					at.setListaNomeProfissionais(retornaNomeProfissionaisAtendimento(rs.getInt("id_atendimento"), con));

				lista.add(at);
			}

		} catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				//comentado walter erro log ex.printStackTrace();
			}
		}
		return lista;
	}


	public List<String> retornaNomeProfissionaisAtendimento(int idAtendimento, Connection con) throws SQLException, ProjetoException {

		List<String> listaProfissionais = new ArrayList<>();
		String sql = "select distinct f.descfuncionario " +
				" from acl.funcionarios f " +
				" join hosp.atendimentos1 a1 on (f.id_funcionario = a1.codprofissionalatendimento) " +
				" join hosp.atendimentos a on a.id_atendimento = a1.id_atendimento " +
				" where a1.id_atendimento = ? and coalesce(a1.excluido, 'N') = 'N' " +
				" order by 1";
		try {
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, idAtendimento);

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				listaProfissionais.add(rs.getString("descfuncionario"));
			}

		} catch (SQLException ex2) {
			con.rollback();
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			con.rollback();
			throw new ProjetoException(ex, this.getClass().getName());
		}
		return listaProfissionais;
	}

	public Integer retornaQuantidadeDePendenciasAnterioresDeEvolucao
			(Integer idUnidade, Long codigoProfissionalAtendimento, Date dataAtende) throws ProjetoException {

		Integer quantidadeDePendenciasAnterioresDeEvolucao = 0;
		String sql = "select count(*) qtd from hosp.atendimentos1 a1 \r\n" +
				"	join hosp.atendimentos a on a.id_atendimento = a1.id_atendimento \r\n" +
				"	join hosp.pacientes pac on pac.id_paciente = a.codpaciente \r\n" +
				"	join acl.funcionarios f on f.id_funcionario = a1.codprofissionalatendimento \r\n" +
				"	join hosp.especialidade e on e.id_especialidade = f.codespecialidade \r\n" +
				"	JOIN hosp.unidade u ON u.id = a.cod_unidade \r\n" +
				"	join hosp.parametro pa on u.id = pa.codunidade \r\n" +
				"	JOIN hosp.empresa emp ON emp.cod_empresa = u.cod_empresa \r\n" +
				"	left join hosp.config_evolucao_unidade_programa_grupo ceu on ceu.codunidade = u.id \r\n" +
				"	left join hosp.programa p on p.id_programa = a.codprograma \r\n" +
				"	left join hosp.grupo g on g.id_grupo = a.codgrupo  \r\n" +
				"	left join hosp.situacao_atendimento sa on a1.id_situacao_atendimento = sa.id \r\n" +
				"	\r\n" +
				"	where coalesce(a.presenca,'N')='S' and a1.id_situacao_atendimento is null \r\n" +
				"	and \r\n" +
				"	case when pa.verifica_periodo_inicial_evolucao_programa = false then \r\n" +
				"		a.dtaatende >= pa.inicio_evolucao_unidade \r\n" +
				"	when pa.verifica_periodo_inicial_evolucao_programa = true then \r\n" +
				"		a.dtaatende >= ceu.inicio_evolucao \r\n" +
				"		and a.codprograma = ceu.codprograma \r\n" +
				"		and a.codgrupo = ceu.codgrupo		\r\n" +
				"	end	\r\n" +
				"	and a.dtaatende < ? \r\n" +
				"	and a1.codprofissionalatendimento = ? and coalesce(a.situacao,'A')<>'C'and coalesce(a1.excluido,'N' )='N'"
				+ " and a.cod_unidade = ? ";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement preparedStatement = con.prepareStatement(sql);
			preparedStatement.setDate(1, new java.sql.Date(dataAtende.getTime()));
			preparedStatement.setLong(2, codigoProfissionalAtendimento);
			preparedStatement.setInt(3, idUnidade);
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				quantidadeDePendenciasAnterioresDeEvolucao = rs.getInt("qtd");
			}

		} catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				//comentado walter erro log ex.printStackTrace();
			}
		}
		return quantidadeDePendenciasAnterioresDeEvolucao;
	}

	public List<PendenciaEvolucaoProgramaGrupoDTO> retornaTotalDePendenciasDeEvolucaoDoUsuarioLogado() throws ProjetoException {

		String sql = "	select count(*) total, p.descprograma, g.descgrupo from hosp.atendimentos1 a1   	\r\n" +
				"	join hosp.atendimentos a on a.id_atendimento = a1.id_atendimento   	\r\n" +
				"	join hosp.pacientes pac on pac.id_paciente = a.codpaciente   	\r\n" +
				"	join acl.funcionarios f on f.id_funcionario = a1.codprofissionalatendimento   \r\n" +
				"	join hosp.especialidade e on e.id_especialidade = f.codespecialidade   \r\n" +
				"	JOIN hosp.unidade u ON u.id = ?    \r\n" +
				"	join hosp.parametro pa on u.id = pa.codunidade \r\n" +
				"	JOIN hosp.empresa emp ON emp.cod_empresa = u.cod_empresa   	\r\n" +
				"	left join hosp.config_evolucao_unidade_programa_grupo ceu on ceu.codunidade = u.id   \r\n" +
				"	join hosp.programa p on p.id_programa = a.codprograma   	\r\n" +
				"	join hosp.grupo g on g.id_grupo = a.codgrupo    \r\n" +
				"	\r\n" +
				"	left join hosp.situacao_atendimento sa on a1.id_situacao_atendimento = sa.id  \r\n" +
				"	where coalesce(a.presenca,'N')='S' and  (a1.id_situacao_atendimento is null)   	\r\n" +
				"	and\r\n" +
				"	case when pa.verifica_periodo_inicial_evolucao_programa = false then\r\n" +
				"		a.dtaatende >= pa.inicio_evolucao_unidade \r\n" +
				"	when pa.verifica_periodo_inicial_evolucao_programa = true then \r\n" +
				"		a.dtaatende >= ceu.inicio_evolucao \r\n" +
				"		and a.codprograma = ceu.codprograma \r\n" +
				"		and a.codgrupo = ceu.codgrupo		\r\n" +
				"	end	\r\n" +
				"	and a.dtaatende<current_date   	and a1.codprofissionalatendimento = ? \r\n" +
				"	and coalesce(a.situacao,'A')<>'C'  	and coalesce(a1.excluido,'N' )='N' \r\n" +
				"	group by p.descprograma, g.descgrupo ";


		List<PendenciaEvolucaoProgramaGrupoDTO> listaPendenciasEvolucao = new ArrayList<>();
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement preparedStatement = con.prepareStatement(sql);
			preparedStatement.setInt(1, user_session.getUnidade().getId());
			preparedStatement.setLong(2, user_session.getId());
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				PendenciaEvolucaoProgramaGrupoDTO pendenciaEvolucaoProgramaGrupo = new PendenciaEvolucaoProgramaGrupoDTO();
				pendenciaEvolucaoProgramaGrupo.setTotalPendencia(rs.getInt("total"));
				pendenciaEvolucaoProgramaGrupo.getPrograma().setDescPrograma(rs.getString("descprograma"));
				pendenciaEvolucaoProgramaGrupo.getGrupo().setDescGrupo(rs.getString("descgrupo"));
				listaPendenciasEvolucao.add(pendenciaEvolucaoProgramaGrupo);
			}

		} catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				//comentado walter erro log ex.printStackTrace();
			}
		}
		return listaPendenciasEvolucao;
	}

	public List<AtendimentoBean> carregaAtendimentosDoProfissionalNaEquipe(AtendimentoBean atendimento,
																		   String campoBusca, String tipo, String buscaEvolucao, String buscaTurno, boolean listaEvolucoesPendentes) throws ProjetoException {

		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().get("obj_funcionario");

		String sql = "select distinct  coalesce(a.presenca,'N') presenca, a.id_atendimento,  a.dtaatende, a.codpaciente, p.nome, p.cns,p.cpf,  a.turno,a1.codprofissionalatendimento, f.descfuncionario,"
				+ " a.codprograma, pr.descprograma, a.codtipoatendimento, t.desctipoatendimento,"
				+ " a.codequipe, e.descequipe, a.codgrupo, g.descgrupo, a.avaliacao, parm.bloqueia_por_pendencia_evolucao_anterior, "
				+ " case when t.equipe_programa is true then 'Sim' else 'Não' end as ehEquipe, a.cod_unidade, a1.id_situacao_atendimento, sa.abono_falta, sa.descricao descsitatendimento,  "

				+ " case when exists (select a11.id_atendimento "
				+ "		from hosp.atendimentos1 a11	left join hosp.situacao_atendimento sa1 on a11.id_situacao_atendimento = sa1.id"
				+ "  where "
				+ "			a11.id_atendimento = a.id_atendimento "
				+ "			and a11.codprofissionalatendimento=? "
				+ "			and sa1.id is null and coalesce(sa1.abono_falta, false)=false and coalesce(a11.excluido,'N')='N')  then 'Evolução Não Realizada' "
				+ "		 else 'Evolução Realizada'\n" + "	end as situacao  "

				+ " from hosp.atendimentos a" + " left join hosp.pacientes p on (p.id_paciente = a.codpaciente)"
				+ " JOIN hosp.atendimentos1 a1 ON (a.id_atendimento = a1.id_atendimento)"
				+ " left join hosp.situacao_atendimento sa on a1.id_situacao_atendimento = sa.id "
				+ " left join acl.funcionarios f on (f.id_funcionario = a1.codprofissionalatendimento) "
				+ " left join hosp.programa pr on (pr.id_programa = a.codprograma) "
				+ " left join hosp.grupo g on (g.id_grupo = a.codgrupo) "
				+ " left join hosp.tipoatendimento t on (t.id = a.codtipoatendimento) "
				+ " left join hosp.equipe e on (e.id_equipe = a.codequipe) "
				+ " left join hosp.parametro parm on (parm.codunidade = a.cod_unidade) and coalesce(a.situacao, 'A')<> 'C'	and coalesce(a1.excluido, 'N' )= 'N' ";


		if(listaEvolucoesPendentes && !user_session.getUnidade().getParametro().isVerificaPeriodoInicialEvolucaoPrograma()) {
			sql += " where a.dtaatende >= parm.inicio_evolucao_unidade and a.dtaatende<=current_date and coalesce(a.presenca,'N')='S' and a1.id_situacao_atendimento is null ";
		}

		else if(listaEvolucoesPendentes) {
			sql +=  " join hosp.config_evolucao_unidade_programa_grupo ceu on ceu.codunidade = a.cod_unidade and ceu.codprograma = a.codprograma and ceu.codgrupo = a.codgrupo  "
					+ " where a.dtaatende >= ceu.inicio_evolucao and a.dtaatende<=current_date and coalesce(a.presenca,'N')='S' and a1.id_situacao_atendimento is null ";
		}

		else
			sql += " where a.dtaatende >= ? and a.dtaatende <= ? ";

		sql = sql + " and a.cod_unidade = ? and coalesce(a1.excluido,'N')='N' and coalesce(a.situacao,'A')<>'C'"
				+ " and exists (select id_atendimento from hosp.atendimentos1 a11 "
				+ " where a11.codprofissionalatendimento=? and a11.id_atendimento = a.id_atendimento and coalesce(a11.excluido,'N')='N') and a1.codprofissionalatendimento=?";

		if (!VerificadorUtil.verificarSeObjetoNulo(atendimento.getPrograma())
				&& !VerificadorUtil.verificarSeObjetoNuloOuZero(atendimento.getPrograma().getIdPrograma())) {
			sql = sql + " and  a.codprograma = ?";
		}
		if (!VerificadorUtil.verificarSeObjetoNulo(atendimento.getGrupo())
				&& !VerificadorUtil.verificarSeObjetoNuloOuZero(atendimento.getGrupo().getIdGrupo())) {
			sql = sql + " and  a.codgrupo = ?";
		}
		if (!VerificadorUtil.verificarSeObjetoNulo(atendimento.getEquipe())
				&& !VerificadorUtil.verificarSeObjetoNuloOuZero(atendimento.getEquipe().getCodEquipe())) {
			sql = sql + " and  a.codequipe = ?";
		}

		if ((buscaEvolucao!=null) && (buscaEvolucao.equals(BuscaEvolucao.COM_EVOLUCAO.getSigla()))) {
			sql = sql + " and a1.id_situacao_atendimento IS NOT NULL ";
		}
		if ((buscaEvolucao!=null) && (buscaEvolucao.equals(BuscaEvolucao.SEM_EVOLUCAO.getSigla()))) {
			sql = sql + " and a1.id_situacao_atendimento IS NULL ";
		}

		if (!buscaTurno.equals("A")) {
			sql = sql + " AND a.turno=? ";
		}

		if (tipo.equals("nome")) {
			sql = sql + " and p.nome like ?";
		} else if (tipo.equals("cpf")) {
			sql = sql + " and p.cpf like ?";
		} else if (tipo.equals("cns")) {
			sql = sql + " and p.cns like ?";
		} else if (tipo.equals("prontuario")) {
			sql = sql + " and p.id_paciente = ?";
		} else if (tipo.equals("matricula")) {
			sql = sql + " and p.matricula like ?";
		}

		sql = sql + " order by presenca desc, a.dtaatende, p.nome";

		ArrayList<AtendimentoBean> lista = new ArrayList<AtendimentoBean>();

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			int i;
			if(listaEvolucoesPendentes) {
				stm.setLong(1, user_session.getId());
				stm.setInt(2, user_session.getUnidade().getId());
				stm.setLong(3, user_session.getId());
				stm.setLong(4, user_session.getId());
				i = 5;
			}
			else {
				stm.setLong(1, user_session.getId());
				stm.setDate(2, new java.sql.Date(atendimento.getDataAtendimentoInicio().getTime()));
				stm.setDate(3, new java.sql.Date(atendimento.getDataAtendimentoFinal().getTime()));
				stm.setInt(4, user_session.getUnidade().getId());
				stm.setLong(5, user_session.getId());
				stm.setLong(6, user_session.getId());
				i = 7;
			}

			if (!VerificadorUtil.verificarSeObjetoNulo(atendimento.getPrograma())
					&& !VerificadorUtil.verificarSeObjetoNuloOuZero(atendimento.getPrograma().getIdPrograma())) {
				stm.setInt(i, atendimento.getPrograma().getIdPrograma());
				i = i + 1;
			}
			if (!VerificadorUtil.verificarSeObjetoNulo(atendimento.getGrupo())
					&& !VerificadorUtil.verificarSeObjetoNuloOuZero(atendimento.getGrupo().getIdGrupo())) {
				stm.setInt(i, atendimento.getGrupo().getIdGrupo());
				i = i + 1;
			}

			if (!VerificadorUtil.verificarSeObjetoNulo(atendimento.getEquipe())
					&& !VerificadorUtil.verificarSeObjetoNuloOuZero(atendimento.getEquipe().getCodEquipe())) {
				stm.setInt(i, atendimento.getEquipe().getCodEquipe());
				i = i + 1;
			}

			if (!buscaTurno.equals("A")) {
				stm.setString(i, buscaTurno);
				i++;
			}

			if (campoBusca != null) {
				if (!campoBusca.equals(null)) {
					if ((tipo.equals("nome")) || (tipo.equals("cpf")) || (tipo.equals("cns"))
							|| (tipo.equals("matricula")))
						stm.setString(i, "%" + campoBusca.toUpperCase() + "%");
					else
						stm.setInt(i, Integer.valueOf(campoBusca));
					i = i + 1;
				}
			}

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				AtendimentoBean atendimentoBean = new AtendimentoBean();
				atendimentoBean.setPresenca(rs.getString("presenca"));
				atendimentoBean.setId(rs.getInt("id_atendimento"));
				//atendimentoBean.setId1(rs.getInt("id_atendimentos1"));
				atendimentoBean.setDataAtendimentoInicio(rs.getDate("dtaatende"));
				atendimentoBean.getPaciente().setId_paciente(rs.getInt("codpaciente"));
				atendimentoBean.getPaciente().setNome(rs.getString("nome"));
				atendimentoBean.getPaciente().setCns(rs.getString("cns"));
				atendimentoBean.getPaciente().setCpf(rs.getString("cpf"));
				atendimentoBean.setTurno(rs.getString("turno"));
				atendimentoBean.getFuncionario().setId(rs.getLong("codprofissionalatendimento"));
				atendimentoBean.getFuncionario().setNome(rs.getString("descfuncionario"));
				atendimentoBean.getPrograma().setIdPrograma(rs.getInt("codprograma"));
				atendimentoBean.getPrograma().setDescPrograma(rs.getString("descprograma"));
				atendimentoBean.getGrupo().setIdGrupo(rs.getInt("codgrupo"));
				atendimentoBean.getGrupo().setDescGrupo(rs.getString("descgrupo"));
				atendimentoBean.setSituacao(rs.getString("situacao"));
				atendimentoBean.getTipoAt().setIdTipo(rs.getInt("codtipoatendimento"));
				atendimentoBean.getTipoAt().setDescTipoAt(rs.getString("desctipoatendimento"));
				atendimentoBean.getEquipe().setCodEquipe(rs.getInt("codequipe"));
				atendimentoBean.getEquipe().setDescEquipe(rs.getString("descequipe"));
				atendimentoBean.setEhEquipe(rs.getString("ehEquipe"));
				atendimentoBean.setAvaliacao(rs.getBoolean("avaliacao"));
				atendimentoBean.getUnidade().setId(rs.getInt("cod_unidade"));
				atendimentoBean.getUnidade().getParametro().setBloqueiaPorPendenciaEvolucaoAnterior
						(rs.getBoolean("bloqueia_por_pendencia_evolucao_anterior"));
				atendimentoBean.getSituacaoAtendimento().setId(rs.getInt("id_situacao_atendimento"));
				atendimentoBean.getSituacaoAtendimento().setDescricao(rs.getString("descsitatendimento"));
				atendimentoBean.getSituacaoAtendimento().setAbonoFalta(rs.getBoolean("abono_falta"));
				lista.add(atendimentoBean);
			}

		} catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				//comentado walter erro log ex.printStackTrace();
			}
		}
		return lista;
	}

	public AtendimentoBean listarAtendimentoProfissionalPorId(int id) throws ProjetoException {

		AtendimentoBean atendimento = new AtendimentoBean();
		String sql = "select a.id_atendimento, a1.id_atendimentos1, a.dtaatende, a.codpaciente, p.nome,a1.codprofissionalatendimento , f.descfuncionario, a1.codprocedimento, pr.codproc, p.dtanascimento, p.sexo, "
				+ "pr.nome as procedimento, a1.id_situacao_atendimento, sa.descricao, sa.atendimento_realizado, a1.evolucao, a.avaliacao, a.cod_laudo, a.grupo_avaliacao, "
				+ "a.codprograma,a1.cbo codcbo, cbo.codigo codigocbo, a1.id_cidprimario "
				+ "from hosp.atendimentos a " + "join hosp.atendimentos1 a1 on a1.id_atendimento = a.id_atendimento "
				+ "left join hosp.situacao_atendimento sa on sa.id = a1.id_situacao_atendimento "
				+ "left join hosp.pacientes p on (p.id_paciente = a.codpaciente) "
				+ "left join acl.funcionarios f on (f.id_funcionario =a1.codprofissionalatendimento) "
				+ "left join hosp.programa on (programa.id_programa = a.codprograma) "
				+ "left join hosp.proc pr on (pr.id = coalesce(a1.codprocedimento, programa.cod_procedimento)) "
				+ " left join hosp.cbo  on cbo.id  = a1.cbo "
				+ "where a.id_atendimento = ? and coalesce(a.situacao, 'A')<> 'C' and coalesce(a1.excluido, 'N' )= 'N' "
				+ "and pr.ativo = 'S'";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, id);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				atendimento.setId(rs.getInt("id_atendimento"));
				atendimento.setId1(rs.getInt("id_atendimentos1"));
				atendimento.setDataAtendimentoInicio(rs.getDate("dtaatende"));
				atendimento.getPaciente().setId_paciente(rs.getInt("codpaciente"));
				atendimento.getPaciente().setSexo(rs.getString("sexo"));
				atendimento.getPaciente().setDtanascimento(rs.getDate("dtanascimento"));
				atendimento.getPaciente().setNome(rs.getString("nome"));
				atendimento.getProcedimento().setIdProc(rs.getInt("codprocedimento"));
				atendimento.getProcedimento().setCodProc(rs.getString("codproc"));
				atendimento.getProcedimento().setNomeProc(rs.getString("procedimento"));
				atendimento.getFuncionario().setId(rs.getLong("codprofissionalatendimento"));
				atendimento.getFuncionario().setNome(rs.getString("descfuncionario"));
				atendimento.getCbo().setCodCbo(rs.getInt("codcbo"));
				atendimento.getCbo().setCodigo(rs.getString("codigocbo"));
				atendimento.getSituacaoAtendimento().setId(rs.getInt("id_situacao_atendimento"));
				atendimento.getSituacaoAtendimento().setDescricao(rs.getString("descricao"));
				atendimento.getSituacaoAtendimento().setAtendimentoRealizado(rs.getBoolean("atendimento_realizado"));
				atendimento.getSituacaoAtendimentoAnterior().setId(rs.getInt("id_situacao_atendimento"));
				atendimento.getSituacaoAtendimentoAnterior().setDescricao(rs.getString("descricao"));
				atendimento.getSituacaoAtendimentoAnterior().setAtendimentoRealizado(rs.getBoolean("atendimento_realizado"));

				atendimento.setEvolucao(rs.getString("evolucao"));
				atendimento.setAvaliacao(rs.getBoolean("avaliacao"));
				atendimento.getInsercaoPacienteBean().getLaudo().setId(rs.getInt("cod_laudo"));
				atendimento.setGrupoAvaliacao(new GrupoDAO().listarGrupoPorIdComConexao(rs.getInt("grupo_avaliacao"), con));
				atendimento.setPrograma(new ProgramaDAO().listarProgramaPorIdComConexao(rs.getInt("codprograma"), con));
				atendimento.getCidPrimario().setIdCid(rs.getInt("id_cidprimario"));
			}

		} catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				//comentado walter erro log ex.printStackTrace();
			}
		}
		return atendimento;
	}

	public AtendimentoBean listarAtendimentoProfissionalPaciente(int id) throws ProjetoException {

		AtendimentoBean atendimento = new AtendimentoBean();
		String sql = "select a.id_atendimento, a1.id_atendimentos1, a.dtaatende, a.codpaciente, p.nome, a1.codprofissionalatendimento, f.descfuncionario, "
				+ "pr.nome as procedimento, a1.id_situacao_atendimento, sa.descricao, sa.atendimento_realizado, a1.evolucao, a.avaliacao, "
				+ "a.cod_laudo, a.grupo_avaliacao, a.codprograma, pro.descprograma, coalesce(a.presenca,'N') presenca, pr.codproc codigo_procedimento, pr.id id_proc, p.dtanascimento, p.sexo, "
				+ " a.codgrupo, g.descgrupo, a1.cbo codcbo,cbo.codigo codigocbo,  pro.permite_alteracao_cid_evolucao, a1.id_cidprimario, c.desccidabrev, con.id id_conselho, con.descricao conselho from hosp.atendimentos a "
				+ "join hosp.atendimentos1 a1 on a1.id_atendimento = a.id_atendimento "
				+ "left join hosp.situacao_atendimento sa on sa.id = a1.id_situacao_atendimento "
				+ "left join hosp.programa pro on (pro.id_programa = a.codprograma)"
				+ "left join hosp.grupo g on (g.id_grupo = a.codgrupo)"
				+ "left join hosp.pacientes p on (p.id_paciente = a.codpaciente) "
				+ "left join acl.funcionarios f on (f.id_funcionario =a1.codprofissionalatendimento) "
				+ "left join hosp.proc pr on (pr.id = a1.codprocedimento) "
				+ "left join hosp.cid c on c.cod = a1.id_cidprimario "+
				"left join hosp.cbo_conselho cc on a1.cbo = cc.id_cbo  " +
				"left join hosp.conselho con on cc.id_conselho = con.id  "
				+ " left join hosp.cbo  on cbo.id  = a1.cbo "
				+ "where a.id_atendimento = ? and a1.codprofissionalatendimento=? and coalesce(a.situacao, 'A')<> 'C'	and coalesce(a1.excluido, 'N' )= 'N' ";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, id);
			stm.setLong(2, user_session.getId());
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				atendimento.setId(rs.getInt("id_atendimento"));
				atendimento.setId1(rs.getInt("id_atendimentos1"));
				atendimento.setDataAtendimento(rs.getDate("dtaatende"));
				atendimento.setDataAtendimentoInicio(rs.getDate("dtaatende"));
				atendimento.getPaciente().setId_paciente(rs.getInt("codpaciente"));
				atendimento.getPaciente().setNome(rs.getString("nome"));
				atendimento.getPaciente().setDtanascimento(rs.getDate("dtanascimento"));
				atendimento.getPaciente().setSexo(rs.getString("sexo"));
				atendimento.getProcedimento().setIdProc(rs.getInt("id_proc"));
				atendimento.getProcedimento().setNomeProc(rs.getString("procedimento"));
				atendimento.getProcedimento().setCodProc(rs.getString("codigo_procedimento"));
				atendimento.getFuncionario().setId(rs.getLong("codprofissionalatendimento"));
				atendimento.getFuncionario().setNome(rs.getString("descfuncionario"));
				atendimento.getCbo().setCodCbo(rs.getInt("codcbo"));
				atendimento.getCbo().setCodigo(rs.getString("codigocbo"));
				atendimento.getCbo().getConselho().setId(rs.getInt("id_conselho"));
				atendimento.getCbo().getConselho().setDescricao(rs.getString("conselho"));
				atendimento.getSituacaoAtendimento().setId(rs.getInt("id_situacao_atendimento"));
				atendimento.getSituacaoAtendimento().setDescricao(rs.getString("descricao"));
				atendimento.getSituacaoAtendimento().setAtendimentoRealizado(rs.getBoolean("atendimento_realizado"));
				atendimento.setEvolucao(rs.getString("evolucao"));
				atendimento.setAvaliacao(rs.getBoolean("avaliacao"));
				atendimento.getInsercaoPacienteBean().getLaudo().setId(rs.getInt("cod_laudo"));
				atendimento.getPrograma().setIdPrograma(rs.getInt("codprograma"));
				atendimento.getPrograma().setDescPrograma(rs.getString("descprograma"));
				atendimento.getPrograma().setPermiteAlteracaoCidNaEvolucao(rs.getBoolean("permite_alteracao_cid_evolucao"));
				atendimento.getGrupo().setIdGrupo(rs.getInt("codgrupo"));
				atendimento.getGrupo().setDescGrupo(rs.getString("descgrupo"));
				atendimento.setGrupoAvaliacao(new GrupoDAO().listarGrupoPorIdComConexao(rs.getInt("grupo_avaliacao"), con));
				atendimento.setPrograma(new ProgramaDAO().listarProgramaPorIdComConexao(rs.getInt("codprograma"), con));
				atendimento.setPresenca(rs.getString("presenca"));
				atendimento.getCidPrimario().setIdCid(rs.getInt("id_cidprimario"));
				atendimento.getCidPrimario().setDescCidAbrev(rs.getString("desccidabrev"));
			}

			atendimento.setListaProcedimentoCid(listarProcedimentosCids(con, atendimento.getId1()));

		} catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				//comentado walter erro log ex.printStackTrace();
			}
		}
		return atendimento;
	}

	private List<ProcedimentoCidDTO> listarProcedimentosCids (Connection conexao, Integer idAtendimentos1) throws SQLException, ProjetoException {

		List<ProcedimentoCidDTO> listaProcedimentoCid = new ArrayList<>();

		String sql = "select aps.id, p.id id_procedimento, p.codproc, p.nome, c.cod, c.cid, c.desccidabrev " +
				"	from hosp.atendimentos1_procedimento_secundario aps " +
				"	join hosp.proc p on aps.id_procedimento = p.id " +
				"	join hosp.cid c on aps.id_cid = c.cod " +
				"	where aps.id_atendimentos1 = ? and coalesce(excluido,'N')='N' order by nome;";
		try {
			PreparedStatement ps = conexao.prepareStatement(sql);
			ps.setInt(1, idAtendimentos1);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				ProcedimentoCidDTO procedimentoCid = new ProcedimentoCidDTO();
				procedimentoCid.setId(rs.getInt("id"));
				procedimentoCid.getProcedimento().setIdProc(rs.getInt("id_procedimento"));
				procedimentoCid.getProcedimento().setCodProc(rs.getString("codproc"));
				procedimentoCid.getProcedimento().setNomeProc(rs.getString("nome"));
				procedimentoCid.getCid().setIdCid(rs.getInt("cod"));
				procedimentoCid.getCid().setCid(rs.getString("cid"));
				procedimentoCid.getCid().setDescCidAbrev(rs.getString("desccidabrev"));

				listaProcedimentoCid.add(procedimentoCid);
			}
		} catch (SQLException ex2) {
			conexao.rollback();
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			conexao.rollback();
			throw new ProjetoException(ex, this.getClass().getName());
		}
		return listaProcedimentoCid;
	}

	public List<AtendimentoBean> carregaAtendimentosEquipe(Integer idAtendimento) throws ProjetoException {

		String sql = "select a.dtaatende, a1.id_atendimentos1, a1.id_atendimento, a1.codprofissionalatendimento, f.descfuncionario, f.cns, " +
				" a1.cbo codcbo, c.descricao,c.codigo codigocbo,  a1.id_situacao_atendimento, sa.descricao situacao_descricao, sa.atendimento_realizado, pr.id, "+
				" a1.codprocedimento, pr.nome as procedimento, pr.codproc, a1.evolucao, a1.perfil_avaliacao, a1.id_cidprimario, " +
				" to_char(a1.horario_atendimento,'HH24:MI') horario_atendimento, a.codprograma, a.codgrupo, p.id_paciente, p.dtanascimento, p.sexo  " +
				" from hosp.atendimentos1 a1 " +
				" join hosp.atendimentos a on a.id_atendimento = a1.id_atendimento  " +
				" left join hosp.situacao_atendimento sa on sa.id = a1.id_situacao_atendimento  " +
				" left join acl.funcionarios f on (f.id_funcionario = a1.codprofissionalatendimento) " +
				" left join hosp.cbo c on (a1.cbo = c.id) " +
				" left join hosp.pacientes p on (a.codpaciente = p.id_paciente)   " +
				" left join hosp.proc pr on (a1.codprocedimento = pr.id) where a1.id_atendimento = ? and coalesce(a1.excluido,'N')='N' " +
				" order by a1.id_atendimentos1";

		ArrayList<AtendimentoBean> lista = new ArrayList<AtendimentoBean>();

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, idAtendimento);

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				AtendimentoBean atendimento = new AtendimentoBean();
				atendimento.setDataAtendimento(rs.getDate("dtaatende"));
				atendimento.setId(rs.getInt("id_atendimento"));
				atendimento.setId1(rs.getInt("id_atendimentos1"));
				atendimento.getFuncionario().setId(rs.getLong("codprofissionalatendimento"));
				atendimento.getFuncionario().setNome(rs.getString("descfuncionario"));
				atendimento.getFuncionario().setCns(rs.getString("cns"));
				atendimento.getCbo().setCodCbo(rs.getInt("codcbo"));
				atendimento.getCbo().setDescCbo(rs.getString("descricao"));
				atendimento.getCbo().setCodigo(rs.getString("codigocbo"));

				atendimento.getSituacaoAtendimento().setId(rs.getInt("id_situacao_atendimento"));
				atendimento.getSituacaoAtendimento().setDescricao(rs.getString("situacao_descricao"));
				atendimento.getSituacaoAtendimento().setAtendimentoRealizado(rs.getBoolean("atendimento_realizado"));
				atendimento.getSituacaoAtendimentoAnterior().setId(rs.getInt("id_situacao_atendimento"));
				atendimento.getSituacaoAtendimentoAnterior().setDescricao(rs.getString("situacao_descricao"));
				atendimento.getSituacaoAtendimentoAnterior().setAtendimentoRealizado(rs.getBoolean("atendimento_realizado"));

				atendimento.getProcedimento().setCodProc(rs.getString("codproc"));
				atendimento.getProcedimento().setNomeProc(rs.getString("procedimento"));
				atendimento.getProcedimento().setIdProc(rs.getInt("id"));
				atendimento.setEvolucao(rs.getString("evolucao"));
				atendimento.setPerfil(rs.getString("perfil_avaliacao"));
				if (!VerificadorUtil.verificarSeObjetoNulo(rs.getString("horario_atendimento")))
					atendimento.setHorarioAtendimento(rs.getString("horario_atendimento"));
				atendimento.getPrograma().setIdPrograma(rs.getInt("codprograma"));
				atendimento.getGrupo().setIdGrupo(rs.getInt("codgrupo"));

				atendimento.getPaciente().setId_paciente(rs.getInt("id_paciente"));
				atendimento.getPaciente().setDtanascimento(rs.getDate("dtanascimento"));
				atendimento.getPaciente().setSexo(rs.getString("sexo"));
				atendimento.getCidPrimario().setIdCid(rs.getInt("id_cidprimario"));
				lista.add(atendimento);
			}

		} catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				//comentado walter erro log ex.printStackTrace();
			}
		}
		return lista;
	}

	public List<AtendimentoBean> carregarEvolucoesDoPaciente(Integer codPaciente) throws ProjetoException {

		String sql = "SELECT a1.evolucao, a.dtaatende, f.descfuncionario, p.nome FROM hosp.atendimentos1 a1 "
				+ "LEFT JOIN hosp.atendimentos a ON (a.id_atendimento = a1.id_atendimento) "
				+ "LEFT JOIN hosp.proc p ON (p.id = a1.codprocedimento) "
				+ "LEFT JOIN acl.funcionarios f ON (f.id_funcionario = a1.codprofissionalatendimento) "
				+ "WHERE a1.evolucao IS NOT NULL AND a.codpaciente = ?    "
				+ "and coalesce(a.situacao, 'A')<> 'C'	and coalesce(a1.excluido, 'N' )= 'N' and p.ativo = 'S' "
				+ "ORDER BY a.dtaatende DESC ";

		ArrayList<AtendimentoBean> lista = new ArrayList<AtendimentoBean>();

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, codPaciente);
			//	stm.setLong(2, user_session.getId());

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				populaResultSetEvolucaoPacienteEquipeOuProfissional(lista, rs);
			}

		} catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				//comentado walter erro log ex.printStackTrace();
			}
		}
		return lista;
	}

	public List<AtendimentoBean> carregarEvolucoesDoPacientePorEquipe(Integer idAtendimento) throws ProjetoException {

		String sql = "SELECT a1.evolucao, a.dtaatende, f.descfuncionario, pr.nome FROM hosp.atendimentos1 a1 " +
				" LEFT JOIN hosp.atendimentos a ON (a.id_atendimento = a1.id_atendimento) " +
				" left join hosp.situacao_atendimento sa on sa.id = a1.id_situacao_atendimento " +
				" left join acl.funcionarios f on (f.id_funcionario = a1.codprofissionalatendimento) " +
				" left join hosp.cbo c on (a1.cbo = c.id) " +
				" left join hosp.proc pr on (a1.codprocedimento = pr.id)" +
				" where a1.evolucao IS NOT NULL AND a1.id_atendimento = ? and coalesce(a1.excluido,'N')='N' and pr.ativo = 'S' " +
				" order by a.dtaatende ";

		ArrayList<AtendimentoBean> lista = new ArrayList<AtendimentoBean>();

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, idAtendimento);

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				populaResultSetEvolucaoPacienteEquipeOuProfissional(lista, rs);
			}

		} catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				//comentado walter erro log ex.printStackTrace();
			}
		}
		return lista;
	}

	private void populaResultSetEvolucaoPacienteEquipeOuProfissional(ArrayList<AtendimentoBean> lista, ResultSet rs) throws SQLException {
		AtendimentoBean atendimento = new AtendimentoBean();
		atendimento.getProcedimento().setNomeProc(rs.getString("nome"));
		atendimento.getFuncionario().setNome(rs.getString("descfuncionario"));
		atendimento.setEvolucao(rs.getString("evolucao"));
		atendimento.setDataAtendimentoInicio(rs.getDate("dtaatende"));

		lista.add(atendimento);
	}

	public List<AtendimentoBean> carregarTodasAsEvolucoesDoPaciente(Integer codPaciente, Date periodoInicialEvolucao, Date periodoFinalEvolucao, EspecialidadeBean especialidade) throws ProjetoException {

		String sql = "SELECT a1.evolucao, a.dtaatende, f.descfuncionario,f.cns, p.nome, ta.desctipoatendimento, programa.descprograma,  " +
				"g.descgrupo, sa.descricao  situacaoatendimento, c.codigo codcbo, c.id idcbo, c.descricao desccbo, "+
				"pa.nome as nome_paciente,  con.id id_conselho, con.descricao conselho " +
				"FROM hosp.atendimentos1 a1  " +
				"LEFT JOIN hosp.atendimentos a ON (a.id_atendimento = a1.id_atendimento)  " +
				" left join hosp.tipoatendimento ta on ta.id = a.codtipoatendimento  " +
				" left  join hosp.situacao_atendimento sa on sa.id  = a1.id_situacao_atendimento  " +
				" left join hosp.pacientes pa on a.codpaciente = pa.id_paciente  " +
				" left join hosp.programa  on programa.id_programa = a.codprograma  " +
				" left join hosp.grupo g on g.id_grupo = a.codgrupo  " +
				"LEFT JOIN hosp.proc p ON (p.id = a1.codprocedimento)  " +
				"LEFT JOIN acl.funcionarios f ON (f.id_funcionario = a1.codprofissionalatendimento)  " +
				" left join hosp.cbo c on c.id  = a1.cbo  " +
				" left join hosp.cbo_conselho cc on a1.cbo = cc.id_cbo  " +
				" left join hosp.conselho con on cc.id_conselho = con.id "+
				"WHERE a1.evolucao IS NOT NULL and coalesce(a.situacao,'')<>'C' and coalesce(a1.excluido,'N')='N' "+
				"and p.ativo = 'S' and a.cod_unidade=?";
		if (!VerificadorUtil.verificarSeObjetoNuloOuZero(codPaciente))
			sql += "AND a.codpaciente = ? ";

		if (!VerificadorUtil.verificarSeObjetoNulo(periodoInicialEvolucao))
			sql += " AND a.dtaatende >= ? AND a.dtaatende <= ? ";

		if ((!VerificadorUtil.verificarSeObjetoNuloOuZero(especialidade)) && (!VerificadorUtil.verificarSeObjetoNuloOuZero(especialidade.getCodEspecialidade())))
			sql += "AND f.codespecialidade = ? ";

		sql += "ORDER BY pa.nome, a.dtaatende ";

		ArrayList<AtendimentoBean> lista = new ArrayList<AtendimentoBean>();

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			int i = 2;

			stm.setInt(1, user_session.getUnidade().getId());

			if (!VerificadorUtil.verificarSeObjetoNuloOuZero(codPaciente)) {
				stm.setInt(i, codPaciente);
				i++;
			}

			if (!VerificadorUtil.verificarSeObjetoNulo(periodoInicialEvolucao)) {
				stm.setDate(i, new java.sql.Date(periodoInicialEvolucao.getTime()));
				i++;
				stm.setDate(i, new java.sql.Date(periodoFinalEvolucao.getTime()));
				i++;
			}

			if ((!VerificadorUtil.verificarSeObjetoNuloOuZero(especialidade)) && (!VerificadorUtil.verificarSeObjetoNuloOuZero(especialidade.getCodEspecialidade()))){
				stm.setInt(i, especialidade.getCodEspecialidade());
				i++;
			}

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				AtendimentoBean atendimento = new AtendimentoBean();
				atendimento.getProcedimento().setNomeProc(rs.getString("nome"));
				atendimento.getFuncionario().setNome(rs.getString("descfuncionario"));
				atendimento.getFuncionario().setCns(rs.getString("cns"));
				atendimento.getCbo().setCodCbo(rs.getInt("idcbo"));
				atendimento.getCbo().setCodigo(rs.getString("codcbo"));
				atendimento.getCbo().setDescCbo(rs.getString("desccbo"));
				atendimento.getCbo().getConselho().setId(rs.getInt("id_conselho"));
				atendimento.getCbo().getConselho().setDescricao(rs.getString("conselho"));
				atendimento.setEvolucao(rs.getString("evolucao"));
				atendimento.setDataAtendimentoInicio(rs.getDate("dtaatende"));
				atendimento.getTipoAt().setDescTipoAt(rs.getString("desctipoatendimento"));
				atendimento.getPrograma().setDescPrograma(rs.getString("descprograma"));
				atendimento.getGrupo().setDescGrupo(rs.getString("descgrupo"));
				atendimento.getSituacaoAtendimento().setDescricao(rs.getString("situacaoatendimento"));
				atendimento.getPaciente().setNome(rs.getString("nome_paciente"));

				lista.add(atendimento);
			}

		} catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				//comentado walter erro log ex.printStackTrace();
			}
		}
		return lista;
	}

	public boolean verificaSeExisteAtendimentoparaProfissionalNaDataNaEquipe(AtendimentoBean atendimento) throws ProjetoException {

		String sql = "select a.id_atendimento from hosp.atendimentos a  " +
				"join hosp.atendimentos1 a1 on a1.id_atendimento = a.id_atendimento " +
				"where dtaatende=? " +
				"and a.codprograma=? and a.codgrupo=? and a.codequipe=? " +
				"and a1.codprofissionalatendimento=? and coalesce(a.situacao,'A')<>'C' " +
				" and coalesce(a1.excluido,'N' )='N'" +
				"";
		boolean rst = false;
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setDate(1,new java.sql.Date( atendimento.getDataAtendimentoInicio().getTime()));
			stm.setInt(2,  atendimento.getPrograma().getIdPrograma());
			stm.setInt(3,  atendimento.getGrupo().getIdGrupo());
			stm.setInt(4,  atendimento.getEquipe().getCodEquipe());
			stm.setLong(5,  atendimento.getFuncionario().getId());
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				rst = true;
			}
		} catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				//comentado walter erro log ex.printStackTrace();
			}
		}
		return rst;
	}

	public boolean alteraSituacaoDeAtendimentoPorProfissional(Integer idSituacaoAtendimento, AtendimentoBean atendimento) throws ProjetoException {

		String sql = "update hosp.atendimentos1 set id_situacao_atendimento = ? where id_atendimento = ?";
		boolean alterado = false;
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			if(VerificadorUtil.verificarSeObjetoNuloOuMenorQueZero(idSituacaoAtendimento))
				stm.setNull(1, Types.NULL);
			else
				stm.setInt(1,  idSituacaoAtendimento);
			stm.setInt(2,  atendimento.getId());

			stm.executeUpdate();

			gravarValidacaoSigtapAnterior(con, atendimento.getId(),atendimento.isValidadoPeloSigtapAnterior());
			if(user_session.getUnidade().getParametro().isVerificaPeriodoInicialEvolucaoPrograma()) {
				atendimento.getSituacaoAtendimento().setId(idSituacaoAtendimento);
				verificarInconsistenciaEvolucaoProgramaGrupo(atendimento, con);
			}

			con.commit();
			alterado = true;
		} catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				//comentado walter erro log ex.printStackTrace();
			}
		}
		return alterado;
	}

	public boolean cancelarEvolucaoAtendimentoPorProfissional(AtendimentoBean atendimento, FuncionarioBean usuarioLiberacao) throws ProjetoException {

		String sql = "update hosp.atendimentos1 set evolucao = null, id_situacao_atendimento=null where id_atendimentos1 = ?";
		boolean alterado = false;
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1,  atendimento.getId1());
			stm.executeUpdate();
			removerProcedimentosSecundariosEvolucao(con, atendimento.getId1());
			gravarLiberacaoCancelamentoEvolucao(con, atendimento.getId(), atendimento.getId1(), usuarioLiberacao);
			con.commit();
			alterado = true;
		} catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				//comentado walter erro log ex.printStackTrace();
			}
		}
		return alterado;
	}

	private void removerProcedimentosSecundariosEvolucao(Connection conexao, Integer idAtendimento1)
			throws SQLException, ProjetoException {

		String sql = "DELETE FROM hosp.atendimentos1_procedimento_secundario WHERE id_atendimentos1 = ?; ";
		try {
			PreparedStatement stm = conexao.prepareStatement(sql);
			stm.setInt(1, idAtendimento1);
			stm.executeUpdate();
		} catch (SQLException ex2) {
			conexao.rollback();
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			conexao.rollback();
			throw new ProjetoException(ex, this.getClass().getName());
		}
	}

	private void gravarLiberacaoCancelamentoEvolucao
			(Connection conexao, Integer idAtendimento, Integer idAtendimentos1, FuncionarioBean usuarioLiberacao) throws SQLException, ProjetoException {
		String sql = "INSERT INTO hosp.liberacoes " +
				"(motivo, usuario_liberacao, data_hora_liberacao, codatendimento, cod_unidade, id_atendimentos1) " +
				"VALUES(?, ?, CURRENT_TIMESTAMP, ?, ?, ?); ";
		try {
			PreparedStatement stm = conexao.prepareStatement(sql);
			stm.setString(1, MotivoLiberacao.CANCELAR_EVOLUCAO.getTitulo());
			stm.setLong(2,  usuarioLiberacao.getId());
			stm.setInt(3, idAtendimento);
			stm.setInt(4, usuarioLiberacao.getUnidade().getId());
			stm.setInt(5, idAtendimentos1);
			stm.executeUpdate();
		} catch (SQLException ex2) {
			conexao.rollback();
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			conexao.rollback();
			throw new ProjetoException(ex, this.getClass().getName());
		}
	}

	private void gravarValidacaoSigtapAnterior(Connection conexao, Integer idAtendimento, boolean validadoPeloSigtapAnterior) throws SQLException, ProjetoException {

		String sql = "update hosp.atendimentos set validado_pelo_sigtap_anterior = ? where id_atendimento = ?";
		try {
			PreparedStatement stm = conexao.prepareStatement(sql);
			stm.setBoolean(1, validadoPeloSigtapAnterior);
			stm.setInt(2, idAtendimento);
			stm.executeUpdate();
		} catch (SQLException ex2) {
			conexao.rollback();
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			conexao.rollback();
			throw new ProjetoException(ex, this.getClass().getName());
		}
	}

	public List<AtendimentoBean> listaAtendimentos1FiltroAjustes(AtendimentoBean atendimentoBusca, boolean apenasSemCids,
																 String campoBusca, String tipoBusca) throws ProjetoException {

		List<AtendimentoBean> listaAtendimentos = new ArrayList<>();

		String sql = "select a.id_atendimento, a1.id_atendimentos1, a.validado_pelo_sigtap_anterior, "+
				"f.descfuncionario, f.id_funcionario, pa.nome as paciente, pa.id_paciente, pa.dtanascimento, p.id as id_procedimento, " +
				"p.nome as procedimento, a.dtaatende, c.cod as id_cidprimario, c.desccidabrev, c.cid, p.codproc, "+
				"a.codprograma, pro.descprograma, a.codgrupo, g.descgrupo, cbo.id idcbo, cbo.descricao, cbo.codigo codcbo " +
				"from hosp.atendimentos1 a1 " +
				"join hosp.atendimentos a on a1.id_atendimento = a.id_atendimento " +
				"join acl.funcionarios f on a1.codprofissionalatendimento = f.id_funcionario " +
				" left join hosp.cbo  on cbo.id = a1.cbo "+
				"join hosp.proc p on a1.codprocedimento = p.id " +
				"join hosp.pacientes pa on a.codpaciente = pa.id_paciente " +
				"left join hosp.cid c on a1.id_cidprimario = c.cod " +
				"left join hosp.programa pro on a.codprograma = pro.id_programa " +
				"left join hosp.grupo g on a.codgrupo = g.id_grupo "+
				"where a.dtaatende between ? and ? and p.ativo = 'S' and coalesce(a.situacao,'')<>'C' and coalesce(a1.excluido,'N')='N'  ";

		if ((tipoBusca.equals("paciente") && (!VerificadorUtil.verificarSeObjetoNuloOuVazio(campoBusca))))
			sql += " and pa.nome ilike ?";

		else if ((tipoBusca.equals("codproc") && (!VerificadorUtil.verificarSeObjetoNuloOuVazio(campoBusca))))
			sql += " and p.codproc = ?";

		else if ((tipoBusca.equals("matpaciente") && (!VerificadorUtil.verificarSeObjetoNuloOuVazio(campoBusca))))
			sql += " and upper(pa.matricula) = ?";

		else if ((tipoBusca.equals("prontpaciente") && (!VerificadorUtil.verificarSeObjetoNuloOuVazio(campoBusca))))
			sql += " and pa.id_paciente = ?";

		if (!VerificadorUtil.verificarSeObjetoNulo(atendimentoBusca.getPrograma())
				&& !VerificadorUtil.verificarSeObjetoNuloOuZero(atendimentoBusca.getPrograma().getIdPrograma())) {
			sql += " and a.codprograma = ? ";
		}
		if (!VerificadorUtil.verificarSeObjetoNulo(atendimentoBusca.getGrupo())
				&& !VerificadorUtil.verificarSeObjetoNuloOuZero(atendimentoBusca.getGrupo().getIdGrupo())) {
			sql += " and a.codgrupo = ? ";
		}

		String filtroSql = "and a1.id_cidprimario is null ";
		String ordenacaoSql = "order by a.dtaatende, pa.nome; ";

		if(!apenasSemCids)
			sql += ordenacaoSql;
		else
			sql += filtroSql + ordenacaoSql;


		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setDate(1, new java.sql.Date(atendimentoBusca.getDataAtendimentoInicio().getTime()));
			stm.setDate(2,  new java.sql.Date(atendimentoBusca.getDataAtendimentoFinal().getTime()));
			if ((tipoBusca.equals("paciente") && (!VerificadorUtil.verificarSeObjetoNuloOuVazio(campoBusca))))
				stm.setString(3, "%" + campoBusca.toUpperCase() + "%");

			else if (((tipoBusca.equals("codproc") || (tipoBusca.equals("matpaciente"))) && (!VerificadorUtil.verificarSeObjetoNuloOuVazio(campoBusca))))
				stm.setString(3, campoBusca.toUpperCase());

			else if ((tipoBusca.equals("prontpaciente") && (!VerificadorUtil.verificarSeObjetoNuloOuVazio(campoBusca)))) {
				campoBusca = campoBusca.replaceAll("[^\\d.]", "");
				if(campoBusca.isEmpty())
					campoBusca = "0";
				stm.setInt(3,Integer.valueOf((campoBusca)));
			}

			int contador = 4;
			if (!VerificadorUtil.verificarSeObjetoNulo(atendimentoBusca.getPrograma())
					&& !VerificadorUtil.verificarSeObjetoNuloOuZero(atendimentoBusca.getPrograma().getIdPrograma())) {
				stm.setInt(contador, atendimentoBusca.getPrograma().getIdPrograma());
				contador++;
			}
			if (!VerificadorUtil.verificarSeObjetoNulo(atendimentoBusca.getGrupo())
					&& !VerificadorUtil.verificarSeObjetoNuloOuZero(atendimentoBusca.getGrupo().getIdGrupo())) {
				stm.setInt(contador, atendimentoBusca.getGrupo().getIdGrupo());
			}
			ResultSet rs = stm.executeQuery();
			while(rs.next()) {
				AtendimentoBean atendimento = new AtendimentoBean();

				atendimento.setId(rs.getInt("id_atendimento"));
				atendimento.setId1(rs.getInt("id_atendimentos1"));
				atendimento.setValidadoPeloSigtapAnterior(rs.getBoolean("validado_pelo_sigtap_anterior"));
				atendimento.getFuncionario().setNome(rs.getString("descfuncionario"));
				atendimento.getFuncionario().setId(rs.getLong("id_funcionario"));
				atendimento.getCbo().setCodCbo(rs.getInt("idcbo"));
				atendimento.getCbo().setDescCbo(rs.getString("descricao"));
				atendimento.getCbo().setCodigo(rs.getString("codcbo"));
				atendimento.getPaciente().setNome(rs.getString("paciente"));
				atendimento.getPaciente().setId_paciente(rs.getInt("id_paciente"));
				atendimento.getPaciente().setDtanascimento(rs.getDate("dtanascimento"));
				atendimento.getProcedimento().setIdProc(rs.getInt("id_procedimento"));
				atendimento.getProcedimento().setNomeProc(rs.getString("procedimento"));
				atendimento.getProcedimento().setCodProc(rs.getString("codproc"));
				atendimento.setDataAtendimento(rs.getDate("dtaatende"));
				atendimento.getCidPrimario().setIdCid(rs.getInt("id_cidprimario"));
				atendimento.getCidPrimario().setDescCidAbrev(rs.getString("desccidabrev"));
				atendimento.getCidPrimario().setCid(rs.getString("cid"));
				atendimento.getPrograma().setIdPrograma(rs.getInt("codprograma"));
				atendimento.getPrograma().setDescPrograma(rs.getString("descprograma"));
				atendimento.getGrupo().setIdGrupo(rs.getInt("codgrupo"));
				atendimento.getGrupo().setDescGrupo(rs.getString("descgrupo"));

				listaAtendimentos.add(atendimento);
			}

		}catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				//comentado walter erro log ex.printStackTrace();
			}
		}
		return listaAtendimentos;
	}


	public boolean atualizaCidDeAtendimento(AtendimentoBean atendimento, CidBean cid) throws ProjetoException {

		boolean alterou = false;

		String sql = "UPDATE hosp.atendimentos1 SET id_cidprimario = ? WHERE id_atendimentos1 = ?;";

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);

			stm.setInt(1, cid.getIdCid());
			stm.setInt(2, atendimento.getId1());
			stm.executeUpdate();
			gravarValidacaoSigtapAnterior(con, atendimento.getId(), atendimento.isValidadoPeloSigtapAnterior());

			gerarLogAlteracaoCid(atendimento, cid, con);

			alterou = true;
			con.commit();
		} catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				//comentado walter erro log ex.printStackTrace();
			}
		}
		return alterou;
	}

	private void gerarLogAlteracaoCid(AtendimentoBean atendimento, CidBean cid, Connection conexao) throws ProjetoException, SQLException {
		try {
			String descricao = montarDescricaoLogAlteracaoAtendimento(atendimento);

			LogBean log = new LogBean(user_session.getId(), descricao, Rotina.ALTERACAO_ATENDIMENTO.getSigla());

			if(VerificadorUtil.verificarSeObjetoNuloOuZero(atendimento.getCidPrimario().getIdCid())) {
				log.adicionarDescricao("Cid1", "", cid.getDescCidAbrev());
				log.adicionarDescricao("ID Cid1", "", cid.getIdCid().toString());
			}
			else {
				log.adicionarDescricao("Cid1", atendimento.getCidPrimario().getDescCidAbrev(), cid.getDescCidAbrev());
				log.adicionarDescricao("ID Cid1", atendimento.getCidPrimario().getIdCid().toString(), cid.getIdCid().toString());
			}
			new LogDAO().gravarLog(log, conexao);
		} catch (Exception e) {
			conexao.rollback();
		}
	}

	public boolean atualizaProcedimentoDoAtendimento(AtendimentoBean atendimento, ProcedimentoBean procedimento) throws ProjetoException {

		boolean alterou = false;

		String sql = "UPDATE hosp.atendimentos1 SET codprocedimento = ? WHERE id_atendimentos1 = ?;";

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);

			stm.setInt(1, procedimento.getIdProc());
			stm.setInt(2, atendimento.getId1());
			stm.executeUpdate();
			gravarValidacaoSigtapAnterior(con, atendimento.getId(), atendimento.isValidadoPeloSigtapAnterior());

			gerarLogAlteracaoProcedimento(atendimento, procedimento, con);

			alterou = true;
			con.commit();
		} catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				//comentado walter erro log ex.printStackTrace();
			}
		}
		return alterou;
	}

	private void gerarLogAlteracaoProcedimento(AtendimentoBean atendimento, ProcedimentoBean procedimento, Connection conexao)
			throws ProjetoException, SQLException {
		try {
			String descricao = montarDescricaoLogAlteracaoAtendimento(atendimento);
			LogBean log = new LogBean(user_session.getId(), descricao, Rotina.ALTERACAO_ATENDIMENTO.getSigla());

			if(VerificadorUtil.verificarSeObjetoNuloOuZero(atendimento.getProcedimento().getIdProc())) {
				log.adicionarDescricao("Procedimento", "", procedimento.getNomeProc());
				log.adicionarDescricao("ID Procedimento", "", procedimento.getIdProc().toString());
			}
			else {
				log.adicionarDescricao
						("Procedimento", atendimento.getProcedimento().getNomeProc(), procedimento.getNomeProc());
				log.adicionarDescricao
						("ID Procedimento", atendimento.getProcedimento().getIdProc().toString(), procedimento.getIdProc().toString());
			}
			new LogDAO().gravarLog(log, conexao);
		} catch (Exception e) {
			conexao.rollback();
		}
	}

	public boolean atualizaCboDeAtendimento(AtendimentoBean atendimento, CboBean cbo) throws ProjetoException {

		boolean alterou = false;

		String sql = "UPDATE hosp.atendimentos1 SET cbo = ? WHERE id_atendimentos1 = ?;";

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);

			stm.setInt(1, cbo.getCodCbo());
			stm.setInt(2, atendimento.getId1());
			stm.executeUpdate();
			gravarValidacaoSigtapAnterior(con, atendimento.getId(), atendimento.isValidadoPeloSigtapAnterior());

			gerarLogAlteracaoCbo(atendimento, cbo, con);

			alterou = true;
			con.commit();
		} catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				//comentado walter erro log ex.printStackTrace();
			}
		}
		return alterou;
	}

	private void gerarLogAlteracaoCbo(AtendimentoBean atendimento, CboBean cbo, Connection conexao) throws ProjetoException, SQLException {
		try {
			String descricao = montarDescricaoLogAlteracaoAtendimento(atendimento);
			LogBean log = new LogBean(user_session.getId(), descricao, Rotina.ALTERACAO_ATENDIMENTO.getSigla());

			log.adicionarDescricao("CBO", atendimento.getCbo().getDescCbo(), cbo.getDescCbo());
			log.adicionarDescricao("ID CBO", atendimento.getCbo().getCodCbo().toString(), cbo.getCodCbo().toString());

			new LogDAO().gravarLog(log, conexao);
		} catch (Exception e) {
			conexao.rollback();
		}
	}

	public List<Integer> listaAnosDeAtendimentos() throws ProjetoException {

		String sql = "select distinct extract (year from a.dtaatende) as ano " +
				"	from hosp.atendimentos a " +
				"	where extract (year from a.dtaatende) <= extract (year from current_date) " +
				"	and extract (year from a.dtaatende) >= 2000	order by ano desc; ";

		List<Integer> listaAnos = new ArrayList<>();

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);

			ResultSet rs = stm.executeQuery();
			while(rs.next()) {
				listaAnos.add(rs.getInt("ano"));
			}

		}catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				//comentado walter erro log ex.printStackTrace();
			}
		}
		return listaAnos;
	}

	public Integer retornaTotalAtendimentosOuAgendamentosDeUmPeriodo(Date dataInicio, Date dataFim, String tipoGeracao, List<ProcedimentoBean> listaProcedimentosFiltro, Integer idConfiguracaoProducaoBpa, String competencia) throws ProjetoException {

		Integer totalAtendimentos = null;

		String sqlProcedimentoPrimario = "select count(*) total \n" +
				"from hosp.atendimentos a \n" +
				"inner join hosp.atendimentos1 a1 on (a.id_atendimento = a1.id_atendimento) \n" +
				"left join hosp.paciente_instituicao pi on pi.id  = a.id_paciente_instituicao  \r\n" +
				"left join hosp.laudo l on l.id_laudo  = pi.codlaudo " +
				"left join hosp.situacao_atendimento sa on sa.id = a1.id_situacao_atendimento \n" +
				"inner join hosp.programa p on (a.codprograma = p.id_programa) \n" +
				"inner join hosp.grupo on (a.codgrupo = grupo.id_grupo) \n" +
				"inner join hosp.pacientes pa on (a.codpaciente = pa.id_paciente) \n" +
				"inner join acl.funcionarios f  on (a1.codprofissionalatendimento = f.id_funcionario) \n" +
				"inner join hosp.proc pr on (a1.codprocedimento = pr.id)\t\n" +
				"left join hosp.especialidade es on es.id_especialidade = f.codespecialidade \n" +
				"join sigtap.procedimento_mensal pm on\n" +
				"\tpm.id_procedimento = a1.codprocedimento\n" +
				"join sigtap.historico_consumo_sigtap hc on\n" +
				"\thc.id = pm.id_historico_consumo_sigtap\n" +
				"join sigtap.instrumento_registro_procedimento_mensal irpm on\n" +
				"\tirpm.id_procedimento_mensal = pm.id\n" +
				"join sigtap.instrumento_registro ir on\n" +
				"\tir.id = irpm.id_instrumento_registro\n" +
				"where  coalesce(a.situacao,'')<>'C' and coalesce(a1.excluido,'N')='N' and hc.status ='A' \n" +
				"and a.dtaatende between ? and ? and coalesce(pr.id_instrumento_registro_padrao, ir.id) = ir.id  and ir.nome like '%BPA%'\n" +
				"and pm.competencia_atual = ? ";


		String sqlProcedimentoSecundario = " select count(*) total \n" +
				"\tfrom hosp.atendimentos a  \n" +
				"\tjoin hosp.atendimentos1 a1 on (a.id_atendimento = a1.id_atendimento)  \n" +
				"\tjoin hosp.atendimentos1_procedimento_secundario aps on (a1.id_atendimentos1 = aps.id_atendimentos1) \n" +
				"left join hosp.paciente_instituicao pi on pi.id  = a.id_paciente_instituicao  \r\n" +
				"left join hosp.laudo l on l.id_laudo  = pi.codlaudo " +
				"\tleft join hosp.situacao_atendimento sa on sa.id = a1.id_situacao_atendimento  \n" +
				"\tjoin hosp.programa p on (a.codprograma = p.id_programa)  \n" +
				"\tjoin hosp.grupo on (a.codgrupo = grupo.id_grupo)  \n" +
				"\tjoin hosp.pacientes pa on (a.codpaciente = pa.id_paciente)  \n" +
				"\tjoin acl.funcionarios f  on (a1.codprofissionalatendimento = f.id_funcionario)  \n" +
				"\tjoin hosp.proc pr on (aps.id_procedimento = pr.id)\t \n" +
				"\tleft join hosp.especialidade es on es.id_especialidade = f.codespecialidade \n" +
				"join sigtap.procedimento_mensal pm on\n" +
				"\tpm.id_procedimento = a1.codprocedimento\n" +
				"join sigtap.historico_consumo_sigtap hc on\n" +
				"\thc.id = pm.id_historico_consumo_sigtap\n" +
				"join sigtap.instrumento_registro_procedimento_mensal irpm on\n" +
				"\tirpm.id_procedimento_mensal = pm.id\n" +
				"join sigtap.instrumento_registro ir on\n" +
				"\tir.id = irpm.id_instrumento_registro\t\n" +
				"\twhere     coalesce(a.situacao,'')<>'C' and coalesce(a1.excluido,'N')='N' and hc.status ='A' and  coalesce(aps.excluido,'N') ='N' \n" +
				"\tand a.dtaatende between ? and ?   and ir.nome like '%BPA%' and pm.competencia_atual = ?";

		String filtroProcedimento = " and a1.codprocedimento = any(?) ";
		String filtroAtendimentoRealizado = " and sa.atendimento_realizado = true";
		String filtroPresencaAtendimentoRealizado = " and a.presenca='S' and ((sa.atendimento_realizado is true) or (a1.id_situacao_atendimento is null)) ";

		String filtroConfiguracaoProducao = " and a.cod_unidade in( select cpbu.id_unidade from hosp.configuracao_producao_bpa cpb \n" +
				"join hosp.configuracao_producao_bpa_unidade cpbu on  cpb.id  = cpbu.id_configuracao_producao_bpa\n" +
				"where cpb .id =?)";

		String filtroLaudoAutorizado = " and l.situacao = 'A' and ((l.data_solicitacao between ? and ?) \r\n" +
				"	or hosp.fn_GetLastDayOfMonth(to_date(l.ano_final||'-'||'0'||''||l.mes_final||'-'||'01', 'YYYY-MM-DD'))  between ? and ?) ";

		if (listaProcedimentosFiltro.size()>0) {
			sqlProcedimentoPrimario += filtroProcedimento;
			sqlProcedimentoSecundario += filtroProcedimento;
		}

		if (tipoGeracao.equals("A")){
			sqlProcedimentoPrimario += filtroAtendimentoRealizado;
			sqlProcedimentoSecundario+= filtroAtendimentoRealizado;
		}
		else {
			sqlProcedimentoPrimario += filtroPresencaAtendimentoRealizado;
			sqlProcedimentoSecundario += filtroPresencaAtendimentoRealizado;
		}

		if (idConfiguracaoProducaoBpa!=null){
			sqlProcedimentoPrimario += filtroConfiguracaoProducao;
			sqlProcedimentoSecundario +=  filtroConfiguracaoProducao;
		}

		if(user_session.getUnidade().getParametro().isBpaComLaudoAutorizado()) {
			sqlProcedimentoPrimario += filtroLaudoAutorizado;
			sqlProcedimentoSecundario += filtroLaudoAutorizado;
		}

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement ps = con.prepareStatement(sqlProcedimentoPrimario);
			totalAtendimentos = mapearPreparedStatmentTotal(dataInicio, dataFim, listaProcedimentosFiltro,
					totalAtendimentos, competencia, idConfiguracaoProducaoBpa,  ps);

			ps = con.prepareStatement(sqlProcedimentoSecundario);
			totalAtendimentos += mapearPreparedStatmentTotal(dataInicio, dataFim, listaProcedimentosFiltro,
					totalAtendimentos, competencia, idConfiguracaoProducaoBpa, ps);

		}catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				//comentado walter erro log ex.printStackTrace();
			}
		}
		return totalAtendimentos;
	}

	private Integer mapearPreparedStatmentTotal(Date dataInicio, Date dataFim,
												List<ProcedimentoBean> listaProcedimentosFiltro, Integer totalAtendimentos, String competencia, Integer codConfiguracaoProducao, PreparedStatement ps)
			throws SQLException {
		ps.setDate(1, new java.sql.Date(dataInicio.getTime()));
		ps.setDate(2, new java.sql.Date(dataFim.getTime()));
		ps.setString(3, competencia);
		int j = 4;
		ArrayList<Integer> lista = new ArrayList<>();
		for (int i = 0; i < listaProcedimentosFiltro.size(); i++) {
			lista.add(listaProcedimentosFiltro.get(i).getIdProc());
		}

		if (listaProcedimentosFiltro.size()>0) {
			ps.setObject(j, ps.getConnection().createArrayOf("INTEGER", lista.toArray()));
			j++;
		}
		if (codConfiguracaoProducao!=null){
			ps.setInt(j, codConfiguracaoProducao);
			j++;
		}
		if(user_session.getUnidade().getParametro().isBpaComLaudoAutorizado()) {
			ps.setDate(j, new java.sql.Date(dataInicio.getTime()));
			j++;
			ps.setDate(j, new java.sql.Date(dataFim.getTime()));
			j++;
			ps.setDate(j, new java.sql.Date(dataInicio.getTime()));
			j++;
			ps.setDate(j, new java.sql.Date(dataFim.getTime()));
		}

		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			totalAtendimentos = rs.getInt("total");
		}
		return totalAtendimentos;
	}

	public List<Integer> gravarTabelaInconsistenciasAtendimento
			(Integer idAtendimento1, String descricao, Connection conexaoAuxiliar) throws ProjetoException, SQLException {

		String sql = "INSERT INTO hosp.inconsistencias_log (id_funcionario, id_atendimento1, datahora, descricao) "+
				" VALUES(?, ?, CURRENT_TIMESTAMP, ?); ";

		List<Integer> listaAnos = new ArrayList<>();

		try {
			PreparedStatement ps = conexaoAuxiliar.prepareStatement(sql);
			ps.setLong(1, user_session.getId());
			ps.setInt(2, idAtendimento1);
			ps.setString(3, descricao);
			ps.executeUpdate();

		} catch (SQLException ex2) {
			conexaoAuxiliar.rollback();
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			conexaoAuxiliar.rollback();
			throw new ProjetoException(ex, this.getClass().getName());
		}
		return listaAnos;
	}

	public boolean atualizaCidDeVariosAtendimento(List<AtendimentoBean> listaAtendimento, CidBean cid)
			throws ProjetoException {

		boolean alterou = false;

		String sql = "UPDATE hosp.atendimentos1 SET id_cidprimario = ? WHERE id_atendimentos1 = ?;";

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);

			for (AtendimentoBean atendimento : listaAtendimento) {
				stm.setInt(1, cid.getIdCid());
				stm.setInt(2, atendimento.getId1());
				stm.executeUpdate();
				gravarValidacaoSigtapAnterior(con, atendimento.getId(), atendimento.isValidadoPeloSigtapAnterior());

				gerarLogAlteracaoCid(atendimento, cid, con);
			}
			alterou = true;
			con.commit();
		} catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				//comentado walter erro log ex.printStackTrace();
			}
		}
		return alterou;
	}

	private String montarDescricaoLogAlteracaoAtendimento(AtendimentoBean atendimento) {
		String descricao = "Paciente: "+ atendimento.getPaciente().getNome()+
				" ID Paciente: "+atendimento.getPaciente().getId_paciente() +
				" ID Atendimento1: "+ atendimento.getId1()+" \n";
		return descricao;
	}

	public boolean atualizaProcedimentoVariosAtendimento
			(List<AtendimentoBean> listaAtendimento, ProcedimentoBean procedimento) throws ProjetoException {

		boolean alterou = false;

		String sql = "UPDATE hosp.atendimentos1 SET codprocedimento = ? WHERE id_atendimentos1 = ?;";

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);

			for (AtendimentoBean atendimento : listaAtendimento) {
				stm.setInt(1, procedimento.getIdProc());
				stm.setInt(2, atendimento.getId1());
				stm.executeUpdate();
				gravarValidacaoSigtapAnterior(con, atendimento.getId(), atendimento.isValidadoPeloSigtapAnterior());

				gerarLogAlteracaoProcedimento(atendimento, procedimento, con);
			}
			alterou = true;
			con.commit();
		} catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				//comentado walter erro log ex.printStackTrace();
			}
		}
		return alterou;
	}

	public boolean atualizaCboVariosAtendimentos(List<AtendimentoBean> listaAtendimento, CboBean cbo)
			throws ProjetoException {

		boolean alterou = false;

		String sql = "UPDATE hosp.atendimentos1 SET cbo = ? WHERE id_atendimentos1 = ?;";

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);

			for (AtendimentoBean atendimento : listaAtendimento) {
				stm.setInt(1, cbo.getCodCbo());
				stm.setInt(2, atendimento.getId1());
				stm.executeUpdate();
				gravarValidacaoSigtapAnterior(con, atendimento.getId(), atendimento.isValidadoPeloSigtapAnterior());

				gerarLogAlteracaoCbo(atendimento, cbo, con);
			}
			alterou = true;
			con.commit();
		} catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				//comentado walter erro log ex.printStackTrace();
			}
		}
		return alterou;
	}

	public CboBean retornaCboProfissionalNoAtendimento(Integer idAtendimento, Long idFuncionario) throws ProjetoException {

		String sql = "select a1.cbo from hosp.atendimentos1 a1 " +
				"join hosp.atendimentos a on a1.id_atendimento = a.id_atendimento " +
				"where a.id_atendimento = ? and a1.codprofissionalatendimento = ?;";

		CboBean cbo = new CboBean();

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);

			ps.setInt(1, idAtendimento);
			ps.setLong(2, idFuncionario);
			ResultSet rs = ps.executeQuery();

			if(rs.next()) {
				cbo.setCodCbo(rs.getInt("cbo"));
			}

		} catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				//comentado walter erro log ex.printStackTrace();
			}
		}
		return cbo;
	}

	public boolean atendimentoPossuiSituacaoAtendimento(Integer idAtendimento1) throws ProjetoException {

		String sql = "select exists (select a1.id_atendimentos1 from hosp.atendimentos1 a1 " +
				"	where a1.id_atendimentos1 = ? and a1.id_situacao_atendimento is not null) possui_situacao_atendimento";

		boolean possuiSituacaoAtendimento = true;

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);

			ps.setInt(1, idAtendimento1);
			ResultSet rs = ps.executeQuery();

			if(rs.next()) {
				possuiSituacaoAtendimento = rs.getBoolean("possui_situacao_atendimento");
			}

		} catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				//comentado walter erro log ex.printStackTrace();
			}
		}
		return possuiSituacaoAtendimento;
	}

	public boolean verificaEvolucaoAtendimentoEhPermitida(AtendimentoBean atendimento) throws ProjetoException {

		String sql = "select exists ( select conf.id from hosp.config_evolucao_unidade_programa_grupo conf " +
				"	where ? >= conf.inicio_evolucao " +
				"	and conf.codgrupo = ? and conf.codprograma = ? and conf.codunidade = ? ) ehPermitido";

		boolean ehPermitido = false;

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);

			ps.setDate(1, new java.sql.Date(atendimento.getDataAtendimento().getTime()));
			ps.setInt(2, atendimento.getGrupo().getIdGrupo());
			ps.setInt(3, atendimento.getPrograma().getIdPrograma());
			ps.setInt(4, user_session.getUnidade().getId());
			ResultSet rs = ps.executeQuery();

			if(rs.next()) {
				ehPermitido = rs.getBoolean("ehPermitido");
			}

		} catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				//comentado walter erro log ex.printStackTrace();
			}
		}
		return ehPermitido;
	}

	public List<String> retornaCodigoProcedimentoDeAtendimentosOuAgendamentosDeUmPeriodo(Date dataInicio, Date dataFim,
																						 String tipoGeracao, List<ProcedimentoBean> listaProcedimentosFiltro, List<Integer> idUnidades, String competencia)
			throws ProjetoException {

		List<String> listaCodigosProcedimentos = new ArrayList<>();

		String sql = "select distinct pr.codproc  \n" +
				"from hosp.atendimentos a  \n" +
				"inner join hosp.atendimentos1 a1 on (a.id_atendimento = a1.id_atendimento)  \n" +
				"left join hosp.situacao_atendimento sa on sa.id = a1.id_situacao_atendimento  \n" +
				"inner join hosp.programa p on (a.codprograma = p.id_programa)  \n" +
				"inner join hosp.grupo on (a.codgrupo = grupo.id_grupo)  \n" +
				"inner join hosp.pacientes pa on (a.codpaciente = pa.id_paciente) \n" +
				"inner join acl.funcionarios f  on (a1.codprofissionalatendimento = f.id_funcionario) \n" +
				"inner join hosp.proc pr on (a1.codprocedimento = pr.id) \n" +
				"left join hosp.especialidade es on es.id_especialidade = f.codespecialidade\n" +
				"join sigtap.procedimento_mensal pm on\n" +
				"\tpm.id_procedimento = a1.codprocedimento\n" +
				"join sigtap.historico_consumo_sigtap hc on\n" +
				"\thc.id = pm.id_historico_consumo_sigtap\n" +
				"join sigtap.instrumento_registro_procedimento_mensal irpm on\n" +
				"\tirpm.id_procedimento_mensal = pm.id\n" +
				"join sigtap.instrumento_registro ir on\n" +
				"\tir.id = irpm.id_instrumento_registro\n" +
				"where coalesce(a.situacao,'')<>'C' and coalesce(a1.excluido,'N')='N' \n" +
				"and a.dtaatende between ? and ? \n" +
				"and pm.competencia_atual = ?  and ir.nome like '%BPA%'";

		if (listaProcedimentosFiltro.size() > 0)
			sql += " and a1.codprocedimento = any(?) ";

		if (!idUnidades.isEmpty())
			sql += " and a.cod_unidade = any(?) ";

		if (tipoGeracao.equals("A")) {
			sql += " and sa.atendimento_realizado = true";
		} else
			sql += " and a.presenca='S' and ((sa.atendimento_realizado is true) or (a1.id_situacao_atendimento is null)) ";

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setDate(1, new java.sql.Date(dataInicio.getTime()));
			ps.setDate(2, new java.sql.Date(dataFim.getTime()));
			ps.setString(3, competencia);

			ArrayList<Integer> lista = new ArrayList<>();
			for (int i = 0; i < listaProcedimentosFiltro.size(); i++) {
				lista.add(listaProcedimentosFiltro.get(i).getIdProc());
			}

			int parametro = 4;
			if (listaProcedimentosFiltro.size() > 0) {
				ps.setObject(parametro, ps.getConnection().createArrayOf("INTEGER", lista.toArray()));
				parametro++;
			}

			if (!idUnidades.isEmpty())
				ps.setObject(parametro, ps.getConnection().createArrayOf("INTEGER", idUnidades.toArray()));

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				listaCodigosProcedimentos.add(rs.getString("codproc"));
			}

		} catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				//comentado walter erro log ex.printStackTrace();
			}
		}
		return listaCodigosProcedimentos;
	}
}
