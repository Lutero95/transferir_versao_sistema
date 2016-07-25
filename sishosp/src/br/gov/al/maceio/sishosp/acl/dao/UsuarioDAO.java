package br.gov.al.maceio.sishosp.acl.dao;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.acl.model.Menu;
import br.gov.al.maceio.sishosp.acl.model.Permissoes;
import br.gov.al.maceio.sishosp.acl.model.Sistema;
import br.gov.al.maceio.sishosp.acl.model.UsuarioBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import javax.faces.context.FacesContext;


public class UsuarioDAO{

    private Connection conexao = null;


    public UsuarioBean autenticarUsuario(UsuarioBean usuario) throws ProjetoException {

        String sql = "select us.id_usuario, us.descusuario, us.login, us.senha, us.email, us.autorizado, "
        		+ "pf.descricao as descperfil, case when us.ativo = 'S' "
        		+ "then true else false end as usuarioativo, "
        		+ "pf.id as idperfil, us.junta_medica from acl.usuarios us "
        		+ "join acl.perfil pf on (pf.id = us.id_perfil) "
        		+ "where (us.login = ?) and (upper(us.senha) = ?) and (us.ativo = ?)";

        UsuarioBean ub = null;
        int count = 1;
        String setoresUsuario = "";

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, usuario.getLogin().toUpperCase());
            pstmt.setString(2, usuario.getSenha().toUpperCase());
            pstmt.setString(3, "S");
            //pstmt.setBoolean(4, usuario.getAutorizado());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                ub = new UsuarioBean();
                ub.setCodigo(rs.getInt("id_usuario"));
                ub.setNome(rs.getString("descusuario"));
                ub.setLogin(rs.getString("login"));
                ub.setSenha(rs.getString("senha"));
                ub.setEmail(rs.getString("email"));
                ub.setAutorizado(rs.getBoolean("autorizado"));
  
                // ACL
                ub.setId(rs.getLong("id_usuario"));
                ub.setUsuarioAtivo(rs.getBoolean("usuarioativo"));
                ub.setIdPerfil(rs.getInt("idperfil"));
                ub.setDescPerfil(rs.getString("descperfil"));

                // RH
                ub.setJuntaMedica(rs.getBoolean("junta_medica"));

