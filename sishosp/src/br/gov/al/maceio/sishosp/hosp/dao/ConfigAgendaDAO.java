package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.ConfigAgendaParte1Bean;
import br.gov.al.maceio.sishosp.hosp.model.ConfigAgendaParte2Bean;
import br.gov.al.maceio.sishosp.hosp.model.TipoAtendimentoBean;

public class ConfigAgendaDAO {

	Connection con = null;
	// PreparedStatement ps = null;
	ProfissionalDAO pDao = new ProfissionalDAO();

	public boolean gravarConfigAgenda(ConfigAgendaParte1Bean confParte1,
			ConfigAgendaParte2Bean confParte2,
			List<ConfigAgendaParte2Bean> listaTipos) throws SQLException {

		if (confParte1.getProfissional().getIdProfissional() == null
				|| confParte1.getQtdMax() == null
				|| confParte1.getAno() == null) {
			return false;
		}

		try {
			System.out.println("VAI CADASTRAR CONFIG AGENDA");
			if (confParte1.getDiasSemana().size() > 0) {// ESCOLHEU DIAS SEMANA
				for (String dia : confParte1.getDiasSemana()) {
					gravaTurno(confParte1, listaTipos, dia);
				}
			} else {// ESCOLHEU DATA ESPECIFICA
				gravaTurno(confParte1, listaTipos, null);
			}

			System.out.println("CADASTROU CONFIG AGENDA");
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
			List<ConfigAgendaParte2Bean> listaTipos) {
		PreparedStatement ps1 = null;
		String sql = "insert into hosp.tipo_atend_agenda (codconfigagenda, codprograma, codtipoatendimento, qtd, codempresa) "
				+ " values(?, ?, ?, ?, ?);";
		try {
			con = ConnectionFactory.getConnection();
			ps1 = con.prepareStatement(sql);
			System.out.println("VAI GRAVAR NA TAB ASDASD");
			for (ConfigAgendaParte2Bean conf : listaTipos) {
				ps1.setInt(1, codConf);
				ps1.setInt(2, conf.getPrograma().getIdPrograma());
				ps1.setInt(3, conf.getTipoAt().getIdTipo());
				ps1.setInt(4, conf.getQtd());
				ps1.setInt(5, 0);// COD EMPRESA ?
				ps1.execute();
				con.commit();
			}

			System.out.println("GRAVOU NA TAB ASDASD");

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
	

	public void gravaTurno(ConfigAgendaParte1Bean confParte1,
			List<ConfigAgendaParte2Bean> listaTipos, String dia)
			throws SQLException {

		String sql = "INSERT INTO hosp.config_agenda(codmedico, diasemana, "
				+ "  qtdmax, dataagenda, turno, mes, ano, codempresa, id_configagenda) "
				+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, DEFAULT) RETURNING id_configagenda;";
		con = ConnectionFactory.getConnection();
		PreparedStatement ps2 = con.prepareStatement(sql);

		ResultSet rs2 = null;

		if (confParte1.getTurno().equals("A")) {
			System.out.println("VAI GRAVAR  A");
			if (dia != null) {
				System.out.println("eh dia a da semana > " + dia);
				ps2.setInt(2, Integer.parseInt(dia));
				ps2.setDate(4, null);
			} else {
				System.out.println("eh data especifica > "
						+ confParte1.getDataEspecifica());
				ps2.setInt(2, 0);
				ps2.setDate(4, new Date(confParte1.getDataEspecifica()
						.getTime()));
			}
			System.out.println("1");
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
				System.out.println("eh dia a da semana > " + dia);
				ps2.setInt(2, Integer.parseInt(dia));
				ps2.setDate(4, null);
			} else {
				System.out.println("eh data especifica > "
						+ confParte1.getDataEspecifica());
				ps2.setInt(2, 0);
				ps2.setDate(4, new Date(confParte1.getDataEspecifica()
						.getTime()));
			}
			ps2.setInt(1, confParte1.getProfissional().getIdProfissional());
			ps2.setInt(3, confParte1.getQtdMax());
			ps2.setString(5, "T");
			System.out.println("32");
			ps2.setInt(6, confParte1.getMes());
			ps2.setInt(7, confParte1.getAno());
			ps2.setInt(8, 0);// COD EMPRESA ?
			System.out.println("33");
			rs2 = ps2.executeQuery();
			con.commit();
			System.out.println("3");
			int idTipo2 = 0;
			if (rs2.next()) {
				idTipo2 = rs2.getInt("id_configagenda");
				insereTipoAtendAgenda(idTipo2, confParte1, listaTipos);

			}
			con.commit();
			System.out.println("4");

		} else {
			if (dia != null) {
				ps2.setInt(2, Integer.parseInt(dia));
				ps2.setDate(4, null);
			} else {
				ps2.setInt(2, 0);
				ps2.setDate(4, new Date(confParte1.getDataEspecifica()
						.getTime()));
			}
			System.out.println("GRAVA TURNO NORMAL");
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
	}

	public List<ConfigAgendaParte1Bean> listarHorarios() {
		List<ConfigAgendaParte1Bean> lista = new ArrayList<>();
		String sql = "SELECT id_configagenda, codmedico, diasemana, horainicio, horafim, qtdmax, dataagenda,"
				+ " turno, mes, ano, codempresa FROM hosp.config_agenda order by id_configagenda ";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				ConfigAgendaParte1Bean conf = new ConfigAgendaParte1Bean();
				conf.setIdConfiAgenda(rs.getInt("id_configagenda"));
				conf.setProfissional(pDao.buscarProfissionalPorID(rs
						.getInt("codmedico")));
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

	public List<ConfigAgendaParte1Bean> listarHorariosPorIDProfissional(int id) {
		List<ConfigAgendaParte1Bean> lista = new ArrayList<>();
		String sql = "SELECT id_configagenda, codmedico, diasemana, horainicio, horafim, qtdmax, dataagenda,"
				+ " turno, mes, ano, codempresa FROM hosp.config_agenda where codmedico = ? order by id_configagenda ";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, id);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				ConfigAgendaParte1Bean conf = new ConfigAgendaParte1Bean();
				conf.setIdConfiAgenda(rs.getInt("id_configagenda"));
				conf.setProfissional(pDao.buscarProfissionalPorID(rs
						.getInt("codmedico")));
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

	public boolean excluirConfig(ConfigAgendaParte1Bean confParte1) {
		System.out.println("ID "+confParte1.getIdConfiAgenda());
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
	public void excluirTabelaTipoAgenda(int id) {
		
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

}
