package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.EspecialidadeBean;

public class EspecialidadeDAO {
	
	Connection con = null;
	PreparedStatement ps = null;
	
	public boolean gravarEspecialidade(EspecialidadeBean esp) throws SQLException{
		
		String sql = "insert into hosp.especialidade (descespecialidade) values (?);";
		try {
			System.out.println("VAI CADASTRAR Especialidade");
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, esp.getDescEspecialidade().toUpperCase());
			ps.execute();
			con.commit();
			con.close();
			System.out.println("CADASTROU Especialidade");
			return true;
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}
	
}
