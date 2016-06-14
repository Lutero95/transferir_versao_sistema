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
	private EquipeDAO equipeDao = new EquipeDAO();
	private CidDAO cidDao = new CidDAO();
	private EquipamentoDAO equipamentoDao = new EquipamentoDAO();
	
	// COMEÇO DO CODIGO
	
	public Boolean cadastrarLaudo(LaudoBean laudo) throws ProjetoException {
        boolean cadastrou = false;
        System.out.println("passou aqui 2");

        /*PacienteBean user_session = (PacienteBean) FacesContext
                .getCurrentInstance().getExternalContext().getSessionMap()
                .get("obj_paciente");*/

        String sql = "insert into hosp.apac (codpaciente, codprograma, codgrupo, codmedico, codproc, dtasolicitacao, recurso, apac, unidade, situacao, dtautorizacao, cid10_1, cid10_2,cid10_3, "
        		+ "codfornecedor, valor, nota, qtd, codequipamento, obs) values (?, ? , ? , ?, ? , ?, ? , ?, ?, ?, "
        		+ "?, ?, ?, ?, ?, ?, ? , ? , ?, ?)";
        
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
                if (laudo.getCid10_3() == null) {
    				stmt.setNull(14, Types.CHAR);
    			} else {
    				stmt.setString(14, laudo.getCid10_3().toUpperCase().trim());
    			}
                if (laudo.getFornecedor().getIdFornecedor() == null) {
    				stmt.setNull(15, Types.INTEGER);
    			} else {
    				stmt.setInt(15, laudo.getFornecedor().getIdFornecedor());
    			}
                if (laudo.getFornecedor().getValor() == null) {
    				stmt.setNull(16, Types.DOUBLE);
    			} else {
    				stmt.setDouble(16, laudo.getFornecedor().getValor());
    			}
                if (laudo.getNota() == null) {
    				stmt.setNull(17, Types.CHAR);
    			} else {
    				stmt.setString(17, laudo.getNota().toUpperCase().trim());
    			}
                if (laudo.getQtd() == null) {
    				stmt.setNull(18, Types.INTEGER);
    			} else {
    				stmt.setInt(18, laudo.getQtd());
    			}
                if (laudo.getEquipamento().getId_equipamento() == null) {
    				stmt.setNull(19, Types.INTEGER);
    			} else {
    				stmt.setInt(19, laudo.getEquipamento().getId_equipamento());
    			}
                if (laudo.getObs() == null) {
    				stmt.setNull(20, Types.CHAR);
    			} else {
    				stmt.setString(20, laudo.getObs().toUpperCase().trim());
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
        		+ "cid10_1 = ?, cid10_2 = ?, cid10_3 = ?, codfornecedor = ?, valor = ?, nota = ?, qtd = ?, codequipamento = ?, obs = ? where id_apac = ?";
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
            if (laudo.getCid10_3() == null) {
				stmt.setNull(14, Types.CHAR);
			} else {
				stmt.setString(14, laudo.getCid10_3().toUpperCase().trim());
			}
            if (laudo.getFornecedor().getIdFornecedor() == null) {
				stmt.setNull(15, Types.INTEGER);
			} else {
				stmt.setInt(15, laudo.getFornecedor().getIdFornecedor());
			}
            if (laudo.getFornecedor().getValor() == null) {
				stmt.setNull(16, Types.DOUBLE);
			} else {
				stmt.setDouble(16, laudo.getFornecedor().getValor());
			}
            if (laudo.getNota() == null) {
				stmt.setNull(17, Types.CHAR);
			} else {
				stmt.setString(17, laudo.getNota().toUpperCase().trim());
			}
            if (laudo.getQtd() == null) {
				stmt.setNull(18, Types.INTEGER);
			} else {
				stmt.setInt(18, laudo.getQtd());
			}
            if (laudo.getEquipamento().getId_equipamento() == null) {
				stmt.setNull(19, Types.INTEGER);
			} else {
				stmt.setInt(19, laudo.getEquipamento().getId_equipamento());
			}
            if (laudo.getObs() == null) {
				stmt.setNull(20, Types.CHAR);
			} else {
				stmt.setString(20, laudo.getObs().toUpperCase().trim());
			}           
            stmt.setInt(21, laudo.getId_apac());
            
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
	
            public Boolean cadastrarLaudoDigita(LaudoBean laudo) throws ProjetoException {
                boolean cadastrou = false;
                System.out.println("passou aqui 2");

                /*PacienteBean user_session = (PacienteBean) FacesContext
                        .getCurrentInstance().getExternalContext().getSessionMap()
                        .get("obj_paciente");*/

                String sql = "insert into hosp.pacienteslaudo (id_paciente, codequipe, codgrupo, codmedico, codprocedimento, "
                		+ "datasolicitacao, periodoinicio, periodofim) values (?, ?, ?, ?, ?, ? , ?, ?)";
                //String sql = "insert into hosp.escola (descescola) values ((select max(cod) +1 from hosp.escola where codempresa=1), ?";
              
                    try {
                    	System.out.println("passou aqui 3");
                        conexao = ConnectionFactory.getConnection();
                        PreparedStatement stmt = conexao.prepareStatement(sql);
                        stmt.setLong(1, laudo.getPaciente().getId_paciente());
                        stmt.setInt(2, laudo.getEquipe().getCodEquipe());
                        stmt.setInt(3, laudo.getGrupo().getIdGrupo());
                        stmt.setInt(4, laudo.getProfissional().getIdProfissional());
                        stmt.setInt(5, laudo.getProcedimento().getIdProc());
                        //stmt.setString(6, laudo.getPaciente().getCns().toUpperCase().trim());
                        //stmt.setString(7, laudo.getPaciente().getCpf().toUpperCase().trim());
                        stmt.setDate(6, new java.sql.Date(laudo.getDtasolicitacao().getTime()));
                        //stmt.setInt(7, laudo.getProrrogar());
                        stmt.setDate(7, new java.sql.Date(laudo.getDtainicio().getTime()));
                        stmt.setDate(8, new java.sql.Date(laudo.getDtafim().getTime()));
                    
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
            
            public Boolean alterarLaudoDigita(LaudoBean laudo) throws ProjetoException {
                boolean alterou = false;
                String sql = "update hosp.pacienteslaudo set id_paciente = ?, codequipe = ?, codgrupo = ?, codmedico = ?, "
        		+ "codprocedimento = ?, datasolicitacao = ?, periodoinicio = ?, periodofim = ? where id = ?";
                try {
                    conexao = ConnectionFactory.getConnection();
                    PreparedStatement stmt = conexao.prepareStatement(sql);
                    stmt.setLong(1, laudo.getPaciente().getId_paciente());
                    stmt.setInt(2, laudo.getEquipe().getCodEquipe());
                    stmt.setInt(3, laudo.getGrupo().getIdGrupo());
                    stmt.setInt(4, laudo.getProfissional().getIdProfissional());
                    stmt.setInt(5, laudo.getProcedimento().getIdProc());
                    //stmt.setString(6, laudo.getPaciente().getCns().toUpperCase().trim());
                    //stmt.setString(7, laudo.getPaciente().getCpf().toUpperCase().trim());
                    stmt.setDate(6, new java.sql.Date(laudo.getDtasolicitacao().getTime()));
                    //stmt.setInt(7, laudo.getProrrogar());
                    stmt.setDate(7, new java.sql.Date(laudo.getDtasolicitacao().getTime()));
                    stmt.setDate(8, new java.sql.Date(laudo.getDtasolicitacao().getTime()));
                    stmt.setInt(9, laudo.getCodLaudoDigita());
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
            public Boolean excluirLaudoDigita(LaudoBean laudo) throws ProjetoException {
                boolean excluir = false;
                String sql = "delete from hosp.pacienteslaudo where id = ?";
                try {
                    conexao = ConnectionFactory.getConnection();
                    PreparedStatement stmt = conexao.prepareStatement(sql);
                    stmt.setInt(1, laudo.getCodLaudoDigita());
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
                		+ "cid10_1, cid10_2, cid10_3,codfornecedor,valor, nota, qtd, codequipamento, "
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
    	                l.setCid10_3(rs.getString("cid10_3"));
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
        		
            	
          		 String sql = "select * from hosp.apac left join hosp.pacientes on apac.codpaciente=pacientes.id_paciente where";
          		
          		if (tipo == 1) {
          			sql += " pacientes.nome like ? order by pacientes.nome";
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
    	                l.setCid10_2(rs.getString("cid10_3"));
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
            
            
            
            public ArrayList<LaudoBean> listaLaudosDigita() {

  
                String sql = "select id, id_paciente, codequipe, codgrupo,codmedico, "
                		+ "codprocedimento,datasolicitacao, periodoinicio, periodofim from hosp.pacienteslaudo order by id_paciente";
                
                ArrayList<LaudoBean> lista = new ArrayList();

                try {
                    conexao = ConnectionFactory.getConnection();
                    PreparedStatement stm = conexao.prepareStatement(sql);
                    ResultSet rs = stm.executeQuery();

                    while (rs.next()) {
                    	LaudoBean l = new LaudoBean();
    	            
    	                //p.setDesceApac(rs.getString("descescola").toUpperCase());
    	                l.setCodLaudoDigita(rs.getInt("id"));
    	                l.setPaciente(pacieDao.listarPacientePorID(rs.getInt("id_paciente")));
    	                l.setEquipe(equipeDao.buscarEquipePorID(rs.getInt("codequipe")));
    	                l.setGrupo(grupoDao.listarGrupoPorId(rs.getInt("codgrupo")));
    	                l.setProfissional(profDao.listarProfissionalPorId(rs.getInt("codmedico")));
    	                l.setProcedimento(procDao.listarProcedimentoPorId(rs.getInt("codprocedimento")));
    	                l.setDtasolicitacao(rs.getDate("datasolicitacao"));
    	                l.setDtainicio(rs.getDate("periodoinicio"));
    	                l.setDtafim(rs.getDate("periodofim"));
    	              
    	              
    	                
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
            
            
            public List<LaudoBean> buscarTipoLaudoDigita(String valor, Integer tipo) {
        		
            	
         		 String sql = "select * from hosp.pacienteslaudo left join hosp.pacientes on pacienteslaudo.id_paciente=pacientes.id_paciente where";
         		
         		if (tipo == 1) {
         			sql += " pacientes.nome like ? order by pacientes.nome";
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
         				
         	
         	        l.setCodLaudoDigita(rs.getInt("id"));
   	                l.setPaciente(pacieDao.listarPacientePorID(rs.getInt("id_paciente")));
   	                l.setEquipe(equipeDao.buscarEquipePorID(rs.getInt("codequipe")));
   	                l.setGrupo(grupoDao.listarGrupoPorId(rs.getInt("codgrupo")));
   	                l.setProfissional(profDao.listarProfissionalPorId(rs.getInt("codmedico")));
   	                l.setProcedimento(procDao.listarProcedimentoPorId(rs.getInt("codprocedimento")));
   	                l.setDtasolicitacao(rs.getDate("datasolicitacao"));
   	                l.setDtainicio(rs.getDate("periodoinicio"));
   	                l.setDtafim(rs.getDate("periodofim"));
   	              
   	   

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
