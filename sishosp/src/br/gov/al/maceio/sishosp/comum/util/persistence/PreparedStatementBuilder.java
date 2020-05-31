package br.gov.al.maceio.sishosp.comum.util.persistence;

import java.sql.PreparedStatement;

public class PreparedStatementBuilder extends AbstractStatementBuilder {

    private PreparedStatementBuilder(PreparedStatement preparedStatement) {
        super(preparedStatement);
    }

    public static PreparedStatementBuilder of(PreparedStatement preparedStatement) {
        return new PreparedStatementBuilder(preparedStatement);
    }
}
