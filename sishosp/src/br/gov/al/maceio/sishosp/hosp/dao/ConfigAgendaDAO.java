package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.ConfigAgendaParte1Bean;
import br.gov.al.maceio.sishosp.hosp.model.ConfigAgendaParte2Bean;
import br.gov.al.maceio.sishosp.hosp.model.LaudoBean;

public class ConfigAgendaDAO {

	Connection con = null;
	ProfissionalDAO pDao = new ProfissionalDAO();
	EquipeDAO eDao = new EquipeDAO();

	// SEM USO, RETIREI
	// public boolean gravarConfigAgenda(ConfigAgendaParte1Bean confParte1,
	// ConfigAgendaParte2Bean confParte2,
	// List<ConfigAgendaParte2Bean> listaTipos) throws SQLException,
	// ProjetoException {
	//
	// // if (confParte1.getProfissional().getIdProfissional() == null
	// // || confParte1.getQtdMax() == null) {
	// // return false;
	// // }
	//
	// try {
	// if (confParte1.getDiasSemana().size() > 0) {// ESCOLHEU DIAS SEMANA
	// for (String dia : confParte1.getDiasSemana()) {
	// gravaTurno(confParte1, listaTipos, dia);
	// }
	// } else {// ESCOLHEU DATA ESPECIFICA
	// gravaTurno(confParte1, listaTipos, null);
	// }
	//
	// return true;
	// } catch (SQLException ex) {
	// throw new RuntimeException(ex);
	// } finally {
	// try {
	// con.close();
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// System.exit(1);
	// }
	// }
	// }

