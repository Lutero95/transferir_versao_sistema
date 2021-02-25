package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.DataUtil;
import br.gov.al.maceio.sishosp.comum.util.TratamentoErrosUtil;
import br.gov.al.maceio.sishosp.hosp.control.LaudoController;
import br.gov.al.maceio.sishosp.hosp.model.BpaConsolidadoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProcedimentoBean;

public class BpaConsolidadoDAO {

	private static final String CODIGO_BPA_CONSOLIDADO = "01";
	
	/* ESTA CONSTANTE SERÁ SUBSTITUÍDA DEPOIS POR DADOS DO BANCO */
	private static final String PRD_IDADE = "000";

	public List<BpaConsolidadoBean> carregaDadosBpaConsolidado(Date dataInicio, Date dataFim, String competencia, String tipoGeracao, List<ProcedimentoBean> listaProcedimentosFiltro) throws ProjetoException {

    	List<BpaConsolidadoBean> listaDeBpaConsolidado = new ArrayList<BpaConsolidadoBean>();
        
    	String sql = "select qtdproc, codproc, cnes, competencia_atual, cbo, atendimento_realizado, presenca, id_situacao_atendimento, id_procedimento  from (\r\n" + 
    			"\r\n" + 
    			"	select count(*) qtdproc,   proc.codproc, coalesce(pa.cnes_producao, emp.cnes) cnes, pm.competencia_atual, cbo.codigo cbo \r\n" + 
    			"		,sa.atendimento_realizado, a.presenca, a1.id_situacao_atendimento, a1.codprocedimento as id_procedimento \r\n" + 
    			"		from hosp.atendimentos1 a1  join acl.funcionarios func on func.id_funcionario  = a1.codprofissionalatendimento \r\n" + 
    			"		left join hosp.cbo on cbo.id = a1.cbo  \r\n" + 
    			"		join hosp.atendimentos a on a.id_atendimento  = a1.id_atendimento  \r\n" + 
    			"		left join hosp.situacao_atendimento sa on sa.id = a1.id_situacao_atendimento  \r\n" + 
    			"		join hosp.proc on proc.id = a1.codprocedimento  \r\n" + 
    			"		join sigtap.procedimento_mensal pm on pm.id_procedimento  = a1.codprocedimento  \r\n" + 
    			"		join sigtap.historico_consumo_sigtap hc on hc.id = pm.id_historico_consumo_sigtap  \r\n" + 
    			"		join sigtap.instrumento_registro_procedimento_mensal irpm on irpm.id_procedimento_mensal  = pm.id  \r\n" + 
    			"		join sigtap.instrumento_registro ir on ir.id  = irpm.id_instrumento_registro \r\n" + 
    			"		cross join hosp.empresa emp  left join hosp.unidade u on emp.cod_empresa = u.cod_empresa \r\n" + 
    			"		left join hosp.parametro pa on u.id = pa.codunidade  where  hc.status='A' and coalesce(a.situacao, '')<> 'C'\r\n" + 
    			"		and coalesce(a1.excluido, 'N')= 'N' and \r\n" + 
    			"		a.dtaatende  between ? and ? \r\n" + 
    			"		and a.cod_unidade<>4  and ir.codigo = ?  and pm.competencia_atual = ?  \r\n" + 
    			"		and coalesce(proc.id_instrumento_registro_padrao, ir.id) = ir.id\r\n" + 
    			"		group by  proc.codproc, 3, pm.competencia_atual, cbo.codigo, sa.atendimento_realizado, a.presenca, a1.id_situacao_atendimento, a1.codprocedimento\r\n" + 
    			"union \r\n" + 
    			"		select count(*) qtdproc,   proc.codproc, coalesce(pa.cnes_producao, emp.cnes) cnes, pm.competencia_atual, cbo.codigo cbo\r\n" + 
    			"		,sa.atendimento_realizado, a.presenca, a1.id_situacao_atendimento, aps.id_procedimento \r\n" + 
    			"		from hosp.atendimentos1 a1  join acl.funcionarios func on func.id_funcionario  = a1.codprofissionalatendimento \r\n" + 
    			"		left join hosp.cbo on cbo.id = a1.cbo  \r\n" + 
    			"		join hosp.atendimentos a on a.id_atendimento  = a1.id_atendimento  \r\n" + 
    			"		join hosp.atendimentos1_procedimento_secundario aps on a1.id_atendimentos1 = aps.id_atendimentos1 \r\n" + 
    			"		left join hosp.situacao_atendimento sa on sa.id = a1.id_situacao_atendimento  \r\n" + 
    			"		join hosp.proc on proc.id = aps.id_procedimento  \r\n" + 
    			"		join sigtap.procedimento_mensal pm on pm.id_procedimento  = aps.id_procedimento  \r\n" + 
    			"		join sigtap.historico_consumo_sigtap hc on hc.id = pm.id_historico_consumo_sigtap  \r\n" + 
    			"		join sigtap.instrumento_registro_procedimento_mensal irpm on irpm.id_procedimento_mensal  = pm.id  \r\n" + 
    			"		join sigtap.instrumento_registro ir on ir.id  = irpm.id_instrumento_registro \r\n" + 
    			"		cross join hosp.empresa emp  left join hosp.unidade u on emp.cod_empresa = u.cod_empresa \r\n" + 
    			"		left join hosp.parametro pa on u.id = pa.codunidade  where  hc.status='A' and coalesce(a.situacao, '')<> 'C'\r\n" + 
    			"		and coalesce(a1.excluido, 'N')= 'N' and \r\n" + 
    			"		a.dtaatende  between ? and ? \r\n" + 
    			"		and a.cod_unidade<>4  and ir.codigo = ?  and pm.competencia_atual = ?  \r\n" + 
    			"		and coalesce(proc.id_instrumento_registro_padrao, ir.id) = ir.id  \r\n" + 
    			"		group by  proc.codproc, 3, pm.competencia_atual, cbo.codigo, sa.atendimento_realizado, a.presenca, a1.id_situacao_atendimento, aps.id_procedimento \r\n" + 
    			") a \r\n" + 
    			"where 1= 1 ";

		if (listaProcedimentosFiltro.size()>0)
			sql+=" and a.id_procedimento = any(?) ";

        if (tipoGeracao.equals("A")){
        	sql+=" and a.atendimento_realizado = true ";
		}
        else
        	sql+=" and a.presenca='S' and ((a.atendimento_realizado is true) or (a.id_situacao_atendimento is null)) ";

		sql+=" group by  a.qtdproc, a.codproc, 3, a.competencia_atual, a.cbo, a.atendimento_realizado, a.presenca, a.id_situacao_atendimento, a.id_procedimento order by a.cbo ";
        
        Connection con = ConnectionFactory.getConnection();
       
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(dataInicio.getTime()));
            ps.setDate(2, new java.sql.Date(dataFim.getTime()));
            ps.setString(3, CODIGO_BPA_CONSOLIDADO);
            ps.setString(4, competencia);
            ps.setDate(5, new java.sql.Date(dataInicio.getTime()));
            ps.setDate(6, new java.sql.Date(dataFim.getTime()));
            ps.setString(7, CODIGO_BPA_CONSOLIDADO);
            ps.setString(8, competencia);
			ArrayList<Integer> lista = new ArrayList<>();
			for (int i = 0; i < listaProcedimentosFiltro.size(); i++) {
				lista.add(listaProcedimentosFiltro.get(i).getIdProc());
			}

			if (listaProcedimentosFiltro.size()>0)
				ps.setObject(9, ps.getConnection().createArrayOf(  "INTEGER", lista.toArray()));

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
                ex.printStackTrace();
            }
        }
        return listaDeBpaConsolidado;
    }

}
