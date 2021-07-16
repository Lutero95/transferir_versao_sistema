package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.administrativo.model.InsercaoProfissionalEquipe;
import br.gov.al.maceio.sishosp.administrativo.model.RemocaoProfissionalEquipe;
import br.gov.al.maceio.sishosp.administrativo.model.SubstituicaoProfissional;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.TratamentoErrosUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.enums.TipoGravacaoHistoricoPaciente;
import br.gov.al.maceio.sishosp.hosp.model.AtendimentoBean;
import br.gov.al.maceio.sishosp.hosp.model.GerenciarPacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.Liberacao;
import br.gov.al.maceio.sishosp.hosp.model.PacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.dto.ProcedimentoCidDTO;
import br.gov.al.maceio.sishosp.hosp.model.dto.SubstituicaoProfissionalEquipeDTO;

public class GerenciarPacienteDAO {

    PreparedStatement ps = null;
    private Connection conexao = null;

    FuncionarioBean user_session = (FuncionarioBean) FacesContext
            .getCurrentInstance().getExternalContext().getSessionMap()
            .get("obj_usuario");


    public List<GerenciarPacienteBean> carregarPacientesInstituicaoBusca(
            GerenciarPacienteBean gerenciar, String campoBusca, String tipoBusca) throws ProjetoException {

        String sql = "select p.id, p.codprograma, prog.descprograma, prog.permite_paciente_sem_laudo, p.codgrupo, g.descgrupo, coalesce(gp.qtdfrequencia,0) qtdfrequencia, coalesce(l.codpaciente, p.id_paciente) codpaciente, pa.nome, pa.matricula, pa.cns, p.codequipe, e.descequipe, "
                + " p.codprofissional, f.descfuncionario, p.status, p.codlaudo, p.data_solicitacao, p.observacao, p.data_cadastro, pr.utiliza_equipamento, pr.codproc , pr.nome as procedimento, "
                + "coalesce((SELECT * FROM hosp.fn_GetLastDayOfMonth(to_date(ano_final||'-'||'0'||''||mes_final||'-'||'01', 'YYYY-MM-DD'))),\n" +
                "  p.data_solicitacao +coalesce(p2.validade_padrao_laudo,0) ) as datafinal, p.inclusao_sem_laudo "
                + " from hosp.paciente_instituicao p "
                + " left join hosp.laudo l on (l.id_laudo = p.codlaudo) "
                + " left join hosp.proc pr on (pr.id = coalesce(l.codprocedimento_primario, p.codprocedimento_primario_laudo_anterior)) "
                + " left join hosp.pacientes pa on (coalesce(l.codpaciente, p.id_paciente) = pa.id_paciente) "
                + " left join hosp.equipe e on (p.codequipe = e.id_equipe) "
                + " left join acl.funcionarios f on (p.codprofissional = f.id_funcionario) "
                + " left join hosp.grupo g on (g.id_grupo = p.codgrupo) "
                + " left join hosp.programa prog on (prog.id_programa = p.codprograma) "
                + " join hosp.parametro p2 on p2.codunidade  = p.cod_unidade  "
                + " left join hosp.grupo_programa gp on (g.id_grupo = gp.codgrupo and  prog.id_programa = gp.codprograma) "
                + " where p.cod_unidade = ? ";
        
        if (!VerificadorUtil.verificarSeObjetoNulo(gerenciar.getPrograma()) 
        		&& !VerificadorUtil.verificarSeObjetoNuloOuZero(gerenciar.getPrograma().getIdPrograma())) {
            sql = sql + " and  p.codprograma = ?";
        }
        if (!VerificadorUtil.verificarSeObjetoNulo(gerenciar.getGrupo()) 
        		&& !VerificadorUtil.verificarSeObjetoNuloOuZero(gerenciar.getGrupo().getIdGrupo())) {
            sql = sql + " and  p.codgrupo = ?";
        }
        
        if (!VerificadorUtil.verificarSeObjetoNulo(gerenciar.getEquipe()) 
        		&& !VerificadorUtil.verificarSeObjetoNuloOuZero(gerenciar.getEquipe().getCodEquipe())) {
            sql = sql + " and  p.codequipe = ?";
        }

        if ((tipoBusca.equals("paciente") && !VerificadorUtil.verificarSeObjetoNuloOuVazio(campoBusca))) {
            sql = sql + " and pa.nome ilike ?";
        }

        if ((tipoBusca.equals("codproc") && !VerificadorUtil.verificarSeObjetoNuloOuVazio(campoBusca))) {
            sql = sql + " and pr.codproc = ?";
        }

        if ((tipoBusca.equals("matpaciente") && !VerificadorUtil.verificarSeObjetoNuloOuVazio(campoBusca))) {
            sql = sql + " and upper(pa.matricula) = ?";
        }

        if ((tipoBusca.equals("prontpaciente") && !VerificadorUtil.verificarSeObjetoNuloOuVazio(campoBusca))) {
            sql = sql + " and pa.id_paciente = ?";
        }


        if ((gerenciar.getStatus()!=null ) && (gerenciar.getStatus().equals("A"))) {
            sql = sql + " and status = 'A'";
        }

        if ((gerenciar.getStatus()!=null ) && (gerenciar.getStatus().equals("D"))) {
            sql = sql + " and status = 'D'";
        }
        sql = sql + " order by pa.nome";

        List<GerenciarPacienteBean> lista = new ArrayList<>();

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, user_session.getUnidade().getId());
            int i = 2;
            if (!VerificadorUtil.verificarSeObjetoNulo(gerenciar.getPrograma()) 
            		&& !VerificadorUtil.verificarSeObjetoNuloOuZero(gerenciar.getPrograma().getIdPrograma())) {
                stmt.setInt(i, gerenciar.getPrograma().getIdPrograma());
                i++;
            }
            if (!VerificadorUtil.verificarSeObjetoNulo(gerenciar.getGrupo()) 
            		&& !VerificadorUtil.verificarSeObjetoNuloOuZero(gerenciar.getGrupo().getIdGrupo())) {
                stmt.setInt(i, gerenciar.getGrupo().getIdGrupo());
                i++;
            }
            if (!VerificadorUtil.verificarSeObjetoNulo(gerenciar.getEquipe()) 
            		&& !VerificadorUtil.verificarSeObjetoNuloOuZero(gerenciar.getEquipe().getCodEquipe())) {
            	stmt.setInt(i, gerenciar.getEquipe().getCodEquipe());
            	i++;
            }
            
            if ((tipoBusca.equals("paciente") && !VerificadorUtil.verificarSeObjetoNuloOuVazio(campoBusca))) {
                stmt.setString(i, "%" + campoBusca.toUpperCase() + "%");
                i++;
            }

            if (((tipoBusca.equals("codproc") || (tipoBusca.equals("matpaciente"))) && !VerificadorUtil.verificarSeObjetoNuloOuVazio(campoBusca))) {
                stmt.setString(i, campoBusca.toUpperCase());
                i++;
            }

