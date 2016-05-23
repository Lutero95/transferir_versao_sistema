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
import br.gov.al.maceio.sishosp.hosp.model.EnderecoBean;
import br.gov.al.maceio.sishosp.hosp.model.EscolaBean;
import br.gov.al.maceio.sishosp.hosp.model.EscolaridadeBean;
import br.gov.al.maceio.sishosp.hosp.model.LaudoBean;
import br.gov.al.maceio.sishosp.hosp.model.PacienteBean;



public class LaudoDAO {
	private Connection conexao = null;
	// COME�O DO CODIGO
	
            public Boolean cadastrarLaudoApac(LaudoBean laudo) throws ProjetoException {
                boolean cadastrou = false;
                System.out.println("passou aqui 2");

                /*PacienteBean user_session = (PacienteBean) FacesContext
                        .getCurrentInstance().getExternalContext().getSessionMap()
                        .get("obj_paciente");*/

                String sql = "insert into hosp.escola (descescola) values (?)";
                //String sql = "insert into hosp.escola (descescola) values ((select max(cod) +1 from hosp.escola where codempresa=1), ?";
                //returning id_paciente
                    try {
                    	System.out.println("passou aqui 3");
                        conexao = ConnectionFactory.getConnection();
                        PreparedStatement stmt = conexao.prepareStatement(sql);
                        stmt.setString(1, laudo.getDesceApac().toUpperCase().trim());
                        //stmt.setInt(2, escola.getCodtipoescola());
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
            
            public Boolean alterarLaudoApac(LaudoBean laudo) throws ProjetoException {
                boolean alterou = false;
                String sql = "update hosp.escola set descescola = ? where id_escola = ?";
                try {
                    conexao = ConnectionFactory.getConnection();
                    PreparedStatement stmt = conexao.prepareStatement(sql);
                    stmt.setString(1, laudo.getDesceApac());
                    stmt.setInt(2, laudo.getCodLaudoApac());
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
            public Boolean excluirLaudoApac(LaudoBean laudo) throws ProjetoException {
                boolean excluir = false;
                String sql = "delete from hosp.escola where id_escola = ?";
                try {
                    conexao = ConnectionFactory.getConnection();
                    PreparedStatement stmt = conexao.prepareStatement(sql);
                    stmt.setInt(1, laudo.getCodLaudoApac());
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
            
            public Boolean cadastrarLaudoBpi(LaudoBean laudo) throws ProjetoException {
                boolean cadastrou = false;
                System.out.println("passou aqui 2");

                /*PacienteBean user_session = (PacienteBean) FacesContext
                        .getCurrentInstance().getExternalContext().getSessionMap()
                        .get("obj_paciente");*/

                String sql = "insert into hosp.tipoescola (desctipoescola)"
                		+ " values (?)";
                
                    try {
                    	System.out.println("passou aqui 3");
                        conexao = ConnectionFactory.getConnection();
                        PreparedStatement stmt = conexao.prepareStatement(sql);
                        stmt.setString(1, laudo.getDescBpi().toUpperCase().trim());
                        
                    
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
            
            public Boolean alterarLaudoBpi(LaudoBean laudo) throws ProjetoException {
                boolean alterou = false;
                String sql = "update hosp.tipoescola set desctipoescola = ? where codtipoescola = ?";
                try {
                    conexao = ConnectionFactory.getConnection();
                    PreparedStatement stmt = conexao.prepareStatement(sql);
                    stmt.setString(1, laudo.getDescBpi());
                    stmt.setInt(2, laudo.getCodLaudoBpi());
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
            
            public Boolean excluirLaudoBpi(LaudoBean laudo) throws ProjetoException {
                boolean excluir = false;
                String sql =  "delete from hosp.escola where codtipoescola = ?";
                try {
                    conexao = ConnectionFactory.getConnection();
                    PreparedStatement stmt = conexao.prepareStatement(sql);
                    stmt.setLong(1, laudo.getCodLaudoBpi());
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
            
            
            public ArrayList<LaudoBean> listaLaudos() {

                String sql = "select * from hosp.escola order by descescola";

                ArrayList<LaudoBean> lista = new ArrayList();

                try {
                    conexao = ConnectionFactory.getConnection();
                    PreparedStatement stm = conexao.prepareStatement(sql);
                    ResultSet rs = stm.executeQuery();

                    while (rs.next()) {
                    	LaudoBean p = new LaudoBean();
    	            
    	                p.setCodLaudoApac(rs.getInt("id_escola"));
    	                p.setDesceApac(rs.getString("descescola").toUpperCase());
    	                p.setCodLaudoBpi(rs.getInt("id_escola"));
    	                p.setDescBpi(rs.getString("id_escola").toUpperCase());
    	                
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
            
            public List<LaudoBean> buscarTipoLaudo(String valor, Integer tipo) {
        		
            	
          		 String sql = "select escola.id_escola, escola.descescola from hosp.escola where";
          		
          		if (tipo == 1) {
          			sql += " escola.descescola like ? order by escola.descescola ";
          		}
          		List<LaudoBean> lista = new ArrayList<>();

          		try {
          			conexao = ConnectionFactory.getConnection();
          			PreparedStatement stmt = conexao.prepareStatement(sql);
          			if (tipo == 1) {
          				stmt.setString(1, "%" + valor.toUpperCase() + "%");
          			}

          			ResultSet rs = stmt.executeQuery();

          			while (rs.next()) {
          				LaudoBean p = new LaudoBean();
          				
          	
          				p.setCodLaudoApac(rs.getInt("id_escola"));
     	                p.setDesceApac(rs.getString("descescola").toUpperCase());
     	                p.setCodLaudoBpi(rs.getInt("id_escola"));
     	                p.setDescBpi(rs.getString("id_escola").toUpperCase());

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
