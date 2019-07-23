package br.gov.al.maceio.sishosp.financeiro.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.financeiro.model.BancoBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
public class BancoDAO {
    
    public boolean cadastrarBanco(BancoBean banco) throws ProjetoException {
        
        String sql = "insert into financeiro.banco(banco, codbanco, agencia, conta, descricao, caixa, ativo) "
            + "values (?, ?, ?, ?, ?, ?, ?)";
        
        FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance()
            .getExternalContext().getSessionMap().get("obj_usuario");
        
        boolean cadastrou = false;
        
        Connection conexao = null;
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement pstm = conexao.prepareStatement(sql);
            pstm.setString(1, banco.getBanco().toUpperCase());
            pstm.setString(2, banco.getCodbanco().toUpperCase());
            pstm.setString(3, banco.getAgencia().toUpperCase());
            pstm.setString(4, banco.getConta().toUpperCase());
            pstm.setString(5, banco.getDescricao().toUpperCase());
            pstm.setString(6, banco.getContaCaixa().toUpperCase());
            pstm.setString(7, "S");
            pstm.execute();
            
            conexao.commit();
            
            cadastrou = true;
            
            pstm.close();
        } catch(Exception ex) {
            
            throw new RuntimeException(ex);
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.exit(1);
            }
        }
        return cadastrou;
    }
    
    public boolean alterarBanco(BancoBean banco) throws ProjetoException {
        
        String sql = "update financeiro.banco set banco = ?, codbanco = ?, agencia = ?, conta = ?, "
    		+ "descricao= ?, caixa = ?, ativo = ? where id = ?";
        
        FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance()
            .getExternalContext().getSessionMap().get("obj_usuario");
        
        boolean alterou = false;
        
        Connection conexao = null;
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement pstm = conexao.prepareStatement(sql);
            pstm.setString(1, banco.getBanco().toUpperCase());
            pstm.setString(2, banco.getCodbanco().toUpperCase());
            pstm.setString(3, banco.getAgencia().toUpperCase());
            pstm.setString(4, banco.getConta().toUpperCase());
            pstm.setString(5, banco.getDescricao().toUpperCase());
            pstm.setString(6, banco.getContaCaixa().toUpperCase());
            pstm.setString(7, banco.getAtivo().toUpperCase());
            pstm.setInt(8, banco.getId());
            pstm.execute();
            
            conexao.commit();
            
            alterou = true;
            
            pstm.close();
        } catch(Exception ex) {
            
            throw new RuntimeException(ex);
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.exit(1);
            }
        }
        return alterou;
    }
    
    public boolean excluirBanco(BancoBean banco) throws ProjetoException {

        String sql = "delete from financeiro.banco where id = ?";

        boolean excluiu = false;

        Connection conexao = null;
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement pstm = conexao.prepareStatement(sql);
            pstm.setInt(1, banco.getId());
            pstm.execute();

            conexao.commit();

            excluiu = true;
            
            pstm.close();
        } catch(Exception ex) {
            throw new ProjetoException(ex);
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.exit(1);
            }
        }
        return excluiu;
    }
    
    public List<BancoBean> listarBancos() throws ProjetoException {
        
        String sql = "select id, banco, codbanco, agencia, conta, descricao, caixa, "
            + "codfilial, ativo from financeiro.banco "
            + "order by descricao";
        
        FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance()
            .getExternalContext().getSessionMap().get("obj_usuario");

        List<BancoBean> lista = new ArrayList<>();
        
        Connection conexao = null;
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement pstm = conexao.prepareStatement(sql);
            ResultSet rs = pstm.executeQuery();
            
            while(rs.next()) {
                BancoBean b = new BancoBean();
                b.setId(rs.getInt("id"));
                b.setCodbanco(rs.getString("codbanco"));
                b.setBanco(rs.getString("banco"));
                b.setAgencia(rs.getString("agencia"));
                b.setConta(rs.getString("conta"));
                b.setDescricao(rs.getString("descricao"));
                b.setContaCaixa(rs.getString("caixa"));
                b.setAtivo(rs.getString("ativo"));
                
                lista.add(b);
            }
            
            rs.close();
            pstm.close();
        } catch(Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.exit(1);
            }
        }
        return lista;
        
    }	// metodo que pega dados JO�O W.
    	public BancoBean buscarBancoPorId(BancoBean idBancoBean) throws ProjetoException {
    		
	        String sql = "select id, banco, codbanco, agencia, conta, descricao, caixa, "
	            + "codfilial, ativo from financeiro.banco where  id = ? "
	            + "order by descricao";
	        
	        FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance()
	            .getExternalContext().getSessionMap().get("obj_usuario");
	        	        	
	        	//TesourariaDAO tdao = new TesourariaDAO();
	        Connection conexao = null;
	        try {
	            conexao = ConnectionFactory.getConnection();
	            PreparedStatement pstm = conexao.prepareStatement(sql);

	            pstm.setInt(1, idBancoBean.getId());
	            
	            ResultSet rs = pstm.executeQuery();
	            
	            BancoBean b = null;

	            while(rs.next()) {
	            	b = new BancoBean();      
	            	idBancoBean = b;
	            	
	                b.setId(rs.getInt("id"));
	                b.setCodbanco(rs.getString("codbanco"));
	                b.setBanco(rs.getString("banco"));
	                b.setAgencia(rs.getString("agencia"));
	                b.setConta(rs.getString("conta"));
	                b.setDescricao(rs.getString("descricao"));
	                b.setContaCaixa(rs.getString("caixa"));
	                b.setAtivo(rs.getString("ativo"));

	               
	               
	            }
	           
	            rs.close();
	            pstm.close();
	            
	            return b;
        } catch(Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.exit(1);
            }
        }
        
		
    }
    
    public List<BancoBean> listarBancosAtivos() throws ProjetoException {
        
        String sql = "select id, banco, codbanco, agencia, conta, descricao, caixa, "
            + "codfilial, ativo from financeiro.banco where   ativo='S' "
            + "order by descricao";
        
        FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance()
            .getExternalContext().getSessionMap().get("obj_usuario");

        List<BancoBean> lista = new ArrayList<>();
        
        Connection conexao = null;
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement pstm = conexao.prepareStatement(sql);
            ResultSet rs = pstm.executeQuery();
            
            while(rs.next()) {
                BancoBean b = new BancoBean();
                b.setId(rs.getInt("id"));
                b.setCodbanco(rs.getString("codbanco"));
                b.setBanco(rs.getString("banco"));
                b.setAgencia(rs.getString("agencia"));
                b.setConta(rs.getString("conta"));
                b.setDescricao(rs.getString("descricao"));
                b.setContaCaixa(rs.getString("caixa"));
                b.setAtivo(rs.getString("ativo"));
                
                lista.add(b);
            }
            
            pstm.close();
            rs.close();
        } catch(Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.exit(1);
            }
        }
        return lista;
    }

    public ArrayList<BancoBean> buscaTodosBancos() throws ProjetoException {

        PreparedStatement ps = null;
        Connection con = ConnectionFactory.getConnection();

        try {
            String sql = "select id,banco, agencia,conta, descricao, codfilial, caixa from financeiro.banco where  ativo='S' order by descricao";

            FuncionarioBean user_session = (FuncionarioBean) FacesContext
                    .getCurrentInstance().getExternalContext().getSessionMap()
                    .get("obj_usuario");

            ps = con.prepareStatement(sql);
            

            ResultSet rs = ps.executeQuery();
            
            BancoBean e = new BancoBean();
            ArrayList<BancoBean> colecao = new ArrayList<BancoBean>();
            while (rs.next()) {
            
                e = new BancoBean();
                e.setId(rs.getInt("id"));
                e.setBanco(rs.getString("banco"));
                //e.setCodbanco(rs.getString("codbanco"));
                e.setAgencia(rs.getString("agencia"));
                e.setConta(rs.getString("conta"));
                e.setDescricao(rs.getString("descricao"));
                e.setContaCaixa(rs.getString("caixa"));
                colecao.add(e);

            }

            ps.close();
            rs.close();
            return colecao;

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

 
    public List<BancoBean> lstBanco() throws ProjetoException {

        Connection con = ConnectionFactory.getConnection();

        FuncionarioBean user_session = (FuncionarioBean) FacesContext
                .getCurrentInstance().getExternalContext().getSessionMap()
                .get("obj_usuario");

        String sql = " select id, coalesce(descricao,'') descricao, codfilial from financeiro.banco  ;";
        ResultSet set = null;
        ArrayList<BancoBean> lst = new ArrayList<BancoBean>();
        try {

            PreparedStatement ps = con.prepareStatement(sql);
            set = ps.executeQuery();

            while (set.next()) {
                BancoBean t = new BancoBean();

                t.setId(set.getInt("id"));
                t.setDescricao(set.getString("descricao"));
                
                lst.add(t);
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