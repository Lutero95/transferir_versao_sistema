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
import br.gov.al.maceio.sishosp.hosp.model.BpaIndividualizadoBean;

public class BpaIndividualizadoDAO {

	private static final String CODIGO_BPA_INDIVIDUALIZADO =  "02";
	
	/* ESTAS CONSTANTES SERAM SUBSTITUÍDAS DEPOIS POR DADOS DO BANCO */
	private static final String PRD_CATEN = "01";
	private static final String PRD_NAUT = "             ";
	private static final String PRD_ETNIA = "    ";
	private static final String PRD_NAC = "010";
	private static final String PRD_EQUIPE_SEQ = "        ";
	private static final String PRD_EQUIPE_AREA = "    ";
	private static final String PRD_LOGRAD_PCNTE = "   ";
	private static final String PRD_DDTEL_PCNT = "           ";
	private static final String PRD_INE = "          ";
	
    public List<BpaIndividualizadoBean> carregaDadosBpaIndividualizado(Date dataInicio, Date dataFim, String competencia) throws ProjetoException {

    	List<BpaIndividualizadoBean> listaDeBpaIndividualizado = new ArrayList<BpaIndividualizadoBean>();
        String sql = "select count(*) qtdproc ,\n" +
				"  proc.codproc, emp.cnes,\n" +
				"  pm.competencia_atual, \n" +
				"  func.cns cnsprofissional\n" +
				"  , cbo.codigo cbo, proc.codproc, \n" +
				"  p.cns cnspaciente,\n" +
				" p.sexo, substring(to_char(m.codigo,'9999999')\t,1,7) codibgemun, cid.cid, extract(year from age(CURRENT_DATE, p.dtanascimento)) idade, \n" +
				" p.nome nomepaciente, p.dtanascimento , raca.codraca, p.cep, tl.codigo  codlogradouro, \n" +
				" p.logradouro enderecopaciente, p.complemento complendpaciente, p.numero numendpaciente,  emp.cnpj, \n" +
				" bairros.descbairro bairropaciente, p.email, dtaatende, p.dtanascimento,\n" +
				" sm.codigo codigo_servico, cm.codigo codigo_classificacao  \n" +
				" from hosp.atendimentos1 a1  \n" +
				"join hosp.atendimentos a on a.id_atendimento  = a1.id_atendimento  \n" +
				"  join acl.funcionarios func on func.id_funcionario  = a1.codprofissionalatendimento  \n" +
				" left join hosp.cbo on cbo.id = func.codcbo  \n" +
				"  join hosp.pacientes p on p.id_paciente  = a.codpaciente  \n" +
				" left join hosp.bairros on bairros.id_bairro = p.codbairro  \n" +
				" left join hosp.tipologradouro tl on tl.id = p.codtipologradouro  \n" +
				" left join hosp.raca on raca.id_raca = p.codraca  \n" +
				" left  join hosp.municipio m on m.id_municipio  = p.codmunicipio  \n" +
				" join hosp.proc on proc.id = a1.codprocedimento  \n" +
				" left join hosp.paciente_instituicao pi on pi.id  = a.id_paciente_instituicao  \n" +
				" left join hosp.programa  prog on prog.id_programa  = a.codprograma \n" +
				" left join hosp.laudo l on l.id_laudo  = pi.codlaudo  \n" +
				" left join hosp.cid on cid.cod = a1.id_cidprimario  \n" +
				" join sigtap.procedimento_mensal pm on pm.id_procedimento  = a1.codprocedimento  \n" +
				" join sigtap.historico_consumo_sigtap hc on hc.id = pm.id_historico_consumo_sigtap "+
				" join sigtap.instrumento_registro_procedimento_mensal irpm on irpm.id_procedimento_mensal  = pm.id  \n" +
				" join sigtap.instrumento_registro ir on ir.id  = irpm.id_instrumento_registro  \n" +
				" join hosp.situacao_atendimento sa on sa.id = a1.id_situacao_atendimento" +
				" cross join hosp.empresa emp\n" +
				"join sigtap.servico sm on sm.id = prog.id_servico \n" +
				"  join sigtap.classificacao cm on cm.id = prog.id_classificacao \n" +
				" where sa.atendimento_realizado is true and hc.status='A' \n" +
				" and a.dtaatende  between ?  and ? \n" +
				" and ir.codigo = ? \n" +
				" and pm.competencia_atual = ? and proc.ativo = 'S' \n" +
				" group by \n" +
				" proc.codproc, \n" +
				" emp.cnes, pm.competencia_atual, \n" +
				" func.cns ,\n" +
				" cbo.codigo, proc.codproc, \n" +
				" p.cns,\n" +
				" p.sexo, m.codigo , cid.cid, extract(year from age(CURRENT_DATE, p.dtanascimento)) , \n" +
				" p.nome , p.dtanascimento , raca.codraca, p.cep, tl.codigo  , \n" +
				" p.logradouro, p.complemento , p.numero, \n" +
				" bairros.descbairro , p.email, a.dtaatende, p.dtanascimento, \n" +
				" cm.codigo, sm.codigo, \n" +
				" emp.cnpj \n" +
				" order by func.cns, p.cns \t";
        
        Connection con = ConnectionFactory.getConnection();
       
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(dataInicio.getTime()));
            ps.setDate(2, new java.sql.Date(dataFim.getTime()));
            ps.setString(3, CODIGO_BPA_INDIVIDUALIZADO);
            ps.setString(4, competencia);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
            	BpaIndividualizadoBean bpaIndividualizado = new BpaIndividualizadoBean();
            	bpaIndividualizado.setPrdCnes(rs.getString("cnes"));
            	bpaIndividualizado.setPrdCmp(rs.getString("competencia_atual"));
            	bpaIndividualizado.setPrdCnsmed(rs.getString("cnsprofissional"));
            	bpaIndividualizado.setPrdCbo(rs.getString("cbo"));
            	bpaIndividualizado.setPrdDtaten(rs.getDate("dtaatende").toString().replaceAll("-", ""));
            	bpaIndividualizado.setPrdPa(rs.getString("codproc")); 
            	bpaIndividualizado.setPrdCnspac(rs.getString("cnspaciente")); 
            	bpaIndividualizado.setPrdSexo(rs.getString("sexo")); 
            	bpaIndividualizado.setPrdIbge(String.valueOf(rs.getInt("codibgemun")));
            	bpaIndividualizado.setPrdCid(rs.getString("cid"));
            	bpaIndividualizado.setPrdIdade(String.valueOf(rs.getInt("idade")));
            	bpaIndividualizado.setPrdQt(String.valueOf(rs.getInt("qtdproc")));
            	/* POSTERIORMENTE ALIMENTAR OS DADOS QUE RECEBEM CONSTRANTES COM DADOS DO BANCO */
            	bpaIndividualizado.setPrdCaten(PRD_CATEN);
            	bpaIndividualizado.setPrdNaut(PRD_NAUT);
            	
