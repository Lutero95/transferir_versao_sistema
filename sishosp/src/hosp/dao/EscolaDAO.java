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

import br.gov.al.maceio.sishosp.acl.model.Sistema;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.EscolaBean;
import br.gov.al.maceio.sishosp.hosp.model.EscolaridadeBean;
import br.gov.al.maceio.sishosp.hosp.model.PacienteBean;



public class EscolaDAO {
	private Connection conexao = null;
	// COME�O DO CODIGO
	
            public Boolean cadastrar(EscolaBean escola) throws ProjetoException {
                boolean cadastrou = false;
                System.out.println("passou aqui 2");

                /*PacienteBean user_session = (PacienteBean) FacesContext
                        .getCurrentInstance().getExternalContext().getSessionMap()
                        .get("obj_paciente");*/

                String sql = "insert into hosp.escola (descescola, dtacadastro)"
                		+ " values (?, CURRENT_TIMESTAMP)";
                //returning id_paciente
                    try {
                    	System.out.println("passou aqui 3");
                        conexao = ConnectionFactory.getConnection();
                        PreparedStatement stmt = conexao.prepareStatement(sql);
                        stmt.setString(1, escola.getDescescola().toUpperCase().trim());
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
            
            public Boolean alterar(EscolaBean escola) throws ProjetoException {
                boolean alterou = false;
                String sql = "update hosp.escola set descescola = ? where codescola = ?";
                try {
                    conexao = ConnectionFactory.getConnection();
                    PreparedStatement stmt = conexao.prepareStatement(sql);
                    stmt.setString(1, escola.getDescescola());
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
            public Boolean excluir(EscolaBean escola) throws ProjetoException {
                boolean excluir = false;
                String sql =  "delete from hosp.escola where codescola = ?";
                try {
                    conexao = ConnectionFactory.getConnection();
                    PreparedStatement stmt = conexao.prepareStatement(sql);
                    stmt.setLong(1, escola.getCodEscola());
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
            
            public ArrayList<EscolaBean> listaEscolas() {

                String sql = "select * from hosp.escola order by descescola";

                ArrayList<EscolaBean> lista = new ArrayList();

                try {
                    conexao = ConnectionFactory.getConnection();
                    PreparedStatement stm = conexao.prepareStatement(sql);
                    ResultSet rs = stm.executeQuery();

                    while (rs.next()) {
                    	EscolaBean p = new EscolaBean();
    	            
    	                p.setCodEscola(rs.getInt("codescola"));
    	                p.setDescescola(rs.getString("descescola").toLowerCase());
    	                p.setCodtipoescola(rs.getInt("codtipoescola"));
    	                
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
            
            public EscolaBean buscaescolacodigo (Integer i) throws ProjetoException {
        		System.out.println("buscaescolacodigo");
        		PreparedStatement ps = null;
        		 conexao = ConnectionFactory.getConnection();

        		try {
        			  
        			String sql = "select codescola, descescola from hosp.escola where codescola=? order by descescola";
        			 
        			ps = conexao.prepareStatement(sql);
        			ps.setInt(1, i);
        			ResultSet rs = ps.executeQuery();

        			
        			EscolaBean escola = new EscolaBean();
        			while (rs.next()) {
        			
        				escola.setCodEscola(rs.getInt(1));
        				escola.setDescescola(rs.getString(2));
        			
        			
        			}
        			return escola;
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
            
            public List<EscolaBean> buscaescola (String s) throws ProjetoException {
            	System.out.println("buscaescola");
            			PreparedStatement ps = null;
            			conexao = ConnectionFactory.getConnection();

            			try {
            				List<EscolaBean> listaescolas = new ArrayList<EscolaBean>();  
            				String sql = "select codescola, descescola from hosp.escola where upper(descescola) like ? order by descescola";
            				 
            				ps = conexao.prepareStatement(sql);
            				ps.setString(1, "%"+s.toUpperCase()+"%");
            				ResultSet rs = ps.executeQuery();

            				List<EscolaBean> colecao = new ArrayList<EscolaBean>();
            				
            				while (rs.next()) {
            					
            					EscolaBean escola = new EscolaBean();
            					escola.setCodEscola(rs.getInt(1));
            					escola.setDescescola(rs.getString(2));
            					colecao.add(escola);
            					
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
