package br.gov.al.maceio.sishosp.hosp.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;

import javax.faces.context.FacesContext;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.EscolaBean;
import br.gov.al.maceio.sishosp.hosp.model.PacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.RacaBean;



public class RacaDAO {
	private Connection conexao = null;
	// COMEÇO DO CODIGO
	
            @SuppressWarnings("deprecation")
			public Boolean cadastrar(RacaBean raca) throws ProjetoException {
                boolean cadastrou = false;
               

                /*PacienteBean user_session = (PacienteBean) FacesContext
                        .getCurrentInstance().getExternalContext().getSessionMap()
                        .get("obj_paciente");*/
                
                
                String sql = "insert into hosp.raca (descraca) values (?)";

                    try { 
                    	conexao = ConnectionFactory.getConnection();
                        PreparedStatement stmt = conexao.prepareStatement(sql);
                        stmt.setString(1, raca.getDescRaca().toUpperCase().trim());
                        
                        
                        //ResultSet rs = stmt.executeQuery();  
                       /* if(rs.next()) { 
                    PacienteBean p = paciente;
                    String idRetorno = null;
                    idRetorno = String.valueOf(rs.getLong("id_paciente"));
                    p.setId_paciente(Long.parseLong(idRetorno));
                    System.out.println("|THU1|"+ idRetorno);
                    }*/
                    stmt.execute();   
                    conexao.commit();
                    cadastrou = true;
                    conexao.close();

                    return cadastrou;
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            
            public Boolean alterar(RacaBean raca) throws ProjetoException {
                boolean alterou = false;
                String sql = "update hosp.raca set descraca = ? where id_raca = ?";
                try {
                    conexao = ConnectionFactory.getConnection();
                    PreparedStatement stmt = conexao.prepareStatement(sql);
                    stmt.setString(1, raca.getDescRaca());
                    stmt.executeUpdate();

                    conexao.commit();
                    
                    alterou = true;
                    
                    return alterou;
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                } finally {
                    try {
                        conexao.close();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
            
            public Boolean excluir(RacaBean raca) throws ProjetoException {
                boolean excluir = false;
                String sql =  "delete from hosp.raca where id_raca = ?";
                try {
                    conexao = ConnectionFactory.getConnection();
                    PreparedStatement stmt = conexao.prepareStatement(sql);
                    stmt.setLong(1, raca.getCodRaca());
                    stmt.execute();

                    
                    conexao.commit();
                    
                    excluir = true;
                    
                    return excluir;
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                } finally {
                    try {
                        conexao.close();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
            
            public ArrayList<RacaBean> listaCor() {

                String sql = "select  id_raca, descraca from hosp.raca order by descraca";

                ArrayList<RacaBean> lista = new ArrayList();

                try {
                    conexao = ConnectionFactory.getConnection();
                    PreparedStatement stm = conexao.prepareStatement(sql);
                    ResultSet rs = stm.executeQuery();

                    while (rs.next()) {
                    	RacaBean p = new RacaBean();
    	            	System.out.println("|1|");
    	                p.setCodRaca(rs.getInt("id_raca"));
    	                p.setDescRaca(rs.getString("descraca").toUpperCase());    
    	             
    	                
    	                lista.add(p);
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                } finally {
                    try {
                        conexao.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        System.exit(1);
                    }
                }
                return lista;
            }
   
            
}
