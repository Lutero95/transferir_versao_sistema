package br.gov.al.maceio.sishosp.comum.util;

import java.text.Normalizer;

public final class StringUtil {

    public static String removeAcentos(String str) {

        str = Normalizer.normalize(str, Normalizer.Form.NFD);
        str = str.replaceAll("[^\\p{ASCII}]", "");
        return str;

    }

    public static String quebrarStringPorQuantidade(String texto, Integer numeroInicial, Integer numeroFinal) {

         return texto.substring(numeroInicial, numeroFinal).trim();

    }

}
