package br.gov.al.maceio.sishosp.comum.util.persistence;


import br.gov.al.maceio.sishosp.comum.util.Utils;
import br.gov.al.maceio.sishosp.comum.util.builder.BeanBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MapeamentoUtils {

    private MapeamentoUtils() {}

    public static <T, B extends BeanBuilder<T>> T obterBean(ResultSet resultSet, Class<B> builderClass) throws SQLException, ReflectiveOperationException {
        List<T> beans = obterListaDeBeans(resultSet, builderClass);
        return Utils.obterPrimeiroElemento(beans);
    }

    public static <T, B extends BeanBuilder<T>> List<T> obterListaDeBeans(ResultSet resultSet, Class<B> builderClass) throws SQLException, ReflectiveOperationException {
        List<T> listaDeBeans = new ArrayList<>();
        while (resultSet.next()) {
            B builder = builderClass.newInstance();
            T bean = builder.mapear(resultSet).construir();
            listaDeBeans.add(bean);
        }
        return listaDeBeans;
    }
}
