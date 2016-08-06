package br.gov.al.maceio.sishosp.acl.dao;

import br.gov.al.maceio.sishosp.acl.model.Permissao;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Arthur Alves, Emerson Gama & Jerônimo do Nascimento 
 * @since 17/03/2015
 */
public class PermissaoDAO {
    
    private Connection conexao = null;
    
    public ArrayList<Permissao> buscarPermissaoDesc(String valor) {
        
        String sql = "select id, descricao from acl.permissao where upper(descricao) "
            + "like ? order by descricao";

        ArrayList<Permissao> lista = new ArrayList();
        
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, "%" + valor.toUpperCase() + "%");
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                Permissao p = new Permissao();
                p.setId(rs.getLong("id"));
                p.setDescricao(rs.getString("descricao"));
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
    
    public ArrayList<Permissao> listarPermissoes() {
        
        String sql = "select * from acl.permissao order by descricao";
             
        ArrayList<Permissao> lista = new ArrayList();

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stm = conexao.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();

            while(rs.next()) {            
                Permissao p = new Permissao();
                p.setId(rs.getLong("id"));
                p.setDescricao(rs.getString("descricao"));
                lista.add(p);
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
    
    public ArrayList<Permissao> listarPermPerfilSource() {
        
        String sql = "select pm.id, pm.descricao, si.id as id_sis, si.descricao as desc_sis, "
            + "si.sigla as sigla_sis from acl.permissao pm "
            + "join acl.perm_geral pg on pg.id_permissao = pm.id "
            + "join acl.menu me on me.id = pg.id_menu "
            + "join acl.menu_sistema ms on ms.id_menu = me.id "
            + "join acl.sistema si on si.id = ms.id_sistema "
            + "where me.tipo = 'menuItem' or me.tipo = 'menuItemRel'"
            + " union "
            + "select pm.id, pm.descricao, si.id as id_sis, si.descricao as desc_sis, "
            + "si.sigla as sigla_sis  from acl.permissao pm "
            + "join acl.perm_geral pg on pg.id_permissao = pm.id "
            + "join acl.funcao fu on fu.id = pg.id_funcao "
            + "join acl.sistema si on si.id = fu.id_sistema order by 2,1";
             
        ArrayList<Permissao> lista = new ArrayList();

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stm = conexao.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();

            while(rs.next()) {            
                Permissao p = new Permissao();
                p.setId(rs.getLong("id"));
                p.setDescricao(rs.getString("descricao"));
                
                p.setIdSistema(rs.getInt("id_sis"));
                p.setDescSistema(rs.getString("desc_sis"));
                p.setSiglaSistema(rs.getString("sigla_sis").toUpperCase());
                lista.add(p);
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

    public ArrayList<Permissao> listarPermNaoAssPerf(Integer id) {

        String sql = "select id, descricao from acl.permissao where id not in "
            + "(select pm.id from acl.permissao pm "
            + "join acl.perm_perfil pp on pm.id = pp.id_permissao "
            + "join acl.perfil pf on pf.id = pp.id_perfil where pf.id = ?)";
             
        ArrayList<Permissao> lista = new ArrayList();

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {            
                Permissao p = new Permissao();
                p.setId(rs.getLong("id"));
                p.setDescricao(rs.getString("descricao"));
                lista.add(p);
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
    
    public ArrayList<Permissao> listarPermAssPerf(Integer id) {

        String sql = "select pm.id, pm.descricao from acl.permissao pm "
            + "join acl.perm_perfil pp on pm.id = pp.id_permissao "
            + "join acl.perfil pf on pf.id = pp.id_perfil "
            + "where pf.id = ? group by pm.id";
             
        ArrayList<Permissao> lista = new ArrayList();

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {            
                Permissao p = new Permissao();
                p.setId(rs.getLong("id"));
                p.setDescricao(rs.getString("descricao"));
                lista.add(p);
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
    
    public ArrayList<Long> listarPermissoesPerfil(Long id) {

        String sql = "select pm.id as permid from acl.perm_perfil pp "
            + "join acl.perfil pf on pf.id = pp.id_perfil "
            + "join acl.permissao pm on pm.id = pp.id_permissao "
            + "where pf.id = ? group by pm.id order by 1";
             
        ArrayList<Long> lista = new ArrayList();

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                lista.add(rs.getLong("permid"));
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
    
    public Long recIdPermissoesMenu(Long idMenu) {

        String sql = "select id_permissao from acl.perm_geral where id_menu = ?";
             
        Long perm = null;

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setLong(1, idMenu);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                perm = rs.getLong("id_permissao");
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
        return perm;
    } 
    
    public Long recIdPermissoesFuncao(Long idFuncao) {

        String sql = "select id_permissao from acl.perm_geral where id_funcao = ?";
        
        Long perm = null;
        
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setLong(1, idFuncao);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                perm = rs.getLong("id_permissao");
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
        return perm;
    }
    
    public Long recIdPermissoesFuncionalidade(Long idFuncionalidade) {

        String sql = "select id_permissao from acl.perm_geral where id_funcionalidade = ?";
        
        Long perm = null;
        
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setLong(1, idFuncionalidade);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                perm = rs.getLong("id_permissao");
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
        return perm;
    }
}