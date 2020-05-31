package br.gov.al.maceio.sishosp.comum.util.builder;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class BeanBuilder<T> implements Builder<T> {

    public T construir() {
        return getBean();
    }

    public abstract BeanBuilder<T> mapear(ResultSet resultSet) throws SQLException;

    protected abstract T getBean();
}
