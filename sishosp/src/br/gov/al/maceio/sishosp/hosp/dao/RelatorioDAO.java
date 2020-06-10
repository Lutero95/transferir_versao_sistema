package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.TratamentoErrosUtil;

public class RelatorioDAO {
    private Connection conexao = null;

    public Boolean popularTabelaTemporariaFrequencia(int chave, int qtdRegistrosInserir) throws ProjetoException {
        Boolean retorno = false;
        String sql = "insert into temporaria_frequencia (chave) values (?)";

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, chave);
            for (int i = 0; i < qtdRegistrosInserir; i++) 
            stmt.execute();
            conexao.commit();
            retorno = true;
        } catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return retorno;
    }
    
    public Boolean limparTabelaTemporariaFrequencia(int chave) throws ProjetoException {
        Boolean retorno = false;
        String sql = "delete from temporaria_frequencia where chave=?";

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, chave);
            stmt.execute();
            conexao.commit();
            retorno = true;
        } catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return retorno;
    }
}
