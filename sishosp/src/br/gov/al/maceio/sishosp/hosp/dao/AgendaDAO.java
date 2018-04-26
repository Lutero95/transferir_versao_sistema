package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.AgendaBean;
import br.gov.al.maceio.sishosp.hosp.model.BloqueioBean;
import br.gov.al.maceio.sishosp.hosp.model.FeriadoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProfissionalBean;
import br.gov.al.maceio.sishosp.hosp.model.TipoAtendimentoBean;

public class AgendaDAO {
	Connection con = null;
	PreparedStatement ps = null;

	public FeriadoBean verificarFeriado(Date dataAtendimento)
			throws ProjetoException {

		String sql = "select codferiado, descferiado, dataferiado from hosp.feriado where dataferiado = ?";
		FeriadoBean fer = null;
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setDate(1, new java.sql.Date(dataAtendimento.getTime()));
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				fer = new FeriadoBean();
				fer.setCodFeriado(rs.getInt("codferiado"));
				fer.setDescFeriado(rs.getString("descferiado"));
				fer.setDataFeriado(rs.getDate("dataferiado"));
			}
			return fer;
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				System.exit(1);
			}
		}
	}

	public List<BloqueioBean> verificarBloqueioProfissional(
			ProfissionalBean prof, Date dataAtendimento, String turno)
			throws ProjetoException {

		List<BloqueioBean> lista = new ArrayList<>();
		ProfissionalDAO pDao = new ProfissionalDAO();
		String sql = "select id_bloqueioagenda, codmedico, dataagenda, turno, descricao, codempresa "
				+ " from hosp.bloqueio_agenda "
				+ " where codmedico = ? and  dataagenda = ? and turno = ? order by id_bloqueioagenda";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, prof.getIdProfissional());
			stm.setDate(2, new java.sql.Date(dataAtendimento.getTime()));
			stm.setString(3, turno.toUpperCase());
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				BloqueioBean bloqueio = new BloqueioBean();
				bloqueio.setIdBloqueio(rs.getInt("id_bloqueioagenda"));
				bloqueio.setProf(pDao.buscarProfissionalPorId(rs
						.getInt("codmedico")));
				bloqueio.setDataInicio(rs.getDate("dataagenda"));
				bloqueio.setTurno(rs.getString("turno"));
				bloqueio.setDescBloqueio(rs.getString("descricao"));
				bloqueio.setCodEmpresa(rs.getInt("codempresa"));

				lista.add(bloqueio);
			}
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				System.exit(1);
			}
		}

		return lista;
	}

	public int verQtdMaxAgendaData(AgendaBean agenda) throws ProjetoException {
		int qtdMax = 0;
		String sqlPro = "select qtdmax from hosp.config_agenda where codmedico = ? and dataagenda = ? and turno = ?";
		String sqlEqui = "select qtdmax from hosp.config_agenda_equipe where codequipe = ? and dataagenda = ? and turno = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = null;
			if (agenda.getProfissional().getIdProfissional() != null) {
				stm = con.prepareStatement(sqlPro);
				stm.setInt(1, agenda.getProfissional().getIdProfissional());
			} else if (agenda.getEquipe().getCodEquipe() != null) {
				stm = con.prepareStatement(sqlEqui);
				stm.setInt(1, agenda.getEquipe().getCodEquipe());
			}
			stm.setDate(2, new java.sql.Date(agenda.getDataAtendimento()
					.getTime()));
			stm.setString(3, agenda.getTurno().toUpperCase());
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				qtdMax = rs.getInt("qtdmax");
			}
			return qtdMax;
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				System.exit(1);
			}
		}
	}

	public int verQtdAgendadosData(AgendaBean agenda) throws ProjetoException {
		int qtd = 0;
		String sqlPro = "select count(*) as qtd from hosp.atendimentos where codmedico = ? and dtaatende = ? and turno = ?;";
		String sqlEqui = "select count(*) as qtd from hosp.atendimentos where codequipe = ? and dtaatende = ? and turno = ?;";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = null;
			if (agenda.getProfissional().getIdProfissional() != null) {
				stm = con.prepareStatement(sqlPro);
				stm.setInt(1, agenda.getProfissional().getIdProfissional());
			} else if (agenda.getEquipe().getCodEquipe() != null) {
				stm = con.prepareStatement(sqlEqui);
				stm.setInt(1, agenda.getEquipe().getCodEquipe());
			}
			stm.setDate(2, new java.sql.Date(agenda.getDataAtendimento()
					.getTime()));
			stm.setString(3, agenda.getTurno().toUpperCase());
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				qtd = rs.getInt("qtd");
			}
			return qtd;
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				System.exit(1);
			}
		}
	}

	// argemiro
	public boolean buscarDataEspecifica(AgendaBean agenda)
			throws ProjetoException {
		int id = 0;
		String sqlPro = "select id_configagenda from hosp.config_agenda where codmedico = ? and dataagenda = ? and turno = ?";
		String sqlEqui = "select id_configagenda from hosp.config_agenda_equipe where codequipe = ? and dataagenda = ? and turno = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = null;
			if (agenda.getProfissional().getIdProfissional() != null) {
				stm = con.prepareStatement(sqlPro);
				stm.setInt(1, agenda.getProfissional().getIdProfissional());
			} else if (agenda.getEquipe().getCodEquipe() != null) {
				stm = con.prepareStatement(sqlEqui);
				stm.setInt(1, agenda.getEquipe().getCodEquipe());
			}
			stm.setDate(2, new java.sql.Date(agenda.getDataAtendimento()
					.getTime()));
			stm.setString(3, agenda.getTurno().toUpperCase());
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				id = rs.getInt("id_configagenda");
			}
			if (id == 0) {
				return false;
			} else
				return true;
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				System.exit(1);
			}
		}
	}

	public boolean buscarTabTipoAtendAgenda(AgendaBean agenda)
			throws ProjetoException {
		int achou = 0;
		String sql = "select codtipoatendimento from hosp.tipo_atend_agenda "
				+ " where codtipoatendimento = ? and codprograma = ? and codgrupo = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, agenda.getTipoAt().getIdTipo());
			stm.setInt(2, agenda.getPrograma().getIdPrograma());
			stm.setInt(3, agenda.getGrupo().getIdGrupo());
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				achou = rs.getInt("codtipoatendimento");
			}
			if (achou == 0) {
				return false;
			} else
				return true;
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				System.exit(1);
			}
		}
	}

	public boolean buscarDiaSemana(AgendaBean agenda) throws ProjetoException {
		Calendar cal = Calendar.getInstance();
		cal.setTime(agenda.getDataAtendimento());
		int diaSemana = cal.get(Calendar.DAY_OF_WEEK);
		int mes = cal.get(Calendar.MONTH);
		int id = 0;
		String sqlPro = "select id_configagenda from hosp.config_agenda where codmedico = ? and diasemana = ? and turno = ? and mes = ?";
		String sqlEqui = "select id_configagenda from hosp.config_agenda_equipe where codequipe = ? and diasemana = ? and turno = ? and mes = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = null;
			if (agenda.getProfissional().getIdProfissional() != null) {
				stm = con.prepareStatement(sqlPro);
				stm.setInt(1, agenda.getProfissional().getIdProfissional());
			} else if (agenda.getEquipe().getCodEquipe() != null) {
				stm = con.prepareStatement(sqlEqui);
				stm.setInt(1, agenda.getEquipe().getCodEquipe());
			}
			stm.setInt(2, diaSemana);
			stm.setString(3, agenda.getTurno().toUpperCase());
			stm.setInt(4, mes + 1);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				id = rs.getInt("id_configagenda");
			}
			if (id == 0) {
				return false;
			} else
				return true;
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				System.exit(1);
			}
		}
	}

	public boolean gravarAgenda(AgendaBean agenda,
			List<AgendaBean> listaNovosAgendamentos) throws ProjetoException {
		String sql = "INSERT INTO hosp.atendimentos(codpaciente, codmedico, codprograma,"
				+ " codconvenio, dtaatende, horaatende, situacao, codatendente, dtamarcacao, codtipoatendimento,"
				+ " turno, codequipe, observacao, ativo, codempresa, codgrupo)"
				+ " VALUES "
				+ "(?, ?, ?, ?, ?,"
				+ " ?, ?, ?, ?, ?,"
				+ " ?, ?, ?, ?, ?, ?) RETURNING id_atendimento;";
		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, agenda.getPaciente().getId_paciente());
			if (agenda.getProfissional().getIdProfissional() != null) {
				ps.setInt(2, agenda.getProfissional().getIdProfissional());
			} else {
				ps.setInt(2, 0);
			}
			ps.setInt(3, agenda.getPrograma().getIdPrograma());// codprograma
			ps.setInt(4, 0);// codconvenio
			ps.setDate(5, new java.sql.Date(agenda.getDataAtendimento()
					.getTime()));
			ps.setString(6, "");// horaatende
			ps.setString(7, "A");// situacao
			ps.setInt(8, 0);// codatendente
			ps.setDate(9, new java.sql.Date(new Date().getTime()));
			ps.setInt(10, agenda.getTipoAt().getIdTipo());
			ps.setString(11, agenda.getTurno().toUpperCase());
			if (agenda.getEquipe().getCodEquipe() != null) {
				ps.setInt(12, agenda.getEquipe().getCodEquipe());
			} else {
				ps.setInt(12, 0);
			}
			ps.setString(13, agenda.getObservacao().toUpperCase());
			ps.setString(14, "S");// ativo
			ps.setInt(15, 0);// COD EMPRESA ?
			ps.setInt(16, agenda.getGrupo().getIdGrupo());

			ResultSet rs = ps.executeQuery();

			int idAgend = 0;
			if (rs.next()) {
				idAgend = rs.getInt("id_atendimento");
			}
			// RETIREI O CODPROCEDIMENTO
			String sql2 = "INSERT INTO hosp.atendimentos1 (codprofissionalatendimento, id_atendimento, "
					+ " cbo) VALUES  (?, ?, ?)";
			ps = con.prepareStatement(sql2);
			if (agenda.getProfissional().getIdProfissional() != null) {
				ps.setInt(1, agenda.getProfissional().getIdProfissional());
				ps.setInt(2, idAgend);
				ps.setInt(3, agenda.getProfissional().getCbo().getCodCbo());
			} else if (agenda.getEquipe().getCodEquipe() != null) {
				for (ProfissionalBean prof : agenda.getEquipe()
						.getProfissionais()) {
					ps.setInt(1, prof.getIdProfissional());
					ps.setInt(2, idAgend);
					ps.setInt(3, prof.getCbo().getCodCbo());
				}
			}

			ps.executeUpdate();
			con.commit();

			return true;
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				System.exit(1);
			}
		}
	}

	public boolean preparaGravarAgendaIntervalo(AgendaBean agenda,
			List<AgendaBean> listaNovosAgendamentos) throws ProjetoException {

		con = ConnectionFactory.getConnection();

		try {

			ArrayList<AgendaBean> horariosErrado = gravarAgendaIntervalo(
					agenda, listaNovosAgendamentos, con);

			if (horariosErrado.size() > 0) {
				con.rollback();
			} else {
				con.commit();
			}

			return true;
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				System.exit(1);
			}
		}
	}

	//SEM USO, RETIREI
	public ArrayList<AgendaBean> gravarAgendaIntervalo(AgendaBean agenda,
			List<AgendaBean> listaNovosAgendamentos, Connection con2)
			throws ProjetoException {

		Date data = null;

		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		df.setLenient(false);
		Date d1 = agenda.getDataAtendimento();
		Date d2 = agenda.getDataAtendimentoFinal();
		Long dt = (d2.getTime() - d1.getTime()) + 3600000; // 1 hora para
															// compensar horário
															// de verão
		ArrayList<AgendaBean> horariosErrados = new ArrayList<AgendaBean>();

		dt = (dt / 86400000L);

		try {

			for (int i = 0; i < dt; i++) {

				Calendar c = Calendar.getInstance();
				c.setTime(agenda.getDataAtendimento());
				c.add(Calendar.DATE, +i);
				data = c.getTime();

				agenda.setDataAtendimento(data);
				if (buscarDataEspecifica(agenda) == false) {
					horariosErrados.add(agenda);
				}

				String sql = "INSERT INTO hosp.atendimentos(codpaciente, codmedico, codprograma,"
						+ " codconvenio, dtaatende, horaatende, situacao, codatendente, dtamarcacao, codtipoatendimento,"
						+ " turno, codequipe, observacao, ativo, codempresa, codgrupo)"
						+ " VALUES "
						+ "(?, ?, ?, ?, ?,"
						+ " ?, ?, ?, ?, ?,"
						+ " ?, ?, ?, ?, ?, ?) RETURNING id_atendimento;";

				ps = con2.prepareStatement(sql);
				ps.setInt(1, agenda.getPaciente().getId_paciente());
				if (agenda.getProfissional().getIdProfissional() != null) {
					ps.setInt(2, agenda.getProfissional().getIdProfissional());
				} else {
					ps.setInt(2, 0);
				}
				ps.setInt(3, agenda.getPrograma().getIdPrograma());// codprograma
				ps.setInt(4, 0);// codconvenio
				ps.setDate(5, new java.sql.Date(data.getTime()));
				ps.setString(6, "");// horaatende
				ps.setString(7, "A");// situacao
				ps.setInt(8, 0);// codatendente
				ps.setDate(9, new java.sql.Date(new Date().getTime()));
				ps.setInt(10, agenda.getTipoAt().getIdTipo());
				ps.setString(11, agenda.getTurno().toUpperCase());
				if (agenda.getEquipe().getCodEquipe() != null) {
					ps.setInt(12, agenda.getEquipe().getCodEquipe());
				} else {
					ps.setInt(12, 0);
				}
				ps.setString(13, agenda.getObservacao().toUpperCase());
				ps.setString(14, "S");// ativo
				ps.setInt(15, 0);// COD EMPRESA ?
				ps.setInt(16, agenda.getGrupo().getIdGrupo());

				ResultSet rs = ps.executeQuery();

				int idAgend = 0;
				if (rs.next()) {
					idAgend = rs.getInt("id_atendimento");
				}
				// RETIREI O CODPROCEDIMENTO
				String sql2 = "INSERT INTO hosp.atendimentos1 (codprofissionalatendimento, id_atendimento, "
						+ " cbo) VALUES  (?, ?, ?)";
				ps = con.prepareStatement(sql2);
				if (agenda.getProfissional().getIdProfissional() != null) {
					ps.setInt(1, agenda.getProfissional().getIdProfissional());
					ps.setInt(2, idAgend);
					ps.setInt(3, agenda.getProfissional().getCbo().getCodCbo());
				} else if (agenda.getEquipe().getCodEquipe() != null) {
					for (ProfissionalBean prof : agenda.getEquipe()
							.getProfissionais()) {
						ps.setInt(1, prof.getIdProfissional());
						ps.setInt(2, idAgend);
						ps.setInt(3, prof.getCbo().getCodCbo());
					}
				}

				ps.executeUpdate();

			}

			return horariosErrados;

		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				System.exit(1);
			}
		}
	}

	// RETIREI, SEM USO
	public void gravarAgendaAtendimento1(AgendaBean agenda, int idAgendamento)
			throws ProjetoException {

		// RETIREI O CODPROCEDIMENTO
		String sql = "INSERT INTO hosp.atendimentos1 (codprofissionalatendimento, id_atendimento, "
				+ " cbo) VALUES  (?, ?, ?)";
		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			if (agenda.getProfissional().getIdProfissional() != null) {
				ps.setInt(1, agenda.getProfissional().getIdProfissional());
				ps.setInt(2, idAgendamento);
				ps.setInt(3, agenda.getProfissional().getCbo().getCodCbo());
			} else if (agenda.getEquipe().getCodEquipe() != null) {
				for (ProfissionalBean prof : agenda.getEquipe()
						.getProfissionais()) {
					ps.setInt(1, prof.getIdProfissional());
					ps.setInt(2, idAgendamento);
					ps.setInt(3, prof.getCbo().getCodCbo());
				}
			}

			ps.execute();
			con.commit();
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				// con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				System.exit(1);
			}
		}
	}

	public List<AgendaBean> listarAgendamentosData(AgendaBean ag)
			throws ProjetoException {
		List<AgendaBean> lista = new ArrayList<AgendaBean>();
		PacienteDAO pDao = new PacienteDAO();
		ProfissionalDAO mDao = new ProfissionalDAO();
		TipoAtendimentoDAO tDao = new TipoAtendimentoDAO();
		EquipeDAO eDao = new EquipeDAO();

		String sqlProf = "SELECT id_atendimento, codpaciente, codmedico, codprograma,"
				+ " codconvenio, dtaatende, horaatende, situacao, codatendente,"
				+ " dtamarcacao, codtipoatendimento, turno, codequipe, observacao, ativo, codempresa"
				+ " FROM  hosp.atendimentos "
				+ " WHERE dtaatende = ? and codmedico = ? and turno = ?;";
		String sqlEqui = "SELECT id_atendimento, codpaciente, codmedico, codprograma,"
				+ " codconvenio, dtaatende, horaatende, situacao, codatendente,"
				+ " dtamarcacao, codtipoatendimento, turno, codequipe, observacao, ativo, codempresa"
				+ " FROM  hosp.atendimentos "
				+ " WHERE dtaatende = ? and codequipe = ? and turno = ?;";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = null;

			if (ag.getProfissional().getIdProfissional() != null) {
				stm = con.prepareStatement(sqlProf);
				stm.setInt(2, ag.getProfissional().getIdProfissional());
			} else if (ag.getEquipe().getCodEquipe() != null) {
				stm = con.prepareStatement(sqlEqui);
				stm.setInt(2, ag.getEquipe().getCodEquipe());
			}

			stm.setDate(1, new java.sql.Date(ag.getDataAtendimento().getTime()));
			stm.setString(3, ag.getTurno());
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				AgendaBean agenda = new AgendaBean();
				agenda.setIdAgenda(rs.getInt("id_atendimento"));
				agenda.setPaciente(pDao.listarPacientePorID(rs
						.getInt("codpaciente")));
				agenda.setProfissional(mDao.buscarProfissionalPorId(rs
						.getInt("codmedico")));
				agenda.setDataAtendimento(rs.getDate("dtaatende"));
				agenda.setSituacao(rs.getString("situacao"));
				agenda.setDataMarcacao(rs.getDate("dtamarcacao"));
				agenda.setTipoAt(tDao.listarTipoPorId(rs
						.getInt("codtipoatendimento")));
				agenda.setTurno(rs.getString("turno"));
				agenda.setEquipe(eDao.buscarEquipePorID(rs.getInt("codequipe")));
				agenda.setObservacao(rs.getString("observacao"));
				agenda.setAtivo(rs.getString("ativo"));
				lista.add(agenda);
			}
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				System.exit(1);
			}
		}
		return lista;
	}

	public List<AgendaBean> consultarAgenda(Date dataAgenda, Integer pront,
			String cns, TipoAtendimentoBean tipo) throws ProjetoException {
		List<AgendaBean> lista = new ArrayList<AgendaBean>();
		PacienteDAO pDao = new PacienteDAO();
		ProfissionalDAO mDao = new ProfissionalDAO();
		TipoAtendimentoDAO tDao = new TipoAtendimentoDAO();
		EquipeDAO eDao = new EquipeDAO();
		ProgramaDAO prDao = new ProgramaDAO();
		ProcedimentoDAO proDao = new ProcedimentoDAO();

		String sql = "SELECT a.id_atendimento, a.codpaciente, a.codmedico, a.codprograma,"
				+ " a.codconvenio, a.dtaatende, a.horaatende, a.situacao, a.codatendente,"
				+ " a.dtamarcacao, a.codtipoatendimento, a.turno, a.codequipe, a.observacao, a.ativo, a.codempresa, a1.codprocedimento"
				+ " FROM  hosp.atendimentos a inner join hosp.atendimentos1 a1 on (a.id_atendimento = a1.id_atendimento) WHERE ";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = null;
			stm = con.prepareStatement(sql);
			if (pront == null && tipo == null) {
				sql += " a.dtaatende = ? ";
				stm = con.prepareStatement(sql);
				stm.setDate(1, new java.sql.Date(dataAgenda.getTime()));
			} else if (pront != null && tipo == null) {
				sql += " a.dtaatende = ? and a.codpaciente = ?";
				stm = con.prepareStatement(sql);
				stm.setDate(1, new java.sql.Date(dataAgenda.getTime()));
				stm.setInt(2, pront);

			} else if (pront == null && tipo != null) {
				sql += "a.dtaatende = ? and a.codtipoatendimento = ?";
				stm = con.prepareStatement(sql);
				stm.setDate(1, new java.sql.Date(dataAgenda.getTime()));
				stm.setInt(2, tipo.getIdTipo());
			} else if (pront != null && tipo != null) {
				sql += "a.dtaatende = ? and a.codtipoatendimento = ? and a.codpaciente = ?";
				stm = con.prepareStatement(sql);
				stm.setDate(1, new java.sql.Date(dataAgenda.getTime()));
				stm.setInt(2, tipo.getIdTipo());
				stm.setInt(3, pront);
			}
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				AgendaBean agenda = new AgendaBean();
				agenda.setIdAgenda(rs.getInt("id_atendimento"));
				agenda.setPaciente(pDao.listarPacientePorID(rs
						.getInt("codpaciente")));
				agenda.setProfissional(mDao.buscarProfissionalPorId(rs
						.getInt("codmedico")));
				agenda.setPrograma(prDao.listarProgramaPorId(rs
						.getInt("codprograma")));
				agenda.setDataAtendimento(rs.getDate("dtaatende"));
				agenda.setSituacao(rs.getString("situacao"));
				agenda.setDataMarcacao(rs.getDate("dtamarcacao"));
				agenda.setTipoAt(tDao.listarTipoPorId(rs
						.getInt("codtipoatendimento")));
				agenda.setTurno(rs.getString("turno"));
				agenda.setEquipe(eDao.buscarEquipePorID(rs.getInt("codequipe")));
				agenda.setObservacao(rs.getString("observacao"));
				agenda.setAtivo(rs.getString("ativo"));
				agenda.setProcedimento(proDao.listarProcedimentoPorId(rs
						.getInt("codprocedimento")));
				lista.add(agenda);
			}
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				System.exit(1);
			}
		}
		return lista;
	}

	public boolean excluirAgendamento(AgendaBean agenda)
			throws ProjetoException {
		String sql = "delete from hosp.atendimentos where id_atendimento = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setLong(1, agenda.getIdAgenda());
			stmt.execute();
			con.commit();
			excluirTabelaAgendamentos1(agenda);
			return true;
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	public void excluirTabelaAgendamentos1(AgendaBean agenda)
			throws ProjetoException {
		String sql = "delete from hosp.atendimentos1 where id_atendimento = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setLong(1, agenda.getIdAgenda());
			stmt.execute();
			con.commit();
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				// con.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	public int verQtdMaxAgendaEspec(AgendaBean agenda) throws ProjetoException {
		Calendar cal = Calendar.getInstance();
		cal.setTime(agenda.getDataAtendimento());
		int diaSemana = cal.get(Calendar.DAY_OF_WEEK);
		int qtdMax = 0;
		String sqlPro = "select qtdmax from hosp.config_agenda where codmedico = ? and diasemana = ? and turno = ?";
		String sqlEqui = "select qtdmax from hosp.config_agenda_equipe where codequipe = ? and diasemana = ? and turno = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = null;
			if (agenda.getProfissional().getIdProfissional() != null) {
				stm = con.prepareStatement(sqlPro);
				stm.setInt(1, agenda.getProfissional().getIdProfissional());
			} else if (agenda.getEquipe().getCodEquipe() != null) {
				stm = con.prepareStatement(sqlEqui);
				stm.setInt(1, agenda.getEquipe().getCodEquipe());
			}
			stm.setInt(2, diaSemana);
			stm.setString(3, agenda.getTurno().toUpperCase());
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				qtdMax = rs.getInt("qtdmax");
			}
			return qtdMax;
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				System.exit(1);
			}
		}
	}

	public int verQtdAgendadosEspec(AgendaBean agenda) throws ProjetoException {
		int qtd = 0;
		String sqlPro = "select count(*) as qtd from hosp.atendimentos where codmedico = ? and dtaatende = ? and turno = ?;";
		String sqlEqui = "select count(*) as qtd from hosp.atendimentos where codequipe = ? and dtaatende = ? and turno = ?;";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = null;
			if (agenda.getProfissional().getIdProfissional() != null) {
				stm = con.prepareStatement(sqlPro);
				stm.setInt(1, agenda.getProfissional().getIdProfissional());
			} else if (agenda.getEquipe().getCodEquipe() != null) {
				stm = con.prepareStatement(sqlEqui);
				stm.setInt(1, agenda.getEquipe().getCodEquipe());
			}
			stm.setDate(2, new java.sql.Date(agenda.getDataAtendimento()
					.getTime()));
			stm.setString(3, agenda.getTurno().toUpperCase());
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				qtd = rs.getInt("qtd");
			}
			return qtd;
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				System.exit(1);
			}
		}
	}

	public boolean confirmarAtendimento(AgendaBean agenda, String situacaoConf)
			throws ProjetoException {
		String sql = "update hosp.atendimentos1 set codprofissionalatendimento = ?, cbo = ?,"
				+ " codprocedimento = ?,  situacao = ?, dtaatendido = ?"
				+ " where id_atendimento = ?";
		try {

			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, agenda.getProfissional().getIdProfissional());
			stmt.setInt(2, agenda.getProfissional().getCbo().getCodCbo());
			stmt.setInt(3, agenda.getProfissional().getProc1().getCodProc());
			stmt.setString(4, situacaoConf.toUpperCase());
			stmt.setDate(5, new java.sql.Date(new Date().getTime()));
			stmt.setInt(6, agenda.getIdAgenda());
			stmt.executeUpdate();
			con.commit();
			return true;
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

}
