package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.CidBean;


public class CidDAO {
	
	Connection con = null;
	PreparedStatement ps = null;
	
	public boolean gravarCid(CidBean cid) throws SQLException{
		
		String sql = "insert into hosp.cid (desccid) values (?);";
		try {
			System.out.println("VAI CADASTRAR Cid");
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, cid.getDescCid().toUpperCase());
			ps.execute();
			con.commit();
			System.out.println("CADASTROU grupo");
			return true;
		} catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.exit(1);
            }
        }
	}

	
	public List<CidBean> listarCid(){
		List<CidBean> lista = new ArrayList<>();
		String sql = "select cod, desccid from hosp.cid order by cod";
        try {
        	System.out.println("CHEGOU AQUI");
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
            	CidBean cid = new CidBean();
            	cid.setIdCid(rs.getInt("cod"));
            	cid.setDescCid(rs.getString("desccid"));    
                
                
                lista.add(cid);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.exit(1);
            }
        }
		return lista;
	}
	
	public List<CidBean> listarCidBusca(String descricao,
			Integer tipo) {
		List<CidBean> lista = new ArrayList<>();
		String sql = "select cod, desccid from hosp.cid ";
		if (tipo == 1) {
			sql += " where desccid LIKE ?  order by cod";
		}
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setString(1, "%" + descricao.toUpperCase() + "%");
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				CidBean cid = new CidBean();
				cid.setIdCid(rs.getInt("cod"));
				cid.setDescCid(rs.getString("desccid"));
				

				lista.add(cid);
			}
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				System.exit(1);
			}
		}
		
		return lista;
	}
	
	public Boolean alterarCid(CidBean cid) throws ProjetoException {
		String sql = "update hosp.cid set desccid = ? where cod = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, cid.getDescCid().toUpperCase());
			stmt.setInt(2, cid.getIdCid());
			stmt.executeUpdate();
			con.commit();
			return true;
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	public Boolean excluirCid(CidBean cid) throws ProjetoException {
		String sql = "delete from hosp.cid where cod = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setLong(1, cid.getIdCid());
			stmt.execute();
			con.commit();
			return true;
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	public CidBean buscaCidPorId (Integer i) throws ProjetoException {
		String sql = "select cod, desccid from hosp.cid where cod =? order by cod";
		try {
			
			ps = con.prepareStatement(sql);
			ps.setInt(1, i);
			ResultSet rs = ps.executeQuery();
			CidBean g = new CidBean();
			while (rs.next()) {
				g.setIdCid(rs.getInt("cod"));
				g.setDescCid(rs.getString("desccid"));
			
			}
			
			return g;
		} catch (Exception sqle) {

			throw new ProjetoException(sqle);

		} finally {
			try {
				con.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
				System.exit(1);
				// TODO: handle exception
			}

		}
	}	
	
	

}
