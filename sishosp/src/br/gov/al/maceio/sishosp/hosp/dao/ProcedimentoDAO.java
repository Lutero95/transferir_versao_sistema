package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.CboBean;
import br.gov.al.maceio.sishosp.hosp.model.CidBean;
import br.gov.al.maceio.sishosp.hosp.model.ProcedimentoBean;
import br.gov.al.maceio.sishosp.hosp.model.dto.CboDTO;
import br.gov.al.maceio.sishosp.hosp.model.dto.CidDTO;
import br.gov.al.maceio.sishosp.hosp.model.dto.InstrumentoRegistroDTO;
import br.gov.al.maceio.sishosp.hosp.model.dto.ModalidadeAtendimentoDTO;
import br.gov.al.maceio.sishosp.hosp.model.dto.ServicoClassificacaoDTO;
import br.gov.saude.servicos.schema.cbo.v1.cbo.CBOType;
import br.gov.saude.servicos.schema.sigtap.procedimento.cid.v1.cid.CIDType;
import br.gov.saude.servicos.schema.sigtap.procedimento.financiamento.v1.tipofinanciamento.TipoFinanciamentoType;
import br.gov.saude.servicos.schema.sigtap.procedimento.nivelagregacao.v1.formaorganizacao.FormaOrganizacaoType;
import br.gov.saude.servicos.schema.sigtap.procedimento.nivelagregacao.v1.grupo.GrupoType;
import br.gov.saude.servicos.schema.sigtap.procedimento.nivelagregacao.v1.subgrupo.SubgrupoType;
import br.gov.saude.servicos.schema.sigtap.procedimento.renases.v1.renases.RENASESType;
import br.gov.saude.servicos.schema.sigtap.procedimento.servicoclassificacao.v1.servico.ServicoType;
import br.gov.saude.servicos.schema.sigtap.procedimento.servicoclassificacao.v1.servicoclassificacao.ServicoClassificacaoType;
import br.gov.saude.servicos.schema.sigtap.procedimento.v1.instrumentoregistro.InstrumentoRegistroType;
import br.gov.saude.servicos.schema.sigtap.procedimento.v1.modalidadeatendimento.ModalidadeAtendimentoType;
import br.gov.saude.servicos.schema.sigtap.procedimento.v1.procedimento.ProcedimentoType;

import javax.faces.context.FacesContext;

