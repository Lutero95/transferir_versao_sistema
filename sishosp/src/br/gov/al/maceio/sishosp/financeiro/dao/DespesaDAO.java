package br.gov.al.maceio.sishosp.financeiro.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.financeiro.model.DespesaBean;
import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;

public class DespesaDAO {
    
    private Connection conexao = null;
    
    public boolean cadastrarDespesa(DespesaBean despesa) throws ProjetoException {
        
        String sql = "insert into financeiro.despesa(descricao) "
            + "values (?)";
        
        FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance()
            .getExternalContext().getSessionMap().get("obj_usuario");
        
        boolean cadastrou = false;
        
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement pstm = conexao.prepareStatement(sql);
            pstm.setString(1, despesa.getDescricao().toUpperCase());
            pstm.execute();
            
            conexao.commit();
            
            cadastrou = true;
            
            pstm.close();
        } catch(SQLException ex) {
            
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
    
    public boolean alterarDespesa(DespesaBean despesa) throws ProjetoException {
        
        String sql = "update financeiro.despesa set descricao = ?  "
            + "where iddespesa = ?";
        
        FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance()
            .getExternalContext().getSessionMap().get("obj_usuario");
        
        boolean alterou = false;
        
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement pstm = conexao.prepareStatement(sql);
            pstm.setString(1, despesa.getDescricao().toUpperCase());
            pstm.setInt(2, despesa.getId());
            pstm.execute();
            
            conexao.commit();
            
            alterou = true;
            
            pstm.close();
        } catch(SQLException ex) {
            
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
    
        public boolean excluirDespesa(DespesaBean despesa) throws ProjetoException {

        String sql = "delete from financeiro.despesa where iddespesa = ?";

        boolean excluiu = false;

        Connection conexao = null;
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement pstm = conexao.prepareStatement(sql);
            pstm.setInt(1, despesa.getId());
            pstm.execute();

            conexao.commit();

            excluiu = true;
            
            pstm.close();
        } catch(SQLException ex) {           
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
    
    public ArrayList<DespesaBean> listarDespesas() throws ProjetoException {
        
        String sql = "select iddespesa, descricao "
            + "from financeiro.despesa  order by descricao";
        
        FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance()
            .getExternalContext().getSessionMap().get("obj_usuario");

        ArrayList<DespesaBean> lista = new ArrayList<>();
        
        Connection conexao = null;
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement pstm = conexao.prepareStatement(sql);
            ResultSet rs = pstm.executeQuery();
            
            while(rs.next()) {
                DespesaBean d = new DespesaBean();
                d.setId(rs.getInt("iddespesa"));
                d.setDescricao(rs.getString("descricao"));
                
                lista.add(d);
            }
            
            rs.close();
            pstm.close();
        } catch(SQLException ex) {
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
}