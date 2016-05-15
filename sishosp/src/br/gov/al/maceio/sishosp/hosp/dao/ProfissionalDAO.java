package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.ProfissionalBean;

public class ProfissionalDAO {
	
	Connection con = null;
	PreparedStatement ps = null;
	
	public boolean gravarProfissional(ProfissionalBean prof) throws SQLException{
		
		System.out.println(prof.toString());
		return true;
		/*String sql = "insert into hosp.medicos (descmedico, codprograma, "
				+ "codespecialidade, cns, ativo, codcbo, codprocedimentopadrao, "
				+ "codprocedimentopadrao2, codempresa)"
				+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?);";
		if(prof.getPrograma().getIdPrograma()!=null&& prof.getEspecialidade().getCodEmpresa()!=null
		&& prof.getCbo().getCodCbo()!=null && prof.getProc1().getCodProc()!=null
		|| prof.getProc2().getCodProc()!=null && !prof.getDescricaoProf().isEmpty() && !prof.getCns().isEmpty()){
			try {
				System.out.println("VAI CADASTRAR Profissional");
				con = ConnectionFactory.getConnection();
				ps = con.prepareStatement(sql);
				ps.setString(1, prof.getDescricaoProf().toUpperCase());
				ps.setInt(2, prof.getPrograma().getIdPrograma());
				ps.setInt(3,  prof.getEspecialidade().getCodEspecialidade());
				ps.setString(4, prof.getCns().toUpperCase());
				ps.setBoolean(5,  prof.isAtivo());
				ps.setInt(6, prof.getCbo().getCodCbo());
				ps.setInt(7, prof.getProc1().getCodProc());
				ps.setInt(8, prof.getProc2().getCodProc());
				ps.setInt(9, 0);//COD EMPRESA ??
				ps.execute();
				con.commit();
				System.out.println("CADASTROU Profissional");
				return true;
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
		}
		return false;*/
	}
	/*
	public List<TipoAtendimentoBean> listarTipoAtPorGrupo(){
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
	} */

}
