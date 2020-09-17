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

import javax.faces.context.FacesContext;
import javax.xml.ws.soap.SOAPFaultException;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.TratamentoErrosUtil;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.model.AtendimentoBean;
import br.gov.al.maceio.sishosp.hosp.model.CboBean;
import br.gov.al.maceio.sishosp.hosp.model.CidBean;
import br.gov.al.maceio.sishosp.hosp.model.HistoricoSigtapBean;
import br.gov.al.maceio.sishosp.hosp.model.ProcedimentoBean;
import br.gov.al.maceio.sishosp.hosp.model.UnidadeBean;
import br.gov.al.maceio.sishosp.hosp.model.dto.GravarProcedimentoMensalDTO;
import br.gov.al.maceio.sishosp.hosp.model.dto.PropriedadeDeProcedimentoMensalExistenteDTO;
import sigtap.br.gov.saude.servicos.schema.cbo.v1.cbo.CBOType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.cid.v1.cid.AgravoType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.cid.v1.cid.CIDType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.financiamento.v1.tipofinanciamento.TipoFinanciamentoType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.nivelagregacao.v1.formaorganizacao.FormaOrganizacaoType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.nivelagregacao.v1.grupo.GrupoType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.nivelagregacao.v1.subgrupo.SubgrupoType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.renases.v1.renases.RENASESType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.servicoclassificacao.v1.servico.ServicoType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.servicoclassificacao.v1.servicoclassificacao.ServicoClassificacaoType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.v1.complexidade.ComplexidadeType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.v1.instrumentoregistro.InstrumentoRegistroType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.v1.modalidadeatendimento.ModalidadeAtendimentoType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.v1.procedimento.ProcedimentoType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.v1.procedimento.ProcedimentoType.CBOsVinculados;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.v1.procedimento.ProcedimentoType.CIDsVinculados;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.v1.procedimento.ProcedimentoType.CIDsVinculados.CIDVinculado;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.v1.procedimento.ProcedimentoType.InstrumentosRegistro;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.v1.procedimento.ProcedimentoType.ModalidadesAtendimento;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.v1.procedimento.ProcedimentoType.RENASESVinculadas;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.v1.procedimento.ProcedimentoType.ServicosClassificacoesVinculados;
import sigtap.br.gov.saude.servicos.schema.sigtap.v1.idadelimite.IdadeLimiteType;
import sigtap.br.gov.saude.servicos.schema.sigtap.v1.idadelimite.UnidadeLimiteType;


public class ProcedimentoDAO {
    Connection con = null;
    PreparedStatement ps = null;

    FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
            .getSessionMap().get("obj_funcionario");

    public boolean gravarProcedimento(ProcedimentoBean proc) throws ProjetoException {

        Boolean retorno = false;

        String sql = "INSERT INTO hosp.proc (codproc, nome, auditivo, tipo_exame_auditivo, utiliza_equipamento, gera_laudo_digita, validade_laudo, "
                + " cod_unidade) VALUES (?, ?, ?, ?, ?, ?, ?, ?) returning id;";
        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);

            ps.setString(1, proc.getCodProc());
            ps.setString(2, proc.getNomeProc().toUpperCase());
            ps.setBoolean(3, proc.getAuditivo());
            ps.setString(4, proc.getTipoExameAuditivo().toUpperCase());
            ps.setBoolean(5, proc.getUtilizaEquipamento());
            if (proc.isGera_laudo_digita() == false) {
                ps.setNull(6, Types.BOOLEAN);
            } else {
                ps.setBoolean(6, proc.isGera_laudo_digita());
            }
            if (proc.isGera_laudo_digita() == false) {
                ps.setNull(7, Types.INTEGER);
            } else {
                ps.setInt(7, proc.getValidade_laudo());
            }

