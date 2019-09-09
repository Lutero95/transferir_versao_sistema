package br.gov.al.maceio.sishosp.financeiro.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.financeiro.model.PortadorBean;




public class PortadorDAO {

	public ArrayList<PortadorBean> buscaTodosPort() throws ProjetoException {

		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;

		try {
			con = ConnectionFactory.getConnection();
			
			String sql = "select codportador, descricao"
					+  "	 from financeiro.portador  order by descricao";

			FuncionarioBean user_session = (FuncionarioBean) FacesContext
					.getCurrentInstance().getExternalContext().getSessionMap()
					.get("obj_usuario");

			ps = con.prepareStatement(sql);
			

			rs = ps.executeQuery();
			
			PortadorBean e = new PortadorBean();
			ArrayList<PortadorBean> colecao = new ArrayList<>();
			
			while (rs.next()) {
				e = new PortadorBean();
				
				e.setCodportador(rs.getInt("codportador"));
				e.setDescricao(rs.getString("descricao"));


				colecao.add(e);
			}

			return colecao;

		} catch (Exception sqle) {
			sqle.printStackTrace();
			throw new ProjetoException(sqle);
		} finally {
			try {
				ps.close();
				rs.close();
				con.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
			}
		}
	}

	public ArrayList<PortadorBean> lstPortadores(String descricao)
			throws ProjetoException {

		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;

		try {
			con = ConnectionFactory.getConnection();
			
			String sql = "select codportador, descricao"
					+ "	 from financeiro.portador where  descricao like  ? order by descricao";

			FuncionarioBean user_session = (FuncionarioBean) FacesContext
					.getCurrentInstance().getExternalContext().getSessionMap()
					.get("obj_usuario");

			ps = con.prepareStatement(sql);
			
			ps.setString(1, "%" + descricao + "%");

			rs = ps.executeQuery();

			
			PortadorBean e = new PortadorBean();
			ArrayList<PortadorBean> colecao = new ArrayList<>();

			while (rs.next()) {

				e = new PortadorBean();
			
				e.setCodportador(rs.getInt("codportador"));
				e.setDescricao(rs.getString("descricao"));


				colecao.add(e);
			}

			return colecao;

		} catch (Exception sqle) {
			sqle.printStackTrace();
			throw new ProjetoException(sqle);
		} finally {
			try {
				ps.close();
				rs.close();
				con.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
			}
		}
	}

	public String inserePort(PortadorBean e) throws ProjetoException {
		PreparedStatement ps = null;
		Connection con = null;
		
		String retorno = "OK";
		try {
			con = ConnectionFactory.getConnection();
			
			String sql = "insert into financeiro.portador (descricao, opcad, datacadastro) values (?,?,current_timestamp) ";
			
			FuncionarioBean user_session = (FuncionarioBean) FacesContext
					.getCurrentInstance().getExternalContext().getSessionMap()
					.get("obj_usuario");
			
			ps = con.prepareStatement(sql);
			ps.setString(1, e.getDescricao().toUpperCase());
			ps.setLong(2, user_session.getId());

			ps.executeUpdate();
			con.commit();
			
		} catch (Exception sqle) {
			sqle.printStackTrace();
			retorno = sqle.getMessage();
			throw new ProjetoException(sqle);

		} finally {
			try {
				ps.close();
				con.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
			}

		}
		return retorno;
	}
	
	public String atualizaPort(PortadorBean e)  throws ProjetoException {
		PreparedStatement ps = null;
		Connection con = null;
		String retorno = "OK";
		
		try {
			con = ConnectionFactory.getConnection();
			
			String sql = "update financeiro.portador set descricao=?  where codportador=?";
			
			ps = con.prepareStatement(sql);
			ps.setString(1, e.getDescricao().toUpperCase());
			ps.setInt(2, e.getCodportador());
			ps.executeUpdate();
			con.commit();
			
		} catch (Exception sqle) {
			sqle.printStackTrace();
			retorno =sqle.getMessage();
			throw new ProjetoException(sqle);
		} finally {
			try {
				ps.close();
				con.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
			}


		}
		return retorno;
	}
	
	public String excluir(PortadorBean bean) throws ProjetoException {
		String retorno = "OK";
		Connection con = null;
		PreparedStatement ps = null;

		String sql = "delete from  financeiro.portador   WHERE codportador= ? ;";

		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);

			
			ps.setInt(1, bean.getCodportador());

			ps.execute();

			con.commit();

			return retorno;

		} catch (SQLException e) {
			e.printStackTrace();
			retorno = e.getMessage();
			return retorno;			
		} finally {
			try {
				ps.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		
	}		
		
}
