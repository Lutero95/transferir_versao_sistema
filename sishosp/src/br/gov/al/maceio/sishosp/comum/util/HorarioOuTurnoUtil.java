package br.gov.al.maceio.sishosp.comum.util;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.EmpresaDAO;
import br.gov.al.maceio.sishosp.hosp.dao.UnidadeDAO;
import br.gov.al.maceio.sishosp.hosp.enums.OpcaoAtendimento;
import br.gov.al.maceio.sishosp.hosp.model.ParametroBean;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public final class HorarioOuTurnoUtil {

    public static String retornarOpcaoAtendimentoUnidade() throws ProjetoException {
        	 UnidadeDAO unidadeDAO = new UnidadeDAO();

        String opcaoAtendimento = unidadeDAO.carregarOpcaoAtendimentoDaUnidade();

        opcaoAtendimento = !opcaoAtendimento.equals(OpcaoAtendimento.AMBOS.getSigla()) ? opcaoAtendimento : OpcaoAtendimento.SOMENTE_TURNO.getSigla();

        return opcaoAtendimento;
    }

    public static ArrayList<String> gerarHorariosAtendimento() throws ParseException, ProjetoException {
        ParametroBean parametroBean = new ParametroBean();
        ArrayList<String> listaHorarios = new ArrayList<>();
        UnidadeDAO unidadeDAO = new UnidadeDAO();

        parametroBean = unidadeDAO.carregarDetalhesAtendimentoDaUnidade();

        java.sql.Time novahora = (Time) parametroBean.getHorarioInicial();
        String horario = null;

        while (parametroBean.getHorarioFinal().after(novahora)) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

            GregorianCalendar gc = new GregorianCalendar();
            String timeAux = sdf.format(novahora);
            
            horario = DataUtil.retornarHorarioEmString(novahora);

            if(unidadeDAO.verificarHorarioDeAlmocoUnidade(horario)) {
                listaHorarios.add(horario);
            }

            gc.setTime(sdf.parse(timeAux));

            gc.add(Calendar.MINUTE, parametroBean.getIntervalo());

            novahora = new java.sql.Time(gc.getTime().getTime());


        }

        return listaHorarios;
    }

    public static ArrayList<String> gerarHorariosAtendimentoOlhandoHorariosCheios(Integer codEquipe, Long codProfissional, Date data) throws ParseException, ProjetoException {
        ParametroBean parametroBean = new ParametroBean();
        ArrayList<String> listaHorarios = new ArrayList<>();
        UnidadeDAO unidadeDAO = new UnidadeDAO();

        parametroBean = unidadeDAO.carregarDetalhesAtendimentoDaUnidade();

        java.sql.Time novahora = (Time) parametroBean.getHorarioInicial();
        String horario = null;

        while (parametroBean.getHorarioFinal().after(novahora)) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

            GregorianCalendar gc = new GregorianCalendar();
            String timeAux = sdf.format(novahora);

            horario = DataUtil.retornarHorarioEmString(novahora);
            
            if ((codEquipe!=null) && (data!=null) && (horario!=null)) {
            if(unidadeDAO.verificarHorariosCheios(codEquipe, null, DataUtil.converterDateUtilParaDateSql(data), horario)
            && unidadeDAO.verificarHorarioDeAlmocoUnidade(horario)) {
                listaHorarios.add(horario);
            }
            }
            else
            {
            	 listaHorarios.add(horario);
            }

            gc.setTime(sdf.parse(timeAux));

            gc.add(Calendar.MINUTE, parametroBean.getIntervalo());

            novahora = new java.sql.Time(gc.getTime().getTime());


        }

        return listaHorarios;
    }

}
