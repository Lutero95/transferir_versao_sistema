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
import br.gov.al.maceio.sishosp.comum.util.TratamentoErrosUtil;
import br.gov.al.maceio.sishosp.financeiro.model.PortadorBean;


public class PortadorDAO {

	public ArrayList<PortadorBean> buscaTodosPort() throws ProjetoException {

		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;

		ArrayList<PortadorBean> colecao = new ArrayList<>();
		try {
			con = ConnectionFactory.getConnection();
			
			String sql = "select codportador, descricao"
					+  "	 from financeiro.portador  order by descricao";

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			
			while (rs.next()) {				
				PortadorBean portador = new PortadorBean();
				portador.setCodportador(rs.getInt("codportador"));
				portador.setDescricao(rs.getString("descricao"));
				colecao.add(portador);
			}
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				ps.close();
				rs.close();
				con.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
			}
		}
		return colecao;
	}

	public ArrayList<PortadorBean> lstPortadores(String descricao)
			throws ProjetoException {

		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;

		ArrayList<PortadorBean> colecao = new ArrayList<>();
		try {
			con = ConnectionFactory.getConnection();
			
			String sql = "select codportador, descricao"
					+ "	 from financeiro.portador where  descricao like  ? order by descricao";

			ps = con.prepareStatement(sql);
			ps.setString(1, "%" + descricao + "%");
			rs = ps.executeQuery();
			PortadorBean portador = new PortadorBean();

			while (rs.next()) {
				portador = new PortadorBean();
				portador.setCodportador(rs.getInt("codportador"));
				portador.setDescricao(rs.getString("descricao"));
				colecao.add(portador);
			}
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				ps.close();
				rs.close();
				con.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
			}
		}
		return colecao;
	}

	public String inserePort(PortadorBean portador) throws ProjetoException {
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
			ps.setString(1, portador.getDescricao().toUpperCase());
			ps.setLong(2, user_session.getId());

			ps.executeUpdate();
			con.commit();
			
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
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
	
	public String atualizaPort(PortadorBean portador)  throws ProjetoException {
		PreparedStatement ps = null;
		Connection con = null;
		String retorno = "OK";
		
		try {
			con = ConnectionFactory.getConnection();
			
			String sql = "update financeiro.portador set descricao=?  where codportador=?";
			
			ps = con.prepareStatement(sql);
			ps.setString(1, portador.getDescricao().toUpperCase());
			ps.setInt(2, portador.getCodportador());
			ps.executeUpdate();
			con.commit();
			
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
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
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		}  finally {
			try {
				ps.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return retorno;
	}		
		
}
