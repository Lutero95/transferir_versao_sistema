package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;

public class GrupoDAO {
	
	Connection con = null;
	PreparedStatement ps = null;
	
	public boolean gravarGrupo(GrupoBean grupo) throws SQLException{
		
		String sql = "insert into hosp.grupo (descgrupo, qtdfrequencia) values (?, ?);";
		try {
			System.out.println("VAI CADASTRAR Grupo");
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, grupo.getDescGrupo().toUpperCase());
			ps.setInt(2, grupo.getQtdFrequencia());
			ps.execute();
			con.commit();
			con.close();
			System.out.println("CADASTROU grupo");
			return true;
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public List<GrupoBean> listarGruposPorPrograma(int codPrograma){
		List<GrupoBean> lista = new ArrayList<>();
		String sql = "select g.id_grupo, g.descgrupo, g.qtdfrequencia from hosp.grupo g, hosp.grupo_programa gp, hosp.programa p"
				+" where p.id_programa = ? and g.id_grupo = gp.codgrupo and p.id_programa = gp.codprograma";
        try {
        	System.out.println("CHEGOU AQUI");
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1,  codPrograma);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
            	GrupoBean grupo = new GrupoBean();
                grupo.setIdGrupo(rs.getInt("id_grupo"));
                grupo.setDescGrupo(rs.getString("descgrupo"));    
                grupo.setQtdFrequencia(rs.getInt("qtdfrequencia")); 
                
                lista.add(grupo);
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
	
	public List<GrupoBean> listarGrupos(){
		List<GrupoBean> lista = new ArrayList<>();
		String sql = "select g.id_grupo, g.descgrupo, g.qtdfrequencia from hosp.grupo g";
        try {
        	System.out.println("CHEGOU AQUI");
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
            	GrupoBean grupo = new GrupoBean();
                grupo.setIdGrupo(rs.getInt("id_grupo"));
                grupo.setDescGrupo(rs.getString("descgrupo"));    
                grupo.setQtdFrequencia(rs.getInt("qtdfrequencia")); 
                
                lista.add(grupo);
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
