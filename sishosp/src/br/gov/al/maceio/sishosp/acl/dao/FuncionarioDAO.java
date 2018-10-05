package br.gov.al.maceio.sishosp.acl.dao;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.dao.CboDAO;
import br.gov.al.maceio.sishosp.hosp.dao.EspecialidadeDAO;
import br.gov.al.maceio.sishosp.hosp.dao.GrupoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.ProcedimentoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.ProgramaDAO;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.LaudoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;
import br.gov.al.maceio.sishosp.acl.model.Menu;
import br.gov.al.maceio.sishosp.acl.model.Permissao;
import br.gov.al.maceio.sishosp.acl.model.Permissoes;
import br.gov.al.maceio.sishosp.acl.model.Sistema;
import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

public class FuncionarioDAO {

    private Connection conexao = null;
    private Connection con = null;
    private PreparedStatement ps = null;
    private EspecialidadeDAO espDao = new EspecialidadeDAO();
    private ProgramaDAO progDao = new ProgramaDAO();
    private CboDAO cDao = new CboDAO();
    private GrupoDAO gDao = new GrupoDAO();
    private ProcedimentoDAO procDao = new ProcedimentoDAO();

    public FuncionarioBean autenticarUsuario(FuncionarioBean usuario)
            throws ProjetoException {

        String sql = "select us.id_funcionario, us.descfuncionario, us.senha, us.email, "
                + "pf.descricao as descperfil, case when us.ativo = 'S' "
                + "then true else false end as usuarioativo, "
                + "pf.id as idperfil from acl.funcionarios us "
                + "join acl.perfil pf on (pf.id = us.id_perfil) "
                + "where (us.cpf = ?) and (upper(us.senha) = ?) and (? = 'S')";

        FuncionarioBean ub = null;
        int count = 1;
        String setoresUsuario = "";

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, usuario.getCpf().replaceAll("[^0-9]", ""));
            pstmt.setString(2, usuario.getSenha().toUpperCase());
            pstmt.setString(3, usuario.getAtivo());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                ub = new FuncionarioBean();
                ub.setCodigo(rs.getInt("id_funcionario"));
                ub.setNome(rs.getString("descfuncionario"));
                ub.setSenha(rs.getString("senha"));
                ub.setEmail(rs.getString("email"));

                // ACL
                ub.setId(rs.getLong("id_funcionario"));
                ub.setUsuarioAtivo(rs.getBoolean("usuarioativo"));
                ub.getPerfil().setId(rs.getLong("idperfil"));
                ub.getPerfil().setDescricao(rs.getString("descperfil"));

