package br.gov.al.maceio.sishosp.comum.util;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public final class DataUtil {

    public static Calendar retornarDataCalendar(Date data) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(data);

        return c1;
    }

    public static String mesIhAnoAtual() {
        Date data = new Date();
        Locale local = new Locale("pt", "BR");
        DateFormat dateFormat = new SimpleDateFormat("MMMM ' - ' yyyy", local);

        String dataAtual = dateFormat.format(data);

        return dataAtual;
    }

    public static Time transformarDateEmTime(Date data) {
        return new java.sql.Time(data.getTime());
    }

    public static String retornarHorarioEmString(Date tempo) {
        String horario = null;

        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        horario = dateFormat.format(tempo);

        return horario;
    }

    public static Time retornarHorarioEmTime(String tempo) {

        SimpleDateFormat formatador = new SimpleDateFormat("HH:mm");
        Date data = null;
        try {
            data = formatador.parse(tempo);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Time time = new Time(data.getTime());

        return time;
    }

    public static java.sql.Date converterDateUtilParaDateSql(Date dataUtil) {
        java.sql.Date dataSql = new java.sql.Date(dataUtil.getTime());

        return dataSql;
    }

    public static Integer extrairAnoDeData(Date data) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        int ano = cal.get(Calendar.YEAR);

        return ano;
    }

    public static Integer extrairDiaDeData(Date data) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        int diaSemana = cal.get(Calendar.DAY_OF_WEEK);

        return diaSemana;
    }

    public static Integer extrairMesDeData(Date data) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        int mes = cal.get(Calendar.MONTH) + 1;

        return mes;
    }

    public static Date adicionarDiasAData(Date data, Integer qtdDias){

        Calendar c = Calendar.getInstance();
        c.setTime(data);
        c.add(Calendar.DATE, +qtdDias);

        data = c.getTime();

        return data;
    }

    public static Date retornarDataAtual(){
        Calendar calendar = new GregorianCalendar();
        Date date = new Date();
        calendar.setTime(date);
        return calendar.getTime();
    }

    public static Date adicionarMesIhAnoEmDate(Integer mes, Integer ano, Boolean ultimoDiaDoMes){

        int dia = 0;
        mes = mes - 1;

        Date data = new Date();
        data.setMonth(mes);
        data.setYear(ano - 1900);

        if(!ultimoDiaDoMes){
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.MONTH, mes);
            dia = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        }
        else{
            dia = 1;
        }

        data.setDate(dia);


        return data;
    }

    public static Long calcularQuantidadeDeDiasDeUmaData(Date data){
        final Long VALOR_DIA_EM_LONG =  86400000L;
        Long quantidadeDeDias = data.getTime() / VALOR_DIA_EM_LONG;

        return quantidadeDeDias;
    }

    public static String mostraMesPorExtenso(Integer mes) {
        String mesPorExtenso = "";

        switch (mes) {
            case 1:
                mesPorExtenso = "Janeiro";
                break;
            case 2:
                mesPorExtenso = "Fevereiro";
                break;
            case 3:
                mesPorExtenso = "Março";
                break;
            case 4:
                mesPorExtenso = "Abril";
                break;
            case 5:
                mesPorExtenso = "Maio";
                break;
            case 6:
                mesPorExtenso = "Junho";
                break;
            case 7:
                mesPorExtenso = "Julho";
                break;
            case 8:
                mesPorExtenso = "Agosto";
                break;
            case 9:
                mesPorExtenso = "Setembro";
                break;
            case 10:
                mesPorExtenso = "Outubro";
                break;
            case 11:
                mesPorExtenso = "Novembro";
                break;
            case 12:
                mesPorExtenso = "Dezembro";
                break;
            default:
                mesPorExtenso = "Erro";

        }
        return mesPorExtenso;
    }

}
