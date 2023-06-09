package br.gov.al.maceio.sishosp.comum.util;

import java.util.ArrayList;
import java.util.List;

public final class VerificadorUtil {

    public static Boolean verificarSeObjetoNulo(Object object) {
        Boolean retorno = true;

        if(object != null){
            retorno = false;
        }

        return retorno;
    }

    public static Boolean verificarSeObjetoNuloOuVazio(Object object) {
        Boolean retorno = true;

        if(object != null && !object.equals("")){
            retorno = false;
        }

        return retorno;
    }

    public static Boolean verificarSeObjetoNuloOuZero(Object object) {
        Boolean retorno = true;

        if((object != null) && (object != null && !object.equals(0))){
            retorno = false;
        }

        return retorno;
    }

    public static Boolean verificarSeListaNuloOuVazia(List<Object> lista) {
        Boolean retorno = true;

        if(lista != null || !lista.isEmpty()){
            retorno = false;
        }

        return retorno;
    }

    public static Boolean verificarSeArrayListNuloOuVazia(ArrayList<Object> lista) {
        Boolean retorno = true;

        if(lista != null || !lista.isEmpty()){
            retorno = false;
        }

        return retorno;
    }
    
    public static Boolean verificarSeObjetoNuloOuMenorQueZero(Object object) {
        Boolean retorno = true;

        if((object != null) && (object != null && Long.valueOf(object.toString()) >= 0)){
            retorno = false;
        }

        return retorno;
    }
    
    public static Boolean verificarSeObjetoNuloOuMenorQueUm(Object object) {
        Boolean retorno = true;

        if((object != null) && (object != null && Long.valueOf(object.toString()) > 0)){
            retorno = false;
        }

        return retorno;
    }

}
