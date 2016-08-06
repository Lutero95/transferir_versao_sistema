package br.gov.al.maceio.sishosp.acl.dao;

import br.gov.al.maceio.sishosp.acl.model.Funcionalidade;
import br.gov.al.maceio.sishosp.acl.model.Menu;
import br.gov.al.maceio.sishosp.acl.model.Sistema;
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
 * @author Thulio 
 * @since 17/03/2015
 */
public class FuncionalidadeDAO {
    
    private Connection conexao;
    
    public Boolean cadastrar(Funcionalidade funcionalidade) {       
        
        boolean cadastrou = false;
        List<Integer> listaId = funcionalidade.getListaSistemas();
        
        try {
            conexao = ConnectionFactory.getConnection();
            CallableStatement cs = conexao.prepareCall("{ ? = call acl.gravarfuncionalidade(?, ?, ?, ?, ?, ?) }");
            cs.registerOutParameter(1, Types.INTEGER);            
            cs.setString(2, funcionalidade.getDescricao());
            cs.setString(3, funcionalidade.getDescPagina());
            cs.setString(4, funcionalidade.getDiretorio());
            cs.setString(5, funcionalidade.getExtensao());
            cs.setBoolean(6, funcionalidade.isAtivo()); 
            cs.setString(7, funcionalidade.getImagem());
            cs.execute();
            
            Integer idRetornoFuncionalidade = cs.getInt(1);
            
            //String codAux = "MN-" + idRetornoFuncionalidade;

            cs = conexao.prepareCall("{ ? = call acl.gravarpermissao(?) }");
            cs.registerOutParameter(1, Types.INTEGER);            
            cs.setString(2, funcionalidade.getDescricao());
            cs.execute();
                       
            Integer idRetornoPerm = cs.getInt(1);
           
            String sql = "insert into acl.perm_geral (id_funcionalidade, id_permissao) values (?, ?)";
            PreparedStatement stmt = conexao.prepareStatement(sql);
        
            stmt = conexao.prepareStatement(sql);
            stmt.setLong(1, idRetornoFuncionalidade);
            stmt.setInt(2, idRetornoPerm);
            stmt.execute();
   
            conexao.commit();
            
            cadastrou = true;
        } catch(SQLException ex) {
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
    
    public Boolean alterar(Funcionalidade funcionalidade) {       
        
    	 String sql = "update acl.funcionalidade set descricao = ?, desc_pagina = ?, diretorio = ?, "
    	            + "extensao = ?, ativo = ?, imagem = ? where id = ?";
        
        boolean alterou = false;
        
        List<Integer> listaId = funcionalidade.getListaSistemas();
        
        try {            
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);           
            stmt.setString(1, funcionalidade.getDescricao());
            stmt.setString(2, funcionalidade.getDescPagina());
            stmt.setString(3, funcionalidade.getDiretorio());
            stmt.setString(4, funcionalidade.getExtensao());
            stmt.setBoolean(5, funcionalidade.isAtivo()); 
            stmt.setString(6, funcionalidade.getImagem());
            stmt.setLong(7, funcionalidade.getId());
            stmt.executeUpdate();
            
            /*if(!funcionalidade.getListaSistemas().isEmpty()) {
                sql = "delete from acl.menu_sistema where id_menu = ?";

                stmt = conexao.prepareStatement(sql);
                stmt.setLong(1, funcionalidade.getId());
                stmt.execute();

                sql = "insert into acl.menu_sistema (id_menu, id_sistema) values (?, ?)";

                stmt = conexao.prepareStatement(sql);
                for(Integer idSis : listaId) {
                    stmt.setLong(1, funcionalidade.getId());
                    stmt.setInt(2, idSis);
                    stmt.execute();
                }      
              
            } */   
            
            conexao.commit();
            
            alterou =  true;
        } catch(SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.exit(1);
            }
        }
        return alterou;
    }
    
    public boolean excluirFuncionalidade(Funcionalidade funcionalidade) {
        
        String sql = "delete from acl.funcionalidade where id = ?";
        
        boolean excluiu = false;
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setLong(1, funcionalidade.getId());
            stmt.execute();
            
            conexao.commit();
            
            excluiu = true;
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
        return excluiu;
    }
    