import org.apache.poi.hssf.dev.ReSave;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

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
    
    public Boolean gravarHistoricoConsumoSigtap(Long idFuncionario)
            throws ProjetoException {
        boolean gravado = false;
        String sql = "INSERT INTO hosp.historico_consumo_sigtap " + 
        		"(mes, ano, data_registro, id_funcionario) " + 
        		"VALUES(?, ?, current_timestamp, ?);";
        
        Integer mesAtual = buscaMesAtual();
        Integer anoAtual = buscaAnoAtual();
        try {
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, mesAtual);
            stm.setInt(2, anoAtual);
            stm.setLong(3, idFuncionario);
            stm.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return gravado;
    }
    

    public Integer buscaMesAtual() throws ProjetoException {
        Integer anoAtual = null;
        String sql = "select extract(month from current_date) as mes_atual";
        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);          
            ResultSet rs = ps.executeQuery();
            if(rs.next())
            	anoAtual = rs.getInt("mes_atual");
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
        return anoAtual;
    }
    
    public Integer buscaAnoAtual() throws ProjetoException {
        Integer anoAtual = null;
        String sql = "select extract(year from current_date) as ano_atual";
        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);          
            ResultSet rs = ps.executeQuery();
            if(rs.next())
            	anoAtual = rs.getInt("ano_atual");
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
        return anoAtual;
    }
    
    public List<String> buscaCodigoModalidadeAtendimento(Integer procedimentoId) throws ProjetoException {
        List<String> listaCodigoModaliadeAtendimento = new ArrayList();
        String sql = "SELECT ma.codigo " + 
        			 "FROM hosp.modalidade_atendimento ma "
        			 + "JOIN hosp.procedimento_mensal pm ON ma.id_procedimento_mensal = pm.id "
        			 + "AND pm.id_procedimento = ? ";
        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, procedimentoId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
            	listaCodigoModaliadeAtendimento.add(rs.getString("codigo"));
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
        return listaCodigoModaliadeAtendimento;
    }
    
    public List<String> buscaCodigoInstrumentosRegistro(Integer procedimentoId) throws ProjetoException {
        List<String> listaCodigoInstrumentoRegistro = new ArrayList();
        String sql = "SELECT ir.codigo " + 
        			 "FROM hosp.instrumento_registro ir "
        			 + "JOIN hosp.procedimento_mensal pm ON ir.id_procedimento_mensal = pm.id "
        			 + "AND pm.id_procedimento = ? ";
        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, procedimentoId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
            	listaCodigoInstrumentoRegistro.add(rs.getString("codigo"));
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
        return listaCodigoInstrumentoRegistro;
    }
    
    public List<String> buscaCodigoCbos(Integer procedimentoId) throws ProjetoException {
        List<String> listaCodigoCbo = new ArrayList();
        String sql = "SELECT cbo.codigo " + 
        			 "FROM hosp.cbo_procedimento_mensal cbo "
        			 + "JOIN hosp.procedimento_mensal pm ON cbo.id_procedimento_mensal = pm.id "
        			 + "AND pm.id_procedimento = ? ";
        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, procedimentoId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
            	listaCodigoCbo.add(rs.getString("codigo"));
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
        return listaCodigoCbo;
    }
    
    public List<String> buscaCodigoCids(Integer procedimentoId) throws ProjetoException {
        List<String> listaCodigoCid = new ArrayList();
        String sql = "SELECT cid.codigo " + 
        			 "FROM hosp.cid_procedimento_mensal cid "
        			 + "JOIN hosp.procedimento_mensal pm ON cid.id_procedimento_mensal = pm.id "
        			 + "AND pm.id_procedimento = ? ";
        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, procedimentoId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
            	listaCodigoCid.add(rs.getString("codigo"));
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
        return listaCodigoCid;
    }
    
    public List<ServicoClassificacaoType> buscarServicosClassificacao(Integer procedimentoId) throws ProjetoException {
        List<ServicoClassificacaoType> listaServicoClassificacao = new ArrayList();
        String sql = "SELECT cm.nome nome_classificacao, cm.codigo codigo_classificacao, "+
        		"sm.codigo codigo_servico, sm.nome nome_servico " + 
        		"FROM hosp.servico_classificacao_mensal sc " + 
        		"JOIN hosp.procedimento_mensal pm ON sc.id_procedimento_mensal = pm.id " + 
        		"join hosp.classificacao_mensal cm on cm.id = sc.id_classificacao " + 
        		"join hosp.servico_mensal sm on sm.id = sc.id_servico " + 
        		"AND pm.id_procedimento = ?";
        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);          
            ps.setInt(1, procedimentoId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
            	ServicoClassificacaoType servicoClassificacao = new ServicoClassificacaoType();
            	servicoClassificacao.setNomeClassificacao(rs.getString("nome_classificacao"));
            	servicoClassificacao.setCodigoClassificacao(rs.getString("codigo_classificacao"));
            	servicoClassificacao.getServico().setNome(rs.getString("nome_servico"));
            	servicoClassificacao.getServico().setCodigo(rs.getString("codigo_servico"));
            	listaServicoClassificacao.add(servicoClassificacao);
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
        return listaServicoClassificacao;
    }
    
    public void executaRotinaNovaCargaSigtap() throws Exception {
    	
    	try {
    		this.con = ConnectionFactory.getConnection();
    		
		} catch (Exception e) {
			throw e;
		}
    }
    
	public Integer inserirTipoFinanciamento(TipoFinanciamentoType tipoFinanciamento, Connection conexao) throws ProjetoException, SQLException {
		String sql = "INSERT INTO hosp.tipo_financiamento (codigo, nome) "
				+ "VALUES(?, ?) returning id";
		Integer idTipoFinanciamento = null;
		try {
			ps = conexao.prepareStatement(sql);
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
    
	public Integer inserirRenases(RENASESType renases, Connection conexao) throws ProjetoException, SQLException {
		String sql = "INSERT INTO hosp.renases_mensal (codigo, nome) "
				+ "VALUES(?, ?) returning id";
		Integer idRenases = null;
		try {
			ps = conexao.prepareStatement(sql);
			ps.setString(1, renases.getCodigo());
			ps.setString(2, renases.getNome());
			ResultSet rs = ps.executeQuery();
			if(rs.next())
				idRenases = rs.getInt("id");
		} catch (Exception ex) {
			conexao.rollback();
			ex.printStackTrace();
			throw new ProjetoException(ex);
		}
		return idRenases;
	}
	
	public Integer inserirFormaOrganizacao(FormaOrganizacaoType formaOrganizacao, Connection conexao) throws ProjetoException, SQLException {
		String sql = "INSERT INTO hosp.forma_de_organizacao " + 
				"(codigo, nome, id_subgrupo_mensal) " + 
				"VALUES(?, ?, ?) returning id ";
		Integer idFormaOrganizacao = null;
		try {
			ps = conexao.prepareStatement(sql);
			Integer idSubgrupo = inserirSubgrupoMensal(formaOrganizacao.getSubgrupo(), conexao);
			ps.setString(1, formaOrganizacao.getCodigo());
			ps.setString(2, formaOrganizacao.getNome());
			ps.setInt(3, idSubgrupo);
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
	
	public Integer inserirSubgrupoMensal(SubgrupoType subgrupo, Connection conexao) throws ProjetoException, SQLException {
		String sql = "INSERT INTO hosp.subgrupo_mensal " + 
				"(codigo, nome, id_grupo_mensal) " + 
				"VALUES(?, ?, ?) returning id ";
		Integer idSubgrupo = null;
		try {
			ps = conexao.prepareStatement(sql);
			Integer idGrupo = inserirGrupoMensal(subgrupo.getGrupo(), conexao);
			
			ps.setString(1, subgrupo.getCodigo());
			ps.setString(2, subgrupo.getNome());
			ps.setInt(3, idGrupo);
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
	
	public Integer inserirGrupoMensal(GrupoType grupo, Connection conexao) throws ProjetoException, SQLException {
		String sql = "INSERT INTO hosp.grupo_mensal " + 
				"(codigo, nome) " + 
				"VALUES(?, ?) returning id ";
		Integer idGrupo = null;
		try {
			ps = conexao.prepareStatement(sql);
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
	
	public Integer inserirProcedimentoMensal(ProcedimentoType procedimentoMensal, Integer idProcedimentoDoSistema, Connection conexao)
			throws ProjetoException, SQLException {
		String sql = "INSERT INTO hosp.procedimento_mensal " + 
				"(id_procedimento, codigo_procedimento, nome, competencia, complexidade, id_tipo_financiamento, "+
				"sexo, quantidade_maxima, idade_minima, unidade_idade_minima, idade_maxima, unidade_idade_maxima, "+
				"servico_ambulatorial, servico_hospitalar, servico_profisional, id_forma_de_organizacao, id_renases, descricao) " + 
				"VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning id; "; 
				
		Integer idProcedimentoMensal = null;
		try {
			ps = conexao.prepareStatement(sql);
			ps.setInt(1, idProcedimentoDoSistema);
			ps.setString(2, procedimentoMensal.getCodigo());
			ps.setString(3, procedimentoMensal.getNome());
			ps.setString(4, procedimentoMensal.getCompetenciaInicial()); //VERIFICAR SE É A INICIAL OU FINAL OU AMBAS E ALTERAR NO BANCO
			ps.setString(5, procedimentoMensal.getComplexidade().name());
			
			Integer idTipoFinanciamento = inserirTipoFinanciamento(procedimentoMensal.getTipoFinanciamento(), conexao);
			ps.setInt(6, idTipoFinanciamento);
			
			ps.setString(7, procedimentoMensal.getSexoPermitido());
			ps.setInt(8, procedimentoMensal.getQuantidadeMaxima());
			ps.setInt(9, procedimentoMensal.getIdadeMinimaPermitida().getQuantidadeLimite());
			ps.setString(10, procedimentoMensal.getIdadeMinimaPermitida().getUnidadeLimite().name());
			ps.setInt(11, procedimentoMensal.getIdadeMaximaPermitida().getQuantidadeLimite());
			ps.setString(12, procedimentoMensal.getIdadeMaximaPermitida().getUnidadeLimite().name());
			ps.setDouble(13, procedimentoMensal.getValorSA().doubleValue());
			ps.setDouble(14, procedimentoMensal.getValorSH().doubleValue());
			ps.setDouble(15, procedimentoMensal.getValorSP().doubleValue());
			
			Integer idFormaOrganizacao = inserirFormaOrganizacao(procedimentoMensal.getFormaOrganizacao(), conexao);
			ps.setInt(16, idFormaOrganizacao);
			
			Integer idRenases = inserirRenases(procedimentoMensal.getRENASESVinculadas().getRENASES().get(0), conexao);
			ps.setInt(17, idRenases);
			
			ps.setString(18, procedimentoMensal.getDescricao());
			ResultSet rs = ps.executeQuery();
			if(rs.next())
				idProcedimentoMensal = rs.getInt("id");
		} catch (Exception ex) {
			conexao.rollback();
			ex.printStackTrace();
			throw new ProjetoException(ex);
		}
		return idProcedimentoMensal;
	}	
    
    public void inserirModalidadeAtendimento
    	(List<ModalidadeAtendimentoType> modalidadesAtendimento, Integer idProcedimentoMensal, Connection conexao) 
    			throws ProjetoException, SQLException {
        String sql = "INSERT INTO hosp.modalidade_atendimento " + 
        		"(codigo, nome, id_procedimento_mensal) " + 
        		"VALUES(?, ?, ?) ";
        try {
        	for(ModalidadeAtendimentoType modalidadeAtendimentoType : modalidadesAtendimento) {
        		ps = conexao.prepareStatement(sql);
        		ps.setString(1, modalidadeAtendimentoType.getCodigo());
        		ps.setString(2, modalidadeAtendimentoType.getNome());
        		ps.setInt(3, idProcedimentoMensal);
        		ps.executeUpdate();
        	}	
        } catch (Exception ex) {
        	conexao.rollback();
            ex.printStackTrace();
            throw new ProjetoException(ex);
        }
    }
    
    public void inserirInstrumentosRegistro
	(List<InstrumentoRegistroType> instrumentosRegistro, Integer idProcedimentoMensal, Connection conexao) 
			throws ProjetoException, SQLException {
    String sql = "INSERT INTO hosp.instrumento_registro " + 
    		"(codigo, nome, id_procedimento_mensal) " + 
    		"VALUES(?, ?, ?) ";
    try {
    	for(InstrumentoRegistroType instrumentoRegistroType : instrumentosRegistro) {
    		ps = conexao.prepareStatement(sql);
    		ps.setString(1, instrumentoRegistroType.getCodigo());
    		ps.setString(2, instrumentoRegistroType.getNome());
    		ps.setInt(3, idProcedimentoMensal);
    		ps.executeUpdate();
    	}	
    } catch (Exception ex) {
    	conexao.rollback();
        ex.printStackTrace();
        throw new ProjetoException(ex);
    }
}
    
    public void inserirCBOs(List<CBOType> cbos, Integer idProcedimentoMensal, Connection conexao) throws ProjetoException, SQLException {
        String sql = "INSERT INTO hosp.cbo_procedimento_mensal  " + 
        		"(codigo, nome, id_procedimento_mensal) " + 
        		"VALUES(?, ?, ?) ";
        try {
        	for(CBOType cboType : cbos) {
        		ps = conexao.prepareStatement(sql);
        		ps.setString(1, cboType.getCodigo());
        		ps.setString(2, cboType.getNome());
        		ps.setInt(3, idProcedimentoMensal);
        		ps.executeUpdate();
        	}	
        } catch (Exception ex) {
        	conexao.rollback();
            ex.printStackTrace();
            throw new ProjetoException(ex);
        }
    }
    
    public void inserirCIDs(List<CIDType> cids, Integer idProcedimentoMensal, Connection conexao) throws ProjetoException, SQLException {
        String sql = "INSERT INTO hosp.cid_procedimento_mensal " + 
        		"(codigo, nome, agravo, sexo_aplicavel, estadio, quantidade_campos_irradiados, id_procedimento_mensal) " + 
        		"VALUES(?, ?, ?, ?, ?, ?, ?);";
        try {
        	for(CIDType cidType : cids) {
        		ps = conexao.prepareStatement(sql);
        		ps.setString(1, cidType.getCodigo());
        		ps.setString(2, cidType.getNome());
        		ps.setString(3, cidType.getAgravo().name());
        		ps.setString(4, cidType.getSexoAplicavel());
        		ps.setBoolean(5, cidType.isEstadio());
        		ps.setInt(6, cidType.getQuantidadeCamposIrradiados());
        		ps.setInt(7, idProcedimentoMensal);
        		ps.executeUpdate();
        	}	
        } catch (Exception ex) {
        	conexao.rollback();
            ex.printStackTrace();
            throw new ProjetoException(ex);
        }
    }
    
    public void inserirServicoClassificacao
    	(List<ServicoClassificacaoType> servicosClassificacoesType, Integer idProcedimentoMensal, Connection conexao) throws ProjetoException, SQLException {
        String sql = "INSERT INTO hosp.servico_classificacao_mensal " + 
        		"(id_servico, id_classificacao, id_procedimento_mensal) " + 
        		"VALUES(?, ?, ?);";
        try {
        	for(ServicoClassificacaoType servicoClassificacaoType : servicosClassificacoesType) {
        		Long idServico = inserirServico(servicoClassificacaoType.getServico(), conexao);
        		Long idClassificacao = inserirClassificacao
        				(servicoClassificacaoType.getCodigoClassificacao(), servicoClassificacaoType.getNomeClassificacao(), conexao);
        		ps = conexao.prepareStatement(sql);
        		ps.setLong(1, idServico);
        		ps.setLong(2, idClassificacao);
        		ps.setInt(3, idProcedimentoMensal);
        		ps.executeUpdate();
        	}	
        } catch (Exception ex) {
        	conexao.rollback();
            ex.printStackTrace();
            throw new ProjetoException(ex);
        }
    }
    
    public Long inserirServico(ServicoType servicoType, Connection conexao) throws ProjetoException, SQLException {
        String sql = "INSERT INTO hosp.servico_mensal " + 
        		"(codigo, nome) VALUES(?, ?) returning id; ";
        Long idServico = null;
        try {
        	ps = conexao.prepareStatement(sql);
        	ps.setString(1, servicoType.getCodigo());
       		ps.setString(2, servicoType.getNome());
        	ResultSet rs = ps.executeQuery();	
        	if(rs.next())
        		idServico = rs.getLong("id");
        } catch (Exception ex) {
        	conexao.rollback();
            ex.printStackTrace();
            throw new ProjetoException(ex);
        }
        return idServico;
    }
    
    public Long inserirClassificacao(String codigo, String nome, Connection conexao) throws ProjetoException, SQLException {
        String sql = "INSERT INTO hosp.classificacao_mensal " + 
        		"(codigo, nome) VALUES(?, ?) returning id; ";
        Long idClassificacao = null;
        try {
        	ps = conexao.prepareStatement(sql);
        	ps.setString(1, codigo);
       		ps.setString(2, nome);
        	ResultSet rs = ps.executeQuery();	
        	if(rs.next())
        		idClassificacao = rs.getLong("id");
        } catch (Exception ex) {
        	conexao.rollback();
            ex.printStackTrace();
            throw new ProjetoException(ex);
        }
        return idClassificacao;
    }
}
