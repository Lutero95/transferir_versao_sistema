package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.TipoAtendimentoBean;

public class TipoAtendimentoDAO {
	
	Connection con = null;
	PreparedStatement ps = null;
	
	public boolean gravarTipoAt(TipoAtendimentoBean tipo) throws SQLException{
		
		String sql = "insert into hosp.tipoatendimento (codgrupo, desctipoatendimento, "
				+ "primeiroatendimento, equipe_programa) values (?, ?, ?, ?);";
		try {
			System.out.println("VAI CADASTRAR TIPO ATEN");
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, tipo.getIdGrupo());
			ps.setString(2, tipo.getDescTipoAt().toUpperCase());
			ps.setBoolean(3, tipo.isPrimeiroAt());
			ps.setBoolean(4, tipo.isEquipe());
			ps.execute();
			con.commit();
			con.close();
			System.out.println("CADASTROU TIPO ATEN");
			return true;
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public List<TipoAtendimentoBean> listarTipoAtPorGrupo(int codGrupo){
		List<TipoAtendimentoBean> lista = new ArrayList<>();
		String sql = "select t.id, t.codgrupo, t.desctipoatendimento, t.primeiroatendimento, t.codempresa, t.equipe_programa"
				+" from hosp.grupo g, hosp.tipoatendimento t"
				+" where g.id_grupo = ? and g.id_grupo = t.codgrupo";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1,  codGrupo);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
            	TipoAtendimentoBean tipo = new TipoAtendimentoBean();
            	tipo.setIdTipo(rs.getInt(1));
            	tipo.setIdGrupo(rs.getInt(2));
                tipo.setDescTipoAt(rs.getString(3));
                tipo.setPrimeiroAt(rs.getBoolean(4));
                tipo.setCodEmpresa(rs.getInt(5));
                tipo.setEquipe(rs.getBoolean(6));
                
                lista.add(tipo);
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
