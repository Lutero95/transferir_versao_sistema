package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.administrativo.model.InsercaoProfissionalEquipe;
import br.gov.al.maceio.sishosp.administrativo.model.RemocaoProfissionalEquipe;
import br.gov.al.maceio.sishosp.administrativo.model.SubstituicaoProfissional;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.*;
import br.gov.al.maceio.sishosp.hosp.model.AgendaBean;
import br.gov.al.maceio.sishosp.hosp.model.AtendimentoBean;
import br.gov.al.maceio.sishosp.hosp.model.GerenciarPacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.HorarioAtendimento;
import br.gov.al.maceio.sishosp.hosp.model.InsercaoPacienteBean;


public class AlteracaoPacienteDAO {

	PreparedStatement ps = null;
	private Connection conexao = null;

	FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
			.getSessionMap().get("obj_funcionario");

	public InsercaoPacienteBean carregarPacientesInstituicaoAlteracao(Integer id) throws ProjetoException {

		String sql = "select id, codprograma, descprograma, cod_procedimento, codgrupo, descgrupo, codpaciente_laudo,\n" +
				"codpaciente_instituicao, nome, codequipe, descequipe, turno, horario, mes_final, ano_final,\n" +
				"codprofissional,descfuncionario, observacao , codlaudo, data_solicitacao ,\n" +
				"codprocedimento_primario, codprocedimento_secundario1, \n" +
				" codprocedimento_secundario2, codprocedimento_secundario3, codprocedimento_secundario4, codprocedimento_secundario5,\n" +
				"(SELECT * FROM hosp.fn_GetLastDayOfMonth(to_date(ano_final||'-'||'0'||''||mes_final||'-'||'01', 'YYYY-MM-DD'))) as vigencia_final, id_cidprimario " +
				" from (\n" +
				"select pi.id, pi.codprograma, p.descprograma, p.cod_procedimento, pi.codgrupo, g.descgrupo, \n" +
				"l.codpaciente codpaciente_laudo, pi.id_paciente codpaciente_instituicao, pacientes.nome, \n" +
				" pi.codequipe, e.descequipe, pi.turno, pi.horario, \n" +
				"  coalesce(l.mes_final,extract (month from ( date_trunc('month',pi.data_solicitacao+ interval '2 months') + INTERVAL'1 month' - INTERVAL'1 day'))) mes_final, \n" +
				" coalesce(l.ano_final, extract (year from ( date_trunc('month',pi.data_solicitacao+ interval '2 months') + INTERVAL'1 month' - INTERVAL'1 day'))) ano_final,\n" +
				" pi.codprofissional, f.descfuncionario, pi.observacao, pi.codlaudo, pi.data_solicitacao, codprocedimento_primario, codprocedimento_secundario1, \n" +
				" codprocedimento_secundario2, codprocedimento_secundario3, codprocedimento_secundario4, codprocedimento_secundario5, l.cid1 id_cidprimario from hosp.paciente_instituicao pi \n" +
				" left join hosp.programa p on (p.id_programa = pi.codprograma) \n" +
				" left join hosp.grupo g on (pi.codgrupo = g.id_grupo) \n" +
				" left join hosp.equipe e on (pi.codequipe = e.id_equipe) \n" +
				" left join acl.funcionarios f on (pi.codprofissional = f.id_funcionario) \n" +
				" LEFT JOIN hosp.laudo l ON (l.id_laudo = pi.codlaudo) \n" +
				" LEFT JOIN hosp.pacientes  ON (coalesce(l.codpaciente, pi.id_paciente) = pacientes.id_paciente) \n" +
				" where pi.id = ?    \n" +
				" )a";

		InsercaoPacienteBean ip = new InsercaoPacienteBean();
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);

			stmt.setInt(1, id);

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				ip = new InsercaoPacienteBean();

