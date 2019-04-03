package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.InsercaoPacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.LaudoBean;

import javax.faces.context.FacesContext;

public class LaudoDAO {

    PreparedStatement ps = null;
    private Connection conexao = null;

    FuncionarioBean user_session = (FuncionarioBean) FacesContext
            .getCurrentInstance().getExternalContext().getSessionMap()
            .get("obj_usuario");

    public Boolean cadastrarLaudo(LaudoBean laudo) {

        FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("obj_funcionario");

        boolean retorno = false;

        String sql = "insert into hosp.laudo "
                + "(codpaciente, id_recurso, data_solicitacao, mes_inicio, ano_inicio, mes_final, ano_final, periodo, codprocedimento_primario, "
                + "codprocedimento_secundario1, codprocedimento_secundario2, codprocedimento_secundario3, codprocedimento_secundario4, codprocedimento_secundario5, "
                + "cid1, cid2, cid3, obs, ativo, cod_empresa ) "
                + "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, true, ?)";

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setLong(1, laudo.getPaciente().getId_paciente());
            stmt.setInt(2, laudo.getRecurso().getIdRecurso());
            stmt.setDate(3, new java.sql.Date(laudo.getData_solicitacao()
                    .getTime()));
            stmt.setInt(4, laudo.getMes_inicio());
            stmt.setInt(5, laudo.getAno_inicio());
            stmt.setInt(6, laudo.getMes_final());
            stmt.setInt(7, laudo.getAno_final());
            stmt.setInt(8, laudo.getPeriodo());
            stmt.setInt(9, laudo.getProcedimento_primario().getIdProc());
            if (laudo.getProcedimento_secundario1() != null) {
                if (laudo.getProcedimento_secundario1().getIdProc() != null) {
                    stmt.setInt(10, laudo.getProcedimento_secundario1()
                            .getIdProc());
                } else {
                    stmt.setNull(10, Types.NULL);
                }
            } else {
                stmt.setNull(10, Types.NULL);
            }

            if (laudo.getProcedimento_secundario2() != null) {
                if (laudo.getProcedimento_secundario2().getIdProc() != null) {
                    stmt.setInt(11, laudo.getProcedimento_secundario2()
                            .getIdProc());
                } else {
                    stmt.setNull(11, Types.NULL);
                }
            } else {
                stmt.setNull(11, Types.NULL);
            }

            if (laudo.getProcedimento_secundario3() != null) {
                if (laudo.getProcedimento_secundario3().getIdProc() != null) {
                    stmt.setInt(12, laudo.getProcedimento_secundario3()
                            .getIdProc());
                } else {
                    stmt.setNull(12, Types.NULL);
                }
            } else {
                stmt.setNull(12, Types.NULL);
            }

            if (laudo.getProcedimento_secundario4() != null) {
                if (laudo.getProcedimento_secundario4().getIdProc() != null) {
                    stmt.setInt(13, laudo.getProcedimento_secundario4()
                            .getIdProc());
                } else {
                    stmt.setNull(13, Types.NULL);
                }
            } else {
                stmt.setNull(13, Types.NULL);
            }

            if (laudo.getProcedimento_secundario5() != null) {
                if (laudo.getProcedimento_secundario5().getIdProc() != null) {
                    stmt.setInt(14, laudo.getProcedimento_secundario5()
                            .getIdProc());
                } else {
                    stmt.setNull(14, Types.NULL);
                }
            } else {
                stmt.setNull(14, Types.NULL);
            }

            if (laudo.getCid1() != null) {
                if (laudo.getCid1().getIdCid() != null) {
                    stmt.setInt(15, laudo.getCid1().getIdCid());
                } else {
                    stmt.setNull(15, Types.NULL);
                }
            } else {
                stmt.setNull(15, Types.NULL);
            }

            if (laudo.getCid2() != null) {
                if (laudo.getCid2().getIdCid() != null) {
                    stmt.setInt(16, laudo.getCid2().getIdCid());
                } else {
                    stmt.setNull(16, Types.NULL);
                }
            } else {
                stmt.setNull(16, Types.NULL);
            }

            if (laudo.getCid3() != null) {
                if (laudo.getCid3().getIdCid() != null) {
                    stmt.setInt(17, laudo.getCid3().getIdCid());
                } else {
                    stmt.setNull(17, Types.NULL);
                }
            } else {
                stmt.setNull(17, Types.NULL);
            }

            if (laudo.getObs() == null) {
                stmt.setNull(18, Types.NULL);
            } else {
                stmt.setString(18, laudo.getObs().toUpperCase().trim());
            }

            stmt.setInt(19, user_session.getEmpresa().getCodEmpresa());

            stmt.execute();
            conexao.commit();
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

    public Boolean alterarLaudo(LaudoBean laudo) {
        boolean retorno = false;
        String sql = "update hosp.laudo set codpaciente = ?, id_recurso = ?, data_solicitacao = ?, mes_inicio = ?, ano_inicio = ?, mes_final = ?, ano_final = ?, " +
                "periodo = ?, codprocedimento_primario = ?, codprocedimento_secundario1 = ?, codprocedimento_secundario2 = ?, codprocedimento_secundario3 = ?, " +
                "codprocedimento_secundario4 = ?, codprocedimento_secundario5 = ?, cid1 = ?, cid2 = ?, cid3 = ?, obs = ? where id_laudo = ?";

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setLong(1, laudo.getPaciente().getId_paciente());
            stmt.setInt(2, laudo.getRecurso().getIdRecurso());
            stmt.setDate(3, new java.sql.Date(laudo.getData_solicitacao()
                    .getTime()));
            stmt.setInt(4, laudo.getMes_inicio());
            stmt.setInt(5, laudo.getAno_inicio());
            stmt.setInt(6, laudo.getMes_final());
            stmt.setInt(7, laudo.getAno_final());
            stmt.setInt(8, laudo.getPeriodo());
            stmt.setInt(9, laudo.getProcedimento_primario().getIdProc());
            if (laudo.getProcedimento_secundario1() != null) {
                if (laudo.getProcedimento_secundario1().getIdProc() != null) {
                    stmt.setInt(10, laudo.getProcedimento_secundario1()
                            .getIdProc());
                } else {
                    stmt.setNull(10, Types.NULL);
                }
            } else {
                stmt.setNull(10, Types.NULL);
            }

            if (laudo.getProcedimento_secundario2() != null) {
                if (laudo.getProcedimento_secundario2().getIdProc() != null) {
                    stmt.setInt(11, laudo.getProcedimento_secundario2()
                            .getIdProc());
                } else {
                    stmt.setNull(11, Types.NULL);
                }
            } else {
                stmt.setNull(11, Types.NULL);
            }

            if (laudo.getProcedimento_secundario3() != null) {
                if (laudo.getProcedimento_secundario3().getIdProc() != null) {
                    stmt.setInt(12, laudo.getProcedimento_secundario3()
                            .getIdProc());
                } else {
                    stmt.setNull(12, Types.NULL);
                }
            } else {
                stmt.setNull(12, Types.NULL);
            }

            if (laudo.getProcedimento_secundario4() != null) {
                if (laudo.getProcedimento_secundario4().getIdProc() != null) {
                    stmt.setInt(13, laudo.getProcedimento_secundario4()
                            .getIdProc());
                } else {
                    stmt.setNull(13, Types.NULL);
                }
            } else {
                stmt.setNull(13, Types.NULL);
            }

            if (laudo.getProcedimento_secundario5() != null) {
                if (laudo.getProcedimento_secundario5().getIdProc() != null) {
                    stmt.setInt(14, laudo.getProcedimento_secundario5()
                            .getIdProc());
                } else {
                    stmt.setNull(14, Types.NULL);
                }
            } else {
                stmt.setNull(14, Types.NULL);
            }

            if (laudo.getCid1() != null) {
                if (laudo.getCid1().getIdCid() != null) {
                    stmt.setInt(15, laudo.getCid1().getIdCid());
                } else {
                    stmt.setNull(15, Types.NULL);
                }
            } else {
                stmt.setNull(15, Types.NULL);
            }

            if (laudo.getCid2() != null) {
                if (laudo.getCid2().getIdCid() != null) {
                    stmt.setInt(16, laudo.getCid2().getIdCid());
                } else {
                    stmt.setNull(16, Types.NULL);
                }
            } else {
                stmt.setNull(16, Types.NULL);
            }

            if (laudo.getCid3() != null) {
                if (laudo.getCid3().getIdCid() != null) {
                    stmt.setInt(17, laudo.getCid3().getIdCid());
                } else {
                    stmt.setNull(17, Types.NULL);
                }
            } else {
                stmt.setNull(17, Types.NULL);
            }

            if (laudo.getObs() == null) {
                stmt.setNull(18, Types.NULL);
            } else {
                stmt.setString(18, laudo.getObs().toUpperCase().trim());
            }
            stmt.setInt(19, laudo.getId());

            stmt.executeUpdate();

            conexao.commit();

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

    public Boolean excluirLaudo(LaudoBean laudo) {
        boolean retorno = false;
        String sql = "update hosp.laudo set ativo = false where id_laudo = ?";

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);

            stmt.setInt(1, laudo.getId());

            stmt.executeUpdate();

            conexao.commit();

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

    public ArrayList<LaudoBean> listaLaudos() throws ProjetoException {

        String sql = "select id_laudo, l.id_recurso, r.descrecurso, l.codpaciente, p.nome, l.data_solicitacao, l.mes_inicio, l.ano_inicio, l.mes_final, l.ano_final, "
                + " l.periodo, l.codprocedimento_primario, pr.nome as procedimento, l.codprocedimento_secundario1, l.codprocedimento_secundario2, "
                + " l.codprocedimento_secundario3, l.codprocedimento_secundario4, l.codprocedimento_secundario5, l.cid1, l.cid2, l.cid3, l.obs "
                + " from hosp.laudo l left join hosp.pacientes p on (p.id_paciente = l.codpaciente) "
                + " left join hosp.proc pr on (pr.codproc = l.codprocedimento_primario) "
                + " left join hosp.recurso r on (l.id_recurso = r.id) where l.ativo is true and l.cod_empresa = ? order by id_laudo";

        ArrayList<LaudoBean> lista = new ArrayList();

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stm = conexao.prepareStatement(sql);
            stm.setInt(1, user_session.getEmpresa().getCodEmpresa());
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                LaudoBean l = new LaudoBean();

                l.setId(rs.getInt("id_laudo"));
                l.getPaciente().setId_paciente(rs.getInt("codpaciente"));
                l.getPaciente().setNome(rs.getString("nome"));
                l.getRecurso().setIdRecurso(rs.getInt("id_recurso"));
                l.getRecurso().setDescRecurso(rs.getString("descrecurso"));
                l.setData_solicitacao(rs.getDate("data_solicitacao"));
                l.setMes_inicio(rs.getInt("mes_inicio"));
                l.setAno_inicio(rs.getInt("ano_inicio"));
                l.setMes_final(rs.getInt("mes_final"));
                l.setAno_final(rs.getInt("ano_final"));
                l.setPeriodo(rs.getInt("periodo"));
                l.getProcedimento_primario().setIdProc(rs.getInt("codprocedimento_primario"));
                l.getProcedimento_primario().setNomeProc(rs.getString("procedimento"));
                l.getProcedimento_secundario1().setIdProc(rs.getInt("codprocedimento_secundario1"));
                l.getProcedimento_secundario2().setIdProc(rs.getInt("codprocedimento_secundario2"));
                l.getProcedimento_secundario3().setIdProc(rs.getInt("codprocedimento_secundario3"));
                l.getProcedimento_secundario4().setIdProc(rs.getInt("codprocedimento_secundario4"));
                l.getProcedimento_secundario5().setIdProc(rs.getInt("codprocedimento_secundario5"));
                l.getCid1().setIdCid(rs.getInt("cid1"));
                l.getCid2().setIdCid(rs.getInt("cid2"));
                l.getCid3().setIdCid(rs.getInt("cid3"));
                l.setObs(rs.getString("obs"));

                lista.add(l);
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

    public LaudoBean buscarLaudosPorId(Integer id) throws ProjetoException {

        LaudoBean l = new LaudoBean();

        String sql = "select id_laudo, l.id_recurso, r.descrecurso, l.codpaciente, p.nome, l.data_solicitacao, l.mes_inicio, l.ano_inicio, l.mes_final, l.ano_final, "
                + " l.periodo, l.codprocedimento_primario, pr.nome as procedimento, l.codprocedimento_secundario1, ps1.nome as nome1, "
                + " l.codprocedimento_secundario2, ps2.nome as nome2, l.codprocedimento_secundario3, ps3.nome as nome3, " +
                " l.codprocedimento_secundario4, ps4.nome as nome4, " +
                " l.codprocedimento_secundario5, ps5.nome as nome5, l.cid1, c1.desccid as desccid1, l.cid2, c2.desccid as desccid2, " +
                " l.cid3, c3.desccid as desccid3, l.obs "
                + " from hosp.laudo l left join hosp.pacientes p on (p.id_paciente = l.codpaciente) "
                + " left join hosp.proc pr on (pr.id = l.codprocedimento_primario) "
                + " left join hosp.proc ps1 on (ps1.id = l.codprocedimento_secundario1) "
                + " left join hosp.proc ps2 on (ps2.id = l.codprocedimento_secundario2) "
                + " left join hosp.proc ps3 on (ps3.id = l.codprocedimento_secundario3) "
                + " left join hosp.proc ps4 on (ps4.id = l.codprocedimento_secundario4) "
                + " left join hosp.proc ps5 on (ps5.id = l.codprocedimento_secundario5) "
                + " left join hosp.cid c1 on (c1.cod = l.cid1) "
                + " left join hosp.cid c2 on (c2.cod = l.cid2) "
                + " left join hosp.cid c3 on (c3.cod = l.cid3) "
                + " left join hosp.recurso r on (l.id_recurso = r.id) where id_laudo = ?";


        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stm = conexao.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {

                l.setId(rs.getInt("id_laudo"));
                l.getPaciente().setId_paciente(rs.getInt("codpaciente"));
                l.getPaciente().setNome(rs.getString("nome"));
                l.getRecurso().setIdRecurso(rs.getInt("id_recurso"));
                l.getRecurso().setDescRecurso(rs.getString("descrecurso"));
                l.setData_solicitacao(rs.getDate("data_solicitacao"));
                l.setMes_inicio(rs.getInt("mes_inicio"));
                l.setAno_inicio(rs.getInt("ano_inicio"));
                l.setMes_final(rs.getInt("mes_final"));
                l.setAno_final(rs.getInt("ano_final"));
                l.setPeriodo(rs.getInt("periodo"));
                l.getProcedimento_primario().setIdProc(rs.getInt("codprocedimento_primario"));
                l.getProcedimento_primario().setNomeProc(rs.getString("procedimento"));
                l.getProcedimento_secundario1().setIdProc(rs.getInt("codprocedimento_secundario1"));
                l.getProcedimento_secundario1().setNomeProc(rs.getString("nome1"));
                l.getProcedimento_secundario2().setIdProc(rs.getInt("codprocedimento_secundario2"));
                l.getProcedimento_secundario2().setNomeProc(rs.getString("nome2"));
                l.getProcedimento_secundario3().setIdProc(rs.getInt("codprocedimento_secundario3"));
                l.getProcedimento_secundario3().setNomeProc(rs.getString("nome3"));
                l.getProcedimento_secundario4().setIdProc(rs.getInt("codprocedimento_secundario4"));
                l.getProcedimento_secundario4().setNomeProc(rs.getString("nome4"));
                l.getProcedimento_secundario5().setIdProc(rs.getInt("codprocedimento_secundario5"));
                l.getProcedimento_secundario5().setNomeProc(rs.getString("nome5"));
                l.getCid1().setIdCid(rs.getInt("cid1"));
                l.getCid1().setDescCid(rs.getString("desccid1"));
                l.getCid2().setIdCid(rs.getInt("cid2"));
                l.getCid2().setDescCid(rs.getString("desccid2"));
                l.getCid3().setIdCid(rs.getInt("cid3"));
                l.getCid3().setDescCid(rs.getString("desccid3"));
                l.setObs(rs.getString("obs"));

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
        return l;
    }

    public ArrayList<InsercaoPacienteBean> listarLaudosVigentesParaPaciente(Integer codPaciente)
            throws ProjetoException {

        ArrayList<InsercaoPacienteBean> lista = new ArrayList<>();

        String sql = "select nome, cns, id_laudo from ( "
                + " select l.id_laudo, l.codpaciente, p.nome, p.cns, l.id_recurso, l.data_solicitacao, l.mes_inicio, l.ano_inicio, l.mes_final, l.ano_final, l.periodo, "
                + " l.codprocedimento_primario, pr.nome as procedimento, l.cid1, ci.desccid,  "
                + " to_date(ano_inicio||'-'||'0'||''||mes_inicio||'-'||'01', 'YYYY-MM-DD') as datainicio,  "
                + " (SELECT * FROM hosp.fn_GetLastDayOfMonth(to_date(ano_final||'-'||'0'||''||mes_final||'-'||'01', 'YYYY-MM-DD'))) as datafinal "
                + " from hosp.laudo l "
                + " left join hosp.pacientes p on (l.codpaciente = p.id_paciente) "
                + " left join hosp.proc pr on (l.codprocedimento_primario = pr.id) "
                + " left join hosp.cid ci on (l.cid1 = cast(ci.cod as integer)) "
                + " where current_date >= to_date(ano_inicio||'-'||'0'||''||mes_inicio||'-'||'01', 'YYYY-MM-DD') "
                + " and current_date <= (SELECT * FROM hosp.fn_GetLastDayOfMonth(to_date(ano_final||'-'||'0'||''||mes_final||'-'||'01', 'YYYY-MM-DD'))) "
                + " AND l.codpaciente = ?"
                + " ) a";
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stm = conexao.prepareStatement(sql);
            stm.setInt(1, codPaciente);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                InsercaoPacienteBean insercao = new InsercaoPacienteBean();
                insercao.getLaudo().setId(rs.getInt("id_laudo"));
                insercao.getLaudo().getPaciente().setNome(rs.getString("nome"));
                insercao.getLaudo().getPaciente().setCns(rs.getString("cns"));

                lista.add(insercao);
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

}