            ps.setInt(8, user_session.getUnidade().getId());
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                proc.setIdProc(rs.getInt("id"));
            }


            gravarProcedimentoUnidade(proc, con);

            con.commit();
            retorno = true;
        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
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


    public void excluirProcedimentoUnidade(Integer idProcedimento, Connection conexaoAuxiliar) throws ProjetoException, SQLException {

        String sql = "DELETE FROM hosp.procedimentos_unidades WHERE id_procedimento = ?; ";
        try {
            PreparedStatement ps = conexaoAuxiliar.prepareStatement(sql);

            ps.setInt(1, idProcedimento);
            ps.executeUpdate();
        } catch (SQLException sqle) {
            conexaoAuxiliar.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conexaoAuxiliar.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
    }

    public void gravarProcedimentoUnidade(ProcedimentoBean procedimento, Connection conexaoAuxiliar) throws ProjetoException, SQLException {

        String sql = "INSERT INTO hosp.procedimentos_unidades (id_procedimento, id_unidade) VALUES(?, ?); ";
        try {
            PreparedStatement ps = conexaoAuxiliar.prepareStatement(sql);

            for (UnidadeBean unidade : procedimento.getListaUnidadesVisualizam()) {
                ps.setInt(1, procedimento.getIdProc());
                ps.setInt(2, unidade.getId());
                ps.executeUpdate();
            }

        } catch (SQLException sqle) {
            conexaoAuxiliar.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conexaoAuxiliar.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
    }

    public boolean alterarProcedimento(ProcedimentoBean proc) throws ProjetoException {

        Boolean retorno = false;

        String sql = "update hosp.proc set nome = ?, auditivo = ?, tipo_exame_auditivo = ?, utiliza_equipamento = ?, "
                + "gera_laudo_digita = ?, validade_laudo = ?, codproc = ? "
                + "where id = ?";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, proc.getNomeProc().toUpperCase());
            stmt.setBoolean(2, proc.getAuditivo());
            if(VerificadorUtil.verificarSeObjetoNuloOuZero(proc.getTipoExameAuditivo()))
                stmt.setNull(3, Types.NULL);
            else
                stmt.setString(3, proc.getTipoExameAuditivo().toUpperCase());
            stmt.setBoolean(4, proc.getUtilizaEquipamento());
            if (proc.isGera_laudo_digita() == false) {
                stmt.setNull(5, Types.BOOLEAN);
            } else {
                stmt.setBoolean(5, proc.isGera_laudo_digita());
            }
            if (proc.isGera_laudo_digita() == false) {
                stmt.setNull(6, Types.INTEGER);
            } else {
                stmt.setInt(6, proc.getValidade_laudo());
            }
            stmt.setString(7, proc.getCodProc());
            stmt.setInt(8, proc.getIdProc());

            excluirProcedimentoUnidade(proc.getIdProc(), con);
            gravarProcedimentoUnidade(proc, con);

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
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return retorno;
    }

    public boolean excluirProcedimento(ProcedimentoBean proc) throws ProjetoException{

        Boolean retorno = false;

        try {
            con = ConnectionFactory.getConnection();

            String sql = "UPDATE hosp.proc set ativo = 'N' WHERE id = ?;";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt = con.prepareStatement(sql);
            stmt.setLong(1, proc.getIdProc());
            stmt.execute();

            con.commit();
            retorno = true;
        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
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

    public List<ProcedimentoBean> listarProcedimento() throws ProjetoException {
        List<ProcedimentoBean> lista = new ArrayList<>();
        String sql = "select id, codproc, nome, auditivo, tipo_exame_auditivo, utiliza_equipamento, gera_laudo_digita, validade_laudo, " +
                " idade_minima, idade_maxima, qtd_maxima, prazo_minimo_nova_execucao, sexo " +
                " from hosp.proc p " +
                " join hosp.procedimentos_unidades pu on (p.id = pu.id_procedimento) " +
                " where pu.id_unidade = ? and ativo = 'S'  order by nome";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, user_session.getUnidade().getId());
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                ProcedimentoBean proc = new ProcedimentoBean();
                proc.setIdProc(rs.getInt("id"));
                proc.setCodProc(rs.getString("codproc"));
                proc.setNomeProc(rs.getString("nome"));
                proc.setAuditivo(rs.getBoolean("auditivo"));
                proc.setTipoExameAuditivo(rs.getString("tipo_exame_auditivo"));
                proc.setUtilizaEquipamento(rs.getBoolean("utiliza_equipamento"));
                proc.setGera_laudo_digita(rs.getBoolean("gera_laudo_digita"));
                proc.setValidade_laudo(rs.getInt("validade_laudo"));
                proc.setIdadeMinima(rs.getInt("idade_minima"));
                proc.setIdadeMaxima(rs.getInt("idade_maxima"));
                proc.setQtdMaxima(rs.getInt("qtd_maxima"));
                proc.setPrazoMinimoNovaExecucao(rs.getInt("prazo_minimo_nova_execucao"));
                proc.setSexo(rs.getString("sexo"));

                lista.add(proc);
            }
        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
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

    public List<ProcedimentoBean> listarTodosProcedimentos() throws ProjetoException {
        List<ProcedimentoBean> lista = new ArrayList<>();
        String sql = "select id, codproc, nome, auditivo, tipo_exame_auditivo, utiliza_equipamento, gera_laudo_digita, validade_laudo, " +
                " idade_minima, idade_maxima, qtd_maxima, prazo_minimo_nova_execucao, sexo " +
                " from hosp.proc p " +
                " where  ativo = 'S'  order by nome";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
         //   stm.setInt(1, user_session.getUnidade().getId());
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                ProcedimentoBean proc = new ProcedimentoBean();
                proc.setIdProc(rs.getInt("id"));
                proc.setCodProc(rs.getString("codproc"));
                proc.setNomeProc(rs.getString("nome"));
                proc.setAuditivo(rs.getBoolean("auditivo"));
                proc.setTipoExameAuditivo(rs.getString("tipo_exame_auditivo"));
                proc.setUtilizaEquipamento(rs.getBoolean("utiliza_equipamento"));
                proc.setGera_laudo_digita(rs.getBoolean("gera_laudo_digita"));
                proc.setValidade_laudo(rs.getInt("validade_laudo"));
                proc.setIdadeMinima(rs.getInt("idade_minima"));
                proc.setIdadeMaxima(rs.getInt("idade_maxima"));
                proc.setQtdMaxima(rs.getInt("qtd_maxima"));
                proc.setPrazoMinimoNovaExecucao(rs.getInt("prazo_minimo_nova_execucao"));
                proc.setSexo(rs.getString("sexo"));

                lista.add(proc);
            }
        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
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

    public List<ProcedimentoBean> listarProcedimentosDaEmpresa() throws ProjetoException {
        List<ProcedimentoBean> lista = new ArrayList<>();
        String sql = "select id, codproc, nome, auditivo, tipo_exame_auditivo, utiliza_equipamento, gera_laudo_digita, validade_laudo, "
                + " idade_minima, idade_maxima, qtd_maxima, prazo_minimo_nova_execucao, sexo "
                + " from hosp.proc where ativo = 'S' order by nome";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                ProcedimentoBean proc = new ProcedimentoBean();
                proc.setIdProc(rs.getInt("id"));
                proc.setCodProc(rs.getString("codproc"));
                proc.setNomeProc(rs.getString("nome"));
                proc.setAuditivo(rs.getBoolean("auditivo"));
                proc.setTipoExameAuditivo(rs.getString("tipo_exame_auditivo"));
                proc.setUtilizaEquipamento(rs.getBoolean("utiliza_equipamento"));
                proc.setGera_laudo_digita(rs.getBoolean("gera_laudo_digita"));
                proc.setValidade_laudo(rs.getInt("validade_laudo"));
                proc.setIdadeMinima(rs.getInt("idade_minima"));
                proc.setIdadeMaxima(rs.getInt("idade_maxima"));
                proc.setQtdMaxima(rs.getInt("qtd_maxima"));
                proc.setPrazoMinimoNovaExecucao(rs.getInt("prazo_minimo_nova_execucao"));
                proc.setSexo(rs.getString("sexo"));

                lista.add(proc);
            }
        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
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

    public List<ProcedimentoBean> buscarProcedimento(String campoBusca, String tipo) throws ProjetoException {
        List<ProcedimentoBean> lista = new ArrayList<>();
        String sql = "select id, codproc, nome, auditivo, tipo_exame_auditivo, utiliza_equipamento, gera_laudo_digita, validade_laudo, "
                + " idade_minima, idade_maxima, qtd_maxima, prazo_minimo_nova_execucao, sexo "
                + " from hosp.proc where cod_unidade = ? and ativo = 'S' ";

        if(tipo.equals("descricao")){
            sql = sql + "and nome ilike ? ";
        }
        else if (tipo.equals("codigo")){
            sql = sql + "and codproc ilike ? ";
        }

        sql = sql + "order by nome";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, user_session.getUnidade().getId());
            stm.setString(2, "%" + campoBusca.toUpperCase() + "%");
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                ProcedimentoBean proc = new ProcedimentoBean();
                proc.setIdProc(rs.getInt("id"));
                proc.setCodProc(rs.getString("codproc"));
                proc.setNomeProc(rs.getString("nome"));
                proc.setAuditivo(rs.getBoolean("auditivo"));
                proc.setTipoExameAuditivo(rs.getString("tipo_exame_auditivo"));
                proc.setUtilizaEquipamento(rs.getBoolean("utiliza_equipamento"));
                proc.setGera_laudo_digita(rs.getBoolean("gera_laudo_digita"));
                proc.setValidade_laudo(rs.getInt("validade_laudo"));
                proc.setIdadeMinima(rs.getInt("idade_minima"));
                proc.setIdadeMaxima(rs.getInt("idade_maxima"));
                proc.setQtdMaxima(rs.getInt("qtd_maxima"));
                proc.setPrazoMinimoNovaExecucao(rs.getInt("prazo_minimo_nova_execucao"));
                proc.setSexo(rs.getString("sexo"));

                lista.add(proc);
            }
        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
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

    public List<ProcedimentoBean> listarProcedimentoLaudo() throws ProjetoException {
        List<ProcedimentoBean> lista = new ArrayList<>();
        String sql = "select id, codproc, nome, auditivo, tipo_exame_auditivo, utiliza_equipamento, gera_laudo_digita, validade_laudo, "
                + " idade_minima, idade_maxima, qtd_maxima, prazo_minimo_nova_execucao, sexo "
                + " from hosp.proc "
                + " join hosp.procedimentos_unidades pu on (proc.id = pu.id_procedimento)"
                + "where pu.id_unidade = ? and gera_laudo_digita is true "
                + " and ativo = 'S' order by nome";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, user_session.getUnidade().getId());
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                ProcedimentoBean proc = new ProcedimentoBean();
                proc.setIdProc(rs.getInt("id"));
                proc.setCodProc(rs.getString("codproc"));
                proc.setNomeProc(rs.getString("nome"));
                proc.setAuditivo(rs.getBoolean("auditivo"));
                proc.setTipoExameAuditivo(rs.getString("tipo_exame_auditivo"));
                proc.setUtilizaEquipamento(rs.getBoolean("utiliza_equipamento"));
                proc.setGera_laudo_digita(rs.getBoolean("gera_laudo_digita"));
                proc.setValidade_laudo(rs.getInt("validade_laudo"));
                proc.setIdadeMinima(rs.getInt("idade_minima"));
                proc.setIdadeMaxima(rs.getInt("idade_maxima"));
                proc.setQtdMaxima(rs.getInt("qtd_maxima"));
                proc.setPrazoMinimoNovaExecucao(rs.getInt("prazo_minimo_nova_execucao"));
                proc.setSexo(rs.getString("sexo"));

                lista.add(proc);
            }
        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
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

    public ProcedimentoBean listarProcedimentoPorId(int id) throws ProjetoException {
        ProcedimentoBean procedimento = new ProcedimentoBean();
        String sql = "select id, codproc, nome, auditivo, tipo_exame_auditivo, utiliza_equipamento, gera_laudo_digita, validade_laudo,"
                + " idade_minima, idade_maxima, qtd_maxima, prazo_minimo_nova_execucao, sexo "
                + "from hosp.proc where id = ? and ativo = 'S' order by nome";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                procedimento = new ProcedimentoBean();
                procedimento.setIdProc(rs.getInt("id"));
                procedimento.setCodProc(rs.getString("codproc"));
                procedimento.setNomeProc(rs.getString("nome"));
                procedimento.setAuditivo(rs.getBoolean("auditivo"));
                procedimento.setTipoExameAuditivo(rs.getString("tipo_exame_auditivo"));
                procedimento.setUtilizaEquipamento(rs.getBoolean("utiliza_equipamento"));
                procedimento.setGera_laudo_digita(rs.getBoolean("gera_laudo_digita"));
                procedimento.setValidade_laudo(rs.getInt("validade_laudo"));
                procedimento.setIdadeMinima(rs.getInt("idade_minima"));
                procedimento.setIdadeMaxima(rs.getInt("idade_maxima"));
                procedimento.setQtdMaxima(rs.getInt("qtd_maxima"));
                procedimento.setPrazoMinimoNovaExecucao(rs.getInt("prazo_minimo_nova_execucao"));
                procedimento.setSexo(rs.getString("sexo"));
                procedimento.setListaUnidadesVisualizam(listarProcedimentoUnidade(procedimento.getIdProc(), con));
            }
        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return procedimento;
    }

    public ArrayList<UnidadeBean> listarProcedimentoUnidade(Integer idProcedimento, Connection conexaoAuxiliar)
            throws ProjetoException, SQLException {

        ArrayList<UnidadeBean> lista = new ArrayList<>();
        String sql = "select pu.id_unidade, u.nome, u.matriz from hosp.procedimentos_unidades pu " +
                "join hosp.unidade u on pu.id_unidade = u.id where pu.id_procedimento = ?";

        try {
            PreparedStatement stm = conexaoAuxiliar.prepareStatement(sql);
            stm.setInt(1, idProcedimento);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                UnidadeBean unidade = new UnidadeBean();
                unidade.setNomeUnidade(rs.getString("nome"));
                unidade.setId(rs.getInt("id_unidade"));
                unidade.setMatriz(rs.getBoolean("matriz"));
                lista.add(unidade);
            }
        } catch (SQLException sqle) {
            conexaoAuxiliar.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conexaoAuxiliar.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
        return lista;
    }

    public List<ProcedimentoBean> listarProcedimentoBusca(
            String descricaoBusca, Integer tipoBuscar) throws ProjetoException {

        List<ProcedimentoBean> lista = new ArrayList<>();
        String sql = "select id,codproc  ||' - '|| nome as nome ,codproc, auditivo, tipo_exame_auditivo, utiliza_equipamento, gera_laudo_digita, validade_laudo,"
                + "idade_minima, idade_maxima, qtd_maxima, prazo_minimo_nova_execucao, sexo  "
                + "from hosp.proc join hosp.procedimentos_unidades pu on\n" +
                "\t(proc.id = pu.id_procedimento) where proc.ativo = 'S' ";
        if (tipoBuscar == 1) {
            sql += " and upper(proc.codproc ||' - '|| proc.nome) LIKE ? and pu.id_unidade = ? order by nome";
        }
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setString(1, "%" + descricaoBusca.toUpperCase() + "%");
            stm.setInt(2, user_session.getUnidade().getId());
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                ProcedimentoBean proc = new ProcedimentoBean();
                proc.setIdProc(rs.getInt("id"));
                proc.setNomeProc(rs.getString("nome"));
                proc.setCodProc(rs.getString("codproc"));
                proc.setAuditivo(rs.getBoolean("auditivo"));
                proc.setTipoExameAuditivo(rs.getString("tipo_exame_auditivo"));
                proc.setUtilizaEquipamento(rs.getBoolean("utiliza_equipamento"));
                proc.setGera_laudo_digita(rs.getBoolean("gera_laudo_digita"));
                proc.setValidade_laudo(rs.getInt("validade_laudo"));
                proc.setIdadeMinima(rs.getInt("idade_minima"));
                proc.setIdadeMaxima(rs.getInt("idade_maxima"));
                proc.setQtdMaxima(rs.getInt("qtd_maxima"));
                proc.setPrazoMinimoNovaExecucao(rs.getInt("prazo_minimo_nova_execucao"));
                proc.setSexo(rs.getString("sexo"));

                lista.add(proc);
            }
        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
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

    public List<ProcedimentoBean> listarProcedimentoQueGeramLaudoBusca(
            String descricaoBusca, Integer tipoBuscar) throws ProjetoException {

        List<ProcedimentoBean> lista = new ArrayList<>();
        String sql = "select id,codproc  ||' - '|| nome as nome ,codproc, auditivo, tipo_exame_auditivo, utiliza_equipamento, gera_laudo_digita, validade_laudo,"
                + "idade_minima, idade_maxima, qtd_maxima, prazo_minimo_nova_execucao, sexo  "
                + "from hosp.proc where ativo = 'S' ";
        if (tipoBuscar == 1) {
            sql += " and (codproc ||' - '|| nome) ILIKE ? and cod_unidade = ? and gera_laudo_digita is true order by nome";
        }
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setString(1, "%" + descricaoBusca.toUpperCase() + "%");
            stm.setInt(2, user_session.getUnidade().getId());
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                ProcedimentoBean proc = new ProcedimentoBean();
                proc.setIdProc(rs.getInt("id"));
                proc.setNomeProc(rs.getString("nome"));
                proc.setCodProc(rs.getString("codproc"));
                proc.setAuditivo(rs.getBoolean("auditivo"));
                proc.setTipoExameAuditivo(rs.getString("tipo_exame_auditivo"));
                proc.setUtilizaEquipamento(rs.getBoolean("utiliza_equipamento"));
                proc.setGera_laudo_digita(rs.getBoolean("gera_laudo_digita"));
                proc.setValidade_laudo(rs.getInt("validade_laudo"));
                proc.setIdadeMinima(rs.getInt("idade_minima"));
                proc.setIdadeMaxima(rs.getInt("idade_maxima"));
                proc.setQtdMaxima(rs.getInt("qtd_maxima"));
                proc.setPrazoMinimoNovaExecucao(rs.getInt("prazo_minimo_nova_execucao"));
                proc.setSexo(rs.getString("sexo"));

                lista.add(proc);
            }
        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
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

    public ArrayList<CidBean> listarCidDoProcedimentoDeumaCompetencia(Integer id_proc) throws ProjetoException {

        ArrayList<CidBean> lista = new ArrayList<>();
        String sql = "select c.cod, c.cid, c.desccid from hosp.cid c left join sigtap.proc_cid p on (c.cod = p.id_cid) where id_proc = ? order by cid";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, id_proc);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                CidBean cid = new CidBean();

                cid.setIdCid(rs.getInt("cod"));
                cid.setDescCid(rs.getString("desccid"));
                cid.setCid(rs.getString("cid"));

                lista.add(cid);
            }
        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
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

    public ArrayList<CboBean> listarCbo(Integer id_proc) throws ProjetoException {

        ArrayList<CboBean> lista = new ArrayList<>();
        String sql = "select c.id, c.descricao from hosp.cbo c left join sigtap.proc_cbo p on (c.id = p.id_cbo) where id_proc = ? order by descricao";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, id_proc);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                CboBean cbo = new CboBean();

                cbo.setCodCbo(rs.getInt("id"));
                cbo.setDescCbo(rs.getString("descricao"));

                lista.add(cbo);
            }
        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
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

    public ProcedimentoBean listarProcedimentoPorIdComConexao(int id, Connection conAuxiliar)
            throws ProjetoException, SQLException {
        ProcedimentoBean proc = new ProcedimentoBean();
        String sql = "select id, codproc, nome, auditivo, tipo_exame_auditivo, utiliza_equipamento, gera_laudo_digita, validade_laudo,"
                + " idade_minima, idade_maxima, qtd_maxima, prazo_minimo_nova_execucao, sexo "
                + "from hosp.proc where id = ? and ativo = 'S' order by nome";
        try {
            PreparedStatement stm = conAuxiliar.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                proc = new ProcedimentoBean();
                proc.setIdProc(rs.getInt("id"));
                proc.setCodProc(rs.getString("codproc"));
                proc.setNomeProc(rs.getString("nome"));
                proc.setAuditivo(rs.getBoolean("auditivo"));
                proc.setTipoExameAuditivo(rs.getString("tipo_exame_auditivo"));
                proc.setUtilizaEquipamento(rs.getBoolean("utiliza_equipamento"));
                proc.setGera_laudo_digita(rs.getBoolean("gera_laudo_digita"));
                proc.setValidade_laudo(rs.getInt("validade_laudo"));
                proc.setIdadeMinima(rs.getInt("idade_minima"));
                proc.setIdadeMaxima(rs.getInt("idade_maxima"));
                proc.setQtdMaxima(rs.getInt("qtd_maxima"));
                proc.setPrazoMinimoNovaExecucao(rs.getInt("prazo_minimo_nova_execucao"));
                proc.setSexo(rs.getString("sexo"));
            }
        } catch (SQLException sqle) {
            conAuxiliar.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conAuxiliar.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
        return proc;
    }

    public List<PropriedadeDeProcedimentoMensalExistenteDTO> buscaModalidadeAtendimentoExistente(Connection con) throws ProjetoException {
        List<PropriedadeDeProcedimentoMensalExistenteDTO> listaModaliadesAtendimentoExistente = new ArrayList();
        String sql = "SELECT ma.id, ma.codigo FROM sigtap.modalidade_atendimento ma ";
        try {
            ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                PropriedadeDeProcedimentoMensalExistenteDTO modalidadeAtendimentoExistente = new PropriedadeDeProcedimentoMensalExistenteDTO();
                modalidadeAtendimentoExistente.setId(rs.getInt("id"));
                modalidadeAtendimentoExistente.setCodigo(rs.getString("codigo"));
                listaModaliadesAtendimentoExistente.add(modalidadeAtendimentoExistente);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return listaModaliadesAtendimentoExistente;
    }

    public List<PropriedadeDeProcedimentoMensalExistenteDTO> buscaInstrumentosRegistroExistente(Connection con) throws ProjetoException {
        List<PropriedadeDeProcedimentoMensalExistenteDTO> listaInstrumentoRegistroExistente = new ArrayList();
        String sql = "SELECT ir.id, ir.codigo FROM sigtap.instrumento_registro ir ";
        try {
            ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                PropriedadeDeProcedimentoMensalExistenteDTO instrumentoRegistroExistente = new PropriedadeDeProcedimentoMensalExistenteDTO();
                instrumentoRegistroExistente.setId(rs.getInt("id"));
                instrumentoRegistroExistente.setCodigo(rs.getString("codigo"));
                listaInstrumentoRegistroExistente.add(instrumentoRegistroExistente);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return listaInstrumentoRegistroExistente;
    }


    public List<PropriedadeDeProcedimentoMensalExistenteDTO> buscaCidsExistentes(Connection con) throws ProjetoException {
        List<PropriedadeDeProcedimentoMensalExistenteDTO> listaCidExistente = new ArrayList();
        String sql = "SELECT cid.cod, cid.cid FROM hosp.cid ";
        try {
            ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                PropriedadeDeProcedimentoMensalExistenteDTO cidExistente = new PropriedadeDeProcedimentoMensalExistenteDTO();
                cidExistente.setId(rs.getInt("cod"));
                cidExistente.setCodigo(rs.getString("cid"));
                listaCidExistente.add(cidExistente);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return listaCidExistente;
    }



    public List<PropriedadeDeProcedimentoMensalExistenteDTO> buscaRenasesExistentes(Connection con) throws ProjetoException {
        List<PropriedadeDeProcedimentoMensalExistenteDTO> listaRenasesExistentes = new ArrayList();
        String sql = "SELECT re.id, re.codigo FROM sigtap.renases re ";
        try {
            ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                PropriedadeDeProcedimentoMensalExistenteDTO renasesExistente = new PropriedadeDeProcedimentoMensalExistenteDTO();
                renasesExistente.setId(rs.getInt("id"));
                renasesExistente.setCodigo(rs.getString("codigo"));
                listaRenasesExistentes.add(renasesExistente);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return listaRenasesExistentes;
    }

    public List<PropriedadeDeProcedimentoMensalExistenteDTO> buscaTiposFinanciamentoExistentes(Connection con) throws ProjetoException {
        List<PropriedadeDeProcedimentoMensalExistenteDTO> listaCodigoTipoFinanciamento = new ArrayList();
        String sql = "SELECT tf.id, tf.codigo FROM sigtap.tipo_financiamento tf ";
        try {
            ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                PropriedadeDeProcedimentoMensalExistenteDTO tipoFinanciamentoExistente = new PropriedadeDeProcedimentoMensalExistenteDTO();
                tipoFinanciamentoExistente.setId(rs.getInt("id"));
                tipoFinanciamentoExistente.setCodigo(rs.getString("codigo"));
                listaCodigoTipoFinanciamento.add(tipoFinanciamentoExistente);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return listaCodigoTipoFinanciamento;
    }



    public void executaRotinaNovaCargaSigtap(List<GravarProcedimentoMensalDTO> listaProcedimentoMensalDTO,
                                             List<GrupoType>  listaGrupos,
                                             List<SubgrupoType>  listaSubGrupos,  List<FormaOrganizacaoType>  listaFormasOrganizacao, Long idFuncionario, Integer mes, Integer ano) throws Exception {

        String codigoProcedimentoQueDeuErro = "";
        try {
            this.con = ConnectionFactory.getConnection();

            desatualizaStatusHistoricoConsumoSigtapAnterior( mes, ano, con);
            Integer idHistoricoConsumoSigtap = gravarHistoricoConsumoSigtap(idFuncionario,mes, ano, con);

            inserirListaDeGrupos(listaGrupos, idHistoricoConsumoSigtap, con);
            inserirListaDeSubGrupos(listaSubGrupos, idHistoricoConsumoSigtap, con);
            inserirFormaOrganizacao(listaFormasOrganizacao,idHistoricoConsumoSigtap,  con);
            for (GravarProcedimentoMensalDTO procedimentoMensalDTO : listaProcedimentoMensalDTO) {
                codigoProcedimentoQueDeuErro = procedimentoMensalDTO.getProcedimentoMensal().getCodigo();

                Integer idProcedimentoMensal = inserirProcedimentoMensal(procedimentoMensalDTO, con,
                        idHistoricoConsumoSigtap);

                inserirCIDs(procedimentoMensalDTO.getProcedimentoMensal().getCIDsVinculados().getCIDVinculado(),
                        idProcedimentoMensal, con);

                inserirModalidadeAtendimento(
                        procedimentoMensalDTO.getProcedimentoMensal().getModalidadesAtendimento().getModalidadeAtendimento(),
                        idProcedimentoMensal, con);

                inserirInstrumentosRegistro(
                        procedimentoMensalDTO.getProcedimentoMensal().getInstrumentosRegistro()
                                .getInstrumentoRegistro(),
                        idProcedimentoMensal, con);

                inserirCBOs(procedimentoMensalDTO.getProcedimentoMensal().getCBOsVinculados().getCBO(),
                        idProcedimentoMensal, con);

                inserirRenases(procedimentoMensalDTO.getProcedimentoMensal().getRENASESVinculadas().getRENASES(),
                        idProcedimentoMensal, con);

                inserirServicoClassificacao(procedimentoMensalDTO.getProcedimentoMensal()
                        .getServicosClassificacoesVinculados().getServicoClassificacao(), idProcedimentoMensal, con);

            }
            con.commit();
        } catch (SOAPFaultException soape) {
            JSFUtil.adicionarMensagemErro("Erro, algo de inesperado ocorreu durante a carga do procedimento "
                    + codigoProcedimentoQueDeuErro
                    + " verifique se o código do procedimento está correto"
                    + " e execute a alteração do procedimento para que seja possível realizar uma nova"
                    + " carga do SIGTAP este mês", "");
            soape.printStackTrace();
        } catch (Exception e) {
            throw e;
        } finally {
            con.close();
        }
    }

    public Integer inserirTipoFinanciamento(TipoFinanciamentoType tipoFinanciamento, Connection conexao) throws ProjetoException, SQLException {
        String sql = "INSERT INTO sigtap.tipo_financiamento (codigo, nome) "
                + "VALUES(?, ?) returning id";
        Integer idTipoFinanciamento = retornaIdTipoFinanciamentoCasoExista(tipoFinanciamento.getCodigo(), conexao);
        try {
            if(VerificadorUtil.verificarSeObjetoNuloOuZero(idTipoFinanciamento)) {
                PreparedStatement ps = conexao.prepareStatement(sql);
                ps.setString(1, tipoFinanciamento.getCodigo());
                ps.setString(2, tipoFinanciamento.getNome());
                ResultSet rs = ps.executeQuery();
                if (rs.next())
                    idTipoFinanciamento = rs.getInt("id");
            }
        } catch (SQLException sqle) {
            conexao.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conexao.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
        return idTipoFinanciamento;
    }

    public Integer retornaIdTipoFinanciamentoCasoExista(String codigo, Connection conexao)
            throws SQLException, ProjetoException {
        String sql = "select id from sigtap.tipo_financiamento where codigo = ?";
        Integer idTipoFinanciamento = 0;
        try {
            PreparedStatement ps = conexao.prepareStatement(sql);

            ps.setString(1, codigo);
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                idTipoFinanciamento = rs.getInt("id");
        } catch (SQLException sqle) {
            conexao.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conexao.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
        return idTipoFinanciamento;
    }


    public void inserirListaDeGrupos(List<GrupoType> listaGrupos,Integer idHistoricoSigtap,  Connection conexao)
            throws ProjetoException, SQLException {
        try {
            for (GrupoType grupo : listaGrupos) {
                inserirGrupoMensal(grupo,idHistoricoSigtap,  conexao);
            }


        } catch (SQLException sqle) {
            conexao.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conexao.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
    }

    public void inserirListaDeSubGrupos(List<SubgrupoType> listaSubGrupos,Integer idHistoricoSigtap,  Connection conexao)
            throws ProjetoException, SQLException {
        try {
            for (SubgrupoType subGrupo : listaSubGrupos) {
                inserirSubgrupoMensal(subGrupo,idHistoricoSigtap,  conexao);
            }


        } catch (SQLException sqle) {
            conexao.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conexao.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
    }

    public void inserirRenases(List<RENASESType> listaRenases, Integer idProcedimentoMensal, Connection conexao)
            throws ProjetoException, SQLException {
        List<PropriedadeDeProcedimentoMensalExistenteDTO> todosRenasesExistentes = buscaRenasesExistentes(conexao);
        String sqlRenases = "INSERT INTO sigtap.renases (codigo, nome) "
                + "VALUES(?, ?) returning id";

        String sqlRenasesProcedimentoMensal = "INSERT INTO sigtap.renases_procedimento_mensal " +
                "(id_procedimento_mensal, id_renases) VALUES(?, ?);";
        List<Integer> listaIdRenasesExistentesDoProcedimento = new ArrayList<Integer>();
        List<RENASESType> listaRenasesAux = new ArrayList<RENASESType>();
        listaRenasesAux.addAll(listaRenases);
        filtraRenasesExistentes(listaRenases, todosRenasesExistentes, listaIdRenasesExistentesDoProcedimento, listaRenasesAux);
        try {
            PreparedStatement stm = null;
            for (RENASESType renases : listaRenases) {
                stm = conexao.prepareStatement(sqlRenases);
                stm.setString(1, renases.getCodigo());
                stm.setString(2, renases.getNome());
                ResultSet rs = stm.executeQuery();
                if(rs.next()) {
                    Integer idNovoRenases = rs.getInt("id");
                    stm = conexao.prepareStatement(sqlRenasesProcedimentoMensal);
                    stm.setInt(1, idProcedimentoMensal);
                    stm.setInt(2, idNovoRenases);
                    stm.executeUpdate();
                }
            }

            for (Integer idRenasesExistente : listaIdRenasesExistentesDoProcedimento) {
                stm = conexao.prepareStatement(sqlRenasesProcedimentoMensal);
                stm.setInt(1, idProcedimentoMensal);
                stm.setInt(2, idRenasesExistente);
                stm.executeUpdate();
            }
        } catch (SQLException sqle) {
            conexao.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conexao.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
    }

    private void filtraRenasesExistentes(List<RENASESType> listaRenases,
                                         List<PropriedadeDeProcedimentoMensalExistenteDTO> todosRenasesExistentes,
                                         List<Integer> listaIdRenasesExistentesDoProcedimento, List<RENASESType> listaRenasesAux) {
        for (RENASESType renases : listaRenasesAux) {
            for (PropriedadeDeProcedimentoMensalExistenteDTO renasesExistente : todosRenasesExistentes) {
                if(renases.getCodigo().equals(renasesExistente.getCodigo())) {
                    listaRenases.remove(renases);
                    listaIdRenasesExistentesDoProcedimento.add(renasesExistente.getId());
                }
            }
        }
    }

    public void  inserirFormaOrganizacao(List<FormaOrganizacaoType> listaFormaOrganizacao,Integer idHistoricoSigtap, Connection conexao) throws ProjetoException, SQLException {
        String sql = "INSERT INTO sigtap.forma_de_organizacao_mensal " +
                "(codigo, nome, id_subgrupo, id_grupo, id_historico_consumo_sigtap) " +
                "VALUES(?, ?, ? , ?, ?) returning id ";
        try {
            PreparedStatement ps = conexao.prepareStatement(sql);
            for(FormaOrganizacaoType formaOrganizacao : listaFormaOrganizacao) {
                ps.setString(1, formaOrganizacao.getCodigo());
                ps.setString(2, formaOrganizacao.getNome());
                Integer idSubgrupoExistente = retornaIdSubgrupoCasoExista(formaOrganizacao.getSubgrupo().getCodigo(),
                        idHistoricoSigtap,conexao);
                ps.setInt(3, idSubgrupoExistente);
                Integer idGrupoExistente = retornaIdGrupoCasoExista(formaOrganizacao.getSubgrupo().getGrupo().getCodigo(),
                        idHistoricoSigtap, conexao);
                ps.setInt(4, idGrupoExistente);

                ps.setInt(5, idHistoricoSigtap);

                ResultSet rs = ps.executeQuery();

            }

        } catch (SQLException sqle) {
            conexao.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conexao.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }

    }

    public Integer retornaIdSubgrupoCasoExista(String codigo,Integer idHistoricoSigtap, Connection conexao) throws SQLException, ProjetoException {
        String sql = "select id from sigtap.subgrupo_mensal where codigo = ? and id_historico_consumo_sigtap=?";
        Integer idSubgrupo = 0;
        try {
            PreparedStatement ps = conexao.prepareStatement(sql);

            ps.setString(1, codigo);
            ps.setInt(2, idHistoricoSigtap);
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                idSubgrupo = rs.getInt("id");
        }catch (SQLException sqle) {
            conexao.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conexao.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
        return idSubgrupo;
    }

    public Integer retornaIdGrupoCasoExista(String codigo, Integer idHistoricoSigtap,  Connection conexao) throws SQLException, ProjetoException {
        String sql = "select id from sigtap.grupo_mensal where codigo = ? and id_historico_consumo_sigtap=?";
        Integer idGrupo = 0;
        try {
            PreparedStatement ps = conexao.prepareStatement(sql);

            ps.setString(1, codigo);
            ps.setInt(2, idHistoricoSigtap);
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                idGrupo = rs.getInt("id");
        }catch (SQLException sqle) {
            conexao.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conexao.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
        return idGrupo;
    }

    public Integer inserirSubgrupoMensal(SubgrupoType subgrupo,Integer idHistoricoSigtap, Connection conexao) throws ProjetoException, SQLException {
        String sql = "INSERT INTO sigtap.subgrupo_mensal " +
                "(codigo, nome, id_grupo, id_historico_consumo_sigtap) " +
                "VALUES(?, ?, (select grupo_mensal.id from sigtap.grupo_mensal where grupo_mensal.codigo=? and grupo_mensal.id_historico_consumo_sigtap=? ), ?) returning id ";
        Integer idSubgrupo = null;
        try {
            PreparedStatement ps = conexao.prepareStatement(sql);

            ps.setString(1, subgrupo.getCodigo());
            ps.setString(2, subgrupo.getNome());
            ps.setString(3, subgrupo.getGrupo().getCodigo());
            ps.setInt(4, idHistoricoSigtap);
            ps.setInt(5, idHistoricoSigtap);
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                idSubgrupo = rs.getInt("id");
        } catch (SQLException sqle) {
            conexao.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conexao.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
        return idSubgrupo;
    }

    public Integer inserirGrupoMensal(GrupoType grupo,Integer idHistoricoSigtap, Connection conexao) throws ProjetoException, SQLException {
        String sql = "INSERT INTO sigtap.grupo_mensal " +
                "(codigo, nome, id_historico_consumo_sigtap) " +
                "VALUES(?, ?, ?) returning id ";
        Integer idGrupo = null;
        try {
            PreparedStatement ps = conexao.prepareStatement(sql);
            ps.setString(1, grupo.getCodigo());
            ps.setString(2, grupo.getNome());
            ps.setInt(3, idHistoricoSigtap);
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                idGrupo = rs.getInt("id");
        } catch (SQLException sqle) {
            conexao.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conexao.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
        return idGrupo;
    }

    public Integer inserirProcedimentoMensal(GravarProcedimentoMensalDTO procedimentoMensalDTO, Connection conexao, Integer idHistoricoConsumoSigtap)
            throws ProjetoException, SQLException {
        String sql = "INSERT INTO sigtap.procedimento_mensal " +
                "(id_procedimento, codigo_procedimento, nome, competencia_atual, complexidade, id_tipo_financiamento, "+
                "sexo, quantidade_maxima, idade_minima, unidade_idade_minima, idade_maxima, unidade_idade_maxima, "+
                "servico_ambulatorial, servico_hospitalar, servico_profisional, "+
                "descricao, id_historico_consumo_sigtap) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning id; ";

        Integer idProcedimentoMensal = null;
        try {
            PreparedStatement stm = conexao.prepareStatement(sql);
            stm.setInt(1, procedimentoMensalDTO.getIdProcedimento());
            stm.setString(2, procedimentoMensalDTO.getProcedimentoMensal().getCodigo());
            stm.setString(3, procedimentoMensalDTO.getProcedimentoMensal().getNome());
            stm.setString(4, procedimentoMensalDTO.getProcedimentoMensal().getCompetenciaValidade());
            stm.setString(5, procedimentoMensalDTO.getProcedimentoMensal().getComplexidade().name());


            Integer idTipoFinanciamento = inserirTipoFinanciamento(procedimentoMensalDTO.getProcedimentoMensal().getTipoFinanciamento(), conexao);
            stm.setInt(6, idTipoFinanciamento);

            stm.setString(7, procedimentoMensalDTO.getProcedimentoMensal().getSexoPermitido());

            if(VerificadorUtil.verificarSeObjetoNuloOuZero(procedimentoMensalDTO.getProcedimentoMensal().getQuantidadeMaxima()))
                stm.setNull(8, Types.NULL);
            else
                stm.setInt(8, procedimentoMensalDTO.getProcedimentoMensal().getQuantidadeMaxima());

            if ( VerificadorUtil.verificarSeObjetoNulo(procedimentoMensalDTO) || VerificadorUtil.verificarSeObjetoNulo(procedimentoMensalDTO.getProcedimentoMensal())
                    || VerificadorUtil.verificarSeObjetoNulo(procedimentoMensalDTO.getProcedimentoMensal().getIdadeMinimaPermitida())
                    || VerificadorUtil.verificarSeObjetoNulo(procedimentoMensalDTO.getProcedimentoMensal().getIdadeMinimaPermitida().getQuantidadeLimite())) {
                stm.setNull(9, Types.NULL);
            }
            else
                stm.setInt(9, procedimentoMensalDTO.getProcedimentoMensal().getIdadeMinimaPermitida().getQuantidadeLimite());

            if (VerificadorUtil.verificarSeObjetoNulo(procedimentoMensalDTO) || VerificadorUtil.verificarSeObjetoNulo(procedimentoMensalDTO.getProcedimentoMensal())
                    || VerificadorUtil.verificarSeObjetoNulo(procedimentoMensalDTO.getProcedimentoMensal().getIdadeMinimaPermitida())
                    || VerificadorUtil.verificarSeObjetoNulo(procedimentoMensalDTO.getProcedimentoMensal().getIdadeMinimaPermitida().getUnidadeLimite())) {
                stm.setNull(10, Types.NULL);
            }
            else
                stm.setString(10, procedimentoMensalDTO.getProcedimentoMensal().getIdadeMinimaPermitida().getUnidadeLimite().name());

            if ((VerificadorUtil.verificarSeObjetoNuloOuZero(procedimentoMensalDTO.getProcedimentoMensal().getIdadeMaximaPermitida()))
                    || (VerificadorUtil.verificarSeObjetoNuloOuZero(procedimentoMensalDTO.getProcedimentoMensal().getIdadeMaximaPermitida().getQuantidadeLimite()))) {
                stm.setNull(11, Types.NULL);
            }
            else
                stm.setInt(11, procedimentoMensalDTO.getProcedimentoMensal().getIdadeMaximaPermitida().getQuantidadeLimite());


            if ( VerificadorUtil.verificarSeObjetoNulo(procedimentoMensalDTO) || VerificadorUtil.verificarSeObjetoNulo(procedimentoMensalDTO.getProcedimentoMensal())
                    || VerificadorUtil.verificarSeObjetoNulo(procedimentoMensalDTO.getProcedimentoMensal().getIdadeMaximaPermitida())
                    || VerificadorUtil.verificarSeObjetoNulo(procedimentoMensalDTO.getProcedimentoMensal().getIdadeMaximaPermitida().getUnidadeLimite())) {
                stm.setNull(12, Types.NULL);
            }
            else
                stm.setString(12, procedimentoMensalDTO.getProcedimentoMensal().getIdadeMaximaPermitida().getUnidadeLimite().name());
            stm.setDouble(13, procedimentoMensalDTO.getProcedimentoMensal().getValorSA().doubleValue());
            stm.setDouble(14, procedimentoMensalDTO.getProcedimentoMensal().getValorSH().doubleValue());
            stm.setDouble(15, procedimentoMensalDTO.getProcedimentoMensal().getValorSP().doubleValue());
            stm.setString(16, procedimentoMensalDTO.getProcedimentoMensal().getDescricao());
            stm.setInt(17, idHistoricoConsumoSigtap);
            ResultSet rs = stm.executeQuery();
            if(rs.next())
                idProcedimentoMensal = rs.getInt("id");
        } catch (Exception ex) {
            conexao.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
        return idProcedimentoMensal;
    }

    public void inserirModalidadeAtendimento (List<ModalidadeAtendimentoType> listaModalidadesAtendimento,
                                              Integer idProcedimentoMensal, Connection conexao)
            throws ProjetoException, SQLException {
        List<PropriedadeDeProcedimentoMensalExistenteDTO> todasModalidadesAtendimentoExistentes =
                buscaModalidadeAtendimentoExistente(conexao);
        String sqlModalidadeAtendimento = "INSERT INTO sigtap.modalidade_atendimento " +
                "(codigo, nome) " +
                "VALUES(?, ?) returning id";

        String sqlModalidadeAtendimentoProcedimentoMensal =
                "INSERT INTO sigtap.modalidade_atendimento_procedimento_mensal " +
                        "(id_procedimento_mensal, id_modalidade_atendimento) " +
                        "VALUES(?, ?);";

        List<Integer> listaIdModalidadesAtendimentoExistentes = new ArrayList<Integer>();
        List<ModalidadeAtendimentoType> listaModalidadeAtendimentoAux = new ArrayList<ModalidadeAtendimentoType>();
        listaModalidadeAtendimentoAux.addAll(listaModalidadesAtendimento);
        filtraModalidadeAtendimentoExistentes(listaModalidadesAtendimento, todasModalidadesAtendimentoExistentes,
                listaIdModalidadesAtendimentoExistentes, listaModalidadeAtendimentoAux);

        try {
            PreparedStatement stm = null;
            for(ModalidadeAtendimentoType modalidadeAtendimentoType : listaModalidadesAtendimento) {
                stm = conexao.prepareStatement(sqlModalidadeAtendimento);
                stm.setString(1, modalidadeAtendimentoType.getCodigo());
                stm.setString(2, modalidadeAtendimentoType.getNome());
                ResultSet rs = stm.executeQuery();
                if(rs.next()) {
                    Integer idNovaModalidadeAtendimento = rs.getInt("id");
                    stm = conexao.prepareStatement(sqlModalidadeAtendimentoProcedimentoMensal);
                    stm.setInt(1, idProcedimentoMensal);
                    stm.setInt(2, idNovaModalidadeAtendimento);
                    stm.executeUpdate();
                }
            }

            for(Integer idModalidadeAtendimentoExistente : listaIdModalidadesAtendimentoExistentes) {
                stm = conexao.prepareStatement(sqlModalidadeAtendimentoProcedimentoMensal);
                stm.setInt(1, idProcedimentoMensal);
                stm.setInt(2, idModalidadeAtendimentoExistente);
                stm.executeUpdate();
            }
        } catch (SQLException sqle) {
            conexao.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conexao.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
    }

    private void filtraModalidadeAtendimentoExistentes(List<ModalidadeAtendimentoType> listaModalidadesAtendimento,
                                                       List<PropriedadeDeProcedimentoMensalExistenteDTO> todasModalidadesAtendimentoExistentes,
                                                       List<Integer> listaIdModalidadesAtendimentoExistentes, List<ModalidadeAtendimentoType> listaModalidadeAtendimentoAux) {
        for (ModalidadeAtendimentoType modalidadeAtendimento : listaModalidadeAtendimentoAux) {
            for (PropriedadeDeProcedimentoMensalExistenteDTO modalidadeExistente : todasModalidadesAtendimentoExistentes) {
                if(modalidadeAtendimento.getCodigo().equals(modalidadeExistente.getCodigo())) {
                    listaModalidadesAtendimento.remove(modalidadeAtendimento);
                    listaIdModalidadesAtendimentoExistentes.add(modalidadeExistente.getId());
                }
            }
        }
    }

    public void inserirInstrumentosRegistro
            (List<InstrumentoRegistroType> listaInstrumentosRegistro, Integer idProcedimentoMensal, Connection conexao)
            throws ProjetoException, SQLException {

        List<PropriedadeDeProcedimentoMensalExistenteDTO> todosInstrumentosRegistroExistentes = buscaInstrumentosRegistroExistente(conexao);
        String sqlInstrumentoRegistro = "INSERT INTO sigtap.instrumento_registro (codigo, nome) "
                + "VALUES(?, ?) returning id";

        String sqlInstrumentoRegistroProcedimentoMensal =
                "INSERT INTO sigtap.instrumento_registro_procedimento_mensal " +
                        "(id_procedimento_mensal, id_instrumento_registro) " +
                        "VALUES(?, ?); ";
        List<Integer> listaIdInstrumentosRegistroExistentes = new ArrayList<Integer>();
        List<InstrumentoRegistroType> listaInstrumentosRegistroAux = new ArrayList<InstrumentoRegistroType>();
        listaInstrumentosRegistroAux.addAll(listaInstrumentosRegistro);
        filtraInstrumentosRegistroExistentes(listaInstrumentosRegistro, todosInstrumentosRegistroExistentes,
                listaIdInstrumentosRegistroExistentes, listaInstrumentosRegistroAux);
        try {
            PreparedStatement stm = null;
            for (InstrumentoRegistroType instrumentoRegistroType : listaInstrumentosRegistro) {
                stm = conexao.prepareStatement(sqlInstrumentoRegistro);
                stm.setString(1, instrumentoRegistroType.getCodigo());
                stm.setString(2, instrumentoRegistroType.getNome());
                ResultSet rs = stm.executeQuery();
                if(rs.next()) {
                    Integer idNovoProcedimentoRegistro = rs.getInt("id");
                    stm = conexao.prepareStatement(sqlInstrumentoRegistroProcedimentoMensal);
                    stm.setInt(1, idProcedimentoMensal);
                    stm.setInt(2, idNovoProcedimentoRegistro);
                    stm.executeUpdate();
                }
            }

            for(Integer idInstrumentoRegistroExistente : listaIdInstrumentosRegistroExistentes) {
                stm = conexao.prepareStatement(sqlInstrumentoRegistroProcedimentoMensal);
                stm.setInt(1, idProcedimentoMensal);
                stm.setInt(2, idInstrumentoRegistroExistente);
                stm.executeUpdate();
            }
        }catch (SQLException sqle) {
            conexao.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conexao.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
    }

    private void filtraInstrumentosRegistroExistentes(List<InstrumentoRegistroType> listaInstrumentosRegistro,
                                                      List<PropriedadeDeProcedimentoMensalExistenteDTO> todosInstrumentosRegistroExistentes,
                                                      List<Integer> listaIdInstrumentosRegistroExistentesDoProcedimento, List<InstrumentoRegistroType> listaInstrumentosRegistroAux) {
        for (InstrumentoRegistroType instrumentoRegistro : listaInstrumentosRegistroAux) {
            for (PropriedadeDeProcedimentoMensalExistenteDTO instrumentoRegistroExistente : todosInstrumentosRegistroExistentes) {
                if(instrumentoRegistro.getCodigo().equals(instrumentoRegistroExistente.getCodigo())) {
                    listaInstrumentosRegistro.remove(instrumentoRegistro);
                    listaIdInstrumentosRegistroExistentesDoProcedimento.add(instrumentoRegistroExistente.getId());
                }
            }
        }
    }

    public void inserirCBOs(List<CBOType> listaCbos, Integer idProcedimentoMensal, Connection conexao)
            throws ProjetoException, SQLException {
        List<PropriedadeDeProcedimentoMensalExistenteDTO> todosCbosExistentes = buscaCbosExistentes(conexao);
        String sqlCbo = "INSERT INTO hosp.cbo (codigo, descricao) VALUES(?, ?) returning id";
        String sqlCboProcedimentoMensal = "INSERT INTO sigtap.cbo_procedimento_mensal " +
                "(id_procedimento_mensal, id_cbo) VALUES(?, ?);";

        List<Integer> listaIdCbosExistentesDoProcedimento = new ArrayList<Integer>();
        List<CBOType> listaCbosAux = new ArrayList<CBOType>();
        listaCbosAux.addAll(listaCbos);
        filtraCbosExistentes(listaCbos, todosCbosExistentes, listaIdCbosExistentesDoProcedimento, listaCbosAux);

        try {
            PreparedStatement stm = null;
            for(CBOType cboType : listaCbos) {
                stm = conexao.prepareStatement(sqlCbo);
                stm.setString(1, cboType.getCodigo().trim());
                stm.setString(2, cboType.getNome().trim());
                ResultSet rs = stm.executeQuery();
                if(rs.next()) {
                    Integer idNovoCbo = rs.getInt("id");
                    stm = conexao.prepareStatement(sqlCboProcedimentoMensal);
                    stm.setInt(1, idProcedimentoMensal);
                    stm.setInt(2, idNovoCbo);
                    stm.executeUpdate();
                }
            }

            for(Integer idCboExistente : listaIdCbosExistentesDoProcedimento) {
                stm = conexao.prepareStatement(sqlCboProcedimentoMensal);
                stm.setInt(1, idProcedimentoMensal);
                stm.setInt(2, idCboExistente);
                stm.executeUpdate();
            }
        } catch (SQLException sqle) {
            conexao.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conexao.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
    }

    private void filtraCbosExistentes(List<CBOType> listaCbos,
                                      List<PropriedadeDeProcedimentoMensalExistenteDTO> todosCbosExistentes,
                                      List<Integer> listaIdCbosExistentesDoProcedimento, List<CBOType> listaCbosAux) {
        for (CBOType cbo : listaCbosAux) {
            for (PropriedadeDeProcedimentoMensalExistenteDTO cboExistente : todosCbosExistentes) {
                if(cbo.getCodigo().equals(cboExistente.getCodigo())) {
                    listaCbos.remove(cbo);
                    listaIdCbosExistentesDoProcedimento.add(cboExistente.getId());
                }
            }
        }
    }

    public List<PropriedadeDeProcedimentoMensalExistenteDTO> buscaCbosExistentes(Connection con) throws ProjetoException {
        List<PropriedadeDeProcedimentoMensalExistenteDTO> listaCboExistente = new ArrayList();
        String sql = "SELECT cbo.id, cbo.codigo FROM hosp.cbo ";
        try {
            ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                PropriedadeDeProcedimentoMensalExistenteDTO cboExistenteDTO = new PropriedadeDeProcedimentoMensalExistenteDTO();
                cboExistenteDTO.setId(rs.getInt("id"));
                cboExistenteDTO.setCodigo(rs.getString("codigo"));
                listaCboExistente.add(cboExistenteDTO);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return listaCboExistente;
    }

    public void inserirCIDs(List<CIDVinculado> listaCidVinculado, Integer idProcedimentoMensal, Connection conexao)
            throws ProjetoException, SQLException {
        List<PropriedadeDeProcedimentoMensalExistenteDTO> todosCidsExistentes = buscaCidsExistentes(conexao);

        String sqlCid = "INSERT INTO hosp.cid (cid, desccid) VALUES(?, ?) returning cod;";

        String sqlCidProcedimentoMensal = "INSERT INTO sigtap.cid_procedimento_mensal (id_procedimento_mensal, id_cid) " +
                "VALUES(?, ?);";
        List<Integer> listaIdCidsExistentesDoProcedimento = new ArrayList<Integer>();
        List<CIDVinculado> listaCidAux = new ArrayList<CIDVinculado>();
        listaCidAux.addAll(listaCidVinculado);
        filtraCidsExistentes(listaCidVinculado, todosCidsExistentes, listaIdCidsExistentesDoProcedimento, listaCidAux);
        try {
            PreparedStatement stm = null;
            for(CIDVinculado cidVinculado : listaCidVinculado) {
                stm = conexao.prepareStatement(sqlCid);
                stm.setString(1, cidVinculado.getCID().getCodigo().trim());
                stm.setString(2, cidVinculado.getCID().getNome());
                ResultSet rs = stm.executeQuery();
                if(rs.next()) {
                    Integer idNovoCid = rs.getInt("cod");
                    stm = conexao.prepareStatement(sqlCidProcedimentoMensal);
                    stm.setInt(1, idProcedimentoMensal);
                    stm.setInt(2, idNovoCid);
                    stm.executeUpdate();
                }
            }

            for(Integer idCidExistente : listaIdCidsExistentesDoProcedimento) {
                stm = conexao.prepareStatement(sqlCidProcedimentoMensal);
                stm.setInt(1, idProcedimentoMensal);
                stm.setInt(2, idCidExistente);
                stm.executeUpdate();
            }
        } catch (SQLException sqle) {
            conexao.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conexao.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
    }

    private void filtraCidsExistentes(List<CIDVinculado> listaCids,
                                      List<PropriedadeDeProcedimentoMensalExistenteDTO> todosCidsExistentes,
                                      List<Integer> listaIdCidsExistentesDoProcedimento, List<CIDVinculado> listaCidsAux) {

        for (CIDVinculado cid : listaCidsAux) {
            for (PropriedadeDeProcedimentoMensalExistenteDTO cidExistente : todosCidsExistentes) {
                if(cid.getCID().getCodigo().trim().equals(cidExistente.getCodigo().trim())) {
                    listaCids.remove(cid);
                    listaIdCidsExistentesDoProcedimento.add(cidExistente.getId());
                }
            }
        }
    }

    public void inserirServicoClassificacao
            (List<ServicoClassificacaoType> servicosClassificacoesType, Integer idProcedimentoMensal, Connection conexao) throws ProjetoException, SQLException {

        String sqlVerificaServico = "select id as id_servico from sigtap.servico where codigo = ?";

        String sqlServicoClassificacao = "INSERT INTO sigtap.servico_classificacao_mensal " +
                "(id_servico, id_classificacao, id_procedimento_mensal) " +
                "VALUES(?, ?, ?);";

        String sqlVerificaClassificacao =
                "select scm.id_classificacao " +
                        "from sigtap.servico_classificacao_mensal scm " +
                        "join sigtap.classificacao cm on scm.id_classificacao = cm.id " +
                        "where scm.id_servico = ? and cm.codigo = ?";

        try {
            PreparedStatement stm = null;
            for(ServicoClassificacaoType servicoClassificacaoType : servicosClassificacoesType) {
                Integer idServico;
                Integer idClassificacao;
                stm = conexao.prepareStatement(sqlVerificaServico);
                stm.setString(1, servicoClassificacaoType.getServico().getCodigo());
                ResultSet rs = stm.executeQuery();
                if(rs.next())
                    idServico = rs.getInt("id_servico");
                else
                    idServico = inserirServico(servicoClassificacaoType.getServico(), conexao);

                stm = conexao.prepareStatement(sqlVerificaClassificacao);
                stm.setInt(1, idServico);
                stm.setString(2, servicoClassificacaoType.getCodigoClassificacao());
                rs = stm.executeQuery();
                if(rs.next())
                    idClassificacao = rs.getInt("id_classificacao");
                else {
                    idClassificacao = inserirClassificacao
                            (servicoClassificacaoType.getCodigoClassificacao(), servicoClassificacaoType.getNomeClassificacao(), conexao);
                }

                stm = conexao.prepareStatement(sqlServicoClassificacao);
                stm.setLong(1, idServico);
                stm.setLong(2, idClassificacao);
                stm.setInt(3, idProcedimentoMensal);
                stm.executeUpdate();
            }
        } catch (SQLException sqle) {
            conexao.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conexao.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
    }

    public Integer inserirServico(ServicoType servicoType, Connection conexao) throws ProjetoException, SQLException {
        String sql = "INSERT INTO sigtap.servico " +
                "(codigo, nome) VALUES(?, ?) returning id; ";
        Integer idServico = null;
        try {
            PreparedStatement ps = conexao.prepareStatement(sql);
            ps.setString(1, servicoType.getCodigo());
            ps.setString(2, servicoType.getNome());
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                idServico = rs.getInt("id");
        } catch (SQLException sqle) {
            conexao.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conexao.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
        return idServico;
    }

    public Integer inserirClassificacao(String codigo, String nome, Connection conexao) throws ProjetoException, SQLException {
        String sql = "INSERT INTO sigtap.classificacao " +
                "(codigo, nome) VALUES(?, ?) returning id; ";
        Integer idClassificacao = null;
        try {
            PreparedStatement ps = conexao.prepareStatement(sql);
            ps.setString(1, codigo);
            ps.setString(2, nome);
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                idClassificacao = rs.getInt("id");
        } catch (SQLException sqle) {
            conexao.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conexao.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
        return idClassificacao;
    }


    public Integer gravarHistoricoConsumoSigtap(Long idFuncionario,Integer mes, Integer ano,  Connection conexao)
            throws ProjetoException, SQLException {
        Integer idHistorico = null;
        String sql = "INSERT INTO sigtap.historico_consumo_sigtap " +
                "(mes, ano, data_registro, id_funcionario) " +
                "VALUES(?, ?, current_timestamp, ?) returning id;";


        try {
            PreparedStatement stm = conexao.prepareStatement(sql);
            stm.setInt(1, mes);
            stm.setInt(2, ano);
            stm.setLong(3, idFuncionario);
            ResultSet rs = stm.executeQuery();
            if(rs.next())
                idHistorico = rs.getInt("id");
        } catch (Exception ex) {
            conexao.rollback();
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return idHistorico;
    }

    public void desatualizaStatusHistoricoConsumoSigtapAnterior(Integer mes, Integer ano,  Connection conexao)
            throws ProjetoException, SQLException {

        String sql = "UPDATE sigtap.historico_consumo_sigtap SET status='D' WHERE status='A' and mes=? and ano=? ";
        try {
            PreparedStatement stm = conexao.prepareStatement(sql);
            stm.setInt(1, mes);
            stm.setInt(2, ano);
            stm.executeUpdate();
        } catch (Exception ex) {
            conexao.rollback();
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }


    public Boolean houveCargaDoSigtapEsteMes(Integer mes, Integer ano) throws ProjetoException {

        String sql = "select exists (select id from sigtap.historico_consumo_sigtap " +
                "where status = 'A' and mes = ? and ano = ? ) houve_carga_este_mes";

        Boolean houveCargaDoSigtap = false;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, mes);
            stm.setInt(2, ano);
            ResultSet rs = stm.executeQuery();

            if (rs.next())
                houveCargaDoSigtap = rs.getBoolean("houve_carga_este_mes");

        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return houveCargaDoSigtap;
    }

    public List<HistoricoSigtapBean> listaHistoricoCargasDoSigtap() throws ProjetoException {

        List<HistoricoSigtapBean> listaHistoricosSigtap = new ArrayList();
        String sql = "select hcs.data_registro, hcs.ano, " +
                "to_char(to_timestamp (hcs.mes::text, 'MM'), 'TMMONTH') as mes " +
                "from sigtap.historico_consumo_sigtap hcs " +
                "order by hcs.data_registro desc";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                HistoricoSigtapBean historicoSigtap = new HistoricoSigtapBean();
                historicoSigtap.setAno(rs.getInt("ano"));
                historicoSigtap.setMesString(rs.getString("mes"));
                historicoSigtap.setDataIhHoraRegistro(rs.getTimestamp("data_registro"));
                listaHistoricosSigtap.add(historicoSigtap);
            }
        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return listaHistoricosSigtap;
    }


    public ProcedimentoType buscaDadosProcedimentoMensal(String codigoProcedimento, Integer ano, Integer mes) throws ProjetoException {

        ProcedimentoType procedimento = new ProcedimentoType();
        try {
            this.con = ConnectionFactory.getConnection();

            procedimento = buscaProcedimentoMensal(ano, mes, codigoProcedimento, con);

            CIDsVinculados cidVinculados = new CIDsVinculados();
            cidVinculados.getCIDVinculado().addAll(listaCidsProcedimentoMensal(ano, mes, codigoProcedimento, con));
            procedimento.setCIDsVinculados(cidVinculados);

            CBOsVinculados cbosVinculados = new CBOsVinculados();
            cbosVinculados.getCBO().addAll(listaCbosProcedimentoMensal(ano, mes, codigoProcedimento, con));
            procedimento.setCBOsVinculados(cbosVinculados);

            ServicosClassificacoesVinculados servicosClassificacoesVinculados = new ServicosClassificacoesVinculados();
            servicosClassificacoesVinculados.getServicoClassificacao()
                    .addAll(listaServicoClassificacaoProcedimentoMensal(ano, mes, codigoProcedimento, con));
            procedimento.setServicosClassificacoesVinculados(servicosClassificacoesVinculados);

            ModalidadesAtendimento modalidadesAtendimento = new ModalidadesAtendimento();
            modalidadesAtendimento.getModalidadeAtendimento()
                    .addAll(listaModalidadeAtendimentoProcedimentoMensal(ano, mes, codigoProcedimento, con));
            procedimento.setModalidadesAtendimento(modalidadesAtendimento);

            InstrumentosRegistro instrumentosRegistro = new InstrumentosRegistro();
            instrumentosRegistro.getInstrumentoRegistro()
                    .addAll(listaInstrumentoRegistroProcedimentoMensal(ano, mes, codigoProcedimento, con));
            procedimento.setInstrumentosRegistro(instrumentosRegistro);

            RENASESVinculadas renasesVinculadas = new RENASESVinculadas();
            renasesVinculadas.getRENASES()
                    .addAll(listaRenasesProcedimentoMensal(ano, mes, codigoProcedimento, con));
            procedimento.setRENASESVinculadas(renasesVinculadas);

        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        }finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return procedimento;
    }


    private ProcedimentoType buscaProcedimentoMensal(Integer ano, Integer mes, String codigoProcedimento, Connection conexao)
            throws SQLException, ProjetoException {

        ProcedimentoType procedimentoMensal = new ProcedimentoType();
        String sql = "select distinct \n" +
                "\tpm.nome,\n" +
                "\tpm.descricao,\n" +
                "\tpm.codigo_procedimento,\n" +
                "\tpm.competencia_atual ,\n" +
                "\tpm.complexidade,\n" +
                "\tpm.sexo,\n" +
                "\tpm.quantidade_maxima,\n" +
                "\tpm.idade_minima,\n" +
                "\tpm.unidade_idade_minima,\n" +
                "\tpm.idade_maxima,\n" +
                "\tpm.unidade_idade_maxima,\n" +
                "\tpm.servico_ambulatorial,\n" +
                "\tpm.servico_hospitalar,\n" +
                "\tpm.servico_profisional,\n" +
                "\ttp.codigo as codigo_tipo_financiamento,\n" +
                "\ttp.nome as tipo_financiamento,\n" +
                "\tfo.codigo as codigo_forma_organizacao,\n" +
                "\tfo.nome as forma_organizacao,\n" +
                "\tsm.codigo as codigo_subgrupo,\n" +
                "\tsm.nome as subgrupo,\n" +
                "\tgm.codigo as codigo_grupo,\n" +
                "\tgm.nome as grupo\n" +
                "from\n" +
                "\tsigtap.procedimento_mensal pm\n" +
                "join sigtap.historico_consumo_sigtap hc on\n" +
                "\thc.id = pm.id_historico_consumo_sigtap left join  sigtap.tipo_financiamento tp on\n" +
                "\ttp.id = pm.id_tipo_financiamento left join sigtap.forma_de_organizacao_mensal fo on\n" +
                "\tfo.id_historico_consumo_sigtap = hc.id \n" +
                "join sigtap.subgrupo_mensal  sm on\n" +
                "\tsm.id = fo.id_subgrupo and sm.id_historico_consumo_sigtap = hc.id \n" +
                "join sigtap.grupo_mensal  gm on\n" +
                "\tgm.id = sm.id_grupo and gm.id = fo.id_grupo and gm.id_historico_consumo_sigtap = hc.id\n" +
                "where\n" +
                "\thc.ano=?\n" +
                "\tand hc.mes=?\n" +
                "\tand hc.status ='A'\n" +
                "\tand pm.codigo_procedimento =?\n" +
                "\tand gm.codigo =trim(substring(pm.codigo_procedimento,1,2))\n" +
                "\tand sm.codigo =trim(substring(pm.codigo_procedimento,3,2)) \n" +
                "\tand fo.codigo =trim(substring(pm.codigo_procedimento,5,2))";

        try {
            PreparedStatement stm = conexao.prepareStatement(sql);
            stm.setInt(1, ano);
            stm.setInt(2, mes);
            stm.setString(3, codigoProcedimento);
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                procedimentoMensal.setNome(rs.getString("nome"));
                procedimentoMensal.setDescricao(rs.getString("descricao"));
                procedimentoMensal.setCodigo(rs.getString("codigo_procedimento"));
                procedimentoMensal.setCompetenciaInicial(rs.getString("competencia_atual"));

                String complexidade = rs.getString("complexidade");
                if(complexidade.equals(ComplexidadeType.ALTA_COMPLEXIDADE_OU_CUSTO.name()))
                    procedimentoMensal.setComplexidade(ComplexidadeType.ALTA_COMPLEXIDADE_OU_CUSTO);

                else if(complexidade.equals(ComplexidadeType.ATENCAO_BASICA.name()))
                    procedimentoMensal.setComplexidade(ComplexidadeType.ATENCAO_BASICA);

                else if(complexidade.equals(ComplexidadeType.MEDIA_COMPLEXIDADE.name()))
                    procedimentoMensal.setComplexidade(ComplexidadeType.MEDIA_COMPLEXIDADE);

                else
                    procedimentoMensal.setComplexidade(ComplexidadeType.NAO_SE_APLICA);

                procedimentoMensal.setSexoPermitido(rs.getString("sexo"));
                procedimentoMensal.setQuantidadeMaxima(rs.getInt("quantidade_maxima"));


                IdadeLimiteType idadeMinimaPermitida = new IdadeLimiteType();
                idadeMinimaPermitida.setQuantidadeLimite(rs.getInt("idade_minima")/12);
               /* String unidadeLimiteIdadeMinima = rs.getString("unidade_idade_minima");
                if(unidadeLimiteIdadeMinima.equals(UnidadeLimiteType.MESES.name()))
                    idadeMinimaPermitida.setUnidadeLimite(UnidadeLimiteType.MESES);
                else
                    idadeMinimaPermitida.setUnidadeLimite(UnidadeLimiteType.ANOS);*/
                procedimentoMensal.setIdadeMinimaPermitida(idadeMinimaPermitida);;

                IdadeLimiteType idadeMaximaPermitida = new IdadeLimiteType();
                idadeMaximaPermitida.setQuantidadeLimite(rs.getInt("idade_maxima")/12);
               /* String unidadeLimiteIdadeMaxima = rs.getString("unidade_idade_maxima");
                if(unidadeLimiteIdadeMaxima.equals(UnidadeLimiteType.MESES.name()))
                    idadeMaximaPermitida.setUnidadeLimite(UnidadeLimiteType.MESES);
                else
                    idadeMaximaPermitida.setUnidadeLimite(UnidadeLimiteType.ANOS);*/
                procedimentoMensal.setIdadeMaximaPermitida(idadeMaximaPermitida);

                procedimentoMensal.setValorSA(rs.getBigDecimal("servico_ambulatorial"));
                procedimentoMensal.setValorSH(rs.getBigDecimal("servico_hospitalar"));
                procedimentoMensal.setValorSP(rs.getBigDecimal("servico_profisional"));

                TipoFinanciamentoType tipoFinanciamento = new TipoFinanciamentoType();
                tipoFinanciamento.setNome(rs.getString("tipo_financiamento"));
                tipoFinanciamento.setCodigo(rs.getString("codigo_tipo_financiamento"));

                procedimentoMensal.setTipoFinanciamento(tipoFinanciamento);

                FormaOrganizacaoType formaOrganizacao = new FormaOrganizacaoType();
                formaOrganizacao.setCodigo(rs.getString("codigo_forma_organizacao"));
                formaOrganizacao.setNome(rs.getString("forma_organizacao"));

                SubgrupoType subgrupo = new SubgrupoType();
                subgrupo.setCodigo(rs.getString("codigo_subgrupo"));
                subgrupo.setNome(rs.getString("subgrupo"));

                GrupoType grupo = new GrupoType();
                grupo.setCodigo(rs.getString("codigo_grupo"));
                grupo.setNome(rs.getString("grupo"));

                subgrupo.setGrupo(grupo);
                formaOrganizacao.setSubgrupo(subgrupo);
                procedimentoMensal.setFormaOrganizacao(formaOrganizacao);
            }
        } catch (SQLException sqle) {
            conexao.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conexao.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
        return procedimentoMensal;
    }


    private List<CIDVinculado> listaCidsProcedimentoMensal
            (Integer ano, Integer mes, String codigoProcedimento, Connection conexao)
            throws SQLException, ProjetoException {

        List<CIDVinculado> listaCidsVinculados = new ArrayList();
        String sql = "select cm.cid codigo, cm.desccid nome \n" +
                "from hosp.cid cm join sigtap.cid_procedimento_mensal cpm on cm.cod = cpm.id_cid \n" +
                "join sigtap.procedimento_mensal pm on pm.id = cpm.id_procedimento_mensal \n" +
                "join sigtap.historico_consumo_sigtap hc on hc.id = pm.id_historico_consumo_sigtap \n" +
                "where hc.id = (select id from sigtap.historico_consumo_sigtap hcs where hcs.ano = ? and hcs.mes = ? \n" +
                "\t\t   order by id desc limit 1) \n" +
                "and pm.codigo_procedimento = ? \n" +
                "order by cm.cid ";

        try {
            PreparedStatement stm = conexao.prepareStatement(sql);
            stm.setInt(1, ano);
            stm.setInt(2, mes);
            stm.setString(3, codigoProcedimento);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                CIDVinculado cidVinculado = new CIDVinculado();
                CIDType cid = new CIDType();
                cid.setCodigo(rs.getString("codigo"));
                cid.setNome(rs.getString("nome"));
/*
                String agravo = rs.getString("agravo");
                if(agravo.equals(AgravoType.AGRAVO_BLOQUEIO.name()))
                    cid.setAgravo(AgravoType.AGRAVO_BLOQUEIO);

                else if(agravo.equals(AgravoType.AGRAVO_NOTIFICACAO.name()))
                    cid.setAgravo(AgravoType.AGRAVO_NOTIFICACAO);
                else
                    cid.setAgravo(AgravoType.SEM_AGRAVO);

                cid.setSexoAplicavel(rs.getString("sexo_aplicavel"));
                cid.setEstadio(rs.getBoolean("estadio"));
                cid.setQuantidadeCamposIrradiados(rs.getInt("quantidade_campos_irradiados"));*/
                cidVinculado.setCID(cid);
                listaCidsVinculados.add(cidVinculado);
            }
        } catch (SQLException sqle) {
            conexao.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conexao.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
        return listaCidsVinculados;
    }



    private List<CBOType> listaCbosProcedimentoMensal
            (Integer ano, Integer mes, String codigoProcedimento, Connection conexao) throws SQLException, ProjetoException {

        List<CBOType> listaCbos = new ArrayList();
        String sql = "select cbo.codigo, cbo.descricao nome \n" +
                "from hosp.cbo join sigtap.cbo_procedimento_mensal cpm on cbo.id = cpm.id_cbo \n" +
                "join sigtap.procedimento_mensal pm on pm.id = cpm.id_procedimento_mensal \n" +
                "join sigtap.historico_consumo_sigtap hc on hc.id = pm.id_historico_consumo_sigtap \n" +
                "where hc.id = (select id from sigtap.historico_consumo_sigtap hcs where hcs.ano = ? and hcs.mes = ? " +
                "\t\t   order by id desc limit 1) \n" +
                "and pm.codigo_procedimento = ? ";

        try {
            PreparedStatement stm = conexao.prepareStatement(sql);
            stm.setInt(1, ano);
            stm.setInt(2, mes);
            stm.setString(3, codigoProcedimento);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                CBOType cbo = new CBOType();
                cbo.setCodigo(rs.getString("codigo"));
                cbo.setNome(rs.getString("nome"));

                listaCbos.add(cbo);
            }
        } catch (SQLException sqle) {
            conexao.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conexao.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
        return listaCbos;
    }

    private List<ServicoClassificacaoType> listaServicoClassificacaoProcedimentoMensal
            (Integer ano, Integer mes, String codigoProcedimento, Connection conexao) throws SQLException, ProjetoException {

        List<ServicoClassificacaoType> listaServicoClassificacao = new ArrayList();
        String sql = "select sm.codigo as codigo_servico, sm.nome as servico, \n" +
                "cm.codigo as codigo_classificacao, cm.nome as classificacao \n" +
                "from sigtap.servico sm join sigtap.servico_classificacao_mensal scm on sm.id = scm.id_servico \n" +
                "join sigtap.classificacao cm on cm.id = scm.id_classificacao \n" +
                "join sigtap.procedimento_mensal pm on pm.id = scm.id_procedimento_mensal \n" +
                "join sigtap.historico_consumo_sigtap hc on hc.id = pm.id_historico_consumo_sigtap \n" +
                "where hc.id = (select id from sigtap.historico_consumo_sigtap hcs where hcs.ano = ? and hcs.mes = ? " +
                "\t\t   order by id desc limit 1) \n" +
                "and pm.codigo_procedimento =  ? ";

        try {
            PreparedStatement stm = conexao.prepareStatement(sql);
            stm.setInt(1, ano);
            stm.setInt(2, mes);
            stm.setString(3, codigoProcedimento);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                ServicoClassificacaoType servicoClassificacao = new ServicoClassificacaoType();
                ServicoType servico = new ServicoType();
                servico.setCodigo(rs.getString("codigo_servico"));
                servico.setNome(rs.getString("servico"));

                servicoClassificacao.setServico(servico);
                servicoClassificacao.setCodigoClassificacao(rs.getString("codigo_classificacao"));
                servicoClassificacao.setNomeClassificacao(rs.getString("classificacao"));

                listaServicoClassificacao.add(servicoClassificacao);
            }
        } catch (SQLException sqle) {
            conexao.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conexao.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
        return listaServicoClassificacao;
    }

    private List<ModalidadeAtendimentoType> listaModalidadeAtendimentoProcedimentoMensal
            (Integer ano, Integer mes, String codigoProcedimento, Connection conexao) throws SQLException, ProjetoException {

        List<ModalidadeAtendimentoType> listaModalidadeAtendimento = new ArrayList();
        String sql = "select ma.codigo, ma.nome " +
                "from sigtap.modalidade_atendimento ma " +
                "join sigtap.modalidade_atendimento_procedimento_mensal mapm on mapm.id_modalidade_atendimento = ma.id " +
                "join sigtap.procedimento_mensal pm on pm.id = mapm.id_procedimento_mensal " +
                "join sigtap.historico_consumo_sigtap hc on hc.id = pm.id_historico_consumo_sigtap " +
                "where hc.id = (select id from sigtap.historico_consumo_sigtap hcs where hcs.ano = ? and hcs.mes = ? " +
                "		   order by id desc limit 1) " +
                "and pm.codigo_procedimento = ?;";

        try {
            PreparedStatement stm = conexao.prepareStatement(sql);
            stm.setInt(1, ano);
            stm.setInt(2, mes);
            stm.setString(3, codigoProcedimento);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                ModalidadeAtendimentoType modalidadeAtendimento = new ModalidadeAtendimentoType();
                modalidadeAtendimento.setCodigo(rs.getString("codigo"));
                modalidadeAtendimento.setNome(rs.getString("nome"));

                listaModalidadeAtendimento.add(modalidadeAtendimento);
            }
        } catch (SQLException sqle) {
            conexao.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conexao.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
        return listaModalidadeAtendimento;
    }

    private List<InstrumentoRegistroType> listaInstrumentoRegistroProcedimentoMensal
            (Integer ano, Integer mes, String codigoProcedimento, Connection conexao) throws SQLException, ProjetoException {

        List<InstrumentoRegistroType> listaInstrumentoRegistro = new ArrayList();
        String sql = "select ir.codigo, ir.nome " +
                "from sigtap.instrumento_registro ir join sigtap.instrumento_registro_procedimento_mensal irm on ir.id = irm.id_instrumento_registro " +
                "join sigtap.procedimento_mensal pm on pm.id = irm.id_procedimento_mensal " +
                "join sigtap.historico_consumo_sigtap hc on hc.id = pm.id_historico_consumo_sigtap " +
                "where hc.id = (select id from sigtap.historico_consumo_sigtap hcs where hcs.ano = ? and hcs.mes = ? " +
                "		   order by id desc limit 1) " +
                "and pm.codigo_procedimento = ?;";

        try {
            PreparedStatement stm = conexao.prepareStatement(sql);
            stm.setInt(1, ano);
            stm.setInt(2, mes);
            stm.setString(3, codigoProcedimento);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                InstrumentoRegistroType instrumentoRegistro = new InstrumentoRegistroType();
                instrumentoRegistro.setCodigo(rs.getString("codigo"));
                instrumentoRegistro.setNome(rs.getString("nome"));

                listaInstrumentoRegistro.add(instrumentoRegistro);
            }
        } catch (SQLException sqle) {
            conexao.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conexao.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
        return listaInstrumentoRegistro;
    }

    private List<RENASESType> listaRenasesProcedimentoMensal
            (Integer ano, Integer mes, String codigoProcedimento, Connection conexao) throws SQLException, ProjetoException {

        List<RENASESType> listaRenases = new ArrayList();
        String sql = "select rm.codigo, rm.nome \n" +
                "from sigtap.renases  rm join sigtap.renases_procedimento_mensal rpm on rm.id = rpm.id_renases \n" +
                "join sigtap.procedimento_mensal pm on pm.id = rpm.id_procedimento_mensal \n" +
                "join sigtap.historico_consumo_sigtap hc on hc.id = pm.id_historico_consumo_sigtap \n" +
                "where hc.id = (select id from sigtap.historico_consumo_sigtap hcs where hcs.ano = ? and hcs.mes = ? \n" +
                "\t\t   order by id desc limit 1) \n" +
                "and pm.codigo_procedimento = ? ";

        try {
            PreparedStatement stm = conexao.prepareStatement(sql);
            stm.setInt(1, ano);
            stm.setInt(2, mes);
            stm.setString(3, codigoProcedimento);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                RENASESType renases = new RENASESType();
                renases.setCodigo(rs.getString("codigo"));
                renases.setNome(rs.getString("nome"));

                listaRenases.add(renases);
            }
        } catch (SQLException sqle) {
            conexao.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conexao.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
        return listaRenases;
    }

    public List<String> listaMesesIhAnosDoHistorico() throws ProjetoException, SQLException {

        List<String> listaMesIhAno = new ArrayList();
        String sql = "select distinct concat (lpad(trim(to_char(mes,'99')), 2, '0'),'/', ano) \n" +
                "as mes_ano,\n" +
                "concat (ano,'/',lpad(trim(to_char(mes,'99')), 2, '0')) as ano_mes\n" +
                "from sigtap.historico_consumo_sigtap hcs order by ano_mes desc";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                String mesIhAno = rs.getString("mes_ano");
                listaMesIhAno.add(mesIhAno);
            }
        } catch (SQLException sqle) {
            con.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            con.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return listaMesIhAno;
    }

    public Integer retornaMaiorAnoCargaSigtap() throws ProjetoException, SQLException {

        Integer ano = null;
        String sql = "select max(ano) ano\n" +
                "from sigtap.historico_consumo_sigtap hcs";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                ano = rs.getInt("ano");
            }
        } catch (SQLException sqle) {
            con.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            con.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return ano;
    }

    public Integer retornaMaiorMêsNoUltimoAnoCargaSigtap() throws SQLException, ProjetoException {

        Integer mes = null;
        String sql = "select max(hcs.mes) mes\n" +
                "from sigtap.historico_consumo_sigtap hcs\n" +
                "where hcs.ano = (select max(hcs2.ano) ano\n" +
                "from sigtap.historico_consumo_sigtap hcs2)";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                mes = rs.getInt("mes");
            }
        } catch (SQLException sqle) {
            con.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            con.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return mes;
    }

    public boolean validaCboProfissionalParaProcedimento(Integer idProcedimento, Long idFuncionario, Date dataSolicitacao) throws ProjetoException {

        String sql = "select exists (\n" +
                "\tselect\n" +
                "\t\tdistinct  f.descfuncionario, cbo.codigo\n" +
                "\tfrom sigtap.procedimento_mensal pm \n" +
                "join sigtap.historico_consumo_sigtap hc on\n" +
                "\t\t\thc.id = pm.id_historico_consumo_sigtap\t\t\n" +
                "\tjoin sigtap.cbo_procedimento_mensal cpm on\n" +
                "\t\tcpm.id_procedimento_mensal =  pm.id\n" +
                "\tjoin hosp.cbo on\n" +
                "\t\tcpm.id_cbo = cbo.id\t\n" +
                "\tjoin hosp.proc on\n" +
                "\t\tproc.id = pm.id_procedimento\n" +
                "\t\tjoin acl.funcionarios f on f.codcbo  = cpm.id_cbo\n" +
                "\t where proc.ativo = 'S' and \n" +
                "\t\t\tpm.id_procedimento = ?\n" +
                "\t\t\tand hc.status = 'A'\n" +
                "\t\t\tand (hc.ano = ?\n" +
                "\t\t\tand hc.mes = ?)\n" +
                "\t\t\tand f.id_funcionario = ?\n" +
                ") ehvalido";
        boolean ehValido = false;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1,  idProcedimento);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dataSolicitacao);
            stm.setInt(2, calendar.get(Calendar.YEAR));
            stm.setInt(3, calendar.get(Calendar.MONTH)+1);
            stm.setLong(4,  idFuncionario);
            ResultSet rs = stm.executeQuery();
            if(rs.next())
                ehValido = rs.getBoolean("ehvalido");
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
        return ehValido;
    }

    public boolean validaCidParaProcedimento(Integer idProcedimento, String codigoCid, Date dataSolicitacao) throws ProjetoException {

        String sql = "select exists (select c.cid from " +
                "hosp.cid c join sigtap.cid_procedimento_mensal cpm on cpm.id_cid = c.cod " +
                "join sigtap.procedimento_mensal pm on pm.id = cpm.id_procedimento_mensal " +
                "join hosp.proc on proc.id = pm.id_procedimento " +
                "where pm.id_procedimento = " +
                "	(select id_procedimento from sigtap.procedimento_mensal pm2 " +
                "	join sigtap.historico_consumo_sigtap hc on hc.id = pm2.id_historico_consumo_sigtap " +
                "	where pm2.id_procedimento = ? and hc.status = 'A' and (hc.ano = ? and hc.mes = ?) \t order by pm2.id desc limit 1 ) "+
                "and c.cid = ? and proc.ativo = 'S' ) ehvalido";
        boolean ehValido = false;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1,  idProcedimento);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dataSolicitacao);
            stm.setInt(2, calendar.get(Calendar.YEAR));
            stm.setInt(3, calendar.get(Calendar.MONTH)+1);
            stm.setString(4, codigoCid);

            ResultSet rs = stm.executeQuery();
            if(rs.next())
                ehValido = rs.getBoolean("ehvalido");
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
        return ehValido;
    }

    public boolean validaIdadePacienteParaProcedimento(Integer idProcedimento, Integer idPaciente, Date dataSolicitacao)
            throws ProjetoException {

        String sql = "select exists (select p.id_paciente " +
                "from hosp.pacientes p where p.id_paciente = ?" +
                "and extract (year from age(current_date, p.dtanascimento))  >=  (select pm.idade_minima from sigtap.procedimento_mensal pm " +
                "	join sigtap.historico_consumo_sigtap hc on hc.id = pm.id_historico_consumo_sigtap " +
                "	where pm.id_procedimento = ? and hc.status = 'A' and (hc.ano = ? and hc.mes = ?) \t order by pm.id desc limit 1 ) " +
                "	" +
                "and extract (year from age(current_date, p.dtanascimento))  <=  (select pm.idade_maxima from sigtap.procedimento_mensal pm " +
                "	join sigtap.historico_consumo_sigtap hc on hc.id = pm.id_historico_consumo_sigtap " +
                "	where pm.id_procedimento = ? and hc.status = 'A'  and (hc.ano = ? and hc.mes = ?) order by pm.id desc limit 1 )) ehvalido";
        boolean ehValido = false;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, idPaciente);
            stm.setInt(2,  idProcedimento);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dataSolicitacao);
            stm.setInt(3, calendar.get(Calendar.YEAR));
            stm.setInt(4, calendar.get(Calendar.MONTH)+1);
            stm.setInt(5, idProcedimento);
            stm.setInt(6, calendar.get(Calendar.YEAR));
            stm.setInt(7, calendar.get(Calendar.MONTH)+1);
            ResultSet rs = stm.executeQuery();
            if(rs.next())
                ehValido = rs.getBoolean("ehvalido");
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
        return ehValido;
    }

    public boolean verificaExisteCargaSigtapParaData(Integer mesSolicitacao, Integer anoSolicitacao) {

        PreparedStatement ps = null;
        boolean existe = false;

        try {
            con = ConnectionFactory.getConnection();

            String sql = "select exists (select * from sigtap.historico_consumo_sigtap hcs where hcs.mes = ? and ano = ? and hcs.status = 'A');";

            ps = con.prepareStatement(sql);
            ps.setInt(1, mesSolicitacao);
            ps.setInt(2, anoSolicitacao);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                existe = rs.getBoolean("exists");
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
        return existe;
    }


    public boolean verificaSeExisteAlgumaCargaSigtap() {

        PreparedStatement ps = null;
        boolean existe = false;

        try {
            con = ConnectionFactory.getConnection();

            String sql = "select count(id) quantidade_cargas from sigtap.historico_consumo_sigtap hcs;";

            ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                existe = rs.getInt("quantidade_cargas") > 0;
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
        return existe;
    }


    public List<ProcedimentoBean> listarProcedimentosPorCbo(Integer idCbo, Integer idPrograma) throws ProjetoException {
        List<ProcedimentoBean> listaProcedimento = new ArrayList<>();
        String sql = "select p.id, p.codproc, p.nome " +
                "from hosp.proc p " +
                "join hosp.programa_procedimento_cbo_especifico ppc on p.id = ppc.id_procedimento " +
                "join hosp.procedimentos_unidades pu on (p.id = pu.id_procedimento) "+
                "where pu.id_unidade = ? and ppc.id_cbo = ? and ppc.id_programa = ? and p.ativo = 'S' ; ";


        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, user_session.getUnidade().getId());
            stm.setInt(2, idCbo);
            stm.setInt(3, idPrograma);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                ProcedimentoBean procedimento = new ProcedimentoBean();
                procedimento.setIdProc(rs.getInt("id"));
                procedimento.setCodProc(rs.getString("codproc"));
                procedimento.setNomeProc(rs.getString("nome"));
                listaProcedimento.add(procedimento);
            }
        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return listaProcedimento;
    }
    public List<ProcedimentoBean> listarTodosProcedimentosDoPrograma(AtendimentoBean atendimento) throws ProjetoException {
        List<ProcedimentoBean> listaProcedimento = new ArrayList<>();

        String sql = "select id, codproc, nome from ( " +
                "	-- PROCEDIMENTO PADRÃO \n" +
                "	select p.id, p.codproc, p.nome from hosp.proc p where p.id = ? " +
                "		union " +
                "	-- PROCEDIMENTO PACIENTE INSTITUICAO \n" +
                "	select p.id, p.codproc, p.nome from hosp.proc p " +
                "	join hosp.paciente_instituicao_procedimento pip on p.id = pip.id_procedimento " +
                "	join hosp.paciente_instituicao pi on pip.id_paciente_instituicao = pi.id " +
                " join hosp.procedimentos_unidades pu on (p.id = pu.id_procedimento) "+
                "	where pu.id_unidade = ? and pi.id = (select a.id_paciente_instituicao from hosp.atendimentos a where a.id_atendimento = ?) " +
                "		union " +
                "	-- PROCEDIMENTO CBO \n" +
                "	select p.id, p.codproc, p.nome " +
                "	from hosp.proc p " +
                "	join hosp.programa_procedimento_cbo_especifico ppc on p.id = ppc.id_procedimento " +
                " join hosp.procedimentos_unidades pu on (p.id = pu.id_procedimento) "+
                "	where pu.id_unidade = ? and ppc.id_cbo = ? and ppc.id_programa = ?) as procedimento " +
                "order by nome; ";


        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, atendimento.getProcedimento().getIdProc());
            stm.setInt(2, user_session.getUnidade().getId());
            stm.setInt(3, atendimento.getId());
            stm.setInt(4, user_session.getUnidade().getId());
            stm.setInt(5, atendimento.getFuncionario().getCbo().getCodCbo());
            stm.setInt(6, atendimento.getPrograma().getIdPrograma());
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                mapearPreparedStatementProcedimento(listaProcedimento, rs);
            }
        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return listaProcedimento;
    }

    private void mapearPreparedStatementProcedimento(List<ProcedimentoBean> listaProcedimento, ResultSet rs)
            throws SQLException {
        ProcedimentoBean procedimento = new ProcedimentoBean();
        procedimento.setIdProc(rs.getInt("id"));
        procedimento.setCodProc(rs.getString("codproc"));
        procedimento.setNomeProc(rs.getString("nome"));
        listaProcedimento.add(procedimento);
    }
}
