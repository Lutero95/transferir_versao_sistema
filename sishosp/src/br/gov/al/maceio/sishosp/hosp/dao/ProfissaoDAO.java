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
import br.gov.al.maceio.sishosp.hosp.model.ProfissaoBean;



public class ProfissaoDAO {
	private Connection conexao = null;
	// COMEÇO DO CODIGO
	
            public Boolean cadastrar(ProfissaoBean profissao) throws ProjetoException {
                boolean cadastrou = false;
                System.out.println("passou aqui 2");

                /*PacienteBean user_session = (PacienteBean) FacesContext
                        .getCurrentInstance().getExternalContext().getSessionMap()
                        .get("obj_paciente");*/

                String sql = "insert into hosp.profissao (descprofissao)"
                		+ " values (?)";
                //returning id_paciente
                    try {
                    	System.out.println("passou aqui 3");
                        conexao = ConnectionFactory.getConnection();
                        PreparedStatement stmt = conexao.prepareStatement(sql);
                        stmt.setString(1, profissao.getDescprofissao().toUpperCase().trim());
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
            
            public Boolean alterar(ProfissaoBean profissao) throws ProjetoException {
                boolean alterou = false;
                String sql = "update hosp.profissao set descprofissao = ? where id_profissao = ?";
                try {
                    conexao = ConnectionFactory.getConnection();
                    PreparedStatement stmt = conexao.prepareStatement(sql);
                    stmt.setString(1, profissao.getDescprofissao());
                    stmt.setInt(2, profissao.getCodprofissao());
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
            public Boolean excluir(ProfissaoBean profissao) throws ProjetoException {
                boolean excluir = false;
                String sql =  "delete from hosp.profissao where id_profissao = ?";
                try {
                    conexao = ConnectionFactory.getConnection();
                    PreparedStatement stmt = conexao.prepareStatement(sql);
                    stmt.setLong(1, profissao.getCodprofissao());
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
            
            public ArrayList<ProfissaoBean> listaProfissoes() {

                String sql = "select * from hosp.profissao order by id_profissao,descprofissao";

                ArrayList<ProfissaoBean> lista = new ArrayList();

                try {
                    conexao = ConnectionFactory.getConnection();
                    PreparedStatement stm = conexao.prepareStatement(sql);
                    ResultSet rs = stm.executeQuery();

                    while (rs.next()) {
                    	ProfissaoBean p = new ProfissaoBean();
    	            	
    	                p.setCodprofissao(rs.getInt("id_profissao"));
    	                p.setDescprofissao(rs.getString("descprofissao").toLowerCase());
	                
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
            
            public ProfissaoBean buscaprofissaocodigo (Integer i) throws ProjetoException {
        		System.out.println("buscaprofissaocodigo");
        		PreparedStatement ps = null;
        		 conexao = ConnectionFactory.getConnection();

        		try {
        			  
        			String sql = "select id_profissao, descprofissao from hosp.profissao where id_profissao=? order by descprofissao";
        			 
        			ps = conexao.prepareStatement(sql);
        			ps.setInt(1, i);
        			ResultSet rs = ps.executeQuery();

        			
        			ProfissaoBean profissao = new ProfissaoBean();
        			while (rs.next()) {
        			
        				profissao.setCodprofissao(rs.getInt(1));
        				profissao.setDescprofissao(rs.getString(2));
        			
        			
        			}
        			return profissao;
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
            
            public List<ProfissaoBean> buscaprofissao(String s) throws ProjetoException {
            	System.out.println("buscaprofissao");
            			PreparedStatement ps = null;
            			conexao = ConnectionFactory.getConnection();

            			try {
            				List<ProfissaoBean> listaprofissoes = new ArrayList<ProfissaoBean>();  
            				String sql = "select id_profissao, descprofissao from hosp.profissao where upper(descprofissao) like ? order by descprofissao";
            				 
            				ps = conexao.prepareStatement(sql);
            				ps.setString(1, "%"+s.toUpperCase()+"%");
            				ResultSet rs = ps.executeQuery();

            				List<ProfissaoBean> colecao = new ArrayList<ProfissaoBean>();
            				
            				while (rs.next()) {
            					
            					ProfissaoBean profissao = new ProfissaoBean();
            					profissao.setCodprofissao(rs.getInt(1));
            					profissao.setDescprofissao(rs.getString(2));
            					colecao.add(profissao);
            					
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
            
            
            public List<ProfissaoBean> buscarTipoProfissao(String valor, Integer tipo) {
        		
        	
        		 String sql = "select profissao.id_profissao, profissao.descprofissao from hosp.profissao where";
        		
        		if (tipo == 1) {
        			sql += " profissao.descprofissao like ? order by profissao.descprofissao ";
        		}
        		List<ProfissaoBean> lista = new ArrayList<>();

        		try {
        			conexao = ConnectionFactory.getConnection();
        			PreparedStatement stmt = conexao.prepareStatement(sql);
        			if (tipo == 1) {
        				stmt.setString(1, "%" + valor.toUpperCase() + "%");
        			}

        			ResultSet rs = stmt.executeQuery();

        			while (rs.next()) {
        				ProfissaoBean p = new ProfissaoBean();
        				
        	
        				p.setCodprofissao(rs.getInt("id_profissao"));
    	                p.setDescprofissao(rs.getString("descprofissao").toLowerCase());

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
