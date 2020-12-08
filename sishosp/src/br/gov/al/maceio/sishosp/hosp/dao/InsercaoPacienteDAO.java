package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import br.gov.al.maceio.sishosp.acl.dao.FuncionarioDAO;
import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.DataUtil;
import br.gov.al.maceio.sishosp.comum.util.TratamentoErrosUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.enums.OpcaoAtendimento;
import br.gov.al.maceio.sishosp.hosp.enums.TipoGravacaoHistoricoPaciente;
import br.gov.al.maceio.sishosp.hosp.model.*;
import br.gov.al.maceio.sishosp.hosp.model.dto.AvaliacaoInsercaoDTO;
import br.gov.al.maceio.sishosp.hosp.model.dto.ProcedimentoCidDTO;

import javax.faces.context.FacesContext;

public class InsercaoPacienteDAO {
	
	private static final String SQL_INSERCAO_ATENDIMENTO = "INSERT INTO hosp.atendimentos(codpaciente, codequipe, situacao, dtaatende, codtipoatendimento, turno, "
			+ " observacao, ativo, id_paciente_instituicao, cod_unidade, horario, encaixe, codatendente, dtamarcacao, codprograma, codgrupo)"
			+ " VALUES (?, ?, 'A', ?, ?, ?, ?, 'S', ?, ?, ?, ?, ?, current_timestamp, ?, ?) RETURNING id_atendimento;";
	
	Connection con = null;
	PreparedStatement ps = null;
	
	FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
			.getSessionMap().get("obj_funcionario");
	
