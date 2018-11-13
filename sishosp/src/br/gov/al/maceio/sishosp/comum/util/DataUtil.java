package br.gov.al.maceio.sishosp.comum.util;

import java.util.Calendar;
import java.util.Date;

public final class DataUtil {

    public static Calendar retornarDataCalendar(Date data) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(data);

        return c1;
    }

}