                if (rs.getRow() == 1) {
                    setoresUsuario = setoresUsuario + ub.getDescSetor() + " ";
                } else if (count == 2 && count != rs.getRow()) {
                    setoresUsuario = setoresUsuario + ", " + ub.getDescSetor() + ", ";
                } else if (count == rs.getRow()) {
                    setoresUsuario = setoresUsuario + ", " + ub.getDescSetor();
                }
                count++;
            }

            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("setores_usuario", setoresUsuario);
            return ub;
      
         
        } catch (SQLException ex) {
        	  ex.printStackTrace();
            throw new ProjetoException(ex);
        } finally {
            try {  
            	   conexao.close();
            } catch (Exception ex) {
                // TODO: handle exception
                ex.printStackTrace();
            }
        }
    }

    
    public List<Sistema> carregarSistemasUsuario(UsuarioBean usuario) throws ProjetoException {

        String sql = "select si.id, si.descricao, si.sigla, si.url, si.imagem, "
                + "si.versao, si.ativo from acl.perm_sistema ps "
                + "join acl.sistema si on si.id = ps.id_sistema "
                + "join acl.usuarios fuc on fuc.id_usuario = ps.id_usuario "
                + "where fuc.id_usuario = ? and si.ativo = true order by si.descricao";

        List<Sistema> listaSistemas = new ArrayList<>();

        Connection conexao = null;
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setLong(1, usuario.getCodigo());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Sistema s = new Sistema();
                s.setId(rs.getInt("id"));
                s.setDescricao(rs.getString("descricao"));
                s.setSigla(rs.getString("sigla"));
                s.setUrl(rs.getString("url") + "?faces-redirect=true");
                s.setImagem(rs.getString("imagem"));
                s.setVersao(rs.getString("versao"));
                s.setAtivo(rs.getBoolean("ativo"));
                listaSistemas.add(s);
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
        return listaSistemas;
    }

    public List<Permissoes> carregarPermissoes(UsuarioBean u) throws ProjetoException {

        String sql = "select  si.id as sid, si.descricao as sdesc, pf.descricao pfdesc, "
                + "pm.id as pmid, pm.descricao as pmdesc, 0 as funid, '' as fundesc, '' as funcodigo, "
                + " false as funativa, 0 as funidsis, me.id as meid, me.descricao as medesc, me.desc_pagina, "
                + "me.diretorio, me.extensao, me.codigo as cod_menu, me.indice, me.tipo, me.ativo as meativo, "
                + "me.action_rel, me.onclick_rel from acl.perm_perfil pp "
                + "join acl.usuarios us on us.id_perfil = pp.id_perfil "
                + "join acl.perfil pf on pf.id = pp.id_perfil "
                + "join acl.permissao pm on pm.id = pp.id_permissao "
                + "join acl.perm_geral pg on pg.id_permissao = pm.id "
                + "join acl.menu me on me.id = pg.id_menu "
                + "join acl.menu_sistema ms on ms.id_menu = me.id "
                + "join acl.sistema si on si.id = ms.id_sistema where us.id_usuario = ? and me.ativo = true "
                + "union select si.id as sid, si.descricao as sdesc, pf.descricao pfdesc, "
                + "pm.id pmid, pm.descricao as pmdesc, fun.id as funid, fun.descricao as fundesc, "
                + "fun.codigo as funcodigo, fun.ativa as funativa, fun.id_sistema as fusidsis, 0 as meid, "
                + "'' as medesc, '', '', '', '' as cod_menu, '', case when '' = '' then 'funcao' end, "
                + "false as meativo, '', '' from acl.perm_perfil pp "
                + "join acl.usuarios us on us.id_perfil = pp.id_perfil "
                + "join acl.perfil pf on pf.id = pp.id_perfil "
                + "join acl.permissao pm on pm.id = pp.id_permissao "
                + "join acl.perm_geral pg on pg.id_permissao = pm.id "
                + "join acl.funcao fun on fun.id = pg.id_funcao "
                + "join acl.sistema si on si.id = fun.id_sistema where us.id_usuario = ? and fun.ativa = true "
                + "union select si.id as sid, si.descricao as sdesc, '' as pfdesc, "
                + "pm.id as pmid, pm.descricao as pmdesc, 0 as funid, '' as fundesc, '' as funcodigo, "
                + "false as funativa, 0 as funidsis, me.id as meid, me.descricao as medesc, me.desc_pagina, "
                + "me.diretorio, me.extensao, me.codigo as cod_menu, me.indice, me.tipo, me.ativo as meativo, "
                + "me.action_rel, me.onclick_rel from acl.perm_usuario pu "
                + "join acl.usuarios us on us.id_usuario = pu.id_usuario "
                + "join acl.permissao pm on pm.id = pu.id_permissao "
                + "join acl.perm_geral pg on pg.id_permissao = pm.id "
                + "join acl.menu me on me.id = pg.id_menu "
                + "join acl.menu_sistema ms on ms.id_menu = me.id "
                + "join acl.sistema si on si.id = ms.id_sistema where us.id_usuario = ? and me.ativo = true "
                + "union select   si.id as sid, si.descricao as sdesc, '', "
                + "pm.id pmid, pm.descricao as pmdesc, fun.id as funid, fun.descricao as fundesc, "
                + "fun.codigo as funcodigo, fun.ativa as funativa, fun.id_sistema as funidsis, 0 as meid, "
                + "'' as medesc, '', '', '', '' as cod_menu, '', case when '' = '' then 'funcao' end, "
                + "false as meativo, '', ''  from acl.perm_usuario pu "
                + "join acl.usuarios us on us.id_usuario = pu.id_usuario "
                + "join acl.permissao pm on pm.id = pu.id_permissao "
                + "join acl.perm_geral pg on pg.id_permissao = pm.id "
                + "join acl.funcao fun on fun.id = pg.id_funcao "
                + "join acl.sistema si on si.id = fun.id_sistema where us.id_usuario = ? and fun.ativa = true "
                + "order by pmdesc, sid";

        List<Permissoes> lista = new ArrayList<>();

        Connection conexao = null;
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setLong(1, u.getCodigo());
            stmt.setLong(2, u.getCodigo());
            stmt.setLong(3, u.getCodigo());
            stmt.setLong(4, u.getCodigo());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Menu m = new Menu();
                m.setId(rs.getLong("meid"));
                m.setDescricao(rs.getString("medesc"));
                m.setCodigo(rs.getString("cod_menu"));
                m.setIndice(rs.getString("indice"));
                m.setTipo(rs.getString("tipo"));
                m.setAtivo(rs.getBoolean("meativo"));
                m.setDiretorio(rs.getString("diretorio"));
                m.setDescPagina(rs.getString("desc_pagina"));
                m.setExtensao(rs.getString("extensao"));
                m.setAction(rs.getString("action_rel"));
                m.setOnclick(rs.getString("onclick_rel"));

                if (rs.getString("tipo").equals("menuItem")) {
                    m.setUrl("/pages/" + m.getDiretorio() + "/"
                            + m.getDescPagina() + m.getExtensao());
                }

                if (rs.getString("funativa").equals("t")) {
                    m.setAtivo(true);
                }

                Permissoes p = new Permissoes();
                p.setIdSistema(rs.getInt("sid"));
                p.setDescSistema(rs.getString("sdesc"));
                p.setDescPerfil(rs.getString("pfdesc"));
                p.setIdPermissao(rs.getInt("pmid"));
                p.setDescPermissao(rs.getString("pmdesc"));
                p.setMenu(m);

                p.setCodigoFuncao(rs.getString("funcodigo"));
                p.setFuncaoAtiva(rs.getBoolean("funativa"));
                p.setIdSistemaFunc(rs.getInt("funidsis"));

                

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
    

    public UsuarioBean autenticarUsuarioacl(UsuarioBean usuario) {

        String sql = "select us.id, us.nome, us.sexo, us.cpf, us.email, us.login, "
                + "us.senha, us.ativo, us.id_perfil, pf.descricao from acl.usuario us "
                + "join acl.perfil pf on pf.id = us.id_perfil where login = ? and "
                + "senha = ? and ativo = true";

        UsuarioBean u = null;

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = null;
             stmt = conexao.prepareStatement(sql);
            stmt.setString(1, usuario.getLogin());
            stmt.setString(2, usuario.getSenha());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                u = new UsuarioBean();
                u.setId(rs.getLong("id"));
                u.setNome(rs.getString("nome"));
                u.setCpf(rs.getString("cpf"));
                u.setEmail(rs.getString("email"));
                u.setLogin(rs.getString("login"));
                u.setSenha(rs.getString("senha"));
                // u.setAtivo(rs.getBoolean("ativo"));
                u.setIdPerfil(rs.getInt("id_perfil"));
                u.setDescPerfil(rs.getString("descricao"));
            }
            rs.close();
            stmt.close();
            conexao.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return u;
    }

   
   
}