    public List<Funcionalidade> buscarFuncionalidadeDesc(String valor) {

        String sql = "select f.id, f.descricao, f.desc_pagina, f.diretorio, f.extensao, f.imagem, f.ativo from acl.funcionalidade f "
        		+ "where upper(f.descricao) like ? order by f.ativo desc, f.descricao";
             
        List<Funcionalidade> lista = new ArrayList();

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, "%" + valor.toUpperCase() + "%");
            ResultSet rs = stmt.executeQuery();
            
            while(rs.next()) {            
            	   Funcionalidade f = new Funcionalidade();              
      
                f.setId(rs.getLong("id"));
                f.setDescricao(rs.getString("descricao"));
                f.setDescPagina(rs.getString("desc_pagina"));
                f.setDiretorio(rs.getString("diretorio"));
                f.setExtensao(rs.getString("extensao"));
                f.setImagem(rs.getString("imagem"));
                f.setAtivo(rs.getBoolean("ativo"));
                

                lista.add(f);
            }
        } catch(SQLException ex) {
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
    
    public List<Funcionalidade> listarFuncionalidadesGeral() {

        String sql = "select f.id, f.descricao, f.desc_pagina, f.diretorio, f.extensao, f.imagem, f.ativo "
        		+ "from acl.funcionalidade f order by f.ativo desc, f.descricao";
             
        List<Funcionalidade> lista = new ArrayList();

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            while(rs.next()) {            
                Funcionalidade f = new Funcionalidade();               
                f.setId(rs.getLong("id"));
                f.setDescricao(rs.getString("descricao"));
                f.setDescPagina(rs.getString("desc_pagina"));
                f.setDiretorio(rs.getString("diretorio"));
                f.setExtensao(rs.getString("extensao"));
                f.setImagem(rs.getString("imagem"));
                f.setAtivo(rs.getBoolean("ativo"));
                lista.add(f);
            }
        } catch(SQLException ex) {
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
    
    public ArrayList<Menu> listarMenus() {

        String sql = "select * from acl.menu order by descricao";
             
        ArrayList<Menu> lista = new ArrayList();
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {            
                Menu m = new Menu();
                m.setId(rs.getLong("id"));
                m.setDescricao(rs.getString("descricao"));
                m.setUrl(rs.getString("url"));
                m.setCodigo(rs.getString("codigo"));
                m.setIndice(rs.getString("indice"));
                m.setTipo(rs.getString("tipo"));
                m.setAtivo(rs.getBoolean("ativo"));
                lista.add(m);
            }
        } catch(SQLException ex) {
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
    
    public List<Menu> listarMenusPaiSubmenus() {

        String sql = "select * from acl.menu where tipo = 'menuPai' or tipo = 'submenu' "
            + "and ativo = true order by descricao, tipo";
             
        List<Menu> lista = new ArrayList();

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            while(rs.next()) {            
                Menu m = new Menu();
                m.setId(rs.getLong("id"));
                m.setDescricao(rs.getString("descricao"));
                m.setCodigo(rs.getString("codigo"));
                m.setIndice(rs.getString("indice"));
                m.setTipo(rs.getString("tipo"));
                m.setAtivo(rs.getBoolean("ativo"));
                                
                m.setDiretorio(rs.getString("diretorio"));
                m.setDescPagina(rs.getString("desc_pagina"));
                m.setExtensao(rs.getString("extensao"));
                
                if(rs.getString("tipo").equals("menuItem")) {
                    m.setUrl("/pages/" + m.getDiretorio() + "/" + m.getDescPagina() + m.getExtensao());
                    
                }
                m.setIndiceAux(rs.getString("descricao"));
                lista.add(m);
            }
        } catch(SQLException ex) {
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
    
    public List<Menu> listarMenuItem() {

        String sql = "select * from acl.menu where tipo = 'menuItem' and ativo = true "
            + "order by descricao";
             
        List<Menu> lista = new ArrayList();

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            while(rs.next()) {            
                Menu m = new Menu();
                m.setId(rs.getLong("id"));
                m.setDescricao(rs.getString("descricao"));
                m.setUrl(rs.getString("url"));
                m.setCodigo(rs.getString("codigo"));
                m.setIndice(rs.getString("indice"));
                m.setTipo(rs.getString("tipo"));
                m.setAtivo(rs.getBoolean("ativo"));
                lista.add(m);
            }
        } catch(SQLException ex) {
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
    
    public ArrayList<Menu> listarMenuItemComSis() {

        String sql = "select me.id, me.descricao, me.codigo, me.indice, me.tipo, "
            + "me.ativo, diretorio, desc_pagina, extensao, si.id as id_sis, "
            + "si.descricao as desc_sis, si.sigla as sigla_sis from acl.permissao pm "
            + "join acl.perm_geral pg on pg.id_permissao = pm.id "
            + "join acl.menu me on me.id = pg.id_menu "
            + "join acl.menu_sistema ms on ms.id_menu = me.id "
            + "join acl.sistema si on si.id = ms.id_sistema "
            
            + "where me.tipo = 'menuItem' or me.tipo = 'menuItemRel'";
             
        ArrayList<Menu> lista = new ArrayList();
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {            
                Menu m = new Menu();
                m.setId(rs.getLong("id"));
                m.setDescricao(rs.getString("descricao"));
                m.setCodigo(rs.getString("codigo"));
                m.setIndice(rs.getString("indice"));
                m.setTipo(rs.getString("tipo"));
                m.setAtivo(rs.getBoolean("ativo"));
                                
                m.setDiretorio(rs.getString("diretorio"));
                m.setDescPagina(rs.getString("desc_pagina"));
                m.setExtensao(rs.getString("extensao"));
                
                if(rs.getString("tipo").equals("menuItem")) {
                    m.setUrl("/pages/" + m.getDiretorio() + "/" + m.getDescPagina() + m.getExtensao());
                    
                }              
                m.setIndiceAux(rs.getString("codigo"));
                
                m.setIdSistema(rs.getInt("id_sis"));
                m.setDescSistema(rs.getString("desc_sis"));
                m.setSiglaSistema(rs.getString("sigla_sis").toUpperCase());
                lista.add(m);
            }
        } catch(SQLException ex) {
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
    
    public ArrayList<Menu> listarMenuItemSourcerEdit(Integer idPerfil) {

        String sql = "select me.id, me.descricao, me.codigo, me.indice, me.tipo, "
            + "me.ativo, diretorio, desc_pagina, extensao, si.id as id_sis, "
            + "si.descricao as desc_sis, si.sigla as sigla_sis from acl.menu me "
            + "join acl.perm_geral pg on pg.id_menu = me.id "
            + "join acl.permissao pm on pm.id = pg.id_permissao "
            + "join acl.menu_sistema ms on ms.id_menu = me.id "
            + "join acl.sistema si on si.id = ms.id_sistema where me.id not in("
            + "select me.id from acl.perm_perfil pp "
            + "join acl.perfil pf on pf.id = pp.id_perfil "
            + "join acl.permissao pm on pm.id = pp.id_permissao "
            + "join acl.perm_geral pg on pg.id_permissao = pm.id "
            + "join acl.menu me on me.id = pg.id_menu "
            + "join acl.menu_sistema ms on ms.id_menu = me.id "
            + "join acl.sistema si on si.id = ms.id_sistema "
            + "where (me.tipo = 'menuItem' or me.tipo = 'menuItemRel') and pf.id = ?) "
            + "and (me.tipo = 'menuItem' or me.tipo = 'menuItemRel') order by me.descricao";
             
        ArrayList<Menu> lista = new ArrayList();
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, idPerfil);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {            
                Menu m = new Menu();
                m.setId(rs.getLong("id"));
                m.setDescricao(rs.getString("descricao"));
                m.setCodigo(rs.getString("codigo"));
                m.setIndice(rs.getString("indice"));
                m.setTipo(rs.getString("tipo"));
                m.setAtivo(rs.getBoolean("ativo"));
                                
                m.setDiretorio(rs.getString("diretorio"));
                m.setDescPagina(rs.getString("desc_pagina"));
                m.setExtensao(rs.getString("extensao"));
                
                if(rs.getString("tipo").equals("menuItem")) {
                    m.setUrl("/pages/" + m.getDiretorio() + "/" + m.getDescPagina() + m.getExtensao());
                    
                }              
                m.setIndiceAux(rs.getString("codigo"));
                
                m.setIdSistema(rs.getInt("id_sis"));
                m.setDescSistema(rs.getString("desc_sis"));
                m.setSiglaSistema(rs.getString("sigla_sis").toUpperCase());
                lista.add(m);
            }
        } catch(SQLException ex) {
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
    // LISTA OK
    public ArrayList<Menu> listarMenuItemTargetEdit(Integer idPerfil) {

        String sql = "select me.id, me.descricao, me.codigo, me.indice, me.tipo, "
            + "me.ativo, diretorio, desc_pagina, extensao, si.id as id_sis, "
            + "si.descricao as desc_sis, si.sigla as sigla_sis from acl.menu me "
            + "join acl.perm_geral pg on pg.id_menu = me.id "
            + "join acl.permissao pm on pm.id = pg.id_permissao "
            + "join acl.menu_sistema ms on ms.id_menu = me.id "
            + "join acl.sistema si on si.id = ms.id_sistema "
            + "join acl.perm_perfil pp on pp.id_permissao = pg.id_permissao "
            + "join acl.perfil pf on pf.id = pp.id_perfil "
            + "where (me.tipo = 'menuItem' or me.tipo = 'menuItemRel') "
            + "and pf.id = ? order by me.descricao;";
             
        ArrayList<Menu> lista = new ArrayList<>();
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, idPerfil);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {            
                Menu m = new Menu();
                m.setId(rs.getLong("id"));
                m.setDescricao(rs.getString("descricao"));
                m.setCodigo(rs.getString("codigo"));
                m.setIndice(rs.getString("indice"));
                m.setTipo(rs.getString("tipo"));
                m.setAtivo(rs.getBoolean("ativo"));
                                
                m.setDiretorio(rs.getString("diretorio"));
                m.setDescPagina(rs.getString("desc_pagina"));
                m.setExtensao(rs.getString("extensao"));
                
                if(rs.getString("tipo").equals("menuItem")) {
                  m.setUrl("/pages/" + m.getDiretorio() + "/" + m.getDescPagina() + m.getExtensao());
                  
                }              
                m.setIndiceAux(rs.getString("codigo"));
                
                m.setIdSistema(rs.getInt("id_sis"));
                m.setDescSistema(rs.getString("desc_sis"));
                m.setSiglaSistema(rs.getString("sigla_sis").toUpperCase());
                lista.add(m);
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
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
    
    public ArrayList<Menu> listarMenuPaiSubmenuComSis() {

        String sql = "select me.id, me.descricao, me.codigo, me.indice, me.tipo, me.ativo, "
            + "diretorio, desc_pagina, extensao, si.id as id_sis, "
            + "si.descricao as desc_sis, si.sigla as sigla_sis from acl.permissao pm "
            + "join acl.perm_geral pg on pg.id_permissao = pm.id "
            + "join acl.menu me on me.id = pg.id_menu "
            + "join acl.menu_sistema ms on ms.id_menu = me.id "
            + "join acl.sistema si on si.id = ms.id_sistema "
            + "where me.tipo = 'menuPai' or me.tipo = 'submenu'";
             
        ArrayList<Menu> lista = new ArrayList();
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                Menu m = new Menu();
                m.setId(rs.getLong("id"));
                m.setDescricao(rs.getString("descricao"));
                m.setCodigo(rs.getString("codigo"));
                m.setIndice(rs.getString("indice"));
                m.setTipo(rs.getString("tipo"));
                m.setAtivo(rs.getBoolean("ativo"));
                                
                m.setDiretorio(rs.getString("diretorio"));
                m.setDescPagina(rs.getString("desc_pagina"));
                m.setExtensao(rs.getString("extensao"));
                
                if(rs.getString("tipo").equals("menuItem")) {
                    m.setUrl("/pages/" + m.getDiretorio() + "/" + m.getDescPagina() + m.getExtensao());
                    
                }              
                m.setIndiceAux(rs.getString("codigo"));
                
                m.setIdSistema(rs.getInt("id_sis"));
                m.setDescSistema(rs.getString("desc_sis"));
                m.setSiglaSistema(rs.getString("sigla_sis").toUpperCase());
                lista.add(m);
            }
        } catch(SQLException ex) {
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
     
    public ArrayList<Menu> listarMenusPorSistema(Integer id) {

        String sql = "select ms.id, " +
            "ms.id_menu, " +
            "ms.id_sistema, " +
            "m.descricao " +
            "from acl.menu_sistema ms join acl.menu m " +
            "on m.id = ms.id_menu " +
            "join acl.sistema s on s.id = ms.id_sistema " +
            "where s.id = ? order by m.descricao";
                     
        ArrayList<Menu> lista = new ArrayList();

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {            
                Menu r = new Menu();
                r.setIdRecSistema(rs.getInt("id"));
                r.setDescricao(rs.getString("descricao"));
                r.setIdSistema(rs.getInt("id_sistema"));
                r.setId(rs.getLong("id_menu"));
                lista.add(r);
            }
        } catch(SQLException ex) {
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
    
    public ArrayList<Sistema> listarSisAssNaoMenuSource(Long idMenu) {

        String sql = "select id, descricao from acl.sistema where id not in "
            + "(select si.id from acl.sistema si "
            + "join acl.menu_sistema ms on ms.id_sistema = si.id "
            + "join acl.menu me on me.id = ms.id_menu "
            + "where me.id = ?)";
             
        ArrayList<Sistema> lista = new ArrayList();

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setLong(1, idMenu);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                Sistema s = new Sistema();
                s.setId(rs.getInt("id"));
                s.setDescricao(rs.getString("descricao"));
                lista.add(s);
            }
        } catch(SQLException ex) {
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
    
    public ArrayList<Sistema> listarSisAssMenuTarget(Long idMenu) {

        String sql = "select si.id, si.descricao from acl.sistema si "
            + "join acl.menu_sistema ms on ms.id_sistema = si.id "
            + "join acl.menu me on me.id = ms.id_menu "
            + "where me.id = ?";
             
        ArrayList<Sistema> lista = new ArrayList();

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setLong(1, idMenu);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                Sistema s = new Sistema();
                s.setId(rs.getInt("id"));
                s.setDescricao(rs.getString("descricao"));
                lista.add(s);
            }
        } catch(SQLException ex) {
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
    
    public ArrayList<Menu> listarMenuItemSourcerUser(Integer idPerfil) {

        String sql = "select me.id, me.descricao, me.codigo, me.indice, me.tipo, "
            + "me.ativo, diretorio, desc_pagina, extensao, si.id as id_sis, "
            + "si.descricao as desc_sis, si.sigla as sigla_sis from acl.menu me "
            + "join acl.perm_geral pg on pg.id_menu = me.id "
            + "join acl.permissao pm on pm.id = pg.id_permissao "
            + "join acl.menu_sistema ms on ms.id_menu = me.id "
            + "join acl.sistema si on si.id = ms.id_sistema "
            + "where me.id not in("
            + "	select me.id from acl.perm_perfil pp "
            + "	join acl.perfil pf on pf.id = pp.id_perfil "
            + "	join acl.permissao pm on pm.id = pp.id_permissao "
            + "	join acl.perm_geral pg on pg.id_permissao = pm.id "
            + "	join acl.menu me on me.id = pg.id_menu "
            + "	join acl.menu_sistema ms on ms.id_menu = me.id "
            + "	join acl.sistema si on si.id = ms.id_sistema "
            + "	where (me.tipo = 'menuItem' or me.tipo = 'menuItemRel') and pf.id = ?"
            + ") and (me.tipo = 'menuItem' or me.tipo = 'menuItemRel') order by me.descricao;";
             
        ArrayList<Menu> lista = new ArrayList();
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, idPerfil);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {            
                Menu m = new Menu();
                m.setId(rs.getLong("id"));
                m.setDescricao(rs.getString("descricao"));
                m.setCodigo(rs.getString("codigo"));
                m.setIndice(rs.getString("indice"));
                
                ////System.out.println("codigo: " + rs.getString("codigo"));
                ////System.out.println("indece: " + rs.getString("indice"));
                
                m.setTipo(rs.getString("tipo"));
                m.setAtivo(rs.getBoolean("ativo"));
                                
                m.setDiretorio(rs.getString("diretorio"));
                m.setDescPagina(rs.getString("desc_pagina"));
                m.setExtensao(rs.getString("extensao"));
                
                if(rs.getString("tipo").equals("menuItem")) {
                    m.setUrl("/pages/" + m.getDiretorio() + "/" + m.getDescPagina() + m.getExtensao());
                    
                }              
                m.setIndiceAux(rs.getString("codigo"));
                
                m.setIdSistema(rs.getInt("id_sis"));
                m.setDescSistema(rs.getString("desc_sis"));
                m.setSiglaSistema(rs.getString("sigla_sis").toUpperCase());
                lista.add(m);
            }
        } catch(SQLException ex) {
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
    
    public ArrayList<Menu> listarMenuItemSourcerEditUser(Integer idPerfil, Integer idUsuario) {

        System.out.println("MENU SOURCE");
        System.out.println("ID PERFIL: " + idPerfil);
        System.out.println("ID USU�?RIO: " + idUsuario + "\n");
        
        String sql = "select me.id, me.descricao, me.codigo, me.indice, me.tipo, "
            + "me.ativo, diretorio, desc_pagina, extensao, si.id as id_sis, "
            + "si.descricao as desc_sis, si.sigla as sigla_sis from acl.menu me "
            + "join acl.perm_geral pg on pg.id_menu = me.id "
            + "join acl.permissao pm on pm.id = pg.id_permissao "
            + "join acl.menu_sistema ms on ms.id_menu = me.id "
            + "join acl.sistema si on si.id = ms.id_sistema "
            + "where me.id not in("
            + "	select me.id from acl.perm_perfil pp "
            + "	join acl.perfil pf on pf.id = pp.id_perfil "
            + "	join acl.permissao pm on pm.id = pp.id_permissao "
            + "	join acl.perm_geral pg on pg.id_permissao = pm.id "
            + "	join acl.menu me on me.id = pg.id_menu "
            + "	join acl.menu_sistema ms on ms.id_menu = me.id "
            + "	join acl.sistema si on si.id = ms.id_sistema "
            + "	where (me.tipo = 'menuItem' or me.tipo = 'menuItemRel') and pf.id = ?"
            + "	union"
            + "	select me.id from acl.perm_usuario pu "
            + "	join acl.permissao pm on pm.id = pu.id_permissao "
            + "	join acl.perm_geral pg on pg.id_permissao = pm.id "
            + "	join acl.menu me on me.id = pg.id_menu "
            + "	join acl.menu_sistema ms on ms.id_menu = me.id "
            + "	join acl.sistema si on si.id = ms.id_sistema "
            + "	where (me.tipo = 'menuItem' or me.tipo = 'menuItemRel') and pu.id_usuario = ?"
            + ") and (me.tipo = 'menuItem' or me.tipo = 'menuItemRel') order by me.descricao;";
             
        ArrayList<Menu> lista = new ArrayList();
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, idPerfil);
            stmt.setInt(2, idUsuario);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {            
                Menu m = new Menu();
                m.setId(rs.getLong("id"));
                m.setDescricao(rs.getString("descricao"));
                m.setCodigo(rs.getString("codigo"));
                m.setIndice(rs.getString("indice"));
                
                ////System.out.println("codigo: " + rs.getString("codigo"));
                ////System.out.println("indece: " + rs.getString("indice"));
                
                m.setTipo(rs.getString("tipo"));
                m.setAtivo(rs.getBoolean("ativo"));
                                
                m.setDiretorio(rs.getString("diretorio"));
                m.setDescPagina(rs.getString("desc_pagina"));
                m.setExtensao(rs.getString("extensao"));
                
                if(rs.getString("tipo").equals("menuItem")) {
                    m.setUrl("/pages/" + m.getDiretorio() + "/" + m.getDescPagina() + m.getExtensao());
                    
                }              
                m.setIndiceAux(rs.getString("codigo"));
                
                m.setIdSistema(rs.getInt("id_sis"));
                m.setDescSistema(rs.getString("desc_sis"));
                m.setSiglaSistema(rs.getString("sigla_sis").toUpperCase());
                lista.add(m);
            }
        } catch(SQLException ex) {
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
    
    public ArrayList<Menu> listarMenuItemTargetEditUser(Integer idUsuario) {

        System.out.println("MENU TARGET");
        System.out.println("ID USU�?RIO: " + idUsuario + "\n");
        
        String sql = "select me.id, me.descricao, me.codigo, me.indice, me.tipo, "
            + "me.ativo, diretorio, desc_pagina, extensao, si.id as id_sis, "
            + "si.descricao as desc_sis, si.sigla as sigla_sis from acl.perm_usuario pu "
            + "join acl.permissao pm on pm.id = pu.id_permissao "
            + "join acl.perm_geral pg on pg.id_permissao = pm.id "
            + "join acl.menu me on me.id = pg.id_menu "
            + "join acl.menu_sistema ms on ms.id_menu = me.id "
            + "join acl.sistema si on si.id = ms.id_sistema "
            + "where (me.tipo = 'menuItem' or me.tipo = 'menuItemRel') and pu.id_usuario = ?";
             
        ArrayList<Menu> lista = new ArrayList();
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {            
                Menu m = new Menu();
                m.setId(rs.getLong("id"));
                m.setDescricao(rs.getString("descricao"));
                m.setCodigo(rs.getString("codigo"));
                m.setIndice(rs.getString("indice"));
                m.setTipo(rs.getString("tipo"));
                m.setAtivo(rs.getBoolean("ativo"));
                                
                m.setDiretorio(rs.getString("diretorio"));
                m.setDescPagina(rs.getString("desc_pagina"));
                m.setExtensao(rs.getString("extensao"));
                
                if(rs.getString("tipo").equals("menuItem")) {
                    m.setUrl("/pages/" + m.getDiretorio() + "/" + m.getDescPagina() + m.getExtensao());
                   
                }              
                m.setIndiceAux(rs.getString("codigo"));
                
                m.setIdSistema(rs.getInt("id_sis"));
                m.setDescSistema(rs.getString("desc_sis"));
                m.setSiglaSistema(rs.getString("sigla_sis").toUpperCase());
                lista.add(m);
            }
        } catch(SQLException ex) {
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
    
    
    public ArrayList<Menu> listarMenusPerfil(Integer idPerfil) {

        String sql = "select me.id, me.descricao, me.codigo, me.indice, me.tipo, "
            + "me.ativo, diretorio, desc_pagina, extensao, si.id as id_sis, "
            + "si.descricao as desc_sis, si.sigla as sigla_sis from acl.perm_perfil pp "
            + "join acl.permissao pm on pm.id = pp.id_permissao "
            + "join acl.perm_geral pg on pg.id_permissao = pm.id "
            + "join acl.menu me on me.id = pg.id_menu "
            + "join acl.menu_sistema ms on ms.id_menu = me.id "
            + "join acl.sistema si on si.id = ms.id_sistema "
            + "where pp.id_perfil = ? order by me.descricao";
             
        ArrayList<Menu> lista = new ArrayList();
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, idPerfil);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {            
                Menu m = new Menu();
                m.setId(rs.getLong("id"));
                m.setDescricao(rs.getString("descricao"));
                m.setCodigo(rs.getString("codigo"));
                m.setIndice(rs.getString("indice"));
                m.setTipo(rs.getString("tipo"));
                m.setAtivo(rs.getBoolean("ativo"));
                                
                m.setDiretorio(rs.getString("diretorio"));
                m.setDescPagina(rs.getString("desc_pagina"));
                m.setExtensao(rs.getString("extensao"));
                
                if(rs.getString("tipo").equals("menuItem")) {
                    m.setUrl("/pages/" + m.getDiretorio() + "/" + m.getDescPagina() + m.getExtensao());
                   
                }              
                m.setIndiceAux(rs.getString("codigo"));
                
                m.setIdSistema(rs.getInt("id_sis"));
                m.setDescSistema(rs.getString("desc_sis"));
                m.setSiglaSistema(rs.getString("sigla_sis").toUpperCase());
                lista.add(m);
            }
        } catch(SQLException ex) {
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