	public boolean gravarConfigAgendaEquipe(ConfigAgendaParte1Bean confParte1,
			ConfigAgendaParte2Bean confParte2,
			List<ConfigAgendaParte2Bean> listaTipos) throws SQLException,
			ProjetoException {

		if (confParte1.getEquipe().getCodEquipe() == null
				|| confParte1.getQtdMax() == null
				|| confParte1.getAno() == null) {
			return false;
		}
		try {
			if (confParte1.getDiasSemana().size() > 0) {// ESCOLHEU DIAS SEMANA
				for (String dia : confParte1.getDiasSemana()) {
					gravaTurnoEquipe(confParte1, listaTipos, dia);
				}
			} else {// ESCOLHEU DATA ESPECIFICA
				gravaTurnoEquipe(confParte1, listaTipos, null);
			}

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

	public void insereTipoAtendAgenda(int codConf,
			ConfigAgendaParte1Bean conf1,
			List<ConfigAgendaParte2Bean> listaTipos) throws ProjetoException {
		PreparedStatement ps1 = null;
		String sql = "insert into hosp.tipo_atend_agenda (codconfigagenda, codprograma, codtipoatendimento, qtd, codempresa, codgrupo) "
				+ " values(?, ?, ?, ?, ?, ?);";
		try {
			con = ConnectionFactory.getConnection();
			ps1 = con.prepareStatement(sql);
			for (ConfigAgendaParte2Bean conf : listaTipos) {
				ps1.setInt(1, codConf);
				ps1.setInt(2, conf.getPrograma().getIdPrograma());
				ps1.setInt(3, conf.getTipoAt().getIdTipo());
				ps1.setInt(4, conf.getQtd());
				ps1.setInt(5, 0);// COD EMPRESA ?
				ps1.setInt(6, conf.getGrupo().getIdGrupo());
				ps1.execute();
				con.commit();
			}

		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
			} catch (Exception ex) {
				ex.printStackTrace();
				System.exit(1);
			}
		}
	}

	public Boolean gravaTurno(ConfigAgendaParte1Bean confParte1,
			List<ConfigAgendaParte2Bean> listaTipos, String dia)
			throws SQLException, ProjetoException {
		boolean gravou = false;

		try {
			String sql = "INSERT INTO hosp.config_agenda(codmedico, diasemana, qtdmax, dataagenda, turno, mes, ano, codempresa, id_configagenda) "
					+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, DEFAULT) RETURNING id_configagenda;";
			con = ConnectionFactory.getConnection();
			PreparedStatement ps2 = con.prepareStatement(sql);

			ResultSet rs2 = null;
			if (confParte1.getTurno().equals("A")) {
				if (dia != null) {
					ps2.setInt(2, Integer.parseInt(dia));
					ps2.setDate(4, null);
				} else {
					ps2.setDate(4, new Date(confParte1.getDataEspecifica()
							.getTime()));
					Calendar c1 = Calendar.getInstance();
					c1.setTime(confParte1.getDataEspecifica());
					ps2.setInt(2, 0);
					confParte1.setMes(c1.get(Calendar.MONTH) + 1);
					confParte1.setAno(c1.get(Calendar.YEAR));

				}
				ps2.setInt(1, confParte1.getProfissional().getIdProfissional());
				ps2.setInt(3, confParte1.getQtdMax());
				ps2.setString(5, "M");
				ps2.setInt(6, confParte1.getMes());
				ps2.setInt(7, confParte1.getAno());

				ps2.setInt(8, 0);// COD EMPRESA ?
				rs2 = ps2.executeQuery();
				con.commit();
				int idTipo1 = 0;
				if (rs2.next()) {
					idTipo1 = rs2.getInt("id_configagenda");
					insereTipoAtendAgenda(idTipo1, confParte1, listaTipos);
				}
				con.commit();

				ps2 = con.prepareStatement(sql);
				if (dia != null) {
					ps2.setInt(2, Integer.parseInt(dia));
					ps2.setDate(4, null);
				} else {
					ps2.setInt(2, 0);
					Calendar c1 = Calendar.getInstance();
					c1.setTime(confParte1.getDataEspecifica());
					ps2.setDate(4, new Date(confParte1.getDataEspecifica()
							.getTime()));
					confParte1.setMes(c1.get(Calendar.MONTH) + 1);
					confParte1.setAno(c1.get(Calendar.YEAR));
				}
				ps2.setInt(1, confParte1.getProfissional().getIdProfissional());
				ps2.setInt(3, confParte1.getQtdMax());
				ps2.setString(5, "T");
				ps2.setInt(6, confParte1.getMes());
				ps2.setInt(7, confParte1.getAno());
				ps2.setInt(8, 0);// COD EMPRESA ?
				rs2 = ps2.executeQuery();
				con.commit();
				int idTipo2 = 0;
				if (rs2.next()) {
					idTipo2 = rs2.getInt("id_configagenda");
					insereTipoAtendAgenda(idTipo2, confParte1, listaTipos);

				}
				con.commit();

			} else {
				if (dia != null) {
					ps2.setInt(2, Integer.parseInt(dia));
					ps2.setDate(4, null);
				} else {
					ps2.setDate(4, new Date(confParte1.getDataEspecifica()
							.getTime()));
					Calendar c1 = Calendar.getInstance();
					c1.setTime(confParte1.getDataEspecifica());
					ps2.setInt(2, 0);
					confParte1.setMes(c1.get(Calendar.MONTH) + 1);
					confParte1.setAno(c1.get(Calendar.YEAR));

				}
				ps2.setInt(1, confParte1.getProfissional().getIdProfissional());
				ps2.setInt(3, confParte1.getQtdMax());
				ps2.setString(5, confParte1.getTurno());
				ps2.setInt(6, confParte1.getMes());
				ps2.setInt(7, confParte1.getAno());
				ps2.setInt(8, 0);// COD EMPRESA ?
				rs2 = ps2.executeQuery();
				con.commit();
				int idTipo3 = 0;
				if (rs2.next()) {
					idTipo3 = rs2.getInt("id_configagenda");
					insereTipoAtendAgenda(idTipo3, confParte1, listaTipos);

				}
			}
			gravou = true;

		} catch (ProjetoException e) {
			e.printStackTrace();
			return false;
		}

		return gravou;
	}

	public void gravaTurnoEquipe(ConfigAgendaParte1Bean confParte1,
			List<ConfigAgendaParte2Bean> listaTipos, String dia)
			throws SQLException, ProjetoException {

		String sql = "INSERT INTO hosp.config_agenda_equipe(codequipe, diasemana, qtdmax, dataagenda, turno, mes, ano, codempresa, id_configagenda) "
				+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, DEFAULT) RETURNING id_configagenda;";
		con = ConnectionFactory.getConnection();
		PreparedStatement ps2 = con.prepareStatement(sql);

