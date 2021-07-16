package br.gov.al.maceio.sishosp.financeiro.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import br.gov.al.maceio.sishosp.financeiro.model.BancoBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.TratamentoErrosUtil;
public class BancoDAO {
    
    public boolean cadastrarBanco(BancoBean banco) throws ProjetoException {
        
        String sql = "insert into financeiro.banco(banco, codbanco, agencia, conta, descricao, caixa, ativo) "
            + "values (?, ?, ?, ?, ?, ?, ?)";
        
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
        } catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                //comentado walter erro log ex.printStackTrace();
                System.exit(1);
            }
        }
        return cadastrou;
    }
    
    public boolean alterarBanco(BancoBean banco) throws ProjetoException {
        
        String sql = "update financeiro.banco set banco = ?, codbanco = ?, agencia = ?, conta = ?, "
    		+ "descricao= ?, caixa = ?, ativo = ? where id = ?";
        
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
        } catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                //comentado walter erro log ex.printStackTrace();
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
        } catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                //comentado walter erro log ex.printStackTrace();
                System.exit(1);
            }
        }
        return excluiu;
    }
    
    public List<BancoBean> listarBancos() throws ProjetoException {
        
        String sql = "select id, banco, codbanco, agencia, conta, descricao, caixa, "
            + "codfilial, ativo from financeiro.banco "
            + "order by descricao";

        List<BancoBean> lista = new ArrayList<>();
        
        Connection conexao = null;
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement pstm = conexao.prepareStatement(sql);
            ResultSet rs = pstm.executeQuery();
            
            while(rs.next()) {
                BancoBean banco = new BancoBean();
                banco.setId(rs.getInt("id"));
                banco.setCodbanco(rs.getString("codbanco"));
                banco.setBanco(rs.getString("banco"));
                banco.setAgencia(rs.getString("agencia"));
                banco.setConta(rs.getString("conta"));
                banco.setDescricao(rs.getString("descricao"));
                banco.setContaCaixa(rs.getString("caixa"));
                banco.setAtivo(rs.getString("ativo"));
                
                lista.add(banco);
            }
            
            rs.close();
            pstm.close();
        } catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                //comentado walter erro log ex.printStackTrace();
                System.exit(1);
            }
        }
        return lista;
        
    }
    	public BancoBean buscarBancoPorId(BancoBean bancoParametro) throws ProjetoException {
    		
	        String sql = "select id, banco, codbanco, agencia, conta, descricao, caixa, "
	            + "codfilial, ativo from financeiro.banco where  id = ? "
	            + "order by descricao";
	        	        	
	        	//TesourariaDAO tdao = new TesourariaDAO();
	        Connection conexao = null;
	        BancoBean banco = null;
	        try {
	            conexao = ConnectionFactory.getConnection();
	            PreparedStatement pstm = conexao.prepareStatement(sql);

	            pstm.setInt(1, bancoParametro.getId());
	            
	            ResultSet rs = pstm.executeQuery();

	            while(rs.next()) {
	            	banco = new BancoBean();      
	            	bancoParametro = banco;
	            	
	                banco.setId(rs.getInt("id"));
	                banco.setCodbanco(rs.getString("codbanco"));
	                banco.setBanco(rs.getString("banco"));
	                banco.setAgencia(rs.getString("agencia"));
	                banco.setConta(rs.getString("conta"));
	                banco.setDescricao(rs.getString("descricao"));
	                banco.setContaCaixa(rs.getString("caixa"));
	                banco.setAtivo(rs.getString("ativo"));
	            }
	           
	            rs.close();
	            pstm.close();
	            
        } catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                //comentado walter erro log ex.printStackTrace();
                System.exit(1);
            }
        }
	    return banco;
    }
    
    public List<BancoBean> listarBancosAtivos() throws ProjetoException {
        
        String sql = "select id, banco, codbanco, agencia, conta, descricao, caixa, "
            + "codfilial, ativo from financeiro.banco where   ativo='S' "
            + "order by descricao";
        
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
        } catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                //comentado walter erro log ex.printStackTrace();
                System.exit(1);
            }
        }
        return lista;
    }

    public ArrayList<BancoBean> buscaTodosBancos() throws ProjetoException {

        PreparedStatement ps = null;
        Connection con = ConnectionFactory.getConnection();

        ArrayList<BancoBean> colecao = new ArrayList<BancoBean>();
        try {
            String sql = "select id,banco, agencia,conta, descricao, codfilial, caixa from financeiro.banco where  ativo='S' order by descricao";

            ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            BancoBean banco = new BancoBean();
            while (rs.next()) {
                banco = new BancoBean();
                banco.setId(rs.getInt("id"));
                banco.setBanco(rs.getString("banco"));
                //e.setCodbanco(rs.getString("codbanco"));
                banco.setAgencia(rs.getString("agencia"));
                banco.setConta(rs.getString("conta"));
                banco.setDescricao(rs.getString("descricao"));
                banco.setContaCaixa(rs.getString("caixa"));
                colecao.add(banco);
            }

            ps.close();
            rs.close();

        } catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
            try {
                con.close();
            } catch (Exception sqlc) {
                sqlc.printStackTrace();
                System.exit(1);
            }
        }
        return colecao;
    }

 
    public List<BancoBean> listaBanco() throws ProjetoException {

        Connection con = ConnectionFactory.getConnection();

        String sql = " select id, coalesce(descricao,'') descricao, codfilial from financeiro.banco  ;";
        ResultSet set = null;
        ArrayList<BancoBean> listaBanco = new ArrayList<BancoBean>();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            set = ps.executeQuery();

            while (set.next()) {
                BancoBean banco = new BancoBean();
                banco.setId(set.getInt("id"));
                banco.setDescricao(set.getString("descricao"));
                listaBanco.add(banco);
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
        return listaBanco;
    }
}