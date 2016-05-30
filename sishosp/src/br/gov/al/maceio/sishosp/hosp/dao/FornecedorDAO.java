package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.EscolaridadeBean;
import br.gov.al.maceio.sishosp.hosp.model.FornecedorBean;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;

public class FornecedorDAO {
	
	Connection con = null;
	PreparedStatement ps = null;
	
	public boolean gravarFornecedor(FornecedorBean fornecedor) throws SQLException{
		
		String sql = "insert into hosp.fornecedor (descfornecedor) values (?);";
		try {
		
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, fornecedor.getDescFornecedor().toUpperCase());
			
			ps.execute();
			con.commit();
			
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
	
		
	public List<FornecedorBean> listarFornecedores(){
		List<FornecedorBean> lista = new ArrayList<>();
		String sql = "select id_fornecedor, descfornecedor from hosp.fornecedor order by descfornecedor";
        try {
        
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
            	FornecedorBean fornecedor = new FornecedorBean();
                fornecedor.setIdFornecedor(rs.getInt("id_fornecedor"));
                fornecedor.setDescFornecedor(rs.getString("descfornecedor"));    
                 
                
                lista.add(fornecedor);
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
	
	public List<FornecedorBean> listarFornecedorBusca(String descricao,
			Integer tipo) {
		List<FornecedorBean> lista = new ArrayList<>();
		String sql = "select id_fornecedor, descfornecedor from hosp.fornecedor ";
		if (tipo == 1) {
			sql += " where descfornecedor LIKE ?  order by id_fornecedor";
		}
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setString(1, "%" + descricao.toUpperCase() + "%");
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				FornecedorBean fornecedor = new FornecedorBean();
				fornecedor.setIdFornecedor(rs.getInt("id_fornecedor"));
                fornecedor.setDescFornecedor(rs.getString("descfornecedor"));    
                
				lista.add(fornecedor);
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
	
	public Boolean alterarFornecedor(FornecedorBean fornecedor) throws ProjetoException {
		String sql = "update hosp.fornecedor set descfornecedor = ? where id_fornecedor = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, fornecedor.getDescFornecedor().toUpperCase());
			stmt.setInt(2, fornecedor.getIdFornecedor());
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
	
	public Boolean excluirFornecedor(FornecedorBean fornecedor) throws ProjetoException {
		String sql = "delete from hosp.fornecedor where id_fornecedor = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setLong(1, fornecedor.getIdFornecedor());
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
		
	public FornecedorBean listarFornecedorPorId(int id) {

		FornecedorBean fornecedor = new FornecedorBean();
		String sql = "select id_fornecedor, descfornecedor, valor from hosp.fornecedor where id_fornecedor = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, id);
			ResultSet rs = stm.executeQuery();
			while(rs.next()){
				
				fornecedor.setIdFornecedor(rs.getInt("id_fornecedor"));
                fornecedor.setDescFornecedor(rs.getString("descfornecedor"));  
                fornecedor.setValor(rs.getDouble("valor"));
                //programa.setCodFederal(rs.getDouble("codfederal"));
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
		return fornecedor;
	}

}
