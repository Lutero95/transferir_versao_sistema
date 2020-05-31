package br.gov.al.maceio.sishosp.comum.util.persistence;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StatementFactory {

    private StatementFactory() {}

    public static PreparedStatementBuilder criarPreparedStatement(Connection conexao, String sql) throws SQLException {
        PreparedStatement statement = conexao.prepareStatement(sql);
        return PreparedStatementBuilder.of(statement);
    }

    public static CallableStatementBuilder criarCallableStatement(Connection conexao, String sql) throws SQLException {
        CallableStatement statement = conexao.prepareCall(sql);
        return CallableStatementBuilder.of(statement);
    }
}
