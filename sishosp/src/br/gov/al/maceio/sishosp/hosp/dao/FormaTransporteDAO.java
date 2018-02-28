package br.gov.al.maceio.sishosp.hosp.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.FormaTransporteBean;





public class FormaTransporteDAO {
	private Connection conexao = null;
	// COME�O DO CODIGO
	
            public Boolean cadastrar(FormaTransporteBean transporte) throws ProjetoException {
                boolean cadastrou = false;
                System.out.println("passou aqui 2");

                /*PacienteBean user_session = (PacienteBean) FacesContext
                        .getCurrentInstance().getExternalContext().getSessionMap()
                        .get("obj_paciente");*/

                String sql = "insert into hosp.formatransporte (descformatransporte)"
                		+ " values (?)";
                //returning id_paciente
                    try {
                    	System.out.println("passou aqui 3");
                        conexao = ConnectionFactory.getConnection();
                        PreparedStatement stmt = conexao.prepareStatement(sql);
                        stmt.setString(1, transporte.getDescformatransporte().toUpperCase().trim());
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
                    cadastrou = true;
                    conexao.close();

                    return cadastrou;
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            
            public Boolean alterar(FormaTransporteBean transporte) throws ProjetoException {
                boolean alterou = false;
                String sql = "update hosp.formatransporte set descformatransporte = ? where id_formatransporte = ?";
                try {
                    conexao = ConnectionFactory.getConnection();
                    PreparedStatement stmt = conexao.prepareStatement(sql);
                    stmt.setString(1, transporte.getDescformatransporte().toUpperCase());
                    stmt.setInt(2, transporte.getCodformatransporte());
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
            public Boolean excluir(FormaTransporteBean transporte) throws ProjetoException {
                boolean excluir = false;
                String sql = "delete from hosp.formatransporte where id_formatransporte = ?";
                try {
                    conexao = ConnectionFactory.getConnection();
                    PreparedStatement stmt = conexao.prepareStatement(sql);
                    stmt.setInt(1, transporte.getCodformatransporte());
                    stmt.executeUpdate();

                    
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
            
            public ArrayList<FormaTransporteBean> listaTransportes() throws ProjetoException {

                String sql = "select * from hosp.formatransporte order by descformatransporte";

                ArrayList<FormaTransporteBean> lista = new ArrayList();

                try {
                    conexao = ConnectionFactory.getConnection();
                    PreparedStatement stm = conexao.prepareStatement(sql);
                    ResultSet rs = stm.executeQuery();

                    while (rs.next()) {
                    	FormaTransporteBean p = new FormaTransporteBean();
    	            	
    	                p.setCodformatransporte(rs.getInt("id_formatransporte"));
    	                p.setDescformatransporte(rs.getString("descformatransporte").toUpperCase());
	                
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
            
            public FormaTransporteBean buscatransportecodigo (Integer i) throws ProjetoException {
        		System.out.println("buscaencaminhadocodigo");
        		PreparedStatement ps = null;
        		 conexao = ConnectionFactory.getConnection();

        		try {
        			  
        			String sql = "select id_formatransporte, descformatransporte from hosp.formatransporte where id_formatransporte=? order by descformatransporte";
        			 
        			ps = conexao.prepareStatement(sql);
        			ps.setInt(1, i);
        			ResultSet rs = ps.executeQuery();

        			
        			FormaTransporteBean transporte = new FormaTransporteBean();
        			while (rs.next()) {
        			
        				transporte.setCodformatransporte(rs.getInt(1));
        				transporte.setDescformatransporte(rs.getString(2));
        			
        			
        			}
        			return transporte;
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
            
            public List<FormaTransporteBean> buscatransporte(String s) throws ProjetoException {
            	System.out.println("buscatransporte");
            			PreparedStatement ps = null;
            			conexao = ConnectionFactory.getConnection();

            			try {
            				List<FormaTransporteBean> listatransportes = new ArrayList<FormaTransporteBean>();  
            				String sql = "select id_formatransporte,id_formatransporte ||'-'|| descformatransporte descformatransporte from hosp.formatransporte where upper(id_formatransporte ||'-'|| descformatransporte) like ? order by descformatransporte";
            				 
            				ps = conexao.prepareStatement(sql);
            				ps.setString(1, "%"+s.toUpperCase()+"%");
            				ResultSet rs = ps.executeQuery();

            				List<FormaTransporteBean> colecao = new ArrayList<FormaTransporteBean>();
            				
            				while (rs.next()) {
            					
            					FormaTransporteBean transporte = new FormaTransporteBean();
            					transporte.setCodformatransporte(rs.getInt("id_formatransporte"));
            					transporte.setDescformatransporte(rs.getString("descformatransporte"));
            					colecao.add(transporte);
            					
            	
            				
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
  
            public List<FormaTransporteBean> buscarTipoTransporte(String valor, Integer tipo) throws ProjetoException {
        		
            	
         		 String sql = "select formatransporte.id_formatransporte, formatransporte.descformatransporte from hosp.formatransporte where";
         		
         		if (tipo == 1) {
         			sql += " formatransporte.descformatransporte like ? order by formatransporte.descformatransporte ";
         		}
         		List<FormaTransporteBean> lista = new ArrayList<>();

         		try {
         			conexao = ConnectionFactory.getConnection();
         			PreparedStatement stmt = conexao.prepareStatement(sql);
         			if (tipo == 1) {
         				stmt.setString(1, "%" + valor.toUpperCase() + "%");
         			}

         			ResultSet rs = stmt.executeQuery();

         			while (rs.next()) {
         				FormaTransporteBean p = new FormaTransporteBean();
         				
         	
         				p.setCodformatransporte(rs.getInt("id_formatransporte"));
     	                p.setDescformatransporte(rs.getString("descformatransporte").toUpperCase());

         				lista.add(p);

         			}
         		} catch (SQLException ex) {
         			ex.printStackTrace();
         			// throw new RuntimeException(ex); //
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
