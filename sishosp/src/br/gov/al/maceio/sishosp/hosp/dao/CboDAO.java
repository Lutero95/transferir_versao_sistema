package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.CboBean;

public class CboDAO {
	
	Connection con = null;
	PreparedStatement ps = null;
	
	public boolean gravarCBO(CboBean cbo) throws SQLException, ProjetoException{
		
		String sql = "insert into hosp.cbo (descricao, codigo) values (?,?);";
		try {
			System.out.println("VAI CADASTRAR CBO");
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, cbo.getDescCbo().toUpperCase());
			ps.setString(2, cbo.getCodigo().toUpperCase());			
			ps.execute();
			con.commit();
			System.out.println("CADASTROU CBO");
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
	
	public List<CboBean> listarCbo() throws ProjetoException{
		List<CboBean> lista = new ArrayList<>();
		String sql = "select id , descricao, codempresa, codigo from hosp.cbo order by descricao ";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
            	CboBean cbo = new CboBean();
            	cbo.setCodCbo(rs.getInt("id"));
            	cbo.setDescCbo(rs.getString("descricao"));    
            	cbo.setCodEmpresa(rs.getInt("codempresa"));
            	cbo.setCodigo(rs.getString("codigo"));                    
                lista.add(cbo);
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
	
	public CboBean listarCboPorId(int id) throws ProjetoException {

		CboBean cbo = new CboBean();
		String sql = "select id , descricao, codempresa, codigo from hosp.cbo where id = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, id);
			ResultSet rs = stm.executeQuery();
			while(rs.next()){
				cbo = new CboBean();
				cbo.setCodCbo(rs.getInt("id"));
	        	cbo.setDescCbo(rs.getString("descricao"));    
	        	cbo.setCodEmpresa(rs.getInt("codempresa"));
	        	cbo.setCodigo(rs.getString("codigo"));    	        	
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
		return cbo;
	}
	
	public List<CboBean> listarCboBusca(String descricao,
			Integer tipo) throws ProjetoException {
		List<CboBean> lista = new ArrayList<>();
		String sql = "select id , descricao, codempresa, codigo from hosp.cbo";
		if (tipo == 1) {
			sql += " where descricao LIKE ? order by descricao";
		}
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setString(1, "%" + descricao.toUpperCase() + "%");
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				System.out.println("next");
				CboBean cbo = new CboBean();
				cbo.setCodCbo(rs.getInt("id"));
				cbo.setDescCbo(rs.getString("descricao"));
				cbo.setCodigo(rs.getString("codigo"));    				
				//cbo.setCodEmpresa(rs.getInt("codempresa")); COD EMPRESA ?

				lista.add(cbo);
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
	
	public Boolean alterarCbo(CboBean cbo) throws ProjetoException {
		String sql = "update hosp.cbo set descricao = ?, codempresa = ?, codigo=? where id = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, cbo.getDescCbo().toUpperCase());
			stmt.setInt(2, 0);//COD EMPRESA ?
			stmt.setString(3, cbo.getCodigo().toUpperCase());			
			stmt.setInt(4, cbo.getCodCbo());
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
	
	public Boolean excluirCbo(CboBean cbo) throws ProjetoException {
		String sql = "delete from hosp.cbo where id = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setLong(1, cbo.getCodCbo());
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
}
