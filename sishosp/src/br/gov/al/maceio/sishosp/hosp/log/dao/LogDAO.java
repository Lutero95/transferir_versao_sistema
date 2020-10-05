package br.gov.al.maceio.sishosp.hosp.log.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.TratamentoErrosUtil;
import br.gov.al.maceio.sishosp.hosp.log.model.LogBean;
import br.gov.al.maceio.sishosp.hosp.log.model.LogVisualizacaoBean;

public class LogDAO {
	
    public void gravarLog(LogBean log, Connection conexaoAuxiliar) throws ProjetoException, SQLException {
        String sql = "INSERT INTO log.logs (id_funcionario, datahora, descricao, rotina) VALUES(?, CURRENT_TIMESTAMP, ?, ?);";

        try {
            PreparedStatement ps = conexaoAuxiliar.prepareStatement(sql);
            ps.setLong(1, log.getIdFuncionario());
            ps.setString(2, log.getDescricao());
            ps.setString(3, log.getRotina());
            
            ps.executeUpdate();
        } catch (SQLException sqle) {
        	conexaoAuxiliar.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
        	conexaoAuxiliar.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
    }

    public void gravarLogVisualizacao(LogVisualizacaoBean logVisualizacao) throws ProjetoException, SQLException {
        String sql = "INSERT INTO log.log_visualizacao_dados (id_funcionario, datahora, rotina) VALUES(?, CURRENT_TIMESTAMP, ?);";
        
        Connection conexao = null;
        try {
        	conexao = ConnectionFactory.getConnection();
            PreparedStatement ps = conexao.prepareStatement(sql);
            ps.setLong(1, logVisualizacao.getIdFuncionario());
            ps.setString(2, logVisualizacao.getRotina());
            
            ps.executeUpdate();
            conexao.commit();
        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        }finally {
        	try {
        		conexao.close();				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
    }
}
