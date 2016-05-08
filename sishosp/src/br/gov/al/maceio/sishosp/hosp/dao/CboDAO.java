package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.CboBean;

public class CboDAO {
	
	Connection con = null;
	PreparedStatement ps = null;
	
	public boolean gravarCBO(CboBean cbo) throws SQLException{
		
		String sql = "insert into hosp.cbo (id, descricao) values (?, ?);";
		try {
			System.out.println("VAI CADASTRAR CBO");
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, cbo.getCodCbo());
			ps.setString(2, cbo.getDescCbo());
			//ps.setInt(3,  cbo.getCodEmpresa());
			ps.execute();
			con.commit();
			con.close();
			System.out.println("CADASTROU CBO");
			return true;
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}
}
