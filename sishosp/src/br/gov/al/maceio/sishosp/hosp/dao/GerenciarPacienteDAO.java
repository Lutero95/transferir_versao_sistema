package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.GerenciarPacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.Liberacao;

import javax.faces.context.FacesContext;

public class GerenciarPacienteDAO {

    PreparedStatement ps = null;
    private Connection conexao = null;

    FuncionarioBean user_session = (FuncionarioBean) FacesContext
            .getCurrentInstance().getExternalContext().getSessionMap()
            .get("obj_usuario");

   
    public List<GerenciarPacienteBean> carregarPacientesInstituicaoBusca(
            GerenciarPacienteBean gerenciar, String campoBusca, String tipoBusca) throws ProjetoException {

        String sql = "select p.id, p.codprograma,prog.descprograma, p.codgrupo, g.descgrupo,coalesce(g.qtdfrequencia,0) qtdfrequencia, coalesce(l.codpaciente, p.id_paciente) codpaciente, pa.nome, pa.matricula, pa.cns, p.codequipe, e.descequipe, "
                + " p.codprofissional, f.descfuncionario, p.status, p.codlaudo, p.data_solicitacao, p.observacao, p.data_cadastro, pr.utiliza_equipamento, pr.codproc , pr.nome as procedimento, "
                + "coalesce((SELECT * FROM hosp.fn_GetLastDayOfMonth(to_date(ano_final||'-'||'0'||''||mes_final||'-'||'01', 'YYYY-MM-DD'))),\n" + 
                " date_trunc('month',p.data_solicitacao+ interval '2 months') + INTERVAL'1 month' - INTERVAL'1 day') as datafinal "
                + " from hosp.paciente_instituicao p "
                + " left join hosp.laudo l on (l.id_laudo = p.codlaudo) "
                + " left join hosp.proc pr on (pr.id = coalesce(l.codprocedimento_primario, p.codprocedimento_primario_laudo_anterior)) "
                + " left join hosp.pacientes pa on (coalesce(l.codpaciente, p.id_paciente) = pa.id_paciente) "
                + " left join hosp.equipe e on (p.codequipe = e.id_equipe) "
                + " left join acl.funcionarios f on (p.codprofissional = f.id_funcionario) "
                + " left join hosp.grupo g on (g.id_grupo = p.codgrupo) "
                + " left join hosp.programa prog on (prog.id_programa = p.codprograma) "
                + " where 1=1";
                        if ((gerenciar.getPrograma()!=null) && (gerenciar.getPrograma().getIdPrograma()!=null)) {
                        	sql = sql + " and  p.codprograma = ?";
                            }
                            if ((gerenciar.getGrupo()!=null) && (gerenciar.getGrupo().getIdGrupo()!=null)) {            
                            	sql = sql + " and  p.codgrupo = ?";
                            }
                		
		if ((tipoBusca.equals("paciente") && (!campoBusca.equals(null)) && (!campoBusca.equals("")))) {
			sql = sql + " and pa.nome ilike ?";
		}

		if ((tipoBusca.equals("codproc") && (!campoBusca.equals(null)) && (!campoBusca.equals("")))) {
			sql = sql + " and pr.codproc = ?";
		}
		
		if ((tipoBusca.equals("matpaciente") && (!campoBusca.equals(null)) && (!campoBusca.equals("")))) {
			sql = sql + " and upper(pa.matricula) = ?";
		}
		
		if ((tipoBusca.equals("prontpaciente") && (!campoBusca.equals(null)) && (!campoBusca.equals("")))) {
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
            int i = 1;
            if ((gerenciar.getPrograma()!=null) && (gerenciar.getPrograma().getIdPrograma()!=null)) {
            stmt.setInt(i, gerenciar.getPrograma().getIdPrograma());
            i = i+1;
            }
            if ((gerenciar.getGrupo()!=null) && (gerenciar.getGrupo().getIdGrupo()!=null)) {            
            stmt.setInt(i, gerenciar.getGrupo().getIdGrupo());
            i = i+1;
            }
			if ((tipoBusca.equals("paciente") && (!campoBusca.equals(null)) && (!campoBusca.equals("")))) {
				stmt.setString(i, "%" + campoBusca.toUpperCase() + "%");
	            i = i+1;
			}

			if (((tipoBusca.equals("codproc") || (tipoBusca.equals("matpaciente"))) && (!campoBusca.equals(null)) && (!campoBusca.equals("")))) {
				stmt.setString(i, campoBusca.toUpperCase());
	            i = i+1;
			}
			
			if ((tipoBusca.equals("prontpaciente") && (!campoBusca.equals(null)) && (!campoBusca.equals("")))) {
				stmt.setInt(i,Integer.valueOf((campoBusca.toUpperCase())));
	            i = i+1;
			}

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                GerenciarPacienteBean gp = new GerenciarPacienteBean();

                gp.setId(rs.getInt("id"));
                gp.getPrograma().setIdPrograma(rs.getInt("codprograma"));
                gp.getPrograma().setDescPrograma(rs.getString("descprograma"));
                gp.getGrupo().setIdGrupo(rs.getInt("codgrupo"));
                gp.getGrupo().setDescGrupo(rs.getString("descgrupo"));
                gp.getGrupo().setQtdFrequencia(rs.getInt("qtdfrequencia"));
                gp.getEquipe().setCodEquipe(rs.getInt("codequipe"));
                gp.getEquipe().setDescEquipe(rs.getString("descequipe"));
                gp.getFuncionario().setId(rs.getLong("codprofissional"));
                gp.getFuncionario().setNome(rs.getString("descfuncionario"));
                gp.setStatus(rs.getString("status"));
                gp.getLaudo().setId(rs.getInt("codlaudo"));
                gp.getLaudo().getProcedimentoPrimario().setCodProc(rs.getString("codproc"));
                gp.getLaudo().getProcedimentoPrimario().setNomeProc(rs.getString("procedimento"));
                gp.getLaudo().getPaciente().setId_paciente(rs.getInt("codpaciente"));
                gp.getLaudo().getPaciente().setNome(rs.getString("nome"));
                gp.getLaudo().getPaciente().setMatricula(rs.getString("matricula"));
                gp.getLaudo().getPaciente().setCns(rs.getString("cns"));
                gp.getLaudo().setVigenciaFinal(rs.getDate("datafinal"));
                gp.setData_solicitacao(rs.getDate("data_solicitacao"));
                gp.setObservacao(rs.getString("observacao"));
                gp.setData_cadastro(rs.getDate("data_cadastro"));
                gp.getLaudo().getProcedimentoPrimario().setUtilizaEquipamento(rs.getBoolean("utiliza_equipamento"));

                lista.add(gp);

            }
        } catch (Exception ex) {
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

    public Boolean desligarPaciente(GerenciarPacienteBean gerenciarRow,
                                    GerenciarPacienteBean gerenciar) throws ProjetoException {

        Boolean retorno = false;
        final String DESLIGADO = "D";

        
        String sql = "update hosp.paciente_instituicao set status = ? "
                + " where id = ?";
        try {
            conexao = ConnectionFactory.getConnection();
            
            GerenciarPacienteDAO gerenciarPacienteDAO = new GerenciarPacienteDAO();
            TransferenciaPacienteDAO gerenciarTransferenciaDAO = new TransferenciaPacienteDAO();
                    if(!gerenciarTransferenciaDAO.apagarAtendimentosNaRenovacao(gerenciarRow.getId(), gerenciar.getDataDesligamento(), conexao)){

                        conexao.close();

                        return retorno;
                    }
            
            PreparedStatement stmt = conexao.prepareStatement(sql);

            stmt.setString(1, DESLIGADO);
            stmt.setInt(2, gerenciarRow.getId());
            stmt.executeUpdate();

            String sql2 = "INSERT INTO hosp.historico_paciente_instituicao (codpaciente_instituicao, data_operacao, motivo_desligamento, tipo, observacao, id_funcionario_gravacao, data_desligamento) "
                    + " VALUES  (?, current_timestamp, ?, ?, ?,?, ?)";
            stmt = conexao.prepareStatement(sql2);
            stmt.setLong(1, gerenciarRow.getId());
            stmt.setInt(2, gerenciar.getMotivo_desligamento());
            stmt.setString(3, DESLIGADO);
            stmt.setString(4, gerenciar.getObservacao());
            stmt.setLong(5, user_session.getId());
            stmt.setDate(6, new java.sql.Date(gerenciar.getDataDesligamento().getTime()));
            stmt.executeUpdate();

            conexao.commit();

            retorno = true;

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return retorno;
    }

    public Boolean encaminharPaciente(GerenciarPacienteBean row,
                                      GerenciarPacienteBean gerenciar) throws ProjetoException {

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
            stmt.setString(2, "D");
            stmt.setString(3, gerenciar.getObservacao());
            stmt.setLong(4, user_session.getId());

            stmt.executeUpdate();

            conexao.commit();

            retorno = true;

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return retorno;
    }

    public Boolean verificarPacienteAtivoInstituicao(Integer codPaciente) throws ProjetoException {

        Boolean retorno = false;

        String sql = " SELECT id FROM hosp.paciente_instituicao pi " + 
        		" join hosp.laudo l on l.id_laudo = pi.codlaudo" + 
        		" WHERE pi.status = 'A' AND l.codpaciente = ?";

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);

            stmt.setInt(1, codPaciente);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                retorno = true;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return retorno;
    }

    public Boolean gravarHistoricoAcaoPaciente(Integer idPacienteInstituicao, String observacao, String tipo, Connection conAuxiliar) {

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

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return retorno;
    }

    public Boolean apagarAtendimentos(Integer idPacienteInstituicao, Connection conAuxiliar, Boolean alteracaoDePaciente) {

        Boolean retorno = false;
        ArrayList<Integer> lista = new ArrayList<Integer>();

        try {

            String sql = "SELECT DISTINCT a1.id_atendimento FROM hosp.atendimentos1 a1 " +
                    "LEFT JOIN hosp.atendimentos a ON (a.id_atendimento = a1.id_atendimento) " +
                    "WHERE a.id_paciente_instituicao = ? AND a.dtaatende >= current_date AND  " +
                    "(SELECT count(*) FROM hosp.atendimentos1 aa1 WHERE aa1.id_atendimento = a1.id_atendimento) = " +
                    "(SELECT count(*) FROM hosp.atendimentos1 aaa1 WHERE aaa1.id_atendimento = a1.id_atendimento AND situacao IS NULL) " +
                    "ORDER BY a1.id_atendimento;";


            ps = null;
            ps = conAuxiliar.prepareStatement(sql);
            ps.setLong(1, idPacienteInstituicao);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(rs.getInt("id_atendimento"));
            }

            for (int i = 0; i < lista.size(); i++) {
                String sql2 = "delete from hosp.atendimentos1 where id_atendimento = ?";

                PreparedStatement ps2 = null;
                ps2 = conAuxiliar.prepareStatement(sql2);
                ps2.setLong(1, lista.get(i));
                ps2.execute();
            }

            for (int i = 0; i < lista.size(); i++) {
                String sql3 = "delete from hosp.atendimentos where id_atendimento = ?";

                PreparedStatement ps3 = null;
                ps3 = conAuxiliar.prepareStatement(sql3);
                ps3.setLong(1, lista.get(i));
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

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return retorno;
    }

    public Boolean gravarLiberacao(Integer idPacienteInstituicao, ArrayList<Liberacao> listaLiberacao, Integer codAtendimento, Connection conAuxiliar) {

        Boolean retorno = false;

        String sql = "INSERT INTO hosp.liberacoes (motivo, usuario_liberacao, data_hora_liberacao, codatendimento, codpaciente_instituicao, cod_unidade, rotina) "
                + " VALUES  (?, ?, CURRENT_TIMESTAMP, ?, ?, ?, ?)";

        try {
            ps = null;

            for(int i=0; i<listaLiberacao.size(); i++) {
                ps = conAuxiliar.prepareStatement(sql);

                ps.setString(1, listaLiberacao.get(i).getMotivo());
                ps.setLong(2, listaLiberacao.get(i).getIdUsuarioLiberacao());
                ps.setInt(3, codAtendimento);
                ps.setInt(4, idPacienteInstituicao);
                ps.setInt(5, user_session.getUnidade().getId());
                ps.setString(6, listaLiberacao.get(i).getRotina());

                ps.executeUpdate();
            }

            retorno = true;

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return retorno;
    }

}
