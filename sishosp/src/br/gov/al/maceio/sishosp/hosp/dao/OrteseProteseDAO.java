package br.gov.al.maceio.sishosp.hosp.dao;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.DataUtil;
import br.gov.al.maceio.sishosp.comum.util.TratamentoErrosUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.enums.StatusMovimentacaoOrteseProtese;
import br.gov.al.maceio.sishosp.hosp.enums.StatusPadraoOrteseProtese;
import br.gov.al.maceio.sishosp.hosp.model.OrteseProtese;

import javax.faces.context.FacesContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrteseProteseDAO {

    Connection con = null;
    PreparedStatement ps = null;
    FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
            .getSessionMap().get("obj_funcionario");

    public OrteseProtese carregarGrupoProgramaOrteseIhProtese() throws ProjetoException {

        OrteseProtese orteseProtese = new OrteseProtese();

        String sql = "SELECT pa.programa_ortese_protese, pr.descprograma, pa.grupo_ortese_protese, g.descgrupo " +
                "FROM hosp.parametro pa " +
                "JOIN hosp.programa pr ON (pr.id_programa = pa.programa_ortese_protese) " +
                "JOIN hosp.grupo g ON (g.id_grupo = pa.grupo_ortese_protese) " +
                "WHERE pa.codunidade = ?;";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, user_session.getUnidade().getId());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                orteseProtese.getPrograma().setIdPrograma(rs.getInt("programa_ortese_protese"));
                orteseProtese.getPrograma().setDescPrograma(rs.getString("descprograma"));
                orteseProtese.getGrupo().setIdGrupo(rs.getInt("grupo_ortese_protese"));
                orteseProtese.getGrupo().setDescGrupo(rs.getString("descgrupo"));
            }
        } catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
            try {
                con.close();
            } catch (Exception ex) {
                //comentado walter erro log ex.printStackTrace();
            }
        }
        return orteseProtese;
    }

    public OrteseProtese carregarEncaminhamentoOrteseIhProtese(Integer idOrteseProtese) throws ProjetoException {

        OrteseProtese orteseProtese = new OrteseProtese();

        String sql = "SELECT o.id, o.cod_fornecedor, f.descfornecedor, o.especificacao, o.data_encaminhamento, o.id_ortese_protese " +
                "FROM hosp.encaminhamento_opm o " +
                "LEFT JOIN hosp.fornecedor f ON (f.id_fornecedor = o.cod_fornecedor) " +
                "WHERE o.id_ortese_protese = ? AND o.data_cancelamento IS NULL;";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idOrteseProtese);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                orteseProtese.setIdEncaminhamento(rs.getInt("id"));
                orteseProtese.getFornecedor().setId(rs.getInt("cod_fornecedor"));
                orteseProtese.getFornecedor().setDescricao(rs.getString("descfornecedor"));
                orteseProtese.setEspecificacao(rs.getString("especificacao"));
                orteseProtese.setDataEncaminhamento(rs.getDate("data_encaminhamento"));
                orteseProtese.setId(rs.getInt("id_ortese_protese"));
            }
        } catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
            try {
                con.close();
            } catch (Exception ex) {
                //comentado walter erro log ex.printStackTrace();
            }
        }
        return orteseProtese;
    }

    public OrteseProtese carregarOrteseIhProtesePorId(Integer id) throws ProjetoException {

        OrteseProtese orteseProtese = new OrteseProtese();

        String sql = "SELECT o.id, o.nota_fiscal, o.cod_equipamento, t.desctipoaparelho, o.cod_laudo, o.cod_fornecedor, f.descfornecedor, " +
                "pa.programa_ortese_protese, pr.descprograma, pa.grupo_ortese_protese, g.descgrupo " +
                "FROM hosp.ortese_protese o " +
                "LEFT JOIN hosp.tipoaparelho t ON (o.cod_equipamento = t.id) " +
                "LEFT JOIN hosp.fornecedor f ON (f.id_fornecedor = o.cod_fornecedor) " +
                "JOIN hosp.parametro pa ON (pa.codunidade = o.cod_unidade) " +
                "JOIN hosp.programa pr ON (pr.id_programa = pa.programa_ortese_protese) " +
                "JOIN hosp.grupo g ON (g.id_grupo = pa.grupo_ortese_protese) " +
                "WHERE o.id = ? ";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                orteseProtese.setId(rs.getInt("id"));
                orteseProtese.getEquipamento().setId_equipamento(rs.getInt("cod_equipamento"));
                orteseProtese.getEquipamento().setDescEquipamento(rs.getString("desctipoaparelho"));
                orteseProtese.getLaudo().setId(rs.getInt("cod_laudo"));
                orteseProtese.getFornecedor().setId(rs.getInt("cod_fornecedor"));
                orteseProtese.getFornecedor().setDescricao(rs.getString("descfornecedor"));
                orteseProtese.setNotaFiscal(rs.getString("nota_fiscal"));
                orteseProtese.getPrograma().setIdPrograma(rs.getInt("programa_ortese_protese"));
                orteseProtese.getPrograma().setDescPrograma(rs.getString("descprograma"));
                orteseProtese.getGrupo().setIdGrupo(rs.getInt("grupo_ortese_protese"));
                orteseProtese.getGrupo().setDescGrupo(rs.getString("descgrupo"));
            }
        } catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
            try {
                con.close();
            } catch (Exception ex) {
                //comentado walter erro log ex.printStackTrace();
            }
        }
        return orteseProtese;
    }

    public List<OrteseProtese> listarOrteseIhProtese() throws ProjetoException {

        List<OrteseProtese> lista = new ArrayList<>();

        String sql = "select " + 
        		"	op.id, " + 
        		"	op.status, " + 
        		"	op.nota_fiscal, " + 
        		"	op.cod_laudo, " + 
        		"	op.situacao, " + 
        		"	eopm.id id_encaminhamento, " + 
        		"	p.id_paciente , " + 
        		"	p.nome nomepaciente " + 
        		"from " + 
        		"	hosp.ortese_protese op " + 
        		"left join hosp.encaminhamento_opm eopm on " + 
        		"	op.id = eopm.id_ortese_protese " + 
        		"left join hosp.laudo l on l.id_laudo  = op.cod_laudo	 " + 
        		"left join hosp.pacientes p on p.id_paciente = l.codpaciente 	 " + 
        		"where " + 
        		"	op.cod_unidade = ? " + 
        		"order by " + 
        		"	id;";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, user_session.getUnidade().getId());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                OrteseProtese orteseProtese = new OrteseProtese();
                orteseProtese.setId(rs.getInt("id"));
                orteseProtese.setStatusPadrao(rs.getString("status"));
                orteseProtese.getLaudo().setId(rs.getInt("cod_laudo"));
                orteseProtese.setNotaFiscal(rs.getString("nota_fiscal"));
                orteseProtese.setStatusMovimentacao(rs.getString("situacao"));
                orteseProtese.setIdEncaminhamento(rs.getInt("id_encaminhamento"));
                orteseProtese.getLaudo().getPaciente().setId_paciente(rs.getInt("id_encaminhamento"));
                orteseProtese.getLaudo().getPaciente().setNome(rs.getString("nomepaciente"));
                lista.add(orteseProtese);
            }
        } catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
            try {
                con.close();
            } catch (Exception ex) {
                //comentado walter erro log ex.printStackTrace();
            }
        }
        return lista;
    }

    public Boolean gravarInsercaoOrteseIhProtese(OrteseProtese orteseProtese) throws ProjetoException {

        Boolean retorno = false;
        String sql = "INSERT INTO hosp.ortese_protese (status, nota_fiscal, cod_programa, cod_grupo, cod_equipamento, cod_laudo, cod_fornecedor, " +
                "data_hora_acao, cod_operador, situacao, cod_unidade) " +
                "values (?,?,?,?,?,?,?,CURRENT_TIMESTAMP ,?,?,?) RETURNING id;";

        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);

            ps.setString(1, StatusPadraoOrteseProtese.PENDENTE.getSigla());

            if (!VerificadorUtil.verificarSeObjetoNulo(orteseProtese.getNotaFiscal())) {
                ps.setString(2, orteseProtese.getNotaFiscal().toUpperCase());
            } else {
                ps.setNull(2, Types.NULL);
            }

            ps.setInt(3, orteseProtese.getPrograma().getIdPrograma());

            ps.setInt(4, orteseProtese.getGrupo().getIdGrupo());

            ps.setInt(5, orteseProtese.getEquipamento().getId_equipamento());

            ps.setInt(6, orteseProtese.getLaudo().getId());

            if (!VerificadorUtil.verificarSeObjetoNulo(orteseProtese.getFornecedor())) {
                ps.setInt(7, orteseProtese.getFornecedor().getId());
            } else {
                ps.setNull(7, Types.NULL);
            }

            ps.setLong(8, user_session.getId());

            ps.setString(9, StatusMovimentacaoOrteseProtese.INSERCAO_DE_SOLICITACAO.getSigla());

            ps.setInt(10, user_session.getUnidade().getId());

            Integer codOrteseIhProtese = null;
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                codOrteseIhProtese = rs.getInt("id");
            }

            retorno = gravarHistoricoMovimentacaoOrteseIhProtese(StatusMovimentacaoOrteseProtese.INSERCAO_DE_SOLICITACAO.getSigla(), codOrteseIhProtese, con);

            if (retorno) {
                con.commit();
            }

        } catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
            try {
                con.close();
            } catch (Exception ex) {
                //comentado walter erro log ex.printStackTrace();
            }
        }
        return retorno;
    }

    public Boolean alterarOrteseIhProtese(OrteseProtese orteseProtese) throws ProjetoException {

        Boolean retorno = false;
        String sql = "UPDATE hosp.ortese_protese set nota_fiscal = ?, cod_equipamento = ?, cod_laudo = ?, cod_fornecedor = ? WHERE id = ?";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);

            if (!VerificadorUtil.verificarSeObjetoNulo(orteseProtese.getNotaFiscal())) {
                stmt.setString(1, orteseProtese.getNotaFiscal().toUpperCase());
            } else {
                stmt.setNull(1, Types.NULL);
            }

            stmt.setInt(2, orteseProtese.getEquipamento().getId_equipamento());
            stmt.setInt(3, orteseProtese.getLaudo().getId());

            if (!VerificadorUtil.verificarSeObjetoNulo(orteseProtese.getFornecedor().getId())) {
                stmt.setInt(4, orteseProtese.getFornecedor().getId());
            } else {
                stmt.setNull(4, Types.NULL);
            }

            stmt.setInt(5, orteseProtese.getId());
            stmt.executeUpdate();

            con.commit();
            retorno = true;

        } catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
            try {
                con.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return retorno;
    }

    public Boolean gravarEncaminhamentoOrteseIhProtese(OrteseProtese orteseProtese) throws ProjetoException {

        Boolean retorno = false;
        String sql = "INSERT INTO hosp.encaminhamento_opm (cod_fornecedor, especificacao, data_encaminhamento, usuario_encaminhamento, id_ortese_protese, cod_unidade) " +
                " values (?,?,?,?,?,?);";

        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);

            ps.setInt(1, orteseProtese.getFornecedor().getId());
            ps.setString(2, orteseProtese.getEspecificacao().toUpperCase());
            ps.setDate(3, DataUtil.converterDateUtilParaDateSql(orteseProtese.getDataEncaminhamento()));
            ps.setLong(4, user_session.getId());
            ps.setInt(5, orteseProtese.getId());
            ps.setInt(6, user_session.getUnidade().getId());
            ps.execute();

            retorno = gravarHistoricoMovimentacaoOrteseIhProtese(StatusMovimentacaoOrteseProtese.ENCAMINHAMENTO_FORNECEDOR.getSigla(), orteseProtese.getId(), con);

            if (retorno) {
                retorno = alterarSituacaoOrteseIhProtese(StatusMovimentacaoOrteseProtese.ENCAMINHAMENTO_FORNECEDOR.getSigla(), orteseProtese.getId(), con);
                if (retorno) {
                    con.commit();
                }
            }

        } catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		}finally {
            try {
                con.close();
            } catch (Exception ex) {
                //comentado walter erro log ex.printStackTrace();
            }
        }
        return retorno;
    }

    public Boolean alterarEncaminhamentoOrteseIhProtese(OrteseProtese orteseProtese) throws ProjetoException {

        Boolean retorno = false;
        String sql = "UPDATE hosp.encaminhamento_opm set cod_fornecedor = ?, especificacao = ?, data_encaminhamento = ? WHERE id = ?";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, orteseProtese.getFornecedor().getId());
            ps.setString(2, orteseProtese.getEspecificacao().toUpperCase());
            ps.setDate(3, DataUtil.converterDateUtilParaDateSql(orteseProtese.getDataEncaminhamento()));
            ps.setInt(4, orteseProtese.getIdEncaminhamento());
            ps.executeUpdate();

            con.commit();
            retorno = true;

        } catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
            try {
                con.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return retorno;
    }

    public Boolean cancelarEncaminhamentoOrteseIhProtese(OrteseProtese orteseProtese) throws ProjetoException {

        Boolean retorno = false;
        String sql = "UPDATE hosp.encaminhamento_opm set data_cancelamento = CURRENT_TIMESTAMP , usuario_cancelamento = ? WHERE id = ?";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setLong(1, user_session.getId());
            ps.setInt(2, orteseProtese.getIdEncaminhamento());
            ps.executeUpdate();

            retorno = gravarHistoricoMovimentacaoOrteseIhProtese(StatusMovimentacaoOrteseProtese.ENCAMINHAMENTO_CANCELADO.getSigla(), orteseProtese.getId(), con);
            if (retorno) {
                retorno = gravarUltimaSituacaoValidaOrteseIhProtese(orteseProtese.getId(), StatusMovimentacaoOrteseProtese.ENCAMINHAMENTO_FORNECEDOR.getSigla(), con);
                if (retorno) {
                    con.commit();
                }
            }

        } catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
            try {
                con.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return retorno;
    }

    public Boolean gravarMedicaoOrteseIhProtese(OrteseProtese orteseProtese) throws ProjetoException {

        Boolean retorno = false;
        String sql = "UPDATE hosp.ortese_protese SET medicao = ?, data_medicao = ? WHERE id = ?";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, orteseProtese.getMedicao());
            ps.setDate(2, DataUtil.converterDateUtilParaDateSql(orteseProtese.getDataMedicao()));
            ps.setInt(3, orteseProtese.getId());
            ps.executeUpdate();
            retorno = gravarHistoricoMovimentacaoOrteseIhProtese(StatusMovimentacaoOrteseProtese.MEDICAO_EFETUADA.getSigla(), orteseProtese.getId(), con);

            if (retorno) {
                retorno = alterarSituacaoOrteseIhProtese(StatusMovimentacaoOrteseProtese.MEDICAO_EFETUADA.getSigla(), orteseProtese.getId(), con);
                if (retorno) {
                    con.commit();
                }
            }

        } catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
            try {
                con.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return retorno;
    }

    public Boolean cancelarMedicaoOrteseIhProtese(Integer id) throws ProjetoException {

        Boolean retorno = false;
        String sql = "UPDATE hosp.ortese_protese SET medicao = NULL, data_medicao = NULL WHERE id = ?";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            retorno = gravarHistoricoMovimentacaoOrteseIhProtese(StatusMovimentacaoOrteseProtese.MEDICAO_CANCELADA.getSigla(), id, con);

            if (retorno) {
                retorno = gravarUltimaSituacaoValidaOrteseIhProtese(id, StatusMovimentacaoOrteseProtese.MEDICAO_EFETUADA.getSigla(), con);
                if (retorno) {
                    con.commit();
                }
            }
        } catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
            try {
                con.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return retorno;
    }

    private Boolean gravarHistoricoMovimentacaoOrteseIhProtese(String statusMovimentacao, Integer codOrteseIhProtese, Connection conAuxiliar)
    		throws ProjetoException, SQLException {

        Boolean retorno = false;

        try {
            String sql = "INSERT INTO hosp.historico_movimentacao_ortese_protese (status, data_hora_acao, cod_operador, cod_ortese_protese) " +
                    "values (?,CURRENT_TIMESTAMP,?,?);";

            ps = conAuxiliar.prepareStatement(sql);
            ps.setString(1, statusMovimentacao);
            ps.setLong(2, user_session.getId());
            ps.setInt(3, codOrteseIhProtese);
            ps.execute();
            retorno = true;
        } catch (SQLException sqle) {
        	conAuxiliar.rollback();
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			conAuxiliar.rollback();
			throw new ProjetoException(ex, this.getClass().getName());
		} 
        return retorno;
    }

    private Boolean gravarUltimaSituacaoValidaOrteseIhProtese(Integer codOrteseIhProtese, String statusDecartar, Connection conAuxiliar) 
    		throws ProjetoException, SQLException {

        Boolean retorno = false;

        String sql = "UPDATE hosp.historico_movimentacao_ortese_protese SET cancelado = TRUE " +
                "WHERE status = ? AND cod_ortese_protese = ?";

        try {
            ps = conAuxiliar.prepareStatement(sql);
            ps.setString(1, statusDecartar);
            ps.setInt(2, codOrteseIhProtese);
            ps.executeUpdate();

            String sql2 = "UPDATE hosp.ortese_protese SET situacao = ? WHERE id = ?;";

            ps = conAuxiliar.prepareStatement(sql2);
            ps.setString(1, retornarUltimaSituacaoValida(codOrteseIhProtese, statusDecartar, conAuxiliar));
            ps.setInt(2, codOrteseIhProtese);
            ps.execute();
            retorno = true;
        } catch (SQLException sqle) {
        	conAuxiliar.rollback();
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			conAuxiliar.rollback();
			throw new ProjetoException(ex, this.getClass().getName());
		}
        return retorno;
    }

    private Boolean gravarUltimaSituacaoIhStatusValidaOrteseIhProtese(Integer codOrteseIhProtese, String statusDecartar, Connection conAuxiliar) 
    		throws ProjetoException, SQLException {

        Boolean retorno = false;

        try {
            String sql = "UPDATE hosp.ortese_protese SET situacao = ?, status = ? WHERE id = ?;";
            ps = conAuxiliar.prepareStatement(sql);
            ps.setString(1, retornarUltimaSituacaoValida(codOrteseIhProtese, statusDecartar, conAuxiliar));
            ps.setString(2, StatusPadraoOrteseProtese.PENDENTE.getSigla());
            ps.setInt(3, codOrteseIhProtese);
            ps.execute();
            retorno = true;
        } catch (SQLException sqle) {
        	conAuxiliar.rollback();
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			conAuxiliar.rollback();
			throw new ProjetoException(ex, this.getClass().getName());
		} 
        return retorno;
    }

    private Boolean alterarSituacaoOrteseIhProtese(String statusMovimentacao, Integer codOrteseIhProtese, Connection conAuxiliar)
    		throws SQLException, ProjetoException {

        Boolean retorno = false;
        String sql = "UPDATE hosp.ortese_protese SET situacao = ? WHERE id = ?";

        try {
            ps = conAuxiliar.prepareStatement(sql);
            ps.setString(1, statusMovimentacao);
            ps.setInt(2, codOrteseIhProtese);
            ps.execute();

            retorno = true;
        } catch (SQLException sqle) {
        	conAuxiliar.rollback();
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			conAuxiliar.rollback();
			throw new ProjetoException(ex, this.getClass().getName());
		}
        return retorno;
    }

    public Boolean verificarSeExisteMedicao(Integer id) throws ProjetoException {

        Boolean retorno = false;
        String sql = "SELECT medicao FROM hosp.ortese_protese WHERE id = ?";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(rs.getString("medicao"))) {
                    retorno = true;
                }
            }
        } catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
            try {
                con.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return retorno;
    }

    public OrteseProtese carregarMedicaoPorIdOrteseProtese(Integer id)
            throws ProjetoException {

        PreparedStatement ps = null;
        OrteseProtese orteseProtese = new OrteseProtese();

        try {
            con = ConnectionFactory.getConnection();
            String sql = "SELECT id, medicao, data_medicao FROM hosp.ortese_protese WHERE id = ?; ";

            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                orteseProtese.setId(rs.getInt("id"));
                orteseProtese.setMedicao(rs.getString("medicao"));
                orteseProtese.setDataMedicao(rs.getDate("data_medicao"));
            }

        } catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
            try {
                con.close();
            } catch (Exception ex) {
                //comentado walter erro log ex.printStackTrace();
            }
        }
        return orteseProtese;
    }

    public String retornarUltimaSituacaoValida(Integer id, String statusDescartar, Connection conAuxiliar) throws ProjetoException, SQLException {

        String resultado = null;

        PreparedStatement ps = null;

        try {
            String sql = "SELECT status FROM hosp.historico_movimentacao_ortese_protese WHERE cod_ortese_protese = ? " +
                    "AND status NOT IN ('MC', 'EC', 'RC', 'CE') and status <> ? ORDER BY id DESC LIMIT 1;";

            //Ver o que significa as siglas na classe: StatusMovimentacaoOrteseProtese

            ps = conAuxiliar.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setString(2, statusDescartar);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                resultado = rs.getString("status");
            }

        } catch (SQLException sqle) {
        	conAuxiliar.rollback();
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			conAuxiliar.rollback();
			throw new ProjetoException(ex, this.getClass().getName());
		} 
        return resultado;
    }

    public Boolean gravarRecebimentoOrteseIhProtese(Integer id) throws ProjetoException {

        Boolean retorno = false;
        String sql = "UPDATE hosp.ortese_protese SET situacao = ?, data_hora_acao = CURRENT_TIMESTAMP WHERE id = ?";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, StatusMovimentacaoOrteseProtese.EQUIPAMENTO_RECEBIDO.getSigla());
            ps.setInt(2, id);
            ps.executeUpdate();
            retorno = gravarHistoricoMovimentacaoOrteseIhProtese(StatusMovimentacaoOrteseProtese.EQUIPAMENTO_RECEBIDO.getSigla(), id, con);

            if (retorno) {
                con.commit();
            }
        } catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		}  finally {
            try {
                con.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return retorno;
    }

    public Boolean cancelarRecebimentoOrteseIhProtese(Integer id) throws ProjetoException {

        Boolean retorno = false;
        try {
            con = ConnectionFactory.getConnection();
            retorno = gravarHistoricoMovimentacaoOrteseIhProtese(StatusMovimentacaoOrteseProtese.RECEBIMENTO_CANCELADO.getSigla(), id, con);
            if (retorno) {
                retorno = gravarUltimaSituacaoValidaOrteseIhProtese(id, StatusMovimentacaoOrteseProtese.EQUIPAMENTO_RECEBIDO.getSigla(), con);
                if (retorno) {
                    con.commit();
                }
            }
        } catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
            try {
                con.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return retorno;
    }

    public Boolean gravarEntregaOrteseIhProtese(Integer id) throws ProjetoException {

        Boolean retorno = false;
        String sql = "UPDATE hosp.ortese_protese SET situacao = ?, status = ?, data_hora_acao = CURRENT_TIMESTAMP WHERE id = ?";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, StatusMovimentacaoOrteseProtese.EQUIPAMENTO_ENTREGUE.getSigla());
            ps.setString(2, StatusPadraoOrteseProtese.ENTREGUE.getSigla());
            ps.setInt(3, id);
            ps.executeUpdate();

            retorno = gravarHistoricoMovimentacaoOrteseIhProtese(StatusMovimentacaoOrteseProtese.EQUIPAMENTO_ENTREGUE.getSigla(), id, con);

            if (retorno) {
                con.commit();
            }

        } catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
            try {
                con.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return retorno;
    }

    public Boolean cancelarEntregaOrteseIhProtese(Integer id) throws ProjetoException {

        Boolean retorno = false;

        try {
            con = ConnectionFactory.getConnection();
            retorno = gravarHistoricoMovimentacaoOrteseIhProtese(StatusMovimentacaoOrteseProtese.CANCELAR_ENTREGA.getSigla(), id, con);

            if (retorno) {
                retorno = gravarUltimaSituacaoIhStatusValidaOrteseIhProtese(id, StatusMovimentacaoOrteseProtese.EQUIPAMENTO_ENTREGUE.getSigla(), con);
                if (retorno) {
                    con.commit();
                }
            }

        } catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
            try {
                con.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return retorno;
    }

    public String verificarSituacao(Integer id) throws ProjetoException {

        String retorno = "";

        String sql = "SELECT situacao FROM hosp.ortese_protese WHERE id = ?";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                retorno = rs.getString("situacao");
            }

        } catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
            try {
                con.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return retorno;
    }

}
