package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.xml.ws.soap.SOAPFaultException;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.model.CboBean;
import br.gov.al.maceio.sishosp.hosp.model.CidBean;
import br.gov.al.maceio.sishosp.hosp.model.HistoricoSigtapBean;
import br.gov.al.maceio.sishosp.hosp.model.ProcedimentoBean;
import br.gov.al.maceio.sishosp.hosp.model.dto.GravarProcedimentoMensalDTO;
import br.gov.al.maceio.sishosp.hosp.model.dto.PropriedadeDeProcedimentoMensalExistenteDTO;
import sigtap.br.gov.saude.servicos.schema.cbo.v1.cbo.CBOType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.financiamento.v1.tipofinanciamento.TipoFinanciamentoType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.nivelagregacao.v1.formaorganizacao.FormaOrganizacaoType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.nivelagregacao.v1.grupo.GrupoType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.nivelagregacao.v1.subgrupo.SubgrupoType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.renases.v1.renases.RENASESType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.servicoclassificacao.v1.servico.ServicoType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.servicoclassificacao.v1.servicoclassificacao.ServicoClassificacaoType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.v1.instrumentoregistro.InstrumentoRegistroType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.v1.modalidadeatendimento.ModalidadeAtendimentoType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.v1.procedimento.ProcedimentoType.CIDsVinculados.CIDVinculado;


public class ProcedimentoDAO {
    Connection con = null;
    PreparedStatement ps = null;

    FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
            .getSessionMap().get("obj_funcionario");

