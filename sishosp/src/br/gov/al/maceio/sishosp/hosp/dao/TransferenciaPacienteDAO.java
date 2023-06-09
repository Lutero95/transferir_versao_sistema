package br.gov.al.maceio.sishosp.hosp.dao;

import br.gov.al.maceio.sishosp.acl.dao.FuncionarioDAO;
import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.administrativo.model.SubstituicaoProfissional;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.DataUtil;
import br.gov.al.maceio.sishosp.comum.util.TratamentoErrosUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.enums.TipoGravacaoHistoricoPaciente;
import br.gov.al.maceio.sishosp.hosp.model.AgendaBean;
import br.gov.al.maceio.sishosp.hosp.model.CboBean;
import br.gov.al.maceio.sishosp.hosp.model.InsercaoPacienteBean;
import javax.faces.context.FacesContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransferenciaPacienteDAO {

	PreparedStatement ps = null;
	private Connection conexao = null;

	FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
			.getSessionMap().get("obj_funcionario");

	public InsercaoPacienteBean carregarPacientesInstituicaoAlteracao(Integer id) throws ProjetoException {

		String sql = "select pi.id, pi.codprograma, p.descprograma, pi.codgrupo, g.descgrupo,  pi.codequipe, e.descequipe, pi.turno,  \n" + 
				" pi.codprofissional, f.descfuncionario, pi.observacao,  pi.codlaudo, pi.data_solicitacao ,\n" + 
				"  l.codpaciente codpaciente_laudo, pi.id_paciente codpaciente_instituicao, pacientes.nome \n" + 
				" from hosp.paciente_instituicao pi \n" + 
				" left join hosp.programa p on (p.id_programa = pi.codprograma) \n" + 
				" left join hosp.grupo g on (pi.codgrupo = g.id_grupo) \n" + 
				" left join hosp.equipe e on (pi.codequipe = e.id_equipe) \n" + 
				" left join acl.funcionarios f on (pi.codprofissional = f.id_funcionario) \n" + 
				"  LEFT JOIN hosp.laudo l ON (l.id_laudo = pi.codlaudo) \n" + 
				"  LEFT JOIN hosp.pacientes  ON (coalesce(l.codpaciente, pi.id_paciente) = pacientes.id_paciente)" + " where pi.id = ?";

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
				ip.getProgramaAtual().setIdPrograma(rs.getInt("codprograma"));
				ip.getProgramaAtual().setDescPrograma(rs.getString("descprograma"));
				ip.getGrupoAtual().setIdGrupo(rs.getInt("codgrupo"));
				ip.getGrupoAtual().setDescGrupo(rs.getString("descgrupo"));
				ip.getEquipeAtual().setCodEquipe(rs.getInt("codequipe"));
				ip.getEquipeAtual().setDescEquipe(rs.getString("descequipe"));
				ip.getFuncionario().setId(rs.getLong("codprofissional"));
				ip.getFuncionario().setNome(rs.getString("descfuncionario"));
				ip.setObservacao(rs.getString("observacao"));
				if ((rs.getString("codpaciente_laudo"))!=null) {
					ip.getLaudo().setId(rs.getInt("codlaudo"));
					ip.getLaudo().getPaciente().setId_paciente(rs.getInt("codpaciente_laudo"));
					ip.getLaudo().getPaciente().setNome(rs.getString("nome"));
				} else
				{
					ip.getPaciente().setId_paciente(rs.getInt("codpaciente_instituicao"));
					ip.getPaciente().setNome(rs.getString("nome"));	
				}

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
			}
		}
		return ip;
	}

    public Boolean apagarAtendimentosNaRenovacao
    	(Integer idPacienteInstituicao,java.util.Date dataTransferencia, Connection conAuxiliar)
    			throws ProjetoException, SQLException {

        Boolean retorno = false;
        ArrayList<Integer> lista = new ArrayList<Integer>();

        try {

            String sql = "SELECT DISTINCT a1.id_atendimento FROM hosp.atendimentos1 a1 " +
                    "LEFT JOIN hosp.atendimentos a ON (a.id_atendimento = a1.id_atendimento) " +
                    "WHERE a.id_paciente_instituicao = ? AND a.dtaatende >= ? AND coalesce(a.situacao,'A') <> 'C' AND coalesce(a1.excluido,'N')='N' and coalesce(a.presenca,'N') ='N' AND  " +
                    "(SELECT count(*) FROM hosp.atendimentos1 aa1 WHERE aa1.id_atendimento = a1.id_atendimento) = " +
                    "(SELECT count(*) FROM hosp.atendimentos1 aaa1 WHERE aaa1.id_atendimento = a1.id_atendimento AND aaa1.id_situacao_atendimento IS NULL) " +
                    "ORDER BY a1.id_atendimento;";


            ps = null;
            ps = conAuxiliar.prepareStatement(sql);
            ps.setLong(1, idPacienteInstituicao);
            ps.setDate(2, new java.sql.Date(dataTransferencia.getTime()));

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(rs.getInt("id_atendimento"));
            }

            for (int i = 0; i < lista.size(); i++) {
                String sql2 = "delete from adm.substituicao_funcionario where id_atendimentos1 in (select id_atendimentos1 from hosp.atendimentos1 where id_atendimento=?)";

                PreparedStatement ps2 = null;
                ps2 = conAuxiliar.prepareStatement(sql2);
                ps2.setLong(1, lista.get(i));
                ps2.execute();
                
                sql2 = "delete from adm.insercao_profissional_equipe_atendimento_1 where id_atendimentos1 in (select id_atendimentos1 from hosp.atendimentos1 where id_atendimento=?)";

                ps2 = null;
                ps2 = conAuxiliar.prepareStatement(sql2);
                ps2.setLong(1, lista.get(i));
                ps2.execute();                
                
                sql2 = "delete from logs.remocao_profissional_equipe_atendimentos1 where id_atendimentos1 in (select id_atendimentos1 from hosp.atendimentos1 where id_atendimento=?)";

                ps2 = null;
                ps2 = conAuxiliar.prepareStatement(sql2);
                ps2.setLong(1, lista.get(i));
                ps2.execute();
                
                sql2 = "delete from logs.substituicao_profissional_equipe_atendimentos1 where id_atendimentos1 in (select id_atendimentos1 from hosp.atendimentos1 where id_atendimento=?)";

                ps2 = null;
                ps2 = conAuxiliar.prepareStatement(sql2);
                ps2.setLong(1, lista.get(i));
                ps2.execute();  
                
                sql2 = "delete from adm.remocao_profissional_equipe_atendimento_1 where id_atendimentos1 in (select id_atendimentos1 from hosp.atendimentos1 where id_atendimento=?)";

                ps2 = null;
                ps2 = conAuxiliar.prepareStatement(sql2);
                ps2.setLong(1, lista.get(i));
                ps2.execute();                  
            	
            	
                sql2 = "delete from hosp.atendimentos1 where id_atendimento = ?";

                ps2 = null;
                ps2 = conAuxiliar.prepareStatement(sql2);
                ps2.setLong(1, lista.get(i));
                ps2.execute();
            }

            for (int i = 0; i < lista.size(); i++) {
                String sql3 = "delete from hosp.atendimentos where id_atendimento = ?";

                PreparedStatement ps3 = null;
                ps3 = conAuxiliar.prepareStatement(sql3);
                ps3.setLong(1, lista.get(i));
                ps3.execute();
            }

            retorno = true;

        } catch (SQLException sqle) {
        	conAuxiliar.rollback();
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			conAuxiliar.rollback();
			throw new ProjetoException(ex, this.getClass().getName());
		} 
        return retorno;
    }
	
	
	public boolean gravarTransferenciaEquipeDiaHorario(InsercaoPacienteBean insercao, InsercaoPacienteBean insercaoParaLaudo,
			List<AgendaBean> listaAgendamento, Integer id_paciente,
			List<FuncionarioBean> listaProfissionais) throws ProjetoException {

		Boolean retorno = false;
		ResultSet rs = null;
		ArrayList<Integer> lista = new ArrayList<Integer>();

		GerenciarPacienteDAO gerenciarPacienteDAO = new GerenciarPacienteDAO();

		try {
			conexao = ConnectionFactory.getConnection();

			String sql2 = "SELECT DISTINCT a1.id_atendimento FROM hosp.atendimentos1 a1 "
					+ "LEFT JOIN hosp.atendimentos a ON (a.id_atendimento = a1.id_atendimento) "
					+ "WHERE a.id_paciente_instituicao = ? AND a.dtaatende >= ? AND  "
					+ "(SELECT count(*) FROM hosp.atendimentos1 aa1 WHERE aa1.id_atendimento = a1.id_atendimento) = "
					+ "(SELECT count(*) FROM hosp.atendimentos1 aaa1 WHERE aaa1.id_atendimento = a1.id_atendimento AND aaa1.id_situacao_atendimento IS NULL) "
					+ "ORDER BY a1.id_atendimento;";

			PreparedStatement ps2 = null;
			ps2 = conexao.prepareStatement(sql2);
			ps2.setLong(1, id_paciente);
			ps2.setDate(2, DataUtil.converterDateUtilParaDateSql(insercao.getDataSolicitacao()));
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

			String sql4 = "delete from hosp.atendimentos a\n" + 
					" WHERE a.id_paciente_instituicao = ? AND a.dtaatende >= ? \n" + 
					" AND  (SELECT count(*) FROM hosp.atendimentos1 aa1 WHERE aa1.id_atendimento = a.id_atendimento) =\n" + 
					" (SELECT count(*) FROM hosp.atendimentos1 aaa1 WHERE aaa1.id_atendimento = a.id_atendimento AND aaa1.id_situacao_atendimento IS NULL) \n" +
					"";

			PreparedStatement ps4 = null;
			ps4 = conexao.prepareStatement(sql4);
			ps4.setLong(1, id_paciente);
			ps4.setDate(2, DataUtil.converterDateUtilParaDateSql(insercao.getDataSolicitacao()));
			ps4.execute();

			String sql5 = "update hosp.paciente_instituicao set status='CT' where id = ?";

			PreparedStatement ps5 = null;
			ps5 = conexao.prepareStatement(sql5);
			ps5.setLong(1, id_paciente);
			ps5.execute();

			String sql6 = "INSERT INTO hosp.historico_paciente_instituicao (codpaciente_instituicao, data_operacao, observacao, tipo, id_funcionario_gravacao) "
					+ " VALUES  (?, current_timestamp, ?, ?, ?)";

			PreparedStatement ps6 = null;
			ps6 = conexao.prepareStatement(sql6);

			ps6.setLong(1, id_paciente);
			ps6.setString(2, insercao.getObservacao());
			ps6.setString(3, TipoGravacaoHistoricoPaciente.TRANSFERENCIA.getSigla());
			ps6.setLong(4, user_session.getId());

			ps6.executeUpdate();

			String sql7 = "insert into hosp.paciente_instituicao (codprograma, codgrupo,  codequipe, status, codlaudo, observacao, cod_unidade, data_solicitacao) "
					+ " values (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id;";

			ps = conexao.prepareStatement(sql7);
			ps.setInt(1, insercao.getPrograma().getIdPrograma());
			ps.setInt(2, insercao.getGrupo().getIdGrupo());
			ps.setInt(3, insercao.getEquipe().getCodEquipe());
			ps.setString(4, "A");
			ps.setInt(5, insercao.getLaudo().getId());
			ps.setString(6, insercao.getObservacao());
			ps.setInt(7, user_session.getUnidade().getId());
			ps.setDate(8, new java.sql.Date(insercao.getDataSolicitacao().getTime()));

			rs = ps.executeQuery();
			int idPacienteInstituicaoNovo = 0;
			if (rs.next()) {
				idPacienteInstituicaoNovo = rs.getInt("id");
			}

			String sql8 = "INSERT INTO hosp.profissional_dia_atendimento (id_paciente_instituicao, id_profissional, dia_semana, horario_atendimento) VALUES  (?, ?, ?, ?)";
			PreparedStatement ps8 = null;
			ps8 = conexao.prepareStatement(sql8);
			
			for (int i = 0; i < listaProfissionais.size(); i++) {
				ps8.setLong(1, idPacienteInstituicaoNovo);
				ps8.setLong(2, listaProfissionais.get(i).getId());
				for (int j = 0; j < listaProfissionais.get(i).getListaDiasAtendimentoSemana().size(); j++) {
					ps8.setInt(3, listaProfissionais.get(i).getListaDiasAtendimentoSemana().get(j).getDiaSemana());
					ps8.setTime(4, DataUtil.retornarHorarioEmTime(listaProfissionais.get(i).getListaDiasAtendimentoSemana().get(j).getHorario()));
					ps8.executeUpdate();
				}
			}
			
			String sql9 = "INSERT INTO hosp.atendimentos(codpaciente,  situacao, dtaatende, codtipoatendimento, turno, "
					+ " observacao, ativo, id_paciente_instituicao, cod_unidade, horario, dtamarcacao, codprograma, codgrupo, codequipe, codatendente)"
					+ " VALUES (?, 'A', ?, ?, ?, ?, 'S', ?, ?, ?, CURRENT_TIMESTAMP, ?, ?, ?, ?) RETURNING id_atendimento;";

			PreparedStatement ps9 = null;
			ps9 = conexao.prepareStatement(sql9);

			for (int i = 0; i < listaAgendamento.size(); i++) {

				ps9.setInt(1, insercaoParaLaudo.getLaudo().getPaciente().getId_paciente());
				ps9.setDate(2, new Date(listaAgendamento.get(i).getDataAtendimento().getTime()));
				ps9.setInt(3, user_session.getUnidade().getParametro().getTipoAtendimento().getIdTipo());

				if (insercao.getTurno() != null) {
					ps9.setString(4, insercao.getTurno());
				} else {
					ps9.setNull(4, Types.NULL);
				}

				ps9.setString(5, insercao.getObservacao());
				ps9.setInt(6, idPacienteInstituicaoNovo);
				ps9.setInt(7, user_session.getUnidade().getId());

				if (insercao.getHorario() != null) {
					ps9.setTime(8, DataUtil.retornarHorarioEmTime(insercao.getHorario()));
				} else {
					ps9.setNull(8, Types.NULL);
				}

				ps9.setInt(9, insercao.getPrograma().getIdPrograma());

				ps9.setInt(10, insercao.getGrupo().getIdGrupo());

				ps9.setInt(11, insercao.getEquipe().getCodEquipe());

				ps9.setLong(12, user_session.getId());

				rs = ps9.executeQuery();

				int idAgend = 0;
				if (rs.next()) {
					idAgend = rs.getInt("id_atendimento");
				}

				for (int h = 0; h < listaProfissionais.size(); h++) {
					
					List<CboBean> listaCbosProfissional = 
							new FuncionarioDAO().listaCbosProfissionalComMesmaConexao(listaProfissionais.get(h).getId(), conexao);
					
					for (int l = 0; l < listaProfissionais.get(h).getListaDiasAtendimentoSemana().size(); l++) {

						if (DataUtil.extrairDiaDeData(listaAgendamento.get(i)
								.getDataAtendimento()) == listaProfissionais.get(h).getListaDiasAtendimentoSemana().get(l).getDiaSemana()) {

							sql4 = "INSERT INTO hosp.atendimentos1 (codprofissionalatendimento, id_atendimento, cbo, codprocedimento, horario_atendimento, id_cidprimario) VALUES  (?, ?, ?, ?, ?, ?)";

							if(gerenciarPacienteDAO.funcionarioEstaAfastadoDurantePeriodo(listaProfissionais.get(h), listaAgendamento.get(i).getDataAtendimento(), conexao, insercao.getTurno()))
			            		continue;
							
							Integer idProcedimentoEspecifico = new AgendaDAO().
									retornaIdProcedimentoEspecifico(insercao.getPrograma().getIdPrograma(), listaCbosProfissional, 
											insercaoParaLaudo.getLaudo().getPaciente().getId_paciente(), insercao.getGrupo().getIdGrupo(),
											insercao.getEquipe().getCodEquipe(), listaProfissionais.get(h).getId(), conexao);

							if(VerificadorUtil.verificarSeObjetoNuloOuZero(idProcedimentoEspecifico))
								idProcedimentoEspecifico = insercao.getPrograma().getProcedimento().getIdProc();

							ps4 = null;
							ps4 = conexao.prepareStatement(sql4);

							ps4.setLong(1, listaProfissionais.get(h).getId());
							ps4.setInt(2, idAgend);
							
							CboBean cboCompativel = new InsercaoPacienteDAO().retornaCboCompativelParaAgenda
									(insercao.getDataSolicitacao(), listaProfissionais.get(h), idProcedimentoEspecifico, conexao);
							
							ps4.setInt(3, cboCompativel.getCodCbo());
							
							if(!VerificadorUtil.verificarSeObjetoNuloOuZero(idProcedimentoEspecifico))
								ps4.setInt(4, idProcedimentoEspecifico);
							else if (!VerificadorUtil.verificarSeObjetoNuloOuZero(insercao.getPrograma().getProcedimento().getIdProc())) {
								ps4.setInt(4, insercao.getPrograma().getProcedimento().getIdProc());
							} else {
								ps4.setNull(4, Types.NULL);
							}
							
							if (VerificadorUtil.verificarSeObjetoNuloOuZero(
									listaProfissionais.get(h).getListaDiasAtendimentoSemana().get(l).getHorario())) {
								ps4.setNull(5, Types.NULL);
							} else {
								ps4.setTime(5,
										DataUtil.retornarHorarioEmTime(listaProfissionais.get(h).getListaDiasAtendimentoSemana().get(l).getHorario()));
							}
							
							if(VerificadorUtil.verificarSeObjetoNuloOuZero(insercaoParaLaudo.getLaudo().getId())) {
								ps4.setNull(6, Types.NULL);
							} else {
								ps4.setInt(6, insercaoParaLaudo.getLaudo().getCid1().getIdCid());
							}

							ps4.executeUpdate();
						}
					}
				}

			}
			if (gerenciarPacienteDAO.gravarHistoricoAcaoPaciente(id_paciente, insercao.getObservacao(), TipoGravacaoHistoricoPaciente.INSERCAO_VIA_TRANSFERENCIA.getSigla(),
					conexao)) {
				conexao.commit();
				retorno = true;
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
			}
		}
		return retorno;
	}

	public boolean gravarTransferenciaEquipeTurno(InsercaoPacienteBean insercao, InsercaoPacienteBean insercaoParaLaudo,
			List<AgendaBean> listAgendamentoProfissional, Integer id_paciente,
			List<FuncionarioBean> listaProfissionais) throws ProjetoException {

		Boolean retorno = false;
		ResultSet rs = null;
		ArrayList<Integer> lista = new ArrayList<Integer>();
		GerenciarPacienteDAO gerenciarPacienteDAO = new GerenciarPacienteDAO();

		try {
			conexao = ConnectionFactory.getConnection();
			
			ArrayList<SubstituicaoProfissional> listaSubstituicao =  gerenciarPacienteDAO.listaAtendimentosQueTiveramSubstituicaoProfissional(id_paciente, conexao) ;

			String sql2 = "SELECT DISTINCT a1.id_atendimento FROM hosp.atendimentos1 a1 "
					+ "LEFT JOIN hosp.atendimentos a ON (a.id_atendimento = a1.id_atendimento) "
					+ "WHERE a.id_paciente_instituicao = ? AND a.dtaatende >= ? AND  "
					+ "(SELECT count(*) FROM hosp.atendimentos1 aa1 WHERE aa1.id_atendimento = a1.id_atendimento) = "
					+ "(SELECT count(*) FROM hosp.atendimentos1 aaa1 WHERE aaa1.id_atendimento = a1.id_atendimento AND aaa1.id_situacao_atendimento IS NULL) "
					+ "ORDER BY a1.id_atendimento;";

			PreparedStatement ps2 = null;
			ps2 = conexao.prepareStatement(sql2);
			ps2.setLong(1, id_paciente);
			ps2.setDate(2, DataUtil.converterDateUtilParaDateSql(insercao.getDataSolicitacao()));
			rs = ps2.executeQuery();

			while (rs.next()) {
				lista.add(rs.getInt("id_atendimento"));
			}

            for (int i = 0; i < listaSubstituicao.size(); i++) {
                sql2 = "delete from adm.substituicao_funcionario where id_atendimentos1 = ?";

                ps2 = null;
                ps2 = conexao.prepareStatement(sql2);
                ps2.setLong(1, listaSubstituicao.get(i).getIdAtendimentos1());
                ps2.execute();
            }
			
			excluirAtendimentos1(lista, conexao);
			
			excluirAtendimentos(lista, conexao);

			String sql5 = "update hosp.paciente_instituicao set status='CT' where id = ?";

			PreparedStatement ps5 = null;
			ps5 = conexao.prepareStatement(sql5);
			ps5.setLong(1, id_paciente);
			ps5.execute();

			String sql6 = "INSERT INTO hosp.historico_paciente_instituicao (codpaciente_instituicao, data_operacao, observacao, tipo, id_funcionario_gravacao) "
					+ " VALUES  (?, current_timestamp, ?, ?, ?)";

			PreparedStatement ps6 = null;
			ps6 = conexao.prepareStatement(sql6);

			ps6.setLong(1, id_paciente);
			ps6.setString(2, insercao.getObservacao());
			ps6.setString(3, TipoGravacaoHistoricoPaciente.TRANSFERENCIA.getSigla());
			ps6.setLong(4, user_session.getId());

			ps6.executeUpdate();

			String sql7 = "insert into hosp.paciente_instituicao (codprograma, codgrupo, codequipe, status, codlaudo, observacao, cod_unidade, data_solicitacao, data_cadastro, turno, id_paciente) "
					+ " values (?, ?,  ?, ?, ?, ?, ?, ?, current_timestamp, ?, ?) RETURNING id;";

			ps = conexao.prepareStatement(sql7);
			ps.setInt(1, insercao.getPrograma().getIdPrograma());
			ps.setInt(2, insercao.getGrupo().getIdGrupo());
			ps.setInt(3, insercao.getEquipe().getCodEquipe());
			ps.setString(4, "A");
			if (insercao.getLaudo().getId() != null) {
				ps.setInt(5, insercao.getLaudo().getId());
			} else {
				ps.setNull(5, Types.NULL);
			}
			
			ps.setString(6, insercao.getObservacao());
			ps.setInt(7, user_session.getUnidade().getId());
			ps.setDate(8, new java.sql.Date(insercao.getDataSolicitacao().getTime()));
			if (insercao.getTurno() != null) {
				ps.setString(9, insercao.getTurno());
			} else {
				ps.setNull(9, Types.NULL);
			}
			
			if (insercao.getPaciente().getId_paciente() != null) {
				ps.setInt(10, insercao.getPaciente().getId_paciente());
			} else {
				ps.setNull(10, Types.NULL);
			}
			

			rs = ps.executeQuery();
			int idPacienteInstituicaoNovo = 0;
			if (rs.next()) {
				idPacienteInstituicaoNovo = rs.getInt("id");
			}

			String sql8 = "INSERT INTO hosp.profissional_dia_atendimento (id_paciente_instituicao, id_profissional, dia_semana) VALUES  (?, ?, ?)";
			PreparedStatement ps8 = null;
			ps8 = conexao.prepareStatement(sql8);

			for (int i = 0; i < listaProfissionais.size(); i++) {
				ps8.setLong(1, idPacienteInstituicaoNovo);
				ps8.setLong(2, listaProfissionais.get(i).getId());
				for (int j = 0; j < listaProfissionais.get(i).getListaDiasAtendimentoSemana().size(); j++) {
					ps8.setInt(3, listaProfissionais.get(i).getListaDiasAtendimentoSemana().get(j).getDiaSemana());
					ps8.executeUpdate();
				}
			}

			String sql9 = "INSERT INTO hosp.atendimentos(codpaciente,  situacao, dtaatende, codtipoatendimento, turno, "
					+ " observacao, ativo, id_paciente_instituicao, cod_unidade, horario, dtamarcacao, codprograma, codgrupo, codequipe, codatendente)"
					+ " VALUES (?, 'A', ?, ?, ?, ?, 'S', ?, ?, ?, CURRENT_TIMESTAMP, ?, ?, ?, ?) RETURNING id_atendimento;";

			PreparedStatement ps9 = null;
			ps9 = conexao.prepareStatement(sql9);

			for (int i = 0; i < listAgendamentoProfissional.size(); i++) {

				ps9.setInt(1, insercaoParaLaudo.getLaudo().getPaciente().getId_paciente());
				ps9.setDate(2, new Date(listAgendamentoProfissional.get(i).getDataAtendimento().getTime()));
				ps9.setInt(3, user_session.getUnidade().getParametro().getTipoAtendimento().getIdTipo());

				if (insercao.getTurno() != null) {
					ps9.setString(4, insercao.getTurno());
				} else {
					ps9.setNull(4, Types.NULL);
				}

				ps9.setString(5, insercao.getObservacao());
				ps9.setInt(6, idPacienteInstituicaoNovo);
				ps9.setInt(7, user_session.getUnidade().getId());

				if (insercao.getHorario() != null) {
					ps9.setTime(8, DataUtil.retornarHorarioEmTime(insercao.getHorario()));
				} else {
					ps9.setNull(8, Types.NULL);
				}

				ps9.setInt(9, insercao.getPrograma().getIdPrograma());

				ps9.setInt(10, insercao.getGrupo().getIdGrupo());

				ps9.setInt(11, insercao.getEquipe().getCodEquipe());

				ps9.setLong(12, user_session.getId());

				rs = ps9.executeQuery();

				int idAgend = 0;
				if (rs.next()) {
					idAgend = rs.getInt("id_atendimento");
				}

				for (int j = 0; j < listaProfissionais.size(); j++) {
					
					List<CboBean> listaCbosProfissional = 
							new FuncionarioDAO().listaCbosProfissionalComMesmaConexao(listaProfissionais.get(j).getId(), conexao);

					for (int h = 0; h < listaProfissionais.get(j).getListaDiasAtendimentoSemana().size(); h++) {

						if (DataUtil.extrairDiaDeData(
								listAgendamentoProfissional.get(i).getDataAtendimento()) == listaProfissionais.get(j).getListaDiasAtendimentoSemana().get(h).getDiaSemana()) {

							String sql10 = "INSERT INTO hosp.atendimentos1 (codprofissionalatendimento, id_atendimento, cbo, codprocedimento, id_cidprimario) VALUES  (?, ?, ?, ?, ?)";
							
							if(gerenciarPacienteDAO.funcionarioEstaAfastadoDurantePeriodo(listaProfissionais.get(j), listAgendamentoProfissional.get(i).getDataAtendimento(), conexao, insercao.getTurno()))
			            		continue;
							
							Integer idProcedimentoEspecifico = new AgendaDAO().
									retornaIdProcedimentoEspecifico(insercao.getPrograma().getIdPrograma(), listaCbosProfissional, 
											insercaoParaLaudo.getLaudo().getPaciente().getId_paciente(), insercao.getGrupo().getIdGrupo(),
											insercao.getEquipe().getCodEquipe(), listaProfissionais.get(j).getId(), conexao);

							if(VerificadorUtil.verificarSeObjetoNuloOuZero(idProcedimentoEspecifico))
								idProcedimentoEspecifico = insercao.getPrograma().getProcedimento().getIdProc();

							PreparedStatement ps10 = null;
							ps10 = conexao.prepareStatement(sql10);

							ps10.setLong(1, listaProfissionais.get(j).getId());
							ps10.setInt(2, idAgend);
							
							CboBean cboCompativel = new InsercaoPacienteDAO().retornaCboCompativelParaAgenda
									(insercao.getDataSolicitacao(), listaProfissionais.get(j), idProcedimentoEspecifico, conexao);
							
							ps10.setInt(3, cboCompativel.getCodCbo());
							
							if(!VerificadorUtil.verificarSeObjetoNuloOuZero(idProcedimentoEspecifico))
								ps10.setInt(4, idProcedimentoEspecifico);
							else if (VerificadorUtil.verificarSeObjetoNuloOuZero(insercao.getPrograma().getProcedimento().getIdProc())) {
								ps10.setNull(4, Types.NULL);
							} else {
								ps10.setInt(4, insercao.getPrograma().getProcedimento().getIdProc());
							}
							
							if(VerificadorUtil.verificarSeObjetoNuloOuZero(insercaoParaLaudo.getLaudo().getId())) {
								ps10.setNull(5, Types.NULL);
							} else {
								ps10.setInt(5, insercaoParaLaudo.getLaudo().getCid1().getIdCid());
							}

							ps10.executeUpdate();
						}
					}
				}

			}
			
			sql6 = "insert into adm.substituicao_funcionario (id_atendimentos1,id_afastamento_funcionario,\n" + 
					"id_funcionario_substituido, id_funcionario_substituto, usuario_acao, data_hora_acao)	\n" + 
					"values ((select id_atendimentos1 from hosp.atendimentos1\n" + 
					"a11 join hosp.atendimentos aa on aa.id_atendimento = a11.id_atendimento\n" + 
					"where aa.dtaatende=? and a11.codprofissionalatendimento=? limit 1),?,?,?,?, current_timestamp)";
			ps6 = null;
			ps6 = conexao.prepareStatement(sql6);
			for (int i = 0; i < listaSubstituicao.size(); i++) {
				sql8 = "update hosp.atendimentos1 set codprofissionalatendimento=?, cbo=? where atendimentos1.id_atendimentos1 = (\n" +
						"select distinct a1.id_atendimentos1 from hosp.paciente_instituicao pi\n" + 
						"join hosp.atendimentos a on a.id_paciente_instituicao = pi.id\n" + 
						"join hosp.atendimentos1 a1 on a1.id_atendimento = a.id_atendimento\n" + 
						"where pi.id=? and a.dtaatende=? and a1.codprofissionalatendimento = ? limit 1)";
				ps8 = null;
				ps8 = conexao.prepareStatement(sql8);
				ps8.setLong(1, listaSubstituicao.get(i).getFuncionario().getId());
				ps8.setLong(2, listaSubstituicao.get(i).getAtendimento().getCbo().getCodCbo());
				ps8.setLong(3, idPacienteInstituicaoNovo);
				ps8.setDate(4,new java.sql.Date( listaSubstituicao.get(i).getDataAtendimento().getTime()));
				ps8.setLong(5, listaSubstituicao.get(i).getAfastamentoProfissional().getFuncionario().getId());
				ps8.execute();
				
				ps6 = null;
				ps6 = conexao.prepareStatement(sql6);
				
				ps6.setDate(1,new java.sql.Date( listaSubstituicao.get(i).getDataAtendimento().getTime()));
				ps6.setLong(2, listaSubstituicao.get(i).getAfastamentoProfissional().getFuncionario().getId());
				ps6.setLong(3, listaSubstituicao.get(i).getAfastamentoProfissional().getId());
				ps6.setLong(4, listaSubstituicao.get(i).getAfastamentoProfissional().getFuncionario().getId());
				ps6.setLong(5, listaSubstituicao.get(i).getFuncionario().getId());
				ps6.setLong(6, listaSubstituicao.get(i).getUsuarioAcao().getId());
				ps6.execute();
			}

			if (gerenciarPacienteDAO.gravarHistoricoAcaoPaciente(idPacienteInstituicaoNovo, insercao.getObservacao(), TipoGravacaoHistoricoPaciente.INSERCAO_VIA_TRANSFERENCIA.getSigla(),
					conexao)) {
				conexao.commit();

				retorno = true;
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
			}
		}
		return retorno;
	}

	private void excluirAtendimentos1(ArrayList<Integer> lista, Connection conexaoAuxiliar) 
			throws SQLException, ProjetoException {
		
		try {
			for (int i = 0; i < lista.size(); i++) {
				String sql3 = "delete from hosp.atendimentos1 a1 where a1.id_atendimento = ? and \r\n" + 
						"	(a1.id_atendimentos1 not in (select rpa.id_atendimentos1 from logs.remocao_profissional_equipe_atendimentos1 rpa \r\n" + 
						"		where rpa.id_atendimentos1 = a1.id_atendimentos1) and \r\n" + 
						"	a1.id_atendimentos1 not in (select ipe.id_atendimentos1 from adm.insercao_profissional_equipe_atendimento_1 ipe \r\n" + 
						"		where ipe.id_atendimentos1 = a1.id_atendimentos1) ); ";

				PreparedStatement ps3 = null;
				ps3 = conexaoAuxiliar.prepareStatement(sql3);
				ps3.setLong(1, lista.get(i));
				ps3.executeUpdate();

				sql3 = "update hosp.atendimentos1 a1 set excluido = 'S', data_hora_exclusao = current_timestamp, \r\n" + 
						"	usuario_exclusao = ? where a1.id_atendimento = ? \r\n" + 
						"	and (a1.id_atendimentos1 = (select distinct rpa.id_atendimentos1 from logs.remocao_profissional_equipe_atendimentos1 rpa \r\n" + 
						"		where rpa.id_atendimentos1 = a1.id_atendimentos1) or\r\n" + 
						"	 a1.id_atendimentos1 = (select ipe.id_atendimentos1 from adm.insercao_profissional_equipe_atendimento_1 ipe \r\n" + 
						"		where ipe.id_atendimentos1 = a1.id_atendimentos1) ); ";

				ps3 = conexaoAuxiliar.prepareStatement(sql3);
				ps3.setLong(1, user_session.getId());
				ps3.setLong(2, lista.get(i));
				ps3.executeUpdate();
			}
		}  catch (Exception ex) {
			conexaoAuxiliar.rollback();
			throw new ProjetoException(ex, this.getClass().getName());
		}
	}
	
	private void excluirAtendimentos(ArrayList<Integer> lista, Connection conexaoAuxiliar)
			throws SQLException, ProjetoException {
		
		try {
			for (int i = 0; i < lista.size(); i++) {
				String sql4 = "delete from hosp.atendimentos a where a.id_atendimento = ? 	and \r\n" + 
						"	(a.id_atendimento not in (select a1.id_atendimento from hosp.atendimentos1 a1 \r\n" + 
						"		join logs.remocao_profissional_equipe_atendimentos1 rpa on a1.id_atendimentos1 = rpa.id_atendimentos1 \r\n" + 
						"		where a1.id_atendimento = ?) and \r\n" + 
						"	a.id_atendimento not in (select a1.id_atendimento from hosp.atendimentos1 a1 \r\n" + 
						"		join adm.insercao_profissional_equipe_atendimento_1 ipe on a1.id_atendimentos1 = ipe.id_atendimentos1 \r\n" + 
						"		where a1.id_atendimento = ?) );	";

				PreparedStatement ps4 = null;
				ps4 = conexaoAuxiliar.prepareStatement(sql4);
				ps4.setLong(1, lista.get(i));
				ps4.setLong(2, lista.get(i));
				ps4.setLong(3, lista.get(i));
				ps4.executeUpdate();

				sql4 = "update hosp.atendimentos a set situacao = 'C' where a.id_atendimento = ? and \r\n" + 
						"	(a.id_atendimento in (select a1.id_atendimento from hosp.atendimentos1 a1 \r\n" + 
						"	join logs.remocao_profissional_equipe_atendimentos1 rpa on a1.id_atendimentos1 = rpa.id_atendimentos1 \r\n" + 
						"	where a1.id_atendimento = ?) or\r\n" + 
						"	a.id_atendimento in (select a1.id_atendimento from hosp.atendimentos1 a1 \r\n" + 
						"	join adm.insercao_profissional_equipe_atendimento_1 ipe on a1.id_atendimentos1 = ipe.id_atendimentos1 \r\n" + 
						"	where a1.id_atendimento = ?));	";

				ps4 = conexaoAuxiliar.prepareStatement(sql4);
				ps4.setLong(1, lista.get(i));
				ps4.setLong(2, lista.get(i));
				ps4.setLong(3, lista.get(i));
				ps4.executeUpdate();
			}
		} catch (Exception ex) {
			conexaoAuxiliar.rollback();
			throw new ProjetoException(ex, this.getClass().getName());
		}
	}
	
	public Boolean verificarSePacienteAtivoEstaNoMesmoProgramaGrupo(InsercaoPacienteBean insercao) throws ProjetoException {

		Boolean retorno = true;

		String sql = "SELECT EXISTS ( SELECT pi.id FROM hosp.paciente_instituicao pi " + 
				"left join hosp.laudo l on l.id_laudo = pi.codlaudo " + 
				" WHERE pi.status = 'A' AND pi.codprograma = ? AND pi.codgrupo = ?" + 
				" and pi.id = ?) existe";

		Connection con = null;
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, insercao.getPrograma().getIdPrograma());
			ps.setInt(2, insercao.getGrupo().getIdGrupo());
			ps.setInt(3, insercao.getId());
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				retorno = rs.getBoolean("existe");
			}

		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				//comentado walter erro log ex.printStackTrace();
			}
		}
		return retorno;
	}
}
