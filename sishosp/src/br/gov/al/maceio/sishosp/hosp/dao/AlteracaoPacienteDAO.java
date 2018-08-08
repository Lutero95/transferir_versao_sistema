package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.gov.al.maceio.sishosp.acl.dao.FuncionarioDAO;
import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.AgendaBean;
import br.gov.al.maceio.sishosp.hosp.model.BloqueioBean;
import br.gov.al.maceio.sishosp.hosp.model.GerenciarPacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.InsercaoPacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.LaudoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProcedimentoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;

public class AlteracaoPacienteDAO {

	PreparedStatement ps = null;
	private Connection conexao = null;

	public InsercaoPacienteBean carregarPacientesInstituicaoAlteracao(Integer id)
			throws ProjetoException {

		String sql = "select pi.id, pi.codprograma, p.descprograma, pi.codgrupo, g.descgrupo, pi.codpaciente, pi.codequipe, e.descequipe, a.turno, a.situacao, "
				+ " pi.codprofissional, f.descfuncionario, pi.observacao, a.codtipoatendimento, t.desctipoatendimento, pi.codlaudo, pi.data_solicitacao "
				+ " from hosp.paciente_instituicao pi "
				+ " left join hosp.programa p on (p.id_programa = pi.codprograma) "
				+ " left join hosp.grupo g on (pi.codgrupo = g.id_grupo) "
				+ " left join hosp.equipe e on (pi.codequipe = e.id_equipe) "
				+ " left join hosp.atendimentos a on (a.id_paciente_instituicao = pi.id) "
				+ " left join hosp.tipoatendimento t on (a.codtipoatendimento = t.id) "
				+ " left join acl.funcionarios f on (pi.codprofissional = f.id_funcionario) "
				+ " where pi.id = ?";

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
				ip.getLaudo().getPaciente()
						.setId_paciente(rs.getInt("codpaciente"));
				ip.getEquipe().setCodEquipe(rs.getInt("codequipe"));
				ip.getEquipe().setDescEquipe(rs.getString("descequipe"));
				ip.getFuncionario().setId(rs.getLong("codprofissional"));
				ip.getFuncionario().setNome(rs.getString("descfuncionario"));
				ip.setObservacao(rs.getString("observacao"));
				ip.getAgenda().getTipoAt()
						.setIdTipo(rs.getInt("codtipoatendimento"));
				ip.getAgenda().getTipoAt()
						.setDescTipoAt(rs.getString("desctipoatendimento"));
				ip.getAgenda().setTurno(rs.getString("turno"));
				ip.getAgenda().setSituacao(rs.getString("situacao"));
				ip.getLaudo().setId(rs.getInt("codlaudo"));
				ip.setData_solicitacao(rs.getDate("data_solicitacao"));

			}

		} catch (SQLException ex) {
			ex.printStackTrace();
			// throw new RuntimeException(ex); //
		} finally {
			try {
				conexao.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				System.exit(1);
			}
		}
		return ip;
	}

	public ArrayList<GerenciarPacienteBean> listarDiasAtendimentoProfissionalEquipe(
			Integer id) throws ProjetoException {

		ArrayList<GerenciarPacienteBean> lista = new ArrayList<>();

		String sql = "select distinct(p.id_profissional), f.descfuncionario, f.codcbo, p.id_paciente_instituicao, dia_semana, "
				+ " case when dia_semana = 1 then 'Domingo' when dia_semana = 2 then 'Segunda' "
				+ " when dia_semana = 3 then 'Terça' when dia_semana = 4 then 'Quarta' "
				+ " when dia_semana = 5 then 'Quinta' when dia_semana = 6 then 'Sexta' when dia_semana = 7 then 'Sábado' "
				+ " end as dia from hosp.profissional_dia_atendimento p "
				+ " left join acl.funcionarios f on (f.id_funcionario = p.id_profissional) "
				+ " where p.id_paciente_instituicao = ? "
				+ " order by id_profissional";
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stm = conexao.prepareStatement(sql);

			stm.setInt(1, id);

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				GerenciarPacienteBean ge = new GerenciarPacienteBean();
				ge.getFuncionario().setNome(rs.getString("descfuncionario"));
				ge.getFuncionario().setId(rs.getLong("id_profissional"));
				ge.setId(rs.getInt("id_paciente_instituicao"));
				ge.getFuncionario().setDiasSemana(rs.getString("dia"));
				ge.getFuncionario().setDiaSemana(rs.getInt("dia_semana"));
				ge.getFuncionario().getCbo().setCodCbo(rs.getInt("codcbo"));

				lista.add(ge);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				conexao.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				System.exit(1);
			}
		}
		return lista;
	}

	public ArrayList<String> listarDiasAtendimentoProfissional(Integer id)
			throws ProjetoException {

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
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				conexao.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				System.exit(1);
			}
		}
		return lista;
	}

	public boolean gravarAlteracaoEquipe(InsercaoPacienteBean insercao,
			InsercaoPacienteBean insercaoParaLaudo,
			List<InsercaoPacienteBean> listaP, Integer id_paciente) throws ProjetoException {

		ResultSet rs = null;
		ArrayList<Integer> lista = new ArrayList<Integer>();
		
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

			String sql3 = "delete from hosp.atendimentos where situacao = 'A' and id_paciente_instituicao = ?";

			PreparedStatement ps3 = null;
			ps3 = conexao.prepareStatement(sql3);
			ps3.setLong(1, id_paciente);
			ps3.execute();

			for (int i = 0; i < lista.size(); i++) {
				String sql4 = "delete from hosp.atendimentos1 where id_atendimento = ?";

				PreparedStatement ps4 = null;
				ps4 = conexao.prepareStatement(sql4);
				ps4.setLong(1, lista.get(i));
				ps4.execute();
			}			
			
			String sql5 = "INSERT INTO hosp.profissional_dia_atendimento (id_paciente_instituicao, id_profissional, dia_semana) VALUES  (?, ?, ?)";
			PreparedStatement ps5 = null;
			ps5 = conexao.prepareStatement(sql5);

			for (int i = 0; i < lista.size(); i++) {
				ps5.setLong(1, insercao.getId());
				ps5.setLong(2, listaP.get(i).getAgenda().getProfissional()
						.getId());
				for (int j = 0; j < listaP.get(i).getFuncionario()
						.getListDiasSemana().size(); j++) {
					ps5.setInt(3,
							Integer.parseInt(listaP.get(i).getFuncionario()
									.getListDiasSemana().get(j)));
					ps5.executeUpdate();
				}
			}

			String sql6 = "INSERT INTO hosp.atendimentos(codpaciente, codmedico, situacao, dtamarcacao, codtipoatendimento, turno, "
					+ " observacao, ativo, id_paciente_instituicao)"
					+ " VALUES (?, ?, 'A', ?, ?, ?, ?, 'S', ?) RETURNING id_atendimento;";

			PreparedStatement ps6 = null;
			ps6 = conexao.prepareStatement(sql6);

			for (int i = 0; i < lista.size(); i++) {

				ps6.setInt(1, insercaoParaLaudo.getLaudo().getPaciente()
						.getId_paciente());
				ps6.setLong(2, listaP.get(i).getAgenda().getProfissional()
						.getId());
				ps6.setDate(3, new java.sql.Date(listaP.get(i).getAgenda()
						.getDataMarcacao().getTime()));
				ps6.setInt(4, insercao.getAgenda().getTipoAt().getIdTipo());
				ps6.setString(5, insercao.getAgenda().getTurno());
				ps6.setString(6, insercao.getObservacao());
				ps6.setInt(7, insercao.getId());

				rs = ps6.executeQuery();

				int idAgend = 0;
				if (rs.next()) {
					idAgend = rs.getInt("id_atendimento");
				}

				String sql7 = "INSERT INTO hosp.atendimentos1 (codprofissionalatendimento, id_atendimento, cbo) VALUES  (?, ?, ?)";

				PreparedStatement ps7 = null;
				ps7 = conexao.prepareStatement(sql7);

				ps7.setLong(1, listaP.get(i).getAgenda().getProfissional()
						.getId());
				ps7.setInt(2, idAgend);
				ps7.setInt(3, listaP.get(i).getAgenda().getProfissional()
						.getCbo().getCodCbo());

				ps7.executeUpdate();

			}

			String sql8 = "INSERT INTO hosp.historico_paciente_instituicao (codpaciente_instituicao, data_operacao, observacao, tipo) "
					+ " VALUES  (?, current_date, ?, ?)";

			PreparedStatement ps8 = null;
			ps8 = conexao.prepareStatement(sql8);

			ps8.setLong(1, insercaoParaLaudo.getLaudo().getPaciente()
					.getId_paciente());
			ps8.setString(2, insercao.getObservacao());
			ps8.setString(3, "R");

			ps8.executeUpdate();

			conexao.commit();

			return true;
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				conexao.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				System.exit(1);
			}
		}
	}

	public boolean gravarAlteracaoProfissional(InsercaoPacienteBean insercao,
			InsercaoPacienteBean insercaoParaLaudo,
			ArrayList<InsercaoPacienteBean> listaAgendamento,
			Integer id_paciente) throws ProjetoException {

		ResultSet rs = null;
		ArrayList<Integer> lista = new ArrayList<Integer>();

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

			String sql3 = "delete from hosp.atendimentos where situacao = 'A' and id_paciente_instituicao = ?";

			PreparedStatement ps3 = null;
			ps3 = conexao.prepareStatement(sql3);
			ps3.setLong(1, id_paciente);
			ps3.execute();

			for (int i = 0; i < lista.size(); i++) {
				String sql4 = "delete from hosp.atendimentos1 where id_atendimento = ?";

				PreparedStatement ps4 = null;
				ps4 = conexao.prepareStatement(sql4);
				ps4.setLong(1, lista.get(i));
				ps4.execute();
			}

			String sql5 = "INSERT INTO hosp.profissional_dia_atendimento (id_paciente_instituicao, id_profissional, dia_semana) VALUES  (?, ?, ?)";
			PreparedStatement ps5 = null;
			ps5 = conexao.prepareStatement(sql5);

			for (int i = 0; i < insercao.getFuncionario().getListDiasSemana()
					.size(); i++) {
				ps5.setLong(1, insercao.getId());
				ps5.setLong(2, insercao.getFuncionario().getId());
				ps5.setInt(
						3,
						Integer.parseInt(insercao.getFuncionario()
								.getListDiasSemana().get(i)));
				ps5.executeUpdate();

			}

			String sql6 = "INSERT INTO hosp.atendimentos(codpaciente, codmedico, situacao, dtamarcacao, codtipoatendimento, turno, "
					+ "observacao, ativo, id_paciente_instituicao)"
					+ " VALUES (?, ?, 'A', ?, ?, ?, ?, 'S', ?) RETURNING id_atendimento;";

			PreparedStatement ps6 = null;
			ps6 = conexao.prepareStatement(sql6);

			for (int i = 0; i < listaAgendamento.size(); i++) {

				ps6.setInt(1, insercaoParaLaudo.getLaudo().getPaciente()
						.getId_paciente());
				ps6.setLong(2, insercao.getFuncionario().getId());
				ps6.setDate(3, new java.sql.Date(listaAgendamento.get(i)
						.getAgenda().getDataMarcacao().getTime()));
				ps6.setInt(4, insercao.getAgenda().getTipoAt().getIdTipo());
				ps6.setString(5, insercao.getAgenda().getTurno());
				ps6.setString(6, insercao.getObservacao());
				ps6.setInt(7, id_paciente);

				rs = ps6.executeQuery();

				int idAgend = 0;
				if (rs.next()) {
					idAgend = rs.getInt("id_atendimento");
				}

				String sql7 = "INSERT INTO hosp.atendimentos1 (codprofissionalatendimento, id_atendimento, cbo) VALUES  (?, ?, ?)";

				PreparedStatement ps7 = null;
				ps7 = conexao.prepareStatement(sql7);

				ps7.setLong(1, insercao.getFuncionario().getId());
				ps7.setInt(2, idAgend);
				ps7.setInt(3, insercao.getFuncionario().getCbo().getCodCbo());

				ps7.executeUpdate();

			}

			String sql8 = "INSERT INTO hosp.historico_paciente_instituicao (codpaciente_instituicao, data_operacao, observacao, tipo) "
					+ " VALUES  (?, current_date, ?, ?)";

			PreparedStatement ps8 = null;
			ps8 = conexao.prepareStatement(sql8);

			ps8.setLong(1, insercaoParaLaudo.getLaudo().getPaciente()
					.getId_paciente());
			ps8.setString(2, insercao.getObservacao());
			ps8.setString(3, "A");

			ps8.executeUpdate();

			conexao.commit();

			return true;
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				conexao.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				System.exit(1);
			}
		}
	}

	public ArrayList<AgendaBean> listaAtendimentos(Integer id)
			throws ProjetoException {

		ArrayList<AgendaBean> lista = new ArrayList<>();

		String sql = "select id_atendimento, codpaciente, situacao, dtamarcacao from hosp.atendimentos "
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
				ge.setDataAtendimento(rs.getDate("dtamarcacao"));

				lista.add(ge);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				conexao.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				System.exit(1);
			}
		}
		return lista;
	}

}
