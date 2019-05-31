package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.DataUtil;
import br.gov.al.maceio.sishosp.hosp.model.GerenciarPacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.InsercaoPacienteBean;

import javax.faces.context.FacesContext;

public class RenovacaoPacienteDAO {

    PreparedStatement ps = null;
    private Connection conexao = null;

    public InsercaoPacienteBean carregarPacientesInstituicaoRenovacao(Integer id)
            throws ProjetoException {

        String sql = "select pi.id, pi.codprograma, p.descprograma, p.cod_procedimento, pi.codgrupo, g.descgrupo, pi.codpaciente, pi.codequipe, e.descequipe, a.turno, "
                + " pi.codprofissional, f.descfuncionario, pi.observacao, a.codtipoatendimento, t.desctipoatendimento "
                + " from hosp.paciente_instituicao pi "
                + " left join hosp.programa p on (p.id_programa = pi.codprograma) "
                + " left join hosp.grupo g on (pi.codgrupo = g.id_grupo) "
                + " left join hosp.equipe e on (pi.codequipe = e.id_equipe) "
                + " left join hosp.atendimentos a on (a.id_paciente_instituicao = pi.id) "
                + " left join hosp.tipoatendimento t on (a.codtipoatendimento = t.id) "
                + " left join acl.funcionarios f on (pi.codprofissional = f.id_funcionario) "
                + " where pi.id = ?";

        List<GerenciarPacienteBean> lista = new ArrayList<>();
        InsercaoPacienteBean ip = new InsercaoPacienteBean();
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);

            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ip = new InsercaoPacienteBean();

