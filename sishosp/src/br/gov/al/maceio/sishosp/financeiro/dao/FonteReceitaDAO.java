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
import br.gov.al.maceio.sishosp.financeiro.model.FonteReceitaBean;


public class FonteReceitaDAO {
    
    private Connection conexao = null;
    
    public boolean cadastrarFonteRec(FonteReceitaBean fonte) throws ProjetoException {
        
        String sql = "insert into clin.fonrec(descricao, codfonrec) "
            + "values (?, ?)";
        boolean cadastrou = false;

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement pstm = conexao.prepareStatement(sql);
            pstm.setString(1, fonte.getDescricao().toUpperCase());
            pstm.setString(2, fonte.getCodFonteRec().toUpperCase());
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
    
    public boolean alterarFonteRec(FonteReceitaBean fonte) throws ProjetoException {
        
        String sql = "update clin.fonrec set descricao = ?, codfonrec = ? "
            + "where id = ?";
        boolean alterou = false;

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement pstm = conexao.prepareStatement(sql);
            pstm.setString(1, fonte.getDescricao().toUpperCase());
            pstm.setString(2, fonte.getCodFonteRec().toUpperCase());
            pstm.setInt(3, fonte.getId());
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
    
	public boolean excluirFonteRec(FonteReceitaBean fonteRec) throws ProjetoException {

		String sql = "delete from clin.fonrec where id = ?";
		boolean excluiu = false;

		Connection conexao = null;
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement pstm = conexao.prepareStatement(sql);
			pstm.setInt(1, fonteRec.getId());
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
    
    public List<FonteReceitaBean> listarFontesRec() throws ProjetoException {
    	PreparedStatement ps = null;
		Connection con = ConnectionFactory.getConnection();
        
		List<FonteReceitaBean> colecao = new ArrayList<>();
        try {
        	String sql = "select id, descricao, codfonrec from clin.fonrec   order by descricao";
            
            ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
			FonteReceitaBean fonteReceita = new FonteReceitaBean();
            
            while(rs.next()) {
                fonteReceita = new FonteReceitaBean();
                fonteReceita.setId(rs.getInt("id"));
                fonteReceita.setDescricao(rs.getString("descricao"));
                fonteReceita.setCodFonteRec(rs.getString("codfonrec"));
                colecao.add(fonteReceita);
            }
            
            rs.close();
            ps.close();
            
        } catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
            try {
                con.close();
            } catch (Exception ex) {
                //comentado walter erro log ex.printStackTrace();
                System.exit(1);
            }
        }
        return colecao;
    }
}