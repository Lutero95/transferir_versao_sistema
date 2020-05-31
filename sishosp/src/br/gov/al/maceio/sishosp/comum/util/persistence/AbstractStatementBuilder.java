package br.gov.al.maceio.sishosp.comum.util.persistence;


import br.gov.al.maceio.sishosp.comum.util.builder.Builder;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractStatementBuilder implements Builder<PreparedStatement> {
    public static final String TIPO_SQL_REFERENTE_AO_TIPO_JAVA_INTEGER = "INTEGER";
    public static final String TIPO_SQL_REFERENTE_A_TIPO_JAVA_STRING = "VARCHAR";
    protected PreparedStatement statement;
    protected Integer index;
    protected Map<Class, String> mapaDeTiposSql = new HashMap();

    public AbstractStatementBuilder() {
    }

    public AbstractStatementBuilder(PreparedStatement statement) {
        this.statement = statement;
        this.index = 0;
        this.mapaDeTiposSql.put(Integer.class, "INTEGER");
        this.mapaDeTiposSql.put(String.class, "VARCHAR");
    }

    public <T> AbstractStatementBuilder comFetchSize(int tamanho) {
        try {
            this.statement.setFetchSize(tamanho);
        } catch (SQLException var3) {
            var3.printStackTrace();
        }

        return this;
    }

    public <T> AbstractStatementBuilder comParametros(T... parametros) throws SQLException {
        for(int i = 0; i < parametros.length; ++i) {
            Object parametro = parametros[i];
            if (parametro == null) {
                this.statement.setNull(i + 1, 1111);
            } else {
                this.montarParametrosNaoNulos(i + 1, parametro);
            }
        }

        return this;
    }

    private void montarParametrosNaoNulos(int indiceParametro, Object parametro) throws SQLException {
        if (parametro instanceof Collection) {
            this.montarParametroCollection(indiceParametro, parametro);
        } else if (parametro instanceof Date) {
            this.statement.setObject(indiceParametro, new Timestamp(((Date)parametro).getTime()));
        } else if (parametro instanceof Boolean) {
            this.statement.setBoolean(indiceParametro, (Boolean)parametro);
        } else if (parametro instanceof Time) {
            this.statement.setTime(indiceParametro, (Time)parametro);
        } else if (parametro instanceof Timestamp) {
            this.statement.setTimestamp(indiceParametro, (Timestamp)parametro);
        } else {
            this.statement.setObject(indiceParametro, parametro);
        }

    }

    private void montarParametroCollection(int indiceParametro, Object parametro) throws SQLException {
        Collection colecao = (Collection)parametro;
        if (!colecao.isEmpty()) {
            String tipoSql = this.retornarTipoSqlComBaseNoPrimeiroObjetoDaColecao(colecao);
            this.statement.setObject(indiceParametro, this.statement.getConnection().createArrayOf(tipoSql, colecao.toArray()));
        }

    }

    private String retornarTipoSqlComBaseNoPrimeiroObjetoDaColecao(Collection colecao) {
        return (String)this.mapaDeTiposSql.getOrDefault(colecao.toArray()[0].getClass(), "INTEGER");
    }

    public <T> AbstractStatementBuilder comParametro(T parametro) throws SQLException {
        if (parametro != null) {
            this.statement.setObject(this.index = this.index + 1, parametro);
        } else {
            this.statement.setNull(this.index = this.index + 1, 1111);
        }

        return this;
    }

    public <T> AbstractStatementBuilder comParametroDate(T parametro) throws SQLException {
        if (parametro != null) {
            this.statement.setObject(this.index = this.index + 1, new java.sql.Date(((Date)parametro).getTime()));
        } else {
            this.statement.setNull(this.index = this.index + 1, 1111);
        }

        return this;
    }

    public <T> AbstractStatementBuilder comParametroTime(T parametro) throws SQLException {
        if (parametro != null) {
            this.statement.setObject(this.index = this.index + 1, new Time(((Date)parametro).getTime()));
        } else {
            this.statement.setNull(this.index = this.index + 1, 1111);
        }

        return this;
    }

    public <T> AbstractStatementBuilder comParametroTimeStamp(T parametro) throws SQLException {
        if (parametro != null) {
            this.statement.setObject(this.index = this.index + 1, new Timestamp(((Date)parametro).getTime()));
        } else {
            this.statement.setNull(this.index = this.index + 1, 1111);
        }

        return this;
    }

    public PreparedStatement construir() {
        return this.statement;
    }
}
