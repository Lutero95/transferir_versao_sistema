package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.DataUtil;
import br.gov.al.maceio.sishosp.hosp.enums.OpcaoConfiguracaoAgenda;
import br.gov.al.maceio.sishosp.hosp.model.ConfigAgendaParte1Bean;
import br.gov.al.maceio.sishosp.hosp.model.ConfigAgendaParte2Bean;

import javax.faces.context.FacesContext;

public class ConfigAgendaDAO {

    Connection con = null;
    FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
            .getSessionMap().get("obj_funcionario");

    public boolean gravarConfigAgendaEquipe(ConfigAgendaParte1Bean confParte1, ConfigAgendaParte2Bean confParte2) {

        Boolean retorno = false;

        try {
            con = ConnectionFactory.getConnection();

            if (confParte1.getOpcao().equals(OpcaoConfiguracaoAgenda.DIA_DA_SEMANA.getSigla())) {
                for (String dia : confParte1.getDiasSemana()) {
                    retorno = gravaTurnoEquipe(confParte1, confParte2, dia, con);
                }
            }
            if (confParte1.getOpcao().equals(OpcaoConfiguracaoAgenda.DATA_ESPECIFICA.getSigla())) {
                retorno = gravaTurnoEquipe(confParte1, confParte2, null, con);
            }

            if (retorno == true) {
                con.commit();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return retorno;
        }
    }

    public void insereTipoAtendAgenda(int codConf,
                                      ConfigAgendaParte1Bean conf1,
                                      List<ConfigAgendaParte2Bean> listaTipos, Connection con) {
        PreparedStatement ps1 = null;
        String sql = "insert into hosp.tipo_atend_agenda (codconfigagenda, codprograma, codtipoatendimento, qtd, codgrupo) "
                + " values(?, ?, ?, ?, ?);";
        try {

            ps1 = con.prepareStatement(sql);
            for (ConfigAgendaParte2Bean conf : listaTipos) {
                ps1.setInt(1, codConf);
                ps1.setInt(2, conf.getPrograma().getIdPrograma());
                ps1.setInt(3, conf.getTipoAt().getIdTipo());
                ps1.setInt(4, conf.getQtd());
                ps1.setInt(5, conf.getGrupo().getIdGrupo());
                ps1.execute();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public Boolean gravaTurno(ConfigAgendaParte1Bean confParte1,
                              List<ConfigAgendaParte2Bean> listaTipos, String dia) {

        FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("obj_funcionario");

        boolean gravou = false;
        int idTipo1 = 0;
        int idTipo2 = 0;
        int idTipo3 = 0;

        try {
            String sql = "INSERT INTO hosp.config_agenda(codmedico, diasemana, qtdmax, dataagenda, turno, mes, ano, id_configagenda, cod_empresa) "
                    + " VALUES (?, ?, ?, ?, ?, ?, ?, DEFAULT, ?) RETURNING id_configagenda;";

            con = ConnectionFactory.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = null;

            //SE FOR AMBOS OS TURNOS - INÍCIO
            if (confParte1.getTurno().equals("A")) {
                if (confParte1.getOpcao().equals(OpcaoConfiguracaoAgenda.DIA_DA_SEMANA.getSigla())) {
                    ps.setInt(2, Integer.parseInt(dia));

                    ps.setDate(4, null);
                }
                if (confParte1.getOpcao().equals(OpcaoConfiguracaoAgenda.DATA_ESPECIFICA.getSigla())) {
                    ps.setDate(4, new Date(confParte1.getDataEspecifica()
                            .getTime()));

                    Calendar c1 = DataUtil.retornarDataCalendar(confParte1.getDataEspecifica());

                    ps.setInt(2, 0);

                    confParte1.setMes(c1.get(Calendar.MONTH) + 1);
                    confParte1.setAno(c1.get(Calendar.YEAR));

                }

                ps.setLong(1, confParte1.getProfissional().getId());

                ps.setInt(3, confParte1.getQtdMax());

                ps.setString(5, "M");

                ps.setInt(6, confParte1.getMes());

                ps.setInt(7, confParte1.getAno());

                ps.setInt(8, user_session.getEmpresa().getCodEmpresa());

                rs = ps.executeQuery();

                if (rs.next()) {
                    idTipo1 = rs.getInt("id_configagenda");
                    insereTipoAtendAgenda(idTipo1, confParte1, listaTipos, con);
                }

                ps = con.prepareStatement(sql);

                if (confParte1.getOpcao().equals(OpcaoConfiguracaoAgenda.DIA_DA_SEMANA.getSigla())) {
                    ps.setInt(2, Integer.parseInt(dia));
                    ps.setDate(4, null);
                }
                if (confParte1.getOpcao().equals(OpcaoConfiguracaoAgenda.DATA_ESPECIFICA.getSigla())) {
                    ps.setInt(2, 0);
                    Calendar c1 = DataUtil.retornarDataCalendar(confParte1.getDataEspecifica());
                    c1.setTime(confParte1.getDataEspecifica());
                    ps.setDate(4, new Date(confParte1.getDataEspecifica()
                            .getTime()));
                    confParte1.setMes(c1.get(Calendar.MONTH) + 1);
                    confParte1.setAno(c1.get(Calendar.YEAR));
                }
                ps.setLong(1, confParte1.getProfissional().getId());
                ps.setInt(3, confParte1.getQtdMax());
                ps.setString(5, "T");
                ps.setInt(6, confParte1.getMes());
                ps.setInt(7, confParte1.getAno());
                rs = ps.executeQuery();

                if (rs.next()) {
                    idTipo2 = rs.getInt("id_configagenda");
                    insereTipoAtendAgenda(idTipo2, confParte1, listaTipos, con);

                }
                //SE FOR AMBOS OS TURNOS - FINAL

                //SE FOR TURNO ÚNICO - INÍCIO
            } else {
                if (confParte1.getOpcao().equals(OpcaoConfiguracaoAgenda.DIA_DA_SEMANA.getSigla())) {
                    ps.setInt(2, Integer.parseInt(dia));
                    ps.setDate(4, null);
                }
                if (confParte1.getOpcao().equals(OpcaoConfiguracaoAgenda.DATA_ESPECIFICA.getSigla())) {
                    ps.setDate(4, new Date(confParte1.getDataEspecifica()
                            .getTime()));

                    Calendar c1 = DataUtil.retornarDataCalendar(confParte1.getDataEspecifica());
                    c1.setTime(confParte1.getDataEspecifica());

                    ps.setInt(2, 0);
                    confParte1.setMes(c1.get(Calendar.MONTH) + 1);
                    confParte1.setAno(c1.get(Calendar.YEAR));

                }
                ps.setLong(1, confParte1.getProfissional().getId());
                ps.setInt(3, confParte1.getQtdMax());
                ps.setString(5, confParte1.getTurno());
                ps.setInt(6, confParte1.getMes());
                ps.setInt(7, confParte1.getAno());
                ps.setInt(8, user_session.getEmpresa().getCodEmpresa());
                rs = ps.executeQuery();

                if (rs.next()) {
                    idTipo3 = rs.getInt("id_configagenda");
                    insereTipoAtendAgenda(idTipo3, confParte1, listaTipos, con);

                }

            }
            //SE FOR TURNO ÚNICO - FINAL

            con.commit();
            gravou = true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return gravou;
        }
    }

    public Boolean gravaTurnoEquipe(ConfigAgendaParte1Bean confParte1, ConfigAgendaParte2Bean confParte2, String dia, Connection conn)
            throws SQLException {

        FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("obj_funcionario");

        Boolean retorno = false;
        String sql = "INSERT INTO hosp.config_agenda_equipe(codequipe, diasemana, qtdmax, dataagenda, turno, mes, ano, " +
                "cod_empresa, id_configagenda, opcao, codprograma, codgrupo) "
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, DEFAULT, ?, ?, ?) RETURNING id_configagenda;";

        PreparedStatement ps = conn.prepareStatement(sql);

        ResultSet rs = null;

        try {
            if (confParte1.getTurno().equals("A")) {
                if (confParte1.getOpcao().equals(OpcaoConfiguracaoAgenda.DIA_DA_SEMANA.getSigla())) {
                    ps.setInt(2, Integer.parseInt(dia));
                    ps.setDate(4, null);
                }
                if (confParte1.getOpcao().equals(OpcaoConfiguracaoAgenda.DATA_ESPECIFICA.getSigla())) {
                    ps.setInt(2, 0);
                    ps.setDate(4, new Date(confParte1.getDataEspecifica()
                            .getTime()));
                }
                ps.setInt(1, confParte1.getEquipe().getCodEquipe());
                ps.setInt(3, confParte1.getQtdMax());
                ps.setString(5, "M");
                ps.setInt(6, confParte1.getMes());
                ps.setInt(7, confParte1.getAno());
                ps.setInt(8, user_session.getEmpresa().getCodEmpresa());
                ps.setString(9, confParte1.getOpcao());
                ps.setInt(10, confParte2.getPrograma().getIdPrograma());
                ps.setInt(11, confParte2.getGrupo().getIdGrupo());
                rs = ps.executeQuery();

                ps = conn.prepareStatement(sql);
                if (confParte1.getOpcao().equals(OpcaoConfiguracaoAgenda.DIA_DA_SEMANA.getSigla())) {
                    ps.setInt(2, Integer.parseInt(dia));
                    ps.setDate(4, null);
                }
                if (confParte1.getOpcao().equals(OpcaoConfiguracaoAgenda.DATA_ESPECIFICA.getSigla())) {
                    ps.setInt(2, 0);
                    ps.setDate(4, new Date(confParte1.getDataEspecifica()
                            .getTime()));
                }

                ps.setInt(1, confParte1.getEquipe().getCodEquipe());
                ps.setInt(3, confParte1.getQtdMax());
                ps.setString(5, "T");
                ps.setInt(6, confParte1.getMes());
                ps.setInt(7, confParte1.getAno());
                ps.setInt(8, user_session.getEmpresa().getCodEmpresa());
                ps.setString(9, confParte1.getOpcao());
                ps.setInt(10, confParte2.getPrograma().getIdPrograma());
                ps.setInt(11, confParte2.getGrupo().getIdGrupo());
                rs = ps.executeQuery();

            } else {
                if (confParte1.getOpcao().equals(OpcaoConfiguracaoAgenda.DIA_DA_SEMANA.getSigla())) {
                    ps.setInt(2, Integer.parseInt(dia));
                    ps.setDate(4, null);
                }
                if (confParte1.getOpcao().equals(OpcaoConfiguracaoAgenda.DATA_ESPECIFICA.getSigla())) {
                    ps.setInt(2, 0);
                    ps.setDate(4, new Date(confParte1.getDataEspecifica()
                            .getTime()));
                }
                ps.setInt(1, confParte1.getEquipe().getCodEquipe());
                ps.setInt(3, confParte1.getQtdMax());
                ps.setString(5, confParte1.getTurno());
                ps.setInt(6, confParte1.getMes());
                ps.setInt(7, confParte1.getAno());
                ps.setInt(8, user_session.getEmpresa().getCodEmpresa());
                ps.setString(9, confParte1.getOpcao());
                ps.setInt(10, confParte2.getPrograma().getIdPrograma());
                ps.setInt(11, confParte2.getGrupo().getIdGrupo());
                rs = ps.executeQuery();
            }
            retorno = true;
        } catch (
                SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return retorno;
    }


    public List<ConfigAgendaParte1Bean> listarHorariosComFiltros(
            ConfigAgendaParte1Bean config, Long codmedico)
            throws ProjetoException {

        List<ConfigAgendaParte1Bean> lista = new ArrayList<>();
        String sql = "SELECT c.id_configagenda, c.codmedico, c.diasemana, c.qtdmax, c.dataagenda, c.turno, c.mes, c.ano, " +
                "f.descfuncionario, f.cns, f.codcbo, f.codprocedimentopadrao " +
                "FROM hosp.config_agenda c " +
                "left join acl.funcionarios f on (c.codmedico = f.id_funcionario) " +
                "where c.codmedico = ? AND c.cod_empresa = ? ";

        if (config != null) {
            if (config.getAno() != null) {
                if (config.getAno() > 0) {
                    sql = sql + " and c.ano = ?";
                }
            }
            if (config.getTurno() != null) {
                if (!config.getTurno().equals("")) {
                    sql = sql + " and c.turno = ?";
                }
            }
            if (config.getMes() != null) {
                if (config.getMes() > 0) {
                    sql = sql + " and c.mes = ?";
                }
            }
        }

        sql = sql + "order by c.id_configagenda ";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            int i = 1;

            stm.setLong(i, codmedico);
            stm.setInt(i+1, user_session.getEmpresa().getCodEmpresa());

            if (config != null) {
                if (config.getAno() != null) {
                    if (config.getAno() > 0) {
                        i = i + 1;
                        stm.setInt(i, config.getAno());
                    }
                }
                if (config.getTurno() != null) {
                    if (!config.getTurno().equals("")) {
                        i = i + 1;
                        stm.setString(i, config.getTurno());
                    }
                }
                if (config.getMes() != null) {
                    if (config.getMes() > 0) {
                        i = i + 1;
                        stm.setInt(i, config.getMes());
                    }
                }
            }

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                ConfigAgendaParte1Bean conf = new ConfigAgendaParte1Bean();
                conf.setIdConfiAgenda(rs.getInt("id_configagenda"));
                conf.getProfissional().setId(rs.getLong("codmedico"));
                conf.getProfissional().setNome(rs.getString("descfuncionario"));
                conf.getProfissional().setCns(rs.getString("cns"));
                conf.getProfissional().getCbo().setCodCbo(rs.getInt("codcbo"));
                conf.getProfissional().getProc1().setIdProc(rs.getInt("codprocedimentopadrao"));
                conf.setDiaDaSemana(rs.getInt("diasemana"));
                conf.setDataEspecifica(rs.getDate("dataagenda"));
                conf.setQtdMax(rs.getInt("qtdmax"));
                conf.setTurno(rs.getString("turno"));
                conf.setMes(rs.getInt("mes"));
                conf.setAno(rs.getInt("ano"));

                lista.add(conf);

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return lista;
    }

    public List<ConfigAgendaParte1Bean> listarHorariosComFiltrosEquipe(
            ConfigAgendaParte1Bean config, int codequipe)
            throws ProjetoException {
        List<ConfigAgendaParte1Bean> lista = new ArrayList<>();
        String sql = "SELECT c.id_configagenda, c.codequipe, c.diasemana, c.qtdmax, c.dataagenda, c.turno, c.mes, c.ano, e.descequipe, c.opcao  "
                + "FROM hosp.config_agenda_equipe c " +
                "LEFT JOIN hosp.equipe e ON (c.codequipe = e.id_equipe) " +
                "where codequipe = ? and c.cod_empresa = ?";
        if (config != null) {
            if (config.getAno() != null) {
                if (config.getAno() > 0) {
                    sql = sql + " and ano = ?";
                }
            }
            if (config.getTurno() != null) {
                if (!config.getTurno().equals("")) {
                    sql = sql + " and turno = ?";
                }
            }
            if (config.getMes() != null) {
                if (config.getMes() > 0) {
                    sql = sql + " and mes = ?";
                }
            }
        }

        sql = sql + "order by id_configagenda ";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            int i = 1;

            stm.setInt(i, codequipe);
            stm.setInt(i+1, user_session.getEmpresa().getCodEmpresa());

            if (config != null) {
                if (config.getAno() != null) {
                    if (config.getAno() > 0) {
                        i = i + 1;
                        stm.setInt(i, config.getAno());
                    }
                }
                if (config.getTurno() != null) {
                    if (!config.getTurno().equals("")) {
                        i = i + 1;
                        stm.setString(i, config.getTurno());
                    }
                }
                if (config.getMes() != null) {
                    if (config.getMes() > 0) {
                        i = i + 1;
                        stm.setInt(i, config.getMes());
                    }
                }
            }

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                ConfigAgendaParte1Bean conf = new ConfigAgendaParte1Bean();
                conf.setIdConfiAgenda(rs.getInt("id_configagenda"));
                conf.getEquipe().setCodEquipe(rs.getInt("codequipe"));
                conf.getEquipe().setDescEquipe(rs.getString("descequipe"));
                conf.setDiaDaSemana(rs.getInt("diasemana"));
                conf.setDataEspecifica(rs.getDate("dataagenda"));
                conf.setQtdMax(rs.getInt("qtdmax"));
                conf.setTurno(rs.getString("turno"));
                conf.setMes(rs.getInt("mes"));
                conf.setAno(rs.getInt("ano"));
                conf.setOpcao(rs.getString("opcao"));

                lista.add(conf);

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return lista;
    }

    public List<ConfigAgendaParte1Bean> listarHorariosEquipe()
            throws ProjetoException {
        List<ConfigAgendaParte1Bean> lista = new ArrayList<>();
        String sql = "SELECT c.id_configagenda, c.codequipe, c.diasemana, c.qtdmax, c.dataagenda, c.turno, c.mes, c.ano, e.descequipe, c.opcao  "
                + "FROM hosp.config_agenda_equipe c " +
                "LEFT JOIN hosp.equipe e ON (c.codequipe = e.id_equipe) " +
                "WHERE c.cod_empresa = ? ORDER BY id_configagenda ";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, user_session.getEmpresa().getCodEmpresa());
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                ConfigAgendaParte1Bean conf = new ConfigAgendaParte1Bean();
                conf.setIdConfiAgenda(rs.getInt("id_configagenda"));
                conf.getEquipe().setCodEquipe(rs.getInt("codequipe"));
                conf.getEquipe().setDescEquipe(rs.getString("descequipe"));
                conf.setDiaDaSemana(rs.getInt("diasemana"));
                conf.setDataEspecifica(rs.getDate("dataagenda"));
                conf.setQtdMax(rs.getInt("qtdmax"));
                conf.setTurno(rs.getString("turno"));
                conf.setMes(rs.getInt("mes"));
                conf.setAno(rs.getInt("ano"));
                conf.setOpcao(rs.getString("opcao"));

                lista.add(conf);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return lista;
    }

    public List<ConfigAgendaParte1Bean> listarHorariosPorIDProfissional(Long id)
            throws ProjetoException {

        List<ConfigAgendaParte1Bean> lista = new ArrayList<>();
        String sql = "SELECT c.id_configagenda, c.codmedico, c.diasemana, c.qtdmax, c.dataagenda, c.turno, c.mes, c.ano, " +
                "f.descfuncionario, f.cns, f.codcbo, f.codprocedimentopadrao " +
                "FROM hosp.config_agenda c " +
                "left join acl.funcionarios f on (c.codmedico = f.id_funcionario) " +
                "where c.codmedico = ? ";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setLong(1, id);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                ConfigAgendaParte1Bean conf = new ConfigAgendaParte1Bean();
                conf.setIdConfiAgenda(rs.getInt("id_configagenda"));
                conf.getProfissional().setId(rs.getLong("codmedico"));
                conf.getProfissional().setNome(rs.getString("descfuncionario"));
                conf.getProfissional().setCns(rs.getString("cns"));
                conf.getProfissional().getCbo().setCodCbo(rs.getInt("codcbo"));
                conf.getProfissional().getProc1().setIdProc(rs.getInt("codprocedimentopadrao"));
                conf.setDiaDaSemana(rs.getInt("diasemana"));
                conf.setDataEspecifica(rs.getDate("dataagenda"));
                conf.setQtdMax(rs.getInt("qtdmax"));
                conf.setTurno(rs.getString("turno"));
                conf.setMes(rs.getInt("mes"));
                conf.setAno(rs.getInt("ano"));
                lista.add(conf);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return lista;
    }

    public Integer carregarGrupoDaEquipe(int id) throws ProjetoException {

        int codgrupo = 0;

        String sql = "SELECT codgrupo FROM hosp.config_agenda_equipe where id_configagenda = ? ";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                codgrupo = rs.getInt("codgrupo");

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return codgrupo;
    }

    public ConfigAgendaParte1Bean listarHorariosPorIDProfissionalEdit(int id)
            throws ProjetoException {

        ConfigAgendaParte1Bean conf = new ConfigAgendaParte1Bean();
        String sql = "SELECT c.id_configagenda, c.codmedico, c.diasemana, c.qtdmax, c.dataagenda, c.turno, c.mes, c.ano, c.opcao, " +
                "f.descfuncionario, f.cns, f.codcbo, f.codprocedimentopadrao " +
                "FROM hosp.config_agenda c " +
                "left join acl.funcionarios f on (c.codmedico = f.id_funcionario) "
                + "where id_configagenda = ? order by id_configagenda ";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                conf.setIdConfiAgenda(rs.getInt("id_configagenda"));
                conf.getProfissional().setId(rs.getLong("codmedico"));
                conf.getProfissional().setNome(rs.getString("descfuncionario"));
                conf.getProfissional().setCns(rs.getString("cns"));
                conf.getProfissional().getCbo().setCodCbo(rs.getInt("codcbo"));
                conf.getProfissional().getProc1().setIdProc(rs.getInt("codprocedimentopadrao"));
                conf.setDiaDaSemana(rs.getInt("diasemana"));
                conf.setDataEspecifica(rs.getDate("dataagenda"));
                conf.setQtdMax(rs.getInt("qtdmax"));
                conf.setTurno(rs.getString("turno"));
                conf.setMes(rs.getInt("mes"));
                conf.setAno(rs.getInt("ano"));
                conf.setOpcao(rs.getString("opcao"));

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return conf;
    }

    public ConfigAgendaParte1Bean listarHorariosPorIDEquipeEdit(int id)
            throws ProjetoException {

        ConfigAgendaParte1Bean conf = new ConfigAgendaParte1Bean();
        String sql = "SELECT c.id_configagenda, c.codequipe, c.diasemana, c.qtdmax, c.dataagenda, c.turno, c.mes, c.ano, e.descequipe, c.opcao  "
                + "FROM hosp.config_agenda_equipe c " +
                "LEFT JOIN hosp.equipe e ON (c.codequipe = e.id_equipe) " +
                "WHERE id_configagenda = ? ORDER BY id_configagenda";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                conf.setIdConfiAgenda(rs.getInt("id_configagenda"));
                conf.getEquipe().setCodEquipe(rs.getInt("codequipe"));
                conf.getEquipe().setDescEquipe(rs.getString("descequipe"));
                conf.setDiaDaSemana(rs.getInt("diasemana"));
                conf.setDataEspecifica(rs.getDate("dataagenda"));
                conf.setQtdMax(rs.getInt("qtdmax"));
                conf.setTurno(rs.getString("turno"));
                conf.setMes(rs.getInt("mes"));
                conf.setAno(rs.getInt("ano"));
                conf.setOpcao(rs.getString("opcao"));

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return conf;
    }

    public List<ConfigAgendaParte1Bean> listarHorariosPorIDEquipe(int id)
            throws ProjetoException {
        List<ConfigAgendaParte1Bean> lista = new ArrayList<>();
        String sql = "SELECT c.id_configagenda, c.codequipe, c.diasemana, c.qtdmax, c.dataagenda, c.turno, c.mes, c.ano, e.descequipe, c.opcao " +
                "FROM hosp.config_agenda_equipe c " +
                "LEFT JOIN hosp.equipe e ON (c.codequipe = e.id_equipe) "
                + "where codequipe = ? order by id_configagenda ";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                ConfigAgendaParte1Bean conf = new ConfigAgendaParte1Bean();
                conf.setIdConfiAgenda(rs.getInt("id_configagenda"));
                conf.getEquipe().setCodEquipe(rs.getInt("codequipe"));
                conf.getEquipe().setDescEquipe(rs.getString("descequipe"));
                conf.setDiaDaSemana(rs.getInt("diasemana"));
                conf.setDataEspecifica(rs.getDate("dataagenda"));
                conf.setQtdMax(rs.getInt("qtdmax"));
                conf.setTurno(rs.getString("turno"));
                conf.setMes(rs.getInt("mes"));
                conf.setAno(rs.getInt("ano"));
                conf.setOpcao(rs.getString("opcao"));

                lista.add(conf);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return lista;
    }

    public boolean excluirConfig(ConfigAgendaParte1Bean confParte1)
            throws ProjetoException {
        String sql = "delete from hosp.config_agenda where id_configagenda = ?";
        try {
            con = ConnectionFactory.getConnection();

            excluirTabelaTipoAgenda(confParte1.getIdConfiAgenda(), con);

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setLong(1, confParte1.getIdConfiAgenda());
            stmt.execute();
            con.commit();
            return true;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public boolean excluirConfigEquipe(ConfigAgendaParte1Bean confParte1) {
        Boolean retorno = false;
        String sql = "delete from hosp.config_agenda_equipe where id_configagenda = ?";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setLong(1, confParte1.getIdConfiAgenda());
            stmt.execute();
            con.commit();
            retorno = true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return retorno;
        }
    }

    public void excluirTabelaTipoAgenda(int id, Connection con) {

        String sql = "delete from hosp.tipo_atend_agenda where codconfigagenda = ?";
        try {

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setLong(1, id);
            stmt.execute();

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public Boolean alterarTurno(ConfigAgendaParte1Bean confParte1,
                                List<ConfigAgendaParte2Bean> listaTipos) {

        Boolean retorno = false;

        try {
            String sql = "UPDATE hosp.config_agenda SET codmedico = ?, diasemana = ?, qtdmax = ?, dataagenda = ?, turno = ?, mes = ?, ano = ? "
                    + " WHERE id_configagenda = ?;";
            con = ConnectionFactory.getConnection();
            PreparedStatement ps2 = con.prepareStatement(sql);


            if (confParte1.getTurno().equals("A")) {
                if (confParte1.getDiaDaSemana() != null) {
                    ps2.setInt(2, confParte1.getDiaDaSemana());
                    ps2.setDate(4, null);
                } else {
                    ps2.setInt(2, 0);
                    ps2.setDate(4, new Date(confParte1.getDataEspecifica()
                            .getTime()));
                }
                ps2.setLong(1, confParte1.getProfissional().getId());
                ps2.setInt(3, confParte1.getQtdMax());
                ps2.setString(5, "M");
                ps2.setInt(6, confParte1.getMes());
                ps2.setInt(7, confParte1.getAno());
                ps2.setInt(8, confParte1.getIdConfiAgenda());
                ps2.executeUpdate();

                String sql2 = "DELETE from hosp.tipo_atend_agenda where codconfigagenda = ?";
                PreparedStatement ps3 = con.prepareStatement(sql2);
                ps3.setInt(1, confParte1.getIdConfiAgenda());
                ps3.execute();

                String sql3 = "INSERT INTO hosp.tipo_atend_agenda (codprograma, codtipoatendimento,  qtd, codgrupo, codconfigagenda) "
                        + " VALUES (?, ?, ?, ?, ?); ";

                PreparedStatement ps4 = con.prepareStatement(sql3);
                for (ConfigAgendaParte2Bean conf : listaTipos) {
                    ps4.setInt(1, conf.getPrograma().getIdPrograma());
                    ps4.setInt(2, conf.getTipoAt().getIdTipo());
                    ps4.setInt(3, conf.getQtd());
                    ps4.setInt(4, conf.getGrupo().getIdGrupo());
                    ps4.setInt(5, confParte1.getIdConfiAgenda());
                    ps4.execute();
                }

                ps2 = con.prepareStatement(sql);
                if (confParte1.getDiaDaSemana() != null) {
                    ps2.setInt(2, confParte1.getDiaDaSemana());
                    ps2.setDate(4, null);
                } else {
                    ps2.setInt(2, 0);
                    ps2.setDate(4, new Date(confParte1.getDataEspecifica()
                            .getTime()));
                }
                ps2.setLong(1, confParte1.getProfissional().getId());
                ps2.setInt(3, confParte1.getQtdMax());
                ps2.setString(5, "T");
                ps2.setInt(6, confParte1.getMes());
                ps2.setInt(7, confParte1.getAno());
                ps2.setInt(8, confParte1.getIdConfiAgenda());
                ps2.executeUpdate();

                sql2 = "DELETE from hosp.tipo_atend_agenda where codconfigagenda = ?";
                ps3 = con.prepareStatement(sql2);
                ps3.setInt(1, confParte1.getIdConfiAgenda());
                ps3.execute();

                sql3 = "INSERT INTO hosp.tipo_atend_agenda (codprograma, codtipoatendimento,  qtd, codgrupo, codconfigagenda) "
                        + " VALUES (?, ?, ?, ?, ?); ";

                ps4 = con.prepareStatement(sql3);
                for (ConfigAgendaParte2Bean conf : listaTipos) {
                    ps4.setInt(1, conf.getPrograma().getIdPrograma());
                    ps4.setInt(2, conf.getTipoAt().getIdTipo());
                    ps4.setInt(3, conf.getQtd());
                    ps4.setInt(4, conf.getGrupo().getIdGrupo());
                    ps4.setInt(5, confParte1.getIdConfiAgenda());
                    ps4.execute();

                }
            } else {
                if (confParte1.getDiaDaSemana() != null) {
                    ps2.setInt(2, confParte1.getDiaDaSemana());
                    ps2.setDate(4, null);
                } else {
                    ps2.setInt(2, 0);
                    ps2.setDate(4, new Date(confParte1.getDataEspecifica()
                            .getTime()));
                }
                ps2.setLong(1, confParte1.getProfissional().getId());
                ps2.setInt(3, confParte1.getQtdMax());
                ps2.setString(5, confParte1.getTurno());
                ps2.setInt(6, confParte1.getMes());
                ps2.setInt(7, confParte1.getAno());
                ps2.setInt(8, confParte1.getIdConfiAgenda());
                ps2.execute();

                /*
                String sql2 = "DELETE from hosp.tipo_atend_agenda where codconfigagenda = ?";
                PreparedStatement ps3 = con.prepareStatement(sql2);
                ps3.setInt(1, confParte1.getIdConfiAgenda());
                ps3.execute();

                String sql3 = "INSERT INTO hosp.tipo_atend_agenda (codprograma, codtipoatendimento,  qtd, codgrupo, codconfigagenda) "
                        + " VALUES (?, ?, ?, ?, ?); ";

                PreparedStatement ps4 = con.prepareStatement(sql3);
                for (ConfigAgendaParte2Bean conf : listaTipos) {
                    ps4.setInt(1, conf.getPrograma().getIdPrograma());
                    ps4.setInt(2, conf.getTipoAt().getIdTipo());
                    ps4.setInt(3, conf.getQtd());
                    ps4.setInt(4, conf.getGrupo().getIdGrupo());
                    ps4.setInt(5, confParte1.getIdConfiAgenda());
                    ps4.execute();
                }
                */
            }
            con.commit();
            retorno = true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return retorno;
        }
    }

    public Boolean alterarTurnoEquipe(ConfigAgendaParte1Bean confParte1)
            throws SQLException, ProjetoException {

        Boolean retorno = false;

        String sql = "UPDATE hosp.config_agenda_equipe SET codequipe = ?, diasemana = ?, qtdmax = ?, dataagenda = ?, turno = ?, mes = ?, ano = ?, opcao = ?"
                + " WHERE id_configagenda = ?;";

        con = ConnectionFactory.getConnection();

        PreparedStatement ps2 = con.prepareStatement(sql);

        try {

            if (confParte1.getOpcao().equals(OpcaoConfiguracaoAgenda.DATA_ESPECIFICA.getSigla())) {
                ps2.setInt(2, 0);
                ps2.setDate(4, new Date(confParte1.getDataEspecifica()
                        .getTime()));
            }
            if (confParte1.getOpcao().equals(OpcaoConfiguracaoAgenda.DIA_DA_SEMANA.getSigla())) {
                ps2.setInt(2, confParte1.getDiaDaSemana());
                ps2.setDate(4, null);
            }

            ps2.setInt(1, confParte1.getEquipe().getCodEquipe());
            ps2.setInt(3, confParte1.getQtdMax());
            ps2.setString(5, confParte1.getTurno());
            ps2.setInt(6, confParte1.getMes());
            ps2.setInt(7, confParte1.getAno());
            ps2.setString(8, confParte1.getOpcao());
            ps2.setInt(9, confParte1.getIdConfiAgenda());
            ps2.execute();

            con.commit();

            retorno = true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return retorno;
        }
    }

}
