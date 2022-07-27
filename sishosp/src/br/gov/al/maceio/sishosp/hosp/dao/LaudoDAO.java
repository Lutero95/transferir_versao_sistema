package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.DataUtil;
import br.gov.al.maceio.sishosp.comum.util.TratamentoErrosUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.enums.MotivoLiberacao;
import br.gov.al.maceio.sishosp.hosp.enums.SituacaoLaudo;
import br.gov.al.maceio.sishosp.hosp.enums.TipoBuscaLaudo;
import br.gov.al.maceio.sishosp.hosp.log.control.LaudoLog;
import br.gov.al.maceio.sishosp.hosp.log.dao.LogDAO;
import br.gov.al.maceio.sishosp.hosp.log.model.LogBean;
import br.gov.al.maceio.sishosp.hosp.model.InsercaoPacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.LaudoBean;
import br.gov.al.maceio.sishosp.hosp.model.PacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.dto.BuscaIdadePacienteDTO;
import br.gov.al.maceio.sishosp.hosp.model.dto.BuscaLaudoDTO;
import br.gov.al.maceio.sishosp.hosp.model.dto.PacienteLaudoEmLoteDTO;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.v1.procedimento.ProcedimentoType;
import sigtap.br.gov.saude.servicos.schema.sigtap.v1.idadelimite.IdadeLimiteType;
import sigtap.br.gov.saude.servicos.schema.sigtap.v1.idadelimite.UnidadeLimiteType;

import javax.faces.context.FacesContext;

public class LaudoDAO {

    PreparedStatement ps = null;
    private Connection conexao = null;

    FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
            .getSessionMap().get("obj_funcionario");

    public boolean existeLaudoComMesmosDados(LaudoBean laudo, PacienteBean paciente) throws ProjetoException {

        boolean existeLaudoComMesmosDados = true;
        String sql = "select exists ( " +
                "	select l.id_laudo from hosp.laudo l " +
                "		where l.codpaciente = ? and l.mes_inicio = ? and l.ano_inicio = ? " +
                "		and l.mes_final = ? and l.ano_final = ? and l.codprocedimento_primario = ? "+
                "		and l.cod_profissional = ? and l.ativo is true) existe_laudo_com_mesmos_dados";

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stm = conexao.prepareStatement(sql);
            stm.setInt(1, paciente.getId_paciente());
            stm.setInt(2, laudo.getMesInicio());
            stm.setInt(3, laudo.getAnoInicio());
            stm.setInt(4, laudo.getMesFinal());
            stm.setInt(5, laudo.getAnoFinal());
            stm.setInt(6, laudo.getProcedimentoPrimario().getIdProc());
            stm.setLong(7, laudo.getProfissionalLaudo().getId());
            ResultSet rs = stm.executeQuery();

            if (rs.next())
                existeLaudoComMesmosDados = rs.getBoolean("existe_laudo_com_mesmos_dados");

        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return existeLaudoComMesmosDados;
    }