	public ArrayList<InsercaoPacienteBean> listarLaudosVigentes(String campoBusca, String tipoBusca)
			throws ProjetoException {

		ArrayList<InsercaoPacienteBean> lista = new ArrayList<>();

		String sql = " select codpaciente,nome,matricula, cns, id_laudo, codproc, procedimento, "
				+ "mes_final, ano_final " + "from ( "
				+ " select l.id_laudo, l.codpaciente, p.nome, p.cns,p.matricula,  l.data_solicitacao, l.mes_final, l.ano_final, "
				+ " pr.codproc, pr.nome as procedimento from hosp.laudo l "
				+ " left join hosp.pacientes p on (l.codpaciente = p.id_paciente) "
				+ " left join hosp.proc pr on (l.codprocedimento_primario = pr.id) " 
				+ " where l.ativo is true and pr.ativo = 'S' ";
		if ((tipoBusca.equals("paciente") && (!campoBusca.equals(null)) && (!campoBusca.equals("")))) {
			sql = sql + " and p.nome ilike ?";
		}

		if ((tipoBusca.equals("matpaciente") && (!campoBusca.equals(null)) && (!campoBusca.equals("")))) {
			sql = sql + " and upper(p.matricula) = ?";
		}

		if ((tipoBusca.equals("prontpaciente") && (!campoBusca.equals(null)) && (!campoBusca.equals("")))) {
			sql = sql + " and p.id_paciente = ?";
		}

		if ((tipoBusca.equals("codproc") && (!campoBusca.equals(null)) && (!campoBusca.equals("")))) {
			sql = sql + " and pr.codproc = ?";
		}
		// current_date >= to_date(ano_inicio||'-'||'0'||''||mes_inicio||'-'||'01',
		// 'YYYY-MM-DD') "
		// + " and current_date <= (SELECT * FROM
		// hosp.fn_GetLastDayOfMonth(to_date(ano_final||'-'||'0'||''||mes_final||'-'||'01',
		// 'YYYY-MM-DD'))) "
		// + " AND NOT EXISTS (SELECT pac.codlaudo FROM hosp.paciente_instituicao pac
		// WHERE pac.codlaudo = l.id_laudo)"
		sql = sql + " ) a order by to_char(ano_final,'9999')|| to_char(mes_final,'99') desc";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			if ((tipoBusca.equals("paciente") && (!campoBusca.equals(null)) && (!campoBusca.equals("")))) {
				stm.setString(1, "%" + campoBusca.toUpperCase() + "%");
			}

			if (((tipoBusca.equals("codproc") || (tipoBusca.equals("matpaciente"))) && (!campoBusca.equals(null))
					&& (!campoBusca.equals("")))) {
				stm.setString(1, campoBusca.toUpperCase());
			}

			if ((tipoBusca.equals("prontpaciente") && (!campoBusca.equals(null)) && (!campoBusca.equals("")))) {
				stm.setInt(1, Integer.valueOf((campoBusca.toUpperCase())));
			}
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				InsercaoPacienteBean insercao = new InsercaoPacienteBean();
				insercao.getLaudo().setId(rs.getInt("id_laudo"));
				insercao.getLaudo().getPaciente().setId_paciente(rs.getInt("codpaciente"));
				insercao.getLaudo().getPaciente().setNome(rs.getString("nome"));
				insercao.getLaudo().getPaciente().setMatricula(rs.getString("matricula"));
				insercao.getLaudo().getPaciente().setCns(rs.getString("cns"));
				insercao.getLaudo().getProcedimentoPrimario().setCodProc(rs.getString("codproc"));
				insercao.getLaudo().getProcedimentoPrimario().setNomeProc(rs.getString("procedimento"));
				insercao.getLaudo().setMesFinal(rs.getInt("mes_final"));
				insercao.getLaudo().setAnoFinal(rs.getInt("ano_final"));

				lista.add(insercao);
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

	public InsercaoPacienteBean carregarLaudoPaciente(int id) throws ProjetoException {

		InsercaoPacienteBean insercao = new InsercaoPacienteBean();

		String sql = "select l.id_laudo, l.codpaciente, p.nome, p.cns, l.data_solicitacao, l.mes_inicio, l.ano_inicio, "
				+ " l.mes_final, l.ano_final, l.periodo, "
				+ " l.codprocedimento_primario, pr.nome as procedimento, l.cid1, ci.desccid, "
				+ " l.data_solicitacao as datainicio,  "
				+ " (SELECT * FROM hosp.fn_GetLastDayOfMonth(to_date(ano_final||'-'||'0'||''||mes_final||'-'||'01', 'YYYY-MM-DD'))) as datafinal "
				+ " from hosp.laudo l " + " left join hosp.pacientes p on (l.codpaciente = p.id_paciente) "
				+ " left join hosp.proc pr on (l.codprocedimento_primario = pr.id) "
				+ " left join hosp.cid ci on (l.cid1 = cast(ci.cod as integer)) "
				+"  where 1=1 and pr.ativo = 'S' "
				// current_date >= to_date(ano_inicio||'-'||'0'||''||mes_inicio||'-'||'01',
				// 'YYYY-MM-DD') "
				// + " and current_date <= (SELECT * FROM
				// hosp.fn_GetLastDayOfMonth(to_date(ano_final||'-'||'0'||''||mes_final||'-'||'01',
				// 'YYYY-MM-DD'))) "
				+ " and l.id_laudo = ?";
		try {
			con = ConnectionFactory.getConnection();

			PreparedStatement stm = con.prepareStatement(sql);

			stm.setInt(1, id);

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				insercao = new InsercaoPacienteBean();
				insercao.getLaudo().setId(rs.getInt("id_laudo"));
				insercao.getLaudo().getPaciente().setId_paciente(rs.getInt("codpaciente"));
				insercao.getLaudo().getPaciente().setNome(rs.getString("nome"));
				insercao.getLaudo().getPaciente().setCns(rs.getString("cns"));
				insercao.getLaudo().setDataSolicitacao(rs.getDate("data_solicitacao"));
				insercao.getLaudo().setMesInicio(rs.getInt("mes_inicio"));
				insercao.getLaudo().setAnoInicio(rs.getInt("ano_inicio"));
				insercao.getLaudo().setMesFinal(rs.getInt("mes_final"));
				insercao.getLaudo().setAnoFinal(rs.getInt("ano_final"));
				insercao.getLaudo().setPeriodo(rs.getInt("periodo"));
				insercao.getLaudo().getProcedimentoPrimario().setIdProc(rs.getInt("codprocedimento_primario"));
				insercao.getLaudo().getProcedimentoPrimario().setNomeProc(rs.getString("procedimento"));
				insercao.getLaudo().getCid1().setIdCid(rs.getInt("cid1"));
				insercao.getLaudo().getCid1().setDescCid(rs.getString("desccid"));
				insercao.getLaudo().setVigenciaInicial(rs.getDate("datainicio"));
				insercao.getLaudo().setVigenciaFinal(rs.getDate("datafinal"));
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
		return insercao;
	}

	public boolean gravarInsercaoEquipeTurno(InsercaoPacienteBean insercao, List<FuncionarioBean> lista,
			ArrayList<AgendaBean> listaAgendamento, ArrayList<Liberacao> listaLiberacao)
			throws ProjetoException {

		Boolean retorno = false;
		int idAtendimento = 0;

		GerenciarPacienteDAO gerenciarPacienteDAO = new GerenciarPacienteDAO();

		String sql = "insert into hosp.paciente_instituicao (codprograma, codgrupo, codequipe, status, codlaudo, observacao, cod_unidade, data_solicitacao, data_cadastro, turno) "
				+ " values (?, ?,  ?, ?, ?, ?, ?, ?, current_timestamp, ?) RETURNING id;";
		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, insercao.getPrograma().getIdPrograma());
			ps.setInt(2, insercao.getGrupo().getIdGrupo());
			ps.setInt(3, insercao.getEquipe().getCodEquipe());
			ps.setString(4, "A");
			ps.setInt(5, insercao.getLaudo().getId());
			ps.setString(6, insercao.getObservacao());
			ps.setInt(7, user_session.getUnidade().getId());
			ps.setDate(8, new java.sql.Date(insercao.getDataSolicitacao().getTime()));
			ps.setString(9, insercao.getTurno());
			ResultSet rs = ps.executeQuery();
			int id = 0;
			if (rs.next()) {
				id = rs.getInt("id");
			}

			String sql2 = "INSERT INTO hosp.profissional_dia_atendimento (id_paciente_instituicao, id_profissional, dia_semana) VALUES  (?, ?, ?)";
			ps = con.prepareStatement(sql2);

			for (int i = 0; i < lista.size(); i++) {
				ps.setLong(1, id);
				ps.setLong(2, lista.get(i).getId());
				for (int j = 0; j < lista.get(i).getListaDiasAtendimentoSemana().size(); j++) {
					ps.setInt(3, lista.get(i).getListaDiasAtendimentoSemana().get(j).getDiaSemana());
					ps.executeUpdate();
				}
			}

			String sql3 = SQL_INSERCAO_ATENDIMENTO;

			PreparedStatement ps3 = null;
			ps3 = con.prepareStatement(sql3);

			for (int i = 0; i < listaAgendamento.size(); i++) {

				ps3.setInt(1, insercao.getLaudo().getPaciente().getId_paciente());
				ps3.setLong(2, insercao.getEquipe().getCodEquipe());
				ps3.setDate(3,
						DataUtil.converterDateUtilParaDateSql(listaAgendamento.get(i).getDataAtendimento()));
				ps3.setInt(4, user_session.getUnidade().getParametro().getTipoAtendimento().getIdTipo());

				if (insercao.getTurno() != null) {
					ps3.setString(5, insercao.getTurno());
				} else {
					ps3.setNull(5, Types.NULL);
				}

				ps3.setString(6, insercao.getObservacao());
				ps3.setInt(7, id);
				ps3.setInt(8, user_session.getUnidade().getId());

				if (insercao.getHorario() != null) {
					ps3.setTime(9, DataUtil.retornarHorarioEmTime(insercao.getHorario()));
				} else {
					ps3.setNull(9, Types.NULL);
				}

				ps3.setBoolean(10, insercao.getEncaixe());

				ps3.setLong(11, user_session.getId());

				ps3.setLong(12, insercao.getPrograma().getIdPrograma());

				ps3.setLong(13, insercao.getGrupo().getIdGrupo());
				

				rs = ps3.executeQuery();

				idAtendimento = 0;
				if (rs.next()) {
					idAtendimento = rs.getInt("id_atendimento");
				}

				for (int j = 0; j < lista.size(); j++) {

					for (int h = 0; h < lista.get(j).getListaDiasAtendimentoSemana().size(); h++) {

						if (DataUtil.extrairDiaDeData(listaAgendamento.get(i).getDataAtendimento()) == lista.get(j).getListaDiasAtendimentoSemana().get(h).getDiaSemana()) {

							String sql4 = "INSERT INTO hosp.atendimentos1 (codprofissionalatendimento, id_atendimento, cbo, codprocedimento, id_cidprimario) VALUES  (?, ?, ?, ?, ?)";

							Integer idProcedimentoEspecifico = new AgendaDAO().
									retornaIdProcedimentoEspecifico(insercao.getPrograma().getIdPrograma(), lista.get(j).getCbo().getCodCbo(),
											insercao.getLaudo().getPaciente().getId_paciente(), insercao.getGrupo().getIdGrupo(), con);
							
							if(VerificadorUtil.verificarSeObjetoNuloOuZero(idProcedimentoEspecifico))
								idProcedimentoEspecifico = insercao.getPrograma().getProcedimento().getIdProc();
							
							PreparedStatement ps4 = null;
							ps4 = con.prepareStatement(sql4);

							ps4.setLong(1, lista.get(j).getId());
							ps4.setInt(2, idAtendimento);
							
							CboBean cboCompativel = null;
							if(user_session.getUnidade().getParametro().isValidaDadosLaudoSigtap()) {
								Date data = retornaDataSolicitacaoParaSigtap(insercao);					            
								cboCompativel = new FuncionarioDAO().buscaCboCompativelComProcedimento(data, idProcedimentoEspecifico, 
				            			lista.get(j).getId(), con);
							} else {
								cboCompativel = new FuncionarioDAO().retornaPrimeiroCboProfissional(lista.get(j).getId());
							}
								
							if (VerificadorUtil.verificarSeObjetoNuloOuZero(cboCompativel.getCodCbo())) {
								ps4.setNull(3, Types.NULL);
							} else {
								ps4.setInt(3, cboCompativel.getCodCbo());
							}
							
							if(!VerificadorUtil.verificarSeObjetoNuloOuZero(idProcedimentoEspecifico)) 
								ps4.setInt(4, idProcedimentoEspecifico);
							else
								ps4.setNull(4, Types.NULL);

							if (VerificadorUtil.verificarSeObjetoNuloOuZero(insercao.getLaudo().getCid1().getIdCid())) {
								ps4.setNull(5, Types.NULL);
							} else {
								ps4.setInt(5, insercao.getLaudo().getCid1().getIdCid());
							}
							ps4.executeUpdate();
						}
					}
				}
			}

			if (gerenciarPacienteDAO.gravarHistoricoAcaoPaciente(id, insercao.getObservacao(), TipoGravacaoHistoricoPaciente.INSERCAO.getSigla(), con)) {
				if (!VerificadorUtil.verificarSeListaNuloOuVazia(Collections.singletonList(listaLiberacao))) {
					GerenciarPacienteDAO gDao = new GerenciarPacienteDAO();
					if (gDao.gravarLiberacao(id, listaLiberacao, idAtendimento, con)) {
						con.commit();
					}
				} else {
					con.commit();
				}
				retorno = true;
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

	private Date retornaDataSolicitacaoParaSigtap(InsercaoPacienteBean insercao) {
		Date data = null;	
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(insercao.getDataSolicitacao());
		int mesSolicitacao = calendar.get(Calendar.MONTH);
		mesSolicitacao++;
		int anoSolicitacao = calendar.get(Calendar.YEAR);
		if(!new ProcedimentoDAO().verificaExisteCargaSigtapParaData(mesSolicitacao, anoSolicitacao))
			data = DataUtil.retornaDataComMesAnterior(insercao.getDataSolicitacao());
		else
			data = insercao.getDataSolicitacao();
		return data;
	}

	public boolean gravarInsercaoEquipeDiaHorario(InsercaoPacienteBean insercao,
			ArrayList<AgendaBean> listaAgendamento, ArrayList<Liberacao> listaLiberacao,
			List<FuncionarioBean> listaProfissionais) throws ProjetoException {

		Boolean retorno = false;
		int idAtendimento = 0;

		GerenciarPacienteDAO gerenciarPacienteDAO = new GerenciarPacienteDAO();

		String sql = "insert into hosp.paciente_instituicao (codprograma, codgrupo, codequipe, status, codlaudo, observacao, cod_unidade, data_solicitacao, data_cadastro) "
				+ " values (?, ?, ?, ?, ?, ?, ?, ?, current_timestamp) RETURNING id;";
		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, insercao.getPrograma().getIdPrograma());
			ps.setInt(2, insercao.getGrupo().getIdGrupo());
			ps.setInt(3, insercao.getEquipe().getCodEquipe());
			ps.setString(4, "A");
			ps.setInt(5, insercao.getLaudo().getId());
			ps.setString(6, insercao.getObservacao());
			ps.setInt(7, user_session.getUnidade().getId());
			ps.setDate(8, new java.sql.Date(insercao.getDataSolicitacao().getTime()));
			ResultSet rs = ps.executeQuery();
			int id = 0;
			if (rs.next()) {
				id = rs.getInt("id");
			}

			String sql2 = "INSERT INTO hosp.profissional_dia_atendimento (id_paciente_instituicao, id_profissional, dia_semana, horario_atendimento) VALUES  (?, ?, ?, ?)";
			ps = con.prepareStatement(sql2);

			for (int i = 0; i < listaProfissionais.size(); i++) {
				ps.setLong(1, id);
				ps.setLong(2, listaProfissionais.get(i).getId());
				for (int j = 0; j < listaProfissionais.get(i).getListaDiasAtendimentoSemana().size(); j++) {
					ps.setInt(3, listaProfissionais.get(i).getListaDiasAtendimentoSemana().get(j).getDiaSemana());
					ps.setTime(4, DataUtil.retornarHorarioEmTime(listaProfissionais.get(i).getListaDiasAtendimentoSemana().get(j).getHorario()));
					ps.executeUpdate();
				}
			}
			
			String sql3 = SQL_INSERCAO_ATENDIMENTO;

			PreparedStatement ps3 = null;
			ps3 = con.prepareStatement(sql3);

			for (int i = 0; i < listaAgendamento.size(); i++) {

				ps3.setInt(1, insercao.getLaudo().getPaciente().getId_paciente());
				ps3.setLong(2, insercao.getEquipe().getCodEquipe());
				ps3.setDate(3,
						DataUtil.converterDateUtilParaDateSql(listaAgendamento.get(i).getDataAtendimento()));
				ps3.setInt(4, user_session.getUnidade().getParametro().getTipoAtendimento().getIdTipo());

				if (insercao.getTurno() != null) {
					ps3.setString(5, insercao.getTurno());
				} else {
					ps3.setNull(5, Types.NULL);
				}

				ps3.setString(6, insercao.getObservacao());
				ps3.setInt(7, id);
				ps3.setInt(8, user_session.getUnidade().getId());

				if (insercao.getHorario() != null) {
					ps3.setTime(9, DataUtil.retornarHorarioEmTime(insercao.getHorario()));
				} else {
					ps3.setNull(9, Types.NULL);
				}

				ps3.setBoolean(10, insercao.getEncaixe());
				ps3.setLong(11, user_session.getId());
				ps3.setLong(12, insercao.getPrograma().getIdPrograma());
				ps3.setLong(13, insercao.getGrupo().getIdGrupo());
				rs = ps3.executeQuery();

				idAtendimento = 0;
				if (rs.next()) {
					idAtendimento = rs.getInt("id_atendimento");
				}
				
				for (int j = 0; j < listaProfissionais.size(); j++) {

					for (int h = 0; h < listaProfissionais.get(j).getListaDiasAtendimentoSemana().size(); h++) {

						if (DataUtil.extrairDiaDeData(
								listaAgendamento.get(i).getDataAtendimento()) == listaProfissionais.get(j).getListaDiasAtendimentoSemana().get(h).getDiaSemana()) {

							String sql4 = "INSERT INTO hosp.atendimentos1 (codprofissionalatendimento, id_atendimento, cbo, codprocedimento, horario_atendimento, id_cidprimario) VALUES  (?, ?, ?, ?, ?, ?)";

							Integer idProcedimentoEspecifico = new AgendaDAO().
									retornaIdProcedimentoEspecifico(insercao.getPrograma().getIdPrograma(), listaProfissionais.get(j).getCbo().getCodCbo(),
											insercao.getLaudo().getPaciente().getId_paciente(), insercao.getGrupo().getIdGrupo(), con);
							if(VerificadorUtil.verificarSeObjetoNuloOuZero(idProcedimentoEspecifico))
								idProcedimentoEspecifico = insercao.getPrograma().getProcedimento().getIdProc();
							
							PreparedStatement ps4 = null;
							ps4 = con.prepareStatement(sql4);

							ps4.setLong(1, listaProfissionais.get(j).getId());
							ps4.setInt(2, idAtendimento);
							
							CboBean cboCompativel = null;
							if(user_session.getUnidade().getParametro().isValidaDadosLaudoSigtap()) {
								Date data = retornaDataSolicitacaoParaSigtap(insercao);					            
								cboCompativel = new FuncionarioDAO().buscaCboCompativelComProcedimento(data, idProcedimentoEspecifico, 
										listaProfissionais.get(j).getId(), con);
							} else {
								cboCompativel = new FuncionarioDAO().retornaPrimeiroCboProfissional(listaProfissionais.get(j).getId());
							}
							
							if (!VerificadorUtil.verificarSeObjetoNuloOuZero(cboCompativel.getCodCbo())) {
								ps4.setInt(3, cboCompativel.getCodCbo());
							} else {
								ps4.setNull(3, Types.NULL);
							}

							if(!VerificadorUtil.verificarSeObjetoNuloOuZero(idProcedimentoEspecifico)) 
								ps4.setInt(4, idProcedimentoEspecifico);
							 else
								ps4.setNull(4, Types.NULL);
							
							if (VerificadorUtil.verificarSeObjetoNuloOuZero(
									listaProfissionais.get(j).getListaDiasAtendimentoSemana().get(h).getHorario())) {
								ps4.setNull(5, Types.NULL);
							} else {
								ps4.setTime(5,
										DataUtil.retornarHorarioEmTime(listaProfissionais.get(j).getListaDiasAtendimentoSemana().get(h).getHorario()));
							}
							ps4.setInt(6, insercao.getLaudo().getCid1().getIdCid());

							ps4.executeUpdate();
						}
					}
				}
			}

			if (gerenciarPacienteDAO.gravarHistoricoAcaoPaciente(id, insercao.getObservacao(), TipoGravacaoHistoricoPaciente.INSERCAO.getSigla(), con)) {
				if (!VerificadorUtil.verificarSeListaNuloOuVazia(Collections.singletonList(listaLiberacao))) {
					GerenciarPacienteDAO gDao = new GerenciarPacienteDAO();
					if (gDao.gravarLiberacao(id, listaLiberacao, idAtendimento, con)) {
						con.commit();
					}
				} else {
					con.commit();
				}
				retorno = true;
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

	public boolean gravarInsercaoProfissional(InsercaoPacienteBean insercao,
			ArrayList<AgendaBean> listaAgendamento) throws ProjetoException {

		Boolean retorno = false;

		GerenciarPacienteDAO gerenciarPacienteDAO = new GerenciarPacienteDAO();

		String sql = "insert into hosp.paciente_instituicao (codprofissional, status, codlaudo, observacao, data_solicitacao, cod_unidade) "
				+ " values (?, ?, ?, ?, ?, ?) RETURNING id;";
		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setLong(1, insercao.getFuncionario().getId());
			ps.setString(2, "A");
			ps.setInt(3, insercao.getLaudo().getId());
			ps.setString(4, insercao.getObservacao());
			ps.setDate(5, new java.sql.Date(insercao.getDataSolicitacao().getTime()));
			ps.setInt(6, user_session.getUnidade().getId());

			ResultSet rs = ps.executeQuery();
			int id = 0;
			if (rs.next()) {
				id = rs.getInt("id");
			}

			String sql2 = "INSERT INTO hosp.profissional_dia_atendimento (id_paciente_instituicao, id_profissional, dia_semana, cod_empresa) VALUES  (?, ?, ?, ?)";
			PreparedStatement ps2 = null;
			ps2 = con.prepareStatement(sql2);

			for (int i = 0; i < insercao.getFuncionario().getListaDiasAtendimentoSemana().size(); i++) {
				ps2.setLong(1, id);
				ps2.setLong(2, insercao.getFuncionario().getId());
				ps2.setInt(3, insercao.getFuncionario().getListaDiasAtendimentoSemana().get(i).getDiaSemana());
				ps2.setInt(4, user_session.getUnidade().getId());
				ps2.executeUpdate();

			}

			String sql3 = "INSERT INTO hosp.atendimentos(codpaciente, codmedico, situacao, dtaatende, codtipoatendimento, turno, observacao, ativo, "
					+ "id_paciente_instituicao, cod_empresa, horario, encaixe)"
					+ " VALUES (?, ?, 'A', ?, ?, ?, ?, 'S', ?, ?, ?, ?) RETURNING id_atendimento;";

			PreparedStatement ps3 = null;
			ps3 = con.prepareStatement(sql3);

			for (int i = 0; i < listaAgendamento.size(); i++) {

				ps3.setInt(1, insercao.getLaudo().getPaciente().getId_paciente());
				ps3.setLong(2, insercao.getFuncionario().getId());
				ps3.setDate(3,
						DataUtil.converterDateUtilParaDateSql(listaAgendamento.get(i).getDataAtendimento()));
				ps3.setInt(4, user_session.getUnidade().getParametro().getTipoAtendimento().getIdTipo());
				ps3.setString(5, insercao.getTurno());
				ps3.setString(6, insercao.getObservacao());
				ps3.setInt(7, id);
				ps3.setInt(8, user_session.getUnidade().getId());
				if (insercao.getHorario() != null) {
					ps3.setTime(9, DataUtil.retornarHorarioEmTime(insercao.getHorario()));
				} else {
					ps3.setNull(9, Types.NULL);
				}

				ps3.setBoolean(10, insercao.getEncaixe());

				rs = ps3.executeQuery();

				int idAgend = 0;
				if (rs.next()) {
					idAgend = rs.getInt("id_atendimento");
				}

				String sql4 = "INSERT INTO hosp.atendimentos1 (codprofissionalatendimento, id_atendimento, cbo, codprocedimento, id_cidprimario) VALUES  (?, ?, ?, ?, ?)";

				Integer idProcedimentoEspecifico = new AgendaDAO().
						retornaIdProcedimentoEspecifico(insercao.getPrograma().getIdPrograma(), insercao.getFuncionario().getCbo().getCodCbo(),
								insercao.getLaudo().getPaciente().getId_paciente(), insercao.getGrupo().getIdGrupo(), con);
				PreparedStatement ps4 = null;
				ps4 = con.prepareStatement(sql4);

				ps4.setLong(1, insercao.getFuncionario().getId());
				ps4.setInt(2, idAgend);
				ps4.setInt(3, insercao.getFuncionario().getCbo().getCodCbo());
				
				if(!VerificadorUtil.verificarSeObjetoNuloOuZero(idProcedimentoEspecifico)) 
					ps4.setInt(4, idProcedimentoEspecifico);
				else
					ps4.setInt(4, insercao.getFuncionario().getProc1().getIdProc());
				
				ps4.setInt(5, insercao.getLaudo().getCid1().getIdCid());

				ps4.executeUpdate();
			}

			if (gerenciarPacienteDAO.gravarHistoricoAcaoPaciente(id, insercao.getObservacao(), TipoGravacaoHistoricoPaciente.INSERCAO.getSigla(), con)) {
				con.commit();
				retorno = true;
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

	public Date dataFinalLaudo(int id) throws ProjetoException {

		Date data = null;

		String sql = "select (SELECT * FROM hosp.fn_GetLastDayOfMonth(to_date(ano_final||'-'||'0'||''||mes_final||'-'||'01', 'YYYY-MM-DD'))) as datafinal "
				+ " from hosp.laudo l where l.id_laudo = ?";

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, id);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				data = rs.getDate("datafinal");
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
		return data;
	}
	
	public Date dataFinalPacienteSemLaudo(InsercaoPacienteBean insercao, Integer codPaciente) throws ProjetoException {

		Date data = null;

		String sql = "select (SELECT * FROM hosp.fn_GetLastDayOfMonth(to_date(ano_final||'-'||'0'||''||mes_final||'-'||'01', 'YYYY-MM-DD')))  + INTERVAL '1 DAYS' as datafinal  from hosp.paciente_instituicao pi  join hosp.laudo l on l.id_laudo = pi.codlaudo  where l.codpaciente=? and pi.codprograma=? and pi.codgrupo=?  and l.id_laudo= ( " +
				"  select max(l1.id_laudo) from hosp.paciente_instituicao pi1 " + 
				" join hosp.laudo l1 on l1.id_laudo = pi1.codlaudo where l1.codpaciente=? and pi1.codprograma=? and pi1.codgrupo=? " + 
				" and to_char(l1.ano_final, '9999')||lpad(trim(to_char(l1.mes_final,'99')),2,'0')= " + 
				" (select max(to_char(l2.ano_final, '9999')||lpad(trim(to_char(l2.mes_final,'99')),2,'0')) from hosp.paciente_instituicao pi2 " + 
				" join hosp.laudo l2 on l2.id_laudo = pi2.codlaudo " + 
				" where l2.codpaciente=? and pi2.codprograma=? and pi2.codgrupo=? " + 
				" ) " + 
				")" ;

		try {
			con = ConnectionFactory.getConnection();

			PreparedStatement stm = con.prepareStatement(sql);

			stm.setInt(1, codPaciente);
			stm.setInt(2, insercao.getPrograma().getIdPrograma());
			stm.setInt(3, insercao.getGrupo().getIdGrupo());
			stm.setInt(4, codPaciente);
			stm.setInt(5, insercao.getPrograma().getIdPrograma());
			stm.setInt(6, insercao.getGrupo().getIdGrupo());
			stm.setInt(7, codPaciente);
			stm.setInt(8, insercao.getPrograma().getIdPrograma());
			stm.setInt(9, insercao.getGrupo().getIdGrupo());
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				data = rs.getDate("datafinal");
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
		return data;
	}
	

	public Boolean verificarSeLaudoConstaNoAtendimento(int codLaudo) throws ProjetoException {

		Boolean retorno = false;
		String sql = "SELECT id_atendimento FROM hosp.atendimentos WHERE cod_laudo = ?";

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, codLaudo);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				retorno = true;
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

	public Boolean verificarSeAlgumAtendimentoDoLaudoTemPerfilAvaliacao(int codLaudo) throws ProjetoException {

		Boolean retorno = false;

		String sql = "SELECT a.id_atendimento " + "FROM hosp.atendimentos a "
				+ "JOIN hosp.atendimentos1 a1 ON (a.id_atendimento = a1.id_atendimento) "
				+ "WHERE cod_laudo = ? AND a1.perfil_avaliacao = 'S';";

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, codLaudo);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				retorno = true;
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

	public Boolean verificarSeAlgumAtendimentoNaoFoiLancadoPerfil(int codLaudo) throws ProjetoException {

		Boolean retorno = false;

		String sql = "SELECT a.id_atendimento " + "FROM hosp.atendimentos a "
				+ "JOIN hosp.atendimentos1 a1 ON (a.id_atendimento = a1.id_atendimento) "
				+ "WHERE cod_laudo = ? AND (a1.perfil_avaliacao = '' OR a1.perfil_avaliacao IS NULL);";

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, codLaudo);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				retorno = true;
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

	public AvaliacaoInsercaoDTO carregarAtendimentoAvaliacao(int codLaudo) throws ProjetoException {

		AvaliacaoInsercaoDTO avaliacaoInsercaoDTO = new AvaliacaoInsercaoDTO();

		String sql = "SELECT a.codprograma, a.grupo_avaliacao, a.codequipe, p.cod_procedimento "
				+ "FROM hosp.atendimentos a " + "JOIN hosp.programa p ON (a.codprograma = p.id_programa) "
				+ "WHERE a.cod_laudo = ?;";

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, codLaudo);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				ProgramaDAO programaDAO = new ProgramaDAO();
				GrupoDAO grupoDAO = new GrupoDAO();
				EquipeDAO equipeDAO = new EquipeDAO();
				ProcedimentoDAO procedimentoDAO = new ProcedimentoDAO();

				avaliacaoInsercaoDTO
						.setProgramaBean(programaDAO.listarProgramaPorIdComConexao(rs.getInt("codprograma"), con));
				avaliacaoInsercaoDTO
						.setGrupoBean(grupoDAO.listarGrupoPorIdComConexao(rs.getInt("grupo_avaliacao"), con));
				avaliacaoInsercaoDTO.setEquipeBean(equipeDAO.buscarEquipePorIDComConexao(rs.getInt("codequipe"), con));
				avaliacaoInsercaoDTO.getProgramaBean().setProcedimento(
						procedimentoDAO.listarProcedimentoPorIdComConexao(rs.getInt("cod_procedimento"), con));
				avaliacaoInsercaoDTO
						.setListaProfissionais(listarProfissionaisDaAvaliacaoQueOPacienteTemPerfil(codLaudo, con));
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
		return avaliacaoInsercaoDTO;
	}

	public ArrayList<FuncionarioBean> listarProfissionaisDaAvaliacaoQueOPacienteTemPerfil(int codLaudo,
			Connection conAuxiliar) throws ProjetoException, SQLException {

		ArrayList<FuncionarioBean> lista = new ArrayList<>();

		String sql = "SELECT a1.codprofissionalatendimento, f.descfuncionario, e.descespecialidade, f.codespecialidade "
				+ "FROM hosp.atendimentos a " + "JOIN hosp.atendimentos1 a1 ON (a.id_atendimento = a1.id_atendimento) "
				+ "JOIN acl.funcionarios f ON (a1.codprofissionalatendimento = f.id_funcionario) "
				+ "JOIN hosp.especialidade e ON (f.codespecialidade = e.id_especialidade) "
				+ "WHERE a.cod_laudo = ? AND a1.perfil_avaliacao = 'S' " + "ORDER BY f.descfuncionario";

		try {

			PreparedStatement stm = conAuxiliar.prepareStatement(sql);
			stm.setInt(1, codLaudo);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				FuncionarioBean funcionarioBean = new FuncionarioBean();
				funcionarioBean.setNome(rs.getString("descfuncionario"));
				funcionarioBean.setId(rs.getLong("codprofissionalatendimento"));
				funcionarioBean.getEspecialidade().setDescEspecialidade(rs.getString("descespecialidade"));
				funcionarioBean.getEspecialidade().setCodEspecialidade(rs.getInt("codespecialidade"));
				lista.add(funcionarioBean);
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

	public Boolean verificarSeExisteLaudoAtivoParaProgramaIhGrupo(int codPrograma, int codGrupo, int codPaciente) throws ProjetoException {

		Boolean retorno = false;

		String sql = "SELECT pi.codprograma, pi.id FROM hosp.paciente_instituicao pi " + 
				"left join hosp.laudo l on l.id_laudo = pi.codlaudo " + 
				" WHERE pi.status = 'A' AND pi.codprograma = ? AND pi.codgrupo = ?" + 
				" and pi.id_paciente = ?";

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, codPrograma);
			stm.setInt(2, codGrupo);
			stm.setInt(3, codPaciente);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				retorno = true;
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

	public boolean dataInclusaoPacienteEstaEntreDataInicialIhFinalDoLaudo(Integer idLaudo, Date dataInclusao) throws ProjetoException {
		
		boolean dataValida = false;
		String sql = "select exists ( " + 
				"	select l.data_solicitacao " + 
				"		from hosp.laudo l where l.id_laudo = ? " + 
				"		and (? between l.data_solicitacao and " + 
				"			(SELECT * FROM hosp.fn_GetLastDayOfMonth(to_date(l.ano_final ||'-'||'0'||''||l.mes_final ||'-'||'01', 'YYYY-MM-DD')) as data_final))) as valida";
		
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);

			stm.setInt(1, idLaudo);
			stm.setDate(2, new java.sql.Date(dataInclusao.getTime()));
			ResultSet rs = stm.executeQuery();

			if (rs.next()) {
				dataValida = rs.getBoolean("valida");
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
		return dataValida;
	}
	
	
	public boolean gravarInsercaoNormalSemLaudo(InsercaoPacienteBean insercao, List<FuncionarioBean> lista,
			ArrayList<AgendaBean> listaAgendamento, OpcaoAtendimento opcaoAtendimento) throws ProjetoException {
		
		boolean inseriu = false;
		String sql = "insert into hosp.paciente_instituicao (codprograma, codgrupo, status, observacao, "
				+ "cod_unidade, data_solicitacao, data_cadastro, turno, id_paciente, inclusao_sem_laudo, codequipe) "
				+ " values (?, ?, 'A', ?, ?, ?, current_timestamp, ?, ?, true, ?) RETURNING id;";
		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, insercao.getPrograma().getIdPrograma());
			ps.setInt(2, insercao.getGrupo().getIdGrupo());
			ps.setString(3, insercao.getObservacao());
			ps.setInt(4, user_session.getUnidade().getId());
			ps.setDate(5, new java.sql.Date(insercao.getDataSolicitacao().getTime()));
			ps.setString(6, insercao.getTurno());
			ps.setInt(7, insercao.getPaciente().getId_paciente());
			ps.setInt(8, insercao.getEquipe().getCodEquipe());
			ResultSet rs = ps.executeQuery();
			Integer idPacienteInstituicao = null;
			if (rs.next()) {
				idPacienteInstituicao = rs.getInt("id");
			}
			
			if(opcaoAtendimento.equals(OpcaoAtendimento.SOMENTE_TURNO))
				inserirDiasAtendimentoTurno(idPacienteInstituicao, lista, con, insercao.getTurno());
			else
				inserirDiasAtendimentoHorario(idPacienteInstituicao, lista, con);
			
			inserirAtendimento(idPacienteInstituicao, insercao, lista, listaAgendamento, insercao.getPaciente(), con);
			new GerenciarPacienteDAO().gravarHistoricoAcaoPaciente(idPacienteInstituicao, insercao.getObservacao(), TipoGravacaoHistoricoPaciente.INSERCAO.getSigla(), con);
			con.commit();
			inseriu = true;
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
		return inseriu;
	}
	
	private void inserirAtendimento(Integer idPacienteInstituicao, InsercaoPacienteBean insercao,
			List<FuncionarioBean> lista, ArrayList<AgendaBean> listaAgendamento, PacienteBean paciente, Connection conAuxiliar) throws ProjetoException, SQLException {
			String sql = SQL_INSERCAO_ATENDIMENTO;
		
		try {
			PreparedStatement ps = conAuxiliar.prepareStatement(sql);
			for (int i = 0; i < listaAgendamento.size(); i++) {

				ps.setInt(1, paciente.getId_paciente());
				ps.setInt(2, insercao.getEquipe().getCodEquipe());
				ps.setDate(3, DataUtil.converterDateUtilParaDateSql(listaAgendamento.get(i).getDataAtendimento()));
				ps.setInt(4, user_session.getUnidade().getParametro().getTipoAtendimento().getIdTipo());
				ps.setString(5, insercao.getTurno());
				ps.setString(6, insercao.getObservacao());
				ps.setInt(7, idPacienteInstituicao);
				ps.setInt(8, user_session.getUnidade().getId());
				if (insercao.getHorario() != null) {
					ps.setTime(9, DataUtil.retornarHorarioEmTime(insercao.getHorario()));
				} else {
					ps.setNull(9, Types.NULL);
				}

				ps.setBoolean(10, insercao.getEncaixe());
				ps.setLong(11, user_session.getId());
				ps.setLong(12, insercao.getPrograma().getIdPrograma());
				ps.setLong(13, insercao.getGrupo().getIdGrupo());
				ResultSet rs = ps.executeQuery();

				Integer idAtendimento = 0;
				if (rs.next()) {
					idAtendimento = rs.getInt("id_atendimento");
				}
				
				for (FuncionarioBean profissional : lista) {
					if(insercao.isInsercaoPacienteSemLaudo() &&
							VerificadorUtil.verificarSeObjetoNuloOuZero(insercao.getLaudo().getId())) {
						inserirAtendimentos1(insercao, idAtendimento, profissional, listaAgendamento.get(i), conAuxiliar);
					}
				}
			}

		} catch (SQLException sqle) {
			conAuxiliar.rollback();
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			conAuxiliar.rollback();
			throw new ProjetoException(ex, this.getClass().getName());
		} 
	}
	
	private void inserirAtendimentos1 (InsercaoPacienteBean insercao, Integer idAtendimento, FuncionarioBean profissional,
			AgendaBean agendamento, Connection conAuxiliar) throws ProjetoException, SQLException {

		try {
			for (int h = 0; h < profissional.getListaDiasAtendimentoSemana().size(); h++) {

				if (DataUtil.extrairDiaDeData(agendamento.getDataAtendimento()) == 
						profissional.getListaDiasAtendimentoSemana().get(h).getDiaSemana()) {
					
					String sql = "INSERT INTO hosp.atendimentos1 (codprofissionalatendimento, id_atendimento, cbo, codprocedimento, id_cidprimario) VALUES  (?, ?, ?, ?, ?)";

					PreparedStatement ps = conAuxiliar.prepareStatement(sql);
					ps = conAuxiliar.prepareStatement(sql);
					
					Integer idProcedimentoEspecifico = new AgendaDAO().
							retornaIdProcedimentoEspecifico(insercao.getPrograma().getIdPrograma(), profissional.getCbo().getCodCbo(),
									insercao.getPaciente().getId_paciente(), insercao.getGrupo().getIdGrupo(), conAuxiliar);
					if(VerificadorUtil.verificarSeObjetoNuloOuZero(idProcedimentoEspecifico))
						idProcedimentoEspecifico = insercao.getPrograma().getProcedimento().getIdProc();

					ps.setLong(1, profissional.getId());
					ps.setInt(2, idAtendimento);
					
					CboBean cboCompativel = null;
					if(user_session.getUnidade().getParametro().isValidaDadosLaudoSigtap()) {
						Date data = retornaDataSolicitacaoParaSigtap(insercao);					            
						cboCompativel = new FuncionarioDAO().buscaCboCompativelComProcedimento(data, idProcedimentoEspecifico, 
								profissional.getId(), con);
					} else {
						cboCompativel = new FuncionarioDAO().retornaPrimeiroCboProfissional(profissional.getId());
					}
					
					if (VerificadorUtil.verificarSeObjetoNuloOuZero(cboCompativel.getCodCbo())) {
						ps.setNull(3, Types.NULL);
					} else {
						ps.setInt(3, cboCompativel.getCodCbo());
					}
					
					if (!VerificadorUtil.verificarSeObjetoNuloOuZero(idProcedimentoEspecifico))
						ps.setInt(4, idProcedimentoEspecifico);
					else if (!VerificadorUtil.verificarSeObjetoNuloOuZero(insercao.getPrograma().getProcedimento().getIdProc())) {
						ps.setInt(4, insercao.getPrograma().getProcedimento().getIdProc());
					} else {
						ps.setNull(4, Types.NULL);
					}
					
					if (VerificadorUtil.verificarSeObjetoNuloOuZero(insercao.getLaudo().getCid1().getIdCid())) {
						ps.setNull(5, Types.NULL);
					} else {
						ps.setInt(5, insercao.getLaudo().getCid1().getIdCid());
					}
					ps.executeUpdate();
				}
			}

		} catch (SQLException sqle) {
			conAuxiliar.rollback();
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			conAuxiliar.rollback();
			throw new ProjetoException(ex, this.getClass().getName());
		}
	}


	public void inserirDiasAtendimentoTurno(Integer idPacienteInstituicao, List<FuncionarioBean> lista,
			Connection conAuxiliar, String turno) throws ProjetoException, SQLException {

		String sql = "INSERT INTO hosp.profissional_dia_atendimento (id_paciente_instituicao, id_profissional, dia_semana, turno) VALUES  (?, ?, ?, ?)";

		try {
			PreparedStatement ps = conAuxiliar.prepareStatement(sql);

			for (int i = 0; i < lista.size(); i++) {
				ps.setLong(1, idPacienteInstituicao);
				ps.setLong(2, lista.get(i).getId());
				
				for (int j = 0; j < lista.get(i).getListaDiasAtendimentoSemana().size(); j++) {
					ps.setInt(3, lista.get(i).getListaDiasAtendimentoSemana().get(j).getDiaSemana());
					
					if(VerificadorUtil.verificarSeObjetoNuloOuVazio(turno))
						ps.setString(4, lista.get(i).getListaDiasAtendimentoSemana().get(j).getTurno());
					else
						ps.setString(4, turno);
					ps.executeUpdate();
				}
			}

		} catch (SQLException sqle) {
			conAuxiliar.rollback();
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			conAuxiliar.rollback();
			throw new ProjetoException(ex, this.getClass().getName());
		} 
	}
	
	public void inserirDiasAtendimentoHorario(Integer idPacienteInstituicao, List<FuncionarioBean> lista,
			Connection conAuxiliar) throws ProjetoException, SQLException {

		String sql = "INSERT INTO hosp.profissional_dia_atendimento (id_paciente_instituicao, id_profissional, dia_semana, horario_atendimento) VALUES  (?, ?, ?, ?)";

		try {
			PreparedStatement ps = conAuxiliar.prepareStatement(sql);

			for (int i = 0; i < lista.size(); i++) {
				ps.setLong(1, idPacienteInstituicao);
				ps.setLong(2, lista.get(i).getId());
				
				for (int j = 0; j < lista.get(i).getListaDiasAtendimentoSemana().size(); j++) {
					ps.setInt(3, lista.get(i).getListaDiasAtendimentoSemana().get(j).getDiaSemana());
					ps.setTime(4, DataUtil.retornarHorarioEmTime(lista.get(i).getListaDiasAtendimentoSemana().get(j).getHorario()));
					ps.executeUpdate();
				}
			}

		} catch (SQLException sqle) {
			conAuxiliar.rollback();
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			conAuxiliar.rollback();
			throw new ProjetoException(ex, this.getClass().getName());
		} 
	}
	
	public boolean gravarInsercaoTurnoSemLaudo(InsercaoPacienteBean insercao, List<FuncionarioBean> lista,
			ArrayList<AgendaBean> listaAgendamento) throws ProjetoException {
		
		boolean inseriu = false;
		
		String sql = "insert into hosp.paciente_instituicao (codprograma, codgrupo, status, observacao, "
						+ "cod_unidade, data_solicitacao, data_cadastro, turno, id_paciente, inclusao_sem_laudo, sessoes) "
						+ " values (?, ?, 'A', ?, ?, ?, current_timestamp, ?, ?, true, ?) RETURNING id;";
		
		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, insercao.getPrograma().getIdPrograma());
			ps.setInt(2, insercao.getGrupo().getIdGrupo());
			ps.setString(3, insercao.getObservacao());
			ps.setInt(4, user_session.getUnidade().getId());
			ps.setDate(5, new java.sql.Date(insercao.getDataSolicitacao().getTime()));
			ps.setString(6, insercao.getTurno());
			ps.setInt(7, insercao.getPaciente().getId_paciente());
			ps.setInt(8, insercao.getSessoes());
			ResultSet rs = ps.executeQuery();
			Integer idPacienteInstituicao = null;
			if (rs.next()) {
				idPacienteInstituicao = rs.getInt("id");
			}
			inserirDiasAtendimentoTurno(idPacienteInstituicao, lista, con, null);
			inserirAtendimentoSemLaudo(idPacienteInstituicao, insercao, lista, listaAgendamento, con);
			inserirProcedimentosCidsDoPacienteInstituicao(idPacienteInstituicao, insercao.getListaProcedimentoCid(), con);
			new GerenciarPacienteDAO().gravarHistoricoAcaoPaciente(idPacienteInstituicao, insercao.getObservacao(), TipoGravacaoHistoricoPaciente.INSERCAO.getSigla(), con);
			con.commit();
			inseriu = true;
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
		return inseriu;
	}

	public void inserirAtendimentoSemLaudo(Integer idPacienteInstituicao, InsercaoPacienteBean insercao,
			List<FuncionarioBean> lista, ArrayList<AgendaBean> listaAgendamento, Connection conAuxiliar) throws ProjetoException, SQLException {

		String sql = "INSERT INTO hosp.atendimentos(codpaciente, situacao, dtaatende, codtipoatendimento, turno, "
				+ " observacao, ativo, id_paciente_instituicao, cod_unidade, encaixe, codatendente, dtamarcacao, codprograma, codgrupo)"
				+ " VALUES (?, 'A', ?, ?, ?, ?, 'S', ?, ?, false, ?, current_timestamp, ?, ?) RETURNING id_atendimento;";

		try {
			PreparedStatement ps = conAuxiliar.prepareStatement(sql);
			Integer limiteSessoes = 0;
			for (int i = 0; i < listaAgendamento.size(); i++) {

				ps.setInt(1, insercao.getPaciente().getId_paciente());
				ps.setDate(2, DataUtil.converterDateUtilParaDateSql(listaAgendamento.get(i).getDataAtendimento()));
				ps.setInt(3, user_session.getUnidade().getParametro().getTipoAtendimento().getIdTipo());

				if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(listaAgendamento.get(i).getTurno())) {
					ps.setString(4, listaAgendamento.get(i).getTurno());
				} else {
					ps.setNull(4, Types.NULL);
				}

				ps.setString(5, insercao.getObservacao());
				ps.setInt(6, idPacienteInstituicao);
				ps.setInt(7, user_session.getUnidade().getId());
				ps.setLong(8, user_session.getId());
				ps.setLong(9, insercao.getPrograma().getIdPrograma());
				ps.setLong(10, insercao.getGrupo().getIdGrupo());
				

				ResultSet rs = ps.executeQuery();

				Integer idAtendimento = 0;
				if (rs.next()) {
					idAtendimento = rs.getInt("id_atendimento");
				}
				
				for (FuncionarioBean profissional : lista) {
					limiteSessoes = inserirAtendimentos1SemLaudo(insercao, idAtendimento, profissional, listaAgendamento.get(i), conAuxiliar, limiteSessoes);
				 if(limiteSessoes.equals(insercao.getSessoes()))
					 break;
				}
				 if(limiteSessoes.equals(insercao.getSessoes()))
					 break;
			}

		} catch (SQLException sqle) {
			conAuxiliar.rollback();
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			conAuxiliar.rollback();
			throw new ProjetoException(ex, this.getClass().getName());
		} 
	}	
		
	private Integer inserirAtendimentos1SemLaudo(InsercaoPacienteBean insercao, Integer idAtendimento, FuncionarioBean profissional,
			AgendaBean agendamento, Connection conAuxiliar, Integer limiteSessao) throws ProjetoException, SQLException {

		try {
			for (int h = 0; h < profissional.getListaDiasAtendimentoSemana().size(); h++) {

				if (DataUtil.extrairDiaDeData(agendamento.getDataAtendimento()) == 
						profissional.getListaDiasAtendimentoSemana().get(h).getDiaSemana() &&
						agendamento.getTurno().equals(profissional.getListaDiasAtendimentoSemana().get(h).getTurno()) ) {

					
					String sql = "INSERT INTO hosp.atendimentos1 (codprofissionalatendimento, id_atendimento, cbo, codprocedimento, id_cidprimario) VALUES  (?, ?, ?, ?, ?)";

					PreparedStatement ps = conAuxiliar.prepareStatement(sql);
					ps = conAuxiliar.prepareStatement(sql);

					ps.setLong(1, profissional.getId());
					ps.setInt(2, idAtendimento);
					
					CboBean cboCompativel = new FuncionarioDAO().retornaPrimeiroCboProfissional(profissional.getId());
					
					if (VerificadorUtil.verificarSeObjetoNuloOuZero(cboCompativel.getCodCbo())) {
						ps.setNull(3, Types.NULL);
					} else {
						ps.setInt(3, cboCompativel.getCodCbo());
					}
					
					ps.setNull(4, Types.NULL);
					ps.setNull(5, Types.NULL);
					ps.executeUpdate();
					
					limiteSessao++;
				}
			}

		} catch (SQLException sqle) {
			conAuxiliar.rollback();
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			conAuxiliar.rollback();
			throw new ProjetoException(ex, this.getClass().getName());
		}
		return limiteSessao;
	}
	
	
	public void inserirProcedimentosCidsDoPacienteInstituicao(Integer idPacienteInstituicao, List<ProcedimentoCidDTO> lista,
			Connection conAuxiliar) throws ProjetoException, SQLException {

		String sql = "INSERT INTO hosp.paciente_instituicao_procedimento_cid " +
				"(id_paciente_instituicao, id_procedimento, id_cid) VALUES(?, ?, ?);";

		try {
			PreparedStatement ps = conAuxiliar.prepareStatement(sql);

			for (ProcedimentoCidDTO procedimentoCid : lista) {
				ps.setInt(1, idPacienteInstituicao);
				ps.setInt(2, procedimentoCid.getProcedimento().getIdProc());
				ps.setInt(3, procedimentoCid.getCid().getIdCid());
				ps.executeUpdate();
			}

		} catch (SQLException sqle) {
			conAuxiliar.rollback();
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			conAuxiliar.rollback();
			throw new ProjetoException(ex, this.getClass().getName());
		} 
	}
	
	public void excluirProcedimentosCidsDoPacienteInstituicao(Integer idPacienteInstituicao, 
			Connection conAuxiliar) throws ProjetoException, SQLException {

		String sql = "DELETE FROM hosp.paciente_instituicao_procedimento_cid " +
				"WHERE id_paciente_instituicao = ?;";

		try {
			PreparedStatement ps = conAuxiliar.prepareStatement(sql);

			ps.setInt(1, idPacienteInstituicao);
			ps.executeUpdate();

		} catch (SQLException sqle) {
			conAuxiliar.rollback();
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			conAuxiliar.rollback();
			throw new ProjetoException(ex, this.getClass().getName());
		} 
	}

}
