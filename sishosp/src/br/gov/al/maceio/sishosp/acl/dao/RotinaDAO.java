package br.gov.al.maceio.sishosp.acl.dao;

import br.gov.al.maceio.sishosp.acl.model.Rotina;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Arthur Alves, Emerson Gama & Jerônimo do Nascimento 
 * @since 17/03/2015
 */
public class RotinaDAO {

    private Connection conexao = null;

    public boolean cadastrarRotina(Rotina rotina) {
    	  boolean cadastrou = false;
          
    	  try {
          
        String sql = "insert into acl.rotina (descricao, desc_pagina, diretorio, extensao) "
            + "values (?, ?, ?, ?)";
        
        	conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, rotina.getDescricao());
            stmt.setString(2, rotina.getDescPagina());
            stmt.setString(3, rotina.getDiretorio());
            stmt.setString(4, rotina.getExtensao());
           /* if(rotina.getIdSistema() == null) { 
                stmt.setNull(5, Types.INTEGER);
            } else {
                stmt.setString(1, rotina.getDescricao());
                stmt.setString(2, rotina.getDescPagina());
                stmt.setString(3, rotina.getDiretorio());
                stmt.setString(4, rotina.getExtensao());
                stmt.setInt(5, rotina.getIdSistema());
            }*/
            stmt.execute();
            conexao.commit();
            
            
            cadastrou = true;
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
          return cadastrou;
    }
    
    public boolean alterarRotina(Rotina rotina) {
        
        String sql = "update acl.rotina set descricao = ?, desc_pagina = ?, diretorio = ?, extensao = ? where id = ?";

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, rotina.getDescricao().toUpperCase());
            stmt.setString(2, rotina.getDescPagina().toUpperCase());
            stmt.setString(3, rotina.getDiretorio().toUpperCase());
            stmt.setString(4, rotina.getExtensao().toUpperCase());
            stmt.setLong(5, rotina.getId());
            stmt.executeUpdate();
            
            conexao.commit();
            
            /* if(rotina.getIdSistema() == null) {
                stmt.setNull(3, Types.INTEGER);
            } else {
                stmt.setInt(3, rotina.getIdSistema());
            }*/
            
            return true;
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
    }
    
    public boolean excluirRotina(Rotina rotina) {
        
        String sql = "delete from acl.rotina where id = ?";
        
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setLong(1, rotina.getId());
            stmt.execute();
            
            conexao.commit();
            
            return true;
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
    }
    
    public ArrayList<Rotina> buscarRotinaDescSis(String valor, Integer idSistema) {
        
        String sql = "select ro.id, ro.descricao, ro.tipo, ro.id_sistema, si.desc_pagina, ro.diretorio, ro.extensao "
            + "as sis_desc from acl.rotina ro join acl.sistema si on si.id = ro.id_sistema "
            + "where ro.descricao like ? and ro.id_sistema = ? order by ro.descricao";
        
        if(idSistema == 0) {
            sql = "select id, descricao, tipo, id_sistema, desc_pagina , diretorio, extensao, '' as sis_desc from acl.rotina "
                + "where descricao like ? and id_sistema is null order by descricao";
        }

        ArrayList<Rotina> lista = new ArrayList();
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, "%" + valor.toUpperCase() + "%");            
            if(idSistema > 0) {
                stmt.setInt(2, idSistema);
            }
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                Rotina r = new Rotina();
                r.setId(rs.getLong("id"));
                r.setDescricao(rs.getString("descricao"));
                //r.setIdSistema(rs.getInt("id_sistema"));
                r.setDiretorio(rs.getString("diretorio"));
                r.setDescPagina(rs.getString("desc_pagina"));
                r.setExtensao(rs.getString("extensao"));
                
             /*   if(r.getIdSistema() == null || r.getIdSistema() == 0) {
                    r.setDescSistema("Nenhum");
                } else {
                    r.setDescSistema(rs.getString("sis_desc"));
                }*/
                lista.add(r);
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

    public ArrayList<Rotina> listarRotinas() {
/*
        String sql = "select ro.id, ro.descricao, ro.tipo, ro.id_sistema, si.descricao, ro.diretorio, ro.desc_pagina, ro.extensao "
            + "as sis_desc from acl.rotina ro join acl.sistema si on si.id = ro.id_sistema "
            + "union select id, descricao, tipo, id_sistema, '' as sis_desc from acl.rotina "
            + "where id_sistema is null order by descricao";
    	   String sql="select ro.id, ro.descricao, ro.id_sistema, si.descricao as sis_desc, ro.diretorio, ro.desc_pagina, ro.extensao "  
    	            +"from acl.rotina ro "
    	            +"join acl.sistema si on si.id = ro.id_sistema "
    	            +"union "
    	            + "select id, descricao as desc, id_sistema, '' as sis_desc, '', '', '' "
    	            + "from acl.rotina where id_sistema is null order by descricao";*/
    	// Alterado 3 colunas de MENU PARA ROTINA
        String sql="select ro.id, ro.descricao, ro.id_sistema, ro.desc_pagina, ro.diretorio, ro.extensao "  
            +"from acl.rotina ro";
        ArrayList<Rotina> lista = new ArrayList();

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stm = conexao.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                Rotina r = new Rotina();
                r.setId(rs.getLong("id"));
                r.setDescricao(rs.getString("descricao"));
                //r.setIdSistema(rs.getInt("id_sistema"));
                r.setDiretorio(rs.getString("diretorio"));
                r.setDescPagina(rs.getString("desc_pagina"));
                r.setExtensao(rs.getString("extensao"));
                
                /*if(r.getIdSistema() == null || r.getIdSistema() == 0) {
                    r.setDescSistema("Comum");
                } else {
                    r.setDescSistema(rs.getString("sis_desc"));
                }*/
                lista.add(r);
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
    
    public ArrayList<Rotina> listarRotinasSemPerfil(Integer id) {

        String sql = "select * from acl.rotina "
            + "where id not in (select r.id from acl.rotina r "
            + "join acl.perm_geral pg on r.id = pg.id_rotina "
            + "join acl.perm_perfil pf on pf.id_permissao = pg.id_permissao "
            + "join acl.perfil p on p.id = pf.id_perfil where p.id = ?) "
            + "order by descricao";

        ArrayList<Rotina> lista = new ArrayList();

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stm = conexao.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                Rotina r = new Rotina();
                r.setId(rs.getLong("id"));
                r.setDescricao(rs.getString("descricao"));
                //r.setIdSistema(rs.getInt("id_sistema"));
                r.setDiretorio(rs.getString("diretorio"));
                r.setDescPagina(rs.getString("desc_pagina"));
                r.setExtensao(rs.getString("extensao"));
                lista.add(r);
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