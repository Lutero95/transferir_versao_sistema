package br.gov.al.maceio.sishosp.comum.util;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.List;

import static org.apache.commons.lang3.builder.ReflectionToStringBuilder.toStringExclude;
import static org.apache.commons.lang3.builder.ToStringStyle.DEFAULT_STYLE;

public class Utils {

    private Utils() {}

    public static <T> T obterPrimeiroElemento(List<T> lista) {
        return lista != null && !lista.isEmpty() ? lista.get(0) : null;
    }

    public static String toStringBuilder(Object obj) {
        ReflectionToStringBuilder.setDefaultStyle(DEFAULT_STYLE);
        return toStringExclude(obj);
    }
}
