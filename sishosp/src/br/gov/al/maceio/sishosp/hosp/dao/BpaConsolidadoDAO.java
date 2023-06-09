package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.DataUtil;
import br.gov.al.maceio.sishosp.comum.util.TratamentoErrosUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.control.LaudoController;
import br.gov.al.maceio.sishosp.hosp.model.BpaConsolidadoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProcedimentoBean;

public class BpaConsolidadoDAO {
	//TODO: verificar usos de static e final
	private static final String CODIGO_BPA_CONSOLIDADO = "01";

	/* ESTA CONSTANTE SERÁ SUBSTITUÍDA DEPOIS POR DADOS DO BANCO */
	private static final String PRD_IDADE = "000";

	public List<BpaConsolidadoBean> carregaDadosBpaConsolidado(Date dataInicio, Date dataFim, String competencia, String tipoGeracao, List<ProcedimentoBean> listaProcedimentosFiltro,List<Integer> idUnidades, Integer codigoConfiguracaoProducao) throws ProjetoException {

		List<BpaConsolidadoBean> listaDeBpaConsolidado = new ArrayList<BpaConsolidadoBean>();
		
		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().get("obj_funcionario");

		String sql = "select qtdproc, codproc, cnes, competencia_atual, cbo, atendimento_realizado, presenca, id_situacao_atendimento, id_procedimento  from (\r\n" + 
				"\r\n" + 
				"	select count(*) qtdproc,a.cod_unidade,   proc.codproc, coalesce(cpb.cnes_producao, emp.cnes)  cnes, pm.competencia_atual, cbo.codigo cbo \r\n" + 
				"		,sa.atendimento_realizado, a.presenca, a1.id_situacao_atendimento, a1.codprocedimento as id_procedimento, \r\n" + 
				"		l.situacao, l.data_solicitacao, l.mes_final, l.ano_final \r\n" + 
				"		from hosp.atendimentos1 a1  join acl.funcionarios func on func.id_funcionario  = a1.codprofissionalatendimento \r\n" + 
				"		left join hosp.cbo on cbo.id = a1.cbo  \r\n" + 
				"		join hosp.atendimentos a on a.id_atendimento  = a1.id_atendimento \r\n" + 
				"		left join hosp.paciente_instituicao pi on pi.id  = a.id_paciente_instituicao  \r\n" + 
				"		left join hosp.laudo l on l.id_laudo  = pi.codlaudo" + 
				"		left join hosp.situacao_atendimento sa on sa.id = a1.id_situacao_atendimento  \r\n" + 
				"		join hosp.proc on proc.id = a1.codprocedimento  \r\n" + 
				"		join sigtap.procedimento_mensal pm on pm.id_procedimento  = a1.codprocedimento  \r\n" + 
				"		join sigtap.historico_consumo_sigtap hc on hc.id = pm.id_historico_consumo_sigtap  \r\n" + 
				"		join sigtap.instrumento_registro_procedimento_mensal irpm on irpm.id_procedimento_mensal  = pm.id  \r\n" + 
				"		join sigtap.instrumento_registro ir on ir.id  = irpm.id_instrumento_registro \r\n" + 
				"		cross join hosp.empresa emp  left join hosp.configuracao_producao_bpa cpb on cpb.id = ?  where coalesce(a.cobranca_descartada,false) is false and  hc.status='A' and coalesce(a.situacao, '')<> 'C'\r\n" +
				"		and coalesce(a1.excluido, 'N')= 'N' and \r\n" + 
				"		a.dtaatende  between ? and ? and ir.nome like '%BPA%' \r\n" + 
				"		and ir.codigo = ?  and pm.competencia_atual = ?  \r\n" + 
				"		and coalesce(proc.id_instrumento_registro_padrao, ir.id) = ir.id\r\n" + 
				"		group by  proc.codproc,a.cod_unidade, coalesce(cpb.cnes_producao, emp.cnes) , pm.competencia_atual, cbo.codigo, sa.atendimento_realizado,\r\n" + 
				"		a.presenca, a1.id_situacao_atendimento, a1.codprocedimento, l.situacao, l.data_solicitacao, l.mes_final, l.ano_final   \r\n" + 
				"union \r\n" + 
				"		select count(*) qtdproc, a.cod_unidade,   proc.codproc, coalesce(cpb.cnes_producao, emp.cnes) cnes, pm.competencia_atual, cbo.codigo cbo\r\n" + 
				"		,sa.atendimento_realizado, a.presenca, a1.id_situacao_atendimento, aps.id_procedimento, \r\n" + 
				"		l.situacao, l.data_solicitacao, l.mes_final, l.ano_final  \r\n" + 
				"		from hosp.atendimentos1 a1  join acl.funcionarios func on func.id_funcionario  = a1.codprofissionalatendimento \r\n" + 
				"		left join hosp.cbo on cbo.id = a1.cbo  \r\n" + 
				"		join hosp.atendimentos a on a.id_atendimento  = a1.id_atendimento  \r\n" + 
				"		join hosp.atendimentos1_procedimento_secundario aps on a1.id_atendimentos1 = aps.id_atendimentos1 \r\n" + 
				"		left join hosp.situacao_atendimento sa on sa.id = a1.id_situacao_atendimento  \r\n" + 
				"		left join hosp.paciente_instituicao pi on pi.id  = a.id_paciente_instituicao  \r\n" + 
				"		left join hosp.laudo l on l.id_laudo  = pi.codlaudo" + 
				"		join hosp.proc on proc.id = aps.id_procedimento  \r\n" + 
				"		join sigtap.procedimento_mensal pm on pm.id_procedimento  = aps.id_procedimento  \r\n" + 
				"		join sigtap.historico_consumo_sigtap hc on hc.id = pm.id_historico_consumo_sigtap  \r\n" + 
				"		join sigtap.instrumento_registro_procedimento_mensal irpm on irpm.id_procedimento_mensal  = pm.id  \r\n" + 
				"		join sigtap.instrumento_registro ir on ir.id  = irpm.id_instrumento_registro \r\n" + 
				"		cross join hosp.empresa emp   \r\n" + 
				"		left join hosp.configuracao_producao_bpa cpb on cpb.id = ?   where coalesce(a.cobranca_descartada,false) is false and   hc.status='A' and coalesce(a.situacao, '')<> 'C'\r\n" +
				"		and coalesce(a1.excluido, 'N')= 'N' and  aps.excluido ='N' and  \r\n" + 
				"		a.dtaatende  between ? and ? \r\n" + 
				"		and ir.codigo = ? and ir.nome like '%BPA%'  and pm.competencia_atual = ?  \r\n" + 
				"		and coalesce(proc.id_instrumento_registro_padrao, ir.id) = ir.id  \r\n" + 
				"		group by  proc.codproc,a.cod_unidade,   coalesce(cpb.cnes_producao, emp.cnes), pm.competencia_atual, cbo.codigo, sa.atendimento_realizado,\r\n" + 
				"		a.presenca, a1.id_situacao_atendimento, aps.id_procedimento, l.situacao, l.data_solicitacao, l.mes_final, l.ano_final    \r\n" + 
				") a \r\n" + 
				"where 1= 1 ";
		
		if (listaProcedimentosFiltro.size()>0)
			sql+=" and a.id_procedimento = any(?) ";

		if(!idUnidades.isEmpty())
			sql += " and a.cod_unidade = any(?) ";

		if (tipoGeracao.equals("A")){
			sql+=" and a.atendimento_realizado = true ";
		}
		else
			sql+=" and a.presenca='S' and ((a.atendimento_realizado is true) or (a.id_situacao_atendimento is null)) ";
		
		if(user_session.getUnidade().getParametro().isBpaComLaudoAutorizado()) {
			sql += "and a.situacao = 'A' and ((a.data_solicitacao between ? and ?) \r\n" + 
				"	or hosp.fn_GetLastDayOfMonth(to_date(a.ano_final||'-'||'0'||''||a.mes_final||'-'||'01', 'YYYY-MM-DD'))  between ? and ?) ";
		}

		sql+=" group by  a.qtdproc, a.cod_unidade, a.codproc,  a.cnes, a.competencia_atual, a.cbo, a.atendimento_realizado, a.presenca, a.id_situacao_atendimento, a.id_procedimento order by a.cbo ";

		Connection con = ConnectionFactory.getConnection();

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			if(VerificadorUtil.verificarSeObjetoNuloOuZero(codigoConfiguracaoProducao))
				ps.setNull(1, Types.NULL);
			else
				ps.setInt(1, codigoConfiguracaoProducao);
			ps.setDate(2, new java.sql.Date(dataInicio.getTime()));
			ps.setDate(3, new java.sql.Date(dataFim.getTime()));
			ps.setString(4, CODIGO_BPA_CONSOLIDADO);
			ps.setString(5, competencia);
			if(VerificadorUtil.verificarSeObjetoNuloOuZero(codigoConfiguracaoProducao))
				ps.setNull(6, Types.NULL);
			else
				ps.setInt(6, codigoConfiguracaoProducao);
			ps.setDate(7, new java.sql.Date(dataInicio.getTime()));
			ps.setDate(8, new java.sql.Date(dataFim.getTime()));
			ps.setString(9	, CODIGO_BPA_CONSOLIDADO);
			ps.setString(10, competencia);
			ArrayList<Integer> lista = new ArrayList<>();
			for (int i = 0; i < listaProcedimentosFiltro.size(); i++) {
				lista.add(listaProcedimentosFiltro.get(i).getIdProc());
			}

			int parametro = 11;
			if (listaProcedimentosFiltro.size()>0) {
				ps.setObject(11, ps.getConnection().createArrayOf(  "INTEGER", lista.toArray()));
				parametro++;
			}

			if(!idUnidades.isEmpty()) {
				ps.setObject(parametro, ps.getConnection().createArrayOf(  "INTEGER", idUnidades.toArray()));
				parametro++;
			}
			
			if(user_session.getUnidade().getParametro().isBpaComLaudoAutorizado()) {
				ps.setDate(parametro, new java.sql.Date(dataInicio.getTime()));
				parametro++;
				ps.setDate(parametro, new java.sql.Date(dataFim.getTime()));
				parametro++;
				ps.setDate(parametro, new java.sql.Date(dataInicio.getTime()));
				parametro++;
				ps.setDate(parametro, new java.sql.Date(dataFim.getTime()));
			}
			
			ResultSet rs = ps.executeQuery();
			LaudoController validacaoSigtap = new LaudoController();
			while (rs.next()) {
				BpaConsolidadoBean bpaConsolidado = new BpaConsolidadoBean();
				Date dataSolicitacaoRefSigtap = DataUtil.montarDataCompletaInicioMesPorAnoMesCompetencia(rs.getString("competencia_atual"));
				String cbo = rs.getString("cbo");
				String codProc = rs.getString("codproc");
				bpaConsolidado.getListaInconsistencias().add(validacaoSigtap.validaCboPermitidoProcedimento(dataSolicitacaoRefSigtap, cbo, codProc, null, false));


				bpaConsolidado.setPrdCnes(rs.getString("cnes"));
				bpaConsolidado.setPrdCmp(rs.getString("competencia_atual"));
				bpaConsolidado.setPrdCbo(rs.getString("cbo"));
				bpaConsolidado.setPrdPa(rs.getString("codproc"));
				bpaConsolidado.setPrdIdade(PRD_IDADE);
				bpaConsolidado.setPrdQt(rs.getString("qtdproc"));
				listaDeBpaConsolidado.add(bpaConsolidado);
			}
		} catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				//comentado walter erro log ex.printStackTrace();
			}
		}
		return listaDeBpaConsolidado;
	}

}
