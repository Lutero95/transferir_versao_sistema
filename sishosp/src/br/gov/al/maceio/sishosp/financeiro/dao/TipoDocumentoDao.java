package br.gov.al.maceio.sishosp.financeiro.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.financeiro.model.TipoDocumentoBean;


public class TipoDocumentoDao {

	public void excluirTipoDocumento(TipoDocumentoBean t) throws ProjetoException {
		String sql = " DELETE FROM financeiro.tipodocumento WHERE codtipodocumento = ? ";

		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = ConnectionFactory.getConnection();

			ps = con.prepareStatement(sql);

			ps.setInt(1, t.getCodtipodocumento());

			ps.execute();

			con.commit();

		} catch (SQLException e) {
			e.printStackTrace();
			JSFUtil.adicionarMensagemErro(e.getMessage(),"Erro");
		} finally {
			try {
				ps.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	public boolean editarTipoDocumento(TipoDocumentoBean t) throws ProjetoException {
		String sql = " UPDATE financeiro.tipodocumento SET descricao=? WHERE codtipodocumento = ?";//, devolucao_venda = ?, cheque=?, cartao_debito=?, cartao_credito=?, promissoria=?  ; ";
		boolean rst = false;
		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, t.getDescricao().toUpperCase());
			ps.setInt(2, t.getCodtipodocumento());
			/*
			ps.setBoolean(2, t.getDevolucao_venda());
			ps.setBoolean(3, t.getCheque());
			ps.setBoolean(4, t.getCartaoDebito());
			ps.setBoolean(5, t.getCartaoCredito());
			ps.setBoolean(6, t.getPromissoria());
			
		*/
			ps.execute();

			con.commit();
			rst = true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return rst;

	}

	public boolean salvarDocumento(TipoDocumentoBean t) throws ProjetoException {
		boolean rst = false;
		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("obj_usuario");

		String sql = " INSERT INTO financeiro.tipodocumento (descricao,  datacadastro, opcad) "//, devolucao_venda, cheque, cartao_debito, cartao_credito, promissoria) "
				+ " VALUES (?,  current_date,?) ";

		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, t.getDescricao().toUpperCase());
			ps.setInt(2, user_session.getCodigo());
			/*
			ps.setBoolean(3, t.getDevolucao_venda());
			ps.setBoolean(4, t.getCheque());
			ps.setBoolean(5, t.getCartaoDebito());
			ps.setBoolean(6, t.getCartaoCredito());
			ps.setBoolean(7, t.getPromissoria());
			*/
			ps.execute();

			con.commit();
			rst = true;
			
		} 
		
		catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return rst;
	}

	public List<TipoDocumentoBean> lstTipoDocumento() throws ProjetoException {

		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("obj_usuario");
		ArrayList<TipoDocumentoBean> lst = new ArrayList<TipoDocumentoBean>();
		String sql = " select * from financeiro.tipodocumento  ;";

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet set = null;
		try {
			con = ConnectionFactory.getConnection();

			ps = con.prepareStatement(sql);
			set = ps.executeQuery();
			while (set.next()) {
				TipoDocumentoBean t = new TipoDocumentoBean();

				t.setCodtipodocumento(set.getInt("codtipodocumento"));
				t.setDescricao(set.getString("descricao"));
				/*
				t.setDevolucao_venda(set.getBoolean("devolucao_venda"));
				t.setCheque(set.getBoolean("cheque"));
				t.setCartaoDebito(set.getBoolean("cartao_debito"));
				t.setCartaoCredito(set.getBoolean("cartao_credito"));
				t.setPromissoria(set.getBoolean("promissoria"));
				*/
				lst.add(t);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				set.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return lst;
	}

}
