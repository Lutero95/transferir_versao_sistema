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

    public static Boolean validaCPF(String CPF) {
        // considera-se erro CPF's formados por uma sequencia de numeros iguais
        if (CPF.equals("00000000000") ||
                CPF.equals("11111111111") ||
                CPF.equals("22222222222") || CPF.equals("33333333333") ||
                CPF.equals("44444444444") || CPF.equals("55555555555") ||
                CPF.equals("66666666666") || CPF.equals("77777777777") ||
                CPF.equals("88888888888") || CPF.equals("99999999999") ||
                (CPF.length() != 11))
            return(false);

        char dig10, dig11;
        int sm, i, r, num, peso;

            // Calculo do 1o. Digito Verificador
            sm = 0;
            peso = 10;
            for (i=0; i<9; i++) {
                // converte o i-esimo caractere do CPF em um numero:
                // por exemplo, transforma o caractere '0' no inteiro 0
                // (48 eh a posicao de '0' na tabela ASCII)
                num = (int)(CPF.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig10 = '0';
            else dig10 = (char)(r + 48); // converte no respectivo caractere numerico

            // Calculo do 2o. Digito Verificador
            sm = 0;
            peso = 11;
            for(i=0; i<10; i++) {
                num = (int)(CPF.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig11 = '0';
            else dig11 = (char)(r + 48);

            // Verifica se os digitos calculados conferem com os digitos informados.
            if ((dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10)))
                return(true);
            else return(false);
    }

}
