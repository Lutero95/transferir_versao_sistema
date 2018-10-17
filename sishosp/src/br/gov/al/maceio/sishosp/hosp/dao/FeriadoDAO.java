package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.FeriadoBean;

public class FeriadoDAO {

	Connection con = null;
	PreparedStatement ps = null;

	public boolean gravarFeriado(FeriadoBean feriado) {
		Boolean retorno = false;
		String sql = "insert into hosp.feriado (descferiado, dataferiado) values (?, ?);";
		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, feriado.getDescFeriado().toUpperCase());
			ps.setDate(2, new java.sql.Date(feriado.getDataFeriado().getTime()));
			ps.execute();
			con.commit();
			retorno = true;
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return retorno;
		}
	}

	public Boolean alterarFeriado(FeriadoBean feriado) {
		Boolean retorno = false;
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
			retorno = true;
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return retorno;
		}
	}

	public Boolean excluirFeriado(FeriadoBean feriado) {
		Boolean retorno = false;
		String sql = "delete from hosp.feriado where codferiado = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setLong(1, feriado.getCodFeriado());
			stmt.execute();
			con.commit();
			retorno = true;
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return retorno;
		}
	}

	public List<FeriadoBean> listarFeriado() throws ProjetoException {
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
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return lista;
	}

	public FeriadoBean listarFeriadoPorId(int id) throws ProjetoException {

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
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return feriado;
	}
}