    public boolean gravarProcedimento(ProcedimentoBean proc) {

        Boolean retorno = false;

        String sql = "INSERT INTO hosp.proc (codproc, nome, auditivo, tipo_exame_auditivo, utiliza_equipamento, gera_laudo_digita, validade_laudo, "
                + "idade_minima, idade_maxima, qtd_maxima, prazo_minimo_nova_execucao, sexo, cod_unidade)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id;";
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
            
            if (proc.getIdadeMinima() == null) {
                ps.setNull(8, Types.NULL);
            } else {
                ps.setInt(8, proc.getIdadeMinima());
            }
            
            if (proc.getIdadeMaxima() == null) {
                ps.setNull(9, Types.NULL);
            } else {
                ps.setInt(9, proc.getIdadeMaxima());
            }

            if (proc.getQtdMaxima() == null) {
                ps.setNull(10, Types.NULL);
            } else {
                ps.setInt(10, proc.getQtdMaxima());
            }

            if (proc.getPrazoMinimoNovaExecucao() == null) {
                ps.setNull(11, Types.NULL);
            } else {
                ps.setInt(11, proc.getPrazoMinimoNovaExecucao());
            }            


            ps.setString(12, proc.getSexo());
            ps.setInt(13, user_session.getUnidade().getId());

            ResultSet rs = ps.executeQuery();

            int idProc = 0;
            if (rs.next()) {
                idProc = rs.getInt("id");
            }

            sql = "INSERT INTO hosp.proc_cbo (id_proc, id_cbo) VALUES (?, ?);";
            ps = con.prepareStatement(sql);
            for (int i = 0; i < proc.getListaCbo().size(); i++) {
                ps.setInt(1, idProc);
                ps.setInt(2, proc.getListaCbo().get(i).getCodCbo());
                ps.execute();
            }

            sql = "INSERT INTO hosp.proc_cid (id_proc, id_cid) VALUES (?, ?);";
            ps = con.prepareStatement(sql);
            for (int i = 0; i < proc.getListaCid().size(); i++) {
                ps.setInt(1, idProc);
                ps.setInt(2, proc.getListaCid().get(i).getIdCid());
                ps.execute();
            }

            sql = "INSERT INTO hosp.proc_recurso (id_proc, id_recurso) VALUES (?, ?);";
            ps = con.prepareStatement(sql);
            for (int i = 0; i < proc.getListaRecurso().size(); i++) {
                ps.setInt(1, idProc);
                ps.setInt(2, proc.getListaRecurso().get(i).getIdRecurso());
                ps.execute();
            }

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
            return retorno;
        }
    }

    public boolean alterarProcedimento(ProcedimentoBean proc) {

        Boolean retorno = false;

        String sql = "update hosp.proc set nome = ?, auditivo = ?, tipo_exame_auditivo = ?, utiliza_equipamento = ?, "
                + "gera_laudo_digita = ?, validade_laudo = ?, codproc = ?, idade_minima = ?, idade_maxima = ?, qtd_maxima = ?, "
                + "prazo_minimo_nova_execucao = ?, sexo = ? "
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
            stmt.setInt(8, proc.getIdadeMinima());
            stmt.setInt(9, proc.getIdadeMaxima());
            stmt.setInt(10, proc.getQtdMaxima());
            stmt.setInt(11, proc.getPrazoMinimoNovaExecucao());
            stmt.setString(12, proc.getSexo());
            stmt.setInt(13, proc.getIdProc());

            stmt.executeUpdate();

            sql = "delete from hosp.proc_cbo where id_proc = ?";
            stmt = con.prepareStatement(sql);
            stmt.setLong(1, proc.getIdProc());
            stmt.execute();

            sql = "delete from hosp.proc_cid where id_proc = ?";
            stmt = con.prepareStatement(sql);
            stmt.setLong(1, proc.getIdProc());
            stmt.execute();

            sql = "delete from hosp.proc_recurso where id_proc = ?";
            stmt = con.prepareStatement(sql);
            stmt.setLong(1, proc.getIdProc());
            stmt.execute();

            sql = "INSERT INTO hosp.proc_cbo (id_proc, id_cbo) VALUES (?, ?);";
            ps = con.prepareStatement(sql);
            for (int i = 0; i < proc.getListaCbo().size(); i++) {
                ps.setInt(1, proc.getIdProc());
                ps.setInt(2, proc.getListaCbo().get(i).getCodCbo());
                ps.execute();
            }

            sql = "INSERT INTO hosp.proc_cid (id_proc, id_cid) VALUES (?, ?);";
            ps = con.prepareStatement(sql);
            for (int i = 0; i < proc.getListaCid().size(); i++) {
                ps.setInt(1, proc.getIdProc());
                ps.setInt(2, proc.getListaCid().get(i).getIdCid());
                ps.execute();
            }

            sql = "INSERT INTO hosp.proc_recurso (id_proc, id_recurso) VALUES (?, ?);";
            ps = con.prepareStatement(sql);
            for (int i = 0; i < proc.getListaRecurso().size(); i++) {
                ps.setInt(1, proc.getIdProc());
                ps.setInt(2, proc.getListaRecurso().get(i).getIdRecurso());
                ps.execute();
            }

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
            return retorno;
        }
    }

    public boolean excluirProcedimento(ProcedimentoBean proc){

        Boolean retorno = false;

        try {
            con = ConnectionFactory.getConnection();

            String sql = "delete from hosp.proc_cbo where id_proc = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setLong(1, proc.getIdProc());
            stmt.execute();

            sql = "delete from hosp.proc_recurso where id_proc = ?";
            stmt = con.prepareStatement(sql);
            stmt.setLong(1, proc.getIdProc());
            stmt.execute();

            sql = "delete from hosp.proc_cid where id_proc = ?";
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
            return retorno;
        }
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
        String sql = "select c.cod, c.cid, c.desccid from hosp.cid c left join hosp.proc_cid p on (c.cod = p.id_cid) where id_proc = ? order by cid";

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
        String sql = "select c.id, c.descricao from hosp.cbo c left join hosp.proc_cbo p on (c.id = p.id_cbo) where id_proc = ? order by descricao";

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
    
    
    
	public void executaRotinaNovaCargaSigtap(List<GravarProcedimentoMensalDTO> listaProcedimentoMensalDTO,
			Long idFuncionario) throws Exception {

		String codigoProcedimentoQueDeuErro = "";
		try {
			this.con = ConnectionFactory.getConnection();
			
			Integer idHistoricoConsumoSigtap = gravarHistoricoConsumoSigtap(idFuncionario, con);
			desatualizaStatusHistoricoConsumoSigtapAnterior(idHistoricoConsumoSigtap, con);
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
		} catch (Exception ex) {
			conexao.rollback();
			ex.printStackTrace();
			throw new ProjetoException(ex);
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
		} catch (Exception ex) {
			conexao.rollback();
			ex.printStackTrace();
			throw new ProjetoException(ex);
		}
		return idTipoFinanciamento;
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
		} catch (Exception ex) {
			conexao.rollback();
			ex.printStackTrace();
			throw new ProjetoException(ex);
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
	
	public Integer inserirFormaOrganizacao(FormaOrganizacaoType formaOrganizacao, Connection conexao) throws ProjetoException, SQLException {

		if(VerificadorUtil.verificarSeObjetoNulo(formaOrganizacao))
			return null;
		
		String sql = "INSERT INTO sigtap.forma_de_organizacao " + 
				"(codigo, nome, id_subgrupo) " + 
				"VALUES(?, ?, ?) returning id ";

		Integer idFormaOrganizacao = retornaIdFormaOrganizacaoCasoExista(formaOrganizacao.getCodigo(), conexao);
		try {
			if(VerificadorUtil.verificarSeObjetoNuloOuZero(idFormaOrganizacao)) {
				PreparedStatement ps = conexao.prepareStatement(sql);
				ps.setString(1, formaOrganizacao.getCodigo());
				ps.setString(2, formaOrganizacao.getNome());
				Integer idSubgrupoExistente = retornaIdSubgrupoCasoExista(formaOrganizacao.getSubgrupo().getCodigo(),
						conexao);
				if (!VerificadorUtil.verificarSeObjetoNuloOuZero(idSubgrupoExistente))
					ps.setInt(3, idSubgrupoExistente);
				else {
					Integer idSubgrupo = inserirSubgrupoMensal(formaOrganizacao.getSubgrupo(), conexao);
					ps.setInt(3, idSubgrupo);
				}
				ResultSet rs = ps.executeQuery();
				if (rs.next())
					idFormaOrganizacao = rs.getInt("id");
			}
		} catch (Exception ex) {
			conexao.rollback();
			ex.printStackTrace();
			throw new ProjetoException(ex);
		}
		return idFormaOrganizacao;
	}
	
	public Integer retornaIdFormaOrganizacaoCasoExista(String codigo, Connection conexao) 
			throws SQLException, ProjetoException {
		String sql = "select id from sigtap.forma_de_organizacao where codigo = ?";
		Integer idFormaOrganizacao = 0;
		try {
			PreparedStatement ps = conexao.prepareStatement(sql);
			
			ps.setString(1, codigo);
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
	
	private Integer retornaIdSubgrupoCasoExista(String codigo, Connection conexao) throws SQLException, ProjetoException {
		String sql = "select id from sigtap.subgrupo where codigo = ?";
		Integer idSubgrupo = 0;
		try {
			PreparedStatement ps = conexao.prepareStatement(sql);
			
			ps.setString(1, codigo);
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
		String sql = "INSERT INTO sigtap.subgrupo " + 
				"(codigo, nome, id_grupo) " + 
				"VALUES(?, ?, ?) returning id ";
		Integer idSubgrupo = null;
		try {
			PreparedStatement ps = conexao.prepareStatement(sql);
			
			ps.setString(1, subgrupo.getCodigo());
			ps.setString(2, subgrupo.getNome());
			Integer idGrupoExistente = retornaIdGrupoCasoExista(subgrupo.getCodigo(), conexao);
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
	
	private Integer retornaIdGrupoCasoExista(String codigo, Connection conexao) throws SQLException, ProjetoException {
		String sql = "select id from sigtap.grupo where codigo = ?";
		Integer idGrupo = 0;
		try {
			PreparedStatement ps = conexao.prepareStatement(sql);
			ps.setString(1, codigo);
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
		String sql = "INSERT INTO sigtap.grupo " + 
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
				"(id_procedimento, codigo_procedimento, nome, competencia_inicial, complexidade, id_tipo_financiamento, "+
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
			stm.setString(4, procedimentoMensalDTO.getProcedimentoMensal().getCompetenciaInicial()); //VERIFICAR SE É A INICIAL OU FINAL OU AMBAS E ALTERAR NO BANCO
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
			
			
			Integer idFormaOrganizacao = inserirFormaOrganizacao(procedimentoMensalDTO.getProcedimentoMensal().getFormaOrganizacao(), conexao);
			if(VerificadorUtil.verificarSeObjetoNuloOuZero(idFormaOrganizacao))
				stm.setNull(16, Types.NULL);
			else
				stm.setInt(16, idFormaOrganizacao);
			
			stm.setString(17, procedimentoMensalDTO.getProcedimentoMensal().getDescricao());
			stm.setInt(18, idHistoricoConsumoSigtap);
			ResultSet rs = stm.executeQuery();
			if(rs.next())
				idProcedimentoMensal = rs.getInt("id");
		} catch (Exception ex) {
			System.err.println("(procedimentoMensalDTO"+procedimentoMensalDTO);
			System.err.println("(procedimentoMensalDTO.getProcedimentoMensal()"+procedimentoMensalDTO.getProcedimentoMensal());
			System.err.println("(procedimentoMensalDTO.getProcedimentoMensal().getIdadeMinimaPermitida()"+procedimentoMensalDTO.getProcedimentoMensal().getIdadeMinimaPermitida());
			conexao.rollback();
			ex.printStackTrace();
			throw new ProjetoException(ex);
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
        } catch (Exception ex) {
        	conexao.rollback();
            ex.printStackTrace();
            throw new ProjetoException(ex);
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
		} catch (Exception ex) {
			conexao.rollback();
			ex.printStackTrace();
			throw new ProjetoException(ex);
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
        	
        	for(Integer idCboExistente : listaIdCbosExistentesDoProcedimento) {
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
        		stm.setString(1, cidVinculado.getCID().getCodigo());
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
        } catch (Exception ex) {
        	conexao.rollback();
            ex.printStackTrace();
            throw new ProjetoException(ex);
        }
    }
    
	private void filtraCidsExistentes(List<CIDVinculado> listaCids,
			List<PropriedadeDeProcedimentoMensalExistenteDTO> todosCidsExistentes,
			List<Integer> listaIdCidsExistentesDoProcedimento, List<CIDVinculado> listaCidsAux) {
		
		for (CIDVinculado cid : listaCidsAux) {
			for (PropriedadeDeProcedimentoMensalExistenteDTO cidExistente : todosCidsExistentes) {
				if(cid.getCID().getCodigo().equals(cidExistente.getCodigo())) {
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
        } catch (Exception ex) {
        	conexao.rollback();
            ex.printStackTrace();
            throw new ProjetoException(ex);
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
        } catch (Exception ex) {
        	conexao.rollback();
            ex.printStackTrace();
            throw new ProjetoException(ex);
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
        } catch (Exception ex) {
        	conexao.rollback();
            ex.printStackTrace();
            throw new ProjetoException(ex);
        }
        return idClassificacao;
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
    
    public List<PropriedadeDeProcedimentoMensalExistenteDTO> buscaFormasOrganizacaoExistentes(Connection con) throws ProjetoException {
        List<PropriedadeDeProcedimentoMensalExistenteDTO> listaFormaOrganizacaoExistente = new ArrayList();
        String sql = "SELECT fo.id, fo.codigo FROM sigtap.forma_de_organizacao fo ";
        try {
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
        }
        return listaFormaOrganizacaoExistente;
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
}
