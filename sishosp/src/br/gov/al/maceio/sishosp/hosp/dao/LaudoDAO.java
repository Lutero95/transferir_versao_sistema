package br.gov.al.maceio.sishosp.hosp.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
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
	private ProfissionalDAO profDao = new ProfissionalDAO();
	private ProgramaDAO progDao = new ProgramaDAO();
	private ProcedimentoDAO procDao = new ProcedimentoDAO();
	private PacienteDAO pacieDao = new PacienteDAO();
	private GrupoDAO grupoDao = new GrupoDAO();
	private FornecedorDAO forneDao = new FornecedorDAO();
	
	// COMEÇO DO CODIGO
	
	public Boolean cadastrarLaudo(LaudoBean laudo) throws ProjetoException {
        boolean cadastrou = false;
        System.out.println("passou aqui 2");

        /*PacienteBean user_session = (PacienteBean) FacesContext
                .getCurrentInstance().getExternalContext().getSessionMap()
                .get("obj_paciente");*/

        String sql = "insert into hosp.apac (codpaciente, codprograma, codgrupo, codmedico, codproc, dtasolicitacao, recurso, apac, unidade, situacao, dtautorizacao, cid10_1, cid10_2, "
        		+ "codfornecedor, valor, nota, qtd, codequipamento, obs) values (?, ? , ? , ?, ? , ?, ? , ?, ?, ?, "
        		+ "?, ?, ?, ?, ?, ?, ? , ? , ?)";
        
            try {
            	System.out.println("passou aqui 3");
                conexao = ConnectionFactory.getConnection();
                PreparedStatement stmt = conexao.prepareStatement(sql);
                stmt.setLong(1, laudo.getPaciente().getId_paciente());
                stmt.setInt(2, laudo.getPrograma().getIdPrograma());
                stmt.setInt(3, laudo.getGrupo().getIdGrupo());
                stmt.setInt(4, laudo.getProfissional().getIdProfissional());
                stmt.setInt(5, laudo.getProcedimento().getIdProc());
                //stmt.setString(6, laudo.getPaciente().getCns().toUpperCase().trim());
                //stmt.setString(7, laudo.getPaciente().getCpf().toUpperCase().trim());
                stmt.setDate(6, new java.sql.Date(laudo.getDtasolicitacao().getTime()));
                stmt.setString(7, laudo.getRecurso());
                stmt.setString(8, laudo.getApac().toUpperCase().trim());
                stmt.setString(9, laudo.getUnidade().toUpperCase().trim());
                stmt.setString(10, laudo.getSituacao().toUpperCase().trim());
                stmt.setDate(11, new java.sql.Date(laudo.getDtautorizacao().getTime()));
                // stmt.setDate(12, new java.sql.Date(laudo.getDtavencimento().getTime()));
              
                if (laudo.getCid10_1() == null) {
    				stmt.setNull(12, Types.CHAR);
    			} else {
    				stmt.setString(12, laudo.getCid10_1().toUpperCase().trim());
    			}
                if (laudo.getCid10_2() == null) {
    				stmt.setNull(13, Types.CHAR);
    			} else {
    				stmt.setString(13, laudo.getCid10_2().toUpperCase().trim());
    			}
                stmt.setInt(14, laudo.getFornecedor().getIdFornecedor());
                stmt.setDouble(15, laudo.getFornecedor().getValor());
                stmt.setString(16, laudo.getNota().toUpperCase().trim());
                stmt.setInt(17, laudo.getQtd());
                stmt.setInt(18, laudo.getCodequipamento());
                if (laudo.getObs() == null) {
    				stmt.setNull(19, Types.CHAR);
    			} else {
    				stmt.setString(19, laudo.getObs().toUpperCase().trim());
    			}
                
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
	
    public Boolean alterarLaudo(LaudoBean laudo) throws ProjetoException {
        boolean alterou = false;
        String sql = "update hosp.apac set codpaciente = ?, codprograma = ?, codgrupo = ?, codmedico = ?, "
        		+ "codproc = ?, dtasolicitacao = ?, recurso = ?, apac = ?, unidade = ?, situacao = ?, dtautorizacao = ?, "
        		+ "cid10_1 = ?, cid10_2 = ?, codfornecedor = ?, valor = ?, nota = ?, qtd = ?, codequipamento = ?, obs = ? where id_apac = ?";
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setLong(1, laudo.getPaciente().getId_paciente());
            stmt.setInt(2, laudo.getPrograma().getIdPrograma());
            stmt.setInt(3, laudo.getGrupo().getIdGrupo());
            stmt.setInt(4, laudo.getProfissional().getIdProfissional());
            stmt.setInt(5, laudo.getProcedimento().getIdProc());
            //stmt.setString(6, laudo.getPaciente().getCns().toUpperCase().trim());
            //stmt.setString(7, laudo.getPaciente().getCpf().toUpperCase().trim());
            stmt.setDate(6, new java.sql.Date(laudo.getDtasolicitacao().getTime()));
            stmt.setString(7, laudo.getRecurso());
            stmt.setString(8, laudo.getApac().toUpperCase().trim());
            stmt.setString(9, laudo.getUnidade().toUpperCase().trim());
            stmt.setString(10, laudo.getSituacao().toUpperCase().trim());
            stmt.setDate(11, new java.sql.Date(laudo.getDtautorizacao().getTime()));
            //stmt.setDate(12, new java.sql.Date(laudo.getDtachegada().getTime()));
            if (laudo.getCid10_1() == null) {
				stmt.setNull(12, Types.CHAR);
			} else {
				stmt.setString(12, laudo.getCid10_1().toUpperCase().trim());
			}
            if (laudo.getCid10_2() == null) {
				stmt.setNull(13, Types.CHAR);
			} else {
				stmt.setString(13, laudo.getCid10_2().toUpperCase().trim());
			}
            stmt.setInt(14, laudo.getFornecedor().getIdFornecedor());
            stmt.setDouble(15, laudo.getFornecedor().getValor());
            stmt.setString(16, laudo.getNota().toUpperCase().trim());
            stmt.setInt(17, laudo.getQtd());
            stmt.setInt(18, laudo.getCodequipamento());
            if (laudo.getObs() == null) {
				stmt.setNull(19, Types.CHAR);
			} else {
				stmt.setString(19, laudo.getObs().toUpperCase().trim());
			}           
            stmt.setInt(20, laudo.getId_apac());
            
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
        public Boolean excluirLaudo(LaudoBean laudo) throws ProjetoException {
        boolean excluir = false;
        String sql = "delete from hosp.apac where id_apac = ?";
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, laudo.getId_apac());
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

    
                /*String sql ="select apac.id_apac, apac.codpaciente, apac.codprograma, apac.codgrupo, "
                		+ "apac.codmedico, apac.codproc, apac.dtasolicitacao, apac.recurso, apac.apac, "
                		+ "apac.unidade, apac.situacao, apac.dtautorizacao, apac.cid10_1, apac.cid10_2, apac.codfornecedor, apac.valor, "
                		+ "apac.nota, apac.qtd, apac.codequipamento, "
                		+ "apac.obs from hosp.apac left join hosp.pacientes on apac.codpaciente=pacientes.id_paciente "
                		+ "left join hosp.programa on apac.codprograma=programa.id_programa "
                		+ "left join hosp.grupo on apac.codgrupo=grupo.id_grupo "
                		+ "left join hosp.medicos on apac.codmedico=medicos.id_medico "
                		+ "left join hosp.proc on apac.codproc=proc.id order by apac";*/
                
                String sql = "select id_apac, codpaciente, codprograma,codgrupo,codmedico, "
                		+ "codproc,dtasolicitacao, recurso,apac, unidade, situacao, dtautorizacao, "
                		+ "cid10_1, cid10_2,codfornecedor,valor, nota, qtd, codequipamento, "
                		+ "obs from hosp.apac order by apac";

                ArrayList<LaudoBean> lista = new ArrayList();

                try {
                    conexao = ConnectionFactory.getConnection();
                    PreparedStatement stm = conexao.prepareStatement(sql);
                    ResultSet rs = stm.executeQuery();

                    while (rs.next()) {
                    	LaudoBean l = new LaudoBean();
    	            
    	                //p.setDesceApac(rs.getString("descescola").toUpperCase());
    	   
    	                l.setId_apac(rs.getInt("id_apac"));
    	                l.setPaciente(pacieDao.listarPacientePorID(rs.getInt("codpaciente")));
    	                l.setPrograma(progDao.listarProgramaPorId(rs.getInt("codprograma")));
    	                l.setGrupo(grupoDao.listarGrupoPorId(rs.getInt("codgrupo")));
    	                l.setProfissional(profDao.listarProfissionalPorId(rs.getInt("codmedico")));
    	                l.setProcedimento(procDao.listarProcedimentoPorId(rs.getInt("codproc")));
    	                l.setDtasolicitacao(rs.getDate("dtasolicitacao"));
    	                l.setRecurso(rs.getString("recurso"));
    	                l.setApac(rs.getString("apac"));
    	                l.setUnidade(rs.getString("unidade"));
    	                l.setSituacao(rs.getString("situacao"));
    	                l.setDtautorizacao(rs.getDate("dtautorizacao"));
    	                l.setCid10_1(rs.getString("cid10_1"));
    	                l.setCid10_2(rs.getString("cid10_2"));
    	                l.setFornecedor(forneDao.listarFornecedorPorId(rs.getInt("codfornecedor")));
    	                l.setValor(rs.getDouble("valor"));
    	                l.setNota(rs.getString("nota"));
    	                l.setQtd(rs.getInt("qtd"));
    	                l.setCodequipamento(rs.getInt("codequipamento"));
    	                l.setObs(rs.getString("obs"));
    	              
    	                
    	                lista.add(l);
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
        		
            	
          		 String sql = "select * from hosp.apac where";
          		
          		if (tipo == 1) {
          			sql += " apac.apac like ? order by apac.apac ";
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
          				LaudoBean l = new LaudoBean();
          				
          	
          				l.setId_apac(rs.getInt("id_apac"));
     	                l.setApac(rs.getString("apac").toUpperCase());
    	                l.setPaciente(pacieDao.listarPacientePorID(rs.getInt("codpaciente")));
    	                l.setPrograma(progDao.listarProgramaPorId(rs.getInt("codprograma")));
    	                l.setGrupo(grupoDao.listarGrupoPorId(rs.getInt("codgrupo")));
    	                l.setProfissional(profDao.listarProfissionalPorId(rs.getInt("codmedico")));
    	                l.setProcedimento(procDao.listarProcedimentoPorId(rs.getInt("codproc")));
    	                l.setDtasolicitacao(rs.getDate("dtasolicitacao"));
    	                l.setRecurso(rs.getString("recurso"));
    	                l.setUnidade(rs.getString("unidade"));
    	                l.setSituacao(rs.getString("situacao"));
    	                l.setDtautorizacao(rs.getDate("dtautorizacao"));
    	                l.setCid10_1(rs.getString("cid10_1"));
    	                l.setCid10_2(rs.getString("cid10_2"));
    	                l.setFornecedor(forneDao.listarFornecedorPorId(rs.getInt("codfornecedor")));
    	                l.setValor(rs.getDouble("valor"));
    	                l.setNota(rs.getString("nota"));
    	                l.setQtd(rs.getInt("qtd"));
    	                l.setCodequipamento(rs.getInt("codequipamento"));
    	                l.setObs(rs.getString("obs"));
    	              
    	   

          				lista.add(l);

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
