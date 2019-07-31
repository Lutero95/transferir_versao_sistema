package br.gov.al.maceio.sishosp.comum.util;

import br.gov.al.maceio.sishosp.hosp.enums.DiasDaSemana;

import java.util.List;

public final class ConfiguracaoAgendaDiasUtil {

    public static String retornarDiasDeAtendimentoPorExtenso(List<String> listaDias) {
        StringBuilder dias = new StringBuilder();

        for (int i = 0; i < listaDias.size(); i++) {

            if (DiasDaSemana.DOMINGO.getSigla().equals(listaDias.get(i))) {
                dias.append("Domingo");

                if (listaDias.size() > 1 && listaDias.size() != i + 1) {
                    dias.append(", ");
                }
            }

            if (DiasDaSemana.SEGUNDA.getSigla().equals(listaDias.get(i))) {
                dias.append("Segunda");
                if (listaDias.size() > 1 && listaDias.size() != i + 1) dias.append(", ");
            }

            if (DiasDaSemana.TERCA.getSigla().equals(listaDias.get(i))) {
                dias.append("Terça");
                if (listaDias.size() > 1 && listaDias.size() != i + 1) {
                    dias.append(", ");
                }
            }

            if (DiasDaSemana.QUARTA.getSigla().equals(listaDias.get(i))) {
                dias.append("Quarta");
                if (listaDias.size() > 1 && listaDias.size() != i + 1) {
                    dias.append(", ");
                }
            }

            if (DiasDaSemana.QUINTA.getSigla().equals(listaDias.get(i))) {
                dias.append("Quinta");
                if (listaDias.size() > 1 && listaDias.size() != i + 1) {
                    dias.append(", ");
                }
            }

            if (DiasDaSemana.SEXTA.getSigla().equals(listaDias.get(i))) {
                dias.append("Sexta");
                if (listaDias.size() > 1 && listaDias.size() != i + 1) {
                    dias.append(", ");
                }
            }

            if (DiasDaSemana.SABADO.getSigla().equals(listaDias.get(i))) {
                dias.append("Sábado");
                if (listaDias.size() > 1 && listaDias.size() != i + 1) {
                    dias.append(", ");
                }
            }

        }

        if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(dias)){
            dias.append(".");
        }

        return dias.toString();

    }

}
