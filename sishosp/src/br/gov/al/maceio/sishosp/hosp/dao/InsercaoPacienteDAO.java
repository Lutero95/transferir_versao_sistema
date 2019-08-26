package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.DataUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.enums.MotivoLiberacao;
import br.gov.al.maceio.sishosp.hosp.model.*;
import br.gov.al.maceio.sishosp.hosp.model.dto.AvaliacaoInsercaoDTO;

import javax.faces.context.FacesContext;

public class InsercaoPacienteDAO {
    Connection con = null;
    PreparedStatement ps = null;

    public ArrayList<InsercaoPacienteBean> listarLaudosVigentes( String campoBusca, String tipoBusca)
            throws ProjetoException {

        ArrayList<InsercaoPacienteBean> lista = new ArrayList<>();

        String sql = "select nome, cns, id_laudo, " +
                "to_date(ano_inicio||'-'||'0'||''||mes_inicio||'-'||'01', 'YYYY-MM-DD') as datainicio, " +
                "(SELECT * FROM hosp.fn_GetLastDayOfMonth(to_date(ano_final||'-'||'0'||''||mes_final||'-'||'01', 'YYYY-MM-DD'))) as datafinal " +
                "from ( "
                + " select l.id_laudo, l.codpaciente, p.nome, p.cns,  l.data_solicitacao, l.mes_inicio, l.ano_inicio, l.mes_final, l.ano_final, l.periodo, "
                + " l.codprocedimento_primario, pr.nome as procedimento, l.cid1, ci.desccid,  "
                + " to_date(ano_inicio||'-'||'0'||''||mes_inicio||'-'||'01', 'YYYY-MM-DD') as datainicio,  "
                + " (SELECT * FROM hosp.fn_GetLastDayOfMonth(to_date(ano_final||'-'||'0'||''||mes_final||'-'||'01', 'YYYY-MM-DD'))) as datafinal "
                + " from hosp.laudo l "
                + " left join hosp.pacientes p on (l.codpaciente = p.id_paciente) "
                + " left join hosp.proc pr on (l.codprocedimento_primario = pr.id) "
                + " left join hosp.cid ci on (l.cid1 = cast(ci.cod as integer)) "
                + " where 1=1 ";
        		if ((tipoBusca.equals("paciente") && (!campoBusca.equals(null)) && (!campoBusca.equals("")))) {
        			sql = sql + " and p.nome ilike ?";
        		}

        		if ((tipoBusca.equals("codproc") && (!campoBusca.equals(null)) && (!campoBusca.equals("")))) {
        			sql = sql + " and pr.codproc = ?";
        		}
                //current_date >= to_date(ano_inicio||'-'||'0'||''||mes_inicio||'-'||'01', 'YYYY-MM-DD') "
                //+ " and current_date <= (SELECT * FROM hosp.fn_GetLastDayOfMonth(to_date(ano_final||'-'||'0'||''||mes_final||'-'||'01', 'YYYY-MM-DD'))) "
           //     + " AND NOT EXISTS (SELECT pac.codlaudo FROM hosp.paciente_instituicao pac WHERE pac.codlaudo = l.id_laudo)"
                sql = sql+ " ) a";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
			if ((tipoBusca.equals("paciente") && (!campoBusca.equals(null)) && (!campoBusca.equals("")))) {
				stm.setString(1, "%" + campoBusca.toUpperCase() + "%");
			}

			if ((tipoBusca.equals("codproc") && (!campoBusca.equals(null)) && (!campoBusca.equals("")))) {
				stm.setString(1, campoBusca);
			}
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                InsercaoPacienteBean insercao = new InsercaoPacienteBean();
                insercao.getLaudo().setId(rs.getInt("id_laudo"));
                insercao.getLaudo().getPaciente().setNome(rs.getString("nome"));
                insercao.getLaudo().getPaciente().setCns(rs.getString("cns"));
                insercao.getLaudo().setVigenciaInicial(rs.getDate("datainicio"));
                insercao.getLaudo().setVigenciaFinal(rs.getDate("datafinal"));

                lista.add(insercao);
            }
        } catch (Exception ex) {
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

    public InsercaoPacienteBean carregarLaudoPaciente(int id)
            throws ProjetoException {

        InsercaoPacienteBean insercao = new InsercaoPacienteBean();

        String sql = "select l.id_laudo, l.codpaciente, p.nome, p.cns, l.data_solicitacao, l.mes_inicio, l.ano_inicio, "
                + " l.mes_final, l.ano_final, l.periodo, "
                + " l.codprocedimento_primario, pr.nome as procedimento, l.cid1, ci.desccid, "
                + " to_date(ano_inicio||'-'||'0'||''||mes_inicio||'-'||'01', 'YYYY-MM-DD') as datainicio,  "
                + " (SELECT * FROM hosp.fn_GetLastDayOfMonth(to_date(ano_final||'-'||'0'||''||mes_final||'-'||'01', 'YYYY-MM-DD'))) as datafinal "
                + " from hosp.laudo l "
                + " left join hosp.pacientes p on (l.codpaciente = p.id_paciente) "
                + " left join hosp.proc pr on (l.codprocedimento_primario = pr.id) "
                + " left join hosp.cid ci on (l.cid1 = cast(ci.cod as integer)) "
                + "  "
                + " where 1=1 "
                //current_date >= to_date(ano_inicio||'-'||'0'||''||mes_inicio||'-'||'01', 'YYYY-MM-DD') "
                //+ " and current_date <= (SELECT * FROM hosp.fn_GetLastDayOfMonth(to_date(ano_final||'-'||'0'||''||mes_final||'-'||'01', 'YYYY-MM-DD'))) "
                + " and l.id_laudo = ?";
        try {
            con = ConnectionFactory.getConnection();

            PreparedStatement stm = con.prepareStatement(sql);

            stm.setInt(1, id);

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                insercao = new InsercaoPacienteBean();
                insercao.getLaudo().setId(rs.getInt("id_laudo"));
                insercao.getLaudo().getPaciente()
                        .setId_paciente(rs.getInt("codpaciente"));
                insercao.getLaudo().getPaciente().setNome(rs.getString("nome"));
                insercao.getLaudo().getPaciente().setCns(rs.getString("cns"));
                insercao.getLaudo().setDataSolicitacao(
                        rs.getDate("data_solicitacao"));
                insercao.getLaudo().setMesInicio(rs.getInt("mes_inicio"));
                insercao.getLaudo().setAnoInicio(rs.getInt("ano_inicio"));
                insercao.getLaudo().setMesFinal(rs.getInt("mes_final"));
                insercao.getLaudo().setAnoFinal(rs.getInt("ano_final"));
                insercao.getLaudo().setPeriodo(rs.getInt("periodo"));
                insercao.getLaudo().getProcedimentoPrimario()
                        .setIdProc(rs.getInt("codprocedimento_primario"));
                insercao.getLaudo().getProcedimentoPrimario()
                        .setNomeProc(rs.getString("procedimento"));
                insercao.getLaudo().getCid1().setIdCid(rs.getInt("cid1"));
                insercao.getLaudo().getCid1()
                        .setDescCid(rs.getString("desccid"));
                insercao.getLaudo().setVigenciaInicial(
                        rs.getDate("datainicio"));
                insercao.getLaudo().setVigenciaFinal(rs.getDate("datafinal"));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return insercao;
    }

    public boolean gravarInsercaoEquipe(InsercaoPacienteBean insercao,
                                        List<FuncionarioBean> lista, ArrayList<InsercaoPacienteBean> listaAgendamento,
                                        ArrayList<Liberacao> listaLiberacao) throws ProjetoException {

        FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("obj_funcionario");

        Boolean retorno = false;
        int idAtendimento = 0;

        GerenciarPacienteDAO gerenciarPacienteDAO = new GerenciarPacienteDAO();

        String sql = "insert into hosp.paciente_instituicao (codprograma, codgrupo, codequipe, status, codlaudo, observacao, cod_unidade, data_solicitacao, data_cadastro) "
                + " values (?, ?,  ?, ?, ?, ?, ?, ?, current_timestamp) RETURNING id;";
        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, insercao.getPrograma().getIdPrograma());
            ps.setInt(2, insercao.getGrupo().getIdGrupo());
            ps.setInt(3, insercao.getEquipe().getCodEquipe());
            ps.setString(4, "A");
            ps.setInt(5, insercao.getLaudo().getId());
            ps.setString(6, insercao.getObservacao());
            ps.setInt(7, user_session.getUnidade().getId());
            ps.setDate(8, new java.sql.Date(insercao.getDataSolicitacao().getTime()));
            ResultSet rs = ps.executeQuery();
            int id = 0;
            if (rs.next()) {
                id = rs.getInt("id");
            }

            String sql2 = "INSERT INTO hosp.profissional_dia_atendimento (id_paciente_instituicao, id_profissional, dia_semana) VALUES  (?, ?, ?)";
            ps = con.prepareStatement(sql2);

            for (int i = 0; i < lista.size(); i++) {
                ps.setLong(1, id);
                ps.setLong(2, lista.get(i).getId());
                for (int j = 0; j < lista.get(i).getListDiasSemana().size(); j++) {
                    ps.setInt(
                            3,
                            Integer.parseInt(lista.get(i).getListDiasSemana()
                                    .get(j)));
                    ps.executeUpdate();
                }
            }

            String sql3 = "INSERT INTO hosp.atendimentos(codpaciente, codequipe, situacao, dtaatende, codtipoatendimento, turno, "
                    + " observacao, ativo, id_paciente_instituicao, cod_unidade, horario, encaixe, codatendente, dtamarcacao, codprograma, codgrupo)"
                    + " VALUES (?, ?, 'A', ?, ?, ?, ?, 'S', ?, ?, ?, ?, ?, current_timestamp, ?, ?) RETURNING id_atendimento;";

            PreparedStatement ps3 = null;
            ps3 = con.prepareStatement(sql3);

            for (int i = 0; i < listaAgendamento.size(); i++) {

                ps3.setInt(1, insercao.getLaudo().getPaciente().getId_paciente());
                ps3.setLong(2, insercao.getEquipe().getCodEquipe());
                ps3.setDate(3, DataUtil.converterDateUtilParaDateSql(listaAgendamento.get(i)
                        .getAgenda().getDataMarcacao()));
                ps3.setInt(4, user_session.getUnidade().getParametro().getTipoAtendimento().getIdTipo());

                if (insercao.getAgenda().getTurno() != null) {
                    ps3.setString(5, insercao.getAgenda().getTurno());
                } else {
                    ps3.setNull(5, Types.NULL);
                }

                ps3.setString(6, insercao.getObservacao());
                ps3.setInt(7, id);
                ps3.setInt(8, user_session.getUnidade().getId());

                if (insercao.getAgenda().getHorario() != null) {
                    ps3.setTime(9, DataUtil.retornarHorarioEmTime(insercao.getAgenda().getHorario()));
                } else {
                    ps3.setNull(9, Types.NULL);
                }

                ps3.setBoolean(10, insercao.getEncaixe());

                ps3.setInt(11, user_session.getCodigo());

                ps3.setLong(12, insercao.getPrograma().getIdPrograma());

                ps3.setLong(13, insercao.getGrupo().getIdGrupo());

                rs = ps3.executeQuery();

                idAtendimento = 0;
                if (rs.next()) {
                    idAtendimento = rs.getInt("id_atendimento");
                }

                for (int j = 0; j < lista.size(); j++) {

                    for (int h = 0; h < lista.get(j).getListDiasSemana().size(); h++) {

                        if (DataUtil.extrairDiaDeData(listaAgendamento.get(i).getAgenda().getDataMarcacao()) ==
                                Integer.parseInt(lista.get(j).getListDiasSemana().get(h))) {

                            String sql4 = "INSERT INTO hosp.atendimentos1 (codprofissionalatendimento, id_atendimento, cbo, codprocedimento) VALUES  (?, ?, ?, ?)";

                            PreparedStatement ps4 = null;
                            ps4 = con.prepareStatement(sql4);

                            ps4.setLong(1, lista.get(j).getId());
                            ps4.setInt(2, idAtendimento);
                            if(VerificadorUtil.verificarSeObjetoNuloOuZero(lista.get(j).getCbo().getCodCbo())){
                                ps4.setNull(3, Types.NULL);
                            }
                            else {
                                ps4.setInt(3, lista.get(j).getCbo().getCodCbo());
                            }
                            ps4.setInt(4, insercao.getPrograma().getProcedimento().getIdProc());

                            ps4.executeUpdate();
                        }
                    }
                }

            }

            if (gerenciarPacienteDAO.gravarHistoricoAcaoPaciente(id, insercao.getObservacao(), "I", con)) {

                if (!VerificadorUtil.verificarSeListaNuloOuVazia(Collections.singletonList(listaLiberacao))) {
                    GerenciarPacienteDAO gDao = new GerenciarPacienteDAO();
                    if (gDao.gravarLiberacao(id, listaLiberacao, idAtendimento, con)) {
                        con.commit();
                    }
                } else {
                    con.commit();
                }

                retorno = true;
            }

        } catch (Exception ex) {
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

    public boolean gravarInsercaoProfissional(InsercaoPacienteBean insercao,
                                              ArrayList<InsercaoPacienteBean> listaAgendamento) {

        FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("obj_funcionario");

        Boolean retorno = false;

        GerenciarPacienteDAO gerenciarPacienteDAO = new GerenciarPacienteDAO();

        String sql = "insert into hosp.paciente_instituicao (codprofissional, status, codlaudo, observacao, data_solicitacao, cod_empresa) "
                + " values (?, ?, ?, ?, ?, ?) RETURNING id;";
        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            ps.setLong(1, insercao.getFuncionario().getId());
            ps.setString(2, "A");
            ps.setInt(3, insercao.getLaudo().getId());
            ps.setString(4, insercao.getObservacao());
            ps.setDate(5, new java.sql.Date(insercao.getDataSolicitacao()
                    .getTime()));
            ps.setInt(6, user_session.getUnidade().getId());

            ResultSet rs = ps.executeQuery();
            int id = 0;
            if (rs.next()) {
                id = rs.getInt("id");
            }

            String sql2 = "INSERT INTO hosp.profissional_dia_atendimento (id_paciente_instituicao, id_profissional, dia_semana, cod_empresa) VALUES  (?, ?, ?, ?)";
            PreparedStatement ps2 = null;
            ps2 = con.prepareStatement(sql2);

            for (int i = 0; i < insercao.getFuncionario().getListDiasSemana()
                    .size(); i++) {
                ps2.setLong(1, id);
                ps2.setLong(2, insercao.getFuncionario().getId());
                ps2.setInt(
                        3,
                        Integer.parseInt(insercao.getFuncionario()
                                .getListDiasSemana().get(i)));
                ps2.setInt(4, user_session.getUnidade().getId());
                ps2.executeUpdate();

            }

            String sql3 = "INSERT INTO hosp.atendimentos(codpaciente, codmedico, situacao, dtaatende, codtipoatendimento, turno, observacao, ativo, " +
                    "id_paciente_instituicao, cod_empresa, horario, encaixe)"
                    + " VALUES (?, ?, 'A', ?, ?, ?, ?, 'S', ?, ?, ?, ?) RETURNING id_atendimento;";

            PreparedStatement ps3 = null;
            ps3 = con.prepareStatement(sql3);

            for (int i = 0; i < listaAgendamento.size(); i++) {

                ps3.setInt(1, insercao.getLaudo().getPaciente().getId_paciente());
                ps3.setLong(2, insercao.getFuncionario().getId());
                ps3.setDate(3, DataUtil.converterDateUtilParaDateSql(listaAgendamento.get(i)
                        .getAgenda().getDataMarcacao()));
                ps3.setInt(4, user_session.getUnidade().getParametro().getTipoAtendimento().getIdTipo());
                ps3.setString(5, insercao.getAgenda().getTurno());
                ps3.setString(6, insercao.getObservacao());
                ps3.setInt(7, id);
                ps3.setInt(8, user_session.getUnidade().getId());
                if (insercao.getAgenda().getHorario() != null) {
                    ps3.setTime(9, DataUtil.retornarHorarioEmTime(insercao.getAgenda().getHorario()));
                } else {
                    ps3.setNull(9, Types.NULL);
                }

                ps3.setBoolean(10, insercao.getEncaixe());

                rs = ps3.executeQuery();

                int idAgend = 0;
                if (rs.next()) {
                    idAgend = rs.getInt("id_atendimento");
                }

                String sql4 = "INSERT INTO hosp.atendimentos1 (codprofissionalatendimento, id_atendimento, cbo, codprocedimento) VALUES  (?, ?, ?, ?)";

                PreparedStatement ps4 = null;
                ps4 = con.prepareStatement(sql4);

                ps4.setLong(1, insercao.getFuncionario().getId());
                ps4.setInt(2, idAgend);
                ps4.setInt(3, insercao.getFuncionario().getCbo().getCodCbo());
                ps4.setInt(4, insercao.getFuncionario().getProc1().getIdProc());

                ps4.executeUpdate();

            }

            if (gerenciarPacienteDAO.gravarHistoricoAcaoPaciente(id, insercao.getObservacao(), "I", con)) {
                con.commit();

                retorno = true;
            }


        } catch (Exception ex) {
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

    public Date dataFinalLaudo(int id) throws ProjetoException {

        Date data = null;

        String sql = "select (SELECT * FROM hosp.fn_GetLastDayOfMonth(to_date(ano_final||'-'||'0'||''||mes_final||'-'||'01', 'YYYY-MM-DD'))) as datafinal "
                + " from hosp.laudo l where l.id_laudo = ?";

        try {
            con = ConnectionFactory.getConnection();

            PreparedStatement stm = con.prepareStatement(sql);

            stm.setInt(1, id);

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                data = rs.getDate("datafinal");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return data;
    }

    public Boolean verificarSeLaudoConstaNoAtendimento(int codLaudo) throws ProjetoException {

        Boolean retorno = false;

        String sql = "SELECT id_atendimento FROM hosp.atendimentos WHERE cod_laudo = ?";

        try {
            con = ConnectionFactory.getConnection();

            PreparedStatement stm = con.prepareStatement(sql);

            stm.setInt(1, codLaudo);

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                retorno = true;
            }

        } catch (Exception ex) {
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

    public Boolean verificarSeAlgumAtendimentoDoLaudoTemPerfilAvaliacao(int codLaudo) throws ProjetoException {

        Boolean retorno = false;

        String sql = "SELECT a.id_atendimento " +
                "FROM hosp.atendimentos a " +
                "JOIN hosp.atendimentos1 a1 ON (a.id_atendimento = a1.id_atendimento) " +
                "WHERE cod_laudo = ? AND a1.perfil_avaliacao = 'S';";

        try {
            con = ConnectionFactory.getConnection();

            PreparedStatement stm = con.prepareStatement(sql);

            stm.setInt(1, codLaudo);

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                retorno = true;
            }

        } catch (Exception ex) {
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

    public Boolean verificarSeAlgumAtendimentoNaoFoiLancadoPerfil(int codLaudo) throws ProjetoException {

        Boolean retorno = false;

        String sql = "SELECT a.id_atendimento " +
                "FROM hosp.atendimentos a " +
                "JOIN hosp.atendimentos1 a1 ON (a.id_atendimento = a1.id_atendimento) " +
                "WHERE cod_laudo = ? AND (a1.perfil_avaliacao = '' OR a1.perfil_avaliacao IS NULL);";

        try {
            con = ConnectionFactory.getConnection();

            PreparedStatement stm = con.prepareStatement(sql);

            stm.setInt(1, codLaudo);

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                retorno = true;
            }

        } catch (Exception ex) {
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

    public AvaliacaoInsercaoDTO carregarAtendimentoAvaliacao(int codLaudo) {

        AvaliacaoInsercaoDTO avaliacaoInsercaoDTO = new AvaliacaoInsercaoDTO();

        String sql = "SELECT a.codprograma, a.grupo_avaliacao, a.codequipe, p.cod_procedimento " +
                "FROM hosp.atendimentos a " +
                "JOIN hosp.programa p ON (a.codprograma = p.id_programa) " +
                "WHERE a.cod_laudo = ?;";

        try {
            con = ConnectionFactory.getConnection();

            PreparedStatement stm = con.prepareStatement(sql);

            stm.setInt(1, codLaudo);

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                ProgramaDAO programaDAO = new ProgramaDAO();
                GrupoDAO grupoDAO = new GrupoDAO();
                EquipeDAO equipeDAO = new EquipeDAO();
                ProcedimentoDAO procedimentoDAO = new ProcedimentoDAO();

                avaliacaoInsercaoDTO.setProgramaBean(programaDAO.listarProgramaPorIdComConexao(rs.getInt("codprograma"), con));
                avaliacaoInsercaoDTO.setGrupoBean(grupoDAO.listarGrupoPorIdComConexao(rs.getInt("grupo_avaliacao"), con));
                avaliacaoInsercaoDTO.setEquipeBean(equipeDAO.buscarEquipePorIDComConexao(rs.getInt("codequipe"), con));
                avaliacaoInsercaoDTO.getProgramaBean().setProcedimento(procedimentoDAO.listarProcedimentoPorIdComConexao(rs.getInt("cod_procedimento"), con));
                avaliacaoInsercaoDTO.setListaProfissionais(listarProfissionaisDaAvaliacaoQueOPacienteTemPerfil(codLaudo, con));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return avaliacaoInsercaoDTO;
    }

    public ArrayList<FuncionarioBean> listarProfissionaisDaAvaliacaoQueOPacienteTemPerfil(int codLaudo, Connection conAuxiliar) {

        ArrayList<FuncionarioBean> lista = new ArrayList<>();

        String sql = "SELECT a1.codprofissionalatendimento, f.descfuncionario, e.descespecialidade, f.codespecialidade " +
                "FROM hosp.atendimentos a " +
                "JOIN hosp.atendimentos1 a1 ON (a.id_atendimento = a1.id_atendimento) " +
                "JOIN acl.funcionarios f ON (a1.codprofissionalatendimento = f.id_funcionario) " +
                "JOIN hosp.especialidade e ON (f.codespecialidade = e.id_especialidade) " +
                "WHERE a.cod_laudo = ? AND a1.perfil_avaliacao = 'S' " +
                "ORDER BY f.descfuncionario";

        try {

            PreparedStatement stm = conAuxiliar.prepareStatement(sql);

            stm.setInt(1, codLaudo);

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
               FuncionarioBean funcionarioBean = new FuncionarioBean();
               funcionarioBean.setNome(rs.getString("descfuncionario"));
               funcionarioBean.setId(rs.getLong("codprofissionalatendimento"));
               funcionarioBean.getEspecialidade().setDescEspecialidade(rs.getString("descespecialidade"));
               funcionarioBean.getEspecialidade().setCodEspecialidade(rs.getInt("codespecialidade"));

               lista.add(funcionarioBean);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return lista;
    }

}
