package br.gov.al.maceio.sishosp.financeiro.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.TratamentoErrosUtil;
import br.gov.al.maceio.sishosp.financeiro.model.CentroCustoBean;

public class CentroCustoDAO {

	public boolean salvarCentro(CentroCustoBean bean) throws ProjetoException {
		boolean result= false; 

		String sql = " INSERT INTO financeiro.ccusto ( descricao)  VALUES (?); ";

		Connection con = ConnectionFactory.getConnection();

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, bean.getDescricao().toUpperCase());

			ps.execute();
			con.commit();
			
			ps.close();
			result= true;
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				//comentado walter erro log ex.printStackTrace();
			}
		}
		return result;

	}

	public boolean editarCusto(CentroCustoBean centroCusto) throws ProjetoException {
		boolean result= false; 
		Connection con = ConnectionFactory.getConnection();

		String sql = " UPDATE financeiro.ccusto SET  descricao=? WHERE idccusto = ? ";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, centroCusto.getDescricao().toUpperCase());
			ps.setInt(2, centroCusto.getIdccusto());

			ps.execute();
			con.commit();
			
			ps.close();
			result= true;
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				//comentado walter erro log ex.printStackTrace();
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
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				//comentado walter erro log ex.printStackTrace();
			}
		}
		return result;
	}

	public List<CentroCustoBean> lstCustos() throws ProjetoException {

		String sql = " select * from financeiro.ccusto ";
		ResultSet set = null;
		Connection con = ConnectionFactory.getConnection();
		ArrayList<CentroCustoBean> listaCentroCusto = new ArrayList<>();

		try {

			PreparedStatement ps = con.prepareStatement(sql);
			set = ps.executeQuery();

			while (set.next()) {
				CentroCustoBean centroCusto = new CentroCustoBean();
				centroCusto.setIdccusto(set.getInt("idccusto"));
				centroCusto.setDescricao(set.getString("descricao"));
				listaCentroCusto.add(centroCusto);
			}
			set.close();
			ps.close();
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				//comentado walter erro log ex.printStackTrace();
			}
		}
		return listaCentroCusto;
	}

}
