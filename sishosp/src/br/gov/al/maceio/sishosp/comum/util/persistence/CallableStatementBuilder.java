package br.gov.al.maceio.sishosp.comum.util.persistence;

import java.sql.CallableStatement;
import java.sql.SQLException;

public class CallableStatementBuilder extends AbstractStatementBuilder {

    private CallableStatementBuilder(CallableStatement callableStatement) {
        super(callableStatement);
    }

    public static CallableStatementBuilder of(CallableStatement callableStatement) {
        return new CallableStatementBuilder(callableStatement);
    }

    public CallableStatementBuilder comParametroRetorno (Integer tipo) throws SQLException {
        ((CallableStatement) statement).registerOutParameter(++index, tipo);
        return this;
    }

    @Override
    public CallableStatement construir() {
        return (CallableStatement) super.construir();
    }
}
