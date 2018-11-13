package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.gov.al.maceio.sishosp.acl.dao.FuncionarioDAO;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.DataUtil;
import br.gov.al.maceio.sishosp.hosp.model.ConfigAgendaParte1Bean;
import br.gov.al.maceio.sishosp.hosp.model.ConfigAgendaParte2Bean;

public class ConfigAgendaDAO {

    Connection con = null;
    FuncionarioDAO pDao = new FuncionarioDAO();
    EquipeDAO eDao = new EquipeDAO();

    public boolean gravarConfigAgendaEquipe(ConfigAgendaParte1Bean confParte1,
                                            ConfigAgendaParte2Bean confParte2,
                                            List<ConfigAgendaParte2Bean> listaTipos) {

        Boolean retorno = false;

        if (confParte1.getEquipe().getCodEquipe() == null
                || confParte1.getQtdMax() == null
                || confParte1.getAno() == null) {
            return false;
        }
        try {
            if (confParte1.getDiasSemana().size() > 0) {// ESCOLHEU DIAS SEMANA
                for (String dia : confParte1.getDiasSemana()) {
                    gravaTurnoEquipe(confParte1, listaTipos, dia);
                }
            } else {// ESCOLHEU DATA ESPECIFICA
                gravaTurnoEquipe(confParte1, listaTipos, null);
            }

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

    public void insereTipoAtendAgenda(int codConf,
                                      ConfigAgendaParte1Bean conf1,
                                      List<ConfigAgendaParte2Bean> listaTipos, Connection con) throws ProjetoException {
        PreparedStatement ps1 = null;
        String sql = "insert into hosp.tipo_atend_agenda (codconfigagenda, codprograma, codtipoatendimento, qtd, codempresa, codgrupo) "
                + " values(?, ?, ?, ?, ?, ?);";
        try {

            ps1 = con.prepareStatement(sql);
            for (ConfigAgendaParte2Bean conf : listaTipos) {
                ps1.setInt(1, codConf);
                ps1.setInt(2, conf.getPrograma().getIdPrograma());
                ps1.setInt(3, conf.getTipoAt().getIdTipo());
                ps1.setInt(4, conf.getQtd());
                ps1.setInt(5, 0);// COD EMPRESA ?
                ps1.setInt(6, conf.getGrupo().getIdGrupo());
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
        boolean gravou = false;
        int idTipo1 = 0;
        int idTipo2 = 0;
        int idTipo3 = 0;

        try {
            String sql = "INSERT INTO hosp.config_agenda(codmedico, diasemana, qtdmax, dataagenda, turno, mes, ano, id_configagenda) "
                    + " VALUES (?, ?, ?, ?, ?, ?, ?, DEFAULT) RETURNING id_configagenda;";

            con = ConnectionFactory.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = null;

            //SE FOR AMBOS OS TURNOS - INÍCIO
            if (confParte1.getTurno().equals("A")) {
                if (dia != null) {
                    ps.setInt(2, Integer.parseInt(dia));

                    ps.setDate(4, null);
                } else {
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

                rs = ps.executeQuery();

                if (rs.next()) {
                    idTipo1 = rs.getInt("id_configagenda");
                    insereTipoAtendAgenda(idTipo1, confParte1, listaTipos, con);
                }

                ps = con.prepareStatement(sql);

                if (dia != null) {
                    ps.setInt(2, Integer.parseInt(dia));
                    ps.setDate(4, null);
                } else {
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
                if (dia != null) {
                    ps.setInt(2, Integer.parseInt(dia));
                    ps.setDate(4, null);
                } else {
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

    public void gravaTurnoEquipe(ConfigAgendaParte1Bean confParte1,
                                 List<ConfigAgendaParte2Bean> listaTipos, String dia)
            throws SQLException, ProjetoException {

        String sql = "INSERT INTO hosp.config_agenda_equipe(codequipe, diasemana, qtdmax, dataagenda, turno, mes, ano, codempresa, id_configagenda) "
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, DEFAULT) RETURNING id_configagenda;";
        con = ConnectionFactory.getConnection();
        PreparedStatement ps2 = con.prepareStatement(sql);

        ResultSet rs2 = null;

        try {
            if (confParte1.getTurno().equals("A")) {
                if (dia != null) {
                    ps2.setInt(2, Integer.parseInt(dia));
                    ps2.setDate(4, null);
                } else {
                    ps2.setInt(2, 0);
                    ps2.setDate(4, new Date(confParte1.getDataEspecifica()
                            .getTime()));
                }
                ps2.setInt(1, confParte1.getEquipe().getCodEquipe());
                ps2.setInt(3, confParte1.getQtdMax());
                ps2.setString(5, "M");
                ps2.setInt(6, confParte1.getMes());
                ps2.setInt(7, confParte1.getAno());
                ps2.setInt(8, 0);// COD EMPRESA ?
                rs2 = ps2.executeQuery();
                con.commit();

                ps2 = con.prepareStatement(sql);
                if (dia != null) {
                    ps2.setInt(2, Integer.parseInt(dia));
                    ps2.setDate(4, null);
                } else {
                    ps2.setInt(2, 0);
                    ps2.setDate(4, new Date(confParte1.getDataEspecifica()
                            .getTime()));
                }
                ps2.setInt(1, confParte1.getEquipe().getCodEquipe());
                ps2.setInt(3, confParte1.getQtdMax());
                ps2.setString(5, "T");
                ps2.setInt(6, confParte1.getMes());
                ps2.setInt(7, confParte1.getAno());
                ps2.setInt(8, 0);// COD EMPRESA ?
                rs2 = ps2.executeQuery();
                con.commit();

            } else {
                if (dia != null) {
                    ps2.setInt(2, Integer.parseInt(dia));
                    ps2.setDate(4, null);
                } else {
                    ps2.setInt(2, 0);
                    ps2.setDate(4, new Date(confParte1.getDataEspecifica()
                            .getTime()));
                }
                ps2.setInt(1, confParte1.getEquipe().getCodEquipe());
                ps2.setInt(3, confParte1.getQtdMax());
                ps2.setString(5, confParte1.getTurno());
                ps2.setInt(6, confParte1.getMes());
                ps2.setInt(7, confParte1.getAno());
                ps2.setInt(8, 0);// COD EMPRESA ?
                rs2 = ps2.executeQuery();
                con.commit();
            }
        } catch (
                SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    public List<ConfigAgendaParte1Bean> listarHorariosComFiltros(
            ConfigAgendaParte1Bean config, Long codmedico)
            throws ProjetoException {

        List<ConfigAgendaParte1Bean> lista = new ArrayList<>();
        String sql = "SELECT c.id_configagenda, c.codmedico, c.diasemana, c.qtdmax, c.dataagenda, c.turno, c.mes, c.ano, c.codempresa, " +
                "f.descfuncionario, f.cns, f.codcbo, f.codprocedimentopadrao " +
                "FROM hosp.config_agenda c " +
                "left join acl.funcionarios f on (c.codmedico = f.id_funcionario) " +
                "where c.codmedico = ? ";

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
        String sql = "SELECT id_configagenda, codequipe, diasemana, qtdmax, dataagenda, turno, mes, ano, codempresa "
                + "FROM hosp.config_agenda_equipe where codequipe = ?";
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
                conf.setEquipe(eDao.buscarEquipePorID(rs.getInt("codequipe")));
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

    public List<ConfigAgendaParte1Bean> listarHorariosEquipe()
            throws ProjetoException {
        List<ConfigAgendaParte1Bean> lista = new ArrayList<>();
        String sql = "SELECT id_configagenda, codequipe, diasemana, qtdmax, dataagenda, turno, mes, ano, codempresa, codgrupo "
                + "FROM hosp.config_agenda_equipe order by id_configagenda ";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                ConfigAgendaParte1Bean conf = new ConfigAgendaParte1Bean();
                conf.setIdConfiAgenda(rs.getInt("id_configagenda"));
                conf.setEquipe(eDao.buscarEquipePorID(rs.getInt("codequipe")));
                conf.setDiaDaSemana(rs.getInt("diasemana"));
                // horainicio
                // horafim
                conf.setDataEspecifica(rs.getDate("dataagenda"));
                conf.setQtdMax(rs.getInt("qtdmax"));
                conf.setTurno(rs.getString("turno"));
                conf.setMes(rs.getInt("mes"));
                conf.setAno(rs.getInt("ano"));
                // codempresa

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
        String sql = "SELECT c.id_configagenda, c.codmedico, c.diasemana, c.qtdmax, c.dataagenda, c.turno, c.mes, c.ano, c.codempresa, " +
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
                // codempresa
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
        String sql = "SELECT c.id_configagenda, c.codmedico, c.diasemana, c.qtdmax, c.dataagenda, c.turno, c.mes, c.ano, c.codempresa, " +
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

    public ConfigAgendaParte1Bean listarHorariosPorIDEquipe2(int id)
            throws ProjetoException {

        ConfigAgendaParte1Bean c = new ConfigAgendaParte1Bean();
        String sql = "SELECT id_configagenda, codequipe, diasemana, qtdmax, dataagenda, turno, mes, ano, codempresa "
                + "FROM hosp.config_agenda_equipe where id_configagenda = ? order by id_configagenda ";
        ConfigAgendaParte1Bean conf = new ConfigAgendaParte1Bean();
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                conf.setIdConfiAgenda(rs.getInt("id_configagenda"));
                conf.setEquipe(eDao.buscarEquipePorID(rs.getInt("codequipe")));
                conf.setDiaDaSemana(rs.getInt("diasemana"));
                conf.setDataEspecifica(rs.getDate("dataagenda"));
                conf.setQtdMax(rs.getInt("qtdmax"));
                conf.setTurno(rs.getString("turno"));
                conf.setMes(rs.getInt("mes"));
                conf.setAno(rs.getInt("ano"));

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
        String sql = "SELECT id_configagenda, codequipe, diasemana,qtdmax, dataagenda, turno, mes, ano, codempresa "
                + "FROM hosp.config_agenda_equipe where codequipe = ? order by id_configagenda ";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                ConfigAgendaParte1Bean conf = new ConfigAgendaParte1Bean();
                conf.setIdConfiAgenda(rs.getInt("id_configagenda"));
                conf.setEquipe(eDao.buscarEquipePorID(rs.getInt("codequipe")));
                conf.setDiaDaSemana(rs.getInt("diasemana"));
                // horainicio
                // horafim
                conf.setDataEspecifica(rs.getDate("dataagenda"));
                conf.setQtdMax(rs.getInt("qtdmax"));
                conf.setTurno(rs.getString("turno"));
                conf.setMes(rs.getInt("mes"));
                conf.setAno(rs.getInt("ano"));
                // codempresa
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
            String sql = "UPDATE hosp.config_agenda SET codmedico = ?, diasemana = ?, qtdmax = ?, dataagenda = ?, turno = ?, mes = ?, ano = ?, codempresa = ? "
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
                ps2.setInt(8, 0);// COD EMPRESA ?
                ps2.setInt(9, confParte1.getIdConfiAgenda());
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
                ps2.setInt(8, 0);// COD EMPRESA ?
                ps2.setInt(9, confParte1.getIdConfiAgenda());
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
                ps2.setInt(8, 0);// COD EMPRESA ?
                ps2.setInt(9, confParte1.getIdConfiAgenda());
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

    public Boolean alterarTurnoEquipe(ConfigAgendaParte1Bean confParte1,
                                      List<ConfigAgendaParte2Bean> listaTipos, String dia)
            throws SQLException, ProjetoException {

        Boolean retorno = false;

        String sql = "UPDATE hosp.config_agenda_equipe SET codequipe = ?, diasemana = ?, qtdmax = ?, dataagenda = ?, turno = ?, mes = ?, ano = ?, codempresa = ? "
                + " WHERE id_configagenda = ?;";
        con = ConnectionFactory.getConnection();
        PreparedStatement ps2 = con.prepareStatement(sql);
        try {
            if (confParte1.getTurno().equals("A")) {
                if (confParte1.getDiaDaSemana() != null) {
                    ps2.setInt(2, confParte1.getDiaDaSemana());
                    ps2.setDate(4, null);
                } else {
                    ps2.setInt(2, 0);
                    ps2.setDate(4, new Date(confParte1.getDataEspecifica()
                            .getTime()));
                }
                ps2.setInt(1, confParte1.getEquipe().getCodEquipe());
                ps2.setInt(3, confParte1.getQtdMax());
                ps2.setString(5, "M");
                ps2.setInt(6, confParte1.getMes());
                ps2.setInt(7, confParte1.getAno());
                ps2.setInt(8, 0);// COD EMPRESA ?
                ps2.setInt(9, confParte1.getIdConfiAgenda());
                ps2.executeUpdate();

                ps2 = con.prepareStatement(sql);
                if (confParte1.getDiaDaSemana() != null) {
                    ps2.setInt(2, confParte1.getDiaDaSemana());
                    ps2.setDate(4, null);
                } else {
                    ps2.setInt(2, 0);
                    ps2.setDate(4, new Date(confParte1.getDataEspecifica()
                            .getTime()));
                }
                ps2.setInt(1, confParte1.getEquipe().getCodEquipe());
                ps2.setInt(3, confParte1.getQtdMax());
                ps2.setString(5, "T");
                ps2.setInt(6, confParte1.getMes());
                ps2.setInt(7, confParte1.getAno());
                ps2.setInt(8, 0);// COD EMPRESA ?
                ps2.setInt(9, confParte1.getIdConfiAgenda());
                ps2.executeUpdate();

            } else {
                if (confParte1.getDiaDaSemana() != null) {
                    ps2.setInt(2, confParte1.getDiaDaSemana());
                    ps2.setDate(4, null);
                } else {
                    ps2.setInt(2, 0);
                    ps2.setDate(4, new Date(confParte1.getDataEspecifica()
                            .getTime()));
                }
                ps2.setInt(1, confParte1.getEquipe().getCodEquipe());
                ps2.setInt(3, confParte1.getQtdMax());
                ps2.setString(5, confParte1.getTurno());
                ps2.setInt(6, confParte1.getMes());
                ps2.setInt(7, confParte1.getAno());
                ps2.setInt(8, 0);// COD EMPRESA ?
                ps2.setInt(9, confParte1.getIdConfiAgenda());
                ps2.executeUpdate();

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

}