                count++;
            }

            FacesContext.getCurrentInstance().getExternalContext()
                    .getSessionMap().put("setores_usuario", setoresUsuario);
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

    public String usuarioAtivo(FuncionarioBean usuario) throws ProjetoException {

        String ativo = "";

        String sql = "select ativo from acl.funcionarios where cpf = ? and senha = ?";

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, usuario.getCpf().replaceAll("[^0-9]", ""));
            pstmt.setString(2, usuario.getSenha().toUpperCase());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                ativo = rs.getString("ativo");

            }

            return ativo;

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

    public List<Sistema> carregarSistemasUsuario(FuncionarioBean usuario)
            throws ProjetoException {

        String sql = "select si.id, si.descricao, si.sigla, si.url, si.imagem, "
                + "si.versao, si.ativo from acl.perm_sistema ps "
                + "join acl.sistema si on si.id = ps.id_sistema "
                + "join acl.funcionarios fuc on fuc.id_funcionario = ps.id_usuario "
                + "where fuc.id_funcionario = ? and si.ativo = true order by si.descricao";

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

    public List<Permissoes> carregarPermissoes(FuncionarioBean u)
            throws ProjetoException {

        String sql = "select  si.id as sid, si.descricao as sdesc, pf.descricao pfdesc, "
                + "pm.id as pmid, pm.descricao as pmdesc, 0 as funid, '' as fundesc, '' as funcodigo, "
                + " false as funativa, 0 as funidsis, me.id as meid, me.descricao as medesc, me.desc_pagina, "
                + "me.diretorio, me.extensao, me.codigo as cod_menu, me.indice, me.tipo, me.ativo as meativo, "
                + "me.action_rel, me.onclick_rel from acl.perm_perfil pp "
                + "join acl.funcionarios us on us.id_perfil = pp.id_perfil "
                + "join acl.perfil pf on pf.id = pp.id_perfil "
                + "join acl.permissao pm on pm.id = pp.id_permissao "
                + "join acl.perm_geral pg on pg.id_permissao = pm.id "
                + "join acl.menu me on me.id = pg.id_menu "
                + "join acl.menu_sistema ms on ms.id_menu = me.id "
                + "join acl.sistema si on si.id = ms.id_sistema where us.id_funcionario = ? and me.ativo = true "
                + "union select si.id as sid, si.descricao as sdesc, pf.descricao pfdesc, "
                + "pm.id pmid, pm.descricao as pmdesc, fun.id as funid, fun.descricao as fundesc, "
                + "fun.codigo as funcodigo, fun.ativa as funativa, fun.id_sistema as fusidsis, 0 as meid, "
                + "'' as medesc, '', '', '', '' as cod_menu, '', case when '' = '' then 'funcao' end, "
                + "false as meativo, '', '' from acl.perm_perfil pp "
                + "join acl.funcionarios us on us.id_perfil = pp.id_perfil "
                + "join acl.perfil pf on pf.id = pp.id_perfil "
                + "join acl.permissao pm on pm.id = pp.id_permissao "
                + "join acl.perm_geral pg on pg.id_permissao = pm.id "
                + "join acl.funcao fun on fun.id = pg.id_funcao "
                + "join acl.sistema si on si.id = fun.id_sistema where us.id_funcionario = ? and fun.ativa = true "
                + "union select si.id as sid, si.descricao as sdesc, '' as pfdesc, "
                + "pm.id as pmid, pm.descricao as pmdesc, 0 as funid, '' as fundesc, '' as funcodigo, "
                + "false as funativa, 0 as funidsis, me.id as meid, me.descricao as medesc, me.desc_pagina, "
                + "me.diretorio, me.extensao, me.codigo as cod_menu, me.indice, me.tipo, me.ativo as meativo, "
                + "me.action_rel, me.onclick_rel from acl.perm_usuario pu "
                + "join acl.funcionarios us on us.id_funcionario = pu.id_usuario "
                + "join acl.permissao pm on pm.id = pu.id_permissao "
                + "join acl.perm_geral pg on pg.id_permissao = pm.id "
                + "join acl.menu me on me.id = pg.id_menu "
                + "join acl.menu_sistema ms on ms.id_menu = me.id "
                + "join acl.sistema si on si.id = ms.id_sistema where us.id_funcionario = ? and me.ativo = true "
                + "union select   si.id as sid, si.descricao as sdesc, '', "
                + "pm.id pmid, pm.descricao as pmdesc, fun.id as funid, fun.descricao as fundesc, "
                + "fun.codigo as funcodigo, fun.ativa as funativa, fun.id_sistema as funidsis, 0 as meid, "
                + "'' as medesc, '', '', '', '' as cod_menu, '', case when '' = '' then 'funcao' end, "
                + "false as meativo, '', ''  from acl.perm_usuario pu "
                + "join acl.funcionarios us on us.id_funcionario = pu.id_usuario "
                + "join acl.permissao pm on pm.id = pu.id_permissao "
                + "join acl.perm_geral pg on pg.id_permissao = pm.id "
                + "join acl.funcao fun on fun.id = pg.id_funcao "
                + "join acl.sistema si on si.id = fun.id_sistema where us.id_funcionario = ? and fun.ativa = true "
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

    public FuncionarioBean autenticarUsuarioacl(FuncionarioBean usuario)
            throws ProjetoException {

        String sql = "select us.id_funcionario, us.descfuncionario, us.cpf, us.email,"
                + "us.senha, us.ativo, us.id_perfil, pf.descricao from acl.funcionarios us "
                + "join acl.perfil pf on pf.id = us.id_perfil where cpf = ? and "
                + "senha = ? and ativo = 'S'";

        FuncionarioBean u = null;

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = null;
            stmt = conexao.prepareStatement(sql);
            stmt.setString(1, usuario.getCpf());
            stmt.setString(2, usuario.getSenha());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                u = new FuncionarioBean();
                u.setId(rs.getLong("id_funcionario"));
                u.setNome(rs.getString("descfuncionario"));
                u.setCpf(rs.getString("cpf"));
                u.setEmail(rs.getString("email"));
                u.setSenha(rs.getString("senha"));
                u.setAtivo(rs.getString("ativo"));
                u.getPerfil().setId(rs.getLong("id_perfil"));
                u.getPerfil().setDescricao(rs.getString("descricao"));
            }
            rs.close();
            stmt.close();
            conexao.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return u;
    }

    public boolean associarUsuarioAoSistemaCad(FuncionarioBean usuario) {

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

    public boolean associarPermAoUsuario(FuncionarioBean usuario) {

        String sql = "insert into acl.perm_usuario (id_usuario, id_permissao, dtacadastro) values (?, ?, CURRENT_TIMESTAMP)";

        boolean associou = false;

        List<Long> listaId = usuario.getListaIdPermissoes();
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

    public Boolean alterar(FuncionarioBean usuario) throws ProjetoException {
        boolean cadastrou = false;
        String sql = "update acl.funcionario set descfuncionario = ?, cpf = ?, email = ?, "
                + "senha = ?, id_perfil = ?, ativo = ? where id_funcionario = ?";
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getCpf().replaceAll("[^0-9]", ""));
            stmt.setString(3, usuario.getEmail());
            stmt.setString(4, usuario.getSenha());
            stmt.setLong(5, usuario.getPerfil().getId());
            stmt.setString(6, usuario.getAtivo());
            stmt.setLong(7, usuario.getCodigo());
            stmt.executeUpdate();

            FuncionarioBean u = usuario;

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

    public Boolean alterarSemPerm(FuncionarioBean usuario)
            throws ProjetoException {
        boolean cadastrou = false;
        String sql = " update acl.funcionarios set descfuncionario = ?, cpf = ?, email = ?, "
                + "senha = ?, id_perfil = ?, ativo = ? where id_funcionario = ? ";

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getCpf().replaceAll("[^0-9]", ""));
            stmt.setString(3, usuario.getEmail());
            stmt.setString(4, usuario.getSenha());
            stmt.setLong(5, usuario.getPerfil().getId());
            stmt.setString(6, usuario.getAtivo());
            stmt.setLong(7, usuario.getCodigo());

            stmt.executeUpdate();
            FuncionarioBean u = usuario;

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

    public ArrayList<FuncionarioBean> buscaUsuarios() throws ProjetoException {

        String sql = "select * from acl.funcionarios u order by ativo,descfuncionario";

        ArrayList<FuncionarioBean> lista = new ArrayList();

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stm = conexao.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                FuncionarioBean n = new FuncionarioBean();
                n = new FuncionarioBean();
                n.setCodigo(rs.getInt("id_funcionario"));
                n.setNome(rs.getString("descfuncionario"));
                n.setSenha(rs.getString("senha"));
                n.setAtivo(rs.getString("ativo"));
                n.setEmail(rs.getString("email"));
                n.setCpf(rs.getString("cpf"));
                n.getPerfil().setId(rs.getLong("id_perfil"));

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
        } finally {
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
                + "join acl.funcionarios us on us.id_funcionario = ps.id_usuario "
                + "where us.id_funcionario = ?) and ativo = 'S' order by descricao";

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
        } finally {
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
                + "join acl.funcionarios us on us.id_funcionario = ps.id_usuario "
                + "where us.id_funcionario = ? and us.ativo = 'S' order by si.descricao";

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
        } finally {
            try {
                conexao.close();
            } catch (Exception e2) {
                // TODO: handle exception
                e2.printStackTrace();
            }

        }
        return listaSistemas;
    }

    public ArrayList<Permissao> listarPermSemPerfSource(Integer id)
            throws ProjetoException {

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
        } finally {
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
                + "union select p.id from acl.funcionarios u "
                + "join acl.perm_usuario pu on u.id_funcionario = pu.id_usuario "
                + "join acl.permissao p on p.id = pu.id_permissao "
                + "where u.id_funcionario = ? order by 1) order by 2";

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
        } finally {
            try {
                conexao.close();
            } catch (Exception e2) {
                // TODO: handle exception
                e2.printStackTrace();
            }

        }
        return lista;
    }

    public ArrayList<Permissao> listarPermSemPerfAssTarget(Integer id)
            throws ProjetoException {

        String sql = "select p.id, p.descricao from acl.funcionarios u "
                + "join acl.perm_usuario pu on u.id_funcionario = pu.id_usuario "
                + "join acl.permissao p on p.id = pu.id_permissao "
                + "where u.id_funcionario = ? order by 2";

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
        } finally {
            try {
                conexao.close();
            } catch (Exception e2) {
                // TODO: handle exception
                e2.printStackTrace();
            }

        }
        return lista;
    }

    public Boolean alterarSenha(FuncionarioBean usuario)
            throws ProjetoException {

        String sql = "update acl.funcionarios set senha = ? where id_funcionario = ?";

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
            String sql = "select * from acl.funcionarios where login=?";
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
            String sql = "select * from acl.funcionarios where cpf=? and ativo=?";

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

    // INÍCIO PROFISSIONALDAO

    public boolean gravarProfissional(FuncionarioBean profissional,
                                      ArrayList<ProgramaBean> lista) throws SQLException,
            ProjetoException {

        FuncionarioBean user_session = (FuncionarioBean) FacesContext
                .getCurrentInstance().getExternalContext().getSessionMap()
                .get("obj_usuario");

        String sql = "INSERT INTO acl.funcionarios(descfuncionario, cpf, senha, log_user, codespecialidade, cns, codcbo, "
                + " codprocedimentopadrao, ativo, realiza_atendimento, datacriacao, primeiroacesso, id_perfil) "
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, current_date, false, ?) returning id_funcionario;";
        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);

            ps.setString(1, profissional.getNome().toUpperCase());

            ps.setString(2, profissional.getCpf());

            ps.setString(3, profissional.getSenha());

            ps.setInt(4, user_session.getCodigo());

            if (profissional.getEspecialidade() != null) {
                if (profissional.getEspecialidade().getCodEspecialidade() != null) {
                    ps.setInt(5, profissional.getEspecialidade().getCodEspecialidade());
                } else {
                    ps.setNull(5, Types.INTEGER);
                }
            } else {
                ps.setNull(5, Types.INTEGER);
            }

            if (profissional.getCns() == null) {
                ps.setNull(6, Types.NULL);
            } else {
                ps.setString(6, profissional.getCns().toUpperCase());
            }

            if (profissional.getCbo() != null) {
                if (profissional.getCbo().getCodCbo() == null) {
                    ps.setNull(7, Types.NULL);
                } else {
                    ps.setInt(7, profissional.getCbo().getCodCbo());
                }
            } else {
                ps.setNull(7, Types.NULL);
            }

            if (profissional.getProc1() != null) {
                if (profissional.getProc1().getIdProc() == null) {
                    ps.setNull(8, Types.NULL);
                } else {
                    ps.setInt(8, profissional.getProc1().getIdProc());
                }
            } else {
                ps.setNull(8, Types.NULL);
            }

            ps.setString(9, profissional.getAtivo());

            ps.setBoolean(10, profissional.getRealizaAtendimento());

            ps.setLong(11, profissional.getPerfil().getId());

            ResultSet rs = ps.executeQuery();

            int idProf = 0;
            if (rs.next()) {
                idProf = rs.getInt("id_funcionario");
            }

            for (int i = 0; i < lista.size(); i++) {
                String sql2 = "INSERT INTO hosp.profissional_programa_grupo(codprofissional, codprograma, codgrupo) VALUES (?, ?, ?);";
                ps = con.prepareStatement(sql2);

                ps.setInt(1, idProf);
                ps.setInt(2, lista.get(i).getIdPrograma());
                ps.setInt(3, lista.get(i).getGrupoBean().getIdGrupo());

                ps.executeUpdate();
            }

            String sql3 = "insert into acl.perm_usuario (id_usuario, id_permissao) values (?, ?)";
            List<Long> listaPerm = profissional.getListaIdPermissoes();
            ps = con.prepareStatement(sql3);
            for (Long idPerm : listaPerm) {
                ps.setLong(1, idProf);
                ps.setLong(2, idPerm);
                ps.execute();
            }

            con.commit();

            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.exit(1);
            }
        }

    }

    // RETIREI, SEM USO
    public void insereTabProfProg(FuncionarioBean prof, int idProf, int gamb)
            throws SQLException, ProjetoException {
        String sql = "insert into hosp.profissional_programa (codprograma, codprofissional)"
                + " values (?, ?);";
        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);

            if (gamb == 0) {
                for (ProgramaBean p : prof.getPrograma()) {
                    ps.setInt(1, p.getIdPrograma());
                    ps.setInt(2, idProf);

                    ps.execute();
                    con.commit();
                }
            } else if (gamb == 1) {
                for (ProgramaBean p : prof.getProgramaNovo()) {
                    ps.setInt(1, p.getIdPrograma());
                    ps.setInt(2, idProf);

                    ps.execute();
                    con.commit();
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                // con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.exit(1);
            }
        }
    }

    // RETIREI, SEM USO
    public void insereTabProfGrupo(FuncionarioBean prof, int idProf, int gamb)
            throws SQLException, ProjetoException {
        String sql = "insert into hosp.profissional_grupo (codgrupo, codprofissional)"
                + " values (?, ?);";
        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);

            if (gamb == 0) {
                for (GrupoBean g : prof.getGrupo()) {
                    ps.setInt(1, g.getIdGrupo());
                    ps.setInt(2, idProf);

                    ps.execute();
                    con.commit();
                }
            } else if (gamb == 1) {
                for (GrupoBean g : prof.getGrupoNovo()) {
                    ps.setInt(1, g.getIdGrupo());
                    ps.setInt(2, idProf);

                    ps.execute();
                    con.commit();
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                // con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.exit(1);
            }
        }
    }

    public List<FuncionarioBean> listarProfissionalBusca(String descricaoBusca,
                                                         Integer tipoBuscar) throws ProjetoException {
        List<FuncionarioBean> lista = new ArrayList<>();
        String sql = "select id_funcionario ,id_funcionario ||'-'|| descfuncionario as descfuncionario, codespecialidade, cns, ativo, codcbo, codprocedimentopadrao "
                + " from acl.funcionarios ";
        if (tipoBuscar == 1) {
            sql += " where upper(id_funcionario ||' - '|| descfuncionario) LIKE ? and realiza_atendimento is true order by descfuncionario";
        }
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setString(1, "%" + descricaoBusca.toUpperCase() + "%");
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                FuncionarioBean prof = new FuncionarioBean();
                prof.setId(rs.getLong("id_funcionario"));
                prof.setNome(rs.getString("descfuncionario"));
                prof.setEspecialidade(espDao.listarEspecialidadePorId(rs
                        .getInt("codespecialidade")));
                prof.setCns(rs.getString("cns"));
                prof.setAtivo(rs.getString("ativo"));
                prof.setCbo(cDao.listarCboPorId(rs.getInt("codcbo")));
                prof.setProc1(procDao.listarProcedimentoPorId(rs
                        .getInt("codprocedimentopadrao")));
                prof.setPrograma(listarProgProf(rs.getInt("id_funcionario")));
                prof.setGrupo(listarProgGrupo(rs.getInt("id_funcionario")));

                lista.add(prof);
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

        return lista;
    }

    public List<FuncionarioBean> listarProfissionalBuscaPorGrupo(
            String descricaoBusca, Integer codgrupo) throws ProjetoException {
        List<FuncionarioBean> lista = new ArrayList<>();
        String sql = "select h.id_funcionario, h.id_funcionario ||'-'|| h.descfuncionario as descfuncionario, h.codespecialidade, "
                + " h.cns, h.ativo, h.codcbo, h.codprocedimentopadrao "
                + " from acl.funcionarios h left join hosp.profissional_grupo g on (g.codprofissional = h.id_funcionario)"
                + " where upper(id_funcionario ||' - '|| descfuncionario) LIKE ? and g.codgrupo = ? and h.ativo = 'S' "
                + " and realiza_atendimento is true order by descfuncionario";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);

            stm.setString(1, "%" + descricaoBusca.toUpperCase() + "%");
            stm.setInt(2, codgrupo);

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                FuncionarioBean prof = new FuncionarioBean();
                prof.setId(rs.getLong("id_funcionario"));
                prof.setNome(rs.getString("descfuncionario"));
                prof.setEspecialidade(espDao.listarEspecialidadePorId(rs
                        .getInt("codespecialidade")));
                prof.setCns(rs.getString("cns"));
                prof.setAtivo(rs.getString("ativo"));
                prof.setCbo(cDao.listarCboPorId(rs.getInt("codcbo")));
                prof.setProc1(procDao.listarProcedimentoPorId(rs
                        .getInt("codprocedimentopadrao")));
                prof.setPrograma(listarProgProf(rs.getInt("id_funcionario")));
                prof.setGrupo(listarProgGrupo(rs.getInt("id_funcionario")));

                lista.add(prof);
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

        return lista;
    }

    public List<FuncionarioBean> listarProfissional() throws ProjetoException {
        List<FuncionarioBean> listaProf = new ArrayList<FuncionarioBean>();
        String sql = "select distinct id_funcionario, descfuncionario, codespecialidade, cns, ativo, codcbo, " +
                " codprocedimentopadrao, cpf, senha, realiza_atendimento, id_perfil "
                + " from acl.funcionarios order by descfuncionario";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                FuncionarioBean prof = new FuncionarioBean();
                prof.setId(rs.getLong("id_funcionario"));
                prof.setCpf(rs.getString("cpf"));
                prof.setSenha(rs.getString("senha"));
                prof.setRealizaAtendimento(rs.getBoolean("realiza_atendimento"));
                prof.setNome(rs.getString("descfuncionario"));
                prof.setEspecialidade(espDao.listarEspecialidadePorId(rs
                        .getInt("codespecialidade")));
                prof.setCns(rs.getString("cns"));
                prof.setAtivo(rs.getString("ativo"));
                prof.setCbo(cDao.listarCboPorId(rs.getInt("codcbo")));
                prof.setProc1(procDao.listarProcedimentoPorId(rs
                        .getInt("codprocedimentopadrao")));
                prof.getPerfil().setId(rs.getLong("id_perfil"));

                listaProf.add(prof);
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

        return listaProf;
    }

    public List<FuncionarioBean> listarProfissionalPorGrupo(Integer codgrupo)
            throws ProjetoException {
        List<FuncionarioBean> listaProf = new ArrayList<FuncionarioBean>();
        String sql = "select m.id_funcionario, m.descfuncionario, m.codespecialidade, e.descespecialidade, m.cns from acl.funcionarios m"
                + " left join hosp.profissional_grupo p on (m.id_funcionario = p.codprofissional)"
                + " left join hosp.especialidade e on (e.id_especialidade = m.codespecialidade)"
                + " where m.ativo = 'S' and realiza_atendimento is true and p.codgrupo = ?";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, codgrupo);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                FuncionarioBean prof = new FuncionarioBean();
                prof.setId(rs.getLong("id_funcionario"));
                prof.setNome(rs.getString("descfuncionario"));
                prof.getEspecialidade().setCodEspecialidade(
                        rs.getInt("codespecialidade"));
                prof.getEspecialidade().setDescEspecialidade(
                        rs.getString("descespecialidade"));
                prof.setCns(rs.getString("cns"));

                listaProf.add(prof);
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

        return listaProf;
    }

    public boolean excluirProfissional(FuncionarioBean profissional)
            throws ProjetoException {
        String sql = "delete from acl.funcionarios where id_funcionario = ?";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setLong(1, profissional.getId());
            stmt.execute();
            con.commit();
            excluirTabProfProg(profissional);
            excluirTabProfGrupo(profissional);
            return true;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    // RETIREI, SEM USO
    public void excluirTabProfProg(FuncionarioBean profissional)
            throws ProjetoException {
        String sql = "delete from hosp.profissional_programa where codprofissional = ?";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setLong(1, profissional.getId());
            stmt.execute();
            con.commit();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                // con.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    // RETIREI, SEM USO
    public void excluirTabProfGrupo(FuncionarioBean profissional)
            throws ProjetoException {
        String sql = "delete from hosp.profissional_grupo where codprofissional = ?";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setLong(1, profissional.getId());
            stmt.execute();
            con.commit();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                // con.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public boolean alterarProfissional(FuncionarioBean profissional,
                                       ArrayList<ProgramaBean> lista) throws ProjetoException {

        String sql = "update acl.funcionarios set descfuncionario = ?, codespecialidade = ?, cns = ?, ativo = ?,"
                + " codcbo = ?, codprocedimentopadrao = ?, id_perfil = ? "
                + " where id_funcionario = ?";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setString(1, profissional.getNome().toUpperCase());

            if (profissional.getEspecialidade() != null) {
                if (profissional.getEspecialidade().getCodEspecialidade() != null) {
                    stmt.setInt(2, profissional.getEspecialidade()
                            .getCodEspecialidade());
                } else {
                    stmt.setInt(2, 0);
                }
            } else {
                stmt.setInt(2, 0);
            }

            if (profissional.getCns() != null) {
                stmt.setString(3, profissional.getCns().toUpperCase());
            } else {
                stmt.setString(3, "");
            }

            stmt.setString(4, profissional.getAtivo());

            if (profissional.getCbo() != null) {
                if (profissional.getCbo().getCodCbo() != null) {
                    stmt.setInt(5, profissional.getCbo().getCodCbo());
                } else {
                    stmt.setInt(5, 0);
                }
            } else {
                stmt.setInt(5, 0);
            }

            if (profissional.getProc1() != null) {
                if (profissional.getProc1().getCodProc() != null) {
                    stmt.setInt(6, profissional.getProc1().getIdProc());
                } else {
                    stmt.setInt(6, 0);
                }
            } else {
                stmt.setInt(6, 0);
            }

            stmt.setLong(7,  profissional.getPerfil().getId());

            stmt.setLong(8, profissional.getId());

            stmt.executeUpdate();

            String sql2 = "delete from hosp.profissional_programa_grupo where codprofissional = ?";
            stmt = con.prepareStatement(sql2);
            stmt.setLong(1, profissional.getId());
            stmt.executeUpdate();

            for (int i = 0; i < lista.size(); i++) {
                String sql3 = "INSERT INTO hosp.profissional_programa_grupo(codprofissional, codprograma, codgrupo) VALUES (?, ?, ?);";
                stmt = con.prepareStatement(sql3);

                stmt.setLong(1, profissional.getId());
                stmt.setInt(2, lista.get(i).getIdPrograma());
                stmt.setInt(3, lista.get(i).getGrupoBean().getIdGrupo());

                stmt.executeUpdate();
            }


            String sql3 = "delete from acl.perm_usuario where id_usuario = ?";
            ps = con.prepareStatement(sql3);
            ps.setLong(1, profissional.getId());
            ps.execute();

            String sql4 = "insert into acl.perm_usuario (id_usuario, id_permissao) values (?, ?)";
            List<Long> listaPerm = profissional.getListaIdPermissoes();
            ps = con.prepareStatement(sql4);
            for (Long idPerm : listaPerm) {
                ps.setLong(1, profissional.getId());
                ps.setLong(2, idPerm);
                ps.execute();
            }

            con.commit();

            return true;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public FuncionarioBean buscarProfissionalPorId(Integer id)
            throws SQLException, ProjetoException {
        FuncionarioBean prof = null;

        String sql = "select id_funcionario, descfuncionario, codespecialidade, cns, ativo, codcbo, codprocedimentopadrao,"
                + " cpf, senha, realiza_atendimento, id_perfil "
                + " from acl.funcionarios where id_funcionario = ? and ativo = 'S' order by descfuncionario";

        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                prof = new FuncionarioBean();
                prof.setId(rs.getLong("id_funcionario"));
                prof.setNome(rs.getString("descfuncionario"));
                prof.setCpf(rs.getString("cpf"));
                prof.setSenha(rs.getString("senha"));
                prof.setRealizaAtendimento(rs.getBoolean("realiza_atendimento"));
                prof.setPrograma(listarProgProf(rs.getInt("id_funcionario")));
                prof.setGrupo(listarProgGrupo(rs.getInt("id_funcionario")));
                prof.setEspecialidade(espDao.listarEspecialidadePorId(rs
                        .getInt("codespecialidade")));
                prof.setCns(rs.getString("cns"));
                prof.setAtivo(rs.getString("ativo"));
                prof.setCbo(cDao.listarCboPorId(rs.getInt("codcbo")));
                prof.setProc1(procDao.listarProcedimentoPorId(rs
                        .getInt("codprocedimentopadrao")));
                prof.getPerfil().setId(rs.getLong("id_perfil"));

            }
            return prof;

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

    }

    public FuncionarioBean buscarProfissionalRealizaAtendimentoPorId(Integer id)
            throws SQLException, ProjetoException {
        FuncionarioBean prof = null;

        String sql = "select id_funcionario, descfuncionario, codespecialidade, cns, ativo, codcbo, codprocedimentopadrao,"
                + " cpf, senha, realiza_atendimento "
                + " from acl.funcionarios where id_funcionario = ? and ativo = 'S' and realiza_atendimento is true order by descfuncionario";

        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                prof = new FuncionarioBean();
                prof.setId(rs.getLong("id_funcionario"));
                prof.setNome(rs.getString("descfuncionario"));
                prof.setCpf(rs.getString("cpf"));
                prof.setSenha(rs.getString("senha"));
                prof.setRealizaAtendimento(rs.getBoolean("realiza_atendimento"));
                prof.setPrograma(listarProgProf(rs.getInt("id_funcionario")));
                prof.setGrupo(listarProgGrupo(rs.getInt("id_funcionario")));
                prof.setEspecialidade(espDao.listarEspecialidadePorId(rs
                        .getInt("codespecialidade")));
                prof.setCns(rs.getString("cns"));
                prof.setAtivo(rs.getString("ativo"));
                prof.setCbo(cDao.listarCboPorId(rs.getInt("codcbo")));
                prof.setProc1(procDao.listarProcedimentoPorId(rs
                        .getInt("codprocedimentopadrao")));

            }

            return prof;
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

    }

    public List<FuncionarioBean> listarProfissionaisPorEquipe(int id)
            throws ProjetoException {

        List<FuncionarioBean> lista = new ArrayList<FuncionarioBean>();
        String sql = "select medico from hosp.equipe_medico where equipe = ? order by medico";
        FuncionarioDAO pDao = new FuncionarioDAO();
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                lista.add(pDao.buscarProfissionalPorId(rs.getInt("medico")));
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

        return lista;
    }

    public List<ProgramaBean> listarProgProf(int idProf)
            throws ProjetoException {
        List<ProgramaBean> lista = new ArrayList<ProgramaBean>();
        String sql = "select codprograma from hosp.profissional_programa where codprofissional = ?";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, idProf);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                lista.add(progDao.listarProgramaPorId(rs.getInt("codprograma")));
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

        return lista;
    }

    public List<GrupoBean> listarProgGrupo(int idProf) throws ProjetoException {
        List<GrupoBean> lista = new ArrayList<GrupoBean>();
        String sql = "select codgrupo from hosp.profissional_grupo where codprofissional = ?";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, idProf);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                lista.add(gDao.listarGrupoPorId(rs.getInt("codgrupo")));
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

        return lista;
    }

    public List<FuncionarioBean> listarProfissionalPorGrupo(int idGrupo)
            throws ProjetoException {
        List<FuncionarioBean> lista = new ArrayList<FuncionarioBean>();
        String sql = "select codprofissional from hosp.profissional_grupo where codgrupo = ?";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, idGrupo);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                lista.add(buscarProfissionalPorId(rs.getInt("codprofissional")));
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

        return lista;
    }

    public ArrayList<ProgramaBean> carregaProfissionalProgramaEGrupos(int idProf)
            throws ProjetoException {
        ArrayList<ProgramaBean> lista = new ArrayList<ProgramaBean>();

        String sql = "select ppg.codprograma, p.descprograma, ppg.codgrupo, g.descgrupo "
                + "from hosp.profissional_programa_grupo ppg "
                + "left join hosp.programa p on (p.id_programa = ppg.codprograma) "
                + "left join hosp.grupo g on (g.id_grupo = ppg.codgrupo)"
                + "where codprofissional = ?";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, idProf);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                ProgramaBean p = new ProgramaBean();

                p.setIdPrograma(rs.getInt("codprograma"));
                p.setDescPrograma(rs.getString("descprograma"));
                p.getGrupoBean().setIdGrupo(rs.getInt("codgrupo"));
                p.getGrupoBean().setDescGrupo(rs.getString("descgrupo"));

                lista.add(p);
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

        return lista;
    }

    // FINAL PROFISSIONALDAO

}
