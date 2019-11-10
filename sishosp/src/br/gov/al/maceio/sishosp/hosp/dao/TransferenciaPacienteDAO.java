package br.gov.al.maceio.sishosp.hosp.dao;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.DataUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.model.AgendaBean;
import br.gov.al.maceio.sishosp.hosp.model.GerenciarPacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.HorarioAtendimento;
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

		String sql = "select pi.id, pi.codprograma, p.descprograma, pi.codgrupo, g.descgrupo,  pi.codequipe, e.descequipe, a.turno, a.situacao, "
				+ " pi.codprofissional, f.descfuncionario, pi.observacao, a.codtipoatendimento, t.desctipoatendimento, pi.codlaudo, pi.data_solicitacao "
				+ " from hosp.paciente_instituicao pi "
				+ " left join hosp.programa p on (p.id_programa = pi.codprograma) "
				+ " left join hosp.grupo g on (pi.codgrupo = g.id_grupo) "
				+ " left join hosp.equipe e on (pi.codequipe = e.id_equipe) "
				+ " left join hosp.atendimentos a on (a.id_paciente_instituicao = pi.id) "
				+ " left join hosp.tipoatendimento t on (a.codtipoatendimento = t.id) "
				+ " left join acl.funcionarios f on (pi.codprofissional = f.id_funcionario) " + " where pi.id = ?";

		List<GerenciarPacienteBean> lista = new ArrayList<>();
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
				ip.setTurno(rs.getString("turno"));
				ip.getLaudo().setId(rs.getInt("codlaudo"));

			}

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				conexao.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return ip;
	}

    public Boolean apagarAtendimentosNaRenovacao(Integer idPacienteInstituicao,java.util.Date dataTransferencia, Connection conAuxiliar) {

        Boolean retorno = false;
        ArrayList<Integer> lista = new ArrayList<Integer>();

        try {

            String sql = "SELECT DISTINCT a1.id_atendimento FROM hosp.atendimentos1 a1 " +
                    "LEFT JOIN hosp.atendimentos a ON (a.id_atendimento = a1.id_atendimento) " +
                    "WHERE a.id_paciente_instituicao = ? AND a.dtaatende >= ? AND  " +
                    "(SELECT count(*) FROM hosp.atendimentos1 aa1 WHERE aa1.id_atendimento = a1.id_atendimento) = " +
                    "(SELECT count(*) FROM hosp.atendimentos1 aaa1 WHERE aaa1.id_atendimento = a1.id_atendimento AND situacao IS NULL) " +
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
                String sql2 = "delete from hosp.atendimentos1 where id_atendimento = ?";

                PreparedStatement ps2 = null;
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

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return retorno;
    }
	
	
	public boolean gravarTransferenciaEquipeDiaHorario(InsercaoPacienteBean insercao, InsercaoPacienteBean insercaoParaLaudo,
			List<AgendaBean> listaAgendamento, Integer id_paciente,
			List<HorarioAtendimento> listaHorarioFinal) throws ProjetoException {

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
					+ "(SELECT count(*) FROM hosp.atendimentos1 aaa1 WHERE aaa1.id_atendimento = a1.id_atendimento AND situacao IS NULL) "
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

			String sql4 = "delete from hosp.atendimentos where id_atendimento in (SELECT DISTINCT a1.id_atendimento FROM hosp.atendimentos1 a1 "
					+ "                    LEFT JOIN hosp.atendimentos a ON (a.id_atendimento = a1.id_atendimento) "
					+ "                    WHERE a.id_paciente_instituicao = ? AND a.dtaatende >= ? AND  "
					+ "                    (SELECT count(*) FROM hosp.atendimentos1 aa1 WHERE aa1.id_atendimento = a1.id_atendimento) = "
					+ "                    (SELECT count(*) FROM hosp.atendimentos1 aaa1 WHERE aaa1.id_atendimento = a1.id_atendimento AND situacao IS NULL) "
					+ "                    ORDER BY a1.id_atendimento)";

			PreparedStatement ps4 = null;
			ps4 = conexao.prepareStatement(sql4);
			ps4.setLong(1, id_paciente);
			ps4.setDate(2, DataUtil.converterDateUtilParaDateSql(insercao.getDataSolicitacao()));
			ps4.execute();

			String sql5 = "update hosp.paciente_instituicao set status='CR' where id = ?";

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
			ps6.setString(3, "T");
			ps6.setLong(4, user_session.getId());

			ps6.executeUpdate();

			String sql7 = "insert into hosp.paciente_instituicao (codprograma, codgrupo, codpaciente, codequipe, status, codlaudo, observacao, cod_unidade) "
					+ " values (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id;";

			ps = conexao.prepareStatement(sql7);
			ps.setInt(1, insercao.getPrograma().getIdPrograma());
			ps.setInt(2, insercao.getGrupo().getIdGrupo());
			ps.setInt(3, insercao.getLaudo().getPaciente().getId_paciente());
			ps.setInt(4, insercao.getEquipe().getCodEquipe());
			ps.setString(5, "A");
			ps.setInt(6, insercao.getLaudo().getId());
			ps.setString(7, insercao.getObservacao());
			ps.setInt(8, user_session.getUnidade().getId());

			rs = ps.executeQuery();
			int idPacienteInstituicaoNovo = 0;
			if (rs.next()) {
				idPacienteInstituicaoNovo = rs.getInt("id");
			}

			String sql8 = "INSERT INTO hosp.profissional_dia_atendimento (id_paciente_instituicao, id_profissional, dia_semana) VALUES  (?, ?, ?)";
			PreparedStatement ps8 = null;
			ps8 = conexao.prepareStatement(sql8);

			for (int i = 0; i < listaHorarioFinal.size(); i++) {
				for (int j = 0; j < listaHorarioFinal.get(i).getListaFuncionarios().size(); j++) {
					ps.setLong(1, idPacienteInstituicaoNovo);
					ps.setLong(2, listaHorarioFinal.get(i).getListaFuncionarios().get(j).getId());
					ps.setInt(3, listaHorarioFinal.get(i).getDiaSemana());
					ps.executeUpdate();
				}
			}

			String sql9 = "INSERT INTO hosp.atendimentos(codpaciente,  situacao, dtaatende, codtipoatendimento, turno, "
					+ " observacao, ativo, id_paciente_instituicao, cod_unidade, horario, dtamarcacao, codprograma, codgrupo, codequipe, codatendente)"
					+ " VALUES (?, 'A', ?, ?, ?, ?, 'S', ?, ?, ?, CURRENT_TIMESTAMP, ?, ?, ?, ?) RETURNING id_atendimento;";

			PreparedStatement ps9 = null;
			ps9 = conexao.prepareStatement(sql9);

			for (int i = 0; i < listaAgendamento.size(); i++) {

				ps9.setInt(1, insercaoParaLaudo.getLaudo().getPaciente().getId_paciente());
				ps9.setDate(2, new Date(listaAgendamento.get(i).getDataMarcacao().getTime()));
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

				for (int h = 0; h < listaHorarioFinal.size(); h++) {
					for (int l = 0; l < listaHorarioFinal.get(h).getListaFuncionarios().size(); l++) {

						if (DataUtil.extrairDiaDeData(listaAgendamento.get(i)
								.getDataMarcacao()) == listaHorarioFinal.get(h).getDiaSemana()) {

							sql4 = "INSERT INTO hosp.atendimentos1 (codprofissionalatendimento, id_atendimento, cbo, codprocedimento) VALUES  (?, ?, ?, ?)";

							ps4 = null;
							ps4 = conexao.prepareStatement(sql4);

							ps4.setLong(1, listaHorarioFinal.get(h).getListaFuncionarios().get(l).getId());
							ps4.setInt(2, idAgend);
							if (VerificadorUtil.verificarSeObjetoNuloOuZero(
									listaHorarioFinal.get(h).getListaFuncionarios().get(l).getCbo().getCodCbo())) {
								ps4.setNull(3, Types.NULL);
							} else {
								ps4.setLong(3,
										listaHorarioFinal.get(h).getListaFuncionarios().get(l).getCbo().getCodCbo());
							}
							ps4.setInt(4,
									listaHorarioFinal.get(h).getListaFuncionarios().get(l).getProc1().getIdProc());

							ps4.executeUpdate();
						}
					}
				}

			}
			if (gerenciarPacienteDAO.gravarHistoricoAcaoPaciente(id_paciente, insercao.getObservacao(), "IT",
					conexao)) {
				conexao.commit();

				retorno = true;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				conexao.close();
			} catch (Exception ex) {
				ex.printStackTrace();
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

			String sql2 = "SELECT DISTINCT a1.id_atendimento FROM hosp.atendimentos1 a1 "
					+ "LEFT JOIN hosp.atendimentos a ON (a.id_atendimento = a1.id_atendimento) "
					+ "WHERE a.id_paciente_instituicao = ? AND a.dtaatende >= ? AND  "
					+ "(SELECT count(*) FROM hosp.atendimentos1 aa1 WHERE aa1.id_atendimento = a1.id_atendimento) = "
					+ "(SELECT count(*) FROM hosp.atendimentos1 aaa1 WHERE aaa1.id_atendimento = a1.id_atendimento AND situacao IS NULL) "
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
			
			for (int i = 0; i < lista.size(); i++) {
				String sql4 = "delete from hosp.atendimentos where id_atendimento = ?";

				PreparedStatement ps4 = null;
				ps4 = conexao.prepareStatement(sql4);
				ps4.setLong(1, lista.get(i));
				ps4.execute();
			}


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
			ps6.setString(3, "T");
			ps6.setLong(4, user_session.getId());

			ps6.executeUpdate();

			String sql7 = "insert into hosp.paciente_instituicao (codprograma, codgrupo, codequipe, status, codlaudo, observacao, cod_unidade, data_solicitacao, data_cadastro, turno) "
					+ " values (?, ?,  ?, ?, ?, ?, ?, ?, current_timestamp, ?) RETURNING id;";

			ps = conexao.prepareStatement(sql7);
			ps.setInt(1, insercao.getPrograma().getIdPrograma());
			ps.setInt(2, insercao.getGrupo().getIdGrupo());
			ps.setInt(3, insercao.getEquipe().getCodEquipe());
			ps.setString(4, "A");
			ps.setInt(5, insercao.getLaudo().getId());
			ps.setString(6, insercao.getObservacao());
			ps.setInt(7, user_session.getUnidade().getId());
			ps.setDate(8, new java.sql.Date(insercao.getDataSolicitacao().getTime()));
			ps.setString(9, insercao.getTurno());

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
				for (int j = 0; j < listaProfissionais.get(i).getListDiasSemana().size(); j++) {
					ps8.setInt(3, Integer.parseInt(listaProfissionais.get(i).getListDiasSemana().get(j)));
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
				ps9.setDate(2, new Date(listAgendamentoProfissional.get(i).getDataMarcacao().getTime()));
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

					for (int h = 0; h < listaProfissionais.get(j).getListDiasSemana().size(); h++) {

						if (DataUtil.extrairDiaDeData(
								listAgendamentoProfissional.get(i).getDataMarcacao()) == Integer
										.parseInt(listaProfissionais.get(j).getListDiasSemana().get(h))) {

							String sql10 = "INSERT INTO hosp.atendimentos1 (codprofissionalatendimento, id_atendimento, cbo, codprocedimento) VALUES  (?, ?, ?, ?)";

							PreparedStatement ps10 = null;
							ps10 = conexao.prepareStatement(sql10);

							ps10.setLong(1, listaProfissionais.get(j).getId());
							ps10.setInt(2, idAgend);
							if (VerificadorUtil.verificarSeObjetoNuloOuZero(
									listaProfissionais.get(j).getCbo().getCodCbo())) {
								ps10.setNull(3, Types.NULL);
							} else {
								ps10.setInt(3, listaProfissionais.get(j).getCbo().getCodCbo());
							}
							
							if (VerificadorUtil.verificarSeObjetoNuloOuZero(
									insercao.getPrograma().getProcedimento().getIdProc())) {
								ps10.setNull(4, Types.NULL);
							} else {
								ps10.setInt(4, insercao.getPrograma().getProcedimento().getIdProc());
							}
							
							

							ps10.executeUpdate();
						}
					}
				}

			}

			if (gerenciarPacienteDAO.gravarHistoricoAcaoPaciente(idPacienteInstituicaoNovo, insercao.getObservacao(), "IT",
					conexao)) {
				conexao.commit();

				retorno = true;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
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
