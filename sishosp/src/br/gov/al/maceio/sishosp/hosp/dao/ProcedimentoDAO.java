package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
}