				ip.setId(rs.getInt("id"));
				ip.getPrograma().setIdPrograma(rs.getInt("codprograma"));
				ip.getPrograma().setDescPrograma(rs.getString("descprograma"));
				ip.getGrupo().setIdGrupo(rs.getInt("codgrupo"));
				ip.getGrupo().setDescGrupo(rs.getString("descgrupo"));
				if ((rs.getString("codpaciente_laudo"))!=null) {
					ip.getLaudo().setId(rs.getInt("codlaudo"));
					ip.getLaudo().getPaciente().setId_paciente(rs.getInt("codpaciente_laudo"));
					ip.getLaudo().getPaciente().setNome(rs.getString("nome"));
					ip.getLaudo().getProcedimentoPrimario().setIdProc(rs.getInt("codprocedimento_primario"));
					ip.getLaudo().getProcedimentoSecundario1().setIdProc(rs.getInt("codprocedimento_secundario1"));
					ip.getLaudo().getProcedimentoSecundario2().setIdProc(rs.getInt("codprocedimento_secundario2"));
					ip.getLaudo().getProcedimentoSecundario3().setIdProc(rs.getInt("codprocedimento_secundario3"));
					ip.getLaudo().getProcedimentoSecundario4().setIdProc(rs.getInt("codprocedimento_secundario4"));
					ip.getLaudo().getProcedimentoSecundario5().setIdProc(rs.getInt("codprocedimento_secundario5"));
					ip.getLaudo().getCid1().setIdCid(rs.getInt("id_cidprimario"));
					ip.setInsercaoPacienteSemLaudo(false);
				} else
				{
					ip.getPaciente().setId_paciente(rs.getInt("codpaciente_instituicao"));
					ip.getPaciente().setNome(rs.getString("nome"));	
					ip.setInsercaoPacienteSemLaudo(true);
				}
					ip.getEquipe().setCodEquipe(rs.getInt("codequipe"));
				ip.getEquipe().setDescEquipe(rs.getString("descequipe"));
				ip.getFuncionario().setId(rs.getLong("codprofissional"));
				ip.getFuncionario().setNome(rs.getString("descfuncionario"));
				ip.setObservacao(rs.getString("observacao"));
				ip.setTurno(rs.getString("turno"));
			/*	if (user_session.getUnidade().getParametro().getOpcaoAtendimento().equals("H"))
					ip.setHorario(StringUtil.quebrarStringPorQuantidade(rs.getString("horario"),
							limitadorHorarioParaStringInicio, limitadorHorarioParaStringFinal));
			*/
				ip.getLaudo().setAnoFinal(rs.getInt("ano_final"));
				ip.getLaudo().setMesFinal(rs.getInt("mes_final"));
				ip.getLaudo().setVigenciaFinal(rs.getDate("vigencia_final"));
				ip.setDataSolicitacao(rs.getDate("data_solicitacao"));
				ip.getPrograma().getProcedimento().setIdProc(rs.getInt("cod_procedimento"));

			}

		} catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				conexao.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return ip;
	}

	public ArrayList<GerenciarPacienteBean> listarDiasAtendimentoProfissionalEquipe(Integer id)
			throws ProjetoException {

		ArrayList<GerenciarPacienteBean> lista = new ArrayList<>();

		String sql = "select distinct(p.id_profissional), f.descfuncionario, f.codcbo, p.id_paciente_instituicao, dia_semana, "
				+ " case when dia_semana = 1 then 'Domingo' when dia_semana = 2 then 'Segunda' "
				+ " when dia_semana = 3 then 'Terça' when dia_semana = 4 then 'Quarta' "
				+ " when dia_semana = 5 then 'Quinta' when dia_semana = 6 then 'Sexta' when dia_semana = 7 then 'Sábado' "
				+ " end as dia, to_char(p.horario_atendimento,'HH24:MI') horario_atendimento from hosp.profissional_dia_atendimento p "
				+ " left join acl.funcionarios f on (f.id_funcionario = p.id_profissional) "
				+ " where p.id_paciente_instituicao = ? " + " order by id_profissional";
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stm = conexao.prepareStatement(sql);

			stm.setInt(1, id);

			ResultSet rs = stm.executeQuery();
			AgendaDAO aDao = new AgendaDAO();
			while (rs.next()) {
				GerenciarPacienteBean ge = new GerenciarPacienteBean();
				ge.getFuncionario().setNome(rs.getString("descfuncionario"));
				ge.getFuncionario().setId(rs.getLong("id_profissional"));
				ge.setId(rs.getInt("id_paciente_instituicao"));
				ge.getFuncionario().setDiasSemana(rs.getString("dia")+ " "+rs.getString("horario_atendimento"));
				ge.getFuncionario().setDiaSemana(rs.getInt("dia_semana"));
				ge.getFuncionario().getCbo().setCodCbo(rs.getInt("codcbo"));
				ge.getFuncionario().setListaDiasAtendimentoSemana(aDao.listaDiasDeAtendimetoParaPacienteInstituicaoIhProfissional(
                        id, ge.getFuncionario().getId(), conexao));

				lista.add(ge);
			}
		} catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
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

	public ArrayList<HorarioAtendimento> listarDiasAtendimentoProfissional(Integer id) throws ProjetoException {

		ArrayList<HorarioAtendimento> lista = new ArrayList<>();

		String sql = "select dia_semana, horario_atendimento, id_profissional from hosp.profissional_dia_atendimento "
				+ " where id_paciente_instituicao = ? ";

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stm = conexao.prepareStatement(sql);

			stm.setInt(1, id);

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				HorarioAtendimento diaAtendimento = new HorarioAtendimento();
				diaAtendimento.setDiaSemana(rs.getInt("dia_semana"));
				diaAtendimento.setHorario(rs.getString("horario_atendimento"));
				diaAtendimento.getFuncionario().setId(rs.getLong("id_profissional"));
				lista.add(diaAtendimento);
			}
		} catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
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

	public boolean gravarAlteracaoEquipeTurno(InsercaoPacienteBean insercao,
			List<AgendaBean> listAgendamentoProfissional, Integer id_paciente,
			List<FuncionarioBean> listaProfissionais) throws ProjetoException {

		Boolean retorno = false;
		ResultSet rs = null;

		try {
			conexao = ConnectionFactory.getConnection();

			GerenciarPacienteDAO gerenciarPacienteDAO = new GerenciarPacienteDAO();
			
			ArrayList<AtendimentoBean> listaAtendimento1ComLiberacao = gerenciarPacienteDAO.listaAtendimentos1QueTiveramLiberacoes(id_paciente, conexao);
			
			ArrayList<SubstituicaoProfissional> listaSubstituicao =  gerenciarPacienteDAO.listaAtendimentosQueTiveramSubstituicaoProfissional(id_paciente, conexao) ;
			
			ArrayList<InsercaoProfissionalEquipe> listaProfissionaisInseridosAtendimentoEquipe =  gerenciarPacienteDAO.listaAtendimentosQueTiveramInsercaoProfissionalAtendimentoEquipePeloIdPacienteInstituicao(id_paciente, conexao) ;

			ArrayList<RemocaoProfissionalEquipe> listaProfissionaisRemovidosAtendimentoEquipe =  gerenciarPacienteDAO.listaAtendimentosQueTiveramRemocaoProfissionalAtendimentoEquipePeloIdPacienteInstituicao(id_paciente, conexao) ;			
			
		//	ArrayList<RemocaoProfissionalEquipe> listaProfissionaisRemovidosEquipe =  gerenciarPacienteDAO.listaAtendimentosQueTiveramRemocaoProfissionalEquipePeloIdPacienteInstituicao(id_paciente, conexao) ;
			
			
			if (!gerenciarPacienteDAO.apagarAtendimentos
					(id_paciente, conexao, true, listaSubstituicao, listaProfissionaisInseridosAtendimentoEquipe, listaProfissionaisRemovidosAtendimentoEquipe, listaAtendimento1ComLiberacao)) {

				conexao.close();

				return retorno;
			}
			
	        String sql = "update hosp.paciente_instituicao set data_solicitacao = ?, observacao=?, turno=?, codlaudo=? "
	                + " where id = ?";
	        
            PreparedStatement stmt = conexao.prepareStatement(sql);

            stmt.setDate(1, new java.sql.Date(insercao.getDataSolicitacao().getTime()));
            stmt.setString(2, insercao.getObservacao());
            stmt.setString(3, insercao.getTurno());
            if (insercao.getLaudo().getId()!=null) 
            	stmt.setInt(4,insercao.getLaudo().getId());
            else
            	stmt.setNull(4, Types.NULL);
            stmt.setInt(5, insercao.getId());
            stmt.executeUpdate();

				String sql6 = "INSERT INTO hosp.profissional_dia_atendimento (id_paciente_instituicao, id_profissional, dia_semana) VALUES  (?, ?, ?)";
				PreparedStatement ps6 = null;
				ps6 = conexao.prepareStatement(sql6);
	
				for (int i = 0; i < listaProfissionais.size(); i++) {
					ps6.setLong(1, insercao.getId());
					ps6.setLong(2, listaProfissionais.get(i).getId());
					for (int j = 0; j < listaProfissionais.get(i).getListaDiasAtendimentoSemana().size(); j++) {
						ps6.setInt(3, listaProfissionais.get(i).getListaDiasAtendimentoSemana().get(j).getDiaSemana());
						ps6.executeUpdate();
					}
				}

				String sql7 = "INSERT INTO hosp.atendimentos(codpaciente, codmedico, situacao, dtaatende, codtipoatendimento, turno, "
					+ " observacao, ativo, id_paciente_instituicao, cod_unidade, horario, dtamarcacao, codprograma, codgrupo, codequipe, codatendente)"
					+ " VALUES (?, ?, 'A', ?, ?, ?, ?, 'S', ?, ?, ?, CURRENT_TIMESTAMP, ?, ?, ?, ?) RETURNING id_atendimento;";

			PreparedStatement ps7 = null;
			ps7 = conexao.prepareStatement(sql7);
			
	    	Integer codPaciente = null;
	    	if ((insercao.getLaudo() != null) && (insercao.getLaudo().getId() != null)) 
	    			codPaciente = insercao.getLaudo().getPaciente().getId_paciente();
	    	
	    	if  ((insercao.getPaciente() != null) && (insercao.getPaciente().getId_paciente() != null))
	    		codPaciente = insercao.getPaciente().getId_paciente();

			for (int i = 0; i < listAgendamentoProfissional.size(); i++) {

				if (!verificarSeAtendimentoExistePorEquipe(insercao,
						listAgendamentoProfissional.get(i).getDataAtendimento(),
						codPaciente, conexao)) {

					ps7.setInt(1, codPaciente);
					ps7.setNull(2, Types.NULL);
					ps7.setDate(3, DataUtil.converterDateUtilParaDateSql(
							listAgendamentoProfissional.get(i).getDataAtendimento()));
					ps7.setInt(4, user_session.getUnidade().getParametro().getTipoAtendimento().getIdTipo());

					if (insercao.getTurno() != null) {
						ps7.setString(5, insercao.getTurno());
					} else {
						ps7.setNull(5, Types.NULL);
					}

					ps7.setString(6, insercao.getObservacao());
					ps7.setInt(7, id_paciente);
					ps7.setInt(8, user_session.getUnidade().getId());

					if (insercao.getHorario() != null) {
						ps7.setTime(9, DataUtil.retornarHorarioEmTime(insercao.getHorario()));
					} else {
						ps7.setNull(9, Types.NULL);
					}

					if (insercao.getPrograma().getIdPrograma() != null) {
						ps7.setInt(10, insercao.getPrograma().getIdPrograma());
					} else {
						ps7.setNull(10, Types.NULL);
					}

					if (insercao.getGrupo().getIdGrupo() != null) {
						ps7.setInt(11, insercao.getGrupo().getIdGrupo());
					} else {
						ps7.setNull(11, Types.NULL);
					}

					if (insercao.getEquipe().getCodEquipe() != null) {
						ps7.setInt(12, insercao.getEquipe().getCodEquipe());
					} else {
						ps7.setNull(12, Types.NULL);
					}

					ps7.setLong(13, user_session.getId());

					rs = ps7.executeQuery();

					int idAgend = 0;
					if (rs.next()) {
						idAgend = rs.getInt("id_atendimento");
					}

					for (int j = 0; j < listaProfissionais.size(); j++) {

						for (int h = 0; h < listaProfissionais.get(j).getListaDiasAtendimentoSemana().size(); h++) {

							if (DataUtil.extrairDiaDeData(
									listAgendamentoProfissional.get(i).getDataAtendimento()) == listaProfissionais.get(j).getListaDiasAtendimentoSemana().get(h).getDiaSemana()) {

								String sql8 = "INSERT INTO hosp.atendimentos1 (codprofissionalatendimento, id_atendimento, cbo, codprocedimento, id_cidprimario) VALUES  (?, ?, ?, ?, ?)";

								PreparedStatement ps8 = null;
								ps8 = conexao.prepareStatement(sql8);

								ps8.setLong(1, listaProfissionais.get(j).getId());
								ps8.setInt(2, idAgend);
								if ((listaProfissionais.get(j).getCbo().getCodCbo() != null)
										&& (listaProfissionais.get(j).getCbo().getCodCbo() != 0)) {
									ps8.setInt(3, listaProfissionais.get(j).getCbo().getCodCbo());
								} else {
									ps8.setNull(3, Types.NULL);
								}

								if ((insercao.getPrograma().getProcedimento().getIdProc() != null)
										&& (insercao.getPrograma().getProcedimento().getIdProc() != 0)) {
									ps8.setInt(4, insercao.getPrograma().getProcedimento().getIdProc());
								} else {
									ps8.setNull(4, Types.NULL);
								}

								if (VerificadorUtil.verificarSeObjetoNuloOuZero(insercao.getLaudo().getCid1().getIdCid())) {
									ps8.setNull(5, Types.NULL);
								} else {
									ps8.setInt(5, insercao.getLaudo().getCid1().getIdCid());
								}

								ps8.executeUpdate();
							}
						}
					}
				}
			}
			
			
			AtendimentoDAO aDAo = new AtendimentoDAO();
			sql6 = "insert into adm.substituicao_funcionario (id_atendimentos1,id_afastamento_funcionario,\n" + 
					"id_funcionario_substituido, id_funcionario_substituto, usuario_acao, data_hora_acao)	\n" + 
					"values ((select id_atendimentos1 from hosp.atendimentos1\n" + 
					"a11 join hosp.atendimentos aa on aa.id_atendimento = a11.id_atendimento\n" + 
					"where aa.dtaatende=? and a11.codprofissionalatendimento=? and aa.codpaciente  = ? limit 1),?,?,?,?, current_timestamp)";
			ps6 = null;
			ps6 = conexao.prepareStatement(sql6);
			for (int i = 0; i < listaSubstituicao.size(); i++) {
				String sql8 = "update hosp.atendimentos1 set codprofissionalatendimento=?, cbo=? where atendimentos1.id_atendimentos1 = (\n" +
						"select distinct a1.id_atendimentos1 from hosp.paciente_instituicao pi\n" + 
						"join hosp.atendimentos a on a.id_paciente_instituicao = pi.id\n" + 
						"join hosp.atendimentos1 a1 on a1.id_atendimento = a.id_atendimento\n" + 
						"where pi.id=? and a.dtaatende=? and a1.codprofissionalatendimento = ? and a.codpaciente  = ? limit 1)";
				PreparedStatement ps8 = null;
				ps8 = conexao.prepareStatement(sql8);
				ps8.setLong(1, listaSubstituicao.get(i).getFuncionario().getId());
				ps8.setLong(2, listaSubstituicao.get(i).getFuncionario().getCbo().getCodCbo());
				ps8.setLong(3, id_paciente);
				ps8.setDate(4,new java.sql.Date( listaSubstituicao.get(i).getDataAtendimento().getTime()));
				ps8.setLong(5, listaSubstituicao.get(i).getAfastamentoProfissional().getFuncionario().getId());
				ps8.setLong(6, listaSubstituicao.get(i).getAtendimento().getPaciente().getId_paciente());
				ps8.execute();
				
				ps6 = null;
				ps6 = conexao.prepareStatement(sql6);
				
				ps6.setDate(1,new java.sql.Date( listaSubstituicao.get(i).getDataAtendimento().getTime()));
				ps6.setLong(2, listaSubstituicao.get(i).getAfastamentoProfissional().getFuncionario().getId());
				ps6.setLong(3, listaSubstituicao.get(i).getAtendimento().getPaciente().getId_paciente());
				ps6.setLong(4, listaSubstituicao.get(i).getAfastamentoProfissional().getId());
				ps6.setLong(5, listaSubstituicao.get(i).getAfastamentoProfissional().getFuncionario().getId());
				ps6.setLong(6, listaSubstituicao.get(i).getFuncionario().getId());
				ps6.setLong(7, listaSubstituicao.get(i).getUsuarioAcao().getId());
				ps6.execute();
			}
			
			sql6 = "insert into adm.insercao_profissional_equipe_atendimento_1 (id_atendimentos1,id_insercao_profissional_equipe_atendimento, id_profissional) "+ 
					"values ((select id_atendimentos1 from hosp.atendimentos1\n" + 
					"a11 join hosp.atendimentos aa on aa.id_atendimento = a11.id_atendimento\n" + 
					"where aa.dtaatende=? and  aa.codprograma=? and aa.codgrupo=? and a11.codprofissionalatendimento =? and aa.codpaciente  = ? and coalesce(aa.situacao, 'A')<> 'C'	and coalesce(a11.excluido, 'N' )= 'N' limit 1),?,?)";
			ps6 = null;
			ps6 = conexao.prepareStatement(sql6);
			for (int i = 0; i < listaProfissionaisInseridosAtendimentoEquipe.size(); i++) {
				AtendimentoBean atendimento = new AtendimentoBean();
				atendimento.setDataAtendimentoInicio( listaProfissionaisInseridosAtendimentoEquipe.get(i).getDataAtendimento());
				atendimento.getPrograma().setIdPrograma(listaProfissionaisInseridosAtendimentoEquipe.get(i).getPrograma().getIdPrograma());
				atendimento.getGrupo().setIdGrupo(listaProfissionaisInseridosAtendimentoEquipe.get(i).getGrupo().getIdGrupo());
				atendimento.getEquipe().setCodEquipe(listaProfissionaisInseridosAtendimentoEquipe.get(i).getEquipe().getCodEquipe());
				atendimento.getFuncionario().setId(listaProfissionaisInseridosAtendimentoEquipe.get(i).getFuncionario().getId());
				if (aDAo.verificaSeExisteAtendimentoparaProfissionalNaDataNaEquipe(atendimento) ) {
				String sql8 = "INSERT INTO hosp.atendimentos1 " +
		                "(codprofissionalatendimento, id_atendimento, cbo, codprocedimento) " +
		                "VALUES (?, (select id_atendimento from hosp.atendimentos aa " + 
		                " where aa.dtaatende=? and  aa.codprograma=? and aa.codgrupo=? and aa.codpaciente  = ? and coalesce(aa.situacao, 'A')<> 'C'	  limit 1), ?, (select cod_procedimento from hosp.programa " + 
		                "where programa.id_programa = ? )) RETURNING id_atendimentos1";

				PreparedStatement ps8 = null;
				ps8 = conexao.prepareStatement(sql8);
				ps8.setLong(1, listaProfissionaisInseridosAtendimentoEquipe.get(i).getFuncionario().getId());
				ps8.setDate(2,new java.sql.Date( listaProfissionaisInseridosAtendimentoEquipe.get(i).getDataAtendimento().getTime()));
				ps8.setLong(3, listaProfissionaisInseridosAtendimentoEquipe.get(i).getPrograma().getIdPrograma());
				ps8.setLong(4, listaProfissionaisInseridosAtendimentoEquipe.get(i).getGrupo().getIdGrupo());
				ps8.setLong(5, listaProfissionaisInseridosAtendimentoEquipe.get(i).getAtendimentoBean().getPaciente().getId_paciente());
				ps8.setLong(6, listaProfissionaisInseridosAtendimentoEquipe.get(i).getFuncionario().getCbo().getCodCbo());
				ps8.setLong(7, listaProfissionaisInseridosAtendimentoEquipe.get(i).getPrograma().getIdPrograma());
				ps8.execute();
				
				ps6 = null;
				ps6 = conexao.prepareStatement(sql6);
				
				ps6.setDate(1,new java.sql.Date( listaProfissionaisInseridosAtendimentoEquipe.get(i).getDataAtendimento().getTime()));
				ps6.setLong(2, listaProfissionaisInseridosAtendimentoEquipe.get(i).getPrograma().getIdPrograma());
				ps6.setLong(3, listaProfissionaisInseridosAtendimentoEquipe.get(i).getGrupo().getIdGrupo());
				ps6.setLong(4, listaProfissionaisInseridosAtendimentoEquipe.get(i).getFuncionario().getId());
				ps6.setLong(5, listaProfissionaisInseridosAtendimentoEquipe.get(i).getAtendimentoBean().getPaciente().getId_paciente());
				ps6.setLong(6, listaProfissionaisInseridosAtendimentoEquipe.get(i).getId());
				ps6.setLong(7, listaProfissionaisInseridosAtendimentoEquipe.get(i).getFuncionario().getId());
				ps6.execute();
			}
			}
			
			
			if (listaProfissionaisRemovidosAtendimentoEquipe.size()>0) {		
				
				sql6 = "insert into adm.remocao_profissional_equipe_atendimento_1 (id_atendimentos1,id_remocao_profissional_equipe_atendimento, id_profissional) "+ 
						"values ((select id_atendimentos1 from hosp.atendimentos1\n" + 
						"a11 join hosp.atendimentos aa on aa.id_atendimento = a11.id_atendimento\n" + 
						"where aa.dtaatende=? and  aa.codprograma=? and aa.codgrupo=?  and a11.codprofissionalatendimento =? and aa.codpaciente  = ? and coalesce(aa.situacao, 'A')<> 'C'	and coalesce(a11.excluido, 'N' )= 'N'  limit 1),?,?)";
				ps6 = null;
				ps6 = null;
				ps6 = conexao.prepareStatement(sql6);
				for (int i = 0; i < listaProfissionaisRemovidosAtendimentoEquipe.size(); i++) {
					AtendimentoBean atendimento = new AtendimentoBean();
					atendimento.setDataAtendimentoInicio( listaProfissionaisRemovidosAtendimentoEquipe.get(i).getDataAtendimento());
					atendimento.getPrograma().setIdPrograma(listaProfissionaisRemovidosAtendimentoEquipe.get(i).getPrograma().getIdPrograma());
					atendimento.getGrupo().setIdGrupo(listaProfissionaisRemovidosAtendimentoEquipe.get(i).getGrupo().getIdGrupo());
					atendimento.getEquipe().setCodEquipe(listaProfissionaisRemovidosAtendimentoEquipe.get(i).getEquipe().getCodEquipe());
					atendimento.getFuncionario().setId(listaProfissionaisRemovidosAtendimentoEquipe.get(i).getFuncionario().getId());
					if (aDAo.verificaSeExisteAtendimentoparaProfissionalNaDataNaEquipe(atendimento) ) {					
					String sql8 = "update hosp.atendimentos1 set excluido='S', usuario_exclusao=?, data_hora_exclusao=current_timestamp where id_atendimentos1=(select id_atendimentos1 from hosp.atendimentos1 " + 
							"	a11 join hosp.atendimentos aa on aa.id_atendimento = a11.id_atendimento " + 
							"	 where aa.dtaatende=? and  aa.codprograma=? and aa.codgrupo=?  and a11.codprofissionalatendimento =? and aa.codpaciente  = ? and coalesce(aa.situacao, 'A')<> 'C'	and coalesce(a11.excluido, 'N' )= 'N'    limit 1)" ;
			               

					PreparedStatement ps8 = null;
					ps8 = conexao.prepareStatement(sql8);
					ps8.setLong(1, user_session.getId());
					ps8.setDate(2,new java.sql.Date( listaProfissionaisRemovidosAtendimentoEquipe.get(i).getDataAtendimento().getTime()));
					ps8.setLong(3, listaProfissionaisRemovidosAtendimentoEquipe.get(i).getPrograma().getIdPrograma());
					ps8.setLong(4, listaProfissionaisRemovidosAtendimentoEquipe.get(i).getGrupo().getIdGrupo());
					ps8.setLong(5, listaProfissionaisRemovidosAtendimentoEquipe.get(i).getFuncionario().getId());
					ps8.setLong(6, listaProfissionaisRemovidosAtendimentoEquipe.get(i).getAtendimentoBean().getPaciente().getId_paciente());
					ps8.execute();
					ps6 = conexao.prepareStatement(sql6);
					
					ps6.setDate(1,new java.sql.Date( listaProfissionaisRemovidosAtendimentoEquipe.get(i).getDataAtendimento().getTime()));
					ps6.setLong(2, listaProfissionaisRemovidosAtendimentoEquipe.get(i).getPrograma().getIdPrograma());
					ps6.setLong(3, listaProfissionaisRemovidosAtendimentoEquipe.get(i).getGrupo().getIdGrupo());
					ps6.setLong(4, listaProfissionaisRemovidosAtendimentoEquipe.get(i).getFuncionario().getId());
					ps6.setLong(5, listaProfissionaisRemovidosAtendimentoEquipe.get(i).getAtendimentoBean().getPaciente().getId_paciente());
					ps6.setLong(6, listaProfissionaisRemovidosAtendimentoEquipe.get(i).getId());
					ps6.setLong(7, listaProfissionaisRemovidosAtendimentoEquipe.get(i).getFuncionario().getId());
					ps6.execute();
				}
				}	
		}
		
			
			/*
			if (listaProfissionaisRemovidosEquipe.size()>0) {			
				sql6 = "insert into logs.remocao_profissional_equipe_atendimentos1 (id_atendimentos1,id_remocao_profissional_equipe, id_funcionario) "+ 
						"values ((select id_atendimentos1 from hosp.atendimentos1\n" + 
						"a11 join hosp.atendimentos aa on aa.id_atendimento = a11.id_atendimento\n" + 
						"where aa.dtaatende=? and  aa.codprograma=? and aa.codgrupo=?  and a11.codprofissionalatendimento =?  limit 1),?,?)";
				ps6 = null;
				ps6 = null;
				ps6 = conexao.prepareStatement(sql6);
				for (int i = 0; i < listaProfissionaisRemovidosEquipe.size(); i++) {
					
					String sql8 = "update hosp.atendimentos1 set excluido='S', usuario_exclusao=?, data_hora_exclusao=current_timestamp where id_atendimentos1=(select id_atendimentos1 from hosp.atendimentos1 " + 
							"	a11 join hosp.atendimentos aa on aa.id_atendimento = a11.id_atendimento " + 
							"	 where aa.dtaatende=? and  aa.codprograma=? and aa.codgrupo=?  and a11.codprofissionalatendimento =?  limit 1)" ;
			               

					PreparedStatement ps8 = null;
					ps8 = conexao.prepareStatement(sql8);
					ps8.setLong(1, user_session.getId());
					ps8.setDate(2,new java.sql.Date( listaProfissionaisRemovidosEquipe.get(i).getDataAtendimento().getTime()));
					ps8.setLong(3, listaProfissionaisRemovidosEquipe.get(i).getPrograma().getIdPrograma());
					ps8.setLong(4, listaProfissionaisRemovidosEquipe.get(i).getGrupo().getIdGrupo());
					ps8.setLong(5, listaProfissionaisRemovidosEquipe.get(i).getFuncionario().getId());
					ps8.execute();
					ps6 = conexao.prepareStatement(sql6);
					
					ps6.setDate(1,new java.sql.Date( listaProfissionaisRemovidosEquipe.get(i).getDataAtendimento().getTime()));
					ps6.setLong(2, listaProfissionaisRemovidosEquipe.get(i).getPrograma().getIdPrograma());
					ps6.setLong(3, listaProfissionaisRemovidosEquipe.get(i).getGrupo().getIdGrupo());
					ps6.setLong(4, listaProfissionaisRemovidosEquipe.get(i).getFuncionario().getId());
					ps6.setLong(5, listaProfissionaisRemovidosEquipe.get(i).getId());
					ps6.setLong(6, listaProfissionaisRemovidosEquipe.get(i).getFuncionario().getId());
					ps6.execute();
				}
				}		
			*/
			
				
			

			if (gerenciarPacienteDAO.gravarHistoricoAcaoPaciente(id_paciente, insercao.getObservacao(), "A", conexao)) {
				conexao.commit();

				retorno = true;
			}

		} catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
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

	public boolean gravarAlteracaoEquipeDiaHorario(InsercaoPacienteBean insercao,
			InsercaoPacienteBean insercaoParaLaudo, List<AgendaBean> listAgendamentoProfissional,
			Integer id_paciente, List<FuncionarioBean> listaProfissionais) throws ProjetoException {

		Boolean retorno = false;
		ResultSet rs = null;

		try {
			conexao = ConnectionFactory.getConnection();

			
			GerenciarPacienteDAO gerenciarPacienteDAO = new GerenciarPacienteDAO();
			
			ArrayList<AtendimentoBean> listaAtendimento1ComLiberacao = gerenciarPacienteDAO.listaAtendimentos1QueTiveramLiberacoes(id_paciente, conexao);

			ArrayList<SubstituicaoProfissional> listaSubstituicao =  gerenciarPacienteDAO.listaAtendimentosQueTiveramSubstituicaoProfissional(id_paciente, conexao) ;
			
			ArrayList<InsercaoProfissionalEquipe> listaProfissionaisInseridosAtendimentoEquipe =  gerenciarPacienteDAO.listaAtendimentosQueTiveramInsercaoProfissionalAtendimentoEquipePeloIdPacienteInstituicao(id_paciente, conexao) ;
			
			ArrayList<RemocaoProfissionalEquipe> listaProfissionaisRemovidosAtendimentoEquipe =  gerenciarPacienteDAO.listaAtendimentosQueTiveramRemocaoProfissionalAtendimentoEquipePeloIdPacienteInstituicao(id_paciente, conexao) ;
			
		//	ArrayList<RemocaoProfissionalEquipe> listaProfissionaisRemovidosEquipe =  gerenciarPacienteDAO.listaAtendimentosQueTiveramRemocaoProfissionalEquipePeloIdPacienteInstituicao(id_paciente, conexao) ;
			
			
			if (!gerenciarPacienteDAO.apagarAtendimentos
					(id_paciente, conexao, true, listaSubstituicao, listaProfissionaisInseridosAtendimentoEquipe, listaProfissionaisRemovidosAtendimentoEquipe, listaAtendimento1ComLiberacao)) {

				conexao.close();

				return retorno;
			}
			
		
			
			String sql = "update hosp.paciente_instituicao set data_solicitacao = ?, observacao=? "
	                + " where id = ?";
	        
            PreparedStatement stmt = conexao.prepareStatement(sql);

            stmt.setDate(1, new java.sql.Date(insercao.getDataSolicitacao().getTime()));
            stmt.setString(2, insercao.getObservacao());
            stmt.setInt(3, insercao.getId());
            stmt.executeUpdate();
			

			String sql6 = "INSERT INTO hosp.profissional_dia_atendimento (id_paciente_instituicao, id_profissional, dia_semana, horario_atendimento) VALUES  (?, ?, ?, ?)";
			PreparedStatement ps6 = null;
			ps6 = conexao.prepareStatement(sql6);

			for (int i = 0; i < listaProfissionais.size(); i++) {
				ps6.setLong(1, insercao.getId());
				ps6.setLong(2, listaProfissionais.get(i).getId());
				for (int j = 0; j < listaProfissionais.get(i).getListaDiasAtendimentoSemana().size(); j++) {
					ps6.setInt(3, listaProfissionais.get(i).getListaDiasAtendimentoSemana().get(j).getDiaSemana());
					ps6.setTime(4, DataUtil.retornarHorarioEmTime(listaProfissionais.get(i).getListaDiasAtendimentoSemana().get(j).getHorario()));
					ps6.executeUpdate();
				}
			}

			String sql7 = "INSERT INTO hosp.atendimentos(codpaciente, codmedico, situacao, dtaatende, codtipoatendimento, turno, "
					+ " observacao, ativo, id_paciente_instituicao, cod_unidade, horario, dtamarcacao, codprograma, codgrupo, codequipe, codatendente)"
					+ " VALUES (?, ?, 'A', ?, ?, ?, ?, 'S', ?, ?, ?, CURRENT_TIMESTAMP, ?, ?, ?, ?) RETURNING id_atendimento;";

			PreparedStatement ps7 = null;
			ps7 = conexao.prepareStatement(sql7);

			for (int i = 0; i < listAgendamentoProfissional.size(); i++) {

				if (!verificarSeAtendimentoExistePorEquipe(insercao,
						listAgendamentoProfissional.get(i).getDataAtendimento(),
						insercaoParaLaudo.getLaudo().getPaciente().getId_paciente(), conexao)) {

					ps7.setInt(1, insercaoParaLaudo.getLaudo().getPaciente().getId_paciente());
					ps7.setNull(2, Types.NULL);
					ps7.setDate(3, DataUtil.converterDateUtilParaDateSql(
							listAgendamentoProfissional.get(i).getDataAtendimento()));
					ps7.setInt(4, user_session.getUnidade().getParametro().getTipoAtendimento().getIdTipo());

					if (insercao.getTurno() != null) {
						ps7.setString(5, insercao.getTurno());
					} else {
						ps7.setNull(5, Types.NULL);
					}

					ps7.setString(6, insercao.getObservacao());
					ps7.setInt(7, id_paciente);
					ps7.setInt(8, user_session.getUnidade().getId());

					if (insercao.getHorario() != null) {
						ps7.setTime(9, DataUtil.retornarHorarioEmTime(insercao.getHorario()));
					} else {
						ps7.setNull(9, Types.NULL);
					}

					if (insercao.getPrograma().getIdPrograma() != null) {
						ps7.setInt(10, insercao.getPrograma().getIdPrograma());
					} else {
						ps7.setNull(10, Types.NULL);
					}

					if (insercao.getGrupo().getIdGrupo() != null) {
						ps7.setInt(11, insercao.getGrupo().getIdGrupo());
					} else {
						ps7.setNull(11, Types.NULL);
					}

					if (insercao.getEquipe().getCodEquipe() != null) {
						ps7.setInt(12, insercao.getEquipe().getCodEquipe());
					} else {
						ps7.setNull(12, Types.NULL);
					}

					ps7.setLong(13, user_session.getId());

					rs = ps7.executeQuery();

					int idAgend = 0;
					if (rs.next()) {
						idAgend = rs.getInt("id_atendimento");
					}

					for (int j = 0; j < listaProfissionais.size(); j++) {

						for (int h = 0; h < listaProfissionais.get(j).getListaDiasAtendimentoSemana().size(); h++) {
							if (DataUtil.extrairDiaDeData(
									listAgendamentoProfissional.get(i).getDataAtendimento()) == listaProfissionais.get(j).getListaDiasAtendimentoSemana().get(h).getDiaSemana()) {

								String sql8 = "INSERT INTO hosp.atendimentos1 (codprofissionalatendimento, id_atendimento, cbo, codprocedimento, horario_atendimento) VALUES  (?, ?, ?, ?, ?)";

								PreparedStatement ps8 = null;
								ps8 = conexao.prepareStatement(sql8);

								ps8.setLong(1, listaProfissionais.get(j).getId());
								ps8.setInt(2, idAgend);
								if ((listaProfissionais.get(j).getCbo().getCodCbo() != null)
										&& (listaProfissionais.get(j).getCbo().getCodCbo() != 0)) {
									ps8.setInt(3, listaProfissionais.get(j).getCbo().getCodCbo());
								} else {
									ps8.setNull(3, Types.NULL);
								}

								if ((insercao.getPrograma().getProcedimento().getIdProc() != null)
										&& (insercao.getPrograma().getProcedimento().getIdProc() != 0)) {
									ps8.setInt(4, insercao.getPrograma().getProcedimento().getIdProc());
								} else {
									ps8.setNull(4, Types.NULL);
								}
								
								if (VerificadorUtil.verificarSeObjetoNuloOuZero(
										listaProfissionais.get(j).getListaDiasAtendimentoSemana().get(h).getHorario())) {
									ps8.setNull(5, Types.NULL);
								} else {
									ps8.setTime(5,
											DataUtil.retornarHorarioEmTime(listaProfissionais.get(j).getListaDiasAtendimentoSemana().get(h).getHorario()));
								}


								ps8.executeUpdate();
							}
						}
					}
				}
			}
			
			AtendimentoDAO aDAo = new AtendimentoDAO();
			sql6 = "insert into adm.substituicao_funcionario (id_atendimentos1,id_afastamento_funcionario,\n" + 
					"id_funcionario_substituido, id_funcionario_substituto, usuario_acao, data_hora_acao)	\n" + 
					"values ((select id_atendimentos1 from hosp.atendimentos1\n" + 
					"a11 join hosp.atendimentos aa on aa.id_atendimento = a11.id_atendimento\n" + 
					"where aa.dtaatende=? and a11.codprofissionalatendimento=? and aa.codpaciente  = ? and coalesce(aa.situacao, 'A')<> 'C'	and coalesce(a11.excluido, 'N' )= 'N' limit 1),?,?,?,?, current_timestamp)";
			ps6 = null;
			ps6 = conexao.prepareStatement(sql6);
			for (int i = 0; i < listaSubstituicao.size(); i++) {
				String sql8 = "update hosp.atendimentos1 set codprofissionalatendimento=?, cbo=? where atendimentos1.id_atendimentos1 = (\n" +
						"select distinct a1.id_atendimentos1 from hosp.paciente_instituicao pi\n" + 
						"join hosp.atendimentos a on a.id_paciente_instituicao = pi.id\n" + 
						"join hosp.atendimentos1 a1 on a1.id_atendimento = a.id_atendimento\n" + 
						"where pi.id=? and a.dtaatende=? and a1.codprofissionalatendimento = ? and a.codpaciente  = ? limit 1)";
				PreparedStatement ps8 = null;
				ps8 = conexao.prepareStatement(sql8);
				ps8.setLong(1, listaSubstituicao.get(i).getFuncionario().getId());
				ps8.setLong(2, listaSubstituicao.get(i).getFuncionario().getCbo().getCodCbo());
				ps8.setLong(3, id_paciente);
				ps8.setDate(4,new java.sql.Date( listaSubstituicao.get(i).getDataAtendimento().getTime()));
				ps8.setLong(5, listaSubstituicao.get(i).getAfastamentoProfissional().getFuncionario().getId());
				ps8.setLong(6, listaSubstituicao.get(i).getAtendimento().getPaciente().getId_paciente());
				ps8.execute();
				
				ps6 = null;
				ps6 = conexao.prepareStatement(sql6);
				
				ps6.setDate(1,new java.sql.Date( listaSubstituicao.get(i).getDataAtendimento().getTime()));
				ps6.setLong(2, listaSubstituicao.get(i).getAfastamentoProfissional().getFuncionario().getId());
				ps6.setLong(3, listaSubstituicao.get(i).getAtendimento().getPaciente().getId_paciente());
				ps6.setLong(4, listaSubstituicao.get(i).getAfastamentoProfissional().getId());
				ps6.setLong(5, listaSubstituicao.get(i).getAfastamentoProfissional().getFuncionario().getId());
				ps6.setLong(6, listaSubstituicao.get(i).getFuncionario().getId());
				ps6.setLong(7, listaSubstituicao.get(i).getUsuarioAcao().getId());
				ps6.execute();
			}
			
			sql6 = "insert into adm.insercao_profissional_equipe_atendimento_1 (id_atendimentos1,id_insercao_profissional_equipe_atendimento, id_profissional) "+ 
					"values ((select id_atendimentos1 from hosp.atendimentos1\n" + 
					"a11 join hosp.atendimentos aa on aa.id_atendimento = a11.id_atendimento\n" + 
					"where aa.dtaatende=? and  aa.codprograma=? and aa.codgrupo=? and a11.codprofissionalatendimento =? and aa.codpaciente  = ?  and coalesce(aa.situacao, 'A')<> 'C'	and coalesce(a11.excluido, 'N' )= 'N' limit 1),?,?)";
			ps6 = null;
			ps6 = conexao.prepareStatement(sql6);
			for (int i = 0; i < listaProfissionaisInseridosAtendimentoEquipe.size(); i++) {
				String sql8 = "INSERT INTO hosp.atendimentos1 " +
		                "(codprofissionalatendimento, id_atendimento, cbo, codprocedimento) " +
		                "VALUES (?, (select id_atendimento from hosp.atendimentos aa " + 
		                " where aa.dtaatende=? and  aa.codprograma=? and aa.codgrupo=? and aa.codpaciente  = ? and coalesce(aa.situacao, 'A')<> 'C'  limit 1), ?, (select cod_procedimento from hosp.programa " + 
		                "where programa.id_programa = ? )) RETURNING id_atendimentos1";

				PreparedStatement ps8 = null;
				ps8 = conexao.prepareStatement(sql8);
				ps8.setLong(1, listaProfissionaisInseridosAtendimentoEquipe.get(i).getFuncionario().getId());
				ps8.setDate(2,new java.sql.Date( listaProfissionaisInseridosAtendimentoEquipe.get(i).getDataAtendimento().getTime()));
				ps8.setLong(3, listaProfissionaisInseridosAtendimentoEquipe.get(i).getPrograma().getIdPrograma());
				ps8.setLong(4, listaProfissionaisInseridosAtendimentoEquipe.get(i).getGrupo().getIdGrupo());
				ps8.setLong(5, listaProfissionaisInseridosAtendimentoEquipe.get(i).getAtendimentoBean().getPaciente().getId_paciente());
				if (VerificadorUtil.verificarSeObjetoNuloOuZero(listaProfissionaisInseridosAtendimentoEquipe.get(i).getFuncionario().getCbo().getCodCbo())) {
					ps8.setNull(6, Types.NULL);
				} else {
					ps8.setLong(6, listaProfissionaisInseridosAtendimentoEquipe.get(i).getFuncionario().getCbo().getCodCbo());
				}
				
				ps8.setLong(7, listaProfissionaisInseridosAtendimentoEquipe.get(i).getPrograma().getIdPrograma());
				ps8.execute();
				
				ps6 = null;
				ps6 = conexao.prepareStatement(sql6);
				
				ps6.setDate(1,new java.sql.Date( listaProfissionaisInseridosAtendimentoEquipe.get(i).getDataAtendimento().getTime()));
				ps6.setLong(2, listaProfissionaisInseridosAtendimentoEquipe.get(i).getPrograma().getIdPrograma());
				ps6.setLong(3, listaProfissionaisInseridosAtendimentoEquipe.get(i).getGrupo().getIdGrupo());
				ps6.setLong(4, listaProfissionaisInseridosAtendimentoEquipe.get(i).getFuncionario().getId());
				ps6.setLong(5, listaProfissionaisInseridosAtendimentoEquipe.get(i).getAtendimentoBean().getPaciente().getId_paciente());
				ps6.setLong(6, listaProfissionaisInseridosAtendimentoEquipe.get(i).getId());
				ps6.setLong(7, listaProfissionaisInseridosAtendimentoEquipe.get(i).getFuncionario().getId());
				ps6.execute();
			}
			
			
			if (listaProfissionaisRemovidosAtendimentoEquipe.size()>0) {		
				
				sql6 = "insert into adm.remocao_profissional_equipe_atendimento_1 (id_atendimentos1,id_remocao_profissional_equipe_atendimento, id_profissional) "+ 
						"values ((select id_atendimentos1 from hosp.atendimentos1\n" + 
						"a11 join hosp.atendimentos aa on aa.id_atendimento = a11.id_atendimento\n" + 
						"where aa.dtaatende=? and  aa.codprograma=? and aa.codgrupo=?  and a11.codprofissionalatendimento =? and aa.codpaciente  = ? and coalesce(aa.situacao, 'A')<> 'C'	and coalesce(a11.excluido, 'N' )= 'N'  limit 1),?,?)";
				ps6 = null;
				ps6 = null;
				ps6 = conexao.prepareStatement(sql6);
				for (int i = 0; i < listaProfissionaisRemovidosAtendimentoEquipe.size(); i++) {
					AtendimentoBean atendimento = new AtendimentoBean();
					atendimento.setDataAtendimentoInicio( listaProfissionaisRemovidosAtendimentoEquipe.get(i).getDataAtendimento());
					atendimento.getPrograma().setIdPrograma(listaProfissionaisRemovidosAtendimentoEquipe.get(i).getPrograma().getIdPrograma());
					atendimento.getGrupo().setIdGrupo(listaProfissionaisRemovidosAtendimentoEquipe.get(i).getGrupo().getIdGrupo());
					atendimento.getEquipe().setCodEquipe(listaProfissionaisRemovidosAtendimentoEquipe.get(i).getEquipe().getCodEquipe());
					atendimento.getFuncionario().setId(listaProfissionaisRemovidosAtendimentoEquipe.get(i).getFuncionario().getId());
					if (aDAo.verificaSeExisteAtendimentoparaProfissionalNaDataNaEquipe(atendimento) ) {					
					String sql8 = "update hosp.atendimentos1 set excluido='S', usuario_exclusao=?, data_hora_exclusao=current_timestamp where id_atendimentos1=(select id_atendimentos1 from hosp.atendimentos1 " + 
							"	a11 join hosp.atendimentos aa on aa.id_atendimento = a11.id_atendimento " + 
							"	 where aa.dtaatende=? and  aa.codprograma=? and aa.codgrupo=?  and a11.codprofissionalatendimento =? and aa.codpaciente  = ? and coalesce(aa.situacao, 'A')<> 'C'	and coalesce(a11.excluido, 'N' )= 'N'    limit 1)" ;
			               

					PreparedStatement ps8 = null;
					ps8 = conexao.prepareStatement(sql8);
					ps8.setLong(1, user_session.getId());
					ps8.setDate(2,new java.sql.Date( listaProfissionaisRemovidosAtendimentoEquipe.get(i).getDataAtendimento().getTime()));
					ps8.setLong(3, listaProfissionaisRemovidosAtendimentoEquipe.get(i).getPrograma().getIdPrograma());
					ps8.setLong(4, listaProfissionaisRemovidosAtendimentoEquipe.get(i).getGrupo().getIdGrupo());
					ps8.setLong(5, listaProfissionaisRemovidosAtendimentoEquipe.get(i).getFuncionario().getId());
					ps8.setLong(6, listaProfissionaisRemovidosAtendimentoEquipe.get(i).getAtendimentoBean().getPaciente().getId_paciente());
					ps8.execute();
					ps6 = conexao.prepareStatement(sql6);
					
					ps6.setDate(1,new java.sql.Date( listaProfissionaisRemovidosAtendimentoEquipe.get(i).getDataAtendimento().getTime()));
					ps6.setLong(2, listaProfissionaisRemovidosAtendimentoEquipe.get(i).getPrograma().getIdPrograma());
					ps6.setLong(3, listaProfissionaisRemovidosAtendimentoEquipe.get(i).getGrupo().getIdGrupo());
					ps6.setLong(4, listaProfissionaisRemovidosAtendimentoEquipe.get(i).getFuncionario().getId());
					ps6.setLong(5, listaProfissionaisRemovidosAtendimentoEquipe.get(i).getAtendimentoBean().getPaciente().getId_paciente());
					ps6.setLong(6, listaProfissionaisRemovidosAtendimentoEquipe.get(i).getId());
					ps6.setLong(7, listaProfissionaisRemovidosAtendimentoEquipe.get(i).getFuncionario().getId());
					ps6.execute();
				}
				}	
			}

			if (gerenciarPacienteDAO.gravarHistoricoAcaoPaciente(id_paciente, insercao.getObservacao(), "A", conexao)) {
				conexao.commit();

				retorno = true;
			}

		} catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
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
	
	public boolean gravarAlteracaoEquipeDiaHorarioDuplicado(InsercaoPacienteBean insercao,
			InsercaoPacienteBean insercaoParaLaudo, List<AgendaBean> listAgendamentoProfissional,
			Integer id_paciente, List<FuncionarioBean> listaProfissionais) throws ProjetoException {

		Boolean retorno = false;
		ResultSet rs = null;
		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().get("obj_funcionario");

		try {
			conexao = ConnectionFactory.getConnection();

			GerenciarPacienteDAO gerenciarPacienteDAO = new GerenciarPacienteDAO();
			/*
			if (!gerenciarPacienteDAO.apagarAtendimentos(id_paciente, conexao, true)) {

				conexao.close();

				return retorno;
			}
			*/

			String sql7 = "INSERT INTO hosp.atendimentos(codpaciente, codmedico, situacao, dtaatende, codtipoatendimento, turno, "
					+ " observacao, ativo, id_paciente_instituicao, cod_unidade, horario, dtamarcacao, codprograma, codgrupo, codequipe, codatendente)"
					+ " VALUES (?, ?, 'A', ?, ?, ?, ?, 'S', ?, ?, ?, CURRENT_TIMESTAMP, ?, ?, ?, ?) RETURNING id_atendimento;";

			PreparedStatement ps7 = null;
			ps7 = conexao.prepareStatement(sql7);

			for (int i = 0; i < listAgendamentoProfissional.size(); i++) {

				if (!verificarSeAtendimentoExistePorEquipe(insercao,
						listAgendamentoProfissional.get(i).getDataAtendimento(),
						insercaoParaLaudo.getLaudo().getPaciente().getId_paciente(), conexao)) {

					ps7.setInt(1, insercaoParaLaudo.getLaudo().getPaciente().getId_paciente());
					ps7.setNull(2, Types.NULL);
					ps7.setDate(3, DataUtil.converterDateUtilParaDateSql(
							listAgendamentoProfissional.get(i).getDataAtendimento()));
					ps7.setInt(4, user_session.getUnidade().getParametro().getTipoAtendimento().getIdTipo());

					if (insercao.getTurno() != null) {
						ps7.setString(5, insercao.getTurno());
					} else {
						ps7.setNull(5, Types.NULL);
					}

					ps7.setString(6, insercao.getObservacao());
					ps7.setInt(7, id_paciente);
					ps7.setInt(8, user_session.getUnidade().getId());

					if (insercao.getHorario() != null) {
						ps7.setTime(9, DataUtil.retornarHorarioEmTime(insercao.getHorario()));
					} else {
						ps7.setNull(9, Types.NULL);
					}

					if (insercao.getPrograma().getIdPrograma() != null) {
						ps7.setInt(10, insercao.getPrograma().getIdPrograma());
					} else {
						ps7.setNull(10, Types.NULL);
					}

					if (insercao.getGrupo().getIdGrupo() != null) {
						ps7.setInt(11, insercao.getGrupo().getIdGrupo());
					} else {
						ps7.setNull(11, Types.NULL);
					}

					if (insercao.getEquipe().getCodEquipe() != null) {
						ps7.setInt(12, insercao.getEquipe().getCodEquipe());
					} else {
						ps7.setNull(12, Types.NULL);
					}

					ps7.setLong(13, user_session.getId());

					rs = ps7.executeQuery();

					int idAgend = 0;
					if (rs.next()) {
						idAgend = rs.getInt("id_atendimento");
					}

					for (int j = 0; j < listaProfissionais.size(); j++) {

						for (int h = 0; h < listaProfissionais.get(j).getListaDiasAtendimentoSemana().size(); h++) {

							if (DataUtil.extrairDiaDeData(
									listAgendamentoProfissional.get(i).getDataAtendimento()) == listaProfissionais.get(j).getListaDiasAtendimentoSemana().get(h).getDiaSemana()) {

								String sql8 = "INSERT INTO hosp.atendimentos1 (codprofissionalatendimento, id_atendimento, cbo, codprocedimento) VALUES  (?, ?, ?, ?)";

								PreparedStatement ps8 = null;
								ps8 = conexao.prepareStatement(sql8);

								ps8.setLong(1, listaProfissionais.get(j).getId());
								ps8.setInt(2, idAgend);
								if ((listaProfissionais.get(j).getCbo().getCodCbo() != null)
										&& (listaProfissionais.get(j).getCbo().getCodCbo() != 0)) {
									ps8.setInt(3, listaProfissionais.get(j).getCbo().getCodCbo());
								} else {
									ps8.setNull(3, Types.NULL);
								}

								if ((insercao.getPrograma().getProcedimento().getIdProc() != null)
										&& (insercao.getPrograma().getProcedimento().getIdProc() != 0)) {
									ps8.setInt(4, insercao.getPrograma().getProcedimento().getIdProc());
								} else {
									ps8.setNull(4, Types.NULL);
								}

								ps8.executeUpdate();
							}
						}
					}
				}
			}

			
			if (gerenciarPacienteDAO.gravarHistoricoAcaoPaciente(id_paciente, insercao.getObservacao(), "A", conexao)) {
				conexao.commit();
				retorno = true;
			}

		} catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
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
	

	public boolean gravarAlteracaoProfissional(InsercaoPacienteBean insercao, InsercaoPacienteBean insercaoParaLaudo,
			ArrayList<AgendaBean> listaAgendamento, Integer id_paciente) throws ProjetoException {

		Boolean retorno = false;
		ResultSet rs = null;
		ArrayList<Integer> lista = new ArrayList<Integer>();

		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().get("obj_funcionario");

		GerenciarPacienteDAO gerenciarPacienteDAO = new GerenciarPacienteDAO();

		try {
			conexao = ConnectionFactory.getConnection();
			String sql2 = "select id_atendimento from hosp.atendimentos where situacao = 'A' and id_paciente_instituicao = ?";

			PreparedStatement ps2 = null;
			ps2 = conexao.prepareStatement(sql2);
			ps2.setLong(1, id_paciente);
			ps2.execute();

			rs = ps2.executeQuery();

			while (rs.next()) {
				lista.add(rs.getInt("id_atendimento"));
			}

			for (int i = 0; i < lista.size(); i++) {
				String sql3 = "delete from hosp.atendimentos1 where id_atendimento = ?";

				PreparedStatement ps3 = null;
				ps3 = conexao.prepareStatement(sql3);
				ps3.setLong(1, lista.get(i));
				ps3.execute();
			}

			String sql4 = "delete from hosp.atendimentos where situacao = 'A' and id_paciente_instituicao = ?";

			PreparedStatement ps4 = null;
			ps4 = conexao.prepareStatement(sql4);
			ps4.setLong(1, id_paciente);
			ps4.execute();

			String sql5 = "INSERT INTO hosp.profissional_dia_atendimento (id_paciente_instituicao, id_profissional, dia_semana) VALUES  (?, ?, ?)";
			PreparedStatement ps5 = null;
			ps5 = conexao.prepareStatement(sql5);

			for (int i = 0; i < insercao.getFuncionario().getListaDiasAtendimentoSemana().size(); i++) {
				ps5.setLong(1, insercao.getId());
				ps5.setLong(2, insercao.getFuncionario().getId());
				ps5.setInt(3, insercao.getFuncionario().getListaDiasAtendimentoSemana().get(i).getDiaSemana());
				ps5.setInt(4, user_session.getUnidade().getId());
				ps5.executeUpdate();

			}

				String sql6 = "INSERT INTO hosp.atendimentos(codpaciente, codmedico, situacao, dtaatende, codtipoatendimento, turno, "
						+ "observacao, ativo, id_paciente_instituicao, cod_unidade)"
						+ " VALUES (?, ?, 'A', ?, ?, ?, ?, 'S', ?, ?) RETURNING id_atendimento;";

				PreparedStatement ps6 = null;
				ps6 = conexao.prepareStatement(sql6);

				for (int i = 0; i < listaAgendamento.size(); i++) {

					ps6.setInt(1, insercaoParaLaudo.getLaudo().getPaciente().getId_paciente());
					ps6.setLong(2, insercao.getFuncionario().getId());
					ps6.setDate(3, new java.sql.Date(listaAgendamento.get(i).getDataAtendimento().getTime()));
					ps6.setInt(4, user_session.getUnidade().getParametro().getTipoAtendimento().getIdTipo());
					ps6.setString(5, insercao.getTurno());
					ps6.setString(6, insercao.getObservacao());
					ps6.setInt(7, id_paciente);
					ps6.setInt(8, user_session.getUnidade().getId());

					rs = ps6.executeQuery();

					int idAgend = 0;
					if (rs.next()) {
						idAgend = rs.getInt("id_atendimento");
					}

					String sql7 = "INSERT INTO hosp.atendimentos1 (codprofissionalatendimento, id_atendimento, cbo, codprocedimento) VALUES  (?, ?, ?, ?)";

					PreparedStatement ps7 = null;
					ps7 = conexao.prepareStatement(sql7);

					ps7.setLong(1, insercao.getFuncionario().getId());
					ps7.setInt(2, idAgend);
					ps7.setInt(3, insercao.getFuncionario().getCbo().getCodCbo());
					ps7.setInt(4, insercao.getFuncionario().getProc1().getIdProc());

					ps7.executeUpdate();

				}


			if (gerenciarPacienteDAO.gravarHistoricoAcaoPaciente(id_paciente, insercao.getObservacao(), "A", conexao)) {
				conexao.commit();
				retorno = true;
			}

		} catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
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

	public ArrayList<AgendaBean> listaAtendimentos(Integer id) throws ProjetoException {

		ArrayList<AgendaBean> lista = new ArrayList<>();

		String sql = "select id_atendimento, codpaciente, situacao, dtaatende from hosp.atendimentos "
				+ "where id_paciente_instituicao = ? and situacao = 'F' order by id_atendimento desc limit 1";
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stm = conexao.prepareStatement(sql);

			stm.setInt(1, id);

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				AgendaBean ge = new AgendaBean();
				ge.setIdAgenda(rs.getInt("id_atendimento"));
				ge.getPaciente().setId_paciente(rs.getInt("codpaciente"));
				ge.setSituacao(rs.getString("situacao"));
				ge.setDataAtendimento(rs.getDate("dtaatende"));

				lista.add(ge);
			}
		} catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
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

	public Boolean verificarSeAtendimentoExistePorEquipe(InsercaoPacienteBean insercaoPacienteBean, java.util.Date data,
			Integer codPaciente, Connection conAuxiliar) throws ProjetoException, SQLException {

		Boolean retorno = false;

		String sql = "SELECT id_atendimento FROM hosp.atendimentos WHERE codprograma = ? AND codgrupo = ? AND codequipe = ? AND dtaatende = ? and codpaciente=? and coalesce(situacao,'')<>'C'";

		try {
			PreparedStatement stm = conAuxiliar.prepareStatement(sql);

			stm.setInt(1, insercaoPacienteBean.getPrograma().getIdPrograma());
			stm.setInt(2, insercaoPacienteBean.getGrupo().getIdGrupo());
			stm.setInt(3, insercaoPacienteBean.getEquipe().getCodEquipe());
			stm.setDate(4, DataUtil.converterDateUtilParaDateSql(data));
			stm.setInt(5, codPaciente);

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				retorno = true;
			}

		} catch (SQLException ex2) {
			conAuxiliar.rollback();
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			conAuxiliar.rollback();
			throw new ProjetoException(ex, this.getClass().getName());
		} 
		return retorno;
	}

	public Boolean verificarSeAtendimentoExistePorProfissional(InsercaoPacienteBean insercaoPacienteBean, Date dataAtendimento,
			Connection conAuxiliar) throws ProjetoException, SQLException {

		Boolean retorno = false;

		String sql = "SELECT id_atendimento FROM hosp.atendimentos WHERE codprograma = ? AND codgrupo = ? AND codmedico = ? AND dtaatende = ? and coalesce(situacao, 'A')<> 'C'";

		try {
			PreparedStatement stm = conAuxiliar.prepareStatement(sql);

			stm.setInt(1, insercaoPacienteBean.getPrograma().getIdPrograma());
			stm.setInt(2, insercaoPacienteBean.getGrupo().getIdGrupo());
			stm.setLong(3, insercaoPacienteBean.getFuncionario().getId());
			stm.setDate(4, DataUtil.converterDateUtilParaDateSql(dataAtendimento));

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				retorno = true;
			}

		} catch (SQLException ex2) {
			conAuxiliar.rollback();
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			conAuxiliar.rollback();
			throw new ProjetoException(ex, this.getClass().getName());
		} 
		return retorno;
	}
	
	public boolean dataInclusaoPacienteEstaEntreDataInicialIhFinalDoLaudo(Integer idLaudo, java.util.Date dataInclusao) throws ProjetoException {
		
		boolean dataValida = false;
		String sql = "select exists ( " + 
				"	select l.data_solicitacao " + 
				"		from hosp.paciente_instituicao pi2 join hosp.laudo l on pi2.codlaudo = l.id_laudo " + 
				"		where l.id_laudo = ?" + 
				"		and (? between l.data_solicitacao and " + 
				"			(SELECT * FROM hosp.fn_GetLastDayOfMonth(to_date(l.ano_final ||'-'||'0'||''||l.mes_final ||'-'||'01', 'YYYY-MM-DD')) as data_final))) as valida";
		
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stm = conexao.prepareStatement(sql);

			stm.setInt(1, idLaudo);
			stm.setDate(2, new Date(dataInclusao.getTime()));
			ResultSet rs = stm.executeQuery();

			if (rs.next()) {
				dataValida = rs.getBoolean("valida");
			}

		} catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				conexao.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return dataValida;
	}	

}
