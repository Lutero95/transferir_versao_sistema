package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.DataUtil;
import br.gov.al.maceio.sishosp.comum.util.TratamentoErrosUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.abstracts.VetorDiaSemanaAbstract;
import br.gov.al.maceio.sishosp.hosp.enums.MotivoLiberacao;
import br.gov.al.maceio.sishosp.hosp.enums.TipoDataAgenda;
import br.gov.al.maceio.sishosp.hosp.model.AgendaBean;
import br.gov.al.maceio.sishosp.hosp.model.ConfigAgendaParte1Bean;
import br.gov.al.maceio.sishosp.hosp.model.HorarioAtendimento;
import br.gov.al.maceio.sishosp.hosp.model.InsercaoPacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.dto.PacientesComInformacaoAtendimentoDTO;

public class AgendaDAO extends VetorDiaSemanaAbstract {
    private static final String NAO = "N";

	private static final String SIM = "S";

	private static final String SEGUNDOS = ":00";
    
    Connection con = null;
    PreparedStatement ps = null;

    public boolean gravarAgenda(AgendaBean agenda, List<AgendaBean> listaNovosAgendamentos,
                                Integer funcionarioLiberacao) throws ProjetoException {

        Boolean retorno = false;
        int idAtendimento = 0;

        String sql = "INSERT INTO hosp.atendimentos(codpaciente, codmedico, codprograma,"
                + " dtaatende, situacao, dtamarcacao, codtipoatendimento,"
                + " turno, codequipe, observacao, ativo, cod_unidade, codgrupo, encaixe, funcionario_liberacao, horario, avaliacao, codatendente)"
                + " VALUES " + "(?, ?, ?, ?, ?," + " current_timestamp , ?, ?, ?, ?,"
                + " ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id_atendimento;";
        try {
            con = ConnectionFactory.getConnection();

            for (int i = 0; i < listaNovosAgendamentos.size(); i++) {
                ps = con.prepareStatement(sql);

                ps.setInt(1, agenda.getPaciente().getId_paciente());
                if (agenda.getProfissional().getId() != null) {
                    ps.setLong(2, agenda.getProfissional().getId());
                } else {
                    ps.setNull(2, Types.NULL);
                }
                if (agenda.getAvaliacao()) {
                    ps.setInt(3, agenda.getProgramaAvaliacao().getIdPrograma());
                } else {
                    ps.setInt(3, agenda.getPrograma().getIdPrograma());
                }
                ps.setDate(4, new java.sql.Date(listaNovosAgendamentos.get(i).getDataAtendimento().getTime()));
                ps.setString(5, "A");
                ps.setInt(6, agenda.getTipoAt().getIdTipo());

                if (agenda.getTurno() != null) {
                    ps.setString(7, agenda.getTurno().toUpperCase());
                } else {
                    ps.setNull(7, Types.NULL);
                }

                if (agenda.getEquipe().getCodEquipe() != null) {
                    ps.setInt(8, agenda.getEquipe().getCodEquipe());
                } else {
                    ps.setNull(8, Types.NULL);
                }
                ps.setString(9, agenda.getObservacao().toUpperCase());
                ps.setString(10, SIM);
                ps.setInt(11, agenda.getUnidade().getId());

                if (!VerificadorUtil.verificarSeObjetoNulo(agenda.getGrupo().getIdGrupo())) {
                    ps.setInt(12, agenda.getGrupo().getIdGrupo());
                } else {
                    ps.setNull(12, Types.NULL);
                }

                ps.setBoolean(13, agenda.getEncaixe());
                if (funcionarioLiberacao > 0) {
                    ps.setLong(14, funcionarioLiberacao);
                } else {
                    ps.setNull(14, Types.NULL);
                }

                if (agenda.getHorario() != null) {
                    ps.setTime(15, DataUtil.retornarHorarioEmTime(agenda.getHorario()));
                } else {
                    ps.setNull(15, Types.NULL);
                }

                ps.setBoolean(16, agenda.getAvaliacao());

                FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
                        .getSessionMap().get("obj_funcionario");
                ps.setLong(17, user_session.getId());

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    idAtendimento = rs.getInt("id_atendimento");
                }

                for (int j = 0; j < listaNovosAgendamentos.size(); j++) {

                    Integer idPrograma;
                    if (agenda.getAvaliacao()) {
                        idPrograma = agenda.getProgramaAvaliacao().getIdPrograma();
                    } else {
                        idPrograma = agenda.getPrograma().getIdPrograma();
                    }

                    String sql2 = "INSERT INTO hosp.atendimentos1 (codprofissionalatendimento, id_atendimento, "
                            + " cbo, codprocedimento) VALUES  (?, ?, ?, ?)";
                    ps = con.prepareStatement(sql2);
                    if (agenda.getProfissional().getId() != null) {

                        Integer idProcedimentoEspecifico = 
                        		retornaIdProcedimentoEspecifico(idPrograma, agenda.getProfissional().getCbo().getCodCbo(), agenda.getPaciente().getId_paciente(), con);
                        
                        ps.setLong(1, agenda.getProfissional().getId());
                        ps.setInt(2, idAtendimento);
                        if (!VerificadorUtil.verificarSeObjetoNuloOuZero(agenda.getProfissional().getCbo().getCodCbo()))
                            ps.setInt(3, agenda.getProfissional().getCbo().getCodCbo());
                        else
                            ps.setNull(3, Types.NULL);


                        if(!VerificadorUtil.verificarSeObjetoNuloOuZero(idProcedimentoEspecifico)) {
                            ps.setInt(4, idProcedimentoEspecifico);
                        }
                        else if (agenda.getAvaliacao()) {
                            ps.setInt(4, agenda.getProgramaAvaliacao().getProcedimento().getIdProc());
                        } else {
                            ps.setInt(4, agenda.getPrograma().getProcedimento().getIdProc());
                        }
                        ps.executeUpdate();
                    } else if (agenda.getEquipe().getCodEquipe() != null) {
                        agenda.getEquipe().setProfissionais(new EquipeDAO().listarProfissionaisDaEquipe(agenda.getEquipe().getCodEquipe()));

                        for (FuncionarioBean prof : agenda.getEquipe().getProfissionais()) {

                            Integer idProcedimentoEspecifico = 
                            		retornaIdProcedimentoEspecifico(idPrograma, prof.getCbo().getCodCbo(), agenda.getPaciente().getId_paciente(), con);

                            ps.setLong(1, prof.getId());
                            ps.setInt(2, idAtendimento);
                            if (!VerificadorUtil.verificarSeObjetoNuloOuZero(prof.getCbo().getCodCbo()))
                                ps.setInt(3, prof.getCbo().getCodCbo());
                            else
                                ps.setNull(3, Types.NULL);


                            if(!VerificadorUtil.verificarSeObjetoNuloOuZero(idProcedimentoEspecifico)) {
                                ps.setInt(4, idProcedimentoEspecifico);
                            }
                            else if (agenda.getAvaliacao()) {
                                if (!VerificadorUtil.verificarSeObjetoNuloOuZero(agenda.getProgramaAvaliacao().getProcedimento().getIdProc()))
                                    ps.setInt(4, agenda.getProgramaAvaliacao().getProcedimento().getIdProc());
                                else
                                    ps.setNull(4, Types.NULL);
                            } else {
                                if (!VerificadorUtil.verificarSeObjetoNuloOuZero(agenda.getPrograma().getProcedimento().getIdProc()))
                                    ps.setInt(4, agenda.getPrograma().getProcedimento().getIdProc());
                                else
                                    ps.setNull(4, Types.NULL);
                            }
                            ps.executeUpdate();
                        }
                    }

                }
            }
            con.commit();

            retorno = true;
        } catch (SQLException ex2) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return retorno;
    }

    public boolean gravarAgendaAvulsa(AgendaBean agenda, List<FuncionarioBean> listaProfissionais, 
    		FuncionarioBean usuarioLiberacao, List<String> listaMotivosLiberacao)
            throws ProjetoException {

        Boolean retorno = false;
        int idAtendimento = 0;

        String sql = "INSERT INTO hosp.atendimentos(codpaciente,  codprograma,"
                + " dtaatende, situacao, dtamarcacao, codtipoatendimento,"
                + " turno,  observacao, ativo, cod_unidade, codgrupo, encaixe, funcionario_liberacao, horario,  avulso, codatendente)"
                + " VALUES " + "(?, ?, ?, ?, current_timestamp ," + " ?, ?, ?, ?, ?,"
                + " ?, ?, ?, ?,  true, ?) RETURNING id_atendimento;";
        try {
            con = ConnectionFactory.getConnection();

            ps = con.prepareStatement(sql);

            ps.setInt(1, agenda.getPaciente().getId_paciente());
            ps.setInt(2, agenda.getPrograma().getIdPrograma());

            ps.setDate(3, new java.sql.Date(agenda.getDataAtendimento().getTime()));
            ps.setString(4, "A");
            ps.setInt(5, agenda.getTipoAt().getIdTipo());

            if (agenda.getTurno() != null) {
                ps.setString(6, agenda.getTurno().toUpperCase());
            } else {
                ps.setNull(6, Types.NULL);
            }

            ps.setString(7, agenda.getObservacao().toUpperCase());
            ps.setString(8, SIM);
            ps.setInt(9, agenda.getUnidade().getId());

            if (!VerificadorUtil.verificarSeObjetoNulo(agenda.getGrupo().getIdGrupo())) {
                ps.setInt(10, agenda.getGrupo().getIdGrupo());
            } else {
                ps.setNull(10, Types.NULL);
            }

            ps.setBoolean(11, false);

            ps.setNull(12, Types.NULL);


            if (agenda.getHorario() != null) {
                ps.setTime(13, DataUtil.retornarHorarioEmTime(agenda.getHorario()));
            } else {
                ps.setNull(13, Types.NULL);
            }
            FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
                    .getSessionMap().get("obj_funcionario");
            ps.setLong(14, user_session.getId());

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                idAtendimento = rs.getInt("id_atendimento");
            }

            for (FuncionarioBean funcionario : listaProfissionais) {
                String sql2 = "INSERT INTO hosp.atendimentos1 (codprofissionalatendimento, id_atendimento, "
                        + " cbo, codprocedimento, horario_atendimento, id_cidprimario) VALUES  (?, ?, ?, ?, ?, ?)";
                Integer idProcedimentoEspecifico = 
                		retornaIdProcedimentoEspecifico(agenda.getPrograma().getIdPrograma(), funcionario.getCbo().getCodCbo(), agenda.getPaciente().getId_paciente(), con);
                ps = con.prepareStatement(sql2);
                ps.setLong(1, funcionario.getId());
                ps.setInt(2, idAtendimento);
                if (!VerificadorUtil.verificarSeObjetoNuloOuZero(funcionario.getCbo().getCodCbo()))
                    ps.setInt(3, funcionario.getCbo().getCodCbo());
                else
                    ps.setNull(3, Types.NULL);

                if(!VerificadorUtil.verificarSeObjetoNuloOuZero(idProcedimentoEspecifico))
                    ps.setInt(4, idProcedimentoEspecifico);
                else if (!VerificadorUtil.verificarSeObjetoNuloOuZero(agenda.getPrograma().getProcedimento().getIdProc()))
                    ps.setInt(4, agenda.getPrograma().getProcedimento().getIdProc());
                else
                    ps.setNull(4, Types.NULL);

                if(VerificadorUtil.verificarSeObjetoNuloOuVazio(agenda.getHorario()))
                    ps.setNull(5, Types.NULL);
                else {
                    Time horario = Time.valueOf(agenda.getHorario()+SEGUNDOS);
                    ps.setTime(5, horario);
                }

                if(VerificadorUtil.verificarSeObjetoNuloOuZero(agenda.getIdCidPrimario()))
                    ps.setNull(6, Types.NULL);
                else
                    ps.setInt(6, agenda.getIdCidPrimario());

                ps.executeUpdate();

            }

            if(!VerificadorUtil.verificarSeObjetoNulo(usuarioLiberacao) 
            		&& !VerificadorUtil.verificarSeObjetoNuloOuZero(usuarioLiberacao.getId())) {
            	for (String motivo : listaMotivosLiberacao) {
            		gravarLiberacaoDuplicidadeAgendaAvulsa(con, idAtendimento, usuarioLiberacao, motivo);					
				}
            }

            con.commit();

            retorno = true;
        } catch (SQLException ex2) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return retorno;
    }
    
    
    public boolean gravarAgendamentosInformandoAtendimento(AgendaBean agenda, List<PacientesComInformacaoAtendimentoDTO> listaPacientesComInformacaoAtendimentoDTO,
    		FuncionarioBean usuarioLiberacao, List<String> listaMotivosLiberacao)
            throws ProjetoException {

			
    	
        Boolean retorno = false;
        int idAtendimento = 0;

        String sql = "INSERT INTO hosp.atendimentos(codpaciente,  codprograma,"
                + " dtaatende, situacao, dtamarcacao, codtipoatendimento,"
                + " turno,  observacao, ativo, cod_unidade, codgrupo, encaixe, "
                + " funcionario_liberacao, horario,  avulso, codatendente, presenca)"
                + " VALUES " + "(?, ?, ?, ?, current_timestamp ," + " ?, ?, ?, ?, ?,"
                + " ?, ?, ?, ?,  true, ?, ?) RETURNING id_atendimento;";
        try {
        	con = ConnectionFactory.getConnection();
        	for (PacientesComInformacaoAtendimentoDTO pacienteComInformacaoAtendimentoDTO : listaPacientesComInformacaoAtendimentoDTO) {

				ps = con.prepareStatement(sql);

				ps.setInt(1, pacienteComInformacaoAtendimentoDTO.getPaciente().getId_paciente());
				ps.setInt(2, agenda.getPrograma().getIdPrograma());

				ps.setDate(3, new java.sql.Date(agenda.getDataAtendimento().getTime()));
				ps.setString(4, "A");
				ps.setInt(5, agenda.getTipoAt().getIdTipo());

				if (agenda.getTurno() != null) {
					ps.setString(6, agenda.getTurno().toUpperCase());
				} else {
					ps.setNull(6, Types.NULL);
				}

				ps.setString(7, agenda.getObservacao().toUpperCase());
				ps.setString(8, SIM);
				ps.setInt(9, agenda.getUnidade().getId());

				if (!VerificadorUtil.verificarSeObjetoNulo(agenda.getGrupo().getIdGrupo())) {
					ps.setInt(10, agenda.getGrupo().getIdGrupo());
				} else {
					ps.setNull(10, Types.NULL);
				}

				ps.setBoolean(11, false);

				ps.setNull(12, Types.NULL);

				if (agenda.getHorario() != null) {
					ps.setTime(13, DataUtil.retornarHorarioEmTime(agenda.getHorario()));
				} else {
					ps.setNull(13, Types.NULL);
				}
				FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
						.getSessionMap().get("obj_funcionario");
				ps.setLong(14, user_session.getId());
				
				if(pacienteComInformacaoAtendimentoDTO.isPresenca())
					ps.setString(15, SIM);
				else
					ps.setString(15, NAO);

				ResultSet rs = ps.executeQuery();

				if (rs.next()) {
					idAtendimento = rs.getInt("id_atendimento");
				}

				String sql2 = "INSERT INTO hosp.atendimentos1 (codprofissionalatendimento, id_atendimento, "
						+ " cbo, codprocedimento, horario_atendimento, id_cidprimario) VALUES  (?, ?, ?, ?, ?, ?)";

				Integer idProcedimentoEspecifico = retornaIdProcedimentoCboEspecifico(agenda.getPrograma().getIdPrograma(),
						agenda.getProfissional().getCbo().getCodCbo(), con);
				ps = con.prepareStatement(sql2);
				ps.setLong(1, agenda.getProfissional().getId());
				ps.setInt(2, idAtendimento);
				if (!VerificadorUtil.verificarSeObjetoNuloOuZero(agenda.getProfissional().getCbo().getCodCbo()))
					ps.setInt(3, agenda.getProfissional().getCbo().getCodCbo());
				else
					ps.setNull(3, Types.NULL);

				if (!VerificadorUtil.verificarSeObjetoNuloOuZero(idProcedimentoEspecifico))
					ps.setInt(4, idProcedimentoEspecifico);
				else if (!VerificadorUtil
						.verificarSeObjetoNuloOuZero(agenda.getPrograma().getProcedimento().getIdProc()))
					ps.setInt(4, agenda.getPrograma().getProcedimento().getIdProc());
				else
					ps.setNull(4, Types.NULL);

				if (VerificadorUtil.verificarSeObjetoNuloOuVazio(agenda.getHorario()))
					ps.setNull(5, Types.NULL);
				else {
					Time horario = Time.valueOf(agenda.getHorario() + SEGUNDOS);
					ps.setTime(5, horario);
				}

				if (VerificadorUtil.verificarSeObjetoNuloOuZero(pacienteComInformacaoAtendimentoDTO.getIdCidPrimario()))
					ps.setNull(6, Types.NULL);
				else
					ps.setInt(6, pacienteComInformacaoAtendimentoDTO.getIdCidPrimario());

				ps.executeUpdate();

				if (!VerificadorUtil.verificarSeObjetoNulo(usuarioLiberacao) 
						&& !VerificadorUtil.verificarSeObjetoNuloOuZero(usuarioLiberacao.getId())) {
					
					for (String motivo : listaMotivosLiberacao) {
						if(pacienteComInformacaoAtendimentoDTO.isDuplicidadeEspecialidade() 
								&& motivo.equals(MotivoLiberacao.DUPLICIDADE_ESPECIALIDADE.getSigla())) {
							gravarLiberacaoDuplicidadeAgendaAvulsa(con, idAtendimento, usuarioLiberacao, motivo);
						}
						else if (motivo.equals(MotivoLiberacao.DUPLICIDADE_AGENDA_AVULSA.getSigla()))
							gravarLiberacaoDuplicidadeAgendaAvulsa(con, idAtendimento, usuarioLiberacao, motivo);	
					}
				}
        	}
            con.commit();

            retorno = true;
        } catch (SQLException ex2) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return retorno;
    }
    

    private void gravarLiberacaoDuplicidadeAgendaAvulsa(Connection conexao, Integer idAtendimento,
                                                        FuncionarioBean usuarioLiberacao, String motivoLiberacao) throws SQLException, ProjetoException {

        String sql = "INSERT INTO hosp.liberacoes "
                + "(motivo, usuario_liberacao, data_hora_liberacao, codatendimento, cod_unidade) "
                + "VALUES(?, ?, CURRENT_TIMESTAMP, ?, ?); ";
        try {
            PreparedStatement stm = conexao.prepareStatement(sql);
            stm.setString(1, motivoLiberacao);
            stm.setLong(2, usuarioLiberacao.getId());
            stm.setInt(3, idAtendimento);
            stm.setInt(4, usuarioLiberacao.getUnidade().getId());
            stm.executeUpdate();
        } catch (SQLException ex2) {
            conexao.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
        } catch (Exception ex) {
            conexao.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
    }

    public boolean excluirAgendamento(AgendaBean agenda) throws ProjetoException {
        Boolean retorno = false;
        String sql = "delete from hosp.atendimentos where id_atendimento = ?";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setLong(1, agenda.getIdAgenda());
            stmt.execute();
            con.commit();
            excluirTabelaAgendamentos1(agenda);
            retorno = true;
        } catch (SQLException ex2) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return retorno;
    }

    public boolean mudaStatusPresenca(AgendaBean agenda) throws ProjetoException {
        Boolean retorno = false;
        String sql = "update hosp.atendimentos set presenca=? where id_atendimento = ?";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            if (agenda.getPresenca().equals(SIM))
                stmt.setString(1, NAO);
            if (agenda.getPresenca().equals(NAO))
                stmt.setString(1, SIM);
            stmt.setLong(2, agenda.getIdAgenda());
            stmt.execute();
            con.commit();
            retorno = true;
        } catch (SQLException ex2) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return retorno;
    }

    public void excluirTabelaAgendamentos1(AgendaBean agenda) throws ProjetoException {
        String sql = "delete from hosp.atendimentos1 where id_atendimento = ?";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setLong(1, agenda.getIdAgenda());
            stmt.execute();
            con.commit();
        } catch (SQLException ex2) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public int verQtdMaxAgendaDataEspecifica(AgendaBean agenda) throws ProjetoException {
        int qtdMax = 0;
        String sqlPro = "select p.qtdmax " + "from hosp.config_agenda_profissional p "
                + "left join hosp.config_agenda_profissional_dias d on (p.id_configagenda = d.id_config_agenda_profissional) "
                + "where p.codmedico = ? and d.data_especifica = ? ";

        String sqlEqui = "select p.qtdmax " + "from hosp.config_agenda_equipe p "
                + "left join hosp.config_agenda_equipe_dias d on (p.id_configagenda = d.id_config_agenda_equipe) "
                + "where p.codequipe = ? and d.data_especifica = ? ";

        String sqlEquiAva = " select p.qtdmax from hosp.config_agenda_equipe p "
                + "left join hosp.config_agenda_equipe_dias d on (p.id_configagenda = d.id_config_agenda_equipe) "
                + "JOIN hosp.tipo_atend_agenda t ON (p.id_configagenda = t.cod_config_agenda_equipe) "
                + "join hosp.tipoatendimento  on tipoatendimento.id = t.codtipoatendimento "
                + "where p.codequipe = ? and d.data_especifica = ? "
                + "and tipoatendimento.primeiroatendimento is true";

        String sqlComplementoFinal = "";

        if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(agenda.getTurno())) {
            sqlComplementoFinal = "AND ? ::time between d.horario_inicio AND d.horario_final";
        } else {
            sqlComplementoFinal = "AND d.turno = ?";

        }

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = null;
            if (agenda.getProfissional().getId() != null) {
                stm = con.prepareStatement(sqlPro+sqlComplementoFinal);
                stm.setLong(1, agenda.getProfissional().getId());
            } else if ((agenda.getTipoAt().isPrimeiroAt() == false) && (agenda.getEquipe().getCodEquipe() != null)) {
                stm = con.prepareStatement(sqlEqui+sqlComplementoFinal);
                stm.setInt(1, agenda.getEquipe().getCodEquipe());
            } else if ((agenda.getTipoAt().isPrimeiroAt() == true) && (agenda.getEquipe().getCodEquipe() != null)) {
                stm = con.prepareStatement(sqlEquiAva+sqlComplementoFinal);
                stm.setInt(1, agenda.getEquipe().getCodEquipe());
            }
            stm.setDate(2, new java.sql.Date(agenda.getDataAtendimento().getTime()));
            if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(agenda.getTurno())) {
                stm.setTime(3, DataUtil.retornarHorarioEmTime(agenda.getHorario()));
            } else {
                stm.setString(3, agenda.getTurno().toUpperCase());
            }
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                qtdMax = rs.getInt("qtdmax");
            }
        } catch (SQLException ex2) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return qtdMax;
    }

    public int verQtdMaxAgendaDataEspecificaPorTipoAtendimento(AgendaBean agenda) throws ProjetoException {
        int qtdMax = 0;
        String sqlPro = "SELECT distinct t.qtd FROM hosp.config_agenda_profissional c "
                + "LEFT JOIN hosp.config_agenda_profissional_dias d on (c.id_configagenda = d.id_config_agenda_profissional) "
                + "JOIN hosp.tipo_atend_agenda t ON (c.id_configagenda = t.cod_config_agenda_profissional) "
                + "WHERE c.codmedico = ? AND d.data_especifica = ? and t.codtipoatendimento=? ";

        String sqlEqui = "SELECT distinct t.qtd FROM hosp.config_agenda_equipe c "
                + "LEFT JOIN hosp.config_agenda_equipe_dias d on (c.id_configagenda = d.id_config_agenda_equipe) "
                + "JOIN hosp.tipo_atend_agenda t ON (c.id_configagenda = t.cod_config_agenda_equipe) "
                + "WHERE c.codequipe = ? AND d.data_especifica = ? and t.codtipoatendimento=? ";

        String sqlEquiAva = "SELECT distinct t.qtd FROM hosp.config_agenda_equipe c \n"
                + "LEFT JOIN hosp.config_agenda_equipe_dias d on (c.id_configagenda = d.id_config_agenda_equipe) \n"
                + "JOIN hosp.tipo_atend_agenda t ON (c.id_configagenda = t.cod_config_agenda_equipe) \n"
                + "join hosp.tipoatendimento  on tipoatendimento.id = t.codtipoatendimento \n"
                + " WHERE c.codequipe =? AND d.data_especifica =? and t.codtipoatendimento=? and tipoatendimento.primeiroatendimento is true ";

        String sqlComplementoFinal = "";

        if (VerificadorUtil.verificarSeObjetoNuloOuVazio(agenda.getTurno())) {
            sqlComplementoFinal = "AND ? ::time between d.horario_inicio AND d.horario_final";
        } else {
            sqlComplementoFinal = "AND d.turno = ?";

        }

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = null;
            if (agenda.getProfissional().getId() != null) {
                stm = con.prepareStatement(sqlPro+sqlComplementoFinal);
                stm.setLong(1, agenda.getProfissional().getId());
            } else if ((agenda.getTipoAt().isPrimeiroAt() == false) && (agenda.getEquipe().getCodEquipe() != null)) {
                stm = con.prepareStatement(sqlEqui+sqlComplementoFinal);
                stm.setInt(1, agenda.getEquipe().getCodEquipe());
            } else if ((agenda.getTipoAt().isPrimeiroAt() == true) && (agenda.getEquipe().getCodEquipe() != null)) {
                stm = con.prepareStatement(sqlEquiAva+sqlComplementoFinal);
                stm.setInt(1, agenda.getEquipe().getCodEquipe());
            }

            stm.setDate(2, new java.sql.Date(agenda.getDataAtendimento().getTime()));

            stm.setInt(3, agenda.getTipoAt().getIdTipo());

            if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(agenda.getTurno())) {
                stm.setString(4, agenda.getHorario());
            } else {
                stm.setString(4, agenda.getTurno().toUpperCase());
            }

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                qtdMax = rs.getInt("qtd");
            }
        } catch (SQLException ex2) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return qtdMax;
    }

    public boolean verificarSeExisteConfigAgendaDiaSemanaGeralPorTipoAtendimento(AgendaBean agenda)
            throws ProjetoException {

        boolean retorno = false;

        String sqlPro = "SELECT distinct coalesce(t.qtd) qtd FROM hosp.config_agenda_profissional c \n"
                + "LEFT JOIN hosp.config_agenda_profissional_dias d on (c.id_configagenda = d.id_config_agenda_profissional) \n"
                + "JOIN hosp.tipo_atend_agenda t ON (c.id_configagenda = t.cod_config_agenda_profissional) \n"
                + "WHERE c.codmedico = ? and c.tipo='G' and t.codtipoatendimento=?";

        String sqlEqui = "SELECT distinct coalesce(t.qtd) qtd FROM hosp.config_agenda_equipe c \n"
                + "LEFT JOIN hosp.config_agenda_equipe_dias d on (c.id_configagenda = d.id_config_agenda_equipe) \n"
                + "JOIN hosp.tipo_atend_agenda t ON (c.id_configagenda = t.cod_config_agenda_equipe) \n"
                + "WHERE c.codequipe = ? and c.tipo='G' and t.codtipoatendimento=?";

        String sqlEquiAva = "SELECT distinct coalesce(t.qtd) qtd FROM hosp.config_agenda_equipe c \n"
                + "LEFT JOIN hosp.config_agenda_equipe_dias d on (c.id_configagenda = d.id_config_agenda_equipe) \n"
                + "JOIN hosp.tipo_atend_agenda t ON (c.id_configagenda = t.cod_config_agenda_equipe) \n"
                + " join hosp.tipoatendimento  on tipoatendimento.id = t.codtipoatendimento "
                + "WHERE c.codequipe = ? and c.tipo='G' and t.codtipoatendimento=? and tipoatendimento.primeiroatendimento is true";

        String sqlComplementoFinal = "";

        if (VerificadorUtil.verificarSeObjetoNuloOuVazio(agenda.getTurno())) {
            sqlComplementoFinal = " AND ? ::time between d.horario_inicio AND d.horario_final";
        } else {
            sqlComplementoFinal = " AND d.turno = ?";
        }

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = null;
            if (agenda.getProfissional().getId() != null) {
                stm = con.prepareStatement(sqlPro+sqlComplementoFinal);
                stm.setLong(1, agenda.getProfissional().getId());
            } else if ((agenda.getTipoAt().isPrimeiroAt() == false) && (agenda.getEquipe().getCodEquipe() != null)) {
                stm = con.prepareStatement(sqlEqui+sqlComplementoFinal);
                stm.setInt(1, agenda.getEquipe().getCodEquipe());
            } else if ((agenda.getTipoAt().isPrimeiroAt() == true) && (agenda.getEquipe().getCodEquipe() != null)) {
                stm = con.prepareStatement(sqlEquiAva+sqlComplementoFinal);
                stm.setInt(1, agenda.getEquipe().getCodEquipe());
            }
            stm.setInt(2, agenda.getTipoAt().getIdTipo());

            if (VerificadorUtil.verificarSeObjetoNuloOuVazio(agenda.getTurno())) {
                stm.setTime(3, DataUtil.retornarHorarioEmTime(agenda.getHorario()));
            } else {
                stm.setString(3, agenda.getTurno().toUpperCase());
            }

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                retorno = true;
            }

        } catch (SQLException ex2) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return retorno;
    }

    public boolean verificarSeExisteConfigAgendaDiaSemanaEspecificaPorTipoAtendimento(AgendaBean agenda)
            throws ProjetoException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(agenda.getDataAtendimento());
        int diaSemana = cal.get(Calendar.DAY_OF_WEEK);
        int mes = cal.get(Calendar.MONTH);
        int ano = cal.get(Calendar.YEAR);
        Boolean retorno = false;

        String sqlPro = "select distinct t.qtd " + "from hosp.config_agenda_profissional p "
                + "left join hosp.config_agenda_profissional_dias d on (p.id_configagenda = d.id_config_agenda_profissional) "
                + " JOIN hosp.tipo_atend_agenda t ON (p.id_configagenda = t.cod_config_agenda_profissional) "
                + "where p.codmedico = ? and d.dia = ? and d.turno = ? and p.mes = ? and p.ano=? and p.tipo='E' and t.codtipoatendimento=?";

        String sqlEqui = "select distinct t.qtd " + "from hosp.config_agenda_equipe p "
                + "left join hosp.config_agenda_equipe_dias d on (p.id_configagenda = d.id_config_agenda_equipe) "
                + " JOIN hosp.tipo_atend_agenda t ON (p.id_configagenda = t.cod_config_agenda_equipe) "
                + "where p.codequipe = ? and d.dia = ? and d.turno = ? and p.mes = ? and p.ano=? and p.tipo='E' and t.codtipoatendimento=?";

        String sqlEquiAva = "select distinct t.qtd from hosp.config_agenda_equipe p \n"
                + "left join hosp.config_agenda_equipe_dias d on (p.id_configagenda = d.id_config_agenda_equipe) \n"
                + " JOIN hosp.tipo_atend_agenda t ON (p.id_configagenda = t.cod_config_agenda_equipe) \n"
                + " join hosp.tipoatendimento  on tipoatendimento.id = t.codtipoatendimento \n"
                + "where p.codequipe = ? and d.dia = ? and d.turno = ? and p.mes = ? and p.ano=?\n"
                + "and p.tipo='E' and t.codtipoatendimento=?\n" + "and tipoatendimento.primeiroatendimento is true";

        String sqlComplementoFinal = "";

        if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(agenda.getTurno())) {
            sqlComplementoFinal = " AND ? ::time between d.horario_inicio AND d.horario_final";
        } else {
            sqlComplementoFinal = " AND d.turno = ?";

        }

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = null;
            if (agenda.getProfissional().getId() != null) {
                stm = con.prepareStatement(sqlPro+sqlComplementoFinal);
                stm.setLong(1, agenda.getProfissional().getId());
            } else if ((agenda.getTipoAt().isPrimeiroAt() == false) && (agenda.getEquipe().getCodEquipe() != null)) {
                stm = con.prepareStatement(sqlEqui+sqlComplementoFinal);
                stm.setInt(1, agenda.getEquipe().getCodEquipe());
            } else if ((agenda.getTipoAt().isPrimeiroAt() == true) && (agenda.getEquipe().getCodEquipe() != null)) {
                stm = con.prepareStatement(sqlEquiAva+sqlComplementoFinal);
                stm.setInt(1, agenda.getEquipe().getCodEquipe());
            }
            stm.setInt(2, diaSemana);

            if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(agenda.getTurno())) {
                stm.setTime(3, DataUtil.retornarHorarioEmTime(agenda.getHorario()));
            } else {
                stm.setString(3, agenda.getTurno().toUpperCase());
            }

            stm.setInt(4, mes + 1);
            stm.setInt(5, ano);
            stm.setInt(6, agenda.getTipoAt().getIdTipo());
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                retorno = true;
            }
        } catch (SQLException ex2) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return retorno;
    }

    public int verQtdMaxConfigAgendaDiaSemanaEspecifica(AgendaBean agenda) throws ProjetoException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(agenda.getDataAtendimento());
        int diaSemana = cal.get(Calendar.DAY_OF_WEEK);
        int mes = cal.get(Calendar.MONTH);
        int ano = cal.get(Calendar.YEAR);
        int qtd = 0;

        String sqlPro = "select distinct p.qtdmax " + "from hosp.config_agenda_profissional p "
                + "left join hosp.config_agenda_profissional_dias d on (p.id_configagenda = d.id_config_agenda_profissional) "
                + "where p.codmedico = ? and d.dia = ? and d.turno = ? and p.mes = ? and p.ano=? and p.tipo='E'";

        String sqlEqui = "select distinct p.qtdmax " + "from hosp.config_agenda_equipe p "
                + "left join hosp.config_agenda_equipe_dias d on (p.id_configagenda = d.id_config_agenda_equipe) "
                + "where p.codequipe = ? and d.dia = ? and d.turno = ? and p.mes = ? and p.ano=? and p.tipo='E'";

        String sqlEquiAva = "select distinct p.qtdmax from hosp.config_agenda_equipe p \n"
                + "left join hosp.config_agenda_equipe_dias d on (p.id_configagenda = d.id_config_agenda_equipe) \n"
                + "JOIN hosp.tipo_atend_agenda t ON (p.id_configagenda = t.cod_config_agenda_equipe) \n"
                + "join hosp.tipoatendimento  on tipoatendimento.id = t.codtipoatendimento \n"
                + "where p.codequipe = ? and d.dia = ? and d.turno = ? and p.mes = ? and p.ano=? and p.tipo='E'\n"
                + "and tipoatendimento.primeiroatendimento is true";

        String sqlComplementoFinal = "";

        if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(agenda.getTurno())) {
            sqlComplementoFinal = " AND ? ::time between d.horario_inicio AND d.horario_final";
        } else {
            sqlComplementoFinal = " AND d.turno = ?";

        }

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = null;
            if (agenda.getProfissional().getId() != null) {
                stm = con.prepareStatement(sqlPro+sqlComplementoFinal);
                stm.setLong(1, agenda.getProfissional().getId());
            } else if ((agenda.getTipoAt().isPrimeiroAt() == false) && (agenda.getEquipe().getCodEquipe() != null)) {
                stm = con.prepareStatement(sqlEqui+sqlComplementoFinal);
                stm.setInt(1, agenda.getEquipe().getCodEquipe());
            } else if ((agenda.getTipoAt().isPrimeiroAt() == true) && (agenda.getEquipe().getCodEquipe() != null)) {
                stm = con.prepareStatement(sqlEquiAva+sqlComplementoFinal);
                stm.setInt(1, agenda.getEquipe().getCodEquipe());
            }
            stm.setInt(2, diaSemana);
            stm.setInt(3, mes + 1);
            stm.setInt(4, ano);

            if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(agenda.getTurno())) {
                stm.setTime(5, DataUtil.retornarHorarioEmTime(agenda.getHorario()));
            } else {
                stm.setString(5, agenda.getTurno().toUpperCase());
            }

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                qtd = rs.getInt("qtdmax");
            }
        } catch (SQLException ex2) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return qtd;
    }

    public int verQtdMaxConfigAgendaDiaSemanaEspecificaPorTipoAtendimento(AgendaBean agenda) throws ProjetoException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(agenda.getDataAtendimento());
        int diaSemana = cal.get(Calendar.DAY_OF_WEEK);
        int mes = cal.get(Calendar.MONTH);
        int ano = cal.get(Calendar.YEAR);
        int qtd = 0;

        String sqlPro = "select distinct t.qtd " + "from hosp.config_agenda_profissional p "
                + "left join hosp.config_agenda_profissional_dias d on (p.id_configagenda = d.id_config_agenda_profissional) "
                + " JOIN hosp.tipo_atend_agenda t ON (p.id_configagenda = t.cod_config_agenda_profissional) "
                + "where p.codmedico = ? and d.dia = ? and p.mes = ? and p.ano=? and p.tipo='E' and t.codtipoatendimento=?";

        String sqlEqui = "select distinct t.qtd " + "from hosp.config_agenda_equipe p "
                + "left join hosp.config_agenda_equipe_dias d on (p.id_configagenda = d.id_config_agenda_equipe) "
                + " JOIN hosp.tipo_atend_agenda t ON (p.id_configagenda = t.cod_config_agenda_equipe) "
                + "where p.codequipe = ? and d.dia = ? and p.mes = ? and p.ano=? and p.tipo='E' and t.codtipoatendimento=?";

        String sqlEquiAva = "select distinct t.qtd from hosp.config_agenda_equipe p \n"
                + "left join hosp.config_agenda_equipe_dias d on (p.id_configagenda = d.id_config_agenda_equipe) \n"
                + " JOIN hosp.tipo_atend_agenda t ON (p.id_configagenda = t.cod_config_agenda_equipe) \n"
                + " join hosp.tipoatendimento  on tipoatendimento.id = t.codtipoatendimento \n"
                + "where p.codequipe = ? and d.dia = ? and p.mes = ? and p.ano=? and p.tipo='E' and t.codtipoatendimento=?\n"
                + "and tipoatendimento.primeiroatendimento is true";

        String sqlComplementoFinal = "";

        if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(agenda.getTurno())) {
            sqlComplementoFinal = " AND ? ::time between d.horario_inicio AND d.horario_final";
        } else {
            sqlComplementoFinal = " AND d.turno = ?";

        }

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = null;
            if (agenda.getProfissional().getId() != null) {
                stm = con.prepareStatement(sqlPro+sqlComplementoFinal);
                stm.setLong(1, agenda.getProfissional().getId());
            } else if ((agenda.getTipoAt().isPrimeiroAt() == false) && (agenda.getEquipe().getCodEquipe() != null)) {
                stm = con.prepareStatement(sqlEqui+sqlComplementoFinal);
                stm.setInt(1, agenda.getEquipe().getCodEquipe());
            } else if ((agenda.getTipoAt().isPrimeiroAt() == true) && (agenda.getEquipe().getCodEquipe() != null)) {
                stm = con.prepareStatement(sqlEquiAva+sqlComplementoFinal);
                stm.setInt(1, agenda.getEquipe().getCodEquipe());
            }
            stm.setInt(2, diaSemana);
            stm.setInt(3, mes + 1);
            stm.setInt(4, ano);
            stm.setInt(5, agenda.getTipoAt().getIdTipo());

            if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(agenda.getTurno())) {
                stm.setTime(6, DataUtil.retornarHorarioEmTime(agenda.getHorario()));
            } else {
                stm.setString(6, agenda.getTurno().toUpperCase());
            }

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                qtd = rs.getInt("qtd");
            }
        } catch (SQLException ex2) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return qtd;
    }

    public int verQtdMaxConfigAgendaDiaSemanaGeral(AgendaBean agenda) throws ProjetoException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(agenda.getDataAtendimento());
        int diaSemana = cal.get(Calendar.DAY_OF_WEEK);
        int qtd = 0;

        String sqlPro = "select distinct p.qtdmax " + "from hosp.config_agenda_profissional p "
                + "left join hosp.config_agenda_profissional_dias d on (p.id_configagenda = d.id_config_agenda_profissional) "
                + "where p.codmedico = ? and d.dia = ? and p.tipo='G'";

        String sqlEqui = "select e.qtdmax " + "from hosp.config_agenda_equipe e "
                + "left join hosp.config_agenda_equipe_dias d on (e.id_configagenda = d.id_config_agenda_equipe) "
                + "where e.codequipe = ? and d.dia = ? and e.tipo='G'";

        String sqlComplementoFinal = "";

        if (VerificadorUtil.verificarSeObjetoNuloOuVazio(agenda.getTurno())) {
            sqlComplementoFinal = " AND ? ::time between d.horario_inicio AND d.horario_final";
        } else {
            sqlComplementoFinal = " AND d.turno = ?";
        }

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = null;
            if (agenda.getProfissional().getId() != null) {
                stm = con.prepareStatement(sqlPro+sqlComplementoFinal);
                stm.setLong(1, agenda.getProfissional().getId());
            } else if (agenda.getEquipe().getCodEquipe() != null) {
                stm = con.prepareStatement(sqlEqui+sqlComplementoFinal);
                stm.setInt(1, agenda.getEquipe().getCodEquipe());
            }
            stm.setInt(2, diaSemana);

            if (VerificadorUtil.verificarSeObjetoNuloOuVazio(agenda.getTurno())) {
                stm.setTime(3, DataUtil.retornarHorarioEmTime(agenda.getHorario()));
            } else {
                stm.setString(3, agenda.getTurno().toUpperCase());
            }

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                qtd = rs.getInt("qtdmax");
            }
        } catch (SQLException ex2) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return qtd;
    }

    public int verQtdMaxConfigAgendaDiaSemanaGeralPorTipoAtendimento(AgendaBean agenda) throws ProjetoException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(agenda.getDataAtendimento());
        int diaSemana = cal.get(Calendar.DAY_OF_WEEK);
        int qtd = 0;

        String sqlPro = "select distinct t.qtd " + "from hosp.config_agenda_profissional p "
                + "left join hosp.config_agenda_profissional_dias d on (p.id_configagenda = d.id_config_agenda_profissional) "
                + " JOIN hosp.tipo_atend_agenda t ON (p.id_configagenda = t.cod_config_agenda_profissional) "
                + "where p.codmedico = ? and d.dia = ? and p.tipo='G' and t.codtipoatendimento=?";

        String sqlEqui = "select distinct t.qtd " + "from hosp.config_agenda_equipe p "
                + "left join hosp.config_agenda_equipe_dias d on (p.id_configagenda = d.id_config_agenda_equipe) "
                + " JOIN hosp.tipo_atend_agenda t ON (p.id_configagenda = t.cod_config_agenda_equipe) "
                + "where p.codequipe = ? and d.dia = ? and p.tipo='G' and t.codtipoatendimento=?";

        String sqlEquiAva = "select distinct t.qtd " + "from hosp.config_agenda_equipe p "
                + "left join hosp.config_agenda_equipe_dias d on (p.id_configagenda = d.id_config_agenda_equipe) "
                + " JOIN hosp.tipo_atend_agenda t ON (p.id_configagenda = t.cod_config_agenda_equipe) "
                + " join hosp.tipoatendimento  on tipoatendimento.id = t.codtipoatendimento "
                + "where p.codequipe = ? and d.dia = ? and p.tipo='G' and t.codtipoatendimento=? and tipoatendimento.primeiroatendimento is true";

        String sqlComplementoFinal = "";

        if (VerificadorUtil.verificarSeObjetoNuloOuVazio(agenda.getTurno())) {
            sqlComplementoFinal = " AND ? ::time between d.horario_inicio AND d.horario_final";
        } else {
            sqlComplementoFinal = " AND d.turno = ?";
        }

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = null;
            if (agenda.getProfissional().getId() != null) {
                stm = con.prepareStatement(sqlPro+sqlComplementoFinal);
                stm.setLong(1, agenda.getProfissional().getId());
            } else if ((agenda.getTipoAt().isPrimeiroAt() == false) && (agenda.getEquipe().getCodEquipe() != null)) {
                stm = con.prepareStatement(sqlEqui+sqlComplementoFinal);
                stm.setInt(1, agenda.getEquipe().getCodEquipe());
            } else if ((agenda.getTipoAt().isPrimeiroAt() == true) && (agenda.getEquipe().getCodEquipe() != null)) {
                stm = con.prepareStatement(sqlEquiAva+sqlComplementoFinal);
                stm.setInt(1, agenda.getEquipe().getCodEquipe());
            }
            stm.setInt(2, diaSemana);
            stm.setInt(3, agenda.getTipoAt().getIdTipo());

            if (VerificadorUtil.verificarSeObjetoNuloOuVazio(agenda.getTurno())) {
                stm.setTime(4, DataUtil.retornarHorarioEmTime(agenda.getHorario()));
            } else {
                stm.setString(4, agenda.getTurno().toUpperCase());
            }

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                qtd = rs.getInt("qtd");
            }
        } catch (SQLException ex2) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return qtd;
    }

    public int verQtdAgendadosData(AgendaBean agenda) throws ProjetoException {
        int qtd = 0;
        String sqlPro = "select count(*) as qtd from hosp.atendimentos where codmedico = ? and dtaatende = ? ";
        String sqlEqui = "select count(*) as qtd from hosp.atendimentos where codequipe = ? and dtaatende = ? ";

        String sqlComplementoFinal = "";

        if (VerificadorUtil.verificarSeObjetoNuloOuVazio(agenda.getTurno())) {
            sqlComplementoFinal = "AND horario = ?";
        } else {
            sqlComplementoFinal = "AND turno = ?";

        }

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = null;
            if (agenda.getProfissional().getId() != null) {
                stm = con.prepareStatement(sqlPro+sqlComplementoFinal);
                stm.setLong(1, agenda.getProfissional().getId());
            } else if (agenda.getEquipe().getCodEquipe() != null) {
                stm = con.prepareStatement(sqlEqui+sqlComplementoFinal);
                stm.setInt(1, agenda.getEquipe().getCodEquipe());
            }
            stm.setDate(2, new java.sql.Date(agenda.getDataAtendimento().getTime()));

            if (VerificadorUtil.verificarSeObjetoNuloOuVazio(agenda.getTurno())) {
                stm.setTime(3, DataUtil.retornarHorarioEmTime(agenda.getHorario()));
            } else {
                stm.setString(3, agenda.getTurno().toUpperCase());
            }

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                qtd = rs.getInt("qtd");
            }
        } catch (SQLException ex2) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return qtd;
    }

    public boolean buscarDataEspecifica(AgendaBean agenda) throws ProjetoException {
        int id = 0;

        String sqlPro = "select p.id_configagenda " + "from hosp.config_agenda_profissional p "
                + "left join hosp.config_agenda_profissional_dias d on (p.id_configagenda = d.id_config_agenda_profissional) "
                + "where p.codmedico = ? and d.data_especifica = ? ";

        String sqlEqui = "select e.id_configagenda " + "from hosp.config_agenda_equipe e "
                + "left join hosp.config_agenda_equipe_dias d on (e.id_configagenda = d.id_config_agenda_equipe) "
                + "where e.codequipe = ? and d.data_especifica = ? ";

        String sqlEquiAva = "select e.id_configagenda \n" + "from hosp.config_agenda_equipe e "
                + "join hosp.tipo_atend_agenda  on tipo_atend_agenda.cod_config_agenda_equipe = e.id_configagenda "
                + "left join hosp.config_agenda_equipe_dias d on (e.id_configagenda = d.id_config_agenda_equipe) "
                + "join hosp.tipoatendimento t on t.id = tipo_atend_agenda.codtipoatendimento "
                + "where e.codequipe = ? and d.data_especifica = ? and t.primeiroatendimento is true ";

        String sqlComplementoFinal = "";
        Boolean retorno = false;
        if (VerificadorUtil.verificarSeObjetoNuloOuVazio(agenda.getTurno())) {
            sqlComplementoFinal = "AND ? ::time between d.horario_inicio AND d.horario_final";
        } else {
            sqlComplementoFinal = "AND d.turno = ?";

        }

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = null;
            if (agenda.getProfissional().getId() != null) {
                stm = con.prepareStatement(sqlPro+sqlComplementoFinal);
                stm.setLong(1, agenda.getProfissional().getId());
            } else if ((agenda.getTipoAt().isPrimeiroAt() == false) && (agenda.getEquipe().getCodEquipe() != null)) {
                stm = con.prepareStatement(sqlEqui+sqlComplementoFinal);
                stm.setInt(1, agenda.getEquipe().getCodEquipe());
            } else if ((agenda.getTipoAt().isPrimeiroAt() == true) && (agenda.getEquipe().getCodEquipe() != null)) {
                stm = con.prepareStatement(sqlEquiAva+sqlComplementoFinal);
                stm.setInt(1, agenda.getEquipe().getCodEquipe());
            }
            stm.setDate(2, new java.sql.Date(agenda.getDataAtendimento().getTime()));

            if (VerificadorUtil.verificarSeObjetoNuloOuVazio(agenda.getTurno())) {
                stm.setTime(3, DataUtil.retornarHorarioEmTime(agenda.getHorario()));
            } else {
                stm.setString(3, agenda.getTurno().toUpperCase());
            }

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                id = rs.getInt("id_configagenda");
            }
            if (id == 0) {
                retorno = false;
            } else
                retorno = true;
        } catch (SQLException ex2) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return retorno;
    }

    public Boolean verificarSeExisteConfigAgendaProfissionalPorTipoAtendimento(Long codProfissional,
                                                                               java.util.Date dataAtendimento, String turno) throws ProjetoException {

        Boolean retorno = false;
        int diaSemana = DataUtil.extrairDiaDeData(dataAtendimento);
        int mes = DataUtil.extrairMesDeData(dataAtendimento);
        int ano = DataUtil.extrairAnoDeData(dataAtendimento);

        String sql = "";

        sql = "SELECT distinct c.id_configagenda FROM hosp.config_agenda_profissional c \n"
                + "LEFT JOIN hosp.config_agenda_profissional_dias d on (c.id_configagenda = d.id_config_agenda_profissional) \n"
                + "JOIN hosp.tipo_atend_agenda t ON (c.id_configagenda = t.cod_config_agenda_profissional) \n"
                + "WHERE c.codmedico = ? AND d.turno =?  and c.tipo='G'\n" + "union all\n"
                + "SELECT distinct c.id_configagenda FROM hosp.config_agenda_profissional c \n"
                + "LEFT JOIN hosp.config_agenda_profissional_dias d on (c.id_configagenda = d.id_config_agenda_profissional) \n"
                + "JOIN hosp.tipo_atend_agenda t ON (c.id_configagenda = t.cod_config_agenda_profissional) \n"
                + "WHERE c.codmedico = ? AND d.turno = ? AND (d.data_especifica = ? OR d.dia = ? AND c.mes = ? AND c.ano = ?)";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setLong(1, codProfissional);
            stm.setString(2, turno.toUpperCase());
            stm.setLong(3, codProfissional);
            stm.setString(4, turno.toUpperCase());
            stm.setDate(5, new java.sql.Date(dataAtendimento.getTime()));
            stm.setInt(6, diaSemana);
            stm.setInt(7, mes);
            stm.setInt(8, ano);

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                retorno = true;
            }

        } catch (SQLException ex2) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return retorno;
    }

    public Boolean verificarSeExisteConfigAgendaEquipePorTipoAtendimento(Integer codEquipe,
                                                                         java.util.Date dataAtendimento, String turno) throws ProjetoException {

        Boolean retorno = false;
        int diaSemana = DataUtil.extrairDiaDeData(dataAtendimento);
        int mes = DataUtil.extrairMesDeData(dataAtendimento);
        int ano = DataUtil.extrairAnoDeData(dataAtendimento);

        String sql = "";

        sql = "SELECT distinct c.id_configagenda FROM hosp.config_agenda_equipe c \n"
                + "LEFT JOIN hosp.config_agenda_equipe_dias d on (c.id_configagenda = d.id_config_agenda_equipe) \n"
                + "JOIN hosp.tipo_atend_agenda t ON (c.id_configagenda = t.cod_config_agenda_equipe) \n"
                + "WHERE c.codequipe = ? AND d.turno =?  and c.tipo='G'\n" + "union all\n"
                + "SELECT distinct c.id_configagenda FROM hosp.config_agenda_equipe c \n"
                + "LEFT JOIN hosp.config_agenda_equipe_dias d on (c.id_configagenda = d.id_config_agenda_equipe) \n"
                + "JOIN hosp.tipo_atend_agenda t ON (c.id_configagenda = t.cod_config_agenda_equipe) \n"
                + "WHERE c.codequipe = ? AND d.turno = ? AND (d.data_especifica = ? OR d.dia = ? AND c.mes = ? AND c.ano = ?)";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setLong(1, codEquipe);
            stm.setString(2, turno.toUpperCase());
            stm.setLong(3, codEquipe);
            stm.setString(4, turno.toUpperCase());
            stm.setDate(5, new java.sql.Date(dataAtendimento.getTime()));
            stm.setInt(6, diaSemana);
            stm.setInt(7, mes);
            stm.setInt(8, ano);

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                retorno = true;
            }

        } catch (SQLException ex2) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return retorno;
    }

    public Boolean verificarSeExisteConfigAgendaEquipeAvaliacao(Integer codEquipe, java.util.Date dataAtendimento,
                                                                String turno) throws ProjetoException {

        Boolean retorno = false;
        int diaSemana = DataUtil.extrairDiaDeData(dataAtendimento);
        int mes = DataUtil.extrairMesDeData(dataAtendimento);
        int ano = DataUtil.extrairAnoDeData(dataAtendimento);

        String sql = "";

        sql = "SELECT distinct c.id_configagenda FROM hosp.config_agenda_equipe c \n"
                + "LEFT JOIN hosp.config_agenda_equipe_dias d on (c.id_configagenda = d.id_config_agenda_equipe) \n"
                + "JOIN hosp.tipo_atend_agenda t ON (c.id_configagenda = t.cod_config_agenda_equipe) \n"
                + " join hosp.tipoatendimento on tipoatendimento.id = t.codtipoatendimento "
                + "WHERE c.codequipe = ? AND d.turno =?  and c.tipo='G' and tipoatendimento.primeiroatendimento is true\n "
                + "union all\n" + "SELECT distinct c.id_configagenda FROM hosp.config_agenda_equipe c \n"
                + "LEFT JOIN hosp.config_agenda_equipe_dias d on (c.id_configagenda = d.id_config_agenda_equipe) \n"
                + "JOIN hosp.tipo_atend_agenda t ON (c.id_configagenda = t.cod_config_agenda_equipe) \n"
                + " join hosp.tipoatendimento on tipoatendimento.id = t.codtipoatendimento "
                + "WHERE c.codequipe = ? AND d.turno = ? AND (d.data_especifica = ? OR d.dia = ? AND c.mes = ? AND c.ano = ?) and tipoatendimento.primeiroatendimento is true";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setLong(1, codEquipe);
            stm.setString(2, turno.toUpperCase());
            stm.setLong(3, codEquipe);
            stm.setString(4, turno.toUpperCase());
            stm.setDate(5, new java.sql.Date(dataAtendimento.getTime()));
            stm.setInt(6, diaSemana);
            stm.setInt(7, mes);
            stm.setInt(8, ano);

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                retorno = true;
            }

        } catch (SQLException ex2) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return retorno;
    }

    public Boolean verificarSeExisteConfigAgendaDataEspecificaPorTipoAtendimento(AgendaBean agenda)
            throws ProjetoException {

        Boolean retorno = false;

        String sqlPro = "SELECT distinct c.id_configagenda FROM hosp.config_agenda_profissional c "
                + "LEFT JOIN hosp.config_agenda_profissional_dias d on (c.id_configagenda = d.id_config_agenda_profissional) "
                + "JOIN hosp.tipo_atend_agenda t ON (c.id_configagenda = t.cod_config_agenda_profissional) "
                + "WHERE c.codmedico =? AND d.data_especifica = ? and t.codtipoatendimento=? ";

        String sqlEqui = "SELECT distinct c.id_configagenda FROM hosp.config_agenda_equipe c "
                + "LEFT JOIN hosp.config_agenda_equipe_dias d on (c.id_configagenda = d.id_config_agenda_equipe) "
                + "JOIN hosp.tipo_atend_agenda t ON (c.id_configagenda = t.cod_config_agenda_equipe) "
                + "WHERE c.codequipe =? AND d.data_especifica = ? and t.codtipoatendimento=? ";

        String sqlEquiAva = "SELECT distinct c.id_configagenda FROM hosp.config_agenda_equipe c "
                + "LEFT JOIN hosp.config_agenda_equipe_dias d on (c.id_configagenda = d.id_config_agenda_equipe) "
                + "JOIN hosp.tipo_atend_agenda t ON (c.id_configagenda = t.cod_config_agenda_equipe) "
                + " join hosp.tipoatendimento  on tipoatendimento.id = t.codtipoatendimento "
                + "WHERE c.codequipe =? AND d.data_especifica = ? and t.codtipoatendimento=? and tipoatendimento.primeiroatendimento is true ";

        String sqlComplementoFinal = "";

        if (VerificadorUtil.verificarSeObjetoNuloOuVazio(agenda.getTurno())) {
            sqlComplementoFinal = "AND ? ::time between d.horario_inicio AND d.horario_final";
        } else {
            sqlComplementoFinal = "AND d.turno = ?";

        }

        try {
            PreparedStatement stm = null;
            con = ConnectionFactory.getConnection();
            if (agenda.getProfissional().getId() != null) {
                stm = con.prepareStatement(sqlPro + sqlComplementoFinal);
                stm.setLong(1, agenda.getProfissional().getId());
            } else if ((agenda.getTipoAt().isPrimeiroAt() == false) && (agenda.getEquipe().getCodEquipe() != null)) {
                stm = con.prepareStatement(sqlEqui + sqlComplementoFinal);
                stm.setInt(1, agenda.getEquipe().getCodEquipe());
            } else if ((agenda.getTipoAt().isPrimeiroAt() == true) && (agenda.getEquipe().getCodEquipe() != null)) {
                stm = con.prepareStatement(sqlEquiAva + sqlComplementoFinal);
                stm.setInt(1, agenda.getEquipe().getCodEquipe());
            }

            stm.setDate(2, new java.sql.Date(agenda.getDataAtendimento().getTime()));
            stm.setLong(3, agenda.getTipoAt().getIdTipo());

            if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(agenda.getTurno())) {
                stm.setString(4, agenda.getHorario());
            } else {
                stm.setString(4, agenda.getTurno().toUpperCase());
            }
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                retorno = true;
            }

        } catch (SQLException ex2) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return retorno;
    }

    public Integer contaConfigGeralQtdMaxTipoAtendimentoProfissional(Long codProfissional, String turno,
                                                                     int codTipoAtendimento) throws ProjetoException {

        Integer retorno = 0;
        String sql = "";

        sql = "SELECT distinct coalesce(t.qtd) qtd FROM hosp.config_agenda_profissional c \n"
                + "LEFT JOIN hosp.config_agenda_profissional_dias d on (c.id_configagenda = d.id_config_agenda_profissional) \n"
                + "JOIN hosp.tipo_atend_agenda t ON (c.id_configagenda = t.cod_config_agenda_profissional) \n"
                + "WHERE c.codmedico = ? AND d.turno = ?  and c.tipo='G' and t.codtipoatendimento=?";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setLong(1, codProfissional);
            stm.setString(2, turno.toUpperCase());
            stm.setInt(3, codTipoAtendimento);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                retorno = rs.getInt("qtd");
            }

        } catch (SQLException ex2) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return retorno;
    }

    public Integer contaConfigGeralQtdMaxTipoAtendimentoEquipe(Integer codEquipe, String turno, int codTipoAtendimento)
            throws ProjetoException {

        Integer retorno = null;
        String sql = "";

        sql = "SELECT distinct coalesce(t.qtd) qtd FROM hosp.config_agenda_equipe c \n"
                + "LEFT JOIN hosp.config_agenda_equipe_dias d on (c.id_configagenda = d.id_config_agenda_equipe) \n"
                + "JOIN hosp.tipo_atend_agenda t ON (c.id_configagenda = t.cod_config_agenda_equipe) \n"
                + "WHERE c.codequipe = ? AND d.turno = ?  and c.tipo='G' and t.codtipoatendimento=?";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, codEquipe);
            stm.setString(2, turno.toUpperCase());
            stm.setInt(3, codTipoAtendimento);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                retorno = rs.getInt("qtd");
            }

        } catch (SQLException ex2) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return retorno;
    }

    public Integer contaConfigEspecificaQtdMaxTipoAtendimentoProfissional(Long codProfissional,
                                                                          java.util.Date dataAtendimento, String turno, int codTipoAtendimento) throws ProjetoException {

        Integer retorno = 0;
        int diaSemana = DataUtil.extrairDiaDeData(dataAtendimento);
        int mes = DataUtil.extrairMesDeData(dataAtendimento);
        int ano = DataUtil.extrairAnoDeData(dataAtendimento);

        String sql = "";

        sql = "SELECT distinct coalesce(t.qtd) qtd FROM hosp.config_agenda_profissional c \n"
                + "LEFT JOIN hosp.config_agenda_profissional_dias d on (c.id_configagenda = d.id_config_agenda_profissional) \n"
                + "JOIN hosp.tipo_atend_agenda t ON (c.id_configagenda = t.cod_config_agenda_profissional) \n"
                + "WHERE c.codmedico = ? AND d.turno = ? AND (d.data_especifica = ? OR d.dia = ? AND c.mes = ? AND c.ano = ?) and t.codtipoatendimento=?";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setLong(1, codProfissional);
            stm.setString(2, turno.toUpperCase());
            stm.setDate(3, new java.sql.Date(dataAtendimento.getTime()));
            stm.setInt(4, diaSemana);
            stm.setInt(5, mes);
            stm.setInt(6, ano);
            stm.setInt(7, codTipoAtendimento);

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                retorno = rs.getInt("qtd");
            }

        } catch (SQLException ex2) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return retorno;
    }

    public Integer contaConfigEspecificaQtdMaxTipoAtendimentoEquipe(Integer codEquipe, java.util.Date dataAtendimento,
                                                                    String turno, int codTipoAtendimento) throws ProjetoException {

        Integer retorno = null;
        int diaSemana = DataUtil.extrairDiaDeData(dataAtendimento);
        int mes = DataUtil.extrairMesDeData(dataAtendimento);
        int ano = DataUtil.extrairAnoDeData(dataAtendimento);

        String sql = "";

        sql = "SELECT distinct coalesce(t.qtd) qtd FROM hosp.config_agenda_equipe c \n"
                + "LEFT JOIN hosp.config_agenda_equipe_dias d on (c.id_configagenda = d.id_config_agenda_equipe) \n"
                + "JOIN hosp.tipo_atend_agenda t ON (c.id_configagenda = t.cod_config_agenda_equipe) \n"
                + "WHERE c.codequipe = ? AND d.turno = ? AND (d.data_especifica = ? OR d.dia = ? AND c.mes = ? AND c.ano = ?) and t.codtipoatendimento=?";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, codEquipe);
            stm.setString(2, turno.toUpperCase());
            stm.setDate(3, new java.sql.Date(dataAtendimento.getTime()));
            stm.setInt(4, diaSemana);
            stm.setInt(5, mes);
            stm.setInt(6, ano);
            stm.setInt(7, codTipoAtendimento);

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                retorno = rs.getInt("qtd");
            }

        } catch (SQLException ex2) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return retorno;
    }

    public Boolean verificarSeExisteTipoAtendimentoEspecificoIntervaloDeDatas(Long codProfissional,
                                                                              java.util.Date dataAtendimento, java.util.Date dataAtendimentoFinal, String turno, String tipoData)
            throws ProjetoException {

        Boolean retorno = false;

        String sql = "";

        sql = "SELECT c.id_configagenda FROM hosp.config_agenda_profissional c "
                + "LEFT JOIN hosp.config_agenda_profissional_dias d on (c.id_configagenda = d.id_config_agenda_profissional) "
                + "LEFT JOIN hosp.tipo_atend_agenda t ON (c.id_configagenda = t.cod_config_agenda_profissional) "
                + "WHERE c.codmedico = ? AND d.turno = ? ";

        if (tipoData.equals(TipoDataAgenda.DATA_UNICA.getSigla())) {
            sql = sql + " AND (d.data_especifica = ? )";
        }
        if (tipoData.equals(TipoDataAgenda.INTERVALO_DE_DATAS.getSigla())) {
            sql = sql + " AND d.data_especifica >= ? AND d.data_especifica <= ?";
        }
        if (tipoData.equals(TipoDataAgenda.DATA_UNICA.getSigla())) {
            sql = sql + " AND (d.data_especifica = ? OR (d.dia = ? AND c.mes = ? AND c.ano = ?))";
        }

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setLong(1, codProfissional);
            stm.setString(2, turno.toUpperCase());
            stm.setDate(3, new java.sql.Date(dataAtendimento.getTime()));
            if (tipoData.equals(TipoDataAgenda.INTERVALO_DE_DATAS.getSigla())) {
                stm.setDate(4, DataUtil.converterDateUtilParaDateSql(dataAtendimentoFinal));
            }

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                retorno = true;
            }

        } catch (SQLException ex2) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return retorno;
    }

    public boolean buscarTabTipoAtendAgenda(AgendaBean agenda) throws ProjetoException {
        int achou = 0;
        String sql = "select codtipoatendimento from hosp.tipo_atend_agenda "
                + " where codtipoatendimento = ? and codprograma = ? and codgrupo = ?";
        Boolean retorno = false;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, agenda.getTipoAt().getIdTipo());
            stm.setInt(2, agenda.getPrograma().getIdPrograma());
            stm.setInt(3, agenda.getGrupo().getIdGrupo());
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                achou = rs.getInt("codtipoatendimento");
            }
            if (achou == 0) {
                retorno = false;
            } else
                retorno = true;
        } catch (SQLException ex2) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return retorno;
    }

    public boolean buscarDiaSemanaMesAnoEspecifico(AgendaBean agenda) throws ProjetoException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(agenda.getDataAtendimento());
        int mes = cal.get(Calendar.MONTH);
        int ano = cal.get(Calendar.YEAR);
        int id = 0;

        String sqlPro = "select p.id_configagenda " + "from hosp.config_agenda_profissional p "
                + "left join hosp.config_agenda_profissional_dias d on (p.id_configagenda = d.id_config_agenda_profissional) "
                + "where p.codmedico = ? and p.mes = ? and p.ano=? and p.tipo='E'";

        String sqlEqui = "select e.id_configagenda " + "from hosp.config_agenda_equipe e "
                + "left join hosp.config_agenda_equipe_dias d on (e.id_configagenda = d.id_config_agenda_equipe) "
                + "where e.codequipe = ?  and e.mes = ?  and e.ano=? and e.tipo='E'";

        String sqlEquiAva = "select e.id_configagenda \n" + "from hosp.config_agenda_equipe e \n"
                + "join hosp.tipo_atend_agenda  on tipo_atend_agenda.cod_config_agenda_equipe = e.id_configagenda\n"
                + "left join hosp.config_agenda_equipe_dias d on (e.id_configagenda = d.id_config_agenda_equipe) \n"
                + "join hosp.tipoatendimento t on t.id = tipo_atend_agenda.codtipoatendimento\n"
                + "where e.codequipe = ?  and e.mes = ?  and e.ano=? and e.tipo='E' and t.primeiroatendimento is true";
        Boolean retorno = false;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = null;
            if (agenda.getProfissional().getId() != null) {
                stm = con.prepareStatement(sqlPro);
                stm.setLong(1, agenda.getProfissional().getId());
            } else if ((agenda.getTipoAt().isPrimeiroAt() == false) && (agenda.getEquipe().getCodEquipe() != null)) {
                stm = con.prepareStatement(sqlEqui);
                stm.setInt(1, agenda.getEquipe().getCodEquipe());
            } else if ((agenda.getTipoAt().isPrimeiroAt() == true) && (agenda.getEquipe().getCodEquipe() != null)) {
                stm = con.prepareStatement(sqlEquiAva);
                stm.setInt(1, agenda.getEquipe().getCodEquipe());
            }

            stm.setInt(2, mes + 1);
            stm.setInt(3, ano);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                id = rs.getInt("id_configagenda");
            }
            if (id == 0) {
                retorno = false;
            } else
                retorno = true;
        } catch (SQLException ex2) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return retorno;
    }

    public boolean buscarDiaSemanaMesAnoGeral(AgendaBean agenda) throws ProjetoException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(agenda.getDataAtendimento());
        int diaSemana = cal.get(Calendar.DAY_OF_WEEK);
        int mes = cal.get(Calendar.MONTH);
        int ano = cal.get(Calendar.YEAR);
        int id = 0;

        String sqlPro = "select p.id_configagenda " + "from hosp.config_agenda_profissional p "
                + "left join hosp.config_agenda_profissional_dias d on (p.id_configagenda = d.id_config_agenda_profissional) "
                + "where p.codmedico = ? and d.turno = ? and p.tipo='G'";

        String sqlEqui = "select e.id_configagenda " + "from hosp.config_agenda_equipe e "
                + "left join hosp.config_agenda_equipe_dias d on (e.id_configagenda = d.id_config_agenda_equipe) "
                + "where e.codequipe = ? and d.dia = ? and d.turno = ? and e.tipo='G'";
        Boolean retorno = false;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = null;
            if (agenda.getProfissional().getId() != null) {
                stm = con.prepareStatement(sqlPro);
                stm.setLong(1, agenda.getProfissional().getId());
            } else if (agenda.getEquipe().getCodEquipe() != null) {
                stm = con.prepareStatement(sqlEqui);
                stm.setInt(1, agenda.getEquipe().getCodEquipe());
            }
            stm.setInt(2, diaSemana);
            stm.setString(3, agenda.getTurno().toUpperCase());
            stm.setInt(4, mes + 1);
            stm.setInt(5, ano);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                id = rs.getInt("id_configagenda");
            }
            if (id == 0) {
                retorno = false;
            } else
                retorno = true;
        } catch (SQLException ex2) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return retorno;
    }

    public List<AgendaBean> listarAgendamentosData(AgendaBean ag) throws ProjetoException {
        List<AgendaBean> lista = new ArrayList<AgendaBean>();

        StringBuilder sqlProf = new StringBuilder();

        sqlProf.append("SELECT a.id_atendimento, a.codpaciente, a.codmedico, a.codprograma, ");
        sqlProf.append("a.codconvenio, a.dtaatende, a.horaatende, a.situacao, a.codatendente, ");
        sqlProf.append("a.dtamarcacao, a.codtipoatendimento, a.turno, a.codequipe, a.observacao, a.ativo, ");
        sqlProf.append("f.descfuncionario, p.nome, t.desctipoatendimento, e.descequipe ");
        sqlProf.append("FROM hosp.atendimentos a ");
        sqlProf.append("LEFT JOIN acl.funcionarios f ON (f.id_funcionario = a.codmedico) ");
        sqlProf.append("LEFT JOIN hosp.pacientes p ON (p.id_paciente = a.codpaciente) ");
        sqlProf.append("LEFT JOIN hosp.tipoatendimento t ON (t.id = a.codtipoatendimento) ");
        sqlProf.append("LEFT JOIN hosp.equipe e ON (e.id_equipe = a.codequipe) ");
        sqlProf.append("WHERE a.dtaatende = ? AND a.codmedico = ? ");

        StringBuilder sqlEqui = new StringBuilder();

        sqlEqui.append("SELECT a.id_atendimento, a.codpaciente, a.codmedico, a.codprograma, ");
        sqlEqui.append("a.codconvenio, a.dtaatende, a.horaatende, a.situacao, a.codatendente, ");
        sqlEqui.append("a.dtamarcacao, a.codtipoatendimento, a.turno, a.codequipe, a.observacao, a.ativo, ");
        sqlEqui.append("f.descfuncionario, p.nome, t.desctipoatendimento, e.descequipe ");
        sqlEqui.append("FROM hosp.atendimentos a ");
        sqlEqui.append("LEFT JOIN acl.funcionarios f ON (f.id_funcionario = a.codmedico) ");
        sqlEqui.append("LEFT JOIN hosp.pacientes p ON (p.id_paciente = a.codpaciente) ");
        sqlEqui.append("LEFT JOIN hosp.tipoatendimento t ON (t.id = a.codtipoatendimento) ");
        sqlEqui.append("LEFT JOIN hosp.equipe e ON (e.id_equipe = a.codequipe) ");
        sqlEqui.append("WHERE dtaatende = ? AND codequipe = ? ");

        String sqlComplementoFinal = "";

        if (VerificadorUtil.verificarSeObjetoNuloOuVazio(ag.getTurno())) {
            sqlComplementoFinal = "AND a.horario = ?";
        } else {
            sqlComplementoFinal = "AND a.turno = ?";

        }

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = null;

            if (ag.getProfissional().getId() != null) {
                stm = con.prepareStatement(sqlProf.toString()+sqlComplementoFinal);
                stm.setLong(2, ag.getProfissional().getId());
            } else if (ag.getEquipe().getCodEquipe() != null) {
                stm = con.prepareStatement(sqlEqui.toString()+sqlComplementoFinal);
                stm.setInt(2, ag.getEquipe().getCodEquipe());
            }

            stm.setDate(1, new java.sql.Date(ag.getDataAtendimento().getTime()));

            if (VerificadorUtil.verificarSeObjetoNuloOuVazio(ag.getTurno())) {
                stm.setTime(3, DataUtil.retornarHorarioEmTime(ag.getHorario()));
            } else {
                stm.setString(3, ag.getTurno().toUpperCase());
            }

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                AgendaBean agenda = new AgendaBean();
                agenda.setIdAgenda(rs.getInt("id_atendimento"));
                agenda.getPaciente().setId_paciente(rs.getInt("codpaciente"));
                agenda.getPaciente().setNome(rs.getString("nome"));
                agenda.getProfissional().setId(rs.getLong("codmedico"));
                agenda.getProfissional().setNome(rs.getString("descfuncionario"));
                agenda.setDataAtendimento(rs.getDate("dtaatende"));
                agenda.setSituacao(rs.getString("situacao"));
                agenda.setDataMarcacao(rs.getDate("dtamarcacao"));
                agenda.getTipoAt().setIdTipo(rs.getInt("codtipoatendimento"));
                agenda.getTipoAt().setDescTipoAt(rs.getString("desctipoatendimento"));
                agenda.setTurno(rs.getString("turno"));
                agenda.getEquipe().setCodEquipe(rs.getInt("codequipe"));
                agenda.getEquipe().setDescEquipe(rs.getString("descequipe"));
                agenda.setObservacao(rs.getString("observacao"));
                agenda.setAtivo(rs.getString("ativo"));
                lista.add(agenda);
            }
        } catch (SQLException ex2) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return lista;
    }

    public List<AgendaBean> consultarAgenda(Date dataAgenda, Date dataAgendaFinal, Integer codUnidade, String situacao,
                                            String campoBusca, String tipo) throws ProjetoException {
        List<AgendaBean> lista = new ArrayList<AgendaBean>();

        String sql = "SELECT a.id_atendimento, a.codpaciente, p.nome,p.matricula, p.cns, a.codmedico, m.descfuncionario, a.situacao, "
                + " a.dtaatende, a.dtamarcacao, a.codtipoatendimento, t.desctipoatendimento, a.turno, a.avulso,  "
                + " a.codequipe, e.descequipe, coalesce(a.presenca, 'N') presenca, "

                + " case when "
                + " (select count(*) from hosp.atendimentos1 a1 where a1.id_atendimento = a.id_atendimento and a1.id_situacao_atendimento is null and coalesce(a1.excluido,'N')='N') =  "
                + " (select count(*) from hosp.atendimentos1 a1 where a1.id_atendimento = a.id_atendimento and coalesce(a1.excluido,'N')='N') "
                + " then 'Atendimento Não Informado' " + " when "
                + " (select count(*) from hosp.atendimentos1 a1 where a1.id_atendimento = a.id_atendimento and a1.id_situacao_atendimento is not null and coalesce(a1.excluido,'N')='N') = "
                + " (select count(*) from hosp.atendimentos1 a1 where a1.id_atendimento = a.id_atendimento and coalesce(a1.excluido,'N')='N') "
                + " then 'Atendimento Informado' " + " else 'Atendimento Informado Parcialmente' " + " end as situacao_atendimento_informado "

                + " FROM  hosp.atendimentos a "
                + " LEFT JOIN hosp.pacientes p ON (p.id_paciente = a.codpaciente) "
                + " LEFT JOIN acl.funcionarios m ON (m.id_funcionario = a.codmedico) "
                + " LEFT JOIN hosp.equipe e ON (e.id_equipe = a.codequipe) "
                + " LEFT JOIN hosp.tipoatendimento t ON (t.id = a.codtipoatendimento) " + " WHERE a.cod_unidade = ? and coalesce(a.situacao,'')<>'C' ";
        if (dataAgenda != null)
            sql = sql + " AND a.dtaatende >= ? AND a.dtaatende <= ?";
        if (!situacao.equals("T"))
            sql = sql + " and coalesce(a.presenca,'N')=?";

        if (tipo.equals("nome")) {
            sql = sql + " and p.nome like ?";
        } else if (tipo.equals("cpf")) {
            sql = sql + " and p.cpf like ?";
        } else if (tipo.equals("cns")) {
            sql = sql + " and p.cns like ?";
        } else if (tipo.equals("prontuario")) {
            sql = sql + " and p.id_paciente = ?";
        } else if (tipo.equals("matricula")) {
            sql = sql + " and p.matricula like ?";
        }

        sql = sql + " order by a.dtaatende , p.nome";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = null;
            stm = con.prepareStatement(sql);

            stm.setInt(1, codUnidade);
            int i = 2;
            if (dataAgenda != null) {
                stm.setDate(i, new java.sql.Date(dataAgenda.getTime()));
                i = i + 1;
                stm.setDate(3, new java.sql.Date(dataAgendaFinal.getTime()));
                i = i + 1;
            }

            if (!situacao.equals("T")) {
                stm.setString(i, situacao);
                i = i + 1;
            }

            if (!campoBusca.equals(null)) {
                if ((tipo.equals("nome")) || (tipo.equals("cpf")) || (tipo.equals("cns")) || (tipo.equals("matricula")))
                    stm.setString(i, "%" + campoBusca.toUpperCase() + "%");
                else
                    stm.setInt(i, Integer.valueOf(campoBusca));
                i = i + 1;
            }

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                AgendaBean agenda = new AgendaBean();
                agenda.setIdAgenda(rs.getInt("id_atendimento"));
                agenda.getPaciente().setId_paciente(rs.getInt("codpaciente"));
                agenda.getPaciente().setMatricula(rs.getString("matricula"));
                agenda.getPaciente().setNome(rs.getString("nome"));
                agenda.getPaciente().setCns(rs.getString("cns"));
                agenda.getProfissional().setId(rs.getLong("codmedico"));
                agenda.getProfissional().setNome(rs.getString("descfuncionario"));
                agenda.setDataAtendimento(rs.getDate("dtaatende"));
                agenda.setDataMarcacao(rs.getDate("dtamarcacao"));
                agenda.getTipoAt().setIdTipo(rs.getInt("codtipoatendimento"));
                agenda.getTipoAt().setDescTipoAt(rs.getString("desctipoatendimento"));
                agenda.setTurno(rs.getString("turno"));
                agenda.getEquipe().setCodEquipe(rs.getInt("codequipe"));
                agenda.getEquipe().setDescEquipe(rs.getString("descequipe"));
                agenda.setPresenca(rs.getString("presenca"));
                agenda.setSituacao(rs.getString("situacao"));
                agenda.setAvulso(rs.getBoolean("avulso"));
                agenda.setSituacaoAtendimentoInformado(rs.getString("situacao_atendimento_informado"));

                if(agenda.getAvulso() || !VerificadorUtil.verificarSeObjetoNuloOuZero(agenda.getEquipe().getCodEquipe()))
                    agenda.setListaNomeProfissionais(new AtendimentoDAO().retornaNomeProfissionaisAtendimento(agenda.getIdAgenda(), con));

                lista.add(agenda);
            }
        } catch (SQLException ex2) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return lista;
    }

    public int verQtdMaxAgendaMesAnoEspecifico(AgendaBean agenda) throws ProjetoException {
        int diaSemana = DataUtil.extrairDiaDeData(agenda.getDataAtendimento());
        int mes = DataUtil.extrairMesDeData(agenda.getDataAtendimento());
        int ano = DataUtil.extrairAnoDeData(agenda.getDataAtendimento());
        int qtdMax = 0;

        String sqlPro = "select p.qtdmax " + "from hosp.config_agenda_profissional p "
                + "left join hosp.config_agenda_profissional_dias d on (p.id_configagenda = d.id_config_agenda_profissional) "
                + "where p.codmedico = ? and d.dia = ? and d.turno = ? AND p.mes = ? AND p.ano = ? and p.tipo='E";

        String sqlEqui = "select e.qtdmax " + "from hosp.config_agenda_equipe e "
                + "left join hosp.config_agenda_equipe_dias d on (e.id_configagenda = d.id_config_agenda_equipe) "
                + "where e.codequipe = ? and d.dia = ? and d.turno = ? AND e.mes = ? AND e.ano = ? and e.tipo='E";

        try {
            con = ConnectionFactory.getConnection();

            PreparedStatement stm = null;

            if (agenda.getProfissional().getId() != null) {
                stm = con.prepareStatement(sqlPro);
                stm.setLong(1, agenda.getProfissional().getId());
            } else if (agenda.getEquipe().getCodEquipe() != null) {
                stm = con.prepareStatement(sqlEqui);
                stm.setInt(1, agenda.getEquipe().getCodEquipe());
            }
            stm.setInt(2, diaSemana);
            stm.setString(3, agenda.getTurno().toUpperCase());
            stm.setInt(4, mes);
            stm.setInt(5, ano);

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                qtdMax = rs.getInt("qtdmax");
            }
        } catch (SQLException ex2) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return qtdMax;
    }

    public Integer verQtdMaxAgendaEspecDataEspecifica(AgendaBean agenda) throws ProjetoException {
        Integer qtdMax = null;

        String sqlPro = "select p.qtdmax " + "from hosp.config_agenda_profissional p "
                + "left join hosp.config_agenda_profissional_dias d on (p.id_configagenda = d.id_config_agenda_profissional) "
                + "where p.codmedico = ? and d.turno = ? and d.data_especifica = ? ";

        String sqlEqui = "select e.qtdmax " + "from hosp.config_agenda_equipe e "
                + "left join hosp.config_agenda_equipe_dias d on (e.id_configagenda = d.id_config_agenda_equipe) "
                + "where e.codequipe = ? and d.turno = ? and d.data_especifica = ?;";

        try {
            con = ConnectionFactory.getConnection();

            PreparedStatement stm = null;

            if (agenda.getProfissional().getId() != null) {
                stm = con.prepareStatement(sqlPro);
                stm.setLong(1, agenda.getProfissional().getId());
            } else if (agenda.getEquipe().getCodEquipe() != null) {
                stm = con.prepareStatement(sqlEqui);
                stm.setInt(1, agenda.getEquipe().getCodEquipe());
            }
            stm.setString(2, agenda.getTurno().toUpperCase());
            stm.setDate(3, DataUtil.converterDateUtilParaDateSql(agenda.getDataAtendimento()));

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                qtdMax = rs.getInt("qtdmax");
            }
        } catch (SQLException ex2) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return qtdMax;
    }

    public int verQtdAgendadosEspec(AgendaBean agenda) throws ProjetoException {
        int qtd = 0;
        String sqlPro = "select count(*) as qtd from hosp.atendimentos where codmedico = ? and dtaatende = ? and turno = ?;";
        String sqlEqui = "select count(*) as qtd from hosp.atendimentos where codequipe = ? and dtaatende = ? and turno = ?;";

        String sqlComplementoFinal = "";

        if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(agenda.getTurno())) {
            sqlComplementoFinal = " AND horario = ?";
        } else {
            sqlComplementoFinal = " AND turno = ?";
        }

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = null;
            if (agenda.getProfissional().getId() != null) {
                stm = con.prepareStatement(sqlPro+sqlComplementoFinal);
                stm.setLong(1, agenda.getProfissional().getId());
            } else if (agenda.getEquipe().getCodEquipe() != null) {
                stm = con.prepareStatement(sqlEqui+sqlComplementoFinal);
                stm.setInt(1, agenda.getEquipe().getCodEquipe());
            }
            stm.setDate(2, new java.sql.Date(agenda.getDataAtendimento().getTime()));

            if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(agenda.getTurno())) {
                stm.setTime(3, DataUtil.retornarHorarioEmTime(agenda.getHorario()));
            } else {
                stm.setString(3, agenda.getTurno().toUpperCase());
            }

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                qtd = rs.getInt("qtd");
            }
        } catch (SQLException ex2) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return qtd;
    }

    public Boolean retornarIntervaloUltimoAgendamento(Integer codPaciente, Integer codTipoAtendimento,
                                                      Integer intervaloMinimo) throws ProjetoException {
        Boolean resultado = true;

        String sql = "SELECT CASE WHEN dtamarcacao - CURRENT_DATE > concat(?,' minutes')::INTERVAL THEN TRUE ELSE FALSE END AS intervalo "
                + "FROM hosp.atendimentos " + "WHERE codpaciente = ? AND codtipoatendimento = ? "
                + "ORDER BY id_atendimento DESC limit 1 ";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, intervaloMinimo);
            stm.setInt(2, codPaciente);
            stm.setInt(3, codTipoAtendimento);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                resultado = rs.getBoolean("intervalo");
            }

        } catch (SQLException ex2) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return resultado;
    }

    public List<ConfigAgendaParte1Bean> retornarDiaAtendimentoGeral(AgendaBean agenda) throws ProjetoException {
        List<ConfigAgendaParte1Bean> lista = new ArrayList<ConfigAgendaParte1Bean>();

        StringBuilder sqlPro = new StringBuilder();

        sqlPro.append("SELECT f.descfuncionario, ");
        sqlPro.append("CASE WHEN d.turno = 'M' THEN 'Manhã' ");
        sqlPro.append(" WHEN d.turno = 'T' THEN 'Tarde' END AS turno, ");
        sqlPro.append(" CASE WHEN d.dia = 1 THEN 'Domingo' ");
        sqlPro.append(" WHEN d.dia = 2 THEN 'Segunda' ");
        sqlPro.append(" WHEN d.dia = 3 THEN 'Terça' ");
        sqlPro.append(" WHEN d.dia = 4 THEN 'Quarta' ");
        sqlPro.append(" WHEN d.dia = 5 THEN 'Quinta'");
        sqlPro.append(" WHEN d.dia = 6 THEN 'Sexta' ");
        sqlPro.append(" WHEN d.dia = 7 THEN 'Sábado' END AS dia, ");
        sqlPro.append(" NULL AS data_especifica ");
        sqlPro.append(" FROM hosp.config_agenda_profissional c ");
        sqlPro.append(
                " LEFT JOIN hosp.config_agenda_profissional_dias d ON (c.id_configagenda = d.id_config_agenda_profissional) ");
        sqlPro.append(" LEFT JOIN acl.funcionarios f ON (c.codmedico = f.id_funcionario) ");
        sqlPro.append(" WHERE c.codmedico = ? and c.tipo='G'");

        String sqlProString = sqlPro.toString();

        StringBuilder sqlEqui = new StringBuilder();

        sqlEqui.append("SELECT f.descequipe, ");
        sqlEqui.append("CASE WHEN d.turno = 'M' THEN 'Manhã' ");
        sqlEqui.append(" WHEN d.turno = 'T' THEN 'Tarde' END AS turno, ");
        sqlEqui.append(" CASE WHEN d.dia = 1 THEN 'Domingo' ");
        sqlEqui.append(" WHEN d.dia = 2 THEN 'Segunda' ");
        sqlEqui.append(" WHEN d.dia = 3 THEN 'Terça' ");
        sqlEqui.append(" WHEN d.dia = 4 THEN 'Quarta' ");
        sqlEqui.append(" WHEN d.dia = 5 THEN 'Quinta'");
        sqlEqui.append(" WHEN d.dia = 6 THEN 'Sexta' ");
        sqlEqui.append(" WHEN d.dia = 7 THEN 'Sábado' END AS dia, ");
        sqlEqui.append(" NULL AS data_especifica ");
        sqlEqui.append(" FROM hosp.config_agenda_equipe c ");
        sqlEqui.append(
                " LEFT JOIN hosp.config_agenda_equipe_dias d ON (c.id_configagenda = d.id_config_agenda_equipe) ");
        sqlEqui.append(" LEFT JOIN hosp.equipe f ON (c.codequipe = f.id_equipe) ");
        sqlEqui.append(" WHERE c.codequipe = ? and c.tipo='G'");

        String sqlEquiString = sqlEqui.toString();

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = null;
            if (agenda.getProfissional().getId() != null) {
                stm = con.prepareStatement(sqlProString);
                stm.setLong(1, agenda.getProfissional().getId());
            } else if (agenda.getEquipe().getCodEquipe() != null) {
                stm = con.prepareStatement(sqlEquiString);
                stm.setInt(1, agenda.getEquipe().getCodEquipe());
            }

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                ConfigAgendaParte1Bean configAgendaParte1Bean = new ConfigAgendaParte1Bean();
                if (agenda.getProfissional().getId() != null)
                    configAgendaParte1Bean.getProfissional().setNome(rs.getString("descfuncionario"));
                if (agenda.getEquipe().getCodEquipe() != null)
                    configAgendaParte1Bean.getEquipe().setDescEquipe(rs.getString("descequipe"));
                configAgendaParte1Bean.setTurno(rs.getString("turno"));
                configAgendaParte1Bean.setDiaSemana(rs.getString("dia"));
                configAgendaParte1Bean.setDataEspecifica(rs.getDate("data_especifica"));

                lista.add(configAgendaParte1Bean);
            }
        } catch (SQLException ex2) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return lista;
    }

    public List<ConfigAgendaParte1Bean> retornarDiaAtendimentoMesAtual(AgendaBean agenda) throws ProjetoException {
        List<ConfigAgendaParte1Bean> lista = new ArrayList<ConfigAgendaParte1Bean>();

        StringBuilder sqlPro = new StringBuilder();

        sqlPro.append("SELECT  ");
        sqlPro.append("CASE WHEN d.turno = 'M' THEN 'Manhã' ");
        sqlPro.append("WHEN d.turno = 'T' THEN 'Tarde' END AS turno, d.dia,");
        sqlPro.append("CASE WHEN d.dia = 1 THEN 'Domingo' ");
        sqlPro.append("WHEN d.dia = 2 THEN 'Segunda' ");
        sqlPro.append("WHEN d.dia = 3 THEN 'Terça' ");
        sqlPro.append("WHEN d.dia = 4 THEN 'Quarta' ");
        sqlPro.append("WHEN d.dia = 5 THEN 'Quinta' ");
        sqlPro.append("WHEN d.dia = 6 THEN 'Sexta' ");
        sqlPro.append("WHEN d.dia = 7 THEN 'Sábado' END AS diasemana, ");
        sqlPro.append("NULL AS data_especifica ");
        sqlPro.append("FROM hosp.config_agenda_profissional c ");
        sqlPro.append(
                "LEFT JOIN hosp.config_agenda_profissional_dias d ON (c.id_configagenda = d.id_config_agenda_profissional) ");
        sqlPro.append("LEFT JOIN acl.funcionarios f ON (c.codmedico = f.id_funcionario) ");
        sqlPro.append("WHERE c.codmedico = ? AND ");
        sqlPro.append(
                "c.mes = (SELECT DATE_PART('MONTH', CURRENT_TIMESTAMP)) AND c.ano = (SELECT DATE_PART('YEAR', CURRENT_TIMESTAMP)) ");
        sqlPro.append("UNION ");
        sqlPro.append("SELECT  ");
        sqlPro.append("CASE WHEN dd.turno = 'M' THEN 'Manhã' ");
        sqlPro.append("WHEN dd.turno = 'T' THEN 'Tarde' END AS turno, ");
        sqlPro.append("dd.dia, NULL AS diasemana, dd.data_especifica ");
        sqlPro.append("FROM hosp.config_agenda_profissional cc ");
        sqlPro.append(
                "LEFT JOIN hosp.config_agenda_profissional_dias dd ON (cc.id_configagenda = dd.id_config_agenda_profissional) ");
        sqlPro.append("LEFT JOIN acl.funcionarios ff ON (cc.codmedico = ff.id_funcionario) ");
        sqlPro.append("WHERE cc.codmedico = ? AND ");
        sqlPro.append(
                "(SELECT DATE_PART('MONTH', dd.data_especifica)) = (SELECT DATE_PART('MONTH', CURRENT_TIMESTAMP)) ");
        sqlPro.append(
                "AND (SELECT DATE_PART('YEAR', dd.data_especifica)) = (SELECT DATE_PART('YEAR', CURRENT_TIMESTAMP)) ");
        sqlPro.append("ORDER BY  dia, turno ");

        String sqlProString = sqlPro.toString();

        StringBuilder sqlEqui = new StringBuilder();

        sqlEqui.append("SELECT  ");
        sqlEqui.append("CASE WHEN d.turno = 'M' THEN 'Manhã' ");
        sqlEqui.append("WHEN d.turno = 'T' THEN 'Tarde' END AS turno, d.dia,");
        sqlEqui.append("CASE WHEN d.dia = 1 THEN 'Domingo' ");
        sqlEqui.append("WHEN d.dia = 2 THEN 'Segunda' ");
        sqlEqui.append("WHEN d.dia = 3 THEN 'Terça' ");
        sqlEqui.append("WHEN d.dia = 4 THEN 'Quarta' ");
        sqlEqui.append("WHEN d.dia = 5 THEN 'Quinta' ");
        sqlEqui.append("WHEN d.dia = 6 THEN 'Sexta' ");
        sqlEqui.append("WHEN d.dia = 7 THEN 'Sábado' END AS diasemana, ");
        sqlEqui.append("NULL AS data_especifica ");
        sqlEqui.append("FROM hosp.config_agenda_equipe c ");
        sqlEqui.append(
                "LEFT JOIN hosp.config_agenda_equipe_dias d ON (c.id_configagenda = d.id_config_agenda_equipe) ");
        sqlEqui.append("WHERE c.codequipe = ? AND ");
        sqlEqui.append(
                "c.mes = (SELECT DATE_PART('MONTH', CURRENT_TIMESTAMP)) AND c.ano = (SELECT DATE_PART('YEAR', CURRENT_TIMESTAMP)) ");
        sqlEqui.append("UNION ");
        sqlEqui.append("SELECT  ");
        sqlEqui.append("CASE WHEN dd.turno = 'M' THEN 'Manhã' ");
        sqlEqui.append("WHEN dd.turno = 'T' THEN 'Tarde' END AS turno, ");
        sqlEqui.append("dd.dia, NULL AS diasemana, dd.data_especifica ");
        sqlEqui.append("FROM hosp.config_agenda_equipe cc ");
        sqlEqui.append(
                "LEFT JOIN hosp.config_agenda_equipe_dias dd ON (cc.id_configagenda = dd.id_config_agenda_equipe) ");
        sqlEqui.append("WHERE cc.codequipe = ? AND ");
        sqlEqui.append(
                "(SELECT DATE_PART('MONTH', dd.data_especifica)) = (SELECT DATE_PART('MONTH', CURRENT_TIMESTAMP)) ");
        sqlEqui.append(
                "AND (SELECT DATE_PART('YEAR', dd.data_especifica)) = (SELECT DATE_PART('YEAR', CURRENT_TIMESTAMP)) ");
        sqlEqui.append("ORDER BY  dia, turno ");

        String sqlEquiString = sqlEqui.toString();

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = null;
            if (agenda.getProfissional().getId() != null) {
                stm = con.prepareStatement(sqlProString);
                stm.setLong(1, agenda.getProfissional().getId());
            } else if (agenda.getEquipe().getCodEquipe() != null) {
                stm = con.prepareStatement(sqlEquiString);
                stm.setInt(1, agenda.getEquipe().getCodEquipe());
            }

            if (agenda.getProfissional().getId() != null) {
                stm.setLong(1, agenda.getProfissional().getId());
            } else if (agenda.getEquipe().getCodEquipe() != null) {
                stm.setInt(1, agenda.getEquipe().getCodEquipe());
            }

            if (agenda.getProfissional().getId() != null) {
                stm.setLong(2, agenda.getProfissional().getId());
            } else if (agenda.getEquipe().getCodEquipe() != null) {
                stm.setInt(2, agenda.getEquipe().getCodEquipe());
            }

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                ConfigAgendaParte1Bean configAgendaParte1Bean = new ConfigAgendaParte1Bean();
                configAgendaParte1Bean.setTurno(rs.getString("turno"));
                configAgendaParte1Bean.setDiaSemana(rs.getString("diasemana"));
                configAgendaParte1Bean.setDataEspecifica(rs.getDate("data_especifica"));

                lista.add(configAgendaParte1Bean);
            }
        } catch (SQLException ex2) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return lista;
    }

    /*
     * public List<ConfigAgendaParte1Bean> retornarDiaAtendimentoEquipe(Integer
     * codEquipe) throws ProjetoException { List<ConfigAgendaParte1Bean> lista = new
     * ArrayList<ConfigAgendaParte1Bean>();
     *
     * StringBuilder sql = new StringBuilder();
     *
     * sql.append("SELECT e.descequipe, ");
     * sql.append("CASE WHEN d.turno = 'M' THEN 'Manhã' ");
     * sql.append("WHEN d.turno = 'T' THEN 'Tarde' END AS turno, ");
     * sql.append("CASE WHEN d.dia = 1 THEN 'Domingo' ");
     * sql.append("WHEN d.dia = 2 THEN 'Segunda' ");
     * sql.append("WHEN d.dia = 3 THEN 'Terça' ");
     * sql.append("WHEN d.dia = 4 THEN 'Quarta' ");
     * sql.append("WHEN d.dia = 5 THEN 'Quinta' ");
     * sql.append("WHEN d.dia = 6 THEN 'Sexta' ");
     * sql.append("WHEN d.dia = 7 THEN 'Sábado' END AS dia ");
     * sql.append("FROM hosp.config_agenda_equipe c ");
     * sql.append("LEFT JOIN hosp.equipe e ON (c.codequipe = e.id_equipe) "); sql.
     * append("LEFT JOIN hosp.config_agenda_equipe_dias d ON (c.id_configagenda = d.id_config_agenda_equipe) "
     * ); sql.append("WHERE e.id_equipe = ? AND  "); sql.
     * append("c.mes = (SELECT DATE_PART('MONTH', CURRENT_TIMESTAMP)) AND c.ano = (SELECT DATE_PART('YEAR', CURRENT_TIMESTAMP)) "
     * ); sql.append("ORDER BY e.descequipe, d.dia, d.turno");
     *
     *
     * try { con = ConnectionFactory.getConnection(); PreparedStatement stm = null;
     * stm = con.prepareStatement(sql.toString());
     *
     * stm.setInt(1, codEquipe);
     *
     * ResultSet rs = stm.executeQuery();
     *
     * while (rs.next()) { ConfigAgendaParte1Bean configAgendaParte1Bean = new
     * ConfigAgendaParte1Bean();
     * configAgendaParte1Bean.getEquipe().setDescEquipe(rs.getString("descequipe"));
     * configAgendaParte1Bean.setTurno(rs.getString("turno"));
     * configAgendaParte1Bean.setDiaSemana(rs.getString("dia"));
     *
     * lista.add(configAgendaParte1Bean); } } catch (Exception ex) {
     * ex.printStackTrace(); throw new RuntimeException(ex); } finally { try {
     * con.close(); } catch (Exception ex) { ex.printStackTrace(); } } return lista;
     * }
     *
     *
     */
    public Boolean numeroAtendimentosEquipe(InsercaoPacienteBean insercao) throws ProjetoException {
        Boolean resultado = false;

        FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("obj_funcionario");

        StringBuilder sql = new StringBuilder();

        sql.append("SELECT ");
        sql.append("CASE WHEN (SELECT count(id_atendimento) FROM hosp.atendimentos WHERE ");
        sql.append("dtaatende = ? AND codequipe = ?) < p.qtd_simultanea_atendimento_equipe ");
        sql.append("THEN TRUE ELSE FALSE END AS pode_marcar ");
        sql.append("FROM hosp.parametro p WHERE p.codunidade = ?");

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql.toString());
            stm.setDate(1, DataUtil.converterDateUtilParaDateSql(insercao.getDataSolicitacao()));
            stm.setInt(2, insercao.getEquipe().getCodEquipe());
            stm.setInt(3, user_session.getUnidade().getId());
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                resultado = rs.getBoolean("pode_marcar");
            }

        } catch (SQLException ex2) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return resultado;
    }

    public Boolean numeroAtendimentosProfissional(InsercaoPacienteBean insercao) throws ProjetoException {
        Boolean resultado = false;

        FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("obj_funcionario");

        StringBuilder sql = new StringBuilder();

        if (insercao.getHorario() != null) {
            sql.append("SELECT ");
            sql.append("CASE WHEN (SELECT count(id_atendimento) FROM hosp.atendimentos WHERE horario = ? ");
            sql.append("AND dtaatende = ? AND codmedico = ?) < p.qtd_simultanea_atendimento_profissional ");
            sql.append("THEN TRUE ELSE FALSE END AS pode_marcar ");
            sql.append("FROM hosp.parametro p WHERE p.codunidade = ?");
        }

        if (insercao.getTurno() != null) {
            sql.append("SELECT ");
            sql.append("CASE WHEN (SELECT count(id_atendimento) FROM hosp.atendimentos WHERE turno = ? ");
            sql.append("AND dtaatende = ? AND codmedico = ?) < p.qtd_simultanea_atendimento_profissional ");
            sql.append("THEN TRUE ELSE FALSE END AS pode_marcar ");
            sql.append("FROM hosp.parametro p WHERE p.codunidade = ?");
        }

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql.toString());

            if (insercao.getHorario() != null) {
                stm.setTime(1, DataUtil.retornarHorarioEmTime(insercao.getHorario()));
            }

            if (insercao.getTurno() != null) {
                stm.setString(1, insercao.getTurno());
            }

            stm.setDate(2, DataUtil.converterDateUtilParaDateSql(insercao.getDataSolicitacao()));
            stm.setLong(3, insercao.getFuncionario().getId());
            stm.setInt(4, user_session.getUnidade().getId());
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                resultado = rs.getBoolean("pode_marcar");
            }

        } catch (SQLException ex2) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return resultado;
    }

    public List<AgendaBean> quantidadeDeAgendamentosDaEquipePorTurno() throws ProjetoException {
        List<AgendaBean> lista = new ArrayList<AgendaBean>();

        FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("obj_funcionario");

        StringBuilder sql = new StringBuilder();

        sql.append("SELECT DISTINCT e.id_equipe, e.descequipe, ");
        sql.append("(SELECT count(codpaciente) FROM ( ");
        sql.append("SELECT DISTINCT aa.codpaciente AS codpaciente FROM hosp.atendimentos aa ");
        sql.append("JOIN hosp.paciente_instituicao pp ON (aa.id_paciente_instituicao = pp.id) ");
        sql.append(
                "JOIN hosp.profissional_dia_atendimento pda ON (aa.id_paciente_instituicao = pda.id_paciente_instituicao) ");
        sql.append("WHERE ((aa.horario >= '07:00' AND aa.horario <= '12:00') OR aa.turno = 'M') ");
        sql.append("AND aa.codequipe = e.id_equipe ");
        sql.append("AND pp.status = 'A' ");
        sql.append("AND pda.dia_semana = 2 ) a1) AS qtd_manha_segunda, ");
        sql.append("(SELECT count(codpaciente) FROM ( ");
        sql.append("SELECT DISTINCT aa.codpaciente AS codpaciente FROM hosp.atendimentos aa ");
        sql.append("JOIN hosp.paciente_instituicao pp ON (aa.id_paciente_instituicao = pp.id) ");
        sql.append(
                "JOIN hosp.profissional_dia_atendimento pda ON (aa.id_paciente_instituicao = pda.id_paciente_instituicao) ");
        sql.append("WHERE ((aa.horario >= '07:00' AND aa.horario <= '12:00') OR aa.turno = 'M') ");
        sql.append("AND aa.codequipe = e.id_equipe ");
        sql.append("AND pp.status = 'A' ");
        sql.append("AND pda.dia_semana = 3 ) a1) AS qtd_manha_terca, ");
        sql.append("(SELECT count(codpaciente) FROM ( ");
        sql.append("SELECT DISTINCT aa.codpaciente AS codpaciente FROM hosp.atendimentos aa ");
        sql.append("JOIN hosp.paciente_instituicao pp ON (aa.id_paciente_instituicao = pp.id) ");
        sql.append(
                "JOIN hosp.profissional_dia_atendimento pda ON (aa.id_paciente_instituicao = pda.id_paciente_instituicao) ");
        sql.append("WHERE ((aa.horario >= '07:00' AND aa.horario <= '12:00') OR aa.turno = 'M') ");
        sql.append("AND aa.codequipe = e.id_equipe ");
        sql.append("AND pp.status = 'A' ");
        sql.append("AND pda.dia_semana = 4 ) a1) AS qtd_manha_quarta, ");
        sql.append("(SELECT count(codpaciente) FROM ( ");
        sql.append("SELECT DISTINCT aa.codpaciente AS codpaciente FROM hosp.atendimentos aa ");
        sql.append("JOIN hosp.paciente_instituicao pp ON (aa.id_paciente_instituicao = pp.id) ");
        sql.append(
                "JOIN hosp.profissional_dia_atendimento pda ON (aa.id_paciente_instituicao = pda.id_paciente_instituicao) ");
        sql.append("WHERE ((aa.horario >= '07:00' AND aa.horario <= '12:00') OR aa.turno = 'M') ");
        sql.append("AND aa.codequipe = e.id_equipe ");
        sql.append("AND pp.status = 'A' ");
        sql.append("AND pda.dia_semana = 5 ) a1) AS qtd_manha_quinta, ");
        sql.append("(SELECT count(codpaciente) FROM ( ");
        sql.append("SELECT DISTINCT aa.codpaciente AS codpaciente FROM hosp.atendimentos aa ");
        sql.append("JOIN hosp.paciente_instituicao pp ON (aa.id_paciente_instituicao = pp.id) ");
        sql.append(
                "JOIN hosp.profissional_dia_atendimento pda ON (aa.id_paciente_instituicao = pda.id_paciente_instituicao) ");
        sql.append("WHERE ((aa.horario >= '07:00' AND aa.horario <= '12:00') OR aa.turno = 'M') ");
        sql.append("AND aa.codequipe = e.id_equipe ");
        sql.append("AND pp.status = 'A' ");
        sql.append("AND pda.dia_semana = 6 ) a1) AS qtd_manha_sexta, ");
        sql.append("(SELECT count(codpaciente) FROM ( ");
        sql.append("SELECT DISTINCT aa.codpaciente AS codpaciente FROM hosp.atendimentos aa ");
        sql.append("JOIN hosp.paciente_instituicao pp ON (aa.id_paciente_instituicao = pp.id) ");
        sql.append(
                "JOIN hosp.profissional_dia_atendimento pda ON (aa.id_paciente_instituicao = pda.id_paciente_instituicao) ");
        sql.append("WHERE (horario >'12:00' OR turno = 'T') ");
        sql.append("AND aa.codequipe = e.id_equipe ");
        sql.append("AND pp.status = 'A' ");
        sql.append("AND pda.dia_semana = 2 ) a1) AS qtd_tarde_segunda, ");
        sql.append("(SELECT count(codpaciente) FROM ( ");
        sql.append("SELECT DISTINCT aa.codpaciente AS codpaciente FROM hosp.atendimentos aa ");
        sql.append("JOIN hosp.paciente_instituicao pp ON (aa.id_paciente_instituicao = pp.id) ");
        sql.append(
                "JOIN hosp.profissional_dia_atendimento pda ON (aa.id_paciente_instituicao = pda.id_paciente_instituicao) ");
        sql.append("WHERE (horario >'12:00' OR turno = 'T') ");
        sql.append("AND aa.codequipe = e.id_equipe ");
        sql.append("AND pp.status = 'A' ");
        sql.append("AND pda.dia_semana = 3 ) a1) AS qtd_tarde_terca, ");
        sql.append("(SELECT count(codpaciente) FROM ( ");
        sql.append("SELECT DISTINCT aa.codpaciente AS codpaciente FROM hosp.atendimentos aa ");
        sql.append("JOIN hosp.paciente_instituicao pp ON (aa.id_paciente_instituicao = pp.id) ");
        sql.append(
                "JOIN hosp.profissional_dia_atendimento pda ON (aa.id_paciente_instituicao = pda.id_paciente_instituicao) ");
        sql.append("WHERE (horario >'12:00' OR turno = 'T') ");
        sql.append("AND aa.codequipe = e.id_equipe ");
        sql.append("AND pp.status = 'A' ");
        sql.append("AND pda.dia_semana = 4 ) a1) AS qtd_tarde_quarta, ");
        sql.append("(SELECT count(codpaciente) FROM ( ");
        sql.append("SELECT DISTINCT aa.codpaciente AS codpaciente FROM hosp.atendimentos aa ");
        sql.append("JOIN hosp.paciente_instituicao pp ON (aa.id_paciente_instituicao = pp.id) ");
        sql.append(
                "JOIN hosp.profissional_dia_atendimento pda ON (aa.id_paciente_instituicao = pda.id_paciente_instituicao) ");
        sql.append("WHERE (horario >'12:00' OR turno = 'T') ");
        sql.append("AND aa.codequipe = e.id_equipe ");
        sql.append("AND pp.status = 'A' ");
        sql.append("AND pda.dia_semana = 5 ) a1) AS qtd_tarde_quinta, ");
        sql.append("(SELECT count(codpaciente) FROM ( ");
        sql.append("SELECT DISTINCT aa.codpaciente AS codpaciente FROM hosp.atendimentos aa ");
        sql.append("JOIN hosp.paciente_instituicao pp ON (aa.id_paciente_instituicao = pp.id) ");
        sql.append(
                "JOIN hosp.profissional_dia_atendimento pda ON (aa.id_paciente_instituicao = pda.id_paciente_instituicao) ");
        sql.append("WHERE (horario >'12:00' OR turno = 'T') ");
        sql.append("AND aa.codequipe = e.id_equipe ");
        sql.append("AND pp.status = 'A' ");
        sql.append("AND pda.dia_semana = 6 ) a1) AS qtd_tarde_sexta ");
        sql.append("FROM hosp.atendimentos a ");
        sql.append("LEFT JOIN hosp.equipe e ON (a.codequipe = e.id_equipe) ");
        sql.append("WHERE a.codequipe IS NOT NULL AND a.cod_unidade = ? ");
        sql.append("ORDER BY e.descequipe ");

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = null;
            stm = con.prepareStatement(sql.toString());

            stm.setInt(1, user_session.getUnidade().getId());

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                AgendaBean agendaBean = new AgendaBean();
                agendaBean.getEquipe().setCodEquipe(rs.getInt("id_equipe"));
                agendaBean.getEquipe().setDescEquipe(rs.getString("descequipe"));
                agendaBean.getQtdAtendimentosManha()[SEGUNDA] = rs.getInt("qtd_manha_segunda");
                agendaBean.getQtdAtendimentosManha()[TERCA] = rs.getInt("qtd_manha_terca");
                agendaBean.getQtdAtendimentosManha()[QUARTA] = rs.getInt("qtd_manha_quarta");
                agendaBean.getQtdAtendimentosManha()[QUINTA] = rs.getInt("qtd_manha_quinta");
                agendaBean.getQtdAtendimentosManha()[SEXTA] = rs.getInt("qtd_manha_sexta");
                agendaBean.getQtdAtendimentosTarde()[SEGUNDA] = rs.getInt("qtd_tarde_segunda");
                agendaBean.getQtdAtendimentosTarde()[TERCA] = rs.getInt("qtd_tarde_terca");
                agendaBean.getQtdAtendimentosTarde()[QUARTA] = rs.getInt("qtd_tarde_quarta");
                agendaBean.getQtdAtendimentosTarde()[QUINTA] = rs.getInt("qtd_tarde_quinta");
                agendaBean.getQtdAtendimentosTarde()[SEXTA] = rs.getInt("qtd_tarde_sexta");

                lista.add(agendaBean);
            }

        } catch (SQLException ex2) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return lista;
    }

    public Integer contarAtendimentosPorTipoAtendimentoPorProfissionalDataUnica(AgendaBean agenda,
                                                                                java.util.Date dataAtendimento) throws ProjetoException {
        Integer qtd = null;

        String sql = "";

        sql = "SELECT count(id_atendimento) as qtd FROM hosp.atendimentos WHERE codtipoatendimento = ? AND codmedico = ? AND turno = ? "
                + "AND (dtaatende = ? OR (extract(month from dtaatende) = ? AND (extract(year from dtaatende) = ?)))";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setLong(1, agenda.getTipoAt().getIdTipo());
            stm.setLong(2, agenda.getProfissional().getId());
            stm.setString(3, agenda.getTurno().toUpperCase());
            stm.setDate(4, new java.sql.Date(dataAtendimento.getTime()));
            stm.setInt(5, DataUtil.extrairMesDeData(dataAtendimento));
            stm.setInt(6, DataUtil.extrairAnoDeData(dataAtendimento));

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                qtd = rs.getInt("qtd");
            }

        } catch (SQLException ex2) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return qtd;
    }

    public Integer contarAtendimentosPorTipoAtendimentoPorEquipeDataUnica(AgendaBean agenda,
                                                                          java.util.Date dataAtendimento) throws ProjetoException {
        Integer qtd = null;

        String sql = "";

        sql = "SELECT count(id_atendimento) as qtd FROM hosp.atendimentos WHERE codtipoatendimento = ? AND codequipe = ? AND turno = ? "
                + "AND (dtaatende = ? OR (extract(month from dtaatende) = ? AND (extract(year from dtaatende) = ?)))";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setLong(1, agenda.getTipoAt().getIdTipo());
            stm.setLong(2, agenda.getEquipe().getCodEquipe());
            stm.setString(3, agenda.getTurno().toUpperCase());
            stm.setDate(4, new java.sql.Date(dataAtendimento.getTime()));
            stm.setInt(5, DataUtil.extrairMesDeData(dataAtendimento));
            stm.setInt(6, DataUtil.extrairAnoDeData(dataAtendimento));

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                qtd = rs.getInt("qtd");
            }

        } catch (SQLException ex2) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return qtd;
    }

    public ArrayList<HorarioAtendimento> listaDiasDeAtendimetoParaPacienteInstituicao(Integer idPacienteInstituicao)
            throws ProjetoException {
        ArrayList<HorarioAtendimento> lista = new ArrayList<HorarioAtendimento>();

        String sql = "SELECT dia_semana, horario_atendimento, id_profissional FROM hosp.profissional_dia_atendimento WHERE id_paciente_instituicao = ?";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = null;
            stm = con.prepareStatement(sql);
            stm.setInt(1, idPacienteInstituicao);

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                HorarioAtendimento diaAtendimento = new HorarioAtendimento();
                diaAtendimento.setDiaSemana(rs.getInt("dia_semana"));
                diaAtendimento.setHorario(rs.getString("horario_atendimento"));
                diaAtendimento.getFuncionario().setId(rs.getLong("id_profissional"));
                lista.add(diaAtendimento);
            }
        } catch (SQLException ex2) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return lista;
    }

    public ArrayList<FuncionarioBean> listaProfissionaisAtendimetoParaPacienteInstituicao(Integer idPacienteInstituicao)
            throws ProjetoException {
        ArrayList<FuncionarioBean> lista = new ArrayList<FuncionarioBean>();

        String sql = "SELECT DISTINCT p.id_profissional, f.descfuncionario, e.id_especialidade, e.descespecialidade, c.id, c.descricao, c.codigo codcbo  "
                + " FROM hosp.profissional_dia_atendimento p "
                + "JOIN acl.funcionarios f ON (p.id_profissional = f.id_funcionario) "
                + "LEFT JOIN hosp.especialidade e ON (f.codespecialidade = e.id_especialidade) "
                + "LEFT JOIN hosp.cbo c ON (f.codcbo = c.id) " + "WHERE p.id_paciente_instituicao = ?";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = null;
            stm = con.prepareStatement(sql);
            stm.setInt(1, idPacienteInstituicao);

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                FuncionarioBean funcionario = new FuncionarioBean();
                funcionario.setId(rs.getLong("id_profissional"));
                funcionario.setNome(rs.getString("descfuncionario"));
                funcionario.getEspecialidade().setDescEspecialidade(rs.getString("descespecialidade"));
                funcionario.getEspecialidade().setCodEspecialidade(rs.getInt("id_especialidade"));
                funcionario.getCbo().setCodCbo(rs.getInt("id"));
                funcionario.getCbo().setDescCbo(rs.getString("descricao"));
                funcionario.getCbo().setCodigo(rs.getString("codcbo"));
                lista.add(funcionario);
            }
        } catch (SQLException ex2) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return lista;
    }

    public ArrayList<FuncionarioBean> listaProfissionaisIhDiasIhHorariosAtendimetoParaPacienteInstituicao(
            Integer idPacienteInstituicao) throws ProjetoException {
        ArrayList<FuncionarioBean> lista = new ArrayList<FuncionarioBean>();

        String sql = "SELECT DISTINCT p.id_profissional, f.descfuncionario, e.id_especialidade, e.descespecialidade, c.id, c.descricao, c.codigo codcbo,   to_char(p.horario_atendimento,'HH24:MI') horario_atendimento  "
                + "  FROM hosp.profissional_dia_atendimento p "
                + "JOIN acl.funcionarios f ON (p.id_profissional = f.id_funcionario) "
                + "LEFT JOIN hosp.especialidade e ON (f.codespecialidade = e.id_especialidade) "
                + "LEFT JOIN hosp.cbo c ON (f.codcbo = c.id) " + "WHERE p.id_paciente_instituicao = ?";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = null;
            stm = con.prepareStatement(sql);
            stm.setInt(1, idPacienteInstituicao);

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                FuncionarioBean funcionario = new FuncionarioBean();
                funcionario.setId(rs.getLong("id_profissional"));
                funcionario.setNome(rs.getString("descfuncionario"));
                funcionario.getEspecialidade().setDescEspecialidade(rs.getString("descespecialidade"));
                funcionario.setListaDiasAtendimentoSemana(listaDiasDeAtendimetoParaPacienteInstituicaoIhProfissional(
                        idPacienteInstituicao, funcionario.getId(), con));
                funcionario.getEspecialidade().setCodEspecialidade(rs.getInt("id_especialidade"));
                funcionario.getCbo().setCodCbo(rs.getInt("id"));
                funcionario.getCbo().setDescCbo(rs.getString("descricao"));
                funcionario.getCbo().setCodigo(rs.getString("codcbo"));
                funcionario.setHorarioAtendimento(rs.getString("horario_atendimento"));
                lista.add(funcionario);
            }
        } catch (SQLException ex2) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return lista;
    }

    public ArrayList<HorarioAtendimento> listaDiasDeAtendimetoParaPacienteInstituicaoIhProfissional
            (Integer idPacienteInstituicao, Long idProfissional, Connection conAuxiliar) throws ProjetoException, SQLException {
        ArrayList<HorarioAtendimento> lista = new ArrayList<HorarioAtendimento>();

        String sql = "SELECT dia_semana, horario_atendimento, id_profissional FROM hosp.profissional_dia_atendimento WHERE id_paciente_instituicao = ? AND id_profissional = ?";

        try {
            PreparedStatement stm = null;
            stm = conAuxiliar.prepareStatement(sql);
            stm.setInt(1, idPacienteInstituicao);
            stm.setLong(2, idProfissional);

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                HorarioAtendimento diaAtendimento = new HorarioAtendimento();
                diaAtendimento.setDiaSemana(rs.getInt("dia_semana"));
                diaAtendimento.setHorario(rs.getString("horario_atendimento"));
                diaAtendimento.getFuncionario().setId(rs.getLong("id_profissional"));
                lista.add(diaAtendimento);
            }
        } catch (SQLException ex2) {
            conAuxiliar.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
        } catch (Exception ex) {
            conAuxiliar.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
        return lista;
    }

    public Boolean verificarSeAtendimentoFoiRealizado(Integer idAgenda) throws ProjetoException {

        Boolean resultado = false;

        String sql = "SELECT id_atendimentos1 FROM hosp.atendimentos1 a1 join hosp.situacao_atendimento sa " +
                "on a1.id_situacao_atendimento = sa.id " +
                "WHERE sa.atendimento_realizado = true AND a1.id_atendimento = ? and coalesce(a1.excluido,'N' )='N';";

        try {
            con = ConnectionFactory.getConnection();

            PreparedStatement stm = con.prepareStatement(sql);

            stm.setInt(1, idAgenda);

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                resultado = true;
            }

        } catch (SQLException ex2) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return resultado;
    }

    public boolean cancelarAgendamento(Integer idAgenda) throws ProjetoException {

        Boolean retorno = false;

        String sql = "update hosp.atendimentos set situacao = ? where id_atendimento = ?";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setString(1, "C");
            stmt.setInt(2, idAgenda);
            stmt.execute();
            con.commit();
            retorno = true;
        } catch (SQLException ex2) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return retorno;
    }


    public Boolean verificarDuplicidadeAgendaAvulsa(AgendaBean agenda) throws ProjetoException {
        Boolean existeAgendaAvulsa = false;

        String sql = "SELECT EXISTS " +
                " (SELECT a.id_atendimento FROM  hosp.atendimentos a " +
                " LEFT JOIN hosp.pacientes p ON (p.id_paciente = a.codpaciente) " +
                " LEFT JOIN hosp.tipoatendimento t ON (t.id = a.codtipoatendimento) " +
                " LEFT JOIN hosp.grupo g ON a.codgrupo = g.id_grupo " +
                " LEFT JOIN hosp.programa pr ON a.codprograma = pr.id_programa " +
                " WHERE a.cod_unidade = ? AND coalesce(a.situacao,'')<>'C' "+
                " AND coalesce(a1.excluido, '')<>'S'" +
                " AND a.dtaatende = ? AND a.avulso IS TRUE " +
                " AND t.id = ? AND g.id_grupo = ? AND pr.id_programa = ?) existe_atendimento_avulso";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, agenda.getUnidade().getId());
            stm.setDate(2, new java.sql.Date(agenda.getDataAtendimento().getTime()));
            stm.setInt(3, agenda.getTipoAt().getIdTipo());
            stm.setInt(4, agenda.getGrupo().getIdGrupo());
            stm.setInt(5, agenda.getPrograma().getIdPrograma());
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                existeAgendaAvulsa = rs.getBoolean("existe_atendimento_avulso");
            }

        } catch (SQLException ex2) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return existeAgendaAvulsa;
    }

    public Integer retornaIdCidDoLaudo(AgendaBean agenda, Integer codUnidade, Integer idPaciente) throws ProjetoException {
        Integer idCid = null;

        String sql = "select " +
                "	l.cid1 " +
                "from " +
                "	hosp.paciente_instituicao p " +
                "left join hosp.laudo l on " +
                "	(l.id_laudo = p.codlaudo) " +
                "where 1=1 " +
                "	and p.cod_unidade = ? " +
                "	and p.codprograma = ? " +
                "	and p.codgrupo = ? " +
                "	and p.status = 'A' " +
                "and coalesce(l.codpaciente, p.id_paciente) = ? " +
                "limit 1;";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, codUnidade);
            ps.setInt(2, agenda.getPrograma().getIdPrograma());
            ps.setInt(3, agenda.getGrupo().getIdGrupo());
            ps.setInt(4, idPaciente);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                idCid = rs.getInt("cid1");
            }

        } catch (SQLException ex2) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return idCid;
    }
    
    public Integer retornaIdProcedimentoEspecifico(Integer idPrograma, Integer idCbo, Integer idPaciente, Connection conAuxiliar) throws ProjetoException, SQLException {
    	Integer idProcedimentoEspecifico = null;
    	try {
			idProcedimentoEspecifico = retornaIdProcedimentoCboEspecifico(idPrograma, idCbo, conAuxiliar);
			if (VerificadorUtil.verificarSeObjetoNuloOuZero(idProcedimentoEspecifico)) {
				idProcedimentoEspecifico = retornaIdProcedimentoIdadeEspecifica(idPrograma, idPaciente, conAuxiliar);
			}
    	} catch (SQLException ex2) {
            conAuxiliar.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
        } catch (Exception ex) {
            conAuxiliar.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
    	
    	return idProcedimentoEspecifico;
    }

    private Integer retornaIdProcedimentoCboEspecifico(Integer idPrograma, Integer idCbo, Connection conAuxiliar)
            throws ProjetoException, SQLException {

        Integer idProcedimento = null;

        String sql = "select ppc.id_procedimento from hosp.cbo c " +
                "join hosp.programa_procedimento_cbo_especifico ppc on c.id = ppc.id_cbo " +
                "where ppc.id_programa = ? and ppc.id_cbo = ?;";

        try {
            PreparedStatement stm = conAuxiliar.prepareStatement(sql);
            stm.setInt(1, idPrograma);
            if (VerificadorUtil.verificarSeObjetoNuloOuZero(idCbo))
                stm.setNull(2, Types.NULL);
            else
                stm.setInt(2, idCbo);

            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                idProcedimento = rs.getInt("id_procedimento");
            }

        } catch (SQLException ex2) {
            conAuxiliar.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
        } catch (Exception ex) {
            conAuxiliar.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
        return idProcedimento;
    }
    
    
    private Integer retornaIdProcedimentoIdadeEspecifica(Integer idPrograma, Integer idPaciente, Connection conAuxiliar)
            throws ProjetoException, SQLException {

        Integer idProcedimento = null;

        String sql = "select ppie.id_procedimento from hosp.proc p " + 
        		"join hosp.programa_procedimento_idade_especifica ppie on p.id = ppie.id_procedimento " + 
        		"where ppie.id_programa = ? and ( " + 
        		"	(select extract (year from age(p.dtanascimento)) idade from hosp.pacientes p " + 
        		"	where p.id_paciente = ?)" + 
        		"between ppie.idade_minima and ppie.idade_maxima);";

        try {
            PreparedStatement stm = conAuxiliar.prepareStatement(sql);
            stm.setInt(1, idPrograma);
            stm.setInt(2, idPaciente);

            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                idProcedimento = rs.getInt("id_procedimento");
            }

        } catch (SQLException ex2) {
            conAuxiliar.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
        } catch (Exception ex) {
            conAuxiliar.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
        return idProcedimento;
    }

    
    public Boolean verificaExisteEspecialidadeNestaData (Integer idPaciente, Date dataAtende, Integer idEspecialidade)
            throws ProjetoException, SQLException {

        Boolean existe = true;

        String sql = "select exists (select f.codespecialidade, a.dtaatende from hosp.atendimentos a " + 
        		"		join hosp.atendimentos1 a1 on a.id_atendimento = a1.id_atendimento " + 
        		"		join hosp.pacientes p on a.codpaciente = p.id_paciente " + 
        		"		join acl.funcionarios f on a1.codprofissionalatendimento = f.id_funcionario " + 
        		"		join hosp.especialidade e on f.codespecialidade = e.id_especialidade " + 
        		"		where p.id_paciente = ? and a.dtaatende = ? and f.codespecialidade = ?) existe; ";

        try {
        	con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, idPaciente);
            stm.setDate(2, new java.sql.Date(dataAtende.getTime()));
            stm.setInt(3, idEspecialidade);

            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                existe = rs.getBoolean("existe");
            }

        } catch (SQLException ex2) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return existe;
    }
}
