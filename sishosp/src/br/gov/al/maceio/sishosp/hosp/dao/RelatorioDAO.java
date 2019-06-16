package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;

public class RelatorioDAO {
    private Connection conexao = null;

    public Boolean popularTabelaTemporariaFrequencia(int chave, int qtdRegistrosInserir) {
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

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return retorno;
        }
    }
    
    public Boolean limparTabelaTemporariaFrequencia(int chave) {
        Boolean retorno = false;
        String sql = "delete from temporaria_frequencia where chave=?";

        try {
            conexao = ConnectionFactory.getConnection();
            
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, chave);
            stmt.execute();
            conexao.commit();
            retorno = true;

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return retorno;
        }
    }

  
}
