package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.TratamentoErrosUtil;
import br.gov.al.maceio.sishosp.hosp.model.CidBean;

public class CidDAO {

	Connection con = null;
	PreparedStatement ps = null;


	public boolean gravarCid(CidBean cid) throws ProjetoException{
		Boolean retorno = false;
		String sql = "insert into hosp.cid (cid,desccid) values (?,?);";

		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			//FuncionarioBean func = null;
		//	func.getNome();
			ps.setString(1, cid.getCid().toUpperCase());
			ps.setString(2, cid.getDescCid().toUpperCase());
			ps.execute();
			con.commit();
			retorno = true;
		 
    } 
		catch (NullPointerException | SQLException sqle ) {
			sqle.printStackTrace();
        
		sqle.printStackTrace();
		 throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(((SQLException) sqle).getSQLState()));
	}finally
	{
		try {
			con.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return retorno;
	}
	}

	public List<CidBean> listarCid() throws ProjetoException {
		List<CidBean> lista = new ArrayList<>();
		String sql = "select cod, desccid, cid from hosp.cid order by cod";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				CidBean cid = new CidBean();
				cid.setIdCid(rs.getInt("cod"));
				cid.setDescCid(rs.getString("desccid"));
				cid.setCid(rs.getString("cid"));

				lista.add(cid);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return lista;
	}

	public List<CidBean> buscarCid(String campoBusca, String tipo) {
		List<CidBean> lista = new ArrayList<>();
		String sql = "select cod, desccid, cid from hosp.cid where ";

		if(tipo.equals("cid")){
			sql = sql + "cid like ?";
		}
		else if(tipo.equals("descricao")){
			sql = sql + "desccid ilike ?";
		}

		sql = sql + "order by cod";

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setString(1, "%" + campoBusca.toUpperCase() + "%");
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				CidBean cid = new CidBean();
				cid.setIdCid(rs.getInt("cod"));
				cid.setDescCid(rs.getString("desccid"));
				cid.setCid(rs.getString("cid"));

				lista.add(cid);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return lista;
	}

	public Boolean alterarCid(CidBean cid) {
		Boolean retorno = false;
		String sql = "update hosp.cid set desccid = ?, cid = ? where cod = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, cid.getDescCid().toUpperCase());
			stmt.setString(2, cid.getCid().toUpperCase());
			stmt.setInt(3, cid.getIdCid());
			stmt.executeUpdate();
			con.commit();
			retorno = true;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			return retorno;
		}
	}

	public Boolean excluirCid(CidBean cid) {
		Boolean retorno = false;
		String sql = "delete from hosp.cid where cod = ?";

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setLong(1, cid.getIdCid());
			stmt.execute();
			con.commit();
			retorno = true;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			return retorno;
		}
	}

	public CidBean buscaCidPorId(Integer id) throws ProjetoException {
		con = ConnectionFactory.getConnection();
		String sql = "select cod, desccid, cid from hosp.cid where cod = ?";
		try {

			ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			CidBean g = new CidBean();
			while (rs.next()) {
				g.setIdCid(rs.getInt("cod"));
				g.setDescCid(rs.getString("desccid"));
				g.setCid(rs.getString("cid"));
			}

			return g;

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new ProjetoException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
			}
		}
	}

	public List<CidBean> listarCidsBusca(String descricao) throws ProjetoException {
		List<CidBean> lista = new ArrayList<>();
		String sql = "select cod, desccidabrev, cid from hosp.cid where desccidabrev ILIKE ? or cid Ilike ?";

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setString(1, "%" + descricao + "%");
			stm.setString(2, "%" + descricao + "%");
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				CidBean cid = new CidBean();
				cid.setIdCid(rs.getInt("cod"));
				cid.setDescCidAbrev(rs.getString("desccidabrev"));
				cid.setCid(rs.getString("cid"));

				lista.add(cid);
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return lista;
	}

	public List<CidBean> listarCidsAutoComplete(String descricao)
			throws ProjetoException {
		List<CidBean> lista = new ArrayList<>();
		String sql = "select c.cod, c.desccidabrev, c.cid from hosp.cid c "
				+ " where 1=1  and desccidabrev ILIKE ? order by c.desccid";

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setString(1, "%" + descricao + "%");

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				CidBean cid = new CidBean();
				cid.setIdCid(rs.getInt("cod"));
				cid.setDescCidAbrev(rs.getString("desccidabrev"));
				cid.setCid(rs.getString("cid"));

				lista.add(cid);
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return lista;
	}
	
	public List<CidBean> listarCidsBuscaPorProcedimento(String descricao, String codigoProcedimento) throws ProjetoException {
		List<CidBean> lista = new ArrayList<>();
		String sql = "select cod, desccidabrev, cid " + 
				"from hosp.cid join sigtap.cid_procedimento_mensal cpm on cid.cod = cpm.id_cid " + 
				"join sigtap.procedimento_mensal pm on pm.id = cpm.id_procedimento_mensal " + 
				"where desccidabrev Ilike ? or cid Ilike ? " + 
				"and (pm.codigo_procedimento = ?)";

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setString(1, "%" + descricao + "%");
			stm.setString(2, "%" + descricao + "%");
			stm.setString(3, codigoProcedimento);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				CidBean cid = new CidBean();
				cid.setIdCid(rs.getInt("cod"));
				cid.setDescCidAbrev(rs.getString("desccidabrev"));
				cid.setCid(rs.getString("cid"));

				lista.add(cid);
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return lista;
	}
	
	public List<CidBean> listarCidsAutoCompletePorProcedimento(String descricao, String codigoProcedimento)
			throws ProjetoException {
		List<CidBean> lista = new ArrayList<>();
		String sql = "select cid.cod, cid.desccidabrev, cid.cid "+
				"from hosp.cid join sigtap.cid_procedimento_mensal cpm on cid.cod = cpm.id_cid " + 
				"join sigtap.procedimento_mensal pm on pm.id = cpm.id_procedimento_mensal " + 
				"where 1=1  and desccidabrev ILIKE ? and (pm.codigo_procedimento = ?) order by desccid";

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setString(1, "%" + descricao.toUpperCase() + "%");
			stm.setString(2, codigoProcedimento);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				CidBean cid = new CidBean();
				cid.setIdCid(rs.getInt("cod"));
				cid.setDescCidAbrev(rs.getString("desccidabrev"));
				cid.setCid(rs.getString("cid"));

				lista.add(cid);
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return lista;
	}
}
