package br.gov.al.maceio.sishosp.acl.dao;

import br.gov.al.maceio.sishosp.acl.model.Menu;
import br.gov.al.maceio.sishosp.acl.model.Permissao;
import br.gov.al.maceio.sishosp.acl.model.Permissoes;
import br.gov.al.maceio.sishosp.acl.model.Sistema;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.acl.model.UsuarioBean;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

public class UsuarioDAO implements IUsuarioDAO {

    private Connection conexao = null;

    public ArrayList<UsuarioBean> listUsuario() throws ProjetoException {
        Connection conn = ConnectionFactory.getConnection();

        String sql = "select * from prot2.usuarios where ativo=? and id_usuario<>53";

        ResultSet rs = null;

        ArrayList<UsuarioBean> list = new ArrayList<>();

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "S");
            rs = ps.executeQuery();

            while (rs.next()) {
                UsuarioBean user = new UsuarioBean();

                user.setCodigo(rs.getInt(1));
                user.setCpf(rs.getString(3));
                user.setSenha(rs.getString(7));

                list.add(user);

            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (Exception e2) {
                // TODO: handle exception
                e2.printStackTrace();
            }

        }

        return list;
    }

    public void updateSenha(ArrayList<UsuarioBean> list)
            throws ProjetoException {
        Connection conn = ConnectionFactory.getConnection();

        String sql = "update prot2.usuarios set senha=? where id_usuario=?";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            for (int i = 0; i < list.size(); i++) {
                ps.setString(1, list.get(i).getSenha());
                ps.setInt(2, list.get(i).getCodigo());
                ps.executeUpdate();
            }

            conn.commit();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            try {
                conn.close();
            } catch (Exception e2) {
                // TODO: handle exception
                e2.printStackTrace();
            }
        }

    }

    @Override
    public UsuarioBean autenticarUsuario(UsuarioBean usuario) throws ProjetoException {

        Connection con = null;

        String sql = "select us.id_usuario, us.descusuario, us.login, us.senha, us.email, "
        		+ "pf.descricao as descperfil, case when us.ativo = 'S' "
        		+ "then true else false end as usuarioativo, "
        		+ "pf.id as idperfil, us.junta_medica from acl.usuarios us "
        		+ "join acl.perfil pf on (pf.id = us.id_perfil) "
        		+ "where (us.login = ?) and (upper(us.senha) = ?) and (us.ativo = ?)";

        UsuarioBean ub = null;
        int count = 1;
        String setoresUsuario = "";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, usuario.getLogin().toUpperCase());
            pstmt.setString(2, usuario.getSenha().toUpperCase());
            pstmt.setString(3, "S");

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                ub = new UsuarioBean();
                ub.setCodigo(rs.getInt("id_usuario"));
                ub.setNome(rs.getString("descusuario"));
                ub.setLogin(rs.getString("login"));
                ub.setSenha(rs.getString("senha"));
                ub.setEmail(rs.getString("email"));
  
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
            throw new ProjetoException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                // TODO: handle exception
                ex.printStackTrace();
            }
        }
    }

    public void desativarUser() throws ProjetoException {
        PreparedStatement ps = null;
        Connection con = ConnectionFactory.getConnection();
        try {
            String sql = "update prot2.usuarios set ativo=?"
                    + "where id_usuario=?";
            UsuarioBean coduser = (UsuarioBean) FacesContext
                    .getCurrentInstance().getExternalContext().getSessionMap()
                    .get("obj_cod_user");

            ps = con.prepareStatement(sql);
            ps.setString(1, "N");
            ps.setInt(2, coduser.getCodigo());
            ps.executeUpdate();
            con.commit();
            ps.close();

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
    }

    public void ativarUser() throws ProjetoException {
        PreparedStatement ps = null;
        Connection con = ConnectionFactory.getConnection();
        try {
            String sql = "update prot2.usuarios set ativo=?"
                    + "where id_usuario=?";
            UsuarioBean coduser = (UsuarioBean) FacesContext
                    .getCurrentInstance().getExternalContext().getSessionMap()
                    .get("obj_cod_user");

            ps = con.prepareStatement(sql);
            ps.setString(1, "S");
            ps.setInt(2, coduser.getCodigo());
            ps.executeUpdate();
            con.commit();
            ps.close();

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

    }

    public boolean updateEditSenha(UsuarioBean U) throws ProjetoException {

        PreparedStatement ps = null;
        Connection con = ConnectionFactory.getConnection();

        try {

            String sql = "update prot2.usuarios set senha=?"
                    + "where id_usuario=?";

            ps = con.prepareStatement(sql);
            ps.setString(1, U.getSenha().toUpperCase());
            ps.setInt(2, U.getCodigo());
            ps.executeUpdate();
            con.commit();

            return true;
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
    }

    public boolean existeUsuarioAtivo(String cpf) throws ProjetoException {
        PreparedStatement ps = null;
        Connection con = ConnectionFactory.getConnection();
        boolean resultado = true;
        try {

            String sql = "select cpf from prot2.usuarios where cpf=? and ativo='S'";
            ps = con.prepareStatement(sql);
            ps.setString(1, cpf);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                resultado = false;
            }

            return resultado;
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

    }

    public boolean updateEditUsuario(UsuarioBean U) throws ProjetoException {

        PreparedStatement ps = null;
        Connection con = ConnectionFactory.getConnection();
        boolean retorno = false;
        try {
            String sql = "update prot2.usuarios set descusuario=?,email=?,administrador=?, ativo=? "
                    + " where id_usuario=?";
            ps = con.prepareStatement(sql);
            ps.setString(1, U.getNome().toUpperCase());
            ps.setString(2, U.getEmail().toUpperCase());
            ps.setBoolean(3, U.isAdministrador());
            ps.setString(4, U.getAtivo());
            ps.setInt(5, U.getCodigo());
            ps.executeUpdate();
            con.commit();
            ps.close();
            retorno = true;
            return retorno;

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

    }

    @Override
    public int newUser(UsuarioBean b) throws ProjetoException {
        Integer id = null;
        Integer result = 0;
        Connection con = ConnectionFactory.getConnection();
        UsuarioBean user_session = (UsuarioBean) FacesContext
                .getCurrentInstance().getExternalContext().getSessionMap()
                .get("obj_usuario");

        try {

            CallableStatement cs = con
                    .prepareCall("{ ? = call prot2.gravausuario(?,?,?,?,?,?,?,?,?,?,?,?) }");

            cs.registerOutParameter(1, Types.INTEGER);
            cs.setString(2, b.getNome().toUpperCase().trim());
            cs.setString(3, b.getCpf().replace(".", "").replace("-", "").trim());
            cs.setInt(4, b.getCodSecre());
            cs.setString(5, b.getSenha().toUpperCase().trim());
            cs.setString(6, "S");
            cs.setString(7, b.getLogin().toUpperCase().trim());
            cs.setBoolean(8, b.isAdministrador());
            cs.setString(9, b.getEmail().toLowerCase().trim());
            cs.setInt(10, user_session.getCodigo());
            cs.setBoolean(11, true);
            cs.setBoolean(12, b.getFavorito());
            cs.setInt(13, b.getIdpessoa());

            cs.executeUpdate();
            con.commit();

            id = cs.getInt(1);

            if (id != null) {
                result = 1;
            }

            permUsusario(b, id);

			
            return result;

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
    }

    public Boolean cadastrar(UsuarioBean usuario) throws ProjetoException {
        boolean cadastrou = false;

        UsuarioBean user_session = (UsuarioBean) FacesContext
                .getCurrentInstance().getExternalContext().getSessionMap()
                .get("obj_usuario");

        try {
            conexao = ConnectionFactory.getConnection();
            CallableStatement cs = conexao.prepareCall("{ ? = call prot2.gravausuario(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setString(2, usuario.getNome().toUpperCase().trim());
            cs.setString(3, usuario.getCpf().replaceAll("[^0-9]", ""));
            cs.setInt(4, usuario.getCodSecre());
            cs.setString(5, usuario.getSenha().toUpperCase().trim());
            cs.setString(6, "S");
            cs.setString(7, usuario.getLogin().toUpperCase().trim());
            cs.setString(8, usuario.getEmail().toLowerCase().trim());
            cs.setInt(9, user_session.getCodigo());
            cs.setBoolean(10, true);
            cs.setInt(11, usuario.getIdPerfil());
            cs.setInt(12, usuario.getIdpessoa());
            cs.setBoolean(13, usuario.isPermissaoJunta());
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
            conexao.commit();
            conexao.close();

            return cadastrou;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public List buscaUsuariosSecretaria() throws ProjetoException {

        PreparedStatement ps = null;
        Connection con = ConnectionFactory.getConnection();
        UsuarioBean user_session = (UsuarioBean) FacesContext
                .getCurrentInstance().getExternalContext().getSessionMap()
                .get("obj_usuario");
        try {
            String sql = "select * from prot2.usuarios u left join prot2.permissoes p on(u.id_usuario=p.idusuario)  "
                    + "where u.codsecretaria=? order by descusuario";

            ps = con.prepareStatement(sql);
            ps.setInt(1, user_session.getUser_secretaria());
            ResultSet rs = ps.executeQuery();
            UsuarioBean n = new UsuarioBean();
            List colecao = new ArrayList<>();

            while (rs.next()) {
                n = new UsuarioBean();
                n.setCodigo(rs.getInt("id_usuario"));
                n.setNome(rs.getString("descusuario"));
                n.setCodSecre(rs.getInt("codsecretaria"));
                n.setSenha(rs.getString("senha"));
                n.setAtivo(rs.getString("ativo"));
                n.setLogin(rs.getString("login"));
                n.setAdministrador((rs.getBoolean("administrador")));
                n.setEmail(rs.getString("email"));
                n.setCpf(rs.getString("cpf"));
                n.getPermissao().setProtocolar(rs.getBoolean("protocolar"));
                n.getPermissao().setTramitar(rs.getBoolean("tramitar"));
                n.getPermissao().setAlterarProtocolo(rs.getBoolean("manutencao_proc"));
                n.getPermissao().setIncluirParecer(rs.getBoolean("inclui_parecer"));
                n.getPermissao().setEncaminharProtocolo(rs.getBoolean("encaminha_protocolo"));
                n.getPermissao().setDocDigital(rs.getBoolean("inclui_docto_digital"));
                n.getPermissao().setAlterarNatureza(rs.getBoolean("manutencao_nat_proc"));
                colecao.add(n);
            }

            return colecao;

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
    }

    public UsuarioBean consultaUsuario() throws ProjetoException {

        PreparedStatement ps = null;
        Connection con = ConnectionFactory.getConnection();

        try {
            String sql = "select usuarios.codusuario, usuarios.codsetor, usuarios.ativo"
                    + "from prot2.usuarios where (usuarios.codusuario =?)";
            Object cod_user = FacesContext.getCurrentInstance()
                    .getExternalContext().getSessionMap()
                    .get("obj_codusuario_combo");

            String Str_cod_user = (String) cod_user;
            int Int_cod_user = Integer.parseInt(Str_cod_user);
            ps = con.prepareStatement(sql);
            ps.setInt(1, Int_cod_user);
            ResultSet rs = ps.executeQuery();

            UsuarioBean b = null;

            while (rs.next()) {

                b = new UsuarioBean();
                b.setUser_setor(rs.getInt(2));
                b.setCodigo(rs.getInt(1));
                b.setAtivo(rs.getString(3));
            }
            return b;
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
            // fecharconexao();

        }
    }

    public String verificaUserCadastrado(String cpf) throws ProjetoException {
        PreparedStatement ps = null;
        Connection con = ConnectionFactory.getConnection();
        String isExist = "N";

        try {
            String sql = "select * from prot2.usuarios where cpf=? and ativo=?";

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

    public String verificaLoginCadastrado(String login) throws ProjetoException {
        PreparedStatement ps = null;
        Connection con = ConnectionFactory.getConnection();
        String isExist = "N";

        try {
            String sql = "select * from prot2.usuarios where login=?";
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

    public String alteraSenha(UsuarioBean u) throws ProjetoException {
        PreparedStatement ps = null;
        Connection con = ConnectionFactory.getConnection();
        try {
            String sql = "update prot2.usuarios set senha=?,primeiroacesso=?"
                    + "where id_usuario=?";
            UsuarioBean user_session = (UsuarioBean) FacesContext
                    .getCurrentInstance().getExternalContext().getSessionMap()
                    .get("obj_usuario");
            ps = con.prepareStatement(sql);
            ps.setString(1, u.getSenha().toUpperCase());
            ps.setBoolean(2, false);
            ps.setInt(3, user_session.getCodigo());
            ps.executeUpdate();
            con.commit();
            return "success";
        } catch (Exception sqle) {

            throw new ProjetoException(sqle);

        } finally {
            try {
                con.close();
            } catch (Exception sqlc) {
                sqlc.printStackTrace();
                System.exit(1);
            }
        }
    }

    public Integer verificaUltimoAdm(UsuarioBean U) throws ProjetoException {

        PreparedStatement ps = null;
        Connection con = ConnectionFactory.getConnection();
        Integer Count = 0;

        try {
            String sql = "select * from prot2.usuarios where codsecretaria=? and administrador=?";
            UsuarioBean user_session = (UsuarioBean) FacesContext
                    .getCurrentInstance().getExternalContext().getSessionMap()
                    .get("obj_usuario");
            ps = con.prepareStatement(sql);
            ps.setInt(1, user_session.getUser_secretaria());
            ps.setBoolean(2, true);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Count = Count + 1;
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
        return Count;
    }

    public List<SelectItem> consultaAnoAberturaProc() throws ProjetoException {
        PreparedStatement ps = null;
        Connection con = ConnectionFactory.getConnection();
        try {
            String sql = "select distinct (date_part('year',datacriacao)) as ano "
                    + "from prot2.processos where codsecreorigem=?";

            UsuarioBean user_session = (UsuarioBean) FacesContext
                    .getCurrentInstance().getExternalContext().getSessionMap()
                    .get("obj_usuario");

            ps = con.prepareStatement(sql);
            ps.setInt(1, user_session.getUser_secretaria());
            ResultSet rs = ps.executeQuery();
            List<SelectItem> collecAnoProcessos = new ArrayList<SelectItem>();
            while (rs.next()) {
                collecAnoProcessos.add(new SelectItem(rs.getInt(1), String.valueOf(rs.getInt(1))));
            }
            return collecAnoProcessos;
        } catch (Exception sqle) {
            throw new ProjetoException(sqle);
        } finally {
            try {
                con.close();
            } catch (Exception sqlc) {
                sqlc.printStackTrace();
                System.exit(1);
            }
        }
    }

    @Override
    public void permUsusario(UsuarioBean user, Integer id)
            throws ProjetoException {
        Connection conn = ConnectionFactory.getConnection();
        String sql = "INSERT INTO prot2.permissoes(idusuario, protocolar, tramitar, "
                + "manutencao_proc, inclui_parecer, encaminha_protocolo, inclui_docto_digital, manutencao_nat_proc) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setBoolean(2, user.getPermissao().getProtocolar());
            ps.setBoolean(3, user.getPermissao().getTramitar());
            ps.setBoolean(4, user.getPermissao().getAlterarProtocolo());
            ps.setBoolean(5, user.getPermissao().getIncluirParecer());
            ps.setBoolean(6, false);
            ps.setBoolean(7, false);
            ps.setBoolean(8, user.getPermissao().getAlterarNatureza());
            ps.executeUpdate();
            conn.commit();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (Exception e2) {
                // TODO: handle exception
                e2.printStackTrace();
            }
        }
    }

    @Override
    public int updateUsuario(UsuarioBean user) throws ProjetoException {
        Connection conn = ConnectionFactory.getConnection();
        Integer result = 0;
        String sql = "update prot2.usuarios set descusuario=?,cpf=?,senha=?,ativo=?,login=?,email=?, primeiroacesso = ? where usuarios.id_usuario=? ;";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, user.getNome().toUpperCase());
            ps.setString(2, user.getCpf().replace(".", "").replace("-", "").trim());
            ps.setString(3, user.getSenha().toUpperCase());
            ps.setString(4, user.getAtivo());
            ps.setString(5, user.getLogin().toUpperCase());
            ps.setString(6, user.getEmail().toUpperCase());
            ps.setBoolean(7, user.isPrimeiroAcesso());
            ps.setInt(8, user.getCodigo());

            result = ps.executeUpdate();
            conn.commit();
            updatePerm(user);
            Integer i = 0;
           
         
            if (i > 0) {
                result += 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    public Boolean alterar(UsuarioBean usuario) throws ProjetoException {
        boolean cadastrou = false;
        String sql = "update prot2.usuarios set descusuario = ?, cpf = ?, codsecretaria = ?, email = ?, "
                + "login = ?, senha = ?, id_perfil = ?, ativo = ?, junta_medica = ? where id_usuario = ?";
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getCpf().replaceAll("[^0-9]", ""));
            stmt.setInt(3, usuario.getCodSecre());
            stmt.setString(4, usuario.getEmail());
            stmt.setString(5, usuario.getLogin());
            stmt.setString(6, usuario.getSenha());
            stmt.setInt(7, usuario.getIdPerfil());
            stmt.setString(8, usuario.getAtivo());
            stmt.setBoolean(9, usuario.isPermissaoJunta());
            stmt.setLong(10, usuario.getCodigo());
            stmt.executeUpdate();

            UsuarioBean u = usuario;

            sql = "delete from acl.perm_sistema where id_usuario = ?";
            stmt = conexao.prepareStatement(sql);
            stmt.setLong(1, usuario.getCodigo());
            stmt.execute();
            
            sql = "insert into acl.perm_sistema (id_usuario, id_sistema) values (?, ?)";
            List<Integer> listaId = usuario.getListaIdSistemas();
            stmt = conexao.prepareStatement(sql);
            for(Integer idSistema : listaId) {
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
            for(Long idPerm : listaPerm) {
                stmt.setLong(1, usuario.getCodigo());
                stmt.setLong(2, idPerm);
                stmt.execute();
            }

            // Integer associouSet = 1;
            Integer associouSet = 0;
            sql = "delete from prot2.setores_usuarios where codusuario=?";
            PreparedStatement ps;
            try {
                ps = conexao.prepareStatement(sql);
                ps.setInt(1, u.getCodigo());
                ps.execute();

            } catch (Exception e) {
                e.printStackTrace();
            }
            ps = null;
            if (!u.getCodSetor().isEmpty()) {
                try {
                    String sql2 = "insert into prot2.setores_usuarios (codsetor, codusuario, ativo) "
                            + "values (?,?,?)";
                    ps = conexao.prepareStatement(sql2);
                    for (int i = 0; i < u.getCodSetor().size(); i++) {
                        ps.setInt(1, u.getCodSetor().get(i));
                        ps.setInt(2, u.getCodigo());
                        ps.setBoolean(3, false);
                        associouSet = ps.executeUpdate();
                    }
                } catch (Exception sqle) {

                    throw new ProjetoException(sqle);
                }
            }
            
         
            
            conexao.commit();
            
            cadastrou = true;
            
            return cadastrou;
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

    public Boolean alterarSemPerm(UsuarioBean usuario) throws ProjetoException {
        boolean cadastrou = false;
        String sql = " update prot2.usuarios set descusuario = ?, cpf = ?, codsecretaria = ?, email = ?, "
                + " login = ?, senha = ?, id_perfil = ?, ativo = ?, junta_medica = ? where id_usuario = ? ";

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getCpf().replaceAll("[^0-9]", ""));
            stmt.setInt(3, usuario.getCodSecre());
            stmt.setString(4, usuario.getEmail());
            stmt.setString(5, usuario.getLogin());
            stmt.setString(6, usuario.getSenha());
            stmt.setInt(7, usuario.getIdPerfil());
            stmt.setString(8, usuario.getAtivo());
            stmt.setBoolean(9, usuario.isPermissaoJunta());
            stmt.setLong(10, usuario.getCodigo());
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

            Integer associouSet = 0;
            if (u.getCodigo() != null && associouSis != false
                    && associouPerm != false) {
                sql = "delete from prot2.setores_usuarios where codusuario=?";
                PreparedStatement ps;
                try {
                    ps = conexao.prepareStatement(sql);
                    ps.setInt(1, u.getCodigo());
                    ps.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ps = null;
                if (!u.getCodSetor().isEmpty()) {
                    try {
                        String sql2 = "insert into prot2.setores_usuarios (codsetor, codusuario, ativo) values (?,?,?)";
                        ps = conexao.prepareStatement(sql2);
                        for (int i = 0; i < u.getCodSetor().size(); i++) {
                            ps.setInt(1, u.getCodSetor().get(i));
                            ps.setInt(2, u.getCodigo());
                            ps.setBoolean(3, false);
                            associouSet = ps.executeUpdate();
                        }
                    } catch (Exception sqle) {
                        sqle.getMessage();
                        throw new ProjetoException(sqle);
                    }
                }
            }
       
                cadastrou = true;
                conexao.commit();
            
            stmt.close();
            conexao.close();
            return cadastrou;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void updatePerm(UsuarioBean user) throws ProjetoException {
        // TODO Auto-generated method stub

        Connection conn = ConnectionFactory.getConnection();
        Integer condicao = 0;

        String select = "select * from prot2.permissoes where idusuario=?";

        ResultSet rs = null;

        try {
            PreparedStatement ps1 = conn.prepareStatement(select);
            ps1.setInt(1, user.getCodigo());
            rs = ps1.executeQuery();
            rs.next();
            condicao = rs.getRow();

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (condicao == 1) {
            try {

                String sql = " update prot2.permissoes set protocolar=?, tramitar=?, manutencao_proc=?, "
                        + " inclui_parecer=?, encaminha_protocolo=?, inclui_docto_digital=?, manutencao_nat_proc=? "
                        + " where idusuario=? ;";

                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setBoolean(1, user.getPermissao().getProtocolar());
                ps.setBoolean(2, user.getPermissao().getTramitar());
                ps.setBoolean(3, user.getPermissao().getAlterarProtocolo());
                ps.setBoolean(4, user.getPermissao().getIncluirParecer());
                ps.setBoolean(5, user.getPermissao().getEncaminharProtocolo());
                ps.setBoolean(6, user.getPermissao().getDocDigital());
                ps.setBoolean(7, user.getPermissao().getAlterarNatureza());
                ps.setInt(8, user.getCodigo());

                ps.executeUpdate();

                conn.commit();

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            } finally {
                try {
                    conn.close();
                } catch (Exception e2) {
                    // TODO: handle exception
                    e2.printStackTrace();
                }
            }
        } else {
            String sql = "INSERT INTO prot2.permissoes(idusuario, protocolar, tramitar, manutencao_proc, inclui_parecer, encaminha_protocolo, inclui_docto_digital) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?);";

            try {
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, user.getCodigo());
                ps.setBoolean(2, user.getPermissao().getProtocolar());
                ps.setBoolean(3, user.getPermissao().getTramitar());
                ps.setBoolean(4, user.getPermissao().getAlterarProtocolo());
                ps.setBoolean(5, user.getPermissao().getIncluirParecer());
                ps.setBoolean(6, false);
                ps.setBoolean(7, false);
                ps.executeUpdate();

                conn.commit();

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            } finally {
                try {
                    conn.close();
                } catch (Exception e2) {
                    // TODO: handle exception
                    e2.printStackTrace();
                }
            }

        }

    }

    @Override
    public List buscaUsuariosPorSecretaria(int cod) throws ProjetoException {
        // TODO Auto-generated method stub

        PreparedStatement ps = null;
        Connection con = ConnectionFactory.getConnection();

        try {
            String sql = "select * from prot2.usuarios u left join prot2.permissoes p on(u.id_usuario=p.idusuario)  "
                    + "where u.codsecretaria=?  order by ativo desc,descusuario";

            ps = con.prepareStatement(sql);
            ps.setInt(1, cod);
            ResultSet rs = ps.executeQuery();
            UsuarioBean n = new UsuarioBean();
            List colecao = new ArrayList<>();

            while (rs.next()) {
                n = new UsuarioBean();
                n.setCodigo(rs.getInt("id_usuario"));
                n.setNome(rs.getString("descusuario"));
                n.setCodSecre(rs.getInt("codsecretaria"));
                n.setSenha(rs.getString("senha"));
                n.setAtivo(rs.getString("ativo"));
                n.setLogin(rs.getString("login"));
                n.setAdministrador((rs.getBoolean("administrador")));
                n.setEmail(rs.getString("email"));
                n.setCpf(rs.getString("cpf"));
                n.getPermissao().setProtocolar(rs.getBoolean("protocolar"));
                n.getPermissao().setTramitar(rs.getBoolean("tramitar"));
                n.getPermissao().setAlterarProtocolo(
                        rs.getBoolean("manutencao_proc"));
                n.getPermissao().setIncluirParecer(
                        rs.getBoolean("inclui_parecer"));
                n.getPermissao().setEncaminharProtocolo(
                        rs.getBoolean("encaminha_protocolo"));
                n.getPermissao().setDocDigital(
                        rs.getBoolean("inclui_docto_digital"));
                n.getPermissao().setAlterarNatureza(
                        rs.getBoolean("manutencao_nat_proc"));
                n.setIdPerfil(rs.getInt("id_perfil"));
                n.setPermissaoJunta(rs.getBoolean("junta_medica"));

                colecao.add(n);
            }

            return colecao;

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
    }

    public ArrayList<UsuarioBean> buscaUsuario(String tipoBusca, String valor,
            int codsecretaria) throws ProjetoException {

        String consulta = "select * from prot2.usuarios u left join prot2.permissoes p on(u.id_usuario=p.idusuario) where u.codsecretaria= ? "
                + " and "
                + tipoBusca
                + " like ?"
                + " order by ativo desc,descusuario; ";

        Connection con = ConnectionFactory.getConnection();

        ResultSet rs = null;

        ArrayList<UsuarioBean> lista = new ArrayList<UsuarioBean>();

        try {

            PreparedStatement psConsulta = con.prepareStatement(consulta);

            psConsulta.setInt(1, codsecretaria);
            psConsulta.setString(2, "%" + valor.toUpperCase() + "%");

            rs = psConsulta.executeQuery();

            while (rs.next()) {

                UsuarioBean n = new UsuarioBean();
                n.setCodigo(rs.getInt("id_usuario"));
                n.setNome(rs.getString("descusuario"));
                n.setCodSecre(rs.getInt("codsecretaria"));
                n.setSenha(rs.getString("senha"));
                n.setAtivo(rs.getString("ativo"));
                n.setLogin(rs.getString("login"));
                n.setAdministrador((rs.getBoolean("administrador")));
                n.setEmail(rs.getString("email"));
                n.setCpf(rs.getString("cpf"));
                n.getPermissao().setProtocolar(rs.getBoolean("protocolar"));
                n.getPermissao().setTramitar(rs.getBoolean("tramitar"));
                n.getPermissao().setAlterarProtocolo(
                        rs.getBoolean("manutencao_proc"));
                n.getPermissao().setIncluirParecer(
                        rs.getBoolean("inclui_parecer"));
                n.getPermissao().setEncaminharProtocolo(
                        rs.getBoolean("encaminha_protocolo"));
                n.getPermissao().setDocDigital(
                        rs.getBoolean("inclui_docto_digital"));
                n.getPermissao().setAlterarNatureza(
                        rs.getBoolean("manutencao_nat_proc"));
                n.setIdPerfil(rs.getInt("id_perfil"));
                n.setPermissaoJunta(rs.getBoolean("junta_medica"));
                lista.add(n);

            }

        } catch (Exception e) {
            // TODO: handle exception

        } finally {
            try {

                con.close();
            } catch (SQLException e) {

                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        return lista;

    }

    public ArrayList<UsuarioBean> buscarPorSetor(Integer codSetor,
            int codsecretaria) throws ProjetoException {

        String consulta = "	select * from prot2.usuarios u left join prot2.permissoes p on(u.id_usuario=p.idusuario) where u.codsecretaria= ? "
                + "and exists (select codusuario from prot2.setores_usuarios where codusuario=u.id_usuario and setores_usuarios.codsetor=?)"
                + "order by ativo desc,descusuario;";

        Connection con = ConnectionFactory.getConnection();

        ResultSet rs = null;

        ArrayList<UsuarioBean> lista = new ArrayList<UsuarioBean>();

        try {

            PreparedStatement psConsulta = con.prepareStatement(consulta);

            psConsulta.setInt(1, codsecretaria);
            psConsulta.setInt(2, codSetor);

            rs = psConsulta.executeQuery();

            while (rs.next()) {

                UsuarioBean n = new UsuarioBean();
                n.setCodigo(rs.getInt("id_usuario"));
                n.setNome(rs.getString("descusuario"));
                n.setCodSecre(rs.getInt("codsecretaria"));
                n.setSenha(rs.getString("senha"));
                n.setAtivo(rs.getString("ativo"));
                n.setLogin(rs.getString("login"));
                n.setAdministrador((rs.getBoolean("administrador")));
                n.setEmail(rs.getString("email"));
                n.setCpf(rs.getString("cpf"));
                n.getPermissao().setProtocolar(rs.getBoolean("protocolar"));
                n.getPermissao().setTramitar(rs.getBoolean("tramitar"));
                n.getPermissao().setAlterarProtocolo(
                        rs.getBoolean("manutencao_proc"));
                n.getPermissao().setIncluirParecer(
                        rs.getBoolean("inclui_parecer"));
                n.getPermissao().setEncaminharProtocolo(
                        rs.getBoolean("encaminha_protocolo"));
                n.getPermissao().setDocDigital(
                        rs.getBoolean("inclui_docto_digital"));
                n.getPermissao().setAlterarNatureza(
                        rs.getBoolean("manutencao_nat_proc"));
                n.setIdPerfil(rs.getInt("id_perfil"));
                n.setPermissaoJunta(rs.getBoolean("junta_medica"));
                lista.add(n);

            }

        } catch (Exception e) {
            // TODO: handle exception

        } finally {
            try {

                con.close();
            } catch (SQLException e) {

                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        return lista;

    }

    // ALC
    public List<UsuarioBean> listarTodos() {

        String sql = "select * from acl.usuario order by nome";

        List<UsuarioBean> lista = new ArrayList<>();

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                UsuarioBean u = new UsuarioBean();
                u.setId(rs.getLong("id"));
                u.setNome(rs.getString("nome"));
                // u.setSexo(rs.getString("sexo"));
                u.setCpf(rs.getString("cpf"));
                u.setEmail(rs.getString("email"));
                u.setLogin(rs.getString("login"));
                u.setSenha(rs.getString("senha"));
                // u.setAtivo(rs.getBoolean("ativo"));
                u.setIdPerfil(rs.getInt("id_perfil"));
                lista.add(u);
            }
            rs.close();
            stmt.close();
            conexao.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return lista;
    }

    public List<UsuarioBean> listarAtivos() {

        String sql = "select * from acl.usuario where ativo = true order by nome";

        List<UsuarioBean> lista = new ArrayList<>();

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                UsuarioBean u = new UsuarioBean();
                u.setId(rs.getLong("id"));
                u.setNome(rs.getString("nome"));
                // u.setSexo(rs.getString("sexo"));
                u.setCpf(rs.getString("cpf"));
                u.setEmail(rs.getString("email"));
                u.setLogin(rs.getString("login"));
                u.setSenha(rs.getString("senha"));
                // u.setAtivo(rs.getBoolean("ativo"));
                u.setIdPerfil(rs.getInt("id_perfil"));
                lista.add(u);
            }
            rs.close();
            stmt.close();
            conexao.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return lista;
    }

    public List<UsuarioBean> listarInativos() {

        String sql = "select * from acl.usuario where ativo = false order by nome";

        List<UsuarioBean> lista = new ArrayList<>();

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                UsuarioBean u = new UsuarioBean();
                u.setId(rs.getLong("id"));
                u.setNome(rs.getString("nome"));
                // u.setSexo(rs.getString("sexo"));
                u.setCpf(rs.getString("cpf"));
                u.setEmail(rs.getString("email"));
                u.setLogin(rs.getString("login"));
                u.setSenha(rs.getString("senha"));
                // u.setAtivo(rs.getBoolean("ativo"));
                u.setIdPerfil(rs.getInt("id_perfil"));
                lista.add(u);
            }
            rs.close();
            stmt.close();
            conexao.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return lista;
    }

    public List<UsuarioBean> buscarPorCPF(String cpf) {

        String sql = "select * from acl.usuario where cpf = ?";

        List<UsuarioBean> lista = new ArrayList<>();

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, cpf);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                UsuarioBean u = new UsuarioBean();
                u.setId(rs.getLong("id"));
                u.setNome(rs.getString("nome"));
                // u.setSexo(rs.getString("sexo"));
                u.setCpf(rs.getString("cpf"));
                u.setEmail(rs.getString("email"));
                u.setLogin(rs.getString("login"));
                u.setSenha(rs.getString("senha"));
                // u.setAtivo(rs.getBoolean("ativo"));
                u.setIdPerfil(rs.getInt("id_perfil"));
                lista.add(u);
            }
            rs.close();
            stmt.close();
            conexao.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return lista;
    }

    public List<UsuarioBean> buscarPorLogin(String login) {

        String sql = "select * from acl.usuario where login = ?";

        List<UsuarioBean> lista = new ArrayList<>();

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                UsuarioBean u = new UsuarioBean();
                u.setId(rs.getLong("id"));
                u.setNome(rs.getString("nome"));
                // u.setSexo(rs.getString("sexo"));
                u.setCpf(rs.getString("cpf"));
                u.setEmail(rs.getString("email"));
                u.setLogin(rs.getString("login"));
                u.setSenha(rs.getString("senha"));
                // u.setAtivo(rs.getBoolean("ativo"));
                u.setIdPerfil(rs.getInt("id_perfil"));
                lista.add(u);
            }
            rs.close();
            stmt.close();
            conexao.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return lista;
    }

    public List<UsuarioBean> buscarPorNome(String nome) {

        String sql = "select * from acl.usuario where upper(nome) like ? order by nome";

        List<UsuarioBean> lista = new ArrayList<>();

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, nome.toUpperCase() + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                UsuarioBean u = new UsuarioBean();
                u.setId(rs.getLong("id"));
                u.setNome(rs.getString("nome"));
                // u.setSexo(rs.getString("sexo"));
                u.setCpf(rs.getString("cpf"));
                u.setEmail(rs.getString("email"));
                u.setLogin(rs.getString("login"));
                u.setSenha(rs.getString("senha"));
                // u.setAtivo(rs.getBoolean("ativo"));
                u.setIdPerfil(rs.getInt("id_perfil"));
                lista.add(u);
            }
            rs.close();
            stmt.close();
            conexao.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return lista;
    }

    public Boolean alterarSenha(UsuarioBean usuario) {

        String sql = "update prot2.usuarios set senha = ? where id_usuario = ?";

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
        }
    }
// THULIO AQUI E AS PERMISSOES DA URL DE ROTINA
    public List<Permissoes> carregarPermissoes(UsuarioBean u) {

        String sql = "select us.login, si.id as sid, si.descricao as sdesc, pf.descricao pfdesc, "
                + "pm.id as pmid, pm.descricao as pmdesc, 0 as funid, '' as fundesc, '' as funcodigo, "
                + "false as funativa, 0 as funidsis, me.id as meid, me.descricao as medesc, ro.desc_pagina, "
                + "ro.diretorio, ro.extensao, me.codigo as cod_menu, me.indice, me.tipo, me.ativo as meativo, "
                + "me.action_rel, me.onclick_rel, ro.descricao as rodesc, ro.id as roid from acl.perm_perfil pp "
                + "join prot2.usuarios us on us.id_perfil = pp.id_perfil "
                + "join acl.perfil pf on pf.id = pp.id_perfil "
                + "join acl.permissao pm on pm.id = pp.id_permissao "
                + "join acl.perm_geral pg on pg.id_permissao = pm.id "
                + "join acl.menu me on me.id = pg.id_menu "
                + "join acl.menu_sistema ms on ms.id_menu = me.id "
                + "join acl.rotina ro on me.id_rotina = ro.id "
                + "join acl.sistema si on si.id = ms.id_sistema where us.id_usuario = ? and me.ativo = true "
                + "union select us.login, si.id as sid, si.descricao as sdesc, pf.descricao pfdesc, "
                + "pm.id pmid, pm.descricao as pmdesc, fun.id as funid, fun.descricao as fundesc, "
                + "fun.codigo as funcodigo, fun.ativa as funativa, fun.id_sistema as fusidsis, 0 as meid, "
                + "'' as medesc, '', '', '', '' as cod_menu, '', case when '' = '' then 'funcao' end, "
                + "false as meativo, '', '', ro.descricao as rodesc, ro.id as roid from acl.perm_perfil pp "
                + "join prot2.usuarios us on us.id_perfil = pp.id_perfil "
                + "join acl.perfil pf on pf.id = pp.id_perfil "
                + "join acl.permissao pm on pm.id = pp.id_permissao "
                + "join acl.perm_geral pg on pg.id_permissao = pm.id "
                + "join acl.funcao fun on fun.id = pg.id_funcao  "
                + "join acl.rotina ro on fun.id_rotina = ro.id "
                + "join acl.sistema si on si.id = fun.id_sistema where us.id_usuario = ? and fun.ativa = true "
                + "union select us.login, si.id as sid, si.descricao as sdesc, '' as pfdesc, "
                + "pm.id as pmid, pm.descricao as pmdesc, 0 as funid, '' as fundesc, '' as funcodigo, "
                + "false as funativa, 0 as funidsis, me.id as meid, me.descricao as medesc, ro.desc_pagina, "
                + "ro.diretorio, ro.extensao, me.codigo as cod_menu, me.indice, me.tipo, me.ativo as meativo, "
                + "me.action_rel, me.onclick_rel, ro.descricao as rodesc, ro.id as roid from acl.perm_usuario pu "
                + "join prot2.usuarios us on us.id_usuario = pu.id_usuario "
                + "join acl.permissao pm on pm.id = pu.id_permissao "
                + "join acl.perm_geral pg on pg.id_permissao = pm.id "
                + "join acl.menu me on me.id = pg.id_menu "
                + "join acl.menu_sistema ms on ms.id_menu = me.id "
                + "join acl.rotina ro on me.id_rotina = ro.id "
                + "join acl.sistema si on si.id = ms.id_sistema where us.id_usuario = ? and me.ativo = true "
                + "union select us.login, si.id as sid, si.descricao as sdesc, '', "
                + "pm.id pmid, pm.descricao as pmdesc, fun.id as funid, fun.descricao as fundesc, "
                + "fun.codigo as funcodigo, fun.ativa as funativa, fun.id_sistema as funidsis, 0 as meid, "
                + "'' as medesc, '', '', '', '' as cod_menu, '', case when '' = '' then 'funcao' end, "
                + "false as meativo, '', '', ro.descricao as rodesc, ro.id as roid from acl.perm_usuario pu "
                + "join prot2.usuarios us on us.id_usuario = pu.id_usuario "
                + "join acl.permissao pm on pm.id = pu.id_permissao "
                + "join acl.perm_geral pg on pg.id_permissao = pm.id "
                + "join acl.funcao fun on fun.id = pg.id_funcao "
                + "join acl.rotina ro on fun.id_rotina = ro.id "
                + "join acl.sistema si on si.id = fun.id_sistema where us.id_usuario = ? and fun.ativa = true "
                + "order by pmdesc, sid";

        List<Permissoes> lista = new ArrayList<>();

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setLong(1, u.getId());
            stmt.setLong(2, u.getId());
            stmt.setLong(3, u.getId());
            stmt.setLong(4, u.getId());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Menu m = new Menu();
                m.setId(rs.getLong("meid"));
                m.setDescricao(rs.getString("medesc"));
                m.setCodigo(rs.getString("cod_menu"));
                m.setIndice(rs.getString("indice"));
                m.setTipo(rs.getString("tipo"));
                m.setAtivo(rs.getBoolean("meativo"));
                m.getRotina().setDiretorio(rs.getString("diretorio"));
                m.getRotina().setDescPagina(rs.getString("desc_pagina"));
                m.getRotina().setExtensao(rs.getString("extensao"));
                m.setAction(rs.getString("action_rel"));
                m.setOnclick(rs.getString("onclick_rel"));

                if (rs.getString("tipo").equals("menuItem")) {
                    m.setUrl("/pages/" + m.getRotina().getDiretorio() + "/"
                            + m.getRotina().getDescPagina() + m.getRotina().getExtensao());
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

                p.setIdRotina(rs.getInt("roid"));
                p.setDescRotina(rs.getString("rodesc"));

                lista.add(p);
            }
            rs.close();
            stmt.close();
            conexao.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return lista;
    }

    public List<Permissoes> FiltroListaPermissao() {

        List<Permissoes> lista = new ArrayList<>();

        return lista;
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
        }
        return associou;
    }

    public boolean associarPermAoUsuario(UsuarioBean usuario) {

        String sql = "insert into acl.perm_usuario (id_usuario, id_permissao) values (?, ?)";

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
        }
        return associou;
    }

    public boolean associarPermAoUsuarioAlt(UsuarioBean usuario) {

        String sql = "delete from acl.perm_usuario where id_usuario = ?";

        String sql2 = "insert into acl.perm_usuario (id_usuario, id_permissao) values (?, ?)";

        boolean associou = false;

        List<Long> listaId = null;

        if (usuario.getListaIdPermissoes() != null) {
            listaId = usuario.getListaIdPermissoes();
        }

        PreparedStatement stmt;
        try {
            // conexao = ConnectionFactory.getConnection();
            stmt = conexao.prepareStatement(sql);
            stmt.setLong(1, usuario.getCodigo());
            stmt.execute();
			// conexao.commit();
            // stmt.close();
            // conexao.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        if (listaId != null) {
            try {
                // conexao = ConnectionFactory.getConnection();
                stmt = conexao.prepareStatement(sql2);
                for (Long idPerm : listaId) {
                }

                for (Long idPerm : listaId) {
                    stmt.setLong(1, usuario.getCodigo());
                    stmt.setLong(2, idPerm);
                    stmt.execute();
                }
				// conexao.commit();
                // stmt.close();
                // conexao.close();

                associou = true;
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        } else {
            // Associou sem permissões extras.
            associou = true;
        }
        return associou;
    }

    public boolean removerPermUsuario(UsuarioBean usuario) {

        String sql = "delete from acl.perm_usuario where id_usuario = ?";

        boolean removeu = false;

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setLong(1, usuario.getCodigo());
            stmt.execute();
            conexao.commit();
            stmt.close();
            conexao.close();

            removeu = true;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return removeu;
    }

    public boolean associarUsuarioAoSistemaAlt(UsuarioBean usuario) {

        String sql = "delete from acl.perm_sistema where id_usuario = ?";

        String sql2 = "insert into acl.perm_sistema (id_usuario, id_sistema) values (?, ?)";

        boolean associou = false;

        List<Integer> listaId = usuario.getListaIdSistemas();

        PreparedStatement stmt;
        try {
            // conexao = ConnectionFactory.getConnection();
            stmt = conexao.prepareStatement(sql);
            stmt.setLong(1, usuario.getCodigo());
            stmt.execute();
			// conexao.commit();
            // stmt.close();
            // conexao.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        if (!usuario.getListaIdSistemas().isEmpty()) {
            try {
                // conexao = ConnectionFactory.getConnection();
                stmt = conexao.prepareStatement(sql2);
                for (Integer idSistema : listaId) {
                    stmt.setLong(1, usuario.getCodigo());
                    stmt.setInt(2, idSistema);
                    stmt.execute();
                }
				// conexao.commit();
                // stmt.close();
                // conexao.close();

                associou = true;
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
        return associou;
    }

    public List<Sistema> recListaSisSoucerCad() {

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
        }
        return listaSistemas;
    }

    public List<Sistema> recListaSisSoucer(Long id) {

        String sql = "select id, descricao from acl.sistema "
                + "where id not in(select si.id from acl.perm_sistema ps "
                + "join acl.sistema si on si.id = ps.id_sistema "
                + "join prot2.usuarios us on us.id_usuario = ps.id_usuario "
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
        }
        return listaSistemas;
    }

    public List<Sistema> recListaSisTarget(Long id) {

        String sql = "select si.id, si.descricao from acl.perm_sistema ps "
                + "join acl.sistema si on si.id = ps.id_sistema "
                + "join prot2.usuarios us on us.id_usuario = ps.id_usuario "
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
        }
        return listaSistemas;
    }

    public List<Sistema> carregarSistemasUsuario(UsuarioBean usuario) {

        String sql = "select si.id, si.descricao, si.sigla, si.url, si.imagem, "
                + "si.versao, si.ativo from acl.perm_sistema ps "
                + "join acl.sistema si on si.id = ps.id_sistema "
                + "join prot2.usuarios us on us.id_usuario = ps.id_usuario "
                + "where us.id_usuario = ? and si.ativo = true order by si.descricao";

        List<Sistema> listaSistemas = new ArrayList<>();

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setLong(1, usuario.getId());
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
            rs.close();
            stmt.close();
            conexao.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return listaSistemas;
    }

    public UsuarioBean autenticarUsuarioacl(UsuarioBean usuario) {

        String sql = "select us.id, us.nome, us.sexo, us.cpf, us.email, us.login, "
                + "us.senha, us.ativo, us.id_perfil, pf.descricao from acl.usuario us "
                + "join acl.perfil pf on pf.id = us.id_perfil where login = ? and "
                + "senha = ? and ativo = true";

        UsuarioBean u = null;

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
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

    public ArrayList<Permissao> listarPermSemPerfSource(Integer id) {

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
        }
        return lista;
    }

    public ArrayList<Permissao> listarPermSemPerfAssSource(Integer idPefil,
            Integer idUsuario) {

        String sql = "select id, descricao from acl.permissao where id not in ("
                + "select pm.id from acl.perfil pf "
                + "join acl.perm_perfil pp on pf.id = pp.id_perfil "
                + "join acl.permissao pm on pm.id = pp.id_permissao "
                + "where pf.id = ? "
                + "union select p.id from prot2.usuarios u "
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
        }
        return lista;
    }

    public ArrayList<Permissao> listarPermSemPerfAssTarget(Integer id) {

        String sql = "select p.id, p.descricao from prot2.usuarios u "
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
        }
        return lista;
    }

   
}
