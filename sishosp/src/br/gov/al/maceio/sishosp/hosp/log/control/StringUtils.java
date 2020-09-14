package br.gov.al.maceio.sishosp.hosp.log.control;

import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;


public class StringUtils extends org.apache.commons.lang3.StringUtils {
    
    public static final String SIM = "SIM";
    public static final String NAO = "NAO";

    public static String removerDaStringFormatacoesInformadas(
            String stringComFormatacao, String... formatacoes) {
        for (String formatacao : formatacoes) {
            stringComFormatacao = remove(stringComFormatacao, formatacao);
        }
        return stringComFormatacao;
    }

    public static Boolean isNotEqualsIgnoreCase(String texto, String stringIgual) {
        return !equalsIgnoreCase(texto, stringIgual);
    }

    public static Long valueLong(String valor) {
        return !VerificadorUtil.verificarSeObjetoNuloOuVazio(valor) ? Long.parseLong(valor) : null;
    }

    public static Long valueLong(final Object valor) {

        return !VerificadorUtil.verificarSeObjetoNuloOuVazio(valor) ? Long
                .parseLong(valor.toString()) : null;
    }

    public static char valueChar(String valor) {
        return !VerificadorUtil.verificarSeObjetoNuloOuVazio(valor) ? valor.charAt(0) : null;
    }

    public static Integer valueInt(String valor) {
        return !VerificadorUtil.verificarSeObjetoNuloOuVazio(valor) ? Integer
                .parseInt(valor) : null;
    }

    public static Integer valueInt(final Object valor) {

        return !VerificadorUtil.verificarSeObjetoNuloOuVazio(valor) ? Integer
                .parseInt(valor.toString()) : null;

    }

    public static Date valueDate(String valor) {
        return !VerificadorUtil.verificarSeObjetoNuloOuVazio(valor) ? parseDate(valor)
                : null;
    }

    public static String valueString(final Object valor) {

        return !VerificadorUtil.verificarSeObjetoNuloOuVazio(valor) ? valor
                .toString() : null;

    }

    public static String removerAcentos(String name) {
        name = Normalizer.normalize(name, Normalizer.Form.NFD);
        name = name.replaceAll("[^\\p{ASCII}]", "");
        return name;
    }

    public static Boolean valueBoolean(String indicadorAtivo) {
        return !VerificadorUtil.verificarSeObjetoNuloOuVazio(indicadorAtivo) ? parseBoolean(indicadorAtivo)
                : null;
    }

    public static Boolean valueBoolean(final Object indicadorAtivo) {

        return !VerificadorUtil.verificarSeObjetoNuloOuVazio(indicadorAtivo) ? Boolean
                .parseBoolean(indicadorAtivo.toString()) : null;
    }

    private static Boolean parseBoolean(String indicadorAtivo) {
        if (SIM.equalsIgnoreCase(indicadorAtivo))
            return true;
        if (NAO.equalsIgnoreCase(removerAcentos(indicadorAtivo)))
            return false;
        return null;
    }

    private static Date parseDate(String dataString) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return format.parse(dataString);
        } catch (ParseException e) {
            throw new RuntimeException(
                    "Não foi possível converter String data para Date");
        }
    }

    public static String parseDate(Date data) {
        return new SimpleDateFormat("dd/MM/yyyy").format(data);
    }

    public static String anoCorrente() {
        return new SimpleDateFormat("yyyy").format(new Date());
    }

    public static String toUpperCase(String valor) {
        if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(valor)) {
            valor = valor.toUpperCase();
        }
        return valor;
    }

    public static String reduzirTamanhoTexto(String texto, Integer tamanhoTexto) {
        if (texto.length() > tamanhoTexto) {
            texto = texto.trim().substring(0, tamanhoTexto) + "...";
        }
        return texto;
    }

    public static Boolean comparacaoStringComTratamento(String valor1, String valor2) {
        return valor1.trim().toUpperCase().equals(valor2.trim().toUpperCase());
    }

    public static String retornarTratamentoUpperTrim(Object valor) {
        return VerificadorUtil.verificarSeObjetoNuloOuVazio(valor) ? null : valor.toString().trim().toUpperCase();
    }

    public static String retirarZeroEsq(String valor) {
        valor = valor.trim();
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < valor.trim().length(); i++) {
            if (!valor.trim().substring(i, i + 1).equals("0")) {
                temp.append(valor.trim(), i, valor.length());
                break;
            }
        }
        return temp.toString();
    }

    public static String removerUltimoCaractereDeUmaString(String str) {
        if (str != null && str.length() > 0) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    public static String removerMascara(String valorComMascara) {
        return valorComMascara.replaceAll("\\D*", "");
    }

    public static String removerMascaraTratamentoNulo(String valor){
        return VerificadorUtil.verificarSeObjetoNuloOuVazio(valor) ? null : removerMascara(valor);
    }

    public static char retornarPrimeiroCaracter(String valor){
        char[] valorArrayChar = valor.trim().toUpperCase().toCharArray();
        return valorArrayChar[0];
    }

}