package br.gov.al.maceio.sishosp.comum.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Types;

import br.gov.al.maceio.sishosp.comum.model.ErroSistema;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactoryPublico;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;

public class ExceptionDAO {
	
	public void gravaExcecao(ErroSistema erroSistema) {
		
		String sql = "INSERT INTO logs.erros_sistema " + 
				"(descricao_erro, datahora_erro, id_usuario_logado, nome_banco, nome_classe, nome_metodo, linha_erro) " + 
				"VALUES(?, CURRENT_TIMESTAMP, ?, ?, ?, ?, ?); ";
		Connection con = null;
		try {
			con = ConnectionFactoryPublico.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, erroSistema.getDescricao());
			
			if(VerificadorUtil.verificarSeObjetoNuloOuZero(erroSistema.getIdUsuarioLogado()))
				ps.setNull(2, Types.NULL);
			else	
				ps.setLong(2, erroSistema.getIdUsuarioLogado());
			
			if(VerificadorUtil.verificarSeObjetoNuloOuVazio(erroSistema.getBanco()))
				ps.setNull(3, Types.NULL);
			else
				ps.setString(3, erroSistema.getBanco());
			
			ps.setString(4, erroSistema.getNomeClasse());
			ps.setString(5, erroSistema.getNomeMetodo());
			ps.setInt(6, erroSistema.getLinhaErro());
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
