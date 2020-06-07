package br.gov.al.maceio.sishosp.comum.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.faces.context.FacesContext;
import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactoryPublico;

public class ExceptionDAO {
	
	FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
			.getSessionMap().get("obj_usuario");
	

	
	public void gravaExcecao(String descricaoErro) {
		
		String sql = "INSERT INTO logs.erros_sistema (descricao_erro, datahora_erro, id_usuario_logado, nome_banco) " + 
				"VALUES(?, CURRENT_TIMESTAMP, ?, ?); ";
		Connection con = null;
		try {
			con = ConnectionFactoryPublico.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, descricaoErro);
			ps.setLong(2, user_session.getId());
			ps.setString(3, user_session.getNomeBancoAcesso());
			ps.executeUpdate();
			con.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
}
