package br.gov.al.maceio.sishosp.hosp.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.LaudoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProcedimentoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;



public class LaudoDAO {
	
	Connection con = null;
	PreparedStatement ps = null;
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
	
	// COME�O DO CODIGO
	
	public Boolean cadastrarLaudo(LaudoBean laudo) throws ProjetoException {
        boolean cadastrou = false;
        System.out.println("passou aqui 2");

        /*PacienteBean user_session = (PacienteBean) FacesContext
                .getCurrentInstance().getExternalContext().getSessionMap()
                .get("obj_paciente");*/

        String sql = "insert into hosp.apac (codpaciente, codprograma, codgrupo, codequipe, codmedico, codproc, dtasolicitacao, recurso, apac, unidade, situacao, dtautorizacao, cid10_1, cid10_2,cid10_3, "
        		+ "codfornecedor, valor, nota, qtd, codequipamento, obs, laudo) values (?, ? , ? , ?, ? , ?, ? , ?, ?, ?, "
        		+ "?, ?, ?, ?, ?, ?, ? , ? , ?, ?, ?, (select coalesce(gera_laudo_digita, false) laudo from hosp.proc where proc.codproc=?)";
        
            try {
            	System.out.println("passou aqui 3");
                conexao = ConnectionFactory.getConnection();
                PreparedStatement stmt = conexao.prepareStatement(sql);
                stmt.setLong(1, laudo.getPaciente().getId_paciente());
                stmt.setInt(2, laudo.getPrograma().getIdPrograma());
                stmt.setInt(3, laudo.getGrupo().getIdGrupo());
                stmt.setInt(4, laudo.getEquipe().getCodEquipe());
                stmt.setInt(5, laudo.getProfissional().getIdProfissional());
                stmt.setInt(6, laudo.getProcedimento().getIdProc());
                //stmt.setString(6, laudo.getPaciente().getCns().toUpperCase().trim());
                //stmt.setString(7, laudo.getPaciente().getCpf().toUpperCase().trim());
                stmt.setDate(7, new java.sql.Date(laudo.getDtasolicitacao().getTime()));
                stmt.setString(8, laudo.getRecurso());
                stmt.setString(9, laudo.getApac().toUpperCase().trim());
                stmt.setString(10, laudo.getUnidade().toUpperCase().trim());
                stmt.setString(11, laudo.getSituacao().toUpperCase().trim());
                if (laudo.getDtautorizacao() == null) {
    				stmt.setNull(12, Types.DATE);
    			} else {
    				stmt.setDate(12, new java.sql.Date(laudo.getDtautorizacao().getTime()));
    			}
                //stmt.setDate(12, new java.sql.Date(laudo.getDtautorizacao().getTime()));
                // stmt.setDate(12, new java.sql.Date(laudo.getDtavencimento().getTime()));
              
                if (laudo.getCid10_1() == null) {
    				stmt.setNull(13, Types.CHAR);
    			} else {
    				stmt.setString(13, laudo.getCid10_1().toUpperCase().trim());
    			}
                if (laudo.getCid10_2() == null) {
    				stmt.setNull(14, Types.CHAR);
    			} else {
    				stmt.setString(14, laudo.getCid10_2().toUpperCase().trim());
    			}
                if (laudo.getCid10_3() == null) {
    				stmt.setNull(15, Types.CHAR);
    			} else {
    				stmt.setString(15, laudo.getCid10_3().toUpperCase().trim());
    			}
                if (laudo.getFornecedor().getIdFornecedor() == null) {
    				stmt.setNull(16, Types.INTEGER);
    			} else {
    				stmt.setInt(16, laudo.getFornecedor().getIdFornecedor());
    			}
                if (laudo.getFornecedor().getValor() == null) {
    				stmt.setNull(17, Types.DOUBLE);
    			} else {
    				stmt.setDouble(17, laudo.getFornecedor().getValor());
    			}
                if (laudo.getNota() == null) {
    				stmt.setNull(18, Types.CHAR);
    			} else {
    				stmt.setString(18, laudo.getNota().toUpperCase().trim());
    			}
                if (laudo.getQtd() == null) {
    				stmt.setNull(19, Types.INTEGER);
    			} else {
    				stmt.setInt(19, laudo.getQtd());
    			}
                if (laudo.getEquipamento().getId_equipamento() == null) {
    				stmt.setNull(20, Types.INTEGER);
    			} else {
    				stmt.setInt(20, laudo.getEquipamento().getId_equipamento());
    			}
                if (laudo.getObs() == null) {
    				stmt.setNull(21, Types.CHAR);
    			} else {
    				stmt.setString(21, laudo.getObs().toUpperCase().trim());
    			}    
                stmt.setInt(22, laudo.getProcedimento().getIdProc());
                
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
        String sql = "update hosp.apac set codpaciente = ?, codprograma = ?, codgrupo = ?, codequipe = ?, codmedico = ?, "
        		+ "codproc = ?, dtasolicitacao = ?, recurso = ?, apac = ?, unidade = ?, situacao = ?, dtautorizacao = ?, "
        		+ "cid10_1 = ?, cid10_2 = ?, cid10_3 = ?, codfornecedor = ?, valor = ?, nota = ?, qtd = ?, codequipamento = ?, obs = ?, (select coalesce(gera_laudo_digita, false) laudo from hosp.proc where proc.codproc=?) where id_apac = ?";
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setLong(1, laudo.getPaciente().getId_paciente());
            stmt.setInt(2, laudo.getPrograma().getIdPrograma());
            stmt.setInt(3, laudo.getGrupo().getIdGrupo());
            stmt.setInt(4, laudo.getEquipe().getCodEquipe());
            stmt.setInt(5, laudo.getProfissional().getIdProfissional());
            stmt.setInt(6, laudo.getProcedimento().getIdProc());
            //stmt.setString(6, laudo.getPaciente().getCns().toUpperCase().trim());
            //stmt.setString(7, laudo.getPaciente().getCpf().toUpperCase().trim());
            stmt.setDate(7, new java.sql.Date(laudo.getDtasolicitacao().getTime()));
            stmt.setString(8, laudo.getRecurso());
            stmt.setString(9, laudo.getApac().toUpperCase().trim());
            stmt.setString(10, laudo.getUnidade().toUpperCase().trim());
            stmt.setString(11, laudo.getSituacao().toUpperCase().trim());
            stmt.setDate(12, new java.sql.Date(laudo.getDtautorizacao().getTime()));
            //stmt.setDate(12, new java.sql.Date(laudo.getDtachegada().getTime()));
            if (laudo.getCid10_1() == null) {
				stmt.setNull(13, Types.CHAR);
			} else {
				stmt.setString(13, laudo.getCid10_1().toUpperCase().trim());
			}
            if (laudo.getCid10_2() == null) {
				stmt.setNull(14, Types.CHAR);
			} else {
				stmt.setString(14, laudo.getCid10_2().toUpperCase().trim());
			}
            if (laudo.getCid10_3() == null) {
				stmt.setNull(15, Types.CHAR);
			} else {
				stmt.setString(15, laudo.getCid10_3().toUpperCase().trim());
			}
            if (laudo.getFornecedor().getIdFornecedor() == null) {
				stmt.setNull(16, Types.INTEGER);
			} else {
				stmt.setInt(16, laudo.getFornecedor().getIdFornecedor());
			}
            if (laudo.getFornecedor().getValor() == null) {
				stmt.setNull(17, Types.DOUBLE);
			} else {
				stmt.setDouble(17, laudo.getFornecedor().getValor());
			}
            if (laudo.getNota() == null) {
				stmt.setNull(18, Types.CHAR);
			} else {
				stmt.setString(18, laudo.getNota().toUpperCase().trim());
			}
            if (laudo.getQtd() == null) {
				stmt.setNull(19, Types.INTEGER);
			} else {
				stmt.setInt(19, laudo.getQtd());
			}
            if (laudo.getEquipamento().getId_equipamento() == null) {
				stmt.setNull(20, Types.INTEGER);
			} else {
				stmt.setInt(20, laudo.getEquipamento().getId_equipamento());
			}
            if (laudo.getObs() == null) {
				stmt.setNull(21, Types.CHAR);
			} else {
				stmt.setString(21, laudo.getObs().toUpperCase().trim());
			}           
            stmt.setInt(22, laudo.getProcedimento().getIdProc());
            stmt.setInt(23, laudo.getId_apac());
            
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

                String sql = "insert into hosp.pacienteslaudo (id_paciente, codmedico, codprocedimento, "
                		+ "datasolicitacao, periodofim) values (?, ?, ?, ?, ?)";
                //String sql = "insert into hosp.escola (descescola) values ((select max(cod) +1 from hosp.escola where codempresa=1), ?";
              
                    try {
                    	System.out.println("passou aqui 3");
                        conexao = ConnectionFactory.getConnection();
                        PreparedStatement stmt = conexao.prepareStatement(sql);
                        stmt.setLong(1, laudo.getPaciente().getId_paciente());
                        stmt.setInt(2, laudo.getProfissional().getIdProfissional());
                        stmt.setInt(3, laudo.getProcedimento().getIdProc());
                        //stmt.setString(6, laudo.getPaciente().getCns().toUpperCase().trim());
                        //stmt.setString(7, laudo.getPaciente().getCpf().toUpperCase().trim());
                        stmt.setDate(4, new java.sql.Date(laudo.getDtasolicitacao().getTime()));
                        //stmt.setInt(7, laudo.getProrrogar());
                        //stmt.setDate(5, new java.sql.Date(laudo.getDtainicio().getTime()));
                        stmt.setDate(5, new java.sql.Date(laudo.getDtafim().getTime()));
                    
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
                String sql = "update hosp.pacienteslaudo set id_paciente = ?, codmedico = ?, "
        		+ "codprocedimento = ?, datasolicitacao = ?, periodofim = ? where id = ?";
                try {
                    conexao = ConnectionFactory.getConnection();
                    PreparedStatement stmt = conexao.prepareStatement(sql);
                    stmt.setLong(1, laudo.getPaciente().getId_paciente());
                    stmt.setInt(2, laudo.getProfissional().getIdProfissional());
                    stmt.setInt(3, laudo.getProcedimento().getIdProc());
                    //stmt.setString(6, laudo.getPaciente().getCns().toUpperCase().trim());
                    //stmt.setString(7, laudo.getPaciente().getCpf().toUpperCase().trim());
                    stmt.setDate(4, new java.sql.Date(laudo.getDtasolicitacao().getTime()));
                    //stmt.setInt(7, laudo.getProrrogar());
                    //stmt.setDate(5, new java.sql.Date(laudo.getDtasolicitacao().getTime()));
                    stmt.setDate(5, new java.sql.Date(laudo.getDtafim().getTime()));
                    stmt.setInt(6, laudo.getCodLaudoDigita());
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
            
            public ArrayList<LaudoBean> listaLaudos() throws ProjetoException {

    
                /*String sql ="select apac.id_apac, apac.codpaciente, apac.codprograma, apac.codgrupo, "
                		+ "apac.codmedico, apac.codproc, apac.dtasolicitacao, apac.recurso, apac.apac, "
                		+ "apac.unidade, apac.situacao, apac.dtautorizacao, apac.cid10_1, apac.cid10_2, apac.codfornecedor, apac.valor, "
                		+ "apac.nota, apac.qtd, apac.codequipamento, "
                		+ "apac.obs from hosp.apac left join hosp.pacientes on apac.codpaciente=pacientes.id_paciente "
                		+ "left join hosp.programa on apac.codprograma=programa.id_programa "
                		+ "left join hosp.grupo on apac.codgrupo=grupo.id_grupo "
                		+ "left join hosp.medicos on apac.codmedico=medicos.id_medico "
                		+ "left join hosp.proc on apac.codproc=proc.id order by apac";*/
                
                String sql = "select id_apac, codpaciente, codprograma,codgrupo, codequipe, codmedico, "
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
    	                l.setEquipe(equipeDao.buscarEquipePorID(rs.getInt("codequipe")));
    	                l.setProfissional(profDao.buscarProfissionalPorId(rs.getInt("codmedico")));
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
            
            public List<LaudoBean> buscarTipoLaudo(String nome, String situacao, String recurso, Integer prontuario, Date dataAutorizacao, Date dataSolicitacao, Integer idPrograma, Integer idGrupo) throws ProjetoException {
        		
            	
          		 String sql = "select * from hosp.apac left join hosp.pacientes on apac.codpaciente=pacientes.id_paciente where";
          		if(idPrograma==null)
          			System.out.println("programa null");
          			
          		if (idPrograma!=null && dataAutorizacao!=null) {
          			sql += " CAST(apac.codprograma AS INT) = ? and apac.dtautorizacao = ? order by pacientes.nome";
          		}
          		/*if (tipo == 2) {
          			sql += " apac.situacao like ? order by pacientes.nome";
          		}
          		if (tipo == 3) {
          			sql += " apac.recurso like ? order by pacientes.nome";
          		}
          		if (tipo == 4) {
          			sql += " CAST(apac.id_apac AS INT) = ? order by pacientes.nome";
          		}
          		if (tipo == 5) {
          			sql += " BETWEEN = ? AND = ? order by pacientes.nome";
          		}*/
          
          		List<LaudoBean> lista = new ArrayList<>();
     
          		try {
          			conexao = ConnectionFactory.getConnection();
          			PreparedStatement stmt = conexao.prepareStatement(sql);
          			if (idPrograma!=null && dataAutorizacao!=null) {
          				System.out.println("Programa:"+idPrograma+"| DATA:|"+dataAutorizacao);
          				stmt.setInt(1,idPrograma);
          				stmt.setDate(2, new java.sql.Date(dataAutorizacao.getTime()));
          			}
          			/*if (tipo == 2) {
          				stmt.setString(1, "%" + valor.toUpperCase() + "%");
          			}
          			if (tipo == 3) {
          				stmt.setString(1, "%" + valor.toUpperCase() + "%");
          			}
          			if (tipo == 4) {
          				stmt.setInt(1,numero);
          			}
          			if (tipo == 5) {
          				stmt.setDate(1, new java.sql.Date(data.getTime()));
          				stmt.setDate(2, new java.sql.Date(data.getTime()));
          			}*/

          			ResultSet rs = stmt.executeQuery();

          			while (rs.next()) {
          				LaudoBean l = new LaudoBean();
          				
          	
          				l.setId_apac(rs.getInt("id_apac"));
     	                l.setApac(rs.getString("apac").toUpperCase());
    	                l.setPaciente(pacieDao.listarPacientePorID(rs.getInt("codpaciente")));
    	                l.setPrograma(progDao.listarProgramaPorId(rs.getInt("codprograma")));
    	                l.setGrupo(grupoDao.listarGrupoPorId(rs.getInt("codgrupo")));
    	                l.setEquipe(equipeDao.buscarEquipePorID(rs.getInt("codequipe")));
    	                l.setProfissional(profDao.buscarProfissionalPorId(rs.getInt("codmedico")));
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
            
            public List<LaudoBean> buscarTipoLaudo1(Integer idPrograma, Date dataAutorizacao) throws ProjetoException {
        		
            	
         		 String sql = "select * from hosp.apac left join hosp.pacientes on apac.codpaciente=pacientes.id_paciente where";
         		if(idPrograma==null)
         			System.out.println("programa null");
         			
         		if (idPrograma!=null && dataAutorizacao!=null) {
         			sql += " CAST(apac.codprograma AS INT) = ? and apac.dtautorizacao = ? order by pacientes.nome";
         		}
         		/*if (situacao!=null && recurso!=null && idPrograma!=null && idGrupo!=null && dataAutorizacao!=null && dataSolicitacao!=null) {
         			sql += " apac.situacao = ? and apac. recurso = ? and CAST(apac.codprograma AS INT) = ? and CAST(apac.codgrupo AS INT) = ? and apac.dtautorizacao = ? and apac.dtasolicitacao = ? order by pacientes.nome";
         		}
         		if (tipo == 3) {
         			sql += " apac.recurso like ? order by pacientes.nome";
         		}
         		if (tipo == 4) {
         			sql += " CAST(apac.id_apac AS INT) = ? order by pacientes.nome";
         		}
         		if (tipo == 5) {
         			sql += " BETWEEN = ? AND = ? order by pacientes.nome";
         		}*/
         
         		List<LaudoBean> lista = new ArrayList<>();
    
         		try {
         			conexao = ConnectionFactory.getConnection();
         			PreparedStatement stmt = conexao.prepareStatement(sql);
         			if (idPrograma!=null && dataAutorizacao!=null) {
         				System.out.println("Programa:"+idPrograma+"| DATA:|"+dataAutorizacao);
         				stmt.setInt(1, idPrograma);
         				stmt.setDate(2, new java.sql.Date(dataAutorizacao.getTime()));
         			}
         			/*if (tipo == 2) {
         				stmt.setString(1, "%" + valor.toUpperCase() + "%");
         			}
         			if (tipo == 3) {
         				stmt.setString(1, "%" + valor.toUpperCase() + "%");
         			}
         			if (tipo == 4) {
         				stmt.setInt(1,numero);
         			}
         			if (tipo == 5) {
         				stmt.setDate(1, new java.sql.Date(data.getTime()));
         				stmt.setDate(2, new java.sql.Date(data.getTime()));
         			}*/

         			ResultSet rs = stmt.executeQuery();

         			while (rs.next()) {
         				LaudoBean l = new LaudoBean();
         				
         	
         				l.setId_apac(rs.getInt("id_apac"));
    	                l.setApac(rs.getString("apac").toUpperCase());
   	                l.setPaciente(pacieDao.listarPacientePorID(rs.getInt("codpaciente")));
   	                l.setPrograma(progDao.listarProgramaPorId(rs.getInt("codprograma")));
   	                l.setGrupo(grupoDao.listarGrupoPorId(rs.getInt("codgrupo")));
   	                l.setEquipe(equipeDao.buscarEquipePorID(rs.getInt("codequipe")));
   	                l.setProfissional(profDao.buscarProfissionalPorId(rs.getInt("codmedico")));
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
            
            
            public ArrayList<LaudoBean> listaLaudosDigita() throws ProjetoException {

  
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
    	                l.setProfissional(profDao.buscarProfissionalPorId(rs.getInt("codmedico")));
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
            
            
            public List<LaudoBean> buscarTipoLaudoDigita(String valor, Integer tipo) throws ProjetoException {
        		
            	
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
   	                l.setProfissional(profDao.buscarProfissionalPorId(rs.getInt("codmedico")));
   	                l.setProcedimento(procDao.listarProcedimentoPorId(rs.getInt("codprocedimento")));
   	                //l.setPrograma(progDao.listarProgramaPorId(rs.getInt("codprograma")));
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
            
            
            public List<LaudoBean> buscarLaudoPersonalizado() throws ProjetoException {
            	LaudoBean l = new LaudoBean();
            	
        		 String sql = "select * from hosp.apac left join hosp.pacientes on apac.codpaciente=pacientes.id_paciente "
        		 		+ "left join hosp.programa on apac.codprograma=programa.id_programa where";
        		
        		if (l.getPrograma().getIdPrograma()!= null && l.getDtautorizacao()!= null) {
        			sql += " CAST(programa.id_programa AS TEXT) = ? and apac.dtautorizacao BETWEEN ? order by pacientes.nome";
        		}
        		List<LaudoBean> lista = new ArrayList<>();

        		try {
        			conexao = ConnectionFactory.getConnection();
        			PreparedStatement stmt = conexao.prepareStatement(sql);
        			if (l.getPrograma().getIdPrograma()!= null && l.getDtautorizacao()!= null) {
        				stmt.setInt(1, l.getPrograma().getIdPrograma());
        				stmt.setDate(2, new java.sql.Date(l.getDtautorizacao().getTime()));
        			}
        			
        			else{
        				System.out.println("porra nenhuma que entrou");
        				
        			}

        			ResultSet rs = stmt.executeQuery();

        			while (rs.next()) {    				
        		
  	                
  	            
  	          
  	                l.setCodLaudoDigita(rs.getInt("id"));
  	                l.setId_apac(rs.getInt("id_apac"));
	                l.setPaciente(pacieDao.listarPacientePorID(rs.getInt("codpaciente")));
	                l.setPrograma(progDao.listarProgramaPorId(rs.getInt("codprograma")));
	                l.setGrupo(grupoDao.listarGrupoPorId(rs.getInt("codgrupo")));
  	                l.setEquipe(equipeDao.buscarEquipePorID(rs.getInt("codequipe")));
	                l.setProfissional(profDao.buscarProfissionalPorId(rs.getInt("codmedico")));
	                l.setProcedimento(procDao.listarProcedimentoPorId(rs.getInt("codproc")));
	                l.setFornecedor(forneDao.listarFornecedorPorId(rs.getInt("codfornecedor")));
	                l.setRecurso(rs.getString("recurso"));
	                l.setApac(rs.getString("apac"));
	                l.setUnidade(rs.getString("unidade"));
	                l.setSituacao(rs.getString("situacao"));
	                l.setDtainicio(rs.getDate("periodoinicio"));
  	                l.setDtafim(rs.getDate("periodofim"));
	                l.setDtautorizacao(rs.getDate("dtautorizacao"));
	                l.setDtasolicitacao(rs.getDate("dtasolicitacao"));
	                l.setCid10_1(rs.getString("cid10_1"));
	                l.setCid10_2(rs.getString("cid10_2"));
	                l.setCid10_3(rs.getString("cid10_3"));
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
            
            
            public LaudoBean buscaLaudoPorId(Integer i) throws ProjetoException {
        		String sql = "select * from hosp.apac where id_apac = ?";
        		LaudoBean l = new LaudoBean();
        		try {
        			conexao = ConnectionFactory.getConnection();
        			ps = conexao.prepareStatement(sql);
        			ps.setInt(1, i);
        			ResultSet rs = ps.executeQuery();		
        			if (rs.next()) {
        				l = new LaudoBean();

      	                l.setCodLaudoDigita(rs.getInt("id"));
      	                l.setId_apac(rs.getInt("id_apac"));
    	                l.setPaciente(pacieDao.listarPacientePorID(rs.getInt("codpaciente")));
    	                l.setPrograma(progDao.listarProgramaPorId(rs.getInt("codprograma")));
    	                l.setGrupo(grupoDao.listarGrupoPorId(rs.getInt("codgrupo")));
      	                l.setEquipe(equipeDao.buscarEquipePorID(rs.getInt("codequipe")));
    	                l.setProfissional(profDao.buscarProfissionalPorId(rs.getInt("codmedico")));
    	                l.setProcedimento(procDao.listarProcedimentoPorId(rs.getInt("codproc")));
    	                l.setFornecedor(forneDao.listarFornecedorPorId(rs.getInt("codfornecedor")));
    	                l.setRecurso(rs.getString("recurso"));
    	                l.setApac(rs.getString("apac"));
    	                l.setUnidade(rs.getString("unidade"));
    	                l.setSituacao(rs.getString("situacao"));
    	                l.setDtainicio(rs.getDate("periodoinicio"));
      	                l.setDtafim(rs.getDate("periodofim"));
    	                l.setDtautorizacao(rs.getDate("dtautorizacao"));
    	                l.setDtasolicitacao(rs.getDate("dtasolicitacao"));
    	                l.setCid10_1(rs.getString("cid10_1"));
    	                l.setCid10_2(rs.getString("cid10_2"));
    	                l.setCid10_3(rs.getString("cid10_3"));
    	                l.setValor(rs.getDouble("valor"));
    	                l.setNota(rs.getString("nota"));
    	                l.setQtd(rs.getInt("qtd"));
    	                l.setCodequipamento(rs.getInt("codequipamento"));
    	                l.setObs(rs.getString("obs"));
      	                l.setLaudo(rs.getBoolean("laudo"));
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
    		return l;
    	}
            
            public LaudoBean listarLaudoPorId(int id) throws ProjetoException {
               
      		LaudoBean l = new LaudoBean();
      		String sql = "select id_apac, codpaciente, codprograma,codgrupo, codequipe, codmedico, "
            		+ "codproc,dtasolicitacao, recurso,apac, unidade, situacao, dtautorizacao, "
            		+ "cid10_1, cid10_2, cid10_3,codfornecedor,valor, nota, qtd, codequipamento, "
            		+ "obs from hosp.apac where id_apac = ?";
      		try {
      			con = ConnectionFactory.getConnection();
      			PreparedStatement stm = con.prepareStatement(sql);
      			stm.setInt(1, id);
      			ResultSet rs = stm.executeQuery();
      			while (rs.next()) {
      				l = new LaudoBean();
      			    l.setId_apac(rs.getInt("id_apac"));
	                l.setPaciente(pacieDao.listarPacientePorID(rs.getInt("codpaciente")));
	                l.setPrograma(progDao.listarProgramaPorId(rs.getInt("codprograma")));
	                l.setGrupo(grupoDao.listarGrupoPorId(rs.getInt("codgrupo")));
	                l.setEquipe(equipeDao.buscarEquipePorID(rs.getInt("codequipe")));
	                l.setProfissional(profDao.buscarProfissionalPorId(rs.getInt("codmedico")));
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
      		return l;
      	}
            
            
            
}
