package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
	
	public List<ProgramaBean> listarProgramas(){
		List<ProgramaBean> lista = new ArrayList<>();
		String sql = "select id_programa, descprograma, codfederal from hosp.programa";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
            	ProgramaBean programa = new ProgramaBean();
            	programa.setIdPrograma(rs.getInt("id_programa"));
                programa.setDescPrograma(rs.getString("descprograma"));    
                programa.setCodFederal(rs.getDouble("codfederal"));
                
                lista.add(programa);
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
