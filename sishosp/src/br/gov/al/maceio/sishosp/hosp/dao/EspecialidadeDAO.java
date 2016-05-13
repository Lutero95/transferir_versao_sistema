package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
	
	public List<EspecialidadeBean> listarEspecialidades(){
		List<EspecialidadeBean> lista = new ArrayList<>();
		String sql = "select id_especialidade, descespecialidade, codempresa from hosp.especialidade";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
            	EspecialidadeBean esp = new EspecialidadeBean();
            	esp.setCodEspecialidade(rs.getInt("id_especialidade"));
            	esp.setDescEspecialidade(rs.getString("descespecialidade"));    
            	esp.setCodEmpresa(rs.getInt("codempresa"));
                
                lista.add(esp);
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
