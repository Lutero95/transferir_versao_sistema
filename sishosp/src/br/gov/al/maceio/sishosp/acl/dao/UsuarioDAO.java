package br.gov.al.maceio.sishosp.acl.dao;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.LaudoBean;
import br.gov.al.maceio.sishosp.acl.model.Menu;
import br.gov.al.maceio.sishosp.acl.model.Permissao;
import br.gov.al.maceio.sishosp.acl.model.Permissoes;
import br.gov.al.maceio.sishosp.acl.model.Sistema;
import br.gov.al.maceio.sishosp.acl.model.UsuarioBean;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;














import javax.faces.context.FacesContext;


public class UsuarioDAO{

    private Connection conexao = null;


    public UsuarioBean autenticarUsuario(UsuarioBean usuario) throws ProjetoException {

        String sql = "select us.id_usuario, us.descusuario, us.login, us.senha, us.email, us.autorizado, "
        		+ "pf.descricao as descperfil, case when us.ativo = 'S' "
        		+ "then true else false end as usuarioativo, "
        		+ "pf.id as idperfil from acl.usuarios us "
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
    

    public UsuarioBean autenticarUsuarioacl(UsuarioBean usuario) throws ProjetoException {

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

    public Boolean cadastrar(UsuarioBean usuario) throws ProjetoException {
        boolean cadastrou = false;

        UsuarioBean user_session = (UsuarioBean) FacesContext
                .getCurrentInstance().getExternalContext().getSessionMap()
                .get("obj_usuario");

        try {
            conexao = ConnectionFactory.getConnection();
            CallableStatement cs = conexao.prepareCall("{ ? = call acl.gravausuario(?, ?, ?, ?, ?, ?, ?, ?, ?) }");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setString(2, usuario.getNome().toUpperCase().trim());
            cs.setString(3, usuario.getCpf().replaceAll("[^0-9]", ""));
            cs.setString(4, usuario.getSenha().toUpperCase().trim());
            cs.setString(5, "S");
            cs.setString(6, usuario.getLogin().toUpperCase().trim());
            cs.setString(7, usuario.getEmail().toLowerCase().trim());
            cs.setInt(8, user_session.getCodigo());
            cs.setBoolean(9, true);
            cs.setInt(10, usuario.getIdPerfil());
            //cs.setInt(11, usuario.getIdpessoa());
            cs.execute();

            UsuarioBean u = usuario;
            String idRetorno = null;
            idRetorno = String.valueOf(cs.getInt(1));
            u.setId(Long.parseLong(idRetorno));

            boolean associouSis = false;
            if (idRetorno != null) {
                associouSis = associarUsuarioAoSistemaCad(u);

            }

            boolean associouPerm = false;
            if (associouSis == true) {
                for (Long perm : u.getListaIdPermissoes()) {
                }
                associouPerm = associarPermAoUsuario(u);
            }

            if (idRetorno != null && associouSis != false) {

                cadastrou = true;
            }
            conexao.commit();
            conexao.close();

            return cadastrou;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                
            } catch (Exception e2) {
                // TODO: handle exception
                e2.printStackTrace();
            }

        }
    }
   
    public boolean associarUsuarioAoSistemaCad(UsuarioBean usuario) {

        String sql = "insert into acl.perm_sistema (id_usuario, id_sistema) values (?, ?)";

        boolean associou = false;

        List<Integer> listaId = usuario.getListaIdSistemas();

        try {
            // conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            for (Integer idSistema : listaId) {
                stmt.setLong(1, usuario.getId());
                stmt.setInt(2, idSistema);
                stmt.execute();
            }
            // conexao.commit();
            // stmt.close();
            // conexao.close();

            associou = true;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                
            } catch (Exception e2) {
                // TODO: handle exception
                e2.printStackTrace();
            }

        }
        return associou;
    }
    
    public boolean associarPermAoUsuario(UsuarioBean usuario) {

        String sql = "insert into acl.perm_usuario (id_usuario, id_permissao, dtacadastro) values (?, ?, CURRENT_TIMESTAMP)";

        boolean associou = false;

        List<Long> listaId = usuario.getListaIdPermissoes();
        System.out.println("id"+usuario.getId());
        System.out.println("lista"+listaId.size());
        try {
            // conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            for (Long idPermissao : listaId) {
                stmt.setLong(1, usuario.getId());
                stmt.setLong(2, idPermissao);
                stmt.execute();
            }
            // conexao.commit();
            // stmt.close();
            // conexao.close();

            associou = true;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                
            } catch (Exception e2) {
                // TODO: handle exception
                e2.printStackTrace();
            }

        }
        return associou;
    }
    
    
    public Boolean alterar(UsuarioBean usuario) throws ProjetoException {
        boolean cadastrou = false;
        String sql = "update acl.usuarios set descusuario = ?, cpf = ?, email = ?, "
                + "login = ?, senha = ?, id_perfil = ?, ativo = ? where id_usuario = ?";
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getCpf().replaceAll("[^0-9]", ""));
            stmt.setString(3, usuario.getEmail());
            stmt.setString(4, usuario.getLogin());
            stmt.setString(5, usuario.getSenha());
            stmt.setInt(6, usuario.getIdPerfil());
            stmt.setString(7, usuario.getAtivo());
            stmt.setLong(8, usuario.getCodigo());
            stmt.executeUpdate();

            UsuarioBean u = usuario;

            sql = "delete from acl.perm_sistema where id_usuario = ?";
            stmt = conexao.prepareStatement(sql);
            stmt.setLong(1, usuario.getCodigo());
            stmt.execute();

            sql = "insert into acl.perm_sistema (id_usuario, id_sistema) values (?, ?)";
            List<Integer> listaId = usuario.getListaIdSistemas();
            stmt = conexao.prepareStatement(sql);
            for (Integer idSistema : listaId) {
                stmt.setLong(1, usuario.getCodigo());
                stmt.setInt(2, idSistema);
                stmt.execute();
            }

            sql = "delete from acl.perm_usuario where id_usuario = ?";
            stmt = conexao.prepareStatement(sql);
            stmt.setLong(1, usuario.getCodigo());
            stmt.execute();

            sql = "insert into acl.perm_usuario (id_usuario, id_permissao) values (?, ?)";
            List<Long> listaPerm = usuario.getListaIdPermissoes();
            stmt = conexao.prepareStatement(sql);
            for (Long idPerm : listaPerm) {
                stmt.setLong(1, usuario.getCodigo());
                stmt.setLong(2, idPerm);
                stmt.execute();
            }

            conexao.commit();

            cadastrou = true;

            return cadastrou;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
   
    public Boolean alterarSemPerm(UsuarioBean usuario) throws ProjetoException {
        boolean cadastrou = false;
        String sql = " update acl.usuarios set descusuario = ?, cpf = ?, email = ?, "
                + " login = ?, senha = ?, id_perfil = ?, ativo = ? where id_usuario = ? ";

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getCpf().replaceAll("[^0-9]", ""));
            stmt.setString(3, usuario.getEmail());
            stmt.setString(4, usuario.getLogin());
            stmt.setString(5, usuario.getSenha());
            stmt.setInt(6, usuario.getIdPerfil());
            stmt.setString(7, usuario.getAtivo());
            stmt.setLong(8, usuario.getCodigo());
            
            stmt.executeUpdate();
            UsuarioBean u = usuario;

            boolean associouSis = false;

            if (u.getCodigo() != null) {
                sql = "delete from acl.perm_sistema where id_usuario = ?";
                String sql2 = "insert into acl.perm_sistema (id_usuario, id_sistema) values (?, ?)";
                List<Integer> listaId = usuario.getListaIdSistemas();
                PreparedStatement stmt2;
                try {
                    stmt2 = conexao.prepareStatement(sql);
                    stmt2.setLong(1, usuario.getCodigo());
                    stmt2.execute();
                } catch (SQLException ex) {
                    ex.getMessage();
                    throw new RuntimeException(ex);
                }
                if (!usuario.getListaIdSistemas().isEmpty()) {
                    try {
                        stmt2 = conexao.prepareStatement(sql2);
                        for (Integer idSistema : listaId) {
                            stmt2.setLong(1, usuario.getCodigo());
                            stmt2.setInt(2, idSistema);
                            stmt2.execute();
                        }
                        associouSis = true;
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }

            boolean associouPerm = false;
            if (associouSis == true) {
                sql = "delete from acl.perm_usuario where id_usuario = ?";
                try {
                    PreparedStatement stmt3 = conexao.prepareStatement(sql);
                    stmt3.setLong(1, usuario.getCodigo());
                    stmt3.execute();
                    associouPerm = true;
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            
            cadastrou = true;
            conexao.commit();
            stmt.close();
            conexao.close();
            return cadastrou;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                conexao.close();
            } catch (Exception e2) {
                // TODO: handle exception
                e2.printStackTrace();
            }

        }
    }
    
    public ArrayList<UsuarioBean> buscaUsuarios() throws ProjetoException {

        
    	String sql = "select * from acl.usuarios u order by ativo,descusuario";

        ArrayList<UsuarioBean> lista = new ArrayList();

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stm = conexao.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
           UsuarioBean n = new UsuarioBean();
           n = new UsuarioBean();
           n.setCodigo(rs.getInt("id_usuario"));
           n.setNome(rs.getString("descusuario"));
           n.setSenha(rs.getString("senha"));
           n.setAtivo(rs.getString("ativo"));
           n.setLogin(rs.getString("login"));
           n.setAdministrador((rs.getBoolean("administrador")));
           n.setEmail(rs.getString("email"));
           n.setCpf(rs.getString("cpf"));
           n.setIdPerfil(rs.getInt("id_perfil"));
                
                lista.add(n);
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

    public List<Sistema> recListaSisSoucerCad() throws ProjetoException {

        String sql = "select id, descricao from acl.sistema "
                + "where ativo = true order by descricao";

        List<Sistema> listaSistemas = new ArrayList<>();

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Sistema s = new Sistema();
                s.setId(rs.getInt("id"));
                s.setDescricao(rs.getString("descricao"));
                listaSistemas.add(s);
            }
            rs.close();
            stmt.close();
            conexao.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }finally {
            try {
                conexao.close();
            } catch (Exception e2) {
                // TODO: handle exception
                e2.printStackTrace();
            }

        }
        return listaSistemas;
    }

    public List<Sistema> recListaSisSoucer(Long id) throws ProjetoException {

        String sql = "select id, descricao from acl.sistema "
                + "where id not in(select si.id from acl.perm_sistema ps "
                + "join acl.sistema si on si.id = ps.id_sistema "
                + "join acl.usuarios us on us.id_usuario = ps.id_usuario "
                + "where us.id_usuario = ?) and ativo = true order by descricao";

        List<Sistema> listaSistemas = new ArrayList<>();

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Sistema s = new Sistema();
                s.setId(rs.getInt("id"));
                s.setDescricao(rs.getString("descricao"));
                listaSistemas.add(s);
            }
            rs.close();
            stmt.close();
            conexao.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }finally {
            try {
                conexao.close();
            } catch (Exception e2) {
                // TODO: handle exception
                e2.printStackTrace();
            }

        }
        return listaSistemas;
    }

    public List<Sistema> recListaSisTarget(Long id) throws ProjetoException {

        String sql = "select si.id, si.descricao from acl.perm_sistema ps "
                + "join acl.sistema si on si.id = ps.id_sistema "
                + "join acl.usuarios us on us.id_usuario = ps.id_usuario "
                + "where us.id_usuario = ? and us.ativo = 'S' order by si.descricao";

        List<Sistema> listaSistemas = new ArrayList<>();

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Sistema s = new Sistema();
                s.setId(rs.getInt("id"));
                s.setDescricao(rs.getString("descricao"));
                listaSistemas.add(s);
            }
            rs.close();
            stmt.close();
            conexao.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }finally {
            try {
                conexao.close();
            } catch (Exception e2) {
                // TODO: handle exception
                e2.printStackTrace();
            }

        }
        return listaSistemas;
    }
    
    public ArrayList<Permissao> listarPermSemPerfSource(Integer id) throws ProjetoException {

        String sql = "select p.id, p.descricao from acl.permissao p "
                + "where p.id not in (select pm.id from acl.perfil pf "
                + "join acl.perm_perfil pp on pf.id = pp.id_perfil "
                + "join acl.permissao pm on pm.id = pp.id_permissao "
                + "where pf.id = ?) order by 2";

        ArrayList<Permissao> lista = new ArrayList<>();

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Permissao p = new Permissao();
                p.setId(rs.getLong("id"));
                p.setDescricao(rs.getString("descricao"));
                lista.add(p);
            }
            rs.close();
            stmt.close();
            conexao.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }finally {
            try {
                conexao.close();
            } catch (Exception e2) {
                // TODO: handle exception
                e2.printStackTrace();
            }

        }
        return lista;
    }

    public ArrayList<Permissao> listarPermSemPerfAssSource(Integer idPefil,
            Integer idUsuario) throws ProjetoException {

        String sql = "select id, descricao from acl.permissao where id not in ("
                + "select pm.id from acl.perfil pf "
                + "join acl.perm_perfil pp on pf.id = pp.id_perfil "
                + "join acl.permissao pm on pm.id = pp.id_permissao "
                + "where pf.id = ? "
                + "union select p.id from acl.usuarios u "
                + "join acl.perm_usuario pu on u.id_usuario = pu.id_usuario "
                + "join acl.permissao p on p.id = pu.id_permissao "
                + "where u.id_usuario = ? order by 1) order by 2";

        ArrayList<Permissao> lista = new ArrayList<>();

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, idPefil);
            stmt.setInt(2, idUsuario);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Permissao p = new Permissao();
                p.setId(rs.getLong("id"));
                p.setDescricao(rs.getString("descricao"));
                lista.add(p);
            }
            rs.close();
            stmt.close();
            conexao.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }finally {
            try {
                conexao.close();
            } catch (Exception e2) {
                // TODO: handle exception
                e2.printStackTrace();
            }

        }
        return lista;
    }

    public ArrayList<Permissao> listarPermSemPerfAssTarget(Integer id) throws ProjetoException {

        String sql = "select p.id, p.descricao from acl.usuarios u "
                + "join acl.perm_usuario pu on u.id_usuario = pu.id_usuario "
                + "join acl.permissao p on p.id = pu.id_permissao "
                + "where u.id_usuario = ? order by 2";

        ArrayList<Permissao> lista = new ArrayList<>();

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Permissao p = new Permissao();
                p.setId(rs.getLong("id"));
                p.setDescricao(rs.getString("descricao"));
                lista.add(p);
            }
            rs.close();
            stmt.close();
            conexao.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }finally {
            try {
                conexao.close();
            } catch (Exception e2) {
                // TODO: handle exception
                e2.printStackTrace();
            }

        }
        return lista;
    }

    public Boolean alterarSenha(UsuarioBean usuario) throws ProjetoException {

        String sql = "update acl.usuarios set senha = ? where id_usuario = ?";

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, usuario.getSenha());
            stmt.setLong(2, usuario.getCodigo());
            stmt.executeUpdate();
            conexao.commit();
            stmt.close();
            conexao.close();

            return true;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                conexao.close();
            } catch (Exception e2) {
                // TODO: handle exception
                e2.printStackTrace();
            }

        }
    }
    
    
    public String verificaLoginCadastrado(String login) throws ProjetoException {
        PreparedStatement ps = null;
        Connection con = ConnectionFactory.getConnection();
        String isExist = "N";

        try {
            String sql = "select * from acl.usuarios where login=?";
            ps = con.prepareStatement(sql);
            ps.setString(1, login.toUpperCase());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                isExist = "S";
            }

        } catch (Exception sqle) {
            throw new ProjetoException(sqle);
        } finally {
            try {
                con.close();
            } catch (Exception sqlc) {
                sqlc.printStackTrace();
                System.exit(1);
                // TODO: handle exception
            }
        }
        return isExist;
    }
  
    public String verificaUserCadastrado(String cpf) throws ProjetoException {
        PreparedStatement ps = null;
        Connection con = ConnectionFactory.getConnection();
        String isExist = "N";

        try {
            String sql = "select * from acl.usuarios where cpf=? and ativo=?";

            ps = con.prepareStatement(sql);
            ps.setString(1, cpf);
            ps.setString(2, "S");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                isExist = "S";
            }

        } catch (Exception sqle) {

            throw new ProjetoException(sqle);

        } finally {
            try {
                con.close();
            } catch (Exception sqlc) {
                sqlc.printStackTrace();
                System.exit(1);
                // TODO: handle exception
            }

        }
        return isExist;
    }
    
}
