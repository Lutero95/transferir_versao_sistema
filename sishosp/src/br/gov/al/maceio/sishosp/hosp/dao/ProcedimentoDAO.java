package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.ProcedimentoBean;

public class ProcedimentoDAO {
	Connection con = null;
	PreparedStatement ps = null;

	public boolean gravarProcedimento(ProcedimentoBean proc)
			throws SQLException {

		String sql = "INSERT INTO hosp.proc (codproc, nome, apac, bpi, auditivo, tipo_exame_auditivo, utiliza_equipamento)"
				+ " VALUES (?, ?, ?, ?, ?, ?, ?);";
		try {
			System.out.println("VAI CADASTRAR Procedimento");
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);

			ps.setInt(1, proc.getCodProc());
			ps.setString(2, proc.getNomeProc().toUpperCase());
			ps.setBoolean(3, proc.getApac());
			ps.setBoolean(4, proc.getBpi());
			ps.setBoolean(5, proc.getAuditivo());
			ps.setString(6, proc.getTipoExameAuditivo().toUpperCase());
			ps.setBoolean(7, proc.getUtilizaEquipamento());

			ps.execute();
			con.commit();
			con.close();
			System.out.println("CADASTROU Procedimento");
			return true;
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}

	public List<ProcedimentoBean> listarProcedimento() {
		List<ProcedimentoBean> lista = new ArrayList<>();
		String sql = "select codproc, nome from hosp.proc";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				ProcedimentoBean proc = new ProcedimentoBean();
				proc.setCodProc(rs.getInt("codproc"));
				proc.setNomeProc(rs.getString("nome"));
				lista.add(proc);
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

	public ProcedimentoBean listarProcedimentoPorId(int id) {

		ProcedimentoBean proc = new ProcedimentoBean();
		String sql = "select codproc, nome from hosp.proc where codproc = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, id);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				proc = new ProcedimentoBean();
				proc.setCodProc(rs.getInt("codproc"));
				proc.setNomeProc(rs.getString("nome"));

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
		return proc;
	}
}