            	bpaIndividualizado.setPrdNmpac(rs.getString("nomepaciente"));
            	bpaIndividualizado.setPrdDtnasc(rs.getString("dtanascimento").toString().replaceAll("-", ""));
            	bpaIndividualizado.setPrdRaca(rs.getString("codraca"));
            	
            	bpaIndividualizado.setPrdEtnia(PRD_ETNIA);
            	bpaIndividualizado.setPrdNac(PRD_NAC);
            	bpaIndividualizado.setPrdSrv(rs.getString("codigo_servico"));
            	bpaIndividualizado.setPrdClf(rs.getString("codigo_classificacao"));
            	bpaIndividualizado.setPrdEquipeSeq(PRD_EQUIPE_SEQ);
            	bpaIndividualizado.setPrdEquipeArea(PRD_EQUIPE_AREA);
            	bpaIndividualizado.setPrdCnpj(rs.getString("cnpj"));
            	bpaIndividualizado.setPrdCepPcnte(rs.getString("cep"));
            	bpaIndividualizado.setPrdLogradPcnte(rs.getString("codlogradouro"));
            	bpaIndividualizado.setPrdEndPcnte(rs.getString("enderecopaciente"));
            	bpaIndividualizado.setPrdComplPcnte(rs.getString("complendpaciente"));
            	bpaIndividualizado.setPrdNumPcnte(rs.getString("numendpaciente"));
            	bpaIndividualizado.setPrdBairroPcnte(rs.getString("bairropaciente"));
            	bpaIndividualizado.setPrdDDtelPcnte(PRD_DDTEL_PCNT);
            	bpaIndividualizado.setPrdEmailPcnte(rs.getString("email"));
            	bpaIndividualizado.setPrdIne(PRD_INE);
            	listaDeBpaIndividualizado.add(bpaIndividualizado);
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
        return listaDeBpaIndividualizado;
    }
    
    public String buscaExtencaoArquivoPeloMesAtual() throws ProjetoException{
        String extensao = new String();
        String sql = " SELECT CASE " + 
        		" WHEN extract(month from current_date) = 1  then '.JAN' " + 
        		" WHEN  extract(month from current_date) = 2 THEN '.FEV' " + 
        		" WHEN  extract(month from current_date) = 3 THEN '.MAR' " + 
        		" WHEN  extract(month from current_date) = 4 THEN '.ABR' " + 
        		" WHEN  extract(month from current_date) = 5 THEN '.MAI' " + 
        		" WHEN  extract(month from current_date) = 6 THEN '.JUN' " + 
        		" WHEN  extract(month from current_date) = 7 THEN '.JUL' " + 
        		" WHEN  extract(month from current_date) = 8 THEN '.AGO' " + 
        		" WHEN  extract(month from current_date) = 9 THEN '.SET' " + 
        		" WHEN  extract(month from current_date) = 10 THEN '.OUT' " + 
        		" WHEN  extract(month from current_date) = 11 THEN '.NOV' " + 
        		" WHEN  extract(month from current_date) = 12 THEN '.DEZ' " + 
        		" END AS extensao";
        
        Connection con = ConnectionFactory.getConnection();
        try {
        	
        	PreparedStatement ps = con.prepareStatement(sql);          
            ResultSet rs = ps.executeQuery();
            if(rs.next())
            	extensao = rs.getString("extensao");
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
        return extensao;
    }

	public List<String> listarCompetencias() throws ProjetoException {
		String sql = "select distinct pm.competencia_atual from sigtap.procedimento_mensal pm ";
		List<String> listaCompetencias = new ArrayList<String>();
		Connection con = null;
        try {
        	con = ConnectionFactory.getConnection();	
        	PreparedStatement ps = con.prepareStatement(sql);          
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
            	String competencia = rs.getString("competencia_atual");
            	listaCompetencias.add(competencia);
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
        return listaCompetencias;
	}
}
