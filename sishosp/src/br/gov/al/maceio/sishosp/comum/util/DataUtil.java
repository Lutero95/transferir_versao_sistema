package br.gov.al.maceio.sishosp.comum.util;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

}