            if ((tipoBusca.equals("prontpaciente") && !VerificadorUtil.verificarSeObjetoNuloOuVazio(campoBusca))) {
                stmt.setInt(i,Integer.valueOf((campoBusca.toUpperCase())));
                i++;
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                GerenciarPacienteBean gerenciarPaciente = new GerenciarPacienteBean();

                gerenciarPaciente.setId(rs.getInt("id"));
                gerenciarPaciente.setInclusaoSemLaudo(rs.getBoolean("inclusao_sem_laudo"));
                gerenciarPaciente.getPrograma().setIdPrograma(rs.getInt("codprograma"));
                gerenciarPaciente.getPrograma().setDescPrograma(rs.getString("descprograma"));
                gerenciarPaciente.getPrograma().setPermitePacienteSemLaudo(rs.getBoolean("permite_paciente_sem_laudo"));
                gerenciarPaciente.getGrupo().setIdGrupo(rs.getInt("codgrupo"));
                gerenciarPaciente.getGrupo().setDescGrupo(rs.getString("descgrupo"));
                //gerenciarPaciente.getGrupo().setQtdFrequencia(rs.getInt("qtdfrequencia"));
                gerenciarPaciente.getEquipe().setCodEquipe(rs.getInt("codequipe"));
                gerenciarPaciente.getEquipe().setDescEquipe(rs.getString("descequipe"));
                gerenciarPaciente.getFuncionario().setId(rs.getLong("codprofissional"));
                gerenciarPaciente.getFuncionario().setNome(rs.getString("descfuncionario"));
                gerenciarPaciente.setStatus(rs.getString("status"));
                gerenciarPaciente.getLaudo().setId(rs.getInt("codlaudo"));
                gerenciarPaciente.getLaudo().getProcedimentoPrimario().setCodProc(rs.getString("codproc"));
                gerenciarPaciente.getLaudo().getProcedimentoPrimario().setNomeProc(rs.getString("procedimento"));
                gerenciarPaciente.getLaudo().getPaciente().setId_paciente(rs.getInt("codpaciente"));
                gerenciarPaciente.getLaudo().getPaciente().setNome(rs.getString("nome"));
                gerenciarPaciente.getLaudo().getPaciente().setMatricula(rs.getString("matricula"));
                gerenciarPaciente.getLaudo().getPaciente().setCns(rs.getString("cns"));
                gerenciarPaciente.getLaudo().setVigenciaFinal(rs.getDate("datafinal"));
                gerenciarPaciente.setData_solicitacao(rs.getDate("data_solicitacao"));
                gerenciarPaciente.setObservacao(rs.getString("observacao"));
                gerenciarPaciente.setData_cadastro(rs.getDate("data_cadastro"));
                gerenciarPaciente.getLaudo().getProcedimentoPrimario().setUtilizaEquipamento(rs.getBoolean("utiliza_equipamento"));

                lista.add(gerenciarPaciente);

            }
        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                //comentado walter erro log ex.printStackTrace();
            }
        }
        return lista;
    }
    
    public List<GerenciarPacienteBean> carregarTodosPacientesInstituicaoBusca(
            GerenciarPacienteBean gerenciar, String campoBusca, String tipoBusca) throws ProjetoException {

        String sql = "select coalesce(l.codpaciente, p.id_paciente) codpaciente, pa.nome, pa.matricula, pa.cns, "
                + " pr.codproc , pr.nome as procedimento, "
                + "coalesce((SELECT * FROM hosp.fn_GetLastDayOfMonth(to_date(ano_final||'-'||'0'||''||mes_final||'-'||'01', 'YYYY-MM-DD'))),\n" +
                " date_trunc('month',p.data_solicitacao+ interval '2 months') + INTERVAL'1 month' - INTERVAL'1 day') as datafinal, p.inclusao_sem_laudo "
                + " from hosp.paciente_instituicao p "
                + " left join hosp.laudo l on (l.id_laudo = p.codlaudo) "
                + " left join hosp.proc pr on (pr.id = coalesce(l.codprocedimento_primario, p.codprocedimento_primario_laudo_anterior)) "
                + " left join hosp.pacientes pa on (coalesce(l.codpaciente, p.id_paciente) = pa.id_paciente) "
                + " left join hosp.equipe e on (p.codequipe = e.id_equipe) "
                + " left join acl.funcionarios f on (p.codprofissional = f.id_funcionario) "
                + " left join hosp.grupo g on (g.id_grupo = p.codgrupo) "
                + " left join hosp.programa prog on (prog.id_programa = p.codprograma) "
                + " left join hosp.grupo_programa gp on (g.id_grupo = gp.codgrupo and  prog.id_programa = gp.codprograma) "
                + " where p.cod_unidade = ? ";
        
        if ((tipoBusca.equals("paciente") && !VerificadorUtil.verificarSeObjetoNuloOuVazio(campoBusca)))
            sql = sql + " and pa.nome ilike ?";

        if ((tipoBusca.equals("codproc") && !VerificadorUtil.verificarSeObjetoNuloOuVazio(campoBusca)))
            sql = sql + " and pr.codproc = ?";

        if ((tipoBusca.equals("matpaciente") && !VerificadorUtil.verificarSeObjetoNuloOuVazio(campoBusca)))
            sql = sql + " and upper(pa.matricula) = ?";

        if ((tipoBusca.equals("prontpaciente") && !VerificadorUtil.verificarSeObjetoNuloOuVazio(campoBusca))) 
            sql = sql + " and pa.id_paciente = ?";

        sql = sql + " order by pa.nome";

        List<GerenciarPacienteBean> lista = new ArrayList<>();

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, user_session.getUnidade().getId());
            
            if ((tipoBusca.equals("paciente") && !VerificadorUtil.verificarSeObjetoNuloOuVazio(campoBusca)))
                stmt.setString(2, "%" + campoBusca.toUpperCase() + "%");

            if (((tipoBusca.equals("codproc") || (tipoBusca.equals("matpaciente"))) && !VerificadorUtil.verificarSeObjetoNuloOuVazio(campoBusca)))
                stmt.setString(2, campoBusca.toUpperCase());
             
            if ((tipoBusca.equals("prontpaciente") && !VerificadorUtil.verificarSeObjetoNuloOuVazio(campoBusca)))
                stmt.setInt(2, Integer.valueOf((campoBusca.toUpperCase())));

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                GerenciarPacienteBean gerenciarPaciente = new GerenciarPacienteBean();

                gerenciarPaciente.getLaudo().getProcedimentoPrimario().setCodProc(rs.getString("codproc"));
                gerenciarPaciente.getLaudo().getProcedimentoPrimario().setNomeProc(rs.getString("procedimento"));
                gerenciarPaciente.getLaudo().getPaciente().setId_paciente(rs.getInt("codpaciente"));
                gerenciarPaciente.getLaudo().getPaciente().setNome(rs.getString("nome"));
                gerenciarPaciente.getLaudo().getPaciente().setMatricula(rs.getString("matricula"));
                gerenciarPaciente.getLaudo().getPaciente().setCns(rs.getString("cns"));

                lista.add(gerenciarPaciente);

            }
        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                //comentado walter erro log ex.printStackTrace();
            }
        }
        return lista;
    }

    public List<PacienteBean> listaPacientesAtivos() throws ProjetoException {

        String sql = "select distinct coalesce(l.codpaciente, p.id_paciente) codpaciente, pa.nome, pa.matricula " +
                " from hosp.paciente_instituicao p " +
                " left join hosp.laudo l on (l.id_laudo = p.codlaudo) " +
                " left join hosp.proc pr on (pr.id = coalesce(l.codprocedimento_primario, p.codprocedimento_primario_laudo_anterior)) " +
                " left join hosp.pacientes pa on (coalesce(l.codpaciente, p.id_paciente) = pa.id_paciente) " +
                " left join hosp.equipe e on (p.codequipe = e.id_equipe) " +
                " left join acl.funcionarios f on (p.codprofissional = f.id_funcionario) " +
                " left join hosp.grupo g on (g.id_grupo = p.codgrupo) " +
                " left join hosp.programa prog on (prog.id_programa = p.codprograma) " +
                " left join hosp.grupo_programa gp on (g.id_grupo = gp.codgrupo and  prog.id_programa = gp.codprograma) " +
                " where p.cod_unidade = ? and pr.ativo = 'S' " +
                "  and p.status ='A' " +
                " order by pa.nome";

        List<PacienteBean> lista = new ArrayList<>();

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, user_session.getUnidade().getId());

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                PacienteBean paciente = new PacienteBean();
                paciente.setId_paciente(rs.getInt("codpaciente"));
                paciente.setNome(rs.getString("nome"));
                paciente.setMatricula(rs.getString("matricula"));
                lista.add(paciente);
            }
        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                //comentado walter erro log ex.printStackTrace();
            }
        }
        return lista;
    }

    public ArrayList<Integer> carregarPacientesInstituicaoDuplicado() throws ProjetoException {

        String sql = "select distinct p.id \n" +
                " from hosp.paciente_instituicao p \n" +
                " join hosp.profissional_dia_atendimento d on d.id_paciente_instituicao = p.id where p.cod_unidade=1 " ;

        ArrayList<Integer> lista = new ArrayList<>();

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                GerenciarPacienteBean gerenciarPaciente = new GerenciarPacienteBean();
                gerenciarPaciente.setId(rs.getInt("id"));
                lista.add(gerenciarPaciente.getId());
            }
        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                //comentado walter erro log ex.printStackTrace();
            }
        }
        return lista;
    }

    public Boolean desligarPaciente(GerenciarPacienteBean gerenciarRow, GerenciarPacienteBean gerenciar)
            throws ProjetoException {

        Boolean retorno = false;

        String sql = "update hosp.paciente_instituicao set status = ? "
                + " where id = ?";
        try {
            conexao = ConnectionFactory.getConnection();

            TransferenciaPacienteDAO gerenciarTransferenciaDAO = new TransferenciaPacienteDAO();
            if (!gerenciarTransferenciaDAO.apagarAtendimentosNaRenovacao(gerenciarRow.getId(),
                    gerenciar.getDataDesligamento(), conexao)) {
                conexao.close();
                return retorno;
            }

            PreparedStatement stmt = conexao.prepareStatement(sql);

            stmt.setString(1, TipoGravacaoHistoricoPaciente.DESLIGAMENTO.getSigla());
            stmt.setInt(2, gerenciarRow.getId());
            stmt.executeUpdate();

            String sql2 = "INSERT INTO hosp.historico_paciente_instituicao (codpaciente_instituicao, data_operacao, motivo_desligamento, tipo, observacao, id_funcionario_gravacao, data_desligamento) "
                    + " VALUES  (?, current_timestamp, ?, ?, ?,?, ?)";
            stmt = conexao.prepareStatement(sql2);
            stmt.setLong(1, gerenciarRow.getId());
            stmt.setInt(2, gerenciar.getMotivo_desligamento());
            stmt.setString(3, TipoGravacaoHistoricoPaciente.DESLIGAMENTO.getSigla());
            stmt.setString(4, gerenciar.getObservacao());
            stmt.setLong(5, user_session.getId());
            stmt.setDate(6, new java.sql.Date(gerenciar.getDataDesligamento().getTime()));
            stmt.executeUpdate();

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
                //comentado walter erro log ex.printStackTrace();
            }
        }
        return retorno;
    }
    
    public Integer retonarQuantidadeAtendimentoComPresenca(Integer idPacienteInstituicao, Date dataDesligamento) throws ProjetoException {

        Integer quantidade = 0;

        String sql = "select count(*) quantidade from hosp.atendimentos a \n" +
                "join hosp.atendimentos1 a1 on a1.id_atendimento  = a.id_atendimento \n" +
                "\tjoin hosp.paciente_instituicao pi on a.id_paciente_instituicao = pi.id \n" +
                "\twhere coalesce(a.presenca,'N') = 'S' and a.dtaatende >= ? \n" +
                "\tAND coalesce(a.situacao,'A') <> 'C' AND coalesce(a1.excluido,'N')='N'\n" +
                "\tand pi.id = ? \n" +
                "\tand not exists \n" +
                "\t\t(select a2.id_atendimentos1 from hosp.atendimentos1 a2 \n" +
                "\t\twhere a2.id_atendimento = a.id_atendimento and a2.id_situacao_atendimento is not null);";
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setDate(1, new java.sql.Date(dataDesligamento.getTime()));
            stmt.setInt(2, idPacienteInstituicao);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                quantidade = rs.getInt("quantidade");
            }

        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                //comentado walter erro log ex.printStackTrace();
            }
        }
        return quantidade;
    }


    public Boolean encaminharPaciente(GerenciarPacienteBean row, GerenciarPacienteBean gerenciar)
            throws ProjetoException {

        Boolean retorno = false;

        String sql = "update hosp.paciente_instituicao set status = 'D', cod_equipamento = ? "
                + " where id = ?";
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);

            stmt.setInt(1, gerenciar.getLaudo().getProcedimentoPrimario().getEquipamento().getId_equipamento());
            stmt.setInt(2, row.getId());
            stmt.executeUpdate();

            String sql2 = "INSERT INTO hosp.historico_paciente_instituicao (codpaciente_instituicao, data_operacao, motivo_desligamento, tipo, observacao,id_funcionario_gravacao) "
                    + " VALUES  (?, current_timestamp, (select motivo_padrao_desligamento_opm from hosp.parametro), ?, ?, ?)";
            stmt = conexao.prepareStatement(sql2);
            stmt.setLong(1, row.getLaudo().getPaciente().getId_paciente());
            stmt.setString(2, TipoGravacaoHistoricoPaciente.DESLIGAMENTO.getSigla());
            stmt.setString(3, gerenciar.getObservacao());
            stmt.setLong(4, user_session.getId());
            stmt.executeUpdate();
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
                //comentado walter erro log ex.printStackTrace();
            }
        }
        return retorno;
    }

    public Boolean verificarPacienteAtivoInstituicao(Integer idPaciente, Integer idPrograma, Integer idGrupo) throws ProjetoException {

        Boolean retorno = false;

        String sql = " SELECT id FROM hosp.paciente_instituicao pi " +
                " left join hosp.laudo l on (l.id_laudo = pi.codlaudo) " +
                " left join hosp.programa p on pi.codprograma = p.id_programa " +
                " left join hosp.grupo g on pi.codgrupo = g.id_grupo " +
                " WHERE pi.status = 'A' AND coalesce(l.codpaciente, pi.id_paciente) = ? " +
                " and p.id_programa = ? and g.id_grupo = ?;";
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, idPaciente);
            stmt.setInt(2, idPrograma);
            stmt.setInt(3, idGrupo);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                retorno = true;
            }

        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                //comentado walter erro log ex.printStackTrace();
            }
        }
        return retorno;
    }

    public Boolean gravarHistoricoAcaoPaciente(Integer idPacienteInstituicao, String observacao, String tipo, Connection conAuxiliar)
            throws ProjetoException, SQLException {

        Boolean retorno = false;

        String sql = "INSERT INTO hosp.historico_paciente_instituicao (codpaciente_instituicao, data_operacao, observacao, tipo,id_funcionario_gravacao) "
                + " VALUES  (?, CURRENT_TIMESTAMP , ?, ?,?)";

        try {
            ps = null;
            ps = conAuxiliar.prepareStatement(sql);

            ps.setLong(1, idPacienteInstituicao);
            ps.setString(2, observacao);
            ps.setString(3, tipo);
            ps.setLong(4, user_session.getId());
            ps.executeUpdate();
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

    public Boolean apagarAtendimentos(Integer idPacienteInstituicao, Connection conAuxiliar, Boolean alteracaoDePaciente,
                                      ArrayList<SubstituicaoProfissional> listaSubstituicaoProfissional,
                                      ArrayList<InsercaoProfissionalEquipe> listaProfissionaisInseridosNaEquipeAtendimento,
                                      ArrayList<RemocaoProfissionalEquipe> listaProfissionaisRemovidosNaEquipeAtendimento,
                                      ArrayList<AtendimentoBean> listaAtendimento1ComLiberacoes) throws SQLException, ProjetoException {

        Boolean retorno = false;
        ArrayList<Integer> lista = new ArrayList<Integer>();

        try {

            String sql = "SELECT DISTINCT a1.id_atendimento FROM hosp.atendimentos1 a1 " +
                    "LEFT JOIN hosp.atendimentos a ON (a.id_atendimento = a1.id_atendimento) " +
                    "WHERE   coalesce(a1.excluido, 'N' )='N' and  a.id_paciente_instituicao = ? AND a.dtaatende >= current_date AND  coalesce(a.presenca, 'N') = 'N' AND " +
                    "(SELECT count(*) FROM hosp.atendimentos1 aa1 WHERE aa1.id_atendimento = a1.id_atendimento and coalesce(aa1.excluido, 'N' )='N') = " +
                    "(SELECT count(*) FROM hosp.atendimentos1 aaa1 WHERE aaa1.id_atendimento = a1.id_atendimento AND aaa1.id_situacao_atendimento IS NULL and coalesce(aaa1.excluido, 'N' )='N') " +
                    "ORDER BY a1.id_atendimento;";


            ps = null;
            ps = conAuxiliar.prepareStatement(sql);
            ps.setLong(1, idPacienteInstituicao);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(rs.getInt("id_atendimento"));
            }

            for (int i = 0; i < listaSubstituicaoProfissional.size(); i++) {
                String sql2 = "delete from adm.substituicao_funcionario where id_atendimentos1 = ?";

                PreparedStatement ps2 = null;
                ps2 = conAuxiliar.prepareStatement(sql2);
                ps2.setLong(1, listaSubstituicaoProfissional.get(i).getIdAtendimentos1());
                ps2.execute();
            }

            for (int i = 0; i < listaProfissionaisInseridosNaEquipeAtendimento.size(); i++) {
                String sql2 = "delete from adm.insercao_profissional_equipe_atendimento_1 where id_atendimentos1 = ?";

                PreparedStatement ps2 = null;
                ps2 = conAuxiliar.prepareStatement(sql2);
                ps2.setLong(1, listaProfissionaisInseridosNaEquipeAtendimento.get(i).getIdAtendimentos1());
                ps2.execute();
            }

            for (int i = 0; i < listaProfissionaisRemovidosNaEquipeAtendimento.size(); i++) {
                String sql2 = "delete from adm.remocao_profissional_equipe_atendimento_1 where id_atendimentos1 = ?";

                PreparedStatement ps2 = null;
                ps2 = conAuxiliar.prepareStatement(sql2);
                ps2.setLong(1, listaProfissionaisRemovidosNaEquipeAtendimento.get(i).getIdAtendimentos1());
                ps2.execute();
            }

            /*
            for (int i = 0; i < listaProfissionaisRemovidosEquipe.size(); i++) {
                String sql2 = "delete from logs.remocao_profissional_equipe_atendimentos1 where id_atendimentos1 = ?";

                PreparedStatement ps2 = null;
                ps2 = conAuxiliar.prepareStatement(sql2);
                ps2.setLong(1, listaProfissionaisRemovidosEquipe.get(i).getIdAtendimentos1());
                ps2.execute();
            }       */


            filtraAtendimento1PorAtendimento(lista, listaAtendimento1ComLiberacoes);
            for (int i = 0; i < lista.size(); i++) {
                String sql2 = "delete\n" +
                        "from\n" +
                        "	hosp.atendimentos1\n" +
                        "where\n" +
                        "	atendimentos1.id_atendimento = ? and atendimentos1.id_situacao_atendimento is null " +
                        "	and (atendimentos1.id_atendimentos1 not in (\n" +
                        "	select\n" +
                        "		distinct sp.id_atendimentos1\n" +
                        "	from\n" +
                        "		logs.substituicao_profissional_equipe_atendimentos1 sp\n" +
                        "	join hosp.atendimentos1 a1 on\n" +
                        "		a1.id_atendimentos1 = sp.id_atendimentos1\n" +
                        "	join hosp.atendimentos a on\n" +
                        "		a.id_atendimento = a1.id_atendimento\n" +
                        "	where\n" +
                        "		a.id_atendimento = ? ))\n" +
                        "	and ( atendimentos1.id_atendimentos1 not in (\n" +
                        "	select\n" +
                        "		distinct rpea.id_atendimentos1\n" +
                        "	from\n" +
                        "		logs.remocao_profissional_equipe_atendimentos1 rpea\n" +
                        "	join hosp.atendimentos1 a1 on\n" +
                        "		a1.id_atendimentos1 = rpea.id_atendimentos1\n" +
                        "	join hosp.atendimentos a on\n" +
                        "		a.id_atendimento = a1.id_atendimento\n" +
                        "	where\n" +
                        "		a.id_atendimento = ? ) )\n" +
                        "and ( atendimentos1.id_atendimentos1 not in (\n" +
                        "	select\n" +
                        "		distinct ipea.id_atendimentos1\n" +
                        "	from\n" +
                        "		adm.insercao_profissional_equipe_atendimento_1 ipea \n" +
                        "	join hosp.atendimentos1 a1 on\n" +
                        "		a1.id_atendimentos1 = ipea.id_atendimentos1\n" +
                        "	join hosp.atendimentos a on\n" +
                        "		a.id_atendimento = a1.id_atendimento\n" +
                        "	where\n" +
                        "		a.id_atendimento = ? ) )\n" +
                        "and ( atendimentos1.id_atendimentos1 not in (\n" +
                        "	select\n" +
                        "		distinct ipea.id_atendimentos1\n" +
                        "	from\n" +
                        "		adm.remocao_profissional_equipe_atendimento_1 ipea\n" +
                        "	join hosp.atendimentos1 a1 on\n" +
                        "		a1.id_atendimentos1 = ipea.id_atendimentos1\n" +
                        "	join hosp.atendimentos a on\n" +
                        "		a.id_atendimento = a1.id_atendimento\n" +
                        "	where\n" +
                        "		a.id_atendimento = ? ) ) ";

                String sqlCondicaoIdAtendimento1Leberacao = " and atendimentos1.id_atendimentos1 != ? ";

                PreparedStatement ps2 = null;
                sql2 = concatenaSqlParaNaoExcluirAtendimentos1ComLiberacao(listaAtendimento1ComLiberacoes, sql2,
                        sqlCondicaoIdAtendimento1Leberacao);
                ps2 = conAuxiliar.prepareStatement(sql2);
                ps2.setLong(1, lista.get(i));
                ps2.setLong(2, lista.get(i));
                ps2.setLong(3, lista.get(i));
                ps2.setLong(4, lista.get(i));
                ps2.setLong(5, lista.get(i));
                concatenaPreparedStatementParaNaoExcluirAtendimentos1ComLiberacao(listaAtendimento1ComLiberacoes, ps2);
                ps2.execute();

            }

            executaExclusaoLogicaDeAtendimentos1(listaAtendimento1ComLiberacoes, conAuxiliar);

            for (int i = 0; i < lista.size(); i++) {
                String sql3 = "delete\n" +
                        "from\n" +
                        "	hosp.atendimentos\n" +
                        "where\n" +
                        "	id_atendimento = ? and situacao is null " +
                        "	and (atendimentos.id_atendimento not in (\n" +
                        "	select\n" +
                        "		distinct a.id_atendimento\n" +
                        "	from\n" +
                        "		logs.substituicao_profissional_equipe_atendimentos1 sp\n" +
                        "	join hosp.atendimentos1 a1 on\n" +
                        "		a1.id_atendimentos1 = sp.id_atendimentos1\n" +
                        "	join hosp.atendimentos a on\n" +
                        "		a.id_atendimento = a1.id_atendimento\n" +
                        "	where\n" +
                        "		a.id_atendimento =? ))\n" +
                        "	and (atendimentos.id_atendimento not in (\n" +
                        "	select\n" +
                        "		distinct a.id_atendimento\n" +
                        "	from\n" +
                        "		logs.remocao_profissional_equipe_atendimentos1 rpea\n" +
                        "	join hosp.atendimentos1 a1 on\n" +
                        "		a1.id_atendimentos1 = rpea.id_atendimentos1\n" +
                        "	join hosp.atendimentos a on\n" +
                        "		a.id_atendimento = a1.id_atendimento\n" +
                        "	where\n" +
                        "		a.id_atendimento =? ) )		\n" +
                        "	and (atendimentos.id_atendimento not in (\n" +
                        "	select\n" +
                        "		distinct a.id_atendimento\n" +
                        "	from\n" +
                        "		adm.insercao_profissional_equipe_atendimento_1 ipea \n" +
                        "	join hosp.atendimentos1 a1 on\n" +
                        "		a1.id_atendimentos1 = ipea.id_atendimentos1\n" +
                        "	join hosp.atendimentos a on\n" +
                        "		a.id_atendimento = a1.id_atendimento\n" +
                        "	where\n" +
                        "		a.id_atendimento =? ) )	\n" +
                        "	and (atendimentos.id_atendimento not in (\n" +
                        "	select\n" +
                        "		distinct a.id_atendimento\n" +
                        "	from\n" +
                        "		adm.remocao_profissional_equipe_atendimento_1 rpea \n" +
                        "	join hosp.atendimentos1 a1 on\n" +
                        "		a1.id_atendimentos1 = rpea.id_atendimentos1\n" +
                        "	join hosp.atendimentos a on\n" +
                        "		a.id_atendimento = a1.id_atendimento\n" +
                        "	where\n" +
                        "		a.id_atendimento =? ) )	";

                PreparedStatement ps3 = null;
                ps3 = conAuxiliar.prepareStatement(sql3);
                ps3.setLong(1, lista.get(i));
                ps3.setLong(2, lista.get(i));
                ps3.setLong(3, lista.get(i));
                ps3.setLong(4, lista.get(i));
                ps3.setLong(5, lista.get(i));
                ps3.execute();

            }

            for (int i = 0; i < lista.size(); i++) {
                String sql3 = "update  hosp.atendimentos set situacao='C' where id_atendimento = ?";
             /*
            	String sql3 = "update  hosp.atendimentos set situacao='C' where id_atendimento = ? and ((atendimentos.id_atendimento  in (select distinct a.id_atendimento from   \n" +
                		"                		                		logs.substituicao_profissional_equipe_atendimentos1  sp   \n" +
                		"                		                		join hosp.atendimentos1 a1 on a1.id_atendimentos1  = sp.id_atendimentos1    \n" +
                		"                		                		join hosp.atendimentos  a on a.id_atendimento  = a1.id_atendimento where a.id_atendimento=? ))   \n" +
                		"                		                		or   \n" +
                		"                		                		(atendimentos.id_atendimento in (select distinct a.id_atendimento from   \n" +
                		"                		                		logs.remocao_profissional_equipe_atendimentos1 rpea   \n" +
                		"                		                		join hosp.atendimentos1 a1 on a1.id_atendimentos1  = rpea.id_atendimentos1   \n" +
                		"                		                		join hosp.atendimentos  a on a.id_atendimento  = a1.id_atendimento where a.id_atendimento=? )   \n" +
                		"                		                		)\n" +
                		"                		                		or   \n" +
                		"                		                		(atendimentos.id_atendimento in (select distinct a.id_atendimento from   \n" +
                		"                		                		logs.substituicao_profissional_equipe_atendimentos1 rpea   \n" +
                		"                		                		join hosp.atendimentos1 a1 on a1.id_atendimentos1  = rpea.id_atendimentos1   \n" +
                		"                		                		join hosp.atendimentos  a on a.id_atendimento  = a1.id_atendimento where a.id_atendimento=? )   \n" +
                		"                		                		)\n" +
                		"                		                		or   \n" +
                		"                		                		(atendimentos.id_atendimento in (select distinct a.id_atendimento from   \n" +
                		"                		                		adm.insercao_profissional_equipe_atendimento_1  rpea   \n" +
                		"                		                		join hosp.atendimentos1 a1 on a1.id_atendimentos1  = rpea.id_atendimentos1   \n" +
                		"                		                		join hosp.atendimentos  a on a.id_atendimento  = a1.id_atendimento where a.id_atendimento=? )   \n" +
                		"                		                		)    \n" +
                		"                		                		or   \n" +
                		"                		                		(atendimentos.id_atendimento in (select distinct a.id_atendimento from   \n" +
                		"                		                		adm.remocao_profissional_equipe_atendimento_1  rpea   \n" +
                		"                		                		join hosp.atendimentos1 a1 on a1.id_atendimentos1  = rpea.id_atendimentos1   \n" +
                		"                		                		join hosp.atendimentos  a on a.id_atendimento  = a1.id_atendimento where a.id_atendimento=? )   \n" +
                		"                		                		)                  		                		\n" +
                		"                		                		)";*/

                PreparedStatement ps3 = null;
                ps3 = conAuxiliar.prepareStatement(sql3);
                ps3.setLong(1, lista.get(i));
               /* ps3.setLong(2, lista.get(i));
                ps3.setLong(3, lista.get(i));
                ps3.setLong(4, lista.get(i));
                ps3.setLong(5, lista.get(i));
                ps3.setLong(6, lista.get(i));*/
                ps3.execute();

            }

            if (alteracaoDePaciente) {
                String sql4 = "delete from hosp.profissional_dia_atendimento where id_paciente_instituicao = ?";

                PreparedStatement ps4 = null;
                ps4 = conAuxiliar.prepareStatement(sql4);
                ps4.setLong(1, idPacienteInstituicao);
                ps4.execute();
            }
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

    private void executaExclusaoLogicaDeAtendimentos1(ArrayList<AtendimentoBean> listaAtendimento1ComLiberacoes,
                                                      Connection conAuxiliar) throws SQLException, ProjetoException {
        String sql = "update hosp.atendimentos1 set excluido = 'S' where id_atendimentos1 = ?";
        try {
            for (AtendimentoBean atendimento : listaAtendimento1ComLiberacoes) {
                PreparedStatement ps = conAuxiliar.prepareStatement(sql);
                ps.setInt(1, atendimento.getId1());
                ps.executeUpdate();
            }
        } catch (SQLException sqle) {
            conAuxiliar.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conAuxiliar.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
    }

    private void filtraAtendimento1PorAtendimento(ArrayList<Integer> lista,
                                                  ArrayList<AtendimentoBean> listaAtendimento1ComLiberacoes) {

        ArrayList<AtendimentoBean> listaAtendimento1ComLiberacoesAux = new ArrayList<AtendimentoBean>();
        listaAtendimento1ComLiberacoesAux.addAll(listaAtendimento1ComLiberacoes);

        for (AtendimentoBean atendimento : listaAtendimento1ComLiberacoesAux) {
            if(!lista.contains(atendimento.getId()))
                listaAtendimento1ComLiberacoes.remove(atendimento);
        }

    }

    private String concatenaSqlParaNaoExcluirAtendimentos1ComLiberacao(
            ArrayList<AtendimentoBean> listaIdAtendimento1ComLiberacoes, String sql, String sqlCondicaoIdAtendimento1Leberacao) {
        for (int i = 0; i < listaIdAtendimento1ComLiberacoes.size(); i++) {
            sql += sqlCondicaoIdAtendimento1Leberacao;
        }
        return sql;
    }

    private void concatenaPreparedStatementParaNaoExcluirAtendimentos1ComLiberacao
            (ArrayList<AtendimentoBean> listaIdAtendimento1ComLiberacoes, PreparedStatement ps) throws SQLException {
        for (int i = 1; i <= listaIdAtendimento1ComLiberacoes.size(); i++) {
            ps.setInt(5+i, listaIdAtendimento1ComLiberacoes.get(i-1).getId1());
        }
    }


    public Boolean apagarAtendimentosDeUmAtendimento
            (Integer idAtendimentos, Connection conAuxiliar,  ArrayList<SubstituicaoProfissional> listaSubstituicaoProfissional,
             List<AtendimentoBean> listaExcluir,  ArrayList<InsercaoProfissionalEquipe> listaProfissionaisInseridosNaEquipeAtendimento,
             ArrayList<RemocaoProfissionalEquipe> listaProfissionaisRemovidosNaEquipeAtendimento) throws SQLException, ProjetoException {

        Boolean retorno = false;
        String sql2 = "";
        try {

            for (int i = 0; i < listaSubstituicaoProfissional.size(); i++) {
                sql2 = "delete from adm.substituicao_funcionario where id_atendimentos1 = ?";

                PreparedStatement ps2 = null;
                ps2 = conAuxiliar.prepareStatement(sql2);
                ps2.setLong(1, listaSubstituicaoProfissional.get(i).getIdAtendimentos1());
                ps2.execute();
            }

            for (int i = 0; i < listaProfissionaisInseridosNaEquipeAtendimento.size(); i++) {
                sql2 = "delete from adm.insercao_profissional_equipe_atendimento_1 where id_atendimentos1 = ?";

                PreparedStatement ps2 = null;
                ps2 = conAuxiliar.prepareStatement(sql2);
                ps2.setLong(1, listaProfissionaisInseridosNaEquipeAtendimento.get(i).getIdAtendimentos1());
                ps2.execute();
            }

            for (int i = 0; i < listaProfissionaisRemovidosNaEquipeAtendimento.size(); i++) {
                sql2 = "delete from adm.remocao_profissional_equipe_atendimento_1 where id_atendimentos1 = ?";

                PreparedStatement ps2 = null;
                ps2 = conAuxiliar.prepareStatement(sql2);
                ps2.setLong(1, listaProfissionaisRemovidosNaEquipeAtendimento.get(i).getIdAtendimentos1());
                ps2.execute();
            }

            //    sql2 = "delete from hosp.atendimentos1  where id_atendimento = ? and situacao is null and coalesce(excluido,'N')='N'";

            sql2 =  "delete " +
                    "from " +
                    "	hosp.atendimentos1 " +
                    "where " +
                    "	atendimentos1.id_atendimento = ? and coalesce(excluido,'N')='N' and id_situacao_atendimento is null" +
                    "	and (atendimentos1.id_atendimentos1 not in ( " +
                    "	select " +
                    "		distinct sp.id_atendimentos1 " +
                    "	from " +
                    "		logs.substituicao_profissional_equipe_atendimentos1 sp " +
                    "	join hosp.atendimentos1 a1 on " +
                    "		a1.id_atendimentos1 = sp.id_atendimentos1 " +
                    "	join hosp.atendimentos a on " +
                    "		a.id_atendimento = a1.id_atendimento " +
                    "	where " +
                    "		a.id_atendimento = ? )) " +
                    "	and ( atendimentos1.id_atendimentos1 not in ( " +
                    "	select " +
                    "		distinct rpea.id_atendimentos1 " +
                    "	from " +
                    "		logs.remocao_profissional_equipe_atendimentos1 rpea " +
                    "	join hosp.atendimentos1 a1 on " +
                    "		a1.id_atendimentos1 = rpea.id_atendimentos1 " +
                    "	join hosp.atendimentos a on " +
                    "		a.id_atendimento = a1.id_atendimento " +
                    "	where " +
                    "		a.id_atendimento = ? ) ) " +
                    "and ( atendimentos1.id_atendimentos1 not in ( " +
                    "	select " +
                    "		distinct ipea.id_atendimentos1 " +
                    "	from " +
                    "		adm.insercao_profissional_equipe_atendimento_1 ipea  " +
                    "	join hosp.atendimentos1 a1 on " +
                    "		a1.id_atendimentos1 = ipea.id_atendimentos1 " +
                    "	join hosp.atendimentos a on " +
                    "		a.id_atendimento = a1.id_atendimento " +
                    "	where " +
                    "		a.id_atendimento = ? ) ) " +
                    "and ( atendimentos1.id_atendimentos1 not in ( " +
                    "	select " +
                    "		distinct ipea.id_atendimentos1 " +
                    "	from " +
                    "		adm.remocao_profissional_equipe_atendimento_1 ipea " +
                    "	join hosp.atendimentos1 a1 on " +
                    "		a1.id_atendimentos1 = ipea.id_atendimentos1 " +
                    "	join hosp.atendimentos a on " +
                    "		a.id_atendimento = a1.id_atendimento " +
                    "	where " +
                    "		a.id_atendimento = ? ) ) "+
                    "and ( atendimentos1.id_atendimentos1 not in( " +
                    "	select distinct a2.id_atendimentos1 " +
                    "	from hosp.atendimentos1 a2 join hosp.liberacoes l " +
                    "	on a2.id_atendimentos1 = l.id_atendimentos1 " +
                    "	join hosp.atendimentos a on a.id_atendimento = a2.id_atendimento " +
                    "	where a.id_atendimento = ?) ) "+
                    "and ( atendimentos1.id_atendimentos1 not in( "+
                    "	select distinct a3.id_atendimentos1 from hosp.atendimentos1 a3 \r\n" + 
                    "   join hosp.atendimentos ate on ate.id_atendimento = a3.id_atendimento\r\n" + 
                    "   join hosp.inconsistencias_log il on a3.id_atendimentos1 = il.id_atendimento1 \r\n" + 
                    "   where ate.id_atendimento = ?)) ";

            PreparedStatement ps2 = null;
            ps2 = conAuxiliar.prepareStatement(sql2);
            ps2.setLong(1, idAtendimentos);
            ps2.setLong(2, idAtendimentos);
            ps2.setLong(3, idAtendimentos);
            ps2.setLong(4, idAtendimentos);
            ps2.setLong(5, idAtendimentos);
            ps2.setLong(6, idAtendimentos);
            ps2.setLong(7, idAtendimentos);
            ps2.execute();

            
            for (int i = 0; i < listaExcluir.size(); i++) {
                sql2 = "update hosp.atendimentos1 set excluido='S', data_hora_exclusao=current_timestamp, usuario_exclusao = ? " + 
                		" where id_atendimentos1 = ? ";

                ps2 = null;
                ps2 = conAuxiliar.prepareStatement(sql2);
                ps2.setLong(1, user_session.getId());
                ps2.setLong(2, listaExcluir.get(i).getId1());
                ps2.execute();
            }
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

    public List<Integer> retornaListaAtendimento01QueNaoPodemTerRegistroExcluidos
            (Integer idAtendimento, Connection conAuxiliar) throws ProjetoException, SQLException {

        List<Integer> listaIdatendimento = new ArrayList<Integer>();
        try {
            String sql = "select distinct atendimentos1.id_atendimentos1 from hosp.atendimentos1 " +
                    "where atendimentos1.id_atendimentos1 in (  " +
                    "	select  " +
                    "		distinct sp.id_atendimentos1  " +
                    "	from  " +
                    "		logs.substituicao_profissional_equipe_atendimentos1 sp  " +
                    "	join hosp.atendimentos1 a1 on  " +
                    "		a1.id_atendimentos1 = sp.id_atendimentos1  " +
                    "	join hosp.atendimentos a on  " +
                    "		a.id_atendimento = a1.id_atendimento  " +
                    "	where  " +
                    "		a.id_atendimento = ? )  " +
                    "	or ( atendimentos1.id_atendimentos1 in (  " +
                    "	select  " +
                    "		distinct rpea.id_atendimentos1  " +
                    "	from  " +
                    "		logs.remocao_profissional_equipe_atendimentos1 rpea  " +
                    "	join hosp.atendimentos1 a1 on  " +
                    "		a1.id_atendimentos1 = rpea.id_atendimentos1  " +
                    "	join hosp.atendimentos a on  " +
                    "		a.id_atendimento = a1.id_atendimento  " +
                    "	where  " +
                    "		a.id_atendimento = ? ) )  " +
                    "or ( atendimentos1.id_atendimentos1 in (  " +
                    "	select  " +
                    "		distinct ipea.id_atendimentos1  " +
                    "	from  " +
                    "		adm.insercao_profissional_equipe_atendimento_1 ipea   " +
                    "	join hosp.atendimentos1 a1 on  " +
                    "		a1.id_atendimentos1 = ipea.id_atendimentos1  " +
                    "	join hosp.atendimentos a on  " +
                    "		a.id_atendimento = a1.id_atendimento  " +
                    "	where  " +
                    "		a.id_atendimento = ? ) )  " +
                    "or ( atendimentos1.id_atendimentos1 in (  " +
                    "	select  " +
                    "		distinct ipea.id_atendimentos1  " +
                    "	from  " +
                    "		adm.remocao_profissional_equipe_atendimento_1 ipea  " +
                    "	join hosp.atendimentos1 a1 on  " +
                    "		a1.id_atendimentos1 = ipea.id_atendimentos1  " +
                    "	join hosp.atendimentos a on  " +
                    "		a.id_atendimento = a1.id_atendimento  " +
                    "	where  " +
                    "		a.id_atendimento = ? )) " +
                    "or ( atendimentos1.id_atendimentos1 in( " +
                    "	select distinct a2.id_atendimentos1 " +
                    "	from hosp.atendimentos1 a2 join hosp.liberacoes l " +
                    "	on a2.id_atendimentos1 = l.id_atendimentos1 " +
                    "	join hosp.atendimentos a on a.id_atendimento = a2.id_atendimento " +
                    "	where a.id_atendimento = ?) )	" +
                    "and atendimentos1.id_atendimento = ?	";


            ps = null;
            ps = conAuxiliar.prepareStatement(sql);
            ps.setInt(1, idAtendimento);
            ps.setInt(2, idAtendimento);
            ps.setInt(3, idAtendimento);
            ps.setInt(4, idAtendimento);
            ps.setInt(5, idAtendimento);
            ps.setInt(6, idAtendimento);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                listaIdatendimento.add(rs.getInt("id_atendimentos1"));
            }

        } catch (SQLException sqle) {
            conAuxiliar.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conAuxiliar.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
        return listaIdatendimento;
    }


    public ArrayList<SubstituicaoProfissional> listaAtendimentosQueTiveramSubstituicaoProfissional
            (Integer idPacienteInstituicao, Connection conAuxiliar) throws ProjetoException, SQLException {

        ArrayList<SubstituicaoProfissional> lista = new ArrayList<SubstituicaoProfissional>();
        try {
            String sql = "select a.codpaciente,a.dtaatende, a1.cbo, sf.*, af.inicio_afastamento, af.fim_afastamento "+
            		" from adm.substituicao_funcionario sf " +
                    "	join hosp.atendimentos1 a1 on a1.id_atendimentos1 = sf.id_atendimentos1 " +
                    "	join hosp.atendimentos a on a.id_atendimento = a1.id_atendimento " +
                    "   join adm.afastamento_funcionario af on sf.id_afastamento_funcionario = af.id "+
                    "	where sf.id_atendimentos1 in ( " +
                    "	SELECT DISTINCT a1.id_atendimentos1 FROM hosp.atendimentos1 a1  " +
                    "LEFT JOIN hosp.atendimentos a ON (a.id_atendimento = a1.id_atendimento)  " +
                    "WHERE a.id_paciente_instituicao = ? AND a.dtaatende >= current_date  " +
                    "AND  (SELECT count(*) FROM hosp.atendimentos1 aa1 WHERE aa1.id_atendimento = a1.id_atendimento) =  " +
                    "(SELECT count(*) FROM hosp.atendimentos1 aaa1 WHERE aaa1.id_atendimento = a1.id_atendimento AND coalesce(situacao, 'A') = 'A')  " +
                    ") 	AND coalesce(a1.excluido, 'N') = 'N'";

            ps = null;
            ps = conAuxiliar.prepareStatement(sql);
            ps.setLong(1, idPacienteInstituicao);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                SubstituicaoProfissional substituicao = new SubstituicaoProfissional();
                substituicao.setDataAtendimento(rs.getDate("dtaatende"));
                substituicao.getAtendimento().getPaciente().setId_paciente(rs.getInt("codpaciente"));
                substituicao.getAtendimento().getCbo().setCodCbo(rs.getInt("cbo"));
                substituicao.getAfastamentoProfissional().setId(rs.getInt("id_afastamento_funcionario"));
                substituicao.getAfastamentoProfissional().setPeriodoInicio(rs.getDate("inicio_afastamento"));
                substituicao.getAfastamentoProfissional().setPeriodoFinal(rs.getDate("fim_afastamento"));
                substituicao.setIdAtendimentos1(rs.getInt("id_atendimentos1"));
                substituicao.getAfastamentoProfissional().getFuncionario().setId(rs.getLong("id_funcionario_substituido"));
                substituicao.getFuncionario().setId(rs.getLong("id_funcionario_substituto"));
                substituicao.getUsuarioAcao().setId(rs.getLong("usuario_acao"));
                substituicao.setDataHoraAcao(rs.getTimestamp("data_hora_acao"));
                lista.add(substituicao);
            }

        } catch (SQLException sqle) {
            conAuxiliar.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conAuxiliar.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
        return lista;
    }


    public ArrayList<InsercaoProfissionalEquipe> listaAtendimentosQueTiveramInsercaoProfissionalAtendimentoEquipe
            (Integer idPacienteInstituicao, Connection conAuxiliar) throws ProjetoException, SQLException {

        ArrayList<InsercaoProfissionalEquipe> lista = new ArrayList<InsercaoProfissionalEquipe>();
        try {
            String sql = "select distinct a.codpaciente,a.dtaatende, a.codprograma, a.codgrupo,a.codequipe, ipe.id_atendimentos1, "+
            		" id_insercao_profissional_equipe_atendimento, id_profissional, a1.cbo codcbo "+
            		" from adm.insercao_profissional_equipe_atendimento_1 ipe \n" +
                    "	join hosp.atendimentos1 a1 on a1.id_atendimentos1 = ipe.id_atendimentos1 \n" +
                    "	join hosp.atendimentos a on a.id_atendimento = a1.id_atendimento \n" +
                    " join acl.funcionarios f on f.id_funcionario = ipe.id_profissional " +
                    "	where ipe.id_atendimentos1 in ( \n" +
                    "	SELECT DISTINCT a1.id_atendimentos1 FROM hosp.atendimentos1 a1  \n" +
                    "LEFT JOIN hosp.atendimentos a ON (a.id_atendimento = a1.id_atendimento)  \n" +
                    "WHERE a.id_paciente_instituicao = ? AND a.dtaatende >= current_date   \n" +
                    "AND  (SELECT count(*) FROM hosp.atendimentos1 aa1 WHERE aa1.id_atendimento = a1.id_atendimento) =  \n" +
                    "(SELECT count(*) FROM hosp.atendimentos1 aaa1 WHERE aaa1.id_atendimento = a1.id_atendimento AND situacao IS NULL  )  \n" +
                    ")";

            ps = null;
            ps = conAuxiliar.prepareStatement(sql);
            ps.setLong(1, idPacienteInstituicao);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                InsercaoProfissionalEquipe insercao = new InsercaoProfissionalEquipe();
                insercao.setDataAtendimento(rs.getDate("dtaatende"));
                insercao.setIdAtendimentos1(rs.getInt("id_atendimentos1"));
                insercao.getAtendimentoBean().getPaciente().setId_paciente(rs.getInt("codpaciente"));
                insercao.getAtendimentoBean().getCbo().setCodCbo(rs.getInt("codcbo"));
                insercao.setId(rs.getInt("id_insercao_profissional_equipe_atendimento"));
                insercao.getFuncionario().setId(rs.getLong("id_profissional"));
                insercao.getPrograma().setIdPrograma(rs.getInt("codprograma"));
                insercao.getGrupo().setIdGrupo(rs.getInt("codgrupo"));
                insercao.getEquipe().setCodEquipe(rs.getInt("codequipe"));
                lista.add(insercao);
            }

        } catch (SQLException sqle) {
            conAuxiliar.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conAuxiliar.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
        return lista;
    }

    public ArrayList<RemocaoProfissionalEquipe> listaAtendimentosQueTiveramRemocaoProfissionalAtendimentoEquipePeloIdPacienteInstituicao
            (Integer idPacienteInstituicao, Connection conAuxiliar) throws ProjetoException, SQLException {

        ArrayList<RemocaoProfissionalEquipe> lista = new ArrayList<RemocaoProfissionalEquipe>();

        try {
            String sql = "select distinct a.codpaciente,a.dtaatende, a.codprograma, a.codgrupo,a.codequipe, ipe.id_atendimentos1, id_remocao_profissional_equipe_atendimento, id_profissional,  a1.cbo codcbo  from adm.remocao_profissional_equipe_atendimento_1 ipe \n" +
                    "	join hosp.atendimentos1 a1 on a1.id_atendimentos1 = ipe.id_atendimentos1 \n" +
                    "	join hosp.atendimentos a on a.id_atendimento = a1.id_atendimento \n" +
                    " join acl.funcionarios f on f.id_funcionario = ipe.id_profissional \n" +
                    "	where ipe.id_atendimentos1 in ( \n" +
                    "	SELECT DISTINCT a1.id_atendimentos1 FROM hosp.atendimentos1 a1  \n" +
                    "LEFT JOIN hosp.atendimentos a ON (a.id_atendimento = a1.id_atendimento)  \n" +
                    "WHERE a.id_paciente_instituicao = ? AND a.dtaatende >= current_date   \n" +
                    "AND  (SELECT count(*) FROM hosp.atendimentos1 aa1 WHERE aa1.id_atendimento = a1.id_atendimento) =  \n" +
                    "(SELECT count(*) FROM hosp.atendimentos1 aaa1 WHERE aaa1.id_atendimento = a1.id_atendimento AND situacao IS NULL  )  \n" +
                    ") and ipe.id_profissional in (select medico from hosp.equipe_medico em  where em.equipe =a.codequipe )";

            ps = null;
            ps = conAuxiliar.prepareStatement(sql);
            ps.setLong(1, idPacienteInstituicao);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                RemocaoProfissionalEquipe remocao = new RemocaoProfissionalEquipe();
                remocao.setDataAtendimento(rs.getDate("dtaatende"));
                remocao.setIdAtendimentos1(rs.getInt("id_atendimentos1"));
                remocao.getAtendimentoBean().getPaciente().setId_paciente(rs.getInt("codpaciente"));
                remocao.setId(rs.getInt("id_remocao_profissional_equipe_atendimento"));
                remocao.getFuncionario().setId(rs.getLong("id_profissional"));
                remocao.getAtendimentoBean().getCbo().setCodCbo(rs.getInt("codcbo"));
                remocao.getPrograma().setIdPrograma(rs.getInt("codprograma"));
                remocao.getGrupo().setIdGrupo(rs.getInt("codgrupo"));
                remocao.getEquipe().setCodEquipe(rs.getInt("codequipe"));
                lista.add(remocao);
            }

        } catch (SQLException sqle) {
            conAuxiliar.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conAuxiliar.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
        return lista;
    }

    public ArrayList<RemocaoProfissionalEquipe> listaAtendimentosQueTiveramRemocaoProfissionalEquipePeloIdPacienteInstituicao
            (Integer idPacienteInstituicao, Connection conAuxiliar) throws ProjetoException, SQLException {

        ArrayList<RemocaoProfissionalEquipe> lista = new ArrayList<RemocaoProfissionalEquipe>();
        try {
            String sql = "select distinct a.dtaatende, a.codprograma, a.codgrupo, ipe.id_atendimentos1, ipe.id_remocao_profissional_equipe, ipe.id_funcionario id_profissional, a1.cbo codcbo "+
            		" from logs.remocao_profissional_equipe_atendimentos1 ipe \n" +
                    "	join hosp.atendimentos1 a1 on a1.id_atendimentos1 = ipe.id_atendimentos1 \n" +
                    "	join hosp.atendimentos a on a.id_atendimento = a1.id_atendimento \n" +
                    " join acl.funcionarios f on f.id_funcionario = ipe.id_funcionario \n" +
                    "	where ipe.id_atendimentos1 in ( \n" +
                    "	SELECT DISTINCT a1.id_atendimentos1 FROM hosp.atendimentos1 a1  \n" +
                    "LEFT JOIN hosp.atendimentos a ON (a.id_atendimento = a1.id_atendimento)  \n" +
                    "WHERE a.id_paciente_instituicao = ? AND a.dtaatende >= current_date  \n" +
                    "AND  (SELECT count(*) FROM hosp.atendimentos1 aa1 WHERE aa1.id_atendimento = a1.id_atendimento) =  \n" +
                    "(SELECT count(*) FROM hosp.atendimentos1 aaa1 WHERE aaa1.id_atendimento = a1.id_atendimento AND situacao IS NULL)  \n" +
                    ")";

            ps = null;
            ps = conAuxiliar.prepareStatement(sql);
            ps.setLong(1, idPacienteInstituicao);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                RemocaoProfissionalEquipe remocao = new RemocaoProfissionalEquipe();
                remocao.setDataAtendimento(rs.getDate("dtaatende"));
                remocao.setIdAtendimentos1(rs.getInt("id_atendimentos1"));
                remocao.setId(rs.getInt("id_remocao_profissional_equipe"));
                remocao.getFuncionario().setId(rs.getLong("id_profissional"));
                remocao.getAtendimentoBean().getCbo().setCodCbo(rs.getInt("codcbo"));
                remocao.getPrograma().setIdPrograma(rs.getInt("codprograma"));
                remocao.getGrupo().setIdGrupo(rs.getInt("codgrupo"));
                lista.add(remocao);
            }

        } catch (SQLException sqle) {
            conAuxiliar.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conAuxiliar.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
        return lista;
    }


    public ArrayList<SubstituicaoProfissionalEquipeDTO> listaAtendimentosQueTiveramSubstituicaoProfissionalEquipePeloIdPacienteInstituicao
            (Integer idPacienteInstituicao, Connection conAuxiliar) throws SQLException, ProjetoException {

        ArrayList<SubstituicaoProfissionalEquipeDTO> lista = new ArrayList<SubstituicaoProfissionalEquipeDTO>();
        try {

            String sql = "select a.dtaatende, sf.*, spe.cod_profissional_substituido , spe.cod_profissional_substituto  from logs.substituicao_profissional_equipe_atendimentos1 sf \n" +
                    " join logs.substituicao_profissional_equipe  spe on spe.id  = sf.id_substituicao_profissional_equipe"+
                    "	join hosp.atendimentos1 a1 on a1.id_atendimentos1 = sf.id_atendimentos1 \n" +
                    "	join hosp.atendimentos a on a.id_atendimento = a1.id_atendimento \n" +
                    "	where sf.id_atendimentos1 in ( \n" +
                    "	SELECT DISTINCT a1.id_atendimentos1 FROM hosp.atendimentos1 a1  \n" +
                    "LEFT JOIN hosp.atendimentos a ON (a.id_atendimento = a1.id_atendimento)  \n" +
                    "WHERE a.id_paciente_instituicao = ? AND a.dtaatende >= current_date  \n" +
                    "AND  (SELECT count(*) FROM hosp.atendimentos1 aa1 WHERE aa1.id_atendimento = a1.id_atendimento) =  \n" +
                    "(SELECT count(*) FROM hosp.atendimentos1 aaa1 WHERE aaa1.id_atendimento = a1.id_atendimento AND situacao IS NULL)  \n" +
                    ")";

            ps = null;
            ps = conAuxiliar.prepareStatement(sql);
            ps.setLong(1, idPacienteInstituicao);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                SubstituicaoProfissionalEquipeDTO substituicao = new SubstituicaoProfissionalEquipeDTO();
                substituicao.setDataAtendimento(rs.getDate("dtaatende"));
                substituicao.setId(rs.getInt("id_substituicao_profissional_equipe"));
                substituicao.setIdAtendimentos1(rs.getInt("id_atendimentos1"));
                substituicao.getFuncionarioRemovido().setId(rs.getLong("cod_profissional_substituido"));
                substituicao.getFuncionarioAssumir().setId(rs.getLong("cod_profissional_substituto"));
                lista.add(substituicao);
            }

        } catch (SQLException sqle) {
            conAuxiliar.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conAuxiliar.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
        return lista;
    }

    public ArrayList<InsercaoProfissionalEquipe> listaAtendimentosQueTiveramInsercaoProfissionalAtendimentoEquipePeloIdAtendimentoCodProfissionalAtendimento
            (Integer idAtendimentos, Long codProfissionalAtendimento, Connection conAuxiliar) throws SQLException, ProjetoException {

        ArrayList<InsercaoProfissionalEquipe> lista = new ArrayList<InsercaoProfissionalEquipe>();
        try {
            String sql = "select distinct a.dtaatende, a.codprograma, a.codgrupo, ipe.id_atendimentos1, id_insercao_profissional_equipe_atendimento, id_profissional, a1.cbo codcbo "+
            		" from adm.insercao_profissional_equipe_atendimento_1 ipe \n" +
                    "	join hosp.atendimentos1 a1 on a1.id_atendimentos1 = ipe.id_atendimentos1 \n" +
                    "	join hosp.atendimentos a on a.id_atendimento = a1.id_atendimento \n" +
                    " join acl.funcionarios f on f.id_funcionario = ipe.id_profissional " +
                    "	where ipe.id_atendimentos1 in ( \n" +
                    "	SELECT DISTINCT a1.id_atendimentos1 FROM hosp.atendimentos1 a1  \n" +
                    "LEFT JOIN hosp.atendimentos a ON (a.id_atendimento = a1.id_atendimento)  \n" +
                    "WHERE a.id_atendimento = ? AND a1.codprofissionalatendimento =?  \n" +
                    "AND  (SELECT count(*) FROM hosp.atendimentos1 aa1 WHERE aa1.id_atendimento = a1.id_atendimento) =  \n" +
                    "(SELECT count(*) FROM hosp.atendimentos1 aaa1 WHERE aaa1.id_atendimento = a1.id_atendimento AND situacao IS NULL)  \n" +
                    ")";

            ps = null;
            ps = conAuxiliar.prepareStatement(sql);
            ps.setLong(1, idAtendimentos);
            ps.setLong(2, codProfissionalAtendimento);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                InsercaoProfissionalEquipe insercao = new InsercaoProfissionalEquipe();
                insercao.setDataAtendimento(rs.getDate("dtaatende"));
                insercao.setIdAtendimentos1(rs.getInt("id_atendimentos1"));
                insercao.setId(rs.getInt("id_insercao_profissional_equipe_atendimento"));
                insercao.getFuncionario().setId(rs.getLong("id_profissional"));
                insercao.getAtendimentoBean().getCbo().setCodCbo(rs.getInt("codcbo"));
                insercao.getPrograma().setIdPrograma(rs.getInt("codprograma"));
                insercao.getGrupo().setIdGrupo(rs.getInt("codgrupo"));
                lista.add(insercao);
            }

        } catch (SQLException sqle) {
            conAuxiliar.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conAuxiliar.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
        return lista;
    }

    public ArrayList<RemocaoProfissionalEquipe> listaAtendimentosQueTiveramRemocaoProfissionalAtendimentoEquipePeloIdAtendimentoCodProfissionalAtendimento
            (Integer idAtendimentos, Long codProfissionalAtendimento, Connection conAuxiliar) throws SQLException, ProjetoException {

        ArrayList<RemocaoProfissionalEquipe> lista = new ArrayList<RemocaoProfissionalEquipe>();

        try {
            String sql = "select distinct a.dtaatende, a.codprograma, a.codgrupo, rpea.id_atendimentos1, id_remocao_profissional_equipe_atendimento, id_profissional, a1.cbo codcbo "+
            		" from adm.remocao_profissional_equipe_atendimento_1 rpea \n" +
                    "	join hosp.atendimentos1 a1 on a1.id_atendimentos1 = rpea.id_atendimentos1 \n" +
                    "	join hosp.atendimentos a on a.id_atendimento = a1.id_atendimento \n" +
                    " join acl.funcionarios f on f.id_funcionario = rpea.id_profissional \n" +
                    "	where rpea.id_atendimentos1 in ( \n" +
                    "	SELECT DISTINCT a1.id_atendimentos1 FROM hosp.atendimentos1 a1  \n" +
                    "LEFT JOIN hosp.atendimentos a ON (a.id_atendimento = a1.id_atendimento)  \n" +
                    "WHERE a.id_atendimento = ? AND a1.codprofissionalatendimento =?  \n" +
                    "AND  (SELECT count(*) FROM hosp.atendimentos1 aa1 WHERE aa1.id_atendimento = a1.id_atendimento) =  \n" +
                    "(SELECT count(*) FROM hosp.atendimentos1 aaa1 WHERE aaa1.id_atendimento = a1.id_atendimento AND situacao IS NULL)  \n" +
                    ")";

            ps = null;
            ps = conAuxiliar.prepareStatement(sql);
            ps.setLong(1, idAtendimentos);
            ps.setLong(2, codProfissionalAtendimento);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                RemocaoProfissionalEquipe remocao = new RemocaoProfissionalEquipe();
                remocao.setDataAtendimento(rs.getDate("dtaatende"));
                remocao.setIdAtendimentos1(rs.getInt("id_atendimentos1"));
                remocao.setId(rs.getInt("id_insercao_profissional_equipe_atendimento"));
                remocao.getFuncionario().setId(rs.getLong("id_profissional"));
                remocao.getAtendimentoBean().getCbo().setCodCbo(rs.getInt("codcbo"));
                remocao.getPrograma().setIdPrograma(rs.getInt("codprograma"));
                remocao.getGrupo().setIdGrupo(rs.getInt("codgrupo"));
                lista.add(remocao);
            }

        } catch (SQLException sqle) {
            conAuxiliar.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conAuxiliar.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
        return lista;
    }

    public ArrayList<SubstituicaoProfissional> listaAtendimentosQueTiveramSubstituicaoProfissionalEmUmAtendimento
            (Integer idAtendimentos, Connection conAuxiliar) throws ProjetoException, SQLException {

        ArrayList<SubstituicaoProfissional> lista = new ArrayList<SubstituicaoProfissional>();
        try {
            String sql = "select a.dtaatende, sf.*, c.id id_cbo from adm.substituicao_funcionario sf " +
                    "	join hosp.atendimentos1 a1 on a1.id_atendimentos1 = sf.id_atendimentos1 " +
                    "	join hosp.atendimentos a on a.id_atendimento = a1.id_atendimento " +
                    "	left join acl.funcionarios f on f.id_funcionario = a1.codprofissionalatendimento " +
                    "	left join hosp.cbo c on c.id = a1.cbo " +
                    "	where sf.id_atendimentos1 in ( " +
                    "	SELECT DISTINCT a1.id_atendimentos1 FROM hosp.atendimentos1 a1 " +
                    "LEFT JOIN hosp.atendimentos a ON (a.id_atendimento = a1.id_atendimento) " +
                    "WHERE a.id_atendimento = ?)";

            ps = null;
            ps = conAuxiliar.prepareStatement(sql);
            ps.setLong(1, idAtendimentos);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                SubstituicaoProfissional substituicao = new SubstituicaoProfissional();
                substituicao.setDataAtendimento(rs.getDate("dtaatende"));
                substituicao.getAfastamentoProfissional().setId(rs.getInt("id_afastamento_funcionario"));
                substituicao.setIdAtendimentos1(rs.getInt("id_atendimentos1"));
                substituicao.getAfastamentoProfissional().getFuncionario().setId(rs.getLong("id_funcionario_substituido"));
                substituicao.getFuncionario().setId(rs.getLong("id_funcionario_substituto"));
                substituicao.getUsuarioAcao().setId(rs.getLong("usuario_acao"));
                substituicao.setDataHoraAcao(rs.getTimestamp("data_hora_acao"));
                substituicao.getAtendimento().getCbo().setCodCbo(rs.getInt("id_cbo"));
                lista.add(substituicao);
            }
        } catch (SQLException sqle) {
            conAuxiliar.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conAuxiliar.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
        return lista;
    }

    public Boolean gravarLiberacao(Integer idPacienteInstituicao, ArrayList<Liberacao> listaLiberacao, Integer codAtendimento, Connection conAuxiliar)
            throws ProjetoException, SQLException {

        Boolean retorno = false;

        String sql = "INSERT INTO hosp.liberacoes (motivo, usuario_liberacao, data_hora_liberacao, codatendimento, codpaciente_instituicao, cod_unidade) "
                + " VALUES  (?, ?, CURRENT_TIMESTAMP, ?, ?, ?)";

        try {
            ps = null;

            for(int i=0; i<listaLiberacao.size(); i++) {
                ps = conAuxiliar.prepareStatement(sql);

                ps.setString(1, listaLiberacao.get(i).getMotivo());
                ps.setLong(2, listaLiberacao.get(i).getIdUsuarioLiberacao());
                ps.setInt(3, codAtendimento);
                ps.setInt(4, idPacienteInstituicao);
                ps.setInt(5, user_session.getUnidade().getId());

                ps.executeUpdate();
            }
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

    public ArrayList<AtendimentoBean> listaAtendimentos1QueTiveramLiberacoes(Integer idPacienteInstituicao, Connection conexao)
            throws SQLException, ProjetoException {
        String sql = "select distinct a1.id_atendimentos1, a1.id_atendimento from hosp.atendimentos1 a1 " +
                "	join hosp.atendimentos a on a1.id_atendimento = a1.id_atendimento " +
                "	join hosp.liberacoes l on l.id_atendimentos1 = a1.id_atendimentos1 " +
                "	where a.id_paciente_instituicao = ? and (a1.excluido is null or a1.excluido = 'N') ;";

        ArrayList<AtendimentoBean> listaAtendimentos1 = new ArrayList<AtendimentoBean>();

        try {
            ps = null;
            ps = conexao.prepareStatement(sql);
            ps.setInt(1, idPacienteInstituicao);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                AtendimentoBean atendimento = new AtendimentoBean();
                atendimento.setId(rs.getInt("id_atendimento"));
                atendimento.setId1(rs.getInt("id_atendimentos1"));
                listaAtendimentos1.add(atendimento);
            }
        } catch (SQLException sqle) {
            conexao.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conexao.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
        return listaAtendimentos1;
    }


    public List<ProcedimentoCidDTO> listaProcedimentosCIDsPacienteInstituicao (Integer idPacienteInstituicao)
    		throws ProjetoException, SQLException {

        List<ProcedimentoCidDTO> lista = new ArrayList<>();
        try {
            String sql = "select p.id, p.nome, p.codproc, c.cod, c.cid, c.desccidabrev " + 
            		"	from hosp.paciente_instituicao_procedimento_cid pipc " + 
            		"	join hosp.paciente_instituicao pi on pipc.id_paciente_instituicao = pi.id " + 
            		"	join hosp.proc p on pipc.id_procedimento = p.id " + 
            		"	join hosp.cid c on pipc.id_cid = c.cod " + 
            		"	where pi.id = ? and p.ativo = 'S' order by p.nome;";

            conexao = ConnectionFactory.getConnection();
            ps = null;
            ps = conexao.prepareStatement(sql);
            ps.setLong(1, idPacienteInstituicao);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ProcedimentoCidDTO procedimentoCidDTO = new ProcedimentoCidDTO();
                procedimentoCidDTO.getProcedimento().setIdProc(rs.getInt("id"));
                procedimentoCidDTO.getProcedimento().setNomeProc(rs.getString("nome"));
                procedimentoCidDTO.getProcedimento().setCodProc(rs.getString("codproc"));
                procedimentoCidDTO.getCid().setIdCid(rs.getInt("cod"));
                procedimentoCidDTO.getCid().setCid(rs.getString("cid"));
                procedimentoCidDTO.getCid().setDescCidAbrev(rs.getString("desccidabrev"));
                lista.add(procedimentoCidDTO);
            }
        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
			try {
				conexao.close();
			} catch (Exception e) {
				//comentado walter erro log ex.printStackTrace();
			}
		}
        return lista;
    }
    
    public Boolean tipoAtendimentoValidaPacienteAtivo(Integer idTipoAtendimento) throws ProjetoException {

        Boolean retorno = false;

        String sql = "select t.agenda_avulsa_valida_paciente_ativo from hosp.tipoatendimento t where t.id = ?;";
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, idTipoAtendimento);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                retorno = rs.getBoolean("agenda_avulsa_valida_paciente_ativo");
            }

        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                //comentado walter erro log ex.printStackTrace();
            }
        }
        return retorno;
    }
    
	public boolean funcionarioEstaAfastadoDurantePeriodo(
			FuncionarioBean funcionario, Date dataAtendimento, Connection conAuxiliar) throws ProjetoException, SQLException {

		boolean existeAfastamento = false;
		try {
			String sql = "select exists (select af.id_funcionario_afastado \r\n" + 
					"	from adm.afastamento_funcionario af \r\n" + 
					"	where af.id_funcionario_afastado = ? and ( \r\n" + 
					"		(af.motivo_afastamento = 'DE' and inicio_afastamento < ?) \r\n" + 
					"		or (? between af.inicio_afastamento and af.fim_afastamento) ) ) ";

			ps = null;
			ps = conAuxiliar.prepareStatement(sql);
			ps.setLong(1, funcionario.getId());
			ps.setDate(2, new java.sql.Date(dataAtendimento.getTime()));
			ps.setDate(3, new java.sql.Date(dataAtendimento.getTime()));
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				existeAfastamento = rs.getBoolean("exists");
			}
			if(existeAfastamento) {
				JSFUtil.adicionarMensagemAdvertencia("Não é possível completar o Agendamento pois o funcionário "
						+funcionario.getNome()+" está afastado durante este período ou foi desligado da instituição", "");
				conAuxiliar.rollback();
			}	
		} catch (SQLException sqle) {
			conAuxiliar.rollback();
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(),
					sqle);
		} catch (Exception ex) {
			conAuxiliar.rollback();
			throw new ProjetoException(ex, this.getClass().getName());
		}
		return existeAfastamento;
	}
}
