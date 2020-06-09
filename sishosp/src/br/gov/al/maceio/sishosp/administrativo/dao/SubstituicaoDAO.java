package br.gov.al.maceio.sishosp.administrativo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.administrativo.model.AfastamentoProfissional;
import br.gov.al.maceio.sishosp.administrativo.model.SubstituicaoProfissional;
import br.gov.al.maceio.sishosp.administrativo.model.dto.BuscaAgendamentosParaFuncionarioAfastadoDTO;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.DataUtil;
import br.gov.al.maceio.sishosp.comum.util.TratamentoErrosUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.model.AtendimentoBean;

public class SubstituicaoDAO {

    Connection con = null;
    PreparedStatement ps = null;


    public Boolean substituirFuncionario(List<AtendimentoBean> listaSelecionados,SubstituicaoProfissional substituicaoFuncionario) {

        Boolean retorno = false;

        String sql = "UPDATE hosp.atendimentos1 SET codprofissionalatendimento = ? WHERE id_atendimentos1 = ? and codprofissionalatendimento=?";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);

            for(int i=0; i<listaSelecionados.size(); i++) {
                stmt.setLong(1, substituicaoFuncionario.getFuncionario().getId());
                stmt.setInt(2, listaSelecionados.get(i).getId1());
                stmt.setLong(3, substituicaoFuncionario.getAfastamentoProfissional().getFuncionario().getId());

                stmt.executeUpdate();

                gravarSubstituicao(listaSelecionados.get(i), substituicaoFuncionario, con);

            }

            con.commit();

