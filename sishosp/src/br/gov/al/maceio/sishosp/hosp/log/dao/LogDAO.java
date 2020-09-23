package br.gov.al.maceio.sishosp.hosp.log.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.TratamentoErrosUtil;
import br.gov.al.maceio.sishosp.hosp.log.model.LogBean;

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

}