		ResultSet rs2 = null;
		if (confParte1.getTurno().equals("A")) {
			if (dia != null) {
				ps2.setInt(2, Integer.parseInt(dia));
				ps2.setDate(4, null);
			} else {
				ps2.setInt(2, 0);
				ps2.setDate(4, new Date(confParte1.getDataEspecifica()
						.getTime()));
			}
			ps2.setInt(1, confParte1.getEquipe().getCodEquipe());
			ps2.setInt(3, confParte1.getQtdMax());
			ps2.setString(5, "M");
			ps2.setInt(6, confParte1.getMes());
			ps2.setInt(7, confParte1.getAno());
			ps2.setInt(8, 0);// COD EMPRESA ?
			rs2 = ps2.executeQuery();
			con.commit();

			ps2 = con.prepareStatement(sql);
			if (dia != null) {
				ps2.setInt(2, Integer.parseInt(dia));
				ps2.setDate(4, null);
			} else {
				ps2.setInt(2, 0);
				ps2.setDate(4, new Date(confParte1.getDataEspecifica()
						.getTime()));
			}
			ps2.setInt(1, confParte1.getEquipe().getCodEquipe());
			ps2.setInt(3, confParte1.getQtdMax());
			ps2.setString(5, "T");
			ps2.setInt(6, confParte1.getMes());
			ps2.setInt(7, confParte1.getAno());
			ps2.setInt(8, 0);// COD EMPRESA ?
			rs2 = ps2.executeQuery();
			con.commit();

		} else {
			if (dia != null) {
				ps2.setInt(2, Integer.parseInt(dia));
				ps2.setDate(4, null);
			} else {
				ps2.setInt(2, 0);
				ps2.setDate(4, new Date(confParte1.getDataEspecifica()
						.getTime()));
			}
			ps2.setInt(1, confParte1.getEquipe().getCodEquipe());
			ps2.setInt(3, confParte1.getQtdMax());
			ps2.setString(5, confParte1.getTurno());
			ps2.setInt(6, confParte1.getMes());
			ps2.setInt(7, confParte1.getAno());
			ps2.setInt(8, 0);// COD EMPRESA ?
			rs2 = ps2.executeQuery();
			con.commit();
		}
	}

	public List<ConfigAgendaParte1Bean> listarHorarios()
			throws ProjetoException {
		List<ConfigAgendaParte1Bean> lista = new ArrayList<>();
		String sql = "SELECT id_configagenda, codmedico, diasemana, qtdmax, dataagenda, turno, mes, ano, codempresa "
				+ "FROM hosp.config_agenda order by id_configagenda ";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				ConfigAgendaParte1Bean conf = new ConfigAgendaParte1Bean();
				conf.setIdConfiAgenda(rs.getInt("id_configagenda"));
				conf.setProfissional(pDao.buscarProfissionalPorId(rs
						.getInt("codmedico")));
				conf.setDiaDaSemana(rs.getInt("diasemana"));
				conf.setDataEspecifica(rs.getDate("dataagenda"));
				conf.setQtdMax(rs.getInt("qtdmax"));
				conf.setTurno(rs.getString("turno"));
				conf.setMes(rs.getInt("mes"));
				conf.setAno(rs.getInt("ano"));
				// codempresa

				lista.add(conf);
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

	public List<ConfigAgendaParte1Bean> listarHorariosComFiltros(
			ConfigAgendaParte1Bean config, int codmedico)
			throws ProjetoException {
		List<ConfigAgendaParte1Bean> lista = new ArrayList<>();
		String sql = "SELECT id_configagenda, codmedico, diasemana, qtdmax, dataagenda, turno, mes, ano, codempresa "
				+ "FROM hosp.config_agenda where codmedico = ?";
		if (config != null) {
			if (config.getAno() != null) {
				if (config.getAno() > 0) {
					sql = sql + " and ano = ?";
				}
			}
			if (config.getTurno() != null) {
				if (!config.getTurno().equals("")) {
					sql = sql + " and turno = ?";
				}
			}
			if (config.getMes() != null) {
				if (config.getMes() > 0) {
					sql = sql + " and mes = ?";
				}
			}
		}

		sql = sql + "order by id_configagenda ";

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			int i = 1;

			stm.setInt(i, codmedico);

			if (config != null) {
				if (config.getAno() != null) {
					if (config.getAno() > 0) {
						i = i + 1;
						stm.setInt(i, config.getAno());
					}
				}
				if (config.getTurno() != null) {
					if (!config.getTurno().equals("")) {
						i = i + 1;
						stm.setString(i, config.getTurno());
					}
				}
				if (config.getMes() != null) {
					if (config.getMes() > 0) {
						i = i + 1;
						stm.setInt(i, config.getMes());
					}
				}
			}

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				ConfigAgendaParte1Bean conf = new ConfigAgendaParte1Bean();
				conf.setIdConfiAgenda(rs.getInt("id_configagenda"));
				conf.setProfissional(pDao.buscarProfissionalPorId(rs
						.getInt("codmedico")));
				conf.setDiaDaSemana(rs.getInt("diasemana"));
				conf.setDataEspecifica(rs.getDate("dataagenda"));
				conf.setQtdMax(rs.getInt("qtdmax"));
				conf.setTurno(rs.getString("turno"));
				conf.setMes(rs.getInt("mes"));
				conf.setAno(rs.getInt("ano"));

				lista.add(conf);

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

	public List<ConfigAgendaParte1Bean> listarHorariosComFiltrosEquipe(
			ConfigAgendaParte1Bean config, int codequipe)
			throws ProjetoException {
		List<ConfigAgendaParte1Bean> lista = new ArrayList<>();
		String sql = "SELECT id_configagenda, codequipe, diasemana, qtdmax, dataagenda, turno, mes, ano, codempresa "
				+ "FROM hosp.config_agenda_equipe where codequipe = ?";
		if (config != null) {
			if (config.getAno() != null) {
				if (config.getAno() > 0) {
					sql = sql + " and ano = ?";
				}
			}
			if (config.getTurno() != null) {
				if (!config.getTurno().equals("")) {
					sql = sql + " and turno = ?";
				}
			}
			if (config.getMes() != null) {
				if (config.getMes() > 0) {
					sql = sql + " and mes = ?";
				}
			}
		}

		sql = sql + "order by id_configagenda ";

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			int i = 1;

			stm.setInt(i, codequipe);

			if (config != null) {
				if (config.getAno() != null) {
					if (config.getAno() > 0) {
						i = i + 1;
						stm.setInt(i, config.getAno());
					}
				}
				if (config.getTurno() != null) {
					if (!config.getTurno().equals("")) {
						i = i + 1;
						stm.setString(i, config.getTurno());
					}
				}
				if (config.getMes() != null) {
					if (config.getMes() > 0) {
						i = i + 1;
						stm.setInt(i, config.getMes());
					}
				}
			}

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				ConfigAgendaParte1Bean conf = new ConfigAgendaParte1Bean();
				conf.setIdConfiAgenda(rs.getInt("id_configagenda"));
				conf.setEquipe(eDao.buscarEquipePorID(rs.getInt("codequipe")));
				conf.setDiaDaSemana(rs.getInt("diasemana"));
				conf.setDataEspecifica(rs.getDate("dataagenda"));
				conf.setQtdMax(rs.getInt("qtdmax"));
				conf.setTurno(rs.getString("turno"));
				conf.setMes(rs.getInt("mes"));
				conf.setAno(rs.getInt("ano"));

				lista.add(conf);

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

	public List<ConfigAgendaParte1Bean> listarHorariosEquipe()
			throws ProjetoException {
		List<ConfigAgendaParte1Bean> lista = new ArrayList<>();
		String sql = "SELECT id_configagenda, codequipe, diasemana, qtdmax, dataagenda, turno, mes, ano, codempresa "
				+ "FROM hosp.config_agenda_equipe order by id_configagenda ";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				ConfigAgendaParte1Bean conf = new ConfigAgendaParte1Bean();
				conf.setIdConfiAgenda(rs.getInt("id_configagenda"));
				conf.setEquipe(eDao.buscarEquipePorID(rs.getInt("codequipe")));
				conf.setDiaDaSemana(rs.getInt("diasemana"));
				// horainicio
				// horafim
				conf.setDataEspecifica(rs.getDate("dataagenda"));
				conf.setQtdMax(rs.getInt("qtdmax"));
				conf.setTurno(rs.getString("turno"));
				conf.setMes(rs.getInt("mes"));
				conf.setAno(rs.getInt("ano"));
				// codempresa

				lista.add(conf);
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

	public List<ConfigAgendaParte1Bean> listarHorariosPorIDProfissional(int id)
			throws ProjetoException {
		List<ConfigAgendaParte1Bean> lista = new ArrayList<>();
		String sql = "SELECT id_configagenda, codmedico, diasemana, qtdmax, dataagenda, turno, mes, ano, codempresa "
				+ "FROM hosp.config_agenda where codmedico = ? order by id_configagenda ";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, id);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				ConfigAgendaParte1Bean conf = new ConfigAgendaParte1Bean();
				conf.setIdConfiAgenda(rs.getInt("id_configagenda"));
				conf.setProfissional(pDao.buscarProfissionalPorId(rs
						.getInt("codmedico")));
				conf.setDiaDaSemana(rs.getInt("diasemana"));
				conf.setDataEspecifica(rs.getDate("dataagenda"));
				conf.setQtdMax(rs.getInt("qtdmax"));
				conf.setTurno(rs.getString("turno"));
				conf.setMes(rs.getInt("mes"));
				conf.setAno(rs.getInt("ano"));
				// codempresa
				lista.add(conf);
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

	public ConfigAgendaParte1Bean listarHorariosPorIDProfissional2(int id)
			throws ProjetoException {

		ConfigAgendaParte1Bean c = new ConfigAgendaParte1Bean();
		String sql = "SELECT id_configagenda, codmedico, diasemana, qtdmax, dataagenda, turno, mes, ano, codempresa "
				+ "FROM hosp.config_agenda where id_configagenda = ? order by id_configagenda ";
		ConfigAgendaParte1Bean conf = new ConfigAgendaParte1Bean();
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, id);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				conf.setIdConfiAgenda(rs.getInt("id_configagenda"));
				conf.setProfissional(pDao.buscarProfissionalPorId(rs
						.getInt("codmedico")));
				conf.setDiaDaSemana(rs.getInt("diasemana"));
				conf.setDataEspecifica(rs.getDate("dataagenda"));
				conf.setQtdMax(rs.getInt("qtdmax"));
				conf.setTurno(rs.getString("turno"));
				conf.setMes(rs.getInt("mes"));
				conf.setAno(rs.getInt("ano"));

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
		return conf;
	}

	public ConfigAgendaParte1Bean listarHorariosPorIDEquipe2(int id)
			throws ProjetoException {

		ConfigAgendaParte1Bean c = new ConfigAgendaParte1Bean();
		String sql = "SELECT id_configagenda, codequipe, diasemana, qtdmax, dataagenda, turno, mes, ano, codempresa "
				+ "FROM hosp.config_agenda_equipe where id_configagenda = ? order by id_configagenda ";
		ConfigAgendaParte1Bean conf = new ConfigAgendaParte1Bean();
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, id);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				conf.setIdConfiAgenda(rs.getInt("id_configagenda"));
				conf.setEquipe(eDao.buscarEquipePorID(rs.getInt("codequipe")));
				conf.setDiaDaSemana(rs.getInt("diasemana"));
				conf.setDataEspecifica(rs.getDate("dataagenda"));
				conf.setQtdMax(rs.getInt("qtdmax"));
				conf.setTurno(rs.getString("turno"));
				conf.setMes(rs.getInt("mes"));
				conf.setAno(rs.getInt("ano"));

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
		return conf;
	}

	public List<ConfigAgendaParte1Bean> listarHorariosPorIDEquipe(int id)
			throws ProjetoException {
		List<ConfigAgendaParte1Bean> lista = new ArrayList<>();
		String sql = "SELECT id_configagenda, codequipe, diasemana,qtdmax, dataagenda, turno, mes, ano, codempresa "
				+ "FROM hosp.config_agenda_equipe where codequipe = ? order by id_configagenda ";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, id);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				ConfigAgendaParte1Bean conf = new ConfigAgendaParte1Bean();
				conf.setIdConfiAgenda(rs.getInt("id_configagenda"));
				conf.setEquipe(eDao.buscarEquipePorID(rs.getInt("codequipe")));
				conf.setDiaDaSemana(rs.getInt("diasemana"));
				// horainicio
				// horafim
				conf.setDataEspecifica(rs.getDate("dataagenda"));
				conf.setQtdMax(rs.getInt("qtdmax"));
				conf.setTurno(rs.getString("turno"));
				conf.setMes(rs.getInt("mes"));
				conf.setAno(rs.getInt("ano"));
				// codempresa
				lista.add(conf);
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

	public List<ConfigAgendaParte2Bean> listarTiposAgendPorId(int id)
			throws ProjetoException {
		List<ConfigAgendaParte2Bean> lista = new ArrayList<ConfigAgendaParte2Bean>();
		String sql = "SELECT codconfigagenda, codprograma, codtipoatendimento, qtd, codgrupo "
				+ " FROM  hosp.tipo_atend_agenda WHERE codconfigagenda = ?;";
		ProgramaDAO pDao = new ProgramaDAO();
		TipoAtendimentoDAO tDao = new TipoAtendimentoDAO();
		GrupoDAO gDao = new GrupoDAO();
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, id);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				ConfigAgendaParte2Bean conf = new ConfigAgendaParte2Bean();
				conf.setPrograma(pDao.listarProgramaPorId(rs
						.getInt("codprograma")));
				conf.setTipoAt(tDao.listarTipoPorId(rs
						.getInt("codtipoatendimento")));
				conf.setQtd(rs.getInt("qtd"));
				conf.setGrupo(gDao.listarGrupoPorId(rs.getInt("codGrupo")));
				lista.add(conf);
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

	public boolean excluirConfig(ConfigAgendaParte1Bean confParte1)
			throws ProjetoException {
		String sql = "delete from hosp.config_agenda where id_configagenda = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setLong(1, confParte1.getIdConfiAgenda());
			stmt.execute();
			con.commit();
			excluirTabelaTipoAgenda(confParte1.getIdConfiAgenda());
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

	public boolean excluirConfigEquipe(ConfigAgendaParte1Bean confParte1)
			throws ProjetoException {
		String sql = "delete from hosp.config_agenda_equipe where id_configagenda = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setLong(1, confParte1.getIdConfiAgenda());
			stmt.execute();
			con.commit();
			// excluirTabelaTipoAgenda(confParte1.getIdConfiAgenda());
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

	public void excluirTabelaTipoAgenda(int id) throws ProjetoException {

		String sql = "delete from hosp.tipo_atend_agenda where codconfigagenda = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setLong(1, id);
			stmt.execute();
			con.commit();
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

	// SEM USO, RETIREI
	// public boolean alterarConfigAgenda(ConfigAgendaParte1Bean confParte1,
	// ConfigAgendaParte2Bean confParte2,
	// List<ConfigAgendaParte2Bean> listaTiposEditar)
	// throws ProjetoException {
	//
	// if (confParte1.getProfissional().getIdProfissional() == null
	// || confParte1.getQtdMax() == null
	// || confParte1.getAno() == null) {
	// return false;
	// }
	//
	// try {
	// if (confParte1.getDiasSemana().size() > 0) {// ESCOLHEU DIAS SEMANA
	// for (String dia : confParte1.getDiasSemana()) {
	// alterarTurno(confParte1, listaTiposEditar, dia);
	// }
	// } else {// ESCOLHEU DATA ESPECIFICA
	// alterarTurno(confParte1, listaTiposEditar, null);
	// }
	//
	// return true;
	// } catch (SQLException ex) {
	// throw new RuntimeException(ex);
	// } finally {
	// try {
	// con.close();
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// System.exit(1);
	// }
	// }
	// }

	// SEM USO, RETIREI
	public boolean alterarConfigAgendaEquipe(ConfigAgendaParte1Bean confParte1,
			ConfigAgendaParte2Bean confParte2,
			List<ConfigAgendaParte2Bean> listaTiposEditar)
			throws ProjetoException {

		if (confParte1.getProfissional().getIdProfissional() == null
				|| confParte1.getQtdMax() == null
				|| confParte1.getAno() == null) {
			return false;
		}

		try {
			if (confParte1.getDiasSemana().size() > 0) {// ESCOLHEU DIAS SEMANA
				for (String dia : confParte1.getDiasSemana()) {
					alterarTurnoEquipe(confParte1, listaTiposEditar, dia);
				}
			} else {// ESCOLHEU DATA ESPECIFICA
				alterarTurnoEquipe(confParte1, listaTiposEditar, null);
			}

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

	public Boolean alterarTurno(ConfigAgendaParte1Bean confParte1,
			List<ConfigAgendaParte2Bean> listaTipos) throws SQLException,
			ProjetoException {

		try {
			String sql = "UPDATE hosp.config_agenda SET codmedico = ?, diasemana = ?, qtdmax = ?, dataagenda = ?, turno = ?, mes = ?, ano = ?, codempresa = ? "
					+ " WHERE id_configagenda = ?;";
			con = ConnectionFactory.getConnection();
			PreparedStatement ps2 = con.prepareStatement(sql);

			if (confParte1.getTurno().equals("A")) {
				if (confParte1.getDiaDaSemana() != null) {
					ps2.setInt(2, confParte1.getDiaDaSemana());
					ps2.setDate(4, null);
				} else {
					ps2.setInt(2, 0);
					ps2.setDate(4, new Date(confParte1.getDataEspecifica()
							.getTime()));
				}
				ps2.setInt(1, confParte1.getProfissional().getIdProfissional());
				ps2.setInt(3, confParte1.getQtdMax());
				ps2.setString(5, "M");
				ps2.setInt(6, confParte1.getMes());
				ps2.setInt(7, confParte1.getAno());
				ps2.setInt(8, 0);// COD EMPRESA ?
				ps2.setInt(9, confParte1.getIdConfiAgenda());
				ps2.executeUpdate();

				String sql2 = "DELETE from hosp.tipo_atend_agenda where codconfigagenda = ?";
				PreparedStatement ps3 = con.prepareStatement(sql2);
				ps3.setInt(1, confParte1.getIdConfiAgenda());
				ps3.execute();

				String sql3 = "INSERT INTO hosp.tipo_atend_agenda (codprograma, codtipoatendimento,  qtd, codgrupo, codconfigagenda) "
						+ " VALUES (?, ?, ?, ?, ?); ";

				PreparedStatement ps4 = con.prepareStatement(sql3);
				for (ConfigAgendaParte2Bean conf : listaTipos) {
					ps4.setInt(1, conf.getPrograma().getIdPrograma());
					ps4.setInt(2, conf.getTipoAt().getIdTipo());
					ps4.setInt(3, conf.getQtd());
					ps4.setInt(4, conf.getGrupo().getIdGrupo());
					ps4.setInt(5, confParte1.getIdConfiAgenda());
					ps4.execute();
				}

				ps2 = con.prepareStatement(sql);
				if (confParte1.getDiaDaSemana() != null) {
					ps2.setInt(2, confParte1.getDiaDaSemana());
					ps2.setDate(4, null);
				} else {
					ps2.setInt(2, 0);
					ps2.setDate(4, new Date(confParte1.getDataEspecifica()
							.getTime()));
				}
				ps2.setInt(1, confParte1.getProfissional().getIdProfissional());
				ps2.setInt(3, confParte1.getQtdMax());
				ps2.setString(5, "T");
				ps2.setInt(6, confParte1.getMes());
				ps2.setInt(7, confParte1.getAno());
				ps2.setInt(8, 0);// COD EMPRESA ?
				ps2.setInt(9, confParte1.getIdConfiAgenda());
				ps2.executeUpdate();

				sql2 = "DELETE from hosp.tipo_atend_agenda where codconfigagenda = ?";
				ps3 = con.prepareStatement(sql2);
				ps3.setInt(1, confParte1.getIdConfiAgenda());
				ps3.execute();

				sql3 = "INSERT INTO hosp.tipo_atend_agenda (codprograma, codtipoatendimento,  qtd, codgrupo, codconfigagenda) "
						+ " VALUES (?, ?, ?, ?, ?); ";

				ps4 = con.prepareStatement(sql3);
				for (ConfigAgendaParte2Bean conf : listaTipos) {
					ps4.setInt(1, conf.getPrograma().getIdPrograma());
					ps4.setInt(2, conf.getTipoAt().getIdTipo());
					ps4.setInt(3, conf.getQtd());
					ps4.setInt(4, conf.getGrupo().getIdGrupo());
					ps4.setInt(5, confParte1.getIdConfiAgenda());
					ps4.execute();

				}
			} else {
				if (confParte1.getDiaDaSemana() != null) {
					ps2.setInt(2, confParte1.getDiaDaSemana());
					ps2.setDate(4, null);
				} else {
					ps2.setInt(2, 0);
					ps2.setDate(4, new Date(confParte1.getDataEspecifica()
							.getTime()));
				}
				ps2.setInt(1, confParte1.getProfissional().getIdProfissional());
				ps2.setInt(3, confParte1.getQtdMax());
				ps2.setString(5, confParte1.getTurno());
				ps2.setInt(6, confParte1.getMes());
				ps2.setInt(7, confParte1.getAno());
				ps2.setInt(8, 0);// COD EMPRESA ?
				ps2.setInt(9, confParte1.getIdConfiAgenda());
				ps2.execute();

				String sql2 = "DELETE from hosp.tipo_atend_agenda where codconfigagenda = ?";
				PreparedStatement ps3 = con.prepareStatement(sql2);
				ps3.setInt(1, confParte1.getIdConfiAgenda());
				ps3.execute();

				String sql3 = "INSERT INTO hosp.tipo_atend_agenda (codprograma, codtipoatendimento,  qtd, codgrupo, codconfigagenda) "
						+ " VALUES (?, ?, ?, ?, ?); ";

				PreparedStatement ps4 = con.prepareStatement(sql3);
				for (ConfigAgendaParte2Bean conf : listaTipos) {
					ps4.setInt(1, conf.getPrograma().getIdPrograma());
					ps4.setInt(2, conf.getTipoAt().getIdTipo());
					ps4.setInt(3, conf.getQtd());
					ps4.setInt(4, conf.getGrupo().getIdGrupo());
					ps4.setInt(5, confParte1.getIdConfiAgenda());
					ps4.execute();
				}
			}
			con.commit();
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public Boolean alterarTurnoEquipe(ConfigAgendaParte1Bean confParte1,
			List<ConfigAgendaParte2Bean> listaTipos, String dia)
			throws SQLException, ProjetoException {

		String sql = "UPDATE hosp.config_agenda_equipe SET codequipe = ?, diasemana = ?, qtdmax = ?, dataagenda = ?, turno = ?, mes = ?, ano = ?, codempresa = ? "
				+ " WHERE id_configagenda = ?;";
		con = ConnectionFactory.getConnection();
		PreparedStatement ps2 = con.prepareStatement(sql);
		try {
			if (confParte1.getTurno().equals("A")) {
				if (confParte1.getDiaDaSemana() != null) {
					ps2.setInt(2, confParte1.getDiaDaSemana());
					ps2.setDate(4, null);
				} else {
					ps2.setInt(2, 0);
					ps2.setDate(4, new Date(confParte1.getDataEspecifica()
							.getTime()));
				}
				ps2.setInt(1, confParte1.getEquipe().getCodEquipe());
				ps2.setInt(3, confParte1.getQtdMax());
				ps2.setString(5, "M");
				ps2.setInt(6, confParte1.getMes());
				ps2.setInt(7, confParte1.getAno());
				ps2.setInt(8, 0);// COD EMPRESA ?
				ps2.setInt(9, confParte1.getIdConfiAgenda());
				ps2.executeUpdate();

				ps2 = con.prepareStatement(sql);
				if (confParte1.getDiaDaSemana() != null) {
					ps2.setInt(2, confParte1.getDiaDaSemana());
					ps2.setDate(4, null);
				} else {
					ps2.setInt(2, 0);
					ps2.setDate(4, new Date(confParte1.getDataEspecifica()
							.getTime()));
				}
				ps2.setInt(1, confParte1.getEquipe().getCodEquipe());
				ps2.setInt(3, confParte1.getQtdMax());
				ps2.setString(5, "T");
				ps2.setInt(6, confParte1.getMes());
				ps2.setInt(7, confParte1.getAno());
				ps2.setInt(8, 0);// COD EMPRESA ?
				ps2.setInt(9, confParte1.getIdConfiAgenda());
				ps2.executeUpdate();

			} else {
				if (confParte1.getDiaDaSemana() != null) {
					ps2.setInt(2, confParte1.getDiaDaSemana());
					ps2.setDate(4, null);
				} else {
					ps2.setInt(2, 0);
					ps2.setDate(4, new Date(confParte1.getDataEspecifica()
							.getTime()));
				}
				ps2.setInt(1, confParte1.getEquipe().getCodEquipe());
				ps2.setInt(3, confParte1.getQtdMax());
				ps2.setString(5, confParte1.getTurno());
				ps2.setInt(6, confParte1.getMes());
				ps2.setInt(7, confParte1.getAno());
				ps2.setInt(8, 0);// COD EMPRESA ?
				ps2.setInt(9, confParte1.getIdConfiAgenda());
				ps2.executeUpdate();

			}
			con.commit();

			return true;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	// SEM USO, RETIREI
	// public void alteraTipoAtendAgenda(ConfigAgendaParte1Bean conf1,
	// List<ConfigAgendaParte2Bean> listaTipos) throws ProjetoException {
	// PreparedStatement ps1 = null;
	//
	// String sql =
	// "UPDATE hosp.tipo_atend_agenda SET codprograma = ?, codtipoatendimento = ?,  qtd = ?, codgrupo = ? "
	// + " WHERE codconfigagenda = ?;";
	// try {
	// con = ConnectionFactory.getConnection();
	// ps1 = con.prepareStatement(sql);
	// for (ConfigAgendaParte2Bean conf : listaTipos) {
	// ps1.setInt(1, conf.getPrograma().getIdPrograma());
	// ps1.setInt(2, conf.getTipoAt().getIdTipo());
	// ps1.setInt(3, conf.getQtd());
	// ps1.setInt(4, conf.getGrupo().getIdGrupo());
	// ps1.setInt(5, conf1.getIdConfiAgenda());
	// ps1.executeUpdate();
	// con.commit();
	// }
	//
	// } catch (SQLException ex) {
	// throw new RuntimeException(ex);
	// } finally {
	// try {
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// System.exit(1);
	// }
	// }
	// }

}
