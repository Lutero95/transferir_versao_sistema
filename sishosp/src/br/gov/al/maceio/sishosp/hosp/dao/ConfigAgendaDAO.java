package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.DataUtil;
import br.gov.al.maceio.sishosp.hosp.enums.OpcaoConfiguracaoAgenda;
import br.gov.al.maceio.sishosp.hosp.enums.TipoDataAgenda;
import br.gov.al.maceio.sishosp.hosp.model.*;

import javax.faces.context.FacesContext;

public class ConfigAgendaDAO {

    Connection con = null;
    FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
            .getSessionMap().get("obj_funcionario");

    public Boolean gravarConfiguracaoAgendaProfissionalInicio(ConfigAgendaParte1Bean confParte1,
                                                              List<ConfigAgendaParte2Bean> listaTipos) throws ProjetoException, SQLException {

        Boolean retorno = false;

        try{
        con = ConnectionFactory.getConnection();

        Integer idConfiguracaoAgenda = gravaConfiguracaoAgendaProfissional(confParte1, listaTipos, con);

        if (idConfiguracaoAgenda != null) {

            if (confParte1.getOpcao().equals(OpcaoConfiguracaoAgenda.DIA_DA_SEMANA.getSigla())) {
                for (int i = 0; i < confParte1.getDiasSemana().size(); i++) {
                    retorno = gravaConfiguracaoAgendaProfissionalDias(confParte1, confParte1.getDiasSemana().get(i), con, idConfiguracaoAgenda);

                    if (!retorno) {
                        return false;
                    }
                }
            }

            if (confParte1.getOpcao().equals(OpcaoConfiguracaoAgenda.DATA_ESPECIFICA.getSigla())) {
                retorno = gravaConfiguracaoAgendaProfissionalDias(confParte1, null, con, idConfiguracaoAgenda);
            }

            if (retorno) {
                con.commit();
            }

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


    public Integer gravaConfiguracaoAgendaProfissional(ConfigAgendaParte1Bean confParte1,
                                                       List<ConfigAgendaParte2Bean> listaTipos, Connection conAuxiliar) {

        FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("obj_funcionario");

        Integer idConfigAgenda = null;

        try {
            String sql = "INSERT INTO hosp.config_agenda_profissional(codmedico, qtdmax, mes, ano, cod_empresa, opcao) "
                    + " VALUES (?, ?, ?, ?, ?, ?) RETURNING id_configagenda;";

            PreparedStatement ps = conAuxiliar.prepareStatement(sql);

            ResultSet rs = null;

            ps.setLong(1, confParte1.getProfissional().getId());

            ps.setInt(2, confParte1.getQtdMax());

            if (confParte1.getMes() != null) {
                ps.setInt(3, confParte1.getMes());
            } else {
                ps.setNull(3, Types.NULL);
            }

            if (confParte1.getAno() != null) {
                ps.setInt(4, confParte1.getAno());
            } else {
                ps.setNull(4, Types.NULL);
            }

            ps.setInt(5, user_session.getEmpresa().getCodEmpresa());

            ps.setString(6, confParte1.getOpcao());

            rs = ps.executeQuery();

            if (rs.next()) {
                idConfigAgenda = rs.getInt("id_configagenda");
                insereTipoAtendAgenda(idConfigAgenda, confParte1, listaTipos, conAuxiliar);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return idConfigAgenda;
        }
    }

    public void insereTipoAtendAgenda(int codConfigAgenda,
                                      ConfigAgendaParte1Bean conf1,
                                      List<ConfigAgendaParte2Bean> listaTipos, Connection conAuxiliar) {
        PreparedStatement ps1 = null;
        String sql = "insert into hosp.tipo_atend_agenda (cod_config_agenda, codprograma, codtipoatendimento, qtd, codgrupo) "
                + " values(?, ?, ?, ?, ?);";
        try {

            ps1 = conAuxiliar.prepareStatement(sql);
            for (ConfigAgendaParte2Bean conf : listaTipos) {
                ps1.setInt(1, codConfigAgenda);
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

    public Boolean gravaConfiguracaoAgendaProfissionalDias(ConfigAgendaParte1Bean confParte1, String dia, Connection conAuxiliar, Integer idConfigAgenda) {

        Boolean retorno = false;

        try {
            String sql2 = "INSERT INTO hosp.config_agenda_profissional_dias (dia, data_especifica, turno, id_config_agenda_profissional) "
                    + " VALUES (?, ?, ?, ?);";

            PreparedStatement ps2 = conAuxiliar.prepareStatement(sql2);

            //SE FOR AMBOS OS TURNOS - INÍCIO
            if (confParte1.getTurno().equals("A")) {
                if (confParte1.getOpcao().equals(OpcaoConfiguracaoAgenda.DIA_DA_SEMANA.getSigla())) {
                    ps2.setInt(1, Integer.parseInt(dia));

                    ps2.setDate(2, null);
                }
                if (confParte1.getOpcao().equals(OpcaoConfiguracaoAgenda.DATA_ESPECIFICA.getSigla())) {
                    ps2.setNull(1, Types.NULL);

                    ps2.setDate(2, new Date(confParte1.getDataEspecifica().getTime()));

                }

                ps2.setString(3, "M");

                ps2.setInt(4, idConfigAgenda);

                ps2.execute();

                ps2 = con.prepareStatement(sql2);

                if (confParte1.getOpcao().equals(OpcaoConfiguracaoAgenda.DIA_DA_SEMANA.getSigla())) {
                    ps2.setInt(1, Integer.parseInt(dia));

                    ps2.setNull(2, Types.NULL);
                }
                if (confParte1.getOpcao().equals(OpcaoConfiguracaoAgenda.DATA_ESPECIFICA.getSigla())) {
                    ps2.setNull(1, Types.NULL);

                    ps2.setDate(2, new Date(confParte1.getDataEspecifica().getTime()));

                }

                ps2.setString(3, "T");

                ps2.setInt(4, idConfigAgenda);

                ps2.execute();

                //SE FOR AMBOS OS TURNOS - FINAL

                //SE FOR TURNO ÚNICO - INÍCIO
            } else {
                if (confParte1.getOpcao().equals(OpcaoConfiguracaoAgenda.DIA_DA_SEMANA.getSigla())) {
                    ps2.setInt(1, Integer.parseInt(dia));

                    ps2.setDate(2, null);
                }
                if (confParte1.getOpcao().equals(OpcaoConfiguracaoAgenda.DATA_ESPECIFICA.getSigla())) {
                    ps2.setNull(1, Types.NULL);

                    ps2.setDate(2, new Date(confParte1.getDataEspecifica().getTime()));

                }

                ps2.setString(3, confParte1.getTurno());

                ps2.setInt(4, idConfigAgenda);

                ps2.execute();

            }
            //SE FOR TURNO ÚNICO - FINAL

            retorno = true;

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return retorno;
        }
    }

    public Boolean gravarConfiguracaoAgendaEquipeInicio(ConfigAgendaParte1Bean confParte1, ConfigAgendaParte2Bean confParte2) throws ProjetoException, SQLException {

        Boolean retorno = false;

        try {
        con = ConnectionFactory.getConnection();

        Integer idConfiguracaoAgenda = gravaConfiguracaoAgendaEquipe(confParte1, confParte2, con);

        if (idConfiguracaoAgenda != null) {

            if (confParte1.getOpcao().equals(OpcaoConfiguracaoAgenda.DIA_DA_SEMANA.getSigla())) {
                for (int i = 0; i < confParte1.getDiasSemana().size(); i++) {
                    retorno = gravaConfiguracaoAgendaEquipeDias(confParte1, confParte1.getDiasSemana().get(i), con, idConfiguracaoAgenda);

                    if (!retorno) {
                        return false;
                    }
                }
            }

            if (confParte1.getOpcao().equals(OpcaoConfiguracaoAgenda.DATA_ESPECIFICA.getSigla())) {
                retorno = gravaConfiguracaoAgendaEquipeDias(confParte1, null, con, idConfiguracaoAgenda);
            }

            if (retorno) {
                con.commit();
            }
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

    public Integer gravaConfiguracaoAgendaEquipe(ConfigAgendaParte1Bean confParte1, ConfigAgendaParte2Bean confParte2, Connection conAuxiliar) {

        FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("obj_funcionario");

        Integer idConfigAgenda = null;

        try {
            String sql = "INSERT INTO hosp.config_agenda_equipe(codequipe, qtdmax, mes, ano, cod_empresa, opcao, codprograma, codgrupo) "
                    + " VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id_configagenda;";

            PreparedStatement ps = conAuxiliar.prepareStatement(sql);

            ResultSet rs = null;

            ps.setInt(1, confParte1.getEquipe().getCodEquipe());

            ps.setInt(2, confParte1.getQtdMax());

            if (confParte1.getMes() != null) {
                ps.setInt(3, confParte1.getMes());
            } else {
                ps.setNull(3, Types.NULL);
            }

            if (confParte1.getAno() != null) {
                ps.setInt(4, confParte1.getAno());
            } else {
                ps.setNull(4, Types.NULL);
            }

            ps.setInt(5, user_session.getEmpresa().getCodEmpresa());

            ps.setString(6, confParte1.getOpcao());

            ps.setInt(7, confParte2.getPrograma().getIdPrograma());

            ps.setInt(8, confParte2.getGrupo().getIdGrupo());

            rs = ps.executeQuery();

            if (rs.next()) {
                idConfigAgenda = rs.getInt("id_configagenda");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return idConfigAgenda;
        }
    }

    public Boolean gravaConfiguracaoAgendaEquipeDias(ConfigAgendaParte1Bean confParte1, String dia, Connection conAuxiliar, Integer idConfigAgenda) {

        Boolean retorno = false;

        try {
            String sql2 = "INSERT INTO hosp.config_agenda_equipe_dias (dia, data_especifica, turno, id_config_agenda_equipe) "
                    + " VALUES (?, ?, ?, ?);";

            PreparedStatement ps2 = conAuxiliar.prepareStatement(sql2);

            //SE FOR AMBOS OS TURNOS - INÍCIO
            if (confParte1.getTurno().equals("A")) {
                if (confParte1.getOpcao().equals(OpcaoConfiguracaoAgenda.DIA_DA_SEMANA.getSigla())) {
                    ps2.setInt(1, Integer.parseInt(dia));

                    ps2.setDate(2, null);
                }
                if (confParte1.getOpcao().equals(OpcaoConfiguracaoAgenda.DATA_ESPECIFICA.getSigla())) {
                    ps2.setNull(1, Types.NULL);

                    ps2.setDate(2, new Date(confParte1.getDataEspecifica().getTime()));

                }

                ps2.setString(3, "M");

                ps2.setInt(4, idConfigAgenda);

                ps2.execute();

                ps2 = con.prepareStatement(sql2);

                if (confParte1.getOpcao().equals(OpcaoConfiguracaoAgenda.DIA_DA_SEMANA.getSigla())) {
                    ps2.setInt(1, Integer.parseInt(dia));

                    ps2.setNull(2, Types.NULL);
                }
                if (confParte1.getOpcao().equals(OpcaoConfiguracaoAgenda.DATA_ESPECIFICA.getSigla())) {
                    ps2.setNull(1, Types.NULL);

                    ps2.setDate(2, new Date(confParte1.getDataEspecifica().getTime()));

                }

                ps2.setString(3, "T");

                ps2.setInt(4, idConfigAgenda);

                ps2.execute();

                //SE FOR AMBOS OS TURNOS - FINAL

                //SE FOR TURNO ÚNICO - INÍCIO
            } else {
                if (confParte1.getOpcao().equals(OpcaoConfiguracaoAgenda.DIA_DA_SEMANA.getSigla())) {
                    ps2.setInt(1, Integer.parseInt(dia));

                    ps2.setDate(2, null);
                }
                if (confParte1.getOpcao().equals(OpcaoConfiguracaoAgenda.DATA_ESPECIFICA.getSigla())) {
                    ps2.setNull(1, Types.NULL);

                    ps2.setDate(2, new Date(confParte1.getDataEspecifica().getTime()));

                }

                ps2.setString(3, confParte1.getTurno());

                ps2.setInt(4, idConfigAgenda);

                ps2.execute();

            }
            //SE FOR TURNO ÚNICO - FINAL

            retorno = true;

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return retorno;
        }
    }

    public List<ConfigAgendaParte1Bean> listarHorariosComFiltrosProfissional(
            ConfigAgendaParte1Bean config, Long codmedico)
            throws ProjetoException {

        List<ConfigAgendaParte1Bean> lista = new ArrayList<>();

        String sql = "SELECT c.id_configagenda, c.codmedico, d.dia, c.qtdmax, d.data_especifica, d.turno, c.mes, c.ano, " +
                "f.descfuncionario, f.cns, f.codcbo, f.codprocedimentopadrao " +
                "FROM hosp.config_agenda_profissional c " +
                "LEFT JOIN hosp.config_agenda_profissional_dias d ON (c.id_configagenda = d.id_config_agenda_profissional) " +
                "LEFT JOIN acl.funcionarios f ON (c.codmedico = f.id_funcionario) " +
                "WHERE c.codmedico = ? AND c.cod_empresa = ?";


        if (config != null) {
            if (config.getAno() != null) {
                if (config.getAno() > 0) {
                    sql = sql + " and c.ano = ?";
                }
            }
            if (config.getTurno() != null) {
                if (!config.getTurno().equals("")) {
                    sql = sql + " and d.turno = ?";
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
            i = i + 1;
            stm.setInt(i, user_session.getEmpresa().getCodEmpresa());

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
                conf.setDiaDaSemana(rs.getInt("dia"));
                conf.setDataEspecifica(rs.getDate("data_especifica"));
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

        String sql = "SELECT c.id_configagenda, c.codequipe, d.dia, c.qtdmax, d.data_especifica, d.turno, c.mes, c.ano, e.descequipe, c.opcao " +
                "FROM hosp.config_agenda_equipe c " +
                "LEFT JOIN hosp.config_agenda_equipe_dias d ON (c.id_configagenda = d.id) " +
                "LEFT JOIN hosp.equipe e ON (c.codequipe = e.id_equipe) " +
                "WHERE c.codequipe = ? AND c.cod_empresa = ?";

        if (config != null) {
            if (config.getAno() != null) {
                if (config.getAno() > 0) {
                    sql = sql + " and c.ano = ?";
                }
            }
            if (config.getTurno() != null) {
                if (!config.getTurno().equals("")) {
                    sql = sql + " and d.turno = ?";
                }
            }
            if (config.getMes() != null) {
                if (config.getMes() > 0) {
                    sql = sql + " and c.mes = ?";
                }
            }
        }

        sql = sql + "ORDER BY c.id_configagenda ";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            int i = 1;

            stm.setInt(i, codequipe);
            stm.setInt(i + 1, user_session.getEmpresa().getCodEmpresa());

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
                conf.setDiaDaSemana(rs.getInt("dia"));
                conf.setDataEspecifica(rs.getDate("data_especifica"));
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

        String sql = "SELECT c.id_configagenda, c.codequipe, d.dia, c.qtdmax, d.data_especifica, d.turno, c.mes, c.ano, e.descequipe, c.opcao " +
                "FROM hosp.config_agenda_equipe c " +
                "LEFT JOIN hosp.config_agenda_equipe_dias d ON (c.id_configagenda = d.id) " +
                "LEFT JOIN hosp.equipe e ON (c.codequipe = e.id_equipe) " +
                "where c.cod_empresa = ? ORDER BY c.id_configagenda";

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
                conf.setDiaDaSemana(rs.getInt("dia"));
                conf.setDataEspecifica(rs.getDate("data_especifica"));
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

        String sql = "SELECT c.id_configagenda, c.codmedico, d.dia, c.qtdmax, d.dia, d.turno, c.mes, c.ano, d.data_especifica, " +
                "f.descfuncionario, f.cns, f.codcbo, f.codprocedimentopadrao " +
                "FROM hosp.config_agenda_profissional c " +
                "LEFT JOIN hosp.config_agenda_profissional_dias d ON (c.id_configagenda = d.id_config_agenda_profissional) " +
                "LEFT JOIN acl.funcionarios f ON (c.codmedico = f.id_funcionario) " +
                "WHERE c.codmedico = ?";

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
                conf.setDiaDaSemana(rs.getInt("dia"));
                conf.setDataEspecifica(rs.getDate("data_especifica"));
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

        String sql = "SELECT c.id_configagenda, c.codmedico, d.dia, c.qtdmax, d.dia, c.mes, c.ano, c.opcao, d.data_especifica, " +
                "f.descfuncionario, f.cns, f.codcbo, f.codprocedimentopadrao, " +
                "CASE WHEN " +
                "(SELECT count(DISTINCT turno) FROM hosp.config_agenda_profissional cc " +
                "LEFT JOIN hosp.config_agenda_profissional_dias dd ON (cc.id_configagenda = dd.id_config_agenda_profissional) " +
                "WHERE cc.id_configagenda = c.id_configagenda) > 1 THEN 'A' " +
                "ELSE d.turno END AS turno " +
                "FROM hosp.config_agenda_profissional c " +
                "LEFT JOIN hosp.config_agenda_profissional_dias d ON (c.id_configagenda = d.id_config_agenda_profissional) " +
                "LEFT JOIN acl.funcionarios f ON (c.codmedico = f.id_funcionario) " +
                "WHERE c.id_configagenda = ?";

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
                conf.setDiaDaSemana(rs.getInt("dia"));
                conf.setDataEspecifica(rs.getDate("data_especifica"));
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

        String sql = "SELECT DISTINCT c.id_configagenda, c.codequipe, c.qtdmax, d.data_especifica,  c.mes, c.ano, e.descequipe, c.opcao, " +
                "CASE WHEN " +
                "(SELECT count(DISTINCT turno) FROM hosp.config_agenda_equipe cc " +
                "LEFT JOIN hosp.config_agenda_equipe_dias dd ON (cc.id_configagenda = dd.id_config_agenda_equipe) " +
                "WHERE cc   .id_configagenda = c.id_configagenda) > 1 then 'A' " +
                "ELSE d.turno END AS turno " +
                "FROM hosp.config_agenda_equipe c " +
                "LEFT JOIN hosp.config_agenda_equipe_dias d ON (c.id_configagenda = d.id_config_agenda_equipe) " +
                "LEFT JOIN hosp.equipe e ON (c.codequipe = e.id_equipe) " +
                "WHERE c.id_configagenda = ? ORDER BY c.id_configagenda";


        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                conf.setIdConfiAgenda(rs.getInt("id_configagenda"));
                conf.getEquipe().setCodEquipe(rs.getInt("codequipe"));
                conf.getEquipe().setDescEquipe(rs.getString("descequipe"));
                conf.setDataEspecifica(rs.getDate("data_especifica"));
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

    public ConfigAgendaParte2Bean listarHorariosPorIDEquipeEditParte2(int id)
            throws ProjetoException {

        ConfigAgendaParte2Bean conf = new ConfigAgendaParte2Bean();

        String sql = "SELECT c.codprograma, c.codgrupo, p.descprograma, g.descgrupo " +
                "FROM hosp.config_agenda_equipe c " +
                "LEFT JOIN hosp.grupo g ON (c.codgrupo = g.id_grupo) " +
                "LEFT JOIN hosp.programa p ON (c.codprograma = p.id_programa) " +
                "WHERE c.id_configagenda = ?";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                conf.setPrograma(new ProgramaBean());
                conf.setGrupo(new GrupoBean());
                conf.getPrograma().setIdPrograma(rs.getInt("codprograma"));
                conf.getPrograma().setDescPrograma(rs.getString("descprograma"));
                conf.getGrupo().setIdGrupo(rs.getInt("codgrupo"));
                conf.getGrupo().setDescGrupo(rs.getString("descgrupo"));

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

        String sql = "SELECT c.id_configagenda, c.codequipe, d.dia, c.qtdmax, d.data_especifica, d.turno, c.mes, c.ano, e.descequipe, c.opcao " +
                "FROM hosp.config_agenda_equipe c " +
                "LEFT JOIN hosp.config_agenda_equipe_dias d ON (c.id_configagenda = d.id_config_agenda_equipe) " +
                "LEFT JOIN hosp.equipe e ON (c.codequipe = e.id_equipe) " +
                "WHERE c.codequipe = ? ORDER BY c.id_configagenda";

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
                conf.setDiaDaSemana(rs.getInt("dia"));
                conf.setDataEspecifica(rs.getDate("data_especifica"));
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

    public boolean excluirConfigProfissional(ConfigAgendaParte1Bean confParte1)
            throws ProjetoException {
        String sql = "delete from hosp.config_agenda_profissional where id_configagenda = ?";
        try {
            con = ConnectionFactory.getConnection();

            excluirTabelaTipoAgenda(confParte1.getIdConfiAgenda(), con);
            excluirTabelaDiasProfissional(confParte1.getIdConfiAgenda(), con);

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

    public Boolean excluirConfigEquipe(ConfigAgendaParte1Bean confParte1)
            throws ProjetoException {

        Boolean retorno = false;

        try {
            con = ConnectionFactory.getConnection();

            if(!excluirTabelaConfigAgendaEquipeDias(confParte1.getIdConfiAgenda(), con)){
                return retorno;
            }

            if(!excluirTabelaConfigAgendaEquipe(confParte1.getIdConfiAgenda(), con)){
                return retorno;
            }

            con.commit();
            retorno = true;

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return retorno;
    }

    public Boolean excluirTabelaConfigAgendaEquipe(Integer id, Connection conAuxiliar) {

        Boolean retorno = false;

        String sql = "delete from hosp.config_agenda_equipe where id_configagenda = ?";

        try {

            PreparedStatement stmt2 = conAuxiliar.prepareStatement(sql);

            stmt2.setInt(1, id);
            stmt2.execute();
            retorno = true;

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return retorno;
        }
    }

    public Boolean excluirTabelaConfigAgendaEquipeDias(Integer id, Connection conAuxiliar) {

        Boolean retorno = false;

        String sql = "delete from hosp.config_agenda_equipe_dias where id_config_agenda_equipe = ?";

        try {

            PreparedStatement stmt = conAuxiliar.prepareStatement(sql);

            stmt.setInt(1, id);
            stmt.execute();
            retorno = true;

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return retorno;
        }
    }

    public void excluirTabelaTipoAgenda(int id, Connection con) {

        String sql = "delete from hosp.tipo_atend_agenda where cod_config_agenda = ?";
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

    public void excluirTabelaDiasProfissional(int id, Connection con) {

        String sql = "delete from hosp.config_agenda_profissional_dias where id_config_agenda_profissional = ?";
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

    public Boolean alterarConfiguracaoAgendaProfissionalInicio(ConfigAgendaParte1Bean confParte1,
                                                              List<ConfigAgendaParte2Bean> listaTipos) throws ProjetoException, SQLException {

        Boolean retorno = false;
        con = ConnectionFactory.getConnection();

        Boolean retornoAlteracao = alteraConfiguracaoAgendaProfissional(confParte1, listaTipos, con);

        if (retornoAlteracao) {

            if (confParte1.getOpcao().equals(OpcaoConfiguracaoAgenda.DIA_DA_SEMANA.getSigla())) {
                excluirTabelaDiasProfissional(confParte1.getIdConfiAgenda(), con);
                for (int i = 0; i < confParte1.getDiasSemana().size(); i++) {
                    retorno = gravaConfiguracaoAgendaProfissionalDias(confParte1, confParte1.getDiasSemana().get(i), con, confParte1.getIdConfiAgenda());

                    if (!retorno) {
                        return false;
                    }
                }
            }

            if (confParte1.getOpcao().equals(OpcaoConfiguracaoAgenda.DATA_ESPECIFICA.getSigla())) {
                retorno = gravaConfiguracaoAgendaProfissionalDias(confParte1, null, con, confParte1.getIdConfiAgenda());
            }

            if (retorno) {
                con.commit();
            }

        }

        return retorno;
    }

    public Boolean alteraConfiguracaoAgendaProfissional(ConfigAgendaParte1Bean confParte1,
                                List<ConfigAgendaParte2Bean> listaTipos, Connection conAuxiliar) {

        Boolean retorno = false;

        try {
            String sql = "UPDATE hosp.config_agenda_profissional SET codmedico = ?, qtdmax = ?, mes = ?, ano = ?, opcao = ? WHERE id_configagenda = ?;";

            PreparedStatement ps = conAuxiliar.prepareStatement(sql);

            ps.setLong(1, confParte1.getProfissional().getId());

            ps.setInt(2, confParte1.getQtdMax());

            if (confParte1.getMes() != null) {
                ps.setInt(3, confParte1.getMes());
            } else {
                ps.setNull(3, Types.NULL);
            }

            if (confParte1.getAno() != null) {
                ps.setInt(4, confParte1.getAno());
            } else {
                ps.setNull(4, Types.NULL);
            }

            ps.setString(5, confParte1.getOpcao());

            ps.setInt(6, confParte1.getIdConfiAgenda());

            ps.executeUpdate();

            excluirTabelaTipoAgenda(confParte1.getIdConfiAgenda(), conAuxiliar);
            insereTipoAtendAgenda(confParte1.getIdConfiAgenda(), confParte1, listaTipos, conAuxiliar);

            retorno = true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return retorno;
        }
    }

    public Boolean alterarConfiguracaoAgendaEquipeInicio(ConfigAgendaParte1Bean confParte1, ConfigAgendaParte2Bean confParte2) throws ProjetoException, SQLException {

        Boolean retorno = false;
        con = ConnectionFactory.getConnection();

        Boolean retornoAlteracao = alteraConfiguracaoAgendaEquipe(confParte1, confParte2, con);

        if (retornoAlteracao) {

            if (confParte1.getOpcao().equals(OpcaoConfiguracaoAgenda.DIA_DA_SEMANA.getSigla())) {
                excluirTabelaConfigAgendaEquipeDias(confParte1.getIdConfiAgenda(), con);
                for (int i = 0; i < confParte1.getDiasSemana().size(); i++) {
                    retorno = gravaConfiguracaoAgendaEquipeDias(confParte1, confParte1.getDiasSemana().get(i), con, confParte1.getIdConfiAgenda());

                    if (!retorno) {
                        return false;
                    }
                }
            }

            if (confParte1.getOpcao().equals(OpcaoConfiguracaoAgenda.DATA_ESPECIFICA.getSigla())) {
                retorno = gravaConfiguracaoAgendaEquipeDias(confParte1, null, con, confParte1.getIdConfiAgenda());
            }

            if (retorno) {
                con.commit();
            }

        }

        return retorno;
    }

    public Boolean alteraConfiguracaoAgendaEquipe(ConfigAgendaParte1Bean confParte1, ConfigAgendaParte2Bean confParte2, Connection conAuxiliar) {

        Boolean retorno = false;

        try {
            String sql = "UPDATE hosp.config_agenda_equipe SET codequipe = ?, qtdmax = ?, mes = ?, ano = ?, opcao = ?, codprograma = ?, codgrupo = ? " +
                    "WHERE id_configagenda = ?;";

            PreparedStatement ps = conAuxiliar.prepareStatement(sql);

            ps.setLong(1, confParte1.getEquipe().getCodEquipe());

            ps.setInt(2, confParte1.getQtdMax());

            if (confParte1.getMes() != null) {
                ps.setInt(3, confParte1.getMes());
            } else {
                ps.setNull(3, Types.NULL);
            }

            if (confParte1.getAno() != null) {
                ps.setInt(4, confParte1.getAno());
            } else {
                ps.setNull(4, Types.NULL);
            }

            ps.setString(5, confParte1.getOpcao());

            ps.setInt(6, confParte2.getPrograma().getIdPrograma());

            ps.setInt(7, confParte2.getGrupo().getIdGrupo());

            ps.setInt(8, confParte1.getIdConfiAgenda());

            ps.executeUpdate();

            retorno = true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return retorno;
        }
    }

    public ArrayList<String> listarDiasAtendimentoProfissionalPorId(Integer id)
            throws ProjetoException {
        ArrayList<String> lista = new ArrayList<>();
        String sql = "SELECT dia FROM hosp.config_agenda_profissional_dias WHERE id_config_agenda_profissional = ? ";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                String dia = rs.getString("dia");
                lista.add(dia);
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

    public ArrayList<String> listarDiasAtendimentoEquipePorId(Integer id)
            throws ProjetoException {
        ArrayList<String> lista = new ArrayList<>();
        String sql = "SELECT dia FROM hosp.config_agenda_equipe_dias WHERE id_config_agenda_equipe = ? ";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                String dia = rs.getString("dia");
                lista.add(dia);
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

    public List<ConfigAgendaParte2Bean> listarTipoAtendimentoConfiguracaoAgendaProfissional(Integer id)
            throws ProjetoException {
        List<ConfigAgendaParte2Bean> lista = new ArrayList<>();

        StringBuilder sql = new StringBuilder();

        sql.append("SELECT t.codprograma, p.descprograma, t.codgrupo, g.descgrupo, ");
        sql.append("t.codtipoatendimento, ti.desctipoatendimento, t.qtd ");
        sql.append("FROM hosp.tipo_atend_agenda t ");
        sql.append("LEFT JOIN hosp.programa p ON (p.id_programa = t.codprograma) ");
        sql.append("LEFT JOIN hosp.tipoatendimento ti ON (ti.id = t.codtipoatendimento) ");
        sql.append("LEFT JOIN hosp.grupo g ON (g.id_grupo = t.codgrupo) ");
        sql.append("WHERE cod_config_agenda = ? ");

        try {
            con = ConnectionFactory.getConnection();

            PreparedStatement stm = con.prepareStatement(sql.toString());

            stm.setInt(1, id);

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                ConfigAgendaParte2Bean conf = new ConfigAgendaParte2Bean();
                conf.setPrograma(new ProgramaBean());
                conf.setGrupo(new GrupoBean());
                conf.setTipoAt(new TipoAtendimentoBean());
                conf.getPrograma().setIdPrograma(rs.getInt("codprograma"));
                conf.getPrograma().setDescPrograma(rs.getString("descprograma"));
                conf.getGrupo().setIdGrupo(rs.getInt("codgrupo"));
                conf.getGrupo().setDescGrupo(rs.getString("descgrupo"));
                conf.getTipoAt().setIdTipo(rs.getInt("codtipoatendimento"));
                conf.getTipoAt().setDescTipoAt(rs.getString("desctipoatendimento"));
                conf.setQtd(rs.getInt("qtd"));

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

    public Boolean verificarSeExisteTipoAtendimentoEspecificoDataUnica(
            Long codProfissional, java.util.Date dataAtendimento, String turno)
            throws ProjetoException {

        Boolean retorno = false;
        int diaSemana = DataUtil.extrairDiaDeData(dataAtendimento);
        int mes = DataUtil.extrairMesDeData(dataAtendimento);
        int ano = DataUtil.extrairAnoDeData(dataAtendimento);

        String sql = "";

        sql = "SELECT c.id_configagenda FROM hosp.config_agenda_profissional c " +
                "LEFT JOIN hosp.config_agenda_profissional_dias d on (c.id_configagenda = d.id_config_agenda_profissional) " +
                "JOIN hosp.tipo_atend_agenda t ON (c.id_configagenda = t.cod_config_agenda) " +
                "WHERE c.codmedico = ? AND d.turno = ?  AND (d.data_especifica = ? OR d.dia = ? AND c.mes = ? AND c.ano = ?)";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setLong(1, codProfissional);
            stm.setString(2, turno.toUpperCase());
            stm.setDate(3, new java.sql.Date(dataAtendimento.getTime()));
            stm.setInt(4, diaSemana);
            stm.setInt(5, mes);
            stm.setInt(6, ano);

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                retorno = true;
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
        return retorno;
    }

    public Boolean verificarSeExisteTipoAtendimentoEspecificoIntervaloDeDatas(
            Long codProfissional, java.util.Date dataAtendimento, java.util.Date dataAtendimentoFinal, String turno, String tipoData)
            throws ProjetoException {

        Boolean retorno = false;
        int diaSemana = DataUtil.extrairDiaDeData(dataAtendimento);
        int mes = DataUtil.extrairMesDeData(dataAtendimento);
        int ano = DataUtil.extrairAnoDeData(dataAtendimento);

        String sql = "";

        sql = "SELECT c.id_configagenda FROM hosp.config_agenda_profissional c " +
                "LEFT JOIN hosp.config_agenda_profissional_dias d on (c.id_configagenda = d.id_config_agenda_profissional) " +
                "LEFT JOIN hosp.tipo_atend_agenda t ON (c.id_configagenda = t.cod_config_agenda) " +
                "WHERE c.codmedico = ? AND d.turno = ? ";

        if(tipoData.equals(TipoDataAgenda.DATA_UNICA.getSigla())) {
            sql = sql + " AND (d.data_especifica = ? )";
        }
        if(tipoData.equals(TipoDataAgenda.INTERVALO_DE_DATAS.getSigla())) {
            sql = sql + " AND d.data_especifica >= ? AND d.data_especifica <= ?";
        }
        if(tipoData.equals(TipoDataAgenda.DATA_UNICA.getSigla())) {
            sql = sql + " AND (d.data_especifica = ? OR (d.dia = ? AND c.mes = ? AND c.ano = ?))";
        }

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setLong(1, codProfissional);
            stm.setString(2, turno.toUpperCase());
            stm.setDate(3, new java.sql.Date(dataAtendimento.getTime()));
            if(tipoData.equals(TipoDataAgenda.INTERVALO_DE_DATAS.getSigla())) {
                stm.setDate(4, DataUtil.converterDateUtilParaDateSql(dataAtendimentoFinal));
            }

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                retorno = true;
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
        return retorno;
    }

}
