package br.gov.al.maceio.sishosp.hosp.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import java.util.List;

import javax.faces.context.FacesContext;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.EncaminhadoBean;





public class EncaminhadoDAO {
	private Connection conexao = null;
	// COMEÇO DO CODIGO
	
            public Boolean cadastrar(EncaminhadoBean encaminhado) throws ProjetoException {
                boolean cadastrou = false;
                System.out.println("passou aqui 2");

                /*PacienteBean user_session = (PacienteBean) FacesContext
                        .getCurrentInstance().getExternalContext().getSessionMap()
                        .get("obj_paciente");*/

                String sql = "insert into hosp.encaminhado (descencaminhado, dtacadastro)"
                		+ " values (?, CURRENT_TIMESTAMP)";
                //returning id_paciente
                    try {
                    	System.out.println("passou aqui 3");
                        conexao = ConnectionFactory.getConnection();
                        PreparedStatement stmt = conexao.prepareStatement(sql);
                        stmt.setString(1, encaminhado.getDescencaminhado().toUpperCase().trim());
                        //stmt.setString(2, paciente.getCpf().replaceAll("[^0-9]", ""));  
                        //stmt.setBoolean(3, true);
                        //stmt.setInt(3, paciente.getIdpessoa());
                        /*ResultSet rs = stmt.executeQuery();  
                        if(rs.next()) { 
                    PacienteBean p = paciente;
                    String idRetorno = null;
                    idRetorno = String.valueOf(rs.getLong("id_usuario"));
                    p.setId_paciente(Long.parseLong(idRetorno));

                    }*/
                    stmt.execute();   
                    System.out.println("passou aqui 4");
                    conexao.commit();
                    conexao.close();

                    return cadastrou;
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            
            public Boolean alterar(EncaminhadoBean encaminhado) throws ProjetoException {
                boolean alterou = false;
                String sql = "update hosp.encaminhado set descencaminhado = ? where codencaminhado = ?";
                try {
                    conexao = ConnectionFactory.getConnection();
                    PreparedStatement stmt = conexao.prepareStatement(sql);
                    stmt.setString(1, encaminhado.getDescencaminhado());
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
            public Boolean excluir(EncaminhadoBean encaminhado) throws ProjetoException {
                boolean excluir = false;
                String sql =  "delete from hosp.encaminhado where codencaminhado = ?";
                try {
                    conexao = ConnectionFactory.getConnection();
                    PreparedStatement stmt = conexao.prepareStatement(sql);
                    stmt.setLong(1, encaminhado.getCodencaminhado());
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
            
            public ArrayList<EncaminhadoBean> listaEncaminhados() {

                String sql = "select * from hosp.encaminhado order by codencaminhado,descencaminhado";

                ArrayList<EncaminhadoBean> lista = new ArrayList();

                try {
                    conexao = ConnectionFactory.getConnection();
                    PreparedStatement stm = conexao.prepareStatement(sql);
                    ResultSet rs = stm.executeQuery();

                    while (rs.next()) {
                    	EncaminhadoBean p = new EncaminhadoBean();
    	            	
    	                p.setCodencaminhado(rs.getInt("codencaminhado"));
    	                p.setDescencaminhado(rs.getString("descencaminhado").toLowerCase());
	                
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
            
            public EncaminhadoBean buscaencaminhadocodigo (Integer i) throws ProjetoException {
        		System.out.println("buscaencaminhadocodigo");
        		PreparedStatement ps = null;
        		 conexao = ConnectionFactory.getConnection();

        		try {
        			  
        			String sql = "select codencaminhado, descencaminhado from hosp.encaminhado where codencaminhado=? order by descencaminhado";
        			 
        			ps = conexao.prepareStatement(sql);
        			ps.setInt(1, i);
        			ResultSet rs = ps.executeQuery();

        			
        			EncaminhadoBean encaminhado = new EncaminhadoBean();
        			while (rs.next()) {
        			
        				encaminhado.setCodencaminhado(rs.getInt(1));
        				encaminhado.setDescencaminhado(rs.getString(2));
        			
        			
        			}
        			return encaminhado;
        		} catch (Exception sqle) {

        			throw new ProjetoException(sqle);

        		} finally {
        			try {
        				conexao.close();
        			} catch (Exception sqlc) {
        				sqlc.printStackTrace();
        				System.exit(1);
        				// TODO: handle exception
        			}

        		}
        	}	
            
            public List<EncaminhadoBean> buscaencaminhado(String s) throws ProjetoException {
            	System.out.println("buscaencaminhado");
            			PreparedStatement ps = null;
            			conexao = ConnectionFactory.getConnection();

            			try {
            				List<EncaminhadoBean> listaencaminhados = new ArrayList<EncaminhadoBean>();  
            				String sql = "select codencaminhado, descencaminhado from hosp.encaminhado where upper(descencaminhado) like ? order by descencaminhado";
            				 
            				ps = conexao.prepareStatement(sql);
            				ps.setString(1, "%"+s.toUpperCase()+"%");
            				ResultSet rs = ps.executeQuery();

            				List<EncaminhadoBean> colecao = new ArrayList<EncaminhadoBean>();
            				
            				while (rs.next()) {
            					
            					EncaminhadoBean encaminhado = new EncaminhadoBean();
            					encaminhado.setCodencaminhado(rs.getInt(1));
            					encaminhado.setDescencaminhado(rs.getString(2));
            					colecao.add(encaminhado);
            					
            	;
            				
            				}
            				return colecao;
            			} catch (Exception sqle) {

            				throw new ProjetoException(sqle);

            			} finally {
            				try {
            					conexao.close();
            				} catch (Exception sqlc) {
            					sqlc.printStackTrace();
            					System.exit(1);
            					// TODO: handle exception
            				}

            			}
            		}	
  
            
}
