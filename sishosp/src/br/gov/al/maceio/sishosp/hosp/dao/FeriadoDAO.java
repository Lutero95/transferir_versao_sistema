package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.FeriadoBean;

public class FeriadoDAO {

	Connection con = null;
	PreparedStatement ps = null;

	public boolean gravarFeriado(FeriadoBean feriado) throws SQLException {

		String sql = "insert into hosp.feriado (descferiado, dataferiado) values (?, ?);";
		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, feriado.getDescFeriado().toUpperCase());
			ps.setDate(2, new java.sql.Date(feriado.getDataFeriado().getTime()));
			ps.execute();
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

	public List<FeriadoBean> listarFeriado() {
		List<FeriadoBean> lista = new ArrayList<>();
		String sql = "select codferiado, descferiado, dataferiado from hosp.feriado order by codferiado ";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				FeriadoBean feriado = new FeriadoBean();
				feriado.setCodFeriado(rs.getInt("codferiado"));
				feriado.setDescFeriado(rs.getString("descferiado"));
				feriado.setDataFeriado(rs.getDate("dataferiado"));

				lista.add(feriado);
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

	public FeriadoBean listarFeriadoPorId(int id) {

		FeriadoBean feriado = new FeriadoBean();
		String sql = "select codferiado, descferiado, dataferiado from hosp.feriado where codferiado = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, id);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				feriado = new FeriadoBean();
				feriado.setCodFeriado(rs.getInt("codferiado"));
				feriado.setDescFeriado(rs.getString("descferiado"));
				feriado.setDataFeriado(rs.getDate("dataferiado"));
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
		return feriado;
	}

	public List<FeriadoBean> listarFeriadoBusca(String descricao, Integer tipo,
			Date data) {
		List<FeriadoBean> lista = new ArrayList<>();

		try {
			con = ConnectionFactory.getConnection();
			String sql = "select codferiado, descferiado, dataferiado from hosp.feriado";
			PreparedStatement stm = null;

			if (tipo == 1) {
				sql += " where descferiado LIKE ? order by codferiado";
				stm = con.prepareStatement(sql);
				stm.setString(1, "%" + descricao.toUpperCase() + "%");
				
			} else if (tipo == 2) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(data);
				int ano = cal.get(Calendar.YEAR);
				int mes = cal.get(Calendar.MONTH) + 1;
				sql += " where EXTRACT(year from dataferiado) = ? and EXTRACT (month from dataferiado) = ?";
				stm = con.prepareStatement(sql);
				stm.setInt(1, ano);
				stm.setInt(2, mes);
			}

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				FeriadoBean feriado = new FeriadoBean();
				feriado.setCodFeriado(rs.getInt("codferiado"));
				feriado.setDescFeriado(rs.getString("descferiado"));
				feriado.setDataFeriado(rs.getDate("dataferiado"));

				lista.add(feriado);
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

	public Boolean alterarFeriado(FeriadoBean feriado) throws ProjetoException {
		String sql = "update hosp.feriado set descferiado = ?, dataferiado = ? where codferiado = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, feriado.getDescFeriado().toUpperCase());
			stmt.setDate(2, new java.sql.Date(feriado.getDataFeriado()
					.getTime()));
			stmt.setInt(3, feriado.getCodFeriado());
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

	public Boolean excluirFeriado(FeriadoBean feriado) throws ProjetoException {
		String sql = "delete from hosp.feriado where codferiado = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setLong(1, feriado.getCodFeriado());
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
}
