package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.BloqueioBean;
import br.gov.al.maceio.sishosp.hosp.model.ProfissionalBean;

public class BloqueioDAO {
	Connection con = null;
	PreparedStatement ps = null;
	ProfissionalDAO pDao = new ProfissionalDAO();

	public boolean gravarBloqueio(BloqueioBean bloqueio) throws SQLException, ProjetoException {

		Calendar calendarData = Calendar.getInstance();
		boolean condicao = true;
		String sql = "insert into hosp.bloqueio_agenda (codmedico, dataagenda, turno, descricao, codempresa) "
				+ " values (?, ?, ?, ?, ?);";
		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			Date dataInicio = bloqueio.getDataInicio();
			Date dataFim = bloqueio.getDataFim();
			do {
				if (dataInicio.after(dataFim) == false) {
					ps.setInt(1, bloqueio.getProf().getIdProfissional());
					ps.setDate(2, new java.sql.Date(dataInicio.getTime()));
					ps.setString(3, bloqueio.getTurno().toUpperCase());
					ps.setString(4, bloqueio.getDescBloqueio().toUpperCase());
					ps.setInt(5, 0); // COD EMPRESA ?
					ps.execute();
					con.commit();
					condicao = true;
					calendarData.setTime(dataInicio);
					calendarData.add(Calendar.DATE, 1);
					dataInicio = calendarData.getTime();
				} else {
					condicao = false;
				}
			} while (condicao);
			con.commit();
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

	public List<BloqueioBean> listarBloqueio() throws ProjetoException {
		List<BloqueioBean> lista = new ArrayList<>();
		String sql = "select id_bloqueioagenda, codmedico,"
				+ " dataagenda, turno, descricao, codempresa from hosp.bloqueio_agenda order by id_bloqueioagenda ";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
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

	/*
	 * public BloqueioBean listarBloqueioPorId(int id) {
	 * 
	 * BloqueioBean bloqueio = new BloqueioBean(); String sql =
	 * "select codbloqueio, descbloqueio, databloqueio from hosp.bloqueio where codbloqueio = ?"
	 * ; try { con = ConnectionFactory.getConnection(); PreparedStatement stm =
	 * con.prepareStatement(sql); stm.setInt(1, id); ResultSet rs =
	 * stm.executeQuery(); while (rs.next()) { bloqueio = new BloqueioBean();
	 * bloqueio.setCodBloqueio(rs.getInt("codbloqueio"));
	 * bloqueio.setDescBloqueio(rs.getString("descbloqueio"));
	 * bloqueio.setDataBloqueio(rs.getDate("databloqueio")); }
	 * 
	 * } catch (SQLException ex) { throw new RuntimeException(ex); } finally {
	 * try { con.close(); } catch (Exception ex) { ex.printStackTrace();
	 * System.exit(1); } } return bloqueio; }
	 */
	public List<BloqueioBean> listarBloqueioPorProfissional(ProfissionalBean prof) throws ProjetoException {
		List<BloqueioBean> lista = new ArrayList<>();
		String sql = "select id_bloqueioagenda, codmedico,"
				+ " dataagenda, turno, descricao, codempresa from hosp.bloqueio_agenda where codmedico = ? order by id_bloqueioagenda";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, prof.getIdProfissional());
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

	public Boolean alterarBloqueio(BloqueioBean bloqueio)
			throws ProjetoException {
		String sql = "update hosp.bloqueio_agenda set codmedico = ?,"
				+ " dataagenda = ?,turno = ?, descricao = ?"
				+ " where id_bloqueioagenda = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			
			System.out.println(bloqueio.getProf().getIdProfissional());
			System.out.println(bloqueio.getDataInicio());
			System.out.println(bloqueio.getTurno().toUpperCase());
			System.out.println(bloqueio.getDescBloqueio().toUpperCase());
			System.out.println(bloqueio.getIdBloqueio());
			
			stmt.setInt(1, bloqueio.getProf().getIdProfissional());
			stmt.setDate(2, new java.sql.Date(bloqueio.getDataInicio()
					.getTime()));
			stmt.setString(3, bloqueio.getTurno().toUpperCase());
			stmt.setString(4, bloqueio.getDescBloqueio().toUpperCase());
			stmt.setInt(5, bloqueio.getIdBloqueio());
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

	public Boolean excluirBloqueio(BloqueioBean bloqueio)
			throws ProjetoException {
		String sql = "delete from hosp.bloqueio_agenda where id_bloqueioagenda = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setLong(1, bloqueio.getIdBloqueio());
			stmt.execute();
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
	
	public List<BloqueioBean> verificarBloqueioProfissional(ProfissionalBean prof, Date dataAtendimento, String turno) throws ProjetoException {
		List<BloqueioBean> lista = new ArrayList<>();
		String sql = "select id_bloqueioagenda, codmedico,"
				+ " dataagenda, turno, descricao, codempresa "
				+ " from hosp.bloqueio_agenda "
				+ " where codmedico = ? and  dataagenda = ? and turno = ?"
				+ " order by id_bloqueioagenda";
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
}
