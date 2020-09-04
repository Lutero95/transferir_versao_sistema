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
import br.gov.al.maceio.sishosp.comum.util.TratamentoErrosUtil;
import br.gov.al.maceio.sishosp.hosp.model.BpaConsolidadoBean;

public class BpaConsolidadoDAO {

	private static final String CODIGO_BPA_CONSOLIDADO = "01";
	
	/* ESTA CONSTANTE SERÁ SUBSTITUÍDA DEPOIS POR DADOS DO BANCO */
	private static final String PRD_IDADE = "000";

	public List<BpaConsolidadoBean> carregaDadosBpaConsolidado(Date dataInicio, Date dataFim, String competencia) throws ProjetoException {

    	List<BpaConsolidadoBean> listaDeBpaConsolidado = new ArrayList<BpaConsolidadoBean>();
        String sql = "select count(*) qtdproc, " + 
        		"  proc.codproc, emp.cnes, pm.competencia_atual, cbo.codigo cbo " +
        		" from hosp.atendimentos1 a1 " + 
        		" join acl.funcionarios func on func.id_funcionario  = a1.codprofissionalatendimento " + 
        		" left join hosp.cbo on cbo.id = func.codcbo " + 
        		" join hosp.atendimentos a on a.id_atendimento  = a1.id_atendimento " + 
        		" join hosp.situacao_atendimento sa on sa.id = a1.id_situacao_atendimento "+
        		" join hosp.proc on proc.id = a1.codprocedimento " + 
        		" join sigtap.procedimento_mensal pm on pm.id_procedimento  = a1.codprocedimento " +
				" join sigtap.historico_consumo_sigtap hc on hc.id = pm.id_historico_consumo_sigtap " +
        		" join sigtap.instrumento_registro_procedimento_mensal irpm on irpm.id_procedimento_mensal  = pm.id " + 
        		" join sigtap.instrumento_registro ir on ir.id  = irpm.id_instrumento_registro " + 
        		" cross join hosp.empresa emp " + 
        		" where sa.atendimento_realizado = true and hc.status='A'" +
        		" and a.dtaatende  between ? and ? " +  
        		" and ir.codigo = ? " + 
        		" and pm.competencia_atual = ? " +
        		" group by " + 
        		"  proc.codproc, emp.cnes, pm.competencia_atual, cbo.codigo " +
        		"order by cbo.codigo ";
        
        Connection con = ConnectionFactory.getConnection();
       
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(dataInicio.getTime()));
            ps.setDate(2, new java.sql.Date(dataFim.getTime()));
            ps.setString(3, CODIGO_BPA_CONSOLIDADO);
            ps.setString(4, competencia);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
            	BpaConsolidadoBean bpaConsolidado = new BpaConsolidadoBean();
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