                ip.setId(rs.getInt("id"));
                ip.getPrograma().setIdPrograma(rs.getInt("codprograma"));
                ip.getPrograma().setDescPrograma(rs.getString("descprograma"));
                ip.getPrograma().getProcedimento().setIdProc(rs.getInt("cod_procedimento"));
                ip.getGrupo().setIdGrupo(rs.getInt("codgrupo"));
                ip.getGrupo().setDescGrupo(rs.getString("descgrupo"));
                ip.getLaudo().getPaciente()
                        .setId_paciente(rs.getInt("codpaciente"));
                ip.getEquipe().setCodEquipe(rs.getInt("codequipe"));
                ip.getEquipe().setDescEquipe(rs.getString("descequipe"));
                ip.getFuncionario().setId(rs.getLong("codprofissional"));
                ip.getFuncionario().setNome(rs.getString("descfuncionario"));
                ip.setObservacao(rs.getString("observacao"));
                ip.getAgenda().getTipoAt()
                        .setIdTipo(rs.getInt("codtipoatendimento"));
                ip.getAgenda().getTipoAt()
                        .setDescTipoAt(rs.getString("desctipoatendimento"));
                ip.getAgenda().setTurno(rs.getString("turno"));

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return ip;
    }

    public ArrayList<GerenciarPacienteBean> listarDiasAtendimentoProfissionalEquipe(
            Integer id) throws ProjetoException {

        ArrayList<GerenciarPacienteBean> lista = new ArrayList<>();

        String sql = "select distinct(p.id_profissional), f.descfuncionario, f.codcbo, p.id_paciente_instituicao, dia_semana, "
                + " case when dia_semana = 1 then 'Domingo' when dia_semana = 2 then 'Segunda' "
                + " when dia_semana = 3 then 'Terça' when dia_semana = 4 then 'Quarta' "
                + " when dia_semana = 5 then 'Quinta' when dia_semana = 6 then 'Sexta' when dia_semana = 7 then 'Sábado' "
                + " end as dia from hosp.profissional_dia_atendimento p "
                + " left join acl.funcionarios f on (f.id_funcionario = p.id_profissional) "
                + " where p.id_paciente_instituicao = ? "
                + " order by id_profissional";
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stm = conexao.prepareStatement(sql);

            stm.setInt(1, id);

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                GerenciarPacienteBean ge = new GerenciarPacienteBean();
                ge.getFuncionario().setNome(rs.getString("descfuncionario"));
                ge.getFuncionario().setId(rs.getLong("id_profissional"));
                ge.setId(rs.getInt("id_paciente_instituicao"));
                ge.getFuncionario().setDiasSemana(rs.getString("dia"));
                ge.getFuncionario().setDiaSemana(rs.getInt("dia_semana"));
                ge.getFuncionario().getCbo().setCodCbo(rs.getInt("codcbo"));

                lista.add(ge);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return lista;
    }

    public ArrayList<String> listarDiasAtendimentoProfissional(Integer id)
            throws ProjetoException {

        ArrayList<String> lista = new ArrayList<>();

        String sql = "select dia_semana from hosp.profissional_dia_atendimento "
                + " where id_paciente_instituicao = ? ";

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stm = conexao.prepareStatement(sql);

            stm.setInt(1, id);

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                lista.add(rs.getString("dia_semana"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return lista;
    }

    public boolean gravarRenovacaoEquipe(InsercaoPacienteBean insercao,
                                         InsercaoPacienteBean insercaoParaLaudo,
                                         List<InsercaoPacienteBean> listAgendamentoProfissional, List<FuncionarioBean> listaProfissionais) {

        FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("obj_funcionario");

        GerenciarPacienteDAO gerenciarPacienteDAO = new GerenciarPacienteDAO();

        Boolean retorno = false;
        ResultSet rs = null;

        String sql1 = "update hosp.paciente_instituicao set status = 'CR' where id = ?";

        try {

            conexao = ConnectionFactory.getConnection();
            ps = conexao.prepareStatement(sql1);
            ps.setInt(1, insercao.getId());

            ps.executeUpdate();

            String sql2 = "insert into hosp.paciente_instituicao (codprograma, codgrupo, codpaciente, codequipe, status, codlaudo, observacao, cod_empresa, data_solicitacao, data_cadastro) "
                    + " values (?, ?, ?, ?, ?, ?, ?, ?, ?, current_timestamp) RETURNING id;";

            PreparedStatement ps2 = null;
            ps2 = conexao.prepareStatement(sql2);
            ps2.setInt(1, insercao.getPrograma().getIdPrograma());
            ps2.setInt(2, insercao.getGrupo().getIdGrupo());
            ps2.setInt(3, insercao.getLaudo().getPaciente().getId_paciente());
            ps2.setInt(4, insercao.getEquipe().getCodEquipe());
            ps2.setString(5, "A");
            ps2.setInt(6, insercaoParaLaudo.getLaudo().getId());
            ps2.setString(7, insercao.getObservacao());
            ps2.setInt(8, user_session.getEmpresa().getCodEmpresa());
            ps2.setDate(9, new java.sql.Date(insercao.getDataSolicitacao().getTime()));
            rs = ps2.executeQuery();
            int idPacienteInstituicao = 0;
            if (rs.next()) {
                idPacienteInstituicao = rs.getInt("id");
            }


            String sql3 = "INSERT INTO hosp.atendimentos(codpaciente, codmedico, situacao, dtaatende, codtipoatendimento, turno, "
                    + " observacao, ativo, id_paciente_instituicao, cod_empresa, horario, dtamarcacao, codprograma, codgrupo, codequipe, codatendente)"
                    + " VALUES (?, ?, 'A', ?, ?, ?, ?, 'S', ?, ?, ?, CURRENT_TIMESTAMP, ?, ?, ?, ?) RETURNING id_atendimento;";

            PreparedStatement ps3 = null;
            rs = null;
            ps3 = conexao.prepareStatement(sql3);

            for (int i = 0; i < listAgendamentoProfissional.size(); i++) {

                ps3.setInt(1, insercaoParaLaudo.getLaudo().getPaciente()
                        .getId_paciente());
                ps3.setLong(2, listAgendamentoProfissional.get(i).getAgenda().getProfissional()
                        .getId());
                ps3.setDate(3, new java.sql.Date(listAgendamentoProfissional.get(i).getAgenda()
                        .getDataMarcacao().getTime()));
                ps3.setInt(4, user_session.getEmpresa().getParametro().getTipoAtendimento().getIdTipo());

                if (insercao.getAgenda().getTurno() != null) {
                    ps3.setString(5, insercao.getAgenda().getTurno());
                } else {
                    ps3.setNull(5, Types.NULL);
                }

                ps3.setString(6, insercao.getObservacao());
                ps3.setInt(7, idPacienteInstituicao);
                ps3.setInt(8, user_session.getEmpresa().getCodEmpresa());

                if (insercao.getAgenda().getHorario() != null) {
                    ps3.setTime(9, DataUtil.retornarHorarioEmTime(insercao.getAgenda().getHorario()));
                } else {
                    ps3.setNull(9, Types.NULL);
                }

                if (insercao.getPrograma().getIdPrograma() != null) {
                    ps3.setInt(10, insercao.getPrograma().getIdPrograma());
                } else {
                    ps3.setNull(10, Types.NULL);
                }

                if (insercao.getGrupo().getIdGrupo() != null) {
                    ps3.setInt(11, insercao.getGrupo().getIdGrupo());
                } else {
                    ps3.setNull(11, Types.NULL);
                }

                if (insercao.getEquipe().getCodEquipe() != null) {
                    ps3.setInt(12, insercao.getEquipe().getCodEquipe());
                } else {
                    ps3.setNull(12, Types.NULL);
                }

                ps3.setInt(13, user_session.getCodigo());

                rs = ps3.executeQuery();

                int idAgend = 0;
                if (rs.next()) {
                    idAgend = rs.getInt("id_atendimento");
                }

                for (int j = 0; j < listaProfissionais.size(); j++) {

                    for (int h = 0; h < listaProfissionais.get(j).getListDiasSemana().size(); h++) {

                        if (DataUtil.extrairDiaDeData(listAgendamentoProfissional.get(i).getAgenda().getDataMarcacao()) ==
                                Integer.parseInt(listaProfissionais.get(j).getListDiasSemana().get(h))) {

                            String sql4 = "INSERT INTO hosp.atendimentos1 (codprofissionalatendimento, id_atendimento, cbo, codprocedimento) VALUES  (?, ?, ?, ?)";

                            PreparedStatement ps4 = null;
                            ps4 = conexao.prepareStatement(sql4);

                            ps4.setLong(1, listaProfissionais.get(j).getId());
                            ps4.setInt(2, idAgend);
                            ps4.setInt(3, listaProfissionais.get(j).getCbo().getCodCbo());
                            ps4.setInt(4, insercao.getPrograma().getProcedimento().getIdProc());

                            ps4.executeUpdate();
                        }
                    }
                }

            }

            if(gerenciarPacienteDAO.gravarHistoricoAcaoPaciente(idPacienteInstituicao, insercao.getObservacao(), "R", conexao)){
                conexao.commit();

                retorno = true;
            }

            retorno = true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return retorno;
        }
    }

    public boolean gravarInsercaoProfissional(InsercaoPacienteBean insercao,
                                              InsercaoPacienteBean insercaoParaLaudo,
                                              ArrayList<InsercaoPacienteBean> listaAgendamento) {

        FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("obj_funcionario");

        GerenciarPacienteDAO gerenciarPacienteDAO = new GerenciarPacienteDAO();

        Boolean retorno = false;
        ResultSet rs = null;

        String sql1 = "update hosp.paciente_instituicao set status = 'CR' where id = ?";
        try {
            conexao = ConnectionFactory.getConnection();
            ps = conexao.prepareStatement(sql1);
            ps.setInt(1, insercao.getId());

            ps.executeUpdate();

            String sql2 = "insert into hosp.paciente_instituicao (codprograma, codgrupo, codpaciente, codequipe, status, codlaudo, observacao, cod_empresa, data_solicitacao, data_cadastro) "
                    + " values (?, ?, ?, ?, ?, ?, ?, ?, ?, current_timestamp) RETURNING id;";

            PreparedStatement ps2 = null;
            ps2 = conexao.prepareStatement(sql2);
            ps2.setInt(1, insercao.getPrograma().getIdPrograma());
            ps2.setInt(2, insercao.getGrupo().getIdGrupo());
            ps2.setInt(3, insercao.getLaudo().getPaciente().getId_paciente());
            ps2.setInt(4, insercao.getEquipe().getCodEquipe());
            ps2.setString(5, "A");
            ps2.setInt(6, insercaoParaLaudo.getLaudo().getId());
            ps2.setString(7, insercao.getObservacao());
            ps2.setInt(8, user_session.getEmpresa().getCodEmpresa());
            ps2.setDate(9, new java.sql.Date(insercao.getDataSolicitacao().getTime()));
            rs = ps2.executeQuery();
            int idPacienteInstituicao = 0;
            if (rs.next()) {
                idPacienteInstituicao = rs.getInt("id");
            }

            String sql3 = "INSERT INTO hosp.profissional_dia_atendimento (id_paciente_instituicao, id_profissional, dia_semana, cod_empresa) VALUES  (?, ?, ?, ?)";
            PreparedStatement ps3 = null;
            ps3 = conexao.prepareStatement(sql3);

            for (int i = 0; i < insercao.getFuncionario().getListDiasSemana()
                    .size(); i++) {
                ps3.setLong(1, insercao.getId());
                ps3.setLong(2, insercao.getFuncionario().getId());
                ps3.setInt(
                        3,
                        Integer.parseInt(insercao.getFuncionario()
                                .getListDiasSemana().get(i)));
                ps3.setInt(4, user_session.getEmpresa().getCodEmpresa());
                ps3.executeUpdate();

            }

            String sql4 = "INSERT INTO hosp.atendimentos(codpaciente, codmedico, situacao, dtaatende, codtipoatendimento, turno, observacao, ativo, " +
                    "id_paciente_instituicao, cod_empresa, horario, encaixe)"
                    + " VALUES (?, ?, 'A', ?, ?, ?, ?, 'S', ?, ?, ?, ?) RETURNING id_atendimento;";

            PreparedStatement ps4 = null;
            ps4 = conexao.prepareStatement(sql4);

            for (int i = 0; i < listaAgendamento.size(); i++) {

                ps4.setInt(1, insercao.getLaudo().getPaciente().getId_paciente());
                ps4.setLong(2, insercao.getFuncionario().getId());
                ps4.setDate(3, DataUtil.converterDateUtilParaDateSql(listaAgendamento.get(i)
                        .getAgenda().getDataMarcacao()));
                ps4.setInt(4, user_session.getEmpresa().getParametro().getTipoAtendimento().getIdTipo());
                ps4.setString(5, insercao.getAgenda().getTurno());
                ps4.setString(6, insercao.getObservacao());
                ps4.setInt(7, idPacienteInstituicao);
                ps4.setInt(8, user_session.getEmpresa().getCodEmpresa());
                if(insercao.getAgenda().getHorario() != null) {
                    ps4.setTime(9, DataUtil.retornarHorarioEmTime(insercao.getAgenda().getHorario()));
                }
                else{
                    ps4.setNull(9, Types.NULL);
                }

                ps4.setBoolean(10, insercao.getEncaixe());

                rs = ps4.executeQuery();

                int idAgend = 0;
                if (rs.next()) {
                    idAgend = rs.getInt("id_atendimento");
                }

                String sql5 = "INSERT INTO hosp.atendimentos1 (codprofissionalatendimento, id_atendimento, cbo, codprocedimento) VALUES  (?, ?, ?, ?)";

                PreparedStatement ps5 = null;
                ps5 = conexao.prepareStatement(sql5);

                ps5.setLong(1, insercao.getFuncionario().getId());
                ps5.setInt(2, idAgend);
                ps5.setInt(3, insercao.getFuncionario().getCbo().getCodCbo());
                ps5.setInt(4, insercao.getFuncionario().getProc1().getIdProc());

                ps5.executeUpdate();

            }

            if(gerenciarPacienteDAO.gravarHistoricoAcaoPaciente(idPacienteInstituicao, insercao.getObservacao(), "R", conexao)){
                conexao.commit();

                retorno = true;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return retorno;
        }
    }

}
