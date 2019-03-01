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

    public static String retornarHorarioEmString(Date tempo) throws ParseException {
        String horario = null;

        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        horario = dateFormat.format(tempo);

        return horario;
    }

}
