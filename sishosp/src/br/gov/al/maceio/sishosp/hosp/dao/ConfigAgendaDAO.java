package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.ConfigAgendaParte1Bean;
import br.gov.al.maceio.sishosp.hosp.model.ConfigAgendaParte2Bean;

public class ConfigAgendaDAO {

	Connection con = null;
	PreparedStatement ps = null;
	ProfissionalDAO pDao = new ProfissionalDAO();

	public boolean gravarConfigAgenda(ConfigAgendaParte1Bean confParte1,
			ConfigAgendaParte2Bean confParte2) throws SQLException {

		if (confParte1.getProfissional().getIdProfissional() == null
				|| confParte1.getQtdMax() == null
				|| confParte1.getAno() == null) {
			return false;
		}

		String sql = "INSERT INTO hosp.config_agenda(codmedico, diasemana, "
				+ "  qtdmax, dataagenda, turno, mes, ano, codempresa)"
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
		try {
			System.out.println("VAI CADASTRAR CONFIG AGENDA");
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);

			if (confParte1.getDiasSemana().size() > 0) {// ESCOLHEU OS DIAS DA
														// SEMANA
				for (String dia : confParte1.getDiasSemana()) {
					System.out.println("DIA > " + dia);
					gravaTurno(confParte1, dia);
				}
			} else {// ESCOLHEU DATA ESPECIFICA
				gravaTurno(confParte1, null);
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

	public void gravaTurno(ConfigAgendaParte1Bean confParte1, String dia)
			throws SQLException {
		if (dia != null) {
			if (confParte1.getTurno().equals("A")) {
				// TURNO MANHA
				ps.setInt(1, confParte1.getProfissional().getIdProfissional());
				ps.setInt(2, Integer.parseInt(dia));
				ps.setInt(3, confParte1.getQtdMax());
				ps.setDate(4, null);
				ps.setString(5, "M");
				ps.setInt(6, confParte1.getMes());
				ps.setInt(7, confParte1.getAno());
				ps.setInt(8, 0);// COD EMPRESA ?
				ps.execute();
				con.commit();
				// TURNO TARDE
				ps.setInt(1, confParte1.getProfissional().getIdProfissional());
				ps.setInt(2, Integer.parseInt(dia));
				ps.setInt(3, confParte1.getQtdMax());
				ps.setDate(4, null);
				ps.setString(5, "T");
				ps.setInt(6, confParte1.getMes());
				ps.setInt(7, confParte1.getAno());
				ps.setInt(8, 0);// COD EMPRESA ?
				ps.execute();
				con.commit();
			} else {
				ps.setInt(1, confParte1.getProfissional().getIdProfissional());
				ps.setInt(2, Integer.parseInt(dia));
				ps.setInt(3, confParte1.getQtdMax());
				ps.setDate(4, null);
				ps.setString(5, confParte1.getTurno());
				ps.setInt(6, confParte1.getMes());
				ps.setInt(7, confParte1.getAno());
				ps.setInt(8, 0);// COD EMPRESA ?
				ps.execute();
				con.commit();
			}
		} else {
			ps.setInt(1, confParte1.getProfissional().getIdProfissional());
			ps.setInt(2, 0);
			ps.setInt(3, confParte1.getQtdMax());
			ps.setDate(4, new Date(confParte1.getDataEspecifica().getTime()));
			ps.setString(5, confParte1.getTurno());
			ps.setInt(6, confParte1.getMes());
			ps.setInt(7, confParte1.getAno());
			ps.setInt(8, 0);// COD EMPRESA ?
			ps.execute();
			con.commit();
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
            	conf.setProfissional(pDao.buscarProfissionalPorID(rs.getInt("codmedico")));    
            	conf.setDiaDaSemana(rs.getInt("diasemana"));
            	//horainicio
            	//horafim
            	conf.setDataEspecifica(rs.getDate("dataagenda"));
            	conf.setQtdMax(rs.getInt("qtdmax"));
            	conf.setTurno(rs.getString("turno"));    
            	conf.setMes(rs.getInt("mes"));
            	conf.setAno(rs.getInt("ano"));
            	//codempresa
                
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
            	conf.setProfissional(pDao.buscarProfissionalPorID(rs.getInt("codmedico")));    
            	conf.setDiaDaSemana(rs.getInt("diasemana"));
            	//horainicio
            	//horafim
            	conf.setDataEspecifica(rs.getDate("dataagenda"));
            	conf.setQtdMax(rs.getInt("qtdmax"));
            	conf.setTurno(rs.getString("turno"));    
            	conf.setMes(rs.getInt("mes"));
            	conf.setAno(rs.getInt("ano"));
            	//codempresa
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
	
	

}
