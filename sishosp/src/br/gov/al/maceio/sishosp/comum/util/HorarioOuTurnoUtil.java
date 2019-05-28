package br.gov.al.maceio.sishosp.comum.util;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.EmpresaDAO;
import br.gov.al.maceio.sishosp.hosp.enums.OpcaoAtendimento;
import br.gov.al.maceio.sishosp.hosp.model.ParametroBean;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public final class HorarioOuTurnoUtil {

    public static String retornarOpcaoAtendimentoEmpresa() throws ProjetoException {
        EmpresaDAO empresaDAO = new EmpresaDAO();

        String opcaoAtendimento = empresaDAO.carregarOpcaoAtendimentoDaEmpresa();

        opcaoAtendimento = !opcaoAtendimento.equals(OpcaoAtendimento.AMBOS.getSigla()) ? opcaoAtendimento : OpcaoAtendimento.SOMENTE_TURNO.getSigla();

        return opcaoAtendimento;
    }

    public static ArrayList<String> gerarHorariosAtendimento() throws ParseException {
        ParametroBean parametroBean = new ParametroBean();
        ArrayList<String> listaHorarios = new ArrayList<>();
        EmpresaDAO empresaDAO = new EmpresaDAO();

        parametroBean = empresaDAO.carregarDetalhesAtendimentoDaEmpresa();

        java.sql.Time novahora = (Time) parametroBean.getHorarioInicial();
        String horario = null;

        while (parametroBean.getHorarioFinal().after(novahora)) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

            GregorianCalendar gc = new GregorianCalendar();
            String timeAux = sdf.format(novahora);

            gc.setTime(sdf.parse(timeAux));

            gc.add(Calendar.MINUTE, parametroBean.getIntervalo());

            novahora = new java.sql.Time(gc.getTime().getTime());

            horario = DataUtil.retornarHorarioEmString(novahora);

            listaHorarios.add(horario);

        }

        return listaHorarios;
    }

}
