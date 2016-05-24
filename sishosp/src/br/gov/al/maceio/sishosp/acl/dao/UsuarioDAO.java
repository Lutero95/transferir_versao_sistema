package br.gov.al.maceio.sishosp.acl.dao;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.acl.model.UsuarioBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import javax.faces.context.FacesContext;


public class UsuarioDAO{

    private Connection conexao = null;


    public UsuarioBean autenticarUsuario(UsuarioBean usuario) throws ProjetoException {

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
            conexao = ConnectionFactory.getConnection();
            PreparedStatement pstmt = conexao.prepareStatement(sql);
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
            	   conexao.close();
            } catch (Exception ex) {
                // TODO: handle exception
                ex.printStackTrace();
            }
        }
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

   
   
}
