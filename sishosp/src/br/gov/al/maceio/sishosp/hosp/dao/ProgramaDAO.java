package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;

public class ProgramaDAO {
	
	Connection con = null;
	PreparedStatement ps = null;
	
	public boolean gravarPrograma(ProgramaBean prog) throws SQLException{
		
		String sql = "insert into hosp.programa (descprograma, codfederal) values (?, ?);";
		try {
			System.out.println("VAI CADASTRAR PROG");
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, prog.getDescPrograma().toUpperCase());
			ps.setDouble(2, prog.getCodFederal());
			ps.execute();
			con.commit();
			con.close();
			System.out.println("CADASTROU PROG");
			return true;
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}

}