    public Integer cadastrarLaudo(LaudoBean laudo) throws ProjetoException {

        FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("obj_funcionario");

        Integer idLaudoGerado = null;

        String sql = "insert into hosp.laudo "
                + "(codpaciente,  data_solicitacao, mes_inicio, ano_inicio, mes_final, ano_final, periodo, codprocedimento_primario, "
                + "codprocedimento_secundario1, codprocedimento_secundario2, codprocedimento_secundario3, codprocedimento_secundario4, codprocedimento_secundario5, "
                + "cid1, cid2, cid3, obs, ativo, cod_unidade, data_hora_operacao, situacao, cod_profissional, validado_pelo_sigtap_anterior, data_autorizacao, usuario_cadastro, "
                + " profissional_externo, nome_profissional_externo, conselho_numero) "
                + "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, true, ?, CURRENT_TIMESTAMP, ?, ?, ?, ?,?, ?, ?,? ) returning id_laudo";

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setLong(1, laudo.getPaciente().getId_paciente());
            stmt.setDate(2, new java.sql.Date(laudo.getDataSolicitacao().getTime()));
            stmt.setInt(3, laudo.getMesInicio());
            stmt.setInt(4, laudo.getAnoInicio());
            stmt.setInt(5, laudo.getMesFinal());
            stmt.setInt(6, laudo.getAnoFinal());
            stmt.setInt(7, laudo.getPeriodo());
            stmt.setInt(8, laudo.getProcedimentoPrimario().getIdProc());
            if (laudo.getProcedimentoSecundario1() != null) {
                if (laudo.getProcedimentoSecundario1().getIdProc() != null) {
                    stmt.setInt(9, laudo.getProcedimentoSecundario1().getIdProc());
                } else {
                    stmt.setNull(9, Types.NULL);
                }
            } else {
                stmt.setNull(9, Types.NULL);
            }

            if (laudo.getProcedimentoSecundario2() != null) {
                if (laudo.getProcedimentoSecundario2().getIdProc() != null) {
                    stmt.setInt(10, laudo.getProcedimentoSecundario2().getIdProc());
                } else {
                    stmt.setNull(10, Types.NULL);
                }
            } else {
                stmt.setNull(10, Types.NULL);
            }

            if (laudo.getProcedimentoSecundario3() != null) {
                if (laudo.getProcedimentoSecundario3().getIdProc() != null) {
                    stmt.setInt(11, laudo.getProcedimentoSecundario3().getIdProc());
                } else {
                    stmt.setNull(11, Types.NULL);
                }
            } else {
                stmt.setNull(11, Types.NULL);
            }

            if (laudo.getProcedimentoSecundario4() != null) {
                if (laudo.getProcedimentoSecundario4().getIdProc() != null) {
                    stmt.setInt(12, laudo.getProcedimentoSecundario4().getIdProc());
                } else {
                    stmt.setNull(12, Types.NULL);
                }
            } else {
                stmt.setNull(12, Types.NULL);
            }

            if (laudo.getProcedimentoSecundario5() != null) {
                if (laudo.getProcedimentoSecundario5().getIdProc() != null) {
                    stmt.setInt(13, laudo.getProcedimentoSecundario5().getIdProc());
                } else {
                    stmt.setNull(13, Types.NULL);
                }
            } else {
                stmt.setNull(13, Types.NULL);
            }

            if (laudo.getCid1() != null) {
                if (laudo.getCid1().getIdCid() != null) {
                    stmt.setInt(14, laudo.getCid1().getIdCid());
                } else {
                    stmt.setNull(14, Types.NULL);
                }
            } else {
                stmt.setNull(14, Types.NULL);
            }

            if (laudo.getCid2() != null) {
                if (laudo.getCid2().getIdCid() != null) {
                    stmt.setInt(15, laudo.getCid2().getIdCid());
                } else {
                    stmt.setNull(15, Types.NULL);
                }
            } else {
                stmt.setNull(15, Types.NULL);
            }

            if (laudo.getCid3() != null) {
                if (laudo.getCid3().getIdCid() != null) {
                    stmt.setInt(16, laudo.getCid3().getIdCid());
                } else {
                    stmt.setNull(16, Types.NULL);
                }
            } else {
                stmt.setNull(16, Types.NULL);
            }

            if (laudo.getObs() == null) {
                stmt.setNull(17, Types.NULL);
            } else {
                stmt.setString(17, laudo.getObs().toUpperCase().trim());
            }

            stmt.setInt(18, user_session.getUnidade().getId());
            stmt.setString(19, laudo.getSituacao());
            if (laudo.isProfissionalExternoLaudo())
                stmt.setNull(20, Types.NULL);
            else
            stmt.setLong(20, laudo.getProfissionalLaudo().getId());
            stmt.setBoolean(21, laudo.isValidadoPeloSigtapAnterior());
            if (laudo.getDataAutorizacao() == null) {
                stmt.setNull(22, Types.NULL);
            } else {
                stmt.setDate(22, DataUtil.converterDateUtilParaDateSql(laudo.getDataAutorizacao()));
            }

            stmt.setLong(23, user_session.getId());
            stmt.setBoolean(24, laudo.isProfissionalExternoLaudo());
            if (!laudo.isProfissionalExternoLaudo())
                stmt.setNull(25, Types.NULL);
            else
                stmt.setString(25, laudo.getNomeProfissionalExternoLaudo().toUpperCase());

            if (!laudo.isProfissionalExternoLaudo())
                stmt.setNull(26, Types.NULL);
            else
                stmt.setString(26, laudo.getConselhoProfissionalExternoLaudo().toUpperCase());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                idLaudoGerado = rs.getInt("id_laudo");
            }
            conexao.commit();

        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return idLaudoGerado;
    }

    public Boolean alterarLaudo(LaudoBean laudo) throws ProjetoException {
        boolean retorno = false;
        String sql = "update hosp.laudo set codpaciente = ?,  data_solicitacao = ?, mes_inicio = ?, ano_inicio = ?, mes_final = ?, ano_final = ?, "
                + "periodo = ?, codprocedimento_primario = ?, codprocedimento_secundario1 = ?, codprocedimento_secundario2 = ?, codprocedimento_secundario3 = ?, "
                + "codprocedimento_secundario4 = ?, codprocedimento_secundario5 = ?, cid1 = ?, cid2 = ?, cid3 = ?, obs = ?, "
                + "situacao = ?, data_autorizacao = ?, usuario_autorizou = ?, data_hora_operacao = CURRENT_TIMESTAMP, cod_profissional = ?, validado_pelo_sigtap_anterior = ?, "
                + "usuario_ultima_alteracao = ?, data_hora_ultima_alteracao = CURRENT_TIMESTAMP, profissional_externo=?, nome_profissional_externo=?, conselho_numero=? "
                + " where id_laudo = ?";

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setLong(1, laudo.getPaciente().getId_paciente());
            stmt.setDate(2, new java.sql.Date(laudo.getDataSolicitacao().getTime()));
            stmt.setInt(3, laudo.getMesInicio());
            stmt.setInt(4, laudo.getAnoInicio());
            stmt.setInt(5, laudo.getMesFinal());
            stmt.setInt(6, laudo.getAnoFinal());
            stmt.setInt(7, laudo.getPeriodo());
            stmt.setInt(8, laudo.getProcedimentoPrimario().getIdProc());
            if (laudo.getProcedimentoSecundario1() != null) {
                if (laudo.getProcedimentoSecundario1().getIdProc() != null) {
                    stmt.setInt(9, laudo.getProcedimentoSecundario1().getIdProc());
                } else {
                    stmt.setNull(9, Types.NULL);
                }
            } else {
                stmt.setNull(9, Types.NULL);
            }

            if (laudo.getProcedimentoSecundario2() != null) {
                if (laudo.getProcedimentoSecundario2().getIdProc() != null) {
                    stmt.setInt(10, laudo.getProcedimentoSecundario2().getIdProc());
                } else {
                    stmt.setNull(10, Types.NULL);
                }
            } else {
                stmt.setNull(10, Types.NULL);
            }

            if (laudo.getProcedimentoSecundario3() != null) {
                if (laudo.getProcedimentoSecundario3().getIdProc() != null) {
                    stmt.setInt(11, laudo.getProcedimentoSecundario3().getIdProc());
                } else {
                    stmt.setNull(11, Types.NULL);
                }
            } else {
                stmt.setNull(11, Types.NULL);
            }

            if (laudo.getProcedimentoSecundario4() != null) {
                if (laudo.getProcedimentoSecundario4().getIdProc() != null) {
                    stmt.setInt(12, laudo.getProcedimentoSecundario4().getIdProc());
                } else {
                    stmt.setNull(12, Types.NULL);
                }
            } else {
                stmt.setNull(12, Types.NULL);
            }

            if (laudo.getProcedimentoSecundario5() != null) {
                if (laudo.getProcedimentoSecundario5().getIdProc() != null) {
                    stmt.setInt(13, laudo.getProcedimentoSecundario5().getIdProc());
                } else {
                    stmt.setNull(13, Types.NULL);
                }
            } else {
                stmt.setNull(13, Types.NULL);
            }

            if (laudo.getCid1() != null) {
                if (laudo.getCid1().getIdCid() != null) {
                    stmt.setInt(14, laudo.getCid1().getIdCid());
                } else {
                    stmt.setNull(14, Types.NULL);
                }
            } else {
                stmt.setNull(14, Types.NULL);
            }

            if (laudo.getCid2() != null) {
                if (laudo.getCid2().getIdCid() != null) {
                    stmt.setInt(15, laudo.getCid2().getIdCid());
                } else {
                    stmt.setNull(15, Types.NULL);
                }
            } else {
                stmt.setNull(15, Types.NULL);
            }

            if (laudo.getCid3() != null) {
                if (laudo.getCid3().getIdCid() != null) {
                    stmt.setInt(16, laudo.getCid3().getIdCid());
                } else {
                    stmt.setNull(16, Types.NULL);
                }
            } else {
                stmt.setNull(16, Types.NULL);
            }

            if (laudo.getObs() == null) {
                stmt.setNull(17, Types.NULL);
            } else {
                stmt.setString(17, laudo.getObs().toUpperCase().trim());
            }

            stmt.setString(18, laudo.getSituacao().toUpperCase());

            if (laudo.getDataAutorizacao() == null) {
                stmt.setNull(19, Types.NULL);
            } else {
                stmt.setDate(19, DataUtil.converterDateUtilParaDateSql(laudo.getDataAutorizacao()));
            }

            stmt.setLong(20, user_session.getId());
            stmt.setLong(21, laudo.getProfissionalLaudo().getId());
            stmt.setBoolean(22, laudo.isValidadoPeloSigtapAnterior());
            stmt.setLong(23, user_session.getId());

            stmt.setBoolean(24, laudo.isProfissionalExternoLaudo());
            if (!laudo.isProfissionalExternoLaudo())
                stmt.setNull(25, Types.NULL);
            else
                stmt.setString(25, laudo.getNomeProfissionalExternoLaudo().toUpperCase());

            if (!laudo.isProfissionalExternoLaudo())
                stmt.setNull(26, Types.NULL);
            else
                stmt.setString(26, laudo.getConselhoProfissionalExternoLaudo().toUpperCase());

            stmt.setInt(27, laudo.getId());
            stmt.executeUpdate();
            LaudoLog laudoLog = new LaudoLog();
            LogBean log = laudoLog.compararLaudos(laudo);
            if (log.getAlteracaoRealizada())
                new LogDAO().gravarLog(log, conexao);

            conexao.commit();
            retorno = true;
        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return retorno;
    }

    public Boolean excluirLaudo(LaudoBean laudo, FuncionarioBean usuarioLiberacao) throws ProjetoException {
        boolean retorno = false;
        String sql = "update hosp.laudo set ativo = false, data_hora_exclusao = current_timestamp, "
                + "usuario_exclusao = ? where id_laudo = ?";

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);

            stmt.setLong(1, user_session.getId());
            stmt.setInt(2, laudo.getId());
            stmt.executeUpdate();
            inserirLiberacaoLaudo(MotivoLiberacao.EXCLUSAO_LAUDO.getTitulo(), laudo.getId(), usuarioLiberacao, conexao);
            conexao.commit();
            retorno = true;
        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return retorno;
    }
    
    public void inserirLiberacaoLaudo(String motivo, Integer idLaudo, FuncionarioBean usuarioLiberacao, Connection conAuxiliar) throws SQLException, ProjetoException {

        Boolean retorno = false;

        String sql = "INSERT INTO hosp.liberacoes (motivo, usuario_liberacao, data_hora_liberacao, id_laudo, cod_unidade) " +
                "values (?, ?, CURRENT_TIMESTAMP, ?, ?);";

        try {
            PreparedStatement ps2 = conAuxiliar.prepareStatement(sql);
            ps2.setString(1, motivo);
            ps2.setLong(2, usuarioLiberacao.getId());
            ps2.setInt(3, idLaudo);
            ps2.setLong(4, usuarioLiberacao.getUnidade().getId());
            ps2.executeUpdate();
        } catch (SQLException sqle) {
            conAuxiliar.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conAuxiliar.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
    }

    public ArrayList<LaudoBean> listaLaudos(BuscaLaudoDTO buscaLaudoDTO)
            throws ProjetoException {
        FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("obj_funcionario");
        String sql = "select id_laudo,p.id_paciente,p.matricula, p.nome, "
                + "pr.codproc , pr.nome as procedimento, c.desccidabrev,  l.mes_final, l.ano_final, "
                + "CASE WHEN l.situacao = 'A' THEN 'Autorizado' ELSE 'Pendente' END AS situacao, func.id_funcionario, coalesce(func.descfuncionario, l.nome_profissional_externo ) descfuncionario " + "from hosp.laudo l "
                + "left join hosp.pacientes p on (p.id_paciente = l.codpaciente) "
                + "left join hosp.proc pr on (pr.id = l.codprocedimento_primario) "
                + "left join acl.funcionarios func on (func.id_funcionario = l.cod_profissional) "
                + " left join hosp.cid c on c.cod  = l.cid1 "
                + " where l.ativo is true ";

        if (user_session.getUnidade() != null && user_session.getUnidade().getRestringirLaudoPorUnidade() != null &&
                user_session.getUnidade().getRestringirLaudoPorUnidade()) {
            sql = sql + " and l.cod_unidade = ?";
        }

        if (!buscaLaudoDTO.getSituacao().equals(SituacaoLaudo.TODOS.getSigla())) {
            sql = sql + " AND l.situacao = ? ";
        }

        if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(buscaLaudoDTO.getTipoPeriodoData())) {
            if (buscaLaudoDTO.getTipoPeriodoData().equals(SituacaoLaudo.AUTORIZADO.getSigla())
                    && (!VerificadorUtil.verificarSeObjetoNulo(buscaLaudoDTO.getDataInicial())
                    && !VerificadorUtil.verificarSeObjetoNulo(buscaLaudoDTO.getDataFinal()))) {
                sql = sql + " and (l.data_autorizacao between ? and ?) ";
            }

            else if (buscaLaudoDTO.getTipoPeriodoData().equals(SituacaoLaudo.PENDENTE.getSigla())
                    && (!VerificadorUtil.verificarSeObjetoNulo(buscaLaudoDTO.getDataInicial())
                    && !VerificadorUtil.verificarSeObjetoNulo(buscaLaudoDTO.getDataFinal()))) {
                sql = sql + " and (l.data_solicitacao between ? and ?) ";
            }

//			else if (buscaLaudoDTO.getTipoPeriodoData().equals(SituacaoLaudo.TODOS.getSigla())) {
//				sql = sql + " and (l.data_solicitacao between ? and ?) ";
//			}
        }


        if (((buscaLaudoDTO.getTipoBusca().equals(TipoBuscaLaudo.NOME_PACIENTE.getSigla()))
                && !VerificadorUtil.verificarSeObjetoNuloOuVazio(buscaLaudoDTO.getCampoBusca()))) {
            sql = sql + " and p.nome ilike ?";
        }

        else if ((buscaLaudoDTO.getTipoBusca().equals(TipoBuscaLaudo.CODIGO_PROCEDIMENTO.getSigla())
                && !VerificadorUtil.verificarSeObjetoNuloOuVazio(buscaLaudoDTO.getCampoBusca()))) {
            sql = sql + " and pr.codproc = ?";
        }

        else if ((buscaLaudoDTO.getTipoBusca().equals(TipoBuscaLaudo.MATRICULA.getSigla())
                && !VerificadorUtil.verificarSeObjetoNuloOuVazio(buscaLaudoDTO.getCampoBusca()))) {
            sql = sql + " and upper(p.matricula) = ?";
        }

        else if ((buscaLaudoDTO.getTipoBusca().equals(TipoBuscaLaudo.PRONTUARIO_PACIENTE.getSigla())
                && !VerificadorUtil.verificarSeObjetoNuloOuVazio(buscaLaudoDTO.getCampoBusca()))) {
            sql = sql + " and p.id_paciente = ?";
        }

        else if(buscaLaudoDTO.getTipoBusca().equals(TipoBuscaLaudo.PRONTUARIO_PACIENTE_ANTIGO.getSigla())
                && !VerificadorUtil.verificarSeObjetoNuloOuVazio(buscaLaudoDTO.getCampoBusca())) {
            sql = sql + " and p.codprontuario_anterior = ?";
        }

        sql = sql + " order by ano_final desc, mes_final desc, nome ";

        ArrayList<LaudoBean> lista = new ArrayList<>();

        try {
            conexao = ConnectionFactory.getConnection();
            int i = 1;
            PreparedStatement stm = conexao.prepareStatement(sql);
            if (user_session.getUnidade().getRestringirLaudoPorUnidade()){
                stm.setInt(i, user_session.getUnidade().getId());
                i++;
            }

            if (!buscaLaudoDTO.getSituacao().equals(SituacaoLaudo.TODOS.getSigla())) {
                stm.setString(i, buscaLaudoDTO.getSituacao());
                i++;
                if(!VerificadorUtil.verificarSeObjetoNulo(buscaLaudoDTO.getDataInicial())
                        && !VerificadorUtil.verificarSeObjetoNulo(buscaLaudoDTO.getDataFinal())) {
                    stm.setDate(i, new java.sql.Date(buscaLaudoDTO.getDataInicial().getTime()));
                    i++;
                    stm.setDate(i, new java.sql.Date(buscaLaudoDTO.getDataFinal().getTime()));
                    i++;
                }
            }

            if (((buscaLaudoDTO.getTipoBusca().equals(TipoBuscaLaudo.NOME_PACIENTE.getSigla()))
                    && !VerificadorUtil.verificarSeObjetoNuloOuVazio(buscaLaudoDTO.getCampoBusca()))) {
                stm.setString(i, "%" + buscaLaudoDTO.getCampoBusca().toUpperCase() + "%");
                i++;
            }


            else if (((buscaLaudoDTO.getTipoBusca().equals(TipoBuscaLaudo.CODIGO_PROCEDIMENTO.getSigla()))
                    || (buscaLaudoDTO.getTipoBusca().equals(TipoBuscaLaudo.MATRICULA.getSigla())))
                    && !VerificadorUtil.verificarSeObjetoNuloOuVazio(buscaLaudoDTO.getCampoBusca())) {
                stm.setString(i, buscaLaudoDTO.getCampoBusca().toUpperCase());
                i++;
            }

            else if ((buscaLaudoDTO.getTipoBusca().equals(TipoBuscaLaudo.PRONTUARIO_PACIENTE.getSigla())
                    || (buscaLaudoDTO.getTipoBusca().equals(TipoBuscaLaudo.PRONTUARIO_PACIENTE_ANTIGO.getSigla()))
                    && !VerificadorUtil.verificarSeObjetoNuloOuVazio(buscaLaudoDTO.getCampoBusca()))) {
                stm.setInt(i, Integer.valueOf(buscaLaudoDTO.getCampoBusca()));
                i++;
            }

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                LaudoBean laudo = new LaudoBean();

                laudo.setId(rs.getInt("id_laudo"));
                laudo.getPaciente().setId_paciente(rs.getInt("id_paciente"));
                laudo.getPaciente().setMatricula(rs.getString("matricula"));
                laudo.getPaciente().setNome(rs.getString("nome"));
                laudo.setMesFinal(rs.getInt("mes_final"));
                laudo.setAnoFinal(rs.getInt("ano_final"));
                laudo.getProcedimentoPrimario().setCodProc(rs.getString("codproc"));
                laudo.getProcedimentoPrimario().setNomeProc(rs.getString("procedimento"));
                laudo.getCid1().setDescCidAbrev(rs.getString("desccidabrev"));
                laudo.setSituacao(rs.getString("situacao"));
                laudo.setMesFinal(rs.getInt("mes_final"));
                laudo.setAnoFinal(rs.getInt("ano_final"));
                FuncionarioBean func = new FuncionarioBean();
                func.setId(rs.getLong("id_funcionario"));
                func.setNome(rs.getString("descfuncionario"));
                laudo.setProfissionalLaudo(func);
                laudo.setVencimento(DataUtil.mostraMesPorExtenso(laudo.getMesFinal()) + "/" + laudo.getAnoFinal().toString());

                lista.add(laudo);
            }
        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return lista;
    }

    public LaudoBean buscarLaudosPorId(Integer id) throws ProjetoException {

        LaudoBean laudo = new LaudoBean();

        String sql = "select id_laudo,l.codpaciente, p.nome, l.data_solicitacao, l.mes_inicio, l.ano_inicio, l.mes_final, l.ano_final, "
                + " l.periodo, l.codprocedimento_primario, pr.codproc, pr.nome as procedimento, l.codprocedimento_secundario1, ps1.nome as nome1, "
                + " l.codprocedimento_secundario2, ps2.nome as nome2, l.codprocedimento_secundario3, ps3.nome as nome3, "
                + " l.codprocedimento_secundario4, ps4.nome as nome4, "
                + " l.codprocedimento_secundario5, ps5.nome as nome5, l.cid1, c1.desccid as desccid1,c1.desccidabrev as desccidabrev1,  l.cid2, c2.desccid as desccid2, c2.desccidabrev as desccidabrev2, "
                + " l.cid3, c3.desccid as desccid3, c3.desccidabrev as desccidabrev3, l.obs, data_autorizacao, situacao , func.id_funcionario, func.descfuncionario,  "
                + " data_solicitacao as datainicio, " +
                "	(SELECT * FROM hosp.fn_GetLastDayOfMonth(to_date(ano_final||'-'||'0'||''||mes_final||'-'||'01', 'YYYY-MM-DD'))) as datafinal, coalesce(profissional_externo,false) profissional_externo, nome_profissional_externo, conselho_numero "
                + " from hosp.laudo l left join hosp.pacientes p on (p.id_paciente = l.codpaciente) "
                + " left join hosp.proc pr on (pr.id = l.codprocedimento_primario) "
                + " left join hosp.proc ps1 on (ps1.id = l.codprocedimento_secundario1) "
                + " left join hosp.proc ps2 on (ps2.id = l.codprocedimento_secundario2) "
                + " left join hosp.proc ps3 on (ps3.id = l.codprocedimento_secundario3) "
                + " left join hosp.proc ps4 on (ps4.id = l.codprocedimento_secundario4) "
                + " left join hosp.proc ps5 on (ps5.id = l.codprocedimento_secundario5) "
                + " left join hosp.cid c1 on (c1.cod = l.cid1) " + " left join hosp.cid c2 on (c2.cod = l.cid2) "
                + " left join hosp.cid c3 on (c3.cod = l.cid3) left join acl.funcionarios func on func.id_funcionario = l.cod_profissional " + "  where id_laudo = ?";

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stm = conexao.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {

                laudo.setId(rs.getInt("id_laudo"));
                laudo.getPaciente().setId_paciente(rs.getInt("codpaciente"));
                laudo.getPaciente().setNome(rs.getString("nome"));
                laudo.setDataSolicitacao(rs.getDate("data_solicitacao"));
                laudo.setDataAutorizacao(rs.getDate("data_autorizacao"));
                laudo.setMesInicio(rs.getInt("mes_inicio"));
                laudo.setAnoInicio(rs.getInt("ano_inicio"));
                laudo.setMesFinal(rs.getInt("mes_final"));
                laudo.setAnoFinal(rs.getInt("ano_final"));
                laudo.setPeriodo(rs.getInt("periodo"));
                laudo.setVigenciaInicial(rs.getDate("datainicio"));
                laudo.setVigenciaFinal(rs.getDate("datafinal"));
                laudo.getProcedimentoPrimario().setIdProc(rs.getInt("codprocedimento_primario"));
                laudo.getProcedimentoPrimario().setCodProc(rs.getString("codproc"));
                laudo.getProcedimentoPrimario().setNomeProc(rs.getString("procedimento"));
                laudo.getProcedimentoSecundario1().setIdProc(rs.getInt("codprocedimento_secundario1"));
                laudo.getProcedimentoSecundario1().setNomeProc(rs.getString("nome1"));
                laudo.getProcedimentoSecundario2().setIdProc(rs.getInt("codprocedimento_secundario2"));
                laudo.getProcedimentoSecundario2().setNomeProc(rs.getString("nome2"));
                laudo.getProcedimentoSecundario3().setIdProc(rs.getInt("codprocedimento_secundario3"));
                laudo.getProcedimentoSecundario3().setNomeProc(rs.getString("nome3"));
                laudo.getProcedimentoSecundario4().setIdProc(rs.getInt("codprocedimento_secundario4"));
                laudo.getProcedimentoSecundario4().setNomeProc(rs.getString("nome4"));
                laudo.getProcedimentoSecundario5().setIdProc(rs.getInt("codprocedimento_secundario5"));
                laudo.getProcedimentoSecundario5().setNomeProc(rs.getString("nome5"));
                if (rs.getString("cid1") != null) {
                    laudo.getCid1().setIdCid(rs.getInt("cid1"));
                    laudo.getCid1().setDescCid(rs.getString("desccid1"));
                    laudo.getCid1().setDescCidAbrev(rs.getString("desccidabrev1"));
                }
                if (rs.getString("cid2") != null) {
                    laudo.getCid2().setIdCid(rs.getInt("cid2"));
                    laudo.getCid2().setDescCid(rs.getString("desccid2"));
                    laudo.getCid2().setDescCidAbrev(rs.getString("desccidabrev2"));
                }

                if (rs.getString("cid3") != null) {
                    laudo.getCid3().setIdCid(rs.getInt("cid3"));
                    laudo.getCid3().setDescCid(rs.getString("desccid3"));
                    laudo.getCid3().setDescCidAbrev(rs.getString("desccidabrev3"));
                }
                laudo.setObs(rs.getString("obs"));
                laudo.setSituacao(rs.getString("situacao"));
                laudo.setProfissionalExternoLaudo(rs.getBoolean("profissional_externo"));
                laudo.setNomeProfissionalExternoLaudo(rs.getString("nome_profissional_externo"));
                laudo.setConselhoProfissionalExternoLaudo(rs.getString("conselho_numero"));
                FuncionarioBean func = new FuncionarioBean();
                func.setId(rs.getLong("id_funcionario"));
                func.setNome(rs.getString("descfuncionario"));
                laudo.setProfissionalLaudo(func);
                //l.getProfissionalLaudo().setCodigo(rs.getInt("id_funcionario"));
                //l.getProfissionalLaudo().setNome(rs.getString("descfuncionario"));

            }
        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return laudo;
    }

    public LaudoBean carregaLaudoParaRenovacao(Integer id) throws ProjetoException {

        LaudoBean laudo = new LaudoBean();

        String sql = "select "
        		+ "l.codpaciente, p.nome, p.cns, p.cpf, p.cns, l.data_solicitacao, l.mes_inicio, "
        		+ "l.ano_inicio, l.mes_final, l.ano_final,l.periodo, l.codprocedimento_primario, "
        		+ "pr.codproc, pr.nome as procedimento, "
        		+ "l.codprocedimento_secundario1, ps1.nome as nome1, "
        		+ "l.codprocedimento_secundario2, ps2.nome as nome2, "
        		+ "l.codprocedimento_secundario3, ps3.nome as nome3, "
        		+ "l.codprocedimento_secundario4, ps4.nome as nome4, "
        		+ "l.codprocedimento_secundario5, ps5.nome as nome5, "
        		+ "l.cid1 as codcid1, c1.desccid as desccid1, c1.desccidabrev desccidabrev1, c1.cid as cid1, "
        		+ "l.cid2 as codcid2, c2.desccid as desccid2, c2.desccidabrev desccidabrev2, c2.cid as cid2, "
        		+ "l.cid3 as codcid3, c3.desccid as desccid3, c3.desccidabrev desccidabrev3, c2.cid as cid3, "
        		+ "l.obs, func.id_funcionario, func.descfuncionario "
        		+ "from hosp.laudo l "
        		+ "left join hosp.pacientes p on (p.id_paciente = l.codpaciente) "
        		+ "left join hosp.proc pr on (pr.id = l.codprocedimento_primario) "
        		+ "left join hosp.proc ps1 on (ps1.id = l.codprocedimento_secundario1) "
        		+ "left join hosp.proc ps2 on (ps2.id = l.codprocedimento_secundario2) "
        		+ "left join hosp.proc ps3 on (ps3.id = l.codprocedimento_secundario3) "
        		+ "left join hosp.proc ps4 on (ps4.id = l.codprocedimento_secundario4) "
        		+ "left join hosp.proc ps5 on (ps5.id = l.codprocedimento_secundario5) "
        		+ "left join hosp.cid c1 on (c1.cod = l.cid1) "
        		+ "left join hosp.cid c2 on (c2.cod = l.cid2) "
        		+ "left join hosp.cid c3 on (c3.cod = l.cid3) "
        		+ "left join acl.funcionarios func on func.id_funcionario = l.cod_profissional "
        		+ "where id_laudo = ?";

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stm = conexao.prepareStatement(sql);
            stm.setInt(1, id);
            String SQL_F = stm.toString();
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {

                laudo.getPaciente().setId_paciente(rs.getInt("codpaciente"));
                laudo.getPaciente().setNome(rs.getString("nome"));
                laudo.getPaciente().setCpf(rs.getString("cpf"));
                laudo.getPaciente().setCns(rs.getString("cns"));
                
                laudo.setMesInicio(rs.getInt("mes_final"));
                laudo.setAnoInicio(rs.getInt("ano_final"));
                laudo.setPeriodo(rs.getInt("periodo"));
                
                laudo.getProcedimentoPrimario().setIdProc(rs.getInt("codprocedimento_primario"));
                laudo.getProcedimentoPrimario().setCodProc(rs.getString("codproc"));
                laudo.getProcedimentoPrimario().setNomeProc(rs.getString("procedimento"));
                
                laudo.getProcedimentoSecundario1().setIdProc(rs.getInt("codprocedimento_secundario1"));
                laudo.getProcedimentoSecundario1().setNomeProc(rs.getString("nome1"));
                
                laudo.getProcedimentoSecundario2().setIdProc(rs.getInt("codprocedimento_secundario2"));
                laudo.getProcedimentoSecundario2().setNomeProc(rs.getString("nome2"));
                
                laudo.getProcedimentoSecundario3().setIdProc(rs.getInt("codprocedimento_secundario3"));
                laudo.getProcedimentoSecundario3().setNomeProc(rs.getString("nome3"));
                
                laudo.getProcedimentoSecundario4().setIdProc(rs.getInt("codprocedimento_secundario4"));
                laudo.getProcedimentoSecundario4().setNomeProc(rs.getString("nome4"));
                
                laudo.getProcedimentoSecundario5().setIdProc(rs.getInt("codprocedimento_secundario5"));
                laudo.getProcedimentoSecundario5().setNomeProc(rs.getString("nome5"));
                
                laudo.getCid1().setCid(rs.getString("cid1"));
                laudo.getCid1().setIdCid(rs.getInt("codcid1"));
                laudo.getCid1().setDescCid(rs.getString("desccid1"));
                laudo.getCid1().setDescCidAbrev(rs.getString("desccidabrev1"));
                
                laudo.getCid2().setCid(rs.getString("cid2"));
                laudo.getCid2().setIdCid(rs.getInt("codcid2"));
                laudo.getCid2().setDescCid(rs.getString("desccid2"));
                laudo.getCid2().setDescCidAbrev(rs.getString("desccidabrev2"));
                
                laudo.getCid3().setCid(rs.getString("cid3"));
                laudo.getCid3().setIdCid(rs.getInt("codcid3"));
                laudo.getCid3().setDescCid(rs.getString("desccid3"));
                laudo.getCid3().setDescCidAbrev(rs.getString("desccidabrev3"));
                
                laudo.setObs(rs.getString("obs"));
                laudo.setSituacao("P");
                FuncionarioBean func = new FuncionarioBean();
                func.setId(rs.getLong("id_funcionario"));
                func.setNome(rs.getString("descfuncionario"));
                laudo.setDataSolicitacao(prepararDataVencimentoApenasComDiasUteis(rs.getInt("mes_final"), rs.getInt("ano_final"), func.getId()));
                laudo.setProfissionalLaudo(func);

            }
        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return laudo;
    }

    private Date prepararDataVencimentoApenasComDiasUteis(int mesFinal, int anoFInal, Long codigoFuncionario) throws ProjetoException, SQLException {
        Date d = DataUtil.montarDataCompleta(1, mesFinal, anoFInal);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
        Date dataMontada = calendar.getTime();

        Boolean revalidar = true;
        Date dataSemFeriado=null;
        Date dataSemFinalDeSemana = dataMontada;

        while (revalidar) {
            revalidar = false;

            while (!VerificadorUtil.verificarSeObjetoNulo(DataUtil.proximaDataRetirandoFinalDeSemana(dataSemFinalDeSemana))) {
                dataSemFinalDeSemana = DataUtil.adicionarDiasAData(dataSemFinalDeSemana, 1);
                revalidar = true;
            }

            revalidar = false;
            BloqueioDAO bDao = new BloqueioDAO();
            Date dataSemBloqueio = dataSemFinalDeSemana;
            while (!VerificadorUtil.verificarSeObjetoNulo(bDao.verificarBloqueioProfissionalDeData(dataSemBloqueio, codigoFuncionario, conexao))) {
                dataSemBloqueio = DataUtil.adicionarDiasAData(dataSemBloqueio, 1);
                revalidar = true;
            }

            revalidar = false;
            FeriadoDAO fDao = new FeriadoDAO();
            dataSemFeriado = dataSemBloqueio;
            while (!VerificadorUtil.verificarSeObjetoNulo(fDao.verificarFeriadoDeData(dataSemFeriado, conexao))) {
                dataSemFeriado = DataUtil.adicionarDiasAData(dataSemFeriado, 1);
                revalidar = true;
            }

            dataSemFinalDeSemana = dataSemFeriado;

        }
        return dataSemFeriado;
    }


    public ArrayList<InsercaoPacienteBean> listarLaudosVigentesParaPaciente(Integer codPaciente)
            throws ProjetoException {

        ArrayList<InsercaoPacienteBean> lista = new ArrayList<>();

        String sql = "select codpaciente,nome, cns, id_laudo,mes_inicio, ano_inicio, mes_final, ano_final, datainicio, "
                + "(SELECT * FROM hosp.fn_GetLastDayOfMonth(to_date(ano_final||'-'||'0'||''||mes_final||'-'||'01', 'YYYY-MM-DD'))) as datafinal, "
                + " id_cidprimario, desccid  from ( "
                + " select l.id_laudo, l.codpaciente, p.nome, p.cns, l.data_solicitacao, l.mes_inicio, l.ano_inicio, l.mes_final, l.ano_final, l.periodo, "
                + " l.codprocedimento_primario, pr.nome as procedimento, l.cid1, ci.desccid,  "
                + " l.data_solicitacao as datainicio,  "
                + " (SELECT * FROM hosp.fn_GetLastDayOfMonth(to_date(ano_final||'-'||'0'||''||mes_final||'-'||'01', 'YYYY-MM-DD'))) as datafinal, ci.cod id_cidprimario   "
                + " from hosp.laudo l " + " left join hosp.pacientes p on (l.codpaciente = p.id_paciente) "
                + " left join hosp.proc pr on (l.codprocedimento_primario = pr.id) "
                + " left join hosp.cid ci on (l.cid1 = cast(ci.cod as integer)) " + " where 1=1  and l.ativo is true "
                // current_date <= (SELECT * FROM
                // hosp.fn_GetLastDayOfMonth(to_date(ano_final||'-'||'0'||''||mes_final||'-'||'01',
                // 'YYYY-MM-DD'))) "

                + " AND l.codpaciente = ?  "//AND NOT EXISTS (SELECT pac.codlaudo FROM hosp.paciente_instituicao pac WHERE pac.codlaudo = l.id_laudo)"
                + " ) a order by datainicio desc";
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stm = conexao.prepareStatement(sql);
            stm.setInt(1, codPaciente);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                InsercaoPacienteBean insercao = new InsercaoPacienteBean();
                insercao.getLaudo().setId(rs.getInt("id_laudo"));
                insercao.getLaudo().getPaciente().setId_paciente(rs.getInt("codpaciente"));
                insercao.getLaudo().getPaciente().setNome(rs.getString("nome"));
                insercao.getLaudo().getPaciente().setCns(rs.getString("cns"));
                insercao.getLaudo().setVigenciaInicial(rs.getDate("datainicio"));
                insercao.getLaudo().setVigenciaFinal(rs.getDate("datafinal"));
                insercao.getLaudo().setMesFinal(rs.getInt("mes_final"));
                insercao.getLaudo().setAnoFinal(rs.getInt("ano_final"));
                insercao.getLaudo().setMesInicio(rs.getInt("mes_inicio"));
                insercao.getLaudo().setAnoInicio(rs.getInt("ano_inicio"));
                insercao.getLaudo().getCid1().setIdCid(rs.getInt("id_cidprimario"));
                insercao.getLaudo().getCid1().setDescCid(rs.getString("desccid"));

                lista.add(insercao);
            }
        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return lista;
    }

    public LaudoBean recuperarPeriodoInicioLaudo(Integer codLaudo) throws ProjetoException {

        LaudoBean laudo = new LaudoBean();

        String sql = "SELECT mes_inicio, ano_inicio FROM hosp.laudo WHERE id_laudo = ?;";

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stm = conexao.prepareStatement(sql);
            stm.setInt(1, codLaudo);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                laudo.setMesInicio(rs.getInt("mes_inicio"));
                laudo.setAnoInicio(rs.getInt("ano_inicio"));
            }

        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return laudo;
    }

    public LaudoBean recuperarUltimoLaudoPaciente(Integer codPaciente, Integer codPrograma, Integer codGrupo) throws ProjetoException {

        LaudoBean laudo = new LaudoBean();

        String sql = "select id_laudo, mes_final, ano_final from hosp.laudo where laudo.id_laudo=(\n" +
                " select max(l1.id_laudo) from hosp.paciente_instituicao pi1\n" +
                " join hosp.laudo l1 on l1.id_laudo = pi1.codlaudo where l1.codpaciente=? and pi1.codprograma=? and pi1.codgrupo=? " +
                " and to_char(l1.ano_final, '9999')||lpad(trim(to_char(l1.mes_final,'99')),2,'0')=\n" +
                " (select max(to_char(l2.ano_final, '9999')||lpad(trim(to_char(l2.mes_final,'99')),2,'0')) from hosp.paciente_instituicao pi2\n" +
                " join hosp.laudo l2 on l2.id_laudo = pi2.codlaudo\n" +
                " where l2.codpaciente=? and pi2.codprograma=? and pi2.codgrupo=? " +
                " )\n" +
                " )";

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stm = conexao.prepareStatement(sql);
            stm.setInt(1, codPaciente);
            stm.setInt(2, codPrograma);
            stm.setInt(3, codGrupo);
            stm.setInt(4, codPaciente);
            stm.setInt(5, codPrograma);
            stm.setInt(6, codGrupo);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
            	laudo.setId(rs.getInt("id_laudo"));
                laudo.setMesFinal(rs.getInt("mes_final"));
                laudo.setAnoFinal(rs.getInt("ano_final"));
            }

        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return laudo;
    }

    public LaudoBean listarLaudosVigentesPorId(Integer id) throws ProjetoException {

        LaudoBean laudoBean = new LaudoBean();

        String sql = "select a.nome, a.cns, id_laudo,mes_inicio, ano_inicio, mes_final, ano_final, " +
                "pr.id as id_procedimento, pr.nome as procedimento, ci.cod as id_cid, ci.desccid, " +
                "to_date(ano_inicio||'-'||'0'||''||mes_inicio||'-'||'01', 'YYYY-MM-DD') as datainicio, " +
                "(SELECT * FROM hosp.fn_GetLastDayOfMonth(to_date(ano_final||'-'||'0'||''||mes_final||'-'||'01', 'YYYY-MM-DD'))) as datafinal " +
                "from ( " +
                " select l.id_laudo, l.codpaciente, p.nome, p.cns, l.data_solicitacao, l.mes_inicio, l.ano_inicio, l.mes_final, l.ano_final, l.periodo, " +
                " l.codprocedimento_primario, pr.nome as procedimento, l.cid1, ci.desccid, " +
                " to_date(ano_inicio||'-'||'0'||''||mes_inicio||'-'||'01', 'YYYY-MM-DD') as datainicio, " +
                " (SELECT * FROM hosp.fn_GetLastDayOfMonth(to_date(ano_final||'-'||'0'||''||mes_final||'-'||'01', 'YYYY-MM-DD'))) as datafinal " +
                " from hosp.laudo l  left join hosp.pacientes p on (l.codpaciente = p.id_paciente) " +
                " left join hosp.proc pr on (l.codprocedimento_primario = pr.id) " +
                " left join hosp.cid ci on (l.cid1 = ci.cod)  where 1=1 " +
                // " current_date <= (SELECT * FROM
                // hosp.fn_GetLastDayOfMonth(to_date(ano_final||'-'||'0'||''||mes_final||'-'||'01',
                // 'YYYY-MM-DD'))) "

                " AND l.id_laudo = ?  AND NOT EXISTS (SELECT pac.codlaudo FROM hosp.paciente_instituicao pac WHERE pac.codlaudo = l.id_laudo) " +
                " ) a left join hosp.pacientes pa on (pa.id_paciente = a.codpaciente) " +
                "	 left join hosp.proc pr on (a.codprocedimento_primario = pr.id) " +
                " 	 left join hosp.cid ci on (a.cid1 = ci.cod) ";
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stm = conexao.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                laudoBean.setId(rs.getInt("id_laudo"));
                laudoBean.getPaciente().setNome(rs.getString("nome"));
                laudoBean.getPaciente().setCns(rs.getString("cns"));
                laudoBean.setVigenciaInicial(rs.getDate("datainicio"));
                laudoBean.setVigenciaFinal(rs.getDate("datafinal"));
                laudoBean.setMesFinal(rs.getInt("mes_final"));
                laudoBean.setAnoFinal(rs.getInt("ano_final"));
                laudoBean.setMesInicio(rs.getInt("mes_inicio"));
                laudoBean.setAnoInicio(rs.getInt("ano_inicio"));
                laudoBean.getProcedimentoPrimario().setIdProc(rs.getInt("id_procedimento"));
                laudoBean.getProcedimentoPrimario().setNomeProc(rs.getString("procedimento"));
                laudoBean.getCid1().setIdCid(rs.getInt("id_cid"));
                laudoBean.getCid1().setDescCid(rs.getString("desccid"));
            }
        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return laudoBean;
    }

    public Boolean verificarSeLaudoAssociadoPacienteTerapia(Integer codigoLaudo) throws ProjetoException {

        Boolean retorno = true;

        String sql = "SELECT * FROM hosp.paciente_instituicao WHERE codlaudo = ?";

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stm = conexao.prepareStatement(sql);
            stm.setInt(1, codigoLaudo);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                retorno = false;
            }

        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return retorno;
    }

    public BuscaIdadePacienteDTO buscarIdadePacienteEmAnoIhMes(Date dataNascimento) throws ProjetoException {

        PreparedStatement ps = null;
        BuscaIdadePacienteDTO idadePaciente = new BuscaIdadePacienteDTO();

        try {
            conexao = ConnectionFactory.getConnection();

            String sql = "select extract( year from age(current_date, CAST(? AS date) )) idade_anos, " +
                    "	   extract( month from age(current_date, CAST(? AS date) )) idade_meses";

            ps = conexao.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(dataNascimento.getTime()));
            ps.setDate(2, new java.sql.Date(dataNascimento.getTime()));
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                idadePaciente.setIdadeAnos(rs.getInt("idade_anos"));
                idadePaciente.setIdadeMeses(rs.getInt("idade_meses"));
            }

        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return idadePaciente;
    }


    public ProcedimentoType buscarIdadeMinimaIhMaximaDeProcedimento(String codigoProcedimento, Date dataSolicitacao) throws ProjetoException {

        PreparedStatement ps = null;
        ProcedimentoType procedimento = new ProcedimentoType();

        try {
            conexao = ConnectionFactory.getConnection();

            String sql = "select pm.idade_minima, pm.unidade_idade_minima, unidade_idade_minima, pm.idade_maxima, pm.unidade_idade_maxima, unidade_idade_maxima " +
                    " from sigtap.procedimento_mensal pm " +
                    " join sigtap.historico_consumo_sigtap hcs on hcs.id = pm.id_historico_consumo_sigtap " +
                    " where hcs.mes = extract (month from CAST(? AS date)) and hcs.ano = extract (year from CAST(? AS date)) " +
                    " and pm.codigo_procedimento = ? and hcs.status = 'A' and (hcs.ano = ? and hcs.mes = ?)";

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dataSolicitacao);
            ps = conexao.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(dataSolicitacao.getTime()));
            ps.setDate(2, new java.sql.Date(dataSolicitacao.getTime()));
            ps.setString(3, codigoProcedimento);
            ps.setInt(4, calendar.get(Calendar.YEAR));
            ps.setInt(5, calendar.get(Calendar.MONTH)+1);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                IdadeLimiteType idadeMinima = new IdadeLimiteType();
                IdadeLimiteType idadeMaxima = new IdadeLimiteType();
                String unidadeIdadeMinima = rs.getString("unidade_idade_minima");
                String unidadeIdadeMaxima = rs.getString("unidade_idade_maxima");

                if(VerificadorUtil.verificarSeObjetoNuloOuVazio(unidadeIdadeMinima)) {
                    if (rs.getInt("idade_minima")==9999)
                        idadeMinima.setQuantidadeLimite(0);
                                else
                    idadeMinima.setQuantidadeLimite(rs.getInt("idade_minima")/12);
                    idadeMinima.setUnidadeLimite(UnidadeLimiteType.ANOS);
                }

                else if(unidadeIdadeMinima.equals(UnidadeLimiteType.MESES.name())) {
                    if (rs.getInt("idade_minima")==9999)
                        idadeMinima.setQuantidadeLimite(0);
                    else
                    idadeMinima.setQuantidadeLimite(rs.getInt("idade_minima"));
                    idadeMinima.setUnidadeLimite(UnidadeLimiteType.MESES);
                }
                else {
                    idadeMinima.setQuantidadeLimite(rs.getInt("idade_minima"));
                    idadeMinima.setUnidadeLimite(UnidadeLimiteType.ANOS);
                }

                procedimento.setIdadeMinimaPermitida(idadeMinima);


                if(VerificadorUtil.verificarSeObjetoNuloOuVazio(unidadeIdadeMaxima)) {
                    idadeMaxima.setQuantidadeLimite(rs.getInt("idade_maxima")/12);
                    idadeMaxima.setUnidadeLimite(UnidadeLimiteType.ANOS);
                }

                else if(unidadeIdadeMaxima.equals(UnidadeLimiteType.MESES.name())) {
                    idadeMaxima.setQuantidadeLimite(rs.getInt("idade_maxima"));
                    idadeMaxima.setUnidadeLimite(UnidadeLimiteType.MESES);
                }
                else {
                    idadeMaxima.setQuantidadeLimite(rs.getInt("idade_maxima"));
                    idadeMaxima.setUnidadeLimite(UnidadeLimiteType.ANOS);
                }

                procedimento.setIdadeMaximaPermitida(idadeMaxima);

            }

        }  catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return procedimento;
    }

    public boolean validaCodigoCidEmLaudo(String codigoCid, Date dataSolicitacao, String codigoProcedimento) throws ProjetoException {

        PreparedStatement ps = null;
        boolean cidValido = false;

        try {
            conexao = ConnectionFactory.getConnection();

            String sql = "select exists (select cm.cid \n" +
                    "from sigtap.procedimento_mensal pm \n" +
                    "join sigtap.historico_consumo_sigtap hcs on hcs.id = pm.id_historico_consumo_sigtap \n" +
                    "join sigtap.cid_procedimento_mensal cpm on cpm.id_procedimento_mensal = pm.id \n" +
                    "join hosp.cid  cm on cm.cod = cpm.id_cid  \n" +
                    "where hcs.mes = extract (month from CAST(? AS date)) and hcs.ano = extract (year from CAST(? AS date)) \n" +
                    "and pm.codigo_procedimento = ? and hcs.status = 'A' and cm.cid = ?) as cid_valido";

            ps = conexao.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(dataSolicitacao.getTime()));
            ps.setDate(2, new java.sql.Date(dataSolicitacao.getTime()));
            ps.setString(3, codigoProcedimento);
            ps.setString(4, codigoCid);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                cidValido = rs.getBoolean("cid_valido");
            }

        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return cidValido;
    }

    public String buscaCodigoCboProfissionalSelecionado(Long idFuncionario) throws ProjetoException {

        PreparedStatement ps = null;
        String codigoCbo = "";

        try {
            conexao = ConnectionFactory.getConnection();

            String sql = "select c.codigo from hosp.cbo c " +
                    "join acl.funcionarios f on c.id = f.codcbo where f.id_funcionario = ?";

            ps = conexao.prepareStatement(sql);
            ps.setLong(1, idFuncionario);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                codigoCbo = rs.getString("codigo");
            }

        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return codigoCbo;
    }

    public boolean validaCodigoCboEmLaudo(String codigoCbo, Date dataSolicitacao, String codigoProcedimento) throws ProjetoException {

        PreparedStatement ps = null;
        boolean cboValido = false;

        try {
            conexao = ConnectionFactory.getConnection();

            String sql = "select exists (select cm.codigo \n" +
                    "from sigtap.procedimento_mensal pm \n" +
                    "join sigtap.historico_consumo_sigtap hcs on hcs.id = pm.id_historico_consumo_sigtap \n" +
                    "join sigtap.cbo_procedimento_mensal cpm on cpm.id_procedimento_mensal = pm.id \n" +
                    "join hosp.cbo cm on cm.id = cpm.id_cbo \n" +
                    "where hcs.mes = extract (month from CAST(? AS date)) and hcs.ano = extract (year from CAST(? AS date)) \n" +
                    "and pm.codigo_procedimento =? and hcs.status = 'A' and cm.codigo = ?) as cbo_valido";

            ps = conexao.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(dataSolicitacao.getTime()));
            ps.setDate(2, new java.sql.Date(dataSolicitacao.getTime()));
            ps.setString(3, codigoProcedimento);
            ps.setString(4, codigoCbo);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                cboValido = rs.getBoolean("cbo_valido");
            }

        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return cboValido;
    }

    public boolean verificaSeProcedimentoPossuiCidsAssociados(Date dataSolicitacao, String codigoProcedimento) throws ProjetoException {

        PreparedStatement ps = null;
        boolean possuiCidsAssociados = true;

        try {
            conexao = ConnectionFactory.getConnection();

            String sql = "select count(*) cid_associados from sigtap.cid_procedimento_mensal cpm " +
                    "join sigtap.procedimento_mensal pm on pm.id = cpm.id_procedimento_mensal " +
                    "join sigtap.historico_consumo_sigtap hcs on hcs.id = pm.id_historico_consumo_sigtap " +
                    "where hcs.mes = extract (month from CAST(? AS date)) and hcs.ano = extract (year from CAST(? AS date)) " +
                    "and pm.codigo_procedimento = ? and hcs.status = 'A' ";

            ps = conexao.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(dataSolicitacao.getTime()));
            ps.setDate(2, new java.sql.Date(dataSolicitacao.getTime()));
            ps.setString(3, codigoProcedimento);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Integer quantidadeCidsAssociados = rs.getInt("cid_associados");
                possuiCidsAssociados = (quantidadeCidsAssociados > 0);
            }

        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return possuiCidsAssociados;
    }

    public boolean sexoDoPacienteValidoComProcedimentoSigtap(Date dataSolicitacao, String sexo, String codigoProcedimento) throws ProjetoException {

        PreparedStatement ps = null;
        boolean sexoValido = false;

        try {
            conexao = ConnectionFactory.getConnection();

            String sql = "select exists (select pm.nome from sigtap.procedimento_mensal pm " +
                    "join sigtap.historico_consumo_sigtap hcs on hcs.id = pm.id_historico_consumo_sigtap " +
                    "where hcs.mes = extract (month from CAST(? AS date)) and hcs.ano = extract (year from CAST(? AS date)) " +
                    "and pm.codigo_procedimento = ? and hcs.status = 'A' and " +
                    "(pm.sexo = ? or pm.sexo = 'I' or pm.sexo = 'N')) sexo_permitido";

            ps = conexao.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(dataSolicitacao.getTime()));
            ps.setDate(2, new java.sql.Date(dataSolicitacao.getTime()));
            ps.setString(3, codigoProcedimento);
            ps.setString(4, sexo);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                sexoValido = rs.getBoolean("sexo_permitido");
            }

        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return sexoValido;
    }

    public boolean cadastrarLaudosEmLote(LaudoBean laudo, List<PacienteLaudoEmLoteDTO> listaPacienteLaudoEmLoteDTO) throws ProjetoException {

        FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("obj_funcionario");

        boolean cadastrou = false;

        String sql = "insert into hosp.laudo "
                + "(codpaciente,  data_solicitacao, mes_inicio, ano_inicio, mes_final, ano_final, periodo, codprocedimento_primario, "
                + "codprocedimento_secundario1, codprocedimento_secundario2, codprocedimento_secundario3, codprocedimento_secundario4, codprocedimento_secundario5, "
                + "cid1, cid2, cid3, obs, ativo, cod_unidade, data_hora_operacao, situacao, cod_profissional, validado_pelo_sigtap_anterior, data_autorizacao ) "
                + "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, true, ?, CURRENT_TIMESTAMP, ?, ?, ?, ?) ";

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);

            for (PacienteLaudoEmLoteDTO pacienteLaudoEmLoteDTO : listaPacienteLaudoEmLoteDTO) {

                stmt.setLong(1, pacienteLaudoEmLoteDTO.getPaciente().getId_paciente());
                stmt.setDate(2, new java.sql.Date(laudo.getDataSolicitacao().getTime()));
                stmt.setInt(3, laudo.getMesInicio());
                stmt.setInt(4, laudo.getAnoInicio());
                stmt.setInt(5, laudo.getMesFinal());
                stmt.setInt(6, laudo.getAnoFinal());
                stmt.setInt(7, laudo.getPeriodo());
                stmt.setInt(8, laudo.getProcedimentoPrimario().getIdProc());

                if (!VerificadorUtil.verificarSeObjetoNulo(laudo.getProcedimentoSecundario1()) &&
                        !VerificadorUtil.verificarSeObjetoNuloOuZero(laudo.getProcedimentoSecundario1().getIdProc())) {
                    stmt.setInt(9, laudo.getProcedimentoSecundario1().getIdProc());
                } else {
                    stmt.setNull(9, Types.NULL);
                }

                if (!VerificadorUtil.verificarSeObjetoNulo(laudo.getProcedimentoSecundario2()) &&
                        !VerificadorUtil.verificarSeObjetoNuloOuZero(laudo.getProcedimentoSecundario2().getIdProc())) {
                    stmt.setInt(10, laudo.getProcedimentoSecundario2().getIdProc());
                } else {
                    stmt.setNull(10, Types.NULL);
                }

                if (!VerificadorUtil.verificarSeObjetoNulo(laudo.getProcedimentoSecundario3()) &&
                        !VerificadorUtil.verificarSeObjetoNuloOuZero(laudo.getProcedimentoSecundario3().getIdProc())) {
                    stmt.setInt(11, laudo.getProcedimentoSecundario3().getIdProc());
                } else {
                    stmt.setNull(11, Types.NULL);
                }

                if (!VerificadorUtil.verificarSeObjetoNulo(laudo.getProcedimentoSecundario4()) &&
                        !VerificadorUtil.verificarSeObjetoNuloOuZero(laudo.getProcedimentoSecundario4().getIdProc())) {
                    stmt.setInt(12, laudo.getProcedimentoSecundario4().getIdProc());
                } else {
                    stmt.setNull(12, Types.NULL);
                }

                if (!VerificadorUtil.verificarSeObjetoNulo(laudo.getProcedimentoSecundario5()) &&
                        !VerificadorUtil.verificarSeObjetoNuloOuZero(laudo.getProcedimentoSecundario5().getIdProc())) {
                    stmt.setInt(13, laudo.getProcedimentoSecundario5().getIdProc());
                } else {
                    stmt.setNull(13, Types.NULL);
                }

                stmt.setInt(14, pacienteLaudoEmLoteDTO.getCid1().getIdCid());

                if (!VerificadorUtil.verificarSeObjetoNulo(pacienteLaudoEmLoteDTO.getCid2())
                        && !VerificadorUtil.verificarSeObjetoNuloOuZero(pacienteLaudoEmLoteDTO.getCid2().getIdCid())) {
                    stmt.setInt(15, pacienteLaudoEmLoteDTO.getCid2().getIdCid());
                } else {
                    stmt.setNull(15, Types.NULL);
                }

                if (!VerificadorUtil.verificarSeObjetoNulo(pacienteLaudoEmLoteDTO.getCid3())
                        && !VerificadorUtil.verificarSeObjetoNuloOuZero(pacienteLaudoEmLoteDTO.getCid3().getIdCid())) {
                    stmt.setInt(16, pacienteLaudoEmLoteDTO.getCid3().getIdCid());
                } else {
                    stmt.setNull(16, Types.NULL);
                }

                if (VerificadorUtil.verificarSeObjetoNuloOuVazio(pacienteLaudoEmLoteDTO.getObservacao())) {
                    stmt.setNull(17, Types.NULL);
                } else {
                    stmt.setString(17, pacienteLaudoEmLoteDTO.getObservacao().toUpperCase().trim());
                }

                stmt.setInt(18, user_session.getUnidade().getId());
                stmt.setString(19, laudo.getSituacao());
                stmt.setLong(20, laudo.getProfissionalLaudo().getId());
                stmt.setBoolean(21, laudo.isValidadoPeloSigtapAnterior());
                if (VerificadorUtil.verificarSeObjetoNulo(laudo.getDataAutorizacao())) {
                    stmt.setNull(22, Types.NULL);
                } else {
                    stmt.setDate(22, DataUtil.converterDateUtilParaDateSql(laudo.getDataAutorizacao()));
                }
                stmt.executeUpdate();
            }

            conexao.commit();
            cadastrou = true;
        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return cadastrou;
    }
}
