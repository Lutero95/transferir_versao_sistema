package br.gov.al.maceio.sishosp.comum.util;


public final class DocumentosUtil {

    public static Boolean validaCns(String s) {
        Boolean retorno = false;

        if (s.matches("[1-2]\\d{10}00[0-1]\\d") || s.matches("[7-9]\\d{14}")) {
            if (somaPonderadaCns(s) % 11 == 0) {
                retorno = true;
            }
        }
        return retorno;
    }

    private static int somaPonderadaCns(String s) {
        char[] cs = s.toCharArray();
        int soma = 0;
        for (int i = 0; i < cs.length; i++) {
            soma += Character.digit(cs[i], 10) * (15 - i);
        }
        return soma;
    }

    public static Boolean validaPIS(String pis) {
        boolean retorno = true;

        if (!pis.equals("")) {
            int liTamanho = 0;
            StringBuffer lsAux = null;
            StringBuffer lsMultiplicador = new StringBuffer("3298765432");
            int liTotalizador = 0;
            int liResto = 0;
            int liMultiplicando = 0;
            int liMultiplicador = 0;
            int liDigito = 99;
            lsAux = new StringBuffer().append(pis);
            liTamanho = lsAux.length();
            if (liTamanho != 11) {
                retorno = false;
            }
            if (retorno) {
                for (int i = 0; i < 10; i++) {
                    liMultiplicando = Integer.parseInt(lsAux.substring(i, i + 1));
                    liMultiplicador = Integer.parseInt(lsMultiplicador.substring(i, i + 1));
                    liTotalizador += liMultiplicando * liMultiplicador;
                }
                liResto = 11 - liTotalizador % 11;
                liResto = liResto == 10 || liResto == 11 ? 0 : liResto;
                liDigito = Integer.parseInt("" + lsAux.charAt(10));
                retorno = liResto == liDigito;
            }
        }
        return retorno;
    }

}