            retorno = true;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            return retorno;
        }
    }

    public boolean gravarSubstituicao(AtendimentoBean atendimentoBean, SubstituicaoProfissional substituicaoFuncionario, Connection conAuxiliar)
    		throws ProjetoException, SQLException {

        Boolean retorno = false;

        String sql = "INSERT INTO adm.substituicao_funcionario " +
                "(id_afastamento_funcionario, id_atendimentos1, id_funcionario_substituido, id_funcionario_substituto, usuario_acao, data_hora_acao) " +
                "VALUES(?, ?, ?, ?, ?, CURRENT_TIMESTAMP );";
        try {

            FuncionarioBean user_session = (FuncionarioBean) FacesContext
                    .getCurrentInstance().getExternalContext().getSessionMap()
                    .get("obj_usuario");

            ps = conAuxiliar.prepareStatement(sql);
            ps.setInt(1, substituicaoFuncionario.getAfastamentoProfissional().getId());
            ps.setInt(2, atendimentoBean.getId1());
            ps.setLong(3, substituicaoFuncionario.getAfastamentoProfissional().getFuncionario().getId());
            ps.setLong(4, substituicaoFuncionario.getFuncionario().getId());
            ps.setLong(5, user_session.getId());

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
    
	public boolean validaPeriodoAfastamentoNaBuscaSubstituicao(AfastamentoProfissional afastamento, Date periodoInicioBusca, Date periodoFimBusca) {
		boolean periodoValidoAfastamentoNaBuscaSubstituicao = false;
		List<AfastamentoProfissional> lista = new ArrayList<>();

		String sql = "select id from adm.afastamento_funcionario where id_funcionario_afastado	=?\n"
				+ "	and ((? between inicio_afastamento and fim_afastamento)\n"
				+ "	and (? between inicio_afastamento and fim_afastamento)) and afastamento_funcionario.id=? ";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setLong(1, afastamento.getFuncionario().getId());
			stm.setDate(2, DataUtil.converterDateUtilParaDateSql(periodoInicioBusca));
			stm.setDate(3, DataUtil.converterDateUtilParaDateSql(periodoFimBusca));
			stm.setInt(4, afastamento.getId());
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				periodoValidoAfastamentoNaBuscaSubstituicao = true;
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
		return periodoValidoAfastamentoNaBuscaSubstituicao;
	}

    public List<AtendimentoBean> listarHorariosParaSeremSubstituidos(BuscaAgendamentosParaFuncionarioAfastadoDTO buscaAgendamentosParaFuncionarioAfastadoDTO, String motivoAfastamento) {

        List<AtendimentoBean> lista = new ArrayList<>();
        String sql = "";
        if (!motivoAfastamento.equals("DE"))
        sql = "SELECT a1.id_atendimentos1, a.codgrupo, g.descgrupo, a.codprograma, p.descprograma, a.dtaatende, " +
                "CASE WHEN a.turno = 'M' THEN 'Manhã' WHEN a.turno = 'T' THEN 'Tarde' END AS turno " +
                "FROM hosp.atendimentos1 a1 " +
                "JOIN hosp.atendimentos a ON (a1.id_atendimento = a.id_atendimento) " +
                "JOIN hosp.grupo g ON (a.codgrupo = g.id_grupo) " +
                "JOIN hosp.programa p ON (a.codprograma = p.id_programa) " +
                "WHERE a1.codprofissionalatendimento = ? AND a.dtaatende >= ? AND a.dtaatende <= ? ";
        else
            sql = "SELECT a1.id_atendimentos1, a.codgrupo, g.descgrupo, a.codprograma, p.descprograma, a.dtaatende, " +
                    "CASE WHEN a.turno = 'M' THEN 'Manhã' WHEN a.turno = 'T' THEN 'Tarde' END AS turno " +
                    "FROM hosp.atendimentos1 a1 " +
                    "JOIN hosp.atendimentos a ON (a1.id_atendimento = a.id_atendimento) " +
                    "JOIN hosp.grupo g ON (a.codgrupo = g.id_grupo) " +
                    "JOIN hosp.programa p ON (a.codprograma = p.id_programa) " +
                    "WHERE a1.codprofissionalatendimento = ? AND a.dtaatende >= ?";
        
        if (!VerificadorUtil.verificarSeObjetoNulo(buscaAgendamentosParaFuncionarioAfastadoDTO.getPrograma())) 
        if (!VerificadorUtil.verificarSeObjetoNulo(buscaAgendamentosParaFuncionarioAfastadoDTO.getPrograma().getIdPrograma())) {
            sql = sql + "AND a.codprograma = ? ";
        }        
        
        if (!VerificadorUtil.verificarSeObjetoNulo(buscaAgendamentosParaFuncionarioAfastadoDTO.getGrupo())) 
        if (!VerificadorUtil.verificarSeObjetoNulo(buscaAgendamentosParaFuncionarioAfastadoDTO.getGrupo().getIdGrupo())) {
            sql = sql + "AND a.codgrupo = ?";
        }
        
        if (!buscaAgendamentosParaFuncionarioAfastadoDTO.getTurno().equals("A"))
            sql = sql + "AND a.turno = ?";



        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setLong(1, buscaAgendamentosParaFuncionarioAfastadoDTO.getFuncionario().getId());
            stm.setDate(2, DataUtil.converterDateUtilParaDateSql(buscaAgendamentosParaFuncionarioAfastadoDTO.getPeriodoInicio()));
            
            int i = 2;
            
            if (!motivoAfastamento.equals("DE")) {
            	i = i + 1;
            stm.setDate(i, DataUtil.converterDateUtilParaDateSql(buscaAgendamentosParaFuncionarioAfastadoDTO.getPeriodoFinal()));
            
            }
            if (!VerificadorUtil.verificarSeObjetoNulo(buscaAgendamentosParaFuncionarioAfastadoDTO.getPrograma())) 
            if (!VerificadorUtil.verificarSeObjetoNulo(buscaAgendamentosParaFuncionarioAfastadoDTO.getPrograma().getIdPrograma())) {
                i = i + 1;
                stm.setInt(i, buscaAgendamentosParaFuncionarioAfastadoDTO.getPrograma().getIdPrograma());
            }

            if (!VerificadorUtil.verificarSeObjetoNulo(buscaAgendamentosParaFuncionarioAfastadoDTO.getGrupo())) 
            if (!VerificadorUtil.verificarSeObjetoNulo(buscaAgendamentosParaFuncionarioAfastadoDTO.getGrupo().getIdGrupo())) {
                i = i + 1;
                stm.setInt(i, buscaAgendamentosParaFuncionarioAfastadoDTO.getGrupo().getIdGrupo());
            }
            
            if (!buscaAgendamentosParaFuncionarioAfastadoDTO.getTurno().equals("A")) {
                i = i + 1;
                stm.setString(i, buscaAgendamentosParaFuncionarioAfastadoDTO.getTurno()); 	
            }

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                AtendimentoBean atendimentoBean = new AtendimentoBean();
                atendimentoBean.setId1(rs.getInt("id_atendimentos1"));
                atendimentoBean.getGrupo().setIdGrupo(rs.getInt("codgrupo"));
                atendimentoBean.getGrupo().setDescGrupo(rs.getString("descgrupo"));
                atendimentoBean.getPrograma().setIdPrograma(rs.getInt("codprograma"));
                atendimentoBean.getPrograma().setDescPrograma(rs.getString("descprograma"));
                atendimentoBean.setDataAtendimentoInicio(rs.getDate("dtaatende"));
                atendimentoBean.setTurno(rs.getString("turno"));

                lista.add(atendimentoBean);
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

}
