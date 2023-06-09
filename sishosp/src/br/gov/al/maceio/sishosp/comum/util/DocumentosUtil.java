package br.gov.al.maceio.sishosp.comum.util;

import java.util.InputMismatchException;

public final class DocumentosUtil {

    public static Boolean validaCNS(String s) {
        Boolean retorno = false;

        if (s.matches("[1-2]\\d{10}00[0-1]\\d") || s.matches("[7-9]\\d{14}")) {
            if (somaPonderadaCNS(s) % 11 == 0) {
                retorno = true;
            }
        }
        return retorno;
    }

    private static int somaPonderadaCNS(String s) {
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
    
    public static Boolean validaCNPJ(String CNPJ) {
    	// considera-se erro CNPJ's formados por uma sequencia de numeros iguais
        if (CNPJ.equals("00000000000000") || CNPJ.equals("11111111111111") ||
            CNPJ.equals("22222222222222") || CNPJ.equals("33333333333333") ||
            CNPJ.equals("44444444444444") || CNPJ.equals("55555555555555") ||
            CNPJ.equals("66666666666666") || CNPJ.equals("77777777777777") ||
            CNPJ.equals("88888888888888") || CNPJ.equals("99999999999999") ||
           (CNPJ.length() != 14))
           return(false);
     
        char dig13, dig14;
        int sm, i, r, num, peso;
     
    // "try" - protege o código para eventuais erros de conversao de tipo (int)
        try {
    // Calculo do 1o. Digito Verificador
          sm = 0;
          peso = 2;
          for (i=11; i>=0; i--) {
    // converte o i-ésimo caractere do CNPJ em um número:
    // por exemplo, transforma o caractere '0' no inteiro 0
    // (48 eh a posição de '0' na tabela ASCII)
            num = (int)(CNPJ.charAt(i) - 48);
            sm = sm + (num * peso);
            peso = peso + 1;
            if (peso == 10)
               peso = 2;
          }
     
          r = sm % 11;
          if ((r == 0) || (r == 1))
             dig13 = '0';
          else dig13 = (char)((11-r) + 48);
     
    // Calculo do 2o. Digito Verificador
          sm = 0;
          peso = 2;
          for (i=12; i>=0; i--) {
            num = (int)(CNPJ.charAt(i)- 48);
            sm = sm + (num * peso);
            peso = peso + 1;
            if (peso == 10)
               peso = 2;
          }
     
          r = sm % 11;
          if ((r == 0) || (r == 1))
             dig14 = '0';
          else dig14 = (char)((11-r) + 48);
     
    // Verifica se os dígitos calculados conferem com os dígitos informados.
          if ((dig13 == CNPJ.charAt(12)) && (dig14 == CNPJ.charAt(13)))
             return(true);
          else return(false);
        } catch (InputMismatchException erro) {
            return(false);
        }	
        
    }

}
