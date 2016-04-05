package br.gov.al.maceio.sishosp.hosp.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;


import javax.faces.context.FacesContext;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.EscolaBean;
import br.gov.al.maceio.sishosp.hosp.model.PacienteBean;



public class PacienteDAO {
	private Connection conexao = null;
	// COMEÇO DO CODIGO
	
            @SuppressWarnings("deprecation")
			public Boolean cadastrar(PacienteBean paciente) throws ProjetoException {
                boolean cadastrou = false;
                System.out.println("passou aqui 2");

                /*PacienteBean user_session = (PacienteBean) FacesContext
                        .getCurrentInstance().getExternalContext().getSessionMap()
                        .get("obj_paciente");*/
                
                /* cep, numero, complemento, referencia, rg, oe, "
                		+ "dtaexpedicaoorg, cpf, cns, localtrabalha, nomeresp,rgresp, "
                		+ "cpfresp,dtanascimentoresp, cartorio, folha, livro, dtaregistro, "
                		+ "reservista, ctps, serie, dgserie, pis, deficiencia, trabalha, "
                		+ "logradouro, cidade, uf, bairro*/
               // , dtanascimento  , cep, uf, cidade, logradouro, cidade, bairro, numero, complemento, referencia 
                String sql = "insert into hosp.pacientes (dtacadastro, nome, dtanascimento, estcivil, sexo, sangue, "
                		+ "pai, mae, conjuge, cep, uf, cidade, bairro, logradouro, numero, complemento, referencia, rg, oe, dtaexpedicaorg, cpf, cns, protreab, "
                		+ "reservista, ctps, serie, pis, cartorio, regnascimento, livro, folha, dtaregistro, trabalha, localtrabalha, codparentesco, "
                		+ "nomeresp, rgresp, cpfresp, dtanascimentoresp) "
                		+ " values (CURRENT_TIMESTAMP, ?, ?, ?, ?, ?, ? , ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ? , ? , ?, ? , ? , "
                		+ "? , ? , ?, ?, ?, ? , ? , ?, ? , ?) returning codpaciente";

                    try {
                    	System.out.println("passou aqui 3");
                    	System.out.println("|WAL|"+ paciente.getNome());
                        conexao = ConnectionFactory.getConnection();
                        PreparedStatement stmt = conexao.prepareStatement(sql);
                        stmt.setString(1, paciente.getNome().toUpperCase().trim());
                        stmt.setDate(2, new java.sql.Date(paciente.getDtanascimento().getTime()));
                        stmt.setString(3, paciente.getEstadoCivil());
                        stmt.setString(4, paciente.getSexo());
                        stmt.setString(5, paciente.getSangue());
                        stmt.setString(6, paciente.getNomePai().toUpperCase().trim());
                        stmt.setString(7, paciente.getNomeMae().toUpperCase().trim());
                        stmt.setString(8, paciente.getConjugue().toUpperCase().trim());
                        //stmt.setInt(9, paciente.getCorRaca());
                        stmt.setInt(9, paciente.getEndereco().getCep());
                        stmt.setString(10, paciente.getEndereco().getUf().toUpperCase().trim());
                        stmt.setString(11, paciente.getEndereco().getMunicipio().toUpperCase().trim());
                        stmt.setString(12, paciente.getEndereco().getBairro().toUpperCase().trim());
                        stmt.setString(13, paciente.getEndereco().getLogradouro().toUpperCase().trim());
                        stmt.setString(14, paciente.getEndereco().getNumero().toUpperCase().trim());
                        stmt.setString(15, paciente.getEndereco().getComplemento().toUpperCase().trim());
                        stmt.setString(16, paciente.getEndereco().getReferencia().toUpperCase().trim());
                        stmt.setString(17, paciente.getRg().toUpperCase().trim());
                        stmt.setString(18, paciente.getOe().toUpperCase().trim());
                        stmt.setDate(19, new java.sql.Date(paciente.getDataExpedicao1().getTime()));
                        stmt.setDouble(20, paciente.getCpf());
                        stmt.setString(21, paciente.getCns().toUpperCase().trim());
                        stmt.setDouble(22, paciente.getProtant());
                        stmt.setString(23, paciente.getReservista().toUpperCase().trim());
                        stmt.setInt(24, paciente.getCtps());
                        stmt.setInt(25, paciente.getSerie());
                        stmt.setString(26, paciente.getPis().toUpperCase().trim());
                        stmt.setString(27, paciente.getCartorio().toUpperCase().trim());
                        stmt.setString(28, paciente.getNumeroCartorio().toUpperCase().trim());
                        stmt.setString(29, paciente.getLivro().toUpperCase().trim());
                        stmt.setInt(30, paciente.getFolha());
                        stmt.setDate(31, new java.sql.Date(paciente.getDataExpedicao2().getTime()));
                        //stmt.setString(32, paciente.getAssociado().toUpperCase().trim());
                        //stmt.setInt(33, paciente.getEscolaridade().getCodescolaridade());
                        //stmt.setInt(34, paciente.getEscola().getCodescola());
                        //stmt.setInt(35, paciente.getProfissao().getCodprofissao());
                        stmt.setString(32, paciente.getTrabalha().toUpperCase().trim());
                        stmt.setString(33, paciente.getLocaltrabalha().toUpperCase().trim());
                        stmt.setInt(34, paciente.getCodparentesco());
                        stmt.setString(35, paciente.getNomeresp().toUpperCase().trim());
                        stmt.setString(36, paciente.getRgresp().toUpperCase().trim());
                        stmt.setDouble(37, paciente.getCpfresp());
                        stmt.setDate(38, new java.sql.Date(paciente.getDataNascimentoresp().getTime()));
                        //stmt.setInt(39, paciente.getEncaminhado().getCodencaminhado());
                        //stmt.setInt(40, paciente.getCodFormaTransporte());
                        //stmt.setString(41, paciente.getDeficiencia());
                        //stmt.setString(42, paciente.getTipoDeficiencia());
                        
                        
                        
     
                        
                        //stmt.setString(2, paciente.getCpf().replaceAll("[^0-9]", ""));  
                        //stmt.setBoolean(3, true);
                        //stmt.setInt(3, paciente.getIdpessoa());
                        ResultSet rs = stmt.executeQuery();  
                        if(rs.next()) { 
                    PacienteBean p = paciente;
                    String idRetorno = null;
                    idRetorno = String.valueOf(rs.getLong("codpaciente"));
                    p.setId_paciente(Long.parseLong(idRetorno));
                    System.out.println("|THU1|"+ idRetorno);
                    }
                    stmt.execute();   
                    System.out.println("|THU|"+ paciente.getNome());
                    System.out.println("passou aqui 4");
                    conexao.commit();
                    conexao.close();

                    return cadastrou;
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            
            public Boolean alterar(PacienteBean paciente) throws ProjetoException {
                boolean alterou = false;
                String sql = "update hosp.pacientes set nome = ? where id_paciente = ?";
                try {
                    conexao = ConnectionFactory.getConnection();
                    PreparedStatement stmt = conexao.prepareStatement(sql);
                    stmt.setString(1, paciente.getNome());
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
            
            public Boolean excluir(PacienteBean paciente) throws ProjetoException {
                boolean excluir = false;
                String sql =  "delete from hosp.pacientes where codpaciente = ?";
                try {
                    conexao = ConnectionFactory.getConnection();
                    PreparedStatement stmt = conexao.prepareStatement(sql);
                    stmt.setLong(1, paciente.getId_paciente());
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
            
            public ArrayList<PacienteBean> listaPacientes() {

                String sql = "select codpaciente, nome, lpad(trim(to_char(cpf,'99999999999')),11,'0') as cpf, rg  from hosp.pacientes order by nome";

                ArrayList<PacienteBean> lista = new ArrayList();

                try {
                    conexao = ConnectionFactory.getConnection();
                    PreparedStatement stm = conexao.prepareStatement(sql);
                    ResultSet rs = stm.executeQuery();

                    while (rs.next()) {
                    	PacienteBean p = new PacienteBean();
    	            	System.out.println("|1|");
    	                p.setId_paciente(rs.getLong("codpaciente"));
    	                p.setNome(rs.getString("nome").toLowerCase());    
    	                p.setCpf(rs.getDouble("cpf")); 
    	                p.setRg(rs.getString("rg").toLowerCase()); 
    	                
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
            
            
            
            public ArrayList<EscolaBean> listaEscolas() {

                String sql = "select * from hosp.escola order by descescola";

                ArrayList<EscolaBean> lista = new ArrayList();

                try {
                    conexao = ConnectionFactory.getConnection();
                    PreparedStatement stm = conexao.prepareStatement(sql);
                    ResultSet rs = stm.executeQuery();

                    while (rs.next()) {
                    	EscolaBean p = new EscolaBean();
    	            	System.out.println("|1|");
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
            
            public PacienteBean buscapacientecodigo (Integer i) throws ProjetoException {
        		PreparedStatement ps = null;
        		conexao = ConnectionFactory.getConnection();

        		try {
        			  
        			String sql = "select codprofissao from hosp.paciente where codpaciente=? order by codprofissao";
        			 
        			ps = conexao.prepareStatement(sql);
        			ps.setInt(1, i);
        			ResultSet rs = ps.executeQuery();

        			
        			PacienteBean paciente = new PacienteBean();
        			while (rs.next()) {
        			
        				paciente.setCodProfissao(rs.getInt(1));

        			}
        			return paciente;
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
