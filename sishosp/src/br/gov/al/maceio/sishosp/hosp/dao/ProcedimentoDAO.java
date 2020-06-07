package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.model.CboBean;
import br.gov.al.maceio.sishosp.hosp.model.CidBean;
import br.gov.al.maceio.sishosp.hosp.model.HistoricoSigtapBean;
import br.gov.al.maceio.sishosp.hosp.model.ProcedimentoBean;
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

    public boolean gravarProcedimento(ProcedimentoBean proc) {

        Boolean retorno = false;

        String sql = "INSERT INTO hosp.proc (codproc, nome, auditivo, tipo_exame_auditivo, utiliza_equipamento, gera_laudo_digita, validade_laudo, "
                + " cod_unidade) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
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

            ps.executeUpdate();

            con.commit();
            retorno = true;
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

    public boolean alterarProcedimento(ProcedimentoBean proc) {

        Boolean retorno = false;

        String sql = "update hosp.proc set nome = ?, auditivo = ?, tipo_exame_auditivo = ?, utiliza_equipamento = ?, "
                + "gera_laudo_digita = ?, validade_laudo = ?, codproc = ? "
                + "where id = ?";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, proc.getNomeProc().toUpperCase());
            stmt.setBoolean(2, proc.getAuditivo());
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

            stmt.executeUpdate();

            con.commit();
            retorno = true;
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

    public boolean excluirProcedimento(ProcedimentoBean proc){

        Boolean retorno = false;

        try {
            con = ConnectionFactory.getConnection();

            String sql = "delete from sigtap.proc_cbo where id_proc = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setLong(1, proc.getIdProc());
            stmt.execute();

            sql = "delete from sigtap.proc_recurso where id_proc = ?";
            stmt = con.prepareStatement(sql);
            stmt.setLong(1, proc.getIdProc());
            stmt.execute();

            sql = "delete from sigtap.proc_cid where id_proc = ?";
            stmt = con.prepareStatement(sql);
            stmt.setLong(1, proc.getIdProc());
            stmt.execute();

            sql = "delete from hosp.proc where id = ?";
            stmt = con.prepareStatement(sql);
            stmt.setLong(1, proc.getIdProc());
            stmt.execute();

            con.commit();
            retorno = true;
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

    public List<ProcedimentoBean> listarProcedimento() throws ProjetoException {
        List<ProcedimentoBean> lista = new ArrayList<>();
        String sql = "select id, codproc, nome, auditivo, tipo_exame_auditivo, utiliza_equipamento, gera_laudo_digita, validade_laudo, "
                + " idade_minima, idade_maxima, qtd_maxima, prazo_minimo_nova_execucao, sexo "
                + " from hosp.proc where cod_unidade = ? order by nome";

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

    public List<ProcedimentoBean> buscarProcedimento(String campoBusca, String tipo) throws ProjetoException {
        List<ProcedimentoBean> lista = new ArrayList<>();
        String sql = "select id, codproc, nome, auditivo, tipo_exame_auditivo, utiliza_equipamento, gera_laudo_digita, validade_laudo, "
                + " idade_minima, idade_maxima, qtd_maxima, prazo_minimo_nova_execucao, sexo "
                + " from hosp.proc where cod_unidade = ? ";

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

    public List<ProcedimentoBean> listarProcedimentoLaudo() throws ProjetoException {
        List<ProcedimentoBean> lista = new ArrayList<>();
        String sql = "select id, codproc, nome, auditivo, tipo_exame_auditivo, utiliza_equipamento, gera_laudo_digita, validade_laudo, "
                + " idade_minima, idade_maxima, qtd_maxima, prazo_minimo_nova_execucao, sexo "
                + " from hosp.proc where cod_unidade = ? and gera_laudo_digita is true order by nome";

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

    public ProcedimentoBean listarProcedimentoPorId(int id)
            throws ProjetoException {
        ProcedimentoBean proc = new ProcedimentoBean();
        String sql = "select id, codproc, nome, auditivo, tipo_exame_auditivo, utiliza_equipamento, gera_laudo_digita, validade_laudo,"
                + " idade_minima, idade_maxima, qtd_maxima, prazo_minimo_nova_execucao, sexo "
                + "from hosp.proc where id = ? order by nome";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
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
        return proc;
    }


    public List<ProcedimentoBean> listarProcedimentoBusca(
            String descricaoBusca, Integer tipoBuscar) throws ProjetoException {

        List<ProcedimentoBean> lista = new ArrayList<>();
        String sql = "select id,codproc  ||' - '|| nome as nome ,codproc, auditivo, tipo_exame_auditivo, utiliza_equipamento, gera_laudo_digita, validade_laudo,"
                + "idade_minima, idade_maxima, qtd_maxima, prazo_minimo_nova_execucao, sexo  "
                + "from hosp.proc ";
        if (tipoBuscar == 1) {
            sql += " where upper(codproc ||' - '|| nome) LIKE ? and cod_unidade = ? order by nome";
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

    public List<ProcedimentoBean> listarProcedimentoQueGeramLaudoBusca(
            String descricaoBusca, Integer tipoBuscar) throws ProjetoException {

        List<ProcedimentoBean> lista = new ArrayList<>();
        String sql = "select id,codproc  ||' - '|| nome as nome ,codproc, auditivo, tipo_exame_auditivo, utiliza_equipamento, gera_laudo_digita, validade_laudo,"
                + "idade_minima, idade_maxima, qtd_maxima, prazo_minimo_nova_execucao, sexo  "
                + "from hosp.proc ";
        if (tipoBuscar == 1) {
            sql += " where (codproc ||' - '|| nome) ILIKE ? and cod_unidade = ? and gera_laudo_digita is true order by nome";
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

    public ArrayList<CidBean> listarCid(Integer id_proc) throws ProjetoException {

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

    public ProcedimentoBean listarProcedimentoPorIdComConexao(int id, Connection conAuxiliar)
            throws ProjetoException {
        ProcedimentoBean proc = new ProcedimentoBean();
        String sql = "select id, codproc, nome, auditivo, tipo_exame_auditivo, utiliza_equipamento, gera_laudo_digita, validade_laudo,"
                + " idade_minima, idade_maxima, qtd_maxima, prazo_minimo_nova_execucao, sexo "
                + "from hosp.proc where id = ? order by nome";
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
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return proc;
    }

    public List<PropriedadeDeProcedimentoMensalExistenteDTO> buscaModalidadeAtendimentoExistente() throws ProjetoException {
        List<PropriedadeDeProcedimentoMensalExistenteDTO> listaModaliadesAtendimentoExistente = new ArrayList();
        String sql = "SELECT ma.id, ma.codigo FROM sigtap.modalidade_atendimento ma ";
        try {
            con = ConnectionFactory.getConnection();
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
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return listaModaliadesAtendimentoExistente;
    }

    public List<PropriedadeDeProcedimentoMensalExistenteDTO> buscaInstrumentosRegistroExistente() throws ProjetoException {
        List<PropriedadeDeProcedimentoMensalExistenteDTO> listaInstrumentoRegistroExistente = new ArrayList();
        String sql = "SELECT ir.id, ir.codigo FROM sigtap.instrumento_registro ir ";
        try {
            con = ConnectionFactory.getConnection();
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
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return listaInstrumentoRegistroExistente;
    }

    public List<PropriedadeDeProcedimentoMensalExistenteDTO> buscaCbosExistentes() throws ProjetoException {
        List<PropriedadeDeProcedimentoMensalExistenteDTO> listaCboExistente = new ArrayList();
        String sql = "SELECT cbo.id, cbo.codigo FROM sigtap.cbo_mensal cbo ";
        try {
            con = ConnectionFactory.getConnection();
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
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return listaCboExistente;
    }

    public List<PropriedadeDeProcedimentoMensalExistenteDTO> buscaCidsExistentes() throws ProjetoException {
        List<PropriedadeDeProcedimentoMensalExistenteDTO> listaCidExistente = new ArrayList();
        String sql = "SELECT cid.id, cid.codigo FROM sigtap.cid_mensal cid ";
        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                PropriedadeDeProcedimentoMensalExistenteDTO cidExistente = new PropriedadeDeProcedimentoMensalExistenteDTO();
                cidExistente.setId(rs.getInt("id"));
                cidExistente.setCodigo(rs.getString("codigo"));
                listaCidExistente.add(cidExistente);
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
        return listaCidExistente;
    }

    public List<PropriedadeDeProcedimentoMensalExistenteDTO> buscaFormasOrganizacaoExistentes() throws ProjetoException {
        List<PropriedadeDeProcedimentoMensalExistenteDTO> listaFormaOrganizacaoExistente = new ArrayList();
        String sql = "SELECT fo.id, fo.codigo FROM sigtap.forma_de_organizacao fo ";
        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                PropriedadeDeProcedimentoMensalExistenteDTO formaOrganizacaoExistente = new PropriedadeDeProcedimentoMensalExistenteDTO();
                formaOrganizacaoExistente.setId(rs.getInt("id"));
                formaOrganizacaoExistente.setCodigo(rs.getString("codigo"));
                listaFormaOrganizacaoExistente.add(formaOrganizacaoExistente);
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
        return listaFormaOrganizacaoExistente;
    }

    public List<PropriedadeDeProcedimentoMensalExistenteDTO> buscaRenasesExistentes() throws ProjetoException {
        List<PropriedadeDeProcedimentoMensalExistenteDTO> listaRenasesExistentes = new ArrayList();
        String sql = "SELECT re.id, re.codigo FROM sigtap.renases re ";
        try {
            con = ConnectionFactory.getConnection();
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
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return listaRenasesExistentes;
    }

    public List<PropriedadeDeProcedimentoMensalExistenteDTO> buscaTiposFinanciamentoExistentes() throws ProjetoException {
        List<PropriedadeDeProcedimentoMensalExistenteDTO> listaCodigoTipoFinanciamento = new ArrayList();
        String sql = "SELECT tf.id, tf.codigo FROM sigtap.tipo_financiamento tf ";
        try {
            con = ConnectionFactory.getConnection();
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
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return listaCodigoTipoFinanciamento;
    }


    public Integer executaRotinaNovaCargaSigtap(GravarProcedimentoMensalDTO procedimentoMensalDTO, Long idFuncionario, Integer idHistorico) throws Exception {

        Integer idHistoricoConsumoSigtap;

        try {
            this.con = ConnectionFactory.getConnection();

            if(VerificadorUtil.verificarSeObjetoNuloOuZero(idHistorico)) {
                idHistoricoConsumoSigtap = gravarHistoricoConsumoSigtap(idFuncionario, con);
                desatualizaStatusHistoricoConsumoSigtapAnterior(idHistoricoConsumoSigtap, con);
            }
            else
                idHistoricoConsumoSigtap = idHistorico;

            Integer idProcedimentoMensal = inserirProcedimentoMensal(procedimentoMensalDTO, con, idHistoricoConsumoSigtap);

            inserirCIDs(procedimentoMensalDTO.getProcedimentoMensal().getCIDsVinculados().getCIDVinculado(), idProcedimentoMensal, con,
                    procedimentoMensalDTO.getListaIdCidsExistente());

            inserirModalidadeAtendimento(procedimentoMensalDTO.getProcedimentoMensal().
                            getModalidadesAtendimento().getModalidadeAtendimento(), idProcedimentoMensal, con,
                    procedimentoMensalDTO.getListaIdModalidadeAtendimentoExistente());

            inserirInstrumentosRegistro(procedimentoMensalDTO.getProcedimentoMensal().
                            getInstrumentosRegistro().getInstrumentoRegistro(), idProcedimentoMensal, con,
                    procedimentoMensalDTO.getListaIdInstrumentosRegistroExistente());

            inserirCBOs(procedimentoMensalDTO.getProcedimentoMensal().getCBOsVinculados().getCBO(), idProcedimentoMensal, con,
                    procedimentoMensalDTO.getListaIdCBOsExistente());

            inserirRenases(procedimentoMensalDTO.getProcedimentoMensal().getRENASESVinculadas().getRENASES(), idProcedimentoMensal, con,
                    procedimentoMensalDTO.getListaIdRenasesExistente());

            inserirServicoClassificacao(procedimentoMensalDTO.getProcedimentoMensal().
                    getServicosClassificacoesVinculados().getServicoClassificacao(), idProcedimentoMensal, con);
            con.commit();
        } catch (Exception e) {
            throw e;
        }finally {
            con.close();
        }
        return idHistoricoConsumoSigtap;
    }

    public Integer inserirTipoFinanciamento(TipoFinanciamentoType tipoFinanciamento, Connection conexao) throws ProjetoException, SQLException {
        String sql = "INSERT INTO sigtap.tipo_financiamento (codigo, nome) "
                + "VALUES(?, ?) returning id";
        Integer idTipoFinanciamento = null;
        try {
            PreparedStatement ps = conexao.prepareStatement(sql);
            ps.setString(1, tipoFinanciamento.getCodigo());
            ps.setString(2, tipoFinanciamento.getNome());
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                idTipoFinanciamento = rs.getInt("id");
        } catch (Exception ex) {
            conexao.rollback();
            ex.printStackTrace();
            throw new ProjetoException(ex);
        }
        return idTipoFinanciamento;
    }

    public void inserirRenases(List<RENASESType> listaRenases, Integer idProcedimentoMensal, Connection conexao,
                               List<Integer> listaIdRenasesExistentes) throws ProjetoException, SQLException {
        String sqlRenases = "INSERT INTO sigtap.renases (codigo, nome) "
                + "VALUES(?, ?) returning id";

        String sqlRenasesProcedimentoMensal = "INSERT INTO sigtap.renases_procedimento_mensal " +
                "(id_procedimento_mensal, id_renases_mensal) VALUES(?, ?);";
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

            for (Integer idRenasesExistente : listaIdRenasesExistentes) {
                stm = conexao.prepareStatement(sqlRenasesProcedimentoMensal);
                stm.setInt(1, idProcedimentoMensal);
                stm.setInt(2, idRenasesExistente);
                stm.executeUpdate();
            }
        } catch (Exception ex) {
            conexao.rollback();
            ex.printStackTrace();
            throw new ProjetoException(ex);
        }
    }

    public Integer inserirFormaOrganizacao(FormaOrganizacaoType formaOrganizacao, Connection conexao) throws ProjetoException, SQLException {
        String sql = "INSERT INTO sigtap.forma_de_organizacao " +
                "(codigo, nome, id_subgrupo_mensal) " +
                "VALUES(?, ?, ?) returning id ";
        Integer idFormaOrganizacao = null;
        try {
            PreparedStatement ps = conexao.prepareStatement(sql);

            ps.setString(1, formaOrganizacao.getCodigo());
            ps.setString(2, formaOrganizacao.getNome());
            Integer idSubgrupoExistente = retornaIdSubgrupoCasoExiste(formaOrganizacao.getSubgrupo(), conexao);
            if(!VerificadorUtil.verificarSeObjetoNuloOuZero(idSubgrupoExistente))
                ps.setInt(3, idSubgrupoExistente);
            else {
                Integer idSubgrupo = inserirSubgrupoMensal(formaOrganizacao.getSubgrupo(), conexao);
                ps.setInt(3, idSubgrupo);
            }
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                idFormaOrganizacao = rs.getInt("id");
        } catch (Exception ex) {
            conexao.rollback();
            ex.printStackTrace();
            throw new ProjetoException(ex);
        }
        return idFormaOrganizacao;
    }

    private Integer retornaIdSubgrupoCasoExiste(SubgrupoType subgrupo, Connection conexao) throws SQLException, ProjetoException {
        String sql = "select id from sigtap.subgrupo_mensal where codigo = ?";
        Integer idSubgrupo = 0;
        try {
            PreparedStatement ps = conexao.prepareStatement(sql);

            ps.setString(1, subgrupo.getCodigo());
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                idSubgrupo = rs.getInt("id");
        } catch (Exception ex) {
            conexao.rollback();
            ex.printStackTrace();
            throw new ProjetoException(ex);
        }
        return idSubgrupo;
    }

    public Integer inserirSubgrupoMensal(SubgrupoType subgrupo, Connection conexao) throws ProjetoException, SQLException {
        String sql = "INSERT INTO sigtap.subgrupo_mensal " +
                "(codigo, nome, id_grupo_mensal) " +
                "VALUES(?, ?, ?) returning id ";
        Integer idSubgrupo = null;
        try {
            PreparedStatement ps = conexao.prepareStatement(sql);

            ps.setString(1, subgrupo.getCodigo());
            ps.setString(2, subgrupo.getNome());
            Integer idGrupoExistente = retornaIdGrupoCasoExiste(subgrupo, conexao);
            if(!VerificadorUtil.verificarSeObjetoNuloOuZero(idGrupoExistente))
                ps.setInt(3, idGrupoExistente);
            else {
                Integer idGrupo = inserirGrupoMensal(subgrupo.getGrupo(), conexao);
                ps.setInt(3, idGrupo);
            }
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                idSubgrupo = rs.getInt("id");
        } catch (Exception ex) {
            conexao.rollback();
            ex.printStackTrace();
            throw new ProjetoException(ex);
        }
        return idSubgrupo;
    }

    private Integer retornaIdGrupoCasoExiste(SubgrupoType subgrupo, Connection conexao) throws SQLException, ProjetoException {
        String sql = "select id from sigtap.grupo_mensal where codigo = ?";
        Integer idGrupo = 0;
        try {
            PreparedStatement ps = conexao.prepareStatement(sql);
            ps.setString(1, subgrupo.getGrupo().getCodigo());
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                idGrupo = rs.getInt("id");
        } catch (Exception ex) {
            conexao.rollback();
            ex.printStackTrace();
            throw new ProjetoException(ex);
        }
        return idGrupo;
    }

    public Integer inserirGrupoMensal(GrupoType grupo, Connection conexao) throws ProjetoException, SQLException {
        String sql = "INSERT INTO sigtap.grupo_mensal " +
                "(codigo, nome) " +
                "VALUES(?, ?) returning id ";
        Integer idGrupo = null;
        try {
            PreparedStatement ps = conexao.prepareStatement(sql);
            ps.setString(1, grupo.getCodigo());
            ps.setString(2, grupo.getNome());
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                idGrupo = rs.getInt("id");
        } catch (Exception ex) {
            conexao.rollback();
            ex.printStackTrace();
            throw new ProjetoException(ex);
        }
        return idGrupo;
    }

    public Integer inserirProcedimentoMensal(GravarProcedimentoMensalDTO procedimentoMensalDTO, Connection conexao, Integer idHistoricoConsumoSigtap)
            throws ProjetoException, SQLException {
        String sql = "INSERT INTO sigtap.procedimento_mensal " +
                "(id_procedimento, codigo_procedimento, nome, competencia, complexidade, id_tipo_financiamento, "+
                "sexo, quantidade_maxima, idade_minima, unidade_idade_minima, idade_maxima, unidade_idade_maxima, "+
                "servico_ambulatorial, servico_hospitalar, servico_profisional, id_forma_de_organizacao, "+
                "descricao, id_historico_consumo_sigtap) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning id; ";

        Integer idProcedimentoMensal = null;
        try {
            PreparedStatement stm = conexao.prepareStatement(sql);
            stm.setInt(1, procedimentoMensalDTO.getIdProcedimento());
            stm.setString(2, procedimentoMensalDTO.getProcedimentoMensal().getCodigo());
            stm.setString(3, procedimentoMensalDTO.getProcedimentoMensal().getNome());
            stm.setString(4, procedimentoMensalDTO.getProcedimentoMensal().getCompetenciaInicial());
            stm.setString(5, procedimentoMensalDTO.getProcedimentoMensal().getComplexidade().name());

            if(!VerificadorUtil.verificarSeObjetoNuloOuZero(procedimentoMensalDTO.getIdTipoFinanciamentoExistente()))
                stm.setInt(6, procedimentoMensalDTO.getIdTipoFinanciamentoExistente());
            else {
                Integer idTipoFinanciamento = inserirTipoFinanciamento(procedimentoMensalDTO.getProcedimentoMensal().getTipoFinanciamento(), conexao);
                stm.setInt(6, idTipoFinanciamento);
            }
            stm.setString(7, procedimentoMensalDTO.getProcedimentoMensal().getSexoPermitido());

            if(VerificadorUtil.verificarSeObjetoNulo(procedimentoMensalDTO.getProcedimentoMensal().getQuantidadeMaxima()))
                stm.setNull(8, Types.NULL);
            else
                stm.setInt(8, procedimentoMensalDTO.getProcedimentoMensal().getQuantidadeMaxima());

            stm.setInt(9, procedimentoMensalDTO.getProcedimentoMensal().getIdadeMinimaPermitida().getQuantidadeLimite());
            stm.setString(10, procedimentoMensalDTO.getProcedimentoMensal().getIdadeMinimaPermitida().getUnidadeLimite().name());
            stm.setInt(11, procedimentoMensalDTO.getProcedimentoMensal().getIdadeMaximaPermitida().getQuantidadeLimite());
            stm.setString(12, procedimentoMensalDTO.getProcedimentoMensal().getIdadeMaximaPermitida().getUnidadeLimite().name());
            stm.setDouble(13, procedimentoMensalDTO.getProcedimentoMensal().getValorSA().doubleValue());
            stm.setDouble(14, procedimentoMensalDTO.getProcedimentoMensal().getValorSH().doubleValue());
            stm.setDouble(15, procedimentoMensalDTO.getProcedimentoMensal().getValorSP().doubleValue());

            if(!VerificadorUtil.verificarSeObjetoNuloOuZero(procedimentoMensalDTO.getIdFormasDeOrganizacaoExistente()))
                stm.setInt(16, procedimentoMensalDTO.getIdFormasDeOrganizacaoExistente());
            else {
                Integer idFormaOrganizacao = inserirFormaOrganizacao(procedimentoMensalDTO.getProcedimentoMensal().getFormaOrganizacao(), conexao);
                stm.setInt(16, idFormaOrganizacao);
            }

            stm.setString(17, procedimentoMensalDTO.getProcedimentoMensal().getDescricao());
            stm.setInt(18, idHistoricoConsumoSigtap);
            ResultSet rs = stm.executeQuery();
            if(rs.next())
                idProcedimentoMensal = rs.getInt("id");
        } catch (Exception ex) {
            conexao.rollback();
            ex.printStackTrace();
            throw new ProjetoException(ex);
        }
        return idProcedimentoMensal;
    }

    public void inserirModalidadeAtendimento (List<ModalidadeAtendimentoType> modalidadesAtendimento,
                                              Integer idProcedimentoMensal, Connection conexao, List<Integer> listaIdModalidadesAtendimentoExistentes)
            throws ProjetoException, SQLException {
        String sqlModalidadeAtendimento = "INSERT INTO sigtap.modalidade_atendimento " +
                "(codigo, nome) " +
                "VALUES(?, ?) returning id";

        String sqlModalidadeAtendimentoProcedimentoMensal =
                "INSERT INTO sigtap.modalidade_atendimento_procedimento_mensal " +
                        "(id_procedimento_mensal, id_modalidade_atendimento) " +
                        "VALUES(?, ?);";
        try {
            PreparedStatement stm = null;
            for(ModalidadeAtendimentoType modalidadeAtendimentoType : modalidadesAtendimento) {
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
        } catch (Exception ex) {
            conexao.rollback();
            ex.printStackTrace();
            throw new ProjetoException(ex);
        }
    }

    public void inserirInstrumentosRegistro(List<InstrumentoRegistroType> instrumentosRegistro,
                                            Integer idProcedimentoMensal, Connection conexao, List<Integer> listaIdInstrumentosRegistroExistentes)
            throws ProjetoException, SQLException {

        String sqlInstrumentoRegistro = "INSERT INTO sigtap.instrumento_registro (codigo, nome) "
                + "VALUES(?, ?) returning id";

        String sqlInstrumentoRegistroProcedimentoMensal =
                "INSERT INTO sigtap.instrumento_registro_procedimento_mensal " +
                        "(id_procedimento_mensal, id_instrumento_registro) " +
                        "VALUES(?, ?); ";

        try {
            PreparedStatement stm = null;
            for (InstrumentoRegistroType instrumentoRegistroType : instrumentosRegistro) {
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
        } catch (Exception ex) {
            conexao.rollback();
            ex.printStackTrace();
            throw new ProjetoException(ex);
        }
    }

    public void inserirCBOs(List<CBOType> cbos, Integer idProcedimentoMensal, Connection conexao, List<Integer> listaIdCbosExistentes)
            throws ProjetoException, SQLException {
        String sqlCbo = "INSERT INTO sigtap.cbo_mensal (codigo, nome) VALUES(?, ?) returning id";

        String sqlCboProcedimentoMensal = "INSERT INTO sigtap.cbo_procedimento_mensal " +
                "(id_procedimento_mensal, id_cbo_mensal) VALUES(?, ?);";
        try {
            PreparedStatement stm = null;
            for(CBOType cboType : cbos) {
                stm = conexao.prepareStatement(sqlCbo);
                stm.setString(1, cboType.getCodigo());
                stm.setString(2, cboType.getNome());
                ResultSet rs = stm.executeQuery();
                if(rs.next()) {
                    Integer idNovoCbo = rs.getInt("id");
                    stm = conexao.prepareStatement(sqlCboProcedimentoMensal);
                    stm.setInt(1, idProcedimentoMensal);
                    stm.setInt(2, idNovoCbo);
                    stm.executeUpdate();
                }
            }

            for(Integer idCboExistente : listaIdCbosExistentes) {
                stm = conexao.prepareStatement(sqlCboProcedimentoMensal);
                stm.setInt(1, idProcedimentoMensal);
                stm.setInt(2, idCboExistente);
                stm.executeUpdate();
            }
        } catch (Exception ex) {
            conexao.rollback();
            ex.printStackTrace();
            throw new ProjetoException(ex);
        }
    }

    public void inserirCIDs(List<CIDVinculado> listaCidVinculado, Integer idProcedimentoMensal, Connection conexao, List<Integer> listaIdCidsExistentes)
            throws ProjetoException, SQLException {
        String sqlCid = "INSERT INTO sigtap.cid_mensal " +
                "(codigo, nome, agravo, sexo_aplicavel, estadio, quantidade_campos_irradiados) " +
                "VALUES(?, ?, ?, ?, ?, ?) returning id;";

        String sqlCidProcedimentoMensal = "INSERT INTO sigtap.cid_procedimento_mensal (id_procedimento_mensal, id_cid_mensal) " +
                "VALUES(?, ?);";
        try {
            PreparedStatement stm = null;
            for(CIDVinculado cidVinculado : listaCidVinculado) {
                stm = conexao.prepareStatement(sqlCid);
                stm.setString(1, cidVinculado.getCID().getCodigo());
                stm.setString(2, cidVinculado.getCID().getNome());
                stm.setString(3, cidVinculado.getCID().getAgravo().name());
                stm.setString(4, cidVinculado.getCID().getSexoAplicavel());
                stm.setBoolean(5, cidVinculado.getCID().isEstadio());
                stm.setInt(6, cidVinculado.getCID().getQuantidadeCamposIrradiados());
                ResultSet rs = stm.executeQuery();
                if(rs.next()) {
                    Integer idNovoCid = rs.getInt("id");
                    stm = conexao.prepareStatement(sqlCidProcedimentoMensal);
                    stm.setInt(1, idProcedimentoMensal);
                    stm.setInt(2, idNovoCid);
                    stm.executeUpdate();
                }
            }

            for(Integer idCidExistente : listaIdCidsExistentes) {
                stm = conexao.prepareStatement(sqlCidProcedimentoMensal);
                stm.setInt(1, idProcedimentoMensal);
                stm.setInt(2, idCidExistente);
                stm.executeUpdate();
            }
        } catch (Exception ex) {
            conexao.rollback();
            ex.printStackTrace();
            throw new ProjetoException(ex);
        }
    }

    public void inserirServicoClassificacao
            (List<ServicoClassificacaoType> servicosClassificacoesType, Integer idProcedimentoMensal, Connection conexao) throws ProjetoException, SQLException {
        String sqlServicoClassificacao = "INSERT INTO sigtap.servico_classificacao_mensal " +
                "(id_servico, id_classificacao, id_procedimento_mensal) " +
                "VALUES(?, ?, ?);";

        String sqlVerificaServico = "select id as id_servico from sigtap.servico_mensal where codigo = ?";

        String sqlVerificaClassificacao =
                "select scm.id_classificacao " +
                        "from sigtap.servico_classificacao_mensal scm " +
                        "join sigtap.classificacao_mensal cm on scm.id_classificacao = cm.id " +
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
        } catch (Exception ex) {
            conexao.rollback();
            ex.printStackTrace();
            throw new ProjetoException(ex);
        }
    }

    public Integer inserirServico(ServicoType servicoType, Connection conexao) throws ProjetoException, SQLException {
        String sql = "INSERT INTO sigtap.servico_mensal " +
                "(codigo, nome) VALUES(?, ?) returning id; ";
        Integer idServico = null;
        try {
            PreparedStatement ps = conexao.prepareStatement(sql);
            ps.setString(1, servicoType.getCodigo());
            ps.setString(2, servicoType.getNome());
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                idServico = rs.getInt("id");
        } catch (Exception ex) {
            conexao.rollback();
            ex.printStackTrace();
            throw new ProjetoException(ex);
        }
        return idServico;
    }

    public Integer inserirClassificacao(String codigo, String nome, Connection conexao) throws ProjetoException, SQLException {
        String sql = "INSERT INTO sigtap.classificacao_mensal " +
                "(codigo, nome) VALUES(?, ?) returning id; ";
        Integer idClassificacao = null;
        try {
            PreparedStatement ps = conexao.prepareStatement(sql);
            ps.setString(1, codigo);
            ps.setString(2, nome);
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                idClassificacao = rs.getInt("id");
        } catch (Exception ex) {
            conexao.rollback();
            ex.printStackTrace();
            throw new ProjetoException(ex);
        }
        return idClassificacao;
    }

    public Integer gravarHistoricoConsumoSigtap(Long idFuncionario, Connection conexao)
            throws ProjetoException, SQLException {
        Integer idHistorico = null;
        String sql = "INSERT INTO sigtap.historico_consumo_sigtap " +
                "(mes, ano, data_registro, id_funcionario) " +
                "VALUES(?, ?, current_timestamp, ?) returning id;";

        Integer mesAtual = buscaMesAtual(conexao);
        Integer anoAtual = buscaAnoAtual(conexao);
        try {
            PreparedStatement stm = conexao.prepareStatement(sql);
            stm.setInt(1, mesAtual);
            stm.setInt(2, anoAtual);
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

    public void desatualizaStatusHistoricoConsumoSigtapAnterior(Integer idHistorico, Connection conexao)
            throws ProjetoException, SQLException {

        String sql = "UPDATE sigtap.historico_consumo_sigtap SET status='D' WHERE id != ?; ";
        try {
            PreparedStatement stm = conexao.prepareStatement(sql);
            stm.setLong(1, idHistorico);
            stm.executeUpdate();
        } catch (Exception ex) {
            conexao.rollback();
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    public Integer buscaMesAtual(Connection conexao) throws ProjetoException, SQLException {
        Integer mesAtual = null;
        String sql = "select extract(month from current_date) as mes_atual";
        try {
            PreparedStatement ps = conexao.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                mesAtual = rs.getInt("mes_atual");
        } catch (Exception ex) {
            conexao.rollback();
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return mesAtual;
    }

    public Integer buscaAnoAtual(Connection conexao) throws ProjetoException, SQLException {
        Integer anoAtual = null;
        String sql = "select extract(year from current_date) as ano_atual";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                anoAtual = rs.getInt("ano_atual");
        } catch (Exception ex) {
            con.rollback();
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return anoAtual;
    }

    public Boolean houveCargaDoSigtapEsteMes() {

        String sql = "select exists (select id from sigtap.historico_consumo_sigtap " +
                "where id = (select id from sigtap.historico_consumo_sigtap hcs " +
                "order by id desc limit 1) and (status = 'A' and mes = EXTRACT(MONTH FROM current_date) "+
                "and ano = EXTRACT(YEAR FROM current_date))) houve_carga_este_mes";
        Boolean houveCargaDoSigtap = false;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();

            if (rs.next())
                houveCargaDoSigtap = rs.getBoolean("houve_carga_este_mes");

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
        return houveCargaDoSigtap;
    }

    public List<HistoricoSigtapBean> listaHistoricoCargasDoSigtap() {

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
        return listaHistoricosSigtap;
    }


    public ProcedimentoType buscaDadosProcedimentoMensal(String codigoProcedimento, Integer ano, Integer mes) throws Exception {

        ProcedimentoType procedimento = new ProcedimentoType();
        try {
            this.con = ConnectionFactory.getConnection();
            if(VerificadorUtil.verificarSeObjetoNuloOuZero(ano) && VerificadorUtil.verificarSeObjetoNuloOuZero(mes)) {
                ano = buscaAnoAtual(con);
                mes = buscaMesAtual(con);
            }
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

        } catch (Exception e) {
            throw e;
        }finally {
            con.close();
        }
        return procedimento;
    }


    public ProcedimentoType buscaProcedimentoMensal(Integer ano, Integer mes, String codigoProcedimento, Connection conexao) throws SQLException {

        ProcedimentoType procedimentoMensal = new ProcedimentoType();
        String sql = "select pm.nome, pm.descricao, pm.codigo_procedimento, pm.competencia, pm.complexidade, " +
                "pm.sexo, pm.quantidade_maxima, pm.idade_minima, pm.unidade_idade_minima, " +
                "pm.idade_maxima, pm.unidade_idade_maxima, pm.servico_ambulatorial, pm.servico_hospitalar, pm.servico_profisional, " +
                "tp.codigo as codigo_tipo_financiamento, tp.nome as tipo_financiamento, fo.codigo as codigo_forma_organizacao, " +
                "fo.nome as forma_organizacao, sm.codigo as codigo_subgrupo, sm.nome as subgrupo, " +
                "gm.codigo as codigo_grupo, gm.nome as grupo " +
                "from sigtap.procedimento_mensal pm " +
                "join sigtap.historico_consumo_sigtap hc on hc.id = pm.id_historico_consumo_sigtap " +
                "left sigtap hosp.tipo_financiamento tp on tp.id = pm.id_tipo_financiamento " +
                "left sigtap hosp.forma_de_organizacao fo on fo.id = pm.id_forma_de_organizacao " +
                "join sigtap.subgrupo_mensal sm on sm.id = fo.id_subgrupo_mensal " +
                "join sigtap.grupo_mensal gm on gm.id = sm.id_grupo_mensal " +
                "where hc.id = (select id from sigtap.historico_consumo_sigtap hcs where hcs.ano = ? and hcs.mes = ? " +
                "		   order by id desc limit 1) " +
                "and pm.codigo_procedimento = ?;";

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
                procedimentoMensal.setCompetenciaInicial(rs.getString("competencia"));

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
                idadeMinimaPermitida.setQuantidadeLimite(rs.getInt("idade_minima"));
                String unidadeLimiteIdadeMinima = rs.getString("unidade_idade_minima");
                if(unidadeLimiteIdadeMinima.equals(UnidadeLimiteType.MESES.name()))
                    idadeMinimaPermitida.setUnidadeLimite(UnidadeLimiteType.MESES);
                else
                    idadeMinimaPermitida.setUnidadeLimite(UnidadeLimiteType.ANOS);
                procedimentoMensal.setIdadeMinimaPermitida(idadeMinimaPermitida);;

                IdadeLimiteType idadeMaximaPermitida = new IdadeLimiteType();
                idadeMaximaPermitida.setQuantidadeLimite(rs.getInt("idade_maxima"));
                String unidadeLimiteIdadeMaxima = rs.getString("unidade_idade_maxima");
                if(unidadeLimiteIdadeMaxima.equals(UnidadeLimiteType.MESES.name()))
                    idadeMaximaPermitida.setUnidadeLimite(UnidadeLimiteType.MESES);
                else
                    idadeMaximaPermitida.setUnidadeLimite(UnidadeLimiteType.ANOS);
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
        } catch (Exception ex) {
            ex.printStackTrace();
            conexao.rollback();
            throw new RuntimeException(ex);
        }
        return procedimentoMensal;
    }


    public List<CIDVinculado> listaCidsProcedimentoMensal
            (Integer ano, Integer mes, String codigoProcedimento, Connection conexao) throws SQLException {

        List<CIDVinculado> listaCidsVinculados = new ArrayList();
        String sql = "select cm.codigo, cm.nome, cm.agravo, cm.sexo_aplicavel, cm.estadio, cm.quantidade_campos_irradiados " +
                "from sigtap.cid_mensal cm join hosp.cid_procedimento_mensal cpm on cm.id = cpm.id_cid_mensal " +
                "join sigtap.procedimento_mensal pm on pm.id = cpm.id_procedimento_mensal " +
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
                CIDVinculado cidVinculado = new CIDVinculado();
                CIDType cid = new CIDType();
                cid.setCodigo(rs.getString("codigo"));
                cid.setNome(rs.getString("nome"));

                String agravo = rs.getString("agravo");
                if(agravo.equals(AgravoType.AGRAVO_BLOQUEIO.name()))
                    cid.setAgravo(AgravoType.AGRAVO_BLOQUEIO);

                else if(agravo.equals(AgravoType.AGRAVO_NOTIFICACAO.name()))
                    cid.setAgravo(AgravoType.AGRAVO_NOTIFICACAO);
                else
                    cid.setAgravo(AgravoType.SEM_AGRAVO);

                cid.setSexoAplicavel(rs.getString("sexo_aplicavel"));
                cid.setEstadio(rs.getBoolean("estadio"));
                cid.setQuantidadeCamposIrradiados(rs.getInt("quantidade_campos_irradiados"));
                cidVinculado.setCID(cid);
                listaCidsVinculados.add(cidVinculado);
            }
        } catch (Exception ex) {
            conexao.rollback();
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return listaCidsVinculados;
    }

    public List<CBOType> listaCbosProcedimentoMensal
            (Integer ano, Integer mes, String codigoProcedimento, Connection conexao) throws SQLException {

        List<CBOType> listaCbos = new ArrayList();
        String sql = "select cbo.codigo, cbo.nome " +
                "from sigtap.cbo_mensal cbo join hosp.cbo_procedimento_mensal cpm on cbo.id = cpm.id_cbo_mensal " +
                "join sigtap.procedimento_mensal pm on pm.id = cpm.id_procedimento_mensal " +
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
                CBOType cbo = new CBOType();
                cbo.setCodigo(rs.getString("codigo"));
                cbo.setNome(rs.getString("nome"));

                listaCbos.add(cbo);
            }
        } catch (Exception ex) {
            conexao.rollback();
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return listaCbos;
    }

    public List<ServicoClassificacaoType> listaServicoClassificacaoProcedimentoMensal
            (Integer ano, Integer mes, String codigoProcedimento, Connection conexao) throws SQLException {

        List<ServicoClassificacaoType> listaServicoClassificacao = new ArrayList();
        String sql = "select sm.codigo as codigo_servico, sm.nome as servico, " +
                "cm.codigo as codigo_classificacao, cm.nome as classificacao " +
                "from sigtap.servico_mensal sm join hosp.servico_classificacao_mensal scm on sm.id = scm.id_servico " +
                "join sigtap.classificacao_mensal cm on cm.id = scm.id_classificacao " +
                "join sigtap.procedimento_mensal pm on pm.id = scm.id_procedimento_mensal " +
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
                ServicoClassificacaoType servicoClassificacao = new ServicoClassificacaoType();
                ServicoType servico = new ServicoType();
                servico.setCodigo(rs.getString("codigo_servico"));
                servico.setNome(rs.getString("servico"));

                servicoClassificacao.setServico(servico);
                servicoClassificacao.setCodigoClassificacao(rs.getString("codigo_classificacao"));
                servicoClassificacao.setNomeClassificacao(rs.getString("classificacao"));

                listaServicoClassificacao.add(servicoClassificacao);
            }
        } catch (Exception ex) {
            conexao.rollback();
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return listaServicoClassificacao;
    }

    public List<ModalidadeAtendimentoType> listaModalidadeAtendimentoProcedimentoMensal
            (Integer ano, Integer mes, String codigoProcedimento, Connection conexao) throws SQLException {

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
        } catch (Exception ex) {
            conexao.rollback();
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return listaModalidadeAtendimento;
    }

    public List<InstrumentoRegistroType> listaInstrumentoRegistroProcedimentoMensal
            (Integer ano, Integer mes, String codigoProcedimento, Connection conexao) throws SQLException {

        List<InstrumentoRegistroType> listaInstrumentoRegistro = new ArrayList();
        String sql = "select ir.codigo, ir.nome " +
                "from sigtap.instrumento_registro ir join hosp.instrumento_registro_procedimento_mensal irm on ir.id = irm.id_instrumento_registro " +
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
        } catch (Exception ex) {
            conexao.rollback();
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return listaInstrumentoRegistro;
    }

    public List<RENASESType> listaRenasesProcedimentoMensal
            (Integer ano, Integer mes, String codigoProcedimento, Connection conexao) throws SQLException {

        List<RENASESType> listaRenases = new ArrayList();
        String sql = "select rm.codigo, rm.nome " +
                "from sigtap.renases_mensal rm join hosp.renases_procedimento_mensal rpm on rm.id = rpm.id_renases_mensal " +
                "join sigtap.procedimento_mensal pm on pm.id = rpm.id_procedimento_mensal " +
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
                RENASESType renases = new RENASESType();
                renases.setCodigo(rs.getString("codigo"));
                renases.setNome(rs.getString("nome"));

                listaRenases.add(renases);
            }
        } catch (Exception ex) {
            conexao.rollback();
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return listaRenases;
    }

    public List<String> listaMesesIhAnosDoHistorico() {

        List<String> listaMesIhAno = new ArrayList();
        String sql = "select distinct concat (mes,' \\ ', ano) "
                + "as mes_ano from sigtap.historico_consumo_sigtap hcs order by mes_ano desc";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                String mesIhAno = rs.getString("mes_ano");
                listaMesIhAno.add(mesIhAno);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return listaMesIhAno;
    }
}
