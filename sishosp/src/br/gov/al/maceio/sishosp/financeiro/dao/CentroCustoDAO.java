package br.gov.al.maceio.sishosp.financeiro.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.financeiro.model.CentroCustoBean;

public class CentroCustoDAO {

	public boolean salvarCentro(CentroCustoBean bean) throws ProjetoException {
		boolean result= false; 
		FuncionarioBean user_session = (FuncionarioBean) FacesContext
				.getCurrentInstance().getExternalContext().getSessionMap()
				.get("obj_usuario");

		String sql = " INSERT INTO financeiro.ccusto ( descricao)  VALUES (?); ";

		Connection con = ConnectionFactory.getConnection();

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, bean.getDescricao().toUpperCase());

			ps.execute();

			con.commit();
			
			ps.close();
			result= true;
		} catch (SQLException e) {
			result= false;
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					e.getMessage(), "Erro:"+e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} finally {

			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;

	}

	public boolean editarCusto(CentroCustoBean c) throws ProjetoException {
		boolean result= false; 
		Connection con = ConnectionFactory.getConnection();

		String sql = " UPDATE financeiro.ccusto SET  descricao=? WHERE idccusto = ? ";

		try {

			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, c.getDescricao().toUpperCase());
			ps.setInt(2, c.getIdccusto());

			ps.execute();
			con.commit();
			
			ps.close();
			result= true;
		} catch (SQLException e) {
			result= false;
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					e.getMessage(), "Erro:"+e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} finally {

			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;

	}

	public boolean deleteCusto(CentroCustoBean c) throws ProjetoException {
		boolean result= false; 
		Connection con = ConnectionFactory.getConnection();

		String sql = " DELETE FROM financeiro.ccusto WHERE idccusto = ? ";

		try {

			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, c.getIdccusto());

			ps.execute();
			con.commit();
			result= true;

			ps.close();
		} catch (SQLException e) {
			result= false;
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					e.getMessage(), "Erro:"+e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} finally {

			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;

	}

	public List<CentroCustoBean> lstCustos() throws ProjetoException {

		FuncionarioBean user_session = (FuncionarioBean) FacesContext
				.getCurrentInstance().getExternalContext().getSessionMap()
				.get("obj_usuario");

		String sql = " select * from financeiro.ccusto ";
		ResultSet set = null;
		Connection con = ConnectionFactory.getConnection();
		ArrayList<CentroCustoBean> lst = new ArrayList<>();

		try {

			PreparedStatement ps = con.prepareStatement(sql);


			set = ps.executeQuery();

			while (set.next()) {

				CentroCustoBean c = new CentroCustoBean();

				c.setIdccusto(set.getInt("idccusto"));
				c.setDescricao(set.getString("descricao"));
				lst.add(c);

			}

			set.close();
			ps.close();
		} catch (SQLException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();

		} finally {

			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return lst;

	}

}
