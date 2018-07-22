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
import br.gov.al.maceio.sishosp.hosp.model.BloqueioBean;
import br.gov.al.maceio.sishosp.hosp.model.GerenciarPacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.InsercaoPacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.LaudoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProcedimentoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;

public class RenovacaoPacienteDAO {

	PreparedStatement ps = null;
	private Connection conexao = null;

	public InsercaoPacienteBean carregarPacientesInstituicaoRenovacao(Integer id)
			throws ProjetoException {

		String sql = "select pi.id, pi.codprograma, p.descprograma, pi.codgrupo, g.descgrupo, pi.codpaciente, pi.codequipe, e.descequipe, a.turno, "
				+ " pi.codprofissional, f.descfuncionario, pi.observacao, a.codtipoatendimento, t.desctipoatendimento "
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

	public boolean gravarRenovacaoEquipe(InsercaoPacienteBean insercao,
			InsercaoPacienteBean insercaoParaLaudo,
			List<InsercaoPacienteBean> lista) throws ProjetoException {

		ResultSet rs = null;
		String sql = "update hosp.paciente_instituicao set status = 'CR' where id = ?";
		try {
			conexao = ConnectionFactory.getConnection();
			ps = conexao.prepareStatement(sql);
			ps.setInt(1, insercao.getId());

			ps.executeUpdate();

			String sql2 = "INSERT INTO hosp.profissional_dia_atendimento (id_paciente_instituicao, id_profissional, dia_semana) VALUES  (?, ?, ?)";
			ps = conexao.prepareStatement(sql2);

			for (int i = 0; i < lista.size(); i++) {
				ps.setLong(1, insercao.getId());
				ps.setLong(2, lista.get(i).getAgenda().getProfissional()
						.getId());
				for (int j = 0; j < lista.get(i).getFuncionario()
						.getListDiasSemana().size(); j++) {
					ps.setInt(
							3,
							Integer.parseInt(lista.get(i).getFuncionario()
									.getListDiasSemana().get(j)));
					ps.executeUpdate();
				}
			}

			String sql3 = "INSERT INTO hosp.atendimentos(codpaciente, codmedico, situacao, dtamarcacao, codtipoatendimento, turno, "
					+ " observacao, ativo, id_paciente_instituicao)"
					+ " VALUES (?, ?, 'A', ?, ?, ?, ?, 'S', ?) RETURNING id_atendimento;";

			PreparedStatement ps3 = null;
			ps3 = conexao.prepareStatement(sql3);

			for (int i = 0; i < lista.size(); i++) {

				ps3.setInt(1, insercaoParaLaudo.getLaudo().getPaciente()
						.getId_paciente());
				ps3.setLong(2, lista.get(i).getAgenda().getProfissional()
						.getId());
				ps3.setDate(3, new java.sql.Date(lista.get(i).getAgenda()
						.getDataMarcacao().getTime()));
				ps3.setInt(4, insercao.getAgenda().getTipoAt().getIdTipo());
				ps3.setString(5, insercao.getAgenda().getTurno());
				ps3.setString(6, insercao.getObservacao());
				ps3.setInt(7, insercao.getId());

				rs = ps3.executeQuery();

				int idAgend = 0;
				if (rs.next()) {
					idAgend = rs.getInt("id_atendimento");
				}

				String sql4 = "INSERT INTO hosp.atendimentos1 (codprofissionalatendimento, id_atendimento, cbo) VALUES  (?, ?, ?)";

				PreparedStatement ps4 = null;
				ps4 = conexao.prepareStatement(sql4);

				ps4.setLong(1, lista.get(i).getAgenda().getProfissional()
						.getId());
				ps4.setInt(2, idAgend);
				ps4.setInt(3, lista.get(i).getAgenda().getProfissional()
						.getCbo().getCodCbo());

				ps4.executeUpdate();

			}

			String sql5 = "INSERT INTO hosp.historico_paciente_instituicao (codpaciente_instituicao, data_operacao, observacao, tipo) "
					+ " VALUES  (?, current_date, ?, ?)";

			PreparedStatement ps5 = null;
			ps5 = conexao.prepareStatement(sql5);

			ps5.setLong(1, insercaoParaLaudo.getLaudo().getPaciente()
					.getId_paciente());
			ps5.setString(2, insercao.getObservacao());
			ps5.setString(3, "R");

			ps5.executeUpdate();

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

	public boolean gravarInsercaoProfissional(InsercaoPacienteBean insercao,
			InsercaoPacienteBean insercaoParaLaudo,
			ArrayList<InsercaoPacienteBean> listaAgendamento)
			throws ProjetoException {

		ResultSet rs = null;

		String sql = "update hosp.paciente_instituicao set status = 'CR' where id = ?";
		try {
			conexao = ConnectionFactory.getConnection();
			ps = conexao.prepareStatement(sql);
			ps.setInt(1, insercao.getId());

			ps.executeUpdate();

			String sql2 = "INSERT INTO hosp.profissional_dia_atendimento (id_paciente_instituicao, id_profissional, dia_semana) VALUES  (?, ?, ?)";
			PreparedStatement ps2 = null;
			ps2 = conexao.prepareStatement(sql2);

			for (int i = 0; i < insercao.getFuncionario().getListDiasSemana()
					.size(); i++) {
				ps2.setLong(1, insercao.getId());
				ps2.setLong(2, insercao.getFuncionario().getId());
				ps2.setInt(
						3,
						Integer.parseInt(insercao.getFuncionario()
								.getListDiasSemana().get(i)));
				ps2.executeUpdate();

			}

			String sql3 = "INSERT INTO hosp.atendimentos(codpaciente, codmedico, situacao, dtamarcacao, codtipoatendimento, turno, observacao, ativo)"
					+ " VALUES (?, ?, 'A', ?, ?, ?, ?, 'S') RETURNING id_atendimento;";

			PreparedStatement ps3 = null;
			ps3 = conexao.prepareStatement(sql3);

			for (int i = 0; i < listaAgendamento.size(); i++) {

				ps3.setInt(1, insercaoParaLaudo.getLaudo().getPaciente()
						.getId_paciente());
				ps3.setLong(2, insercao.getFuncionario().getId());
				ps3.setDate(3, new java.sql.Date(listaAgendamento.get(i)
						.getAgenda().getDataMarcacao().getTime()));
				ps3.setInt(4, insercao.getAgenda().getTipoAt().getIdTipo());
				ps3.setString(5, insercao.getAgenda().getTurno());
				ps3.setString(6, insercao.getObservacao());

				rs = ps3.executeQuery();

				int idAgend = 0;
				if (rs.next()) {
					idAgend = rs.getInt("id_atendimento");
				}

				String sql4 = "INSERT INTO hosp.atendimentos1 (codprofissionalatendimento, id_atendimento, cbo) VALUES  (?, ?, ?)";

				PreparedStatement ps4 = null;
				ps4 = conexao.prepareStatement(sql4);

				ps4.setLong(1, insercao.getFuncionario().getId());
				ps4.setInt(2, idAgend);
				ps4.setInt(3, insercao.getFuncionario().getCbo().getCodCbo());

				ps4.executeUpdate();

			}

			String sql5 = "INSERT INTO hosp.historico_paciente_instituicao (codpaciente_instituicao, data_operacao, observacao, tipo) "
					+ " VALUES  (?, current_date, ?, ?)";

			PreparedStatement ps5 = null;
			ps5 = conexao.prepareStatement(sql5);

			ps5.setLong(1, insercaoParaLaudo.getLaudo().getPaciente()
					.getId_paciente());
			ps5.setString(2, insercao.getObservacao());
			ps5.setString(3, "R");

			ps5.executeUpdate();

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

}
