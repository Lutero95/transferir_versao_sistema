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
        
		System.out.println(((SQLException) sqle).getErrorCode());
		System.out.println(sqle.getLocalizedMessage());
		System.out.println(((SQLException) sqle).getSQLState());
		System.out.println(sqle.getCause());
		System.out.println(sqle.getSuppressed());
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
		String sql = "select cod, desccid, cid from hosp.cid where desccidabrev LIKE ? or cid like ?";

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setString(1, "%" + descricao.toUpperCase() + "%");
			stm.setString(2, "%" + descricao.toUpperCase() + "%");
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				CidBean c = new CidBean();
				c.setIdCid(rs.getInt("cod"));
				c.setDescCid(rs.getString("desccid"));
				c.setCid(rs.getString("cid"));

				lista.add(c);
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

	public List<CidBean> listarCidsBuscaPorProcedimentoAutoComplete(String descricao)
			throws ProjetoException {
		List<CidBean> lista = new ArrayList<>();
		String sql = "select c.cod, c.desccid, c.cid from hosp.cid c "
				//"left join hosp.proc_cid p on (p.id_cid = c.cod) "
			//	+ " where c.cod = ? and desccid LIKE ? order by c.desccid";
				+ " where 1=1  and desccid LIKE ? order by c.desccid";

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
		//	stm.setInt(1, id_proc);
			stm.setString(1, "%" + descricao.toUpperCase() + "%");

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				CidBean c = new CidBean();
				c.setIdCid(rs.getInt("cod"));
				c.setDescCid(rs.getString("desccid"));
				c.setCid(rs.getString("cid"));

				lista.add(c);
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

	public List<CidBean> listarCidsBusca() throws ProjetoException {
		List<CidBean> lista = new ArrayList<>();
		String sql = "select c.cod, c.desccid, c.cid from hosp.cid c  "
//				+ " where c.cod = ? order by c.desccid";
				+ " order by c.desccid";

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			//stm.setInt(1, id_proc);

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				CidBean c = new CidBean();
				c.setIdCid(rs.getInt("cod"));
				c.setDescCid(rs.getString("desccid"));
				c.setCid(rs.getString("cid"));

				lista.add(c);
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
