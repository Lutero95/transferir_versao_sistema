package br.gov.al.maceio.sishosp.comum.util;

import br.gov.al.maceio.sishosp.hosp.enums.DiasDaSemana;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.joda.time.Interval;

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

    public static Timestamp retornarDataIhHoraAtual(){
        Timestamp dataDeHoje = new Timestamp(System.currentTimeMillis());

        return dataDeHoje;
    }

    public static Date adicionarMesIhAnoEmDate(Integer mes, Integer ano, Boolean ultimoDiaDoMes){

    	LocalDate dataAtual = LocalDate.now();
        
        
        if(!ultimoDiaDoMes){
        	dataAtual = LocalDate.now().withMonth(mes).withYear(ano).with(TemporalAdjusters.lastDayOfMonth());
        }
        else{
        	dataAtual = LocalDate.now().withMonth(mes).withYear(ano).withDayOfMonth(1);
        }

        Date data = Date.from(dataAtual.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return data;
    }
    
    
    
    public static int calculaQuantidadeDiasEntreDuasDatas(long timestamp1, long timestamp2) {
        Interval interval = new Interval(timestamp1, timestamp2);
        Long quantidadeDias = interval.toDurationMillis();
        quantidadeDias = (quantidadeDias / 86400000L)+1;
        return quantidadeDias.intValue();
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

    public static Date montarDataCompleta(Integer dia, Integer mes, Integer ano){

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, (ano));
        cal.set(Calendar.MONTH, ajustarMes(mes));
        cal.set(Calendar.DAY_OF_MONTH, dia);
        Date data = cal.getTime();

        return data;
    }

    public static Date montarDataCompletaInicioMesPorAnoMesCompetencia(String anoMesCompetencia){
        Integer ano = Integer.valueOf(anoMesCompetencia.substring(0,4));
        Integer mes = Integer.valueOf(anoMesCompetencia.substring(4 ,6));
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, (ano));
        cal.set(Calendar.MONTH, ajustarMes(mes));
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date data = cal.getTime();

        return data;
    }

    private static Integer ajustarMes(Integer mes){
        return mes - 1;
    }

    public static String ajustarHorarioParaHoraIhMinuto(String horarioComSegundos){
        if(VerificadorUtil.verificarSeObjetoNuloOuVazio(horarioComSegundos)){
            return null;
        }
        return horarioComSegundos.substring(0, 5);
    }

    public static Date proximaDataRetirandoFinalDeSemana(Date data){

        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        int day = cal.get(Calendar.DAY_OF_WEEK);

        if(day == 1){
            data = adicionarDiasAData(data, 1);
            return data;
        }
        if(day == 7){
            data = adicionarDiasAData(data, 1);
            return data;
        }
        else{
            return null;
        }

    }

    public static int pegarDiaDaSemana(Date data){

        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        int day = cal.get(Calendar.DAY_OF_WEEK);

        return day;
    }
    
	public static Date retornaDataComMesAnterior(Date data) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(data);
		int mes = calendar.get(Calendar.MONTH);
		mes++;
		LocalDate localDate = LocalDate.of(calendar.get(Calendar.YEAR), mes, calendar.get(Calendar.DAY_OF_MONTH));
		localDate = localDate.minusMonths(1);
		data = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		return data;
	}
	
	public static boolean dataEstaEntrePeriodoInformado(Date data, Date dataInicio, Date dataFim) {
		return data.compareTo(dataInicio) >= 0 && data.compareTo(dataFim) <= 0;
	}

}
