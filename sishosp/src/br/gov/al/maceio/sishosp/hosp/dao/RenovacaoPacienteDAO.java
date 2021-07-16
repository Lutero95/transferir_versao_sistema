package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.gov.al.maceio.sishosp.acl.dao.FuncionarioDAO;
import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.DataUtil;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.TratamentoErrosUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.enums.TipoGravacaoHistoricoPaciente;
import br.gov.al.maceio.sishosp.hosp.model.AgendaBean;
import br.gov.al.maceio.sishosp.hosp.model.CboBean;
import br.gov.al.maceio.sishosp.hosp.model.GerenciarPacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.InsercaoPacienteBean;

import javax.faces.context.FacesContext;

public class RenovacaoPacienteDAO {

	PreparedStatement ps = null;
	private Connection conexao = null;

	public InsercaoPacienteBean carregarPacientesInstituicaoRenovacao(Integer id) throws ProjetoException {

		String sql = "select pi.id, pi.codprograma, p.descprograma, p.cod_procedimento, pi.codgrupo, g.descgrupo, l.codpaciente, pacientes.nome nomepaciente,pi.codequipe, e.descequipe, pi.turno, "
				+ " pi.codprofissional, f.descfuncionario, pi.observacao "
				+ " from hosp.paciente_instituicao pi " + " left join hosp.laudo l on (l.id_laudo = pi.codlaudo) "
				+ " left join hosp.programa p on (p.id_programa = pi.codprograma) "
				+ " left join hosp.grupo g on (pi.codgrupo = g.id_grupo) "
				+ " left join hosp.equipe e on (pi.codequipe = e.id_equipe) "
				+ " LEFT JOIN hosp.pacientes  ON (coalesce(l.codpaciente, pi.id_paciente) = pacientes.id_paciente) \n"
				+ " left join acl.funcionarios f on (pi.codprofissional = f.id_funcionario) " + " where pi.id = ?";

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
				ip.getPrograma().getProcedimento().setIdProc(rs.getInt("cod_procedimento"));
				ip.getGrupo().setIdGrupo(rs.getInt("codgrupo"));
				ip.getGrupo().setDescGrupo(rs.getString("descgrupo"));
				ip.getLaudo().getPaciente().setId_paciente(rs.getInt("codpaciente"));
				ip.getLaudo().getPaciente().setNome(rs.getString("nomepaciente"));
				ip.getEquipe().setCodEquipe(rs.getInt("codequipe"));
				ip.getEquipe().setDescEquipe(rs.getString("descequipe"));
				ip.getFuncionario().setId(rs.getLong("codprofissional"));
				ip.getFuncionario().setNome(rs.getString("descfuncionario"));
				ip.setObservacao(rs.getString("observacao"));
				ip.setTurno(rs.getString("turno"));

			}

		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				conexao.close();
			} catch (Exception ex) {
				//comentado walter erro log //comentado walter erro log ex.printStackTrace();
			}
		}
		return ip;
	}

	public ArrayList<String> listarDiasAtendimentoProfissional(Integer id) throws ProjetoException {

		ArrayList<String> lista = new ArrayList<>();

		String sql = "select dia_semana from hosp.profissional_dia_atendimento "
				+ " where id_paciente_instituicao = ? ";

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stm = conexao.prepareStatement(sql);
			stm.setInt(1, id);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				lista.add(rs.getString("dia_semana"));
			}
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				conexao.close();
			} catch (Exception ex) {
				//comentado walter erro log //comentado walter erro log ex.printStackTrace();
			}
		}
		return lista;
	}

	public boolean gravarRenovacaoEquipeTurno(InsercaoPacienteBean insercao, InsercaoPacienteBean insercaoParaLaudo,
			ArrayList<FuncionarioBean> listaProfissionais,
			ArrayList<AgendaBean> listAgendamentoProfissional) throws ProjetoException {

		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().get("obj_funcionario");

		GerenciarPacienteDAO gerenciarPacienteDAO = new GerenciarPacienteDAO();
		FuncionarioDAO funcionarioDAO = new FuncionarioDAO();

		Boolean retorno = false;
		ResultSet rs = null;

		String sql1 = "update hosp.paciente_instituicao set status = 'CR' where id = ?";

		try {

			conexao = ConnectionFactory.getConnection();
			ps = conexao.prepareStatement(sql1);
			ps.setInt(1, insercao.getId());
			ps.executeUpdate();

			String sql2 = "insert into hosp.paciente_instituicao (codprograma, codgrupo,  codequipe, status, codlaudo, observacao, cod_unidade, data_solicitacao, data_cadastro, id_paciente, turno, "+
			" codprocedimento_primario_laudo_anterior, codprocedimento_secundario1_laudo_anterior, codprocedimento_secundario2_laudo_anterior, "+
			" codprocedimento_secundario3_laudo_anterior, codprocedimento_secundario4_laudo_anterior, "+
			" codprocedimento_secundario5_laudo_anterior) "
					+ " values (?, ?, ?, ?, ?, ?, ?, ?,  current_timestamp, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id;";
			PreparedStatement ps2 = null;
			ps2 = conexao.prepareStatement(sql2);
			ps2.setInt(1, insercao.getPrograma().getIdPrograma());
			ps2.setInt(2, insercao.getGrupo().getIdGrupo());
			ps2.setInt(3, insercao.getEquipe().getCodEquipe());
			ps2.setString(4, "A");
			if (!VerificadorUtil.verificarSeObjetoNuloOuZero(insercaoParaLaudo.getLaudo().getId())) {
				ps2.setInt(5, insercaoParaLaudo.getLaudo().getId());
			} else {
				ps2.setNull(5, Types.NULL);
			}
			ps2.setString(6, insercao.getObservacao());
			ps2.setInt(7, user_session.getUnidade().getId());
			ps2.setDate(8, new java.sql.Date(insercao.getDataSolicitacao().getTime()));
			if (!VerificadorUtil.verificarSeObjetoNulo(insercaoParaLaudo.getPaciente()) &&
					!VerificadorUtil.verificarSeObjetoNuloOuZero(insercaoParaLaudo.getPaciente().getId_paciente())) {
				ps2.setInt(9, insercaoParaLaudo.getPaciente().getId_paciente());
			} else {
				ps2.setNull(9, Types.NULL);
			}
			ps2.setString(10, insercao.getTurno());

			if (!VerificadorUtil.verificarSeObjetoNuloOuZero(insercaoParaLaudo.getProcedimentoPrimarioSemLaudo().getIdProc())) {
				ps2.setInt(11, insercaoParaLaudo.getProcedimentoPrimarioSemLaudo().getIdProc());
			} else {
				ps2.setNull(11, Types.NULL);
			}
			
			if (!VerificadorUtil.verificarSeObjetoNuloOuZero(insercaoParaLaudo.getProcedimentoSecundario1SemLaudo().getIdProc())) {
				ps2.setInt(12, insercaoParaLaudo.getProcedimentoSecundario1SemLaudo().getIdProc());
			} else {
				ps2.setNull(12, Types.NULL);
			}

			if (!VerificadorUtil.verificarSeObjetoNuloOuZero(insercaoParaLaudo.getProcedimentoSecundario2SemLaudo().getIdProc())) {
				ps2.setInt(13, insercaoParaLaudo.getProcedimentoSecundario2SemLaudo().getIdProc());
			} else {
				ps2.setNull(13, Types.NULL);
			}
			
			if (!VerificadorUtil.verificarSeObjetoNuloOuZero(insercaoParaLaudo.getProcedimentoSecundario3SemLaudo().getIdProc())) {
				ps2.setInt(14, insercaoParaLaudo.getProcedimentoSecundario3SemLaudo().getIdProc());
			} else {
				ps2.setNull(14, Types.NULL);
			}
			
			if (!VerificadorUtil.verificarSeObjetoNuloOuZero(insercaoParaLaudo.getProcedimentoSecundario4SemLaudo().getIdProc())) {
				ps2.setInt(15, insercaoParaLaudo.getProcedimentoSecundario4SemLaudo().getIdProc());
			} else {
				ps2.setNull(15, Types.NULL);
			}
			
			if (!VerificadorUtil.verificarSeObjetoNuloOuZero(insercaoParaLaudo.getProcedimentoSecundario5SemLaudo().getIdProc())) {
				ps2.setInt(16, insercaoParaLaudo.getProcedimentoSecundario5SemLaudo().getIdProc());
			} else {
				ps2.setNull(16, Types.NULL);
			}
			
			
			rs = ps2.executeQuery();
			int idPacienteInstituicao = 0;
			if (rs.next()) {
				idPacienteInstituicao = rs.getInt("id"); 
			}

			String sql8 = "INSERT INTO hosp.profissional_dia_atendimento (id_paciente_instituicao, id_profissional, dia_semana) VALUES  (?, ?, ?)";
			PreparedStatement ps8 = null;
			ps8 = conexao.prepareStatement(sql8);

			for (int i = 0; i < listaProfissionais.size(); i++) {
				ps8.setLong(1, idPacienteInstituicao);
				ps8.setLong(2, listaProfissionais.get(i).getId());
				for (int j = 0; j < listaProfissionais.get(i).getListaDiasAtendimentoSemana().size(); j++) {
					ps8.setInt(3,listaProfissionais.get(i).getListaDiasAtendimentoSemana().get(j).getDiaSemana());
					ps8.executeUpdate();
				}
			}

			String sql3 = "INSERT INTO hosp.atendimentos(codpaciente,  situacao, dtaatende, codtipoatendimento, turno, "
					+ " observacao, ativo, id_paciente_instituicao, cod_unidade, horario, dtamarcacao, codprograma, codgrupo, codequipe, codatendente)"
					+ " VALUES (?,  'A', ?, ?, ?, ?, 'S', ?, ?, ?, CURRENT_TIMESTAMP, ?, ?, ?, ?) RETURNING id_atendimento;";

			PreparedStatement ps3 = null;
			rs = null;
			ps3 = conexao.prepareStatement(sql3);
			
			Integer codPaciente;
			if (!VerificadorUtil.verificarSeObjetoNuloOuZero(insercaoParaLaudo.getLaudo().getPaciente().getId_paciente()))
				codPaciente = insercaoParaLaudo.getLaudo().getPaciente().getId_paciente();
			else
				codPaciente = insercaoParaLaudo.getPaciente().getId_paciente();

			for (int i = 0; i < listAgendamentoProfissional.size(); i++) {
				ps3.setInt(1, codPaciente);
				ps3.setDate(2, new java.sql.Date(listAgendamentoProfissional.get(i).getDataAtendimento().getTime()));
				ps3.setInt(3, user_session.getUnidade().getParametro().getTipoAtendimento().getIdTipo());

				if (insercao.getTurno() != null) {
					ps3.setString(4, insercao.getTurno());
				} else {
					ps3.setNull(4, Types.NULL);
				}

				ps3.setString(5, insercao.getObservacao());
				ps3.setInt(6, idPacienteInstituicao);
				ps3.setInt(7, user_session.getUnidade().getId());

				if (insercao.getHorario() != null) {
					ps3.setTime(8, DataUtil.retornarHorarioEmTime(insercao.getHorario()));
				} else {
					ps3.setNull(8, Types.NULL);
				}

				if (insercao.getPrograma().getIdPrograma() != null) {
					ps3.setInt(9, insercao.getPrograma().getIdPrograma());
				} else {
					ps3.setNull(9, Types.NULL);
				}

				if (insercao.getGrupo().getIdGrupo() != null) {
					ps3.setInt(10, insercao.getGrupo().getIdGrupo());
				} else {
					ps3.setNull(10, Types.NULL);
				}

				if (insercao.getEquipe().getCodEquipe() != null) {
					ps3.setInt(11, insercao.getEquipe().getCodEquipe());
				} else {
					ps3.setNull(11, Types.NULL);
				}

				ps3.setLong(12, user_session.getId());

				rs = ps3.executeQuery();

				int idAgend = 0;
				if (rs.next()) {
					idAgend = rs.getInt("id_atendimento");
				}

				for (int j = 0; j < listaProfissionais.size(); j++) {

					List<CboBean> listaCbosProfissional = 
							funcionarioDAO.listaCbosProfissionalComMesmaConexao(listaProfissionais.get(j).getId(), conexao);
					
					for (int h = 0; h < listaProfissionais.get(j).getListaDiasAtendimentoSemana().size(); h++) {

						if (DataUtil.extrairDiaDeData(
								listAgendamentoProfissional.get(i).getDataAtendimento()) == listaProfissionais.get(j).getListaDiasAtendimentoSemana().get(h).getDiaSemana()) {

							String sql4 = "INSERT INTO hosp.atendimentos1 (codprofissionalatendimento, id_atendimento, cbo, codprocedimento, id_cidprimario) VALUES  (?, ?, ?, ?, ?)";

							if(gerenciarPacienteDAO.funcionarioEstaAfastadoDurantePeriodo(listaProfissionais.get(j), listAgendamentoProfissional.get(i).getDataAtendimento(), conexao))
			            		return false;
							
							Integer idProcedimentoEspecifico = new AgendaDAO().
									retornaIdProcedimentoEspecifico(insercao.getPrograma().getIdPrograma(), listaCbosProfissional,
											codPaciente, insercao.getGrupo().getIdGrupo(), insercao.getEquipe().getCodEquipe(),
											listaProfissionais.get(j).getId(), conexao);
							
							if(VerificadorUtil.verificarSeObjetoNuloOuZero(idProcedimentoEspecifico))
								idProcedimentoEspecifico = insercao.getPrograma().getProcedimento().getIdProc();
							
							PreparedStatement ps4 = null;
							ps4 = conexao.prepareStatement(sql4);

							ps4.setLong(1, listaProfissionais.get(j).getId());
							ps4.setInt(2, idAgend);
							
							CboBean cboCompativel = retornaCboCompativelParaAgenda
									(insercao.getDataSolicitacao(), listaProfissionais.get(j), idProcedimentoEspecifico, insercao.getId(), conexao);
							
							ps4.setInt(3, cboCompativel.getCodCbo());

							if (!VerificadorUtil.verificarSeObjetoNuloOuZero(idProcedimentoEspecifico))
								ps4.setInt(4, idProcedimentoEspecifico);
							else
								ps4.setNull(4, Types.NULL);
							
							
							if(!insercaoParaLaudo.isInsercaoPacienteSemLaudo()) {
								ps4.setInt(5, insercaoParaLaudo.getLaudo().getCid1().getIdCid());
							} else {
								ps4.setNull(5, Types.NULL);
							}
							
							ps4.executeUpdate();
						}
					}
				}

			}
			
			if (gerenciarPacienteDAO.gravarHistoricoAcaoPaciente(idPacienteInstituicao, insercao.getObservacao(), TipoGravacaoHistoricoPaciente.RENOVACAO.getSigla(),
					conexao)) {
				conexao.commit();
				retorno = true;
			}

			retorno = true;
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				conexao.close();
			} catch (Exception ex) {
				//comentado walter erro log //comentado walter erro log ex.printStackTrace();
			}
		}
		return retorno;
	}

	public boolean gravarRenovacaoEquipeDiaHorario(InsercaoPacienteBean insercao,
			InsercaoPacienteBean insercaoParaLaudo, ArrayList<AgendaBean> listaAgendamento,
			List<FuncionarioBean> listaProfissionais) throws ProjetoException {

		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().get("obj_funcionario");

		GerenciarPacienteDAO gerenciarPacienteDAO = new GerenciarPacienteDAO();
		FuncionarioDAO funcionarioDAO = new FuncionarioDAO();

		Boolean retorno = false;
		ResultSet rs = null;

		String sql1 = "update hosp.paciente_instituicao set status = 'CR' where id = ?";

		try {

			conexao = ConnectionFactory.getConnection();
			ps = conexao.prepareStatement(sql1);
			ps.setInt(1, insercao.getId());
			ps.executeUpdate();

			String sql2 = "insert into hosp.paciente_instituicao (codprograma, codgrupo,  codequipe, status, codlaudo, observacao, cod_unidade, data_solicitacao, data_cadastro, id_paciente, turno, "+
					" codprocedimento_primario_laudo_anterior, codprocedimento_secundario1_laudo_anterior, codprocedimento_secundario2_laudo_anterior, "+
					" codprocedimento_secundario3_laudo_anterior, codprocedimento_secundario4_laudo_anterior, "+
					" codprocedimento_secundario5_laudo_anterior) "
					+ " values (?, ?, ?, ?, ?,  ?, ?, ?, current_timestamp, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id;";

			PreparedStatement ps2 = null;
			ps2 = conexao.prepareStatement(sql2);
			ps2.setInt(1, insercao.getPrograma().getIdPrograma());
			ps2.setInt(2, insercao.getGrupo().getIdGrupo());
			ps2.setInt(3, insercao.getEquipe().getCodEquipe());
			ps2.setString(4, "A");
			if (insercaoParaLaudo.getLaudo().getId() != null) {
				ps2.setInt(5, insercaoParaLaudo.getLaudo().getId());
			} else {
				ps2.setNull(5, Types.NULL);
			}
			ps2.setString(6, insercao.getObservacao());
			ps2.setInt(7, user_session.getUnidade().getId());
			ps2.setDate(8, new java.sql.Date(insercao.getDataSolicitacao().getTime()));

			if (insercaoParaLaudo.getPaciente().getId_paciente() != null) {
				ps2.setInt(9, insercaoParaLaudo.getPaciente().getId_paciente());
			} else {
				ps2.setNull(9, Types.NULL);
			}
			ps2.setString(10, insercao.getTurno());

			if ((insercaoParaLaudo.getProcedimentoPrimarioSemLaudo().getIdProc()!= null) && (insercaoParaLaudo.getProcedimentoPrimarioSemLaudo().getIdProc()!= 0)) {
				ps2.setInt(11, insercaoParaLaudo.getProcedimentoPrimarioSemLaudo().getIdProc());
			} else {
				ps2.setNull(11, Types.NULL);
			}

			if ((insercaoParaLaudo.getProcedimentoSecundario1SemLaudo().getIdProc()!= null) && (insercaoParaLaudo.getProcedimentoSecundario1SemLaudo().getIdProc()!= 0) ) {
				ps2.setInt(12, insercaoParaLaudo.getProcedimentoSecundario1SemLaudo().getIdProc());
			} else {
				ps2.setNull(12, Types.NULL);
			}

			if ((insercaoParaLaudo.getProcedimentoSecundario2SemLaudo().getIdProc() != null) && (insercaoParaLaudo.getProcedimentoSecundario2SemLaudo().getIdProc() != 0)) {
				ps2.setInt(13, insercaoParaLaudo.getProcedimentoSecundario2SemLaudo().getIdProc());
			} else {
				ps2.setNull(13, Types.NULL);
			}

			if ((insercaoParaLaudo.getProcedimentoSecundario3SemLaudo().getIdProc() != null) && (insercaoParaLaudo.getProcedimentoSecundario3SemLaudo().getIdProc() != 0)) {
				ps2.setInt(14, insercaoParaLaudo.getProcedimentoSecundario3SemLaudo().getIdProc());
			} else {
				ps2.setNull(14, Types.NULL);
			}

			if ((insercaoParaLaudo.getProcedimentoSecundario4SemLaudo().getIdProc() != null) && (insercaoParaLaudo.getProcedimentoSecundario4SemLaudo().getIdProc() != 0)) {
				ps2.setInt(15, insercaoParaLaudo.getProcedimentoSecundario4SemLaudo().getIdProc());
			} else {
				ps2.setNull(15, Types.NULL);
			}

			if ((insercaoParaLaudo.getProcedimentoSecundario5SemLaudo().getIdProc() != null) && (insercaoParaLaudo.getProcedimentoSecundario5SemLaudo().getIdProc() != 0)) {
				ps2.setInt(16, insercaoParaLaudo.getProcedimentoSecundario5SemLaudo().getIdProc());
			} else {
				ps2.setNull(16, Types.NULL);
			}

			rs = ps2.executeQuery();
			int idPacienteInstituicao = 0;
			if (rs.next()) {
				idPacienteInstituicao = rs.getInt("id");
			}

			String sql8 = "INSERT INTO hosp.profissional_dia_atendimento (id_paciente_instituicao, id_profissional, dia_semana, horario_atendimento) VALUES  (?, ?, ?,?)";
			PreparedStatement ps8 = null;
			ps8 = conexao.prepareStatement(sql8);
			
			for (int i = 0; i < listaProfissionais.size(); i++) {
				ps8.setLong(1, idPacienteInstituicao);
				ps8.setLong(2, listaProfissionais.get(i).getId());
				for (int j = 0; j < listaProfissionais.get(i).getListaDiasAtendimentoSemana().size(); j++) {
					ps8.setInt(3, listaProfissionais.get(i).getListaDiasAtendimentoSemana().get(j).getDiaSemana());
					ps8.setTime(4, DataUtil.retornarHorarioEmTime(listaProfissionais.get(i).getListaDiasAtendimentoSemana().get(j).getHorario()));
					ps8.executeUpdate();
				}
			}


			String sql3 = "INSERT INTO hosp.atendimentos(codpaciente,  situacao, dtaatende, codtipoatendimento, turno, "
					+ " observacao, ativo, id_paciente_instituicao, cod_unidade, horario, dtamarcacao, codprograma, codgrupo, codequipe, codatendente)"
					+ " VALUES (?,  'A', ?, ?, ?,?, 'S', ?, ?, ?, CURRENT_TIMESTAMP, ?, ?, ?, ?) RETURNING id_atendimento;";

			PreparedStatement ps3 = null;
			rs = null;
			ps3 = conexao.prepareStatement(sql3);

			Integer codPaciente;
			if (!VerificadorUtil.verificarSeObjetoNuloOuZero(insercaoParaLaudo.getLaudo().getPaciente().getId_paciente()))
				codPaciente = insercaoParaLaudo.getLaudo().getPaciente().getId_paciente();
			else
				codPaciente = insercaoParaLaudo.getPaciente().getId_paciente();
			
			for (int i = 0; i < listaAgendamento.size(); i++) {
				ps3.setInt(1, codPaciente);
				ps3.setDate(2, new java.sql.Date(listaAgendamento.get(i).getDataAtendimento().getTime()));
				ps3.setInt(3, user_session.getUnidade().getParametro().getTipoAtendimento().getIdTipo());

				if (insercao.getTurno() != null) {
					ps3.setString(4, insercao.getTurno());
				} else {
					ps3.setNull(4, Types.NULL);
				}

				ps3.setString(5, insercao.getObservacao());
				ps3.setInt(6, idPacienteInstituicao);
				ps3.setInt(7, user_session.getUnidade().getId());

				if (insercao.getHorario() != null) {
					ps3.setTime(8, DataUtil.retornarHorarioEmTime(insercao.getHorario()));
				} else {
					ps3.setNull(8, Types.NULL);
				}

				if (insercao.getPrograma().getIdPrograma() != null) {
					ps3.setInt(9, insercao.getPrograma().getIdPrograma());
				} else {
					ps3.setNull(9, Types.NULL);
				}

				if (insercao.getGrupo().getIdGrupo() != null) {
					ps3.setInt(10, insercao.getGrupo().getIdGrupo());
				} else {
					ps3.setNull(10, Types.NULL);
				}

				if (insercao.getEquipe().getCodEquipe() != null) {
					ps3.setInt(11, insercao.getEquipe().getCodEquipe());
				} else {
					ps3.setNull(11, Types.NULL);
				}

				ps3.setLong(12, user_session.getId());

				rs = ps3.executeQuery();

				int idAgend = 0;
				if (rs.next()) {
					idAgend = rs.getInt("id_atendimento");
				}

				for (int h = 0; h < listaProfissionais.size(); h++) {
					
					List<CboBean> listaCbosProfissional = 
							funcionarioDAO.listaCbosProfissionalComMesmaConexao(listaProfissionais.get(h).getId(), conexao);
					
					for (int l = 0; l < listaProfissionais.get(h).getListaDiasAtendimentoSemana().size(); l++) {

						if (DataUtil.extrairDiaDeData(listaAgendamento.get(i)
								.getDataAtendimento()) ==listaProfissionais.get(h).getListaDiasAtendimentoSemana().get(l).getDiaSemana()) {

							String sql4 = "INSERT INTO hosp.atendimentos1 (codprofissionalatendimento, id_atendimento, cbo, codprocedimento, horario_atendimento, id_cidprimario) VALUES  (?, ?, ?, ?, ?, ?)";

							if(gerenciarPacienteDAO.funcionarioEstaAfastadoDurantePeriodo(listaProfissionais.get(h), listaAgendamento.get(i).getDataAtendimento(), conexao))
			            		return false;
							
							Integer idProcedimentoEspecifico = new AgendaDAO().
									retornaIdProcedimentoEspecifico(insercao.getPrograma().getIdPrograma(), 
											listaCbosProfissional, codPaciente, insercao.getGrupo().getIdGrupo(), 
											insercao.getEquipe().getCodEquipe(), listaProfissionais.get(h).getId(), conexao);
							
							if(VerificadorUtil.verificarSeObjetoNuloOuZero(idProcedimentoEspecifico))
								idProcedimentoEspecifico = insercao.getPrograma().getProcedimento().getIdProc();
							
							PreparedStatement ps4 = null;
							ps4 = conexao.prepareStatement(sql4);

							ps4.setLong(1, listaProfissionais.get(h).getId());
							ps4.setInt(2, idAgend);
							
							CboBean cboCompativel = retornaCboCompativelParaAgenda
									(insercao.getDataSolicitacao(), listaProfissionais.get(h), idProcedimentoEspecifico, insercao.getId(), conexao);
							
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
							
							if(!insercaoParaLaudo.isInsercaoPacienteSemLaudo()) {
								ps4.setInt(6, insercaoParaLaudo.getLaudo().getCid1().getIdCid());
							} else {
								ps4.setNull(6, Types.NULL);
							}

							ps4.executeUpdate();
						}
					}
				}

			}

			if (gerenciarPacienteDAO.gravarHistoricoAcaoPaciente(idPacienteInstituicao, insercao.getObservacao(), TipoGravacaoHistoricoPaciente.RENOVACAO.getSigla(),
					conexao)) {
				conexao.commit();
				retorno = true;
			}

			retorno = true;
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				conexao.close();
			} catch (Exception ex) {
				//comentado walter erro log //comentado walter erro log ex.printStackTrace();
			}
		}
		return retorno;
	}

	public boolean gravarInsercaoProfissional(InsercaoPacienteBean insercao, InsercaoPacienteBean insercaoParaLaudo,
			ArrayList<AgendaBean> listaAgendamento) throws ProjetoException {

		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().get("obj_funcionario");

		GerenciarPacienteDAO gerenciarPacienteDAO = new GerenciarPacienteDAO();
		FuncionarioDAO funcionarioDAO = new FuncionarioDAO();

		Boolean retorno = false;
		ResultSet rs = null;

		String sql1 = "update hosp.paciente_instituicao set status = 'CR' where id = ?";
		try {
			conexao = ConnectionFactory.getConnection();
			ps = conexao.prepareStatement(sql1);
			ps.setInt(1, insercao.getId());

			ps.executeUpdate();

			String sql2 = "insert into hosp.paciente_instituicao (codprograma, codgrupo,  codequipe, status, codlaudo, observacao, cod_unidade, data_solicitacao, data_cadastro) "
					+ " values (?, ?, ?, ?, ?, ?, ?, ?,  current_timestamp) RETURNING id;";

			PreparedStatement ps2 = null;
			ps2 = conexao.prepareStatement(sql2);
			ps2.setInt(1, insercao.getPrograma().getIdPrograma());
			ps2.setInt(2, insercao.getGrupo().getIdGrupo());
			ps2.setInt(3, insercao.getEquipe().getCodEquipe());
			ps2.setString(4, "A");
			ps2.setInt(5, insercaoParaLaudo.getLaudo().getId());
			ps2.setString(6, insercao.getObservacao());
			ps2.setInt(7, user_session.getUnidade().getId());
			ps2.setDate(8, new java.sql.Date(insercao.getDataSolicitacao().getTime()));
			rs = ps2.executeQuery();
			int idPacienteInstituicao = 0;
			if (rs.next()) {
				idPacienteInstituicao = rs.getInt("id");
			}

			String sql3 = "INSERT INTO hosp.profissional_dia_atendimento (id_paciente_instituicao, id_profissional, dia_semana) VALUES  (?, ?, ?)";
			PreparedStatement ps3 = null;
			ps3 = conexao.prepareStatement(sql3);

			for (int i = 0; i < insercao.getFuncionario().getListaDiasAtendimentoSemana().size(); i++) {
				ps3.setLong(1, insercao.getId());
				ps3.setLong(2, insercao.getFuncionario().getId());
				ps3.setInt(3,insercao.getFuncionario().getListaDiasAtendimentoSemana().get(i).getDiaSemana());
				ps3.executeUpdate();

			}

			String sql4 = "INSERT INTO hosp.atendimentos(codpaciente, codmedico, situacao, dtaatende, codtipoatendimento, turno, observacao, ativo, "
					+ "id_paciente_instituicao, cod_unidade, horario, encaixe)"
					+ " VALUES (?, ?, 'A', ?, ?, ?, ?, 'S', ?, ?, ?, ?) RETURNING id_atendimento;";

			PreparedStatement ps4 = null;
			ps4 = conexao.prepareStatement(sql4);

			for (int i = 0; i < listaAgendamento.size(); i++) {

				ps4.setInt(1, insercao.getLaudo().getPaciente().getId_paciente());
				ps4.setLong(2, insercao.getFuncionario().getId());
				ps4.setDate(3,
						DataUtil.converterDateUtilParaDateSql(listaAgendamento.get(i).getDataAtendimento()));
				ps4.setInt(4, user_session.getUnidade().getParametro().getTipoAtendimento().getIdTipo());
				ps4.setString(5, insercao.getTurno());
				ps4.setString(6, insercao.getObservacao());
				ps4.setInt(7, idPacienteInstituicao);
				ps4.setInt(8, user_session.getUnidade().getId());
				if (insercao.getHorario() != null) {
					ps4.setTime(9, DataUtil.retornarHorarioEmTime(insercao.getHorario()));
				} else {
					ps4.setNull(9, Types.NULL);
				}

				ps4.setBoolean(10, false);

				rs = ps4.executeQuery();

				int idAgend = 0;
				if (rs.next()) {
					idAgend = rs.getInt("id_atendimento");
				}

				String sql5 = "INSERT INTO hosp.atendimentos1 (codprofissionalatendimento, id_atendimento, cbo, codprocedimento, id_cidprimario) VALUES  (?, ?, ?, ?, ?)";
				
				List<CboBean> listaCbosProfissional = 
						funcionarioDAO.listaCbosProfissionalComMesmaConexao(insercao.getFuncionario().getId(), conexao);
				
				if(gerenciarPacienteDAO.funcionarioEstaAfastadoDurantePeriodo(insercao.getFuncionario(), listaAgendamento.get(i).getDataAtendimento(), conexao))
            		return false;
				
				Integer idProcedimentoEspecifico = new AgendaDAO().
						retornaIdProcedimentoEspecifico(insercao.getPrograma().getIdPrograma(), listaCbosProfissional, 
								insercao.getLaudo().getPaciente().getId_paciente(), insercao.getGrupo().getIdGrupo(),
								insercao.getEquipe().getCodEquipe(), insercao.getFuncionario().getId(), conexao);
				
				if(VerificadorUtil.verificarSeObjetoNuloOuZero(idProcedimentoEspecifico))
					idProcedimentoEspecifico = insercao.getFuncionario().getProc1().getIdProc();
				
				PreparedStatement ps5 = null;
				ps5 = conexao.prepareStatement(sql5);

				ps5.setLong(1, insercao.getFuncionario().getId());
				ps5.setInt(2, idAgend);
				
				CboBean cboCompativel = retornaCboCompativelParaAgenda
						(insercao.getDataSolicitacao(), insercao.getFuncionario(), idProcedimentoEspecifico, insercao.getId(), conexao);
				
				ps4.setInt(3, cboCompativel.getCodCbo());
				ps5.setInt(4, idProcedimentoEspecifico);
				
				if(!insercaoParaLaudo.isInsercaoPacienteSemLaudo()) {
					ps4.setInt(5, insercaoParaLaudo.getLaudo().getCid1().getIdCid());
				} else {
					ps4.setNull(5, Types.NULL);
				}
				ps5.executeUpdate();
			}

			if (gerenciarPacienteDAO.gravarHistoricoAcaoPaciente(idPacienteInstituicao, insercao.getObservacao(), TipoGravacaoHistoricoPaciente.RENOVACAO.getSigla(),
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
				//comentado walter erro log //comentado walter erro log ex.printStackTrace();
			}
		}
		return retorno;
	}
	
	private CboBean retornaCboCompativelParaAgenda(Date dataSolicitacao, FuncionarioBean profissional,
			Integer idProcedimentoEspecifico, Integer idPacienteInstituicao, Connection conexao) throws ProjetoException, SQLException {
		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().get("obj_funcionario");
		
		CboBean cboCompativel;
		if(user_session.getUnidade().getParametro().isValidaDadosLaudoSigtap()) {
			Date data = new InsercaoPacienteDAO().retornaDataSolicitacaoParaSigtap(dataSolicitacao);					            
			cboCompativel = new FuncionarioDAO().buscaCboCompativelComProcedimento(data, idProcedimentoEspecifico, 
					profissional.getId(), conexao);
			
			if(VerificadorUtil.verificarSeObjetoNuloOuZero(cboCompativel.getCodCbo())) {
				data = retornaUltimaDataSolicitacao(idPacienteInstituicao, conexao);
				cboCompativel = new FuncionarioDAO().buscaCboCompativelComProcedimento(data, idProcedimentoEspecifico, 
						profissional.getId(), conexao);	
			}
			
			if(VerificadorUtil.verificarSeObjetoNuloOuZero(cboCompativel.getCodCbo())) {
				throw new ProjetoException
					("CBO do profissional "+profissional.getNome()+
							" não é compatível com este procedimento ou com o especifíco do programa!");
			}
		} else {
			cboCompativel = new FuncionarioDAO().retornaPrimeiroCboProfissional(profissional.getId());
		}
		return cboCompativel;
	}
	
	public Date retornaUltimaDataSolicitacao(Integer idPacienteInstituicao, Connection conexaoAuxiliar) 
			throws ProjetoException, SQLException {
		
		String sql = "SELECT data_solicitacao FROM hosp.paciente_instituicao where id = ?;"; 
		Date dataSolicitacao = null;
		
		try {
			PreparedStatement stm = conexaoAuxiliar.prepareStatement(sql);
			stm.setInt(1, idPacienteInstituicao);
			ResultSet rs = stm.executeQuery();

			if (rs.next()) {
				dataSolicitacao = rs.getDate("data_solicitacao");
			}
		} catch (SQLException sqle) {
			conexaoAuxiliar.rollback();
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			conexaoAuxiliar.rollback();
			throw new ProjetoException(ex, this.getClass().getName());
		} 
		return dataSolicitacao;
	}

}
